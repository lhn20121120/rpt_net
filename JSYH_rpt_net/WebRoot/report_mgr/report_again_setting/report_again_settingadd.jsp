<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm" />
<jsp:useBean id="utilFormOrg" scope="page" class="com.fitech.net.common.UtilForm" />
<html:html locale="true">
	<head>
		<html:base/>
		<title>重报管理设定</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
	</head>
	<jsp:include page="../../calendar.jsp" flush="true">
  <jsp:param name="path" value="../../"/>
</jsp:include>
	<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<center>
	<table cellspacing="0" cellpadding="0" border="0" width="98%">
		<tr>
			<td height="5"></td>
		</tr>
		<tr>
			 <td>当前位置 >> 	报表管理 >> 报表重报设定 >> 新增重报设定</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
	<br><br><br><br>
	<html:form action="/newReportAgainSetting" method="post">
	<html:hidden property="repInId" />
		<table cellSpacing="1" cellPadding="4" border="0" width="80%" class="tbcolor">
			<tr height="30" class="titletab">
				<th align="center" colspan="4" >新增重报设定</th>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%">报表名：</td>
				<td width="30%">
					<html:text property="repName" size="30" styleClass="input-text" readonly="true"/>
				</td>
				<td width="20%">版本号：</td>
				<td width="30%">
					<html:text property="versionId" size="7" styleClass="input-text" readonly="true"/>
				</td>
			</tr>
			<TR bgcolor="#FFFFFF">
				<TD>子行：</TD>
				<TD><html:text property="orgName" size="17" styleClass="input-text" readonly="true"/></TD>
				<TD>币种：</TD>
				<TD><html:select property="curId" disabled="true">
				                        <html:option value="">（币种未知）</html:option>
									    <html:optionsCollection name="utilForm" property="curIds"/>
								</html:select></TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD>数据范围：</TD>
				<TD><html:select property="dataRangeId" disabled="true">
				         <html:option value="">（数据范围未知）</html:option>
									    <html:optionsCollection name="utilForm" property="dataRgTypes"/>
								</html:select></TD>
				<TD>模板年份：</TD>
				<TD>
					<html:text property="year" size="4"  readonly="true"/>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD>报表期数：</TD>
				<TD><html:text  size="4" property="term" readonly="true"/></TD>
				<TD>重报设置日期：</TD>
				<TD><html:text property="setDate" maxlength="10" styleClass="input-text" size="10" onclick="return showCalendar('setDate', 'y-mm-dd');" readonly="true"/></TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD>重报原因：</TD>
				<td colspan="3"><html:text  property="cause"  size="80"/></td>
			</tr>
		</table>
		
		<table cellSpacing="0" cellPadding="0" border="0" width="80%">
			<tr><td height="10"></td></tr>
			<tr>
				<td align="center">
					<input type="button" class="input-button" onclick="window.history.go(-1)" value=" 返 回 ">&nbsp;&nbsp;
					<html:submit  value=" 保存 " styleClass="input-button"/>
				</td>
			</tr>
		</table>
	</html:form>
	</center>
	</body>
</html:html>