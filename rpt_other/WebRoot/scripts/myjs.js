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
      ���ܣ��޸� window.setTimeout��ʹ֮���Դ��ݲ����Ͷ������
      ʹ�÷����� setTimeout(�ص�����,ʱ��,����1,,����n)
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


