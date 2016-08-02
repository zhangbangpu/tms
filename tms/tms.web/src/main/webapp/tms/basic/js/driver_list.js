loadScript('js/hui/jquery.hui.dict.js', function () {
    var dict = $('#search_form').dict({setkeys:['truck_driver.status','islist']});
},true,true);

//查询名称与别名一致
$ips.load('fields','getConfig',{'modeltype':'driver'},function(data){
	$.each(data,function(k,v){
		if(k=="truck_driver.drivername" && v.nickname){
			$("#drivername_lable").text(lang_site[v.fieldname]);
		}else if(k=='truck_driver.phone' && v.nickname){
			$('#phone_lable').text(lang_site[v.fieldname]);
		}else if(k=='truck_driver.orgcode' && v.nickname){
			$('#orgcode_lable').text(lang_site[v.fieldname]);
		}else if(k=='truck_driver.fromdate' && v.nickname){
			$('#fromdate_lable').text(lang_site[v.fieldname]);
		}else if(k=='truck_driver.status' && v.nickname){
			$('#status_lable').text(lang_site[v.fieldname]);
		}
	});
});

var _driver_header;

//Registration validation script
loadScript("js/plugin/jquery-form/jquery-form.min.js");
var drivers = {};
var lang_site = {};

$(function(){
	var params1 = new Array();
    params1.push({'name':'module', 'value':'driver'});
    params1.push({'name':'type', 'value':'html'});
    getlanguage(params1);

    var params2 = new Array();
    params2.push({'name':'module', 'value':'driver'});
    params2.push({'name':'type', 'value':'js'});
    getlanguage(params2);
    
    params2.push({'name':'headkey', 'value':'driver'});
    getlanguage(params2);
    
    _driver_header = [
		              // 全选按钮 如果你的全选按钮不生效，很可能就是你缺少了某个class
		              {
		                  sTitle: '<label class="no-margin"><input type="checkbox" name="checkbox style-0 " class="checkbox style-0 checkAll" id="checkAll"><span></span></label>',
		                  sName: "idCheckbox",
		                  sWidth: "20px",
		                  sClass: "center",
		                  bSortable: false
		              },
		              {
		                  sTitle: lang_site.operation,       // 表头显示文字
		                  sName: "idAction",   // 字段名 如果返回数据有该字段，会自动填充，
												// 如果没有，需要在fnServerData 中设置， 或使用
												// fnRender 回调
		                  sWidth: "25px",      // 显示宽度
		                  sClass: "center",    // class
		                  bSortable: false     // 是否允许排序
		              },
		          ];
    
	//更多搜索条件按钮
	var btnMoreSearch = $("#btnMoreSearch");
	btnMoreSearch.attr("state", "close");
	btnMoreSearch.click(function() {
		if (btnMoreSearch.attr("state") == "close") {
			$(".widget-body-toolbar").css("height", "auto");
			btnMoreSearch.text(lang_site.less);
			btnMoreSearch.attr("state", "open");
		} else {
			$(".widget-body-toolbar").css("height", "70");
			btnMoreSearch.text(lang_site.more);
			btnMoreSearch.attr("state", "close");
		}
	});

	var alias = $ips.load('fields', 'getLayoutConfigs',{'modeltype':'driver'});
	if (alias && alias != 'false') {
	    $.each(alias, function(i, v) {
	        if (v.isshow == 0) {
	            $("#" + v.fieldname).hide();
	            $("#" + v.fieldname + "_div").hide();
	        }
	        $("#" + v.fieldname + '_lable').html(v.nickname);
	    });
	}
	//加载日期插件
	var begintime = moment().subtract('days',2).format('YY-MM-DD');
	var endtime = moment().format('YY-MM-DD');
	$('#fromdate').daterangepicker({
		singleDatePicker: false,
		timePicker: false, //是否启用时间选择
		timePickerIncrement: 1, //分钟选择的间隔
		format: 'YY-MM-DD', //返回值的格式
		timePicker12Hour: false, //采用24小时计时制
		locale: getLocale()
	});

	//搜索按钮
	$("#s_btn1").click(function() {
		$('#dt_basic').grid('fnPageChange', "first");
	});

	$("#s_btn2").click(function() {
		$('#dt_basic').grid('fnPageChange', "first");
	});
	//加载司机姓名选择框
	$("#drivername").select2({
	    placeholder: lang_site.plsEnterName,
	    minimumInputLength: 1,
	    multiple: false,
	    allowClear: true,
	    // 数据加载
	    query: function(e) {
	        $ips.load('driver', 'getDrivername', {'dterm': e.term}, function(data) {
	            var item=[];
	    		$.each(data.result,function(x,y){
	    			item.push({id: y.drivername, text: y.drivername });
	    		})
	            var data = {results: item};
	            e.callback(data);
	        });
	    },
	});

	//加载手机号选择框
	$("#pterm").select2({
	    placeholder: lang_site.plsEnterPhone,
	    minimumInputLength: 1,
	    multiple: false,
	    allowClear: true,
	    // 数据加载
	    query: function(e) {
	        $ips.load('driver', 'getPhone', {'pterm': e.term}, function(data) {
	            var item=[];
	    		$.each(data.result,function(x,y){
	    			item.push({id: y.phone, text: y.phone });
	    		});
	            var data = {results: item};
	            e.callback(data);
	        });
	    },
	});

	$("#driver_delete").click(function(){
		var ids = new Array();
		ids = getRowIds();
		if(ids.length>0){
			for(idx in ids){
				var driver = drivers[ids[idx]];
				if(driver.fromtype == 3){
					$ips.error(lang_site.sharedDriverExistsCannotDel);
					return false;
				}
			}
			$ips.confirm(lang_site.wantToDelRecord,function(btn) {
			    if (btn == lang_site.confirm_2) {
					$ips.load("driver", "delete", {'ids':ids}, function(data) {
						if(data.length == 0){
							$ips.succeed(lang_site.sucessDel);
							$('#dt_basic').grid("fnDraw");
						} else {
		            		 $ips.error(lang_site.deleteFailed);// + data
		            	 }
					});
			    }
			});
		}else{
			$ips.alert(lang_site.selectDelItem);
		}
		return false;// 防止点击提示框，跳转页面
	});
	var alias = $ips.load('settings','getTableHeader',{setkey:'driver'});
	if(alias && alias!=false){
		$.each(alias,function(i,v){
			_driver_header.push({sTitle:v.fieldtext,sName:v.fieldname});
		});
	}
	$("#saveIcBind").click(function(){
		$("#hidden_13").html('');
		var iccard = $("#iccard").val();
		var id = $("#driverid").val();
		$('#iccard_bind_form').validate({
			rules: {
				iccard: {// ic卡
					required: true,
					digits:true,
					maxlength:13,
					minlength:13
				}
			},
			messages: {
				iccard: {
					required: lang_site.plsFillCoding,
					digits:lang_site.plsFillNumber,
					maxlength:lang_site.enterThirteenDigitNo,
					minlength:lang_site.enterThirteenDigitNo
				}
			},
			errorPlacement : function(error, element) {
				error.insertAfter(element.parent());
				$(".bangdingIc").addClass("txt-color-red");
			}
		});
		//2014-4-30 zkg 修改字符类型为字符串类型,当需要用到的时候再进行int转换
		var b = '0';
		if(!$('#iccard_bind_form').validate().form()){
			return false;
		}
		for(var i=0;i<=12;i++){
			if(i!=12){
				//2014-4-30, zkg去掉parseInt转换，防止数字相加
				b += iccard[i];
			}else{
				b+='';
				//zkg 字符串转换为数字
				var c=parseInt(b.substr(b.length-1));
				if(c!=iccard[11]){
					$("#hidden_13").html("<font color='red'>"+lang_site.incorrectICNo+"</font>");
					$("#hidden_13").show();
					return false;
				}
			}
		}
		var result = $ips.load('driver','icCardBind',{driverid: id,status:0,iccard:iccard});
		if(result.code==0) {
			$ips.succeed(lang_site.bindSucc);
			$('#dt_basic').grid("fnDraw");
		} else {
			$ips.error(result.message);
		}
		
	});
});

