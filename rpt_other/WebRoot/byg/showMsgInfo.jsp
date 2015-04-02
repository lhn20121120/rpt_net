<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>" >
    
    <title>消息详细内容</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<style type="text/css">
		.readmailinfo{
			background-color: #FFF0D0;
			width:100%;
		
		}
	
	
	
	</style>
	<script type="text/javascript">
		var msgInfo = window.dialogArguments;

		
	
		function initMsg(){
			document.getElementById("sender").innerHTML=msgInfo.userName;//发件人
			document.getElementById("receiver").innerHTML=msgInfo.touserName;//收件人
			document.getElementById("sendTime").innerHTML=msgInfo.startTime;//时间
			document.getElementById("subject").innerHTML=msgInfo.title;//标题
			document.getElementById("content").innerHTML=msgInfo.content;//消息内容
			document.getElementById("attachment").innerHTML=msgInfo.filePath;//附件
		}
	
	</script>
  </head>
  
  <body onload="initMsg()">
   	<div >
	   	<div class="readmailinfo" >
	   	<span id="subject" style="height: 30px;margin-top: 20px;margin-left: 20px;font-weight: bold;font-size: 15px;"></span>
   		<table style="width:100%">
   			<tr>
   				<td width="10%" align="right" style="color:gray;font-size: 13px;">发件人：</td><td width="80%"><span id="sender" style="color: limegreen;font-size: 13px;"></span></td>
   			</tr>
   			<tr>
   				<td width="10%" align="right" style="color: gray;font-size: 13px;">收件人：</td><td width="80%"><span id="receiver" style="font-size: 12px;"></span></td>
   			</tr>
   			<tr>
   				<td width="10%" align="right" style="color: gray;font-size: 13px;">时&nbsp;&nbsp;&nbsp;&nbsp;间：</td><td width="80%"><span id="sendTime" style="font-size: 13px;"></span></td>
   			</tr>
   			
   			<tr>
   				<td width="10%" align="right" style="color: gray;font-size: 13px;">附&nbsp;&nbsp;&nbsp;&nbsp;件：</td><td width="80%"><span id="attachment" style="font-size: 13px;"></span></td>
   			</tr>
   			</table>
	   	</div>
	   	<div class="mailcontent" style="border:lightblue 1 solid ;">
		   	<table >
		   		
	   			<tr>
	   				<td width="10%" align="right" style="color: gray;background-color: #FFF0D0;font-size: 13px;">内&nbsp;&nbsp;&nbsp;&nbsp;容：</td><td width="80%"><span id="content" style="height: 250px;overflow:auto;font-size: 13px;"></span></td>
	   			</tr>
	   		</table>
	   	</div>
   	</div>
  </body>
</html>
