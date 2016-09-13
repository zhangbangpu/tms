//编辑
var parms = $ips.getUrlParams();
loadScript("js/plugin/jquery-form/jquery-form.min.js", runFormValidation);
var editid;

$(function(){
	//加载数据
    $ips.load("otd", "queryAll", null, function(data) {
    	console.info(data);
        if (data) {
        	for (var i = 0, max = data.length; i < max; i++) {
//				console.info(data[i]);
        		var result = data[i];
        		$('#id'+(i+1)).val(result.id);
        		$('#name'+(i+1)).val(result.name);
        		$('#status'+(i+1)).val(result.status);
        		$('#hours'+(i+1)).val(result.hours);
        		$('#minute'+(i+1)).val(result.minute);
        		$('#seconds'+(i+1)).val(result.seconds);
			}
        }
    });
	
    $("#status1").change(function(data){
    	console.info("status1:"+data);
    });
    
    
	// 保存
	$("#btnSubmit").click(function(){
		return classlinepriceSave(false);
	});
	
})

 
// Registration validation script
function runFormValidation() {
	var $checkoutForm = $('#editfrom').validate({
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
//    alert($checkoutForm);
	//form验证 对应runFormValidation方法里面
//    if (!$('#editfrom').validate($checkoutForm).form()) {
//        return false;
//    }
    
    var pararm = $("#editfrom").serialize();
//	var color = $("#color").val();
//	pararm += "&color=#"+color;
//    var privacy = 1;
//    if($("#privacy").is(':checked')){
//        privacy = 0;
//    }
//    pararm += "&privacy="+privacy;
//	//这样后端还是需要处理
//	var params = $("#editfrom").serializeArray();
//	var jsonParams = '{';
//		$.each(params, function(i, item) {
//            jsonParams += '"'+item.name+'":"'+item.value+'",'
//        });
//		jsonParams = jsonParams.substring(0,jsonParams.length-1) + '}';
//	console.log(jsonParams);
	$ips.load("otd", "update", pararm, function(result){
//		$ips.unLockPage();
		console.log(result);
		if(result) {
			$ips.succeed("保存站点成功。");
//			if (newed) { //继续标注
//				$('#editfrom')[0].reset();
//			} else{
//				$ips.locate("tms/basic","site_list");
//			}
		}else {
			$ips.error("保存站点表失败。" + result);
		}
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

function orglist() {
//	loadScript("/js/hui/jquery.hui.tree.js?v=1.1", function() {
//		$('#orgname').tree({
//			type: 'fiter',
//			module: 'sysDept',
//			method: 'queryDept4Tree',
//			selectid: 'deptid',
//			chooseType: 1,
//			//chooseId: chooseId,
//			params: {
//				selectids: ''
//			},
//			callback: function(result) {
//			}
//		});
//	},true,true);
}

