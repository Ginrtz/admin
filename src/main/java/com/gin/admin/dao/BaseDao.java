package com.gin.admin.dao;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gin.admin.util.ClassUtils;
import com.gin.admin.util.StringUtils;

@Repository
public class BaseDao {
	Logger logger = LoggerFactory.getLogger(BaseDao.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	@Value("${spring.datasource.db-type:mysql}")
	private String dbType;

	/**
	 * 数据库类型
	 */
	public static final String DATABSE_TYPE_MYSQL = "mysql";
	public static final String DATABSE_TYPE_ORACLE = "oracle";
	public static final String DATABSE_TYPE_SQLSERVER = "sqlserver";
	/**
	 * 分页SQL
	 */
	public static final String MYSQL_SQL = "select * from ({0}) sel_tab00 limit {1},{2}"; // mysql
	public static final String ORACLE_SQL = "select * from (select row_.*,rownum rownum_ from ({0}) row_ where rownum <= {1}) where rownum_>{2}"; // oracle
	public static final String SQLSERVER_SQL = "select * from ( select row_number() over(order by tempColumn) tempRowNumber, * from (select top {1} tempColumn = 0, {0}) t ) tt where tempRowNumber > {2}"; // sqlserver

	/**
	 * 根据sql语句，返回对象集合
	 *
	 * @param clazz      类型
	 * @param sql        语句
	 * @param parameters 参数集合
	 * @return bean对象集合
	 */
	public <T> List<T> findList(final Class<T> clazz, final String sql, Object... parameters) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("[BaseDao]\tsql：" + sql);
				logger.debug("[BaseDao]\tparameters：" + Arrays.asList(parameters));
			}
			if (parameters != null) {
				return jdbcTemplate.query(sql, parameters, new ResultRowMapper<>(clazz));
			} else {
				return jdbcTemplate.query(sql, new ResultRowMapper<>(clazz));
			}
		} catch (Exception e) {
			return null;
		}
	}

	protected class ResultRowMapper<T> implements RowMapper<T> {
		private Class<T> clazz;

		public ResultRowMapper(Class<T> clazz) {
			this.clazz = clazz;
		}

		@Override
		public T mapRow(ResultSet rs, int rowNum) throws SQLException {
			T obj = ClassUtils.newInstance(clazz);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnName(i);
				ClassUtils.setFieldValue(clazz, StringUtils.toJavaName(columnName), obj, rs.getObject(i));
			}
			return obj;
		}

	}

	/**
	 * 根据sql语句，返回对象
	 *
	 * @param sql        语句
	 * @param clazz      类型
	 * @param parameters 参数集合
	 * @return bean对象
	 */
	public <T> T find(Class<T> clazz, final String sql, Object... parameters) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("[BaseDao]\tsql：" + sql);
				logger.debug("[BaseDao]\tparameters：" + Arrays.asList(parameters));
			}
			if (parameters != null) {
				return jdbcTemplate.queryForObject(sql, parameters, resultBeanMapper(clazz));
			} else {
				return jdbcTemplate.queryForObject(sql, resultBeanMapper(clazz));
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 根据sql语句，返回Map对象
	 *
	 */
	public Map<String, Object> findForMap(final String sql, Object... parameters) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("[BaseDao]\tsql：" + sql);
				logger.debug("[BaseDao]\tparameters：" + Arrays.asList(parameters));
			}
			if (parameters != null) {
				return jdbcTemplate.queryForMap(sql, parameters);
			} else {
				return jdbcTemplate.queryForMap(sql);
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 根据sql语句，返回Map对象集合
	 */
	public List<Map<String, Object>> findForListMap(final String sql, Object... parameters) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("[BaseDao]\tsql：" + sql);
				logger.debug("[BaseDao]\tparameters：" + Arrays.asList(parameters));
			}
			if (parameters != null) {
				return jdbcTemplate.queryForList(sql, parameters);
			} else {
				return jdbcTemplate.queryForList(sql);
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 执行sql<br>
	 */
	public int execute(final String sql) {
		return executeWithBean(sql, null);
	}

	/**
	 * 执行sql
	 *
	 * @param sql  sql语句，占位符为冒号+参数名格式（update user set first_name = :firstName where id = 1）
	 * @param bean 参数对象,对象属性名要和sql占位符相同，如果参数为Map，Map的key要和sql占位符相同
	 */
	public int executeWithBean(final String sql, Object bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("[BaseDao]\tsql：" + sql);
			logger.debug("[BaseDao]\tparameters：" + bean);
		}
		if (bean != null) {
			return namedJdbcTemplate.update(sql, paramBeanMapper(bean));
		} else {
			return jdbcTemplate.update(sql);
		}
	}

	/**
	 * 执行sql
	 *
	 * @param sql        sql语句，占位符使用?（update user set first_name = ? where id = 1）
	 * @param parameters 可变参数列表，对应参数和sql中占位符位置对应
	 */
	public int executeWithParams(final String sql, Object... parameters) {
		if (logger.isDebugEnabled()) {
			logger.debug("[BaseDao]\tsql：" + sql);
			logger.debug("[BaseDao]\tparameters：" + Arrays.asList(parameters));
		}
		if (parameters != null) {
			return jdbcTemplate.update(sql, parameters);
		} else {
			return jdbcTemplate.update(sql);
		}
	}

	/**
	 * 批量执行sql
	 *
	 * @param sql        sql语句，sql中占位符使用?
	 * @param parameters 参数集合，集合中每个元素为参数列表，和sql中占位符位置对应
	 */
	public int[] batchUpdateWithParams(final String sql, List<Object[]> parameters) {
		int[] updateCounts = jdbcTemplate.batchUpdate(sql, parameters);
		return updateCounts;
	}

	/**
	 * 批量执行sql
	 *
	 * @param sql        sql中占位符使用冒号+参数名
	 * @param parameters 参数集合，集合中每个元素为参数对象,对象属性名和sql占位符相同，Map类参数的key与占位符相同
	 */
	public int[] batchUpdateWithBean(final String sql, List<? extends Object> parameters) {
		if (logger.isDebugEnabled()) {
			logger.debug("[BaseDao]\tsql：" + sql);
			logger.debug("[BaseDao]\tparameters：" + parameters);
		}
		DefaultPreparedStatementSetter pss = new DefaultPreparedStatementSetter(sql, parameters);
		String exesql = sql.replaceAll("\\:(\\w+)", "?");
		int[] updateCounts = jdbcTemplate.batchUpdate(exesql, pss);
		return updateCounts;
	}

	private class DefaultPreparedStatementSetter implements BatchPreparedStatementSetter {
		private List<?> objList;
		private List<String> placeHolderArray;

		public DefaultPreparedStatementSetter(String sql, List<?> objList) {
			this.objList = objList;
			placeHolderArray = new ArrayList<>();
			Pattern pattern = Pattern.compile("\\:(\\w+)");
			Matcher matcher = pattern.matcher(sql);
			while (matcher.find()) {
				placeHolderArray.add(matcher.group(0));
			}
		}

		@SuppressWarnings("rawtypes")
		@Override
		public void setValues(PreparedStatement ps, int i) throws SQLException {
			Object object = objList.get(i);
			if (object instanceof Map) {
				Set keySet = ((Map) object).keySet();
				for (Object key : keySet) {
					String placeHolder = ":" + key.toString();
					if (placeHolderArray.contains(placeHolder)) {
						Object value = ((Map) object).get(key);
						if (value != null) {
							ps.setObject(placeHolderArray.indexOf(placeHolder) + 1, value);
						} else {
							ps.setNull(placeHolderArray.indexOf(placeHolder) + 1, Types.VARCHAR);
						}
					}
				}
			} else {
				Field[] fields = ClassUtils.getAllFields(object.getClass());
				for (Field field : fields) {
					String placeHolder = ":" + field.getName();
					if (placeHolderArray.contains(placeHolder)) {
						Object value = ClassUtils.getValue(field.getName(), object);
						if (value != null) {
							ps.setObject(placeHolderArray.indexOf(placeHolder) + 1, value);
						} else {
							ps.setNull(placeHolderArray.indexOf(placeHolder) + 1, Types.VARCHAR);
						}
					}
				}
			}
		}

		@Override
		public int getBatchSize() {
			return objList == null ? 0 : objList.size();
		}
	}

	private <T> BeanPropertyRowMapper<T> resultBeanMapper(Class<T> clazz) {
		return BeanPropertyRowMapper.newInstance(clazz);
	}

	private BeanPropertySqlParameterSource paramBeanMapper(Object object) {
		return new BeanPropertySqlParameterSource(object);
	}

	/**
	 * 使用指定的检索标准检索数据并分页返回数据
	 */
	public List<Map<String, Object>> findForJdbc(String sql, int page, int rows) {
		// 封装分页SQL
		sql = createPageSql(sql, page, rows);
		if (logger.isDebugEnabled()) {
			logger.debug("[BaseDao]\tsql：" + sql);
		}
		return this.jdbcTemplate.queryForList(sql);
	}

	/**
	 * 使用指定的检索标准检索数据并分页返回数据
	 */
	public List<Map<String, Object>> findForListPage(String sql, int page, int rows, Object... parameters) {
		// 封装分页SQL
		sql = createPageSql(sql, page, rows);
		if (logger.isDebugEnabled()) {
			logger.debug("[BaseDao]\tsql：" + sql);
			logger.debug("[BaseDao]\tparameters：" + Arrays.asList(parameters));
		}
		return findForListMap(sql, parameters);
	}

	/**
	 * 使用指定的检索标准检索数据并返回全部数据
	 */
	public List<Map<String, Object>> findForList(String sql, Object... parameters) {
		if (logger.isDebugEnabled()) {
			logger.debug("[BaseDao]\tsql：" + sql);
			logger.debug("[BaseDao]\tparameters：" + Arrays.asList(parameters));
		}
		return this.jdbcTemplate.queryForList(sql, parameters);
	}

	/**
	 * 使用指定的检索标准检索数据并分页返回数据
	 *
	 */
	public <T> List<T> findObjForListPage(Class<T> clazz, String sql, int page, int rows) {
		List<T> rsList = new ArrayList<>();
		// 封装分页SQL
		sql = createPageSql(sql, page, rows);
		if (logger.isDebugEnabled()) {
			logger.debug("[BaseDao]\tsql：" + sql);
		}
		List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
		T po = null;
		for (Map<String, Object> m : mapList) {
			try {
				BeanUtils.populate(po, m);
				rsList.add(po);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rsList;
	}

	/**
	 * 使用指定的检索标准检索数据并分页返回数据
	 *
	 */
	public <T> List<T> findObjForListPage(Class<T> clazz, String sql, int page, int rows, Object... parameters) {
		// 封装分页SQL
		sql = createPageSql(sql, page, rows);
		if (logger.isDebugEnabled()) {
			logger.debug("[BaseDao]\tsql：" + sql);
			logger.debug("[BaseDao]\tparameters：" + Arrays.asList(parameters));
		}
		return jdbcTemplate.query(sql, new ResultRowMapper<>(clazz), parameters);
	}

	public Map<String, Object> findOneForJdbc(String sql, Object... parameters) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("[BaseDao]\tsql：" + sql);
				logger.debug("[BaseDao]\tparameters：" + Arrays.asList(parameters));
			}
			return jdbcTemplate.queryForMap(sql, parameters);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 使用指定的检索标准检索数据并分页返回数据For JDBC-采用预处理方式
	 *
	 */
	public Long countForJdbc(String sql, Object... parameters) {
		sql = createCountSql(sql);
		if (logger.isDebugEnabled()) {
			logger.debug("[BaseDao]\tsql：" + sql);
			logger.debug("[BaseDao]\tparameters：" + Arrays.asList(parameters));
		}
		if (parameters == null) {
			return jdbcTemplate.queryForObject(sql, Long.class);
		}
		return jdbcTemplate.queryForObject(sql, parameters, Long.class);
	}

	private String createCountSql(String sql) {
		int index = sql.toLowerCase().indexOf("from");
		return "select count(*) as ct " + sql.substring(index);
	}

	/**
	 * 按照数据库类型，封装SQL
	 */
	private String createPageSql(String sql, int page, int rows) {
		int beginNum = (page - 1) * rows;
		if (DATABSE_TYPE_MYSQL.equals(dbType)) {
			sql = MessageFormat.format(MYSQL_SQL, sql, beginNum, rows);
		} else {
			int beginIndex = (page - 1) * rows;
			int endIndex = beginIndex + rows;
			if (DATABSE_TYPE_ORACLE.equals(dbType)) {
				sql = MessageFormat.format(ORACLE_SQL, sql, endIndex, beginIndex);
			} else if (DATABSE_TYPE_SQLSERVER.equals(dbType)) {
				sql = MessageFormat.format(SQLSERVER_SQL, sql.substring(getAfterSelectInsertPoint(sql)), endIndex, beginIndex);
			}
		}
		return sql;
	}

	private int getAfterSelectInsertPoint(String sql) {
		int selectIndex = sql.toLowerCase().indexOf("select");
		int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");
		return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
	}

}
