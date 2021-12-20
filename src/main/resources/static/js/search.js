var now_page = 1;
var max_page = 1;

function GetQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  decodeURI(r[2]); return null;
}

function getMaxPage() {
    $.ajax({
        type:"POST",
        contentType:"application/x-www-form-urlencoded",
        url:host+"/item/searchresult",
        data:{
            "keyWord": GetQueryString("keyword"),
        },
        xhrFields:{withCredentials:true},
        success:function (data) {
            if(data.status == "success") {
                var num = data.data;
                if(num != 0){
                    max_page = (num / 10 + 1).toFixed(0);
                }
                reloadPagenation();
            }
        },
        error:function (data) {
            alert("获取商品数量失败，原因是"+data.responseText);
        }
    });
}

function reloadItem(itemList){
    const num = itemList.length;
    $(".goods_type_list").children("li").hide();
    for(var i = 0; i < 10 && i < num; i ++){
        var itemVO = itemList[i];
        $(".pagenation").show();
        $("#item_"+i).show();
        $("#item_"+i).attr({
            "data-id" : itemVO.id,
        });
        $("#item_"+i).children("a").children("img").attr("src",itemVO.imgUrl);
        $("#item_"+i).children("h4").children("a").html(itemVO.title);
        $("#item_"+i).find("span.prize").html("￥ "+itemVO.price.toFixed(2));
        $("#item_"+i).find("span.unit").html("/一件");
        $("#item_"+i).on("click",function () {
            window.location.href="detail.html?id="+$(this).data("id");
        })
    }

}

function reloadPagenation() {
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

function getItemList(){
    $.ajax({
        type:"POST",
        contentType:"application/x-www-form-urlencoded",
        url:host+"/item/search",
        data:{
            "keyWord": GetQueryString("keyword"),
            "page": now_page,
        },
        xhrFields:{withCredentials:true},
        success:function (data) {
            if(data.status == "success") {
                var itemList = data.data;
                getMaxPage();
                reloadItem(itemList);
            }else {
                alert("搜素失败，原因是"+data.data.errMsg);
            }
        },
        error:function (data) {
            alert("搜索失败，原因是"+data.responseText);
        }
    });
}

jQuery(document).ready(function () {
    $('#search_title').html("搜索："+GetQueryString("keyword"));
    getMaxPage();
    getItemList();
    $("#pg_dn").on("click",function () {
        if(now_page < max_page){
            now_page ++;
            getItemList();
            reloadActivePage();
        }

    });
    $("#pg_up").on("click",function () {
        if(now_page>1){
            now_page --;
            getItemList();
            reloadActivePage();
        }
    })

});