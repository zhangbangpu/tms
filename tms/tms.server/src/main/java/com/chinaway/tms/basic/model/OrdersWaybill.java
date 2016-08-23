package com.chinaway.tms.basic.model;

import java.io.Serializable;

public class OrdersWaybill implements Serializable {
	
		private Integer id;//   	private Integer ordersid;//   订单id	private Integer waybillid;//   运单id	public Integer getId() {	    return this.id;	}	public void setId(Integer id) {	    this.id=id;	}	public Integer getOrdersid() {	    return this.ordersid;	}	public void setOrdersid(Integer ordersid) {	    this.ordersid=ordersid;	}	public Integer getWaybillid() {	    return this.waybillid;	}	public void setWaybillid(Integer waybillid) {	    this.waybillid=waybillid;	}
}

