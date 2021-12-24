import $ from 'jquery';

$(function (){
  $('.info_submit'). on('click', function () {
    var title = $('#title').val();
    var description = $('#description').val();
    var sort = $('#sort option:selected').val();
    var price = $('#price').val();
    var stock = $('#stock').val();
    var imgUrl = $('#imgUrl').val();
    if (title == null || title == '') {
      alert('商品名不能为空');
    } else if (description == null || description == '') {
      alert('商品描述不能为空');
    } else if (price == null || price == '') {
      alert('价格不能为空');
    } else if (stock == null || stock == '') {
      alert('库存不能为空');
    } else if (imgUrl == null || imgUrl == '') {
      alert('图片地址不能为空');
    } else {
      $.ajax({
        type: 'POST',
        contentType: 'application/x-www-form-urlencoded',
        url: '/item/create',
        data: {
          title: title,
          description: description,
          sort: sort,
          price: price,
          stock: stock,
          imgUrl: imgUrl,
        },
        xhrFields: { withCredentials: true },
        success(data) {
          if (data.status == 'success') {
            alert('添加成功');
            window.location.href = '/store_center_all.html';
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
});
