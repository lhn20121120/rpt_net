<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	String alertMsg = "";
	com.cbrc.smis.util.FitechMessages messages=null;
	if(session.getAttribute(Config.MESSAGES) != null){
		messages=(com.cbrc.smis.util.FitechMessages)session.getAttribute(Config.MESSAGES);
		alertMsg=messages.getMsg();
		session.setAttribute(Config.MESSAGES,null);
	}
%>
<html>
  <head>
    <title>指标生成提示信息</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
	<body>
		<div align="center">
			<table cellspacing="0" cellpadding="4" border="0" width="100%" align="center">			
				<tr height='150'>
					<td align="left" valign="top">
						<font size="2"><%=alertMsg%></font>
					</td>
				</tr>
				<tr>
					<td align="center">
						<a href="javascript:window.close()"><font size="2">关闭窗口</font></a>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>