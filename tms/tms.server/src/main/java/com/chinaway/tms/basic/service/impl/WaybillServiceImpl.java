package com.chinaway.tms.basic.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinaway.tms.basic.dao.WaybillMapper;
import com.chinaway.tms.basic.model.OrderItem;
import com.chinaway.tms.basic.model.Orders;
import com.chinaway.tms.basic.model.OrdersWaybill;
import com.chinaway.tms.basic.model.Waybill;
import com.chinaway.tms.basic.service.CpmdService;
import com.chinaway.tms.basic.service.OrderItemService;
import com.chinaway.tms.basic.service.OrdersService;
import com.chinaway.tms.basic.service.OrdersWaybillService;
import com.chinaway.tms.basic.service.WaybillService;
import com.chinaway.tms.basic.vo.GoodsVo;
import com.chinaway.tms.basic.vo.OrderVo;
import com.chinaway.tms.core.AbstractService;
import com.chinaway.tms.core.BaseMapper;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.utils.page.PageBean;
import com.chinaway.tms.ws.service.PushService;

@Service
public class WaybillServiceImpl extends AbstractService<Waybill, Integer> implements WaybillService {
	
	private String orgcode = "200UHN";
	
	@Autowired
	private WaybillMapper waybillMapper;
	@Autowired
	private OrdersService ordersService;
	@Autowired
	private OrdersWaybillService ordersWaybillService;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private PushService pushService;
	
	/**具体子类service的实现需要使用的mapper*/
	@Override
	@Autowired
	public void setBaseMapper(BaseMapper<Waybill, Integer> baseMapper) {
		super.setBaseMapper(baseMapper);
	}

	@Override
	public PageBean<Waybill> select2PageBean(Map<String, Object> map) {
		PageBean<Waybill> pageBean = new PageBean<>();
		pageBean.setPageNo(Integer.parseInt(map.get("pageNo").toString()));
		pageBean.setPageSize(Integer.parseInt(map.get("pageSize").toString()));
		//注意map要先设置pageBean,拦截器里面要获取其值
		map.put("pageBean", pageBean);
		map.put("needPage", true);//是否分页，默认是false不分页
		pageBean.setResult(waybillMapper.selectAll4Page(map));
		return pageBean;
	}
	
	@Override
	public List<Waybill> selectAllTckNumByCtn(Map<String, Object> argsMap) {
		return waybillMapper.selectAllTckNumByCtn(argsMap);
	}
	
	@Override
	public List<Waybill> selectAllTckNumRvwedByCtn(Map<String, Object> argsMap) {
		return waybillMapper.selectAllTckNumRvwedByCtn(argsMap);
	}
	
	public List<Waybill> selectByIds(String ids){
		String[] idsArray = ids.split(",");
		return waybillMapper.selectByIds(idsArray);
	}
	
