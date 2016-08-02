package com.chinaway.tms.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.chinaway.tms.utils.page.PageBean;

public interface BaseService<T, ID extends Serializable> {
//	void setBaseMapper();
	/**
	 * 删除
	 * @param id	主键id
	 * @return
	 */
	public int deleteById(ID id);
	
	/**
	 * 批量删除(空实现)
	 * @param ids	多个主键值，用","隔开
	 * @return
	 */
	public int deleteById(String ids);
	
	/**
	 * 增加
	 * @param record	对象
	 * @return
	 */
	public int insert(T record);

//	int insertSelective(T record);
	
	/**
	 * 动态更新
	 * @param record	对象
	 * @return
	 */
	public int updateSelective(T record);

	/**
	 * 更新
	 * @param record	对象
	 * @return
	 */
	public int update(T record);
	
	/**
	 * 根据主键id查询单条记录
	 * @param id	主键id
	 * @return
	 */
	public T selectById(ID id);

	/**
	 * 条件查询所有
	 * @param map
	 * @return
	 */
	public List<T> selectAll4Page(Map<String,Object> map);
	
	/**
	 * 分页查询
	 * @param map
	 * @return
	 */
	public PageBean<T> select2PageBean(Map<String, Object> map);
	
}