<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
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
<%        if(session.getAttribute("new_add_view")!=null)
        		session.removeAttribute("new_add_view");
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
	
			</TD>

			<TD>
			<%
			Operator operator = session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null ? (Operator) session
			.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) : new Operator();
			com.fitech.net.form.OrgNetForm orgForm=com.fitech.net.adapter.StrutsOrgNetDelegate.selectOne(operator.getOrgId(),true);
			System.out.println(orgForm.getOrg_type_id()+"--*");
			if(orgForm.getOrg_type_id().equals(new Integer(2))){
		

%>
				<jsp:include page="/netOrg/newAddOrg/AddOrg2.jsp" flush="true" />
				<%}else{ %>
					<jsp:include page="/netOrg/newAddOrg/newAddOrg.jsp" flush="true" />
					<%
					}%>
			</TD>
		</tr>
	</table>
</body>
</html:html>

