var urlParam = $ips.getUrlParams();
var orderid = urlParam.id, sdatetime = '', pdatetime = '---';
var reviewParams = [];

/**
 * 订单详情
 */

$(function () {
    //$ips.lockPage();
	
    $ips.load('orders', 'queryOneById', {id: orderid}, function (data) {
        sdatetime = data.sdatetime ? data.sdatetime : '---';
        pdatetime = data.rdatetime ? data.rdatetime : '---';
        if (!data) {
            $ips.error('没有查询到订单信息');
        }
        //回放
        //getReviewInfo(data.steps);

        fillFields(data.baseInfo);
        // 填充货品
        createGoodLists(data.baseInfo.goods);
        // 生成时间段
        createTimeLine(data.baseInfo, data.dispatchInfos, data.steps);
        // 获取事件
        createEvent(data.steps);

        //$ips.unLockPage();

        $('#eventContent').jarvismenu({
            accordion : true,
            speed : $.menu_speed,
            closedSign : '<em class="fa fa-expand-o"></em>',
            openedSign : '<em class="fa fa-collapse-o"></em>',
            position: 'before'
        });

        dealLiHtmlCss($('#eventContent'));
    })
})

function dealLiHtmlCss(liListContainer){
    if(liListContainer == '' || liListContainer == undefined || typeof(liListContainer) != 'object')return;
    var liList = liListContainer.find('li');
    if(liList.length>1){
        for(var i = 0,maxLen = liList.length;i<maxLen;i++){
            var ulHtml = $(liList[i]).find('ul');
            if(ulHtml.length>0){
                for(var n= 0,len=ulHtml.length;n<len;n++){
                    dealLiHtmlCss($(ulHtml));
                }
            }else{
                for(var m= 0,max=liList.length;m<max;m++){
                    $(liList[i]).css('background-color','#f9f9f9');
                }
                $(liList[liList.length-1]).parent().parent().css('background-color','#f9f9f9');
            }
        }

    }

}

function createGoodLists(goods) {
    var goodsHtml = '';
    if (goods) {
        $.each(goods, function (k, v) {
            goodsHtml += '<tr>';
            goodsHtml += '    <td>' + v.goodname + '</td>';
            goodsHtml += '    <td>' + v.unit + '</td>';
            goodsHtml += '    <td>' + v.number + '</td>';
            goodsHtml += '    <td>' + v.weight + '</td>';
            goodsHtml += '    <td>' + v.volume + '</td>';
            goodsHtml += '</tr>';
        })
    }
    $('#goodsHtml').html(goodsHtml);
}

function fillFields(data) {
    $.each(data, function (k, v) {
        $('#' + k).html(v);
    });
}

function createTimeLine(order, dispatchs, steps) {
    var statusHtml = '', planHtml = '', standHtml = '';

    var dispatchStatus = getDispatchStatus(dispatchs);

    if (dispatchStatus.started == false) {
        statusHtml += '<li>';
        statusHtml += '<div class="txt pull-left">';
        statusHtml += '<span class="onOverHide">未开始</span>';
        statusHtml += '</div>';
        planHtml += '<li>---</li>';
        standHtml += '<li>---</li>';
    } else {

        var dispatchsHtml = createDispatchsHTML(dispatchs);
        statusHtml = dispatchsHtml.statusHtml;
        planHtml = dispatchsHtml.planHtml;
        standHtml = dispatchsHtml.standHtml;

        if(dispatchStatus.finished){
           var stepsHtml = createStepsHTML(steps);
           statusHtml += stepsHtml.statusHtml;
           planHtml += stepsHtml.planHtml;
           standHtml += stepsHtml.standHtml;
        }
    }
	
	if(order.signstatus){
       var signHtml = createSignHTML();
       statusHtml += signHtml.statusHtml;
       planHtml += signHtml.planHtml;
       standHtml += signHtml.standHtml;
	}

    $("#status").html(statusHtml);
    $("#plantime").html(planHtml);
    $("#standtime").html(standHtml);
    addWidth();
}

function getDispatchStatus(dispatchs){
   var started = false;
   var finished = true;

   for(var i =0; i < dispatchs.length; i ++){
       if(dispatchs[i].started){
	      started = true;
	   }
	   if(!dispatchs[i].started){
	      finished = false;
	   }
   }

   return { started: started, finished: finished };

}

