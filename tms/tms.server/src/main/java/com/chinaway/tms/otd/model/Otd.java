package com.chinaway.tms.otd.model;

import java.io.Serializable;

public class Otd implements Serializable {
	
	private Integer id;//   	private String name;//   名称	private String status;//   任务状态 0关闭  1启用	private String type;//   任务类型	private Integer hours;//   小时	private Integer minute;//   分钟	private Integer seconds;//   秒	private Double tims;//   所需时间（分钟）	public Integer getId() {	    return this.id;	}	public void setId(Integer id) {	    this.id=id;	}	public String getName() {	    return this.name;	}	public void setName(String name) {	    this.name=name;	}	public String getStatus() {	    return this.status;	}	public void setStatus(String status) {	    this.status=status;	}	public String getType() {	    return this.type;	}	public void setType(String type) {	    this.type=type;	}	public Integer getHours() {	    return this.hours;	}	public void setHours(Integer hours) {	    this.hours=hours;	}	public Integer getMinute() {	    return this.minute;	}	public void setMinute(Integer minute) {	    this.minute=minute;	}	public Integer getSeconds() {	    return this.seconds;	}	public void setSeconds(Integer seconds) {	    this.seconds=seconds;	}	public Double getTims() {	    return this.tims;	}	public void setTims(Double tims) {	    this.tims=tims;	}
}

