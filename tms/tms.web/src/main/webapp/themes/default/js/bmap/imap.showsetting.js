/**
 * 全图的显示设置 - dupeng 2011/11/24
 * @param object huimap 地图对象
 * @param object options 覆盖默认显示设置
 */

imap.showsetting = function(id,huimap,options,toptions) {
    if(!id || !huimap) {
		$ips.error('error');
        return;
    }
	this.id = id;
    this.huimap = huimap;
    this.huimapObj = this.huimap.ditu('getMap');
    var self = this;
    if(options) {
		$.each(options,function(k,v) {
			if(self.options[k]) {
				$.extend(self.options[k],v);
			}
		});
	}
	if(toptions) {
		$.extend(this.toptions,toptions);
		if(toptions.checkVals) {
			$.each(toptions.checkVals, function(type,value) {
				if(value) {
					if('other' == type) {
						$.extend(self.options.other,value);
					} else if('undefined' != typeof self.options[type]) {
						self.options[type].checked = value.show == 1 ? true : false;
						var z = value.level;
						if('undefined' != typeof self.options[type].max && self.options[type].max[0] == z) {
							self.options[type].max[1] = true;
							self.options[type].min[1] = false;
						};
						if(value.types) self.options[type].types = value.types;
						if(value.names) self.options[type].names = value.names;
					}
				}
			});
		}
	}
    //初始化imap对象
    this._create();
}

