$(function () {
    var userVO = [];
    function reloadUserInfo() {
        $('#name').html("<span>用户名：</span>"+userVO.name);
        $('#telphone').html("<span>绑定手机：</span>"+userVO.telphone);
        $('#email').html("<span>绑定邮箱：</span>"+userVO.email);
        $('#gender').html("<span>性别：</span>"+(userVO.gender==1?"男":"女"));
        $('#age').html("<span>年龄：</span>"+userVO.age);
    }
    $.ajax({
        type:"GET",
        url:host+"/user/get",
        xhrFields:{withCredentials:true},
        success:function (data) {
            if(data.status == "success") {
                userVO = data.data;
                reloadUserInfo();
            }else {
                alert("获取用户信息失败，原因是"+data.data.errMsg);
                if(data.data.errCode==20003){
                    window.location.href="login.html";
                }
            }
        },
        error:function (data) {
            alert("获取用户信息失败，原因是"+data.responseText);
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
});