/**
 * 树形菜单ui封装
 * add by dupeng at 2011/12/22 
 * via zTree[http://baby666.cn/hunter/zTree.html]
 */

(function($) {
$.widget("hui.tree", {

	_treeObj		: null,
	_isShow			: false,
	libFiles		: [
		$ips.appPath + 'js/plugin/zTree/js/jquery.ztree.all-3.5.min.js',
		$ips.appPath + 'css/zTreeStyle.css'
	],

	options			: {
		type			: 'normal',	// normal:正常DIV select:多选模式
        selectid		: null,		// 目标元素ID
		module			: 'tree',	// 模块
		method			: 'index',	// 方法
		params			: {},		// 请求参数
		async			: false,	// 异步请求
		callback		: null,      	// 回调函数
		onSuccess		: null		//加载成功 wangwei bug 3737 
	},

	/**
	 * 初始化
	 */
	_create			: function() {
		var self    = this;

        self._loadLib();
	},

	_loadLib 		: function(libFiles,fn) {
		var self = this;
		var libFiles = libFiles || self.libFiles;

		$ips.include([{
			load: libFiles,
			complete: function () {
				self._initplugin();
			}
		}]);
	},

	_initplugin 	: function() {
		var self    = this,
            opt     = self.options,
			that    = self.element;
		// 初始化zTree选项
		if(opt.type == 'select') {	// 多选框模式
			var selectid = opt.selectid || '__' + that.attr('id');
			self.options.selectid = selectid;

			// 初始化样式
			that.wrap('<div id="'+selectid+'_div" class="ui-dropdownchecklist ui-dropdownchecklist-hover"></div>');
			that.addClass('ui-dropdownchecklist-text');

			// 创建目标容器
			if(!$('#'+selectid).length) that.parent().append('<input type="hidden" id="'+selectid+'" />');
			var targ = $('<div id="'+selectid+'_target" style="display:none;position:absolute; border:1px solid #ccc;background-color:#FFFFFF"><ul id="'+selectid+'_target_tree" class="ztree" style="margin-top:0; max-height: auto; overflow-x: hidden; padding-right:25px"></ul></div>');
			$('body').append(targ);

			// 默认zTreeOptions
			self._zTreeOptions.check.chkboxType = {"Y":"s", "N":"s"};

			// 覆盖默认的zTreeOptions
			self._setzTreeOptions();

			// 生成zTree
			$ips.load(opt.module,opt.method,opt.params,function(result) {
				$.fn.zTree.init($("#"+selectid+'_target_tree'), self._zTreeOptions, result.data);
				self._treeObj = $.fn.zTree.getZTreeObj(selectid+'_target_tree');
				that.val(result.res[1]);
				$('#'+selectid).val(result.res[0]);
				if(self.options.onSuccess) {//wangwei bug 3737
					self.options.onSuccess();
				}
			});

			// 绑定事件
			$('#'+selectid+'_div').click(function() {
				if(that.val()!=''){
					that.val('');
				}
				self._showTarget();
			});
			$('#'+selectid+'_div').keyup(function(e) {
				if(13 == e.keyCode) {
					self.hideTarget(self,true);return;
				}
				if(that.val()!=''){
					self.hideTarget();
				}
			});

		} else {	// 正常DIV模式
			var selectid = opt.selectid || '__' + that.attr('id');
			self.options.selectid = selectid;

			// ul容器
			that.append('<ul id="'+selectid+'_target_tree" class="ztree" style="height: auto;"></ul>');


			// 默认zTreeOptions
			self._zTreeOptions.callback.onCheck = function() {
				var result = self._getResult(self._treeObj.getCheckedNodes());
				$('#'+selectid).val(result[0]);
			}

			// 覆盖默认的zTreeOptions
			self._setzTreeOptions();

			// 生成zTree
			$ips.load(opt.module,opt.method,opt.params,function(result) {
				$.fn.zTree.init(that.find('ul:first-child'), self._zTreeOptions, result.data);
				self._treeObj = $.fn.zTree.getZTreeObj(selectid+'_target_tree');
				$('#'+selectid).val(result.res[0]);
				if(self.options.onSuccess) {//wangwei bug 3737
					self.options.onSuccess();
				}

				var result = self._getResult(self._treeObj.getCheckedNodes());
				$('#'+selectid).val(result[0]);
			});
		}
	},

	/**
	 * select状态下,切换隐藏显示,隐藏的同时返回选择值
	 */
	_showTarget		: function() {
		var self	= this,
			that    = self.element,
			selectid = self.options.selectid,
			targ	= $('#'+selectid+'_target'),
			offset	= that.offset(),
			result = false;
		if(self._isShow) {
			targ.fadeOut("fast");
			result = self._getResult(self._treeObj.getCheckedNodes());
			if(self.options.callback) {
				self.options.callback(result);
			}
			that.val(result[1]);
			$('#'+selectid).val(result[0]);
			$("body").unbind("mouseup");
		} else {
			targ.css({left:offset.left + "px", top:offset.top + that.outerHeight() + "px"}).slideDown("fast");
			$("body").bind("mouseup", (function(s) { return function(e) { s.hideTarget(s,e); }})(self));
		}
		self._isShow = !self._isShow;
		return result;
	},

	hideTarget		: function(t,e) {
		var self		= t ? t : this,
			selectid	= self.options.selectid,
			targ		= $('#'+selectid+'_target');
		if(self._isShow) {
			if(e) {
				if(true === e) {
				} else if('undefined'!=typeof e.target) {
					var clickid = '__'+e.target.id;
					if(clickid.indexOf(selectid)>-1) {
						return;
					}
				} else {
					return;
				}
				var result = self._getResult(self._treeObj.getCheckedNodes());
				self.element.val(result[1]);
				$('#'+selectid).val(result[0]);
				if(self.options.callback) {
					self.options.callback(result);
				}
			}
			targ.fadeOut("fast");
			self._isShow = false;
		}
	},

	_getResult		: function(result) {
		var self = this,
			ids = names = '',
			sperator = '',
			len = result.length,
			count = 0;
		if(len) {
			for(var i = 0; i < len; i++) {
				ids += sperator + result[i].id;
				names += sperator + result[i].name;
				sperator = ',';
				count ++;
			}
		}
		return [ids,names,count];
	},

	_createMenuDiv	: function() {
	},

	_setzTreeOptions: function() {
		var self	= this,
			opt		= self.options;
		var huiOpts = ['type','selectid','module','method','params','async','callback'];
		$.each(self._zTreeOptions,function(key,value) {
			$.each(value,function(k,v) {
				if('undefined' != typeof(opt[k]) && $.inArray(k,huiOpts) == -1) {
					self._zTreeOptions[key][k] = opt[k];
				}
			});
		});
	},

	/**
	 * zTree选项
	 */
	_zTreeOptions	: {
		check: {
			enable: true,
			chkboxType: {"Y":"ps", "N":"ps"}
		},
		view: {
			dblClickExpand: false,
			addDiyDom: null,
			addHoverDom: null,
			removeHoverDom: null
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			beforeClick: null,
			onClick: null
		}
	}

});
})(jQuery);