package com.chinaway.tms.basic.model;

import java.io.Serializable;

public class SysRoleMenu implements Serializable {

	private Integer roleid;// 角色id
	private Integer menuid;// 菜单id

	public Integer getroleid() {
		return this.roleid;
	}

	public void setroleid(Integer roleid) {
		this.roleid = roleid;
	}

	public Integer getMenuid() {
		return this.menuid;
	}

	public void setMenuid(Integer menuid) {
		this.menuid = menuid;
	}
}
