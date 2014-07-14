<%@ page contentType="text/html;charset=gb2312" errorPage=""%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="random" scope="page" class="java.util.Random" />
<%
	String image_src = request.getParameter("src")==null?"":request.getParameter("src");
%>
<html>
	<head>
		<title>图表放大展示</title>
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<link href="../../css/common.css" type="text/css" rel="stylesheet">
		<meta http-equiv="Expires" content="Thu,   01   Dec   1900   16:00:00   GMT">
	</head>
	<script>
		function Goback(){
			window.location="<%=request.getContextPath()%>/manager_frontpage/general_view/view_query.jsp";
		}
	</script>
	<body>
		<table width="100%" align="center">
			<tr>
				<td align="right">
				  <input type="button" class="input-button" value="返  回" onclick = "return Goback()">
				<td>
			</tr>
			<tr>
				<td vAlign="top" align="center">
					<img src="<%=image_src%>" style="cursor:hand;">
				</td>
			</tr>
		</table>
	<body>
</html>

<script language=javascript ></script>