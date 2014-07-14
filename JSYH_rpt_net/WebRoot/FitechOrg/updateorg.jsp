<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.fitech.org.UtilForm" />
<html:html locale="true">
	<head>
		<html:base/>
		<title>修改银行</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../css/common.css" rel="stylesheet" type="text/css">
	</head>
	<jsp:include page="../calendar.jsp" flush="true">
  <jsp:param name="path" value="../"/>
</jsp:include>
<script Language="JavaScript">
function validate(){
		alert("更新成功！");
		return true;
		}
	</script>
	<body>
	<center>
	<table cellspacing="0" cellpadding="0" border="0" width="98%">
		<tr>
			<td height="5"></td>
		</tr>
		<tr>
			 <td>当前位置 >> 银行管理 >> 修改银行</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
		<%
		String orgid1=(String)request.getAttribute("orgid1");
		String orgname=(String)request.getAttribute("orgname");
		String orgtype=(String)request.getAttribute("orgtype");
		String isCorp=(String)request.getAttribute("isCorp");
		if(orgname==null)
		orgname="";
		if(orgtype==null)
		orgtype="";
		if(isCorp==null)
		isCorp="";
		%>
	</table>
	<br><br><br><br>
	<html:form action="/edit" method="post" onsubmit="return validate()">
		<table cellSpacing="1" cellPadding="4" border="0" width="90%" class="tbcolor">
			<tr height="30">
				<td background="../image/barbk.jpg" align="center" colspan="8">修改银行</td>
			</tr>
			<tr bgcolor="#FFFFFF">
			    <input type="hidden" name="orgId" value="<%=orgid1%>" >
				<td width="20%">银行名称：</td>
				<td width="30%">
					<input type="text" name="orgName" value="<%=orgname%>" >
				</td>
				<TD width="40%">银行类型：</TD>
				<td width="40%">
				    <input type="text" name="orgType" value="<%=orgtype%>" >	
				</td>
				</tr>	
			<TR bgcolor="#FFFFFF">	
				
				<TD>银行分类：</TD>
			    <TD><html:select  styleId="orgClsId" property="orgClsId">
                                <html:optionsCollection name="utilForm" property="orgCls" label="label" value="value" />
								</html:select></TD>
				<TD> </TD>
				<TD width="30%"><input type="hidden" name="h1" ></TD>
			</TR>
			
		</table>
		
		<table cellSpacing="0" cellPadding="0" border="0" width="80%">
			<tr><td height="10"></td></tr>
			<tr>
				<td align="center">
				  <input type="button" class="input-button" onclick="window.history.go(-1)" value=" 返 回 ">&nbsp;
					<html:button property="back" value="查看" styleClass="input-button" onclick="window.location.assign('fitechorg.do')"/>
					<input type="submit" value=" 保存 " class="input-button">
				</td>
			</tr>
		</table>
	</html:form>
	</center>
	</body>
</html:html>