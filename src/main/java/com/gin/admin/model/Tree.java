package com.gin.admin.model;

import java.util.List;

import com.gin.nicedao.annotation.Column;
import com.gin.nicedao.annotation.MapEntity;

public class Tree<T> {
	@Column
	private Integer parentId;
	@MapEntity(fkField = "parentId")
	protected T parent;
	@Column
	protected Integer treeSort;
	protected List<T> children;

	public T getParent() {
		return parent;
	}

	public void setParent(T parent) {
		this.parent = parent;
	}

	public List<T> getChildren() {
		return children;
	}

	public void setChildren(List<T> children) {
		this.children = children;
	}

	public Integer getTreeSort() {
		return treeSort;
	}

	public void setTreeSort(Integer treeSort) {
		this.treeSort = treeSort;
	}
}
