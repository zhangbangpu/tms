//if (/^.*?dev\.ucenter\.g7s\.chinawayltd\.com.*?$/.test(window.location.href) == false) {
//	$ips.alert('操作失败，资源添加及编辑请在[dev.g7s.chinawayltd.com]进行，修改完成后通知框架组进行同步！', function () {
//		$ips.locate('sysMenu', 'index');
//	});
	//$ips.lockPage('操作失败，资源添加及编辑请在[dev.g7s.chinawayltd.com]进行，通知框架组进行同步！');
	// setTimeout(function () {
	// 	$ips.locate('resource', 'index');
	// }, 6000);
//}
// 父资源补全
//$("#parentid").select2({
//    placeholder: "请选择父资源",
//    data : []
//})


//var projecttypes = $ips.load('resource', 'getprojects', {});
//if (typeof projecttypes != 'undefined' && projecttypes.length != 0) {
//	$.each(projecttypes, function (index, item) {
//		$('<option></option>').val(item.projecttype).text(item.projecttype).appendTo($('#projecttype'));
//	});
//}
var isupdate = updateid = 0;
var parms = $ips.getUrlParams();
if(typeof parms.id != 'undefined' && parms.id != '') {
	var entity = $ips.load("sysMenu", "queryOneById", "id=" + parms["id"]);
	if (entity) {
//		if (entity.subsystem) {
//			renderParentSelect2(entity.subsystem);
//			$("#pid").select2('val', entity.parentid);
//		}
		$ips.fillFormInput('frmInfo',entity);
		hideRow(entity.type);
		//修改标记
		isupdate = 1;
		//需要更新的Id
		updateid = entity['id'];

	}
}


// Load form valisation dependency 
loadScript("js/plugin/jquery-form/jquery-form.min.js", runFormValidation);
// Registration validation script
function runFormValidation() {
	var $checkoutForm = $('#frmInfo').validate({
		// Rules for form validation
		rules : {
 
//			subsystem:{
//				required : true
//			},
 
			name:{
				required : true
			},
 
			type:{
				required : true
			},
 
			target:{
				required : true
			}
//			,
// 			orderby:{
//				number: true,
//				min: 0
//			},
			
		},
		// Messages for form validation
		messages : {
		},
		// Do not change code below
		errorPlacement : function(error, element) {
			error.insertAfter(element.parent());
		}
	});
	
	// 保存
	$("#btnSubmit").click(function(){
		return resourceSave(false);
	});
	
	// 保存并新建
	$("#btnSubmitNew").click(function(){
		return resourceSave(true);
	});
}
	
function resourceSave(newed) {
	if(!$('#frmInfo').validate().form()) {
        return false;
    }
	
	var pararm = $("#frmInfo").serializeArray();
//	pararm.push({'name' : 'subsystem', 'value' : $('#subsystem').select2('val')});
	pararm.push({'name' : 'type', 'value' : $('#type').select2('val')});
	pararm.push({'name' : 'target', 'value' : $('#target').select2('val')});
	pararm.push({'name' : 'requesturl', 'value' : $('#requesturl').select2('val')});
	pararm.push({'name' : 'img', 'value' : $('#img').select2('val')});
//	pararm.push({'name' : 'pid', 'value' : $('#pid').select2('val')});
	if(isupdate)
		pararm['id'] = updateid;
	$ips.load("sysMenu", "addMenu", pararm, function(result){
		if(result) {
			$ips.succeed("保存成功。");
			if (newed) {
				isupdate = updateid = 0;
				$('#frmInfo')[0].reset();
			} else
				window.history.go(-1);
		} else {
			$ips.error("保存失败。" + result);
		}
	}, function(result) {
		// 验证失败的code号码
		if (result.code == 555) {
			var errors = {};
			$.each(eval('(' + result.message + ')'), function($k, $v) {
				if ($('input#' + $k).size() > 0)
					errors[$k] = $v;
				else
					$ips.error($k + '：' + $v);
			});
			// 显示后端验证失败信息
			validator.showErrors(errors);
		} else
			$ips.showError(result.code, [result.message]);
	});
	return false;
}

// 子系统补全

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

