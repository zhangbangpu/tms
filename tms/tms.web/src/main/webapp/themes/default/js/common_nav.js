$(function() {
//	 var data = eval("[{'id':1,'parentId':0,'name':'Dashboard','resUrl':'index.html','clazz':'menu-item-parent','type':0,'title':'Dashboard','img':'fa fa-lg fa-fw fa-home'}," +
//	 "{'id':2,'parentId':0,'name':'Inbox','resUrl':'inbox.html','clazz':'menu-item-parent','type':0,'title':'','img':'fa fa-lg fa-fw fa-inbox'}," +
//	 "{'id':3,'parentId':0,'name':'Graphs','resUrl':'#','clazz':'menu-item-parent','type':0,'title':'','img':'fa fa-lg fa-fw fa-bar-chart-o'}," +
//	 "{'id':4,'parentId':0,'name':'Tables','resUrl':'#','clazz':'menu-item-parent','type':0,'title':'','img':'fa fa-lg fa-fw fa-table'}," +
//	 "{'id':6,'parentId':3, 'name':'Flot Chart','resUrl':'flot.html','clazz':'','type':1,'title':'','img':''}," +
//	 "{'id':7,'parentId':3, 'name':'Morris Charts','resUrl':'morris.html','clazz':'','type':1,'title':'','img':''}," +
//	 "{'id':8,'parentId':6, 'name':'Inline Charts','resUrl':'inline-charts.html','clazz':'','type':1,'title':'','img':''},"	 +
//	 "{'id':9,'parentId':4, 'name':'Normal Tables','resUrl':'table.html','clazz':'menu-item-parent','type':1,'title':'','img':'fa fa-lg fa-fw fa-table'}," +
//	 "{'id':10,'parentId':4, 'name':'Data Tables','resUrl':'datatables2.html','clazz':'menu-item-parent','type':1,'title':'','img':'fa fa-lg fa-fw fa-table'}," +
//	 "{'id':10,'parentId':4, 'name':'容器','resUrl':'/container/index.html','clazz':'menu-item-parent','type':1,'title':'','img':'fa fa-lg fa-fw fa-table','module':'container','method':'index'}," +
//	 "{'id':5,'parentId':0, 'name':'Forms','resUrl':'#','clazz':'menu-item-parent','type':0,'title':'','img':'fa fa-lg fa-fw fa-pencil-square-o'}]");

//	var data = eval('[{"resUrl":"/ws/querySite","id":2,"parentId":0,"title":"站点管理","name":"站点管理","img":"fa fa-lg fa-fw fa-table","type":"1","clazz":"menu-item-parent"},{"resUrl":"/sysUser/queUserByCtnPgBn?page=1&rows=10&sysUser=","id":3,"parentId":0,"title":"用户管理","name":"用户管理","img":"fa fa-lg fa-fw fa-table","type":"1","clazz":"menu-item-parent"},{"resUrl":"/sysRole/queRoleByCtnPgBn?page=1&rows=10&sysRole=","id":4,"parentId":0,"title":"角色管理","name":"角色管理","img":"fa fa-lg fa-fw fa-table","type":"1","clazz":"menu-item-parent"},{"resUrl":"/sysMenu/queryAllMenu?page=1&rows=10&sysMenu=","id":5,"parentId":0,"title":"菜单管理","name":"菜单管理","img":"fa fa-lg fa-fw fa-table","type":"1","clazz":"menu-item-parent"},{"resUrl":"/sysDept/queryDeptByCondition?page=1&rows=10&sysDept=","id":6,"parentId":0,"title":"机构管理","name":"机构管理","img":"fa fa-lg fa-fw fa-table","type":"1","clazz":"menu-item-parent"}]');
//	 var menu = resourceMenu(data);
//		$("nav").html(ulLi(menu));
	//var param_u = 
	$ips.load("login","loginGetMenuList",{id:1},function(data){
		console.log(data);
		var menu = resourceMenu(data);
		$("nav").html(ulLi(menu));
	});

});

//登陆
$('#logoutButton').click(function() {
    $ips.load('login', 'logout', pararm, function(result) {
    	console.log(result);
    	console.log(typeof result);
    	window.location.href="login.html";
//        if (result.success == true) {
//        } else {
//            $('.padding-top-10:first').animate({right: '10px'},80);
//            $('.padding-top-10:first').animate({right: ''},80);
//            $('.padding-top-10:first').animate({left: '10px'},80);
//            $('.padding-top-10:first').animate({left: ''},80);
//            $('.padding-top-10:first').removeAttr('style');
//            $ips.error(result.message);
//        }
    });
    return false;
});

function Menu() {

	this.loadMenu = function(uri, params, callback) {

		$.ajax({
			url : uri,
			async : true,
			dataType : 'json',
			success : function(data) {
//				alert(data);
			},
			error : function(data) {
				console.log(data);
			}
		});

	}
}

// 将json处理成arrayTree
function resourceMenu(data) {
	// var prant = new Array();
	// 菜单顶级
	var tree = new Array();
	var refer = new Array();
	// 获取顶级
	for (var i = 0; i < data.length; i++) {
		refer[data[i].id] = data[i];
	}

	for (var i = 0; i < data.length; i++) {
		if (data[i].parentId == 0) {
			// 直接将父级数据添加到tree数组中
			tree.push(data[i]);
		} else {
			// 在父级数据中存入子级数据的引用
			if (!refer[data[i].parentId].children) {
				refer[data[i].parentId].children = new Array();
			}
			refer[data[i].parentId].children.push(data[i]);
		}

	}

	return tree;
	// console.log(tree);
}

function ulLi(data, children) {

	var html = children ? '<ul style="display: none;">' : '<ul>';

	for (var i = 0; i < data.length; i++) {
		var value = data[i];
		if (value.type == 0) {
			html += '<li><a href="' + value.resUrl + '" title="' + value.name
					+ '"><i class="' + value.img + '"></i> <span class="'
					+ value.clazz + '">' + value.name + '</span>';
			// if(value.children){
			// html+='<b class="collapse-sign"><em class="fa
			// fa-expand-o"></em></b>';
			// }
			html += '</a>';
		} else {
			html += '<li><a href="' + value.resUrl + '" ';
			if(value.module && value.method){
				html += 'id="'+value.module+','+value.method+'"';
			}
			html += '>' + value.name + '</a>';
		}
		if (value.children) {
			html += ulLi(value.children, true);
		}
		html += '</li>';
	}
	html += '</ul>';
	// console.log(html);
	return html;
}

var indexJs;

function addEvent(){
	$("nav a").click(function(){
//		alert(this.href);
		indexJs = this.href;
		/*if(this.id){
			alert(this.id);
			var id=(this.id+'').split(',');
			$ips.locate(id[0], id[1]);
		}*/
	});
}
