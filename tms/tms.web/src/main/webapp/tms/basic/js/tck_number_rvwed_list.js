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


$("#exportbtn").click(function(){
	
	var ids = getRowIds(false);
	
    $ips.confirm("您确定要导出选中的记录吗?",function(btn) {
        if (btn == "确定") {
            $ips.load("tckNumRvwed", "export", "ids=" + ids, function(result){
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

$("#reviewedbtn").click(function(){
	
	var ids = getRowIds(true);
	if(ids.length != 1) {
		ids.length>1?$ips.error("只能审核一条！"):$ips.error("未选择记录！");
        return;
    }
	
	$ips.confirm("您确定要审核选中的记录吗?",function(btn) {
		if (btn == "确定") {
			$ips.load("tckNumRvwed", "updateTckNumRvwed", {id : parseInt(ids),state : 1} , function(result){
				console.log(result.code == 0);
				if(result.code == 0) {
					$ips.succeed("审核成功。");
					$('#tblMain').grid("fnDraw");
				} else {
					$ips.error("审核失败！" + result);
				}
			});
		}
	});
});

$("#auditDismissedbtn").click(function(){
	
	var ids = getRowIds(true);
	if(ids.length != 1) {
		ids.length>1?$ips.error("只能审核一条！"):$ips.error("未选择记录！");
        return;
    }
	
	$ips.confirm("您确定要审核选中的记录吗?",function(btn) {
		if (btn == "确定") {
			$ips.load("tckNumRvwed", "updateTckNumRvwed", {id : parseInt(ids),state : -1}, function(result){
				console.log(result.code == 0);
				if(result.code == 0) {
					$ips.succeed("审核成功。");
					$('#tblMain').grid("fnDraw");
				} else {
					$ips.error("审核失败！" + result);
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
	            </div>';
				}
            },
                {sTitle: "车次编号", sName: "code"},
                {sTitle: "创建时间", sName: "createtime"},
                {sTitle: "车次类型", sName: "orderfrom"},
                {sTitle: "承运商", sName: "subcontractor"},
                {sTitle: "出发地", sName: "fhaddress"},
                {sTitle: "到达地", sName: "shaddress"},
                {sTitle: "货品总数量", sName: "amount"},
                {sTitle: "货品总重量", sName: "weight"},
                {sTitle: "货品总体积", sName: "volume"},
			]
		,
		"fnServerData" : function(sSource, aoData, fnCallback) {
			 searchParams = genSearchParams();
			$ips.gridLoadData(sSource, aoData, fnCallback, "tckNumRvwed", "page", searchParams, function(data) {
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

