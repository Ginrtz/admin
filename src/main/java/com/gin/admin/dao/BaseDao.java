package com.gin.admin.jdbc.dao;

import java.sql.BatchUpdateException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.gin.admin.jdbc.dao.util.MapperUtils;
import com.gin.admin.model.base.PageInfo;
import com.gin.admin.util.ModelConvertUtils;
import com.gin.admin.util.StringUtils;

@Repository
public class BaseDao {
	private Log logger = LogFactory.getLog(BaseDao.class);
	private static final String MYSQL_PAGE_SQL = "select * from ({0}) _table0 limit {1},{2}"; // mysql
	private static final String ORACLE_PAGE_SQL = "select * from (select row_.*,rownum rownum_ from ({0}) row_ where rownum <= {1}) where rownum_ > {2}"; // oracle
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Value("${spring.datasource.dbtype}")
	private String dbType;

	/**
	 * 获取当前数据库类型，在application.yml中配置spring.datasource.dbtype
	 *
	 * @return
	 */
	public String getDbType() {
		return dbType;
	}

	public Number findCount(String sql, Object... parameters) {
		Map<String, Object> map = findFirst(sql, parameters);
		Number num = null;
		if (map != null) {
			for (String key : map.keySet()) {
				Object value = map.get(key);
				if (value != null) {
					if (value instanceof Number) {
						num = (Number) value;
					} else {
						String s = value.toString();
						if (s.matches("\\d+")) {
							Long v = Long.valueOf(s);
							if (v < Integer.MAX_VALUE) {
								num = v.intValue();
							} else {
								num = v;
							}
						}
					}
				}
				break;
			}
		}
		return num;
	}

