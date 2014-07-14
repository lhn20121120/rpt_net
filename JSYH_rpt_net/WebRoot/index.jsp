<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd"> 
<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%     
  response.setHeader("Pragma","No-cache");     
  response.setHeader("Cache-Control","no-cache");     
  response.setHeader("Expires","0");     
  %> 
<html:html locale="true">
	<head>
		<html:base/>
		<title>科融数据-统计报表系统</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="style/globalStyle.css" rel="stylesheet" type="text/css">
	</head>
	<frameset id="superFrame" name="superFrame" rows="74,*" cols="*" border="0" frameSpacing="0" frameBorder="0">
		<frame name="topFrame" scrolling="no" noresize target="contents" src="top.jsp">
		<frameset name="firstFrame" rows="*" cols="170,*" framespacing="0" frameborder="no" border="0">
			<frame name="naviFrame" target="mainFrame" src="navimenu.jsp" scrolling="no" noresize>
			<frameset rows="1,*" cols="*" framespacing="0" frameborder="no" border="0">
				<frame name="middleFrame" target="mainFrame" src="middle.htm" noresize scrolling="no">
				<frameset rows="*" cols="1,*,1" framespacing="0" frameborder="no" border="0">
					<frame name="plugFrame" target="mainFrame" src="plug.htm" scrolling="no" noresize>
					<frame name="mainFrame" target="mainFrame" src="mainFrame.jsp">
					<frame name="rightFrame" target="mainFrame" src="right.htm" scrolling="no" noresize>
				</frameset>
			</frameset>
		</frameset>
		<noframes>
			<body>
				<p>此网页使用了框架，但您的浏览器不支持框架。</p>
			</body>
		</noframes>
	</frameset>
</html:html>
