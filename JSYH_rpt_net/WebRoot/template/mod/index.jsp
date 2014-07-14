<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
	<head>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<script src="../css/common.js" type="text/javascript" language="javascript"></script>
	</head>
	<frameset rows="60,*" border="0">
		<frame name="title" scrolling="no" noresize target="contents" src="mod/search.jsp">
		<frame name="contents" noresize target="contents" src="mod/init.jsp">
		<noframes>
			<body>
				<p>此网页使用了框架，但您的浏览器不支持框架。</p>
			</body>
		</noframes>
	</frameset>
</html:html>
