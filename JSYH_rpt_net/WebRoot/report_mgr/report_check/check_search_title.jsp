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
			 <td>当前位置 >> 	数据查询 >> 报表重报设定</td>
		</tr>
	</table>
	<html:form action="/searchReport" method="post">
		<table cellspacing="0" cellpadding="0" border="0" width="95%" align="center">
			<tr>
				<td>报送子行：
					<input name="T1" size="17">
				</td>
				<td>名称：
						<html:select property="repName">
							<html:option value="0">（所有报表名称）</html:option>
							<html:optionsCollection name="utilForm" property="repList"/>
						</html:select>
				</td>
				
				<td>报送范围：
						<html:select property="childRepId">
							<html:option value="0">（所有报送子行）</html:option>
							<html:optionsCollection name="utilFormOrg" property="orgCls"/>
						</html:select>
				</td>
				<td>
					<html:submit styleClass="input-button" value=" 查 询 "/>
				</td>
			</tr>
			<table cellspacing="0" cellpadding="0" border="0" width="95%" align="center">
				<tr>
					<td>
						时间：
						<input name="startDate" type="text" maxlength="10" class="input-text" size="10">
							-
						<input name="endDate" type="text" maxlength="10" class="input-text" size="10">
					</td>
						<td align="center">
							<INPUT id="Radio9" type="radio" value="Radio20" name="RadioGroup">错误记录
							<strong><font  size="2">×</font></strong>&nbsp;
							<INPUT id="Radio5" type="radio" value="Radio16" name="RadioGroup">正确记录
							<strong><font  size="2">√</font></strong>&nbsp;
							<INPUT id="Radio6" type="radio" value="Radio17" name="RadioGroup">错报记录
							<strong><font size="2" >※</font></strong>&nbsp;
							<INPUT id="Radio7" type="radio" value="Radio18" name="RadioGroup">漏报记录
							<strong><font size="2" >¤</font></strong>&nbsp;
							<INPUT id="Radio10" type="radio" value="Radio21" name="RadioGroup">异常记录
							<strong><font size="2" >？</font></strong>&nbsp;
							<INPUT id="Radio8" type="radio" value="Radio19" name="RadioGroup" CHECKED>全部记录
						</td>
				</tr>
			</table>
		</table>
	</html:form>
  </body>
</html:html>
