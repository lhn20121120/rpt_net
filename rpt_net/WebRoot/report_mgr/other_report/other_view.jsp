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
		<center>
		<table cellSpacing="0" cellPadding="0" border="1" width="98%">
			<tr>
				<td vAlign="top" width="17%" background="../../image/barbk.jpg" height="35"><img height="34" src="../../image/column6.jpg" width="90" border="0"></td>
				<td vAlign="top" width="17%" background="../../image/barbk.jpg" height="35"><img height="34" src="../../image/column5.jpg" width="90" align="top" border="0"></td>
				<td width="18%" background="../../image/barbk.jpg" align="center">文件类型</td>
				<td width="23%" background="../../image/barbk.jpg" align="center">文件大小</td>
				<td vAlign="top" background="../../image/barbk.jpg" height="35" align="center"><img src="../../image/column8.jpg"></td>
			</tr>
			<TR>
				<TD vAlign="middle" noWrap align="center" height="30">北京工行</TD>
				<TD vAlign="middle" noWrap align="center" height="30">
					<font face="Arial" size="2">0000/00/00</font></TD>
				<TD vAlign="middle" noWrap align="left" height="30">
					　</TD>
				<TD vAlign="middle" noWrap align="left" height="30">
					<p align="center"><font size="2">54k</font></TD>
				<TD vAlign="middle" noWrap align="center" height="30">
					<a href="#"><img src="icon/word.bmp" border="0"></a>
				</TD>
			</TR>
			<TR>
				<TD vAlign="middle" noWrap align="center" height="30">
					<font face="宋体" size="2" color="#000000">……</font></TD>
				<TD vAlign="middle" noWrap align="center" height="30">
					<font face="宋体" size="2" color="#000000">……</font></TD>
				<TD vAlign="middle" noWrap align="left" height="30">
					　</TD>
				<TD vAlign="middle" noWrap align="left" height="30">
					<p align="left">
						<a href="#"><font face="宋体" size="2" color="#000000">……</font></a></p>
				</TD>
				<TD vAlign="middle" noWrap align="center" height="30">
					<a href="#"><img src="icon/winrar.bmp" border="0"></a>
				</TD>
			</tr>
		</table>
		
		<table cellSpacing="0" cellPadding="0" border="0" width="98%">
			<tr>
				<TD>
					<jsp:include page="../../apartpage.jsp" flush="true">
				  		<jsp:param name="url" value=""/>
				  	</jsp:include>
				</TD>
			</tr>
		</table>
	</center>
	</body>
</html:html>
