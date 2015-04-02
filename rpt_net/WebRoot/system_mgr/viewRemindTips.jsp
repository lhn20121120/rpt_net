<%@ page language="java" pageEncoding="GB2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
	<html:base />
	<title>温馨小贴士</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" type="text/css" rel="stylesheet">
	<script type="text/javascript">
		function _edit(){
			window.location="<%=request.getContextPath()%>/system_mgr/remindTips.jsp";
		}
		
		function _del(){
			if(confirm("您确定要删除该贴士吗？\n")==true){
				window.location="<%=request.getContextPath()%>/system_mgr/saveRemindTips.do?type=del";
			}
		}
	</script>
</head>
<body>
	<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 >> 系统管理 >> 贴士提醒
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<form name="form1">
		<table width="98%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
			<tr class="titletab" id="tbcolor">
				<th height="24" align="center" id="list" colspan="10">
					温馨贴士
				</th>
			</tr>
			<tr>
			<TR class="middle">
				<TD  align="center">
					<strong>贴士内容</strong>
				</TD>
				<TD  align="center" width="20%">
					<strong>设定日期</strong>
				</TD>
				<TD  align="center" width="20%">
					<strong>贴士发布人</strong>
				</TD>
				<TD  align="center" width="20%">
					<strong>操作</strong>
				</TD>
			</TR>
			<%
				if(com.cbrc.smis.common.Config.REMINDTIPS != null){				
				%>
				<tr bgcolor="#FFFFFF">
					<td align="center">
						<%=com.cbrc.smis.common.Config.REMINDTIPS.getRtTitle()%>
					</td>
					<td align="center">
					<%=com.cbrc.smis.common.Config.REMINDTIPS.getRtDate()%>
					</td>
					<td align="center">
						<%=com.cbrc.smis.common.Config.REMINDTIPS.getRtUserName()%>
					</td>
					<td align="center">
						<input type="button" class="input-button" value="编 辑" onclick="_edit()">
						<input type="button" class="input-button" value="删 除" onclick="_del()">
					</td>
				</tr>
				<%
				}else{
				%>
				<tr align="center">
					<td bgcolor="#ffffff" colspan="4" align="left">
						<a href="remindTips.jsp"><font color="red">新增小贴士</font></a>
					</td>
				</tr>
				<%
				}
			%>			
		</table>
		<br />
	</form>
</body>
</html:html>
