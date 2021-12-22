import $ from 'jquery';

function getCartList() {
  $.ajax({
    type: 'GET',
    url: '/cart/list',
    xhrFields: { withCredentials: true },
    success(data) {
      if (data.status == 'success') {
        const cart_list = data.data;
        reloadCartList(cart_list);
      } else {
        alert('获取购物车商品失败，原因是' + data.data.errMsg);
        if (data.data.errCode == 20003) {
          window.location.href = '/login.html';
        }
      }
    },
    error(data) {
      alert('获取购物车商品失败，原因是' + data.responseText);
    },
  });
}

function getItem(item_id) {
  let g_itemVO = [];

  $.ajax({
    type: 'GET',
    url: '/item/get',
    data: {
      id: item_id,
    },
    async: false,
    xhrFields: { withCredentials: true },
    success(data) {
      if (data.status == 'success') {
        g_itemVO = data.data;
      } else {
        alert('获取购物车商品失败，原因是' + data.data.errMsg);
      }
    },
    error(data) {
      alert('获取购物车商品失败，原因是' + data.responseText);
    },
  });

  return g_itemVO;
}

function checkCart(id) {
  if (id == null) {
    return;
  }
  let cart_total_price = parseFloat($('#cart_total').html());
  cart_total_price += parseFloat($('#totalPrice_' + id).html());
  $('#cart_total').html(cart_total_price.toFixed(2));
  let num = parseInt($('#cart_num').html());
  num += parseInt($('#amount_' + id).val());
  $('#cart_num').html(num);
  isAllChecked();
}

function uncheckCart(id) {
  if (id == null) {
    return;
  }
  let cart_total_price = parseFloat($('#cart_total').html());
  cart_total_price -= parseFloat($('#totalPrice_' + id).html());
  $('#cart_total').html(cart_total_price.toFixed(2));
  let num = parseInt($('#cart_num').html());
  num -= parseInt($('#amount_' + id).val());
  $('#cart_num').html(num);
  isAllChecked();
}

function isAllChecked() {
  let is_all_checked = true;
  $('input:checkbox').each(() => {
    if (!$(this).is($('#select_all'))) {
      if (!$(this).is(':checked')) {
        is_all_checked = false;
      }
    }
  });
  if (is_all_checked) {
    $('#select_all').prop('checked', true);
  } else {
    $('#select_all').prop('checked', false);
  }
}

