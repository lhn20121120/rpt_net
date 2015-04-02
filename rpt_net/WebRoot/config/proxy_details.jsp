<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
	<head>
	<html:base/>
		<title>代报关系设定</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../css/common.css" type="text/css" rel="stylesheet">
		<jsp:include page="../calendar.jsp" flush="true">
		  <jsp:param name="path" value="../"/>
		</jsp:include>
	</head>
	<body>
		<center>
		<table cellspacing="0" cellpadding="2" border="0" width="85%">
			<TBODY>
				<tr>
					<td colspan="3" height="5"></td>
				</tr>
				<tr background="../image/search_title_bg.gif">
					<td>
						当前位置 >> 参数设定 >> 代报关系设定
					</td>
				</tr>
				<tr>
					<td colspan="3" height="10"></td>
				</tr>
			</TBODY>
		</table>
		<table cellspacing="0" border="0" width="80%">
			<tr>
				<td>
					<table width="95%" border="0" align="center">
						<tr>
							<td width="100%" align="center"><strong>北京银行</strong></td>
						<tr>
					</table>
					<fieldset style="padding: 2" align="center">
						<legend><strong>被代报机构列表</strong></legend>
					<br>
					<table cellSpacing="1" cellPadding="4" width="95%" border="0" align="center" class="tbColor">
						<tr>
							<td width="10%" align="center">序号</td>
							<td width="40%" align="center">上报机构名称</td>
							<td width="50%" align="center">机构地址</td>
						</tr>
						<logic:present name="" scope="request">
							<logic:iterate indexId="ind" id="item" name="">
								<TR bgcolor="#FFFFFF">
									<TD vAlign="middle" noWrap align="center" height="30">
										<font face="Arial" size="2">
											<bean:write name="ind"/>
										</font>
									</TD>
									<TD vAlign="middle" noWrap align="center" height="30" >
										<font size="2"><bean:write name="item" property=""/></font>
									</TD>
									<TD vAlign="middle" noWrap align="center" height="30">
										<bean:write name="item" property=""/>																		
									</TD>
								</TR>
							</logic:iterate>
						</logic:present>
						<logic:notPresent name="Records" scope="request">
							<tr align="left">
									<td bgcolor="#ffffff" colspan="3">
										无匹配被代报机构
									</td>
							</tr>
						</logic:notPresent>		
					</table>
				<div align="center">
					<table width="95%" border="0">
						<TR>
							<TD width="80%">
								<jsp:include page="../apartpage.jsp" flush="true">
									<jsp:param name="url" value="" />
								</jsp:include>
							</TD>
							<TD width="20%" align="right">
								<html:button property="back" styleClass="input-button" onclick="window.history.back()" value=" 返回 " />
							</TD>
						</tr>
					</table>
				</div>
					<br>
					</fieldset>
				</td>
			</tr>
		</table>
		</center>
	</body>
</html:html>