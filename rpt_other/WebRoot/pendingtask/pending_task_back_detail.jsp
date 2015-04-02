<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>待办任务</title>
	
	<link href="<%=request.getContextPath() %>/css/index.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath() %>/css/common0.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath() %>/css/table.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath() %>/css/globalStyle.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.4.min.js"></script>
</head>

<body>
<div>
<br/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td nowrap="nowrap">
    <div class="toolbar">
    <div class="tableinfo"><span><img src="<%=request.getContextPath() %>/images/icon01.gif" alt="列表" /></span><b style="font-size:12px;">当前位置 >>报表处理 >>退回原因</b></div>
    </div>
    </td>
  </tr>
</table>
</div>
<div style="width:70%;border:0 solid black;height:310px;padding:10px;"><fieldset style="background-color:white;height:290px;" >
<legend style="color:red;">退回原因输入栏</legend>
<form id="fm" action="<%=request.getContextPath() %>/pendingTaskAction!saveBackTask.action" method="post">
<table width="95%" border="0" height="90%" cellspacing="0" cellpadding="0"  align="center">
	<tr height="10px"><td colspan="3"></td></tr>
	<tr height="260px">
   		<td colspan="3" align="center">
   			<textarea id="returndesc" readonly="readonly" name="pendingTaskQueryConditions.returnDesc" style="width:95%;height:95%;" rows="" cols=""><s:property value="wMoni.returnDesc"/></textarea>
   		</td>
 	</tr>
  <tr height="30px">
  	<td colspan="3" align="center"><!--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 重报天数:<input type="text"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  	<input name="btn" type="button" class="searchbtn" onclick="window.close();" value="关  闭" /></td>
  </tr>
  </table>
</form>
　</fieldset>
</div>
</body>
</html>