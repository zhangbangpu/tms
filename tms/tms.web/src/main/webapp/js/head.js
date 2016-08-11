if (!window.location.origin)
   window.location.origin = window.location.protocol+"//"+window.location.host;
window.onload = function ()
{
    resourceUrl = getResourceUrl();
}
RESOURCEURL = getResourceUrl();

var initJs = [
    RESOURCEURL+'js/libs/jquery-2.0.2.min.js',
    RESOURCEURL+'js/libs/jquery-ui-1.10.3.min.js',
    RESOURCEURL+'js/yepnopejs/yepnope.1.5.4-min.js',
    RESOURCEURL+'js/bootstrap/bootstrap.min.js',
    RESOURCEURL+'js/notification/SmartNotification.min.js',
    RESOURCEURL+'js/smartwidgets/jarvis.widget.min.js',
    RESOURCEURL+'js/plugin/easy-pie-chart/jquery.easy-pie-chart.min.js',
    RESOURCEURL+'js/plugin/sparkline/jquery.sparkline.min.js',
    RESOURCEURL+'js/plugin/jquery-validate/jquery.validate.js',
    RESOURCEURL+'js/plugin/masked-input/jquery.maskedinput.min.js',
    RESOURCEURL+'js/plugin/select2/select2.js',
    RESOURCEURL+'js/plugin/bootstrap-slider/bootstrap-slider.min.js',
    RESOURCEURL+'js/plugin/msie-fix/jquery.mb.browser.min.js',
    RESOURCEURL+'js/plugin/smartclick/smartclick.js'
];
for (var index in initJs) {
    loadFile(initJs[index]);
    loadFile(RESOURCEURL + 'js/ips.public.js');
    loadFile(RESOURCEURL + 'js/iframe.js');
}

var initCss = [
    RESOURCEURL+'css/bootstrap.min.css',
    RESOURCEURL+'css/smartadmin-production.css',
    RESOURCEURL+'css/smartadmin-skins.css',
    RESOURCEURL+'css/demo-increase.css',
    RESOURCEURL+'css/jquery.multiselect2side.css',
    RESOURCEURL+'css/demo.css'
];
for (var index in initCss) {
    loadFile(initCss[index], 'css');
}

function loadFile(file, type)
{
    if (type == 'css') {
        document.write('<link' + ' rel ="stylesheet" type="text/css" media="screen" href="'+ file +'">');
    } else {
        document.write('<scr' + 'ipt type="text/javascript" src="' + file + '"></scr' + 'ipt>');
    }
    
}   

function getResourceUrl() {
    var xmlhttp = getXmlHttp();
    if (xmlhttp == null ) {
        alert('获取资源地址失败');
        return '';
    }
    try {  
//        $ips.load("index", "getResourceUrl");
//         xmlhttp.open('GET', 'inside.php?t=json&m=index&f=getResourceUrl', false);
//         xmlhttp.send();
	    	var script = document.getElementsByTagName('script');
	        for (var i = 0; i < script.length; i++) {
	            var match = script[i].src.match(/head\.js($|\?.*)/);
	            if (match) {
	                return script[i].src.replace(/js\/head\.js.*/, '');
	            }
	        }
    	
         console.log(xmlhttp);
         if ( xmlhttp.status == 200 ) {
             return xmlhttp.responseText;
         }
         throw ''; 
    } catch(e) {
         return '';
    }
}

function getXmlHttp() {
    var xmlhttp=null;
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    }
    if (!xmlhttp&&window.ActiveXObject) {
    try {
        xmlhttp = new ActiveXObject("Msxml2.XMLHTTP.5.0")
    } catch(e) {
        try {
            xmlhttp = new ActiveXObject("Msxml2.XMLHTTP.4.0")
        } catch(e) {
            try {
                new ActiveXObject("Msxml2.XMLHTTP")
            } catch(e) {
                try{
                    new ActiveXObject("Microsoft.XMLHTTP")}catch(e){}
                }
            }
        }
    }
    if(xmlhttp){
        return xmlhttp;
    } else {
        return null;
    }
}
