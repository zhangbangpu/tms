imap.setMapCursor = function(type) {
	var cur = '';
    if(type == 'move') {
        cur = '';
    } else if(type == 'point') {
        cur = 'crosshair';
    }
    // $('#map div:first-child div:first-child').css('cursor',cur);
    imap.huimapObj.setDefaultCursor(cur);
};

// imap.setMarkerType = function(jqObj,category) {
// 	var self = this;
// 	self.sethtml = function(value) {
// 		var value = value || category;
// 		with(jqObj) {
// 			html("");
// 			append('<option value="0">--选择--</option>');
// 			$ips.load('dict','find',{fild:'value,text',type:'MAPMARKER_CATEGORY',oporgcode:$ips.userinfo.orgroot},function(data) {
// 				if(data.length>0){
// 					$.each(data,function(i,o) {
// 						var isselect = value == o.value ? 'selected' : '';
// 						append('<option '+isselect+' value="'+o.value+'">'+o.text+'</option>');
// 					});
// 				}
// 				append('<option style="color:red;" value="addoption">+新建类别</option>');
// 			});
// 		}

// 	};
// 	self.sethtml();
// 	jqObj.change(function() {
// 		var val = $(this).val();
// 		if(val == 'addoption') {
// 			var html = '<form name="form_add_category" id="form_add_category" >'
// 					   +'<table cellspacing="4" cellpadding="4" width="100%"><tr>'
// 					   +'<th align="right">标注点类型名称：</th>'
// 					   +'<td><input type="text" size="30"  name="categoryname" id="form_categoryname" style="width: 186px; height: 19px"/></td>'
// 					   +'</tr></table></form>';
// 			$ips.win(html,350,140,'添加',function(msg) { if(msg=='addcategory') {
// 				var modename = $('#form_categoryname').val();
// 				if(modename == '') {
// 					$ips.alert('类别不能为空');
// 					jqObj.val("0");
// 					return;
// 				}
// 				$ips.load("dict",'save',{text:modename,type:'MAPMARKER_CATEGORY','parentid':'0'},function(result) {
// 					if(result.code>0) {
// 						$ips.succeed("添加成功");
// 						self.sethtml(result.value);
// 					 } else {
// 						$ips.alert(result.message);
// 						jqObj.val("0");
// 					 }
// 				});
// 			} else {
// 				jqObj.val("0");
// 			} }, null, [['添加','addcategory']]);
// 		}
// 	});
// };