// 导出按钮
loadScript('js/hui/jquery.hui.exportdata.js', function () {
    $('#export').exportdata({dataModule : 'driver',dataMethod:'getList',searchForm:'#search_form',title:lang_site.exportDrivers,partDataRows:10000,partSize:100});
},true,true);

loadScript('js/hui/jquery.hui.grid.js', function () {
    $('#dt_basic').grid({
        "aoColumns" : _driver_header,
        "fnServerData" : function(sSource, aoData, fnCallback) {
            // 搜索条件 键值对
        	var searchParams = $("#search_form").serializeArray();
        	var st = $('#status').val();
        	if(st == null){
        		searchParams.push({'name':'status','value':'all'});
        	}
            $ips.gridLoadData(sSource, aoData, fnCallback, "driver", "getList", searchParams, function(data) {
            	drivers = {};
                $.each(data.result, function(i, item) {
                	drivers[item.id]=item;
                    item.idCheckbox = '<label class="checkbox"><input id="' + item.id + '" type="checkbox" name="checkbox-inline" value="'+item.id+'" class="checkbox style-0"><span></span></label>';
                    item.idAction = '<div class="btn-group"><button data-toggle="dropdown" class="btn btn-default btn-xs dropdown-toggle">' +
                    '<i class="fa fa-pencil"></i>' +
                    '<i class="fa fa-caret-down"></i>' +
                    '</button>' +
                    '<ul class="dropdown-menu">' +
                    '<li><a href="javascript:void(0);" data-button-resource="782D8B29DA011CEB09E80BEDF56D7032" onclick="edit(\'' + item.id + '\')">'+lang_site.edit_2+'</a></li>' +
                    '<li><a href="javascript:void(0);" data-button-resource="8F01594727788B38506789BEBBC58C2D" onclick="driverDelete(\'' + item.id + '\')">'+lang_site.del+'</a></li>' +
                    '</ul></div>'; 
                    item.drivername = '<a href="javascript:void(0);" onclick=detail("' + item.id + '")>' + item.drivername + '</a>';
                    if(item.driverimage != null&&item.driverimage != undefined&&item.driverimage != ''){
                    	item.driverimage = '<a href="javascript:void(0);" class="photoTip" url="'+item.driverimage+'">'+lang_site.viewImg + '</a>';
                    }else{
                    	item.driverimage = '';
                    }
                    
                    if(item.fromtype == '3'){
                        item.drivername += '  <i class="fa fa-cloud-upload txt-color-orangeDark" title="'+lang_site.share+'"></i>';
                    }

                  // 身份证，驾驶证，从业资格证等附件
                     item.idcardattach = item.idcardattach != '' && item.idcardattach  ? '<a href="javascript:void(0)" class="extInfo"><input type="hidden" value="'+item.idcardattach+'">'+lang_site.view+'</a>' : '';
                     item.driverlicense = item.driverlicense != '' && item.driverlicense ? '<a href="javascript:void(0)" class="extInfo"><input type="hidden" value="'+item.driverlicense+'">'+lang_site.view+'</a>' : '';
                     item.workingcredential = item.workingcredential != '' && item.workingcredential ? '<a href="javascript:void(0)" class="extInfo"><input type="hidden" value="'+item.workingcredential+'">'+lang_site.view+'</a>' : '';
                     
                });
            });
        },
        "fnDrawCallback" : function (oSettings) {
            $(oSettings.nTable).find("tr:last td").css({
                "border": '1px solid #DDDDDD'
            });
            // 附件tooltip
			loadScript('/js/poshytip/jquery.poshytip.min.js',function() {
				$('.extInfo').each(function(){
					var src = $(this).find("input").val();
					if(src){
						var img = $("<img>").attr("src",src);
						$(this).poshytip({
							content:img[0],
							alignTo:"target",
							alignX:"left",
							alignY:"center",
							offsetX:10
						});
					}
				})
				
				$('.photoTip').each(function(){
					var src = $(this).attr('url');
					if(src){
						var img = $("<img>").attr("src",src).css({'height':'185px','width':'157px'});
						$(this).poshytip({
							content:img[0],
							alignTo:"target",
							alignX:"left",
							alignY:"center",
							offsetX:10
						});
					}
				})
				
			});
         },
	"aaSorting" : [[2 ,'desc']],//默认排序
	"oLanguage" : {"sUrl": $ips.appPath + getLanguageTxt()},
    });
});

