function addListener(element, e, fn) { element.addEventListener ? element.addEventListener(e, fn, false) : element.attachEvent("on" + e, fn) };
function removeListener(element, e, fn) { element.removeEventListener ? element.removeEventListener(e, fn, false) : element.detachEvent("on" + e, fn) };

function MarkerClusterer(map, opt_markers, opt_options) {
    this.map_ = map;
    this.markers_ = [];
    this.clusters_ = [];
    this.sizes = [53, 56, 66, 78, 90];
    this.styles_ = [];
    this.ready_ = true;

    var options = opt_options || {};

    this.notListened_ = options['notListened'] || false;
    this.gridSize_ = options['gridSize'] || 60;
    this.maxZoom_ = options['maxZoom'] || null;
    this.styles_ = options['styles'] || [];
    this.imagePath_ = options['imagePath'] ||
    this.MARKER_CLUSTER_IMAGE_PATH_;
    this.imageExtension_ = options['imageExtension'] ||
    this.MARKER_CLUSTER_IMAGE_EXTENSION_;

    this.setupStyles_();

    this.setMap(map);

    this.prevZoom_ = this.map_.getZoom();

    var that = this;
    if (!that.notListened_) {
        this.map_.addEventListener('zoomend', function() {
            var maxZoom = 20;
            var zoom = that.map_.getZoom();
            if (zoom < 0 || zoom > maxZoom) {
                return;
            }
            //debugger;
            if (that.prevZoom_ != zoom) {
                that.prevZoom_ = that.map_.getZoom();
                that.resetViewport();
                that.redraw();
            }
        });
        this.map_.addEventListener('moveend', function() {
            var maxZoom = 20;
            var zoom = that.map_.getZoom();
            if (zoom < 0 || zoom > maxZoom) {
                return;
            }
            //debugger;
            if (that.prevZoom_ != zoom) {
                that.prevZoom_ = that.map_.getZoom();
                that.resetViewport();
            }
            that.redraw();
        });
    }
    // Finally, add the markers
    if (opt_markers && opt_markers.length) {
        this.addMarkers(opt_markers, false);
    }
}

MarkerClusterer.prototype = BMap.Overlay;

MarkerClusterer.prototype.MARKER_CLUSTER_IMAGE_PATH_ = '/js/imap/baidu/images/m';

MarkerClusterer.prototype.MARKER_CLUSTER_IMAGE_EXTENSION_ = 'png';

MarkerClusterer.prototype.setupStyles_ = function() {
    for (var i = 0, size; size = this.sizes[i]; i++) {
        this.styles_.push({
            url: this.imagePath_ + (i + 1) + '.' + this.imageExtension_,
            height: size,
            width: size
        });
    }
};

MarkerClusterer.prototype.setStyles = function(styles) {
    this.styles_ = styles;
};

MarkerClusterer.prototype.getStyles = function() {
    return this.styles_;
};

MarkerClusterer.prototype.getMarkers = function() {
    return this.markers_;
};

MarkerClusterer.prototype.getTotalMarkers = function() {
    return this.markers_.length;
};

MarkerClusterer.prototype.setMaxZoom = function(maxZoom) {
    this.maxZoom_ = maxZoom;
};

MarkerClusterer.prototype.getMaxZoom = function() {
    return this.maxZoom_ || 20;
};


MarkerClusterer.prototype.calculator_ = function(markers, numStyles) {
    var index = 0;
    var count = markers.length;
    var dv = count;
    while (dv !== 0) {
        dv = parseInt(dv / 10, 10);
        index++;
    }

    index = Math.min(index, numStyles);
    return {
        text: count,
        index: index
    };
};

MarkerClusterer.prototype.setCalculator = function(calculator) {
    this.calculator_ = calculator;
};

MarkerClusterer.prototype.getCalculator = function() {
    return this.calculator_;
};

MarkerClusterer.prototype.addMarkers = function(markers, opt_nodraw) {
    for (var i = 0, marker; marker = markers[i]; i++) {
        this.pushMarkerTo_(marker);
    }
    if (!opt_nodraw) {
        this.redraw();
    }
};

MarkerClusterer.prototype.pushMarkerTo_ = function(marker) {
    this.map_.removeOverlay(marker);
    marker.isAdded = false;
    this.markers_.push(marker);
};