// 折线贴近
imap.routeNodes = [];
imap.gpoly=null;
imap.waypoints=[];
imap.myNode=null;
imap.huimap=null;
imap.huimapObj=null;
imap.setMyNode = function() {
	if (imap.myNode == null) {
		// 创建用于显示和拖动鼠标在显示时的路线标记
		var center = imap.huimap.ditu('getCenter');
		imap.myNode = imap.huimap.ditu('marker',{
			lat:center.lat,
			lng:center.lng,
			icon:{
				image: '/css/skin/map_skin/node.gif',
				iconsize: [10, 10],
				iconanchor: [5, 5],
				infowindowanchor: [5, 5]
			},
			draggable:true,
			onclick:function() {
				log.debug('===============');
				var gLatLng = imap.myNode.getPosition();
				imap.nodeclick(gLatLng.lat,gLatLng.lng,imap.myNode.keyindex);
			},
			onmouseover:function() {
				log.debug('===============');
				var gLatLng = imap.myNode.getPosition();
				imap.nodemouseover(gLatLng.lat,gLatLng.lng,imap.myNode.keyindex);
			},
			//added by caoyuling at 2013-8-13 #taskid = 1333
			onmouseout:function() {
				log.debug('===============');
				var gLatLng = imap.myNode.getPosition();
				imap.nodemouseout(gLatLng.lat,gLatLng.lng,imap.myNode.keyindex);
			}
			// bmode:true
		});
	}
	// imap.myNode.hide();
	imap.myNode.enableDragging();
};
imap._mapmousemoveevt = null;
imap._mapzoomendevt = null;
imap._mapmoveendevt = null;
imap.mapmoveend = function() {};
imap.nodeclick = function() {};
imap.nodehide = function() {};
imap.nodeshow = function() {};
imap.nodemouseover = function() {};
imap.nodemouseout = function() {};
imap.initLineNode = function(mappoints) {
	// var zoom = self.huimapObj.getZoom();
	imap.getPixPoints(mappoints);
	// log.debug(imap.routeNodes);
	imap._removeEvts();
	imap._mapmousemoveevt = GEvent.addListener(imap.huimapObj, 'mousemove', function(e) {
		// 如果鼠标在显示的线路上面,需要检测是当前编辑线路
		imap.getProximity(e);
	});
	imap._mapzoomendevt = GEvent.addListener(imap.huimapObj, "zoomend", function() {
		imap.mapmoveend();
	});
	imap._mapmoveendevt = GEvent.addListener(imap.huimapObj, "moveend", function() {
		imap.mapmoveend();
	});
};
imap.disableLineNode = function() {
	imap._removeEvts();
	if(imap.myNode) imap.myNode.hide();
};
imap._removeEvts = function() {
	if(imap._mapmousemoveevt) GEvent.removeListener(imap._mapmousemoveevt);
	if(imap._mapzoomendevt) GEvent.removeListener(imap._mapzoomendevt);
	if(imap._mapmoveendevt) GEvent.removeListener(imap._mapmoveendevt);
};
imap.getPixPoints = function(_points,zoom) {
	var map = imap.huimapObj;
	var zoom = zoom || map.getZoom();
	var last_point;
	var i = 0, routeNodes = [];
	for (var j = 0; j < _points.length; j++) {
		var point = map.pointToPixel(_points[j]);
		point.x = parseInt(0.5+point.x);
		point.y = parseInt(0.5+point.y);

		if (j==0 || last_point.x != point.x || last_point.y != point.y) {
			routeNodes.push(point.x);
			routeNodes.push(point.y);
			routeNodes.push(i); // store the index of polyline containing this node
			last_point = point;
		}
		i++;
	}
	if(routeNodes.length) imap.routeNodes = routeNodes;
	return routeNodes;
};
imap.getProximity = function(e) {
	var mouseLatLng = e.point;
	var point;
	var map = imap.huimapObj, myNode = imap.myNode,
		routeNodes = imap.routeNodes, gpoly = imap.gpoly, waypoints = imap.waypoints,
		zoom = map.getZoom();

	if(!myNode) {
		//生成标点
		imap.setMyNode();
	}

	if (routeNodes.length == 0) {
		var _points = gpoly.getPath();
		routeNodes = imap.getPixPoints(_points,zoom);
	}

	var l = routeNodes.length;

	if (!mouseLatLng || l <= 1) // no route is displayed or route is already being dragged
		return;

	var mousePx = map.pointToPixel(mouseLatLng);
	var mouseX = mousePx.x;
	var mouseY = mousePx.y;

	var x = routeNodes[0];
	var y = routeNodes[1];
	//我们将搜索最近点鼠标位置显示标记拖动
	var minX = x; // we will search closest point on the line to mouse position for displaying marker there available for dragging
	var minY = y;
	var minDist = 99999;

	for (var n = 3; n < l; n +=3) {
		var x0 = x;
		var y0 = y;
		x = routeNodes[n];
		y = routeNodes[n+1];

		if ((x < mouseX-50 && x0 < mouseX-50) || (x > mouseX+50 && x0 > mouseX+50)) {
			continue;
		}
		if ((y < mouseY-50 && y0 < mouseY-50) || (y > mouseY+50 && y0 > mouseY+50)) {
			continue;
		}

		var dx = x - x0;
		var dy = y - y0;
		var d = dx*dx + dy*dy; // lenght^2 of segment n

		var u = ((mouseX - x) * dx + (mouseY - y) * dy) / d; // a bit of vector algebra :)
		var x2 = x + (u*dx); // x,y coordinates in pixels of closest point to mouse in segment n
		var y2 = y + (u*dy);

		var dist = (mouseX - x2)*(mouseX - x2) + (mouseY - y2)*(mouseY - y2); // distance^2 from mouse to closest point in segment n

		if (minDist > dist) { // closest point in segment n is closest point overall so far
			var d1 = (mouseX - x0)*(mouseX - x0) + (mouseY - y0)*(mouseY - y0); // distance^2 from mouse to end of segment n in pixels
			var d2 = (mouseX - x)*(mouseX - x) + (mouseY - y)*(mouseY - y)

			if ((d1 - dist) + (d2 - dist) > d) { // closest point is outside the segment, so the real closest point is either start or end of segment
				//最近点跑到外面去了，所以是真正的最接近点段的开始或结束
				if (d1 < d2) {
					dist = d1;
					x2 = x0;
					y2 = y0;
				}
				else {
					dist = d2;
					x2 = x;
					y2 = y;
				}
			}
		}

		if (minDist > dist) { // closest point in segment n is closest point overall so far.最洁净的点
			minDist = dist;
			minX = x2;
			minY = y2;
			myNode.keyindex = routeNodes[n+2]; // remember index of segment closest to mouse,记得段指数最接近鼠标
		}
	}

	if (minDist > 2500) { // mouse is not close enough to the displayed line,鼠标距离线不够近,
		if(myNode) {myNode.hide();
			var gLatLng = myNode.getPosition();
			imap.nodehide(gLatLng.lat,gLatLng.lng,myNode.keyindex);
		} // do not display marker for dragging the polyline,不显示拖动的折线标记
	}
	else {
		for (n = waypoints.length; --n >= 0;) { // check if mouse is not too close to existing waypoints markers,
			//检查如果鼠标是不是太接近现有航点标记
			var markerPx = map.pointToPixel(waypoints[n].getPosition());

			dx = markerPx.x - minX;
			dy = markerPx.y - minY;

			if (dx*dx + dy*dy < 25) { // mouse is too close to existing marker,鼠标太接近现有的标记
				//不拖行显示额外的标记 - 用户拖动现有航点
				myNode.hide(); // do not show additional marker for dragging the line - the user is about to drag existing waypoint
				var gLatLng = myNode.getPosition();
				imap.nodehide(gLatLng.lat,gLatLng.lng,myNode.keyindex);
				return;
			}
		}

		myNode.setPosition(map.pixelToPoint(new BMap.Pixel(minX, minY)));
		myNode.show(); // display marker for dragging the polyline,显示拖动的折线标记
		var gLatLng = myNode.getPosition();
		imap.nodeshow(gLatLng.lat,gLatLng.lng,myNode.keyindex);
	}
};

