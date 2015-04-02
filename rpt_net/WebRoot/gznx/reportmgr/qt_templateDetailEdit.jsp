<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<% 

String reportName = "";
	if (request.getAttribute("ReportName") != null) {
		reportName = (String) request.getAttribute("ReportName");
	} else {
		if (request.getParameter("reportName") != null)
			//reportName=FitechUtil.getParameter(request,"reportName");
			reportName = request.getParameter("reportName");

	}
  Operator operator=(Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String orgId=operator.getOrgId();
	/** 报表选中标志 **/
		String reportFlg = "0";
		
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}

%>
<html:html locale="true">
	<head>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" type="text/css" rel="stylesheet">		
	</head>
	<script language="javascript">
		<logic:present name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
			var _curPage="<bean:write name='<%=Config.APART_PAGE_OBJECT%>' property='curPage' scope='request'/>";
		</logic:present>
		<logic:notPresent name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
			var _curPage="0";
		</logic:notPresent>
		
		/**
		  *查看报送范围
		  *
		  */			
		function viewAFRepRange(orgId,childRepId,versionId,reportName){
			window.location="<%=request.getContextPath()%>/gznx/preEditBSFW.do" + 
			  		"?childRepId=" + childRepId + 
			 		"&versionId=" + versionId + 
			 		"&reportName="+reportName+
			 		"&orgId="+orgId;
		}
		function _back(){
		window.location="<%=request.getContextPath()%>/afreportDefine.do";
		}

	</script>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<body>
		<TABLE cellSpacing="0" width="95%" border="0" align="center" cellpadding="4">
			<tr>
				<td height="8">
				</td>
			</tr>
			<tr>
				<td>
				 	当前位置 >> 报表管理 >> 模板修改 
				</td>
			</tr>
			<tr>
				<td height="10"> 
				</td>
			</tr>
				<TR>
					<TD>
						<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0"  class="tbcolor">
							<tr class="titletab">
								<th colspan="8" align="center" id="list">
									<strong>										
										<logic:present name="ReportName" scope="request"><%=reportName%></logic:present>
									</strong>
								</th>
							</tr>
				<TR class="middle" align="center">
					<TD width="10%" align="center">
						<b>模板ID</b>
					</TD>
					<td width="10%" align="center">
						<b>版本号</b>
					</td>
					<TD width="15%" align="center">
						<b>有效期</b>
					</TD>
					<td width="15%" align="center"> 
						<b>报表信息</b>		
					</td>
					<td width="15%" align="center"> 
						<b>表内表间关系</b>		
					</td>
					<TD width="10%" align="center">
						<b>报送范围</b>
					</TD>
					<td width="10%" align="center"> 
						<b>报送频度</b>		
					</td>
					<%if(reportFlg.equals("2")){ %>
					<td width="10%" align="center"> 
						<b>报文定义</b>		
					</td>
					<%} %>
				</TR>
					<logic:present name="Records" scope="request">
						<logic:iterate id="item" name="Records">
							<TR bgcolor="#FFFFFF">
								<TD align="center" width="10%">
									<bean:write name="item" property="templateId"/>
								</TD>
								<TD align="center" width="10%">
									<bean:write name="item" property="versionId"/>
								</TD>
								<TD  align="center" width="15%">
									<bean:write name="item" property="endDate"/>
								</TD>
								<TD  align="center" width="15%">
									<a href="../../gznx/afTemplateEdit.do?templateId=<bean:write name="item" property="templateId"/>&versionId=<bean:write name="item" property="versionId"/>">修改</a>
								</TD>
								<TD  align="center" width="15%">
								<logic:equal name="item" property="reportStyle" value="2">
									<a href="../../gznx/viewQDValidateList.do?templateId=<bean:write name="item" property="templateId"/>&versionId=<bean:write name="item" property="versionId"/>">修改</a>
								</logic:equal>
								<logic:notEqual name="item" property="reportStyle" value="2">
									<a href="../../gznx/editBJGXInit.do?childRepId=<bean:write name="item" property="templateId"/>&versionId=<bean:write name="item" property="versionId"/>&reportName=<bean:write name="item" property="templateName"/>">修改</a>
								</logic:notEqual>
								</TD>
								<TD  align="center" width="10%">
									
									<a href="javascript:viewAFRepRange('<%=orgId%>','<bean:write name="item" property="templateId"/>','<bean:write name="item" property="versionId"/>','<bean:write name="item" property="templateName"/>')">修改</a>
								</TD>
								<TD  align="center" width="10%">
									<a href="../../gznx/editBSPL.do?childRepId=<bean:write name="item" property="templateId"/>&versionId=<bean:write name="item" property="versionId"/>&reportName=<bean:write name="item" property="templateName"/>">修改</a>
								</TD>
								<%if(reportFlg.equals("2")){ %>
								<TD  align="center" width="10%">
									<a href="../../gznx/addRHReport.do?templateId=<bean:write name="item" property="templateId"/>&versionId=<bean:write name="item" property="versionId"/>&templateName=<bean:write name="item" property="templateName"/>">修改</a>
								</TD>
								<%} %>
							</TR>
						</logic:iterate>
					</logic:present>
					<logic:notPresent name="Records" scope="request">
							<tr bgcolor="#FFFFFF">
								<td colspan="6">
									无匹配记录
								</td>
							</tr>
					</logic:notPresent>
					</TABLE>
						
					<table border="0" cellpadding="0" cellspacing="4" width="100%" align="center">
						<tr>
							<td align="center">
								<input type="button" class="input-button" onclick="_back()" value=" 返 回 ">
							</td>
						</tr>
					</table>
				</TD>
			</TR>
		</TABLE>		
	</body>
</html:html>
