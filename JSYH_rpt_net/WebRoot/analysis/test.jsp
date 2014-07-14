<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<% 
	String childRepId = request.getParameter("childRepId")==null?(request.getAttribute("childRepId")!=null?request.getAttribute("childRepId").toString():null):request.getParameter("childRepId");
	String versionId = request.getParameter("versionId")==null?(request.getAttribute("versionId")!=null?request.getAttribute("versionId").toString():null):request.getParameter("versionId");
	 
 %>
 <jsp:useBean id="utilFormBean" scope="page" class="com.cbrc.smis.form.UtilForm" />
<jsp:useBean id="utilCellForm" scope="page" class="com.fitech.net.form.UtilCellForm" />
<jsp:setProperty property="childRepId" name="utilCellForm" value="<%=childRepId%>"/>
<jsp:setProperty property="versionId" name="utilCellForm" value="<%=versionId%>"/>
<html:html locale="true">
<head>
	<html:base />
</head>
<body background="../image/total.gif">
	 <html:form action="/selForseReportAgain" method="post">
					<html:select property="repName" styleId="cellId" onchange="changeCell()">									
						 <html:optionsCollection name="utilCellForm" property="cellList"/>
						</html:select>									
</html:form>
	
</body>
</html:html>
