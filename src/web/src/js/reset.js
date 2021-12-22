$(function () {
  var error_password = false;
  var error_check_password = false;

  $('#pwd').blur(function () {
    check_pwd();
  });

  $('#cpwd').blur(function () {
    check_cpwd();
  });
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

  $('#reset_sub').on('click', function () {
    check_pwd();
    check_cpwd();

    if (error_password == false && error_check_password == false) {
      var password = $('#pwd').val();
      $.ajax({
        type: 'POST',
        contentType: 'application/x-www-form-urlencoded',
        url: host + '/user/reset',
        data: {
          password: password,
        },
        xhrFields: { withCredentials: true },
        success: function (data) {
          if (data.status == 'success') {
            window.location.href = 'skip-2.html';
          } else {
            alert('新密码设置失败，原因是' + data.data.errMsg);
            if (data.data.errCode == 20005) {
              window.location.href = 'findpwd.html';
            } else if (data.data.errCode == 10001) {
              clearPassword();
            }
          }
        },
        error: function (data) {
          alert('新密码设置失败，原因是' + data.responseText);
        },
      });
    } else {
      return false;
    }
  });

  function clearPassword() {
    $('#pwd').val('');
    $('#cpwd').val('');
  }
});
