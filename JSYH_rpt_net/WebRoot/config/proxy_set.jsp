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
		<link href="../css/common.css" rel="stylesheet" type="text/css">
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
		<html:form action="/config/viewMToRepOrg" method="POST">
			<table border="0" width="85%">
				<tbody>
					<tr>
						<td colspan="2" height="1"></td>
					</tr>
					<tr>
						<td align="center">
							上报子行名称：<html:text property="orgName" size="40" styleClass="input-text"/>
						</td>
						<td align="left">
							<html:submit styleClass="input-button" value="查询"/>
						</td>
					</tr>
					<tr>
						<td colspan="2" height="10"></td>
					</tr>
				</TBODY>
			</table>
		</html:form>
		
		<table cellspacing="0" border="0" width="90%">
			<tr>
				<td>
					<table width="95%" border="0" align="center">
						<tr>
							<td width="100%" align="center"><strong>上报子行列表</strong></td>
						<tr>
					</table>
					<table cellSpacing="1" cellPadding="4" width="95%" border="0" align="center" class="tbColor">
						<tr>
							<td width="5%" align="center">序号</td>
							<td width="25%" align="center">上报子行名称</td>
							<td width="10%" align="center">子行类型</td>
							<td width="15%" align="center">起始时间</td>
							<td width="15%" align="center">终止时间</td>
							<td width="15%" align="center">代报时间设定</td>
							<td width="15%" align="center">代报详细信息</td>
						</tr>
						<logic:present name="Records" scope="request">
							<logic:iterate indexId="ind" id="item" name="Records">
								<TR bgcolor="#FFFFFF">
									<TD vAlign="middle" noWrap align="center" height="30">
										<font face="Arial" size="2">
											<bean:write name="ind"/>
										</font>
									</TD>
									<TD vAlign="middle" noWrap align="center" height="30" >
										<font size="2"><bean:write name="item" property="orgName"/></font>
									</TD>
									<TD vAlign="middle" noWrap align="center" height="30">
										<bean:write name="item" property="orgType"/>																		
									</TD>
									<TD vAlign="middle" noWrap align="center" height="30">
										<bean:write name="item" property="startDate"/>
									</TD>
									<TD vAlign="middle" noWrap align="center" height="30">
										<bean:write name="item" property="endDate"/>
									</TD>
									<TD vAlign="middle" noWrap align="center" height="30">
										<a href=".do?<bean:write name="item" property="orgId"/>">设定</a>
									</TD>
									<td vAlign="middle" noWrap align="center" height="30">
										<a href=".do?<bean:write name="item" property="orgId"/>">查看</a>
									</td>
								</TR>
							</logic:iterate>
						</logic:present>
						<logic:notPresent name="Records" scope="request">
							<tr align="left">
									<td bgcolor="#ffffff" colspan="7">
										无匹配上报子行
									</td>
							</tr>
						</logic:notPresent>	
					</table>
				</td>
			</tr>
		</table>
			<table width="80%" border="0">
				<TR>
					<TD width="100%">
						<jsp:include page="../apartpage.jsp" flush="true">
							<jsp:param name="url" value="viewMToRepOrg.do" />
						</jsp:include>
					</TD>
				</tr>
			</table>
		</center>
	</body>
</html:html>