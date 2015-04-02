String.prototype.Trim = function()
{
  return this.replace(/(^\s*)|(\s*$)/g, "");
}

String.prototype.LTrim = function()
{
  return this.replace(/(^\s*)/g, "");
}

String.prototype.RTrim = function()
{
  return this.replace(/(\s*$)/g, "");
}

  /**//*
      功能：修改 window.setTimeout，使之可以传递参数和对象参数
      使用方法： setTimeout(回调函数,时间,参数1,,参数n)
  */
  var __sto = setTimeout;
  window.setTimeout = function(callback,timeout,param)
  {
      var args = Array.prototype.slice.call(arguments,2);
      var _cb = function()
      {
          callback.apply(null,args);
      }

      __sto(_cb,timeout);
  }


