package com.chinaway.tms.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseMapper<T, ID extends Serializable> {
	int deleteById(ID id);

	int insert(T record);

//	int insertSelective(T record);

	T selectById(ID id);
	
	List<T> selectAll4Page(Map<String,Object> map);

	int updateSelective(T record);

	int update(T record);
}