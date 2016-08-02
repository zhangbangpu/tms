// Load form valisation dependency 

loadScript('/js/poshytip/jquery.poshytip.min.js','');
loadScript("js/plugin/jquery-form/jquery-form.min.js", runFormValidation);
// Registration validation script
var lang_site = {};

$(function(){
	var params1 = new Array();
    params1.push({'name':'module', 'value':'driver'});
    params1.push({'name':'type', 'value':'html'});
    getlanguage(params1);

    var params2 = new Array();
    params2.push({'name':'module', 'value':'driver'});
    params2.push({'name':'type', 'value':'js'});
    getlanguage(params2);
    
    params2.push({'name':'headkey', 'value':'driver'});
    getlanguage(params2);

    $("#age").attr("placeholder", lang_site.age);

    //图片待上传时，位置调整。
    var par_div = $('#drivername_div').parent();

    for(var i=0;i<4;i++){
        $('#driverimage_div').next().removeClass("col");
        $('#driverimage_div').next().removeClass("col-6");
        var htmlContent = $('#driverimage_div').next().prop("outerHTML");
        $('#driverimage_div').next().remove();
        par_div.append(htmlContent);
    }
    
    if($('#age')){
    	$('#age').attr('placeholder',lang_site.age)
    }
    var nowDate = new Date();
    var year = nowDate.getFullYear();
    var month = nowDate.getMonth()+1;
    month = month < 10 ? "0" + month : month;
    var day = nowDate.getDate();
    var strNow = year+'-'+month + '-'+day;
    var fullTime = strNow+' ' + nowDate.getHours()+':'+nowDate.getMinutes()+':'+nowDate.getSeconds();
    var max = (year+50)+'-'+month+'-'+day+' '+nowDate.getHours()+":"+nowDate.getMinutes()+":"+nowDate.getSeconds();
    var min = (year-3)+'-'+month+'-'+day+' '+nowDate.getHours()+":"+nowDate.getMinutes()+":"+nowDate.getSeconds();

    //处理日期
    var begintime = moment().format('YYYY-MM-DD');
    $("#fromdate_1").val(begintime);
    $("#licensedate").val(begintime);
    $("#examdate").val(begintime);
    $("#drivecertdate").val(begintime);
    $('#fromdate_1').daterangepicker({ 
        singleDatePicker: true, //必须要有默input 认value
        //timePicker: true,     //是否启用时间选择
        timePickerIncrement:1,  //分钟选择的间隔
        format: 'YYYY-MM-DD',   //返回值的格式
        //timePicker12Hour: false,  //采用24小时计时制
        showDropdowns: true,        //是否显示年、月下拉框
        locale: getLocale()
    });
    $('#licensedate').daterangepicker({ 
        singleDatePicker: true, //必须要有默input 认value
        //timePicker: true,     //是否启用时间选择
        timePickerIncrement:1,  //分钟选择的间隔
        format: 'YYYY-MM-DD',   //返回值的格式
        //timePicker12Hour: false,  //采用24小时计时制
        showDropdowns: true,        //是否显示年、月下拉框
        // 设定日期范围
        minDate: min,
        maxDate: max,
        locale: getLocale()
    });
    $('#examdate').daterangepicker({ 
        singleDatePicker: true, //必须要有默input 认value
        //timePicker: true,     //是否启用时间选择
        timePickerIncrement:1,  //分钟选择的间隔
        format: 'YYYY-MM-DD',   //返回值的格式
        //timePicker12Hour: false,  //采用24小时计时制
        showDropdowns: true,        //是否显示年、月下拉框
        locale: getLocale()
    });
    $('#drivecertdate').daterangepicker({ 
        singleDatePicker: true, //必须要有默input 认value
        //timePicker: true,     //是否启用时间选择
        timePickerIncrement:1,  //分钟选择的间隔
        format: 'YYYY-MM-DD',   //返回值的格式
        //timePicker12Hour: false,  //采用24小时计时制
        showDropdowns: true,        //是否显示年、月下拉框
		// 从业资格证有效期只能选
        minDate: min,
        maxDate: max,
        locale: getLocale()
    }).on('show.daterangepicker', function(ev, picker) {
        $("body").css("padding-bottom","150px");
    }).on('hide.daterangepicker', function(ev, picker) {
        $("body").css("padding-bottom","0");
    });
    
    //身份证附件
    $('#idcardattach').hide();
    var idcardattachRow = $('#idcardattach').closest('label');
    idcardattachRow.empty();
    $('<a href="">'+lang_site.upload+'</a>').attr({
        'data-toggle': 'modal',
        'data-target': '#idcard_modal'
    }).addClass('btn btn-sm btn-link').bind('click', function() {
        $('#idcardfile').val('');
    }).appendTo(idcardattachRow);

    //司机照片附件
    $('#driverimage').hide();
    var driverimageRow = $('#driverimage').closest('label');
    driverimageRow.empty();
    $('<a href="">'+lang_site.uploadpictures+'</a>').attr({
        'data-toggle': 'modal',
        'data-target': '#driverimage_modal'
    }).addClass('btn btn-sm btn-link').bind('click', function() {
        $('#driverimagefile').val('');
    }).appendTo(driverimageRow);

    //驾驶证附件
    $('#driverlicense').hide();
    var driverlicenseRow = $('#driverlicense').closest('label');
    driverlicenseRow.empty();
    $('<a href="">'+lang_site.upload+'</a>').attr({
        'data-toggle': 'modal',
        'data-target': '#license'
    }).addClass('btn btn-sm btn-link').bind('click', function() {
        $('#driverlicensefile').val('');
    }).appendTo(driverlicenseRow);
    
    //从业资格证附件
    $('#workingcredential').hide();
    var workingcredentialRow = $('#workingcredential').closest('label');
    workingcredentialRow.empty();
    $('<a href="">'+lang_site.upload+'</a>').attr({
        'data-toggle': 'modal',
        'data-target': '#working'
    }).addClass('btn btn-sm btn-link').bind('click', function() {
        $('#workingfile').val('');
    }).appendTo(workingcredentialRow);

});


