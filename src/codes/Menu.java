package com.gin.admin.model;

import java.io.Serializable;

import com.gin.admin.model.base.Tree;

/**
 * 菜单
 *
 * @author gin 2019-10-31
 */
public class Menu extends Tree<Menu> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id; // ID
	private String path; // 菜单路径
	private String component; // 菜单组件
	private String name; // 菜单名称
	private String title; // 菜单标题
	private String icon; // 菜单图标
	private Boolean breadcrumb; // 是否显示面包屑
	
	/**
	 * 获取ID
	 *
	 * @return ID
	 */
	public Long getId(){
		return this.id;
	}
	
	/**
	 * 设置ID
	 *
	 * @param id ID
	 */
	public void setId(Long id){
		this.id = id;
	}
	/**
	 * 获取菜单路径
	 *
	 * @return 菜单路径
	 */
	public String getPath(){
		return this.path;
	}
	
	/**
	 * 设置菜单路径
	 *
	 * @param path 菜单路径
	 */
	public void setPath(String path){
		this.path = path;
	}
	/**
	 * 获取菜单组件
	 *
	 * @return 菜单组件
	 */
	public String getComponent(){
		return this.component;
	}
	
	/**
	 * 设置菜单组件
	 *
	 * @param component 菜单组件
	 */
	public void setComponent(String component){
		this.component = component;
	}
	/**
	 * 获取菜单名称
	 *
	 * @return 菜单名称
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * 设置菜单名称
	 *
	 * @param name 菜单名称
	 */
	public void setName(String name){
		this.name = name;
	}
	/**
	 * 获取菜单标题
	 *
	 * @return 菜单标题
	 */
	public String getTitle(){
		return this.title;
	}
	
	/**
	 * 设置菜单标题
	 *
	 * @param title 菜单标题
	 */
	public void setTitle(String title){
		this.title = title;
	}
	/**
	 * 获取菜单图标
	 *
	 * @return 菜单图标
	 */
	public String getIcon(){
		return this.icon;
	}
	
	/**
	 * 设置菜单图标
	 *
	 * @param icon 菜单图标
	 */
	public void setIcon(String icon){
		this.icon = icon;
	}
	/**
	 * 获取是否显示面包屑
	 *
	 * @return 是否显示面包屑
	 */
	public Boolean getBreadcrumb(){
		return this.breadcrumb;
	}
	
	/**
	 * 设置是否显示面包屑
	 *
	 * @param breadcrumb 是否显示面包屑
	 */
	public void setBreadcrumb(Boolean breadcrumb){
		this.breadcrumb = breadcrumb;
	}
}