MarkerClusterer.prototype.addMarker = function(marker, opt_nodraw) {
    this.pushMarkerTo_(marker);
    if (!opt_nodraw) {
        this.redraw();
    }
};

MarkerClusterer.prototype.removeMarker = function(marker) {
    var index = -1;
    if (this.markers_.indexOf) {
        index = this.markers_.indexOf(marker);
    } else {
        for (var i = 0, m; m = this.markers_[i]; i++) {
            if (m == marker) {
                index = i;
                continue;
            }
        }
    }

    if (index == -1) {
        return false;
    }

    this.markers_.splice(index, 1);
    this.map_.removeOverlay(marker);

    this.resetViewport();
    this.redraw();
    return true;
};


MarkerClusterer.prototype.setReady_ = function(ready) {
    if (!this.ready_) {
        this.ready_ = ready;
        this.createClusters_();
    }
};


MarkerClusterer.prototype.getTotalClusters = function() {
    return this.clusters_.length;
};

MarkerClusterer.prototype.getMap = function() {
    return this.map_;
};

MarkerClusterer.prototype.setMap = function(map) {
    this.map_ = map;
};

MarkerClusterer.prototype.getGridSize = function() {
    return this.gridSize_;
};
MarkerClusterer.prototype.setGridSize = function(size) {
    this.gridSize_ = size;
};

MarkerClusterer.prototype.getExtendedBounds = function(bounds) {

    // Turn the bounds into latlng.
    var tr = new BMap.Point(bounds.getNorthEast().lng,
      bounds.getNorthEast().lat);
    var bl = new BMap.Point(bounds.getSouthWest().lng,
      bounds.getSouthWest().lat);

    var trPix = this.map_.pointToPixel(tr);
    trPix.x += this.gridSize_;
    trPix.y -= this.gridSize_;

    var blPix = this.map_.pointToPixel(bl);
    blPix.x -= this.gridSize_;
    blPix.y += this.gridSize_;

    var ne = this.map_.pixelToPoint(trPix);
    var sw = this.map_.pixelToPoint(blPix);

    bounds.extend(ne);
    bounds.extend(sw);

    return bounds;
};

MarkerClusterer.prototype.isMarkerInBounds_ = function(marker, bounds) {
    return bounds.containsPoint(marker.getPosition());
};


MarkerClusterer.prototype.clearMarkers = function() {
    this.resetViewport();
    this.markers_ = [];
};


MarkerClusterer.prototype.resetViewport = function() {
    // Remove all the clusters
    for (var i = 0, cluster; cluster = this.clusters_[i]; i++) {
        cluster.remove();
    }

    // Reset the markers to not be added and to be invisible.
    for (var i = 0, marker; marker = this.markers_[i]; i++) {
        marker.isAdded = false;
        this.map_.removeOverlay(marker);
    }

    this.clusters_ = [];
};


MarkerClusterer.prototype.redraw = function() {
    this.createClusters_();
    if('undefined' != typeof BMapLib && 'undefined' != typeof BMapLib.SimpleMarker) BMapLib.SimpleMarker.show(this.map_);
};

MarkerClusterer.prototype.Sort = function() {
return this.clusters_.sort(compareCluster);
};
function compareCluster(obj1, obj2) {
    var iNum1 = obj1.getMarkers();
    var iNum2 = obj2.getMarkers();
    if (iNum1 > iNum2) {
        return -1;
    } else if (iNum1 < iNum2) {
        return 1;
    } else {
        return 0;
    }
}
MarkerClusterer.prototype.createClusters_ = function() {
    if (!this.ready_) {
        return;
    }
    //debugger;
    // Get our current map view bounds.
    // Create a new bounds object so we don't affect the map.
    var mapBounds = new BMap.Bounds(this.map_.getBounds().getSouthWest(),
      this.map_.getBounds().getNorthEast());
    var bounds = this.getExtendedBounds(mapBounds);

    // log.profile('createClusters_');
    var count = 0;
    for (var i = 0, marker; marker = this.markers_[i]; i++) {
        //this.Sort();
        var added = marker.isAdded;
        if (!added && this.isMarkerInBounds_(marker, bounds)) {
            count ++;
            for (var j = 0, cluster; cluster = this.clusters_[j]; j++) {
                if (!added && cluster.getCenter() && cluster.isMarkerInClusterBounds(marker)) {
                    added = true;
                    cluster.addMarker(marker, true);
                    break;
                }
            }

            if (!added) {
                added = true;
                // Create a new cluster.
                var cluster = new Cluster(this);
                cluster.addMarker(marker, true);
                this.clusters_.push(cluster);
            }
        }
    }

    var zoom = this.map_.getZoom();
    var mz = this.getMaxZoom();
    if (zoom <= mz) {
        for (var j = 0, cluster; cluster = this.clusters_[j]; j++) {
            cluster.showIcon();
        }
    }

    // log.profile('createClusters_');
};

