package com.gin.admin.dao.model;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gin.admin.util.ClassUtils;
import com.gin.admin.util.StringUtils;

/**
 * 自定义查询结果到实体类的映射
 */
public class ResultRowMapper<T> implements RowMapper<T> {
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
			Field field = ClassUtils.getField(clazz, StringUtils.toJavaName(columnName));
			if (field != null) {
				ClassUtils.setFieldValue(clazz, field, obj, rs.getObject(i));
			}
		}
		return obj;
	}

}
