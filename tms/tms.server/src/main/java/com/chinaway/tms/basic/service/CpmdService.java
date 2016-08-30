package com.chinaway.tms.basic.service;

import java.util.List;
import java.util.Map;

import com.chinaway.tms.basic.model.Cpmd;
import com.chinaway.tms.core.BaseService;

public interface CpmdService extends BaseService<Cpmd, Integer> {

	/**
	 * 查询所有商品根据条件
	 * @param argsMap
	 * @return
	 */
	List<Cpmd> selectAllCpmdByCtn(Map<String, Object> argsMap);

	/**
	 * 连表查询商品根据订单id
	 * @param id
	 * @return
	 */
	List<Cpmd> selectCpmdByOrdersId(Integer id);
	
}
