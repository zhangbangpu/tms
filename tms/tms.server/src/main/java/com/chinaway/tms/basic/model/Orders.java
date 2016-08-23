package com.chinaway.tms.basic.model;

import java.io.Serializable;

/**
 * 订单
 * @author shu
 *
 */
public class Orders implements Serializable {
	
	private Integer id;//   	private String code;//   订单编号	private String fromcode;//   来源编号	private String orderfrom;//   订单来源：sap，wms	private java.util.Date createtime;//   创建时间	private String deptname;//   所属机构	private String subcontractor;//   转包承运商	private Double amount;//   货品数量	private String unit;//   货品单位	private Double weight;//   weight	private Double volume;//   货品总体积(m³)	private String fhaddress;//   发货地址	private String shaddress;//   收货地址	private java.util.Date requstarttime;//   要求发货时间	private java.util.Date requendtime;//   要求收货时间	private String state;//   当前执行阶段	private Integer exceptcount;//   异常事件数
	private String type;//   订单类型
	private String city;//   城市
	private String status = "0";//   状态
	private String cpmdName;//    货品名称
		public Integer getId() {	    return this.id;	}	public void setId(Integer id) {	    this.id=id;	}	public String getCode() {	    return this.code;	}	public void setCode(String code) {	    this.code=code;	}	public String getFromcode() {	    return this.fromcode;	}	public void setFromcode(String fromcode) {	    this.fromcode=fromcode;	}	public String getOrderfrom() {	    return this.orderfrom;	}	public void setOrderfrom(String orderfrom) {	    this.orderfrom=orderfrom;	}	public java.util.Date getCreatetime() {	    return this.createtime;	}	public void setCreatetime(java.util.Date createtime) {	    this.createtime=createtime;	}	public String getDeptname() {	    return this.deptname;	}	public void setDeptname(String deptname) {	    this.deptname=deptname;	}	public String getSubcontractor() {	    return this.subcontractor;	}	public void setSubcontractor(String subcontractor) {	    this.subcontractor=subcontractor;	}	public Double getAmount() {	    return this.amount;	}	public void setAmount(Double amount) {	    this.amount=amount;	}	public String getUnit() {	    return this.unit;	}	public void setUnit(String unit) {	    this.unit=unit;	}	public Double getWeight() {	    return this.weight;	}	public void setWeight(Double weight) {	    this.weight=weight;	}	public Double getVolume() {	    return this.volume;	}	public void setVolume(Double volume) {	    this.volume=volume;	}	public String getFhaddress() {	    return this.fhaddress;	}	public void setFhaddress(String fhaddress) {	    this.fhaddress=fhaddress;	}	public String getShaddress() {	    return this.shaddress;	}	public void setShaddress(String shaddress) {	    this.shaddress=shaddress;	}	public java.util.Date getRequstarttime() {	    return this.requstarttime;	}	public void setRequstarttime(java.util.Date requstarttime) {	    this.requstarttime=requstarttime;	}	public java.util.Date getRequendtime() {	    return this.requendtime;	}	public void setRequendtime(java.util.Date requendtime) {	    this.requendtime=requendtime;	}	public String getState() {	    return this.state;	}	public void setState(String state) {	    this.state=state;	}	public Integer getExceptcount() {	    return this.exceptcount;	}	public void setExceptcount(Integer exceptcount) {	    this.exceptcount=exceptcount;	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCpmdName() {
		return cpmdName;
	}
	public void setCpmdName(String cpmdName) {
		this.cpmdName = cpmdName;
	}
	
}

