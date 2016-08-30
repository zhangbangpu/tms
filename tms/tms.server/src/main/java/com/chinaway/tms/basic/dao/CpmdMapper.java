package com.chinaway.tms.basic.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.chinaway.tms.basic.model.Cpmd;
import com.chinaway.tms.core.BaseMapper;

public interface CpmdMapper extends BaseMapper<Cpmd, Integer> {

	/**
	 * 查询所有商品信息
	 * @param argsMap
	 * @return
	 */
	List<Cpmd> selectAllCpmdByCtn(Map<String, Object> argsMap);

	/**
	 * 查询商品根据订单id
	 * @return
	 */
	List<Cpmd> selectCpmdByOrdersId(@Param(value="orderid")Integer orderid);
	
}