// 获取选中的id
function getRowIds(){
	var id = '' ;
	$('#dt_basic input:checkbox[class="checkbox style-0"]:checked').each(function(){ 
		if(id==""){
			id += $(this).val();
		}else{
			id += ',' + $(this).val();
		}
        
    });
	if(id==""){
		return [];
	}else{
		return id.split(",");
	}
	
	
}

function edit(id){
	var driver = drivers[id];
	if(driver.fromtype == 3){
		$ips.error(lang_site.sharedDriverCannotModify);
		return false;
	}
	window.location.href = '#driver/add.html?id='+id;
}

function detail(id){
	window.location.href = '#driver/detail.html?id='+id;
}

function driverDelete(id) {
	var driver = drivers[id];
	if(driver.fromtype == 3){
		$ips.error(lang_site.sharedDriverExistsCannotDel);
		return false;
	}
	var ids = new Array();
	if(id!=""){
		ids.push(id);
	}else{
		ids = getRowIds();
	}
	
	if(id.length>0){
		$ips.confirm(lang_site.comfirmDelRecord,function(btn) {
	        if (btn == lang_site.confirm_2) {
	            $ips.load("driver", "delete", {ids:ids}, function(result){
	                if(result.length == 0) {
	                	$ips.succeed(lang_site.sucessDel);
	                	$('#dt_basic').grid('fnPageChange', "first");
	            	 } else {
	            		 $ips.error(lang_site.failRMdriverInfo);// + result
	            	 }
	            });
			}
	    });
	}else{
		$ips.alert(lang_site.selectDelItem);
		return false;
		
	}
    
}

