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


//function edit(){
//	if(getRowIds() && getRowIds().split(",").length == 1){
//		$parm=parmData[getRowIds()];
//		return true;
//	}else{
//		$ips.error("请选择创建容器的镜像");
//		return false;
//	}
//}

function vehicleModelDelete(id) {
    $ips.confirm("您确定要删除这条记录吗?",function(btn) {
        if (btn == "确定") {
            $ips.load("vehicleModel", "deleteById", "ids=" + id, function(result){
                if(result > 0) {
            		 $ips.succeed("删除成功。");
            		 $('#tblMain').grid("fnDraw");
            	 } else {
            		 $ips.error("删除失败！" + result);
            	 }
            });
		}
    });
}

$("#deletebtn").click(function(){
	
	var ids = getRowIds(true);
//	if(ids.length != 1) {
//		ids.length>1?$ips.error("只能删除一条！"):$ips.error("未选择记录！");
//        return;
//    }
	
    $ips.confirm("您确定要删除选中的记录吗?",function(btn) {
        if (btn == "确定") {
            $ips.load("vehicleModel", "deleteById", "ids=" + ids, function(result){
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
					   		<a href="#tms/basic/vehicle_model_edit.html?id='+ data +'" data-button-resource="E28F31186ECDC04FCCBAE9641AF17B33">编辑</a>\
					   	</li>\
	                   <li class="divider"></li>\
	                   <li>\
	                   		<a href="javascript:void(0);" onclick="vehicleModelDelete(\'' + data.id + '\')" data-button-resource="21E96E9F4B5C1F5522229FB71DBA9A68">删除</a>\
	                   </li>\
	                </ul>\
	            </div>';
				}
            },
                {sTitle: "车型名称", sName: "name"},
                {sTitle: "额度重量", sName: "weight"},
                {sTitle: "额度体积", sName: "volum"},
                {sTitle: "承运商id", sName: "wlcompany"},
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
			$ips.gridLoadData(sSource, aoData, fnCallback, "vehicleModel", "page", searchParams, function(data) {
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

