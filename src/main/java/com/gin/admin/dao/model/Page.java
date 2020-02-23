package com.gin.admin.dao.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页数据
 */
public class Page<T> {
	private int total;// 数据总量
	private int totalPage;// 总页数
	private int page;// 当前页数
	private int pageSize;// 页容量
	private List<T> data = new ArrayList<>();// 数据

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

}
