<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.fitech.model.etl.common.ETLConfig"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   
	<link href="<%=basePath%>/css/index.css" rel="stylesheet" type="text/css" />
	<link href="<%=basePath%>/css/common0.css" rel="stylesheet" type="text/css" />
	<link href="<%=basePath%>/css/table.css" rel="stylesheet" type="text/css" />
	<link href="<%=basePath%>/css/globalStyle.css" rel="stylesheet" type="text/css" />
  </head>
  
  <body >
  	<table style="border:solid 1px #bdd5e0;" border="0" cellspacing="0" cellpadding="0" align="center" width="100%" height="50%">
  		<tr>
  			<td>
  				<form action="<%=request.getContextPath() %>/getResultSetAction!getfilePath.action" method="post" id="searchFileForm">
			  		<input type="text" value="aaaaa">
			  		<input type="button" value="test"/> 			
			  	</form>
  			</td>
  			
  		</tr>
  		</table>
   <iframe src="../../rpt_net/viewNXDataReport.do" width="100%" height="50%"/>
  </body>
</html>
