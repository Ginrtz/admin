package com.gin.admin.hibernate.model;

import com.gin.admin.hibernate.enums.Operator;

public class QueryParam {
	private Class<?> fieldClass;
	private String field;
	private Object obj;
	private Operator operator;

	public QueryParam(String field, Object obj, Operator operator) {
		if (obj != null) {
			this.fieldClass = obj.getClass();
		}
		this.field = field;
		this.obj = obj;
		this.operator = operator;
	}

	public Class<?> getFieldClass() {
		return fieldClass;
	}

	public void setFieldClass(Class<?> fieldClass) {
		this.fieldClass = fieldClass;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}
}
