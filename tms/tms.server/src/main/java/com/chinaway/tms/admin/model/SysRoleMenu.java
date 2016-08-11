package com.chinaway.tms.admin.model;

import java.io.Serializable;

public class SysRoleMenu implements Serializable {

	private static final long serialVersionUID = -7004789906044546976L;
	
	private Integer roleid;// 角色id
	private Integer menuid;// 菜单id
	
	public Integer getRoleid() {
		return roleid;
	}
	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}
	public Integer getMenuid() {
		return menuid;
	}
	public void setMenuid(Integer menuid) {
		this.menuid = menuid;
	}

}
