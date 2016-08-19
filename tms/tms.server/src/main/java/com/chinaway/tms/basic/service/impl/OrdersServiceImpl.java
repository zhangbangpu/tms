package com.chinaway.tms.basic.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chinaway.tms.basic.dao.OrdersMapper;
import com.chinaway.tms.basic.dao.SiteMapper;
import com.chinaway.tms.basic.dao.VehicleModelMapper;
import com.chinaway.tms.basic.model.Cpmd;
import com.chinaway.tms.basic.model.Orders;
import com.chinaway.tms.basic.model.Site;
import com.chinaway.tms.basic.model.VehicleModel;
import com.chinaway.tms.basic.service.OrdersService;
import com.chinaway.tms.core.AbstractService;
import com.chinaway.tms.core.BaseMapper;
import com.chinaway.tms.utils.page.PageBean;

@Service
public class OrdersServiceImpl extends AbstractService<Orders, Integer>implements OrdersService {

	@Autowired
	private OrdersMapper orderMapper;

	@Autowired
	private SiteMapper siteMapper;
	
	@Autowired
	private VehicleModelMapper vehicleModelMapper;

	/** 具体子类service的实现需要使用的mapper */
	@Override
	@Autowired
	public void setBaseMapper(BaseMapper<Orders, Integer> baseMapper) {
		super.setBaseMapper(baseMapper);
	}

	@Override
	public PageBean<Orders> select2PageBean(Map<String, Object> map) {
		PageBean<Orders> pageBean = new PageBean<>();
		pageBean.setPageNo(Integer.parseInt(map.get("pageNo").toString()));
		pageBean.setPageSize(Integer.parseInt(map.get("pageSize").toString()));
		// 注意map要先设置pageBean,拦截器里面要获取其值
		map.put("pageBean", pageBean);
		map.put("needPage", true);// 是否分页，默认是false不分页
		pageBean.setResult(orderMapper.selectAll4Page(map));
		return pageBean;
	}

	@Override
	public List<Orders> selectAllOrdersByCtn(Map<String, Object> map) {
		return orderMapper.selectAllOrdersByCtn(map);
	}

	@Override
	@Transactional
	public int deleteById(String ids) {
		String[] idsStr = ids.split(",");
		if (idsStr.length > 0) {
			for (String id : idsStr) {
				orderMapper.deleteById(Integer.parseInt(id));
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int selectMaxId() {
		return orderMapper.selectMaxId();
	}

	@Override
	public Integer queryWlcompanyByOrderId(Map<String, Object> map) {
		List<Orders> orderList = orderMapper.selectAllOrdersByCtn(map);
		List<Site> siteList = siteMapper.selectAllSiteByCtn(null);

		List<Integer> wlcompanyList = new ArrayList<Integer>();
		Orders retOrder = new Orders();
        Integer orderId ;
		for (Orders order : orderList) {
			for (Site site : siteList) {
				if (order.getShaddress().indexOf(site.getName()) != -1) {
					wlcompanyList.add(site.getWlcompany());
					orderId = order.getId();
					retOrder.setId(orderId);
					retOrder.setSubcontractor(String.valueOf(site.getWlcompany()));
				}
			}
		}
		//承运商信息同步到订单表
		orderMapper.updateSelective(retOrder);
		
		return wlcompanyList.get(0);
	}

	@Override
	public List<Integer> queryWlcompanysByOrderId(Map<String, Object> map) {
		List<Orders> orderList = orderMapper.selectAllOrdersByCtn(map);
		Map<String, Object> argsMap = new HashMap<String, Object>();
		List<Site> siteList = siteMapper.selectAllSiteByCtn(argsMap);

		List<Integer> wlcompanyList = new ArrayList<Integer>();

		for (Orders order : orderList) {
			for (Site site : siteList) {
				if (!StringUtils.isEmpty(order.getShaddress()) && !StringUtils.isEmpty(site.getName())
						&& null != site.getWlcompany() && order.getShaddress().indexOf(site.getName()) != -1) {
					wlcompanyList.add(site.getWlcompany());
				}
			}
		}

		return wlcompanyList;
	}

	/**
	 * 第一次匹配 生成运单
	 * @return
	 */
	@Override
	public int generateWaybill(Map<String, Object> map){
		Cpmd cpmd = new Cpmd();
//		argsMap.put("updatetime", cpmd.getUpdatetime());
//		List<Cpmd> cpmdList = cpmdService.selectAllCpmdByCtn(argsMap);
//		for(){
//			
//		}
		int code = 1;
		Integer waybill = 0;
		Integer wlcompany = this.queryWlcompanyByOrderId(map);
		Integer id = Integer.parseInt(String.valueOf(map.get("id")));
		
		Orders order = orderMapper.selectById(id);
		//判断订单能否找到承运商
		if(null != wlcompany && 0 != wlcompany){
			//判断能否匹配上车型
			Map<String, Object> argsMap = new HashMap<String, Object>();
			argsMap.put("wlcompany", wlcompany);
			List<VehicleModel> vehicleModelList = vehicleModelMapper.selectAllVehicleModelByCtn(argsMap);
			for (VehicleModel vehicleModel : vehicleModelList) {
                //体积匹配不小于20%
				if((8 < (order.getVolume()/vehicleModel.getVolum())*10) && (order.getVolume()/vehicleModel.getVolum())*10 < 10){
                	
                }else{
                	continue;
                }
				
				//重量匹配不小于20%
				if((8 < (order.getWeight()/vehicleModel.getWeight())*10) && ((order.getWeight()/vehicleModel.getWeight())*10 < 10)){
					code = 0;
					//TODO 生成运单 运单下发
//					waybill = 
					break;
				}else{
					continue;
				}
			}
		}
		
		return code;
	}

}