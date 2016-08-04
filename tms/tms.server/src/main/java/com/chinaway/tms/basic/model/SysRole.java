package com.chinaway.tms.basic.model;

import java.io.Serializable;

public class SysRole implements Serializable {
	
		private Integer id;//   主键	private String name;//   名称	private String description;//   描述	private String type;//   角色类型	public Integer getId() {	    return this.id;	}	public void setId(Integer id) {	    this.id=id;	}	public String getName() {	    return this.name;	}	public void setName(String name) {	    this.name=name;	}	public String getDescription() {	    return this.description;	}	public void setDescription(String description) {	    this.description=description;	}	public String getType() {	    return this.type;	}	public void setType(String type) {	    this.type=type;	}
}

