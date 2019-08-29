package com.gin.admin.jdbc.dao.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.annotation.Transient;

import com.gin.admin.jdbc.dao.exception.DaoException;
import com.gin.admin.util.StringUtils;

public class MapperUtils {
	protected static Log logger = LogFactory.getLog(MapperUtils.class);
	private static Map<Class<?>, String> inserts = new ConcurrentHashMap<>();
	private static Map<Class<?>, String> finds = new ConcurrentHashMap<>();
	private static Map<Class<?>, String> deletes = new ConcurrentHashMap<>();
	private static Map<Class<?>, String> updates = new ConcurrentHashMap<>();

	/**
	 * 获取insert语句
	 *
	 * @param classOfEntity
	 * @param objs          待保存对象
	 * @param parameterList 空参数列表
	 * @param assignedId    手动设置id
	 * @return
	 */
	public static String getInsertSql(Class<?> classOfEntity, List<?> objs, List<List<Object>> parameterList,
			boolean assignedId) throws DaoException {
		parameterList.clear();
		String sql = inserts.get(classOfEntity);
		Field[] fields = classOfEntity.getDeclaredFields();
		if (sql == null) {
			synchronized (inserts) {
				sql = inserts.get(classOfEntity);
				if (sql == null) {
					StringBuilder sb = new StringBuilder();
					StringBuilder names = new StringBuilder();
					StringBuilder values = new StringBuilder();
					sb.append("insert into ");
					sb.append(StringUtils.toSqlName(classOfEntity.getSimpleName()));
					sb.append("(");
					for (Field field : fields) {
						if (field.isAnnotationPresent(Transient.class)) {
							continue;
						}
						if (names.length() > 0) {
							names.append(",");
							values.append(",");
						}
						names.append(StringUtils.toSqlName(field.getName()));
						values.append("?");
					}
					sb.append(names);
					sb.append(") values (");
					sb.append(values);
					sb.append(")");
					sql = sb.toString();
					inserts.put(classOfEntity, sql);
				}
			}
		}
		for (Object obj : objs) {
			List<Object> parameters = new ArrayList<>();
			for (Field field : fields) {
				if (field.isAnnotationPresent(Transient.class)) {
					continue;
				}
				Object value = null;
				if ("id".equalsIgnoreCase(field.getName())) {
					if (assignedId) {
						continue;
					}
					value = UUID.randomUUID().toString().replace("-", "");
				} else {
					field.setAccessible(true);
					try {
						value = field.get(obj);
					} catch (IllegalArgumentException e) {
						throw new DaoException("获取" + classOfEntity.getName() + "的" + field.getName() + "的值失败：参数异常", e);
					} catch (IllegalAccessException e) {
						throw new DaoException("获取" + classOfEntity.getName() + "的" + field.getName() + "的值失败：拒绝访问", e);
					}
				}
				parameters.add(value);
			}
			parameterList.add(parameters);
		}
		return sql;
	}

	/**
	 * 获取根据主键的查询语句
	 *
	 * @param classOfEntity
	 * @return
	 */
	public static String getFindSql(Class<?> classOfEntity) throws DaoException {
		String sql = finds.get(classOfEntity);
		if (sql == null) {
			synchronized (finds) {
				sql = finds.get(classOfEntity);
				if (sql == null) {
					StringBuilder sb = new StringBuilder();
					sb.append("select * from ");
					sb.append(StringUtils.toSqlName(classOfEntity.getSimpleName()));
					sb.append(" where id = ?");
					sql = sb.toString();
					finds.put(classOfEntity, sql);
				}
			}
		}
		return sql;
	}

	/**
	 * 获取查询主键的sql语句
	 *
	 * @param classOfEntity
	 * @return
	 */
	public static String getFindIdSql(Class<?> classOfEntity) {
		String sql = "select id from " + StringUtils.toSqlName(classOfEntity.getSimpleName());
		return sql;
	}

	/**
	 * 获取清空表的sql语句
	 *
	 * @param classOfEntity
	 * @return
	 */
	public static String getClearSql(Class<?> classOfEntity) {
		String sql = "delete from " + StringUtils.toSqlName(classOfEntity.getSimpleName());
		return sql;
	}

