package com.chinaway.tms.admin.model;

import java.io.Serializable;

public class SysLog implements Serializable {
	
		private Integer id;//   主键	private String loginname;//   登录名	private String rolename;//   角色名	private String content;//   内容	private String clientip;//   客户端ip	private java.util.Date createtime;//   创建时间	public Integer getId() {	    return this.id;	}	public void setId(Integer id) {	    this.id=id;	}	public String getLoginname() {	    return this.loginname;	}	public void setLoginname(String loginname) {	    this.loginname=loginname;	}	public String getRolename() {	    return this.rolename;	}	public void setRolename(String rolename) {	    this.rolename=rolename;	}	public String getContent() {	    return this.content;	}	public void setContent(String content) {	    this.content=content;	}	public String getClientip() {	    return this.clientip;	}	public void setClientip(String clientip) {	    this.clientip=clientip;	}	public java.util.Date getCreatetime() {	    return this.createtime;	}	public void setCreatetime(java.util.Date createtime) {	    this.createtime=createtime;	}
}

