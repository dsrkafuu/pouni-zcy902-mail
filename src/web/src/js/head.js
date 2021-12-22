import $ from 'jquery';

function reloadUserName(user_name) {
  $('.login_info').html('欢迎您：<em>' + user_name + '</em>');
  $('.login_info').show();
  $('.login_btn').hide();
}

function getLoginUser() {
  var userVO = [];
  $.ajax({
    type: 'GET',
    url: '/user/get',
    xhrFields: { withCredentials: true },
    success(data) {
      if (data.status == 'success') {
        userVO = data.data;
        reloadUserName(userVO.name);
      } else {
        $('.login_info').hide();
        $('.login_btn').show();
      }
    },
    error() {
      $('.login_info').hide();
      $('.login_btn').show();
    },
  });
}

$(() => {
  getLoginUser();
});
