<%@ page contentType="text/html;charset=gb2312" errorPage=""%>
<%@ page session="true"%>
<%@ page import="com.fitech.gznx.common.Config"%>
<%@ page import="com.fitech.gznx.util.FitechUtil"%>

<%@ page import="com.fitech.gznx.form.LendingQueryForm"%>
<%@ page import="com.fitech.gznx.entity.GraphBean"%>
<%@ page import="com.fitech.gznx.security.OperatorLead"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.io.File"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="random" scope="page" class="java.util.Random" />
<%OperatorLead operator = null;

				operator = new OperatorLead();
			String imagePath = request.getContextPath()+"/temp/leading/"+operator.getSessionId();	
%>
<html>
	<head>
		<title>盈利情况</title>
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="Thu,   01   Dec   1900   16:00:00   GMT">
	</head>
	<body>
		<table width="98%" align="center">
			<tr>
				<td align="left">
					<img title="点击看大图" src="<%=imagePath%>_yinglibiandong.jpg?random=<%=random.nextDouble()%>" style="cursor:hand;"  onclick="return showBigImg('<%=imagePath%>_yinglibiandong','<%=random.nextDouble()%>')">
				</td>
			</tr>
		<%--if(operator.getOrgID().equals("99999")){--%>
		<%-- 	<tr>
				<td align="left">
					<img title="点击看大图" src="<%=imagePath%>_zichanlirunbiandong_gefenhang.jpg?random=<%=random.nextDouble()%>" style="cursor:hand;"  onclick="return showBigImg('<%=imagePath%>_zichanlirunbiandong_gefenhang','<%=random.nextDouble()%>')">
				</td>
			</tr>
			--%>
		</table>
		<%--}--%>
		<br>
		<%--if(operator.getOrgID().equals("99999")){--%>
		<!--<table width="98%" align="center">
			<tr>
				<td align="center">
					<img title="点击看大图" src="<%=imagePath%>_zichanlirunbiandong.jpg?random=<%=random.nextDouble()%>" style="cursor:hand;"  onclick="return showBigImg('<%=imagePath%>_zichanlirunbiandong','<%=random.nextDouble()%>')">
				</td>
			</tr>
		</table>
		-->
		<%--}--%>
	</body>
</html>

<script language=javascript ></script>