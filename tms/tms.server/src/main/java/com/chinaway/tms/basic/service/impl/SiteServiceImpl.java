package com.chinaway.tms.basic.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinaway.tms.basic.dao.SiteMapper;
import com.chinaway.tms.basic.model.Site;
import com.chinaway.tms.basic.service.SiteService;
import com.chinaway.tms.core.AbstractService;
import com.chinaway.tms.core.BaseMapper;
import com.chinaway.tms.utils.page.PageBean;

@Service
public class SiteServiceImpl extends AbstractService<Site, Integer> implements SiteService {
	
	@Autowired
	private SiteMapper siteMapper;
	
	/**具体子类service的实现需要使用的mapper*/
	@Override
	@Autowired
	public void setBaseMapper(BaseMapper<Site, Integer> baseMapper) {
		super.setBaseMapper(baseMapper);
	}

	@Override
	public PageBean<Site> select2PageBean(Map<String, Object> map) {
		PageBean<Site> pageBean = new PageBean<>();
		pageBean.setPageNo(Integer.parseInt(map.get("pageNo").toString()));
		pageBean.setPageSize(Integer.parseInt(map.get("pageSize").toString()));
		//注意map要先设置pageBean,拦截器里面要获取其值
		map.put("pageBean", pageBean);
		map.put("needPage", true);//是否分页，默认是false不分页
		pageBean.setResult(siteMapper.selectAll4Page(map));
		return pageBean;
	}
	
	@Override
	@Transactional
	public int deleteById(String ids) {
		String[] idsStr = ids.split(",");
		if (idsStr.length > 0) {
			for (String id : idsStr) {
				siteMapper.deleteById(Integer.parseInt(id));
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public List<Site> queAllSiteByCtn(Map<String, Object> argsMap) {
		return siteMapper.queAllSiteByCtn(argsMap);
	}

	@Override
	public PageBean<Site> queSiteByCtnPgBn(Map<String, Object> argsMap) {
		return siteMapper.queSiteByCtnPgBn(argsMap);
	}
}
