package com.chinaway.tms.basic.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.chinaway.tms.basic.model.Orders;
import com.chinaway.tms.basic.model.Waybill;
import com.chinaway.tms.core.BaseService;

public interface WaybillService extends BaseService<Waybill, Integer> {

	/**
	 * 查询所有车次列表
	 * @param argsMap
	 * @return
	 */
	List<Waybill> selectAllTckNumByCtn(Map<String, Object> argsMap);

	/**
	 * 查询所有车次审核
	 * @param argsMap
	 * @return
	 */
	List<Waybill> selectAllTckNumRvwedByCtn(Map<String, Object> argsMap);

	/**
	 * 添加运单（并推送）
	 * @param waybill
	 * @param ordersList 
	 * @param orders 
	 * @return
	 */
	Integer insertWaybill(Waybill waybill, List<Orders> ordersList) throws Exception;

	/**
	 * 修改运单(运单审核)
	 * @param waybill
	 * @return
	 */
	int updateWaybill(Waybill waybill) throws Exception;

	/**
	 * 查询最后一条记录id
	 * @return
	 */
	int selectMaxId();

	/**
	 * 根据订单id查询订单列表
	 * @param ids
	 * @return
	 */
	List<Waybill> selectByIds(String ids);

	Date selectMaxUpdateTime();

	/**
	 * 自动生成运单，并添加关联表
	 * @param waybill
	 * @param ordersList
	 * @return
	 */
	int insert2Auto(Waybill waybill, List<Orders> ordersList);
	
}
