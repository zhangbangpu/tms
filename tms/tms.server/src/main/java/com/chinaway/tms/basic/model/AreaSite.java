package com.chinaway.tms.basic.model;

import java.io.Serializable;

public class AreaSite implements Serializable {
		private Integer id;//   	private String sitecode;//   站点编码	private String areacode;//   站点区域编码	private String sitename;//   站点名称
		public Integer getId() {	    return this.id;	}	public void setId(Integer id) {	    this.id=id;	}	public String getSitecode() {	    return this.sitecode;	}	public void setSitecode(String sitecode) {	    this.sitecode=sitecode;	}	public String getAreacode() {	    return this.areacode;	}	public void setAreacode(String areacode) {	    this.areacode=areacode;	}
	public String getSitename() {
		return sitename;
	}
	public void setSitename(String sitename) {
		this.sitename = sitename;
	}
}

