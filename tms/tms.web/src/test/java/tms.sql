/*
Navicat MySQL Data Transfer

Source Server         : tms
Source Server Version : 50639
Source Host           : localhost:3306
Source Database       : tms

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2018-03-15 18:19:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for area
-- ----------------------------
DROP TABLE IF EXISTS `area`;
CREATE TABLE `area` (
  `id` int(50) NOT NULL AUTO_INCREMENT COMMENT '主建',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `code` varchar(100) DEFAULT NULL COMMENT '区域站点编号',
  `wlcompany` varchar(100) DEFAULT NULL COMMENT '承运商name',
  `deptname` varchar(255) DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of area
-- ----------------------------
INSERT INTO `area` VALUES ('1', '四川成都', '1', '1', '1', '2018-03-14 21:28:08');
INSERT INTO `area` VALUES ('2', '湖北武汉', '2', '2', '2', '2018-03-15 15:48:22');

-- ----------------------------
-- Table structure for area_site
-- ----------------------------
DROP TABLE IF EXISTS `area_site`;
CREATE TABLE `area_site` (
  `id` int(50) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sitecode` varchar(100) DEFAULT NULL COMMENT '站点编码',
  `areacode` varchar(100) DEFAULT NULL COMMENT '站点区域编码',
  `sitename` varchar(100) DEFAULT NULL COMMENT '站点名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of area_site
-- ----------------------------
INSERT INTO `area_site` VALUES ('1', '01', '2', '四川成都天府四街');
INSERT INTO `area_site` VALUES ('2', '03', '1', '湖北武汉雄楚大道');
INSERT INTO `area_site` VALUES ('3', '03', '1', '湖北武汉关山大道');
INSERT INTO `area_site` VALUES ('4', '02', '2', '四川成都天府三街');
INSERT INTO `area_site` VALUES ('5', '08', '1', null);

-- ----------------------------
-- Table structure for car
-- ----------------------------
DROP TABLE IF EXISTS `car`;
CREATE TABLE `car` (
  `id` int(50) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) DEFAULT NULL COMMENT '车辆名称',
  `code` varchar(100) DEFAULT NULL COMMENT '车辆编码',
  `weight` varchar(100) DEFAULT NULL COMMENT '额度重量',
  `volum` varchar(100) DEFAULT NULL COMMENT '额度体积',
  `carno` int(50) DEFAULT NULL COMMENT '车牌号',
  `wlcompany` varchar(100) DEFAULT NULL COMMENT '承运商id',
  `vehicleModelName` varchar(200) DEFAULT NULL COMMENT '车型名称  车系名称+年代款+排量+变速箱类型/驱动方式+型号名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of car
-- ----------------------------
INSERT INTO `car` VALUES ('1', '1', '1', '10.0', '30.0', '1', '1', '1');
INSERT INTO `car` VALUES ('2', '2', '2', '20.0', '200.0', '2', '2', '22222');
INSERT INTO `car` VALUES ('3', '3', '3', '30.0', '300.0', '3', '3', '33333');

-- ----------------------------
-- Table structure for cpmd
-- ----------------------------
DROP TABLE IF EXISTS `cpmd`;
CREATE TABLE `cpmd` (
  `id` int(50) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `matnr` varchar(100) DEFAULT NULL COMMENT '商品编号',
  `maktx` varchar(100) DEFAULT NULL COMMENT '商品名称',
  `meins` varchar(100) DEFAULT NULL COMMENT '基本单位',
  `brgew` varchar(100) DEFAULT NULL COMMENT '毛重',
  `ntgew` varchar(100) DEFAULT NULL COMMENT '净重',
  `gewei` varchar(100) DEFAULT NULL COMMENT '重量单位',
  `volum` varchar(100) DEFAULT NULL COMMENT '体积',
  `voleh` varchar(100) DEFAULT NULL COMMENT '体积单位',
  `groes` varchar(100) DEFAULT NULL COMMENT '规格',
  `ean11` varchar(100) DEFAULT NULL COMMENT '商品主条码',
  `bclass` varchar(100) DEFAULT NULL COMMENT '商品大类编码',
  `swor` varchar(100) DEFAULT NULL COMMENT '商品大类描述',
  `sclass` varchar(100) DEFAULT NULL COMMENT '商品小类编码',
  `mhdhb` varchar(100) DEFAULT NULL COMMENT '保质期',
  `zzpsms` varchar(100) DEFAULT NULL COMMENT '配送模式',
  `zzspbm` varchar(100) DEFAULT NULL COMMENT '商品部门',
  `zzspsx` varchar(100) DEFAULT NULL COMMENT '商品属性',
  `zzlsdj` varchar(100) DEFAULT NULL COMMENT '零售价定价模式',
  `zzhydj` varchar(100) DEFAULT NULL COMMENT '会员价定价模式',
  `zzmlfs` varchar(100) DEFAULT NULL COMMENT '商品毛利份数',
  `zzsdbl` varchar(100) DEFAULT NULL COMMENT '市调价计算百分比',
  `bstrf` varchar(100) DEFAULT NULL COMMENT '最小补货数量',
  `zzbrand` varchar(100) DEFAULT NULL COMMENT '品牌类别',
  `updatetime` datetime DEFAULT NULL COMMENT '最后更新时间',
  `amount` varchar(100) DEFAULT NULL COMMENT '数量',
  `orderid` int(50) DEFAULT NULL COMMENT '订单id',
  `fromcode` varchar(100) DEFAULT NULL COMMENT '订单来源编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cpmd
-- ----------------------------
INSERT INTO `cpmd` VALUES ('1', '1', '1', '1', '1', '1', '10.0', '100.0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2018-03-14 21:27:27', '1', '1', '1');

-- ----------------------------
-- Table structure for line
-- ----------------------------
DROP TABLE IF EXISTS `line`;
CREATE TABLE `line` (
  `id` int(11) NOT NULL COMMENT '主键',
  `pdeptname` varchar(200) DEFAULT NULL COMMENT '所属顶级机构',
  `deptname` varchar(200) DEFAULT NULL COMMENT '所属机构',
  `code` varchar(200) DEFAULT NULL COMMENT '班线编码',
  `name` varchar(200) DEFAULT NULL COMMENT '班线名称',
  `startsite` varchar(200) DEFAULT NULL COMMENT '首站点',
  `endsite` varchar(200) DEFAULT NULL COMMENT '到达站点',
  `halfwaysite` varchar(200) DEFAULT NULL COMMENT '途经点',
  `runtime` varchar(100) DEFAULT NULL COMMENT '运行时间(分钟)',
  `mileage` varchar(200) DEFAULT NULL COMMENT '计划总里程(公里)',
  `vehicleline` varchar(200) DEFAULT NULL COMMENT '绑定行驶线路',
  `updatetime` datetime DEFAULT NULL COMMENT '修改时间',
  `fromcode` varchar(200) DEFAULT NULL COMMENT '班线来源机构编码',
  `linetype` varchar(200) DEFAULT NULL COMMENT '班线类型',
  `transtype` varchar(200) DEFAULT NULL COMMENT '运费类型',
  `transmoney` varchar(200) DEFAULT NULL COMMENT '运费',
  `operates` varchar(200) DEFAULT NULL COMMENT '操作记录',
  `roadtoll` varchar(200) DEFAULT NULL COMMENT '过路费(元)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of line
-- ----------------------------
INSERT INTO `line` VALUES ('1', '物流中心', '分拨站', '01', '旅游班线', '01', '08', '02', '30', '1146km', '最近线路', '2018-03-14 21:26:52', '03', '1', '1', '750', '修改线路成功', '300');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` int(50) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(100) DEFAULT NULL COMMENT '订单编号',
  `fromcode` varchar(100) DEFAULT NULL COMMENT '来源编号',
  `orderfrom` varchar(100) DEFAULT NULL COMMENT '订单来源：sap，wms',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `deptname` varchar(100) DEFAULT NULL COMMENT '所属机构',
  `subcontractor` varchar(100) DEFAULT NULL COMMENT '转包承运商',
  `amount` varchar(100) DEFAULT NULL COMMENT '货品数量',
  `unit` varchar(100) DEFAULT NULL COMMENT '货品单位',
  `weight` varchar(100) DEFAULT NULL COMMENT '重量',
  `volume` varchar(100) DEFAULT NULL COMMENT '货品总体积(m³)',
  `fhaddress` varchar(100) DEFAULT NULL COMMENT '发货地址',
  `shaddress` varchar(100) DEFAULT NULL COMMENT '收货地址',
  `requstarttime` datetime DEFAULT NULL COMMENT '要求发货时间',
  `requendtime` datetime DEFAULT NULL COMMENT '要求收货时间',
  `state` varchar(100) DEFAULT NULL COMMENT '当前执行阶段',
  `exceptcount` varchar(100) DEFAULT NULL COMMENT '异常事件数',
  `status` varchar(100) DEFAULT NULL COMMENT '状态 0自动  1手动',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `pid` int(11) DEFAULT NULL COMMENT '父id',
  `areacode` varchar(100) DEFAULT NULL COMMENT '区域编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES ('1', '1', '1', '1', '2018-03-14 21:28:19', '1', '1', '1', '1', '10', '100', '02', '01', '2018-03-14 21:28:33', '2018-03-14 21:28:38', '0', '1', '1', '2018-03-14 21:28:44', '1', '2');

-- ----------------------------
-- Table structure for orders_waybill
-- ----------------------------
DROP TABLE IF EXISTS `orders_waybill`;
CREATE TABLE `orders_waybill` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ordersid` int(11) DEFAULT NULL COMMENT '订单id',
  `waybillid` int(11) DEFAULT NULL COMMENT '运单id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of orders_waybill
-- ----------------------------
INSERT INTO `orders_waybill` VALUES ('1', '1', '1');

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
  `id` int(50) DEFAULT NULL COMMENT '主键',
  `orderid` int(100) DEFAULT NULL COMMENT '订单编号',
  `goodscode` varchar(200) DEFAULT NULL COMMENT '商品编号',
  `goodsname` varchar(200) DEFAULT NULL COMMENT '商品名称',
  `number` int(50) DEFAULT NULL COMMENT '总件数',
  `weight` varchar(100) DEFAULT NULL COMMENT '货品总重(Kg)',
  `volume` varchar(100) DEFAULT NULL COMMENT '货品总体积(m3)',
  `unit` varchar(200) DEFAULT NULL COMMENT '货品数量单位'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES ('1', '1', '1', '1', '1', '10', '100', '1');

-- ----------------------------
-- Table structure for site
-- ----------------------------
DROP TABLE IF EXISTS `site`;
CREATE TABLE `site` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(200) DEFAULT NULL COMMENT 'name',
  `code` varchar(100) DEFAULT NULL COMMENT '站点编码',
  `area` varchar(200) DEFAULT NULL COMMENT '范围',
  `deptname` varchar(100) DEFAULT NULL COMMENT '所属机构',
  `types` varchar(100) DEFAULT NULL COMMENT '站点类型',
  `isprivate` varchar(100) DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `province` varchar(100) DEFAULT NULL COMMENT '省份',
  `city` varchar(100) DEFAULT NULL COMMENT '城市',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `createtime` datetime DEFAULT NULL COMMENT '注册时间',
  `wlcompany` varchar(100) DEFAULT NULL COMMENT '承运商id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of site
-- ----------------------------
INSERT INTO `site` VALUES ('1', '天府四街仓库', '02', '500', '1919酒类直供', '标点', '私有', '2018-03-14 22:17:15', '四川省', '成都', '四川省成都市双流区天府四街软件园', '2016-08-02 17:28:21', '1');
INSERT INTO `site` VALUES ('2', '武汉三环洪山加油站', '01', '500', '1', '1', '私有', '2018-03-14 21:40:58', '湖北省', '武汉市', '湖北省武汉市洪山区', '2018-03-14 21:36:53', '2');

-- ----------------------------
-- Table structure for smd
-- ----------------------------
DROP TABLE IF EXISTS `smd`;
CREATE TABLE `smd` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `werks` varchar(100) DEFAULT NULL COMMENT '门店编码',
  `ze58sttp` varchar(100) DEFAULT NULL COMMENT '门店类型',
  `ze58rp01` varchar(100) DEFAULT NULL COMMENT '门店级别',
  `loclb` varchar(100) DEFAULT NULL COMMENT '省仓编码',
  `name1` varchar(100) DEFAULT NULL COMMENT '门店名称',
  `ze58longi` varchar(50) DEFAULT NULL COMMENT '经度',
  `ze58lati` varchar(50) DEFAULT NULL COMMENT '纬度',
  `stras` varchar(200) DEFAULT NULL COMMENT '地址',
  `tel_number` int(11) DEFAULT NULL COMMENT '电话',
  `pstlz` varchar(50) DEFAULT NULL COMMENT '邮编',
  `ze58hus22` varchar(100) DEFAULT NULL COMMENT '房东姓名',
  `ze58hus23` varchar(50) DEFAULT NULL COMMENT '房东电话',
  `tel_number1` int(11) DEFAULT NULL COMMENT '物管联系电话',
  `ze58huf02` varchar(50) DEFAULT NULL COMMENT '是否可开增票',
  `ze58scyn` varchar(50) DEFAULT NULL COMMENT '是否关店',
  `zclass` varchar(100) DEFAULT NULL COMMENT '区域',
  `region` varchar(100) DEFAULT NULL COMMENT '省编码',
  `bezei_1` varchar(100) DEFAULT NULL COMMENT '省名称',
  `cityc` varchar(100) DEFAULT NULL COMMENT '市编码',
  `bezei_2` varchar(100) DEFAULT NULL COMMENT '市名称',
  `lzone` varchar(100) DEFAULT NULL COMMENT '区/县编码',
  `vtext` varchar(100) DEFAULT NULL COMMENT '区/县名称',
  `updatetime` datetime DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of smd
-- ----------------------------
INSERT INTO `smd` VALUES ('1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2018-03-14 21:29:17');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` int(50) NOT NULL AUTO_INCREMENT,
  `deptid` varchar(100) DEFAULT NULL COMMENT '部门id ',
  `name` varchar(100) DEFAULT NULL COMMENT '部门名称',
  `pid` varchar(100) DEFAULT NULL COMMENT '父id',
  `levels` varchar(100) DEFAULT NULL COMMENT '层级',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `sotid` int(50) DEFAULT NULL COMMENT '排序号',
  `state` varchar(100) DEFAULT NULL COMMENT '节点状态，open 或 closed',
  `isenable` varchar(100) DEFAULT NULL COMMENT '是否可用状态',
  `customerid` varchar(100) DEFAULT NULL COMMENT '自定义机构编号',
  `contact` varchar(100) DEFAULT NULL COMMENT '联系人',
  `address` varchar(100) DEFAULT NULL COMMENT '地址',
  `tel` varchar(100) DEFAULT NULL COMMENT '电话',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('1', '1', '软件一部', '0', '1', '2018-03-14 21:29:26', '1', '1', '1', '1', '01', '章先生', '软件园一路', '111111', '1');
INSERT INTO `sys_dept` VALUES ('3', '2', '软件二部', '1', '2', '2018-03-14 23:10:05', '1', '2', '1', '1', '2', '2', '2', '2', '1');
INSERT INTO `sys_dept` VALUES ('4', '3', '软件三部', '2', '3', '2018-03-14 23:10:09', '1', '3', '1', '1', '3', '章先生', '3', '3', '1');

-- ----------------------------
-- Table structure for sys_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `sys_dictionary`;
CREATE TABLE `sys_dictionary` (
  `id` int(50) DEFAULT NULL COMMENT '主键',
  `pid` int(50) DEFAULT NULL COMMENT '父id',
  `name` varchar(100) DEFAULT NULL COMMENT '文字描述',
  `value` varchar(100) DEFAULT NULL COMMENT '具体值',
  `descs` varchar(400) DEFAULT NULL COMMENT '描述'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_dictionary
-- ----------------------------
INSERT INTO `sys_dictionary` VALUES ('1', '1', '1', '1', '1');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` int(50) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `loginname` varchar(255) DEFAULT NULL COMMENT '登录名',
  `rolename` varchar(255) DEFAULT NULL COMMENT '角色名',
  `content` varchar(255) DEFAULT NULL COMMENT '内容',
  `clientip` varchar(255) DEFAULT NULL COMMENT '客户端ip',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES ('1', '1', '1', '1', '1', '2018-03-14 21:29:46');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` int(50) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL COMMENT '菜单名称',
  `pid` int(50) DEFAULT NULL COMMENT '父id',
  `levels` varchar(50) DEFAULT NULL COMMENT '层级',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `requesturl` varchar(200) DEFAULT NULL COMMENT '请求路径',
  `menutype` varchar(100) DEFAULT NULL COMMENT '菜单类型',
  `sotid` int(11) DEFAULT NULL COMMENT '排序号',
  `clazz` varchar(50) DEFAULT NULL COMMENT '样式',
  `title` varchar(200) DEFAULT NULL COMMENT '标题',
  `img` varchar(100) DEFAULT NULL COMMENT '图片',
  `type` varchar(50) DEFAULT NULL COMMENT '资源类型',
  `subsystem` varchar(200) DEFAULT NULL COMMENT '连接地址',
  `target` varchar(50) DEFAULT NULL COMMENT '打开窗口',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '站点管理', '0', '1', '2018-03-13 19:08:34', '/tms/basic/site_list.html?page=1&rows=10', 'menu', '1', 'menu-item-parent', '站点管理', 'fa fa-lg fa-fw fa-table', '1', null, '_self');
INSERT INTO `sys_menu` VALUES ('2', '用户管理', '0', '2', '2018-03-13 19:07:01', '/tms/admin/user_list.html?page=1&rows=10&sysUser=', 'menu', '2', 'menu-item-parent', '用户管理', 'fa fa-lg fa-fw fa-table', '1', null, '_self');
INSERT INTO `sys_menu` VALUES ('3', '角色管理', '0', '3', '2018-03-13 19:07:59', '/tms/admin/role_list.html?page=1&rows=10&sysRole=', 'menu', '3', 'menu-item-parent', '角色管理', 'fa fa-lg fa-fw fa-table', '1', null, '_self');
INSERT INTO `sys_menu` VALUES ('4', '菜单管理', '0', '4', '2018-03-13 19:08:10', '/tms/admin/menu_list.html?page=1&rows=10&sysMenu=', 'menu', '4', 'menu-item-parent', '菜单管理', 'fa fa-lg fa-fw fa-table', '1', null, '_self');
INSERT INTO `sys_menu` VALUES ('5', '部门管理', '0', '5', '2018-03-13 19:08:21', '/tms/admin/dept_list.html?page=1&rows=10&sysDept=', 'menu', '5', 'menu-item-parent', '部门管理', 'fa fa-lg fa-fw fa-table', '1', null, '_self');
INSERT INTO `sys_menu` VALUES ('6', '区域站点管理', '0', '6', '2018-03-13 19:40:52', '/tms/basic/area_list.html?page=1&rows=10', 'menu', '6', 'menu-item-paren', '区域站点管理', 'fa fa-lg fa-fw fa-table', '1', null, '_self');
INSERT INTO `sys_menu` VALUES ('7', '车次管理', '0', '7', '2018-03-13 19:44:03', '/tms/basic/tck_number_list.html?page=1&rows=10', 'menu', '7', 'menu-item-parent', '车次管理', 'fa fa-lg fa-fw fa-table', '1', null, '_self');
INSERT INTO `sys_menu` VALUES ('8', '车型管理', '0', '8', '2018-03-13 19:54:07', '/tms/basic/vehicle_model_list.html?page=1&rows=10', 'menu', '8', 'menu-item-parent', '车型管理', 'fa fa-lg fa-fw fa-table', '1', null, '_self');
INSERT INTO `sys_menu` VALUES ('9', '订单管理 ', '0', '9', '2018-03-14 10:14:49', '/tms/order/order_list.html?page=1&rows=10', 'menu', '9', 'menu-item-paren', '订单管理 ', 'fa fa-lg fa-fw fa-table', '1', null, '_self');
INSERT INTO `sys_menu` VALUES ('10', 'OTD设置', '0', '10', '2018-03-13 19:58:32', '/tms/otd/otd_edit.html?page=1&rows=10', 'menu', '10', 'menu-item-paren', '订单管理 ', 'fa fa-lg fa-fw fa-table', '1', null, '_self');
INSERT INTO `sys_menu` VALUES ('11', '车次审核管理', '0', '11', '2018-03-14 12:06:22', '/tms/basic/tck_number_rvwed_list.html?page=1&rows=10', 'menu', '11', 'menu-item-paren', '车次审核管理', 'fa fa-lg fa-fw fa-table', '1', null, '_self');
INSERT INTO `sys_menu` VALUES ('12', '部门修改', '5', '12', '2018-03-14 11:18:35', '/tms/admin/dept_edit.html?page=1&rows=10&sysDept=', 'button', '12', 'menu-item-paren', '部门修改', 'fa fa-lg fa-fw fa-table', '0', null, '_self');
INSERT INTO `sys_menu` VALUES ('13', '菜单修改', '4', '13', '2018-03-14 11:19:59', '/tms/admin/menu_edit.html?page=1&rows=10&sysMenu=', 'button', '13', 'menu-item-paren', '菜单修改', 'fa fa-lg fa-fw fa-table', '0', null, '_self');
INSERT INTO `sys_menu` VALUES ('14', '角色修改', '3', '14', '2018-03-14 11:21:58', '/tms/admin/role_edit.html?page=1&rows=10&sysRole=', 'button', '14', 'menu-item-paren', '角色修改', 'fa fa-lg fa-fw fa-table', '0', null, '_self');
INSERT INTO `sys_menu` VALUES ('15', '用户修改', '2', '15', '2018-03-14 11:25:14', '/tms/admin/user_edit.html?page=1&rows=10&sysUser=', 'button', '15', 'menu-item-paren', '用户修改', 'fa fa-lg fa-fw fa-table', '0', null, '_self');
INSERT INTO `sys_menu` VALUES ('16', '区域站点修改', '6', '16', '2018-03-14 11:28:49', '/tms/basic/area_edit.html?page=1&rows=10', 'button', '16', 'menu-item-paren', '区域站点修改', 'fa fa-lg fa-fw fa-table', '0', null, '_self');
INSERT INTO `sys_menu` VALUES ('17', '站点修改', '1', '17', '2018-03-14 11:39:42', '/tms/basic/site_edit.html?page=1&rows=10', 'button', '17', 'menu-item-paren', '站点修改', 'fa fa-lg fa-fw fa-table', '0', null, '_self');
INSERT INTO `sys_menu` VALUES ('18', '车次修改', '7', '18', '2018-03-14 11:41:24', '/tms/basic/tck_number_edit.html?page=1&rows=10', 'button', '18', 'menu-item-paren', '车次修改', 'fa fa-lg fa-fw fa-table', '0', null, '_self');
INSERT INTO `sys_menu` VALUES ('19', '车次详情', '7', '19', '2018-03-14 11:56:24', '/tms/basic/tck_number_detail.html?page=1&rows=10', 'button', '19', 'menu-item-paren', '车次详情', 'fa fa-lg fa-fw fa-table', '0', null, '_self');
INSERT INTO `sys_menu` VALUES ('20', '车次审核修改', '11', '20', '2018-03-14 11:57:29', '/tms/basic/tck_number_rvwed_edit.html?page=1&rows=10', 'button', '20', 'menu-item-paren', '车次审核修改', 'fa fa-lg fa-fw fa-table', '0', null, '_self');
INSERT INTO `sys_menu` VALUES ('21', '车型修改', '8', '21', '2018-03-14 11:58:52', '/tms/basic/vehicle_model_edit.html?page=1&rows=10', 'button', '21', 'menu-item-paren', '车型修改', 'fa fa-lg fa-fw fa-table', '0', null, '_self');
INSERT INTO `sys_menu` VALUES ('22', '订单详情', '9', '22', '2018-03-14 12:00:39', '/tms/order/order_detail.html?page=1&rows=10', 'button', '22', 'menu-item-paren', '订单修改', 'fa fa-lg fa-fw fa-table', '0', null, '_self');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(50) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `type` varchar(50) DEFAULT NULL COMMENT '角色类型',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '系统管理员', '超级管理员', '1', '2018-03-13 15:29:25', '2018-03-13 15:29:27');
INSERT INTO `sys_role` VALUES ('2', '品牌公司角色', '品牌公司角色', '1', '2018-03-13 20:13:14', '2018-03-13 20:13:17');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `roleid` int(50) unsigned NOT NULL COMMENT '角色id',
  `menuid` int(50) NOT NULL COMMENT '菜单id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('2', '1');
INSERT INTO `sys_role_menu` VALUES ('2', '6');
INSERT INTO `sys_role_menu` VALUES ('2', '7');
INSERT INTO `sys_role_menu` VALUES ('2', '8');
INSERT INTO `sys_role_menu` VALUES ('2', '9');
INSERT INTO `sys_role_menu` VALUES ('2', '10');
INSERT INTO `sys_role_menu` VALUES ('1', '1');
INSERT INTO `sys_role_menu` VALUES ('1', '2');
INSERT INTO `sys_role_menu` VALUES ('1', '3');
INSERT INTO `sys_role_menu` VALUES ('1', '4');
INSERT INTO `sys_role_menu` VALUES ('1', '5');
INSERT INTO `sys_role_menu` VALUES ('1', '6');
INSERT INTO `sys_role_menu` VALUES ('1', '7');
INSERT INTO `sys_role_menu` VALUES ('1', '8');
INSERT INTO `sys_role_menu` VALUES ('1', '9');
INSERT INTO `sys_role_menu` VALUES ('1', '10');
INSERT INTO `sys_role_menu` VALUES ('1', '11');
INSERT INTO `sys_role_menu` VALUES ('1', '12');
INSERT INTO `sys_role_menu` VALUES ('1', '13');
INSERT INTO `sys_role_menu` VALUES ('1', '14');
INSERT INTO `sys_role_menu` VALUES ('1', '15');
INSERT INTO `sys_role_menu` VALUES ('1', '16');
INSERT INTO `sys_role_menu` VALUES ('1', '17');
INSERT INTO `sys_role_menu` VALUES ('1', '18');
INSERT INTO `sys_role_menu` VALUES ('1', '19');
INSERT INTO `sys_role_menu` VALUES ('1', '20');
INSERT INTO `sys_role_menu` VALUES ('1', '21');
INSERT INTO `sys_role_menu` VALUES ('1', '22');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `loginname` varchar(100) DEFAULT NULL COMMENT '登录名',
  `realname` varchar(100) DEFAULT NULL COMMENT '姓名',
  `password` varchar(50) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '公司名称',
  `intro` varchar(400) DEFAULT NULL COMMENT '公司简介',
  `certificate` varchar(400) DEFAULT NULL COMMENT '公司资质',
  `corporation` varchar(100) DEFAULT NULL COMMENT '公司法人',
  `corporationim` varchar(200) DEFAULT NULL COMMENT '法人身份证照片',
  `phone` varchar(100) DEFAULT NULL COMMENT '联系方式',
  `state` varchar(50) DEFAULT NULL COMMENT '状态',
  `createtime` datetime DEFAULT NULL COMMENT '注册时间',
  `codeid` int(20) DEFAULT NULL COMMENT '编码id',
  `deptid` int(20) DEFAULT NULL COMMENT '部门id',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '1', 'e10adc3949ba59abbe56e057f20f883e', '2', '111', '1111', '11', '111', '11', '11111', '1', '2018-03-13 15:03:50', '1', '1', '12@12.12');
INSERT INTO `sys_user` VALUES ('2', 'zhang', 'zhang', 'e10adc3949ba59abbe56e057f20f883e', '2', '111', null, null, null, null, '1', '1', '2018-03-14 10:26:50', null, '1', '121@12.com');
INSERT INTO `sys_user` VALUES ('3', 'zhang1', 'zhang1', 'e10adc3949ba59abbe56e057f20f883e', null, '邦邦物流', null, null, null, null, '111', '1', '2018-03-15 17:51:35', null, '2', 'bangpuzhang@163.com');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `userid` int(11) unsigned NOT NULL COMMENT '用户id',
  `roleid` int(11) NOT NULL COMMENT '角色id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1');
INSERT INTO `sys_user_role` VALUES ('2', '2');
INSERT INTO `sys_user_role` VALUES ('3', '2');

-- ----------------------------
-- Table structure for userinfo
-- ----------------------------
DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE `userinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `loginname` varchar(100) DEFAULT NULL COMMENT '登录名',
  `password` varchar(50) DEFAULT NULL COMMENT '密码',
  `type` varchar(100) DEFAULT NULL COMMENT '账户类型',
  `name` varchar(100) DEFAULT NULL COMMENT '公司名称',
  `intro` varchar(200) DEFAULT NULL COMMENT '公司简介',
  `certificate` varchar(100) DEFAULT NULL COMMENT '公司资质',
  `corporation` varchar(100) DEFAULT NULL COMMENT '公司法人姓名',
  `corporationim` varchar(100) DEFAULT NULL COMMENT '法人身份证照片',
  `phone` varchar(50) DEFAULT NULL COMMENT '联系方式',
  `state` varchar(100) DEFAULT NULL COMMENT '未认证、认证不通过、已认证、拉黑',
  `createtime` datetime DEFAULT NULL COMMENT '注册时间',
  `capital` varchar(100) DEFAULT NULL COMMENT '注册资金',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of userinfo
-- ----------------------------
INSERT INTO `userinfo` VALUES ('1', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '1', '1', '1', '1', '1', null, '1', '1', '2018-03-13 14:46:03', '1', '121@123.com');

-- ----------------------------
-- Table structure for vehicle_model
-- ----------------------------
DROP TABLE IF EXISTS `vehicle_model`;
CREATE TABLE `vehicle_model` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) DEFAULT NULL COMMENT '车型名称',
  `weight` varchar(100) DEFAULT NULL COMMENT '额度重量',
  `volum` varchar(100) DEFAULT NULL COMMENT '额度体积',
  `wlcompany` varchar(100) DEFAULT NULL COMMENT '承运商id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of vehicle_model
-- ----------------------------
INSERT INTO `vehicle_model` VALUES ('1', '小货车', '10.0', '100.0', '1');
INSERT INTO `vehicle_model` VALUES ('2', '中型货车', '20.0', '200.0', '2');
INSERT INTO `vehicle_model` VALUES ('3', '中大型货车', '30.0', '300.0', '3');
INSERT INTO `vehicle_model` VALUES ('4', '大货车', '40.0', '400.0', '4');
INSERT INTO `vehicle_model` VALUES ('5', '中巴客运车', '10.0', '400.0', '5');
INSERT INTO `vehicle_model` VALUES ('6', '大巴客运车', '20.0', '200.0', '2');

-- ----------------------------
-- Table structure for warehouse
-- ----------------------------
DROP TABLE IF EXISTS `warehouse`;
CREATE TABLE `warehouse` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `bukrs` varchar(100) DEFAULT NULL COMMENT '公司代码',
  `butxt` varchar(100) DEFAULT NULL COMMENT '公司名称',
  `werks` varchar(100) DEFAULT NULL COMMENT '仓库编号',
  `name1` varchar(400) DEFAULT NULL COMMENT '仓库描述',
  `kunnr` varchar(100) DEFAULT NULL COMMENT '仓库的客户号',
  `lifnr` varchar(100) DEFAULT NULL COMMENT '仓库的供应商号',
  `regio` varchar(100) DEFAULT NULL COMMENT '省编码',
  `bezei_1` varchar(100) DEFAULT NULL COMMENT '省名称',
  `cityc` varchar(100) DEFAULT NULL COMMENT '市编码',
  `bezei_2` varchar(100) DEFAULT NULL COMMENT '市名称',
  `transpzone` varchar(100) DEFAULT NULL COMMENT '区/县编码',
  `vtext` varchar(100) DEFAULT NULL COMMENT '区/县名称',
  `updatetime` datetime DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of warehouse
-- ----------------------------
INSERT INTO `warehouse` VALUES ('1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2018-03-14 21:30:17');

-- ----------------------------
-- Table structure for waybill
-- ----------------------------
DROP TABLE IF EXISTS `waybill`;
CREATE TABLE `waybill` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(100) DEFAULT NULL COMMENT '运单编号',
  `fromcode` varchar(100) DEFAULT NULL COMMENT '来源编号',
  `orderfrom` varchar(100) DEFAULT NULL COMMENT '订单来源：sap，wms',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `deptname` varchar(100) DEFAULT NULL COMMENT '所属机构',
  `subcontractor` varchar(100) DEFAULT NULL COMMENT '转包承运商',
  `amount` varchar(100) DEFAULT NULL COMMENT '货品数量',
  `unit` varchar(100) DEFAULT NULL COMMENT '货品单位',
  `weight` varchar(100) DEFAULT NULL COMMENT '货品总重量(kg)',
  `volume` varchar(100) DEFAULT NULL COMMENT '货品总体积(m³)',
  `fhaddress` varchar(100) DEFAULT NULL COMMENT '发货地址',
  `shaddress` varchar(100) DEFAULT NULL COMMENT '收货地址',
  `requstarttime` datetime DEFAULT NULL COMMENT '要求发货时间',
  `requendtime` datetime DEFAULT NULL COMMENT '要求收货时间',
  `state` varchar(50) DEFAULT NULL COMMENT '当前执行阶段 默认 0 初始',
  `exceptcount` varchar(50) DEFAULT NULL COMMENT '异常事件数',
  `c_weight` varchar(100) DEFAULT NULL COMMENT '匹配车型重量(kg)',
  `c_volume` varchar(50) DEFAULT NULL COMMENT '匹配车型体积(m³)',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `wlcompany` varchar(100) DEFAULT NULL COMMENT '承运商',
  `gstarttime` datetime DEFAULT NULL COMMENT '发车时间',
  `garrivetime` datetime DEFAULT NULL COMMENT '到达时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of waybill
-- ----------------------------
INSERT INTO `waybill` VALUES ('1', '1', '1', '1', '2018-03-14 21:23:04', '1', '1', '1', '1', '10.0', '100.0', '1', '1', '2018-03-14 21:23:13', '2018-03-14 21:23:16', '0', '1', '1', '1', '2018-03-14 21:23:22', '1', '2018-03-14 21:23:27', '2018-03-14 21:23:30');
