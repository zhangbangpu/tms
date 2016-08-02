// 弹出窗口
(function($) {
	$.widget("hui.custormDialog",{
		options: {
			title: null,
			// 内容ID
			contentId: null,
			width: 600,
			height: "auto",
			buttons: []
		},
		
		_create: function(){
			$(this.options.contentId).hide();

			this.options.buttons = this._buildBtns(this.options.buttons);
			
			var self = this;
			$(this.element).bind('click', function (event) {
				if($(self.options.contentId).length > 0){
					self._buildHtmlBox();
					self._getDialog();
				}else{
					$ips.showError(2,['ContentID 未找到']);
				}
				event.preventDefault();
			});
		},
		
		//SHOW DIALOG
		_getDialog: function(){
			this._convertToHtml();

			$('#dialog_div').dialog({
				autoOpen : true,
				width : this.options.width,
				height: this.options.height,
				resizable : false,
				modal : true,
				title : "<div class='widget-header'><h4>" + this.options.title + "</h4></div>",
				buttons : this.options.buttons
			});
		},
		
		// BUILD BUTTIONS
		_buildBtns : function(btns){
			
			$.each(this.options.buttons, function(index, item){
				var callback = null;
				if (typeof item.click != 'undefined') {
					callback = item.click;
				}
				
				item.click = function(){
					if (callback != null) {
						callback();
					}
					$(this).dialog("close");
				}
				btns[index] = item;	
			});
			return btns;
		},
		
		// CONVERT DIALOG TITLE TO HTML
		_convertToHtml: function(){
			$.widget("ui.dialog", $.extend({}, $.ui.dialog.prototype, {
				_title : function(title) {
					if (!this.options.title) {
						title.html("&#160;");
					} else {
						title.html(this.options.title);
					}
				}
			}));
		},
		
		//ADD DIALOG DIV
		_buildHtmlBox: function(){
			var content = $(this.options.contentId).clone().html();
			var HtmlBox = '<div id="dialog_div"><p>' + content + '</p></div>';
			this.element.append(HtmlBox);
		}
	});
})(jQuery);