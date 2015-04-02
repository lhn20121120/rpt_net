<%@ page contentType="text/html;charset=gb2312" errorPage=""%>

<%@ page session="true" import="java.lang.StringBuffer"%>
<%@ page import="com.fitech.gznx.common.Config"%>
<%@ page import="com.fitech.gznx.security.OperatorLead"%>
<%@ page import="com.fitech.gznx.graph.GraphDataSource"%>
<%@ page import="com.fitech.gznx.graph.Table"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="random" scope="page" class="java.util.Random" />
<html>
<head>
<title></title>
</head>
<body>
<%
			
			OperatorLead operator = new OperatorLead();
			String imagePath = request.getContextPath()+"/temp/leading/"+operator.getSessionId();	
%>
<table width="98%" align="center">
	<tr>
		<td vAlign="top">
		<img title="点击看大图" src="<%=imagePath%>_zichanzongti.jpg?random=<%=random.nextDouble()%>" style="cursor:hand;" onclick="return showBigImg('<%=imagePath%>_zichanzongti','<%=random.nextDouble()%>')">
	</td>
	</tr>
	<tr>
		<td vAlign="top">
		<img title="点击看大图" src="<%=imagePath%>_fuzhaijisuoyouzhe.jpg?random=<%=random.nextDouble()%>" style="cursor:hand;" onclick="return showBigImg('<%=imagePath%>_fuzhaijisuoyouzhe','<%=random.nextDouble()%>')">
	</td>
	</tr>
</table>
<br>
</body>
</html>
<script language=javascript ></script>