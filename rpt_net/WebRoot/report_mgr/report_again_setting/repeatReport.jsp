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
			 <td>当前位置 >> 	数据审核 >> 报表重报设定</td>
		</tr>
		<tr><td height="10"></td></tr>
	</table>
					<table border="0" cellspacing="0" cellpadding="2" width="90%" align="center">
						<tr>
							<td>报送子行：
							<html:text property="orgId" size="17" styleClass="input-text"/>
							</td>
							<td colspan="2">
								模板：
								<html:select property="repName" size="1" >
										<html:option value="0">（所有报表模板）</html:option>
										<html:optionsCollection name="utilForm" property="repList" label="label" value="value"/>
								</html:select>
							</td>
						</tr>
						<tr>
							<td>报送范围：
							<html:select property="orgType"  size="1" >
									<html:option value="0">（所有报送子行）</html:option>
									<html:optionsCollection name="utilFormOrg" property="orgCls" label="label" value="value"/>
							</html:select>
							</td>
							<td >
								时间：
								<html:text property="startDate" maxlength="10" styleClass="input-text" size="10"/>
								-
								<html:text property="endDate" maxlength="10" styleClass="input-text" size="10"/>
							</td>
							<td>
								<html:submit styleClass="input-button" value=" 查 询 "/>
							</td>
						</tr>
					</table>
		</html:form>
	
	<html:form method="post" action="/searchReport">
		<table cellSpacing="1" cellPadding="4" border="0" width="98%" class="tbcolor">
			<tr height="30">
				<td background="../../image/barbk.jpg" align="center" colspan="4">报表重报设定</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%">报表名：</td>
				<td width="30%"><bean:write name="" property=""/>
				</td>
				<td width="20%">版本号：</td>
				<td width="30%"><bean:write name="" property=""/>
				</td>
			</tr>
			<TR bgcolor="#FFFFFF">
				<TD>子行：</TD>
				<TD><bean:write name="" property=""/>
				</TD>
				<TD>币种：</TD>
				<TD><bean:write name="" property=""/>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD>数据范围：</TD>
				<TD><bean:write name="" property=""/>
				</TD>
				<TD>模板年份：</TD>
				<TD><bean:write name="" property=""/>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD>报表期数：</TD>
				<TD colspan="3"><bean:write name="" property=""/>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD>重报原因：</TD>
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
					<input type="button" class="input-button" onclick="window.history.back()" value=" 返 回 ">&nbsp; 
					<html:cancel styleClass="input-button" value=" 取 消 "/>&nbsp;
					<html:submit styleClass="input-button" value=" 保 存 "/>
				</td>
			</tr>
		</table>
	</html:form>	
	</body>
</html:html>