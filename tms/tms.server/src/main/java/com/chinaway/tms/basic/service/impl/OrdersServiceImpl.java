package com.chinaway.tms.basic.service.impl;

import java.util.ArrayList;
import java.util.Date;
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
import com.chinaway.tms.basic.dao.WaybillMapper;
import com.chinaway.tms.basic.model.Cpmd;
import com.chinaway.tms.basic.model.OrderItem;
import com.chinaway.tms.basic.model.Orders;
import com.chinaway.tms.basic.model.OrdersWaybill;
import com.chinaway.tms.basic.model.Site;
import com.chinaway.tms.basic.model.VehicleModel;
import com.chinaway.tms.basic.model.Waybill;
import com.chinaway.tms.basic.service.CpmdService;
import com.chinaway.tms.basic.service.OrdersService;
import com.chinaway.tms.basic.service.OrdersWaybillService;
import com.chinaway.tms.basic.service.WaybillService;
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
	
	@Autowired
	private WaybillMapper waybillMapper;
	
	@Autowired
	private WaybillService waybillService;
	
	@Autowired
	private OrdersWaybillService ordersWaybillService;
	
	@Autowired
	private CpmdService cpmdService;

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
		map.put("state", "0");
		map.put("status", "0");
		List<Orders> orderList = orderMapper.selectAllOrdersByCtn(map);
		List<Site> siteList = siteMapper.selectAllSiteByCtn(null);

		List<Integer> wlcompanyList = new ArrayList<Integer>();
		Orders retOrder = new Orders();
		Integer orderId;
		for (Orders order : orderList) {
			for (Site site : siteList) {
				if (order.getShaddress().indexOf(site.getName()) != -1) {
					wlcompanyList.add(site.getWlcompany());
					orderId = order.getId();
					retOrder.setId(orderId);
					retOrder.setSubcontractor(String.valueOf(site.getWlcompany()));
					map.put("id", orderId);
					// 承运商信息同步到订单表
					orderMapper.updateSelective(retOrder);
				}
			}
		}

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
	
	public int moreGenerateWaybill(){
		return 0;
	}

	/**
	 * 第一次匹配 生成运单
	 * @return
	 */
	@Override
	@Transactional
	public List<String> generateWaybill(Orders order){
//		Cpmd cpmd = new Cpmd();
//		argsMap.put("updatetime", cpmd.getUpdatetime());
//		List<Cpmd> cpmdList = cpmdService.selectAllCpmdByCtn(argsMap);
//		for(){
//			
//		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", order.getId());
		Integer wlcompany = this.queryWlcompanyByOrderId(map);
		List<String> waybills = new ArrayList<String>();
		
		//判断订单能否找到承运商
		if(null != wlcompany && 0 != wlcompany){
			//判断能否匹配上车型
			Map<String, Object> argsMap = new HashMap<String, Object>();
			argsMap.put("wlcompany", wlcompany);
			
			try{
				//查询运单匹配上的车辆装载
				List<VehicleModel> vehicleModelList = vehicleModelMapper.selectAllVehicleModelByCtn(argsMap);
				for (VehicleModel vehicleModel : vehicleModelList) {
					
					System.out.println("------------订单体积：" + order.getVolume());
					System.out.println("------------车型体积：" + vehicleModel.getVolum());
					System.out.println("------------订单除以车型：" + order.getVolume()/vehicleModel.getVolum());
	                //体积匹配不小于20%
					if (machVehMod(order.getVolume(), vehicleModel.getVolum())) {
	                	
	                }else{
	                	continue;
	                }
					
					// 重量匹配不小于20%
					if (machVehMod(order.getWeight(), vehicleModel.getWeight())) {
						Waybill waybill = this.setWaybill(order, vehicleModel);
						waybillMapper.insert(waybill);
						waybills.add(waybill.getCode());
						this.insertWaybillOrders(order, waybill.getId(), vehicleModel.getWlcompany());
						break;
					} else {
						continue;
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		return waybills;
	}
	
	public void insertWaybillOrders(Orders order, Integer waybillid, String wlcompany) {
		// 判断运单生成成功，修改订单状态我1 已生成运单
		order.setState("1");
		order.setSubcontractor(wlcompany);
		// 修改订单状态
		int retOrdCode = orderMapper.updateSelective(order);
		if (retOrdCode > 0) {
			OrdersWaybill ordersWaybill = new OrdersWaybill();
			ordersWaybill.setOrdersid(order.getId());
			ordersWaybill.setWaybillid(waybillid);
			ordersWaybillService.insert(ordersWaybill);
		}
	}
	
	/**
	 * 匹配车型的体积和质量 是否在订单的体积和质量20%内
	 * @param orderParam
	 * @param vehModParam
	 * @return
	 */
	private boolean machVehMod(double orderParam, double vehModParam) {
		String mach = String.valueOf((orderParam / vehModParam) * 100);
		mach = mach.substring(0, mach.indexOf("."));
		if (80 < Integer.parseInt(mach) && Integer.parseInt(mach) < 100) {
			return true;
		}
		return false;
	}
	
	public Waybill setWaybill(Orders order,Waybill record) throws Exception {
		int maxId = waybillService.selectMaxId();
		record.setAmount(order.getAmount());
		record.setCode("tms" + maxId );
		record.setDeptname(order.getDeptname());
		record.setExceptcount(order.getExceptcount());
		record.setFhaddress(order.getFhaddress());
		record.setFromcode(order.getFromcode());
		record.setOrderfrom(order.getOrderfrom());
		record.setRequendtime(order.getRequendtime());
		record.setRequstarttime(order.getRequstarttime());
		record.setShaddress(order.getShaddress());
		record.setState("0");// 阶段初始为 0
		record.setSubcontractor(record.getSubcontractor());
		record.setUnit(order.getUnit());
		record.setVolume(order.getVolume());
		record.setWeight(order.getWeight());
		record.setCreatetime(new Date());
		return record;
	}
	
	public Waybill setWaybill(Orders order, VehicleModel vehicleModel) throws Exception {
		Waybill waybill = new Waybill();
		int maxId = waybillService.selectMaxId();
		waybill.setAmount(order.getAmount());
		waybill.setC_volume(vehicleModel.getVolum());
		waybill.setC_weight(vehicleModel.getWeight());
		waybill.setCode("tms" + maxId );
		waybill.setDeptname(order.getDeptname());
		waybill.setExceptcount(order.getExceptcount());
		waybill.setFhaddress(order.getFhaddress());
		waybill.setFromcode(order.getFromcode());
		waybill.setOrderfrom(order.getOrderfrom());
		waybill.setRequendtime(order.getRequendtime());
		waybill.setRequstarttime(order.getRequstarttime());
		waybill.setShaddress(order.getShaddress());
		waybill.setState("0");// 阶段初始为 0
		waybill.setSubcontractor(vehicleModel.getWlcompany());
		waybill.setUnit(order.getUnit());
		waybill.setVolume(order.getVolume());
		waybill.setWeight(order.getWeight());
		waybill.setCreatetime(new Date());
		return waybill;
	}

	@Override
	public List<Orders> selectByIds(String ids) {
		String[] idsArray = ids.split(",");
		return orderMapper.selectByIds(idsArray);
	}

	@Override
	public Orders selectDetailById(Integer id) {
		Orders orders = orderMapper.selectById(id);
		List<Cpmd> cpmdList = cpmdService.selectCpmdByOrdersId(id);
		OrderItem orderItem = new OrderItem();
		orderItem.setGoods(cpmdList);
		orders.setBaseInfo(orderItem);
		orders.getDispatchInfos();
		orders.getSteps();
		return orders;
	}
	
}