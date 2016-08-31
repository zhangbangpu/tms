package com.chinaway.tms.basic.model;

import java.io.Serializable;

import com.chinaway.tms.utils.json.JsonUtil;

/**
 * 车辆bean
 * @author zhang
 *
 */
public class Car implements Serializable {
	
	private static final long serialVersionUID = -3410164787140355903L;
	
	private Integer id;//   	private String name="车辆1";//   车辆名称	private String code="a1111";//   车辆编码	private Double weight=20d;//   额度重量	private Double volum=1000d;//   额度体积	private String carno="川A66888";//   车牌号	private String wlcompany="12332223232121";//   承运商id
	private String vehicleModelName="Q5062.6T4MRCA";//    车型名称  车系名称+年代款+排量+变速箱类型/驱动方式+型号名称
		public Integer getId() {	    return this.id;	}	public void setId(Integer id) {	    this.id=id;	}	public String getName() {	    return this.name;	}	public void setName(String name) {	    this.name=name;	}	public String getCode() {	    return this.code;	}	public void setCode(String code) {	    this.code=code;	}	public Double getWeight() {	    return this.weight;	}	public void setWeight(Double weight) {	    this.weight=weight;	}	public Double getVolum() {	    return this.volum;	}	public void setVolum(Double volum) {	    this.volum=volum;	}	public String getCarno() {	    return this.carno;	}	public void setCarno(String carno) {	    this.carno=carno;	}	public String getWlcompany() {	    return this.wlcompany;	}	public void setWlcompany(String wlcompany) {	    this.wlcompany=wlcompany;	}
	public String getVehicleModelName() {
		return vehicleModelName;
	}
	public void setVehicleModelName(String vehicleModelName) {
		this.vehicleModelName = vehicleModelName;
	}
	
	public static void main(String[] args){
		Car car = new Car();
		System.out.println(JsonUtil.obj2JsonStr(car));
	}
}
