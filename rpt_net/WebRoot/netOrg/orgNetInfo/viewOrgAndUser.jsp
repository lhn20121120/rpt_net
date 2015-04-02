<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="style/globalStyle.css" rel="stylesheet" type="text/css">
		<script src="script/globalScript.js" type="text/javascript" language="javascript"></script>
		<base target="mainFrame">
	</head>
	<frameset rows="60%,*" cols="*" border="0">
		<frame name="showOrgInfo" scrolling="no" noresize target="contents" src="<%=request.getContextPath()%>/netOrg/orgNetInfo/viewOrgInFo.jsp">
		<frame name="showUserInfo"  noresize target="contents" src="<%=request.getContextPath()%>/netOrg/orgNetInfo/viewUserInFo.jsp">
		<noframes>
			<body>
				<p>此网页使用了框架，但您的浏览器不支持框架。</p>
			</body>
		</noframes>
	</frameset>
</html>