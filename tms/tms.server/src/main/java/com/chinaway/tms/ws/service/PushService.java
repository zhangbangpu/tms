package com.chinaway.tms.ws.service;

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
	 * 推送车次（运单）和订单信息给承运商
	 * @param orderParam 订单参数
	 * @param depParam 运单参数
	 */
	public boolean addOrderAndDep(String orderParam, String depParam) throws Exception;
	
}
