<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm" />
<jsp:useBean id="utilFormOrg" scope="page" class="com.fitech.net.common.UtilForm" />

<html:html locale="true">

	<head>
		<title>Title</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
	</head>
	<body >
	<table border="0" width="90%" align="center">
		<tr><td height="8"></td></tr>
		<tr>
			 <td>��ǰλ�� >> 	���ݲ�ѯ >> �����ر��趨</td>
		</tr>
	</table>
	<html:form action="/searchReport" method="post">
		<table cellspacing="0" cellpadding="0" border="0" width="95%" align="center">
			<tr>
				<td>�������У�
					<input name="T1" size="17">
				</td>
				<td>���ƣ�
						<html:select property="repName">
							<html:option value="0">�����б������ƣ�</html:option>
							<html:optionsCollection name="utilForm" property="repList"/>
						</html:select>
				</td>
				
				<td>���ͷ�Χ��
						<html:select property="childRepId">
							<html:option value="0">�����б������У�</html:option>
							<html:optionsCollection name="utilFormOrg" property="orgCls"/>
						</html:select>
				</td>
				<td>
					<html:submit styleClass="input-button" value=" �� ѯ "/>
				</td>
			</tr>
			<table cellspacing="0" cellpadding="0" border="0" width="95%" align="center">
				<tr>
					<td>
						ʱ�䣺
						<input name="startDate" type="text" maxlength="10" class="input-text" size="10">
							-
						<input name="endDate" type="text" maxlength="10" class="input-text" size="10">
					</td>
						<td align="center">
							<INPUT id="Radio9" type="radio" value="Radio20" name="RadioGroup">�����¼
							<strong><font  size="2">��</font></strong>&nbsp;
							<INPUT id="Radio5" type="radio" value="Radio16" name="RadioGroup">��ȷ��¼
							<strong><font  size="2">��</font></strong>&nbsp;
							<INPUT id="Radio6" type="radio" value="Radio17" name="RadioGroup">����¼
							<strong><font size="2" >��</font></strong>&nbsp;
							<INPUT id="Radio7" type="radio" value="Radio18" name="RadioGroup">©����¼
							<strong><font size="2" >��</font></strong>&nbsp;
							<INPUT id="Radio10" type="radio" value="Radio21" name="RadioGroup">�쳣��¼
							<strong><font size="2" >��</font></strong>&nbsp;
							<INPUT id="Radio8" type="radio" value="Radio19" name="RadioGroup" CHECKED>ȫ����¼
						</td>
				</tr>
			</table>
		</table>
	</html:form>
  </body>
</html:html>
