<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.fitech.net.adapter.StrutsMRegionDelegate"%>
<%@ page import="com.fitech.net.adapter.StrutsOrgNetDelegate"%>
<%@ page import="com.fitech.net.form.OrgNetForm"%>
<%@ page import="com.fitech.net.form.MRegionForm"%>
<%@ page import="com.fitech.net.form.OrgTypeForm"%>
<%@ page import="com.fitech.net.adapter.StrutsOrgTypeDelegate"%>
<%@ page import="com.fitech.net.adapter.StrutsMRegionDelegate"%>
<%@ page import="com.fitech.net.hibernate.OrgType"%>
<%@ page import="com.fitech.net.hibernate.OrgNet"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<html:html locale="true">
<head>
	<html:base />
	<title>机构修改</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
</head>
<%			 if(session.getAttribute("update_view")!=null)
        		session.removeAttribute("update_view");
%>
<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table cellSpacing="1" cellPadding="4" width="100%" border="0" align="center" bgcolor="#ffffff" class="tbcolor">
		<TR class="tbcolor1">
			<th align="center" height="30" colspan="2">
				<span style="FONT-SIZE: 11pt"> 机构设定</span>
			</th>
		</TR>
		<tr>
			<TD>
				<jsp:include page="/netOrg/newAddOrg/OrgInfoTree.jsp" flush="true" />
			</TD>

			<TD>
				<jsp:include page="/netOrg/newAddOrg/updateOrg.jsp" flush="true" />
			</TD>
		</tr>
	</table>
</body>
</html:html>

