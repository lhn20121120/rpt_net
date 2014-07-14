<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base />
	<title>部门信息管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript">
		function deleteRole(roleId)
		{
			if(confirm("你确定要删除该角色信息吗?"))
				window.location = "../popedom_mgr/deleteRole.do?roleId="+roleId;
			
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
	<table cellspacing="0" cellpadding="0" border="0" width="98%">
		<tr>
			<td height="5"></td>
		</tr>
		<tr>
			<td>
				当前位置 >> 系统管理 >> 角色管理
			</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
	<br>
	<table width="95%" border="0" align="center">

		<tr>
			<td align="right">
				<html:button property="addDept" value="增加角色"
					styleClass="input-button"
					onclick="location.assign('./role_mgr/role_add.jsp')" />
			</td>
		</tr>
		<tr>
			<td height="5">
			<td>
		</tr>
	</table>
	<table width="90%" border="0" cellpadding="4" cellspacing="1"
		class="tbcolor">
		<tr class="titletab">
			<th height="25" align="center" colspan="6">
				角色基本信息
			</th>
		</tr>
		<tr align="center" class="middle">
			<td align="center" width="15%">
				序号
			</td>
			<td align="center" width="25%">
				角色名称
			</td>
			<td align="center" width="15%">
				修改
			</td>
			<td align="center" width="15%">
				删除
			</td>
			<td align="center" width="15%">
				菜单功能分配
			</td>
		</tr>
		<logic:present name="Records" scope="request">
			<logic:iterate id="item" name="Records" indexId="index">
				<tr align="center">
					<td bgcolor="#ffffff">
						<%=((Integer) index).intValue()
												+ 1%>
					</td>
					<td bgcolor="#ffffff">
						<logic:notEmpty name="item" property="roleName">
							<bean:write name="item" property="roleName" />
						</logic:notEmpty>
						<logic:empty name="item" property="roleName">
				                	未知
				                </logic:empty>
					</td>
					<td bgcolor="#ffffff">
						<a
							href="../role_mgr/role_update.jsp?roleName=<bean:write name="item" property="roleName"/>&roleId=<bean:write name="item" property="roleId"/>">修改</a>
					</td>
					<td bgcolor="#ffffff">
						<a
							href="javascript:deleteRole(<bean:write name="item" property="roleId"/>)">删除</a>
					</td>
					<td bgcolor="#ffffff">
						<a
							href="../viewRoleTool.do?roleId=<bean:write name="item" property="roleId"/>&roleName=<bean:write name="item" property="roleName"/>">菜单功能分配</a>
					</td>
					<!--<td bgcolor="#ffffff">
			                	<a href="../../orgmanage/org_tool_add.do?roleId=<bean:write name="item" property="roleId"/>&roleName=<bean:write name="item" property="roleName"/>">系统功能分配</a>
			                </td>
			         -->
				</tr>
			</logic:iterate>
		</logic:present>
		<logic:notPresent name="Records" scope="request">
			<tr align="left">
				<td bgcolor="#ffffff" colspan="5">
					暂无角色信息
				</td>
			</tr>
		</logic:notPresent>
	</table>
	<table width="90%" border="0">
		<TR>
			<TD width="80%">
				<jsp:include page="../../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../viewRole.do" />
				</jsp:include>
			</TD>
		</tr>
	</table>
</body>
</html:html>
