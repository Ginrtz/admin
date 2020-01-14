package com.gin.admin.model.base;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求响应结果
 *
 * @author o1760
 */
public class JsonResult {
	/** 操作成功 */
	public static final int CODE_SUCCESS = 0;
	/** 用户名或密码错误 */
	public static final int CODE_VERIFY_ERROR = 1;
	/** token无效 */
	public static final int CODE_TOKEN_INVALID = 2;

	private int code = CODE_SUCCESS;
	private String message = "操作成功";
	private Map<String, Object> data;
	public static JsonResult success = new JsonResult();

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void put(String key, Object value) {
		if (data == null) {
			data = new HashMap<>();
		}
		data.put(key, value);
	}

	public void remove(String key) {
		if (data == null) {
			return;
		}
		data.remove(key);
	}

	public Object get(String key) {
		if (data == null) {
			return null;
		}
		return data.get(key);
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public <T> void setEntity(T entity) {
		if (entity == null) {
			return;
		}
		Field[] fields = entity.getClass().getDeclaredFields();
		for (Field field : fields) {
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
				continue;
			}
			field.setAccessible(true);
			try {
				data.put(field.getName(), field.get(entity));
			} catch (Exception e) {
			}
			field.setAccessible(false);
		}
	}
}
