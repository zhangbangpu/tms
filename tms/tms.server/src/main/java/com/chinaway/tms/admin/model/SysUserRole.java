package com.chinaway.tms.admin.model;

import java.io.Serializable;

/**
 * 用户角色bean
 * @author zhang
 *
 */
public class SysUserRole implements Serializable {
		private static final long serialVersionUID = 1579467190398275039L;
	
	private Integer userid;  //   用户id	private Integer roleid;  //   角色id
		public Integer getUserid() {	    return this.userid;	}	public void setUserid(Integer userid) {	    this.userid=userid;	}	public Integer getRoleid() {	    return this.roleid;	}	public void setRoleid(Integer roleid) {	    this.roleid=roleid;	}
}

