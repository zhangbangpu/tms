//更多搜索条件按钮!
//var searchParams = '';//初始化数据

//var parms = $ips.getUrlParams();
//loadScript("js/plugin/jquery-form/jquery-form.min.js", importFile);


// 搜索按钮
$("#s_btn1").click(function() {
	$('#tblMain').grid("fnPageChange", "first");
});

//搜索条件 回车 触发
$('input').keydown(function(e){
	if(e.keyCode==13){
		$('#tblMain').grid("fnPageChange", "first");
	}
});

function genSearchParams(){
	var searchParams = $("#frmSearch").serializeArray();
	return searchParams;
}

function areaDelete(id) {
    $ips.confirm("您确定要删除这条记录吗?",function(btn) {
        if (btn == "确定") {
            $ips.load("area", "deleteById", "ids=" + id, function(result){
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
	areaDelete(ids);
//    $ips.confirm("您确定要删除选中的记录吗?",function(btn) {
//        if (btn == "确定") {
//            $ips.load("area", "deleteById", "ids=" + ids, function(result){
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


//Registration validation script
function importFile() {
	var $checkoutForm = $('#frmImport').validate({
		rules : {
		},
		messages : {
		},
		errorPlacement : function(error, element) {
			error.insertAfter(element.parent());
		}
	});
	
	var pararm = $("#frmImport").serialize();
	var sitefile = $("#sitefile").val();
	pararm += "&sitefile=#"+sitefile;
    var privacy = 1;
    if($("#privacy").is(':checked')){
        privacy = 0;
    }
    pararm += "&privacy="+privacy;
	$ips.load("site", "importSite", pararm, function(result){
//		$ips.unLockPage();
//		console.log(result);
		if(result) {
			$ips.succeed("保存站点成功。");
		}else {
			$ips.error("保存站点表失败。" + result);
		}
	});
	return false;
}

$("#exportbtn").click(function(){
	var ids = getRowIds(false);
    $ips.confirm("您确定要导出选中的记录吗?",function(btn) {
        if (btn == "确定") {
            $ips.load("site", "export", "ids=" + ids, function(result){
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
					   		<a href="#tms/basic/area_edit.html?id='+ data +'" data-button-resource="E28F31186ECDC04FCCBAE9641AF17B33">编辑</a>\
					   	</li>\
	                   <li class="divider"></li>\
	                   <li>\
	                   		<a href="javascript:void(0);" onclick="areaDelete(\'' + data + '\')" data-button-resource="21E96E9F4B5C1F5522229FB71DBA9A68">删除</a>\
	                   </li>\
	                </ul>\
	            </div>';
				}
            },
                {sTitle: "区域站点编号", sName: "code"},
                {sTitle: "区域站点名称", sName: "name"},
                {sTitle: "组织机构", sName: "deptname"},
                {sTitle: "承运商", sName: "wlcompanyname"},
                /*{sTitle: "站点类型", sName: "types", 
                	mRender:function(data, type, full){
                		if(data == 1){
                			return "加油站点";
                		}else {
                			return "装货点";
                		}
                	}
                },*/
                {sTitle: "更新时间", sName: "updatetime"}
			]
		,
		"fnServerData" : function(sSource, aoData, fnCallback) {
			var searchParams = genSearchParams();
			console.log(searchParams);
			$ips.gridLoadData(sSource, aoData, fnCallback, "area", "page", searchParams, function(data) {
				
				var dataArray = new Array();
				$.each(data.result, function(i, item) {
					dataArray[item.id+""] = item;
				});
//				parmData = dataArray;
			});
		}
	});
});

