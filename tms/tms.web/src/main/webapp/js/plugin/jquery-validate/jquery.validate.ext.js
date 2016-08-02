$.validator.addMethod("load", function(value, element, params) {
	if ( this.optional(element) )
		return "dependency-mismatch";

	var previous = this.previousValue(element);
	if ( previous.old !== value ) {
		previous.old = value;
		var ps = params.split('/');
		
		// 重新组装参数，替换#
		var p = '';
		if (ps[2]) {
			var ss = ps[2].split('&');
			for (i = 0; i < ss.length; i++) {
				var ss2 = ss[i].split('=');
				var v = ss2[1];
				if (v.charAt(0) == '#') v = $(v).val();
				p += ss2[0] + '=' + v + '&';
			}
		}
		p += element.name + '=' + value;
		previous.valid = $ips.load(ps[0], ps[1], p);
	} else if( this.pending[element.name] ) {
		return "pending";
	}
	return previous.valid;
}, "输入数据不合格");

(function($) {

$.extend($.validator.defaults, {
	errorPlacement: function(error, element) {
//		element.focus(function() {
//			if (element.hasClass('error')) {
//				var name = element.attr('name') || element.attr('id');
//				var label = element.next("label[for='" + name + "']");
//				if ( label.length == 0 ) error.insertAfter(element)
//				error.show();
//			}
//		});
		showMessage(error, element);
		element.focus(function() {
			showMessage(error, element);
		});
		element.blur(function() {error.hide()});
	}
});
  
})(jQuery);

function showMessage(error,element){
	if (element.hasClass('error')) {
		var name = element.attr('name') || element.attr('id');
		var label = element.next("label[for='" + name + "']");
		if ( label.length == 0 ) error.insertAfter(element)
		error.show();
	}
}