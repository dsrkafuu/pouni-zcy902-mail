import $ from 'jquery';

$(function () {
  var now_page = 1;
  var max_page = 1;
  var logistics_list = [];
  var key = 'abc';
  getMaxPage();
  getLogisticsList();

  $('#pg_dn'). on('click', function () {
    var keyword = $('#search_1').val();
    var keyword_1 = $('#option_3').val();
    if (now_page < max_page) {
      if(!keyword){
        now_page++;
        getLogisticsList();
        reloadActivePage();
      }else{
        if(keyword_1 == 0 ){
          now_page++;
          key = keyword;
          getLogisticsListById();
          reloadActivePage();
        }
        if(keyword_1 == 1){
          now_page++;
          key = keyword;
          getLogisticsListByTitle();
          reloadActivePage();
        }
        if(keyword_1 == 3){
          now_page++;
          key = keyword;
          getLogisticsListByAddressId();
          reloadActivePage();
        }
      }
    }
  });
  $('#pg_up'). on('click', function () {
    var keyword = $('#search_1').val();
    var keyword_1 = $('#option_3').val();
    if (now_page > 1) {
      if(!keyword){
        now_page--;
        getLogisticsList();
        reloadActivePage();
      }else{
        if(keyword_1 == 0 ){
          now_page--;
          key = keyword;
          getLogisticsListById();
          reloadActivePage();
        }
        if(keyword_1 == 1){
          now_page--;
          key = keyword;
          getLogisticsListByTitle();
          reloadActivePage();
        }
        if(keyword_1 == 3){
          now_page--;
          key = keyword;
          getLogisticsListByAddressId();
          reloadActivePage();
        }
      }

    }
  });

  $('.input_btn'). on('click', function () {
    var keyword = $('#search_1').val();
    var keyword_1 = $('#option_3').val();
    if(keyword_1 == 0 ){
      key = keyword;
      getLogisticsListById();
      reloadActivePage();
    }
    if(keyword_1 == 1){
      key = keyword;
      getLogisticsListByTitle();
      reloadActivePage();
    }
    if(keyword_1 == 3){
      key = keyword;
      getLogisticsListByAddressId();
      reloadActivePage();
    }
  });
  function getMaxPage() {
    $.ajax({
      type: 'GET',
      url: '/store/getcount',
      xhrFields: { withCredentials: true },
      success(data) {
        if (data.status == 'success') {
          var num = data.data;
          if (num != 0) {
            max_page = Math.ceil(num / 3);
            reloadPagenation();
          }
        } else {
          alert('获取订单数量失败，原因是' + data.data.errMsg);
        }
      },
      error(data) {
        alert('获取订单数量失败，原因是' + data.responseText);
      },
    });
  }

  function reloadPagenation() {
    $('.pagenation').show();
    for (let j = 2; j <= max_page; j++) {
      let t = j - 1;
      $('#page' + t).after('<a id="page' + j + '" href="#">' + j + '</a>');
    }
  }

  function reloadActivePage() {
    var past_page = parseInt($('.pagenation').children('.active').html());
    if (past_page != now_page) {
      $('.pagenation').children('.active').removeClass('active');
      if (max_page > 6) {
        switch (now_page) {
          case 1:
          case 2:
            $('#page' + now_page).addClass('active');
            break;
          case 3:
            $('#page' + now_page).html('3');
            $('#page4').html('.');
            $('#page' + now_page).addClass('active');
            break;
          case max_page - 1:
            $('#page5').html(max_page - 1);
            $('#page5').addClass('active');
            break;
          case max_page - 0:
            $('#page6').addClass('active');
            break;
          default:
            $('#page3').html('.');
            $('#page5').html('.');
            $('#page4').html(now_page);
            $('#page4').addClass('active');
            break;
        }
      } else {
        $('#page' + now_page).addClass('active');
      }
    }
  }

  function getLogisticsList() {
    $.ajax({
      type: 'POST',
      contentType: 'application/x-www-form-urlencoded',
      url: '/store/getlogistics',
      data: {
        page: now_page,
      },
      xhrFields: { withCredentials: true },
      success(data) {
        if (data.status == 'success') {
          logistics_list = data.data;
          reloadLogistics();
        } else {
          alert('获取订单列表失败，原因是' + data.data.errMsg);
        }
      },
      error(data) {
        alert('获取订单列表失败，原因是' + data.responseText);
      },
    });
  }

  function getLogisticsListByTitle() {
    $.ajax({
      type: 'POST',
      contentType: 'application/x-www-form-urlencoded',
      url: '/store/getlogistics_title',
      data: {
        page: now_page,
        title: key,
      },
      xhrFields: { withCredentials: true },
      success(data) {
        if (data.status == 'success') {
          logistics_list = data.data;
          reloadLogistics();
        } else {
          alert('获取订单列表失败，原因是' + data.data.errMsg);
        }
      },
      error(data) {
        alert('获取订单列表失败，原因是' + data.responseText);
      },
    });
  }

  function getLogisticsListById() {
    $.ajax({
      type: 'POST',
      contentType: 'application/x-www-form-urlencoded',
      url: '/store/getlogistics_id',
      data: {
        page: now_page,
        id: key,
      },
      xhrFields: { withCredentials: true },
      success(data) {
        if (data.status == 'success') {
          logistics_list = data.data;
          reloadLogistics();
        } else {
          alert('获取订单列表失败，原因是' + data.data.errMsg);
        }
      },
      error(data) {
        alert('获取订单列表失败，原因是' + data.responseText);
      },
    });
  }

  function getLogisticsListByAddressId() {
    $.ajax({
      type: 'POST',
      contentType: 'application/x-www-form-urlencoded',
      url: '/store/getlogistics_addressid',
      data: {
        page: now_page,
        addressName: key,
      },
      xhrFields: { withCredentials: true },
      success(data) {
        if (data.status == 'success') {
          logistics_list = data.data;
          reloadLogistics();
        } else {
          alert('获取订单列表失败，原因是' + data.data.errMsg);
        }
      },
      error(data) {
        alert('获取订单列表失败，原因是' + data.responseText);
      },
    });
  }
  function reloadLogistics() {
    if (logistics_list != null) {
      $('.logistics_list').children('div').hide();
      for (var i = 0; i < logistics_list.length; i++) {
        var logisticsVO = logistics_list[i];
        var orderVO = getOrder(logisticsVO.orderId);
        var itemVO = getItem(orderVO.itemId);
        var addressVO = getAddress(logisticsVO.addressId);
        $('#logistics_' + i).show();
        $('#time_' + i).html(
          new Date(logisticsVO.createTime).format('yyyy-MM-dd hh:mm:ss')
        );
        $('#order_id_' + i).html('订单号：' + logisticsVO.orderId);
        console.log(orderVO);
        if (orderVO.status == 2) {
          $('#order_status_' + i).html('未发货');
        } else {
          $('#order_status_' + i).html('已发货');
        }
        $('#img_' + i).attr('src', itemVO.imgUrl);
        $('#title_' + i).html(itemVO.title);
        $('#amount_' + i).html(orderVO.amount);
        $('#price_' + i).html(orderVO.itemPrice.toFixed(2));
        $('#total_' + i).html(orderVO.orderPrice.toFixed(2));
        $('#addressee_' + i).html(addressVO.addresseeName);
        $('#telphone_' + i).html(addressVO.encrptTelphone);
        $('#address_' + i).html(addressVO.address);

        if (orderVO.status === 2) {
          //$('#order_status_' + i).html('待发货');
          $('#status_' + i).html('待发货');
          $('#write_info_' + i).show();
          $('#send_list_' + i).hide();
          $('#write_info_' + i).attr({
            'data-id': logisticsVO.id,
            'data-i': i,
          });
          $('#sub_info_' + i).attr({
            'data-id': logisticsVO.id,
            'data-i': i,
          });
          $('#get_logistics_' + i).hide();
          $('#write_info_' + i).on('click', function () {
            $(this).hide();
            var i = $(this).data('i');
            $(this).next().show();
            $('#sub_info_' + i).on('click', function () {
              var id = $(this).data('id');
              var i = $(this).data('i');
              var company = $('#company_' + i).val();
              var num = $('#number_' + i).val();
              if (company == null || company == '') {
                $('#company_' + i).attr('placeholder', '快递公司不能为空');
              } else if (num == null || num == '') {
                $('#number_' + i).attr('placeholder', '快递单号不能为空');
              } else {
                $.ajax({
                  type: 'POST',
                  contentType: 'application/x-www-form-urlencoded',
                  url: '/store/delivery',
                  data: {
                    id: id,
                    company: company,
                    number: num,
                  },
                  xhrFields: { withCredentials: true },
                  success(data) {
                    if (data.status == 'success') {
                      getLogisticsList();
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
          });
        } else if (orderVO.status === 4) {
          $('#status_' + i).html('运输中');
          $('#get_logistics_' + i).show();
          $('#write_info_' + i).hide();
          $('#send_list_' + i).hide();
        }
      }
    }
  }

  function getItem(id) {
    var itemVO = [];
    $.ajax({
      type: 'GET',
      url: '/item/get',
      data: {
        id: id,
      },
      async: false,
      xhrFields: { withCredentials: true },
      success(data) {
        if (data.status == 'success') {
          itemVO = data.data;
        } else {
          alert('获取商品信息失败，原因是' + data.data.errMsg);
        }
      },
      error(data) {
        alert('获取商品信息失败，原因是' + data.responseText);
      },
    });
    return itemVO;
  }

  function getOrder(id) {
    var orderVO = [];
    $.ajax({
      type: 'GET',
      url: '/order/getbyid',
      data: {
        id: id,
      },
      async: false,
      xhrFields: { withCredentials: true },
      success(data) {
        if (data.status == 'success') {
          console.log(data);
          orderVO = data.data;
        } else {
          alert('获取订单信息失败，原因是' + data.data.errMsg);
        }
      },
      error(data) {
        alert('获取订单信息失败，原因是' + data.responseText);
      },
    });
    return orderVO;
  }

  function getAddress(id) {
    var addressVO = [];
    $.ajax({
      type: 'GET',
      url: '/address/get',
      data: {
        id: id,
      },
      async: false,
      xhrFields: { withCredentials: true },
      success(data) {
        if (data.status == 'success') {
          addressVO = data.data;
        } else {
          alert('获取地址信息失败，原因是' + data.data.errMsg);
        }
      },
      error(data) {
        alert('获取地址信息失败，原因是' + data.responseText);
      },
    });
    return addressVO;
  }

  Date.prototype.format = function (fmt) {
    var o = {
      'M+': this.getMonth() + 1, //月份
      'd+': this.getDate(), //日
      'h+': this.getHours(), //小时
      'm+': this.getMinutes(), //分
      's+': this.getSeconds(), //秒
      'q+': Math.floor((this.getMonth() + 3) / 3), //季度
      S: this.getMilliseconds(), //毫秒
    };
    if (/(y+)/.test(fmt)) {
      fmt = fmt.replace(
        RegExp.$1,
        (this.getFullYear() + '').substr(4 - RegExp.$1.length)
      );
    }
    for (var k in o) {
      if (new RegExp('(' + k + ')').test(fmt)) {
        fmt = fmt.replace(
          RegExp.$1,
          RegExp.$1.length == 1
            ? o[k]
            : ('00' + o[k]).substr(('' + o[k]).length)
        );
      }
    }
    return fmt;
  };
});
