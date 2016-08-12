// 搜索按钮
$("#btnSearch").click(function() {
	
	$('#tblMain').grid("fnPageChange", "first");
});
function genSearchParams()
{
	var searchParams = $("#frmSearch").serializeArray();
	return searchParams;
}

$("#deleteRole").click(function(){
	var ids = new Array();
	ids = getRowIds();
	console.log(ids);
	if(ids.length>0){
		$ips.confirm("您确定要删除这条记录吗?",function(btn) {
		    if (btn == "确定") {
				$ips.load("sysRole", "delRole", {'ids':ids}, function(data) {
					if(data>0){
						$.smallBox({
							title : "提示",
							content : "<i class='fa fa-clock-o'></i> <i>删除成功</i>",
							color : "#296191",
							iconSmall : "fa fa-thumbs-up bounce animated",
							timeout : 4000
						});
						$('#tblMain').grid("fnDraw");
					}
				});
		    }
		});
	}else{
		$.smallBox({
			title : "提示",
			content : "<i class='fa fa-clock-o'></i> <i>请选择删除项</i>",
			color : "#296191",
			iconSmall : "fa fa-thumbs-up bounce animated",
			timeout : 4000
		});
	}
	
	return false;//防止点击提示框，跳转页面
});

//$("#orgcode").select2({
//    placeholder: '请选择机构',
//    minimumInputLength: 1,  
//    multiple:false,
//    allowClear : true,
//    //数据加载
//    query: function (query){
//        $ips.load('sysDept','getOrgByName',{keyword:query.term, isorgroot : 1},function(e){
//            var _pre_data = [];
//            $.each(e,function(k,v){
//                _pre_data.push({id:v.orgcode,text:v.name});
//            });
//            var data = {results: _pre_data};                 
//            query.callback(data);
//        });         
//    }
//}); 

// 表格控件
loadScript('js/hui/jquery.hui.grid.js', function () {
	$('#tblMain').grid({
		"aoColumns" : [
           	{sTitle: '<label class="no-margin"><input type="checkbox" name="checkbox style-0 " class="checkbox style-0 checkAll" id="checkAll"><span></span></label>', sName: "idCheckbox", sWidth: "20px", sClass: "center", bSortable: false},
           	{sTitle: "操作", sName: "idAction", sWidth: "25px", sClass: "center", bSortable: false},
//			{sTitle: "所属机构", sName: "orgname"},
			{sTitle: "角色名称", sName: "name"},
			{sTitle: "创建时间", sName: "createtime"},
			{sTitle: "更新时间", sName: "updatetime"}
		],
		"fnServerData" : function(sSource, aoData, fnCallback) {
			var searchParams = genSearchParams();
			$ips.gridLoadData(sSource, aoData, fnCallback, "sysRole", "queRoleByCtnPgBn", searchParams, function(data) {
				$.each(data.result, function(i, item) {
					item.idCheckbox = '<label class="checkbox"><input id="' + item.id + '" type="checkbox" name="checkbox-inline" value="'+item.id+'" class="checkbox style-0"><span></span></label>';
					item.idAction = '<div class="btn-group"><button data-toggle="dropdown" class="btn btn-default btn-xs dropdown-toggle">' +
										'<i class="fa fa-pencil fa-fw"></i>' +
										'<i class="fa fa-caret-down"></i>' +
									'</button>' +
					                '<ul class="dropdown-menu">' +
									   '<li><a href="javascript:void(0);" data-button-resource="0933F41DC2D4608D61631DE826689B02" onclick="roleEdit(\'' + item.id + '\')">编辑</a></li>' +
					                   '<li class="divider"></li>' +
					                   '<li><a href="javascript:void(0);" data-button-resource="F76F35A9900A047358E5484DC66A1D49" onclick="roleDelete(\'' + item.id + '\')">删除</a></li>' +
					                '</ul></div>';
				});
			});
		}
	});
});


function roleEdit(id) {
	$ips.locate("sysRole", "updateRole", "id="+id);
}

function roleDelete(id) {
    $ips.confirm("您确定要删除这条记录吗?",function(btn) {
        if (btn == "确定") {
            $ips.load("sysRole", "delRole", "ids=" + id, function(result){
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

//获取选中的id
function getRowIds(){
	var id = '' ;
	$('#tblMain input:checkbox[name="checkbox-inline"]:checked').each(function(){ 
		if(id==""){
			id += $(this).val();
		}else{
			id += ',' + $(this).val();
		}
        
    });
//	if(id==""){
//		return [];
//	}else{
//		return id.split(",");
//	}
	return id;
}
