package com.chinaway.tms.basic.model;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 站点bean
 * @author zhang
 *
 */
public class Site implements Serializable {
		private static final long serialVersionUID = -7317870487305179544L;
	
	private Integer id;//   	private String name;//   name	private String code;//   站点编码	private String area;//   范围	private String deptname;//   所属机构	private String types;//   站点类型	private String isprivate;//   
	@JSONField(format="yyyy-MM-dd HH:mm:ss")	private java.util.Date updatetime;//   更新时间	private String province;//   省份	private String city;//   城市	private String address;//   地址
	@JSONField(format="yyyy-MM-dd HH:mm:ss")	private java.util.Date createtime;//   注册时间
	private Integer wlcompany;//   承运商id
    private MultipartFile imgFile;//    文件
		public Integer getId() {	    return this.id;	}	public void setId(Integer id) {	    this.id=id;	}	public String getName() {	    return this.name;	}	public void setName(String name) {	    this.name=name;	}	public String getCode() {	    return this.code;	}	public void setCode(String code) {	    this.code=code;	}	public String getArea() {	    return this.area;	}	public void setArea(String area) {	    this.area=area;	}	public String getDeptname() {	    return this.deptname;	}	public void setDeptname(String deptname) {	    this.deptname=deptname;	}	public String getTypes() {	    return this.types;	}	public void setTypes(String types) {	    this.types=types;	}	public String getIsprivate() {	    return this.isprivate;	}	public void setIsprivate(String isprivate) {	    this.isprivate=isprivate;	}	public java.util.Date getUpdatetime() {	    return this.updatetime;	}	public void setUpdatetime(java.util.Date updatetime) {	    this.updatetime=updatetime;	}	public String getProvince() {	    return this.province;	}	public void setProvince(String province) {	    this.province=province;	}	public String getCity() {	    return this.city;	}	public void setCity(String city) {	    this.city=city;	}	public String getAddress() {	    return this.address;	}	public void setAddress(String address) {	    this.address=address;	}	public java.util.Date getCreatetime() {	    return this.createtime;	}	public void setCreatetime(java.util.Date createtime) {	    this.createtime=createtime;	}
	public Integer getWlcompany() {
		return wlcompany;
	}
	public void setWlcompany(Integer wlcompany) {
		this.wlcompany = wlcompany;
	}
	public MultipartFile getImgFile() {
        return imgFile;
    }
    public void setImgFile(MultipartFile imgFile) {
        this.imgFile = imgFile;
    }
	
}