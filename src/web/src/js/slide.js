import $ from 'jquery';

$(() => {
  var $slides = $('.slide_pics li');
  var len = $slides.length;
  var nowli = 0;
  var prevli = 0;
  var $prev = $('.prev');
  var $next = $('.next');
  var $points = $('.points li');
  var ismove = false;
  var timer = null;
  $slides.not(':first').css({ left: 760 });
  $slides.each(function (index) {
    var $li = $('<li>');

    if (index == 0) {
      $li.addClass('active');
    }

    $li.appendTo($('.points'));
  });
  timer = setInterval(autoplay, 4000);

  $('.slide').mouseenter(() => {
    clearInterval(timer);
  });

  $('.slide').mouseleave(() => {
    timer = setInterval(autoplay, 4000);
  });

  function autoplay() {
    nowli++;
    move();
    $points.eq(nowli).addClass('active').siblings().removeClass('active');
  }

  $points.click(function () {
    if (ismove) {
      return;
    }
    nowli = $(this).index();

    if (nowli == prevli) {
      return;
    }

    $(this).addClass('active').siblings().removeClass('active');
    move();
  });

  $prev.click(() => {
    if (ismove) {
      return;
    }
    nowli--;
    move();
    $points.eq(nowli).addClass('active').siblings().removeClass('active');
  });

  $next.click(() => {
    if (ismove) {
      return;
    }
    nowli++;
    move();
    $points.eq(nowli).addClass('active').siblings().removeClass('active');
  });

  function move() {
    ismove = true;

    if (nowli < 0) {
      nowli = len - 1;
      prevli = 0;
      $slides.eq(nowli).css({ left: -760 });
      $slides.eq(nowli).animate({ left: 0 }, 800, 'easeOutExpo');
      $slides.eq(prevli).animate({ left: 760 }, 800, 'easeOutExpo', () => {
        ismove = false;
      });
      prevli = nowli;
      return;
    }

    if (nowli > len - 1) {
      nowli = 0;
      prevli = len - 1;
      $slides.eq(nowli).css({ left: 760 });
      $slides.eq(nowli).animate({ left: 0 }, 800, 'easeOutExpo');
      $slides.eq(prevli).animate({ left: -760 }, 800, 'easeOutExpo', () => {
        ismove = false;
      });
      prevli = nowli;
      return;
    }

    if (prevli < nowli) {
      $slides.eq(nowli).css({ left: 760 });
      $slides.eq(prevli).animate({ left: -760 }, 800, 'easeOutExpo');
      $slides.eq(nowli).animate({ left: 0 }, 800, 'easeOutExpo', () => {
        ismove = false;
      });
      prevli = nowli;
    } else {
      $slides.eq(nowli).css({ left: -760 });
      $slides.eq(prevli).animate({ left: 760 }, 800, 'easeOutExpo');
      $slides.eq(nowli).animate({ left: 0 }, 800, 'easeOutExpo', () => {
        ismove = false;
      });
      prevli = nowli;
    }
  }
});
