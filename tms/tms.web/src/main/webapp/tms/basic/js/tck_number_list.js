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

function tckNumDelete(id) {
    $ips.confirm("您确定要删除这条记录吗?",function(btn) {
        if (btn == "确定") {
            $ips.load("tckNum", "deleteById", "ids=" + id, function(result){
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


//function edit(){
//	if(getRowIds() && getRowIds().split(",").length == 1){
//		$parm=parmData[getRowIds()];
//		return true;
//	}else{
//		$ips.error("请选择创建容器的镜像");
//		return false;
//	}
//}


$("#deletebtn").click(function(){
	
	var ids = getRowIds(true);
//	if(ids.length != 1) {
//		ids.length>1?$ips.error("只能删除一条！"):$ips.error("未选择记录！");
//        return;
//    }
	
    $ips.confirm("您确定要删除选中的记录吗?",function(btn) {
        if (btn == "确定") {
            $ips.load("tckNum", "deleteById", "ids=" + ids, function(result){
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
	                   		<a href="javascript:void(0);" onclick="tckNumDelete(\'' + data + '\')" data-button-resource="21E96E9F4B5C1F5522229FB71DBA9A68">删除</a>\
	                   </li>\
	                </ul>\
	            </div>';
				}
            },
            {sTitle: "车次编号", sName: "code"},
            {sTitle: "创建时间", sName: "createtime"},
//           {sTitle: "车次类型", sName: "orderfrom"},
//            {sTitle: "任务状态", sName: "state"},
            {sTitle: "状态", sName: "state", 
            	mRender:function(data, type, full){
            		if(data == -1){
            			return "审核不通过";
            		}else if(data == 0){
            			return "初始";
            		}else if(data == 1){
            			return "审核通过";
            		}else{
            			return "运单在途";
            		}
            	}
            },
            {sTitle: "承运商", sName: "subcontractor"},
            {sTitle: "出发地", sName: "fhaddress"},
            {sTitle: "到达地", sName: "shaddress"},
            {sTitle: "货品总数量", sName: "amount"},
            {sTitle: "货品总重量", sName: "weight"},
            {sTitle: "货品总体积", sName: "volume"},
            {sTitle: "异常事件数", sName: "exceptcount"},
			]
		,
		"fnServerData" : function(sSource, aoData, fnCallback) {
			 searchParams = genSearchParams();
			$ips.gridLoadData(sSource, aoData, fnCallback, "tckNum", "page", searchParams, function(data) {
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

