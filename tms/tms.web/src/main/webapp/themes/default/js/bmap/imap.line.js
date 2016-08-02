/**
 * 地图路径显示和编辑 - hanjk
 * baidu modify by dupeng
 *
 * @param object
 *            huimap 地图对象
 * @param object
 *            options 显示区域选项 options : { minzoom : 7, editable : false }
 *
 */
imap.line = function(huimap, options) {
	if (!huimap) {
		$ips.alert('请传入地图参数');
		return;
	}
	this.huimap = huimap;
	this.huimapObj = this.huimap.ditu('getMap');
	if(options.line.color && options.line.color.indexOf("#")<0){
		options.line.color = "#"+options.line.color;
	}
	$.extend(this.options, options);
	// 初始化imap对象
	this._create();
}

$win = false;
imap.line.prototype = {
	huimap : null, // hui地图对象
	huimapObj : null, // 地图对象
	curLine : null, // 当前选中的线路
	overlayPre : 'line_',
	color : '#0000FF',// 线路的颜色
	editcolor : '#FF33FF',// 编辑的时候改变线路的颜色
	options : {
		minzoom : 7, // 缩放级别必须大于该值, 若缩放级别小于等于该值, 则不获取区域
		editable : false, // 是否对区域编辑
		enable : false,
		line: {}, 
		info        : {}//信息填充,{"start" : function()},字段对应的信息
	},
	_addMListener : null,
	_nowOp : null,
	_curIndex   : 0, //当前路径点的序号,创建完一个后自增
	_lineIndex	: 0,//当前线段的序号
	_create : function() {
		var self = this;
		var line = self._initLineDate();
		self._showPolyline(line);
	},
	/**
	 * 设置 通过经纬度 组成的路线
	 */
	_showPolyline : function(line) {
		var self = this, line = line || self.curLine;
		if(!line.lnglats){
			return false;
		}
		var polyline = {
			overlayId : self.overlayPre + line.id,
			latlngs : line.lnglats,
			color : line.color,
			weight : 4,
			bmode: true
		};
		var plineobj = self.huimap.ditu('polyline', polyline);
		line.gpolys.push(plineobj);
		self.curLine = line;
		self._setSEmarker(line);

	},
	_setSEmarker	:function(line){
		var self = this;
		var lnglats = line.lnglats;
		var arr_lnglat = lnglats.split(";");
		var s_lnglat = arr_lnglat[0];
		var e_lnglat = arr_lnglat[arr_lnglat.length-1];
		var a_s_lnglat = s_lnglat.split(",");
		var a_e_lnglat = e_lnglat.split(",");
		line.start = {lng:a_s_lnglat[0],lat:a_s_lnglat[1],pid:"start"};
		line.end = {lng:a_e_lnglat[0],lat:a_e_lnglat[1],pid:"end"};
		if(line.start.lat){
			var op = self._getMarkerPoint(line.start);
			self.huimap.ditu('marker', op);
			 
		}
		if(line.end.lat){
			var op = self._getMarkerPoint(line.end);
			self.huimap.ditu('marker', op);
		}
	},
	
	/**
	*	添加一个标注点
	*	
	**/
	addMarker : function(point) {
		var self = this,line = self.curLine,curIndex = self._curIndex;
		if(!curIndex){
			self._newLine();
		}
		if(curIndex>19){
			$ips.alert("最多20个点");
			return;
		}
		self.editPoint(point);
	},
	/**
	 * point {lat,lng,radius,address} ,google经纬度
	 */
	editPoint : function(point) {
		var self = this,curIndex = self._curIndex;
		if(null==self.curLine){
		 	self._newLine();
		}
		var index = curIndex || 0;
		line = self.curLine;
		imap.lockedOverlay = 'line';
		if(point){//传输来的点,设置 起始点
			var glnglat = {lat:point.lat, lng:point.lng};
			var blnglat = self.huimap.ditu("g2b",{lat:glnglat.lat, lng:glnglat.lng});
			self._editPoint(new BMap.Point(blnglat.lng,blnglat.lat), index, point.address);
		}else{//手工创建
			imap.setMapCursor('point');
			self.clearAddListener();
			self._addMListener = GEvent.addListener(self.huimapObj, 'click', function(e) {
				self.clearAddListener();
				imap.setMapCursor("move");// 改变鼠标手势
				self._editPoint(e.point, index);
			});
		}
		
	},

	/*
	 * marker 为空,即为添加,否则为修改
	 * 序号
	 */
	_editPoint : function(lnglat, index,address) {
		var self = this, line = self.curLine;
		if (!line)
			return;
		var point = {lat:lnglat.lat, lng:lnglat.lng, pid:index};
		var op = self._getMarkerPoint(point);
		marker = self.huimap.ditu('marker', op);
		var markpoint = self._setMarker(index,marker);
		if(address){
			markpoint.address = address;
		}
		self.updateMarkerInfo(index,markpoint);
		if(index > 0){
			if(self.options.line.ishighway == 0){
				line.bDir.setPolicy(BMAP_DRIVING_POLICY_LEAST_TIME);
			}
			else{ //避开高速
				line.bDir.setPolicy(BMAP_DRIVING_POLICY_AVOID_HIGHWAYS);
			}
			//得到开始经纬度,和结束经纬度
			self._lineIndex = index-1;
			line.bDir.search(line.waypoints[index-1].point, line.waypoints[index].point);
		}
		self._curIndex++;
	},
	/**
	* 设置标注点信息
	**/
	_setMarker:function(pid,marker){
		var self = this,
			line = line || self.curLine;
		var _marker = {};
		_marker["point"] = marker.getPosition();	
		_marker["pid"] = pid;
		_marker["mkobj"] = marker;
		line.waypoints[pid] = _marker;
		return _marker;
	},
	/**
	* 设置路径点信息
	**/
	updateMarkerInfo	: function(pid,marker){
		var self = this;
		var htmlInfo = self.options.info.waypoint;
		if(!htmlInfo){
			return;
		}
	 	var gpoint = self.huimap.ditu('b2g',marker.point);
	 	marker.gpoint = gpoint;
		var sglatlng = gpoint.lat + ',' + gpoint.lng;
		if(marker.address){
			htmlInfo.infoCallback(marker);
		}else{
			$ips.geo(sglatlng, (function(hInfo,m){
				return function(data){
					if ('undefined' != typeof(data['poilist'])) {
						var p = data['poilist'][0];
						m.address = p.address;
					} 
					hInfo.infoCallback(m);
				}
				})(htmlInfo,marker));
		}
	},
	/**
	 * 从新加载线路,当更改路线方式的时候
	 */
	reloadLine : function(ishighway) {
		var self = this;
		var line = self.curLine;
		if(!line.waypoints || line.waypoints.length<1){
			return ;
		}
		var hvy = (ishighway == 0) ? true : false;
		line.ishighway  = hvy;
		if(hvy){
			line.bDir.setPolicy(BMAP_DRIVING_POLICY_LEAST_TIME);
		}
		else{
			line.bDir.setPolicy(BMAP_DRIVING_POLICY_AVOID_HIGHWAYS);
		}
		//重新加载
		line.bDir.clearResults();
		$.each(line.gpolys,function(key,gpoly){
			self.huimapObj.removeOverlay(gpoly);
		})
		$.each(line.waypoints,function(key,waypoint){
			waypoint.isSearch = true;
		})
		self._markerDragend(self,line.waypoints[0]);
	},

	clearAddListener : function() {
		var self = this;
		if (self._addMListener)
			GEvent.removeListener(self._addMListener);
	},

	/**
	 * 编辑线路 进入编辑页面
	 */
	editLine : function() {
		var self = this, line = self.curLine;
		self._route2my();
		if(line.waypoints.length<2||line.gpoly==null||line.gpoly.getPath().length<2){
			$ips.alert("请添加终点");
			return;
		}
		self._nowOp = "edit";
		line.name = $("#name").val();
		self.setMapEvent();
	},
	updateOverlayColor  : function(color,line) {
        var self = this,
            line = line || self.curLine;
        line.color = color;
        if(line && line.gpolys){
			$.each(line.gpolys,function(key,gpoly){
				gpoly.setStrokeColor(color);
			})
        }
        
    },
    closeInfoWindow : function() {
		var self = this;
		self.huimapObj.closeInfoWindow();
	},
	/**
	* 初始化线路数据,
	* 将传输过来的 g经纬度转为b经纬度
	*/
	_initLineDate:function(){
		var self = this;
		var oline = self.options.line;
		if(!oline){
			return;
		}
		if(oline.glnglats){
			oline.lnglats = self.huimap.ditu("strg2b",oline.glnglats);
		}
		if(oline.start.glng){
			var lnglat = self.huimap.ditu("g2b",{lng:oline.start.glng,lat:oline.start.glat});
			oline.start.lng = lnglat.lng;
			oline.start.lat = lnglat.lat;
		}
		if(oline.end.glat){
			var lnglat = self.huimap.ditu("g2b",{lng:oline.end.glng,lat:oline.end.glat});
			oline.end.lng = lnglat.lng;
			oline.end.lat = lnglat.lat;
		}
		return oline;
	},
	 /**
	 * 退出编辑
	 */
	cancelEdit : function() {
		var self = this, line = self.curLine;
		if (!line)
			return;
		self.removeLine(line);
		line = null;
		self._curIndex = 0;
		self._lineIndex = 0;
		self.clearAddListener();
		// 还原到最初状态
		self._nowOp = null;
        imap.lockedOverlay = null;
	},
	/**
	* 删除线路
	*/
	removeLine : function(line) {
		var self = this, line = line || self.curLine;
		if(line.bDir){
			line.bDir.clearResults();
			line.bDir = null;
		}
		self.huimap.ditu("clearOverlays");
		line = null;
	},
	/**
	* 初始化线路
	*/
	_newLine : function(linfo) {
		var self = this,
			linfo = linfo || self.options.line;
		self._nowOp = 'add';
		imap.lockedOverlay = 'line';
		// 1:创建点
		var	line	=	{
			id	:	'new',
			color   :   '',
			name	:	'',
			savetype	:	0,
			gpolys	:	[],//多个polyline
			waypoints	:	[],//途经点
			distobj	:	{lineDist:[],dist:0},//每个线段的距离,,dist总距离
		};
		line = $.extend(true,line,linfo);
		self.curLine = line;
		// 设置线路
		line.bDir = new BMap.DrivingRoute(self.huimapObj,
				{renderOptions:{map: self.huimapObj, autoViewport: false, enableDragging: false}}
			);
		//结果完成
		line.bDir.setSearchCompleteCallback((function(self) {
			return function(result) {
				var plan = result.getPlan(0);
				if(!plan) {
					$ips.alert('查找失败, 没有查到相应的路线, 请重新修改起点终点');
					return;
				}
				var lineIndex = self._lineIndex;
				
				var dist = plan.getDistance(false);
				var dis = Math.round(dist / 10) / 100;
				line.distobj.lineDist[lineIndex] = dis;
				line.distobj.dist = 0;
				$.each(line.distobj.lineDist,function(key,dist){
					line.distobj.dist += dist ;
				})
				
			}
		})(self));
		line.bDir.setPolylinesSetCallback((function(self) {
			return function(routes) {
				var polyline = routes[0].getPolyline();
				var path = routes[0].getPath();
				/*
				*清理自动生成的线路,然后重新画
				*/
				var lineIndex = self._lineIndex;
				//删除之前生成的
				if(line.gpolys[lineIndex]){
					self.huimapObj.removeOverlay(line.gpolys[lineIndex]);
				}
				self._addPolyline(path,line,lineIndex);
				//删除线路生成的
				self.huimapObj.removeOverlay(polyline);
				
				//设置里程信息,起点终点
				var lineInfo =  self.options.info.line;
				lineInfo.infoCallback(line)

				var nextPointIndex = lineIndex + 2,
				waylength = line.waypoints.length;
				if(nextPointIndex < waylength){
					var waypoint = line.waypoints[lineIndex + 1];
						next_waypoint = line.waypoints[nextPointIndex];
					if(next_waypoint.isSearch){
						self._lineIndex = lineIndex +1;
						line.bDir.search(waypoint.point,next_waypoint.point);
					}
				}
			}
		})(self));
		line.bDir.setMarkersSetCallback((function(self) {
			return function(arr) {
				/**
				*移除地图自动生成的marker,起点终点
				**/
				var startPoint = arr[0],
					endPoint = arr[arr.length-1];
				self.huimapObj.removeOverlay(startPoint.marker);
				self.huimapObj.removeOverlay(endPoint.marker);
			}
		})(self));
		return line;
	},
	 /*
	 * 设置关键点
	 */
	_getMarkerPoint : function(pp) {
		var self = this;
		var m = {
			overlayId : 'line_marker_' + pp.pid,
			lat : pp.lat,
			lng : pp.lng,
			pid : pp.pid,
			bmode	: true,
			icon : self._getMarkerIcon(pp.pid),
			draggable : false
		};
		if(!isNaN(parseInt(pp.pid))){
			m.draggable = true;
			//从新计算线路
			m.ondragend = (function(s){
				return  function(lnglat){
					var waypoint = line.waypoints[this.pid];
					waypoint.point = new BMap.Point(lnglat.lng,lnglat.lat);
					waypoint.mkobj.setPosition(lnglat);
					self._markerDragend(s,waypoint)
				}
			})(self)
		}
		return m;
	},
	/*
	* iline,当前线路对象
	* objmarker,当前标注点
	*/
	_markerDragend	: function(self,waypoint){
		var index = waypoint.pid;
		var preIndex = index -1,
			nextIndex = index + 1,
			line = self.curLine;
			waylength = line.waypoints.length;
		if(index > 0){
			var pre_waypoint = line.waypoints[preIndex];
			self._lineIndex = preIndex;
			line.bDir.search(pre_waypoint.point,waypoint.point);
		}
		if(nextIndex<waylength){
			var next_waypoint = line.waypoints[nextIndex];
			if(index==0){
				self._lineIndex = index;
				line.bDir.search(waypoint.point,next_waypoint.point);
			}else{
				next_waypoint.isSearch = true;
			}
		}
	},
	_getMarkerIcon : function(pid) {
		var self = this;
		var image = self._getMarkerImage(pid);
		var ix = 23;
		if(!isNaN(parseInt(pid))){
			ix = 27;
		}
		return {
			image : '/css/skin/map_skin/'+image,
			iconsize : [ix,36],
			iconanchor : [9, 34],
			infowindowanchor : [9, 2]
		};
	},
	/**
	* 根据pid 得到图片
	*/
	_getMarkerImage	: 	function(pid){
		if(pid == 'start' || pid == 'end') {
			 return 'mark_lines_'+pid+'.png';
		}
		pid = parseInt(pid);
		pid = pid + 1;
		if(pid < 10){
			pid = "0" + pid;
		}
		return "yellow"+ pid +".png";
	},
	_addPolyline 	: function(path,line,plineIndex){
		var self = this;
		var zip_path =  BDouglasPeucker(path,10);
		var glnglats = self.huimap.ditu("arrb2g",zip_path);
		var polyline = new BMap.Polyline(path,{strokeColor:line.color});     
        self.huimapObj.addOverlay(polyline);
        polyline.glnglats = glnglats;
        line.gpolys[plineIndex] = polyline;
	},
};