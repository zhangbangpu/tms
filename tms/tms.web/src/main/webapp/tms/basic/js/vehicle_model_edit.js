//编辑
var parms = $ips.getUrlParams();
loadScript("js/plugin/jquery-form/jquery-form.min.js", runFormValidation);
var editid;

$(function(){
    //编辑
    var param = $ips.getUrlParams();
    if (param.id) {
        editid = param.id;
        
        var isedit = $("#isedit").val();
        $ips.load("vehicleModel", "queryOneById", {'id': param.id}, function(data) {
            if (data) {
                var data1 = new Array();
                chooseId = data.orgcode;
                $.each(data, function(i, v) {
                    if (v != "" && v != null) {
                        data1[i] = v;
                    }
                    if (v == "0000-00-00" || v == "0000-00-00 00-00-00") {
                        data1[i] = "";
                    }
                });
                $ips.fillFormInput("editfrom", data1);
//                //img handle
//                $('#showdriverimage').attr('src',data1['driverimage']);
//                $('#driverimageattach_tip').css('display','block');
            }
			orglist();
        });
    } else {
        editid = '';
        orglist();
    }
	
})

 
// Registration validation script
function runFormValidation() {
	var $checkoutForm = $('#editfrom').validate({
		// Rules for form validation
		rules : {
			name: {
                required: true
            }
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
		return vehicleModel(false);
	});
	
	// 保存并新建
	$("#btnSubmitNew").click(function(){
		return vehicleModel(true);
	});
}
	
function vehicleModel(newed) {
//    alert($checkoutForm);
	//form验证 对应runFormValidation方法里面
    if (!$('#editfrom').validate().form()) {
        return false;
    }
    
    var pararm = $("#editfrom").serialize();
	var color = $("#color").val();
	pararm += "&color=#"+color;
    var privacy = 1;
    if($("#privacy").is(':checked')){
        privacy = 0;
    }
    pararm += "&privacy="+privacy;
	$ips.load("vehicleModel", "addVehicleModel", pararm, function(result){
//		$ips.unLockPage();
		console.log(result);
		if(result) {
			$ips.succeed("保存站点成功。");
			if (newed) { //继续标注
				$('#editfrom')[0].reset();
			} else{
				$ips.locate("tms/basic","vehicle_model_list");
			}
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

