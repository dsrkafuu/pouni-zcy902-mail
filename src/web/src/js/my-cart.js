function getCartNum() {
    $.ajax({
        type:"GET",
        url:host+"/cart/getnum",
        xhrFields:{withCredentials:true},
        success:function (data) {
            if(data.status == "success") {
                var n = data.data;
                reloadCartNum(n);
            }else {
                $('.goods_count').hide();
            }
        },
        error:function (data) {
            $('.goods_count').hide();
        }
    });
}

function reloadCartNum(n) {
    $('.goods_count').html(n);
    $('.goods_count').show();
}

jQuery(document).ready(function () {
    getCartNum();
});