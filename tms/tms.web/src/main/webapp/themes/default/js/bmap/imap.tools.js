/**
 * 地图右上角显示的地图工具 - dupeng 2011/11/24
 * @param object huimap 地图对象
 * @param array tools 显示的控件名称 ['move','zoomin','zoomout','fullview','distance','drawrect','marker','polygon','line','maptype']
 */

imap.tools = function(id,huimap,tools) {
    if(!id || !huimap || !tools) {
		$ips.error('error');
        return;
    }
	this.id = id;
    this.huimap = huimap;
    this.huimapObj = this.huimap.ditu('getMap');
    this.tools = tools;
    //初始化imap对象
    this._create();
}

imap.tools.prototype = {
	id			: null,
    huimap      : null,     //hui地图对象
    huimapObj   : null,     //地图对象
	_now		: null,		//当前选中
	_trafficLayer	: null,		//交通流量图层
	_default	: {
		lat		: 34.45,
		lng		: 110.21,
		zoom	: 5
	},
	_create		: function() {
		//初始化
		var self = this,
			id = self.id,
			tools = self.tools,
			_tools = self._tools;
		self._contenters = {map_tools:[],map_mark:[],map_traffic:[],none:[]};
		for(var i=0; i<tools.length; i++) {
			var _t = _tools[tools[i]];
			if(_t) {
				var _ct = _t.contenter;
				var li = self._createli(tools[i],_t);
				self._contenters[_ct].push(li);
			}
		}
		$.each(self._contenters,function(k,v) {
			if(v.length) {
				var ul = $('<ul class="'+k+'"></ul>');
				for(var j=0; j<v.length; j++) {
					ul.append(v[j]);
				}
				$('#'+id).prepend(ul);
			}
		});
		$('#itools_move').addClass('active');
	},
	_createli	: function(name,opts) {
		var self = this;
		var li = $('<li id="itools_'+name+'"></li>'),
			inhtml = opts.html || '<span class="'+opts.cls+'"></span>',
			a = $('<a href="javascript:void(0);" onclick="itools._click(this)" title="'+opts.tt+'">'+inhtml+'</a>');
		li.append(a);
		return li;
	},
	setToMove	: function() {
		$('#itools_move a').click();
		this._tools.drawrect._drawrect_ = 'close';
		this._tools.replaydistance._replaydistance_ = 'close';
		this._tools.newzoomin._drawrect_ = 'close';
		this._tools.newzoomout._drawrect_ = 'close';
		//定时定点查车工具
		this._tools.drawrect_2._drawrect_ = 'close';
		this._now = 'move';
	},
	getCurrTool : function() {
		return this._now;
	},
	_click		: function(t) {
		var self = itools, mapObj = self.huimapObj;
		if(self._now) {
			$('#itools_'+self._now).removeClass('active');
			switch(self._now) {
				case 'distance':
					//关闭测量工具
					self.huimap.ditu('distanceToolClose');
					$('#itools_move').addClass('active');
					break;
				case 'replaydistance':
					$('#itools_move').addClass('active');
					break;
				case 'drawrect':
					//关闭画框工具
					imap.drawrect.close();
					$('#itools_move').addClass('active');
					break;
				//定时定点查车
				case 'drawrect_2':
					//关闭画框工具
					imap.drawrect.close();
					$('#itools_move').addClass('active');
					break;	
				case 'traffic':
					//关闭交通流量图层
                    if(self._trafficLayer) {
                        //log.debug('关闭交通流量图层');
                        mapObj.removeTileLayer(self._trafficLayer);
                        self._trafficLayer = null;
                        //self._tools.traffic._traffic_ = 'close';
                    }
					$('#itools_move').addClass('active');
					break;
				case 'marker':
					imarker.clearAddListener();
					imap.setMapCursor('move');
					break;
				case 'polygon':
					ipolygon.clearAddListener();
					imap.setMapCursor('move');
					break;
				case 'line':
					iline.clearAddListener();
					imap.setMapCursor('move');
					break;
			}
		}

		var obj = $(t).parent('li');
		if(!obj) return;
		var type = obj.attr('id').replace('itools_','');
		self._now = type;
		obj.addClass('active');

		if(type == 'marker' || type == 'polygon' || type == 'line') {
			var islock = imap.lockedOverlay ? true : false;
			if(islock) {
				$ips.alert('当前页面正处于编辑状态,请先完成编辑或取消编辑后再试!');
				return;
			}
			$('#itools_move').removeClass('active');
			self.huimap.one('click',function() {
				$('#itools_'+type).removeClass('active');
				$('#itools_move').addClass('active');
			});
		}
		switch(type) {
			case 'zoomin':
				mapObj.zoomIn();
				obj.removeClass('active');
				$('#itools_move').addClass('active');
				break;
			case 'zoomout':
				mapObj.zoomOut();
				obj.removeClass('active');
				$('#itools_move').addClass('active');
				break;
			case 'fullview':
				//百度不关闭信息窗口,设置中心点有问题
				if(mapObj.getInfoWindow()) mapObj.closeInfoWindow();
				mapObj.setCenter(new BMap.Point(self._default.lng,self._default.lat));
				mapObj.setZoom(self._default.zoom);
				obj.removeClass('active');
				$('#itools_move').addClass('active');
				break;
			case 'distance':
				if(self._tools.distance._distance_ == 'close') {
					self._tools.distance._distance_ = 'open';
					//打开测量工具
					self.huimap.ditu('distanceToolOpen');
					$('#itools_move').removeClass('active');
				} else if(self._tools.distance._distance_ == 'open') {
					self._tools.distance._distance_ = 'close';
					//关闭测量工具
					self.huimap.ditu('distanceToolClose');
					obj.removeClass('active');
					$('#itools_move').addClass('active');
				}
				break;
			case 'replaydistance':
				if(self._tools.replaydistance._replaydistance_ == 'close') {
					self._tools.replaydistance._replaydistance_ = 'open';
					//打开测量
					_iTrack.addDisEvent();
					$('#itools_move').removeClass('active');
				} else if(self._tools.replaydistance._replaydistance_ == 'open') {
					self._tools.replaydistance._replaydistance_ = 'close';
					//关闭测量
					_iTrack.removeDisEvent();
					obj.removeClass('active');
					$('#itools_move').addClass('active');
				}
				break;
			case 'newzoomin':
			case 'newzoomout':
			case 'drawrect':
				if(self._tools[type]._drawrect_ == 'close') {
					self._tools[type]._drawrect_ = 'open';
					//打开测量工具
					imap.drawrect.open();
					$('#itools_move').removeClass('active');
				} else if(self._tools[type]._drawrect_ == 'open') {
					self._tools[type]._drawrect_ = 'close';
					//关闭测量工具
					imap.drawrect.close();
					obj.removeClass('active');
					$('#itools_move').addClass('active');
				}
				break;
			//定时定点查车
			case 'drawrect_2':
				if(self._tools[type]._drawrect_ == 'close') {
					self._tools[type]._drawrect_ = 'open';
					//打开测量工具
					imap.drawrect.open();
					$('#itools_move').removeClass('active');
				} else if(self._tools[type]._drawrect_ == 'open') {
					self._tools[type]._drawrect_ = 'close';
					//关闭测量工具
					imap.drawrect.close();
					obj.removeClass('active');
					$('#itools_move').addClass('active');
				}
				break;	
			case 'traffic':
				if(self._tools.traffic._traffic_ == 'close') {
					self._tools.traffic._traffic_ = 'open';
					//打开交通流量
                    //log.debug('打开交通流量图层');
                    if(!self._trafficLayer) self._trafficLayer = new BMap.TrafficLayer();
                    mapObj.addTileLayer(self._trafficLayer);
					$('#itools_move').removeClass('active');
				} else if(self._tools.traffic._traffic_ == 'open') {
					self._tools.traffic._traffic_ = 'close';
					//关闭交通流量
                    if(self._trafficLayer) {
                        //log.debug('关闭交通流量图层');
                        mapObj.removeTileLayer(self._trafficLayer);
                        self._trafficLayer = null;
                    }
					obj.removeClass('active');
					$('#itools_move').addClass('active');
				}
				break;
			case 'marker':
				imarker.addMarker();
				break;
			case 'polygon':
				ipolygon.addNewPolygon();
				break;
			case 'line':
				iline.addNewLine();
				break;
			case 'maptype':
				self._tools.maptype._nowtype_ ++;
				var maptype = self._tools.maptype;
				var len = maptype._maptypes_.length; _index = maptype._nowtype_ % len, _nextindex = (maptype._nowtype_+1) % len;
				var _type = eval(maptype._maptypes_[_index]);
				mapObj.setMapType(_type);
				$('#itools_'+type+' a').html(maptype._mapChinese_[_nextindex]);
				obj.removeClass('active');
				$('#itools_move').addClass('active');
				break;
		}
	},
    tools		: null,
	_contenters	: {map_tools:[],map_mark:[],none:[]},
	_tools		: {
		move		: {
			cls			: 't_1',
			tt			: '移动地图',
			contenter	: 'map_tools'
		},
		zoomin		: {
			cls			: 't_2',
			tt			: '地图放大',
			contenter	: 'map_tools'
		},
		zoomout		: {
			cls			: 't_3',
			tt			: '地图缩小',
			contenter	: 'map_tools'
		},
		fullview	: {
			cls			: 't_6',
			tt			: '全景地图',
			contenter	: 'map_tools'
		},
		distance	: {
			cls			: 't_7',
			tt			: '地图测距',
			contenter	: 'map_tools',
			_distance_	: 'close'
		},
		replaydistance	: {
			cls			: 't_5',
			tt			: '回放测距',
			contenter	: 'map_tools',
			_replaydistance_	: 'close'
		},
		newzoomin	: {
			cls			: 't_2',
			tt			: '拉框放大',
			contenter	: 'map_tools',
			_drawrect_	: 'close'
		},
		newzoomout	: {
			cls			: 't_3',
			tt			: '拉框缩小',
			contenter	: 'map_tools',
			_drawrect_	: 'close'
		},
		drawrect	: {
			cls			: 't_4',
			tt			: '画框选择',
			contenter	: 'map_tools',
			_drawrect_	: 'close'
		},
		drawrect_2	: {
			cls			: 't_4_2',
			tt			: '定时定点查车',
			contenter	: 'map_tools',
			_drawrect_	: 'close'
		},
		marker		: {
			cls			: 'm_1',
			tt			: '标注点',
			contenter	: 'map_mark'
		},
		polygon		: {
			cls			: 'm_2',
			tt			: '标注区域',
			contenter	: 'map_mark'
		},
		line		: {
			cls			: 'm_3',
			tt			: '标注线',
			contenter	: 'map_mark'
		},
		traffic		: {
			cls			: 'm_4',
			tt			: '交通流量',
			contenter	: 'map_traffic',
			_traffic_	: 'close'
		},
		maptype		: {
			cls			: '',
			tt			: '切换地图类型',
			contenter	: 'none',
			html		: '卫星',
			_nowtype_	: 0,
			_maptypes_	: ['BMAP_NORMAL_MAP','BMAP_HYBRID_MAP'],
			_mapChinese_	: ['地图','卫星']
		}
	}
};
