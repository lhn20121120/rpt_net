<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.util.FitechUtil"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<jsp:useBean id="utilForm" scope="page"
	class="com.cbrc.smis.form.UtilForm" />
<%
	String childRepId = null, versionId = null;
	Integer OATId = new Integer(0);

	if (request.getParameter("childRepId") != null)
		childRepId = (String) request.getParameter("childRepId");
	if (request.getParameter("versionId") != null)
		versionId = (String) request.getParameter("versionId");
	if (request.getAttribute("OATId") != null)
		OATId = (Integer) request.getAttribute("OATId");

	if (childRepId != null)
		request.setAttribute("ChildRepID", childRepId);
	if (versionId != null)
		request.setAttribute("VersionID", versionId);
	boolean system_schema_id = false;
	if (Config.SYSTEM_SCHEMA_FLAG == 1) {
		system_schema_id = true;
	}
%>

<html:html locale="true">
<head>
<html:base />
<title>人行模版列设定</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="0">
<link href="<%=request.getContextPath()%>/css/common.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/comm.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/func.js"></script>
<script type="text/javascript">
function back(templateId,versionId,reportName){
	reportName = encodeURI(encodeURI(reportName));
	 window.location="<%=request.getContextPath()%>/viewAFTemplateDetail.do?templateId="+templateId+
	 	"&versionId=" + versionId + 
		"&reportName="+ reportName+
		"&bak2=2";
}


</script>
</head>

<logic:present name="Message" scope="request">
	<logic:greaterThan name="Message" property="size" value="0">
		<script language="javascript">
			alert("<bean:write name='Message' property='alertMsg'/>");
		</script>
	</logic:greaterThan>
</logic:present>
<body background="<%=request.getContextPath()%>/image/total.gif">
	<table border="0" cellspacing="0" cellpadding="4" width="80%"
		align="center">
		<tr>
			<td height="8"></td>
		</tr>
		<tr>
			<td>当前位置 >> 报表管理 >> 报表定义管理 >> 模板修改>> 人行模版列设定</td>
		</tr>
		<tr>
			<td height="10"></td>
		</tr>
	</table>
	<br>
	<html:form styleId="form1" action="/institution/afTemplateColDefine.do"
		method="Post" >
		<TABLE cellSpacing="1" cellPadding="4" width="80%" border="0"
			class="tbcolor" id="tbl">
			<TR class="tbcolor1">
				<th colspan="4" align="center" id="list" height="30"><span
					style="FONT-SIZE: 11pt"> <logic:present name="reportName"
							scope="request">
						《<bean:write name="reportName" scope="request" />》
					</logic:present>
				</span></th>
			</TR>

			<TR bgcolor="#FFFFFF">
				<TD align="center" width="50%" class="tableHeader"><b>列号</b></TD>
				<TD align="center" width="50%" class="tableHeader"><b>对应列名称</b>
				</TD>

			</tr>
			<logic:present name="templateId" scope="request">
				<input type="hidden" name="templateId"
					value="<bean:write name="templateId"/>">
			</logic:present>
			
			<logic:present name="versionId" scope="request">
				<input type="hidden" name="versionId"
					value="<bean:write name="versionId"/>">
			</logic:present>
			<logic:present name="reportName" scope="request">
				<input type="hidden" name="reportName"
					value="<bean:write name="reportName"/>">
			</logic:present>
				<logic:present name="colDefines" scope="request">
											<logic:iterate id="item" name="colDefines">
												<tr bgcolor="#FFFFFF">
													<td align="center"><bean:write name="item" property="id.col"/></td>
													<td align="center"><bean:write name="item" property="colName"/></td>
												</TR>
											</logic:iterate>
									</logic:present>
									<logic:notPresent name="colDefines" scope="request">
										<tr bgcolor="#FFFFFF">
											<td colspan="2">
												暂无记录
											</td>
										</tr>
									</logic:notPresent>

			<tr bgcolor="#FFFFFF">
			<td align="center">
			<select name="col">
			<option value="A">A</option>
			<option value="B">B</option>
			<option value="C">C</option>
			</select>
			</td>
				<td align="center">
				<html:select property="colName"   >
						<html:optionsCollection name="parameters" value="parName"
							label="parName" />
				</html:select>
				</td>
					
			</tr>

		</TABLE>

		<table border="0" cellspacing="0" cellpadding="4" width="80%"
			align="center">
			<TR>
				<TD align="center"><input type="submit" value=" 保 存 "
					class="input-button">&nbsp; <input class="input-button"
					type="button" value=" 返 回 " onclick="javascript:back('${templateId }','${versionId }','${reportName }')"></TD>
			</TR>
		</table>

	</html:form>
	<form>

</body>
</html:html>