function add_zero(temp){
    if (temp < 10)
        return "0" + temp;
    else
        return temp;
}
function bindIcCard(){
	var selected = getRowIds();
	$(".bangdingIc").removeClass("txt-color-red");
	$("#iccard").val('');//点击绑定清空原文本框的值
	if(selected){
		id = selected[0];
		var driver = drivers[id];
		if(driver.fromtype == 3){
			$ips.error(lang_site.sharedDriverCannotModify);
			return false;
		}
		var $work = $('#boxWork');// box
		var card = $ips.load('driver','getDriverById',{id:id,isedit:1});
		cardno = card.iccard;
		if(!(cardno=="" || cardno == null || cardno == 'null')){
		$('input[name="driverid"]',$work).val('');// 清空
			$ips.alert(lang_site.unbindICFirst);
			return ;
		} else {
			$('input[name="driverid"]',$work).val(id); //zkg 填充driverid值
		}
		
		$('#boxWork').modal();
	}
}

function unBindIcCard(){
	var selected = getRowIds();
	if(selected){
		id = selected[0];
		var driver = drivers[id];
		if(driver.fromtype == 3){
			$ips.error(lang_site.sharedDriverCannotModify);
			return false;
		}
		// 根据driverid获取iccard号
		var driverinfo = $ips.load('driver','getDriverById',{id:id});
		iccard = driverinfo.iccard;
		if(!(!iccard && typeof(iccard)!="undefined" && iccard!=0) && iccard!=""){
			$ips.confirm(lang_site.comfirmUnboundIC+iccard+lang_site.unboundComfirm,function(c) {
				if(c=='ok' || c==lang_site.confirm_2){
					var result = $ips.load('driver','icCardBind',{driverid: id,status:1,iccard:iccard});
					if(result.code==0) {
						$ips.succeed(lang_site.unbundSucc);
						$('#dt_basic').grid("fnDraw");
					} else {
						$ips.error(result.message);
					}
				}
			});
		}else{
			$ips.alert(lang_site.donotUnbindIC);
		}
	}
}
function unBindWeiXin(){
	var selected = getRowIds();
	if(selected){
		id = selected[0];
		var driver = drivers[id];
		if(driver.fromtype == 3){
			$ips.error(lang_site.sharedDriverCannotModify);
			return false;
		}
		// 根据driverid获取iccard号
		var driverinfo = $ips.load('driver','getDriverById',{id:id});
		weixin = driverinfo.wechat_openid;

		if(!(!weixin && typeof(weixin)!="undefined" && weixin!=0) && weixin!=""){
			if(weixin == '是'){
                weixin = lang_site.yes_shi;
			}
			if(weixin == '否'){
                weixin = lang_site.no_fou;
			}
			$ips.confirm(lang_site.comfirmUndoundWechat+weixin+lang_site.unboundComfirm,function(c) {
				if(c=='ok' || c==lang_site.confirm_2){
					$ips.load('driver','updateField',{driverid: id, fieldName: 'wechat_openid', fieldValue :''}, function(rel){
						
						if (rel > 0) {
							$ips.succeed(lang_site.unbundSucc);
							$('#dt_basic').grid("fnDraw");
						};

					});
				}
			});
		}else{
			$ips.alert(lang_site.donotUnbindWechat);
		}
	}
}


loadScript("/js/hui/jquery.hui.tree.js",function() {
    $('#orgname').tree({
        type : 'select',
        method : 'getOrgans',
        module : 'truck',
        selectid : 'orgcodes',
        params : {
            selectids : ''
        },
        callback : function(result) {
        }
    });
},true,true);