function reloadCartList(cart_list) {
  const len = cart_list.length;

  for (let i = 0; i < len; i++) {
    const cartVO = cart_list[i];
    const itemVO = getItem(cartVO.itemId);
    let htmlContent = '';
    htmlContent +=
      '<ul class="cart_list_td clearfix">' + '       <li class="col01">';
    if (cartVO.status == 1) {
      htmlContent +=
        '<input type="checkbox" name="" id="check_' +
        cartVO.id +
        '" data-id="' +
        cartVO.id +
        '">';
    } else {
      htmlContent += '已失效';
    }
    htmlContent +=
      '</li>' +
      '       <li class="col02"><img src="' +
      itemVO.imgUrl +
      '"></li>' +
      '       <li class="col03">' +
      itemVO.title +
      '<br><em>' +
      itemVO.price.toFixed(2) +
      '元</em></li>' +
      '       <li class="col04">1件</li>' +
      '       <li class="col05"  id="price_' +
      cartVO.id +
      '" data-price="' +
      cartVO.price +
      '">' +
      cartVO.price.toFixed(2) +
      '元</li>' +
      '       <li class="col06">';
    if (cartVO.status == 1) {
      htmlContent +=
        '          <div class="num_add">' +
        '               <a href="#" class="add fl" id="add_' +
        cartVO.id +
        '" data-id="' +
        cartVO.id +
        '">+</a>' +
        '               <input type="text" class="num_show fl" value="' +
        cartVO.amount +
        '" id="amount_' +
        cartVO.id +
        '">' +
        '               <a href="#" class="minus fl" id="minus_' +
        cartVO.id +
        '" data-id="' +
        cartVO.id +
        '">-</a>' +
        '          </div>';
    } else {
      htmlContent += '已失效';
    }
    htmlContent +=
      '       </li>' +
      '       <li class="col07" id="totalPrice_' +
      cartVO.id +
      '">' +
      cartVO.totalPrice.toFixed(2) +
      '元</li>' +
      '       <li class="col08"><a href="#" id="delete_' +
      cartVO.id +
      '" data-id="' +
      cartVO.id +
      '">删除</a></li>' +
      '</ul>';

    $('#cart_list').append(htmlContent);

    $('#check_' + cartVO.id).on('click', () => {
      const id = $(this).data('id');
      if ($(this).is(':checked')) {
        checkCart(id);
      } else {
        uncheckCart(id);
      }
    });

    $('#add_' + cartVO.id).on('click', () => {
      const id = $(this).data('id');
      const past_amount = parseInt($('#amount_' + id).val());
      if (past_amount < 99) {
        const amount = past_amount + 1;
        $.ajax({
          type: 'POST',
          contentType: 'application/x-www-form-urlencoded',
          url: '/cart/update',
          data: {
            id: id,
            amount: amount,
          },
          xhrFields: { withCredentials: true },
          success(data) {
            if (data.status == 'success') {
              $('#amount_' + id).val(amount);
              const total_price =
                (parseFloat($('#price_' + id).data('price')) * 100 * amount) /
                100;
              $('#totalPrice_' + id).html(total_price.toFixed(2) + '元');
            } else {
              alert('增加数量失败，原因是' + data.data.errMsg);
            }
          },
          error(data) {
            alert('增加数量失败，原因是' + data.responseText);
          },
        });
      } else {
        alert('最多一次购买99件商品');
      }
    });

    $('#minus_' + cartVO.id).on('click', () => {
      const id = $(this).data('id');
      const past_amount = parseInt($('#amount_' + id).val());

      if (past_amount > 1) {
        const amount = past_amount - 1;
        $.ajax({
          type: 'POST',
          contentType: 'application/x-www-form-urlencoded',
          url: '/cart/update',
          data: {
            id: id,
            amount: amount,
          },
          xhrFields: { withCredentials: true },
          success(data) {
            if (data.status == 'success') {
              $('#amount_' + id).val(amount);
              const total_price =
                (parseFloat($('#price_' + id).data('price')) * 100 * amount) /
                100;
              $('#totalPrice_' + id).html(total_price.toFixed(2) + '元');
            } else {
              alert('减少数量失败，原因是' + data.data.errMsg);
            }
          },
          error(data) {
            alert('减少数量失败，原因是' + data.responseText);
          },
        });
      } else {
        alert('至少购买1件商品');
      }
    });

    $('#delete_' + cartVO.id).on('click', () => {
      const id = $(this).data('id');
      $.ajax({
        type: 'POST',
        contentType: 'application/x-www-form-urlencoded',
        url: '/cart/delete',
        data: {
          id: id,
        },
        xhrFields: { withCredentials: true },
        success(data) {
          if (data.status == 'success') {
            window.location.reload();
          } else {
            alert('删除失败，原因是' + data.data.errMsg);
          }
        },
        error(data) {
          alert('删除失败，原因是' + data.responseText);
        },
      });
    });
  }

  $('#cart_list').find('*').trigger('create');
}

$(() => {
  getCartList();

  $('#select_all').on('click', () => {
    if ($(this).is(':checked')) {
      $('input:checkbox').each(() => {
        if (!$(this).is(':checked')) {
          $(this).prop('checked', true);
          checkCart($(this).data('id'));
        }
      });
    } else {
      $('input:checkbox').each(() => {
        if ($(this).is(':checked')) {
          $(this).prop('checked', false);
          uncheckCart($(this).data('id'));
        }
      });
    }
  });

  $('#settle').on('click', () => {
    let id_build = '';
    $('input:checkbox').each(() => {
      if (!$(this).is($('#select_all'))) {
        if ($(this).is(':checked')) {
          const id = $(this).data('id');
          if (id_build == '') {
            id_build += id;
          } else {
            id_build += ',';
            id_build += id;
          }
        }
      }
    });
    window.location.href = '/cart-order.html?id-build=' + id_build;
  });
});
