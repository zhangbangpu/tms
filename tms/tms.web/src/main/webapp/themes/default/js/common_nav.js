$(function() {
	 var data = eval("[{'id':1,'parentId':0,'name':'Dashboard','resUrl':'index.html','clazz':'menu-item-parent','type':0,'title':'Dashboard','img':'fa fa-lg fa-fw fa-home'}," +
	 "{'id':2,'parentId':0,'name':'Inbox','resUrl':'inbox.html','clazz':'menu-item-parent','type':0,'title':'','img':'fa fa-lg fa-fw fa-inbox'}," +
	 "{'id':3,'parentId':0,'name':'Graphs','resUrl':'#','clazz':'menu-item-parent','type':0,'title':'','img':'fa fa-lg fa-fw fa-bar-chart-o'}," +
	 "{'id':4,'parentId':0,'name':'Tables','resUrl':'#','clazz':'menu-item-parent','type':0,'title':'','img':'fa fa-lg fa-fw fa-table'}," +
	 "{'id':6,'parentId':3, 'name':'Flot Chart','resUrl':'flot.html','clazz':'','type':1,'title':'','img':''}," +
	 "{'id':7,'parentId':3, 'name':'Morris Charts','resUrl':'morris.html','clazz':'','type':1,'title':'','img':''}," +
	 "{'id':8,'parentId':6, 'name':'Inline Charts','resUrl':'inline-charts.html','clazz':'','type':1,'title':'','img':''},"	 +
	 "{'id':9,'parentId':4, 'name':'Normal Tables','resUrl':'table.html','clazz':'menu-item-parent','type':1,'title':'','img':'fa fa-lg fa-fw fa-table'}," +
	 "{'id':10,'parentId':4, 'name':'Data Tables','resUrl':'datatables2.html','clazz':'menu-item-parent','type':1,'title':'','img':'fa fa-lg fa-fw fa-table'}," +
	 "{'id':10,'parentId':4, 'name':'容器','resUrl':'/container/index.html','clazz':'menu-item-parent','type':1,'title':'','img':'fa fa-lg fa-fw fa-table','module':'container','method':'index'}," +
	 "{'id':5,'parentId':0, 'name':'Forms','resUrl':'#','clazz':'menu-item-parent','type':0,'title':'','img':'fa fa-lg fa-fw fa-pencil-square-o'}]");

//
//	$.getJSON("resource/menuLists", function(data) {
//        //var $menu = new Menu();
//        //$menu.loadMenu("/data.txt");
//
//		var menu = resourceMenu(data);
//		$("nav").html(ulLi(menu));
//		initTree();
//		addEvent();
//	});
//	$ips.load("http://localhost:8080/rest/resource/menuLists",null,null,function(data){
//	$.getJSON("http://localhost:8080/resource/menuLists", function(data) {
        //var $menu = new Menu();
        //$menu.loadMenu("/data.txt");

	$ips.load("resource/menuLists",null,null,function(data){
		var menu = resourceMenu(data);
		$("nav").html(ulLi(menu));
		initTree();
	});

	
	/*$ips.load("resource/menuLists",null,null,function(data){
		alert(data);
		var menu = resourceMenu(data.data);
		$("nav").html(ulLi(menu));
		initTree();
		addEvent();
	}, true);*/

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
