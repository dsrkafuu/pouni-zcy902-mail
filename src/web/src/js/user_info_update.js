import $ from 'jquery';

$(function (){
  var userVO = [];
  function reloadUserInfo() {
    $('#name').val(userVO.name);
    $('#telphone').html('<span>绑定手机：</span>' + userVO.telphone);
    $('#email').val(userVO.email);
    $('#age').val(userVO.age);
    $('input:radio[value=' + userVO.gender + ']').attr('checked', 'true');
  }
  $.ajax({
    type: 'GET',
    url: '/user/get',
    xhrFields: { withCredentials: true },
    success(data) {
      if (data.status == 'success') {
        userVO = data.data;
        reloadUserInfo();
      } else {
        alert('获取用户信息失败，原因是' + data.data.errMsg);
      }
    },
    error(data) {
      alert('获取用户信息失败，原因是' + data.responseText);
    },
  });
  $('#update'). on('click', function () {
    var name = $('#name').val();
    var email = $('#email').val();
    var age = $('#age').val();
    var gender = $('input:radio:checked').val();
    if (name == null || name == '') {
      alert('用户名不能为空');
    } else if (email == null || email == '') {
      alert('绑定邮箱不能为空');
    } else if (age == null || age == '') {
      alert('年龄不能为空');
    } else if (gender == null || gender == '') {
      alert('性别不能不选');
    } else {
      $.ajax({
        type: 'POST',
        contentType: 'application/x-www-form-urlencoded',
        url: '/user/update',
        data: {
          name: name,
          email: email,
          age: age,
          gender: gender,
        },
        xhrFields: { withCredentials: true },
        success(data) {
          if (data.status == 'success') {
            alert('添加成功');
            window.location.href = '/user_center_info.html';
          } else {
            alert('添加失败，原因是' + data.data.errMsg);
          }
        },
        error(data) {
          alert('添加失败，原因是' + data.responseText);
        },
      });
    }
  });
  $('#logout'). on('click', function () {
    $.ajax({
      type: 'GET',
      url: '/user/logout',
      xhrFields: { withCredentials: true },
      success(data) {
        if (data.status == 'success') {
          alert('注销登录成功');
          window.location.href = '/index.html';
        } else {
          alert('注销登录失败，原因是' + data.data.errMsg);
          if (data.data.errCode == 20003) {
            window.location.href = '/login.html';
          }
        }
      },
      error(data) {
        alert('注销登录失败，原因是' + data.responseText);
      },
    });
  });
});
