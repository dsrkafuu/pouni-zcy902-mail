import $ from 'jquery';

$(() => {
  var storeVO = [];
  function reloadSoreInfo() {
    var htmlContent = '';
    htmlContent +=
      '<li><span>商铺名：</span>' +
      storeVO.storeName +
      '</li>' +
      '<li><span>商铺地址：</span>' +
      storeVO.address +
      '</li>' +
      '<li><span>联系方式：</span>' +
      storeVO.telphone +
      '</li>';
    $('#store_info').append(htmlContent);
    $('#create_time').html(
      '<span>注册时间：</span>' +
        new Date(storeVO.createTime).toLocaleDateString()
    );
  }
  $.ajax({
    type: 'GET',
    url: '/store/get',
    xhrFields: { withCredentials: true },
    success(data) {
      if (data.status == 'success') {
        storeVO = data.data;
        reloadSoreInfo();
      } else {
        alert('获取商铺信息失败，原因是' + data.data.errMsg);
      }
    },
    error(data) {
      alert('获取商铺信息失败，原因是' + data.responseText);
    },
  });
});