imap.showsetting.prototype = {
	id			: null,		//html元素ID
    huimap      : null,     //hui地图对象
    huimapObj   : null,     //地图对象
	_filterId	: '1',		//车辆筛选当前筛选ID
	_lockTruck	: null,
	_create		: function() {
		//初始化
		var self = this;
		//监控地图级别的变化
		self.huimapObj.addEventListener("zoomend", function() {
			$('#'+self.id+' .level span').html(self.huimap.ditu('getZoom'));
		});
		self._createhtml();
	},
	_createhtml	: function() {
		var self = this,
			id = self.id,
			opts = self.options,
			topts = self.toptions,
			html = '',
			single = opts.truck.display ? '' : ' single',
			truckViewClass = 't_icon',
			zoom = topts.zoom || '5';
		if(topts.filterId) self._filterId = topts.filterId;
		self._lockTruck = !!!opts.truck.checked;
		if(topts.icontype == 'tnt') truckViewClass = 't_iconTnt';
		$.each(opts,function(type,opt) {
			opt.addclass = '';
            if(opt.checked == false) {
                opt.addclass = ' disable';
            }
        });
		var truckView=1;
		var isheadstock;
		html += '<div style="position:relative"><div class="level'+single+'">地图级别 <span style="font-size:16px;">'+zoom+'</span></div><table border="0" cellspacing="0" cellpadding="0">';

		if(opts.truck.display) {
			//车辆筛选
			html += '<tr class="line"><td width="100px;">&nbsp;</td><td align="center">'+
				'<div style="color:#fff;">车辆筛选</div></td><td colspan="2">'+
				'<div id="truckView_selectedBox" class="map_tview"><div id="t_showNow">'+
				'<div class="'+truckViewClass+' ti0"></div><div class="t_label">全部车辆</div></div><div class="t_arrow"></div><ul id="truckView_selectPop" style="display:none">';

			var ft = topts.filterKeys.split('&');
			$.each(self._filterContents,function(k,v) {
				var nm = v.name, tt = v.title;
				if($.inArray(nm,ft) > -1) {
					if(tt) {
						html += '<li class="title">'+tt+'</li>';
					}
					$.each(v.contents,function(k1,v1) {
						var sty = '', icondiv = labelcls = '';
						if(v1.cls) icondiv = '<div class="'+truckViewClass+' '+v1.cls+'"></div>';
						else labelcls = 'noico';
						if(v1.key == self._filterId) sty = ' style="display:none;"';
						html += '<li'+sty+'><a href="javascript:void(0);" id="truckView_Option_'+v1.key+'">'+icondiv+'<div class="t_label '+labelcls+'">'+v1.html+'</div></a></li>';
					});
				}
			});

			html += '</ul></div></td></tr>';
		}
		html += '<tr><td align="right" width="100px">自动显示(<a href="javascript:void(0);" id="ms">设置</a>)：</td>'+
			'<td><div id="pop_showmarker_switch_icon" class="mark m2'+opts.marker.addclass+'"><span>标注点</div></td>'+
			'<td><div id="pop_showpolygon_switch_icon" class="mark m3'+opts.polygon.addclass+'"><span>区域</div></td>'+
			'<td><div id="pop_showline_switch_icon" class="mark m4'+opts.line.addclass+'"><span>线路</div></td></tr></table>';

		//自动显示的复选框和单选框
		html += '<div class="showSet" id="map_showSet" style="display:none"><ul>';
		$.each(opts,function(type,opt) {
            if(opt.display) {
				var truckcondition="";
                var lang_c = self._lang[type],
                	labellang = '自动显示'+lang_c,
                    c_checked = opt.checked ? ' checked="checked"' : '',
                    r1_checked = '',
                    r2_checked = '';
                var multisel = minsel = maxsel = '',
                	names = opt.names || '',
                	types = opt.types || '';
                if(type == 'marker' || type == 'polygon')
                	multisel = '<label><input type="text" size="6" id="pop_show'+type+'_choice" value="'+names+'" placeholder="选择类别" /><input type="hidden" id="pop_show'+type+'_choice_val" value="'+types+'" /></label>';
				if(type=="truck"){
					var headstock_chk = opts.other.headstock == '1' ? ' checked="checked"' : '';
					var trailer_chk = opts.other.trailer == '1' ? ' checked="checked"' : '';
					truckcondition='<input name="headstock_switch" type="checkbox" id="headstock_switch" '+headstock_chk+' />车头<input name="trailer_switch" type="checkbox" id="trailer_switch" '+trailer_chk+' />挂车';
				}

				if(opt.min) {
					if(opt.min[1]) r1_checked = ' checked="checked"';
					minsel = '<label style="color:green;"><input type="radio" value="'+opt.min[0]+'" name="pop_show'+type+'" '+r1_checked+' />'+opt.min[0]+'级</label>';
				}
				if(opt.max) {
					if(opt.max[1]) r2_checked = ' checked="checked"';
					maxsel = '<label style="color:green;"><input type="radio" value="'+opt.max[0]+'" name="pop_show'+type+'" '+r2_checked+' />'+opt.max[0]+'级</label>';
				}

				html += '<li><label class="title"><input type="checkbox" id="pop_show'+type+'_switch" name="pop_show'+type+'_switch" '+c_checked+' />'+labellang+'</label>'+
					minsel+maxsel+multisel+truckcondition+'</li>';
				if(type == 'truck') {
					var c_checked2 = opts.other.pop_usecluster == '1' ? ' checked="checked"' : '';
					html += '<li><div style="padding-left:20px;clear:both;overflow:hidden;"><label class="title" style="width:auto;"><input type="checkbox" id="pop_usecluster_switch" name="pop_usecluster_switch" '+c_checked2+' />以聚合方式显示车辆</label></div></li>';
					var c_checked3 = opts.other.pop_truckcarnum == '1' ? ' checked="checked"' : '',
						c_checked3_val = opts.other.pop_truckcarnum_val || '';
					html += '<li><div style="padding-left:20px;clear:both;overflow:hidden;"><label class="title" style="width:auto;"><input type="checkbox" id="pop_truckcarnum_switch" name="pop_truckcarnum_switch" '+c_checked3+' />自动显示车牌</label><input type="text" size="1" id="pop_truckcarnum_val" name="pop_truckcarnum_val" style="border:1px solid;" value="'+c_checked3_val+'" />级</div></li>';
				}
            }
        });

		//释放滚轮
		var c_checked = opts.other.pop_scrollwheel == '1' ? ' checked="checked"' : '';
		html += '<li><label class="title"><input type="checkbox" id="pop_scrollwheel_switch" name="pop_scrollwheel_switch" '+c_checked+' />释放滚轮</label></li>';

		if(topts.tntcustomer) {
			html += '<li><label><input type="checkbox" onclick="itntcustomer.change(\'round\',this)" checked />Round</label>'+
					'<label><select id="tntcustomer_round" style="width:50px;" onchange="itntcustomer.change(\'round\',this)"><option value="">全部</option></select></label>'+
					'<label><input type="checkbox" onclick="itntcustomer.change(\'territory\',this)" checked />Territory</label>'+
					'<label><select id="tntcustomer_territory" style="width:50px;" onchange="itntcustomer.change(\'territory\',this)"><option value="">全部</option></select></label>'+
					'</li>';
			$ips.load('customertnt','getcustomertypes',null,function(data) {
				$.each(data,function(k,v) {
					$.each(v,function(k1,v1) {
						$('#tntcustomer_'+k).append('<option value="'+v1+'">'+v1+'</option>');
					});
				});
			});
		}


		html += '</ul></div>';
		html += '</div>';
		$('#'+id).html(html);
		if(opts.truck.display) {
			self.setFilterData();
		}

		//事件
		//设置显示隐藏
		$("#ms").toggle(function(){
				$('#map_showSet').show();
				$(this).html("完成");
		},function(){
				$('#map_showSet').hide();
				$(this).html("设置");
		});

		
		//显示设置的更改事件
		$("#map_showSet ul input").each(function() {
            var that = this;
            $(that).click(function() {
                var type = $(this).attr('type');
                if(type == 'checkbox') {    //显示开关
                    var id = $(this).attr('id'),
						chk = $(this).attr('checked'),
                        objstr = $(this).attr('name').replace('pop_show','').replace('_switch',''),
                        obj = null;
                    if('pop_usecluster' == objstr) {
                    	if('undefined' != itruck) itruck.changeCluster(chk);
                    } else if('pop_truckcarnum' == objstr) {
                    	var zoom = $('#pop_truckcarnum_val').val() || 0;
                    	//小于5不合法
                    	if(zoom < 5) {
                    		zoom = 5;
                    		$('#pop_truckcarnum_val').val('5');
                    		$('#pop_truckcarnum_val').css('border','1px solid');
                    	}
                    	if(!chk) zoom = 20;
                    	if('undefined' != itruck) itruck.changeShowLabelZoom(zoom);
                    } else if('pop_scrollwheel' == objstr) {
                    	if(chk) self.huimapObj.enableScrollWheelZoom();
                    	else self.huimapObj.disableScrollWheelZoom();
                    } else {
	                    if(objstr){
							if(objstr == "headstock" || objstr == "trailer"){
								switch (objstr){
									case "headstock":
										if($("#trailer_switch").attr('checked')){
											isheadstock=1;
										}else{
											isheadstock=9;
										}
										break;
									case "trailer":
										if($("#headstock_switch").attr('checked')){
											isheadstock=1;
										}else{
											isheadstock=10;
										}
										break;
								}
								if(chk){
									itruck._changeMarkerVisible(truckView,"","","",isheadstock);
								}else{
									if(isheadstock==1){
										if(objstr=="headstock"){
											isheadstock=9;
										}else{
											isheadstock=10;
										}
									}else{
										isheadstock=11;//数值设置为不进入switch中的数值
									}
									itruck._changeMarkerVisible(truckView,"","","",isheadstock+2);
								}
							}else{
								obj = eval('i'+objstr);
							}
						}
	                    if(obj) {
							if($("#headstock_switch").attr('checked') && $("#trailer_switch").attr('checked')){
								isheadstock=1
							}else if($("#headstock_switch").attr('checked')){
								isheadstock=9
							}else if($("#trailer_switch").attr('checked')){
								isheadstock=10
							}
	                    	obj.setEnabled(chk,truckView,isheadstock);
							if(objstr!='truck') {
								if(chk) {
									$('#'+id+'_icon').removeClass('disable');
								} else {
									$('#'+id+'_icon').addClass('disable');
								}
							} else {
								obj.getData();
								self._lockTruck = !chk;
							}
	                    }
                    }
                    self.saveSetting();
                } else if(type == 'radio') {    //缩放级别
                    var val = $(this).val(),
                        objstr = $(this).attr('name').replace('pop_show',''),
                        obj = null;
                    if(objstr) obj = eval('i'+objstr);
                    if(obj) {
                        obj.setMinzoom(val);
                    }
                    self.saveSetting();
                }
            });
        });
		$('#pop_truckcarnum_val').blur(function() {
			var zoom = $(this).val();
			if(zoom < 5) {
				$(this).css('border','1px solid red');
			} else {
				$(this).css('border','1px solid');
				if('undefined' != itruck && $('#pop_truckcarnum_switch').attr('checked') == true) itruck.changeShowLabelZoom(zoom);
				self.saveSetting();
			}
		});
		//选择类别 - 初始化多选框
		$('#pop_showmarker_choice, #pop_showpolygon_choice').each(function() {
			var that = this,
				type = $(that).attr('id').replace('pop_show','').replace('_choice','');
				selvalele = $('#' + $(that).attr('id') + '_val');
			$(that).multiselect({
	    		method:'markertype',
	    		params:{isroot:1},
	    		selectids:self.options[type].types,
	    		callback: (function(selvalele,type) {
	    			return function(data) {
			            var val = data.vals && data.vals.length > 0 ? data.vals.join(',') : '',
			                html = data.htmls && data.htmls.length > 0 ? data.htmls.join(',') : '';
			            selvalele.val(val);
			            $(that).val(html);
			            self.saveSetting();
			            //setOptions
			            var obj = null;
			            if(type) obj = eval('i'+type);
			            if(obj) {
			            	obj.setOptions({mtypes: val});
			            	obj.deleteAll();
			            	obj.getData();
			        	}
		    		}
	    		})(selvalele,type)
	    	});
		});
		
		//车辆筛选切换
		$("#t_showNow").click(function() {
			if(self._lockTruck) return;
			$("#truckView_selectPop").toggle();
			$("#truckView_selectedBox").toggleClass("active");
		});
		$("#truckView_selectPop li a").click(function() {
			var t = $(this),
				text = t.html(),
				val = t.attr('id').replace('truckView_Option_','');
			truckView=val;
			$('#truckView_Option_'+self._filterId).parent('li').show();
			t.parent('li').hide();
			self._filterId = val;
			var is9=$("#headstock_switch").attr("checked");
			var is10=$("#trailer_switch").attr("checked");
			if(is9 && is10){
				isheadstock=1;
			}else if(is9){
				isheadstock=9;
			}else if(is10){
				isheadstock=10;
			}
			if(itruck) itruck._changeMarkerVisible(val,"","","",isheadstock);
			$("#t_showNow").html(text);
			$("#truckView_selectPop").hide();
			$("#truckView_selectedBox").removeClass("active");
		});
		$(document).bind('mouseup', function(e) {
			var clicked = $(e.target);
			if (! clicked.parents().hasClass("map_tview")){
				$("#truckView_selectPop").hide();
				$("#truckView_selectedBox").removeClass("active");
			}
		});
	},
	saveSetting		: function() {
		var setting = {
			truck: {},
			marker: {},
			polygon: {},
			line: {},
			other: {}
		};
		var nArr = ['pop_usecluster','pop_truckcarnum','pop_scrollwheel','headstock','trailer'];
		$("#map_showSet ul input").each(function() {
			var that = this;
			var type = $(that).attr('type');
            if(type == 'checkbox') {    //显示开关
                var id = $(this).attr('id'),
					chk = $(this).attr('checked') ? 1 : 0,
                    objstr = $(this).attr('name').replace('pop_show','').replace('_switch','');
                if($.inArray(objstr,nArr) === -1) {
                	setting[objstr].show = chk;
                }
            } else if(type == 'radio') {    //缩放级别
                var val = $(this).val(),
					chk = $(this).attr('checked') ? val : 0,
                    objstr = $(this).attr('name').replace('pop_show','');
                if(chk) setting[objstr].level = chk;
            } else if(type == 'hidden') {	//类别值
            	var val = $(this).val(),
            		objstr = $(this).attr('id').replace('pop_show','').replace('_choice_val','');
            	setting[objstr].types = val;
            } else if(type == 'text') {	//类别名称
            	var val = $(this).val(),
            		objstr = $(this).attr('id').replace('pop_show','').replace('_choice','');
            	if(objstr != 'pop_truckcarnum_val') {
            		setting[objstr].names = val;
            	}
            }
		});
		//其他设置
		$.each(nArr, function(k,v) {
			var chk = $('#'+v+'_switch').attr('checked') ? 1 : 0;
			setting['other'][v] = chk;
		});
		setting['other']['pop_truckcarnum_val'] = $('#pop_truckcarnum_val').val();
		$ips.load('map','setisetting',{data:setting},function(data) {
		});
	},
    setFilterData   : function() {
		var self = this;
        $ips.load('message','truckStat','mappage=1',function(data) {
            if(data) {
                var countsArr = {'1':data.total,'2':data.run,'3':data.stop,'4':data.leave};
                $.each(countsArr,function(k,v) {
                    var html = self._filterId2Ch[k] + ' ' + v;
                    $('#truckView_Option_'+k+' div[class^="t_label"]').html(html);
					if(self._filterId == k) {
						$('#t_showNow div[class^="t_label"]').html(html);
					}
                });
            }
        });
        window.setTimeout('isetting.setFilterData()',120000);
    },
	setFilterTaskCount	: function(dt) {
		var self = this;
		$.each(dt,function(k,v) {
			var html = self._filterId2Ch[k] + ' ' + v;
			$('#truckView_Option_'+k+' div[class^="t_label"]').html(html);
			if(self._filterId == k) {
				$('#t_showNow div[class^="t_label"]').html(html);
			}
		});
	},
    setcheckval : function(type,val) {
        var self = this,
            opts = self.options,
            val = val || false;
        if(!opts[type].display) return;
        opts[type].checked = val;
        var id = 'pop_show'+type+'_switch';
        if($('#'+id).length && val != $('#'+id).attr('checked')) {
            $('#'+id).click();
        	if(type == 'truck') {
        		self._lockTruck = !val;
        	} else {
				if(val) {
					$('#'+id+'_icon').removeClass('disable');
				} else {
					$('#'+id+'_icon').addClass('disable');
				}
			}
        }
    },
	_lang        : {
        truck       : '车辆',
        marker      : '标注点',
        line        : '线路',
        polygon     : '区域',
        alarm       : '报警',
        level       : '级'
    },
	_filterContents	: [{
		name		: 'all',
		title		: null,
		contents	: [{
			html	: '全部车辆',
			cls		: 'ti0',
			key		: '1'
		}]
	},{
		name		: 'task',
		title		: '任务状态',
		contents	: [{
			html	: '待发车',
			cls		: null,
			key		: '5'
		},{
			html	: '在途',
			cls		: null,
			key		: '6'
		},{
			html	: '无任务',
			cls		: null,
			key		: '7'
		},{
			html	: '晚发仍未发',
			cls		: null,
			key		: '8'
		}]
	},{
		name		: 'run',
		title		: '行驶状态',
		contents	: [{
			html	: '运行',
			cls		: 'ti1',
			key		: '2'
		},{
			html	: '静止',
			cls		: 'ti2',
			key		: '3'
		},{
			html	: '离线',
			cls		: 'ti3',
			key		: '4'
		}]
	}],
    _filterId2Ch    : {
        '1'     : '全部车辆',
        '2'     : '行驶',
        '3'     : '静止',
        '4'     : '离线',
		'5'		: '待发车',
		'6'		: '在途',
		'7'		: '无任务',
		'8'		: '晚发仍未发'
    },
	toptions	: {
		filterKeys	: 'all&run',
		filterId	: null,
		checkVals	: null
	},
    options   : {
        truck       : {
            display     : true,
            checked     : true
        },
        marker      : {
            display     : true,
            checked     : true,
            min         : [7,true],
            max         : [12,false],
            names 		: '',
            types 		: ''
        },
        line        : {
            display     : true,
            checked     : false,
            min         : [7,true],
            max         : [12,false]
        },
        polygon     : {
            display     : true,
            checked     : true,
            min         : [6,true],
            max         : [10,false],
            names 		: '',
            types 		: ''
        },
        alarm       : {
            display     : false,
            checked     : false,
            min         : [6,true],
            max         : [10,false]
        },
        other		: {
            pop_usecluster 		: 1,
            pop_truckcarnum 	: 1,
            pop_scrollwheel 	: 0,
            pop_truckcarnum_val : 12,
			headstock			: 1,
			trailer 			: 1
        }
    },
    getparams   : function() {
        return this.options;
    }
};
