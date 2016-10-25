//更多搜索条件按钮!
var searchParams = '';//初始化数据

// 搜索按钮
$("#s_btn1").click(function() {
	$('#tblMain').grid("fnPageChange", "first");
});

//搜索条件 回车 触发
$('#frmSearch input').keydown(function(e){
	if(e.keyCode==13){
		$('#tblMain').grid("fnPageChange", "first");
	}
});

function genSearchParams(){
	searchParams = $("#frmSearch").serializeArray();
	return searchParams;
}


/**
 * 开启自动调度
 */
$("#btn_auto_start").click(function(){
    $ips.confirm("您确定要开启自动调度吗?",function(btn) {
        if (btn == "确定") {
            $ips.load("orders", "autoGenerateWaybill", {
            	//无参数
            }, function(result){
            	console.log(result);
            	var msg = result.message;
                if(msg == '') {
            		 $ips.succeed("自动生成运单成功!");
            		 $('#tblMain').grid("fnDraw");
            	 } else {
            		 $ips.error(msg);
            	 }
            });
		}
    });
});


function ordersDelete(id) {
    $ips.confirm("您确定要删除这条记录吗?",function(btn) {
        if (btn == "确定") {
            $ips.load("orders", "deleteById", "ids=" + id, function(result){
                if(result.code == 0) {
            		 $ips.succeed("删除成功。");
            		 $('#tblMain').grid("fnDraw");
            	 } else {
            		 $ips.error("删除失败！" + result);
            	 }
            });
		}
    });
}

function orderDetail(id) {
    if (id == null) {
        var id = getRowId();
    }
    if (!id) return;
    var urls = "tms/order/order_detail.html?id=" + id;
    $ips.locatesubsystem(urls, true);
}

//获取选择的id
function getRowIds(array) {
    var id = '';
    var idArray = new Array();
    $('#tblMain tbody input:checkbox[class="checkbox style-0"]:checked').each(function() {
        if(id==""){
            id += $(this).val();
        }else{
            id += ',' + $(this).val();
        }
        idArray.push($(this).val());
    });
    if(array){
    	return idArray;
    }
    return id;
}

/**
 * 删除
 */
$("#deletebtn").click(function(){
	
	var ids = getRowIds(true);
//	if(ids.length != 1) {
//		ids.length>1?$ips.error("只能删除一条！"):$ips.error("未选择记录！");
//        return;
//    }
	ordersDelete(ids);
//    $ips.confirm("您确定要删除选中的记录吗?",function(btn) {
//        if (btn == "确定") {
//            $ips.load("orders", "deleteById", "ids=" + ids, function(result){
//            	console.log(result.code == 0);
//                if(result.code == 0) {
//            		 $ips.succeed("删除成功。");
//            		 $('#tblMain').grid("fnDraw");
//            	 } else {
//            		 $ips.error("删除失败！" + result);
//            	 }
//            });
//		}
//    });
});

/**
 * 手动下发车次
 */
//var waybillRow = $('#btn_depature').closest('div');
//
$("#btn_depature_init").bind('click', function() {
	var ids = getRowIds(true);
    var length = ids.length;
    if (length < 1) {
        $ips.error('请至少选择一个订单');
        return;
    }
    
    //检查订单状态和类型
    $ips.load("orders", "checkOrders", "ids=" + ids, function(result){
    	//{ids : ids} 参数异常
        if(result.msg == '') {
        	$('#departureModal').modal('show');
    	 } else {
    		 $ips.error(result);
    		 return;
    	 }
    });
    
    //加载 承运商下拉框数据
    $("#r_wlcompany").select2({
        placeholder: '请选择承运商',
//        minimumInputLength: 2,
        multiple: false,
        allowClear: true,
        //数据加载
        query: function (query) {
            $ips.load('sysUser', 'queryWL2combo', {
                name: query.term 
            }, function (e) {
                var _pre_data = [];
                $.each(e, function (k, v) {
                	_pre_data.push({
                		id: v.id,
                		text: v.name
                	});
                });
                var data = {
                    results: _pre_data
                };
                query.callback(data);
            });
        }
    });
    
    $("#r_wlcompany").on('change',function(data){
//    	console.info(data);
//    	console.info('-----');
    	var wlcompanyId = data.added.id
    	console.info(wlcompanyId);
    	
    	//加载 车型下拉框数据
    	$("#vehiclemodel").select2({
    		placeholder: '请选择车型',
//    		minimumInputLength: 2,
    		multiple: false,
    		allowClear: true,
    		//数据加载
    		query: function (query) {
    			$ips.load('vehicleModel', 'selectAllVehicleModelByCtn', {
    				wlcompany : wlcompanyId,
    				keyword: query.term
    			}, function (e) {
    				var _pre_data = [];
    				$.each(e, function (k, v) {
    					_pre_data.push({
    						id: v.id,
    						text: "重量(kg)："+ v.weight +" ,体积(m³)："+ v.volum
    					});
    				});
    				var data = {
    						results: _pre_data
    				};
    				query.callback(data);
    				
    				//级联 车型
    				
    			});
    		}
    	});
    });
    
});


