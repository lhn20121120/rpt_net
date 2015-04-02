<%@ page language="java" pageEncoding="GB2312"%>
<%@ page import="com.cbrc.smis.security.Operator" %>
<%@ page import="com.cbrc.smis.common.Config" %>
<jsp:useBean id="loginBean" scope="page" class="com.cbrc.smis.security.Login"/>
<%
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String noLoginUrl = operator.getNoLoginUrl();
	String portal_url = (String)session.getAttribute("portal_url");
//	session.invalidate();
	//非超级用户的注销
	if(true || operator.isSuperManager() != true){
		if(Config.PORTAL){%>
			<script language="javascript">
				window.parent.location='/portal/logout.jsp';
			</script>
		<%}else{
			session.invalidate();
			if(portal_url!=null && !portal_url.trim().equals(""))
				response.sendRedirect(portal_url);
			else
				response.sendRedirect("notLogin.jsp");
		}
	}else{	//超级用户注销系统	
		%>
		<script language="javascript">
			window.parent.location="<%=request.getContextPath()%>/preUserLogin.do";
		</script>
		<%
	}
%>


