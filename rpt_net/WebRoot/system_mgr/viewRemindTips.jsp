<%@ page language="java" pageEncoding="GB2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
	<html:base />
	<title>��ܰС��ʿ</title>
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
			if(confirm("��ȷ��Ҫɾ������ʿ��\n")==true){
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
				��ǰλ�� >> ϵͳ���� >> ��ʿ����
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
					��ܰ��ʿ
				</th>
			</tr>
			<tr>
			<TR class="middle">
				<TD  align="center">
					<strong>��ʿ����</strong>
				</TD>
				<TD  align="center" width="20%">
					<strong>�趨����</strong>
				</TD>
				<TD  align="center" width="20%">
					<strong>��ʿ������</strong>
				</TD>
				<TD  align="center" width="20%">
					<strong>����</strong>
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
						<input type="button" class="input-button" value="�� ��" onclick="_edit()">
						<input type="button" class="input-button" value="ɾ ��" onclick="_del()">
					</td>
				</tr>
				<%
				}else{
				%>
				<tr align="center">
					<td bgcolor="#ffffff" colspan="4" align="left">
						<a href="remindTips.jsp"><font color="red">����С��ʿ</font></a>
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
