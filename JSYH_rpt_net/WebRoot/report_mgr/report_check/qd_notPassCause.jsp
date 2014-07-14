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
	<TABLE cellSpacing="0" width="96%" border="0" align="center" cellpadding="4">
		<TR>
			<TD>
				<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
					<tr class="titletab">
						<th colspan="4" align="center" id="list">
							<strong> 校验不通过原因 </strong>
						</th>
					</tr>
					<logic:present name="Records" scope="request">
						<logic:iterate id="item" name="Records">
							<tr bgcolor="#FFFFFF">
								<td>
									<bean:write name="item" property="cellFormuView" />
								</td>
								<td align="center">
									<font color="red">校验未通过</font>
								</td>
								<td>
									<bean:write name="item" property="cause" />
								</td>
							</tr>
						</logic:iterate>
					</logic:present>
					<tr>
					</tr>
					<tr>
						<td align="center">
							<a href="javascript:window.close()">关闭窗口</a>
						</td>
					</tr>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</body>
</html:html>