var editid = '';
var checkoutForm;
var driverimage_on;
chooseId = null;
function runFormValidation() {
    checkoutForm = {
        // Rules for form validation
        rules: {
            drivername: {
                required: true,
                maxlength: 30
            },
            orgcode: {
                required: true,
            }

        },
        // Messages for form validation
        messages: {
            drivername: {
                required: lang_site.required,
                maxlength: lang_site.maxThirtyChars
            },
            orgcode: {
                required: lang_site.required,
                number: lang_site.digital11
            }

        },
        // Do not change code below
        errorPlacement: function(error, element) {
            error.insertAfter(element.parent());
        }
    };
    //后台设置字段
    var has_dict_arr = []; 
    var alias = $ips.load('fields', 'getLayoutConfigs',{'modeltype':'driver'});
    if (alias && alias != 'false') {
        $.each(alias, function(i, v) {
            //使用照片动态调整位置开关
            if(v.fieldname == 'driverimage'&& v.isshow == 1){
                driverimage_on = 1;
            }

            if (v.isshow == 0) {
                $("#" + v.fieldname + "_div").hide();
                $("#" + v.fieldname).hide();
            }else{
                //是否字典
                if (v.dict.hasdict == 1) {
                    has_dict_arr.push(v.tbname + '.' + v.fieldname);
                }
            }

            //必填
            if (v.isshow == 1 && v.isrequired >= 1) {
                var name = $("#" + v.fieldname + '_lable').attr('class');
                if (name == undefined) {
                    name = "";
                }
                $("#" + v.fieldname + '_lable').attr('class', name + ' txt-color-red');

                checkoutForm.rules[v.fieldname] = {
                    required: true
                };
                checkoutForm.messages[v.fieldname] = {
                    required: lang_site.required
                };
            }
            $("#" + v.fieldname + '_lable').html(lang_site[v.fieldname]);


        });
        //字典构造
        $ips.load('dict', 'getDictBySetkey', {setkey: has_dict_arr}, function(data) {
            if (data) {
                $.each(data, function(k, v) {
                    makeDictOptions(v,k);
                });
            }
        });
    }
    // console.log(checkoutForm.rules);
    //编辑
    var param = $ips.getUrlParams();
    if (param.id) {
        editid = param.id;
        
        var isedit = $("#isedit").val();
        $ips.load("driver", "getDriverById", {'id': param.id,'isedit':isedit}, function(data) {
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
                $ips.fillFormInput("driver_form", data1);
                //img handle
                $('#showdriverimage').attr('src',data1['driverimage']);
                $('#driverimageattach_tip').css('display','block');
            }
			orglist();
			//显示附件“查看”
            extenttips();
        });
    } else {
        editid = '';
        orglist();
    }

}

