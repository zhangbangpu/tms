package com.chinaway.tms.admin.model;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class SysRole implements Serializable {
	
		private Integer id;//   主键	private String name;//   名称	private String description;//   描述	private String type;//   角色类型
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date createtime = new Date();//    创建时间
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date updatetime = new Date();//    更新时间
	private String deptid;//    部门id
		public Integer getId() {	    return this.id;	}	public void setId(Integer id) {	    this.id=id;	}	public String getName() {	    return this.name;	}	public void setName(String name) {	    this.name=name;	}	public String getDescription() {	    return this.description;	}	public void setDescription(String description) {	    this.description=description;	}	public String getType() {	    return this.type;	}	public void setType(String type) {	    this.type=type;	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public String getDeptid() {
		return deptid;
	}
	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	
}
