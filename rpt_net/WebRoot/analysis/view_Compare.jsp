<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.List,com.cbrc.smis.form.ReportInInfoForm,com.cbrc.smis.other.Expression"%>
<jsp:useBean id="utilFormOrg" scope="page" class="com.fitech.net.common.UtilForm" />
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />

<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../js/func.js"></script>
	<jsp:include page="../calendar.jsp" flush="true">
		<jsp:param name="path" value="../" />
	</jsp:include>
</head>
<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 >> 统计分析 >> 数据比对分析
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
							
	<table border="0" cellpadding="7" cellspacing="0" width="100%" align="center">
		<html:form action="/analysis/viewCompare" method="post">
			<tr>
				<td>
				<TABLE cellSpacing="1" cellPadding="7" width="100%" border="0" class="tbcolor">
					<tr class="titletab">
						<th colspan="7" align="center" id="list">
							<strong>
								数据比对分析
							</strong>
						</th>
					</tr>
					<TR class="middle">
						<TD class="tableHeader" width="30%">
							<b>
								报表名称
							</b>
						</TD>
						<TD class="tableHeader" width="9%">
							<b>
								年份-期数
							</b>
						</TD>
						<TD class="tableHeader" width="7%">
							<b>
								单元格
							</b>
						</TD>
						<TD class="tableHeader" width="10%">
							<b>
								数据
							</b>
						</TD>
						<TD class="tableHeader" width="18%">
							<b>
								比上期上升/下降
							</b>
						</TD>
						<TD class="tableHeader" width="16%">
							<b>
								比上二期上升/下降
							</b>
						</TD>
						<TD class="tableHeader" width="16%">
							<b>
								比上年同期上升/下降
							</b>
						</TD>
					</TR>
					<logic:present name="Records" scope="request">
						<logic:iterate id="item" name="Records">
						<TR bgcolor="#FFFFFF">						
							<TD align="center">							
									<bean:write name="item" property="reportStr"/>
							</TD>
							
							<td  align="center">
								<bean:write name="item" property="year"/>-<bean:write name="item" property="term"/>
							</td>
							<td align="center"><bean:write name="item" property="cellName"/></td>
							<td align="center"><bean:write name="item" property="reportValue"/></td>
							<td align="center">
							<logic:notEmpty name="item" property="preValue">
								<font color="red"><bean:write name="item" property="preValue"/>%</font>
							</logic:notEmpty>
							</td>
							
							<td align="center">
							<logic:notEmpty name="item" property="preTowValue">
								<font color="red"><bean:write name="item" property="preTowValue"/>%</font>
							</logic:notEmpty>

							</td>
							
							<td align="center">
							<logic:notEmpty name="item" property="preYearValue">
								<font color="red"><bean:write name="item" property="preYearValue"/>%</font>
							</logic:notEmpty>
							</td>
	
							
						</tr>	
						</logic:iterate>
					</logic:present>
					<logic:notPresent name="Records" scope="request">
						<tr bgcolor="#FFFFFF">
							<td colspan="7">
								暂无异常变化信息
							</td>
						</tr>
					</logic:notPresent>
				</TABLE>
				</td>
			</tr>
		</html:form>
	</table>
</body>
</html:html>
