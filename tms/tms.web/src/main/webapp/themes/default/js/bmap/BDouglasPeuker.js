function BDouglasPeucker(data, kink){
    threshold = kink * 360.0 / (2.0 * Math.PI * 6378137.0);  /* Now in degrees */
    douglasPeucker(data, 0, Math.floor(data.length/2), threshold);
    douglasPeucker(data, Math.floor(data.length/2), data.length - 1, threshold);
    var d = [];
    for(var i=0; i<data.length; i++){
        if(data[i].removed != true){
            d.push(data[i]);
        }
    }
    return d;
}
function douglasPeucker(data, begin, end, threshold){
    if(begin >= end){
        return;
    }

    // 起止点
    var a = data[begin];
    var b = data[end];

    // 一些系数
    var M = Math.sqrt((a.lat - b.lat)*(a.lat - b.lat) +
            (a.lng - b.lng)*(a.lng - b.lng));
    var A = (a.lat - b.lat)/M;
    var B = (b.lng - a.lng)/M;
    var C = (a.lng* b.lat - b.lng* a.lat)/M;

    var i = begin + 1;
    var k = begin + 1;
    var max = 0;
    // 寻找最大值
    for(;i<=end-1; i++){
        var point = data[i];
        var current = Math.abs(A*point.lng + B*point.lat + C);
        if(max < current){
            k = i;
            max = current;
        }
    }
    // 递归
    if(max > threshold){
        douglasPeucker(data, begin, k, threshold);
        douglasPeucker(data, k, end, threshold);
    }
    else{
        //标记被去掉的点
        for(i = begin+1; i<= end-1; i++){
            data[i].removed = true;
        }
    }
}