$("#btn_depature").bind('click', function() {
//	$("#generateWaybill").bind('click', function() {
	var ids = getRowIds(false);
	if ($('#wlcompany').val() == '' || $('#wlcompany').val() == '') {
		$ips.error('承运商不能为空');
		return false;
	}
	if ($('#vehiclemodel').val() == '' || $('#vehiclemodel').val() == '') {
		$ips.error('车型不能为空!');
		return false;
	}

	$ips.load('tckNum', 'addTckNum', {
		ordersid : ids,
		wlcompany : $('#wlcompany').val(),
		vehiclemodel : $('#vehiclemodel').val()
	}, function(res) {
		if (typeof res.code != 'undefined' && res.code != 0) {
			$ips.error(res.message);
			return false;
		}
		$ips.succeed('生成运单成功');
		$('#departureModal .modal-header button').trigger('click');
		$('#departureModal').modal('hide');
	})
	/*$ips.confirm("您确定要生成运单吗?",function(btn) {
	      if (btn == "确定") {
	      }
	});*/
	
	return false;
});

//waybillRow.empty();
//$('<div class="btn-group"><a class="btn btn-default" data-button-resource="048625502851586667FA938190987180">生成运单</a></div>').bind('click', function() {
//	var ids = getRowIds(false);
//	var orders = $ips.load("orders", "queryStatusById", {id : ids});
//  
//	if (orders.length > 0) {
//		$ips.error("订单号的状态不正确，生成运单失败！");
//		return false;
//	}
//	$(this).attr({'data-toggle' : "modal",'data-target' : "#myModal"})
//	$('#wlcompany').val('');
//	$('#vehiclemodel').val('');
//}).appendTo(waybillRow);

