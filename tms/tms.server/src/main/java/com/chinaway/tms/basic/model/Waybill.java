package com.chinaway.tms.basic.model;

import java.io.Serializable;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 运单
 * @author zhang
 *
 */
public class Waybill implements Serializable {
	
	private static final long serialVersionUID = -5499181670910811369L;
	
	private Integer id;//   	private String code;//   运单编号	private String fromcode;//   来源编号	private String orderfrom;//   订单来源：sap，wms
	@JSONField(format="yyyy-MM-dd HH:mm:ss")	private java.util.Date createtime;//   创建时间	private String deptname;//   所属机构	private String subcontractor;//   转包承运商	private Double amount;//   货品数量	private String unit;//   货品单位	private Double weight;//   货品总重量(kg)	private Double volume;//   货品总体积(m³)	private String fhaddress;//   发货地址	private String shaddress;//   收货地址	private java.util.Date requstarttime;//   要求发货时间	private java.util.Date requendtime;//   要求收货时间	private String state = "0";//   当前执行阶段 默认 0 初始	private Integer exceptcount;//   异常事件数	private Double c_weight;//   匹配车型重量(kg)	private Double c_volume;//   匹配车型体积(m³)
	private String ordersid;//   订单id
	private String wlcompany;//   承运商
	private String vehiclemodel;//   车型
		public Integer getId() {	    return this.id;	}	public void setId(Integer id) {	    this.id=id;	}	public String getCode() {	    return this.code;	}	public void setCode(String code) {	    this.code=code;	}	public String getFromcode() {	    return this.fromcode;	}	public void setFromcode(String fromcode) {	    this.fromcode=fromcode;	}	public String getOrderfrom() {	    return this.orderfrom;	}	public void setOrderfrom(String orderfrom) {	    this.orderfrom=orderfrom;	}	public java.util.Date getCreatetime() {	    return this.createtime;	}	public void setCreatetime(java.util.Date createtime) {	    this.createtime=createtime;	}	public String getDeptname() {	    return this.deptname;	}	public void setDeptname(String deptname) {	    this.deptname=deptname;	}	public String getSubcontractor() {	    return this.subcontractor;	}	public void setSubcontractor(String subcontractor) {	    this.subcontractor=subcontractor;	}	public Double getAmount() {	    return this.amount;	}	public void setAmount(Double amount) {	    this.amount=amount;	}	public String getUnit() {	    return this.unit;	}	public void setUnit(String unit) {	    this.unit=unit;	}	public Double getWeight() {	    return this.weight;	}	public void setWeight(Double weight) {	    this.weight=weight;	}	public Double getVolume() {	    return this.volume;	}	public void setVolume(Double volume) {	    this.volume=volume;	}	public String getFhaddress() {	    return this.fhaddress;	}	public void setFhaddress(String fhaddress) {	    this.fhaddress=fhaddress;	}	public String getShaddress() {	    return this.shaddress;	}	public void setShaddress(String shaddress) {	    this.shaddress=shaddress;	}	public java.util.Date getRequstarttime() {	    return this.requstarttime;	}	public void setRequstarttime(java.util.Date requstarttime) {	    this.requstarttime=requstarttime;	}	public java.util.Date getRequendtime() {	    return this.requendtime;	}	public void setRequendtime(java.util.Date requendtime) {	    this.requendtime=requendtime;	}	public String getState() {	    return this.state;	}	public void setState(String state) {	    this.state=state;	}	public Integer getExceptcount() {	    return this.exceptcount;	}	public void setExceptcount(Integer exceptcount) {	    this.exceptcount=exceptcount;	}	public Double getC_weight() {	    return this.c_weight;	}	public void setC_weight(Double c_weight) {	    this.c_weight=c_weight;	}	public Double getC_volume() {	    return this.c_volume;	}	public void setC_volume(Double c_volume) {	    this.c_volume=c_volume;	}
	public String getOrdersid() {
		return ordersid;
	}
	public void setOrdersid(String ordersid) {
		this.ordersid = ordersid;
	}
	public String getWlcompany() {
		return wlcompany;
	}
	public void setWlcompany(String wlcompany) {
		this.wlcompany = wlcompany;
	}
	public String getVehiclemodel() {
		return vehiclemodel;
	}
	public void setVehiclemodel(String vehiclemodel) {
		this.vehiclemodel = vehiclemodel;
	}
	
}
