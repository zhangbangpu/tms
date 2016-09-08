package com.chinaway.tms.basic.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单详情
 * @author zhang
 *
 */
public class OrderItem implements Serializable {
	
	private static final long serialVersionUID = 2740752498555850147L;
	
	private Integer id;//   	private Integer orderid;//   订单编号	private String goodscode;//   商品编号	private String goodsname;//   商品名称
	private List<Cpmd> goods= new ArrayList<Cpmd>();  //id
	private String orderno;  //   订单号
	private String number;  //   总件数
	private String sdatetime;  //   要求发货时间
	private String wmsno;  //   ERP/WMS单号
	private String entity;  //   货品单位
	private String rcompany;  //   收货单位
	private Date createtime;  //创建时间
	private String weight;  //   货品总重(Kg)
	private String rlocation;  //   收货地址
	private String orgcodeName;  //   所属机构
	private String volume;  //   货品总体积(m3)
	private String rsitename;  //   收货站点
	private String fromorgcodeName;  //   来源机构
	private String scompany;  //   发货单位
	private String rname;  //   收货人
	private String toorgcodeName;  //   转包承运商
	private String slocation;  //   发货地址
	private String rphone;  //   收货人电话
	private Date assigntime;  //   转包时间
	private Date ssitename;  //   发货站点
	private Date rdatetime;  //   要求收货时间
	private String departureslist;  //   实施模式
	private String sname;  //   sname
	private String events;  //   events
	private String classname;  //   线路名称
	private String sphone;  //   发货人电话
		public Integer getId() {	    return this.id;	}	public void setId(Integer id) {	    this.id=id;	}	public Integer getOrderid() {	    return this.orderid;	}	public void setOrderid(Integer orderid) {	    this.orderid=orderid;	}	public String getGoodscode() {	    return this.goodscode;	}	public void setGoodscode(String goodscode) {	    this.goodscode=goodscode;	}	public String getGoodsname() {	    return this.goodsname;	}	public void setGoodsname(String goodsname) {	    this.goodsname=goodsname;	}
	public List<Cpmd> getGoods() {
		return goods;
	}
	public void setGoods(List<Cpmd> goods) {
		this.goods = goods;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getSdatetime() {
		return sdatetime;
	}
	public void setSdatetime(String sdatetime) {
		this.sdatetime = sdatetime;
	}
	public String getWmsno() {
		return wmsno;
	}
	public void setWmsno(String wmsno) {
		this.wmsno = wmsno;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getRcompany() {
		return rcompany;
	}
	public void setRcompany(String rcompany) {
		this.rcompany = rcompany;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getRlocation() {
		return rlocation;
	}
	public void setRlocation(String rlocation) {
		this.rlocation = rlocation;
	}
	public String getOrgcodeName() {
		return orgcodeName;
	}
	public void setOrgcodeName(String orgcodeName) {
		this.orgcodeName = orgcodeName;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getRsitename() {
		return rsitename;
	}
	public void setRsitename(String rsitename) {
		this.rsitename = rsitename;
	}
	public String getFromorgcodeName() {
		return fromorgcodeName;
	}
	public void setFromorgcodeName(String fromorgcodeName) {
		this.fromorgcodeName = fromorgcodeName;
	}
	public String getScompany() {
		return scompany;
	}
	public void setScompany(String scompany) {
		this.scompany = scompany;
	}
	public String getRname() {
		return rname;
	}
	public void setRname(String rname) {
		this.rname = rname;
	}
	public String getToorgcodeName() {
		return toorgcodeName;
	}
	public void setToorgcodeName(String toorgcodeName) {
		this.toorgcodeName = toorgcodeName;
	}
	public String getSlocation() {
		return slocation;
	}
	public void setSlocation(String slocation) {
		this.slocation = slocation;
	}
	public String getRphone() {
		return rphone;
	}
	public void setRphone(String rphone) {
		this.rphone = rphone;
	}
	public Date getAssigntime() {
		return assigntime;
	}
	public void setAssigntime(Date assigntime) {
		this.assigntime = assigntime;
	}
	public Date getSsitename() {
		return ssitename;
	}
	public void setSsitename(Date ssitename) {
		this.ssitename = ssitename;
	}
	public Date getRdatetime() {
		return rdatetime;
	}
	public void setRdatetime(Date rdatetime) {
		this.rdatetime = rdatetime;
	}
	public String getDepartureslist() {
		return departureslist;
	}
	public void setDepartureslist(String departureslist) {
		this.departureslist = departureslist;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getEvents() {
		return events;
	}
	public void setEvents(String events) {
		this.events = events;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public String getSphone() {
		return sphone;
	}
	public void setSphone(String sphone) {
		this.sphone = sphone;
	}
	
}