<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
	String appPath = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+appPath+"/";
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>数据抽取</title>
	<link href="<%=appPath%>/css/common0.css" rel="stylesheet" type="text/css">
	<link href="<%=appPath%>/css/table.css" rel="stylesheet" type="text/css" />
	<link href="<%=appPath%>/css/globalStyle.css" rel="stylesheet" type="text/css" />

</head>

<body>

<div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td nowrap="nowrap">
    <div class="toolbar">
    <div class="tableinfo"><span><img src="<%=appPath%>/images/icon01.gif" /></span><b>当前位置：ETL监控》数据库</b></div>
    </div>
    </td>
  </tr>
</table>
</div>
</body>
</html>