	@Override
	@Transactional
	public int deleteById(String ids) {
		String[] idsStr = ids.split(",");
		if (idsStr.length > 0) {
			for (String id : idsStr) {
				waybillMapper.deleteById(Integer.parseInt(id));
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	@Transactional
	public Integer insertWaybill(Waybill waybill , List<Orders> ordersList) throws Exception {
		 int ret = waybillMapper.insert(waybill);
		 
		 for(Orders orders: ordersList){
			 ordersService.insertWaybillOrders(orders, waybill.getId(), waybill.getWlcompany());
		 }
		 //推送订单、运单信息给g7
		 pushOrderAndDep(waybill, "1");//表示手动
		 //只推送来源于wms的订单
//		 if("wms".equalsIgnoreCase(ordersList.get(0).getOrderfrom())){
//			 pushService.dep2wmsWS(waybill, ordersList);
//		 }
		 
		 return ret;
	}

	@Override
	@Transactional
	public int updateWaybill(Waybill waybill) throws Exception {
		int retCode = waybillMapper.updateSelective(waybill);
		
//		Map<String, Object> orderMap = new HashMap<String, Object>();
//		orderMap.put("waybillid", waybill.getId());
		
		List<Orders> orderList = ordersService.selectByWayId(waybill.getId());
		
		//审核通过（推送订单、运单信息）
		if("1".equals(waybill.getState())){
			//订单下发
			for (Orders orders : orderList) {
				orders.setState("1");
				ordersService.updateSelective(orders);
			}
			
			pushOrderAndDep(waybill, "0");//表示自动
//			pushService.dep2wmsWS(waybill, orderList);
			
		}else if("-1".equals(waybill.getState())){//审核不通过
			//还原订单
			for (Orders orders : orderList) {
				orders.setState("0");
				ordersService.updateSelective(orders);
			}
			//删除中间表
			Map<String, Object> argsmap = new HashMap<String, Object>();
			argsmap.put("waybillid", waybill.getId());
			ordersWaybillService.deleteByCtn(argsmap);
		}
	
		return retCode;
	}

	@Override
	public int selectMaxId() {
		return waybillMapper.selectMaxId();
	}

	/**
	 * 推送车次（运单）和订单信息给承运商
	 * @param waybill 运单
	 * @throws Exception
	 */
	private void pushOrderAndDep(Waybill waybill, String status) throws Exception {
		//暂时未加一个方法直接根据waybillid 查询List<Orders>
		List<String> orderCodeList = new ArrayList<>();//订单code List给新增运单时使用
		List<OrderVo> ordersList = new ArrayList<>();
		OrderVo orderVo = null;
		
		Map<String,Object> map = new HashMap<>();
		map.put("waybillid", waybill.getId());
		List<OrdersWaybill> list = ordersWaybillService.selectAll4Page(map);
		for (OrdersWaybill ordersWaybill : list) {
			int orderId = ordersWaybill.getOrdersid();
			Orders orders = ordersService.selectById(orderId);
			orders.setState("1");
			orders.setStatus(status);
			//修改订单状态
			ordersService.updateSelective(orders);
			//将订单编号放入运单需要的List
			orderCodeList.add(orders.getCode());
			
			orderVo = setOrderVo(orders);
			ordersList.add(orderVo);
		}
		
		//获得推送参数
		String orderParam = getOrderParam(ordersList);
		String depParam = getDepParam(waybill, orderCodeList);
		
		boolean isSuccess = pushService.addOrderAndDep(orderParam, depParam);
		if(isSuccess){
			System.out.println("推送成功");
		}else{
			System.out.println("推送失败");
		}
	}

	/**
	 * 将订单翻译为推送订单信息
	 * 
	 * @param orders	原订单信息
	 * @return
	 */
	private OrderVo setOrderVo(Orders orders) {
//		OrderVo orderVo;
		GoodsVo goodsVo;
		List<OrderItem> orderItemList;
		List<GoodsVo> goods = new ArrayList<>();

		//将tms的订单复制给g7的订单（参数名不同）
		OrderVo orderVo = new OrderVo();
		orderVo.setOrderno(orders.getCode());
		orderVo.setWmsno(orders.getFromcode());
		orderVo.setSlocation(orders.getShaddress());
		orderVo.setRlocation(orders.getFhaddress());
//		orderVo.setSsitename(orders.getShaddress());
//		orderVo.setRsitename(orders.getFhaddress());
		//站点现在是地址
//		orderVo.setSsitename(orders.getShsitename());
//		orderVo.setRsitename(orders.getFhsitename());
		orderVo.setSdatetime(orders.getRequstarttime().toLocaleString());
		if(orders.getRequendtime() != null){
			orderVo.setRdatetime(orders.getRequendtime().toLocaleString());
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("orderid", orders.getId());//共用了外部的map
		orderItemList = orderItemService.selectAll4Page(map);
		for (OrderItem orderItem : orderItemList) {
			//订单明细表没有 商品的具体信息，只有商品编号,vo类 可存放
			goodsVo = new GoodsVo();
			goodsVo.setGoodsname(orderItem.getGoodsname());
			goodsVo.setSku(orderItem.getGoodscode());
			goodsVo.setNumber(orderItem.getNumber());
			goodsVo.setVolume(orderItem.getVolume());
			goodsVo.setWeight(orderItem.getWeight());
			goodsVo.setUnit(orderItem.getUnit());
			goods.add(goodsVo);
		}
//		cpmdList = cpmdService.selectCpmdByOrdersId(orderId);
//		for (Cpmd cpmd : cpmdList) {
//			goodsVo.setGoodsname(cpmd.getMaktx());
//			goodsVo.setNumber(cpmd.getMaktx());
//		}
		orderVo.setGoods(goods);
		return orderVo;
	}

	/**
	 * 获得订单推送参数
	 * @param ordersList
	 * @return
	 */
	private String getOrderParam(List<OrderVo> ordersList) {
		Map<String,Object> orderParamMap = new HashMap<>();
		orderParamMap.put("orgcode", orgcode);
		orderParamMap.put("repeatway", 1); //0 重复报错,1覆盖,2 忽略按成功返回
		orderParamMap.put("unification", 0);
		orderParamMap.put("orders", ordersList);
		String orderParam = JsonUtil.obj2JsonStr(orderParamMap);
		System.out.println("orderParamMap:"+orderParamMap);
//		    	String orderParam ="{\"orgcode\": \"20016C\",\"repeatway\": 1,\"unification\": 0,\"orders\": [{"
//		    			+ "\"orderno\" : \"tms2016090601\","
//						+ "\"wmsno\" : \"wxg2016051901\","
//						+ "\"userorderno\" : \"BSORDER001\", "
//						+ "\"begintime\" : \"2016-05-19 12:00:00\","
//						+ "\"scompany\" : \"天下汇通\","
//						+ "\"sprovince\" : \"北京市\","
//						+ "\"scity\"  : \"北京\","
//						+ "\"sdistricts\" : \"海淀区\","
//						+ "\"slocation\" : \"北京市海淀区农大南路1号\","
//						+ "\"sname\" : \"王新刚\", "
//						+ "\"sphone\" : \"18701432591\","
//						+ "\"sdatetime\" : \"2016-05-20 12:00:00\","
//						+ "\"ssitename\" : \"北京站\","
//					    + "\"rcompany\" : \"天下汇通\","
//						+ "\"rprovince\" : \"上海市\", "
//						+ "\"rcity\"  : \"上海\", "
//						+ "\"rdistricts\" : \"闵行区\", "
//						+ "\"rlocation\" : \"上海市闵行区七宝古镇1号\", "
//						+ "\"rname\" : \"新刚王\","
//						+ "\"rphone\" : \"17701432591\", "
//						+ "\"rdatetime\" : \"2016-05-21 12:00:00\","
//						+ "\"rsitename\"    : \"上海站\","
//			
//					    + " \"goods\":[{"
//					    		+ "\"goodsname\": \"肥皂\","
//					                + "\"sku\": \"10001\","
//					                + "\"unit\": \"盒\","
//					               + " \"number\": 11,"
//					                + "\"weight\": 11,"
//					                + "\"volume\": 11"
//					          + "  }] "
//					      + "}]"
//				+ "}";
		return orderParam;
	}

	/**
	 * 获得运单推送参数
	 * @param waybill
	 * @param orderCodeList
	 * @return
	 */
	private String getDepParam(Waybill waybill, List<String> orderCodeList) {
		Map<String,Object> depParamMap = new HashMap<>();
		depParamMap.put("orgcode", orgcode);
		depParamMap.put("orders", orderCodeList);
		depParamMap.put("departureno", waybill.getCode());
		//由于只匹配车型没匹配车辆，所以设定的一些时间都作用
//		    	depParamMap.put("begintime", "2016-07-10 17:01:00");//车次创建时间，
//		    	depParamMap.put("starttime", "2016-09-09 17:01:00");//计划发车时间
//		    	depParamMap.put("endtime", "2016-09-10 17:01:00");//计划到达时间
		depParamMap.put("type", "1");
		depParamMap.put("classlinemode", "0");
		depParamMap.put("zptmode", "1");
		depParamMap.put("issuetogether", "0"); // 0 有一个成功算成功 1，全部必须下发成功    
		depParamMap.put("issued", "0");
		
		String depParam = JsonUtil.obj2JsonStr(depParamMap);
		System.out.println("depParamMap:"+depParamMap);
//		    	String depParam ="{\"orgcode\": \"20016C\","
//		    			+ " \"orders\" : [\"tms2016090601\"], "
//		    			+ "\"departureno\" : \"D2016090601\", "
////		    			+ "\"begintime\" : \"2016-07-10 17:01:00\","
////		    			+ "\"carriagetype\" : \"3.5M箱式\", "
////		    			
//		    			+ " \"type\":\"1\",\"classlinemode\":\"0\",\"zptmode\":\"1\", "
//		    			+ " \"issuetogether\":\"0\",\"issued\":\"0\" "
////		    			+ "\"starttime\" : \"2016-07-19 17:19:49\","
////		    			+ "\"endtime\" : \"2016-07-21 17:19:49\" "
//		    		+ "}";
		return depParam;
	}

	@Override
	public Date selectMaxUpdateTime() {
		
		return waybillMapper.selectMaxUpdateTime();
	}
}