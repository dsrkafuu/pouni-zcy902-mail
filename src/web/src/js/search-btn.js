import $ from 'jquery';

$(() => {
  $('.input_btn').on('click', () => {
    var keyword = $('#search').val();
    if (keyword == null || keyword == '') {
      alert('关键词不能为空');
    } else {
      window.location.href = '/searchlist.html?keyword=' + keyword;
    }
  });
});
