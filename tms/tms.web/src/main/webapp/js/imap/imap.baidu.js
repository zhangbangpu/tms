/**
 * 百度地图组件封装
 *
 * @author dupeng
 * @date 2014/2/22
 */

imap.baidu = {
	apiUrl 		: 'http://api.map.baidu.com/getscript?v=2.0&ak=dVeyh7XGPHUsoAmcaltrHCiP',
	jsFiles 	: [
		'tools.distancetool',
		'tools.simplemarker'
	],
	vars 		: {},	//存放地图
	instances 	: {
		maps 		: {},
		markers 	: {},
		polygons 	: {},
		polylines 	: {}
	},
	creater 	: function(container,options) {
		var self = this;
		self._container = container;
		$.extend(self._options,options);
		self._create();
		return self;
	}
};

imap.baidu.creater.prototype = {
	_map 		: null,
	_mapLoaded 	: false,
	_container 	: null,
	_events 	: [],
	_options 	: {
	},
	/**
	 * 添加已创建好的实例
	 * @param key 键
	 * @param value 值
	 * @param type 类型
	 */
	addinstance : function(key,value,type) {
		imap.baidu.instances[type][key] = value;
	},
	/**
	 * 给地图/覆盖物添加事件
	 * @param eventtype 事件类型
	 * @param callback 回调函数
	 * @param target 目标
	 */
	addevent 	: function(eventtype,callback,target) {
		var self = this,
			target = target || {};
		var obj = target._obj || self._map;
		console.log(callback);
		var listener = obj.addEventListener(eventtype,callback);
		self._events.push({
			eventtype : eventtype,
			listener : callback,
			obj : obj
		});
		return true;
	},
	removeevent : function(eventtype,target) {
		var self = this,
			target = target || {};
		var obj = target._obj || self._map;
		for (var i = self._events.length - 1; i >= 0; i--) {
			if(self._events[i].eventtype == eventtype && self._events[i].obj == obj) {
				log.debug(obj,eventtype,self._events[i].listener);
				obj.removeEventListener(eventtype,self._events[i].listener);
				delete self._events[i];
				return true;
				break;
			}
		};
		return false;
	},
	/**
	 * 引入API, 创建地图
	 */
	_create 	: function() {
		var self = this,
			container = self._container,
			opt = self._options;
		if(!self._mapLoaded) self._loadApi(function() {
			self._mapLoaded = true;
			self._loadLib();
			//创建地图
			var map = new BMap.Map(container);
			self._map = map;
			var latlng = self._g2b({lat:opt.lat, lng:opt.lng});
			if(latlng) {
				//中心位置
				var point = new BMap.Point(latlng.lng, latlng.lat);
				map.centerAndZoom(point,opt.zoom);
			} else if(opt.address) {
				var myGeo = new BMap.Geocoder();
				myGeo.getPoint(opt.address, function(point) {
					if(point) {
						map.centerAndZoom(point,opt.zoom);
					}
				});
			}
			if(opt.onload) map.addEventListener('load',opt.onload);
			self._addControls();
			// 是否允许鼠标滚轮缩放
			if (opt.scrollwheel == true) { map.enableScrollWheelZoom(); }
			//最小级别为3
			map.setMinZoom(3);
			//标记标点
			// if($.isArray(opt.markers) && opt.markers.length > 0) {
			// 	self.markers(opt.markers);
			// }
			self.addinstance(container,self,'maps');
		});
	},
	_marker 	: function() {},
	/**
	 * 纠正经纬度偏移,用于显示和初始化  google -> baidu
	 */
	_g2b 		: function(latlng) {
		if(!latlng) return;
		var lat = lng = 0;
		if('string' == typeof(latlng)) {
			var arr = latlng.split(',');
			lat = Math.round((parseFloat(arr[0])+0.0060) * 10000000) / 10000000;
			lng = Math.round((parseFloat(arr[1])+0.0065) * 10000000) / 10000000;
			return lat + ',' + lng;
		} else if('object' == typeof(latlng) && !isNaN(parseFloat(latlng.lat))) {
			lat = Math.round((parseFloat(latlng.lat)+0.0060) * 10000000) / 10000000;
			lng = Math.round((parseFloat(latlng.lng)+0.0065) * 10000000) / 10000000;
			return {lat:lat,lng:lng};
		}
		return false;
	},
	/**
	 * 还原google经纬度,用于保存
	 */
	_b2g: function(latlng) {
		if(!latlng) return;
		var lat = lng = 0;
		if('string' == typeof(latlng)) {
			var arr = latlng.split(',');
			lat = Math.round((parseFloat(arr[0])-0.0060) * 1000000) / 1000000;
			lng = Math.round((parseFloat(arr[1])-0.0065) * 1000000) / 1000000;
			return lat + ',' + lng;
		} else if('object' == typeof(latlng) && !isNaN(parseFloat(latlng.lat))) {
			lat = Math.round((parseFloat(latlng.lat)-0.0060) * 1000000) / 1000000;
			lng = Math.round((parseFloat(latlng.lng)-0.0065) * 1000000) / 1000000;
			return {lat:lat,lng:lng};
		}
		return false;
	},
	_loadApi 	: function(callback) {
		loadScript(imap.baidu.apiUrl, callback);
	},
	_loadLib	: function() {
		var jsFiles = imap.baidu.jsFiles;
		for (var i = jsFiles.length - 1; i >= 0; i--) {
			loadScript(imap.libpath + 'baidu/imap.baidu.' + jsFiles[i] + '.js');
		};
	},
	_addControls : function() {
		var self = this;
		self._map.addControl(new BMap['OverviewMapControl']({isOpen:false}));
		self._map.addControl(new BMap['NavigationControl']());
		self._map.addControl(new BMap['ScaleControl']());
	}
};