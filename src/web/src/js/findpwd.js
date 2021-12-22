import $ from 'jquery';

$(() => {
  var error_telphone = false;
  var error_check = false;

  $('#tel').blur(() => {
    check_telphone();
  });

  $('#check').blur(() => {
    check_check();
  });

  $('#get_otpCode').on('click', () => {
    check_telphone();
    if (error_telphone == false) {
      var telphone = $('#tel').val();
      $.ajax({
        type: 'POST',
        contentType: 'application/x-www-form-urlencoded',
        url: '/user/forgot',
        data: {
          telphone: telphone,
        },
        xhrFields: { withCredentials: true },
        success(data) {
          if (data.status == 'success') {
            alert('验证码发送成功');
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

  $('#findpwd_sub').on('click', () => {
    check_telphone();
    check_check();

    if (error_telphone == false && error_check == false) {
      var telphone = $('#tel').val();
      var otp_code = $('#check').val();
      $.ajax({
        type: 'POST',
        contentType: 'application/x-www-form-urlencoded',
        url: '/user/check',
        data: {
          telphone: telphone,
          otpCode: otp_code,
        },
        xhrFields: { withCredentials: true },
        success(data) {
          if (data.status == 'success') {
            window.location.href = '/skip-1-reset.html';
          } else {
            if (data.data.errCode == 10001) {
              window.location.href = '/skip-3.html';
            } else {
              alert('验证失败，原因是' + data.data.errMsg);
            }
          }
        },
        error(data) {
          alert('验证失败，原因是' + data.responseText);
        },
      });
    } else {
      return false;
    }
  });

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
});
