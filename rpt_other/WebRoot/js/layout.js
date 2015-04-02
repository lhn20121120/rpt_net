// JavaScript Document
$(function(){
	var oc_i=true;	   
	var left=$(".left");
	var right=$(".right");
	var i_right=187;
	var mainHeight=$(window).height()-103;
	var window_w=$(window).width();
	var rightWidth=window_w-i_right;
	left.css("height",mainHeight);
	right.css({"width":rightWidth,"height":mainHeight});
	$(window).resize(function(){
		mainHeight=$(window).height()-103;
	    window_w=$(window).width();
		rightWidth=window_w-i_right;
		left.css("height",mainHeight);
		right.css({"width":rightWidth,"height":mainHeight});
	})
	//
	var secondLink=$(".linkOl")//二级导航
	var linktitle=$(".linktitle")//一级导航
	linktitle.click(function(){
	var next=$(this).next();
		if(next.is(":hidden")){
		secondLink.not(next).slideUp();	
		next.slideDown();	
		}
		linktitle.removeClass("linkAfter");
		$(this).addClass("linkAfter");
	})
	//左列收缩
	$("#qjoc_button").click(function(){
		if(oc_i){
			i_right=16;
			left.hide();
			right.width(window_w-i_right);
			$(this).addClass("qjoc_buttonAfter");
			oc_i=false;
		}else{
			$(this).removeClass("qjoc_buttonAfter");
			i_right=187;
			left.show();
			right.width(window_w-i_right);
			$(this).removeClass("qjoc_buttonAfter");
			oc_i=true;
		}
	})
	//菜单
	var lefttitleLi = $(".lefttitle li");
	var leftlink=$(".leftlink");
	lefttitleLi.click(function(){
		var layerId=$("#"+$(this).attr("layerId"));						   
		lefttitleLi.removeClass();
		$(this).addClass("after");
		leftlink.hide();
		layerId.show();
	})
	
	//滚动提示
	moveLi=$("#moveTitle li");
	moveLi.eq(0).css("top",0);
	moveLiSize=moveLi.size();

  var aone=setInterval("moveLiAction()",3000);
   $("#moveTitle").mouseover(function(){
   clearInterval(aone);
   }).mouseout(function(){
   aone=setInterval("moveLiAction()",3000); 
   });

})

//滚动提示
	var moveNumber=0;
	var moveLi;
	var moveLiSize;
	function moveLiAction(){
  		moveNumber++;
		if(moveNumber>=moveLiSize){moveNumber=0}
		moveLi.eq(moveNumber).animate({top:0},500);
		var newMoveNumber=moveNumber-1;
		if(newMoveNumber<0){newMoveNumber=moveLiSize-1}
		moveLi.eq(newMoveNumber).animate({top:-20},500,function(){$(this).css("top",20)});

	}

