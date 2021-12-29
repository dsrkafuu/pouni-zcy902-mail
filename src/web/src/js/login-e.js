import $ from 'jquery';

var emptyAccount = false;
var emptyPassword = false;

function saveUser(account) {
  localStorage.setItem('njcci_account_e', account);
}

function getSavedUser() {
  var account = localStorage.getItem('njcci_account_e');
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

$(function () {
  getSavedUser();

  $('#user_account').blur(function () {
    check_account();
  });

  $('#user_password').blur(function () {
    check_password();
  });

  $('#login_btn').on('click', function () {
    check_account();
    check_password();
    if (emptyAccount == false && emptyPassword == false) {
      var account = $('#user_account').val();
      var password = $('#user_password').val();
      var needSave = $('.more_input input[type="checkbox"]').prop('checked');
      needSave && saveUser(account);
      $.ajax({
        type: 'POST',
        contentType: 'application/x-www-form-urlencoded',
        url: '/store/login',
        data: {
          telphone: account,
          password: password,
        },
        xhrFields: { withCredentials: true },
        success(data) {
          if (data.status == 'success') {
            alert('登录成功');
            window.location.href = '/store_center_info.html';
          } else {
            alert('登录失败，原因是' + data.data.errMsg);
          }
        },
        error(data) {
          alert('登录失败，原因是' + data.responseText);
        },
      });
    }
  });
});