function orglist() {
    loadScript("/js/hui/jquery.hui.tree.js", function() {
        $('#orgname').tree({
            type : 'select',
            method : 'getOrgans',
            module : 'truck',
            selectid : 'orgcodes',
			chooseType: 1,
            chooseId : chooseId,
            params : {
                selectids : ''
            },
            callback : function(result) {
                console.log(result);
            }
        });
    },true,true);
}

function makeDictOptions(dict,id)
{
    if(dict == undefined || id == undefined){
        return;
    }
    var _options = '';
    $.each(dict, function(k, v) {
        _options += '<option value="'+v.id+'">'+v.setvalue+'</option>';
    });
    $("#"+id).html(_options);
}

function savedriver(type) {
    //js从设置中获取必填项，生成必填项验证的rules，
    //给页面添加必填样式
	$ips.lockPage();
    //js页面验证
    if (!$('#driver_form').validate(checkoutForm).form()) {
    	$ips.unLockPage();
        return false;
    }
    var data = $("#driver_form :visible").serializeArray();

    if (editid != '') {
        data.push({name: 'id', value: editid});
    } else {
    	var idval = $("#driverid").val();
        
        if(idval!=0){
        	editid = idval;
        	data.push({name: 'id', value: idval});
        }
        
    }
    $ips.load("driver", "add", data, function(data) {
    	$ips.unLockPage();
        if (editid == '') {
            if (data.code > 0) {
                $ips.error(lang_site.addDriverFails);//错误信息提示
            } else {
                $ips.succeed(lang_site.driverAddedSuccessfully);//成功信息
                if (type == 1) {
                    //跳转到列表页
                    //window.location.href="#driver/index.html";
                    window.location.href = "#driver/index.html";
                }
                if (type == 2) {
                    location.reload();
                }

            }
        } else {
            if (data.code > 0) {
                $ips.error(lang_site.editDriverFails);//错误信息提示
            } else {
                $ips.succeed(lang_site.editSuccessfully);//成功信息
                if (type == 1) {
                    //跳转到列表页
                    //window.location.href="#driver/index.html";
                    window.location.href = "#driver/index.html";
                }
                if (type == 2) {
                    location.reload();
                }
            }
        }
    });
}

function add_zero(temp){
    if (temp < 10)
        return "0" + temp;
    else
        return temp;
}

loadScript('js/hui/jquery.hui.upload.js', uploadFile);

