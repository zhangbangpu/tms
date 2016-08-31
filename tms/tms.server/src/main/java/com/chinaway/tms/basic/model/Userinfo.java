package com.chinaway.tms.basic.model;

import java.io.Serializable;

/**
 * 用户信息
 * @author zhang
 *
 */
public class Userinfo implements Serializable {
	
	private static final long serialVersionUID = 8438289484045001157L;
	
	private Integer id;//   	private String loginname;//   登录名	private String password;//   密码	private String type;//   账户类型	private String name;//   公司名称	private String intro;//   公司简介	private String certificate;//   公司资质	private String corporation;//   公司法人姓名	private String corporationim;//   法人身份证照片	private String phone;//   联系方式	private String state;//   未认证、认证不通过、已认证、拉黑	private java.util.Date createtime;//   注册时间	private String capital;//   注册资金	private String email;//   邮箱	public Integer getId() {	    return this.id;	}	public void setId(Integer id) {	    this.id=id;	}	public String getLoginname() {	    return this.loginname;	}	public void setLoginname(String loginname) {	    this.loginname=loginname;	}	public String getPassword() {	    return this.password;	}	public void setPassword(String password) {	    this.password=password;	}	public String getType() {	    return this.type;	}	public void setType(String type) {	    this.type=type;	}	public String getName() {	    return this.name;	}	public void setName(String name) {	    this.name=name;	}	public String getIntro() {	    return this.intro;	}	public void setIntro(String intro) {	    this.intro=intro;	}	public String getCertificate() {	    return this.certificate;	}	public void setCertificate(String certificate) {	    this.certificate=certificate;	}	public String getCorporation() {	    return this.corporation;	}	public void setCorporation(String corporation) {	    this.corporation=corporation;	}	public String getCorporationim() {	    return this.corporationim;	}	public void setCorporationim(String corporationim) {	    this.corporationim=corporationim;	}	public String getPhone() {	    return this.phone;	}	public void setPhone(String phone) {	    this.phone=phone;	}	public String getState() {	    return this.state;	}	public void setState(String state) {	    this.state=state;	}	public java.util.Date getCreatetime() {	    return this.createtime;	}	public void setCreatetime(java.util.Date createtime) {	    this.createtime=createtime;	}	public String getCapital() {	    return this.capital;	}	public void setCapital(String capital) {	    this.capital=capital;	}	public String getEmail() {	    return this.email;	}	public void setEmail(String email) {	    this.email=email;	}
}

