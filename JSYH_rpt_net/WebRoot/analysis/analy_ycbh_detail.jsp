<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" type="text/css" rel="stylesheet">
</head>
<body style="TEXT-ALIGN: center" background="../image/total.gif">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table border="0" width="90%" align="center">
		<tr><td height="3"></td></tr>
			<tr>
				<td>
					当前位置 >> 统计分析 >> 异常变化查看
				</td>
			</tr>
		</table>
	<TABLE cellSpacing="0" width="100%" border="0" align="center" cellpadding="6">
		 <TR>
			 <TD>
				<TABLE cellSpacing="0" cellPadding="6" width="100%" border="0" class="tbcolor">
					<tr class="titletab">
						<th colspan="6" align="center" id="list">
							<strong>
								异常变化信息
							</strong>
						</th>
					</tr>
					<TR class="middle">
						<TD class="tableHeader" width="30%">
							<b>
								报表名称
							</b>
						</TD>
						<TD class="tableHeader" width="10%">
							<b>
								年份-期数
							</b>
						</TD>
						<TD class="tableHeader" width="10%">
							<b>
								单元格
							</b>
						</TD>
						<TD class="tableHeader" width="10%">
							<b>
								数据
							</b>
						</TD>
						<TD class="tableHeader" width="10%">
							<b>
								比上期上升/下降
							</b>
						</TD>
						
						<TD class="tableHeader" width="10%">
							<b>
								比上年同期上升/下降
							</b>
						</TD>
					</TR>
					<logic:present name="Records" scope="request">
						<logic:iterate id="item" name="Records">
						<TR bgcolor="#FFFFFF">
							<td><logic:present name="ReportName" scope="request">
								<bean:write name="ReportName" scope="request"/>
							</logic:present>
							</td>
							<td>
							<logic:present name="ReportDate" scope="request">
								<bean:write name="ReportDate" scope="request"/>
							</logic:present>
							</td>
							<td align="center"><bean:write name="item" property="cellName"/></td>
							<td align="center"><bean:write name="item" property="reportValue"/></td>
							<td align="center">+<bean:write name="item" property="thanPrevRise"/>/-<bean:write name="item" property="thanPrevFall"/></td>
							<td align="center">+<bean:write name="item" property="thanSameRise"/>/-<bean:write name="item" property="thanSameFall"/></td>
						</tr>	
						</logic:iterate>
					</logic:present>
					<logic:notPresent name="Records" scope="request">
						<tr bgcolor="#FFFFFF">
							<td colspan="6">
								暂无异常变化信息
							</td>
						</tr>
					</logic:notPresent>
				</TABLE>
			</td>
		 </tr>
 
	 </TABLE>
</body>
</html:html>
