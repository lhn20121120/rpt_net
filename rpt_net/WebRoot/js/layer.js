// JavaScript Document
var w_width;
var w_height;
var cBg;
$(function(){
	w_width=$(window).width();
    w_height=$(window).height();
	cBg=$("#cBg")
	$(window).resize(function(){
	w_width=$(window).width();
    w_height=$(window).height();
	})
   //去虚线
   $("a").focus(function(){
   $(this).blur();
   })
  <!-- 事件 -->	 
  $(".clayer_close").click(function(){
      closeAction();
  }) 

})

  <!-- 弹出居中层方法 -->	  
  function mLayerAction(layer){
	   layer=$(layer)
	   var topErr
       var l_width=layer.width();
	   var l_height=layer.height();
	   var left=(w_width-l_width)/2;
	   var top=(w_height-l_height)/2;
	   var bodyHeight=$("body").height();
	   if(bodyHeight<w_height){bodyHeight=w_height}
	   topErr=$(window).scrollTop();
	   layer.css({"left":left,"top":top+topErr});
	   layer.show();
	   cBg.height(bodyHeight);
	   cBg.show();
  }
  <!-- 弹出居中层方法2 -->	  
  function layerAction(id){
	   layer=$("#"+id)
       var topErr;
       var l_width=layer.width();
	   var l_height=layer.height();
	   var left=(w_width-l_width)/2;
	   var top=(w_height-l_height)/2;
	   var bodyHeight=$("body").height();
	   if(bodyHeight<w_height){bodyHeight=w_height}
	   topErr=$(window).scrollTop();
	   layer.css({"left":left,"top":top+topErr});
	   layer.show();
	   cBg.height(bodyHeight);
	   cBg.show();
  }
  <!-- lB_middle 绑定 与 移除 -->
  function closeAction(){
       $(".commonLayer").hide();
	   cBg.hide();
  }

  
  function meunAction(layer){
	  var meun=$("#"+layer.attr("layer"));
	  var offset = layer.offset();
      var left=offset.left+Number(layer.attr("leftErr"));
	  var top=offset.top+Number(layer.attr("topErr"));
	      meun.css({"left":left,"top":top});
		  meun.show();
  }