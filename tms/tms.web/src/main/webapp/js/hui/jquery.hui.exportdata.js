/**
 * 导出控件
 * 通过代码生成器生成的模块，指定模块名【dataModule】后应该可以直接使用
 * 
 * <pre>
 * loadScript('js/hui/jquery.hui.exportdata.js', function () {
 *    $('#export').exportdata({dataModule : 'moduleName'});
 * });
 * </pre>
 * 
 * @author Farmer.Li <me@farmerli.com>
 */
(function($) {

$.widget( "hui.exportdata", {
    options: {
        // 生成导出文件 模块、方法
        module : "exportdata",
        method : "exec",
        // 进度获取 模块、方法
        progressModule : "exportdata",
        progressMethod : "getprogress",
        // 打包 模块、方法
        packModule : 'exportdata',
        packMethod : 'pack',
        // 数据 模块、方法
        dataModule : '',
        dataMethod : 'search',
        // 取数据总数 模块、方法 通过数据接口取到分页信息
        totalModule : 'exportdata',
        totalMethod : 'getTotal',
        // 文件下载配置
        downloadModule : 'exportdata',
        downloadMethod : 'download',
        // 分包数据条数
        partDataRows : 1000,
        // 搜索form Id
        searchForm : '#search-box',
        // 搜索参数 支持函数 此处设置了可以不设置 searchForm
        searchParams : null,
        // 进度条更新频率
        updateProgressTime : 3000,
        title: '导出'
    },

    _create: function() {
        var opts = this.options;
        if (opts.dataMethod == '') {
            throw new Error('dataMethod is null');
        }
        if (opts.dataModule == '') {
            throw new Error('dataModule is null');
        }
        var e = this.element;
        if ($('#exportdata-modal-container').length == 0) {
            this._createModal();
        }
        var self = this;
        $(e).bind('click', function () {
            self._execExport();
        });
        this._bindModalBtnEvent();
    },
    // bind modal 各按钮事件
    _bindModalBtnEvent : function () {
        var self = this,opts = this.options;
        // 下载
        $('body').delegate('button[data-file-src]', 'click', function () {
            if ($(this).hasClass('disabled')) {
                return ;
            }
            //window.location.href = $(this).attr('data-file-src');
            console.log(self._getDownloadUrl($(this).attr('data-file-src')));
            window.location.href = self._getDownloadUrl($(this).attr('data-file-src'));
        });
        // 打包下载
        $('#export-pacts-btn').bind('click', function () {
            if ($(this).hasClass('disabled')) {
                return ;
            }
            var file = $('this').attr('data-pack-file');
            if (typeof file == 'undefined') {
                if (self._checkAllPartDone() == false) {
                    return ;
                }
                var zipfile = $(this).attr('data-pack-file');
                if (typeof zipfile == 'undefined') {
                    var files = [];
                    $.each($('div[id^="export-part-"]').find('button'), function () {
                        files.push($(this).attr('data-file-src'));
                    });
                    zipfile = $ips.load(opts.packModule, opts.packMethod, {files : files});
                    $(this).attr('data-pack-file', zipfile);
                }
                //window.location.href = zipfile;   
                window.location.href = self._getDownloadUrl(zipfile);             
            }
        });
    },
    _getDownloadUrl : function (key) {
        return 'inside.php?t=json&m=' + this.options.downloadModule + '&f=' + this.options.downloadMethod + '&file=' + key
    },
    // 检查所有子包是否全部完成
    _checkAllPartDone : function () {
        var res = true;
        $.each($('div[id^="export-part-"]').find('button'), function () {
            if (typeof $(this).attr('data-file-src') == 'undefined') {
                res = false;
            }
        });
        return res;
    },
    // 执行导出
    _execExport : function () {
        var searchParams = this._getSearchParams();
        var total = this._getTotal(searchParams);
        if (total === false) {
            return ;
        }
        var parts = Math.ceil(total / this.options.partDataRows);
        $('#export-part-list').empty();
        $('#export-pacts-btn').addClass('disabled');
        for (var i = 1; i <= parts; i++) {
            this._doCreateFile(searchParams, i, parts, total);
        }
        $('#exportdata-modal-container').modal();
        this._updateProgress();
    },
    // 更新进度
    _updateProgress : function () {
        var opts = this.options, self = this;
        
        $.each($('[data-cache-key]'), function () {
            if (parseInt($(this).css('width')) == 100) {
                return ;
            }
            var key = $(this).attr('data-cache-key');
            
            setTimeout(function () {
                self._getProgress(self, opts.progressModule, opts.progressMethod, key, opts.updateProgressTime);
            }, opts.updateProgressTime);
        });
    },
    // 获取进度
    _getProgress : function (self, progressModule, progressMethod, cachekey, time) {
        $ips.load(progressModule, progressMethod, {progresscachekey : cachekey}, function (res) {
            var dom = $('[data-cache-key="' + cachekey + '"]');
            if (parseInt(res) >= 100) {
                res = 100;
                dom.closest('[id^="export-part-"]').find('button').removeClass('disabled');
                $('#export-pacts-btn').removeClass('disabled');
                if (self._checkAllPartDone() == true) {
                    $('#export-pacts-btn').removeClass('disabled');
                }
            }
            dom.css('width', res + '%');
            if (parseInt(res) < 100) {
                setTimeout(function () {
                    self._updateProgress(self, progressModule, progressMethod, cachekey, time);
                }, time);
            }

        });
        
    },
    // 执行创建下载文件
    _doCreateFile : function (searchParams, page, parts, total) {
        var opts = this.options;
        searchParams.push({name : 'pageNo', value: page});
        searchParams.push({name : 'pageSize', value: opts.partDataRows});
        if (page != 1) {
            $('<hr />').appendTo('#export-part-list');
        }
        var start = page * opts.partDataRows - opts.partDataRows + 1;
        var end = page * opts.partDataRows;
        if (end > total) {
            end = total;
        }
        var cacheKey = 'progresschache-' + (new Date().getTime()).toString() + '-' + page;
        $('<div class="row margin-top-5" id="export-part-' + page + '">\
            <div class="col col-10">\
                <label class="label part-title">数据包' + page + '（' + start+ '-' + end + '）</label>\
                <label class="select">\
                    <div class="progress progress-xs progress-striped active">\
                        <div class="progress-bar bg-color-blue" data-cache-key="' + cacheKey + '" role="progressbar" style="width: 0%"></div>\
                    </div>\
                </label>\
            </div>\
            <div class="col col-2">\
                <button type="button" class="btn btn-sm btn-success btn-block margin-top-10 disabled">下载</button>\
            </div>\
        </div>').appendTo('#export-part-list');
        searchParams.push({name : 'progresscachekey', value: cacheKey});
        $ips.load(opts.module, opts.method, searchParams, function (res) {
            $('[data-cache-key="' + cacheKey + '"]').closest('div[id^="export-part-"]').find('button').attr('data-file-src', res);
        });
    },
    // 获取当前的搜索参数
    _getSearchParams : function () {
        var opts = this.options;
        if (opts.searchParams != null) {
            if (typeof opts.searchParams == 'function') {
                var params = opts.searchParams();
            } else {
                var params = opts.searchParams;
            }
        } else {
            var params = $(opts.searchForm).serializeArray();
            $.each($(opts.searchForm).find('[name*="time"]'), function () {
                var times = $(this).val().split(" - ");
                var name = $(this).attr('name');
                params.push({name: name + 'Ge', value: times[0]}); 
                params.push({name: name + 'Lt', value: times[1]}); 
            });
        }

        params.push({name : 'dataModule', value: opts.dataModule});
        params.push({name : 'dataMethod', value: opts.dataMethod});

        return params;
    },
    // 获取数据总数
    _getTotal : function (searchParams) {
        var opts = this.options;
        var res = $ips.load(opts.totalModule, opts.totalMethod, searchParams);
        return res;
    },

    // 创建 modal
    _createModal : function () {
        var opts = this.options,
            title = opts.title || '导出';
        var a = $('<div class="modal fade" id="exportdata-modal-container" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false">\
    <div class="modal-dialog">\
        <div class="modal-content">\
            <div class="modal-header">\
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">\
                    &times;\
                </button>\
                <h4 class="modal-title" id="myModalLabel">'+title+'</h4>\
            </div>\
            <div class="modal-body">\
                <form class="smart-form" action="">\
                    <fieldset class="no-padding">\
                        <div class="col-xs-12 no-padding" id="export-part-list">\
                        </div>\
                    </fieldset>\
                </form>\
            </div>\
            <div class="modal-footer">\
                <button type="button" class="btn btn-primary disabled" id="export-pacts-btn">打包下载</button>\
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>\
            </div>\
        </div>\
    </div>\
</div>').appendTo('body');
    }
});

})( jQuery );