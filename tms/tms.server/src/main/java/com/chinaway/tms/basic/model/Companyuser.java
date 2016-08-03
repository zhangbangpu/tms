package com.chinaway.tms.basic.model;

import java.io.Serializable;
import java.util.Date;
import com.chinaway.tms.utils.json.JsonUtil;

public class Companyuser implements Serializable {
	
	private static final long serialVersionUID = -8763212651178091811L;
	
	private Integer id = 0;//   主键	private String loginname = "汇通物流";//   登录名	private String password = "123";//   密码	private String type = "1";//   类型	private String name = "某某某物流";//   公司名称	private String intro = "公司是什么公司，什么时候上市，人员多少等等";//   公司简介	private String certificate = "c://tp.png";//   公司资质	private String corporation = "张三";//   公司法人	private String corporationim = "c://sfz.png";//   法人身份证照片	private String phone = "15800023132";//   联系方式	private String state = "3";//   状态	private java.util.Date createtime = new Date();//   注册时间
		public Integer getId() {	    return this.id;	}	public void setId(Integer id) {	    this.id=id;	}	public String getLoginname() {	    return this.loginname;	}	public void setLoginname(String loginname) {	    this.loginname=loginname;	}	public String getPassword() {	    return this.password;	}	public void setPassword(String password) {	    this.password=password;	}	public String getType() {	    return this.type;	}	public void setType(String type) {	    this.type=type;	}	public String getName() {	    return this.name;	}	public void setName(String name) {	    this.name=name;	}	public String getIntro() {	    return this.intro;	}	public void setIntro(String intro) {	    this.intro=intro;	}	public String getCertificate() {	    return this.certificate;	}	public void setCertificate(String certificate) {	    this.certificate=certificate;	}	public String getCorporation() {	    return this.corporation;	}	public void setCorporation(String corporation) {	    this.corporation=corporation;	}	public String getCorporationim() {	    return this.corporationim;	}	public void setCorporationim(String corporationim) {	    this.corporationim=corporationim;	}	public String getPhone() {	    return this.phone;	}	public void setPhone(String phone) {	    this.phone=phone;	}	public String getState() {	    return this.state;	}	public void setState(String state) {	    this.state=state;	}	public java.util.Date getCreatetime() {	    return this.createtime;	}	public void setCreatetime(java.util.Date createtime) {	    this.createtime=createtime;	}
	
	public static void main(String args[]){
		Companyuser companyuser = new Companyuser();
		System.out.println(JsonUtil.obj2JsonStr(companyuser));
	}
}