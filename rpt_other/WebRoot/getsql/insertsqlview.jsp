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
    
    <title>My JSP 'insertsqlview.jsp' starting page</title>
    
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
  <s:if test="objStrList==null || objStrList.size()==0">
  	
  </s:if>
  <s:else>
  	<a href="<%=request.getContextPath() %>/getResultSetAction!downLoadInsertSQL.action" class="easyui-linkbutton" plain="true" iconCls="icon-print" style="float:left;border:1px solid">下载</a>
  </s:else>
   <table align="center" cellpadding="0" cellspacing="0"  style="width: 100%;border:solid 1px #bdd5e0;margin-top: 4px;margin-bottom: 4px" >
   	
   		<s:if test="objStrList==null || objStrList.size()==0">
   		<tr>
   			<td>暂无数据</td>
   		</tr>
    		
    	</s:if>
    	<s:else>
	    	<s:iterator value="objStrList" id="o">
	    		<tr>
	    			<td style="padding-left: 4px;padding-bottom: 4px"><s:property value="#o"/></td>
	    		</tr>
	    		
	    	</s:iterator>
    	</s:else>
   </table>
    <s:if test="objStrList==null || objStrList.size()==0">
  	
  </s:if>
  <s:else>
  	<a href="<%=request.getContextPath() %>/getResultSetAction!downLoadInsertSQL.action" class="easyui-linkbutton" plain="true" iconCls="icon-print" style="float:left;border:1px solid" >下载</a>
  </s:else>
  </body>
</html>
