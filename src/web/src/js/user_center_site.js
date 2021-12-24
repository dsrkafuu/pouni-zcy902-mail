import $ from 'jquery';

$(function (){
  var address_list = [];
  function reloadAddressList() {
    if (address_list != null) {
      for (var i = 0; i < address_list.length; i++) {
        var addressVO = address_list[i];
        var htmlContent = '';
        htmlContent +=
          '<dd>' +
          addressVO.address +
          '  （' +
          addressVO.addresseeName +
          ' 收） ' +
          addressVO.encrptTelphone +
          '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio"  id="address_' +
          addressVO.id +
          '" name="address" value="' +
          addressVO.id +
          '"';
        if (addressVO.status == 1) {
          htmlContent += ' checked ';
        }
        htmlContent += '>设为默认 </dd>';
        $('#address_list').append(htmlContent);
        if (addressVO.status == 2) {
          $('#address_' + addressVO.id). on('click', function () {
            var id = $(this).val();
            $.ajax({
              type: 'POST',
              contentType: 'application/x-www-form-urlencoded',
              url: '/address/set',
              data: {
                id: id,
              },
              xhrFields: { withCredentials: true },
              success(data) {
                if (data.status == 'success') {
                  window.location.reload();
                } else {
                  alert('设置失败，原因是' + data.data.errMsg);
                }
              },
              error(data) {
                alert('设置失败，原因是' + data.responseText);
              },
            });
          });
        }
      }
    }
  }

  $.ajax({
    type: 'GET',
    url: '/address/list',
    xhrFields: { withCredentials: true },
    success(data) {
      if (data.status == 'success') {
        address_list = data.data;
        reloadAddressList();
      } else {
        alert('获取地址列表失败，原因是' + data.data.errMsg);
      }
    },
    error(data) {
      alert('获取地址列表失败，原因是' + data.responseText);
    },
  });

  $('#sub_bnt'). on('click', function () {
    var name = $('#name').val();
    var address = $('#address').val();
    var postcode = $('#postcode').val();
    var telphone = $('#telphone').val();
    if (name == null || name == '') {
      alert('收件人不能为空');
    } else if (address == null || address == '') {
      alert('详细地址不能为空');
    } else if (postcode == null || postcode == '') {
      alert('邮编不能为空');
    } else if (telphone == null || telphone == '') {
      alert('手机不能为空');
    } else {
      $.ajax({
        type: 'POST',
        contentType: 'application/x-www-form-urlencoded',
        url: '/address/create',
        data: {
          addresseeName: name,
          address: address,
          postcode: postcode,
          addresseeTelphone: telphone,
        },
        xhrFields: { withCredentials: true },
        success(data) {
          if (data.status == 'success') {
            alert('提交成功');
            window.location.reload();
          } else {
            alert('提交失败，原因是' + data.data.errMsg);
          }
        },
        error(data) {
          alert('提交失败，原因是' + data.responseText);
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
