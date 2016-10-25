// 搜索按钮
$("#btnSearch").click(function() {
	$('#tblMain').grid("fnPageChange", "first");
});
function genSearchParams()
{
	var searchParams = $("#frmSearch").serializeArray();
//	if(searchParams == ""){
//		searchParams = "{sysUser:}"
//	}
	return searchParams;
}

$("#deleteUser").click(function(){
	var ids = new Array();
	ids = getRowIds();
	if(ids.length>0){
		$ips.confirm("您确定要删除这条记录吗?",function(btn) {
		    if (btn == "确定") {
				$ips.load("sysUser", "delUser", {'ids':ids}, function(data) {
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
//        $ips.load('org','getOrgByName',{keyword:query.term},function(e){
//            var _pre_data = [];
//            $.each(e,function(k,v){
//                _pre_data.push({id:v.orgcode,text:v.name});
//            });
//            var data = {results: _pre_data};                 
//            query.callback(data);
//        });
//    },
//     initSelection: function(element, callback) {
//        console.log($(element).val());
//    },
//}).on("select2-removed", function(e) { console.log(e);})
//.on("select2-selecting", function(f) {$("#orgname").val(f.object.text);$("#orgcode").val(f.object.id);}); 

// 表格控件
loadScript('js/hui/jquery.hui.grid.js', function () {
	$('#tblMain').grid({
		"aoColumns" : [
           	{sTitle: '<label class="no-margin"><input type="checkbox" name="checkbox style-0 " class="checkbox style-0 checkAll" id="checkAll"><span></span></label>', sName: "idCheckbox", sWidth: "20px", sClass: "center", bSortable: false},
           	{sTitle: "操作", sName: "idAction", sWidth: "25px", sClass: "center", bSortable: false},
            {sTitle: "登陆名", sName: "loginname"},
//          {sTitle: "姓名", sName: "realname"},
            {sTitle: "公司名称", sName: "name"},
            {sTitle: "角色", sName: "rolename"},
//			{sTitle: "机构", sName: "deptName"},
			{sTitle: "手机", sName: "phone"},
			{sTitle: "邮箱", sName: "email"},
			{sTitle: "创建时间", sName: "createtime"}
		],
		"fnServerData" : function(sSource, aoData, fnCallback) {
			var searchParams = genSearchParams();
			$ips.gridLoadData(sSource, aoData, fnCallback, "sysUser", "queUserByCtnPgBn", searchParams, function(data) {
				console.log(data);
				$.each(data.result, function(i, item) {
					item.idCheckbox = '<label class="checkbox"><input id="' + item.id + '" type="checkbox" name="checkbox-inline" value="'+item.id+'" class="checkbox style-0"><span></span></label>';
					item.idAction = '<div class="btn-group"><button data-toggle="dropdown" class="btn btn-default btn-xs dropdown-toggle">' +
										'<i class="fa fa-pencil fa-fw"></i>' +
										'<i class="fa fa-caret-down"></i>' +
									'</button>' +
					                '<ul class="dropdown-menu">' +
									   '<li><a href="#tms/admin/user_edit.html?id='+ item.id +'" data-button-resource="3D0D299083B8B9738EA929013B8B7F1C">编辑</a></li>' +
					                   '<li class="divider"></li>' +
					                   '<li><a href="javascript:void(0);" data-button-resource="B8E2AAE664980C89BB68369D36C052F4" onclick="userDelete(\'' + item.id + '\')">删除</a></li>' +
					                '</ul></div>';
					     /*if('1' == item.status ){
					    	 item.statusName = '禁用';
					     }else{
					    	 item.statusName = '可用';
					     }*/
				});
			});
		}
	});
});


// // 表格
// function runDataTables() {
// 	$('#tblMain').grid({
// 		"fnServerData" : function(sSource, aoData, fnCallback) {
// 			$ips.gridLoadData(sSource, aoData, fnCallback, "user", "search", searchParams, function(data) {
// 				$.each(data.result, function(i, item) {
// 					item.idCheckbox = '<label class="checkbox"><input id="chk_' + item.id + '" type="checkbox"  name="checkbox-inline" value="'+item.id+'"><i></i></label>';
// 					item.idAction = '<div class="btn-group"><button data-toggle="dropdown" class="btn btn-default btn-xs dropdown-toggle">' +
// 										'<i class="fa fa-pencil"></i>' +
// 										'<i class="fa fa-caret-down"></i>' +
// 									'</button>' +
// 					                '<ul class="dropdown-menu">' +
// 									   '<li><a href="javascript:void(0);" onclick="userEdit(\'' + item.id + '\')">编辑</a></li>' +
// 					                   '<li class="divider"></li>' +
// 					                   '<li><a href="javascript:void(0);" onclick="userDelete(\'' + item.id + '\')">删除</a></li>' +
// 					                '</ul></div>';
// 					     if('1' == item.status ){
// 					    	 item.statusName = '禁用';
// 					     }else{
// 					    	 item.statusName = '可用';
// 					     }
// 				});
// 			});
// 		},
// 		"aoColumns" : [
// 		               	{sTitle: '<label class="checkbox"><input type="checkbox" class="checkbox style-0"><span></span></label>', sName: "idCheckbox", sWidth: "20px", sClass: "center", bSortable: false},
// 		               	{sTitle: "操作", sName: "idAction", sWidth: "25px", sClass: "center", bSortable: false},
// 						{sTitle: "机构", sName: "orgname"},
// 						{sTitle: "登陆名", sName: "username"},
// 						{sTitle: "绑定ip", sName: "bindip"},
// 						{sTitle: "姓名", sName: "realname"},
// 						{sTitle: "用户手机号", sName: "mobile"},
// 						{sTitle: "用户电子邮箱", sName: "email"},
// 						{sTitle: "创建时间", sName: "createtime"},
// 						{sTitle: "状态", sName: "statusName"},
// 					],
// 		"fnDrawCallback": function( oSettings ) {
// 			        $(oSettings.nTable).find(".btn-group").slice(-2).addClass('dropup'); //列表中下拉菜单下拉
// 			        $(oSettings.nTable).find(".btn-group").slice(0,1).removeClass('dropup');//列表中下拉菜单上
// 			        $(oSettings.nTable).parent(".dt-wrapper").css({
// 			            'overflow': 'auto',
// 			            'min-height': '100px',
// 			            'background-color': '#F9F9F9'
// 				    }); //列表外出现滚动条

// 	              } 
// 	});
// }

//function userEdit(id) {
//	$ips.locate("sysUser", "queryOneById", "id="+id);
//}

function userDelete(id) {
    $ips.confirm("您确定要删除这条记录吗?",function(btn) {
        if (btn == "确定") {
            $ips.load("sysUser", "delUser", "ids=" + id, function(result){
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