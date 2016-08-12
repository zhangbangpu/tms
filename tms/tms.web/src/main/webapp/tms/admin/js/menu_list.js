//loadScript("/js/hui/jquery.hui.tree.js",function() {
//	$('#resource-list').tree({
//		type : 'select',
//		method : 'index',
//		selectid : 'orgvalue',
//		params : {
//			'dataType' : 'resource',
//			'subsystem' : '89F2494272BD2B44210229FBB393572A' // 车辆管理
//		},
//		callback : function(result) {
//			console.log(result);
//		}
//	});
//});

// 搜索按钮
$("#btnSearch").click(function() {
	
	$('#tblMain').grid("fnPageChange", "first");
});

function genSearchParams()
{
	var searchParams = $("#frmSearch").serializeArray();
	
	var time = $("#createtime").val();
	if (time) {
		var times = time.split(" - ");
		searchParams.push({name: "createtimeGe", value: times[0]}); 
		searchParams.push({name: "createtimeLt", value: times[1]}); 
	}
//	if ($('#subsystem').select2('val') != '') {
//		searchParams.push({name: "subsystem", value: $('#subsystem').select2('val')}); 
//	}
//
//	if ($('#type').select2('val') != '') {
//		searchParams.push({name: "type", value: $('#type').select2('val')}); 
//	}
	
	var name = $("#name").val();
	if (name) {
		searchParams.push({name: "name", value: name}); 
	}
	return searchParams;
}

$("#deleteResource").click(function(){
	var ids = new Array();
	ids = getRowIds();
	console.log(ids);
	if(ids.length>0){
		$ips.confirm("您确定要删除这条记录吗?",function(btn) {
		    if (btn == "确定") {
				$ips.load("sysMenu", "delMenu", {'ids':ids}, function(data) {
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

function resourceEdit(id) {
	$ips.locate("sysMenu", "updateMenu", "id="+id);
}

function resourceDelete(id) {
    $ips.confirm("您确定要删除这条记录吗?",function(btn) {
        if (btn == "确定") {
            $ips.load("sysMenu", "delMenu", "ids=" + id, function(result){
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
// 资源类型补全
//$("#type").select2({
//    placeholder: "请选择类型",
//    selectOnBlur:true,
//    allowClear: true,
//	data: (function () {
//		var data = $ips.load('resource','types',{});
//		var item=[];
//		if (data == null) {
//			item.push({id: '', text: '没有可选择项', disabled: true});
//		}
//		$.each(data, function (key, it) {
//			item.push({id : key, text : it});
//		});
//		return item;
//	})()
//});

//$("#subsystem").select2({
//    placeholder: "请选择子系统",
//    selectOnBlur:true,
//    allowClear: true,
//	data: (function () {
//		var data = $ips.load('subsystem','codeList',{});
//		var item=[];
//		if (data == null) {
//			item.push({id: '', text: '没有可选择项', disabled: true});
//		}
//		$.each(data, function (key, it) {
//			item.push({id : key, text : it});
//		});
//		return item;
//	})()
//})
// 表格控件
loadScript('js/hui/jquery.hui.grid.js', function () {
	$('#tblMain').grid({
		aoColumns : [
           	{sTitle: '<label class="checkbox"><input type="checkbox" name="checkbox style-0" class="checkbox style-0 checkAll" id="checkAll"><span></span></label>', sName: "idCheckbox","bSortable": false},
           	{sTitle: "操作", sName: "idAction", sWidth: "25px", sClass: "center", bSortable: false},
			{sTitle: "ID", sName: "id"},
//			{sTitle: "所属子系统", sName: "subsystem"},
			{sTitle: "名称", sName: "name"},
			{sTitle: "排序", sName: "sotid"},
//			{sTitle: "资源类型", sName: "type"},
			{sTitle: "连接地址", sName: "requesturl"},
			{sTitle: "打开窗口", sName: "target"}
		],
		"fnServerData" : function(sSource, aoData, fnCallback) {
			var searchParams = genSearchParams();
			$ips.gridLoadData(sSource, aoData, fnCallback, "sysMenu", "page", searchParams, function(data) {
				$.each(data.result, function(i, item) {

					item.idCheckbox = '<label class="checkbox"><input id="' + item.id + '" type="checkbox" name="checkbox-inline" value="'+item.id+'" class="checkbox style-0"><span></span></label>';
					item.idAction = '<div class="btn-group"><button data-toggle="dropdown" class="btn btn-default btn-xs dropdown-toggle">' +
							'<i class="fa fa-pencil fa-fw"></i>' +
							'<i class="fa fa-caret-down"></i>' +
						'</button>' +
		                '<ul class="dropdown-menu">' +
						   '<li><a href="javascript:void(0);" data-button-resource="B42D5811D906814E360904EDB360A467" onclick="resourceEdit(\'' + item.id + '\')">编辑</a></li>' +
		                   '<li class="divider"></li>' +
		                   '<li><a href="javascript:void(0);"  data-button-resource="EF5F3071BA06469A995BAF5AFAF36CBD" onclick="resourceDelete(\'' + item.id + '\')">删除</a></li>' +
		                '</ul></div>';
	                item.orderby = '<a class="orderby">' + item.orderby + '</a>'
	                item.type = item.type == 'menu' ? '菜单' : '按钮';
	                item.target = item.target == '_self' ? '本页' : '新页';
//	                item.subsystem = item.subsystemName;
				});
			});
		},
		fnDrawCallback : function (oSettings) {
			loadScript("js/plugin/x-editable/jquery.mockjax.min.js");
			loadScript("js/plugin/x-editable/x-editable.min.js", function () {
				$.fn.editable.defaults.mode = 'popup';
				$.each($('.orderby'), function () {
					var id = $(this).closest('tr').find('input[type="checkbox"]').attr('id');
					$(this).editable({
						emptytext: 0,
				        defaultValue: 0,
				        type: 'text',
				        pk : id,
				        url: '/inside.php?m=resource&f=changeorderby&t=json',
				        title: '设置别名',
				        ajaxOptions: {
				            type: 'post',
				            dataType: 'json'
				        },
				        success : function (response, newValue) {
				            if (response.code > 0) {
				            	$ips.error(response.message);
				            	return false;
				            }
				        }
					});
				});
				
			});
		}
	});
});

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
