import $ from 'jquery';

var isEmail = false;
var emptyAccount = false;
var emptyPassword = false;

function saveUser(account) {
  localStorage.setItem('njcci_account', account);
}

function getSavedUser() {
  var account = localStorage.getItem('njcci_account');
  if (account != null) {
    $('#user_account').val(account);
  }
}

function check_account() {
  var account = $('#user_account').val();
  if (account == null || account == '') {
    emptyAccount = true;
    $('.user_error').show();
  } else {
    emptyAccount = false;
    $('.user_error').hide();
  }
}

function check_password() {
  var password = $('#user_password').val();
  if (password == null || password == '') {
    emptyPassword = true;
    $('.pwd_error').show();
  } else {
    emptyPassword = false;
    $('.pwd_error').hide();
  }
}

function judge_is_email() {
  var re = /^[a-z0-9][\w.-]*@[a-z0-9-]+(\.[a-z]{2,5}){1,2}$/;
  if (re.test($('#user_account').val())) {
    isEmail = true;
  } else {
    isEmail = false;
  }
}

$(function () {
  getSavedUser();

  $('#user_account').blur(function () {
    check_account($(this).val());
  });

  $('#user_password').blur(function () {
    check_password($(this).val());
  });

  $('#login_btn').on('click', function () {
    check_account();
    check_password();
    judge_is_email();
    if (emptyAccount == false && emptyPassword == false) {
      var account = $('#user_account').val();
      var password = $('#user_password').val();
      var needSave = $('.more_input input[type="checkbox"]').prop('checked');
      needSave && saveUser(account);
      if (isEmail == true) {
        $.ajax({
          type: 'POST',
          contentType: 'application/x-www-form-urlencoded',
          url: '/user/loginbyemail',
          data: {
            email: account,
            password: password,
          },
          xhrFields: { withCredentials: true },
          success(data) {
            if (data.status == 'success') {
              alert('登录成功');
              window.location.href = '/index.html';
            } else {
              alert('登录失败，原因是' + data.data.errMsg);
            }
          },
          error(data) {
            alert('登录失败，原因是' + data.responseText);
          },
        });
      } else {
        $.ajax({
          type: 'POST',
          contentType: 'application/x-www-form-urlencoded',
          url: '/user/loginbytelphone',
          data: {
            telphone: account,
            password: password,
          },
          xhrFields: { withCredentials: true },
          success(data) {
            if (data.status == 'success') {
              alert('登录成功');
              window.location.href = '/index.html';
            } else {
              alert('登录失败，原因是' + data.data.errMsg);
            }
          },
          error(data) {
            alert('登录失败，原因是' + data.responseText);
          },
        });
      }
    }
  });
});
