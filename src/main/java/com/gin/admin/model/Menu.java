package com.gin.admin.model;

import java.io.Serializable;

import com.gin.admin.model.base.Tree;

public class Menu extends Tree<Menu> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String path;
	private String component;
	private String name;
	private String title;
	private String icon;
	private Integer breadcrumb;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getBreadcrumb() {
		return breadcrumb;
	}

	public void setBreadcrumb(Integer breadcrumb) {
		this.breadcrumb = breadcrumb;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
