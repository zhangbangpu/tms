package com.chinaway.tms.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class AbstractService<T, ID extends Serializable> implements BaseService<T, ID> {
	private BaseMapper<T, ID> baseMapper;

	public void setBaseMapper(BaseMapper<T, ID> baseMapper) {
		this.baseMapper = baseMapper;
	}

	@Override
	public int deleteById(ID id) {
		return baseMapper.deleteById(id);
	}
	
	@Override
	public int deleteById(String ids) {
		return 1;
	}

//	@Override
//	public int insertSelective(T record) {
//		return baseMapper.insertSelective(record);
//	}

	@Override
	public T selectById(ID id) {
		return baseMapper.selectById(id);
	}

	@Override
	public List<T> selectAll4Page(Map<String, Object> map) {
		return baseMapper.selectAll4Page(map);
	}

	@Override
	public int updateSelective(T record) {
		return baseMapper.updateSelective(record);
	}

	@Override
	public int update(T record) {
		return baseMapper.update(record);
	}

	@Override
	public int insert(T record) {
		return baseMapper.insert(record);
	}

}
