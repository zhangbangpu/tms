package com.chinaway.tms.basic.model;

import java.io.Serializable;
import java.util.Date;

public class Area implements Serializable {
		private Integer id;//   	private String name;//   	private String code;//   	private Integer wlcompany;//   承运商id	private String wlcompanyname;//   承运商name	private String deptname;//   部门编码
	private String deptidname;//   部门名称
	private Date updatetime;
	
	private String sitecodes;//  站点编码，多个用逗号隔开
	private String sitenames;//  站点名称，多个用逗号隔开
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
        if(this == obj) return true;
        
        if(obj instanceof Area){
        	Area area =(Area)obj;
        	if(area.code.equals(this.code)) return true; // 只比较code
            // 比较id和username 一致时才返回true 之后再去比较 hashCode  
//        	if(user.id == this.id && user.username.equals(this.username)) return true;  
        }  
        return false;  
	}
	
	@Override
	public int hashCode() {
		return code.hashCode();
	}
		public Integer getId() {	    return this.id;	}	public void setId(Integer id) {	    this.id=id;	}	public String getName() {	    return this.name;	}	public void setName(String name) {	    this.name=name;	}	public String getCode() {	    return this.code;	}	public void setCode(String code) {	    this.code=code;	}	public Integer getWlcompany() {	    return this.wlcompany;	}	public void setWlcompany(Integer wlcompany) {	    this.wlcompany=wlcompany;	}

	public String getWlcompanyname() {
		return wlcompanyname;
	}

	public void setWlcompanyname(String wlcompanyname) {
		this.wlcompanyname = wlcompanyname;
	}

	public String getDeptname() {
		return deptname;
	}

	public String getDeptidname() {
		return deptidname;
	}

	public void setDeptidname(String deptidname) {
		this.deptidname = deptidname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getSitecodes() {
		return sitecodes;
	}

	public void setSitecodes(String sitecodes) {
		this.sitecodes = sitecodes;
	}

	public String getSitenames() {
		return sitenames;
	}

	public void setSitenames(String sitenames) {
		this.sitenames = sitenames;
	}
}

