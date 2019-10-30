package com.gin.admin.util;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.util.List;

public class SqlUtils {

	public static String createSelectSql(Object bean, List<Object> parameters) {
		parameters.clear();
		Class<?> clazz = bean.getClass();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from ");
		sb.append(StringUtils.toSqlName(clazz.getSimpleName()));
		sb.append(" where 1=1");
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (!field.isAnnotationPresent(Transient.class) && !"serialVersionUID".equals(field.getName())) {
				String col_name = StringUtils.toSqlName(field.getName());
				Object value = ClassUtils.getValue(field.getName(), bean);
				if (null != value) {
					sb.append(" and ");
					sb.append(col_name);
					sb.append(" = ?");
					parameters.add(value);
				}
			}
		}
		return sb.toString();
	}
}
