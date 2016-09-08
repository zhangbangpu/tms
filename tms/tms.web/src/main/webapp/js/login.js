//判断用户是否登陆
//$ips.load('login', 'isLogin', null, function(result) {
//    if (result.success)
//    {
//        if (getPar('referer') != false) {
//            window.location.href = getPar('referer');
//        }
//        redirectToGateway();
//    }
//});
function getPar(par) {
    //获取当前URL
    var local_url = document.location.href;
    //获取要取得的get参数位置
    var get = local_url.indexOf(par + '=');
    if (get == -1) {
        return false;
    }
    //截取字符串
    var get_par = local_url.slice(par.length + get + 1);
    //判断截取后的字符串是否还有其他get参数
    var nextPar = get_par.indexOf('&');
    if (nextPar != -1) {
        get_par = get_par.slice(0, nextPar);
    }
    return get_par;
}
//$.backstretch([
//  'img/g7/bg2.jpg',
//  'img/g7/bg1.jpg'
//  ], {
//    fade: 1000,
//    duration: 7000
//});
$('#login-form').validate({
    rules: {
        user_name: {
            required: true
            // email : true
        },
        password: {
            required: true,
            minlength: 3,
            maxlength: 20
        }
    },

    // Messages for form validation
    messages: {
        email: {
            required: '请输入用户名'
            // email : 'Please enter a VALID email address'
        },
        password: {
            required: '请输入密码'
        }
    },

    // Do not change code below
    errorPlacement: function(error, element) {
        error.insertAfter(element.parent());
    }
});

// 登陆
$('#form_button').click(function() {
    if (!$('#login-form').validate().form()) {
        return false;
    }
    var pararm = $('#login-form').serialize();
    $ips.load('login', 'login', pararm, function(result) {
    	console.log(result);
        if (result.code == 0) {
        	window.location.href="index.html?username="+result.username;
//        	window.location.href="index.html?username="+result.username + result.defaultIndex;
//        	window.location.href="/jsp/index.jsp#tms/basic/site_list.html";
        } else {
            $('.padding-top-10:first').animate({right: '10px'},80);
            $('.padding-top-10:first').animate({right: ''},80);
            $('.padding-top-10:first').animate({left: '10px'},80);
            $('.padding-top-10:first').animate({left: ''},80);
            $('.padding-top-10:first').removeAttr('style');
            $ips.error(result.message);
        }
    });
    return false;
});

//根据平台类型以及浏览器宽度调制登录框的大小
function loadCSS() {
    var kwidth = $('body').width();
    if ((navigator.userAgent.match(
        /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|wOSBrowser|BrowserNG|WebOS)/i
        )) && kwidth < 768) {
        $('#loginBg').removeClass('normalWidth');
    }
    else {
        $('#loginBg').addClass('normalWidth');
    }
}
loadCSS();
$(window).resize(function() {
    loadCSS();
});

function redirectToGateway() {
    var xmlhttp = getXmlHttp();
    if (xmlhttp == null) {
        alert('获取资源地址失败');
        return '';
    }
    try {
        //$ips.load("index", "getResourceUrl");
         xmlhttp.open('GET', 'inside.php?t=json&m=index&f=getGetwayUrl', false);
         xmlhttp.send();
         //console.log(xmlhttp);
         if (xmlhttp.status == 200) {
             //return xmlhttp.responseText;
             window.location.href = xmlhttp.responseText;
         }
         throw '';
    } catch (e) {
         return '';
    }
}

