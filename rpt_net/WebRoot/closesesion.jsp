<%@ page language="java" pageEncoding="GB2312"%>
<%@ page import="com.cbrc.smis.security.Operator" %>
<%@ page import="com.cbrc.smis.common.Config" %>
<jsp:useBean id="loginBean" scope="page" class="com.cbrc.smis.security.Login"/>
<%
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String noLoginUrl = operator.getNoLoginUrl();
	loginBean.exit(request);
	
	%>
<script language="javascript">
	window.close;
</script>


