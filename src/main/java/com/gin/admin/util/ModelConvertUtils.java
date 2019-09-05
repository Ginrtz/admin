package com.gin.admin.util;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

public class ModelConvertUtils {
	public static <T> T mapToBean(Map<String, ? extends Object> map, Class<T> classOfItem)
			throws IllegalArgumentException, IntrospectionException {
		if (Character.class.isAssignableFrom(classOfItem) || String.class.isAssignableFrom(classOfItem)
				|| Number.class.isAssignableFrom(classOfItem) || Boolean.class.isAssignableFrom(classOfItem)
				|| Date.class.isAssignableFrom(classOfItem)) {
			return ConvertUtils.fromOnlyOneField(map, classOfItem);
		}
		T obj = ConvertUtils.fromDB(map, classOfItem);
		if (obj == null) {
			obj = ClassUtils.newInstance(classOfItem);
		}
		Field[] fields = classOfItem.getFields();
		for (Field field : fields) {
			String fieldName = StringUtils.toSqlName(field.getName());
			Object value = map.get(fieldName);
			ClassUtils.invokeWriteMethod(classOfItem, field.getName(), obj, value);
		}
		return obj;
	}
}
