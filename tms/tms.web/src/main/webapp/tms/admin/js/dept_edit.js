
//$("#parentid").select2({
//    placeholder: '请选择机构',
//    minimumInputLength: 1,
//    multiple: false,
//    allowClear: true,
//    selectOnBlur: true,
//    //数据加载
//    query: function(query) {
//        $ips.load('org', 'getOrgByName', {keyword: query.term}, function(e) {
//            var _pre_data = [];
//            $.each(e, function(k, v) {
//                _pre_data.push({id: v.id, text: v.name});
//            });
//            var data = {results: _pre_data};
//            query.callback(data);
//        });
//    },
//    initSelection: function(element, callback) {
//        //console.log($(element).val());
//    },
//}).on('change', function(e) {
//    if ($("#parentid").select2('val') == '') {
//        $('#authcode-row').show();
//    } else {
//        $('#authcode-row').hide();
//    }
//});

//var isupdate = updateid = 0;
//var parms = $ips.getUrlParams();
//if (parms["id"]) {
//    var entity = $ips.load("org", "get", "id=" + parms["id"]);
//    if (!entity.id) {
//        $ips.error('指定的机构未找到');
//        window.setTimeout("window.location.hash = '#org/index.html'", 2000);
//    }
//
//    if (entity) {
//        $ips.fillFormInput('frmInfo', entity);
//        
//        if (entity.parentid != '0') {
//            $("#parentid").attr('data-placeholder', entity.parentname);
//            $('#parentid').select2('data', {id : entity.parentid, text : entity.parentname});
//        }
//        $('#parentid').select2("enable", false);
//
//		if(entity.userOrgcode == entity.orgcode){
//			$('input[name=name]').attr("disabled", "true")
//                .css("border", "1px solid #DDDDDD")
//			    .parent('label').attr("class", "input state-success");
//			$('input[name=customerid]').attr("disabled", "true")
//			    .css("border", "1px solid #DDDDDD")
//			    .parent('label').attr("class", "input state-success");
//			$('input[name=authcode]').attr("disabled", "true")
//			    .css("border", "1px solid #DDDDDD")
//			    .parent('label').attr("class", "input state-success");	
//		}
//        if(entity.orgcode !== entity.orgroot){
//        	$('#authcode-row').hide();
//        }
//        //修改标记
//        isupdate = 1;
//        //需要更新的Id
//        updateid = entity['id'];
//    }
//} else {
//	var currentUserInfos = $ips.getCurrentUser();
//	if(currentUserInfos.isSuper){
//		 $("#parentid").attr('data-placeholder', '请选择机构');
//	}else{
//        $('#parentid').select2('data', {id : currentUserInfos.organ.orgid, text : currentUserInfos.organ.name});
//        $('#parentid').on('select2-clearing', function () {
//            return false;
//        })
//        
//		$("#parentid").select2('val', currentUserInfos.organ.orgid);
//        $('#authcode-row').hide();
//	}
//}

//$('#loginLogo').hide();
//var userpicRow = $('#loginLogo').closest('label');
//userpicRow.empty();
//$('<a href="">上传</a>').attr({
//	'data-toggle': "modal",
//	'data-target': "#myUpload"
//}).addClass('btn btn-sm btn-link').bind('click', function() {
//	$('#imgFile').val('');
//}).appendTo(userpicRow);


//$('#gatewayLogo').hide();
//var userpicRow = $('#gatewayLogo').closest('label');
//userpicRow.empty();
//$('<a href="">上传</a>').attr({
//	'data-toggle': "modal",
//	'data-target': "#myUpload2"
//}).addClass('btn btn-sm btn-link').bind('click', function() {
//	$('#imgFile2').val('');
//}).appendTo(userpicRow);

//$('#gatewayBackground').hide();
//var userpicRow = $('#gatewayBackground').closest('label');
//userpicRow.empty();
//$('<a href="">上传</a>').attr({
//    'data-toggle': "modal",
//    'data-target': "#gatewayBackgroundModal"
//}).addClass('btn btn-sm btn-link').bind('click', function() {
//    $('#gatewayBackgroundInput').val('');
//}).appendTo(userpicRow);


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
            contact: {
                required: true,
            },
            address: {
                required: true,
            },
            tel: {
                required: true,
                number: true
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


    // 保存
    $("#btnSubmit").click(function() {
        return orgSave(false);
    });

    // 保存并新建
    $("#btnSubmitNew").click(function() {
        return orgSave(true);
    });
}


