var urlParam = $ips.getUrlParams();
var waybillid = urlParam.id;


/**
 * 运单详情
 */

$(function () {
	
    $ips.load('tckNum', 'queryWaybillDetail', {id: waybillid}, function (data) {
    	console.log(data);
        if (!data) {
            $ips.error('没有查询到运单信息');
        }
        orderLists(data.ordersList);
        createGoodLists(data.goodsList);
    })
})

function orderLists(orders) {
    var ordersHtml = '';
    if (orders) {
        $.each(orders, function (k, v) {
            ordersHtml += '<tr align="center">';
            ordersHtml += '    <td width="100">' + v.code + '</td>';
            ordersHtml += '    <td>' + v.amount + '</td>';
            ordersHtml += '    <td>' + v.fromcode + '</td>';
            ordersHtml += '    <td width="100">' + v.fhaddress + '</td>';
            ordersHtml += '    <td width="100">' + v.shaddress + '</td>';
            ordersHtml += '    <td width="80">' + v.subcontractor + '</td>';
            ordersHtml += '    <td width="150">' + v.deptname + '</td>';
            ordersHtml += '    <td>' + v.weight + '</td>';
            ordersHtml += '    <td>' + v.volume + '</td>';
            ordersHtml += '    <td width="100">' + v.requstarttime + '</td>';
            ordersHtml += '    <td width="100">' + v.requendtime + '</td>';
            ordersHtml += '</tr>';
        })
    }
    $('#ordersHtml').html(ordersHtml);
}

function createGoodLists(goods) {
	var goodsHtml = '';
	if (goods) {
		$.each(goods, function (k, v) {
			goodsHtml += '<tr>';
			goodsHtml += '    <td>' + v.maktx + '</td>';
			goodsHtml += '    <td>' + v.matnr + '</td>';
			goodsHtml += '    <td>' + v.amount + '</td>';
			goodsHtml += '    <td>' + v.brgew + '</td>';
			goodsHtml += '    <td>' + v.volum + '</td>';
			goodsHtml += '    <td>' + v.fromcode + '</td>';
			goodsHtml += '</tr>';
		})
	}
	$('#goodsHtml').html(goodsHtml);
}