// 表格控件
loadScript('js/hui/jquery.hui.grid.js', function () {
	$('#tblMain').grid({
		"aoColumns" : [
           	{
                sTitle: '<label class="no-margin">\
                	<input type="checkbox" name="checkbox style-0 " class="checkbox style-0 checkAll" id="checkAll">\
                	<span></span>\
                </label>',
                sName: "id",
                sWidth: "20px",
                sClass: "center",
                bSortable: false, 
                mRender:function(data, type, full){
					return '<label class="checkbox">\
					<input id="' + data + '" type="checkbox" name="checkbox-inline" value="'+data+'" class="checkbox style-0" />\
					<span></span>\
				</label>';
				}
            },{
                sTitle: "操作",
                sName: "id",
                sWidth: "25px",
                sClass: "center",
                bSortable: false,
                mRender:function(data, type, full){
                	return '<div class="btn-group">' 
                	+ '<button data-toggle="dropdown" class="btn ' + 'btn-default btn-xs dropdown-toggle">' + '<i class="fa fa-pencil"></i>' + '<i class="fa fa-caret-down"></i>' + '</button>'
                    + '<ul class="dropdown-menu">' +
                    '<li>' +
                    '<a href="#tms/order/order_detail.html?id='+ data +'">订单详情</a>' +
                    '</li>' +
                    '<li class="divider"></li>' +
                    '<li>' + '<a href="javascript:void(0);" onclick="' + 'ordersDelete(\'' + data + '\')">' + '删除</a>'
                    + '</li>'
                    + '</ul>'
                    + '</div>';
				}
            },
                {sTitle: "订单号", sName: "code"},
                {sTitle: "订单来源编号", sName: "fromcode"},
                {sTitle: "订单来源", sName: "orderfrom"},
                {sTitle: "承运商", sName: "subcontractor"},
                {sTitle: "货品名称", sName: "cpmdName"},
                {sTitle: "货品数量", sName: "amount"},
                {sTitle: "货品重量", sName: "weight"},
                {sTitle: "货品体积", sName: "volume"},
                {sTitle: "发货地址", sName: "fhaddress"},
                {sTitle: "收货地址", sName: "shaddress"},
                {sTitle: "订单状态", sName: "state",
                	mRender:function(data, type, full){
                		if(data == 0){
                			return "初始";
                		}else if(data == 1){
                			return "下发";
                		}else if(data == 2){
                			return "在途运输";
                		}else if(data == 3){
                			return "签收";
                		}
                	}
                },
                {sTitle: "订单来源类型", sName: "status",
                	mRender:function(data, type, full){
                		if(data == 0){
                			return "自动生成";
                		}else if(data == 1){
                			return "手动下发";
                		}
                	}
                },
                {sTitle: "异常事件数", sName: "exceptcount"},
                /*{sTitle: "状态", sName: "status", 
                	mRender:function(data, type, full){
//                        		console.log(full[4]);
                		var dateStr = full[5].replace(/-/g,"/");
                		var update_date = new Date(dateStr);
                		var new_date = new Date();
                		var num = (new_date-update_date)/1000/60;//单位是毫秒
//                        		console.log("num:"+num);
//                        		console.log(typeof num);
                		if(data == 0 || num>3){
                			return "<span style='background-color:red;'>关闭</span>";
                		}else if(data == 1){
                			return "连接";
                		}
                	}
                },*/
                {sTitle: "更新时间", sName: "updatetime"}
			]
		,
		"fnServerData" : function(sSource, aoData, fnCallback) {
			searchParams = genSearchParams();
			$ips.gridLoadData(sSource, aoData, fnCallback, "orders", "page", searchParams, function(data) {
//				console.log(typeof data);
//				console.log(data);
				var dataArray = new Array();
				$.each(data.result, function(i, item) {
					dataArray[item.id+""] = item;
				});
				parmData = dataArray;
			});
		}
	});
});


