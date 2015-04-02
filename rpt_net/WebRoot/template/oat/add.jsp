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
	<script language="javascript">
			function validate()
			{
				//类别名
				var OATName = document.getElementById('OATName');
				//类别注释
				var OATMemo = document.getElementById('OATMemo');
				if(OATName.value=="")
				{
					alert("类别名不能为空");
					OATName.focus();
					return false;
				}
				if(OATMemo.value.length>150)
				{
					alert("频度类别注释的长度不能超过150");
					OATMemo.focus();
					return false;
				}
				
			}
	</script>
</head>
<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
					alert("<bean:write name='Message' property='alertMsg'/>");
				</script>
		</logic:greaterThan>
	</logic:present>
	<table border="0" cellpadding="0" cellspacing="0" width="96%">
		<tr><td height="5"></td></tr>
		<tr>
			<td height="10">
				当前位置 >> 模板管理 >> 频度类别设定 >> 新增类别
			</td>
		</tr>
		<tr><td height="5"></td></tr>
	</table>
	<TABLE name="tbl" id="tbl" cellSpacing="1" cellPadding="1" width="80%" border="0" class="tbcolor" align="center">
		<tr class="titletab">
			<TD class="tableHeader" align="center" height="20">新增频度类别</td>
		</TR>
		<tr>
			<td bgcolor="#FFFFFF">
				<html:form method="post" action="/template/oat/OATAdd" onsubmit="return validate()">
				<table border="0" width="100%" cellpadding="4" cellspacing="0">
					<tr>
						<td>类别名称：</td>
						<td><html:text property="OATName" size="50" maxlength="100" styleClass="input-text"/></td>
					</tr>
					<tr>
						<td>备注：</td>
						<td><html:textarea property="OATMemo" cols="50" rows="10"/></td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<html:submit value="确定" styleClass="input-button"/>&nbsp;
							<html:button property="back" value="返回" styleClass="input-button" onclick="window.location.assign('../oat/OATInit.do')"/>
						</td>
					</tr>
				</table>
				</html:form>
			</td>
		</tr>
	</table>
</body>
</html:html>
