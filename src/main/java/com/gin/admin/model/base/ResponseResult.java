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
public class ResponseResult {
	/** 操作成功 */
	public static final int CODE_SUCCESS = 200;
	/** 用户名或密码错误 */
	public static final int CODE_VERIFY_ERROR = 401;
	/** token无效 */
	public static final int CODE_TOKEN_INVALID = 403;
	/** 用户在其他客户端登录 */
	public static final int CODE_TOKEN_KICKED_OUT = 408;
	private int code = CODE_SUCCESS;
	private String message = "操作成功";
	private Map<String, Object> data = new HashMap<>();
	public static ResponseResult success = new ResponseResult();

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
		data.put(key, value);
	}

	public void remove(String key) {
		data.remove(key);
	}

	public Object get(String key) {
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
		}
	}
}
