<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <link href="<%=basePath%>/css/index.css" rel="stylesheet" type="text/css" />
	<link href="<%=basePath%>/css/common0.css" rel="stylesheet" type="text/css" />
	<link href="<%=basePath%>/css/table.css" rel="stylesheet" type="text/css" />
	<link href="<%=basePath%>/css/globalStyle.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/demo.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.4.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/easyui-lang-zh_CN.js"></script>
  </head>
  
  <body style="overflow-x:auto ">
    <table  align="center" cellpadding="0" cellspacing="0"  style="width: 100%">
    	<s:iterator value="objArrayList" id="o" >
    		<tr>
    			<s:iterator value="#o" id="j">
    				<td align="center" style="border:solid 1px #bdd5e0;padding:4px">
    					<s:property value="#j"/>
    				</td>
    			</s:iterator> 			
    		</tr>
    	</s:iterator>
    </table>
  </body>
</html>
