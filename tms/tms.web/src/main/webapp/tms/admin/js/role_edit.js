//$("#orgcode").select2({
//    placeholder : '请选择机构',
//    minimumInputLength : 1,
//    allowClear : true,
//    // 数据加载
//    query : function(query) {
//        $ips.load('sysDept', 'getDeptByName', {keyword : query.term, isorgroot : 1}, function(e) {
//            var _pre_data = [];
//            $.each(e, function(k, v) {
//                _pre_data.push({
//                    id : v.orgcode,
//                    text : v.name
//                });
//            });
//            var data = {
//                results : _pre_data
//            };
//            query.callback(data);
//        });
//    },
//    initSelection : function (e, r) {
//    }
//});
//var user = $ips.getCurrentUser();
//if (!user.isSuper) {
//    $('#projecttyperow').hide();
//} else {
//    var projecttypes = $ips.load('resource', 'getprojects', {});
//    console.log(projecttypes);
//    if (typeof projecttypes != 'undefined' && projecttypes.length != 0) {
//        $.each(projecttypes, function (index, item) {
//            $('<option></option>').val(item.projecttype).text(item.projecttype).appendTo($('#projecttype'));
//        });
//    }
//}
var isupdate = updateid = 0;
var parms = $ips.getUrlParams();
if (parms["id"]) {
    var entity = $ips.load("sysRole", "queryOneById", "id=" + parms["id"]);
    if (!entity.id) {
        window.setTimeout("window.location.hash = '#index.html'", 2000);
    }
    if (entity) {
        // 超级管理员需要指点当前编辑角色拥有的定制项目
//        if (user.isSuper) {
//            entity.projecttype = $ips.load("sysRole", "getTypeByRole", {roleid : entity.id});
//        }
        $ips.fillFormInput('frmInfo', entity);
        $("#orgcode").select2('data', {id : entity.orgroot, text : entity.orgname});
        $("#orgcode").select2('val', entity.orgroot);
        $("#orgcode").select2('enable', false);

        //修改标记
        isupdate = 1;
        //需要更新的Id
        updateid = entity['id'];
    }
} else {
//    $("#orgcode").select2('data', {id : user.organ.orgroot, text : user.organ.name});
//    $("#orgcode").select2('val', user.organ.orgroot);
//    if (user.isSuper != 1) {
        $('#orgcode').select2('enable', false);
//    }
}
//loadScript("/js/hui/jquery.hui.tree.js", function() {
//    $('#resource-list').tree({
//        method: 'index',
//        selectid: 'orgvalue',
//        params: {
//            'dataType': 'resource',
//            'subsystem': '', // 车辆管理
//            'rid': parms['id'],
//            'projecttype' : $('#projecttype').val()
//
//        },
//        callback: function(result) {
//            
//        }
//    });
//});

// Load form valisation dependency 
loadScript("js/plugin/jquery-form/jquery-form.min.js", runFormValidation);
// Registration validation script
function runFormValidation() {
    var $checkoutForm = $('#frmInfo').validate({
        // Rules for form validation
        rules: {
            name: {
                required: true
            },
        },
        // Messages for form validation
        messages: {
        },
        // Do not change code below
        errorPlacement: function(error, element) {
            error.insertAfter(element.parent());
        }
    });
}

// 保存
$("#btnSubmit").click(function() {
    return roleSave(false);
});

// 保存并新建
$("#btnSubmitNew").click(function() {
    return roleSave(true);
});

function roleSave(newed) {
    if (!$('#frmInfo').validate().form()) {
        return false;
    }

    var params = $("#frmInfo").serializeArray();
    
	var obj = document.getElementsByName('menuId'); // 选择所有name="'test'"的对象，返回数组
	// 取到对象数组后，我们来循环检测它是不是被选中
	var s = '';
	for (var i = 0; i < obj.length; i++) {
		if (obj[i].checked)
			s += obj[i].value + ','; // 如果选中，将value添加到变量s中
	} 
    
    params.push({name:'menuIds', value: s});
    if (isupdate) {
        params.push({name:'id', value: updateid});
    }
    
    //params += '&id=' + updateid;
    $ips.load("sysRole", "addRole", params, function(result) {
        if (result) {
            $ips.succeed("保存成功。");
            if (newed) {
                isupdate = updateid = 0;
                //$('#frmInfo')[0].reset();
                window.history.go(0);
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
        } else if (result.code == 2) {
            $ips.showError(result.code, [result.message]);
        } else
            $ips.showError(result.code, result.message);
    });
    return false;
}

$('#projecttype').bind('change', function () {
    //$ips.lockPage('<h4><i class="fa fa-repeat  fa-spin"></i> 正在重载资源...</h4>');
    $('#resource-list').tree('destroy');
    $('#resource-list').tree({
        method: 'index',
        selectid: 'orgvalue',
        params: {
            'dataType': 'resource',
            'subsystem': '', // 车辆管理
            'rid': parms['id'],
            'projecttype' : $('#projecttype').val()
        },
        complete: function(result,a) {
            console.log(result);
            console.log(a);
            //$ips.unLockPage();
            //console.log(11);
        }
    });
});