$(function () {
    var now_page = 1;
    var max_page = 1;
    var order_list = [];
    getMaxPage();
    getOrderList();

    $("#pg_dn").on("click",function () {
        if(now_page < max_page){
            now_page ++;
            getOrderList();
            reloadActivePage();
        }

    });
    $("#pg_up").on("click",function () {
        if(now_page>1){
            now_page --;
            getOrderList();
            reloadActivePage();
        }
    });

    $("#logout").on("click",function () {
        $.ajax({
            type:"GET",
            url:host+"/user/logout",
            xhrFields:{withCredentials:true},
            success:function (data) {
                if(data.status == "success") {
                    alert("注销登录成功");
                    window.location.href="index.html";
                }else {
                    alert("注销登录失败，原因是"+data.data.errMsg);
                    if(data.data.errCode==20003){
                        window.location.href="login.html";
                    }
                }
            },
            error:function (data) {
                alert("注销登录失败，原因是"+data.responseText);
            }
        });
    })


    function getMaxPage() {
        $.ajax({
            type:"GET",
            url:host+"/order/count",
            xhrFields:{withCredentials:true},
            success:function (data) {
                if(data.status == "success") {
                    var num = data.data;
                    if(num != 0){
                        max_page = (num / 10 + 1).toFixed(0);
                        reloadPagenation();
                    }
                }else {
                    alert("获取订单数量失败，原因是"+data.data.errMsg);
                    if(data.data.errCode==20003){
                        window.location.href="login.html";
                    }
                }
            },
            error:function (data) {
                alert("获取订单数量失败，原因是"+data.responseText);
            }
        });
    }

    function reloadPagenation() {
        $(".pagenation").show();
        if(max_page <= 6){
            for(var j = 2; j <= max_page; j ++){
                var t = j - 1;
                $("#page"+t).after("<a id=\"page"+j+"\" href=\"#\">"+j+"</a>");
            }
        }else{
            $("#page1").after("<a id=\"page2\" href=\"#\">2</a>");
            for(var j = 3; j <= 5; j ++){
                var t = j - 1;
                $("#page"+t).after("<a id=\"page"+j+"\" href=\"#\">.</a>");
            }
            $("#page5").after("<a id=\"page6\" href=\"#\">"+max_page+"</a>");
        }
    }

    function reloadActivePage() {
        var past_page = parseInt($(".pagenation").children(".active").html());
        if(past_page != now_page){
            $(".pagenation").children(".active").removeClass("active");
            switch (now_page) {
                case 1:
                case 2:$("#page"+now_page).addClass("active");break;
                case 3:$("#page"+now_page).html("3");$("#page4").html(".");$("#page"+now_page).addClass("active");break;
                case max_page-1:$("#page5").html(max_page-1);$("#page5").addClass("active");break;
                case max_page-0:$("#page6").addClass("active");break;
                default :$("#page3").html(".");$("#page5").html(".");$("#page4").html(now_page);$("#page4").addClass("active");break;
            }
        }
    }



    function getOrderList() {
        $.ajax({
            type:"GET",
            url:host+"/order/list",
            data:{
                "page": now_page,
            },
            xhrFields:{withCredentials:true},
            success:function (data) {
                if(data.status == "success") {
                    order_list = data.data;
                    reloadOrder();
                }else {
                    alert("获取订单列表失败，原因是"+data.data.errMsg);
                }
            },
            error:function (data) {
                alert("获取订单列表失败，原因是"+data.responseText);
            }
        });
    }

    function reloadOrder() {
        if(order_list != null){
            $(".order_list").hide();
            for(var i = 0; i < order_list.length; i ++){
                var orderVO = order_list[i];
                var itemVO = getItem(orderVO.itemId);
                $('#order_'+i).show();
                $("#time_"+i).html(new Date(orderVO.createTime).format("yyyy-MM-dd hh:mm:ss"));
                $("#order_id_"+i).html(orderVO.id);
                if(orderVO.status == 1){
                    $("#order_status_"+i).html("未支付");
                }else {
                    $("#order_status_"+i).html("已支付");
                }
                $("#img_"+i).attr("src",itemVO.imgUrl);
                $("#title_"+i).html(itemVO.title);
                $("#amount_"+i).html(orderVO.amount);
                $("#price_"+i).html(orderVO.itemPrice.toFixed(2));
                $("#total_"+i).html(orderVO.orderPrice.toFixed(2));
                if(orderVO.status == 1){
                    $("#logistics_status_"+i).html("待付款");
                    $("#pay_btn_"+i).show();
                    $("#pay_btn_"+i).attr({
                        "data-id":orderVO.id,
                    });
                    $("#cancel_btn_"+i).show();
                    $("#cancel_btn_"+i).attr({
                        "data-id":orderVO.id,
                    });
                    $("#send_msg_"+i).hide();
                    $("#get_logistics_"+i).hide();
                    $("#pay_btn_"+i).on("click",function () {
                        var id = $(this).data("id");
                        window.location.href="center-order.html?id="+id;
                    });
                    $("#cancel_btn_"+i).on("click",function () {
                        var id = $(this).data("id");
                        $.ajax({
                            type:"POST",
                            contentType:"application/x-www-form-urlencoded",
                            url:host+"/order/cancel",
                            data:{
                                "id": id,
                            },
                            xhrFields:{withCredentials:true},
                            success:function (data) {
                                if(data.status == "success") {
                                   window.location.reload();
                                }else {
                                    alert("取消订单失败，原因是"+data.data.errMsg);
                                }
                            },
                            error:function (data) {
                                alert("取消订单失败，原因是"+data.responseText);
                            }
                        });
                    })
                }else if (orderVO.status == 2) {
                    $("#logistics_status_"+i).html("待发货");
                    $("#send_msg_"+i).show();
                    $("#pay_btn_"+i).hide();
                    $("#cancel_btn_"+i).hide();
                    $("#get_logistics_"+i).hide();
                }else {
                    $("#logistics_status_"+i).html("运输中");
                    $("#get_logistics_"+i).show();
                    $("#pay_btn_"+i).hide();
                    $("#cancel_btn_"+i).hide();
                    $("#send_msg_"+i).hide();
                }
            }
        }
    }

    function getItem(id) {
        var itemVO = [];
        $.ajax({
            type:"GET",
            url:host+"/item/get",
            data:{
                "id":id,
            },
            async:false,
            xhrFields:{withCredentials:true},
            success:function (data) {
                if(data.status == "success"){
                    itemVO = data.data;
                }else {
                    alert("获取商品信息失败，原因是"+data.data.errMsg);
                }
            },
            error:function (data) {
                alert("获取商品信息失败，原因是"+data.responseText);
            }
        });
        return itemVO;
    }





    Date.prototype.format = function(fmt) {
        var o = {
            "M+" : this.getMonth()+1,                 //月份
            "d+" : this.getDate(),                    //日
            "h+" : this.getHours(),                   //小时
            "m+" : this.getMinutes(),                 //分
            "s+" : this.getSeconds(),                 //秒
            "q+" : Math.floor((this.getMonth()+3)/3), //季度
            "S"  : this.getMilliseconds()             //毫秒
        };
        if(/(y+)/.test(fmt)) {
            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
        }
        for(var k in o) {
            if(new RegExp("("+ k +")").test(fmt)){
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
            }
        }
        return fmt;
    }
});