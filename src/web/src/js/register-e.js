import $ from 'jquery';

$(() => {
  var error_name = false;
  var error_telphone = false;
  var error_password = false;
  var error_check_password = false;
  var error_check = false;
  var error_address = false;

  $('#user_name').blur(() => {
    check_user_name();
  });

  $('#tel').blur(() => {
    check_telphone();
  });

  $('#pwd').blur(() => {
    check_pwd();
  });

  $('#cpwd').blur(() => {
    check_cpwd();
  });

  $('#check').blur(() => {
    check_check();
  });

  $('#address').blur(() => {
    check_address();
  });

  $('#allow').click(() => {
    if ($(this).is(':checked')) {
      error_check = false;
      $(this).siblings('span').hide();
    } else {
      error_check = true;
      $(this).siblings('span').html('请勾选同意');
      $(this).siblings('span').show();
    }
  });

  function check_user_name() {
    var len = $('#user_name').val().length;
    if (len < 2 || len > 10) {
      $('#user_name').next().html('请输入2-10个字符的商铺名');
      $('#user_name').next().show();
      error_name = true;
    } else {
      $('#user_name').next().hide();
      error_name = false;
    }
  }

  function check_check() {
    var len = $('#check').val().length;
    if (len != 4) {
      $('#check').next().html('请输入4位数的验证码');
      $('#check').next().show();
      error_check = true;
    } else {
      $('#check').next().hide();
      error_check = false;
    }
  }

  function check_telphone() {
    var phone = $('#tel').val();
    if (!/^1[3456789]\d{9}$/.test(phone)) {
      $('#tel').next().next().html('请输入正确的国内手机号');
      $('#tel').next().next().show();
      error_telphone = true;
    } else {
      $('#tel').next().next().hide();
      error_telphone = false;
    }
  }

  function check_pwd() {
    var len = $('#pwd').val().length;
    if (len < 8 || len > 20) {
      $('#pwd').next().html('密码最少8位，最长20位');
      $('#pwd').next().show();
      error_password = true;
    } else {
      $('#pwd').next().hide();
      error_password = false;
    }
  }

  function check_cpwd() {
    var pass = $('#pwd').val();
    var cpass = $('#cpwd').val();

    if (pass != cpass) {
      $('#cpwd').next().html('两次输入的密码不一致');
      $('#cpwd').next().show();
      error_check_password = true;
    } else {
      $('#cpwd').next().hide();
      error_check_password = false;
    }
  }

  function check_address() {
    var len = $('#address').val().length;
    if (len <= 3) {
      $('#address').next().html('请输入详细的地址');
      $('#address').next().show();
      error_address = true;
    } else {
      $('#address').next().hide();
      error_address = false;
    }
  }

  $('#get_otpCode').on('click', () => {
    check_telphone();
    if (error_telphone == false) {
      var telphone = $('#tel').val();
      $.ajax({
        type: 'POST',
        contentType: 'application/x-www-form-urlencoded',
        url: '/store/getotp',
        data: {
          telphone: telphone,
        },
        xhrFields: { withCredentials: true },
        success(data) {
          if (data.status == 'success') {
            alert(
              '验证码发送成功，可惜只发给了管理员（我是不会告诉你管理员穷到买不起短信服务的T_T），快找管理员要验证码吧'
            );
          } else {
            alert('验证码发送失败，原因是' + data.data.errMsg);
          }
        },
        error(data) {
          alert('验证码发送失败，原因是' + data.responseText);
        },
      });
    }
  });

  $('#register_sub').on('click', () => {
    check_user_name();
    check_telphone();
    check_pwd();
    check_cpwd();
    check_check();
    check_address();

    if (
      error_name == false &&
      error_password == false &&
      error_check_password == false &&
      error_check == false &&
      error_telphone == false &&
      error_check == false &&
      error_address == false
    ) {
      var name = $('#user_name').val();
      var telphone = $('#tel').val();
      var otp_code = $('#check').val();
      var password = $('#pwd').val();
      var address = $('#address').val();
      $.ajax({
        type: 'POST',
        contentType: 'application/x-www-form-urlencoded',
        url: '/store/register',
        data: {
          telphone: telphone,
          otpCode: otp_code,
          storeName: name,
          address: address,
          password: password,
        },
        xhrFields: { withCredentials: true },
        success(data) {
          if (data.status == 'success') {
            alert('注册成功');
            window.location.href = '/login-e.html';
          } else {
            alert('注册失败，原因是' + data.data.errMsg);
          }
        },
        error(data) {
          alert('注册失败，原因是' + data.responseText);
        },
      });
    } else {
      return false;
    }
  });
});
