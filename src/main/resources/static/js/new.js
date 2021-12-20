jQuery(document).ready(function () {
    var new_itemList = [];
    $.ajax({
        type:"GET",
        url:host+"/item/listbyupdatetime",
        xhrFields:{withCredentials:true},
        success:function (data) {
            if(data.status == "success") {
                new_itemList = data.data;
                reloadNewItem(new_itemList);
            }else {
                alert("获取新品推荐失败，原因是"+data.data.errMsg);
            }
        },
        error:function (data) {
            alert("获取新品推荐失败，原因是"+data.responseText);
        }
    });
});

function reloadNewItem(list_data) {
    for(var i=0; i < 2; i++){
        var new_itemVO = list_data[i];
        $("#goods_price_"+i).parent().attr({
            "data-id" : new_itemVO.id,
            "id" : "itemDetail"+new_itemVO.id
        });
        $("#goods_title_"+i).text(new_itemVO.title);
        $("#goods_img_"+i).attr("src",new_itemVO.imgUrl);
        $("#goods_price_"+i).text("￥ "+new_itemVO.price.toFixed(2));
        $("#itemDetail"+new_itemVO.id).on("click",function () {
            window.location.href="detail.html?id="+$(this).data("id");
        })
    }
}