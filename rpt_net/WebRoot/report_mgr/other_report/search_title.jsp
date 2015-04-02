<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
	<head>
	<html:base/>	
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">	
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
	</head>
	<body>
		<table cellSpacing="0" cellPadding="2" border="0" width="807">
			<tr>
				<td height="18" colspan="2">
					<img height="48" alt="" src="../../image/query.gif" width="115" align="left">
				</td>
			</tr>
			<tr>
				<td colspan="2" background="../../image/line_bg.gif"></td>
			</tr>
			<tr>
				<td height="22">
					文件名：<INPUT id="Text1" type="text" name="Text1" size="27" class="input-text">&nbsp;&nbsp;&nbsp; 上报机构：
					<input type="text" name="" size="20" class="input-text">&nbsp;&nbsp;&nbsp; 上报时间：起 
					<INPUT id="startDate" name="startDate" type="text" size="10" value="" maxlength="10" class="input-text">
					止<INPUT id="endDate"  name="endDate" type="text" size="10" maxlength="10" value="" class="input-text">
				</td>
				<td height="22" width="88">
					<INPUT class="input-button" id="OK" type="button" value=" 查 询 " name="OK" style="float: right">
				</td>
			</tr>
		</table>
		</body>
</html:html>