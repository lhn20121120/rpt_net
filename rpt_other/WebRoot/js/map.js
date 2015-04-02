// JavaScript Document
$(function(){
	var w_h=$(window).height();
	var iframe=$("#googleIfra");
	iframe.height(w_h-500);
	$(window).resize(function(){
		w_h=$(window).height();
		iframe.height(w_h-500);
	})
	//ÇÐ»»
	var mapLi=$(".maptab li");
	var infoDiv=$(".infoDiv");
	mapLi.click(function(){
		var layerId=$("#"+$(this).attr("layerId"));						   
		mapLi.removeClass();
		$(this).addClass("tabafter");
		infoDiv.hide();
		layerId.show();
	})
	
})