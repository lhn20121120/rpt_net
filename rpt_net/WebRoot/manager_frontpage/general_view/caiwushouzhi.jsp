<%@ page contentType="text/html;charset=gb2312" errorPage=""%>
<%@ page import="com.fitech.gznx.common.Config"%>
<%@ page import="com.fitech.gznx.security.OperatorLead"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="random" scope="page" class="java.util.Random" />
<%
	OperatorLead operator = null;
//	if(session.getAttribute(Config.OPERATOR_SESSION_NAME)!=null)
//	{
//		operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_NAME);
//	}
//	else
		operator = new OperatorLead();
	String imagePath = request.getContextPath()+"/temp/leading/"+operator.getSessionId();
%>
<html>
	<head>
		<title>财务收支</title>
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="Thu,   01   Dec   1900   16:00:00   GMT">
	</head>
	<body>
		<table width="98%" align="center">
			<tr>
				<td vAlign="top">
					<img title="点击看大图" src="<%=imagePath%>_shouruxi.jpg?random=<%=random.nextDouble()%>" style="cursor:hand;" onclick="return showBigImg('<%=imagePath%>_shouruxi','<%=random.nextDouble()%>')">
				</td>
			</tr>
			<tr>
				<td vAlign="top">
					<img title="点击看大图" src="<%=imagePath%>_zhichuxi.jpg?random=<%=random.nextDouble()%>" style="cursor:hand;" onclick="return showBigImg('<%=imagePath%>_zhichuxi','<%=random.nextDouble()%>')">
				</td>
			</tr>
		</table>
	<body>
</html>

<script language=javascript ></script>