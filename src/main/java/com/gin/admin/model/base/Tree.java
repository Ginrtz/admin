package com.gin.admin.model.base;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 树形结构
 *
 * @param <T>
 */
public abstract class Tree<T> {
	protected T parent;
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

	public void setChildren(T[] children) {
		if (children != null) {
			this.children = Arrays.asList(children);
		}
	}

	/**
	 * 列出所有子元素
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> listAllChildren() {
		List<T> list = new LinkedList<>();
		list.add((T) this);
		if (this.children != null) {
			for (T sub : this.children) {
				list.addAll(((Tree<T>) sub).listAllChildren());
			}
		}
		return list;
	}
}
