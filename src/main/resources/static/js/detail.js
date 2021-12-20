var g_itemVO = [];

var g_itemList = [];

const list_title = ["卧室", "餐厅", "客厅", "书房", "墙纸", "家装风格"];

function GetQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  decodeURI(r[2]); return null;
}

function addAnimation($add_x,$add_y,$to_x,$to_y) {
    $(".add_jump").css({'left':$add_y+80,'top':$add_x+10,'display':'block'});
    $(".add_jump").stop().animate({
            'left': $to_y+7,
            'top': $to_x+7},
        "fast", function() {
            $(".add_jump").fadeOut('fast',function(){
                getCartNum();
            });

        });
}

function reloadDom(amount) {
    $("#storeName").text(g_itemVO.storeName);
    $("#title").text(g_itemVO.storeName + g_itemVO.title);
    $("#description").text(g_itemVO.description);
     $("#stock").text(g_itemVO.stock);
    $("#price").text(g_itemVO.price.toFixed(2));
    $("#imgUrl").attr("src",g_itemVO.imgUrl);
    $("#sales").text(g_itemVO.sales);
    var totalPrice = g_itemVO.price*amount;
    $("#total_price").text(totalPrice.toFixed(2));
    $("#sort").text(list_title[g_itemVO.sort - 1]);

}

function reloadStoreItems(){
    if(g_itemList != null){
        var num = g_itemList.length;
        for(var i = 0; i < num; i ++){
            var itemVO = g_itemList[i];
            $("#item_"+i).show();
            $("#item_"+i).children("a").children("img").attr("src",itemVO.imgUrl);
            $("#item_"+i).children("h4").children("a").html(itemVO.title);
            $("#item_"+i).find("span.prize").html("￥ "+itemVO.price.toFixed(2));
            $("#item_"+i).find("span.unit").html("/一件");
            $("#item_"+i).attr("data-id",itemVO.id);
            $("#item_"+i).on("click",function () {
                window.location.href="detail.html?id="+$(this).data("id");
            })
        }
    }

}

jQuery(document).ready(function () {


    //获取商品详情
    $.ajax({
        type:"GET",
        url:host+"/item/get",
        data:{
            "id":GetQueryString("id"),
        },
        xhrFields:{withCredentials:true},
        success:function (data) {
            if(data.status == "success") {
                g_itemVO = data.data;
                var amount =parseInt($('#amount').val());
                reloadDom(amount);
            }else {
                alert("获取信息失败，原因是"+data.data.errMsg);
            }
        },
        error:function (data) {
            alert("获取信息失败，原因是"+data.responseText);
        }
    });

    $('#add_amount').on("click",function () {
        var amount =parseInt($('#amount').val());
        var price = parseFloat($('#price').text());
        amount ++;
        $('#amount').val(amount);
        var totalPrice = price*100*amount/100;
        $("#total_price").text(totalPrice.toFixed(2)+"元");
    });

    $('#minus_amount').on("click",function () {
        var amount =parseInt($('#amount').val());
        var price = parseFloat($('#price').text());
        if(amount==1){
            alert("数量不能小于1");
            return;
        }else {
            amount --;
            $('#amount').val(amount);
            var totalPrice = price*amount;
            $("#total_price").text(totalPrice.toFixed(2)+"元");
        }
    });

    $("#buy_now").on("click",function () {
        var amount =parseInt($('#amount').val());
        window.location.href="item-order.html?amount="+amount+"&itemId="+g_itemVO.id;
    });

    $('#add_cart').on("click",function(){

        var $add_x = $('#add_cart').offset().top;
        var $add_y = $('#add_cart').offset().left;

        var $to_x = $('#show_count').offset().top;
        var $to_y = $('#show_count').offset().left;

        var amount =parseInt($('#amount').val());
        $.ajax({
            type:"POST",
            contentType:"application/x-www-form-urlencoded",
            url:host+"/cart/add",
            data:{
                "itemId":g_itemVO.id,
                "amount":amount,
            },
            xhrFields:{withCredentials:true},
            success:function (data) {
                if(data.status == "success") {
                    addAnimation($add_x,$add_y,$to_x,$to_y);
                }else {
                    alert("添加失败，原因是"+data.data.errMsg);
                    if(data.data.errCode==20003){
                        window.location.href="login.html";
                    }
                }
            },
            error:function (data) {
                alert("添加失败，原因是"+data.responseText);
            }
        });
    });

    $('#get_items').on("click",function () {
        if($('.detail_tab').children("li.active").html() == "商品介绍"){
            $('#get_items').addClass("active");
            $('#get_detail').removeClass("active");
            $('#detail_content').hide();
            $('#items_content').show();
            var store_name = $('#storeName').html();
            $.ajax({
                type:"POST",
                contentType:"application/x-www-form-urlencoded",
                url:host+"/item/listbystorename",
                data:{
                    "storeName": store_name,
                    "page":1,
                },
                xhrFields:{withCredentials:true},
                success:function (data) {
                    if(data.status == "success") {
                        g_itemList = data.data;
                        reloadStoreItems();
                    }else {
                        alert("获取本店精选失败，原因是"+data.data.errMsg);
                    }
                },
                error:function (data) {
                    alert("获取本店精选失败，原因是"+data.responseText);
                }
            });
        }
    });
    $('#get_detail').on("click",function () {
        if($('.detail_tab').children("li.active").html() == "本店精选"){
            $('#get_detail').addClass("active");
            $('#get_items').removeClass("active");
            $('#items_content').hide();
            $('#detail_content').show();
        }
    })


});