function createDispatchsHTML(dispatchs){
       var statusHtml = '';
	   var planHtml = '';
	   var standHtml = '';
	   
	   var dispatchNames = {unhandled: '未处理', implemented: '已配载', inssued: '已调度'};

	   var statusCount = dispatchs.length;

	   var preStatusStarted = true;
        $.each(dispatchs, function (k, v) {

			 if(preStatusStarted == false){
			      return;
			 }
            // 时间轴              
			  statusHtml += v.started ? 
				            '<li class="done">' : '<li>';
			  if(k != 0){
				  statusHtml +=  '<div class="dash pull-left"></div>';
			  }
			  statusHtml +=    '<div class="txt pull-left">';
			  statusHtml +=       '<i class="iconfont fa fa-check-circle-o pull-left gou"></i>';
			  statusHtml +=       '<span class="onOverHide">' + dispatchNames[v.name] + '</span>';
			  statusHtml +=    '</div>';    
			  statusHtml += '</li>';

			  // 实际时间
			  var starttime = v.starttime ? v.starttime : '---';
				// 计划时间
			  var pstarttime =  '---';
			   
			  planHtml +=  '<li>' + pstarttime + '</li>';      
		
			  standHtml += ( v.started ? '<li class="done">' : '<li>' ) + starttime + '</li>';

			  preStatusStarted = v.started;
				
        });

       return { statusHtml: statusHtml, planHtml: planHtml, standHtml: standHtml};
}

function createSignHTML(){
       var statusHtml = '';
	   var planHtml = '';
	   var standHtml = '';
	   
            // 时间轴              
			  statusHtml +=	'<li class="done">';
			  statusHtml +=	   '<div class="dash pull-left"></div>';
			  statusHtml +=    '<div class="txt pull-left">';
			  statusHtml +=       '<i class="iconfont fa fa-check-circle-o pull-left gou"></i>';
			  statusHtml +=       '<span class="onOverHide">' + '已签收' + '</span>';
			  statusHtml +=    '</div>';    
			  statusHtml += '</li>';

			  var pstarttime =  '---';
			   
			  planHtml +=  '<li>' + pstarttime + '</li>';      
		
			  standHtml += '<li class="done">' + pstarttime + '</li>';


       return { statusHtml: statusHtml, planHtml: planHtml, standHtml: standHtml};
}

function createStepsHTML(steps){
       var statusHtml = '';
	   var planHtml = '';
	   var standHtml = '';
	   
	   var statusCount = steps.length;

	   for(var k = 0; k < statusCount; k ++){
        //$.each(steps, function (k, v) {
            var v = steps[k];
			// 时间轴
            if (v.status >= 1) {
                statusHtml += '<li class="done">';
               // if (k != 0) {
                    statusHtml += '<span class="dash pull-left"></span>';
               // }
                statusHtml += '<div class="txt pull-left">';
                statusHtml += '<i class="iconfont fa fa-check-circle-o pull-left gou"></i>';
                statusHtml += '    <span class="onOverHide" style="width:auto">' + v.aliasname + '已发车</span>';
                statusHtml += '</div>';
                if (v.status == 1) {
                    //增加完成节点
                    statusHtml += '<li>';
                    statusHtml += '<span class="dash pull-left"></span>';
                    statusHtml += '<div class="txt pull-left">';
                    statusHtml += '    <span class="onOverHide">' + v.aliasname + '已到达</span>';
                    statusHtml += '</div>';
                } else {
                    statusHtml += '<li class="done">';
                    statusHtml += '<span class="dash pull-left"></span>';
                    statusHtml += '<div class="txt pull-left">';
                    statusHtml += '<i class="iconfont fa fa-check-circle-o pull-left gou"></i>';
                    statusHtml += '    <span class="onOverHide">' + v.aliasname + '已到达</span>';
                    statusHtml += '</div>';
                }
            } else {
                statusHtml += '<li>';
               // if (k != 0) {
                    statusHtml += '<div class="dash pull-left"></div>';
                //}
                statusHtml += '<div class="txt pull-left">';
                /*if( k == 0 ){
                 statusHtml += '<span class="onOverHide">'+ v.aliasaliasname +'发车</span>';
                 }else{*/
                statusHtml += '<span class="onOverHide">' + v.aliasname + '</span>';
                //}
                statusHtml += '</div>';
            }
            statusHtml += '</li>';

            // 实际时间
            var gstarttime = v.gstarttime ? v.gstarttime : '---';
            var garrivetime = v.garrivetime ? v.garrivetime : '---';
            // 计划时间
            var pstarttime = v.pstarttime ? v.pstarttime : '---';
            var parrivetime = v.parrivetime ? v.parrivetime : '---';
            // 如果只有一个状态 并且showtime
            if (statusCount == 1) {
                planHtml += '<li>' + sdatetime + '</li>';
                if (v.status > 0) {
                    if (v.showTime) {
                        planHtml += '<li>' + pdatetime + '</li>';
                    } else {
                        planHtml += '<li>---</li>';
                    }
                }
            } else {
                if (k == 0) {
                    planHtml += '<li>' + sdatetime + '</li>';
                } else {
                    planHtml += '<li>---</li>';
                }
            }
            if (v.status >= 1) {
                if (v.status == 2) {
                    // 任务结束
                    if (statusCount > 1) {
                        if (v.showTime) {
                            planHtml += '<li>' + pdatetime + '</li>';
                        } else {
                            planHtml += '<li>' + parrivetime + '</li>';
                        }
                    }
                   // if (parseInt(v.implementstatus) == 0) {
                        standHtml += '<li class="done">' + gstarttime + '</li>';
                   // } else {
                   //     standHtml += '<li class="done_red">' + gstarttime + '</li>';
                   // }
                    standHtml += '<li class="done">' + garrivetime + '</li>';
                } else {
                    // 任务开始
                    if (statusCount > 1) {
                        if (v.showTime) {
                            planHtml += '<li>' + pdatetime + '</li>';
                        } else {
                            planHtml += '<li>' + parrivetime + '</li>';
                        }
                    }
                   // if (parseInt(v.implementstatus) == 0) {
                        standHtml += '<li class="done">' + gstarttime + '</li>';
                    //} else {
                    //    standHtml += '<li class="done_red">' + gstarttime + '</li>';
                    //}
                    standHtml += '<li>---</li>';
                }
            } else {
                // 任务未下发
                standHtml += '<li>' + garrivetime + '</li>';
            }

           //前一阶段未完成，即使下一阶段已开始也不再显示
			if(v.status != 2){
			    break;
			}
        }

      return { statusHtml: statusHtml, planHtml: planHtml, standHtml: standHtml};
}

