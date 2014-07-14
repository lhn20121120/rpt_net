<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String reportName = "";
	if (request.getAttribute("ReportName") != null) {
		reportName = (String) request.getAttribute("ReportName");
	} else {
		if (request.getParameter("reportName") != null)
			//reportName=FitechUtil.getParameter(request,"reportName");
			reportName = request.getParameter("reportName");

	}
%>
<html:html locale="true">
	<head>
		<html:base/>
		<title>报表并表范围及报送频度设定</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" type="text/css" rel="stylesheet">
	</head>
	
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	
	<body background="../image/total.gif">
		<table border="0" cellspacing="0" cellpadding="4" width="90%" align="center">
			<tr><td height="8"></td></tr>
			<tr>
				<td>
					当前位置 >> 报表管理 >> 模板查看 >> 查看详细信息 >> 查看报送频度
				</td>
			</tr>
			<tr><td height="10"></td></tr>		
		</table>
		<TABLE cellSpacing="1" cellPadding="4" width="90%" border="0" class="tbcolor">
			<TR class="tbcolor1">
				<th colspan="4" align="center" id="list" height="30">
					<span style="FONT-SIZE: 11pt">《
						<logic:present name="ReportName" scope="request">									
							<%=reportName%>
						</logic:present>》报表并表范围及报送频度时间
					</span>
				</th>
			</TR>
			<TR bgcolor="#FFFFFF">
	
				<TD align="center" width="25%" class="tableHeader">
					<b>频度</b>
				</TD>
				<TD align="center" width="25%" class="tableHeader">
					<b>时间(天)</b>
				</TD>
				<TD align="center" width="25%" class="tableHeader">
					<b>补报时间(天)</b>
				</TD>
			</tr>
				<logic:present name="Records" scope="request">
					<logic:iterate id="item" name="Records">
						<tr bgcolor="#FFFFFF">
							
							<td align="center">
								<logic:present name="item" property="repFreqName">
									<bean:write name="item" property="repFreqName"/>
								</logic:present>
								<logic:notPresent name="item" property="repFreqName">
									无
								</logic:notPresent>
							</td>
							<TD align="center">
								<logic:present name="item" property="normalTime">
									<bean:write name="item" property="normalTime"/>
								</logic:present>
								<logic:notPresent name="item" property="normalTime">
									0
								</logic:notPresent>
							</TD>
							<TD align="center">
								<logic:present name="item" property="delayTime">
									<bean:write name="item" property="delayTime"/>
								</logic:present>
								<logic:notPresent name="item" property="delayTime">
									0
								</logic:notPresent>
							</TD>
						</tr>
					</logic:iterate>
				</logic:present>
				
				<logic:notPresent name="Records" scope="request">
					<tr bgcolor="#FFFFFF">
						<td colspan="4">
							无匹配记录
						</td>
					</tr>
				</logic:notPresent>
		</TABLE>
		
		<table border="0" cellspacing="0" cellpadding="4" width="90%" align="center">
			<TR>
				<TD align="center">
					<input class="input-button" type="button" value=" 返 回 " onclick="history.back()">
				</TD>
			</TR>
		</table>
	</body>
</html:html>
