//编辑
//var parms = $ips.getUrlParams();
var carInfo = [];
var classlinetype = [];
var updateid;

$(function(){
	if($parm){
		updateid = $parm.taskid;
		$("#account").val($parm.account);
		$("#password").val($parm.password);
//		$("#parentCard").val($parm.parentCard);
//		$("#subCardList").val($parm.subCardList);
		$("#cardType").val($parm.cardType);
		$("#taskType").val($parm.taskType);
		$("#jobnum").val($parm.jobnum);
		$("#level").val($parm.level);
		$("#params").val($parm.params);
		$("#taskid").val($parm.taskid);
		$parm = false;
	}
	
	// 保存
	$("#btnSubmit").click(function(){
		return classlinepriceSave(false);
	});
	
	// 保存并新建
	$("#btnSubmitNew").click(function(){
		return classlinepriceSave(true);
	});
})

 
// Registration validation script
function runFormValidation() {
	var $checkoutForm = $('#frmInfo').validate({
		// Rules for form validation
		rules : {
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
		return classlinepriceSave(false);
	});
	
	// 保存并新建
	$("#btnSubmitNew").click(function(){
		return classlinepriceSave(true);
	});
}
	
function classlinepriceSave(newed) {
	//if(!$('#frmInfo').validate().form()) {
	//    return false;
	//}
//	if($('#classlinename').val() == '') {
//		$ips.error('回调地址名称必填!');
//		return '';
//	}
//
//	if($('#classlineid').val() == '') {
//		$ips.error('回调地址必填!');
//		return '';
//	}

	var params = $("#frmInfo").serializeArray();
	console.log(params);
//	if(updateid){
//	}
	var jsonParams = '{';
		$.each(params, function(i, item) {
            jsonParams += '"'+item.name+'":"'+item.value+'",'
        });
		jsonParams += '"id":'+ updateid +'}';
	console.log(jsonParams);
		
	$ips.load("fuelCard/save", jsonParams, "", "json", function(result){
		if(result) {
			console.log(result);
			$ips.succeed("保存成功。");
			// $('#areaorgcode').select2('val', '');
			// $('#classlineid').select2('val', '');
			// $('#carnum').select2('val', '');
			if (newed) {
//				isupdate = updateid = 0;
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
		} else if(result.code == 2) {
			$ips.error(result.message );
		}
		else
			$ips.showError(result.code, result.message);
	});
	return false;
}

// PAGE RELATED SCRIPTS
/*$ips.include(["js/plugin/jquery-form/jquery-form.min.js"],
		   runFormValidation
);*/

//验证数字和浮点数
function checkFloat(number) {
	var reg = /^\d+(\.\d+)?$/g;
	if (reg.test(number)) {
		return number;
	}else {
		return 0;
	}
}

/**
 判断输入框中输入的日期格式为yyyy-mm-dd和正确的日期
*/
function IsDate(str) {
	var reg = /^(\d+)-(\d{1,2})-(\d{1,2})$/; 
	    var r = str.match(reg); 
	    if(r==null)return false; 
	    r[2]=r[2]-1; 
	    var d= new Date(r[1], r[2],r[3]); 
	    if(d.getFullYear()!=r[1])return false; 
	    if(d.getMonth()!=r[2])return false; 
	    if(d.getDate()!=r[3])return false; 
	    return true;
 }

