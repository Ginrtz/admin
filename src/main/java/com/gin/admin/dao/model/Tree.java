package com.gin.admin.dao.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Tree<T> {
	protected Long parentId;
	@JsonIgnore
	protected T parent;
	protected Integer treeSort;
	protected List<T> children;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public T getParent() {
		return parent;
	}

	public void setParent(T parent) {
		this.parent = parent;
	}

	public Integer getTreeSort() {
		return treeSort;
	}

	public void setTreeSort(Integer treeSort) {
		this.treeSort = treeSort;
	}

	public List<T> getChildren() {
		return children;
	}

	public void setChildren(List<T> children) {
		this.children = children;
	}

}
