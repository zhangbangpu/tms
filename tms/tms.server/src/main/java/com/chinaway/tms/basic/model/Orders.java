package com.chinaway.tms.basic.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单
 * @author shu
 *
 */
public class Orders implements Serializable {
	
	private static final long serialVersionUID = -7206999889938664252L;
	
	private Integer id;//   	private String code;//   订单编号	private String fromcode;//   来源编号	private String orderfrom;//   订单来源：sap，wms	private java.util.Date createtime;//   创建时间	private String deptname;//   所属机构	private String subcontractor;//   转包承运商	private Double amount;//   货品数量	private String unit;//   货品单位	private Double weight;//   weight	private Double volume;//   货品总体积(m³)	private String fhaddress;//   发货地址	private String shaddress;//   收货地址	private java.util.Date requstarttime;//   要求发货时间	private java.util.Date requendtime;//   要求收货时间	private String state;//   当前执行阶段	private Integer exceptcount;//   异常事件数
	private String city;//   城市
	private String status = "0";//   状态
	private String cpmdName;//    货品名称
	private String updatetime;//  更新时间
	private Integer pid;//    父id
	private List<String> stateList = new ArrayList<String>();//    执行阶段列表
	private OrderItem baseInfo = new OrderItem();
	private List<Map<String,Object>> dispatchInfos = new ArrayList<Map<String,Object>>();//  
	private List<Map<String,Object>> steps = new ArrayList<Map<String,Object>>();
		public Integer getId() {	    return this.id;	}	public void setId(Integer id) {	    this.id=id;	}	public String getCode() {	    return this.code;	}	public void setCode(String code) {	    this.code=code;	}	public String getFromcode() {	    return this.fromcode;	}	public void setFromcode(String fromcode) {	    this.fromcode=fromcode;	}	public String getOrderfrom() {	    return this.orderfrom;	}	public void setOrderfrom(String orderfrom) {	    this.orderfrom=orderfrom;	}	public java.util.Date getCreatetime() {	    return this.createtime;	}	public void setCreatetime(java.util.Date createtime) {	    this.createtime=createtime;	}	public String getDeptname() {	    return this.deptname;	}	public void setDeptname(String deptname) {	    this.deptname=deptname;	}	public String getSubcontractor() {	    return this.subcontractor;	}	public void setSubcontractor(String subcontractor) {	    this.subcontractor=subcontractor;	}	public Double getAmount() {	    return this.amount;	}	public void setAmount(Double amount) {	    this.amount=amount;	}	public String getUnit() {	    return this.unit;	}	public void setUnit(String unit) {	    this.unit=unit;	}	public Double getWeight() {	    return this.weight;	}	public void setWeight(Double weight) {	    this.weight=weight;	}	public Double getVolume() {	    return this.volume;	}	public void setVolume(Double volume) {	    this.volume=volume;	}	public String getFhaddress() {	    return this.fhaddress;	}	public void setFhaddress(String fhaddress) {	    this.fhaddress=fhaddress;	}	public String getShaddress() {	    return this.shaddress;	}	public void setShaddress(String shaddress) {	    this.shaddress=shaddress;	}	public java.util.Date getRequstarttime() {	    return this.requstarttime;	}	public void setRequstarttime(java.util.Date requstarttime) {	    this.requstarttime=requstarttime;	}	public java.util.Date getRequendtime() {	    return this.requendtime;	}	public void setRequendtime(java.util.Date requendtime) {	    this.requendtime=requendtime;	}	public String getState() {	    return this.state;	}	public void setState(String state) {	    this.state=state;	}	public Integer getExceptcount() {	    return this.exceptcount;	}	public void setExceptcount(Integer exceptcount) {	    this.exceptcount=exceptcount;	}
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
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public List<String> getStateList() {
		return stateList;
	}
	public void setStateList(List<String> stateList) {
		stateList.add("0");
		stateList.add("1");
		stateList.add("2");
		stateList.add("3");
		stateList.add("4");
		this.stateList = stateList;
	}
	public OrderItem getBaseInfo() {
		return baseInfo;
	}
	public void setBaseInfo(OrderItem baseInfo) {
		this.baseInfo = baseInfo;
	}
	public List<Map<String, Object>> getDispatchInfos() {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("started", true);
		map.put("name", "1");
		map.put("starttime", "2016-08-30 15:31:10");
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("started", true);
		map1.put("name", "2");
		map1.put("starttime", "2016-08-30 15:31:10");
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("started", true);
		map2.put("name", "3");
		map2.put("starttime", "2016-08-30 15:31:10");
		dispatchInfos.add(map);
		dispatchInfos.add(map1);
		dispatchInfos.add(map2);
		return dispatchInfos;
	}
	public void setDispatchInfos(List<Map<String, Object>> dispatchInfos) {
		this.dispatchInfos = dispatchInfos;
	}
	public List<Map<String, Object>> getSteps() {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gstarttime", "2016-08-30 15:31:10");
		map.put("garrivetime", "2016-08-30 15:32:10");
		map.put("pstarttime", "2016-08-30 15:33:10");
		map.put("parrivetime", "2016-08-30 15:34:10");
		map.put("status", "2016-08-30 15:35:10");
		map.put("showTime", "2016-08-30 15:36:10");
		map.put("carnum", "奥迪");
		map.put("implementstep", "1");
		map.put("drivername", "A6");
		
		List<Map<String, Object>> monitorList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map6 = new HashMap<String, Object>();
		map6.put("type", "zpt");
		List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map7 = new HashMap<String, Object>();
		map7.put("type", "statusLog");
		map7.put("time", "2016-08-30 15:31:10");
		map7.put("name", "2016-08-30 15:31:10");
		map7.put("address", "1");
		List<Map<String, Object>> photoList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map10 = new HashMap<String, Object>();
		map10.put("photoUrl", "/img/g7tms/logo.png");
		photoList.add(map10);
		map7.put("photo", photoList);
		map7.put("itime", "2016-08-30 15:31:10");
		map7.put("sitmane", "2016-08-30 15:31:10");
		map7.put("otime", "2016-08-30 15:31:10");
		Map<String, Object> map8 = new HashMap<String, Object>();
		map8.put("type", "eventLog");
		map8.put("time", "2016-08-30 15:31:10");
		map8.put("name", "2016-08-30 15:31:10");
		map8.put("address", "1");
		map8.put("photo", photoList);
		map8.put("itime", "2016-08-30 15:31:10");
		map8.put("sitmane", "2016-08-30 15:31:10");
		map8.put("otime", "2016-08-30 15:31:10");
		Map<String, Object> map9 = new HashMap<String, Object>();
		map9.put("type", "eventLog");
		map9.put("time", "2016-08-30 15:31:10");
		map9.put("name", "2016-08-30 15:31:10");
		map9.put("address", "1");
		map9.put("photo", photoList);
		map9.put("itime", "2016-08-30 15:31:10");
		map9.put("sitmane", "2016-08-30 15:31:10");
		map9.put("otime", "2016-08-30 15:31:10");
		detailList.add(map7);
		detailList.add(map8);
		detailList.add(map9);
		
		map6.put("type", "classline");
		map6.put("detail", detailList);
		monitorList.add(map6);
		map.put("monitorList", monitorList);
		
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("gstarttime", "2016-08-30 15:31:10");
		map1.put("garrivetime", "2016-08-30 15:32:10");
		map1.put("pstarttime", "2016-08-30 15:33:10");
		map1.put("parrivetime", "2016-08-30 15:34:10");
		map1.put("status", "2016-08-30 15:35:10");
		map1.put("showTime", "2016-08-30 15:36:10");
		map1.put("carnum", "奔驰");
		map1.put("implementstep", "1");
		map1.put("drivername", "Q5");
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("gstarttime", "2016-08-30 15:31:10");
		map2.put("garrivetime", "2016-08-30 15:32:10");
		map2.put("pstarttime", "2016-08-30 15:33:10");
		map2.put("parrivetime", "2016-08-30 15:34:10");
		map2.put("status", "2016-08-30 15:35:10");
		map2.put("showTime", "2016-08-30 15:36:10");
		map2.put("carnum", "宝马");
		map2.put("implementstep", "1");
		map2.put("drivername", "T3");
		steps.add(map);
		steps.add(map1);
		steps.add(map2);
		
		Map<String, Object> map4 = new HashMap<String, Object>();
		map4.put("aliasname", "1");
		map4.put("departures", steps);
		mapList.add(map4);
		return mapList;
	}
	
	public void setSteps(List<Map<String, Object>> steps) {
		this.steps = steps;
	}
	
}