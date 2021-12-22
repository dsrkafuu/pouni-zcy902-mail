$(function () {

    function GetQueryString(name)
    {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  decodeURI(r[2]); return null;
    }
    var id = GetQueryString("id");
    var itemVO = [];
    var orderVO = [];
    var address_list = [];
    $.ajax({
        type:"GET",
        url:host+"/address/list",
        xhrFields:{withCredentials:true},
        success:function (data) {
            if(data.status == "success") {
                address_list = data.data;
                reloadAddress();
            }else {
                alert("加载地址信息失败，原因是"+data.data.errMsg);
                if(data.data.errCode==20003){
                    window.location.href="login.html";
                }
            }
        },
        error:function (data) {
            alert("加载地址信息失败，原因是"+data.responseText);
        }
    });
    $.ajax({
        type:"GET",
        url:host+"/order/getbyid",
        data:{
            "id":id,
        },
        xhrFields:{withCredentials:true},
        success:function (data) {
            if(data.status == "success") {
                orderVO = data.data;
                getItem();
            }else {
                alert("加载订单失败，原因是"+data.data.errMsg);
                if(data.data.errCode==20003){
                    window.location.href="login.html";
                }
            }
        },
        error:function (data) {
            alert("加载订单失败，原因是"+data.responseText);
        }
    });
    function getItem() {
        $.ajax({
            type:"GET",
            url:host+"/item/get",
            data:{
                "id":orderVO.itemId,
            },
            xhrFields:{withCredentials:true},
            success:function (data) {
                if(data.status == "success"){
                    itemVO = data.data;
                    reloadOrderList();
                }else {
                    alert("获取商品信息失败，原因是"+data.data.errMsg);
                }
            },
            error:function (data) {
                alert("获取商品信息失败，原因是"+data.responseText);
            }
        });
    }

    function reloadAddress() {
        if(address_list == null){
            $('#address_list').append("<dd>请编辑收货地址</dd>");
        }else{
            for(var i = 0; i < address_list.length; i ++){
                var htmlContent = "";
                var addressVO = address_list[i];
                htmlContent += "<dd><input type=\"radio\" name=\"address\" value=\""+addressVO.id+"\"";
                if(addressVO.status == 1){
                    htmlContent += " checked ";
                }
                htmlContent += ">"+addressVO.address+" （"+addressVO.addresseeName+" 收） "+addressVO.encrptTelphone + "</dd>";
                $('#address_list').append(htmlContent);
            }
        }
    }

    function reloadOrderList() {
        var amount = 0;
        var total = 0;
        if (orderVO != null) {
            var htmlContent = "";
            amount += orderVO.amount;
            total += orderVO.orderPrice;
            htmlContent += "<ul class=\"goods_list_td clearfix\">\n" +
                "                <li class=\"col01\">" + 1 + "</li>\n" +
                "                <li class=\"col02\"><img src=\"" + itemVO.imgUrl + "\"></li>\n" +
                "                <li class=\"col03\">" + itemVO.title + "</li>\n" +
                "                <li class=\"col04\">一件</li>\n" +
                "                <li class=\"col05\">" + itemVO.price + "</li>\n" +
                "                <li class=\"col06\">" + orderVO.amount + "</li>\n" +
                "                <li class=\"col07\">" + orderVO.orderPrice + "</li>\t\n" +
                "           </ul>";
            $('#order_list').append(htmlContent);
            $('#amount').html(amount);
            $('.total').html(total.toFixed(2) + "元");
            $('#order_btn').on("click", function () {
                var paymentMethod = $('input[name="pay_style"]:checked').val();
                var addressId = $('input[name="address"]:checked').val();
                $.ajax({
                    type: "POST",
                    contentType: "application/x-www-form-urlencoded",
                    url: host+"/order/complete",
                    data: {
                        "id": orderVO.id,
                        "paymentMethod": paymentMethod,
                        "addressId": addressId,
                    },
                    xhrFields: {withCredentials: true},
                    success: function (data) {
                        if (data.status == "success") {
                            alert("提交订单成功");
                            window.location.href = "user_center_order.html";
                        } else {
                            alert("提交订单失败，原因是" + data.data.errMsg);
                            if (data.data.errCode == 20003) {
                                window.location.href = "login.html";
                            }
                            window.location.href = "user_center_order.html";
                        }
                    },
                    error: function (data) {
                        alert("提交订单失败，原因是" + data.responseText);
                        window.location.href = "user_center_order.html";
                    }
                });
            });
        }
    }

});