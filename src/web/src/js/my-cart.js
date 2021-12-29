import $ from 'jquery';

function getCartNum() {
  $.ajax({
    type: 'GET',
    url: '/cart/getnum',
    xhrFields: { withCredentials: true },
    success(data) {
      if (data.status == 'success') {
        var n = data.data;
        reloadCartNum(n);
      } else {
        $('.goods_count').hide();
      }
    },
    error() {
      $('.goods_count').hide();
    },
  });
}

function reloadCartNum(n) {
  $('.goods_count').html(n);
  $('.goods_count').show();
}

$(function () {
  getCartNum();
});
