package com.chinaway.tms.basic.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.chinaway.tms.basic.model.Orders;
import com.chinaway.tms.basic.model.VehicleModel;
import com.chinaway.tms.basic.model.Waybill;
import com.chinaway.tms.core.BaseService;

public interface OrdersService extends BaseService<Orders, Integer> {

	/**
	 * 查询最大id
	 * @return
	 */
	int selectMaxId();

	/**
	 * 查询所有订单根据条件
	 * @param argsMap
	 * @return
	 */
	List<Orders> selectAllOrdersByCtn(Map<String, Object> argsMap);
	
	/**
	 * 根据订单id查询承运商id
	 * @param map
	 * @return
	 */
	public Integer queryWlcompanyByOrderId(Map<String, Object> map);
	
	/**
	 * 根据订单id查询运单列表
	 * @param map
	 * @return
	 */
	public List<Integer> queryWlcompanysByOrderId(Map<String, Object> map);

	/**
	 * 生成运单
	 * @param orders 
	 * @return
	 */
	public List<String> generateWaybill(Orders orders);
	
	/**
	 * 根据订单对象，车型对象 生成运单
	 * @param order
	 * @param vehicleModel
	 * @return
	 * @throws Exception
	 */
	public Waybill setWaybill(Orders order, VehicleModel vehicleModel) throws Exception;

	/**
	 * 根据id列表查询订单
	 * @param ids
	 * @return
	 */
	List<Orders> selectByIds(String ids);
	
	/**
	 * 添加运单订单关系表
	 * 
	 * @param order
	 * @param waybillid
	 * @param wlcompany
	 * @param ret
	 */
	public void insertWaybillOrders(Orders order, Integer waybillid, String wlcompany);

	/**
	 * 查询订单详情
	 * @param i
	 * @return
	 */
	Orders selectDetailById(Integer i);

	/**
	 * 新增订单和明细
	 * @param order
	 * @param goodsList
	 * @return
	 */
	int insertOrder(Orders order, List<Map<String, Object>> goodsList);

	Date selectMaxUpdateTime();
	
}