//上传附件
function uploadFile() {
		//身份证上传
	  $('#idcardfile').upload({
		  module: 'truck',
		  method: 'uploadImage',
	      onSuccess: function(data) {
	    	  if($('#idcardattachurl').val()){
	    		  var already_i = true;
		      }else{
		    	  already_i = false;
		      }
	    	  $('#idcardattachurl').val(data.data);
	    	//显示附件“查看”
	    	    extenttips(already_i);
	      }
	  });
	  $('#saveidcard').bind('click', function() {
	      if ($('#idcardfile').val() == '') {
	          $ips.error(lang_site.noChosenFile);
	          return false;
	      }
	
	      $('#idcardfile').upload('submit');
	      $('#idcard_modal .modal-header button').trigger('click');
	      return false;
	  });


    //司机照片上传
    $('#driverimagefile').upload({
        module: 'truck',
        method: 'uploadImage',
        onSuccess: function(data) {
            //显示附件 图片
            $('#showdriverimage').attr('src' , data.data);
            $('#driverimageurl').val(data.data);
            extenttips();
        }
    });
    $('#savedriverimage').bind('click', function() {
        if ($('#driverimagefile').val() == '') {
            $ips.error(lang_site.noChosenFile);
            return false;
        }

        $('#driverimagefile').upload('submit');
        $('#driverimage_modal .modal-header button').trigger('click');
        return false;
    });

	  //驾驶证上传
	  $('#driverlicensefile').upload({
	      module: 'truck',
	      method: 'uploadImage',
	      onSuccess: function(data) {
	    	  if($('#driverlicenseurl').val()){
	    		  var already_d = true;
		      }else{
		    	  already_d = false;
		      }
	    	  $('#driverlicenseurl').val(data.data);
	    	  //显示附件“查看”
	    	    extenttips(already_d);
	      }
	  });
	  $('#savelicense').bind('click', function() {
	      if ($('#driverlicensefile').val() == '') {
	          $ips.error(lang_site.noChosenFile);
	          return false;
	      }
	
	      $('#driverlicensefile').upload('submit');
	      $('#license .modal-header button').trigger('click');
	      return false;
	  });
	  
	  //从业资格证附件上传
	  $('#workingfile').upload({
	      module: 'truck',
	      method: 'uploadImage',
	      onSuccess: function(data) {
	    	  if($('#workingcredentialurl').val()){
	    		  var already_w = true;
		      }else{
		    	  already_w = false;
		      }
	    	  $('#workingcredentialurl').val(data.data);
	    	//显示附件“查看”
	    	    extenttips(already_w);
	      }
	  });
	  $('#saveworking').bind('click', function() {
	      if ($('#workingfile').val() == '') {
	          $ips.error(lang_site.noChosenFile);
	          return false;
	      }
	
	      $('#workingfile').upload('submit');
	      $('#working .modal-header button').trigger('click');
	      return false;
	  });
	  
}

//判断是否显示“查看”
function extenttips(isupload){
	if(!isupload){
		isupload = false;
	}
	// 附件tooltip
	var idcardattachurl = $('#idcardattachurl').val();
	if(idcardattachurl){
		$('#idcardattach_tip').css('display','block');
		var img = $("<img>").attr("src",idcardattachurl);
		if(isupload){
			$('.extiat').poshytip('update',img[0]);
		}else{
			$('.extiat').poshytip({
				content:img[0],
				alignTo:"target",
				alignX:"left",
				alignY:"center",
				offsetX:10
			});
		}
	}
	var driverimageurl = $('#showdriverimage').attr('src');
    if(driverimage_on == 1){
        $('#showdriverimage').css('display','block');
        var img = $("<img>").attr("src",driverimageurl);
        $('#driverimage_modal').css('display','none');
    }

	var driverlicenseurl = $('#driverlicenseurl').val();
	if(driverlicenseurl){
		$('#driverlicense_tip').css('display','block');
		var img = $("<img>").attr("src",driverlicenseurl);
		if(isupload){
			$('.extdlt').poshytip('update',img[0]);
		}else{
			$('.extdlt').poshytip({
				content:img[0],
				alignTo:"target",
				alignX:"left",
				alignY:"center",
				offsetX:10
			});
		}
	}
	
	var workingcredentialurl = $('#workingcredentialurl').val();
	if(workingcredentialurl){
		$('#workingcredential_tip').css('display','block');
		var img = $("<img>").attr("src",workingcredentialurl);
		if(isupload){
			$('.extwct').poshytip('update',img[0]);
		}else{
			$('.extwct').poshytip({
				content:img[0],
				alignTo:"target",
				alignX:"left",
				alignY:"center",
				offsetX:10
			});
		}
	}
}