// 资源类型补全
//$("#type").select2({
//    placeholder: "请选择类型",
//    selectOnBlur:true,
//    allowClear: true,
//	data: (function () {
//		var data = $ips.load('sysMenu','getTypes',{});
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
//$("#type").on('select2-selected', function () {
//	hideRow($('#type').select2('val'));
//});
function hideRow(type) {
	var domIds = ['#url-row', '#icon-row', '#target-row'];
	$.each(domIds, function (index, item) {
		if (type == 'button') {
			$(item).hide();
		} else {
			$(item).show();
		}
	});
}
// 打开方式补全
//$("#target").select2({
//    placeholder: "请选择打开方式",
//    selectOnBlur:true,
//    allowClear: true,
//	data: [{id : '_self', text : '本页'},{'id' : '_blank', text : '新页面'}]
//})

//$("#subsystem").on("select2-selected",function () {
//	renderParentSelect2($('#subsystem').select2('val'));
//});
//function renderParentSelect2(subsystem)
//{
//	try {
//		$("#pid").select2('destroy');
//	} catch (e) { }
//	
//	var data = [];
//	var subsystem = subsystem;
//	if (subsystem != '') {
//		var res = $ips.load('sysMenu', 'parentcompletion', {subsystem : subsystem});
//		if (res) {
//			$.each(res, function () {
//				var name = this.name;
//				var pre = '';
//				for (var i = 0; i < this.level; i ++) {
//					pre += '——';
//				}
//				name = pre + name;
//				data.push({'id' : this.id, 'text' : name, 'projecttype' : this.projecttype});
//			});
//		}
//	}
//
//	$("#parentid").select2({
//	    selectOnBlur:true,
//	    allowClear: true,
//	    placeholder: data.length == 0 ? '无可选择的父资源' : "请选择父资源",
//	    data : data
//	});
//	$("#parentid").on('change', function (e) {
//		$.each(data, function () {
//			if (this.id == e.val) {
//				if (this.projecttype != '') {
//					$('#projecttype').val(this.projecttype).prop('disabled', true);
//					$('#addOptionBtn').prop('disabled', true);
//				} else {
//					$('#projecttype').val('').prop('disabled', false);
//					$('#addOptionBtn').prop('disabled', false);
//					if (typeof entity != 'undefined' && entity.projecttype != '') {
//						$('#projecttype').val(entity.projecttype);
//					}
//				}
//				return false;
//			}
//		})
//	});
//	$("#parentid").on('select2-removed', function (e) {
//		$('#projecttype').val('').prop('disabled', false);
//		$('#addOptionBtn').prop('disabled', false);
//		if (typeof entity != 'undefined' && entity.projecttype != '') {
//			$('#projecttype').val(entity.projecttype);
//		}
//	});
//}

//$ips.load('sysMenu', 'getAllMenu', {}, function (data) {
//	var selectDom = $('#relateid');
//	$.each (data, function (index, item) {
//		var group = $('<optgroup></optgroup>').attr('label', item.name);
//
//		if (item.options != undefined) {
//			$.each(item.options, function (ix, it) {
//				var pre = '';
//				for (var i = 0; i < it.level; i ++) {
//					pre += '&nbsp;&nbsp;&nbsp;&nbsp;';
//				}
//				group.append($('<option></option>').attr('value', it.id).html(pre + it.name));
//			});
//		}
//		group.appendTo(selectDom);
//	});
//	if (typeof entity != 'undefined' && !!entity.relateid) {
//		selectDom.val(entity.relateid);
//	}
//});

//$("#addOptionBtn").click(function(e) {
//
//	$.SmartMessageBox({
//		title : "项目定制资源",
//		content : "您可以在这里增加定制项目的名称",
//		buttons : "[取消][增加]",
//		input : "text",
//		placeholder : "请输入项目名称"
//	}, function(ButtonPress, Value) {
//		if (ButtonPress == '增加') {
//			if (Value == '') {
//				$ips.error('未输入项目名称');
//				return false;
//			}
//			$('<option></option>').val(Value).text(Value).appendTo($('#projecttype'));
//			$('#projecttype').val(Value);
//		}
//	});
//
//	e.preventDefault();
//})

//$('#isdisable').bind('change', function () {
//	if ($(this).val() == 1) {
//		$('#isdefault').val(0).prop('disabled', true);
//	} else {
//		$('#isdefault').prop('disabled', false);
//	}
//});
//$('#isdefault').bind('change', function () {
//	if ($(this).val() == 1 && $('#isdisable').val() == 1) {
//		$(this).val('0');
//	}
//});