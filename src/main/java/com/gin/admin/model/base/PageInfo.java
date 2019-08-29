package com.gin.admin.model.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页对象
 *
 * @param <T>
 */
public class PageInfo<T> implements Serializable {

	private static final long serialVersionUID = -2539381776966116877L;
	public static final Integer PAGE_SIZE = 10;

	/**
	 * 当前页数
	 */
	private Integer page;

	/**
	 * 每页的显示记录数
	 */
	private Integer pageSize;

	/**
	 * 总记录数
	 */
	private Integer totalRecord;

	/**
	 * 总页数
	 */
	private Integer totalPage;

	/**
	 * 排序列
	 */
	private String orderProperty;

	/**
	 * 排序顺序
	 */
	private String orderDirection;

	/**
	 * 当前页中存放的数据
	 */
	private List<T> data;

	public PageInfo() {
		this(1, 0, 0, null);
	}

	public PageInfo(Integer page, Integer pageSize) {
		this(page, pageSize, 0, null);
	}

	public PageInfo(Integer page, Integer pageSize, Integer totalRecord, List<T> data) {
		setPage(page);
		setPageSize(pageSize);
		setTotalRecord(totalRecord);
		setData(data);
		getTotalPage();
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		if (page == null || page <= 0) {
			page = 1;
		}
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize == null || pageSize <= 0) {
			pageSize = PAGE_SIZE;
		}
		this.pageSize = pageSize;
		getTotalPage();
	}

	public Integer getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(Integer totalRecord) {
		if (totalRecord == null || totalRecord <= 0) {
			totalRecord = 0;
		}
		this.totalRecord = totalRecord;
		getTotalPage();
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		if (data == null) {
			data = new ArrayList<T>();
		}
		this.data = data;
	}

	public Integer getTotalPage() {
		if (totalRecord != null && pageSize != null) {
			totalPage = totalRecord / pageSize;
			if (totalRecord % pageSize != 0) {
				totalPage++;
			}
		}
		return totalPage;
	}

	public String getOrderProperty() {
		return orderProperty;
	}

	public void setOrderProperty(String orderProperty) {
		this.orderProperty = orderProperty;
	}

	public String getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}
}
