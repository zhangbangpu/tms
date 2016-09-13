package com.chinaway.tms.basic.vo;

/**
 * g7系统的商品
 * @author shu
 *
 */
public class GoodsVo {
	private String goodsid;	//货品编号
	private String amount;	//货品数量
	
	private String goodsname;	//货品名称
	private String sku;	//货品SKU编号
	private String unit;	//货品数量单位
	private double number;	//货品总数量
	private String weight;	//货品总重量
	private String volume;	//货品总体积
	
	public String getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getGoodsname() {
		return goodsname;
	}
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public double getNumber() {
		return number;
	}
	public void setNumber(double number) {
		this.number = number;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	
	@Override
	public String toString() {
		return "GoodsVo [goodsname=" + goodsname + ", sku=" + sku + ", unit=" + unit + ", number=" + number
				+ ", weight=" + weight + ", volume=" + volume + "]";
	}
	
}
