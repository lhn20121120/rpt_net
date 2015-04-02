<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<LINK href="../../style/globalStyle.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="../../script/globalScript.js" type="text/javascript"></script>
		<base target="main">
	</head>
	<frameset border="0" frameSpacing="0" rows="110,*" frameBorder="0">
		<frame name="title" src="search_title.jsp" noResize scrolling="auto" target="contents">
		<frame name="contents" src="search_content_view.jsp" noResize target="contents">
		<noframes>
			<body>
				<p>此网页使用了框架，但您的浏览器不支持框架。</p>
			</body>
		</noframes>
	</frameset>
</html:html>
