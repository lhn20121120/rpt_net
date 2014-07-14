<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">

	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript">
		<%
		   String childRepId=request.getAttribute("childRepId").toString();
		   String versionId=request.getAttribute("versionId").toString();
		   String notSetCell=request.getAttribute("notSetCell").toString();
		%>
        function _submit()
        {
           var url= "<%=request.getContextPath()%>/template/changeProTmpt.do?method=showAllData&childRepId=<%=childRepId%>&versionId=<%=versionId%>";
           window.location=url;
        }
		</script>
</head>
<body style="TEXT-ALIGN: center" background="../../image/total.gif">

	<P style="MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px"></P>
	<P style="MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px"></P>
	<p style="MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px"></p>
	<table cellSpacing="0" width="96%" border="0" align="center" cellpadding="4">
		<tr>
			<td width="100%" align="right">
				<INPUT class="input-button" type="button" value="ˢ  ��" onclick="_submit()">
			</td>
		</tr>
	</table>
	<TABLE cellSpacing="0" width="96%" border="0" align="center" cellpadding="4">
		<TR>
			<TD>
				<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
					<tr class="titletab">
						<th colspan="6" align="center" id="list">
							<strong> ���趨��Ԫ���б� </strong>
						</th>
					</tr>
					<TR class="middle">
						<TD class="tableHeader" width="15%">
							<b>��Ԫ����</b>
						</TD>
						<TD class="tableHeader" width="20%">
							<b>������ʽ</b>
						</TD>
						<TD class="tableHeader" width="35%">
							<b>��ʽ</b>
						</TD>
						<TD class="tableHeader" width="10%">
							<b>Ĭ��ֵ</b>
						</TD>
						<TD class="tableHeader" width="20%">
							<b>���Ի�����</b>
						</TD>
					</TR>
					<logic:present name="Records" scope="request">
						<logic:iterate id="item" name="Records">
							<TR bgcolor="#FFFFFF">
								<TD align="center">
									<logic:notEmpty name="item" property="cellName">
										<bean:write name="item" property="cellName" />
									</logic:notEmpty>
									<logic:empty name="item" property="cellName">��</logic:empty>
								</TD>
								<TD align="center">
									<logic:notEmpty name="item" property="idrRelativeName">
										<bean:write name="item" property="idrRelativeName" />
									</logic:notEmpty>
									<logic:empty name="item" property="idrRelativeName">��</logic:empty>
								</TD>
								<TD align="center">
									<logic:notEmpty name="item" property="idrFormula">
										<bean:write name="item" property="idrFormula" />
									</logic:notEmpty>
									<logic:empty name="item" property="idrFormula">��</logic:empty>
								</TD>
								<TD align="center">
									<logic:notEmpty name="item" property="idrDefaultvalue">
										<bean:write name="item" property="idrDefaultvalue" />
									</logic:notEmpty>
									<logic:empty name="item" property="idrDefaultvalue">��</logic:empty>
								</TD>
								<TD align="center">
									<logic:notEmpty name="item" property="idrInitvalue">
										<bean:write name="item" property="idrInitvalue" />
									</logic:notEmpty>
									<logic:empty name="item" property="idrInitvalue">��</logic:empty>
								</TD>
							</TR>
						</logic:iterate>
					</logic:present>
					<logic:notPresent name="Records" scope="request">
						<tr bgcolor="#FFFFFF">
							<td colspan="5">
								��ƥ���¼
							</td>
						</tr>
					</logic:notPresent>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	<table>
		<tr>
			<td align="left" width="700">
                δ�趨��Ԫ��<%=notSetCell%>
			</td>
		</tr>
	</table>
</body>
</html:html>
