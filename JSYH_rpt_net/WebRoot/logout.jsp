<%@ page language="java" pageEncoding="GB2312"%>
<%@ page import="com.cbrc.smis.security.Operator" %>
<%@ page import="com.cbrc.smis.common.Config" %>
<jsp:useBean id="loginBean" scope="page" class="com.cbrc.smis.security.Login"/>
<%
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String noLoginUrl = operator.getNoLoginUrl();
	loginBean.exit(request);
	//非超级用户的注销
	if(operator.isSuperManager() != true){
		response.sendRedirect("notLogin.jsp");
		%>
		<script language="javascript">
			//window.top.location="<%=noLoginUrl%>";
		</script>
		<%
	}else{	//超级用户注销系统	
		%>
		<script language="javascript">
			window.parent.location="<%=request.getContextPath()%>/preUserLogin.do";
		</script>
		<%
	}
%>


