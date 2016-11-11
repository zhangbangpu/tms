package com.chinaway.tms.basic.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinaway.tms.basic.dao.AreaMapper;
import com.chinaway.tms.basic.dao.AreaSiteMapper;
import com.chinaway.tms.basic.model.Area;
import com.chinaway.tms.basic.model.AreaSite;
import com.chinaway.tms.basic.service.AreaService;
import com.chinaway.tms.core.AbstractService;
import com.chinaway.tms.core.BaseMapper;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.page.PageBean;

@Service
public class AreaServiceImpl extends AbstractService<Area, Integer> implements AreaService {
	
	@Autowired
	private AreaMapper areaMapper;
	@Autowired
	private AreaSiteMapper areaSiteMapper;
	
	/**具体子类service的实现需要使用的mapper*/
	@Override
	@Autowired
	public void setBaseMapper(BaseMapper<Area, Integer> baseMapper) {
		super.setBaseMapper(baseMapper);
	}

	@Override
	public PageBean<Area> select2PageBean(Map<String, Object> map) {
		PageBean<Area> pageBean = new PageBean<>();
		pageBean.setPageNo(Integer.parseInt(map.get("pageNo").toString()));
		pageBean.setPageSize(Integer.parseInt(map.get("pageSize").toString()));
		//注意map要先设置pageBean,拦截器里面要获取其值
		map.put("pageBean", pageBean);
		map.put("needPage", true);//是否分页，默认是false不分页
		pageBean.setResult(areaMapper.selectAll4Page(map));
		return pageBean;
	}
	
	@Override
	@Transactional
	public int deleteById(String ids) {
		String[] idsStr = ids.split(",");
		if (idsStr.length > 0) {
			for (String str : idsStr) {
				int id = Integer.parseInt(str);
				Area area = areaMapper.selectById(id);
				areaSiteMapper.deleteByAreaCode(area.getCode());
				areaMapper.deleteById(id);
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public Area selectBySiteCode(String siteCode) {
		
		return areaMapper.selectBySiteCode(siteCode);
	}

	@Override
	@Transactional
	public int insertAreaAndItem(Area area) {
		areaMapper.insert(area);
		String sitecodes = area.getSitecodes();
		if(sitecodes !=null || !"".equals(sitecodes)){
			String[] sitecodesArr= sitecodes.split(",");
			AreaSite areaSite = null;
			for (String sitecode : sitecodesArr) {
				areaSite = new AreaSite();
				areaSite.setSitecode(sitecode);
				areaSite.setAreacode(area.getCode());
				
				areaSiteMapper.insert(areaSite);
			}
			
		}
		return 1;
	}

	@Override
	@Transactional
	public int updatetAreaAndItem(Area area) {
		areaMapper.updateSelective(area);
		String sitecodes = area.getSitecodes();
		if(!"".equals(sitecodes)){
			String[] sitecodesArr= sitecodes.split(",");
			AreaSite areaSite = null;
			for (String sitecode : sitecodesArr) {
				areaSite = new AreaSite();
				areaSite.setSitecode(sitecode);
				areaSite.setAreacode(area.getCode());
				Map<String, Object> map = new HashMap<>();
				map.put("areacode", areaSite.getAreacode());
				map.put("sitecode", areaSite.getSitecode());
				List<AreaSite> list = areaSiteMapper.selectAll4Page(map);
				if(list.size() > 0){
					areaSiteMapper.update(areaSite);//特别改过update
				}else{
					areaSiteMapper.insert(areaSite);
				}
			}
			
		}else{
			//设置不为空应该不会触发
			areaSiteMapper.deleteByAreaCode(area.getCode());
		}
		return 1;
	}
}
