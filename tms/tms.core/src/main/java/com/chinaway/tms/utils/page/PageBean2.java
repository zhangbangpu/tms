package com.chinaway.tms.utils.page;

import java.util.List;

/**
 * 分页类
 * 
 * @ClassName PageBean 
 * @author shuheng
 * @param <T> 泛型
 */
public class PageBean2<T> {
	private int total;
	private List<T> rows;
	private int pageSize=15;//每页显示多少行
	private int pageNo=1;//第几页
	
	public PageBean2() {
		super();
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageLast(){
		return pageSize;
	}
	public int getStart(){
		return (pageNo-1)*pageSize;
	}
}