	/**
	 * 获取根据主键的删除语句
	 *
	 * @param classOfEntity
	 * @param objs
	 * @param parameterList
	 * @return
	 */
	public static String getDeleteSql(Class<?> classOfEntity, List<?> objs, List<List<Object>> parameterList)
			throws DaoException {
		Field idField = null;
		try {
			idField = classOfEntity.getDeclaredField("id");
			idField.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new DaoException(classOfEntity.getName() + "不包含id字段或无法访问！");
		}
		String sql = deletes.get(classOfEntity);
		if (sql == null) {
			synchronized (deletes) {
				sql = deletes.get(classOfEntity);
				if (sql == null) {
					StringBuilder sb = new StringBuilder();
					sb.append("delete from ");
					sb.append(StringUtils.toSqlName(classOfEntity.getSimpleName()));
					sb.append(" where id = ?");
					sql = sb.toString();
					deletes.put(classOfEntity, sql);
				}
			}
		}
		if (parameterList != null) {
			parameterList.clear();
			for (Object obj : objs) {
				List<Object> idList = new ArrayList<>();
				try {
					Object value = idField.get(obj);
					idList.add(value);
				} catch (IllegalArgumentException e) {
					throw new DaoException("获取" + classOfEntity.getName() + "的id的值失败：参数异常", e);
				} catch (IllegalAccessException e) {
					throw new DaoException("获取" + classOfEntity.getName() + "的id的值失败：拒绝访问", e);
				}
				parameterList.add(idList);
			}
		}
		return sql;
	}

	/**
	 * 获取update语句
	 *
	 * @param classOfEntity
	 * @param objs
	 * @param parameterList
	 * @param ignoreNulls
	 * @return
	 */
	public static String getUpdateSql(Class<?> classOfEntity, Object obj, List<Object> parameterList,
			boolean ignoreNulls) throws DaoException {
		Field idField = null;
		try {
			idField = classOfEntity.getDeclaredField("id");
			idField.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new DaoException(classOfEntity.getName() + "不包含id字段或无法访问！", e);
		}
		parameterList.clear();
		String sql;
		if (ignoreNulls) {
			sql = localGetUpdateSql(classOfEntity, obj, parameterList, ignoreNulls);
		} else {
			sql = updates.get(classOfEntity);
			if (sql == null) {
				synchronized (updates) {
					sql = updates.get(classOfEntity);
					if (sql == null) {
						sql = localGetUpdateSql(classOfEntity, obj, parameterList, ignoreNulls);
						updates.put(classOfEntity, sql);
					}
				}
			}
			if (parameterList.size() == 0) {
				Field[] fields = classOfEntity.getDeclaredFields();
				for (Field field : fields) {
					if (field.isAnnotationPresent(Transient.class)) {
						continue;
					}
					if ("id".equalsIgnoreCase(field.getName())) {
						continue;
					}
					field.setAccessible(true);
					try {
						Object value = field.get(obj);
						parameterList.add(value);
					} catch (IllegalArgumentException e) {
						throw new DaoException("获取" + classOfEntity.getName() + "的" + field.getName() + "的值失败：参数异常", e);
					} catch (IllegalAccessException e) {
						throw new DaoException("获取" + classOfEntity.getName() + "的" + field.getName() + "的值失败：拒绝访问", e);
					}
				}
				try {
					Object id = idField.get(obj);
					parameterList.add(id);
				} catch (IllegalArgumentException e) {
					throw new DaoException("获取" + classOfEntity.getName() + "的id的值失败：参数异常", e);
				} catch (IllegalAccessException e) {
					throw new DaoException("获取" + classOfEntity.getName() + "的id的值失败：拒绝访问", e);
				}
			}
		}
		return sql;
	}

	private static String localGetUpdateSql(Class<?> classOfEntity, Object obj, List<Object> parameterList,
			boolean ignoreNulls) {
		StringBuilder sql = new StringBuilder();
		StringBuilder set = new StringBuilder();
		sql.append("update ");
		sql.append(StringUtils.toSqlName(classOfEntity.getSimpleName()));
		sql.append(" set ");
		Field[] fields = classOfEntity.getDeclaredFields();
		Field idField = null;
		for (Field field : fields) {
			if (field.isAnnotationPresent(Transient.class)) {
				continue;
			}
			field.setAccessible(true);
			if ("id".equalsIgnoreCase(field.getName())) {
				idField = field;
				continue;
			}
			try {
				Object value = field.get(obj);
				if (!ignoreNulls || value != null) {
					if (set.length() > 0) {
						set.append(",");
					}
					parameterList.add(value);
					set.append(StringUtils.toSqlName(field.getName()));
					set.append("=? ");
				}
			} catch (IllegalArgumentException e) {
				throw new DaoException("获取" + classOfEntity.getName() + "的" + field.getName() + "的值失败：参数异常", e);
			} catch (IllegalAccessException e) {
				throw new DaoException("获取" + classOfEntity.getName() + "的" + field.getName() + "的值失败：拒绝访问", e);
			}
		}
		sql.append(set);
		sql.append(" where id = ?");
		try {
			Object id = idField.get(obj);
			parameterList.add(id);
		} catch (IllegalArgumentException e) {
			throw new DaoException("获取" + classOfEntity.getName() + "的id的值失败：参数异常", e);
		} catch (IllegalAccessException e) {
			throw new DaoException("获取" + classOfEntity.getName() + "的id的值失败：拒绝访问", e);
		}
		return sql.toString();
	}
}
