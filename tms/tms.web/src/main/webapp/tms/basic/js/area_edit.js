//编辑
var parms = $ips.getUrlParams();
loadScript("js/plugin/jquery-form/jquery-form.min.js", runFormValidation);
var editid;

$(function(){
	
    $("#orgname").select2({
        placeholder: '请选择机构',
        //minimumInputLength: 0,
        multiple: false,
        allowClear: true,
        //数据加载
        query: function (query) {
            $ips.load('sysDept', 'queryDeptByCondition', {
                name: query.term 
            }, function (e) {
                var _pre_data = [];
                $.each(e, function (k, v) {
                	_pre_data.push({
                		id: v.id,
                		text: v.name
                	});
                });
                var data = {
                    results: _pre_data
                };
                query.callback(data);
            });
        }
    });
    
	//加载 承运商下拉框数据
    $("#wlcompany").select2({
        placeholder: '请选择承运商',
        //minimumInputLength: 0,
        multiple: false,
        allowClear: true,
        //数据加载
        query: function (query) {
            $ips.load('sysUser', 'queAllUserByCtn', {
            	type: 2,
                name: query.term 
            }, function (e) {
                var _pre_data = [];
                $.each(e, function (k, v) {
                	_pre_data.push({
                		id: v.id,
                		text: v.name
                	});
                });
                var data = {
                    results: _pre_data
                };
                query.callback(data);
            });
        }
    });
    
    //加载 承运商下拉框数据
    $("#sitecodes").select2({
    	placeholder: '请选择站点',
//    	minimumInputLength: 2,
    	multiple: true,
    	allowClear: true,
    	//数据加载
    	query: function (query) {
    		$ips.load('site', 'selectAllSiteByCtn', {
    			name: query.term 
    		}, function (e) {
    			var _pre_data = [];
    			$.each(e, function (k, v) {
    				_pre_data.push({
    					id: v.code,
    					text: v.name
    				});
    			});
    			var data = {
    					results: _pre_data
    			};
    			query.callback(data);
    		});
    	}
    });
    
    //编辑
    var param = $ips.getUrlParams();
    if (param.id) {
        editid = param.id;
        
        var entity = $ips.load("area", "queryOneById", "id=" + parms["id"]);
        if (!entity.id) {
    		window.setTimeout("window.location.hash = '#home.html'", 2000);
    	}
    	if (entity) {
    		$ips.fillFormInput("editfrom", entity);
    		$("#orgname").select2('data', {id : entity.deptname, text : entity.deptidname}).val(entity.deptname);
    		$("#wlcompany").select2('data', {id : entity.wlcompany, text : entity.wlcompanyname}).val(entity.wlcompany);
    		$("#sitecodes").select2('data', {id : entity.sitecodes, text : entity.sitenames}).val(entity.sitecodes);
    	}
//        $ips.load("area", "queryOneById", {'id': param.id}, function(data) {
//            if (data) {
//                var data1 = new Array();
//                chooseId = data.orgcode;
//                $.each(data, function(i, v) {
//                    if (v != "" && v != null) {
//                        data1[i] = v;
//                    }
//                    if (v == "0000-00-00" || v == "0000-00-00 00-00-00") {
//                        data1[i] = "";
//                    }
//                });
//                $ips.fillFormInput("editfrom", data1);
//                var entity = data1.get(0);
//                $("#orgname").select2('data', {id : entity.deptname, text : entity.deptidname}).val(entity.deptname);
//                $("#wlcompany").select2('data', {id : entity.wlcompany, text : entity.wlcompanyname}).val(entity.wlcompany);
//                $("#sitecodes").select2('data', {id : entity.sitecodes, text : entity.sitenames}).val(entity.sitecodes);
//
//            }
//			orglist();
//        });
    } else {
        editid = '';
    }
	
})

 
// Registration validation script
function runFormValidation() {
    var $checkoutForm = $('#editfrom').validate({
        // Rules for form validation
        rules: {
            name: {
                required: true
            }
        },
        // Messages for form validation
        messages: {
        },
        // Do not change code below
        errorPlacement: function(error, element) {
            error.insertAfter(element.parent());
        }
    });
	
	
	// 保存
	$("#btnSubmit").click(function(){
		return siteSave(false);
	});
	
	// 保存并新建
	$("#btnSubmitNew").click(function(){
		return siteSave(true);
	});
}
	
function siteSave(newed) {
    if (!$('#editfrom').validate().form()) {
        return false;
    }
    
    var pararm = $("#editfrom").serialize();
	
    $ips.load("area", "addArea", pararm, function(result){
//		$ips.unLockPage();
		console.log(result);
		if(result) {
			$ips.succeed("保存区域站点成功。");
			if (newed) { //继续标注
				$('#editfrom')[0].reset();
			} else{
				$ips.locate("tms/basic","area_list");
			}
		}else {
			$ips.error("保存区域站点表失败。" + result);
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

//加载 机构下拉框数据
function orglist() {}
	
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
//}

