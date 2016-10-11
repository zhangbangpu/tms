package com.chinaway.tms.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * cxf-webservice 订单接口（给SAP）
 * @author shu
 *
 */
@WebService
public interface Order2SAPWS {
	
	/**
	 * 由于SAP平台格式要求：{"CONTENT":"TMS_MM_PURCHASE_SAP_0UT:ID","ITABLE":[{实际参数}]}
	 * @param orderInfo
	 * @return
	 */
	 @WebMethod
	 public String addOrder(@WebParam(name = "orderInfo") String orderInfo);
}
