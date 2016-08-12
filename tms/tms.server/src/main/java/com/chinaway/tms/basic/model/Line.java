package com.chinaway.tms.basic.model;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.chinaway.tms.utils.json.JsonUtil;

public class Line implements Serializable {
	
	private static final long serialVersionUID = 7009433621776630334L;
	
	private Integer id=0;    //   主键	private String pdeptname = "物流中心";    //   所属顶级机构	private String deptname = "分拨站";    //   所属机构	private String code = "01";    //   班线编码	private String name = "旅游班线";    //   班线名称	private String startsite = "01";    //   首站点	private String endsite = "08";    //   到达站点	private String halfwaysite = "02";    //   途经点	private String runtime = "30";    //   运行时间(分钟)	private String mileage = "20km";    //   计划总里程(公里)	private String vehicleline = "最近线路";    //   绑定行驶线路
	@JSONField(format="yyyy-MM-dd HH:mm:ss")	private java.util.Date updatetime = new Date();    //   修改时间	private String fromcode = "03";    //   班线来源机构编码	private String linetype = "1";    //   班线类型	private String transtype = "2";    //   运费类型	private Double transmoney = 1d;    //   运费	private String operates = "修改线路成功";    //   操作记录	private Double roadtoll = 1d;    //   过路费(元)
		public Integer getId() {	    return this.id;	}	public void setId(Integer id) {	    this.id=id;	}	public String getPdeptname() {	    return this.pdeptname;	}	public void setPdeptname(String pdeptname) {	    this.pdeptname=pdeptname;	}	public String getDeptname() {	    return this.deptname;	}	public void setDeptname(String deptname) {	    this.deptname=deptname;	}	public String getCode() {	    return this.code;	}	public void setCode(String code) {	    this.code=code;	}	public String getName() {	    return this.name;	}	public void setName(String name) {	    this.name=name;	}	public String getStartsite() {	    return this.startsite;	}	public void setStartsite(String startsite) {	    this.startsite=startsite;	}	public String getEndsite() {	    return this.endsite;	}	public void setEndsite(String endsite) {	    this.endsite=endsite;	}	public String getHalfwaysite() {	    return this.halfwaysite;	}	public void setHalfwaysite(String halfwaysite) {	    this.halfwaysite=halfwaysite;	}	public String getRuntime() {	    return this.runtime;	}	public void setRuntime(String runtime) {	    this.runtime=runtime;	}	public String getMileage() {	    return this.mileage;	}	public void setMileage(String mileage) {	    this.mileage=mileage;	}	public String getVehicleline() {	    return this.vehicleline;	}	public void setVehicleline(String vehicleline) {	    this.vehicleline=vehicleline;	}	public java.util.Date getUpdatetime() {	    return this.updatetime;	}	public void setUpdatetime(java.util.Date updatetime) {	    this.updatetime=updatetime;	}	public String getFromcode() {	    return this.fromcode;	}	public void setFromcode(String fromcode) {	    this.fromcode=fromcode;	}	public String getLinetype() {	    return this.linetype;	}	public void setLinetype(String linetype) {	    this.linetype=linetype;	}	public String getTranstype() {	    return this.transtype;	}	public void setTranstype(String transtype) {	    this.transtype=transtype;	}	public Double getTransmoney() {	    return this.transmoney;	}	public void setTransmoney(Double transmoney) {	    this.transmoney=transmoney;	}	public String getOperates() {	    return this.operates;	}	public void setOperates(String operates) {	    this.operates=operates;	}	public Double getRoadtoll() {	    return this.roadtoll;	}	public void setRoadtoll(Double roadtoll) {	    this.roadtoll=roadtoll;	}
	@Override
	public String toString() {
		return "Line [id=" + id + ", pdeptname=" + pdeptname + ", deptname=" + deptname + ", code=" + code + ", name="
				+ name + ", startsite=" + startsite + ", endsite=" + endsite + ", halfwaysite=" + halfwaysite
				+ ", runtime=" + runtime + ", mileage=" + mileage + ", vehicleline=" + vehicleline + ", updatetime="
				+ updatetime + ", fromcode=" + fromcode + ", linetype=" + linetype + ", transtype=" + transtype
				+ ", transmoney=" + transmoney + ", operates=" + operates + ", roadtoll=" + roadtoll + "]";
	}
	
	public static void main(String args[]){
		Line line = new Line();
		System.out.println(JsonUtil.obj2JsonStr(line));
	}
	
}

