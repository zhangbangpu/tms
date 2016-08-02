/**
 * 地图API封装
 *
 * add by dupeng at 2014/2/22
 */

window.imap = {
	loadModule : function(modulename,options,callback) {
		if(!modulename) return;
		var options = options || {},
			callback = callback || false;
		loadScript("/themes/default/js/imap/imap."+modulename+".js",(function(modulename,options,callback) {
			return function() {
				var mapdiv = options.mapdiv || 'mapDiv';
				imap['i'+modulename] = new imap[modulename]($('#'+mapdiv),options);
				if('function' == typeof callback) { 
					callback();
				}
			}
		})(modulename,options,callback));
	},
	getModule : function(modulename) {
		return imap['i'+modulename] || false;
	}
};

// $ips.include('http://api.map.baidu.com/getscript?v=2.0&ak=dVeyh7XGPHUsoAmcaltrHCiP');
// document.write('<script type="text/javascript" src="http://api.map.baidu.com/getscript?v=2.0&ak=dVeyh7XGPHUsoAmcaltrHCiP"></script>');

(function($,imap) {

$.widget("hui.ditu", {

	map 			: false,	//地图
	autoNum 		: 0,
	drawingManager  : null,
	markerclusterer : null,
	overlays 		: {},		//地图上已有覆盖物
	_allowEvents 	: ['ondragend','onclick','onmouseover','onmouseout'],
	_distancetool 	: null,		//测距工具
	_mapLoaded 		: false,
	_mapsrv 		: 'baidu',
	config 			: {
		baidu 			: {
			apiUrl 			: 'http://api.map.baidu.com/getscript?v=2.0&ak=dVeyh7XGPHUsoAmcaltrHCiP',
			libFiles 			: [
				$ips.appPath + 'js/imap/baidu/imap.baidu.tools.distancetool.js',
				$ips.appPath + 'js/imap/baidu/imap.baidu.tools.simplemarker.js',
				$ips.appPath + 'js/imap/baidu/imap.baidu.tools.markerclusterer.js',
				$ips.appPath + 'themes/default/js/bmap/DrawingManager.js',
				$ips.appPath + 'js/imap/baidu/imap.baidu.tools.searchinrectangle.js'
			]
		}
	},

	options 		: {
		mapsrv 				: 'baidu',	// * 地图类型, 默认:baidu
		address 			: '',		// 中心点地址
		lat 				: 108,		// 中心点经度
		lng 				: 38.6,		// 中心点纬度
		zoom 				: 5,		// 缩放级别
		markers 			: [],		// 默认加载的标记数组，详细参考marker函数的参数
		icon 				: {			// 标记图标默认设置
			image 				: "/img/map_skin/marker.png",
			iconsize 			: [20, 34],
			iconanchor 			: [9, 34],
			infowindowanchor 	: [9, 2]
		},
		polygonOptions 		: {			//区域默认选项
			clickable 		: true,
			strokeColor 	: '#CC00FF',
			strokeWeight	: 2,
			strokeOpacity 	: 1,
			fillColor 		: '#CC00FF',
			fillOpacity 	: 0.3
		},
		onload 			: false,	// 当完成地图设置时会触发此事件
		// maptype 		: BMAP_NORMAL_MAP,		//地图类型
		scrollwheel 	: true,	// 是否允许滚轮缩放
		controls 		: [],
		html_prepend 	: '<div class="gmap_marker">',
		html_append 	: '</div>'
	},

	_loadApi		: function(callback) {
		var self = this,
			mapsrv = self._mapsrv;
		loadScript(self.config[mapsrv].apiUrl, callback);
	},

	_loadLib 		: function(libFiles,fn) {
		var self = this,
			mapsrv = self._mapsrv;

		var libFiles = libFiles || self.config[mapsrv].libFiles;

		$ips.include([{
			load: libFiles,
			complete: function () {
				fn();
			}
		}]);

		// var f1 = libFiles.pop(), func = fn;
		// if(libFiles.length > 0) {
		// 	func = (function(me) {
		// 		return function() {
		// 			me._loadLib(libFiles,fn);
		// 		}
		// 	})(self);
		// }
		// // console.log('load:' + f1);
		// loadScript('/js/imap/'+mapsrv+'/imap.baidu.tools.' + f1 + '.js',func);
	},

	/**
	 * 创建地图
	 */
	_create 		: function() {
		var self 	= this,
			opt 	= self.options,
			map 	= self.map,
			mapsrv 	= opt.mapsrv;
		self._mapsrv = mapsrv;
		//创建地图
		if(!map) {
			self._loadApi(function() {
				self._loadLib(self.config[mapsrv].libFiles,function() {
					var domid = self.element.attr('id');
					self._mapLoaded = true;
					//创建地图
					var map = new BMap.Map(domid);
					self.map = map;
					var latlng = self.g2b({lat:opt.lat, lng:opt.lng});
					if(opt.onload) map.addEventListener('load',opt.onload);
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
					//显示的地图控件
					if($.isArray(opt.controls)) {

						//兼容google命名
						var gcontrols = {
							'GLargeMapControl3D'	:	'NavigationControl',
							'GScaleControl'			:	'ScaleControl',
							'GOverviewMapControl'	:	'OverviewMapControl'
						};
						var positions = {
							'topleft' : 'BMAP_ANCHOR_TOP_LEFT',
							'topright' : 'BMAP_ANCHOR_TOP_RIGHT',
							'bottomleft' : 'BMAP_ANCHOR_BOTTOM_LEFT',
							'bottomright' : 'BMAP_ANCHOR_BOTTOM_RIGHT'
						};
						var size = 'new BMap.Size(20,50)';
						if(opt.controls.length < 1) {
							opt.controls = ['ScaleControl','NavigationControl'];
						}
						for(var i = 0; i < opt.controls.length; i++) {
							var ctname = pos = cparams = '';
							if($.isArray(opt.controls[i])) {
								// 0 name 1 position
								ctname = gcontrols[opt.controls[i][0]] || opt.controls[i][0];
								pos = positions[opt.controls[i][1]];
								if(pos) cparams = '{anchor:'+pos+',offset:'+size+'}';
							} else {
								ctname = gcontrols[opt.controls[i]] || opt.controls[i];
							}
							eval('map.addControl(new BMap.'+ctname+'('+cparams+'))');
						}
						//鹰眼控件
						map.addControl(new BMap.OverviewMapControl({isOpen:false}));
					}
					// 是否允许鼠标滚轮缩放
					if (opt.scrollwheel == true) { map.enableScrollWheelZoom(); }
					//最小级别为3
					map.setMinZoom(3);

					//标记标点
					if($.isArray(opt.markers) && opt.markers.length > 0) {
						self.markers(opt.markers);
					}
					self.map = map;


					
					//实例化鼠标绘制工具
					self.drawingManager = new BMapLib.DrawingManager(map, {
						isOpen: false, //是否开启绘制模式
						enableDrawingTool: false, //是否显示工具栏
						drawingToolOptions: {
							anchor: BMAP_ANCHOR_TOP_RIGHT, //位置
							offset: new BMap.Size(5, 5), //偏离值
							scale: 0.8 //工具栏缩放比例
						},
						circleOptions: opt.polygonOptions, //圆的样式
						polylineOptions: opt.polygonOptions, //线的样式
						polygonOptions: opt.polygonOptions, //多边形的样式
						rectangleOptions: opt.polygonOptions //矩形的样式
					});
					console.log(self.drawingManager);
					
					
				});
				
			});
		}
		return self;
	},
	openDrawing: function(type){
		this.drawingManager.open();
		this.drawingManager.setDrawingMode(type);
	},
	closeDrawing: function(type){
		this.drawingManager.close();
	},
	

	/**
	 * 纠正经纬度偏移,用于显示和初始化  google -> baidu
	 */
	g2b: function(latlng) {
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
	b2g: function(latlng) {
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

	/**
	 * 针对使用SimpleMarker批量创建的标注点做显示
	 */
	showmarker: function(isLabel) {
		if('undefined' != BMapLib && 'undefined' != BMapLib.SimpleMarker) BMapLib.SimpleMarker.show(this.map, isLabel);
	},
	
	/**
	 * 根据ID标隐藏标注点
	 * overlayId : ['003','004']
	 * 或者 overlayId : '003'
	 */
	hidemarkers: function(overlayId) {
		var markers =[];
		var self = this;
		if('string' == typeof overlayId){
		   markers[0]=self.overlays[overlayId] || false;
		}else{
			for(var i = 0; i< overlayId.length;i++ ){
				markers[i]=self.overlays[overlayId[i]] || false;
			}
			
		}
		console.log(markers);
		for(var j = 0; j < markers.length; j++) {
			var marker = markers[j];
			marker.hide();
		}
	},
	
	/**
	 * 根据ID显示标注点
	 * overlayId : ['003','004']
	 * 或者 overlayId : '003'
	 */
	showmarkers: function(overlayId) {
		var markers =[];
		var self = this;
		if('string' == typeof overlayId){
		   markers[0]=self.overlays[overlayId] || false;
		}else{
			for(var i = 0; i< overlayId.length;i++ ){
				markers[i]=self.overlays[overlayId[i]] || false;
			}
			
		}
		console.log(markers);
		for(var j = 0; j < markers.length; j++) {
			var marker = markers[j];
			marker.show();
		}
	},
	
	/**
	 *
	 * opts : ['003','004']
	 */
	markercluster: function(opts) {
		var markers =[];
		var self = this;
		
		for(var i = 0; i< opts.length;i++ ){
			markers.push(self.overlays[opts[i]]);
		 }
		newzoom = self.getZoom();
		if(newzoom < 8){
			var markerClusterer = new MarkerClusterer(self.map, markers, {notListened: true, gridSize: 100, maxZoom: 8});
		}else{
			for(var i = 0; i< opts.length;i++ ){
				self.map.addOverlay((self.overlays[opts[i]]));
			 }
		}
		console.log(markers);
		console.log(newzoom);
	},
	
	/**
	 * 地图标注标点
	 * opts : {
	 * 		overlayId: "test001",		// 标记的唯一ID
	 * 		address: "北京",			// 地址
	 * 		lat: 42,					// 经纬度
	 * 		lng: 3,
	 * 		draggable: false,			// 是否可拖曳
	 * 		center: false,				// 将标记坐标设为地图中心点
	 * 		html: "<b>Hello</b>",		// 信息窗内容
     *      label: labelOption,         // 选项见 this.label    -- 只适用于map
	 * 		popup: false,				// 是否立即显示信息窗
	 * 		icon: {						// 图标设置
	 * 			image: "http://www.google.com/mapfiles/marker.png",
	 *			offset: [0, 0],
	 *			iconsize:			[20, 34],
	 *			iconanchor:			[9, 34],
	 *			infowindowanchor:	[9, 2]
	 *		},
	 *		onsuccess: function(marker) {},		// 创建或更新成功事件
	 * 		onclick: function(latlng) {},		// 点击事件
	 * 		ondragend: function(latlng) {},		// 拖曳结束事件
	 * 		bmode: false,					// *** bmode为true,经纬度需要转换,默认为false ***
	 *		nodraw: false,					// 不自动addOverlay
	 *		simple: false,					// 使用SimpleMarker创建
	 *		batch: false,					// 如果使用SimpleMarker创建, 则相当于batch模式
	 *		text: false						// 标注上显示文字(目前只支持如果使用SimpleMarker创建的标点)
	 *      _mcMarkers	: [],		//聚合对象中的点
	 *      usecluster  : true,     //是否使用聚合
	 * }
	 */
	marker: function(opts) {
		var self = this,
			popup = opts.popup || false,
			popupmarker = opts.popupmarker || false,
			center = opts.center || false,
			bmode = opts.bmode || false,
			draggable = opts.draggable || false;
		    hide = opts.hide || false;
		var nodraw = opts.nodraw || false,
			simple = opts.simple || false,
			batch = opts.batch || false;
		var latlng = false,
			overlayId = self._getOverlayId(opts.overlayId,'marker');

		if(!bmode) {
			latlng = self.g2b(opts);
		} else if(!isNaN(parseFloat(opts.lat)) && !isNaN(parseFloat(opts.lng))) {
			latlng = {lat:opts.lat,lng:opts.lng};
		}

		if(latlng) {
			//定位到中心点
			if(center) {
				var point = new BMap.Point(latlng.lng, latlng.lat);
				self.map.setCenter(point);
			}

			var marker = null;
			if(simple) {
				opts.icon = $.extend({},this.options.icon,opts.icon);
				var markerOpt = {
                    batchMode: batch
                    ,url: opts.icon.image
                    ,size: new BMap.Size(opts.icon.iconsize[0],opts.icon.iconsize[1])
                    ,anchor: new BMap.Size(opts.icon.iconanchor[0],opts.icon.iconanchor[1])
                };
                if(opts.text) markerOpt.text = opts.text; // TASK #1111 dupeng
                
               // console.log(markerOpt);
                if(opts.icon.offset) markerOpt.offset = new BMap.Size(opts.icon.offset[0],opts.icon.offset[1]);
				marker = new BMapLib.SimpleMarker(new BMap.Point(latlng.lng, latlng.lat),markerOpt);
			} else {
				// 处理icon
				var icon = self.getIcon(opts.icon);
				var marker_opts = {icon: icon};
				// 标点拖动
				if(draggable) {
					marker_opts.enableDragging = true;
				}
				// 标点隐藏
				if(hide) {
					marker_opts.hide();
				}
				// 创建标注
				marker = new BMap.Marker(new BMap.Point(latlng.lng, latlng.lat), marker_opts);
			}

			if(marker) {
				marker.overlayId = overlayId;
				marker.bmode = bmode;
				// 事件
				var allowEvt = self._allowEvents;
				for(var i = 0; i < allowEvt.length; i++) {
					if(opts[allowEvt[i]] && 'function' == typeof opts[allowEvt[i]]) {
						var evt = allowEvt[i].replace('on','');
						marker.addEventListener(evt, (function(self,opts,e) {
							var bmode = opts.bmode || false;
							return function() {
								var point = this.getPosition(),
									lat = point.lat,
									lng = point.lng,
									bmode = opts.bmode || false;
								var latlng = {lat:lat,lng:lng};
								if(!bmode) {
									latlng = self.b2g(latlng);
								}
								opts[e](latlng);
							}
						})(self,opts,allowEvt[i]));
					}
				}
			}

			if(!nodraw) self.map.addOverlay(marker); // 将标注添加到地图中
			// 放入所有标点
			self.overlays[overlayId] = marker;
			if(opts.onsuccess) {
				opts.onsuccess(opts,marker);
			}

			// 弹出HTML
			if(opts.html) {
				var html = self.options.html_prepend + opts.html + self.options.html_append;
				var infoWinOpt = {};
				if(simple && opts.icon.infowindowanchor && opts.icon.infowindowanchor.length == 2) {
					var w = -Math.ceil(opts.icon.iconsize[0]/2)+opts.icon.infowindowanchor[0],
						h = -Math.ceil(opts.icon.iconsize[1])+opts.icon.infowindowanchor[1];
					infoWinOpt.offset = new BMap.Size(w,h);
				}
				marker.__infoWindow__ = new BMap.InfoWindow(html, infoWinOpt);  // 创建信息窗口对象
				marker.addEventListener("click", (function(infowin) {
					return function() {
						this.openInfoWindow(infowin);
					}
				})(marker.__infoWindow__));
				if(popup) marker.openInfoWindow(marker.__infoWindow__);
			}

            //附加文本标注
            if(opts.label) {
                opts.label.marker = marker;
                opts.label.overlayId = '_label_' + overlayId;
                opts.label.simple = simple;
                marker.labelobj = self.label(opts.label);
            }
            
            //附加圆形区域
            if(opts.circle) {
            	opts.circle.marker = marker;
                opts.circle.overlayId = '_circle_' + overlayId;
                marker.circle = self.circle(opts.circle);
            	self.map.addOverlay(marker.circle);
            }
			return marker;
		} else if(opts.address) {
			// 替换参数为地址
			if(opts.html == '_address') { opts.html = opts.address; }
			$ips.geo(opts.address,function(data) {
	            if(data.code == 0 && data.total > 0) {
	            	log.debug(data.poilist[0]);
	            	var point = data.poilist[0];
	            	if(point) {
						var gpoint = self.b2g(point);
						opts.lat = gpoint.lat;
						opts.lng = gpoint.lng;
					}
	            } else {
	                opts.lat = 40;
					opts.lng = 116.4;
	            }
				opts.center = true;
				self.marker(opts);
				// log.debug(this);
                if(opts.onsuccess) {
                    opts.onsuccess(opts,this);
                }
	        },false);
		}
		return false;
	},
	
	/**
	 * 添加聚合点
	 * @param object mkobj		    标注点
	 */
	_addMcMarker    : function(mkobj) {
        this._mcMarkers.push(mkobj);
        return this._mcMarkers.length-1 || 0;
    },
    
	/**
	 * 获取标点经纬度
	 * @param object marker			多边形对象
	 * @param boolen bmode			百度地图模式, 默认false, 默认返回google模式
	 */
	getMarkerPoint: function(marker,bmode) {
		if(marker) {
			var self = this;
			var point = marker.getPosition();
			var lat = point.lat,
				lng = point.lng;
			if(!bmode) {
				var latlng = self.b2g({lat:lat,lng:lng});
				lat = latlng.lat;
				lng = latlng.lng;
			}
			return {lat:lat,lng:lng};
		}
		return false;
	},
	markers: function(markers) {
		// 创建标记列表
		for(var j = 0; j < markers.length; j++) {
			var marker = markers[j];
			this.marker(marker);
		}
	},
	/**
	 * 更新标记的属性，不存在则创建
	 * 若更新操作, 可更新: 图标, 位置, label显示开关 label: {display: "show/hide/toggle"}
	 * 创建参数同this.marker()
	 */
	updateMarker: function(opts) {
		var self = this,
			overlayId = opts.overlayId || false;
		if(overlayId && self.overlays[overlayId] && (self.overlays[overlayId] instanceof BMap.Marker || self.overlays[overlayId] instanceof BMapLib.SimpleMarker)) {
			var marker = self.overlays[overlayId];
			//修改位置
			if(opts.lat && opts.lng) {
				var latlng = false;
				var bmode = marker.bmode || opts.bmode || false;
				if(!bmode) {
					latlng = self.g2b(opts);
				} else if(opts.lat && opts.lng) {
					latlng = {lat:opts.lat,lng:opts.lng};
				}
				var point = new BMap.Point(latlng.lng,latlng.lat);
				marker.setPosition(point);
				if(opts.center) self.map.setCenter(point);
                if(opts.onsuccess) { opts.onsuccess(opts) };
                //如果有附属label, 则同时更新label位置
                self.updateLabel({
                	overlayId: overlayId + '_label_',
                	lat: latlng.lat,
                	lng: latlng.lng,
                	bmode: true
                });
			} else if(opts.address) {
				var myGeo = new BMap.Geocoder();
				myGeo.getPoint(opts.address, (function(self,opts) {
                    return function(point) {
                        if(point) {
                            marker.setPosition(point);
                            if(opts.center) self.map.setCenter(point);
                            var gpoint = self.b2g(point);
                            opts.lat = gpoint.lat;
                            opts.lng = gpoint.lng;
                            if(opts.onsuccess) { opts.onsuccess(opts) };
                        }
                    }
                })(self,opts));
			}
			//修改图标
			if(opts.icon) {
				//更换图标
				if(marker instanceof BMapLib.SimpleMarker) {
					//simpleMarker可更改offset和image
					if('undefined' != typeof opts.icon.offset && 'function' == typeof marker.setOffset) {
						marker.setOffset(opts.icon.offset);
					}
					if('undefined' != typeof opts.icon.image && 'function' == typeof marker.setImage) {
						marker.setImage(opts.icon.image);
					}
				} else if(marker instanceof BMap.Marker) {
					var icon = self.getIcon(opts.icon);
					if(icon) marker.setIcon(icon);
				}
				
			}
			//显示关闭label
			if(opts.label && marker.labelobj) {
				//label显示/隐藏
				opts.label.overlayId = marker.labelobj.overlayId;
				self.updateLabel(opts.label);
			}
		} else {
			self.marker(opts);
		}
	},

	/**
	 * 创建一个区域
	 * @param string overlayId  对象唯一标识ID
	 * @param string points	 组成多边形的经纬度字符串
	 * @param object styleopts  区域的边界和填充选项
	 * {
	 *		strokeColor: String, 	//边线颜色
	 *		fillColor: String, 		//填充颜色
	 *		strokeWeight: Number, 	//边线的宽度
	 *		strokeOpacity: Number, 	//边线透明度
	 *		fillOpacity: Number, 	//填充的透明度
	 * }
	 * @param object events	 区域的触发事件
	 */
	polygon: function(opts) {
		// overlayId,points,styleopts,events
		var self = this,
			map = self.map,
			overlayId = opts.overlayId || self._getOverlayId(overlayId,'polygon'),
			points = opts.points || false;
			circle = opts.circle || false;
			styleopts = opts.styleopts || {},
			events = opts.events || {},
			bmode = opts.bmode || false;
		styleopts = $.extend({},self.options.polygonOptions,styleopts);
		//set gmap.clickable == map.enableClicking
		if('undefined' != styleopts.clickable && styleopts.clickable == false) {
			styleopts.enableClicking = false;
		}
		//如果有circle,画出圆形
		if(!points && circle && circle.center) points = self._getCircleBounds(circle.center,circle.radius,true);
		//边界点
		var latlngarr = self._points2arr(points);
		var latlngs = new Array();
		if(latlngarr) {
			for(var i = 0; i < latlngarr.length; i++) {
				var lat = latlngarr[i][1],
					lng = latlngarr[i][0];
				if(!bmode) {
					var latlng = self.g2b({lat:lat,lng:lng});
					lat = latlng.lat;
					lng = latlng.lng;
				}
				latlngs.push(new BMap.Point(lng,lat));
			}
		}
		var polygon = new BMap.Polygon(latlngs,styleopts);
		polygon.overlayId = overlayId;

		//事件
        if(events) {
			//百度 lineupdate 兼容 google lineupdated
			if(events['lineupdated']) {
				events['lineupdate'] = events['lineupdated'];
				delete events['lineupdated'];
			}
            // events : remove() click() mouseover() mouseout() lineupdated()
            $.each(events,function(evt,fn) {
				polygon.addEventListener(evt, fn);
            });
        }
		map.addOverlay(polygon);
		// 放入所有标点
		self.overlays[overlayId] = polygon;
		return polygon;
	},
	/**
	 * 更新一个区域, 如果不存在则创建
	 * 可更新边界(支持圆形),样式
	 */
	updatePolygon: function(opts) {
		var self = this,
			map = self.map,
			overlayId = opts.overlayId || self._getOverlayId(overlayId,'polygon'),
			points = opts.points || false;
			circle = opts.circle || false;
			styleopts = opts.styleopts || false;
		if(overlayId && self.overlays[overlayId] && (self.overlays[overlayId] instanceof BMap.Polygon)) {
			var polygon = self.overlays[overlayId],
				bmode = polygon.bmode || false;
			//如果有circle,画出圆形
			if(!points && circle && circle.center) points = self._getCircleBounds(circle.center,circle.radius,true);
			//更新边界
			if(points) {
				//边界点
				var latlngarr = self._points2arr(points);
				var latlngs = new Array();
				if(latlngarr) {
					for(var i = 0; i < latlngarr.length; i++) {
						var lat = latlngarr[i][1],
							lng = latlngarr[i][0];
						if(!bmode) {
							var latlng = self.g2b({lat:lat,lng:lng});
							lat = latlng.lat;
							lng = latlng.lng;
						}
						latlngs.push(new BMap.Point(lng,lat));
					}
				}
				polygon.setPath(latlngs);
			}
			//更新样式
			if(styleopts) {
				if('undefined' != typeof styleopts.strokeColor) polygon.setStrokeColor(styleopts.strokeColor);
				if('undefined' != typeof styleopts.fillColor) polygon.setFillColor(styleopts.fillColor);
				if('undefined' != typeof styleopts.strokeWeight) polygon.setStrokeWeight(styleopts.strokeWeight);
				if('undefined' != typeof styleopts.strokeOpacity) polygon.setStrokeOpacity(styleopts.strokeOpacity);
				if('undefined' != typeof styleopts.fillOpacity) polygon.setFillOpacity(styleopts.fillOpacity);
			}
		} else {
			return self.polygon(opts);
		}
	},
	_points2arr: function(points,reverse) {
		if(!points) return;
		var reverse = reverse || false;
		var arr = new Array();
		ar = points.split(';');
		for(var i=0; i<ar.length; i++) {
			if(!ar[i] || ar[i]=='') continue;
			var a = ar[i].split(',');
			var lng = parseFloat(a[0]),
				lat = parseFloat(a[1]);
			if(reverse) {
				// lat,lng
				arr.push([lat,lng]);
			} else {
				arr.push([lng,lat]);
			}
		}
		return arr;
	},
	/**
	 * 返回多边形边界, 可直接存放到数据库
	 * @param object polygon		多边形对象
	 * @param boolen bmode			百度地图模式, 默认false, 默认返回google模式
	 */
	getPolygonPoints: function(polygon,bmode) {
		if(polygon) {
			var self = this,
				points = '',
				bmode = bmode || false;
			var paths = polygon.getPath();
			if(paths && paths.length) {
				for(var i = 0; i < paths.length; i++) {
					var lat = paths[i].lat,
						lng = paths[i].lng;
					if(!bmode) {
						//返回的是转换成google的经纬度
						var latlng = self.b2g({lat:lat,lng:lng});
						lat = latlng.lat;
						lng = latlng.lng;
					}
					points += lng+','+lat+';';
				}
			}
			return points;
		}
		return false;
	},

	/**
	 * 返回一个圆的边界 -- (为兼容google map全图,百度地图已有现成方法)
	 * @param Point center		中心点经纬度
	 * @param Number radius		半径(单位:米)
	 */
	_getCircleBounds: function(center,radius,bmode) {
		if(!center) return;
		var self = this,
			bmode = bmode || false,
			radius = radius || 500;
		var _points = '';
		var distance = radius/1000,
			lat = center.lat,
			lng = center.lng;
		if(!bmode) {
			var latlng = self.g2b({lat:lat,lng:lng});
			lat = latlng.lat;
			lng = latlng.lng;
		}
		var bpoint = new BMap.Point(lng,lat);
		for(i = 0; i < 72; i++) {
			var point = self._destination(bpoint, i * 360/72, distance);
			_points += point.lng + ',' + point.lat + ';';
		}
		var point = self._destination(bpoint, 0, distance);
		_points += point.lng + ',' + point.lat + ';';
		return _points;
	},
	_destination: function(orig, hdng, dist) {
		var R = 6371; // earth's mean radius in km
		var oX, oY;
		var x, y;
		var d = dist/R;  // d = angular distance covered on earth's surface
		hdng = hdng * Math.PI / 180; // degrees to radians
		oX = orig.lng * Math.PI / 180;
		oY = orig.lat * Math.PI / 180;

		y = Math.asin( Math.sin(oY)*Math.cos(d) + Math.cos(oY)*Math.sin(d)*Math.cos(hdng) );
		x = oX + Math.atan2(Math.sin(hdng)*Math.sin(d)*Math.cos(oY), Math.cos(d)-Math.sin(oY)*Math.sin(y));

		y = y * 180 / Math.PI;
		x = x * 180 / Math.PI;
		return new BMap.Point(x, y);
	},
	/**
	 * 返回可视范围内的地图经纬度区间 -dupeng
	 */
	getViewLatlngs: function(bmode) {
		var self = this,
			bounds = this.map.getBounds(),
			bmode = bmode || false;
		var sw = bounds.getSouthWest(),
			ne = bounds.getNorthEast();
		if(!bmode) {
			sw = self.b2g(sw);
			ne = self.b2g(ne);
		}
		return {minlat:sw.lat,minlng:sw.lng,maxlat:ne.lat,maxlng:ne.lng};
	},

	/**
	 * 从地图中删除所有叠加
	 */
	clearOverlays: function() {
		this.map.clearOverlays();
		this.overlays = {};
	},

	/**
	 * 根据ID获取覆盖物
	 */
	get: function(overlayId) {
		if(!overlayId) return;
		var self = this;
		return self.overlays[overlayId] || false;
	},

	/**
	 * 根据ID删除覆盖物,新增可传对象
	 */
	remove: function(obj) {
		if(!obj) return;
		var self = this,
			result = false;
		if('string' == typeof(obj)) {
			if(self.overlays[obj]) {
				//如果删除对象是一个标注点, 检查是否有附属label, 如果有一并删掉
				if('undefined' != typeof self.overlays[obj].labelobj) {
					self.map.removeOverlay(self.overlays[obj].labelobj);
				}
				self.map.removeOverlay(self.overlays[obj]);
				result = true;
				self.overlays[obj] = undefined;
			}
		} else if('object' == typeof(obj)) {
			var overlayId = obj.overlayId || false;
			if(overlayId) {
				self.map.removeOverlay(obj);
				result = true;
				self.overlays[overlayId] = undefined;
			}
		}
		return result;
	},

	setCenter: function(lat, lng, zoom, bmode) {
		var self = this,
			zoom = zoom || self.getZoom(),
			bmode = bmode || false;
		if(!bmode) {
			var latlng = self.g2b({lat:lat,lng:lng});
			lat = latlng.lat;
			lng = latlng.lng;
		}
		var point = new BMap.Point(lng, lat);
		self.map.setCenter(point);
		self.map.setZoom(zoom);
	},

	getCenter: function(bmode) {
		var center = this.map.getCenter();
		if(!bmode) {
			center = this.b2g(center);
		}
		return {lat:center.lat, lng:center.lng};
	},

	getZoom: function() {
		return this.map.getZoom();
	},
	getMap:	function(){
		return this.map;
	},
	getMarkers:function(){
		return this.overlays;
	},

	// 返回或获取覆盖物ID,没有id自增
	_getOverlayId: function(id,type) {
		var id = id || false,
			type = type || 'marker';
		if(id) {
			return id;
		} else {
			var index = this.autoNum;
			this.autoNum ++;
			return type+'_'+index;
		}
	},

	// 返回百度地图图标
	getIcon: function(icon) {
		var icon = icon || {};
		//如果已经是百度图标对象,直接返回
		if(icon instanceof BMap.Icon) {
			return icon;
		}
		icon = $.extend({},this.options.icon,icon);
		var sz = false, iopts = {};
		if(icon.iconsize && icon.iconsize.length == 2) {
			sz = new BMap.Size(icon.iconsize[0],icon.iconsize[1]);
		}
		if(icon.iconanchor && icon.iconanchor.length == 2) {
			iopts.anchor = new BMap.Size(icon.iconanchor[0],icon.iconanchor[1]);
		}
		if(icon.infowindowanchor && icon.infowindowanchor.length == 2) {
			iopts.infoWindowAnchor = new BMap.Size(icon.infowindowanchor[0],icon.infowindowanchor[1]);
		}
		// if(icon.offset && icon.offset.length == 2) {
		// 	iopts.imageOffset = new BMap.Size(icon.offset[0],icon.offset[1]);
		// }
		return new BMap.Icon(icon.image,sz,iopts);
	},
	

    /**
	 * 添加圆形范围
	 * circle: {
	 * 		overlayId: "circle001",									// 标记的唯一ID
     *      radius: 100,
	 * 		bmode: false,											// *** bmode为true,经纬度需要转换,默认为false ***
     *      marker: {},                                          
     *     	display: show											// 是否显示，默认显示
	 *  }
	 */
    circle: function(opts) {
		var self = this,
			overlayId = self._getOverlayId(opts.overlayId,'circle'),
			bmode = opts.bmode || false,
			marker = opts.marker || {};
		var point = marker.getPosition();
		var lat = point.lat,
		    lng = point.lng;
		if(!bmode) {
			var latlng = self.b2g({lat:lat,lng:lng});
			lat = latlng.lat;
			lng = latlng.lng;
		}
    	console.log(point);
    	var point = new BMap.Point(lng,lat);
    	var center = self.map.getCenter();
    	var zoom = self.map.getZoom();
    	console.log(center);
    	console.log(zoom);
    	self.map.centerAndZoom(point, zoom);
    	self.map.setCenter(center);
    	var circle = new BMap.Circle(point,opts.radius);
    	if('hide' == opts.display){
    		circle.hide();
    	}
        return circle;
    },

    /**
	 * 添加文本标注
	 * label: {
	 * 		overlayId: "label001",									// 标记的唯一ID
     *      lat: 39.9493,
     *      lng: 116.3975,
     *      html: '文本标注内容',
     *      style: {                                                // 属性用驼峰式命名,如:backgroundColor
     *          color : "red",
     *          fontSize : "12px",
     *          backgroundImage:'url("./cpop.png")'
     *      },
     *      offset: [10,10],
	 * 		bmode: false,											// *** bmode为true,经纬度需要转换,默认为false ***
     *      marker: false,                                          // 如果marker存在, 给marker附加label
     *		simple: false											// 是否使用SimpleMarker
	 *  }
	 */
    label: function(opts) {
		var self = this,
			overlayId = self._getOverlayId(opts.overlayId,'label'),
			bmode = opts.bmode || false,
			simple = opts.simple || false;
		if(!opts.marker) {
			var lat = opts.lat || 0, lng = opts.lng || 0;
            if(!bmode) {
                var latlng = self.g2b(opts);
                lat = latlng.lat;
                lng = latlng.lng;
            }
		}
		if(simple) {
			if(opts.marker) {
				overlayId = opts.marker.overlayId + '_label_';
				var point = opts.marker.getPosition();
				lat = point.lat;
				lng = point.lng;
				// if(bmode) {
				// 	var latlng = self.g2b(point);
				// 	lat = latlng.lat;
				// 	lng = latlng.lng;
				// }
			}

			//'background-color:#ffcc00;border:1px solid #666;padding:0 2px;font-size:11px;width:60px;text-align:center;white-space:nowrap;'
			var style = 'white-space:nowrap;font-weight:normal;';
			if(opts.style) {
				//转换驼峰式命名到普通命名
				$.each(opts.style, function(k,v) {
					var key = k.replace(/([A-Z])/,'-$1').toLowerCase();
					style += key + ':' + v + ';';
				});
			}
			var _opt_ = {
				size: new BMap.Size(0,15)	//控制高度
                ,style: style
                ,text: opts.html
                ,isLabel: true
            };
            if(opts.offset && opts.offset.length == 2) {
            	_opt_.anchor = new BMap.Size(-opts.offset[0],-opts.offset[1]);
            }
            if(opts.nodraw) _opt_.batchMode = true;
			var label = new BMapLib.SimpleMarker(new BMap.Point(lng,lat),_opt_);
            self.map.addOverlay(label);
		} else {
	        var _opt_ = {};
	        if(!opts.marker) {
	            _opt_.position = new BMap.Point(lng,lat);
	        }
	        if(opts.offset && opts.offset.length == 2) _opt_.offset = new BMap.Size(opts.offset[0],opts.offset[1]);
	        var label = new BMap.Label(opts.html,_opt_);
	        if(opts.style) label.setStyle(opts.style);
	        if(opts.marker) {
	            opts.marker.setLabel(label);
	        } else {
	            self.map.addOverlay(label);
	        }
        }
        label.bmode = bmode;
        label.overlayId = overlayId;
        label.__nowdisplay = 'show';
        if(opts.display == 'hide') {
        	label.hide();
        	label.__nowdisplay = 'hide';
        }
        // 放入所有标点
		self.overlays[overlayId] = label;
        return label;
    },
    updateLabel: function(opts) {
        if(!opts.overlayId) return;
        var self = this,
            overlayId = self._getOverlayId(opts.overlayId,'label'),
            bmode = opts.bmode || false;
        var obj = self.get(overlayId);
        if(obj) {
            if(opts.lat && opts.lng) {
                var lat = opts.lat, lng = opts.lng;
                //更新位置
                if(!bmode) {
                    var latlng = self.g2b(opts);
                    lat = latlng.lat;
                    lng = latlng.lng;
                }
                obj.setPosition(new BMap.Point(lng,lat));
            }
            if(opts.html) {
            	if(obj instanceof BMapLib.SimpleMarker) obj.setText(opts.html);
            	else if(obj instanceof BMap.Label) obj.setContent(opts.html);
            }
            if(opts.display) {
            	var display = opts.display || 'show';
				if(display == 'toggle') {
					var nowdisplay = obj.__nowdisplay || 'show';
					if(nowdisplay == 'hide') display = 'show';
					else if(nowdisplay == 'show') display = 'hide';
				}
				if(display == 'show') obj.show();
				else if(display == 'hide') obj.hide();
				obj.__nowdisplay = display;
            }
            return obj;
        }
        return false;
    },

	/**
	 * 添加折线
	 * line: {
	 * 		overlayId: "line001",									// 标记的唯一ID
	 * 		latlngs: '116.378517,39.878918;116.3871,39.913423;116.362724,39.923429;',	// 折线格式必须一致 也可传顶点数组 [[39.9493, 116.3975], [39.9593, 116.4071]]
	 *  	color: "#CC00FF",										// color 是一个字符串，包含十六进制数字、HTML 样式的颜色，如 #RRGGBB
	 *  	weight: 2,												// 线条宽度
	 *  	opacity: 1,												// 线条透明度
	 *  	encoder: {				// 编码折线，有值则忽略latlngs属性
	 *  		points: "yzocFzynhVq}@n}@o}@nzD",
	 *  		levels: "BBB",
	 * 			zoomFactor: 32,				// 编码的 levels 字符串中相邻缩放级别组合之间的倍数
	 * 			numLevels: 4				// 是已编码的 levels 字符串中包含的缩放级别数
	 * 		}
	 * 		onclick: function(latlng) {},							// 点击事件
	 *  	onsuccess: function(line) {},							// 创建成功事件
	 * 		bmode: false											// *** bmode为true,经纬度需要转换,默认为false ***
	 *  }
	 */
	polyline: function(opts) {
		if(!opts.latlngs && !opts.encoder ) return;
		var self = this,
			overlayId = self._getOverlayId(opts.overlayId,'polyline'),
			color = opts.color || 'green',
			weight = opts.weight || 5,
			opacity = opts.opacity || 0.7,
			bmode = opts.bmode || false,
			encoder = opts.encoder,
			strokestyle = opts.strokestyle || 'solid';
		var plarr = new Array();
		if(encoder) {
			//编码折线
			plarr = self._decodePath(encoder.points,bmode);
		} else {
			var latlngs = opts.latlngs;
			if('string' == typeof latlngs) latlngs = self._points2arr(latlngs,true);
			for(var i = 0; i < latlngs.length; i++) {
				var lng = latlngs[i][1],
					lat = latlngs[i][0];
				if(!bmode) {
					var latlng = self.g2b({lat:lat,lng:lng});
					lat = latlng.lat;
					lng = latlng.lng;
				}
				var point = new BMap.Point(lng,lat);
				plarr.push(point);
			}
		}

		if(plarr.length) {
			var polyline = new BMap.Polyline(plarr,{strokeColor:color, strokeWeight:weight, strokeOpacity:opacity, strokeStyle:strokestyle});
			polyline.overlayId = overlayId;
			self.map.addOverlay(polyline);
			// 放入所有标点
			self.overlays[overlayId] = polyline;
			//事件
			if(opts.onclick) {
				polyline.addEventListener('click',function() {
					if(arguments[0]) {
						var point = arguments[0].point;
						if(point) opts.onclick({lat:point.lat,lng:point.lng});
					}
				});
			}
			if(opts.onsuccess) {
				opts.obj = polyline;
				opts.onsuccess(opts);
			}
			if(polyline && 'undefined' != typeof opts.autoview && opts.autoview) {
				//自动适应地图
				var path = polyline.getPath();
				var viewport = self.map.getViewport(path);
				if(viewport) {
					self.map.setCenter(viewport.center);
					self.map.setZoom(viewport.zoom);
				}
			}
			return polyline;
		}
		return false;
	},
	getPolylinePoints: function(polyline,bmode) {
		if(polyline) {
			var self = this,
				points = '',
				bmode = bmode || false;
			var paths = polyline.getPath();
			if(paths && paths.length) {
				for(var i = 0; i < paths.length; i++) {
					var lat = paths[i].lat,
						lng = paths[i].lng;
					if(!bmode) {
						//返回的是转换成google的经纬度
						var latlng = self.b2g({lat:lat,lng:lng});
						lat = latlng.lat;
						lng = latlng.lng;
					}
					points += lng+','+lat+';';
				}
			}
			return points;
		}
		return false;
	},
	//解析编码折线, 目前用于跟踪页面连接当前点和最后一点//BUG #3698
	decodePath: function(encoderpoints, bmode) {
		var bmode = bmode || false;
		return this._decodePath(encoderpoints,bmode);
	},
	/**
	 * Decode Encoded Latitudes and Longitudes String
	 * http://code.google.com/apis/maps/documentation/utilities/polylinealgorithm.html
	 */
	_decodePath:		function(str,bmode) {
		var bmode = bmode || false;
		var b = str ? str.length : 0,
			c = Array(Math.floor(str.length /2)),
			d = 0,e = 0, f = 0, g = 0;
		for (; d < b; ++g) {
			var h = 1,n = 0,q;
			do q = str.charCodeAt(d++) - 63 - 1, h += q << n, n += 5;
			while (q >= 31);
			e += h & 1 ? ~ (h >> 1) : h >> 1;
			h = 1;
			n = 0;
			do q = str.charCodeAt(d++) - 63 - 1, h += q << n, n += 5;
			while (q >= 31);
			f += h & 1 ? ~ (h >> 1) : h >> 1;
			var latlng = {lat: e * 1.0E-5, lng: f * 1.0E-5};
			if(!bmode) latlng = this.g2b(latlng);
			c[g] = new GLatLng(latlng.lat, latlng.lng, !0);
		}
		c.length = g;
		return c;
	},
	/**
	 * Decode Encoded Levels String
	 */
	_decodeLevel:		function(str) {
		var a = [];
		for (var i = 0; i < str.length; ++i) {
		var level = str.charCodeAt(i) - 63;
		a.push(level);
		}
		return a;
	},

	//打开地图信息窗口
	openInfoWindowHtml: function(html,point,bmode) {
		var self = this,
			bmode = bmode || false,
			lat = point.lat,
			lng = point.lng;
		if(!bmode) {
			var latlng = self.g2b({lat:lat,lng:lng});
			lat = latlng.lat;
			lng = latlng.lng;
		}
		var _BPoint = new BMap.Point(lng,lat);
		var infoWinOpt = {
			enableMessage : false
		};
		if(point.offset) {
			infoWinOpt.offset = new BMap.Size(point.offset[0],point.offset[1]);
		}
		var infoWindow = new BMap.InfoWindow(html, infoWinOpt);
        self.map.openInfoWindow(infoWindow,_BPoint);
	},

	//关闭信息窗口
	closeInfoWindow: function() {
		var self = this,
			map = self.map;
		map.closeInfoWindow();
	},

    //获取打开的地图信息窗口
    getInfoWindow: function() {
        return this.map.getInfoWindow();
    },

	//初始化测距工具
	initDistanceTool: function(callback) {
		if(!this._distancetool) {
			this._distancetool = new BMapLib.DistanceTool(this.map);
			if(callback) this._distancetool.addEventListener('ondrawend',callback);
		}
	},
	//测距打开
	distanceToolOpen: function() {
		this._distancetool.open();
	},
	//测距关闭
	distanceToolClose: function() {
		this._distancetool.close();
	},
	//初始化画框工具
	initRectTool: function(title,callback) {
		var title = title || '框选',
			callback = callback || false;
		//画框工具
		if(!this._recttool) {
			this._recttool = new BMapLib.SearchInRectangle(this.getMap(),{
				renderOptions:{
					map: this.getMap(),
					strokeWeight: 3,
					strokeColor: "red",
					fillColor:"white",
					opacity: 0.5,
					followText: title,
					autoClose: true,
					autoViewport: false,
					alwaysShowOverlay: false,
					callback: function(bds) {
						if(callback) callback(bds);
					}
				}
			});
			this._recttool.setLineStyle("dashed");
		}
	},
	//画框工具打开
	rectToolOpen: function() {
		this._recttool.open();
	},
	//画框工具关闭
	rectToolClose: function() {
		this._recttool.close();
	},
	getLatLng: function(lat,lng) {
		return new BMap.Point(lng,lat);
	},

	hidePanes: function(forced) {
		// firefox已经进行了批量dom优化，不需要隐藏
		if (forced || !$.browser.mozilla) {
			// log.debug("hidePanes");
		    var panes = this.map.getPanes();
		    panes.markerPane.style.display = "none";
		    panes.markerShadow.style.display = "none";
		    panes.markerMouseTarget.style.display = "none";  
		    panes.labelPane.style.display = "none";  
		    panes.mapPane.style.display = "none";  
	    }
	},

	showPanes: function(forced) {
		if (forced || !$.browser.mozilla) {
			// log.debug("showPanes");
		    var panes = this.map.getPanes();
		    panes.markerPane.style.display = "";
		    panes.markerShadow.style.display = "";
		    panes.markerMouseTarget.style.display = "";
		    panes.labelPane.style.display = "";  
		    panes.mapPane.style.display = "";  
		}
	},

	_events: [],

	/**
	 * 给地图/覆盖物添加事件
	 * @param eventtype 事件类型
	 * @param target 目标
	 */
	addEvent: function(eventtype,callback,target) {
		var self = this,
			target = target || {};
		var obj = target._obj || self.map;
		var listener = obj.addEventListener(eventtype,callback);
		self._events.push({
			eventtype : eventtype,
			listener : callback,
			obj : obj
		});
		return true;
	},

	removeEvent: function(eventtype,target) {
		var self = this,
			target = target || {};
		var obj = target._obj || self.map;
		for (var i = self._events.length - 1; i >= 0; i--) {
			if('undefined' == typeof self._events[i]){
				return;
			}
			if(self._events[i].eventtype == eventtype && self._events[i].obj == obj) {
				log.debug(obj,eventtype,self._events[i].listener);
				obj.removeEventListener(eventtype,self._events[i].listener);
				delete self._events[i];
			}
		};
		return false;
	},

	setMapType: function(maptype) {
		var self = this,
			maptypes = {
				'NORMAL_MAP' : 'BMAP_NORMAL_MAP',
				'HYBRID_MAP' : 'BMAP_HYBRID_MAP'
			};
		if(maptypes[maptype]) {
			var type = eval(maptypes[maptype]);
			self.map.setMapType(type);
			return true;
		}
		return false;
	}
});
})(jQuery,imap);

(function(window) {
	var TO_BLNG = function(lng){return parseFloat(lng)+0.0065;};
	var TO_BLAT = function(lat){return parseFloat(lat)+0.0060;};
	var TO_GLNG = function(lng){return parseFloat(lng)-0.0065;};
	var TO_GLAT = function(lat){return parseFloat(lat)-0.0060;};

	// 百度仿google事件
	window.GEvent = new Object();
	window.GEvent.__events__ = new Array();
	window.GEvent.addListener = function(src,event,handler) {
		if(src.addEventListener) {
			src.addEventListener(event,handler);
			window.GEvent.__events__.push({event:event,handler:handler,src:src});
			return handler;
		}
		return false;
	};
	window.GEvent.removeListener = function(handler) {
		for(var i=0, len = window.GEvent.__events__.length; i<len; ++i) {
			var o = window.GEvent.__events__[i];
			if(o.handler !== handler) continue;
			if(o.src.removeEventListener) {
				o.src.removeEventListener(o.event,o.handler);
				window.GEvent.__events__.splice(i,1);
				return;
			}
		}
	};

	// //经纬度对象
	// BMap.Point.prototype.getLat = function() {
	// 	return this.lat;
	// };
	// BMap.Point.prototype.getLng = function() {
	// 	return this.lng;
	// };
	// //point.toUrlValue
	// BMap.Point.prototype.toUrlValue = function(i) {
	// 	var i = Number(i) || 6, j = Math.pow(10,i);
	// 	return Math.round(this.lat * j) / j + ',' + Math.round(this.lng * j) / j;
	// };
	// window.GLatLng = function(lat,lng) {
	// 	this.lat = lat;
	// 	this.lng = lng;
	// 	return this;
	// };
	// window.GLatLng.prototype = BMap.Point.prototype;

	// //标注点对象
	// //BMap.Marker.prototype.getLatLng = BMap.Marker.prototype.getPosition;
	// //BMap.Marker.prototype.setLatLng = BMap.Marker.prototype.setPosition;
	// // TASK #1111 dupeng
	// BMap.Label.prototype.getLatLng = 
	// BMap.Marker.prototype.getLatLng = function() {
	// 	var bmode = this.bmode || false,
	// 		oldpos = this.getPosition();
	// 		// log.debug('bmode: ' + bmode);
	// 	if(bmode) {
	// 		return oldpos;
	// 	} else {
	// 		return new BMap.Point(TO_GLNG(oldpos.getLng()),TO_GLAT(oldpos.getLat()));
	// 	}
	// }
	// BMap.Label.prototype.setLatLng = 
	// BMap.Marker.prototype.setLatLng = function(latlng,bmode) {
	// 	var bmode =  bmode ||this.bmode,
	// 		bmode = bmode || false;
	// 		newpos = null;
	// 	if(bmode) {
	// 		newpos = new BMap.Point(latlng.getLng(),latlng.getLat());
	// 	} else {
	// 		newpos = new BMap.Point(TO_BLNG(latlng.getLng()),TO_BLAT(latlng.getLat()));
	// 	}
	// 	this.setPosition(newpos);
	// }
	// BMap.Marker.prototype.setImage = function(url) {
	// 	var newicon = this.getIcon();
	// 	newicon.setImageUrl(url);
	// 	this.setIcon(newicon);
	// };
	// //opt暂不处理
	// BMap.Marker.prototype.openInfoWindowHtml = function(content,opt) {
	// 	var infoWin = new BMap.InfoWindow(content);
	// 	this.openInfoWindow(infoWin);
	// };

 //    BMap.Marker.prototype.getPixel = function() {
 //    	if(!this.getMap()) return;
 //        if('undefined' == typeof this._pixelPosition || !this.isVisible()) {
 //            var pixelPosition = this.getMap().pointToOverlayPixel(this.getPosition()); 
 //            this._pixelPosition = pixelPosition;
 //        }
 //        return this._pixelPosition;
 //    };

	// //线
	// BMap.Polyline.prototype.getVertexCount = function() {
	// 	return this.getPath().length;
	// };

	// //Bounds
	// BMap.Bounds.prototype.containsLatLng = BMap.Bounds.prototype.containsPoint;

	// //地图
	// BMap.Map.prototype.openInfoWindowHtml = function(latlng,html) {
	// 	var infoWin = new BMap.InfoWindow(html),
	// 		newpos = new BMap.Point(TO_BLNG(latlng.getLng()),TO_BLAT(latlng.getLat()));
	// 	this.openInfoWindow(infoWin,newpos);
	// };
	/*BMap.Map.prototype.setCenter = function(point,zoom) {
		this.setCenter(point);
		this.setZoom(zoom);
	}*/
})(window);
