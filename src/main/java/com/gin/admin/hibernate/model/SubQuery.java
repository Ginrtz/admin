package com.gin.admin.hibernate.model;

import com.gin.admin.hibernate.enums.Operator;

public class SubQuery {
	private String property;
	private Operator opt;
	private CriteriaQuery<?> query;

	public SubQuery(Operator opt, CriteriaQuery<?> query) {
		this.opt = opt;
		this.query = query;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Operator getOpt() {
		return opt;
	}

	public void setOpt(Operator opt) {
		this.opt = opt;
	}

	public CriteriaQuery<?> getQuery() {
		return query;
	}

	public void setQuery(CriteriaQuery<?> query) {
		this.query = query;
	}

}
