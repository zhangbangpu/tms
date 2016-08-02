package com.chinaway.tms.utils.page;

import java.util.List;

/**
 * 分页类
 * 
 * @ClassName PageBean
 * @author shuheng
 * @param <T>
 *            泛型
 */
public class PageBean<T> {
	private int totalCount;
	private List<T> result;
	// private int total;
	// private List<T> rows;
	private int pageSize = 15;// 每页显示多少行
	private int pageNo = 1;// 第几页

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageLast() {
		return pageSize;
	}

	public int getStart() {
		return (pageNo - 1) * pageSize;
	}
}
