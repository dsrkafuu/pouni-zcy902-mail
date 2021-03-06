document.onkeydown = function (e) {
  e = window.event || e;
  var k = e.keyCode;
  //屏蔽ctrl+R，F5键，ctrl+F5键  F3键！验证
  if (
    (e.ctrlKey == true && k == 82) ||
    k == 116 ||
    (e.ctrlKey == true && k == 116) ||
    k == 114
  ) {
    e.keyCode = 0;
    alert('当前页面不能刷新！');
    e.returnValue = false;
    e.cancelBubble = true;
    return false;
  }
  if (k == 8) {
    alert('不能返回或后退！');
    e.keyCode = 0;
    e.returnValue = false;
    return false;
  }
  //屏蔽 Ctrl+n   验证可以实现效果
  if (e.ctrlKey && k == 78) {
    e.keyCode = 0;
    e.returnValue = false;
    e.cancelBubble = true;
    return false;
  }
  //屏蔽F11   验证可以实现效果
  if (k == 122) {
    e.keyCode = 0;
    e.returnValue = false;
    e.cancelBubble = true;
    return false;
  }
  //屏蔽 shift+F10  验证可以实现效果
  if ((e.shiftKey && k == 121) || (e.ctrlKey && k == 121)) {
    e.keyCode = 0;
    e.returnValue = false;
    e.cancelBubble = true;
    return false;
  }

  //屏蔽Alt+F4
  if (e.altKey && k == 115) {
    window.showModelessDialog(
      'about:blank',
      '',
      'dialogWidth:1px;dialogheight:1px'
    );
    e.keyCode = 0;
    e.returnValue = false;
    e.cancelBubble = true;
    return false;
  }
  //屏蔽 Alt+ 方向键 ← ;屏蔽 Alt+ 方向键 → ！验证
  if (e.altKey && (k == 37 || k == 39)) {
    alert('不准你使用ALT+方向键前进或后退网页！');
    e.keyCode = 0;
    e.returnValue = false;
    e.cancelBubble = true;
    return false;
  }
};

//屏蔽右键菜单，！验证
document.oncontextmenu = function (event) {
  if (window.event) {
    event = window.event;
  }
  try {
    var the = event.srcElement;
    if (
      !(
        (the.tagName == 'INPUT' && the.type.toLowerCase() == 'text') ||
        the.tagName == 'TEXTAREA'
      )
    ) {
      return false;
    }
    return true;
  } catch (e) {
    return false;
  }
};
