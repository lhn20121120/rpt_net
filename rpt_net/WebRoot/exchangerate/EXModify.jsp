<%@ page language="java" pageEncoding="GBK"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm" />
<html>
	<head>
		<title>���ʹ���</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="../css/common.css" rel="stylesheet" type="text/css">
	</head>
	<body scroll=yes><html:form method="post" action="/exchangerate/update">
		<table width="106%" height=100% border="0" align="center" cellpadding="0" cellspacing="0" style="width:100��">
			<tr>
				<td height=25 id=location>
					������λ�ã����ʹ���&gt;&gt; �����޸�
				</td>
			</tr>
			<tr>
				<td valign="top" id=operation>
					<html:submit styleClass="input-button" value="�� ��"></html:submit>
					<input name="Submit3" type="button" class="input-button" value="�� ��" onClick="history.back();seach/frxx.htm">
					<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
						<tr>
							<td>
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<fieldset id="fieldset">
									<legend>
										<strong>���ʹ���</strong>
									</legend>
									
										<TABLE width="100%" border="0" align="center" cellpadding="2" cellspacing="2">
											<TBODY>
												<TR>
													<TD width="100" align="right">
														��������:
													</TD>
													<TD align="left">
													<html:hidden name='exChangeRateForm' property='vcid'/>
													<html:hidden name='exChangeRateForm' property='vdid'/>
														<bean:write name='exChangeRateForm' property='vccnname' />
													</TD>
													<TD align="right">
														Ӣ������:
													</TD>
													<TD width="300" align="left">
														<bean:write name='exChangeRateForm' property='vcenname' />
													</TD>
												</TR>
												<TR>
													<TD width="100" align="right">
														����:
													</TD>
													<TD>
														<bean:write name='exChangeRateForm' property='vdname' />
													</TD>
													<TD align="right">
														����:
													</TD>
													<TD>
														<html:text property="eeramt" styleClass="input-text" size="10" maxlength="10" />
													</TD>
												</TR>

											</TBODY>
										</TABLE>
									
								</fieldset>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table></html:form>
	</body>	
</html>