MarkerClusterer.prototype.hidePanes = function() {
    var panes = this.map_.getPanes();
    panes.markerPane.style.display = "none";
    panes.markerShadow.style.display = "none";
    panes.markerMouseTarget.style.display = "none";  
    panes.labelPane.style.display = "none";  
    panes.mapPane.style.display = "none";  
}
MarkerClusterer.prototype.showPanes = function() {
    var panes = this.map_.getPanes();
    panes.markerPane.style.display = "";
    panes.markerShadow.style.display = "";
    panes.markerMouseTarget.style.display = "";
    panes.labelPane.style.display = "";  
    panes.mapPane.style.display = "";  
}

function Cluster(markerClusterer) {
    this.markerClusterer_ = markerClusterer;
    this.map_ = markerClusterer.getMap();
    this.gridSize_ = markerClusterer.getGridSize();
    this.center_ = null;
    this.markers_ = [];
    this.bounds_ = null;
    this.clusterIcon_ = new ClusterIcon(this, markerClusterer.getStyles(), markerClusterer.getGridSize());
    this.map_.addOverlay(this.clusterIcon_);
}


Cluster.prototype.isMarkerAlreadyAdded = function(marker) {
    if (this.markers_.indexOf) {
        return this.markers_.indexOf(marker) != -1;
    } else {
        for (var i = 0, m; m = this.markers_[i]; i++) {
            if (m == marker) {
                return true;
            }
        }
    }
    return false;
};

Cluster.prototype.addMarker = function(marker, noShowed) {
    if (this.isMarkerAlreadyAdded(marker)) {
        return false;
    }
    if (!this.center_) {
        this.center_ = marker.getPosition();
        this.calculateBounds_();
    }

    marker.isAdded = true;
    this.markers_.push(marker);

    this.updateIcon(marker, noShowed);
    return true;
};

Cluster.prototype.getMarkerClusterer = function() {
    return this.markerClusterer_;
};

Cluster.prototype.getMarkers = function() {
    return this.markers_.length;
};

Cluster.prototype.getBounds = function() {
    this.calculateBounds_();
    return this.bounds_;
};


Cluster.prototype.remove = function() {
    this.clusterIcon_.remove();
    delete this.markers_;
};

Cluster.prototype.getCenter = function() {
    return this.center_;
};

Cluster.prototype.calculateBounds_ = function() {
    var bounds = new BMap.Bounds(this.center_, this.center_);
    this.bounds_ = this.markerClusterer_.getExtendedBounds(bounds);
};


Cluster.prototype.isMarkerInClusterBounds = function(marker) {
    return this.bounds_.containsPoint(marker.getPosition());
};


Cluster.prototype.getMap = function() {
    return this.map_;
};


Cluster.prototype.updateIcon = function(marker, noShowed) {
    var zoom = this.map_.getZoom();
    var mz = this.markerClusterer_.getMaxZoom();

    if (zoom > mz) {
        this.map_.addOverlay(marker);
        return;
    } else if (this.markers_.length == 1) {
        this.map_.addOverlay(marker);
        this.clusterIcon_.hide();
        return;
    } else if (this.markers_.length == 2) {
        this.map_.removeOverlay(this.markers_[0]);
    }

    var numStyles = this.markerClusterer_.getStyles().length;
    var sums = this.markerClusterer_.getCalculator()(this.markers_, numStyles);
    this.clusterIcon_.setCenter(this.center_);
    this.clusterIcon_.setSums(sums);
    if (!noShowed)
        this.showIcon();
};

Cluster.prototype.showIcon = function() {
    if (this.clusterIcon_.sums_)
        this.clusterIcon_.show();
}

function ClusterIcon(cluster, styles, opt_padding) {
    this.styles_ = styles;
    this.padding_ = opt_padding || 0;
    this.cluster_ = cluster;
    this.center_ = null;
    this.div_ = null;
    this.sums_ = null;
    this.visible_ = false;
}

