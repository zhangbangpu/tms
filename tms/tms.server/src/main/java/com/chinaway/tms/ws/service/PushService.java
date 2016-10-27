package com.chinaway.tms.ws.service;

import java.util.List;
import java.util.Map;

import com.chinaway.tms.basic.model.Orders;
import com.chinaway.tms.basic.model.Waybill;

/**
 * 专门的推送接口
 * @author shu
 *
 */
public interface PushService {

	/**
	 * 推送用户（承运商）信息给g7平台
	 * @param param
	 */
	public boolean addUser(String param) throws Exception;
	
	/**
	 * 推送订单信息给承运商
	 * @param param
	 */
	public boolean addOrder(String param) throws Exception;
	
	/**
	 * 推送车次（运单）信息给承运商
	 * @param param
	 */
	public boolean addDeparture(String param) throws Exception;
	
	/**
	 * 将运单信息推送给wms 接口
	 * @throws Exception
	 */
	public boolean dep2wmsWS(Waybill waybill) throws Exception;
	
	/**
	 * 推送车次（运单）和订单信息给承运商
	 * @param orderParam 订单参数
	 * @param depParam 运单参数
	 */
	public boolean addOrderAndDep(String orderParam, String depParam) throws Exception;
	
	/**
	 * 查询订单
	 * @param param
	 * @return
	 */
	public List<Orders> selectAllOrders(Map<String, Object> paramMap) throws Exception ;
	
	/**
	 * 查询运单（车次）
	 * @param param
	 * @return
	 */
	public List<Waybill> selectAllDeparture(Map<String, Object> paramMap) throws Exception ;
	
	/**
	 * 查询订单详情
	 * @param param
	 * @return
	 */
	public Map<String, Object> selectOrderDetail(Map<String, Object> paramMap) throws Exception ;
	
}