function createEvent(steps) {
    if (!steps) return;
    var eventHtml = '';
    $.each(steps, function (k, v) {
        eventHtml += '<li class="">';
        eventHtml += '        <a class="" href="#" style="">【' + v.aliasname + '阶段】</a>';
        eventHtml += '<ul style="display: none;" class="">';
        $.each(v.departures, function (dk, dv) {
            eventHtml += '<li class="">';
            eventHtml += '        <a class="" href="#"  style="">【' + dv.carnum + '、' + dv.drivername + '】</a>';
            if (dv.gstarttime != '') {
                eventHtml += '<span class="btn btn-xs pull-width80 btn-warning" style="margin-left: 50px;" href="javascript:;" title="播放" id="play_' + dv.carnum + dv.implementstep + '" onclick="' + 'reviewByCarnum(\'' + dv.carnum+dv.implementstep + '\')">';
                eventHtml += '<i class="fa fa-play"></i>';
                eventHtml += '</span>';
            }
            eventHtml += '<ul style="display: none;" class="">';
            if(dv.monitorList){
                $.each(dv.monitorList, function (mk, mv) {
                	if ( mv.detail  != null ){
                		eventHtml += createOneEvent(mv.type, mv.detail);
                	}
                });
            }
            eventHtml +='</ul>'
            eventHtml += '</li>';
        });
        eventHtml += '</li>';
        eventHtml += '</ul>';


    })
    $('#eventContent').html(eventHtml);
}

function reviewByCarnum(carnum) {
    sendMessageToGateway({pageScrollTo : 0});
    imap.getModule('review').reViewByCarnum(carnum);
}


function createOneEvent(type, details) {
    var eventHtml = '';
    if (details.length > 0) {
        $.each(details, function (dk, dv) {
            if (dv.type == 'statusLog') {
                eventHtml += '<li class="">';
                eventHtml +=  getTypeName(type) + dv.time + ' ' + dv.name ;
                eventHtml += '</li>';
            }
            if (dv.type == 'eventLog') {
                eventHtml += '<li class="" style="background: scroll 0 0">';
                eventHtml +=  getTypeName(type) + dv.time + ' 在 ' + dv.address + '发生 ' + dv.name;
                eventHtml += '    <div class="margin-top-10">';
                if (dv.photo) {
                    $.each(dv.photo, function (imgk, imgv) {
                        eventHtml += '<img width="350" height="200" style="margin-left: 10px" alt="" src="' + imgv.photourl + '">';
                    })
                }
                eventHtml += '    </div>';
                eventHtml += '</li>';
            }
            if (dv.type == 'classlineLog') {
                eventHtml += '<li class="">';
                if (dv.itime) {
                    eventHtml +=getTypeName(type) + ' 在 ' + dv.itime + ' 驶入 ' + dv.sitmane ;
                }
                if (dv.otime) {
                    eventHtml +=  getTypeName(type) + ' 在 ' + dv.otime + ' 离开 ' + dv.sitmane;
                }
                eventHtml += '</li>';
            }
        });
    }
    return eventHtml;
}

//给时间轴添加宽度
function addWidth() {
    var stage_width = 0;
    var stage_index = 0;
    $("#status").each(function () {
        $(this).find("li").each(function () {
            ++stage_index;
        });
    })
    stage_width = stage_index * 254;
    $(".schedule").width(stage_width);
}

// 获取事件类型
function getTypeName(str) {
    switch (str) {
        case "zpt" :
            var name = '【司机宝APP】';
            break;
        case "classline" :
            var name = '【车载设备】';
            break;
    }
    return name;
}

function datetime_to_unix(datetime) {
    var tmp_datetime = datetime.replace(/:/g, '-');
    tmp_datetime = tmp_datetime.replace(/ /g, '-');
    var arr = tmp_datetime.split("-");
    var now = new Date(Date.UTC(arr[0], arr[1] - 1, arr[2], arr[3] - 8, arr[4], arr[5]));
    return parseInt(now.getTime() / 1000);
}

