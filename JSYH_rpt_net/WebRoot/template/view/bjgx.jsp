<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.form.MCellFormuForm,com.cbrc.smis.util.FitechUtil"%>

<html:html locale="true">
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
<head>
	<html:base /> 
	<title>表内、表间关系设定</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript">
		function goBack()
		{
			window.location = "./viewTemplateDetail.do?childRepId=<bean:write name="ChildRepId"/>&reportName=<bean:write name="ReportName"/>";
		}
	</script>
</head>
<body background="../../image/total.gif">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
					alert("<bean:write name='Message'/>");
				</script>
		</logic:greaterThan>
	</logic:present>
	<table border="0" cellspacing="0" cellpadding="4" width="95%" align="center">
		<tr><td height="8"></td></tr>
			<tr>
				<td>
					当前位置 >> 报表管理 >> 报表定义管理>> 模板查看 >> 查看表内表间关系
				</td>
			</tr>
			<tr><td height="10"></td></tr>		
		<tr>
			<td>
				<table id="tbl" border="0" cellpadding="4" cellspacing="1" width="95%" class="bgcolor" align="center">
					<TR class="tbcolor1">
						<th colspan="4" align="center" id="list" height="30">
							<span style="FONT-SIZE: 11pt"> 《
								<logic:present name="ReportName" scope="request">
									<%=reportName%>									
								</logic:present>》表内表间关系
							</span>
						</th>
					</TR>
					<tr>
						<td class="tableHeader" width="8%">
							序号
						</td>
						<td class="tableHeader" width="80%">
							表达式
						</td>
						<td class="tableHeader" width="12%">
							类型
						</td>
					</tr>
					<logic:present name="Records" scope="request">
						<logic:iterate id="item" name="Records" indexId="index">
							<tr bgcolor="#FFFFFF">
								<td align="center">
									<%=((Integer) index).intValue() + 1%>
								</td>
								<td align="center">
									<bean:write name="item" property="cellFormu" />
								</td>
								<td align="center">
									<logic:equal name="item" property="formuType" value="1">
										<font color="#008066">表内校验</font>
									</logic:equal>
									<logic:equal name="item" property="formuType" value="2">
										<font color="#CC0000">表间校验</font>
									</logic:equal>
								</td>
							</tr>
						</logic:iterate>
					</logic:present>
					<logic:notPresent name="Records" scope="request">
						<tr>
							<td bgcolor="#FFFFFF" colspan="3">
								无表内表间校验
							</td>
						</tr>
					</logic:notPresent>
				</table>
				<table cellSpacing="1" cellPadding="4" width="90%" border="0" align="center">
					<tr>
						<TD colspan="7" bgcolor="#FFFFFF">
							<jsp:include page="../../apartpage.jsp" flush="true">
								<jsp:param name="url" value="../viewMCellToFormu.do" />
							</jsp:include>
						</TD>
					</tr>
				</table>
				<table border="0" cellpadding="0" cellspacing="4" width="90%" align="center">
					<tr>
						<td align="center">
							<input type="button" class="input-button" onclick="history.back()" value=" 返 回 ">
						</td>
					</tr>
				</table>
			</td>
		</tr>

	</table>
</body>
</html:html>
