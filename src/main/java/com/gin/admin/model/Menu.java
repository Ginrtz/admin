package com.gin.admin.model;

import java.io.Serializable;

import com.gin.nicedao.annotation.Column;
import com.gin.nicedao.annotation.Id;
import com.gin.nicedao.annotation.Table;

@Table
public class Menu extends Tree<Menu> implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	private Integer id;
	@Column
	private String path;
	@Column
	private String component;
	@Column
	private String name;
	@Column
	private String title;
	@Column
	private String icon;
	@Column
	private Integer breadcrumb;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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
