<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312" import="java.io.*"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%
	/** 报表选中标志 **/
	String reportFlg = "0";
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null) {
		reportFlg = (String) session
				.getAttribute(Config.REPORT_SESSION_FLG);
	}
	String excelPath = Config.WEBROOTPATH+"templateFiles\\excel\\";
	excelPath = excelPath.replace("\\", "\\\\");
%>
<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" type="text/css" href="../css/pageControl.css" />
	<link href="../../css/calendar-blue.css" type="text/css"
		rel="stylesheet">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link href="../../css/tree.css" rel="stylesheet" type="text/css">
	<script language="javascript" src="../../js/jquery-1.4.2.js"></script>
	<style type="text/css">
		#t1 td{
			height: 25px;
			border-right:1 solid #cccccc;
			border-bottom:1 solid #cccccc;
		}
	</style>
	
</head>
<body>
	<table border="0" cellspacing="0" cellpadding="4" width="100%"
		align="center">
		<tr>
			<td height="8"></td>
		</tr>
		<tr>
			<td>
					当前位置 >> 报表管理 >> 模板查看 >> 查看详细信息 >>查看版式校验信息
			</td>
		</tr>
		<tr>
			<td height="10"></td>
		</tr>
	</table>
	<table cellpadding="4" cellspacing="1" border="0" width="100%"
		align="center" class="tbcolor" id="addTable">
		<tr class="titletab">
			<th align="center" colspan="2">
				校验报表
			</th>
		</tr>
		<tr align="center" bgcolor="#ffffff">
			<td align="center">
				<table id="t1" cellspacing="0" cellpadding="0" border="0"
					width="60%" align="center" class="tbcolor" style="border:1 solid #cccccc;border-right:0px;border-right:0px;border-bottom:0px;">
					<TR class="middle" align="center" height="20">
						<TD width="30%" align="center">
							<b>单元格名称</b>
						</TD>
						<td width="70%" align="center">
							<b>单元格值</b>
						</td>
					</TR>
					<logic:present name="aflist" scope="request">
						<logic:iterate id="item" name="aflist">
							<tr height='20' align='center' bgcolor='#FFFFFF'>
								<td>
									<bean:write name="item" property="id.cellName"/>
								</td>
								<td>
									<bean:write name="item" property="cellContext"/>
								</td>
							</tr>
						</logic:iterate>
					</logic:present>
				</table>
			</td>
		</tr>
	</table>
	<input type="button" class="input-button" onclick="history.back()" value=" 返 回 ">
</body>
</html:html>
