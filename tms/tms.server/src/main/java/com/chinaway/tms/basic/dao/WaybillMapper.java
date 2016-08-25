package com.chinaway.tms.basic.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.chinaway.tms.basic.model.Waybill;
import com.chinaway.tms.core.BaseMapper;

public interface WaybillMapper extends BaseMapper<Waybill, Integer> {

	/**
	 * 按条件查询所有车次
	 * @param argsMap
	 * @return
	 */
	List<Waybill> selectAllTckNumByCtn(Map<String, Object> argsMap);

	/**
	 * 按条件查询所有审核
	 * @param argsMap
	 * @return
	 */
	List<Waybill> selectAllTckNumRvwedByCtn(Map<String, Object> argsMap);

	/**
	 * 查询最后一条数据id
	 * @return
	 */
	int selectMaxId();

	/**
	 * 根据id列表查询运单信息
	 * @param idsArray
	 * @return
	 */
	List<Waybill> selectByIds(@Param(value="array")String[] idArry);
	
}