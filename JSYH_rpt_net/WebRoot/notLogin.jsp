<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=GB2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
<head>
	<html:base/>
	<title>未登录系统</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<script language="javascript">
	  top.window.location="<%=request.getContextPath()%>/preUserLogin.do";
	</script>
</head>
<body>  
</body>
</html:html>