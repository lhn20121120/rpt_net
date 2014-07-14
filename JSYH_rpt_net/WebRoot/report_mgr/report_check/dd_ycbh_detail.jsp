<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
</head>
<body style="TEXT-ALIGN: center" background="../../image/total.gif">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<TABLE cellSpacing="0" width="96%" border="0" align="center" cellpadding="4">
		<tr>
			<td align="center" colspan="2">
				<logic:present name="ReportName" scope="request">
					<bean:write name="ReportName" scope="request"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td>�������У�
				<logic:present name="ReportOrg" scope="request">
					<bean:write name="ReportOrg" scope="request"/>
				</logic:present>
			</td>
			<td align="right">�������ڣ�
				<logic:present name="ReportDate" scope="request">
					<bean:write name="ReportDate" scope="request"/>
				</logic:present>
			</td>
		</tr>
	</table>
	<TABLE cellSpacing="0" width="96%" border="0" align="center" cellpadding="4">
		<TR>
			<TD>
				<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
					<tr class="titletab">
						<th colspan="6" align="center" id="list">
							<strong>
								�쳣�仯��Ϣ
							</strong>
						</th>
					</tr>
					<TR class="middle">
						<TD class="tableHeader" width="30%">
							<b>
								��Ԫ��
							</b>
						</TD>
						<TD class="tableHeader" width="10%">
							<b>
								����
							</b>
						</TD>
						<TD class="tableHeader" width="10%">
							<b>
								����������
							</b>
						</TD>
						<TD class="tableHeader" width="10%">
							<b>
								�������½�
							</b>
						</TD>
						<TD class="tableHeader" width="10%">
							<b>
								������ͬ������
							</b>
						</TD>
						<TD class="tableHeader" width="20%">
							<b>
								������ͬ���½�
							</b>
						</TD>
					</TR>
					<logic:present name="Records" scope="request">
						<logic:iterate id="item" name="Records">
						<tr>
							<td align="center"><bean:write name="item" property="cellName"/></td>
							<td align="center"><bean:write name="item" property="reportValue"/></td>
							<td align="center"><bean:write name="item" property="thanPrevRise"/></td>
							<td align="center"><bean:write name="item" property="thanPrevFall"/></td>
							<td align="center"><bean:write name="item" property="thanSameRise"/></td>
							<td align="center"><bean:write name="item" property="thanSameFall"/></td>
						</tr>	
						</logic:iterate>
					</logic:present>
					<logic:notPresent name="Records" scope="request">
						<tr bgcolor="#FFFFFF">
							<td colspan="6">
								�����쳣�仯��Ϣ
							</td>
						</tr>
					</logic:notPresent>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</body>
</html:html>
