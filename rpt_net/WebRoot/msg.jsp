<%@ page language="java" import="java.util.*" pageEncoding="GB2312"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String msg = request.getParameter("msg") != null ? request.getParameter("msg") : "";

msg.replaceAll("<br/>", "");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>»ã×ÜÐÅÏ¢</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body style="overflow:auto;">
  <input type="hidden" name ="message" id= "message" value="<%=msg%>"/>
  
   <strong id="msg"></strong> 
   <script type="text/javascript">
  	var msg1 =  document.getElementById("message").value;
 //	alert(msg1);
  	document.getElementById("msg").innerHTML=msg1;
   </script>
  </body>
</html>
