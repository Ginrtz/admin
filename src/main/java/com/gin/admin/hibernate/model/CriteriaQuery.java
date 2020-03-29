package com.gin.admin.hibernate.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gin.admin.hibernate.enums.Operator;
import com.gin.admin.hibernate.enums.SortDirection;

public class CriteriaQuery<T> {
	private Class<T> entityClass;
	private int curPage = 1;// 当前页
	private int pageSize = 10;// 默认一页条数
	private int total = 0;
	private String projection;
	private List<QueryParam> queryParams;
	private List<SubQuery> subQuerys;
	private Map<String, SortDirection> orderMap;// 排序字段
	private String field = "";// 查询需要显示的字段
	private String fuzzyFields = "";// 模糊查询的字段
	private List<T> results = new ArrayList<>();// 结果集

	public CriteriaQuery(Class<T> entityClass) {
		this.entityClass = entityClass;
		this.queryParams = new ArrayList<>();
		this.orderMap = new LinkedHashMap<>();
		this.subQuerys = new ArrayList<>();
	}

	public void eq(String fieldName, Object obj) {
		if (obj instanceof CriteriaQuery) {
			CriteriaQuery<?> query = (CriteriaQuery<?>) obj;
			SubQuery subQuery = new SubQuery(Operator.EQUAL, query);
			subQuery.setProperty(fieldName);
			subQuerys.add(subQuery);
		} else {
			queryParams.add(new QueryParam(fieldName, obj, Operator.EQUAL));
		}
	}

	public void ne(String fieldName, Object obj) {
		if (obj instanceof CriteriaQuery) {
			CriteriaQuery<?> query = (CriteriaQuery<?>) obj;
			SubQuery subQuery = new SubQuery(Operator.NOT_EQUAL, query);
			subQuery.setProperty(fieldName);
			subQuerys.add(subQuery);
		} else {
			queryParams.add(new QueryParam(fieldName, obj, Operator.NOT_EQUAL));
		}
	}

	public void gt(String fieldName, Object obj) {
		if (obj instanceof CriteriaQuery) {
			CriteriaQuery<?> query = (CriteriaQuery<?>) obj;
			SubQuery subQuery = new SubQuery(Operator.GREATER_THAN, query);
			subQuery.setProperty(fieldName);
			subQuerys.add(subQuery);
		} else {
			queryParams.add(new QueryParam(fieldName, obj, Operator.GREATER_THAN));
		}
	}

	public void ge(String fieldName, Object obj) {
		if (obj instanceof CriteriaQuery) {
			CriteriaQuery<?> query = (CriteriaQuery<?>) obj;
			SubQuery subQuery = new SubQuery(Operator.GREATER_THAN_OR_EQUAL, query);
			subQuery.setProperty(fieldName);
			subQuerys.add(subQuery);
		} else {
			queryParams.add(new QueryParam(fieldName, obj, Operator.GREATER_THAN_OR_EQUAL));
		}
	}

	public void lt(String fieldName, Object obj) {
		if (obj instanceof CriteriaQuery) {
			CriteriaQuery<?> query = (CriteriaQuery<?>) obj;
			SubQuery subQuery = new SubQuery(Operator.LESS_THAN, query);
			subQuery.setProperty(fieldName);
			subQuerys.add(subQuery);
		} else {
			queryParams.add(new QueryParam(fieldName, obj, Operator.LESS_THAN));
		}
	}

	public void le(String fieldName, Object obj) {
		if (obj instanceof CriteriaQuery) {
			CriteriaQuery<?> query = (CriteriaQuery<?>) obj;
			SubQuery subQuery = new SubQuery(Operator.LESS_THAN_OR_EQUAL, query);
			subQuery.setProperty(fieldName);
			subQuerys.add(subQuery);
		} else {
			queryParams.add(new QueryParam(fieldName, obj, Operator.LESS_THAN_OR_EQUAL));
		}
	}

	public void isNull(String fieldName) {
		queryParams.add(new QueryParam(fieldName, null, Operator.IS_NULL));
	}

	public void isNotNull(String fieldName) {
		queryParams.add(new QueryParam(fieldName, null, Operator.IS_NOT_NULL));
	}

	public void in(String fieldName, Object obj) {
		if (obj instanceof CriteriaQuery) {
			CriteriaQuery<?> query = (CriteriaQuery<?>) obj;
			SubQuery subQuery = new SubQuery(Operator.IN, query);
			subQuery.setProperty(fieldName);
			subQuerys.add(subQuery);
		} else {
			queryParams.add(new QueryParam(fieldName, obj, Operator.IN));
		}
	}

	public void notIn(String fieldName, Object obj) {
		if (obj instanceof CriteriaQuery) {
			CriteriaQuery<?> query = (CriteriaQuery<?>) obj;
			SubQuery subQuery = new SubQuery(Operator.NOT_IN, query);
			subQuery.setProperty(fieldName);
			subQuerys.add(subQuery);
		} else {
			queryParams.add(new QueryParam(fieldName, obj, Operator.NOT_IN));
		}
	}

	public void like(String fieldName, Object obj) {
		queryParams.add(new QueryParam(fieldName, obj, Operator.LIKE));
	}

	public void addOrder(String fieldName, SortDirection direction) {
		orderMap.put(fieldName, direction);
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public List<?> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getFuzzyFields() {
		return fuzzyFields;
	}

	public void setFuzzyFields(String fuzzyFields) {
		this.fuzzyFields = fuzzyFields;
	}

	public String getProjection() {
		return projection;
	}

	public void setProjection(String projection) {
		this.projection = projection;
	}

	public List<QueryParam> getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(List<QueryParam> queryParams) {
		this.queryParams = queryParams;
	}

	public List<SubQuery> getSubQuerys() {
		return subQuerys;
	}

	public void setSubQuerys(List<SubQuery> subQuerys) {
		this.subQuerys = subQuerys;
	}

	public Map<String, SortDirection> getOrderMap() {
		return orderMap;
	}

	public void setOrderMap(Map<String, SortDirection> orderMap) {
		this.orderMap = orderMap;
	}

	public void clear() {
		queryParams = null;
		entityClass = null;
		orderMap = null;
		field = null;
	}
}