// Load form valisation dependency 
//loadScript("js/hui/jquery.hui.upload.js", uploadFile);

//上传登陆页logo
//function uploadFile() {
//	//编辑时登陆页logo
//	$("#imgFile").upload({
//		module: "org",
//		method: "uploadImage",
//		onSuccess: function(uploadRes) {
//			if(uploadRes.data){
//					var appendInput=$("<input type='hidden' value='"+uploadRes.data+"' name='loginlogoid' />");
//					$("input[name=loginlogoid]").remove();
//					$("#frmInfo").append(appendInput);
//			}else{
//				$ips.error("上传失败，请按照提示选择上传logo");
//				return false;
//			}
//			
//		}
//	});
//
//	//编辑时登陆页logo
//	$("#imgFile2").upload({
//		module: "org",
//		method: "uploadImage",
//		onSuccess: function(uploadRes) {
//			if(uploadRes.data){
//					var appendInput=$("<input type='hidden' value='"+uploadRes.data+"' name='gatewaylogoid' />");
//					$("input[name=gatewaylogoid]").remove();
//					$("#frmInfo").append(appendInput);
//			}else{
//				$ips.error("上传失败，请按照提示选择上传logo");
//				return false;
//			}
//			
//		}
//	});
//
//
//    $("#gatewayBackgroundInput").upload({
//        module: "org",
//        method: "uploadImage",
//        onSuccess: function(uploadRes) {
//            if(uploadRes.data){
//                    var appendInput=$("<input type='hidden' value='"+uploadRes.data+"' name='gatewaybackgroundid' />");
//                    $("input[name=gatewaybackgroundid]").remove();
//                    $("#frmInfo").append(appendInput);
//            }else{
//                $ips.error("上传失败，请按照提示选择上传logo");
//                return false;
//            }
//            
//        }
//    });
//
//	//编辑时上传头像
//	$('#saveImage').bind('click', function() {
//		if ($('#imgFile').val() == '') {
//			$ips.error('没有选择文件');
//			return false;
//		}
//
//		$("#imgFile").upload("submit");
//		$('#myUpload .modal-header button').trigger('click');
//		return false;
//	});
//
//	//编辑时上传头像
//	$('#saveImage2').bind('click', function() {
//		if ($('#imgFile2').val() == '') {
//			$ips.error('没有选择文件');
//			return false;
//		}
//
//		$("#imgFile2").upload("submit");
//		$('#myUpload2 .modal-header button').trigger('click');
//		return false;
//	});
//    //编辑时上传头像
//    $('#gatewayBackgroundBtn').bind('click', function() {
//        if ($('#gatewayBackgroundInput').val() == '') {
//            $ips.error('没有选择文件');
//            return false;
//        }
//
//        $("#gatewayBackgroundInput").upload("submit");
//        $('#gatewayBackgroundModal .modal-header button').trigger('click');
//        return false;
//    });
//}


function orgSave(newed) {
    if (!$('#frmInfo').validate().form()) {
        return false;
    }

    var pararm = $("#frmInfo").serializeArray();
//    pararm.push({
//        name : 'parentid',
//        value : $('#parentid').select2('val')
//    });
//    if (isupdate) {
//	    pararm.push({
//	        name : 'id',
//	        value : updateid
//	    });
//    }
    $("#btnSubmit").attr('disabled','disabled');
    $("#btnSubmitNew").attr('disabled','disabled');
    $ips.load("sysDept", "addDept", pararm, function(result) {
        if (result) {
            $ips.succeed("保存成功。");
            if (newed) {
                isupdate = updateid = 0;
                $('#frmInfo')[0].reset();
                $("#btnSubmit").removeAttr('disabled');
                $("#btnSubmitNew").removeAttr('disabled');
            } else
            	window.history.go(-1);
        } else {
        	 $("#btnSubmit").removeAttr('disabled');
        	 $("#btnSubmitNew").removeAttr('disabled');
            $ips.error("保存失败。" + result);
        }
    }, function(result) {
        // 验证失败的code号码
    	 $("#btnSubmit").removeAttr('disabled');
    	 $("#btnSubmitNew").removeAttr('disabled');
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