	public Map<String, Object> findFirst(String sql, Object... parameters) {
		List<Map<String, Object>> list = findListPage(sql, 1, 1, parameters);
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

	public <T> T findFirst(Class<T> classOfItem, String sql, Object... parameters) {
		List<T> list = findListPage(classOfItem, sql, 1, 1, parameters);
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

	public List<Map<String, Object>> findListPage(String sql, int page, int pageSize, Object... parameters) {
		long begin = 0l;
		long end = 0l;
		if (logger.isDebugEnabled()) {
			begin = new Date().getTime();
		}
		String pageSql = getPageSql(sql, page, pageSize);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(pageSql, parameters);
		if (logger.isDebugEnabled()) {
			end = new Date().getTime();
			logger.info("[" + (end - begin) + "ms] \n" + sql + "\n");
		}
		return list;
	}

	public <T> List<T> findListPage(Class<T> classOfItem, String sql, int page, int pageSize, Object... parameters) {
		List<Map<String, Object>> list = findListPage(sql, page, pageSize, parameters);
		List<T> lst = new ArrayList<>();
		for (Map<String, Object> map : list) {
			T obj = null;
			try {
				obj = ModelConvertUtils.fromDB(map, classOfItem);
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}
			lst.add(obj);
		}
		return lst;
	}

	public List<Map<String, Object>> findList(String sql, Object... parameters) {
		return findListPage(sql, -1, -1, parameters);
	}

	public <T> List<T> findList(Class<T> classOfItem, String sql, Object... parameters) {
		return findListPage(classOfItem, sql, -1, -1, parameters);
	}

	public PageInfo<Map<String, Object>> findPageInfo(PageInfo<?> pi, String sql, Object... sqlParameters) {
		PageInfo<Map<String, Object>> pg = new PageInfo<>();
		if (pi != null) {
			pg.setPage(pi.getPage());
			pg.setPageSize(pi.getPageSize());
		}
		Integer count = findCount(getCountSql(sql), sqlParameters).intValue();
		pg.setTotalRecord(count);
		List<Map<String, Object>> data = findList(sql, pg.getPage(), pg.getPageSize(), sqlParameters);
		pg.setData(data);
		return pg;
	}

	public <T> PageInfo<T> findPageInfo(PageInfo<?> pi, Class<T> classOfItem, String sql, Object... sqlParameters) {
		PageInfo<Map<String, Object>> pg = findPageInfo(pi, sql, sqlParameters, getCountSql(sql), sqlParameters);
		PageInfo<T> pt = new PageInfo<>(pg.getPage(), pg.getPageSize(), pg.getTotalRecord(), new ArrayList<T>());
		for (Map<String, Object> map : pg.getData()) {
			T obj = null;
			try {
				obj = ModelConvertUtils.fromDB(map, classOfItem);
			} catch (Exception e) {
				if (logger.isDebugEnabled()) {
					logger.warn(e.getMessage(), e);
				}
			}
			pt.getData().add(obj);
		}
		return pt;
	}

	public int execute(String sql, Object... parameters) {
		long begin = 0l;
		long end = 0l;
		if (logger.isDebugEnabled()) {
			begin = new Date().getTime();
		}
		int num;
		try {
			num = jdbcTemplate.update(sql, parameters);
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		if (logger.isDebugEnabled()) {
			end = new Date().getTime();
			logger.info("[" + (end - begin) + "ms] \n" + sql + "\n");
		}
		return num;
	}

	public int[] batchExecute(String[] sqls) throws BatchUpdateException {
		if (sqls.length == 0) {
			return new int[0];
		}
		long begin = 0l;
		long end = 0l;
		if (logger.isDebugEnabled()) {
			begin = new Date().getTime();
		}
		int[] num;
		try {
			num = jdbcTemplate.batchUpdate(sqls);
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		if (logger.isDebugEnabled()) {
			end = new Date().getTime();
			StringBuilder tmp = new StringBuilder();
			for (int i = 0; i < sqls.length; i++) {
				String sql = sqls[i];
				tmp.append("batch - " + i + " : \n");
				tmp.append(sql + "\n");
			}
			logger.info("[" + (end - begin) + "ms] \n" + tmp);
		}
		return num;
	}

	private int[] batchLocal(String sql, List<Object[]> parameters) throws BatchUpdateException {
		if (parameters.size() == 0) {
			return new int[0];
		}
		int[] rowsAffected;
		long begin = 0l;
		long end = 0l;
		if (logger.isDebugEnabled()) {
			begin = new Date().getTime();
		}
		try {
			rowsAffected = jdbcTemplate.batchUpdate(sql, parameters);
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		if (logger.isDebugEnabled()) {
			end = new Date().getTime();
			logger.info("[" + (end - begin) + "ms] [BatchUpdate: " + parameters.size() + "] \n" + sql + "\n");
		}
		return rowsAffected;
	}

	public <T> T find(Class<T> classOfItem, Object id) {
		String sql = MapperUtils.getFindSql(classOfItem);
		return findFirst(classOfItem, sql, id);
	}

	public void save(Object o) {
		save(o, false);
	}

	public void save(Object o, boolean assignedId) {
		List<List<Object>> parameterList = new ArrayList<>(1);
		List<Object> datas = new ArrayList<>();
		datas.add(o);
		String sql = MapperUtils.getInsertSql(o.getClass(), datas, parameterList, assignedId);
		Object[] parameters = parameterList.get(0).toArray();
		execute(sql, parameters);
	}

	public void batchSave(List<?> datas) throws BatchUpdateException {
		batchSave(datas, false);
	}

	public void batchSave(List<?> datas, boolean assignedId) throws BatchUpdateException {
		if (datas != null && datas.size() > 0) {
			List<List<Object>> parameterList = new ArrayList<>();
			String sql = MapperUtils.getInsertSql(datas.get(0).getClass(), datas, parameterList, assignedId);
			List<Object[]> parameters = new ArrayList<>(parameterList.size());
			for (List<Object> param : parameterList) {
				parameters.add(param.toArray());
			}
			batchLocal(sql, parameters);
		}
	}

	public int update(Object o) {
		return update(o, false);
	}

	public int update(Object o, boolean ignoreNulls) {
		List<Object> parameterList = new ArrayList<>();
		String sql = MapperUtils.getUpdateSql(o.getClass(), o, parameterList, ignoreNulls);
		Object[] parameters = parameterList.toArray();
		int i = execute(sql, parameters);
		return i;
	}

	public <T> int delete(T o) {
		return delete(o.getClass(), o);
	}

	private int delete(Class<?> classOfTable, Object o) {
		List<List<Object>> parameterList = new ArrayList<>(1);
		List<Object> datas = new ArrayList<>();
		datas.add(o);
		String sql = MapperUtils.getDeleteSql(classOfTable, datas, parameterList);
		Object[] parameters = parameterList.get(0).toArray();
		int i = execute(sql, parameters);
		return i;
	}

	public void batchDelete(List<?> datas) throws BatchUpdateException {
		if (datas != null && datas.size() > 0) {
			List<List<Object>> parameterList = new ArrayList<>();
			String sql = MapperUtils.getDeleteSql(datas.get(0).getClass(), datas, parameterList);
			List<Object[]> parameters = new ArrayList<>(parameterList.size());
			for (List<Object> param : parameterList) {
				parameters.add(param.toArray());
			}
			batchLocal(sql, parameters);
		}
	}

	public SqlRowSet findRowSet(String sql, int currentPage, int pageSize, Object... parameters) {
		long begin = 0l;
		long end = 0l;
		if (logger.isDebugEnabled()) {
			begin = new Date().getTime();
		}
		String pageSql = getPageSql(sql, currentPage, pageSize);
		SqlRowSet rowSet;
		try {
			rowSet = jdbcTemplate.queryForRowSet(pageSql, parameters);
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		if (logger.isDebugEnabled()) {
			end = new Date().getTime();
			logger.info("[" + (end - begin) + "ms] \n" + sql + "\n");
		}
		return rowSet;
	}

	/**
	 * 获取统计sql
	 *
	 * @param sql 原始sql
	 * @return
	 */
	private static String getCountSql(String sql) {
		sql = StringUtils.trim(sql, ";");
		sql = sql.replaceAll("(?i)order by [\\s|\\S]+$", "");// 去除order by
		sql = "select count(*) as ct_ from (" + sql + ") count_";
		return sql;
	}

	/**
	 * 获取分页sql语句（默认mysql格式）
	 *
	 * @param sql
	 * @param currentPage 页码
	 * @param pageSize    页容量
	 * @return
	 */
	private String getPageSql(String sql, int currentPage, int pageSize) {
		if (currentPage < 0 || pageSize < 0) {
			return sql;
		}
		if ("oracle".equalsIgnoreCase(dbType)) {
			return MessageFormat.format(ORACLE_PAGE_SQL, sql, currentPage * pageSize, (currentPage - 1) * pageSize);
		} else {
			return MessageFormat.format(MYSQL_PAGE_SQL, sql, (currentPage - 1) * pageSize, pageSize);
		}
	}
}
