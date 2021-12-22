import $ from 'jquery';

//定义全局商品数组信息

const list_title = ['卧室', '餐厅', '客厅', '书房', '墙纸', '家装风格'];
$(() => {
  getData(1);
  reloadButton();
});

function reloadItem(floor, list_data) {
  var num = list_data.length;
  for (var i = 0; i < 4 && i < num; i++) {
    var itemVO = list_data[i];
    $('#goods_title_f' + floor + '_' + i)
      .parent()
      .attr({
        'data-id': itemVO.id,
        id: 'itemDetail' + itemVO.id,
      });
    $('#goods_title_f' + floor + '_' + i).text(itemVO.title);
    $('#goods_img_f' + floor + '_' + i).attr('src', itemVO.imgUrl);
    $('#goods_img_f' + floor + '_' + i)
      .parent('a')
      .attr('href', 'detail.html?id=' + itemVO.id);
    $('#goods_price_f' + floor + '_' + i).text('￥ ' + itemVO.price.toFixed(2));
    $('#itemDetail' + itemVO.id).on('click', () => {
      window.location.href = '/detail.html?id=' + $(this).data('id');
    });
  }
}
function getData(i) {
  var g_itemList = [];
  $.ajax({
    type: 'POST',
    contentType: 'application/x-www-form-urlencoded',
    url: '/item/list',
    data: {
      sort: i,
      page: 1,
    },
    xhrFields: { withCredentials: true },
    success(data) {
      if (data.status == 'success') {
        g_itemList = data.data;
        reloadItem(i, g_itemList);
        if (i <= 5) {
          getData(i + 1);
        }
      } else {
        alert(
          '获取' + list_title[i - 1] + '分类列表失败，原因是' + data.data.errMsg
        );
        if (i <= 5) {
          getData(i + 1);
        }
      }
    },
    error(data) {
      alert(
        '获取' + list_title[i - 1] + '分类列表失败，原因是' + data.responseText
      );
      if (i <= 5) {
        getData(i + 1);
      }
    },
  });
}
function reloadButton() {
  for (var i = 1; i <= 6; i++) {
    $('#get_more_' + i).attr({
      'data-id': i,
    });
    $('#get_more_' + i).on('click', () => {
      window.location.href = '/list.html?sort=' + $(this).data('id');
    });
  }
}
