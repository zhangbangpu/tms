//$ips.include([$ips.appPath + "js/plugin/jquery-uploadify/jquery.uploadify.js",
//              $ips.appPath + "js/plugin/jquery-uploadify/uploadify.css"]);
//$ips.include($ips.appPath + "js/hui/hui_upload/upload.css");
		
(function( $ ) {

$.widget( "hui.upload", {
	options: {
		// 默认参数
		buttonText: "选择文件",
		onSuccess: false,		// 上传成功回调函数
		onError: false,			// 上传失败回调函数
		module : "import",
		method : "upload"
	},

	_create: function() {
		var opts = this.options;
		
		var e = this.element;
		var label = e.parents("label");
		label.attr("for", "file").addClass("input-file");
		e.wrap('<div class="button"></div>').after("<span>浏览</span>");
		label.append('<input type="text" placeholder="选择文件..." readonly="readonly">');
		label.append('<div class="progress"><div class="progress-bar bg-color-blue" style="width: 0%;">0%</div></div>');
		label.children("div.progress").hide();
    	e.change(function() {
    		label.children("input").val(e.val());
		});
	},
	
	submit: function() {
		var opts = this.options;
		var e = this.element;
		var from = e.parents("form");
		var label = e.parents("label");
		var btn = label.find("div.button span");
		var progress = label.children("div.progress");
		var bar = progress.children("div");
		from.ajaxSubmit({
			url: $ips.getApiurl() + "?t=json&m=" + opts.module + "&f=" + opts.method, 
			dataType:  'json',
			beforeSend: function() {
        		var percentVal = '0%';
        		bar.width(percentVal);
        		bar.html(percentVal);
				btn.text("上传中...");
				progress.show();
    		},
    		uploadProgress: function(event, position, total, percentComplete) {
        		var percentVal = percentComplete + '%';
        		
        		bar.width(percentVal);
        		bar.html(percentVal);
    		},
			success: function(data) {
        		var percentVal = '100%';
        		
        		bar.width(percentVal);
        		bar.html(percentVal);
				btn.text(opts.buttonText);
				progress.hide();
				
				if (opts.onSuccess)
					opts.onSuccess(data);
			},
			error:function(xhr) {
				bar.width('0')
				btn.text("上传失败");
				log.debug(xhr.responseText);
				
				if (opts.onError)
					opts.onError(data);
			}
		});
	}
});

})( jQuery );