<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.fitech.org.UtilForm" />
<html:html locale="true">
	<head>
		<html:base/>
		<title>新增银行</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../css/common.css" rel="stylesheet" type="text/css">
	</head>

	<jsp:include page="../calendar.jsp" flush="true">
  <jsp:param name="path" value="../"/>
</jsp:include>
<script language="javascript">
			function validate()
			{
				var txtRoleName = document.getElementById('orgId');
				if(txtRoleName.value=="")
				{
					alert("银行ID不能为空！");
					txtRoleName.focus();
					return false;
				}
				else
				{　 alert("添加成功");
					return true;
					}
			}
	</script>
	<body>
	<center>
	<table cellspacing="0" cellpadding="0" border="0" width="98%">
		<tr>
			<td height="5"></td>
		</tr>
		<tr>
			 <td>当前位置 >> 银行管理 >> 新增银行</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
	<br><br><br><br>
	<html:form action="/neworg" method="post" onsubmit="return validate()">
		<table cellSpacing="1" cellPadding="4" border="0" width="90%" class="tbcolor">
			<tr height="30">
				<td background="../image/barbk.jpg" align="center" colspan="8">新增银行</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="10%">银行ID：</td>
				<td width="20%">
					<html:text property="orgId" size="10" styleClass="input-text"/>
				</td>
				<td width="20%">银行名称：</td>
				<td width="30%">
					<html:text property="orgName" size="30" styleClass="input-text"/>
				</td>
				</tr>
				<tr bgcolor="#FFFFFF">
				<TD width="40%">银行类型：</TD>
				<td width="40%">
					<html:text property="orgType" size="30" styleClass="input-text"/>
				</td>
				
			
				<TD>银行分类：</TD>
			<TD ><html:select property="orgClsId">
									    <html:optionsCollection name="utilForm" property="orgCls" label="label" value="value" />
								</html:select></TD>
			</TR>
			
		</table>
		
		<table cellSpacing="0" cellPadding="0" border="0" width="80%">
			<tr><td height="10"></td></tr>
			<tr>
				<td align="center">
				
				<html:button property="back" value="返回" styleClass="input-button" onclick="window.location.assign('../fitechorg.do')"/>
				<input type="submit" value=" 保存 " class="input-button">
				</td>
			</tr>
		</table>
	</html:form>
	</center>
	</body>
</html:html>