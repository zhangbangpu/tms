var userOrgid = null;

// 搜索按钮
$("#btnSearch").click(function() {
	
	$('#tblMain').grid("fnPageChange", "first");
});
function genSearchParams()
{
	var searchParams = $("#frmSearch").serializeArray();
	return searchParams;
}

$("#deleteOrg").click(function(){
	var ids = new Array();
	ids = getRowIds();

	if(ids.length>0){
		$ips.confirm("您确定要删除这条记录吗? （将删除该机构所有下属机构、角色和用户，请谨慎操作！）",function(btn) {
		    if (btn == "确定") {
				$ips.load("sysDept", "delDept", {'ids':ids}, function(data) {
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



// 表格控件
loadScript('js/hui/jquery.hui.grid.js', function () {
	$('#tblMain').grid({
		"aoColumns" : [
	       	{sTitle: '<label class="checkbox"><input type="checkbox" name="checkbox style-0" class="checkbox style-0 checkAll" id="checkAll"><span></span></label>', sName: "idCheckbox","bSortable": false},
	       	{sTitle: "操作", sName: "idAction", sWidth: "25px", sClass: "center", bSortable: false},
			{sTitle: "机构名称", sName: "name"},
			{sTitle: "机构编码", sName: "deptid"},
			{sTitle: "自定义机构编号", sName: "customerid"},
			{sTitle: "联系人", sName: "contact"},
			{sTitle: "地址", sName: "address"},
			{sTitle: "电话", sName: "tel"},
			{sTitle: "备注", sName: "remark"}
		],
		"fnServerData" : function(sSource, aoData, fnCallback) {
			var searchParams = genSearchParams();
			$ips.gridLoadData(sSource, aoData, fnCallback, "sysDept", "page", searchParams, function(data) {
				userOrgid = data.userOrgid;
				$.each(data.result, function(i, item) {
					item.idCheckbox = '<label class="checkbox"><input id="' + item.id + '" type="checkbox" name="checkbox-inline" value="'+item.id+'" class="checkbox style-0"><span></span></label>';
					item.idAction = '<div class="btn-group"><button data-toggle="dropdown" class="btn btn-default btn-xs dropdown-toggle">' +
										'<i class="fa fa-pencil fa-fw"></i>' +
										'<i class="fa fa-caret-down"></i>' +
									'</button>' +
					                '<ul class="dropdown-menu">' 
//									+ '<li><a href="javascript:void(0);" data-button-resource="BCD01F0FB983E7BEBC8EFBE9073C327A" onclick="orgEdit(\'' + item.id + '\')">编辑</a></li>'
					                ;

					//判断不允许删除自己机构
					if(userOrgid != item.id){
						item.idAction += 
//							'<li class="divider"></li>' +
					                     '<li><a href="javascript:void(0);"  data-button-resource="169EC2F61F4260BB45F098004E86B3BA" onclick="orgDelete(\'' + item.id + '\')">删除</a></li>';
					}
					item.idAction += '</ul></div>';
				});
			});
		}
	});
});


function orgEdit(id) {
	$ips.locate("sysDept", "updateDept", "id="+id);
}

function orgDelete(id) {
    $ips.confirm("您确定要删除这条记录吗?（将删除该机构所有下属机构、角色和用户，请谨慎操作！）",function(btn) {
        if (btn == "确定") {
            $ips.load("sysDept", "delDept", "ids=" + id, function(result){
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
