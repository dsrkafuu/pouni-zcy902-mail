
var emptyAccount = false;
var emptyPassword = false;

function check_account() {
    var account = $("#user_account").val();
    if(account == null || account == ""){
        emptyAccount = true;
        $('.user_error').show();
    }else {
        emptyAccount = false;
        $('.user_error').hide();
    }
}

function check_password() {
    var password = $("#user_password").val();
    if(password == null || password == ""){
        emptyPassword = true;
        $('.pwd_error').show();
    }else{
        emptyPassword = false;
        $('.pwd_error').hide();
    }
}



jQuery(document).ready(function () {

    $('#user_account').blur(function() {
        check_account();
    });

    $('#user_password').blur(function () {
        check_password();
    });


    $('#login_btn').on("click",function () {
        check_account();
        check_password();
        if(emptyAccount == false && emptyPassword == false){
            var account = $("#user_account").val();
            var password = $("#user_password").val();
            $.ajax({
                type:"POST",
                contentType:"application/x-www-form-urlencoded",
                url:host+"/store/login",
                data:{
                    "telphone":account,
                    "password":password,
                },
                xhrFields:{withCredentials:true},
                success:function (data) {
                    if(data.status == "success") {
                        alert("登录成功");
                        window.location.href="store_center_info.html";
                    }else {
                        alert("登录失败，原因是"+data.data.errMsg);
                    }
                },
                error:function (data) {
                    alert("登录失败，原因是"+data.responseText);
                }
            });
        }
    })

});

