<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.form.UtilMDataRGTypeForm" %>
<%@ page import="com.cbrc.smis.form.ReportInForm"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ page import="java.util.*" %>

<jsp:useBean id="utilOrgForm" scope="page" class="com.cbrc.smis.form.UtilOrgForm" />

<html:html locale="true">
<head>
	<html:base />
	<title>机构报送情况统计</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../../js/func.js"></script>
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>
</head>
<%
	String orgName = "";
	if(request.getAttribute("orgName")!=null)
		orgName = request.getAttribute("orgName").toString();
	String reportTimeUnit= Config.REPORT_TIME_UNIT;
	if(reportTimeUnit!=null && reportTimeUnit.equals(Config.REPORT_TIME_UNIT_HOUR))
		request.setAttribute("reportTimeUnit", reportTimeUnit);
%>
<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertdMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table border="0" width="96%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 &gt;&gt; 报表查询 &gt;&gt; 报送统计 &gt;&gt; 报表状态信息
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	
	<TABLE cellSpacing="0" width="98%" border="0" align="center" cellpadding="4">
		<TR>
			<TD>
				<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
					<tr class="titletab">
						<th align="center" colspan="12">
							<%=orgName %> - 报表报送情况
						</th>
					</tr>
					<TR class="middle">
						<td align="center" valign="middle"  width="5%">
							编号
						</td>
						<td align="center" valign="middle"  width="20%">
							报表名称
						</td>
						<td align="center" valign="middle" width="5%" NOWRAP>
							版本号
						</td>
						<td align="center" valign="middle" width="5%" NOWRAP>
							频度
						</td>
						<td align="center" valign="middle" width="10%">
							报表期数
						</td>
						<Td align="center" valign="middle" width="15%">
							机构
						</Td>
						<Td align="center" valign="middle" width="12%">
							实际报送时间
						</Td>
						<Td align="center" valign="middle" width="12%">
							应该报送时间
						</Td>
						<Td align="center" valign="middle" width="10%">
							<logic:present name="reportTimeUnit" scope="request">
								迟报小时数
							</logic:present>
							<logic:notPresent name="reportTimeUnit" scope="request">
								迟报天数
							</logic:notPresent>
						</Td>
					</TR>

					<logic:present name="Records" scope="request">
						<logic:iterate id="viewReportIn" name="Records">							
							<TR bgcolor="#FFFFFF">
								<TD align="center">
									<bean:write name="viewReportIn" property="childRepId" />
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="repName" />
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="versionId" />
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="actuFreqName" />
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="year" />-<bean:write name="viewReportIn" property="term" />
								</TD>
								<TD align="center" >
									<bean:write name="viewReportIn" property="orgName" />
								</TD>
								<TD align="center" >
								<logic:present name="reportTimeUnit" scope="request">
									<bean:write name="viewReportIn" property="realRepTime" format="yyyy-MM-dd HH:mm:ss"/>		
								</logic:present>
								<logic:notPresent name="reportTimeUnit" scope="request">
										<bean:write name="viewReportIn" property="realRepTime" format="yyyy-MM-dd"/>
								</logic:notPresent>
								</TD>
								<TD align="center" >
								<logic:present name="reportTimeUnit" scope="request">
									<bean:write name="viewReportIn" property="sureRepTime" format="yyyy-MM-dd HH:mm:ss"/>	
								</logic:present>
								<logic:notPresent name="reportTimeUnit" scope="request">
									<bean:write name="viewReportIn" property="sureRepTime" format="yyyy-MM-dd"/>
								</logic:notPresent>
								</TD>
								<TD align="center">
								<bean:write name="viewReportIn" property="laterDay" />
								</TD>
							</TR>
						</logic:iterate>
					</logic:present>
					<logic:notPresent name="Records" scope="request">
						<tr align="left">
							<td bgcolor="#ffffff" colspan="12">
								暂无符合条件的记录
							</td>
						</tr>
					</logic:notPresent>
				</table>
			</TD>
		</TR>	
		<tr>
			<td align="center">
				<input type="button" onclick="javascript:window.close();" value="关闭窗口">
			</td>
		</tr>
	</TABLE>
</body>
</html:html>
