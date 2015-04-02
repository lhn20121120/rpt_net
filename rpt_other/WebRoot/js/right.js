// JavaScript Document
$(function(){
	var w_h=$(window).height();
	$("#rightIframe").height(w_h-3200);
	$(window).resize(function(){
		w_h=$(window).height();
		$("#rightIframe").height(w_h-3200);
	})
	//
	tabLI=$("#tab li");
	tabLiS=tabLI.size();
	tabList();
	$(".tab_after").css("z-index",tabLiS);
	tabLI.click(function(){
		tabList();
		$(this).css("z-index",tabLiS);
		tabLI.removeClass();
		$(this).addClass("tab_after");
	})
})
//≈≈¡–tab
var tabLI;
var tabLiS;
function tabList(){
	for(var i=0;i<tabLiS;i++){
		var left=92*i;
		tabLI.eq(i).css({"left":left,"z-index":tabLiS-i});
	}
}