/**
 * IPS js公共文件 包含： 1、启动初始化 2、数据获取接口
 */
// 使用Minify合并js、css
var USE_MINIFY = false;
// 模块枚举常量
MOD_MENU = 1; // 菜单
MOD_HEADER = 2; // 头
MOD_FOOTER = 3; // 尾
MOD_MAP = 4; // 地图
MOD_VD = 5; // 表单验证
MOD_MABC = 6; // mapabc地图
MOD_DROPDOWN = 7; // 图片下拉
MOD_TIP = 8; // TIP弹出
MOD_TREE = 9; // 树形弹出
MOD_BAIDU = 10; // 百度地图 目前用于地解解析lyh
MOD_SEARCH = 11; // 快速搜索运单
MOD_MSG = 12; // 页面短消息提示

imap = {};

function Ips() {
    this.version = '0.1';
    this.appPath = this.getAppPath();
    this.menu = {};
    this.user = {};
    this.baidu = null, this.geocoder = null, 
        this.google = null, this.g_geocoder = null, 
        this.errormsg = '';
    this.scrollHeight = 0;
    this.options = {
        apiurl: this.appPath + 'inside.php',
        theme: 'default', // 主题所有目录
        // modules: [MOD_MENU, MOD_HEADER], // 默认加载的模块
        modules: [MOD_MENU, MOD_FOOTER, MOD_SEARCH, MOD_MSG], // 默认加载的模块
        remote: true,
        // ips.cn
        mapkey: 'ABQIAAAAxrHu5EISPYphVttcHp_FWBS7wQTQeJUu8' + 
            '10f3ZSHmg0TLMBKwhRbIXv0Wq8dXKka3JRe164NLlxvIw',
        mabckey: 'bbc104d078952ef35d1fdd94879a0d132cc0' + 
            '4ac2083577d05f272affdd5520601c2197f5b57146df'
    };

    // 主是所在路径
    this.themePath = this.appPath + '/themes/default/';
    this.webPath = this.themePath;
    try {
        this.cache = window['sessionStorage'] || {};
    } catch (e) {
        this.cache = {};
    }
    this.flag = {}; // 存一些标志位

    /**
     * 数据加载器
     *
     * @param  {[type]}   module     模块名，为空默认当前index。
     * @param  {[type]}   method     方法名，为空默认当前index。
     * @param  {[type]}   params     传给服务器数据，和$.ajax.data规则一样。
     * @param  {Function} callback   成功时的回调函数，不设置则使用阻塞模式，否则使用异步
     * @param  {[type]}   onerror    异常处理回调函数，不设置则使用默认提示
     * @param  {[type]}   remote     强制决定是否使用远程模式
     * @param  {[type]}   cached     是否使用客户端缓存，默认为false
     * @param  {[type]}   expiration 缓存失效时间，单位秒，默认为0不失效
     * @param  {[type]}   beforesend [description]
     * @this   {this}   this
     * 
     * @return {[type]}              [description]
     */
    this.load = function(
        module, method, params, callback, onerror, 
        remote, cached, expiration, beforesend) {
        module = module || 'index';
        method = method || 'index';
        params = params || {};
        callback = callback || false;
        onerror = onerror || false;
        remote = remote || this.options.remote;
        cached = cached || false;
        expiration = expiration || 0;
        beforesend = beforesend || false;
        var self = this,
            result = false;

        // 检查缓存
        var key = null;
        if (cached) {
            key = typeof params === 'string' ? params : $.param(params);
            key = module + '/' + method + '?' + key;
            result = self.cacheGet(key);
            if (result != null) {
                if (callback)
                    callback(result);
                return result;
            }
        }

        var purl = this.appPath + module;
        if(method){
        	purl += '/' + method;
        }
//        var purl = this.appPath + 'data/' + module + '/' + method + '.dat';
//        if (remote) {
//            purl = this.options.apiurl;
//            if (purl.indexOf('?') > -1)
//                purl += '&';
//            else
//                purl += '?';
//            purl += 't=json&m=' + module + '&f=' + method;
//        }
        $.ajax({
            async: callback ? true : false,
            timeout: 150000,
            type: 'POST',
            url: purl,
            data: params,
            processData: true,
            datatype: 'json',
//            beforeSend: function(XMLHttpRequest) {
//                XMLHttpRequest.setRequestHeader('x-csrf-token',$.cookie('_TOKEN'));
//                if (beforesend)
//                    beforesend();
//            },
            success: function(json) {
                if (typeof json === 'string') {
                    if (json.length < 1) {
                        $ips.unLockPage();
                        self.showError(2, ['json无效，数据为空']);
                        return;
                    }
                    try {
                        result = eval('(' + json + ')');
                    } catch (e) {
                        if (typeof console.log != 'undefined') {
                            console.log(json);
                        }
                        self.showError(2, ['服务器返回数据无法解析']);
                        sendMessageToGateway({
                            'HttpError': 500
                        });
                        return false;
                    }
                } else {
                    result = json;
                }

                // 解包
                if (result != null && result.code != 0) {
                    $ips.unLockPage();
                    if (onerror)
                        onerror(result);
                    else
                        self.showError(result.code, [result.message]);
                    result = false;
                } else {
                    //if (result.data) {
                    // 如果是page，则解包
                    // if (result.jsonh == 1 && $.isArray(result.data))
                    // {
                    // result.data = JSONH.unpack(result.data);
                    // } else if (result.data.jsonh == 1 &&
                    // $.isArray(result.data.result)) {
                    // result.data.result =
                    // JSONH.unpack(result.data.result);
                    // }
                    //}
                    if (callback)
                        callback(result.data);
                    // 缓存数据
                    if (cached)
                        self.cachePut(key, result.data, expiration);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                try {
                    if (XMLHttpRequest.status == 401) {
                        self.loadDataStatus = 401;
                        renderLoginToken();
                        return false;
                    }
                    if (typeof self.loadDataStatus != 'undefined' && 
                        self.loadDataStatus == 401) {
                        self.alert('正在验证登录状态...');
                        return false;
                    }

                    if (onerror) {
                        onerror(
                            {
                                'XMLHttpRequest' : XMLHttpRequest,
                                'textStatus' : textStatus,
                                'errorThrown' : errorThrown
                            }
                        );
                        return ;
                    }

                    if (XMLHttpRequest.status == 0) {
                        self.error('获取数据失败, 网络异常, 请检查网络或重试操作!');
                        return false;
                    }

                    if (typeof console.error != 'undefined') {
                        console.error(XMLHttpRequest, textStatus, errorThrown);
                    }
                    if (window.top != window) {
                        sendMessageToGateway({
                            'HttpError': XMLHttpRequest.status
                        });
                        return;
                    } else {
                        $ips.error('获取数据时发生错误, 错误编码: ' + XMLHttpRequest.status);
                    }
                    
                } catch (e) {}
            }
        });
        return result ? result.data : result;
    };

    this.getApiurl = function() {
        return this.options.apiurl;
    };

    /**
     * 存入缓存
     *
     * @param  {[type]} key        键
     * @param  {[type]} value      值
     * @param  {[type]} expiration 超时，不设置或<=0则永不超时，单位：秒
     * @this   {this}   this
     */
    this.cachePut = function(key, value, expiration) {
        if (key && value) {
            expiration = expiration || 0;
            var timestamp = Date.parse(new Date()) / 1000;
            var cacheItem = {
                time: timestamp,
                expiration: expiration,
                value: value
            };
            this.cache[key] = JSON.stringify(cacheItem);
        }
    };

    /**
     * 从缓存取值
     *
     * @param  {[type]} key [description]
     * @this   {this}   this
     * @return {[type]}     [description]
     */
    this.cacheGet = function(key) {
        var item = this.cache[key];
        if (item) {
            item = JSON.parse(item);
            var timestamp = Date.parse(new Date()) / 1000;
            if (item.value && (item.expiration <= 0 || 
                timestamp - item.time < item.expiration))
                return item.value;
        }
        return null;
    };

    /**
     * 清空缓存
     * @this   {this} this
     */
    this.cacheClear = function() {
        if (window['sessionStorage'])
            this.cache.clear();
    };

    /**
     * 删除指定key的缓存
     *
     * @param  {[type]} key [description]
     * @this   {this} this
     */
    this.cacheRemove = function(key) {
        if (window['sessionStorage'])
            this.cache.removeItem(key);
        else
            delete this.cache[key];
    };

    /**
     * 跳转到错误提示
     *
     * @param  {[type]} code     [description]
     * @param  {[type]} messages [description]
     * @this   {this} this
     */
    this.showError = function(code, messages) {
        if (code == 403 || code == 510) {
            setTimeout(function() {
                // var host = window.location.host;
                var params = 'url=' + window.location.pathname;
                if (!this.errormsg && code == 510)
                    this.errormsg = encodeURIComponent(
                        messages[0].substring(10, 19));
                if (this.errormsg)
                    params = 'msg=' + this.errormsg;
                $ips.locate('index', 'index', params);
            }, 1);
        } else if (code == 2) {
            var message = messages[0];
            message = message.replace(/com.alibaba.dubbo.rpc.RpcException: /,
             '');
            this.error(message);
        } else {
            this.growl('获取数据失败，请重试或联系管理员！');
            this.debug('获取数据失败，请重试或联系管理员！');
        }
        // 开启firebug时输出错误描述
        for (var i = 0; i < messages.length; i++)
            this.debug(messages[i]);
    };

    this.debug = function(obj) {
        if (typeof(console) != 'undefined' && 
            typeof(console.debug) != 'undefined')
            console.debug(obj);
    };

    this.goUrl = function() {

    };

    /**
     * 设置是否使用远程数据 
     *
     * @param  {[type]} remote [description]
     * @this   {[type]} this
     */
    this.setRemote = function(remote) {
        this.options.remote = remote;
    };

    /**
     * 启动初始化
     *
     * @param  {[type]} opts [description]
     * @this   {[type]} this
     */
    this.startup = function(opts) {
        $.extend(this.options, opts);
        var curModule = this.curModel();
        var thisUrl = document.URL;
        var urlAt = thisUrl.lastIndexOf('/');
        var thisUri = thisUrl.substr(urlAt + 1);
        var key = 'index_config';
        var that = this;
        // 先取缓存
        var result = this.cacheGet(key);
        if (!result || result == null || !result.user) {
            var extparams = {
                'model': curModule,
                'uri': thisUri
            };
            result = this.load('index', 'config', extparams);
        }
        if (result.initjs) {
            if (result.user) {
                this.user = result.user;
                // 得到用户信息，缓存
                if (result.user.id)
                    this.cachePut(key, result);
            }

            this.include(this.appPath + result.initjs + 
            '?timestamp' + (new Date()).getTime(), function() {
                if (window.loadPageScript)
                    loadPageScript();
            });
        }
        if (result.theme) {
            this.options.theme = result.theme;
            this.themePath = this.appPath + 'themes/' + result.theme + '/';
        }
        sendMessageToGateway({
            getTheme: 1
        });
    };

    /**
     * 获取当前用户
     *
     * @this   {[type]} this
     * @return {object} [description]
     */
    this.getCurrentUser = function() {
        var key = 'index_config';
        var cacheResult = this.cacheGet(key);
        var loadResult = this.load('index', 'config');

        if (!loadResult || loadResult == null || !loadResult.user) {
            return null;
        }
        if (cacheResult.user.id !== loadResult.user.id) {
            this.cachePut(key, loadResult);
        }
        return loadResult.user;
    };

    /**
     * 动态加载一个或一组js/css文件
     *
     * @param  {[type]}   urls     [description]
     * @param  {Function} callback [description]
     */
    this.include = function(urls, callback) {
        if ($.isArray(urls) && jQuery.type(urls[0]) === 'string') {
            urls = {
                load: urls,
                complete: callback
            };
        } else if (jQuery.type(urls) === 'string') {
            urls = {
                load: [urls],
                complete: callback
            };
        }

        yepnope(urls);
    };

    /**
     * 阻塞方式加载页面
     *
     * @param  {[type]} url [description]
     *
     * @this   {[type]} this
     * @return {[type]}     [description]
     */
    this.get = function(url) {
        return $.ajax({
            url: url,
            async: false
        }).responseText;
    };

    /**
     * 初始化地图，需要使用地图的页面手动调用
     * 
     * @this   {[type]} this
     */
    this.initMap = function() {
        $ips.include('http://ditu.google.cn/maps?hl=zh-CN&amp;' + 
            'file=api&amp;v=2&amp;sensor=true&amp;key=' + 
            this.options.mapkey);
    };
    
    this.flag.module = {};
    /**
     * 加载模块
     *
     * @param  {[type]} modules 不设置则加载options中的默认设置
     * @param  {[type]} usedef  同时载入缺省的模块
     * @this   {[type]} this
     */
    this.loadModules = function(modules, usedef) {
        modules = modules || this.options.modules;
        usedef = usedef || false;
        if (typeof modules === 'number')
            modules = [modules];
        if (usedef)
            modules = modules.concat(this.options.modules);

        var cache = this.flag.module;
        var files = new Array();

        for (var i = 0; i < modules.length; i++) {
            var module = modules[i];
            // 防止重复加载
            if (cache[module])
                continue;
            cache[module] = true;
            switch (module) {
                case MOD_MENU: // 菜单
            // files.push(this.appPath +
            // "js/jquery.mb.menu/inc/jquery.metadata.js");
            // files.push(this.appPath +
            // "js/jquery.mb.menu/inc/jquery.hoverIntent.js");
            // files.push(this.appPath + "js/jquery.mb.menu/inc/mbMenu.js");
                    files.push(this.appPath + 
                        'themes/default/js/common_nav.js');
                    break;
                case MOD_HEADER: // 头
                    var key = 'common_header';
                    var html = null; // this.cacheGet(key);
                    html = null;
                    if (html == null) {
                        html = this.get(this.themePath + 'common/header.html');
                        this.cachePut(key, html);
                    }
                    this.htmls.push(html);
                    break;
                case MOD_FOOTER: // 尾
                    var key = 'common_footer';
                    var html = this.cacheGet(key);
                    if (html == null) {
                        html = this.get(this.themePath + 'common/footer.html');
                        html = ['footer', html];
                        this.cachePut(key, html);
                    }
                    this.htmls.push(html);
                    break;
                case MOD_MAP: // 地图
                    this.google = true;
                    // css
                    document.write('<link type="text/css" rel="stylesheet" ' + 
                        'href="' + 
                        $ips.appPath + 'css/map_g_set.css" />');
                    files.push('http://ditu.google.cn/maps?hl=zh-CN&amp;' + 
                        'file=api&amp;v=2&amp;sensor=true&amp;key=' + 
                        this.options.mapkey);
                    break;
                case MOD_MABC: // mapabc地图
                    files.push('http://app.mapabc.com/apis' + 
                        '?&t=flashmap&v=2.4&key=' + 
                        this.options.mabckey);
                    break;
                case MOD_BAIDU: // baidu地图
                    this.baidu = true;
                    document.write('<link type="text/css" ' + 
                        'rel="stylesheet" href="' + 
                        $ips.appPath + 'css/map_g_set.css" />');
                    // files.push("http://api.map.baidu.com/api?v=1.3");
                    document.write('<link rel="stylesheet" type="text/css" ' + 
                        'href="http://api.map.baidu.com/res/13/bmap.css"/>');
                    document.write('<script type="text/javascript" ' + 
                        'src="http://api.map.baidu.com/getscript?v=' + 
                        '1.3&key=&services=&t=20121022090043"></script>');
                    break;
                case MOD_VD: // 表单验证
                    files.push(this.appPath + 
                        'js/validate/jquery.validate.js');
                    files.push(this.appPath + 
                        'js/validate/jquery.validate.ext.js');
                    files.push(this.appPath + 
                        'js/validate/messages_cn.js');
                    break;
                case MOD_DROPDOWN: // 图片下拉
                    files.push(this.appPath + 
                        'js/msdropdown/jquery.dd.js');
                    break;
                case MOD_TIP:
                    document.write('<link type="text/css" ' + 
                        'rel="stylesheet" href="' + 
                        $ips.appPath + 
                        'js/poshytip/tip-yellow/tip-yellow.css" />');
                    files.push(this.appPath + 
                        'js/poshytip/jquery.poshytip.min.js');
                    break;
                case MOD_TREE:
                    document.write('<link rel="stylesheet" href="' + 
                        $ips.appPath + 
                        'js/jquery.ztree/css/zTreeStyle/zTreeStyle.css"' + 
                        ' type="text/css">');
                    files.push(this.appPath + 
                        'js/jquery.ztree/js/jquery.ztree.core-3.0.js');
                    files.push(this.appPath + 
                        'js/jquery.ztree/js/jquery.ztree.excheck-3.0.min.js');
                    files.push(this.appPath + 
                        'js/jquery.ztree/js/jquery.ztree.exedit-3.0.min.js');
                    break;
                case MOD_SEARCH:
                    files.push(this.appPath + 
                        'themes/default/js/common_fast_search.js');
                    break;
                case MOD_MSG:
                    files.push(this.appPath + 
                        'themes/default/js/common_msg.js');
                    break;
            }
        }
        $ips.include(files);
    };

    /**
     * 根据两个经纬度数值计算距离
     *
     * @param  {[type]} latlng1 起始点位置
     * @param  {[type]} latlng2 终止点位置
     *
     * @return {[type]}
     */
    this.latlngDistance = function(latlng1, latlng2) {
        var flng = latlng1[1] * Math.PI / 180.0;
        var flat = latlng1[0] * Math.PI / 180.0;
        var slng = latlng2[1] * Math.PI / 180.0;
        var slat = latlng2[0] * Math.PI / 180.0;
        var r = Math.sin(flat) * Math.sin(slat);
        r += Math.cos(flat) * Math.cos(slat) * Math.cos(flng - slng);
        return Math.acos(r) * 6378137.0;
    };
    /**
     * 弹出提示消息
     *
     * @param  {[type]} _content [description]
     * @param  {[type]} _timeout [description]
     * @param  {[type]} _title   [description]
     * @param  {[type]} _icon    [description]
     * @param  {[type]} _color   [description]
     */
    this.growl = function(_content, _timeout, _title, _icon, _color) {
        _title = _title || '提示消息';
        _color = _color || '#296191';
        _icon = _icon || 'fa fa-bell';
        _timeout = _timeout || 5000;
        $.smallBox({
            title: _title,
            content: _content,
            color: _color,
            timeout: _timeout,
            icon: _icon
        });
    };
}

Ips.prototype = {
    /**
     * 返回APP所在的根路径, 用于加载资源文件
     *
     * @return {String}
     */
    getAppPath: function() {
        var script = document.getElementsByTagName('script');
        for (var i = 0; i < script.length; i++) {
            var match = script[i].src.match(/ips\.public\.js($|\?.*)/);
            if (match) {
                return script[i].src.replace(/js\/ips\.public\.js.*/, '');
            }
        }
    },
    /**
     * 
     */
    /**
     * 解析URL返回GET参数,可以手动传入如:aa=bb&cc=dd
     *
     * @param  {[type]}  purl        [description]
     * @param  {Boolean} isNoToLower [description]
     *
     * @return {Object}
     */
    getUrlParams: function(purl, isNoToLower) {
        isNoToLower = isNoToLower || false;
        var url = purl || window.location.href;
        var paraObj = {};
        if (url.indexOf('?') < 0) {
            return paraObj;
        }
        url = url.replace('#', '');
        var paraString = url.substring(url.indexOf('?') + 1, url.length)
        .split('&');
        if (!isNoToLower) {
            for (var i = 0; j = paraString[i]; i++) {
                paraObj[j.substring(0, j.indexOf('=')).toLowerCase()] = 
                j.substring(j.indexOf('=') + 1, j.length);
            }
        } else {
            for (var i = 0; j = paraString[i]; i++) {
                try {
                    paraObj[j.substring(0, j.indexOf('='))] = 
                    decodeURIComponent(j.substring(j.indexOf('=') + 
                        1, j.length));
                } catch (ex) {
                    paraObj[j.substring(0, j.indexOf('='))] = 
                    unescape(j.substring(j.indexOf('=') + 1, j.length));
                }
            }
        }
        return paraObj;
    },

    /**
     * 填充表单,匹配字段名字相同的值 {name:'xiaoming'} 匹配:name
     *
     * @param  {[type]} formid 表单ID,不能为空
     * @param  {[type]} params JSON格式,如: {name:'xiaoming',age:20} 
     *                         也可传String,如:aa=bb&cc=dd 
     * @param  {[type]} pre    前缀
     */
    fillFormInput: function(formid, params, pre) {
        if (!formid || !params)
            return;
        pre = pre || '';
        if (typeof(params) == 'string') {
            params = this.getUrlParams(params);
        }
        var checker = false;
        for (var i in params) {
            var v = params[i];
            if ($('#' + formid + ' input[name=' + i + ']').length) {
                if ($('#' + formid + ' input[name=' + i + ']')
                    .attr('type') == 'checkbox'
                ) {
                    checker = v == 1 ? true : false;
                    $('#' + formid + ' input[name=' + i + ']')
                    .attr('checked', checker);
                } else {
                    $('#' + formid + ' input[name=' + i + ']').val(v);
                }
            } else if ($('#' + pre + i).length)
                $('#' + pre + i).val(v);
        }
    },
    /**
     * 跳转另一个模块的页面
     */
    locate: function(module, method, params, blank) {
        module = module || 'index';
        method = method || 'index';
        params = params || false;
        var url = '#' + module + '/' + method + '.html';
        if (params)
            url += '?' + params;
        if (blank)
            window.open(url);
        else
            window.location.hash = url;
    },

    /**
     * 跳转到另一个系统模块的页面
     *
     * @param  {[type]} url   目标url
     * @param  {[type]} blank 是否在新页面打开，不填 在当前页面打开，true在 新页面打开
     */
    locatesubsystem: function(url, blank) {
        url = url || '';
        if ('' == url) {
            $ips.showError(2, ['跳转目标url不能为空']);
            return;
        }
        var url = '#' + url;
        if (blank)
            url += '&__blank__' + blank;
        sendMessageToGateway({
            toURL: url
        });
    },
    // 当前模块
    curModel: function() {
        var path = location.hash.replace(/^#/, '');
        var ps = path.split('/');
        if (ps.length < 2)
            return 'index';
        else
            return ps[ps.length - 2] || 'index';
    },
    // 当前方法
    curMethod: function() {
        var path = location.hash.replace(/^#/, '');
        var ps = path.split('/');
        if (ps.length < 2)
            ps = 'index.html';
        else
            ps = ps[ps.length - 1];
        var fs = ps.split('.');
        return fs[0] || 'index';
    },
    /**
     * 信息提示
     */
    alert: function(_content, callback, _title) {
        var btns = btns || '[确定]';
        this.confirm(_content, callback, _title, btns);
    },
    /**
     * 成功信息提示
     */
    succeed: function(_content, callback, _title) {
        _title = _title || '操作成功';
        btns = '[确定]';
        this.growl(_content, null, null, null, '#296191');
    },

    /**
     * 保存成功的信息提示
     */
    saved: function(_content, callback, _title, btns) {
        _content = _content || '保存数据成功！';
        _title = _title || '操作成功';
        btns = btns || '[返回列表][继续添加]';
        this.confirm(_content, callback, _title, btns);
    },

    /**
     * 失败信息提示
     */
    error: function(_content, callback, _title) {
        _title = _title || '操作失败';
        btns = '[确定]';
        this.growl(_content, null, null, null, '#C46A69');
    },

    /**
     * 询问信息
     */
    confirm: function(_content, callback, _title, btns) {
        _title = _title || '信息提示';
        btns = btns || '[确定][取消]';
        $.SmartMessageBox({
            title: _title,
            content: _content,
            buttons: btns
        }, callback);
        $('#Msg1').css({
            top: this.scrollHeight + 60
        });
    },

    /**
     * 地址解析
     *
     * @param  {[type]}   q          地址解析的地址，可经纬度字符串（格式：纬度,经度）
     * @param  {Function} callback   回调函数
     * @param  {[type]}   maptype    [description]
     * @param  {[type]}   centercity [description]
     */
    geo: function(q, callback, maptype, centercity) {
        var callback = callback || false;
        var result = {
            code: -1
        };
        var latlng = q.split(',');
        var centercity = centercity || '全国';
        if (this.geocoder == null)
            this.geocoder = new BMap.Geocoder();
        // 经纬度-->地址
        if (latlng.length == 2) {
            var _lat = Math.round((parseFloat(latlng[0]) + 0.0060) * 
                1000000) / 1000000;
            var _lng = Math.round((parseFloat(latlng[1]) + 0.0065) * 
                1000000) / 1000000;
            var pt = new BMap.Point(_lng, _lat);
            this.geocoder.getLocation(pt, function(data) {
                // log.debug(data);
                var address = '';
                result.total = 0;
                if (data) {
                    result.code = 0;
                    var addComp = data.addressComponents;
                    result.total = 1;
                    result.poilist = new Array();

                    var point = {};
                    point.name = data.address;
                    point.lat = _lat;
                    point.lng = _lng;
                    point.province = addComp.province;
                    point.city = addComp.city;
                    point.county = addComp.district;
                    address = data.address;
                    point.address = address;
                    result.poilist.push(point);
                }
                if (callback)
                    callback(result);
            }, {
                poiRadius: 1000,
                numPois: 1
            });
        } else { // 地址-->经纬度
            $ips.load('geo', 'search', {
                addr: q
            }, function(data) {
                var result = {
                    code: 0,
                    total: 0,
                    poilist: []
                };
                if (data) {
                    var point = {
                        lat: data.lat,
                        lng: data.lng,
                        address: data.address,
                        name: data.name,
                        province: data.province,
                        city: data.city,
                        county: data.county
                    };
                    result.total = 1;
                    result.poilist.push(point);
                    callback(result);
                    return;
                } else {
                    var gc = this.geocoder;
                    gc.getPoint(q, function(rs) {
                        if (rs) {
                            result.code = 0;
                            result.total = 1;
                            result.poilist = new Array();
                            var point = {};
                            point.lng = Math.round((parseFloat(rs.lng) - 
                                0.0065) * 1000000) / 1000000;
                            point.lat = Math.round((parseFloat(rs.lat) - 
                                0.0060) * 1000000) / 1000000;
                            gc.getLocation(rs, function(data) {
                                point.province = '';
                                point.city = '';
                                point.county = '';
                                if (data) {
                                    var addcom = data.addressComponents;
                                    point.address = data.address;
                                    point.name = q;
                                    point.province = addcom.province;
                                    point.city = addcom.city;
                                    point.county = addcom.district;
                                } else {
                                    point.address = '';
                                    point.name = q;
                                }
                                result.poilist.push(point);
                                if (callback)
                                    callback(result);
                            });
                        } else {
                            // log.debug();
                            callback(result);
            // var ops = {
            // renderOptions : {
            // autoViewport : false,
            // selectFirstResult : false
            // },
            // pageCapacity : 6,
            // onSearchComplete : function(rs) {
            // var poi = rs._pois;
            // result.code = 0;
            // result.total = poi.length;
            // result.poilist = new Array();
            // if (poi.length > 0) {
            // for (var i = 0; i < poi.length; i++) {
            // var _pp = poi[i].point;
            // var point = {};
            // point.lng = Math.round(
            // (parseFloat(_pp.lng) - 0.0065) * 1000000)
            // / 1000000;
            // point.lat = Math.round(
            // (parseFloat(_pp.lat) - 0.0060) * 1000000)
            // / 1000000;
            // point.province = poi[i].province;
            // point.city = poi[i].city;
            // point.county = poi[i].district;
            // var address = '';
            // var poiaddr = poi[i].address;
            // if (poiaddr.indexOf(poi[i].city) > -1)
            // point.address = poiaddr;
            // else {
            // if (point.province != point.city)
            // address = point.province + '' + point.city;
            // else
            // address = point.city;
            // point.address = address + '' + poiaddr;
            // }
            // point.name = poi[i].title;
            // result.poilist.push(point);
            // }
            // }
            // if (callback)
            // callback(result);
            // }
            // };
            // var myLocalsearch = new BMap.LocalSearch(centercity, ops);
            // myLocalsearch.clearResults();
            // myLocalsearch.search(q);
                        }
                    }, centercity);
                }
            });
        }

    },
    setGridHight: function() {
        // 高度自适应
        $(window).resize(function() {
            var gHeight = $(window).height();
            var hh = eval(gHeight - 283);
            if (hh < 200)
                hh = 200;
            $('.bDiv').css('height', hh + 'px');
        });
    },
    /**
     * 别名查询
     *
     * @param  {[type]} module     模块
     * @param  {[type]} method     方法
     * @param  {[type]} params     参数
     * @param  {[type]} mapping    对应关系
     * @param  {[type]} expiration 缓存有效期
     *
     * @return {void}
     */
    code2Text: function(module, method, params, mapping, expiration) {
        module = module || 'organ';
        method = method || 'find';
        mapping = mapping || {
            code: 'orgcode',
            text: 'name'
        };
        params = params || {};
        expiration = expiration || 0;
        params = $.param(params) + '&fields=' + 
            mapping.code + ',' + mapping.text;
        var key = module + '/' + method + '/' + params;
        result = this.cacheGet(key);
        if (result != null) {
            return result;
        }
        var data = this.load(module, method, params);
        this.cachePut(key, data, expiration);
        return data;
    },
    /**
     * 将文本框中的值转换成大写
     *
     * @param  {event} event 事件
     * @param  {object} obj  _self当前需要转换的对象
     */
    codeToUpper: function(event, obj) {
        var key = event.which;
        if (key >= 97 && key <= 122) { // 找到输入是小写字母的ascII码的范围
            event.preventDefault(); // 取消事件的默认行为
            var pos = this.getPosition($(obj)[0]);
            var _val = $(obj).val();
            $(obj).val(_val.substr(0, pos) + 
            String.fromCharCode(key - 32) + 
            _val.substr(pos, _val.length)); // 转换
        }
    },
    getPosition: function(obj) {
        var result = 0;
        if (obj.selectionStart || obj.selectionStart == 0) { // IE以外
            result = obj.selectionStart;
        } else { // IE
            var rng;
            if (obj.tagName == 'textarea') { // TEXTAREA
                rng = event.srcElement.createTextRange();
                rng.moveToPoint(event.x, event.y);
            } else { // Text
                rng = document.selection.createRange();
            }
            rng.moveStart('character', -event.srcElement.value.length);
            result = rng.text.length;
        }
        return result;
    },
    createScriptTag: function(url, success, error) {
        var scriptTag = document.createElement('script');
        scriptTag.setAttribute('type', 'text/javascript');
        scriptTag.setAttribute('src', url);
        scriptTag.onload = scriptTag.onreadystatechange = function() {
            if (
                (!this.readyState ||
                    this.readyState == 'loaded' ||
                    this.readyState == 'complete'
                ) && success
            ) {
                success();
            }
        };
        scriptTag.onerror = function() {
            if (error)
                error(data.url + ' failed to load');
        };
        var head = this.getHead();
        head.appendChild(scriptTag);
    },
    getHead: function() {
        return document.getElementsByTagName('head')[0] ||
            document.documentElement;
    },
    /*
     * 通过url 在传递日期到后台服务时，格式必须为：2013-02-25,对于月、日为个位数的，前面必须添加一位0
     * @param id : html标签的id名称
     */
    getUrlDate: function(id) {
        var time = $('#' + id).val();
        //对于没有时分秒的结束时间要加一天进行查询和导出
        var time1 = null;
        if (time != '') {
            var aa = new Date(Date.parse(time.replace(/-/g, '/')));
            aa.setDate(aa.getDate() + 1);
            time = aa;

            time1 = time.getFullYear().toString();
            if ((time.getMonth() + 1).toString().length == 1) {
                time1 += '-0' + (time.getMonth() + 1).toString();
            } else {
                time1 += '-' + (time.getMonth() + 1).toString();
            }
            if (time.getDate().toString().length == 1) {
                time1 += '-0' + time.getDate().toString();
            } else {
                time1 += '-' + time.getDate().toString();
            }
        }
        return time1;
    },
    getBaseCode: function(id) {
        return $('#' + id).val() == '' ? this.user.orgcode : $('#' + id).val();
    },
    // 表格数据加载统一封装函数
    gridLoadData: function(
        sSource,  aoData,  fnCallback, module, method, params, preProcess) {
        params = params || [];
        // 参数处理
        var data = {};
        $.each(aoData, function(i, item) {
            data[item.name] = item.value;
        });
        var sEcho = data['sEcho'] || 1;
        var iDisplayStart = data['iDisplayStart'] || 0;
        var iDisplayLength = data['iDisplayLength'] || 10;
        var fields = data['sColumns'] || null;
        if (fields == null)
            return;

        // 先转换为对象，方便后续处理
        var oParams = {};
        $.each(params, function(i, item) {
            oParams[item.name] = item.value;
        });

        // 分页参数
        oParams['pageNo'] = iDisplayStart / iDisplayLength + 1;
        oParams['pageSize'] = iDisplayLength;

        // 排序
        var fieldDefs = fields.split(',');
        var sortColumns = '';
        for (var i = 0; i < data['iSortingCols']; i++) {
            if (i > 0)
                sortColumns += ',';
            sortColumns += fieldDefs[data['iSortCol_' + i]] +
                ' ' + data['sSortDir_' + i];
        }
        if (sortColumns != '')
            oParams['sortColumns'] = sortColumns;

        // 加载数据及数据处理
        $ips.load(module, method, oParams, function(pager) {
            // 数据预处理
            if (preProcess)
                preProcess(pager);
            var aaData = new Array();
            $.each(pager.result, function(i, item) {
                var data = new Array();
                $.each(fieldDefs, function(j, key) {
                    data[j] = item[key] ? item[key] : '';
                });
                aaData.push(data);
            });
            var data = {
                'sEcho': sEcho,
                'iTotalRecords': pager.totalCount,
                'iTotalDisplayRecords': pager.totalCount,
                'aaData': aaData
            };
            fnCallback(data);
        });
    },
    /**
     * 锁定页面
     *
     * @param {string} message 消息文本
     * @param {object} css     css
     * @param {int}    timeout 自动解锁时间
     *
     * @return {void}
     */
    lockPage: function(message, css, timeout) {
        var o = {
            message: '<h4><i class="fa fa-repeat  fa-spin"></i> 正在加载……</h4>',
            css: {
                border: 'none',
                padding: '15px',
                backgroundColor: '#fff',
                '-webkit-border-radius': '10px',
                '-moz-border-radius': '10px',
                opacity: .8,
                color: '#000',
                top: '100px'
            }
        };
        if (message !== undefined && message != '') {
            o.message = message;
        }
        if (css !== undefined && typeof css == 'object') {
            o.css = css;
        }
        $.blockUI(o);
        if (timeout != undefined && timeout > 0) {
            setTimeout(this.unLockPage, timeout);
        }
    },
    /**
     * 解除锁定页面
     *
     * @return {void}
     */
    unLockPage: function() {
        //$.unblockUI();
    }
};

var $ips = new Ips();

// 运行启动初始化
var opts = {};

var host = window.location.host;
if (host == 'ips.9.com') {
    /**
     * opts.mapkey
     *
     * @type {String}
     */
    opts.mapkey = 'ABQIAAAAxrHu5EISPYphVttcHp_FWBSfPudfG5OFYjTa' +
        'xJ5t6VGk2NPvhRTyVeFPAQ9DCiNsQh-dLG1454Wb7A';
    /**
     * opts.mabckey
     *
     * @type {String}
     */
    opts.mabckey = '9f3ee75f1b93856234dac49e6de951a4d8d3ef85b' +
        '02b18002ed7e596c6b3c7284d2d7df093301c1d';
} else if (
    host == 'ipstest.huoyunren.com' || host == 'ipstest.huoyunren.com:8081'
) {
    // opts.mapkey =
    // "ABQIAAAAxrHu5EISPYphVttcHp_FWBTRI6LePoIIdamECkL0ct5Nq6jx5RRPeXaKEBX4aciVG86nhlDlqimeWA";
    // 8081
    /**
     * opts.mapkey
     *
     * @type {String}
     */
    opts.mapkey = 'ABQIAAAAxrHu5EISPYphVttcHp_FWBRLxqsOk' +
        'MOb4pnn565xqEL-30H23RQHmxSIEhAS586KIXHd5yYwogzRrw';
    /**
     * opts.mabckey
     *
     * @type {String}
     */
    opts.mabckey = '4bc6ab0e90c39be299e7e7ae71a842fedf41f6dd' +
        'd434b92bfcf3d832c6db94a16644742a7e6ae3cd';
} else if (host.indexOf('deppon.huoyunren.com') > -1) {
    // opts.mapkey =
    // "ABQIAAAAxT6j61Dz1Mr_n1ES5nEL9hTqHUJVLUcA2hrl2f19JJcStONFhxQCNTN9xJOg-nKVL5_vI4NH4kiPwQ";
    // huoyunren.com
    /**
     * opts.mapkey
     *
     * @type {String}
     */
    opts.mapkey = 'ABQIAAAAxT6j61Dz1Mr_n1ES5nEL9hSIxEKllCsRa7n6HEZ9' +
        'fQ-i_5KSoBS_u5pFjsSan3Cvd6QhclSXn8-loA';
    /**
     * opts.mabckey
     *
     * @type {String}
     */
    opts.mabckey = '09b3f18f740d6f0bc44491c170a47d90084a3cb6a95' +
        '6897995fd0664f0d85a5c377b627c7451b0bd';
} else if (host.indexOf('huoyunren.com') > -1) {
    // opts.mapkey =
    // "ABQIAAAAxT6j61Dz1Mr_n1ES5nEL9hTqHUJVLUcA2hrl2f19JJcStONFhxQCNTN9xJOg-nKVL5_vI4NH4kiPwQ";
    // huoyunren.com
    /**
     * opts.mapkey
     *
     * @type {String}
     */
    opts.mapkey = 'ABQIAAAAxrHu5EISPYphVttcHp_FWBQJs_GPdA1vC8REkc' +
        'FwpH2QCEo8thTTbWFw5R4reS3PJzid13GzFOIWDg';
    /**
     * opts.mabckey
     *
     * @type {String}
     */
    opts.mabckey = '09b3f18f740d6f0bc44491c170a47d90084a3cb6a95' +
        '6897995fd0664f0d85a5c377b627c7451b0bd';
}

/**
 * 屏蔽所有javascript错误
 *
 * @return {void} 无
 */
window.onerror = function() {
};



