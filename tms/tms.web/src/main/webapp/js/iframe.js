/**
 * 发送消息到gateway
 *
 * @param {object} obj 数据键值对
 *
 * @return {void}
 * @author Farmer.Li <me@farmerli.com>
 */
function sendMessageToGateway(obj)
{
    if (!obj) {
        return ;
    }
    window.parent.postMessage(JSON.stringify(obj), '*');
}

/**
 * 验证当前页面的包含资源ID button 权限，没有访问权限的将被隐藏
 *
 * @return {void}
 * @author Farmer.Li <me@farmerli.com>
 */
function validateButtonsPrivilege()
{
    var ids = [];
    $('[data-button-resource]').each(function () {
        ids.push($(this).attr('data-button-resource'));
    });
    if (ids.length == 0) {
        return false;
    }
    $ips.load('common', 'validatebuttons', {ids : ids}, function (res) {
        $.each(ids, function (index, item) {
            if (typeof res[item] == 'undefined') {
                $('[data-button-resource="' + item + '"]').hide();
            }
        });
    });
}