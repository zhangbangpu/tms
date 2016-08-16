$("#roleids").select2({
	placeholder : '请选择角色',
	minimumInputLength : 1,
	multiple : false,
	allowClear : true,
	data : [],
	initSelection : function () {}
});
$("#orgcode").select2({
	placeholder : '请选择机构',
	minimumInputLength : 1,
	allowClear : true,
	// 数据加载
	query : function(query) {
		$ips.load('sysDept', 'getDeptByName', {
			keyword : query.term
		}, function(e) {
			var _pre_data = [];
			$.each(e, function(k, v) {
				_pre_data.push({
					id : v.orgcode,
					text : v.name
				});
			});
			var data = {
				results : _pre_data
			};
			query.callback(data);
		});
	},
	initSelection : function (e, r) {
	}
}).on('change', function (e) {
	var deptid = $('#deptid').select2('val');
	if (deptid == '') {
		$("#roleids").select2({'data' : []});
		$("#roleids").select2('val', '');
		return false;
	}
	$ips.load('sysRole', 'getRoleByDeptid', {deptid : deptid}, function(res) {
        var data = [];
        $.each(res, function(k, v) {
            data.push({
                id : v.id,
                text : v.name
            });
        });
        $("#roleids").select2({'data' : data});
		$("#roleids").select2('val', '');
    });
});
var isupdate = updateid = 0;
var parms = $ips.getUrlParams();
if (parms["id"]) {
	var entity = $ips.load("sysUser", "getUserById", "id=" + parms["id"]);
	if (!entity.id) {
		window.setTimeout("window.location.hash = '#index.html'", 2000);
	}
	if (entity) {
		$ips.fillFormInput('frmInfo', entity);

		$("#deptid").select2('data', {id : entity.deptid, text : entity.deptname}).val(entity.deptid);
		var roles = $ips.load('role', 'getRoleByDeptid', {orgcode : $('#deptid').val()});
		var initRoles = [];
		$.each(roles, function(k, v) {
            initRoles.push({
                id : v.id,
                text : v.name
            });
        });
		$("#roleids").select2({data : initRoles});
		$('#roleids').select2('val', entity.roleids);
		$('input[name=username]').attr("readonly", "readonly");
		$('#userpic').hide();
		var userpicRow = $('#userpic').closest('label');
		userpicRow.empty();
		$('<a href="">上传</a>').attr({
			'data-toggle' : "modal",
			'data-target' : "#myUpload"
		}).addClass('btn btn-sm btn-link').bind('click', function() {
			$('#imgFile').val('');
		}).appendTo(userpicRow);
		$('#passwd').hide();
		var passwdRow = $('#passwd').closest('label');
		passwdRow.empty();
		$('<a href="">修改密码</a>').attr({
			'data-toggle' : "modal",
			'data-target' : "#myModal"
		}).addClass('btn btn-sm btn-link').bind('click', function() {
			$('#newPassword').val('');
			$('#newPasswordAgain').val('');
		}).appendTo(passwdRow);
		// 修改标记
		isupdate = 1;
		// 需要更新的Id
		updateid = entity['id'];
	}
} else {
	// $('#userpic').closest('div').hide();
	// $('#userpic').hide();
	// var userpicRow = $('#userpic').closest('label');
	// userpicRow.empty();
	// $('<input href="">上传</a>').bind('click', function() {
	// $('#imgFile').val('');
	// }).appendTo(userpicRow);
}


$('#saveNewPassword').bind(
		'click',
		function() {
			if ($('#newPassword').val() == ''
					|| $('#newPasswordAgain').val() == '') {
				$ips.error('密码不能为空');
				return false;
			}
			if ($('#newPassword').val() != $('#newPasswordAgain').val()) {
				$ips.error('两次密码输入不一致!');
				return false;
			}
			if ($('#newPassword').val().length < 6
					|| $('#newPassword').val().length > 16) {
				$ips.error('密码长度不得小于6位大于16');
				return false;
			}
			$ips.load('user', 'changePassword', {
				id : parms["id"],
				password : $('#newPassword').val()
			}, function(res) {
				if (typeof res.code != 'undefined' && res.code != 0) {
					$ips.error(res.message);
					return false;
				}
				$ips.succeed('密码修改成功');
				$('#myModal .modal-header button').trigger('click');
			})
			return false;
		});
// Load form valisation dependency
loadScript("js/plugin/jquery-form/jquery-form.min.js", runFormValidation);
loadScript("js/hui/jquery.hui.upload.js", uploadFile);

// 上传头像
function uploadFile() {
	// 编辑时上传头像
	$("#imgFile").upload(
			{
				module : "sysUser",
				method : "uploadImage",
				onSuccess : function(uploadRes) {
					if (uploadRes.data) {
						var appendInput = $("<input type='hidden' value='"
								+ uploadRes.data + "' name='picid' />");
						$("#frmInfo").append(appendInput);
					} else {
						console.log('UPLOAD ERROR');
					}

				}
			});

	// 新建上传头像
	// $("#userpic").upload({
	// module: "user",
	// method: "uploadImage",
	// onSuccess: function(upres) {
	// // console.log(upres);
	// // gPicId = upres;
	// }
	// });

	// 编辑时上传头像
	$('#saveImage').bind('click', function() {
		if ($('#imgFile').val() == '') {
			$ips.error('没有选择文件');
			return false;
		}

		$("#imgFile").upload("submit");
		$('#myUpload .modal-header button').trigger('click');
		return false;
	});
}

// Registration validation script
function runFormValidation() {
	var $checkoutForm = $('#frmInfo').validate({
		// Rules for form validation
		rules : {
			username : {
				required : true
			},
			passwd : {
				required : true
			},
			email : {
				email : true
			},
			realname : {
				required : true
			}
		},
		// Messages for form validation
		messages : {},
		// Do not change code below
		errorPlacement : function(error, element) {
			error.insertAfter(element.parent());
		}
	});

	// 保存
	$("#btnSubmit").click(function() {
		return userSave(false);
	});
	// 保存并新建
	$("#btnSubmitNew").click(function() {
		return userSave(true);
	});
}

function userSave(newed) {
	if (!$('#frmInfo').validate().form()) {
		return false;
	}

//	$('#frmInfo').ajaxSubmit({
//		url: $ips.getApiurl() + "?t=json&m=user&f=save",
//		dataType: 'json',
//		success: function(result) {alert(result);
//			if (result) {
//				$ips.succeed("保存成功。");
//				if (newed) {
//					isupdate = updateid = 0;
//					$('#frmInfo')[0].reset();
//				} else
//					window.history.go(-1);
//			} else {
//				$ips.error("保存失败。" + result.message);
//			}
//
//		}});
//
//	return false;


	var pararm = $("#frmInfo").serialize();
    if (isupdate)
        pararm += '&id=' + updateid;
	$ips.load("sysUser", "addUser", pararm, function(result) {
        if (result) {
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
        } else if (result.code == 2) {
            $ips.showError(result.code, [result.message]);
        } else
            $ips.showError(result.code, result.message);
    });
    return false;
}
