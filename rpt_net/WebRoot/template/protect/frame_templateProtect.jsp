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
    <%
        String childRepId=request.getAttribute("childRepId")!=null?request.getAttribute("childRepId").toString():null;
        String versionId=request.getAttribute("versionId")!=null?request.getAttribute("versionId").toString():null;
        String ocx=request.getAttribute("ocx")!=null?request.getAttribute("ocx").toString():null;
        String obtain_js=request.getAttribute("obtain_js")!=null?request.getAttribute("obtain_js").toString():null;
        String excelName=request.getAttribute("excelName")!=null?request.getAttribute("excelName").toString():null;
        String idrId=request.getAttribute("idrId")!=null?request.getAttribute("idrId").toString():null;
        String idrRelative=request.getAttribute("idrRelative")!=null?request.getAttribute("idrRelative").toString():null;
        String idrFormula=request.getAttribute("idrFormula")!=null?request.getAttribute("idrFormula").toString():null;
        String idrDefaultvalue=request.getAttribute("idrDefaultvalue")!=null?request.getAttribute("idrDefaultvalue").toString():null;
        String idrInitvalue=request.getAttribute("idrInitvalue")!=null?request.getAttribute("idrInitvalue").toString():null; 
    %>

	<frameset rows="75%,*" cols="*" border="0">
		<frame name="showexcel" scrolling="no" noresize target="contents" src="../template/protect/showExcel.jsp?reportUrl=<bean:write name="reportUrl"/>&childRepId=<%=childRepId%>&versionId=<%=versionId%>&ocx=<%=ocx%>">
		<frame src="../template/protect/setExcel.jsp?css=<bean:write name="css"/>&childRepId=<%=childRepId%>&versionId=<%=versionId%>&obtain_js=<%=obtain_js%>&excelName=<%=excelName%>&idrId=<%=idrId%>&idrRelative=<%=idrRelative%>&idrFormula=<%=idrFormula%>&idrDefaultvalue=<%=idrDefaultvalue%>&idrInitvalue=<%=idrInitvalue%>" name="setExcel" noresize>
		<noframes>
			<body>
				<p>此网页使用了框架，但您的浏览器不支持框架。</p>
			</body>
		</noframes>
	</frameset>
</html>