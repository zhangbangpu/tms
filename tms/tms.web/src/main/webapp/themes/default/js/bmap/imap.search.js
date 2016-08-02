/**
 * 地图搜索 - dupeng
 * zava:
 * imap 系列 经纬度内部处理时 统一为baidu经纬度
 * imap 系列,经纬度回传,通过回调函数,将 百度经纬度,统一 处理为{glat,glng}
 * 当然 也可以{glat,glng,lat,lng}
 *
 */
imap.search = function(huimap,options) {
    if(!huimap) {
        $ips.alert('请传入地图参数');
        return;
    }
    this.huimap = huimap;
    this.huimapObj = this.huimap.ditu('getMap');
    $.extend(this.options,options);
    if(options&& options.infoWin){
        this.infoWin = options.infoWin;
    }  
    //初始化
    this._create();
}

imap.search.prototype = {
    huimap      : null,     //hui地图对象
    huimapObj   : null,     //地图对象
    overlayPre  : 'search_',
    imarkers    : {},
    _curIndex   : null,
    infoWin    : {},//信息窗口,{html,callback}
    options     : {
        searchzoom  : 11    //搜索到结果后定位地图级别的最小值
    },
    _create     : function() {

    },
    _clearSearch    : function() {
        var self = this,
            imarkers = self.imarkers;
        if(imarkers) {
            $.each(imarkers,function(k,marker) {
                if(marker) {
                    self.huimapObj.removeOverlay(marker.mkobj);
                }
            });
        }
        if('undefined' != typeof self.ipolyline && self.ipolyline) {
            self.huimapObj.removeOverlay(self.ipolyline);
        }
        self.imarkers = {};
        self.huimapObj.closeInfoWindow();
    },
    searchAddress  : function(address) {
        var self = this,
            opt = self.options;
        self._clearSearch();
        /**
        *搜索地址库
        *得到的经纬度是google
        **/
        $ips.geo(address,function(data) {
            if(data.code == 0 && data.total > 0) {
                $.each(data.poilist, function(k,marker) {
                    var overlayId = self.overlayPre + k,
                        image = 'num_'+(k+1),
                        iconObj = self._getMarkerIcon(image);
                    var lat = marker.lat,lng = marker.lng;
                    var mkopt = {
                        overlayId   :   overlayId,
                        lat         :   lat,
                        lng         :   lng,
                        icon        :   iconObj,
                        bmode       :   false,
                        onclick     :   function(latlng) {
                            var overlayId = this.overlayId,
                                index = overlayId.replace(self.overlayPre,'');
                            self.openInfoWindowHtml(self.imarkers[index]);
                            self._curIndex = index;
                        }
                    };
                    var overlay = self.huimap.ditu('marker',mkopt);
                    marker.mkopt = mkopt;
                    marker.mkobj = overlay;
                    //将标记放入imarkers
                    self.imarkers[k] = marker;
                    if(k == 0) {
                        self.openInfoWindowHtml(marker);
                        var sczoom = opt.searchzoom,
                            nowzoom = self.huimapObj.getZoom(),
                            zoom = nowzoom < sczoom ? sczoom : nowzoom;
                        var bPoint = self.huimap.ditu("g2b",{lng:marker.lng,lat:marker.lat});
                        self.huimapObj.setCenter(new BMap.Point(bPoint.lng,bPoint.lat));
						self.huimapObj.setZoom(zoom);
                        self._curIndex = k;
                    }
                });
            } else if(!markdata || markdata.length < 1) {
                $ips.alert('位置解析失败，请正确输入地址');
            }
        },false);
        //第一个点行政区域显示
        var bdary = new BMap.Boundary();
        bdary.get(address, function(rs) {       //获取行政区域
            if(rs.boundaries.length) {
                self.ipolyline = self.huimap.ditu('polyline',{
                    overlayId : 'polyline1',
                    latlngs : rs.boundaries[0],
                    strokestyle : 'dashed',
                    color : '#ff0000',
                    weight : 3,
                    bmode : true
                });
            }
        });
    },
    _getMarkerIcon   : function(image) {
        var image = image || 'num_1';
        return {
            image : 'http://www.huoyunren.com/images/mabc/'+image+'.png',
            shadow: "http://www.google.com/mapfiles/shadow50.png",
            iconsize:			[37, 27],
            shadowsize:			[20, 15],
            iconanchor:			[18, 27],
            infowindowanchor:	[0, 0]
        };
    },
    _getLabelIcon   : function(image) {
        var icon = icon && icon.indexOf('mark_order')!=-1 ? icon : 'mark_order01';
        return {
            image : '/css/skin/map_skin/'+icon+'.png',
            shadow: '/css/skin/map_skin/mark_babble_shadow.png',
            iconsize:           [20, 25],
            shadowsize:         [33, 33],
            iconanchor:         [10, 26],
            infowindowanchor:   [9, 2]
        };
    },
    /**
    * marker 是geo ,搜索出来的 google经纬度,
    **/
    openInfoWindowHtml  : function(marker) {
        var self = this,
            infoWin = self.infoWin;
        if(!marker) return;
         var html = null;
         _point = new BMap.Point(marker.lng,marker.lat);
        if(infoWin.html){
            html = infoWin.html;
            self.huimap.ditu('openInfoWindowHtml',html,_point,false,(function(s,m){
                return function(){
                    //直接将google经纬度回传
                    m.glng = m.lng;
                    m.glat = m.lat;
                    s.infoWin.infoCallback(m);
                } 
            })(self,marker));  
        }else{
            html = self._getMarkerHtml(marker);
            self.huimap.ditu('openInfoWindowHtml',html,_point,false);  
        }
    },
    _getMarkerHtml  : function(marker) {
        var self = this;
        var setmarker = setpolygon = '';
        if('undefined' != typeof(imarker) && imarker.options.editable) setmarker = '<a href="javascript:void(0);" onclick="isearch.markerIt();">设为标点</a>&nbsp;';
        if('undefined' != typeof(ipolygon) && ipolygon.options.editable) setpolygon = '<a href="javascript:void(0);" onclick="isearch.polygonIt();">设为区域</a>&nbsp;';
        return '<table>' +
               '<tr><td style="white-space:nowrap">名称：</td><td>'+marker.name+'</td></tr>'+
               '<tr><td style="white-space:nowrap">地址：</td><td>'+marker.address+'</td></tr>'+
               '<tr><td style="white-space:nowrap" colspan="2">'+
               setmarker+
               setpolygon+
               '<a href="javascript:void(0);" onclick="isearch.deleteIt();">删除</a>&nbsp;'+
               '</td></tr>'+
               '</table>';
    },
    deleteIt    : function() {
        var self = this,
            index = self._curIndex,
            marker = self.imarkers[index];
        self.huimapObj.removeOverlay(marker.mkobj);
        self.imarkers[index] = null;
        self.huimapObj.closeInfoWindow();
    },
    markerIt    : function() {
        if(!imarker || !imarker.options.editable) {
            $ips.alert('当前页面不可编辑标注点!');
            return;
        }
        var self = this;
        var _BPoint = self._getNowGlatlng();
        self.deleteIt();
        if(_BPoint) imarker._editNewMarker(_BPoint);
    },
    polygonIt   : function() {
        if(!ipolygon || !ipolygon.options.editable) {
            $ips.alert('当前页面不可编辑区域!');
            return;
        }
        var self = this;
        var _BPoint = self._getNowGlatlng();
        self.deleteIt();
        if(_BPoint) ipolygon._editNewPolygon(_BPoint);
    },
    _getNowGlatlng  : function() {
        var self = this,
            index = self._curIndex,
            marker = self.imarkers[index];
		//转换到百度
		var latlng = self.huimap.ditu('g2b',marker);
        var lat = latlng.lat, lng = latlng.lng;
        var _BPoint = new BMap.Point(lng,lat);
        return _BPoint;
    }
};