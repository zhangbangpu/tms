var HUI_GRID_VERSION = '0.0.1';

(function( $ ) {

$.widget( "hui.grid", {
    tableContainer : null,
	options: {
        btnsSelector : {
            // 必须且只允许选择一个时可用
            onlyCheckedOne : '.btn-only-one',
            // 必须选择一个时可用
            mustCheckedOne : '.btn-must-one',
            // 必须且只允许选择多个时可用
            mustCheckedMulti : '.btn-must-multi'
        },
        libraryFiles : [
            'js/plugin/datatables/jquery.dataTables-cust.min.js',
            'js/plugin/datatables/ColReorder.min.js',
            'js/plugin/datatables/ColVis.min.js',
            'js/plugin/datatables/ZeroClipboard.js',
            'js/plugin/datatables/media/js/TableTools.min.js',
            'js/plugin/datatables/DT_bootstrap.js'
        ],
        // libary 加载完成的回调函数
        loadedLibaryCallback : null,
		// 默认参数
		"sPaginationType" : "bootstrap_full",
		"bServerSide" : true,
		"bFilter" : false,
		"aaSorting" : [],		// 默认排序
		"bAutoWidth": false,
		"bProcessing": true, 	// 是否启用进度显示，进度条等等，对处理大量数据很有用处
		"iDisplayLength": 10, 	// 每页显示数据量
		"oLanguage" : {"sUrl": $ips.appPath + "js/plugin/datatables/zh-cn.txt"},
		"fnDrawCallback": null,
		"sSwfPath" : $ips.appPath + "js/plugin/datatables/media/swf/copy_csv_xls_pdf.swf"
	},

	_create: function() {
        this.tableContainer = this.element;
        this._bindBtnEvent();
        this._switchBtn(this);
        //load dataTable
        this._loadTableLibary(this);
	},
	// 绑定按钮事件
    _bindBtnEvent : function () {
        var self = this;
        $(this.tableContainer).delegate(' input[type="checkbox"]', 'click', function () {
            self._switchBtn(self);
        });
    },
    // 加载 datatable 库
    _loadTableLibary : function (self) {
        var file = self.options.libraryFiles.shift();
        if (typeof file == 'undefined') {
            var dataTableOpts = self._margeDefault();
            // 用dataTable实现 
            self.element.dataTable(dataTableOpts);
            return ;
        }
    	loadScript(file, function () {
    		if (self.options.libraryFiles.length == 0) {
    			if (self.options.loadedLibaryCallback != null) {
    				self.options.loadedLibaryCallback();
    			}
    			var dataTableOpts = self._margeDefault();
		        // 用dataTable实现 
		        self.element.dataTable(dataTableOpts);
    		} else {
    			self._loadTableLibary(self);
    		}
    	});
    },
    // 切换按钮状态
    _switchBtn : function (self) {
        var total = self._getCleckedTotal();
        var btns = self.options.btnsSelector;
        if (total == 0) {
            self._disableBtn($(btns.onlyCheckedOne));
            self._disableBtn($(btns.mustCheckedOne));
            self._disableBtn($(btns.mustCheckedMulti));
        }
        if (total == 1) {
            self._useableBtn($(btns.onlyCheckedOne));
            self._useableBtn($(btns.mustCheckedOne));
            self._disableBtn($(btns.mustCheckedMulti));
        }

        if (total > 1) {
            self._useableBtn($(btns.mustCheckedMulti));
            self._useableBtn($(btns.mustCheckedOne));
            self._disableBtn($(btns.onlyCheckedOne));
        }
    },
    // 获取选择的总数
    _getCleckedTotal : function () {
        return $(this.tableContainer).find('input[type="checkbox"]:checked').not('.checkAll').length;
    },
    // disable button
    _disableBtn : function (dom) {
        $(dom).addClass('disabled');
    },
    // useable button
    _useableBtn : function (dom) {
        $(dom).removeClass('disabled');
    },
    _margeDefault : function () {
    	var callback = this.options.fnDrawCallback;
    	var self = this;
    	this.options.fnDrawCallback = function (oSettings) {
    		self._defaultFnDrawCallback(oSettings);
    		if (callback != null) {
    			callback(oSettings);
    		}
    	};
    	return this.options;
    },
    _defaultFnDrawCallback : function( oSettings ) {
		$(oSettings.nTable).find('tbody tr.even').css('backgroundColor', '#FFF');//偶数行加白色背景
		$(oSettings.nTable).find(".btn-group").slice(-5).addClass('dropup'); //列表中下拉菜单下拉
        $(oSettings.nTable).find(".btn-group").slice(0,4).removeClass('dropup');//列表中下拉菜单上翻

		var _tr_length = $(oSettings.nTable).find(".btn-group").length;
        if(_tr_length < 3){
			var _min_height = 330;
		}else{
			var _min_height = 450;
		}
        $(oSettings.nTable).parent(".dt-wrapper").css({
            'overflow': 'auto',
            'min-height': _min_height + 'px',
            'background-color': '#F9F9F9'
	    }); //列表外出现滚动条
        $(oSettings.nTable).css('width','100%'); //列表自适应宽度
        $(oSettings.nTable).find("th,td").css('white-space', 'nowrap'); //列表中字段不换行
        $(oSettings.nTable).find("th label input,td label input").next("span").css('margin-right', '0');
        L = $(oSettings.nTable).children("tr").length;
        if(L < 6){
            $(oSettings.nTable).find("tr:last td").css({
                "border": '1px solid #DDDDDD'
            });                 
        }
        var chk = $(oSettings.nTable).find(".checkAll");
        chk.click(function(event) {
            if ($(this).prop('checked')) {
            	 $(this).parents("table").find('tbody td input.checkbox').prop('checked', true);
            	 $(this).parents("table").find('tbody tr').not('.checkAll').addClass('active');
            	 $(this).siblings("span").removeClass('someone');
            } else {
            	 $(this).parents("table").find('tbody td input.checkbox').prop('checked', false);
            	 $(this).parents("table").find('tbody tr').not('.checkAll').removeClass('active');
                }
            });
            $(oSettings.nTable).find("td input.checkbox").click(function(event) {
                if (!$(this).prop('checked')) {
                    chk.siblings("span").addClass('someone');
                }
                checkStateAll = true;
                checkStateOne = false;
                $("tbody td input.checkbox").each(function(index, el) {
                    checkStateAll = checkStateAll && $(this).prop('checked');
                    checkStateOne = checkStateOne || $(this).prop('checked');
                });
                if (checkStateAll) {
                    checkStateOne = false;
                    chk.prop('checked', true);
                    chk.siblings("span").removeClass('someone');
                }
                if (checkStateOne) {
                    chk.prop('checked', true);
                    chk.siblings("span").addClass('someone');
                }
                if (checkStateAll === checkStateOne) {
                    chk.prop('checked', false);
                    chk.siblings("span").removeClass('someone');
                }
            });
            
            //单击选中checked
            $(oSettings.nTable).find("td").click(function(event){
            	var iptCBox = $(this).find("input.checkbox");
            	if(typeof iptCBox.attr('type') == 'undefined'){
            		
            		if ($(event.target).get(0).tagName == 'A'){
                		event.stopPropagation();
                		return true;
                	}
            		
            		var cbox = $(this).closest("tr").find("input.checkbox");
            		
            		if(typeof cbox.attr('type') != 'undefined'){
            			if (cbox.prop('checked')) {
                            $(this).closest("tr").removeClass('active');
                        } else {
                            $(this).closest("tr").addClass('active');
                        }
                    	cbox.click();
            		}
            	}
                	
            });
            
            validateButtonsPrivilege();
        },
        /**
         * 重新加载数据并翻到指定页码
         */
        fnPageChange: function(oSettings, mAction) {
            var e = this.element;
            e.fnPageChange(oSettings, mAction);
        },
        /**
         * 重新加载数据并重绘
         */
        fnDraw: function() {
            this.element.fnDraw();
        },
        setOptions: function(p) {
            var e = this.element;
            e.flexOptions(p);
        },
        getSort: function(p) {
            this.element.getSort();
        },
        /** 获取选中的行，返回选中行的主键 */
        getCheckedRows: function() {
        var inp = $(this.tableContainer).find('input[type="checkbox"]:checked').not('.checkAll');
        var res = [];
        $.each(inp, function (index, item) {
            res.push($(item).val());
        })
        return res;
        },
        /** 选中指定ID的行 */
	setCheckedRows: function(ids, idprefix) {
        idprefix = typeof idprefix == 'undefined' ? '' : idprefix;
            for (i = 0; i < ids.length; i++) {
			var chk = $('#' + idprefix + ids[i], this.element);
                chk.attr('checked', 'checked');
            }
        },
        /**
         * 获取或设置单元格的值
         */
        cells: function(col, row, value) {
            return this.element.cells(col, row, value);
        },
        /**
         * 手动添加数据
         */
        addData: function(data) {
        },
        /**
         * 导出表格数据 
         * @param type	导出类型：pdf、xls、csv
         * @param fileName 文件名
         */
        save: function(type, fileName) {
            var e = this.element;
            var movieId = this.element.movieId || false;
            if (!movieId) {
                var id = e.attr("id");
                movieId = 'ZeroClipboard_TableToolsMovie_' + id;
                var opts = this.options;
                var moviePath = opts.sSwfPath;
                var div = document.createElement('div');
                var style = div.style;
                style.position = 'absolute';
                style.left = '0px';
                style.top = '0px';
                style.width = '0px';
                style.height = '0px';

                e.parent().append(div);
                div.innerHTML = this._getHTML(id, movieId, opts.sSwfPath, 0, 0);

                this.element.movieId = movieId;
            }
            var movie = document.getElementById(movieId);
        },
        _getHTML: function(id, movieId, moviePath, width, height) {
            // return HTML for movie
            var html = '';
            var flashvars = 'id=' + id
            '&width=' + width +
                    '&height=' + height;

            if (navigator.userAgent.match(/MSIE/)) {
                // IE gets an OBJECT tag
                var protocol = location.href.match(/^https/i) ? 'https://' : 'http://';
                html += '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="' + protocol + 'download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=10,0,0,0" width="' + width + '" height="' + height + '" id="' + movieId + '" align="middle"><param name="allowScriptAccess" value="always" /><param name="allowFullScreen" value="false" /><param name="movie" value="' + moviePath + '" /><param name="loop" value="false" /><param name="menu" value="false" /><param name="quality" value="best" /><param name="bgcolor" value="#ffffff" /><param name="flashvars" value="' + flashvars + '"/><param name="wmode" value="transparent"/></object>';
            }
            else {
                // all other browsers get an EMBED tag
                html += '<embed id="' + movieId + '" src="' + moviePath + '" loop="false" menu="false" quality="best" bgcolor="#ffffff" width="' + width + '" height="' + height + '" name="' + movieId + '" align="middle" allowScriptAccess="always" allowFullScreen="false" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" flashvars="' + flashvars + '" wmode="transparent" />';
            }
            return html;
        },
        /**
         * 扩张全选
         */
        _checkAllOrNot: function(p) {
            var e = this.element;
            var ischeck = $(this).attr("checked");
            $('tbody tr', g.bDiv).each(function() {
                var chk = $("input.itemchk", this);
                if (chk.length == 0 || !chk[0].disabled) {
                    if (ischeck) {
                        $(this).addClass("trSelected");
                    } else {
                        $(this).removeClass("trSelected");
                    }
                }
            });
            $("input.itemchk", g.bDiv).each(function() {
                if (!this.disabled) {
                    this.checked = ischeck;
                    //Raise Event
                    if (p.onrowchecked) {
                        p.onrowchecked.call(this);
                    }
                }
            });
        }
    });

})(jQuery);
