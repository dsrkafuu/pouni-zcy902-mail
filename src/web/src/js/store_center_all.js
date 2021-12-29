import $ from 'jquery';

var now_page = 1;
var max_page = 1;

function getMaxPage() {
  $.ajax({
    type: 'GET',
    url: '/item/storeitemscount',
    xhrFields: { withCredentials: true },
    success(data) {
      if (data.status == 'success') {
        var num = data.data;
        if (num != 0) {
          max_page = Math.ceil(num / 10);
        }
        reloadPagenation();
      } else {
        alert('获取商品数量失败，原因是' + data.data.errMsg);
      }
    },
    error(data) {
      alert('获取商品数量失败，原因是' + data.responseText);
    },
  });
}

function reloadItem(itemList) {
  const num = itemList.length;
  $('.goods_type_list').children('li').hide();
  for (var i = 0; i < 10 && i < num; i++) {
    var itemVO = itemList[i];
    $('.pagenation').show();
    $('#item_' + i).show();
    $('#item_' + i).attr({
      'data-id': itemVO.id,
    });
    $('#item_' + i)
      .children('a')
      .children('img')
      .attr('src', itemVO.imgUrl);
    $('#item_' + i)
      .children('h4')
      .children('a')
      .html(itemVO.title);
    $('#item_' + i)
      .find('span.prize')
      .html('￥ ' + itemVO.price.toFixed(2));
    $('#item_' + i)
      .find('span.unit')
      .html('/一件');
    $('#item_' + i).on('click', function () {
      window.location.href = '/detail.html?id=' + $(this).data('id');
    });
  }
}

function reloadPagenation() {
  for (let j = 2; j <= max_page; j++) {
    let t = j - 1;
    $('#page' + t).after('<a id="page' + j + '" href="#">' + j + '</a>');
  }
}

function reloadActivePage() {
  var past_page = parseInt($('.pagenation').children('.active').html());
  if (past_page != now_page) {
    $('.pagenation').children('.active').removeClass('active');
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
  }
}

function getItemList() {
  $.ajax({
    type: 'POST',
    contentType: 'application/x-www-form-urlencoded',
    url: '/item/storeitemlist',
    data: {
      page: now_page,
    },
    xhrFields: { withCredentials: true },
    success(data) {
      if (data.status == 'success') {
        var itemList = [];
        itemList = data.data;
        reloadItem(itemList);
      } else {
        alert('获取商品列表失败，原因是' + data.data.errMsg);
      }
    },
    error(data) {
      alert('获取商品列表失败，原因是' + data.responseText);
    },
  });
}

$(function () {
  getMaxPage();
  getItemList();
  $('#pg_dn').on('click', function () {
    if (now_page < max_page) {
      now_page++;
      getItemList();
      reloadActivePage();
    }
  });
  $('#pg_up').on('click', function () {
    if (now_page > 1) {
      now_page--;
      getItemList();
      reloadActivePage();
    }
  });
});
