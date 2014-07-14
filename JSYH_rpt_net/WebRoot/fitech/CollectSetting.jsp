<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base />
	<title>
		汇总关系设定
	</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript" src="../js/comm.js"></script>
	
</head>
<body background="../../image/total.gif">
	<table border="0" cellspacing="0" cellpadding="4" width="80%" align="center">
		<tr>
			<td height="10"></td>
		</tr>
		<tr>
			<td>
				<fieldset id="fieldset">
					<legend>
						<strong>
							汇总规则设定
						</strong>
					</legend>
					<br>
					<div align="center">
					模板：<select name="">
							<OPTION value="1">G01资产负债表</OPTION>
							<OPTION value="1">G12贷款质量迁徙情况表</OPTION>
							<OPTION value="1">G13最大十家关注类贷款情况表</OPTION>
							<OPTION value="1">G04利润表</OPTION>
							<OPTION value="1">G31有价证券及投资情况表</OPTION>
							</select>
					</div>
					<br>
					<table id="tbl" border="0" cellpadding="4" cellspacing="1" width="90%" class="bgcolor" align="center">
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
					</table>
					<br>
					<form>
						<table border="0" cellpadding="0" cellspacing="4" width="90%" align="center">
							<tr>
								<td align="center">
									<input type="button" value="导入表达式" class="input-button">
									&nbsp;&nbsp;
									<input type="submit" value=" 保 存 " class="input-button">
								</td>
							</tr>
						</table>
					</form>
				</fieldset>
			</td>
		</tr>
		
	</table>
</body>
</html:html>
