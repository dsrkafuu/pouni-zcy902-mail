$(function () {
  var orderVO_list = [];
  var address_list = [];
  function GetQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]);
    return null;
  }

  function getItem(item_id) {
    var g_itemVO = [];
    $.ajax({
      type: 'GET',
      url: host + '/item/get',
      data: {
        id: item_id,
      },
      async: false,
      xhrFields: { withCredentials: true },
      success: function (data) {
        if (data.status == 'success') {
          g_itemVO = data.data;
        } else {
          alert('获取商品信息失败，原因是' + data.data.errMsg);
        }
      },
      error: function (data) {
        alert('获取商品信息失败，原因是' + data.responseText);
      },
    });
    return g_itemVO;
  }

  function reloadAddress() {
    if (address_list == null) {
      $('#address_list').append('<dd>请编辑收货地址</dd>');
    } else {
      for (var i = 0; i < address_list.length; i++) {
        var htmlContent = '';
        var addressVO = address_list[i];
        htmlContent +=
          '<dd><input type="radio" name="address" value="' + addressVO.id + '"';
        if (addressVO.status == 1) {
          htmlContent += ' checked ';
        }
        htmlContent +=
          '>' +
          addressVO.address +
          ' （' +
          addressVO.addresseeName +
          ' 收） ' +
          addressVO.encrptTelphone +
          '</dd>';
        $('#address_list').append(htmlContent);
      }
    }
  }

  function reloadOrderList() {
    var amount = 0;
    var total = 0;
    if (orderVO_list != null) {
      for (var i = 0; i < orderVO_list.length; i++) {
        var j = i + 1;
        var htmlContent = '';
        var itemVO = getItem(orderVO_list[i].itemId);
        amount += orderVO_list[i].amount;
        total += orderVO_list[i].orderPrice;
        htmlContent +=
          '<ul class="goods_list_td clearfix">\n' +
          '                <li class="col01">' +
          j +
          '</li>\n' +
          '                <li class="col02"><img src="' +
          itemVO.imgUrl +
          '"></li>\n' +
          '                <li class="col03">' +
          itemVO.title +
          '</li>\n' +
          '                <li class="col04">一件</li>\n' +
          '                <li class="col05">' +
          itemVO.price +
          '</li>\n' +
          '                <li class="col06">' +
          orderVO_list[i].amount +
          '</li>\n' +
          '                <li class="col07">' +
          orderVO_list[i].orderPrice +
          '</li>\t\n' +
          '           </ul>';
        $('#order_list').append(htmlContent);
      }
      $('#amount').html(amount);
      $('.total').html(total.toFixed(2) + '元');
      $('#order_btn').on('click', function () {
        var error_complete = false;
        var paymentMethod = $('input[name="pay_style"]:checked').val();
        var addressId = $('input[name="address"]:checked').val();
        for (var i = 0; i < orderVO_list.length; i++) {
          $.ajax({
            type: 'POST',
            contentType: 'application/x-www-form-urlencoded',
            url: host + '/order/complete',
            data: {
              id: orderVO_list[i].id,
              paymentMethod: paymentMethod,
              addressId: addressId,
            },
            xhrFields: { withCredentials: true },
            success: function (data) {
              if (data.status == 'success') {
              } else {
                error_complete = true;
                alert('提交订单失败，原因是' + data.data.errMsg);
                if (data.data.errCode == 20003) {
                  window.location.href = 'login.html';
                }
              }
            },
            error: function (data) {
              error_complete = true;
              alert('提交订单失败，原因是' + data.responseText);
            },
          });
        }
        if (error_complete == false) {
          window.location.href = 'user_center_order.html';
        }
      });
    }
  }
  $.ajax({
    type: 'GET',
    url: host + '/address/list',
    xhrFields: { withCredentials: true },
    success: function (data) {
      if (data.status == 'success') {
        address_list = data.data;
        reloadAddress();
      } else {
        alert('加载地址信息失败，原因是' + data.data.errMsg);
        if (data.data.errCode == 20003) {
          window.location.href = 'login.html';
        }
      }
    },
    error: function (data) {
      alert('加载地址信息失败，原因是' + data.responseText);
    },
  });
  var id_build = GetQueryString('id-build');
  $.ajax({
    type: 'POST',
    contentType: 'application/x-www-form-urlencoded',
    url: host + '/order/createfromcart',
    data: {
      id: id_build,
    },
    xhrFields: { withCredentials: true },
    success: function (data) {
      if (data.status == 'success') {
        orderVO_list = data.data;
        reloadOrderList();
      } else {
        alert('加载订单信息失败，原因是' + data.data.errMsg);
        if (data.data.errCode == 20003) {
          window.location.href = 'login.html';
        }
      }
    },
    error: function (data) {
      alert('加载订单信息失败，原因是' + data.responseText);
    },
  });
});