//运力拆单按钮(被限定为只有未生成运单时才能拆单)
$(function () {
    $('#btn_splitOrder').on('click', function () {
        CheckParentId = getRowIds();
        if (CheckParentId == false) {
            return;
        }
        StepConfig = ['pickup', 'transitions', 'delivery'];
        GoodInfo = ['goodname', 'unit', 'number', 'weight', 'volume'];
        GoodstepInfo = [];
        $('#childOrderList,#goodsTable').html('');
        if (undefined != CheckParentId) {
            var result = $ips.load('orders', 'queryOrderAndItemById', {id: CheckParentId});
            console.log(result);
            if (result) {
//                var user = $ips.getCurrentUser();
//                //条件判断
//                if (result.parent.parentid != null && result.parent.createuser == user.organ.orgcode) {
//                    $ips.error('子单不可进行拆单操作');
//                    return false;
//                }
//                if (stepCount == 3) {
//                    $ips.error('主单已被全程转包或生成车次，无法拆单');
//                    return false;
//                }
//
//                //渲染货品
//                if (result.parent.goods.length != 0) {
//                    var rea = '',
//                        totalNum = 0,
//                        totalWeight = 0,
//                        totalVolume = 0,
//                        unit = '';
//                    $.each(result.parent['goods'], function (key, value) {
//                        rea += '<tr><td>' + value.goodname + '</td><td>' + value.unit + '</td><td>' + value.number + '</td><td>' + value.weight + '</td><td>' + value.volume + '</td></tr>';
//                        unit = value.unit;
//                        totalNum += value.number;
//                        totalWeight += value.weight;
//                        totalVolume += value.volume;
//                    });
//                    $('#unit').text(unit);
//                    $('#goodsNum').text(totalNum);
//                    $('#goodsWeight').text(totalWeight);
//                    $('#goodsVolume').text(totalVolume);
//                    var goodsDetail = '<table width="100%" >' +
//                        '<thead><tr><th>货品名称</th><th>单位</th><th>货品数量</th><th>货品重量</th><th>货品体积</th></tr></thead>' +
//                        '<tbody>' + rea + '</tbody>' +
//                        '</table>';
//                    $("#goodsTable").html(goodsDetail);
//                }
//                if (result.children.length != 0) {
//                    renderChildList(result.children, stepInfo);
//                }
                $('#splitOrderModal').modal('show');
            } else {
                $ips.error("获取货品信息失败")
            }
        }
    })
    //创建子单按钮
    $('#btnAaddChildOrder').on('click', function () {
        var allowStep = [];
//        for (var i = 0, maxLen = StepConfig.length; i < maxLen; i++) {
//            if (forbiddenStep[StepConfig[i]])continue;
//            allowStep.push(StepConfig[i]);
//        }
        var stepInfo = []
        var result = $ips.load('order', 'createSuborder', {'parentOrderid': CheckParentId, 'step': allowStep[0]});
        if (result) {
            $ips.succeed('子单创建成功');
            stepInfo[result.id] = allowStep[0];
            GoodstepInfo[result.id] = allowStep[0];
            renderChildList([result], stepInfo);
        }
    })
    //删除子单按钮
    $('#btnDelChildOrder').on('click', function () {
        var suborderids = getRowIds('childOrderList', 'childOrder');
        if (suborderids.length < 1) {
            $ips.error("请选择需要删除的子订单");
            return;
        }
        $ips.load('order', 'deleteSuborder', {
            'parentOrderid': CheckParentId,
            'suborderids': suborderids
        }, function (result) {
            if (result.fail.length > 0) {
                var rea = '';
                $.each(result.fail, function (key, value) {
                    rea += '<tr><td>' + value.orderno + '</td><td>' + value.errorMsg + '</td></tr>';

                });
                var title = '删除成功<span class="txt-color-green padding-5">' + result.succeed.length + '</span>条，失败<span class="txt-color-red padding-5">' + result.fail.length + '</span>条';
                var reason = '<table width="100%" >' +
                    '<thead id="reason"><tr><th>失败订单号</th><th>失败原因</th></tr></thead>' +
                    '<tbody>' + rea + '</tbody>' +
                    '</table>';
                $("#fModalLabel").html(title);
                $("#freason").html(reason);

                $('#fModal').modal();
            }
            if (result.succeed.length > 0) {
                $ips.succeed('子单删除成功!');
                for (var i = 0, maxLen = result.succeed.length; i < maxLen; i++) {
                    $('#OrderItem' + result.succeed[i].orderid).remove();
                    $('#collapse' + result.succeed[i].orderid).remove();
                }
            }
        })
    })
    //弹框关闭，刷新列表
    $('#splitClose').on('click', function () {
        $('#tblMain').grid('fnDraw');
    })

    function renderChildList(childOrders, stepInfo) {

        var reg4Order = new RegExp('#id#', 'g');
        for (var i = 0, maxLen = childOrders.length; i < maxLen; i++) {
            var orderItem = childOrders[i],
                id = orderItem.id,
                childOrderHtml = $('#childOrderTmp').html().replace(reg4Order, id);
            $('#childOrderList').append(childOrderHtml);
            $('#splitType_' + id).val(stepInfo[id]);//子单的阶段
            GoodstepInfo[id] = stepInfo[id];
            $('#orderno_' + id).text(orderItem.orderno);
            var reg4Good = new RegExp('#gid#', 'g');
            addGoodItem(id, orderItem.goods);

            //增加货品条目按钮
            $('#btnAddGood_' + id).click(function () {
                var currentid = $(this).attr('id').split('_')[1];
                addGoodItem(currentid, null);
            })
            //删除货品条目按钮
            $('#btnDelGood_' + id).click(function () {
                var currentid = $(this).attr('id').split('_')[1],
                    goodids = getRowIds('goodsList_' + currentid, 'goodsList');
                if (goodids.length < 1) {
                    $ips.error("请选择需要删除的货品");
                    return;
                }
                delGoodItem(currentid, goodids);
            })
            //货品确认按钮
            $('#btnGoodsSure_' + id).click(function () {

                var currentid = $(this).attr('id').split('_')[1],
                    goodsList = [],
                    goodsListObj = $('#goodsList_' + currentid).find('[name="goodsItem"]');
                for (var i = 0, maxlen = goodsListObj.length; i < maxlen; i++) {
                    var goodItem = $(goodsListObj[i]),
                        zeroCount = 0,
                        goodInfo = {};
                    for (var m = 0, max = GoodInfo.length; m < max; m++) {
                        var goodInfoItem = goodItem.find("input[name='" + GoodInfo[m] + "']").val();
                        goodInfo[GoodInfo[m]] = goodInfoItem;
                        if (goodInfoItem == '')zeroCount++;
                    }
                    if (zeroCount == GoodInfo.length)continue;
                    goodsList.push(goodInfo);
                    //验证数字是否合法(缺)
                    var reg = new RegExp("^[0-9]*$");
                    if (!reg.test(goodsList[0].number) || !reg.test(goodsList[0].volume) || !reg.test(goodsList[0].weight)) {
                        $ips.error('数量、体积、重量请输入数字');
                        return;
                    }

                }
                var stepType = $('#splitType_' + currentid).val();
                if (stepType == '') {
                    $ips.error('请选择该子单的阶段');
                    return false;
                }
                if (forbiddenStep[stepType]) {
                    $ips.error('该阶段主单已被转包或生成车次，无法添加该阶段子单');
                    return false;
                }
                //if(goodsList.length==0)return;
                $ips.load('order', 'editSuborder', {
                    'stepType': stepType,
                    'subOrderid': currentid,
                    'parentid': CheckParentId,
                    'goodsList': goodsList
                }, function (result) {
                    if (result == 'succeed') {
                        GoodstepInfo[currentid] = stepType;
                        $ips.succeed('货品更新成功')
                    } else {
                        $ips.error('货品更新失败')
                    }
                })
            })

            //货品取消按钮
            $('#btnGoodsBack_' + id).click(function () {
                var currentid = $(this).attr('id').split('_')[1];
                var childOrderInfo = $ips.load('order', 'getDetail', {'orderId': currentid});
                $('#goodsList_' + currentid).find('[name="goodsItem"]').remove();
                var goods = childOrderInfo ? childOrderInfo.goods : null;
                addGoodItem(currentid, goods);
                $('#splitType_' + currentid).val(GoodstepInfo[currentid]);
            })

        }

        function addGoodItem(orderid, goods) {
            var goodsLen = goods == null ? 0 : goods.length;
            if (goodsLen > 0) {
                for (var n = 0; n < goodsLen; n++) {
                    var goodsItem = goods[n],
                        goodsListHtml = $('#goodsTmp').html().replace(reg4Order, orderid).replace(reg4Good, n);
                    $('#goodsList_' + orderid).append(goodsListHtml);
                    for (var i = 0, maxlen = GoodInfo.length; i < maxlen; i++) {
                        $('#' + GoodInfo[i] + '_' + orderid + '_' + n).val(goodsItem[GoodInfo[i]]);
                    }
                }
            } else {
                goodsListHtml = $('#goodsTmp').html().replace(reg4Order, orderid).replace(reg4Good, uuid(4));
                $('#goodsList_' + orderid).append(goodsListHtml);
            }
        }

        function delGoodItem(orderid, goodids) {
            if (orderid && goodids) {
                for (var i = 0, maxLen = i.length; i < goodids.length; i++) {
                    $('#goodsList_' + orderid).children('#' + goodids[i]).remove();
                }
            }
        }
    }
});



