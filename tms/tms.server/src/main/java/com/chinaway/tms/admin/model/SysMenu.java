package com.chinaway.tms.admin.model;

import java.io.Serializable;

public class SysMenu implements Serializable {
	
		private Integer id;  //   主键	private String name;  //   菜单名称	private Integer pid;  //   父id	private String levels;  //   层级	private java.util.Date createtime;  //   创建时间	private String requesturl;  //  请求路径	private String menutype;  //  菜单类型	private Integer sotid;  //  排序号
	private String clazz;  //  样式
	private String title;  //  标题
	private String img;  //  图片
		public Integer getId() {	    return this.id;	}	public void setId(Integer id) {	    this.id=id;	}	public String getName() {	    return this.name;	}	public void setName(String name) {	    this.name=name;	}	public Integer getPid() {	    return this.pid;	}	public void setPid(Integer pid) {	    this.pid=pid;	}	public String getLevels() {	    return this.levels;	}	public void setLevels(String levels) {	    this.levels=levels;	}	public java.util.Date getCreatetime() {	    return this.createtime;	}	public void setCreatetime(java.util.Date createtime) {	    this.createtime=createtime;	}	public String getRequesturl() {	    return this.requesturl;	}	public void setRequesturl(String requesturl) {	    this.requesturl=requesturl;	}	public String getMenutype() {	    return this.menutype;	}	public void setMenutype(String menutype) {	    this.menutype=menutype;	}	public Integer getSotid() {	    return this.sotid;	}	public void setSotid(Integer sotid) {	    this.sotid=sotid;	}
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
}