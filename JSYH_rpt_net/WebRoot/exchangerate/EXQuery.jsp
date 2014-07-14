<%@ page language="java" pageEncoding="GBK"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm" />
<html>
	<head>
		<title>汇率管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<!--
<link href="../../css/common.css" rel="stylesheet" type="text/css">
<link href="css/common.css" rel="stylesheet" type="text/css">-->
		<link href="../css/common.css" rel="stylesheet" type="text/css">
<script language="javascript" src="../js/comm.js"></script>
<script language="javascript" src="../js/functions.js"></script>
<script language="javascript">
function _update(vcid,vdid){

window.location="<%=request.getContextPath()%>/exchangerate/edit.do?vcid="+vcid+"&vdid="+vdid;
}
</script>
<jsp:include page="../calendar.jsp" flush="true">
  <jsp:param name="path" value="../"/>
</jsp:include>
	</head>

	<body scroll=yes>
		<table width="102%" height=100% border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>

				<td height=25 id=location>
					→您的位置：汇率管理&gt;&gt; 汇率查询
				</td>
			</tr>
			<tr>

				<td id=operation height="29">
				</td>
			</tr>

			<tr>
				<td valign="top">
					<table width="90%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
						<tr id="tbcolor">
							<th align="center" id="list">
								汇率管理
							</th>
						</tr>
						<tr>
							<td bgcolor="#FFFFFF">
								<html:form method="post" action="/exchangerate/Find">
									<TABLE width="100%" border="0" align="center" cellpadding="2" cellspacing="2">
										<TBODY>
											<TR>
												<TD width="23%" align="right">
													币种:
												</TD>
												<TD width="23%">
													<html:select property="vcid">
													<option value="">--请选择币种名称--</option>
														<html:optionsCollection name="utilForm" property="vcurrency" />
													</html:select>
												</TD>
												<TD width="14%" align="right">
													日期:
												</TD>
												<TD width="40%">
													<html:text property="vdname" styleClass="input-text" size="10" maxlength="10" readonly="true" />
													<input type="button" class="input-text" value="选择日期" onclick="return showCalendar('vdname', 'y-mm-dd');">
													</TD>
												<TD>
													<html:submit styleClass="input-button" value="查 询"></html:submit>
												</TD>
											</TR>
										</TBODY>
									</TABLE>
								</html:form>
							</td>
						</tr>
						<tr>
							<td bgcolor="#FFFFFF">
								<table width="100%" border="0" cellpadding="0" cellspacing="0" id="list">
									<tr>
										<th width="40" height="28">
											币种号
										</th>
										<th>
											中文名称
										</th>
										<th>
											英文名称
										</th>
										<th>
											汇率
										</th>
										<th>
											日期
										</th>
										<th>
											操作
										</th>
									</tr>
									<logic:present name="Records" scope="request">
										<logic:iterate id="item" name="Records" scope="request">
											<tr>
												<td align="center">
													<bean:write name='item' property='vcid' />
												</td>
												<td>
													<div align="center">
														<bean:write name='item' property='vccnname' />
													</div>
												</td>
												<td>
													<div align="center">
														<bean:write name='item' property='vcenname' />
													</div>
												</td>
												<td>
													<div align="center">
														<bean:write name='item' property='eeramt' format="#0.0000000"/>
													</div>
												</td>
												<td>
													<div align="center">
														<bean:write name='item' property='vdname' />
													</div>
												</td>
												<td>
													<div align="center">
														<a href="EXModify.htm"><input type="button" value="修 改" onclick="_update('<bean:write name='item' property='vcid' />','<bean:write name='item' property='vdid' />')" class="input-button"></a>
													</div>
												</td>
											</tr>
										</logic:iterate>
									</logic:present>
									<logic:notPresent name="Records" scope="request">
									<tr><td>
											暂无记录
										</td></tr>
									</logic:notPresent>

								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height=25>
					<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" id=operation>
						<tr>
							<td height="62">
								&nbsp;
							</td>
						</tr>
					</table>
				</td>
			</tr>

		</table>
	</body>
</html>
