package com.chinaway.tms.basic.model;

import java.io.Serializable;

public class VehicleModel implements Serializable {
	
		private Integer id;//   	private String name;//   车型名称	private Double weight;//   额度重量	private Double volum;//   额度体积	private String wlcompany;//   承运商id	public Integer getId() {	    return this.id;	}	public void setId(Integer id) {	    this.id=id;	}	public String getName() {	    return this.name;	}	public void setName(String name) {	    this.name=name;	}	public Double getWeight() {	    return this.weight;	}	public void setWeight(Double weight) {	    this.weight=weight;	}	public Double getVolum() {	    return this.volum;	}	public void setVolum(Double volum) {	    this.volum=volum;	}	public String getWlcompany() {	    return this.wlcompany;	}	public void setWlcompany(String wlcompany) {	    this.wlcompany=wlcompany;	}
}

