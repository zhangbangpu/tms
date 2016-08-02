if (!window.location.origin)
    /**
     * window.location.origin
     *
     * @type {[type]}
     */
   window.location.origin = window.location.protocol + 
   '//' + window.location.host;
RESOURCEURL = getBaseUrl('resourceurl', 
    'inside.php?t=json&m=index&f=getResourceUrl');
UCENTERURL = getBaseUrl('ucenterurl', 
    'inside.php?t=json&m=index&f=getUcenterUrl');
var resourceFiles = [
    '/js/libs/jquery-2.1.3.min.js',
    '/js/libs/jquery-ui-1.10.3.min.js',
    '/js/bootstrap/bootstrap.min.js',
    '/js/yepnopejs/yepnope.1.5.4-min.js',
    '/js/notification/SmartNotification.min.js',
    '/js/smartwidgets/jarvis.widget.min.js',
    '/js/plugin/easy-pie-chart/jquery.easy-pie-chart.min.js',
    '/js/plugin/sparkline/jquery.sparkline.min.js',
    '/js/plugin/jquery-validate/jquery.validate.min.js',
    '/js/plugin/masked-input/jquery.maskedinput.min.js',
    '/js/plugin/select2/select2.min.js',
    '/js/plugin/bootstrap-slider/bootstrap-slider.min.js',
    '/js/plugin/msie-fix/jquery.mb.browser.min.js',
    '/js/plugin/smartclick/smartclick.js',
    '/js/plugin/jquerycookie/jquery.cookie.js',
    '/js/plugin/jquery-validate/jquery.validate.ext.js',
    '/js/blackbirdjs/blackbird.js',
    '/js/plugin/jquery-validate/messages_bs_zh.js',
    '/js/plugin/blockui/jquery.blockUI.js',
    '/js/plugin/iframeresizer/js/iframeResizer.contentWindow.min.js',
    '/js/plugin/date/moment.js',
    '/js/plugin/date/daterangepicker.js'
];
loadFile(RESOURCEURL + '??' + resourceFiles.join(','));
loadFile(window.location.origin + '/js/iframe.js');
var initCss = [
    '/css/bootstrap.min.css',
    '/css/smartadmin-production.css',
    '/css/smartadmin-skins.css',
    '/css/demo-increase.css',
    '/css/jquery.multiselect2side.css',
    '/css/daterangepicker-bs3.css',
    '/css/demo.css',
    'css/font-awesome.min.css'
];
loadFile(RESOURCEURL + '??' + initCss.join(','), 'css');

function loadFile(file, type)
{
    if (type == 'css') {
        document.write('<link' + ' rel ="stylesheet" type="text/css"' + 
            ' media="screen" href="' + file + '">');
    } else {
        document.write('<scr' + 'ipt type="text/javascript" src="' + 
            file + '"></scr' + 'ipt>');
    }
}

/**
 * 获取地址
 *
 * @param  {[type]} key     [description]
 * @param  {[type]} resturl [description]
 * @return {[type]}
 */
function getBaseUrl(key, resturl)
{
    var url = getCookie(key);
    if (!!url) {
        return unescape(url);
    }

    var xmlhttp = getXmlHttp();
    if (xmlhttp == null) {
        alert('获取资源地址失败');
        return '';
    }
    try {
         xmlhttp.open('GET', resturl, false);
         xmlhttp.send();
         //console.log(xmlhttp);
         if (xmlhttp.status == 200) {
             return xmlhttp.responseText;
         }
         throw '';
    } catch (e) {
         return '';
    }
}

function getXmlHttp() {
    var xmlhttp = null;
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    }
    if (!xmlhttp && window.ActiveXObject) {
    try {
        xmlhttp = new ActiveXObject('Msxml2.XMLHTTP.5.0');
    } catch (e) {
        try {
            xmlhttp = new ActiveXObject('Msxml2.XMLHTTP.4.0');
        } catch (e) {
            try {
                new ActiveXObject('Msxml2.XMLHTTP');
            } catch (e) {
                try {
                    new ActiveXObject('Microsoft.XMLHTTP')}catch (e) {}
                }
            }
        }
    }
    if (xmlhttp) {
        return xmlhttp;
    } else {
        return null;
    }
}

function checkLogin()
{
    var currentUrl = window.location.href;
    $.loginURL = UCENTERURL + '/login.html?referer=' + escape(currentUrl);
    $.cookieURL = UCENTERURL + 
        '/inside.php?t=json&m=login&f=thirdlogin&jsoncallback=?';
    if (!!$.cookie('_TOKEN') == false) {
        renderLoginToken();
    }

}

function renderLoginToken()
{
    $.getJSON($.cookieURL, function(data) {
        if (!!data && !!data._TOKEN) {
            $.cookie('_TOKEN', data._TOKEN, {path: '/'});
            window.location.reload(true);
        } else {
            if (window.top != window) {
                sendMessageToGateway({toLogin: 1});
            } else {
                window.location.href = $.loginURL;
            }
            return false;
        }
    });
}

function getCookie(name)
{
    var arr, reg = new RegExp('(^| )' + name + '=([^;]*)(;|$)');

    if (arr = document.cookie.match(reg))

        return (arr[2]);
    else
        return null;
}


