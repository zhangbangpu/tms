package com.chinaway.tms.basic.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * g7系统的订单
 * @author shu
 *
 */
public class OrderVo {
	private String orderno;	//订单号
	private String wmsno;	 //详情单号
	private String userorderno;//客户订单号
	private String begintime;
	private String scompany;
	private String sprovince; //发货省份
	private String scity;		 //发货城市
	private String sdistricts;	 //发货区县
	private String slocation;	 //发货地址
	private String sname;
	private String sphone;
	private String sdatetime;
	private String ssitename;
	private String rcompany;
	private String rprovince; //发货省份
	private String rcity;		 //发货城市
	private String rdistricts;	 //发货区县
	private String rlocation;	 //发货地址
	private String rname;
	private String rphone;
	private String rdatetime;
	private String rsitename;
	private String projectnum;
	private String projectname;
	private String typenum;
	private String typename;
	private String extfield1;
	private String extfield2;
	
	private List<GoodsVo> goods;

	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getWmsno() {
		return wmsno;
	}
	public void setWmsno(String wmsno) {
		this.wmsno = wmsno;
	}
	public String getUserorderno() {
		return userorderno;
	}
	public void setUserorderno(String userorderno) {
		this.userorderno = userorderno;
	}
	public String getBegintime() {
		return begintime;
	}
	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}
	public String getScompany() {
		return scompany;
	}
	public void setScompany(String scompany) {
		this.scompany = scompany;
	}
	public String getSprovince() {
		return sprovince;
	}
	public void setSprovince(String sprovince) {
		this.sprovince = sprovince;
	}
	public String getScity() {
		return scity;
	}
	public void setScity(String scity) {
		this.scity = scity;
	}
	public String getSdistricts() {
		return sdistricts;
	}
	public void setSdistricts(String sdistricts) {
		this.sdistricts = sdistricts;
	}
	public String getSlocation() {
		return slocation;
	}
	public void setSlocation(String slocation) {
		this.slocation = slocation;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getSphone() {
		return sphone;
	}
	public void setSphone(String sphone) {
		this.sphone = sphone;
	}
	public String getSdatetime() {
		return sdatetime;
	}
	public void setSdatetime(String sdatetime) {
		this.sdatetime = sdatetime;
	}
	public String getSsitename() {
		return ssitename;
	}
	public void setSsitename(String ssitename) {
		this.ssitename = ssitename;
	}
	public String getRcompany() {
		return rcompany;
	}
	public void setRcompany(String rcompany) {
		this.rcompany = rcompany;
	}
	public String getRprovince() {
		return rprovince;
	}
	public void setRprovince(String rprovince) {
		this.rprovince = rprovince;
	}
	public String getRcity() {
		return rcity;
	}
	public void setRcity(String rcity) {
		this.rcity = rcity;
	}
	public String getRdistricts() {
		return rdistricts;
	}
	public void setRdistricts(String rdistricts) {
		this.rdistricts = rdistricts;
	}
	public String getRlocation() {
		return rlocation;
	}
	public void setRlocation(String rlocation) {
		this.rlocation = rlocation;
	}
	public String getRname() {
		return rname;
	}
	public void setRname(String rname) {
		this.rname = rname;
	}
	public String getRphone() {
		return rphone;
	}
	public void setRphone(String rphone) {
		this.rphone = rphone;
	}
	public String getRdatetime() {
		return rdatetime;
	}
	public void setRdatetime(String rdatetime) {
		this.rdatetime = rdatetime;
	}
	public String getRsitename() {
		return rsitename;
	}
	public void setRsitename(String rsitename) {
		this.rsitename = rsitename;
	}
	public String getProjectnum() {
		return projectnum;
	}
	public void setProjectnum(String projectnum) {
		this.projectnum = projectnum;
	}
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public String getTypenum() {
		return typenum;
	}
	public void setTypenum(String typenum) {
		this.typenum = typenum;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public String getExtfield1() {
		return extfield1;
	}
	public void setExtfield1(String extfield1) {
		this.extfield1 = extfield1;
	}
	public String getExtfield2() {
		return extfield2;
	}
	public void setExtfield2(String extfield2) {
		this.extfield2 = extfield2;
	}
	public List<GoodsVo> getGoods() {
		return goods;
	}
	public void setGoods(List<GoodsVo> goods) {
		this.goods = goods;
	}
	
	@Override
	public String toString() {
		return "OrderVo [orderno=" + orderno + ", wmsno=" + wmsno + ", userorderno=" + userorderno + ", begintime="
				+ begintime + ", scompany=" + scompany + ", sprovince=" + sprovince + ", scity=" + scity
				+ ", sdistricts=" + sdistricts + ", slocation=" + slocation + ", sname=" + sname + ", sphone=" + sphone
				+ ", sdatetime=" + sdatetime + ", ssitename=" + ssitename + ", rcompany=" + rcompany + ", rprovince="
				+ rprovince + ", rcity=" + rcity + ", rdistricts=" + rdistricts + ", rlocation=" + rlocation
				+ ", rname=" + rname + ", rphone=" + rphone + ", rdatetime=" + rdatetime + ", rsitename=" + rsitename
				+ ", projectnum=" + projectnum + ", projectname=" + projectname + ", typenum=" + typenum + ", typename="
				+ typename + ", extfield1=" + extfield1 + ", extfield2=" + extfield2 + ", goods=" + goods + "]";
	}
	
}