ClusterIcon.prototype = new BMap.Overlay();

ClusterIcon.prototype.initialize = function(map) {
    this.map_ = map;
    this.div_ = document.createElement('DIV');
    if (this.visible_) {
        var pos = this.getPosFromLatLng_(this.center_);
        this.div_.style.cssText = this.createCss(pos);
        this.div_.innerHTML = this.sums_.text;
    }

    var panes = this.map_.getPanes().markerPane.appendChild(this.div_);
    var that = this;


    addListener(that.div_, "click", function() {

        that.map_.panTo(that.cluster_.getCenter());
        that.map_.zoomIn();
    });

    return this.div_;
}


ClusterIcon.prototype.getPosFromLatLng_ = function(latlng) {
    var pos = this.map_.pointToOverlayPixel(latlng);
    pos.x -= parseInt(this.width_ / 2, 10);
    pos.y -= parseInt(this.height_ / 2, 10);
    return pos;
};

ClusterIcon.prototype.draw = function() {
    if (this.visible_) {
        var pos = this.getPosFromLatLng_(this.center_);
        this.div_.style.top = pos.y + 'px';
        this.div_.style.left = pos.x + 'px';
    }
};

ClusterIcon.prototype.hide = function() {
    if (this.div_) {
        this.div_.style.display = 'none';
    }
    this.visible_ = false;
};

ClusterIcon.prototype.show = function() {
    if (this.div_) {
        if (this.sums_) {
            this.div_.innerHTML = this.text_;
            this.useStyle();
        }

        var pos = this.getPosFromLatLng_(this.center_);
        this.div_.style.cssText = this.createCss(pos);
        this.div_.style.display = '';
    }

    this.visible_ = true;
};

ClusterIcon.prototype.remove = function() {
    if (this.div_ && this.div_.parentNode) {
        this.hide();
        this.div_.parentNode.removeChild(this.div_);
        this.div_ = null;
    }
};

ClusterIcon.prototype.setSums = function(sums) {
    this.sums_ = sums;
    this.text_ = sums.text;
    this.index_ = sums.index;
    // if (this.div_) {
    //     this.div_.innerHTML = sums.text;
    // }

    // this.useStyle();
};

ClusterIcon.prototype.useStyle = function() {
    var index = Math.max(0, this.sums_.index - 1);
    index = Math.min(this.styles_.length - 1, index);
    var style = this.styles_[index];
    this.url_ = style.url;
    this.height_ = style.height;
    this.width_ = style.width;
    this.textColor_ = style.opt_textColor;
    this.anchor = style.opt_anchor;
    this.textSize_ = style.opt_textSize;
};


ClusterIcon.prototype.setCenter = function(center) {
    this.center_ = center;
};

ClusterIcon.prototype.createCss = function(pos) {
    var style = [];
    if (document.all) {
        style.push('filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(' +
        'sizingMethod=scale,src="' + this.url_ + '");');
    } else {
        style.push('background:url(' + this.url_ + ');');
    }

    if (typeof this.anchor_ === 'object') {
        if (typeof this.anchor_[0] === 'number' && this.anchor_[0] > 0 &&
        this.anchor_[0] < this.height_) {
            style.push('height:' + (this.height_ - this.anchor_[0]) +
          'px; padding-top:' + this.anchor_[0] + 'px;');
        } else {
            style.push('height:' + this.height_ + 'px; line-height:' + this.height_ +
          'px;');
        }
        if (typeof this.anchor_[1] === 'number' && this.anchor_[1] > 0 &&
        this.anchor_[1] < this.width_) {
            style.push('width:' + (this.width_ - this.anchor_[1]) +
          'px; padding-left:' + this.anchor_[1] + 'px;');
        } else {
            style.push('width:' + this.width_ + 'px; text-align:center;');
        }
    } else {
        style.push('height:' + this.height_ + 'px; line-height:' +
        this.height_ + 'px; width:' + this.width_ + 'px; text-align:center;');
    }

    var txtColor = this.textColor_ ? this.textColor_ : 'black';
    var txtSize = this.textSize_ ? this.textSize_ : 11;

    style.push('cursor:pointer; top:' + pos.y + 'px; left:' +
      pos.x + 'px; color:' + txtColor + '; position:absolute; font-size:' +
      txtSize + 'px; font-family:Arial,sans-serif; font-weight:bold');
    return style.join('');
};