/**
 * 地图区域显示和编辑 - dupeng
 * @param object huimap 地图对象
 * @param object options 显示区域选项
 *              options : {
 *                              minzoom : 7,
 *                              editable : false
 *                        }
 */
imap.polygon = function(huimap,options) {
    if(!huimap) {
        $ips.alert('请传入地图参数');
        return;
    }
    this.huimap = huimap;
    this.huimapObj = this.huimap.ditu('getMap');
     if(options.polygon.color && options.polygon.color.indexOf("#")<0){
        options.polygon.color = "#"+options.polygon.color;
    }
    $.extend(this.options,options);
    this.infoWin = options.infoWin;
    //初始化imap对象
    this._create();
}


imap.polygon.prototype = {
    huimap      : null,     //hui地图对象
    huimapObj   : null,     //地图对象
    drawingManager  : null, //花地图对象
    infoWin     :{},//信息窗口,{html ,callback},显示html,将中心经纬度和 区域边界回传
    ipolygons   : {},       //当前地图上所有区域
    curPolygon  : null,     //当前选中的区域
    overlayPre  : 'polygon_',
    options     : {
        minzoom     : 7,        //缩放级别必须大于该值, 若缩放级别小于等于该值, 则不获取区域
        editable    : false,    //是否对区域编辑
        enable      : true,     //可用状态
        editid      : null,     //页面加载直接将此ID设置为可编辑状态
        mtypes      : '',        //类别
        polygon      : {}        //初始化标注点的默认属性
    },
    _curPolygonId   : null,
    _stepCache      : [],
    _nowOp          : null,

    _create     : function() {
        var self = this,
        styleOptions = {
            strokeColor:"#CC00FF",    //边线颜色。
            fillColor:"#CC00FF",      //填充颜色。当参数为空时，圆形将没有填充效果。
            strokeWeight: 3,       //边线的宽度，以像素为单位。
            strokeOpacity: 0.8,    //边线透明度，取值范围0 - 1。
            fillOpacity: 0.6,      //填充的透明度，取值范围0 - 1。
            strokeStyle: 'solid' //边线的样式，solid或dashed。
        };
        self.drawingManager = new BMapLib.DrawingManager(self.huimapObj, {
            isOpen: false, //是否开启绘制模式
            enableDrawingTool: false, //是否显示工具栏
            polygonOptions: styleOptions, //多边形的样式
        });
        self.drawingManager.addEventListener('overlaycomplete',(function(s) {
            return function(e) {
                self.overlaycomplete(e,s);
            }
            })(self)
        );
        self.drawingManager.setDrawingMode(BMAP_DRAWING_POLYGON)
        self.showMarker();
    },
        //回调获得覆盖物信息
    overlaycomplete : function(e,s){
        var self = this;
        var ploygon = e.overlay;
        s.drawingManager.close();
        var lnglats = $('#map').ditu('getPolygonPoints',ploygon,true);
        var center = self.getCentroid(ploygon);
        //通过得到的经纬度用 自有方法创建 多边形
        self._nowOp = 'add';
        var polygon = {
            id          : '_new',
            lnglats      : lnglats,
            name        : '',
            address     : '',
            lat         : center.lat,
            lng         : center.lng,
        };
        $.extend(polygon,this.options.polygon);
        var gpoint = self.huimap.ditu('b2g',center);
        polygon.glat = gpoint.lat;
        polygon.glng = gpoint.lng;
        polygon.glnglats = self.huimap.ditu('strb2g',lnglats);
        s._showPolygon(polygon);
        s.huimapObj.removeOverlay(ploygon);
    },
    setOptions      : function(opts) {
        $.extend(this.options,opts);
    },
    /*
    *添加标注 ,标注区域
    */
    addMarker       : function() {
        if(imap.lockedOverlay){
            $ips.alert('当前页面正处于编辑状态,请先完成编辑或取消编辑后再试!');
            return;
        }
        var self = this;
        if(self._nowOp) {
            self.removeAll();
        }
        self.curPolygon = null;
        self.drawingManager.open();
        //地图点击可以画区域
		imap.setMapCursor('point');
    },
    disEditable      : function(){
        var self = this;
        if(self.drawingManager)
            self.drawingManager.close();
    },
    showMarker : function(polygon){
        var self = this,opt = self.options,
            polygon = polygon || opt.polygon;
        if(!polygon.id){
            return;
        }
        polygon.glng = polygon.lng;
        polygon.glat = polygon.lat;
        var latlng = self.huimap.ditu('g2b',polygon);
        polygon.lat = latlng.lat;
        polygon.lng = latlng.lng;
        var blnglat = self.huimap.ditu("strg2b",polygon.lnglats);
        polygon.glnglats =  polygon.lnglats;
        polygon.lnglats =  blnglat;
        self._showPolygon(polygon);
        self.ipolygons[polygon.id]  = polygon;
        self.huimap.ditu('setCenter',polygon.lat,polygon.lng,14,true);
    },
    _showPolygon : function(polygon) {
        var self = this;
        imap.lockedOverlay = 'polygon';
        polygon = self.addPolygon(polygon);
		polygon.canmodify = 1;
        self.curPolygon = polygon;
        self._setCurAddress();
    },
    addPolygon : function(polygon) {
        var self = this;
        var overlayId = self.overlayPre + polygon.id;
        var mkopt = {
            overlayId   :   overlayId,
            points      :   polygon.lnglats,
            editable    :   true,
            myopts      :   {
                    lat     :   polygon.lat,
                    lng     :   polygon.lng,
					bmode	:	true
            },
            styleopts   :   {},
            events      :   {
                    click   :   function(point) {
                        self.openInfoWindowHtml();
                    },
                    lineupdated : function() {
                        var t = this,
                            overlayId = t.overlayId,
                            mapid = overlayId.replace(self.overlayPre,''),
                            polygon = null;
                        var islast = this._lastStep || false;
                        if(!islast){
                            return;
                        }
                        if(self._nowOp == 'add') {
                            polygon = self.curPolygon;
                        } else {
                            polygon = self.ipolygons[mapid];
                        }
                        if(polygon) {
                            if(islast){
                                self._stepCache.push(polygon.lnglats);
                                polygon.lnglats = self.huimap.ditu('getPolygonPoints',t,true);
                                polygon.glnglats = self.huimap.ditu("strb2g",polygon.lnglats);
                                self.openInfoWindowHtml();
                            }
                            else {
                                polygon._lastStep = false;
                            }
                        }
                    },
                    mouseover  : function() {
                        this._lastStep = true;
                    }
            }
        };
        //设置区域颜色
        if(polygon.color) {
            mkopt.styleopts.strokeColor = polygon.color;
            mkopt.styleopts.fillColor = polygon.color;
        }
        polygon.mkopt = mkopt;
		if(mkopt.points && mkopt.points!='') {
            if(polygon.mkobj) {
                self.huimapObj.addOverlay(polygon.mkobj);
            } else {
    			var overlay = self.huimap.ditu('polygon',mkopt.overlayId,mkopt.points,mkopt.myopts,mkopt.styleopts,mkopt.events);
    			overlay.enableEditing();
                polygon.mkobj = overlay;
            }
		}
        self._addCenterMarker(polygon);
        return polygon;
    },
    _addCenterMarker    : function(polygon) {
        var self = this;
        var overlayId = self.overlayPre + polygon.id;
        if(polygon._center_mkobj) {
            self.huimapObj.addOverlay(polygon._center_mkobj);
            return polygon;
        }
        //区域中心点
        var icon = polygon.icon || false;
        var iconObj = self._getMarkerIcon(icon);
        var _center_mkopt = {
            overlayId   :   '_center_' + overlayId,
            lat         :   polygon.lat,
            lng         :   polygon.lng,
            draggable   :   true,
            icon        :   iconObj,
            bmode       :   true
        };
        _center_mkopt.ondragend = (function(s) {
            return function(latlng) {
                var lat = latlng.lat,lng = latlng.lng;
                var overlayId = this.overlayId,
                    pid = overlayId.replace('_center_','').replace(s.overlayPre,'');
                var polygon = s.ipolygons[pid] || self.curPolygon;
                if(polygon) {
                    polygon.lat = Math.round(lat*1000000)/1000000;
                    polygon.lng = Math.round(lng*1000000)/1000000;
                    var showlatlng = self.huimap.ditu('b2g',polygon);
                    polygon.glat = showlatlng.lat;
                    polygon.glng = showlatlng.lng;
                    self._setCurAddress();
                }
            }
        })(self);
        polygon._center_mkobj = self.huimap.ditu('marker',_center_mkopt);
        return polygon;
    },
    lastStep        : function() {
        var self = this,
            polygon = self.curPolygon;
        if(polygon && self._stepCache.length) {
            var points = self._stepCache.pop();
            polygon.mkobj._lastStep = false;
            self._updatePolygon(polygon,'points',points);
        } else {
            $ips.alert('已经是第一步');
        }
    },
    updateOverlayColor  : function(color) {
        var self = this,
            polygon = self.curPolygon;
        if(polygon) self._updatePolygon(polygon,'styleopts',color);
    },
    removePolygon   : function(polygon) {
        if(!polygon) return;
        var self = this;
        self.huimapObj.removeOverlay(polygon.mkobj);
        self.huimapObj.removeOverlay(polygon._center_mkobj);
    },
    _updatePolygon  : function(polygon,item,value) {
        var self = this,
            item = item || false;
        if(!item || !polygon || !value) return;
        switch(item) {
            case 'points':
				//百度更新边界
				var pp = new Array();
				for(var i = 0, points = value.split(';'), len = points.length; i < len; i++) {
					if(points[i] && points[i] != '') {
						var p = points[i].split(',');
						if(p.length) {
							pp.push(new BMap.Point(p[0],p[1]));
						}
					}
				}
				polygon.mkobj.setPath(pp);
                break;
            case 'myopts':
                break;
            case 'styleopts':
				polygon.mkobj.setFillColor(value);
				polygon.mkobj.setStrokeColor(value);
                break;
            default:
                break;
        }
    },
    removeAll      : function() {
        var self = this,
            polygon = self.curPolygon;
        if(!polygon || !polygon.mkobj) {
            return;
        }
        var overlay = polygon.mkobj;
        self.closeInfoWindow();    
        self.removePolygon(polygon);
        if(self._stepCache.length) {
            //清空操作缓存
            self._stepCache = [];
        }
        self._nowOp = null;
        imap.lockedOverlay = null;
    },
    _setCurAddress      : function() {
        var self = this,
            polygon = self.curPolygon;
        if(!polygon) return;
        var latlng = self.huimap.ditu('b2g',polygon);
        $ips.geo(latlng.lat + ',' + latlng.lng, (function(s,polygon){
                return function(p) {
                    if(p.code == 0 && p.total > 0) {
                        var address = p.poilist[0].address,
                            province = p.poilist[0].province,
                            city = p.poilist[0].city;
                        if(address) polygon.address=address;
                        if(province) polygon.province=province;
                        if(city) polygon.city=city;
                        s.openInfoWindowHtml();
                    }
            }
        })(self,polygon))
    },
    openInfoWindowHtml : function() {
        var self = this,polygon = self.curPolygon;
        if(!polygon) return;
        var _BPoint = new BMap.Point(polygon.lng,polygon.lat);
        if(polygon.address){
            self.huimap.ditu('openInfoWindowHtml',self.infoWin.html,_BPoint,true,(function(s,m){
                return function(){
                    s.infoWin.infoCallback(m);
                } 
            })(self,polygon));    
        }else{
            self._setCurAddress();
        }

    },
    closeInfoWindow     : function() {
        var self = this;
        self.huimapObj.closeInfoWindow();
    },
    _getMarkerIcon   : function(icon) {
		var icontype = this.options.icontype || '';
		switch(icontype) {
			case 'tnt':
				return {
					image : '/css/skin/map_skin/mark_flagNone.png',
					shadow: '/css/skin/map_skin/mark_flagNone_shadow.png',
                    iconsize:           [23, 24],
                    shadowsize:         [38, 31],
                    iconanchor:         [1, 24],
                    infowindowanchor:   [1, 24]
				};
				break;
			default:
				var icon = icon && icon.indexOf('mark_flag')!=-1 ? icon : 'mark_flag01';
				var shadow = 'mark_flag_shadow';
				if(icon == 'mark_flagNone') shadow = 'mark_flagNone_shadow';
				return {
					image : '/css/skin/map_skin/'+icon+'.png',
					shadow: '/css/skin/map_skin/'+shadow+'.png',
                    iconsize:           [23, 24],
                    shadowsize:         [38, 31],
                    iconanchor:         [1, 24],
                    infowindowanchor:   [1, 24]
				};
				break;
		}

    },
    getCentroid : function(p){
        var self = this;
        var pts = p.getPath(),
                n = pts.length,
                lat = 0,
                lng = 0,
                i = 0,
                j = n - 1,
                p1,
                p2,
                f;
            for (i; i < n; i += 1) {
                p1 = pts[i];
                p2 = pts[j];
                f = p1.lat * p2.lng - p2.lat * p1.lng;
                lat += (p1.lat + p2.lat) * f;
                lng += (p1.lng + p2.lng) * f;
                j = i;
            }

            f = self.getLatLngArea(p) * 6;

            return {lat:(lat / f), lng:(lng / f)};

    },
    getLatLngArea : function(p){
        var self = this;
        var area = 0,
                pts = p.getPath(),
                n = pts.length,
                i = 0,
                j = n - 1,
                p1,
                p2;
            for (i; i < n; i += 1) {
                p1 = pts[i];
                p2 = pts[j];
                area += p1.lat * p2.lng;
                area -= p1.lng * p2.lat;
                j = i;
            }
            area = area / 2;
            return area;
    }, 
};