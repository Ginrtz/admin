package com.gin.admin.model;

import java.io.Serializable;

import com.gin.admin.dao.model.Tree;


/**
 * 菜单
 *
 * @author gin 2019-10-31
 */
public class Menu extends Tree<Menu> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id; // ID
	private String title; // 菜单标题
	private String url; // 菜单地址
	private String icon; // 菜单图标
	private Integer status;// 状态

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
