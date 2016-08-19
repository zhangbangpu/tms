package com.chinaway.tms.basic.model;

import java.io.Serializable;

public class OrderItem implements Serializable {
	
		private Integer id;//   	private Integer orderid;//   订单id	private Integer goodscode;//   商品编号	private String goodsname;//   商品名称	public Integer getId() {	    return this.id;	}	public void setId(Integer id) {	    this.id=id;	}	public Integer getOrderid() {	    return this.orderid;	}	public void setOrderid(Integer orderid) {	    this.orderid=orderid;	}	public Integer getGoodscode() {	    return this.goodscode;	}	public void setGoodscode(Integer goodscode) {	    this.goodscode=goodscode;	}	public String getGoodsname() {	    return this.goodsname;	}	public void setGoodsname(String goodsname) {	    this.goodsname=goodsname;	}
}

