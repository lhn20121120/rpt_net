<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
	<head>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
	
	</head>
	<body>
	<html:form method="post" action="/searchReport">
	<table border="0" width="90%" align="center">
		<tr><td height="8"></td></tr>
		<tr>
			 <td>��ǰλ�� >> 	������� >> �����ر��趨</td>
		</tr>
		<tr><td height="10"></td></tr>
	</table>
					<table border="0" cellspacing="0" cellpadding="2" width="90%" align="center">
						<tr>
							<td>�������У�
							<html:text property="orgId" size="17" styleClass="input-text"/>
							</td>
							<td colspan="2">
								ģ�壺
								<html:select property="repName" size="1" >
										<html:option value="0">�����б���ģ�壩</html:option>
										<html:optionsCollection name="utilForm" property="repList" label="label" value="value"/>
								</html:select>
							</td>
						</tr>
						<tr>
							<td>���ͷ�Χ��
							<html:select property="orgType"  size="1" >
									<html:option value="0">�����б������У�</html:option>
									<html:optionsCollection name="utilFormOrg" property="orgCls" label="label" value="value"/>
							</html:select>
							</td>
							<td >
								ʱ�䣺
								<html:text property="startDate" maxlength="10" styleClass="input-text" size="10"/>
								-
								<html:text property="endDate" maxlength="10" styleClass="input-text" size="10"/>
							</td>
							<td>
								<html:submit styleClass="input-button" value=" �� ѯ "/>
							</td>
						</tr>
					</table>
		</html:form>
	
	<html:form method="post" action="/searchReport">
		<table cellSpacing="1" cellPadding="4" border="0" width="98%" class="tbcolor">
			<tr height="30">
				<td background="../../image/barbk.jpg" align="center" colspan="4">�����ر��趨</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%">��������</td>
				<td width="30%"><bean:write name="" property=""/>
				</td>
				<td width="20%">�汾�ţ�</td>
				<td width="30%"><bean:write name="" property=""/>
				</td>
			</tr>
			<TR bgcolor="#FFFFFF">
				<TD>���У�</TD>
				<TD><bean:write name="" property=""/>
				</TD>
				<TD>���֣�</TD>
				<TD><bean:write name="" property=""/>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD>���ݷ�Χ��</TD>
				<TD><bean:write name="" property=""/>
				</TD>
				<TD>ģ����ݣ�</TD>
				<TD><bean:write name="" property=""/>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD>����������</TD>
				<TD colspan="3"><bean:write name="" property=""/>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD>�ر�ԭ��</TD>
				<td colspan="3"></td>
			</TR>
			<tr bgcolor="#FFFFFF">
				<td></td>
				<td colspan="3">
				<html:textarea property="" styleClass="input-text" cols="130" rows="6" />
				</td>
			</tr>
		</table>
	</html:form>
	<html:form method="post" action="/config/ViewReport">
		<table cellSpacing="0" cellPadding="0" border="0" width="80%">
			<tr><td height="10"></td></tr>
			<tr>
				<td align="center">
					<input type="button" class="input-button" onclick="window.history.back()" value=" �� �� ">&nbsp; 
					<html:cancel styleClass="input-button" value=" ȡ �� "/>&nbsp;
					<html:submit styleClass="input-button" value=" �� �� "/>
				</td>
			</tr>
		</table>
	</html:form>	
	</body>
</html:html>