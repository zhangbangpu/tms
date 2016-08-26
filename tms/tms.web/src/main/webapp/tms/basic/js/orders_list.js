//更多搜索条件按钮!
var searchParams = '';//初始化数据

// 搜索按钮
$("#s_btn1").click(function() {
	$('#tblMain').grid("fnPageChange", "first");
});

function genSearchParams()
{
	searchParams = $("#frmSearch").serializeArray();
	return searchParams;
}

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

var waybillRow = $('#generateWaybillbtn').closest('div');
//function edit(){
//	if(getRowIds() && getRowIds().split(",").length == 1){
//		$parm=parmData[getRowIds()];
//		return true;
//	}else{
//		$ips.error("请选择创建容器的镜像");
//		return false;
//	}
//}
$("#generateWaybill").bind('click', function() {
	var ids = getRowIds(false);
	if ($('#wlcompany').val() == '' || $('#wlcompany').val() == '') {
		$ips.error('承运商不能为空');
		return false;
	}
	if ($('#vehiclemodel').val() == '' || $('#vehiclemodel').val() == '') {
		$ips.error('车型不能为空!');
		return false;
	}

	$ips.confirm("您确定要生成运单吗?",function(btn) {
        if (btn == "确定") {
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
				$('#myModal .modal-header button').trigger('click');
			})
        }
    });
	
	return false;
});

waybillRow.empty();
$('<div class="btn-group"><a class="btn btn-default" data-button-resource="048625502851586667FA938190987180">生成运单</a></div>').bind('click', function() {
	var ids = getRowIds(false);
	var orders = $ips.load("orders", "queryStatusById", {id : ids});
    
	if (orders.length > 0) {
		$ips.error("订单号的状态不正确，生成运单失败！");
		return false;
	}
	$(this).attr({'data-toggle' : "modal",'data-target' : "#myModal"})
	$('#wlcompany').val('');
	$('#vehiclemodel').val('');
}).appendTo(waybillRow);

$("#exportbtn").click(function(){
	var ids = getRowIds(false);
    $ips.confirm("您确定要导出选中的记录吗?",function(btn) {
        if (btn == "确定") {
            $ips.load("orders", "export", "ids=" + ids, function(result){
                if(result.code == 0) {
            		 $ips.succeed("导出成功。");
            		 $('#tblMain').grid("fnDraw");
            	 } else {
            		 $ips.error("导出失败！" + result);
            	 }
            });
		}
    });
});

$("#deletebtn").click(function(){
	
	var ids = getRowIds(true);
	if(ids.length != 1) {
		ids.length>1?$ips.error("只能删除一条！"):$ips.error("未选择记录！");
		return;
	}
	
	$ips.confirm("您确定要删除选中的记录吗?",function(btn) {
		if (btn == "确定") {
			$ips.load("orders", "deleteById", "ids=" + ids, function(result){
				console.log(result.code == 0);
				if(result.code == 0) {
					$ips.succeed("删除成功。");
					$('#tblMain').grid("fnDraw");
				} else {
					$ips.error("删除失败！" + result);
				}
			});
		}
	});
});


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
//                	console.log(data);
                	return '<div class="btn-group">\
					<button data-toggle="dropdown" class="btn btn-default btn-xs dropdown-toggle">\
						<i class="fa fa-pencil"></i>\
						<i class="fa fa-caret-down"></i>\
					</button>\
	                <ul class="dropdown-menu">\
	                   <li>\
	                   		<a href="javascript:void(0);" onclick="ordersDelete(\'' + data + '\')" data-button-resource="21E96E9F4B5C1F5522229FB71DBA9A68">删除</a>\
	                   </li>\
	                </ul>\
	            </div>';
				}
            },
                {sTitle: "订单号", sName: "code"},
//                {sTitle: "订单类型", sName: "type"},
				{
					sTitle : "订单类型",
					sName : "state",
					mRender : function(data, type, full) {
						if (data == 0) {
							return "初始";
						} else if (data == 1) {
							return "已生成运单";
						} else if (data == 2) {
							return "订单已下发";
						} else if (data == 3) {
							return "订单在途";
						} else
							(data == 4)
						{
							return "已签收";
						}
					}
				},
                {sTitle: "城市", sName: "city"},
                {sTitle: "发货地址", sName: "fhaddress"},
                {sTitle: "收货地址", sName: "shaddress"},
                {sTitle: "收货地址", sName: "shaddress"},
//                {sTitle: "订单状态", sName: "status"},
                {sTitle: "订单状态", sName: "status", 
                	mRender:function(data, type, full){
                		if(data == 0){
                			return "自动";
                		}else if(data == 1){
                			return "手动";
                		}
                	}
                },
                {sTitle: "承运商", sName: "subcontractor"},
                {sTitle: "货品名称", sName: "cpmdName"},
                {sTitle: "货品数量", sName: "amount"},
                {sTitle: "货品重量", sName: "weight"},
                {sTitle: "货品体积", sName: "volume"},
                {sTitle: "异常事件数", sName: "exceptcount"},
			]
		,
		"fnServerData" : function(sSource, aoData, fnCallback) {
			 searchParams = genSearchParams();
			$ips.gridLoadData(sSource, aoData, fnCallback, "orders", "page", searchParams, function(data) {
				console.log(typeof data);
				console.log(data);
				var dataArray = new Array();
				$.each(data.result, function(i, item) {
					dataArray[item.id+""] = item;
				});
				parmData = dataArray;
			});
		}
	});
});