imap.getSource = function() {
	var setkey = '_global_setting_source_';
	var source = $ips.cacheGet(setkey);
	if(source) {
		return source;
	}
	source = {
		truck: {
			show: false
		},
		marker: {
			show: false,
			add: false,
			edit: false,
			delete: false
		},
		polygon: {
			show: false,
			add: false,
			edit: false,
			delete: false
		},
		line: {
			show: false,
			add: false,
			edit: false,
			delete: false
		}
	};

	var globalaction = $ips.load('index','getglobalaction');
	source = $.extend(true,source,globalaction);
	//缓存1小时
	$ips.cachePut(setkey,source,3600);
	return source;
};

imap.setMap = function(huimap) {
	if(huimap) {
		imap.huimap = huimap;
		imap.huimapObj = huimap.ditu('getMap');
	}
};

imap.rarefy = function(objs,fn,pixelrange,pixmaxcount,bmode) {
	log.profile('rarefy');
	var pixelrange = pixelrange || 10,
		pixmaxcount = pixmaxcount || 10,
		bmode = bmode || false;
	var viewlatlng = imap.huimap.ditu('getViewLatlngs',true);
	var p = imap.huimapObj.pixelToPoint(new BMap.Pixel(1,1));
    var rLat = pixelrange * (viewlatlng.maxlat - p.lat);
    var rLng = pixelrange * (p.lng - viewlatlng.minlng);
    // log.debug(rLat,rLng);
    var newobjs = new Array();
    var total = 0, c = 0;
    for(var i in objs) {
    	var obj = objs[i];
    	if('undefined' == typeof obj || !obj) continue;
    	var lat = lng = 0;
    	if(obj instanceof BMap.Marker || ('undefined' != typeof BMapLib.SimpleMarker) && (obj instanceof BMapLib.SimpleMarker)) {
    		var latlng = obj.getPosition();
    		lat = latlng.lat;
    		lng = latlng.lng;
    	} else if('undefined' != typeof obj.lat) {
    		var latlng = obj;
    		if(bmode) latlng = imap.huimap.ditu('b2g',latlng);
    		lat = obj.lat;
    		lng = obj.lng;
    	}
        if(lat<viewlatlng.minlat || lat>viewlatlng.maxlat || lng<viewlatlng.minlng || lng>viewlatlng.maxlng)
            continue;
        var count = 0;
        for(var j = 0, o; o = newobjs[j]; j++) {
            var dx = lat - o._lat, dy = lng - o._lng;
            if (Math.abs(dx) <= rLat && Math.abs(dy) <= rLng)
                count ++;
            c ++;
            if (count >= pixmaxcount)
                break;
        }
        if(count < pixmaxcount) {
        	obj._lat = lat;
        	obj._lng = lng;
        	var obj = 'function' == typeof fn ? fn(obj,'add') : obj;
            if(obj) newobjs.push(obj);
        } else {
        	if('function' == typeof fn) fn(obj,'delete');
        }
        total ++;
    }
    log.profile('rarefy');
    log.debug(newobjs);
    log.debug('rarefy : ' + newobjs.length + '/' + total + '/' + c);
	return newobjs;
};

