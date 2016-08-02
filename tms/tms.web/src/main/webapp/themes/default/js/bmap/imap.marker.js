/**
 * 百度地图标注点显示和编辑 - dupeng & zava
 * @param object huimap 地图对象
 * @param object options 标点选项
 *              options : {
 *                              minzoom : 7,
 *                              editable : false
 *                        }
 */
imap.marker = function(huimap,options) {
    if(!huimap) {
        $ips.alert('请传入地图参数');
        return;
    }
    this.huimap = huimap;
    this.huimapObj = this.huimap.ditu('getMap');
    imap.setMap(huimap);
    if(options.marker.color && options.marker.color.indexOf("#")<0){
        options.marker.color = "#"+options.marker.color;
    }
    $.extend(this.options,options);
    this.infoWin = options.infoWin;
    //初始化imap对象
    this._create();
}

imap.marker.prototype = {
    huimap      : null,     //hui地图对象
    huimapObj   : null,     //地图对象

    imarkers    : {},       //当前地图上所有标点
    curMarker   : null,     //当前选中的标点
    overlayPre  : 'marker_',
    infoWin    : {},//信息窗口,{html,callback}
    options     : {
        minzoom     : 7,        //缩放级别必须大于该值, 若缩放级别小于等于该值, 则不获取标点
        editable    : false,    //是否对标点编辑
        enable      : true,     //可用状态
        marker      : {}        //初始化标注点的默认属性{lat:,lng:,lnglats:}
    },
    _imarkerBounds  : {},
    _curMarkerId    : null,
    _addMListener   : null,
    _nowOp          : null,
    _create     : function() {
        var self = this;
        self.showMarker();
    },
    setOptions      : function(opts) {
        $.extend(this.options,opts);
    },
    showMarker      : function(marker){
        var self = this,opt = self.options,
            marker = marker || opt.marker;
        if(!marker.id){
            return;
        }
        marker.gpoint = {lng :marker.lng,lat:marker.lat};
        var latlng = self.huimap.ditu('g2b',marker);
        marker.lat = latlng.lat;
        marker.lng = latlng.lng;
        self._showMarker(marker);
        self.huimap.ditu('setCenter',marker.lat,marker.lng,null,true);
    },
    addMarker       : function() {
        var self = this;
        if(imap.lockedOverlay){
            $ips.alert('当前页面正处于编辑状态,请先完成编辑或取消编辑后再试!');
            return;
        }
        if(self._nowOp) {
            self.deleteIt();
        }
        self.curMarker = null;
        //地图点击增加标点
		imap.setMapCursor('point');
        self.clearAddListener();
        self._addMListener = GEvent.addListener(self.huimapObj, 'click', (function(sf) {
            return function(e) {
                self.clearAddListener();
                sf._editNewMarker(e.point);
            }
        })(self));
	},
    disEditable      : function(){
        var self = this;
         self.clearAddListener();
    },
    clearAddListener  : function() {
        var self = this;
        if(self._addMListener) {
            self.huimapObj.removeEventListener('click',self._addMListener);
            self._addMListener = null;
        }
    },
    _editNewMarker   : function(_BPoint) {
        var self = this,
            opt = self.options;

        self._nowOp = 'add';
        //在鼠标点击的位置创建一个新标点
        var lat = _BPoint.lat,
            lng = _BPoint.lng, 
            name  = "";
        var marker = {
            id          : "_new", 
            name		: name,
            lat			: lat,
            lng         : lng,
        };
        $.extend(marker,this.options.marker);
        marker.radius = 100;//默认半径100
        var gpoint = self.huimap.ditu('b2g',marker);
        marker.gpoint = gpoint; 
        self._showMarker(marker);
    },
    /*
    * marker 完整的 marker 对象
    * mkopt : overlay对象
    */
    _showMarker     :function(marker){
        var self = this,
            iconObj = self._getMarkerIcon(),
            overlayId = self.overlayPre + marker.id; 
        imap.lockedOverlay = 'marker';
        var mkopt = {
            overlayId   :   overlayId,
            lat         :   marker.lat,
            lng         :   marker.lng,
            icon        :   iconObj,
            draggable   : true, 
            onclick     :   function(latlng) {
                self.openInfoWindowHtml();
            },
            bmode       :   true
        };
        mkopt.ondragend = (function(s) {
            return function(latlng) {
                s._setMarkerLatlng(latlng);
                s._setBound(marker);
                marker.address = "";
                self.openInfoWindowHtml();
            }
        })(self);
        marker.mkopt = mkopt;
        var overlay = self.huimap.ditu('marker',mkopt);
        marker.mkobj = overlay;
        marker.canmodify = 1;
        self.curMarker = marker;
        imap.setMapCursor('move');
        self.imarkers[marker.id] = marker;
        self._setBound(marker);
        self.openInfoWindowHtml();
    },
    /**
    * 清理当前地图
    */
    deleteIt     : function(marker) {
        var self = this;
        //取消弹出框 
        self.huimapObj.closeInfoWindow();
        if(!marker) {
            var markers = self.imarkers;
            $.each(markers,function(id,marker){
                self.deleteIt(marker);
            })
        }else{
            var overlay = marker.mkobj;
            self.huimapObj.removeOverlay(overlay);
            self._deleteBound(marker.id);
            self._nowOp = null;
            imap.lockedOverlay = null;
        }
    },
    setMarkerImage  : function(overlay,icon) {
        var self = this;
        if(!overlay) {
			overlay = self.curMarker.mkobj;
        }
        if(!overlay) return;
        var icon = icon && icon.indexOf('mark_bubble')!=-1 ? icon : 'mark_bubble01';
        var url = "/css/skin/map_skin/"+icon+".png";
		overlay.setImage(url);
    },
    _setMarkerLatlng : function(latlng) {
        var self = this, marker = null;
        marker = self.curMarker;
        marker.lat = Math.round(latlng.lat*1000000)/1000000;
        marker.lng = Math.round(latlng.lng*1000000)/1000000;
        marker.mkopt.lat = latlng.lat;
        marker.mkopt.lng = latlng.lng;
        var gpoint = self.huimap.ditu('b2g',marker);
        marker.gpoint = gpoint;
    },
    updateOverlayColor  : function(color) {
        var self = this,
            marker = self.curMarker;
        self._imarkerBounds[marker.id].setFillColor(color);
        self._imarkerBounds[marker.id].setStrokeColor(color);
    },
    setBound       : function(radius){
        var self = this,
            marker = self.curMarker;
        self._setBound(marker,radius);
    },
    _setBound      : function(marker,radius){
        var self = this,
            marker = marker || self.curMarker;
            bounds = self._imarkerBounds;
        marker.radius = radius || marker.radius;    
        var mapid = marker.id;
        self._deleteBound(mapid);
        var points = self.huimap.ditu('getCircleBounds',{lat:marker.lat,lng:marker.lng},marker.radius,true);
        var overlayId = '_bounds_' + self.overlayPre + mapid;
        var color = self._getColor(marker.color);
        var strokeColor = color || "#CC66FF";
        var fillColor = color || "#CC66FF";
        var overlay = self.huimap.ditu('polygon',overlayId,points,{bmode:true},{strokeColor:strokeColor,fillColor:fillColor},{
            click: (function(s,m){
                return function(){
                    s.openInfoWindowHtml();
                }
            })(self,marker)
        });
        marker.glnglats = self.huimap.ditu('strb2g',points);
        marker.glng = marker.gpoint.lng;
        marker.glat = marker.gpoint.lat;
        bounds[mapid] = overlay;
    },
    _deleteBound   : function(mapid) {
        var self = this,
            bounds = self._imarkerBounds;
        if(bounds) {
            if(mapid) {
                if(bounds[mapid]) {
                    self.huimapObj.removeOverlay(bounds[mapid]);
                    bounds[mapid] = null;
                }
            } else {
                $.each(bounds,function(k,v) {
                    if(v) {
                        self.huimapObj.removeOverlay(v);
                        bounds[k] = null;
                    }
                });
            }
        }
    },
    _setCurAddress      : function() {
        var self = this,
            marker = self.curMarker;
        if(!marker) return;
		var latlng = self.huimap.ditu('b2g',marker);
        $ips.geo(latlng.lat + ',' + latlng.lng, (function(s,m){
                return function(p) {
                    if(p.code == 0 && p.total > 0) {
                        var address = p.poilist[0].address,
                            province = p.poilist[0].province,
                            city = p.poilist[0].city;
                        if(address) m.address=address;
                        if(province) m.province=province;
                        if(city) m.city=city;
                        s.openInfoWindowHtml();
                    }
            }
        })(self,marker))
    },
    openInfoWindowHtml : function() {
    	var self = this,marker = self.curMarker;
        if(!marker) return;
        var _BPoint = new BMap.Point(marker.lng,marker.lat);
        if(marker.address){
            self.huimap.ditu('openInfoWindowHtml',self.infoWin.html,_BPoint,true,(function(s,m){
                return function(){
                    s.infoWin.infoCallback(m);
                } 
            })(self,marker));    
        }else{
            self._setCurAddress();
        }
    },
    closeInfoWindow     : function() {
        var self = this;
        self.huimapObj.closeInfoWindow();
    },
    _getMarkerIcon   : function(icon) {
        var icon = icon && icon.indexOf('mark_bubble')!=-1 ? icon : 'mark_bubble01';
        return {
            image : '/css/skin/map_skin/'+icon+'.png',
            shadow: '/css/skin/map_skin/mark_babble_shadow.png',
            iconsize:           [11, 27],
            shadowsize:         [33, 33],
            iconanchor:         [5, 27],
            infowindowanchor:   [5, 2]
        };
    },
    _getColor   : function(color){
        if(color && color.indexOf("#")<0){
        return "#"+color;
        }
    },
   removeAll      : function() {
        var self = this;
        self.deleteIt();
    },
};