var fileList = {
	index : $ips.appPath + 'themes/default/js/index.js'
};

function loadPageScript() {
	// 载入配置中的文件
	var page = $ips.curModel(); // 当前页面标识
	if ($ips.curMethod())
		page = page + "_" + $ips.curMethod();
	if (page) {
		var file = null;
		if (fileList[page])
			file = fileList[page];
		else
			file = $ips.themePath + 'js/' + page + '.js';
		//$.getScript(file);
		loadScript(file, null, true);
	}
};