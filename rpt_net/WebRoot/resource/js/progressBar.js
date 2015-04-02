function ProgressBar(msg)
{
	this.msg = msg;
	this.show = function()	{
		var processBarw, processBarh, bordercolor;
		processBarw = 200;
		processBarh = 25;
		bordercolor = "#6B8FEF";
		var sWidth, sHeight;
		sWidth = parseInt(document.all(msg).style.width);
		sHeight =parseInt(document.all(msg).style.height);
		var processBarBgObj = document.createElement("div");
		processBarBgObj.setAttribute("id", "processBarBgDiv");
		processBarBgObj.style.position = "absolute";
		processBarBgObj.style.top = "100";
		processBarBgObj.style.background = "#DEE0E8";
		processBarBgObj.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
		processBarBgObj.style.opacity = "0.6";
		processBarBgObj.style.left = "35";
		processBarBgObj.style.width = sWidth + "px";
		processBarBgObj.style.height = sHeight + "px";
		processBarBgObj.style.zIndex = "10000";
		document.body.appendChild(processBarBgObj);
		
//		var processBarObj = document.createElement("div");
//		processBarObj.setAttribute("id", "processBarDiv");
//		processBarObj.setAttribute("align", "center");
//		processBarObj.style.background = "#EBEEFF";
//		processBarObj.style.border = "1px solid " + bordercolor;
//		processBarObj.style.position = "absolute";
//		processBarObj.style.width = processBarw + "px";
//		processBarObj.style.height = processBarh + "px";
//		processBarObj.style.left = (document.body.offsetWidth-parseInt(processBarw))/2;   
//		processBarObj.style.top = (document.body.offsetHeight-parseInt(processBarh))/2;	
//		
//		processBarObj.style.font = "12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
//		
//		processBarObj.style.textAlign = "center";
//		processBarObj.style.lineHeight = "25px";
//		processBarObj.style.zIndex = "10001";
//		document.body.appendChild(processBarObj);
//		var processBarHTML = "<marquee direction=\"right\" scrollamount=\"8\" scrolldelay=\"100\"><span class=\"progressBarHandle-0\"></span> <span class=\"progressBarHandle-1\"></span> <span class=\"progressBarHandle-2\"></span> <span class=\"progressBarHandle-3\"></span> <span class=\"progressBarHandle-4\"></span> <span class=\"progressBarHandle-5\"></span> <span class=\"progressBarHandle-6\"></span> <span class=\"progressBarHandle-7\"></span> <span class=\"progressBarHandle-8\"></span> <span class=\"progressBarHandle-9\"></span> </marquee> ";
//		var msgHTML = "<font size='2'>"+this.msg+"</font>";
//		document.getElementById("processBarDiv").innerHTML = "<br>"+processBarHTML+msgHTML+"<br>";
//			
	};
	
	this.close = function() 	{
		var processBarBgDiv=document.getElementById("processBarBgDiv");
		processBarBgDiv.removeNode(true);
//		var processBarDiv=document.getElementById("processBarDiv");
//		processBarDiv.removeNode(true);
	};
	
}