<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<% 
String appPath = request.getContextPath();
String reportName = "";
	if (request.getAttribute("ReportName") != null) {
		reportName = (String) request.getAttribute("ReportName");
	} else {
		if (request.getParameter("reportName") != null){
			reportName = request.getParameter("reportName");
		}
		System.out.println("jsp edit++++++++++++++++++++++++++++"+reportName);	}
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
		 * 查看汇总范围
		 * @param {Object} orgId
		 * @param {Object} childRepId
		 * @param {Object} versionId
		 * @param {Object} reportName
		 */
				
		function viewHZSD(orgId,childRepId,versionId,reportName){
			window.location="<%=request.getContextPath()%>/gznx/preEditHZSD.do" + 
			  		"?childRepId=" + childRepId + 
			 		"&versionId=" + versionId + 
			 		"&reportName="+reportName+
			 		"&orgId="+orgId+
			 		"&opration=view";
		}
		
		function viewHZGS(orgId,childRepId,versionId,reportName){
			window.location="<%=request.getContextPath()%>/gznx/EditHZGS.do" + 
			  		"?templateId=" + childRepId + 
			 		"&versionId=" + versionId + 
			 		"&reportName="+reportName+
			 		"&orgId="+orgId+
			 		"&opration=view";
		}
		
		function viewBSSD(orgId,childRepId,versionId,reportName){
			window.location="<%=request.getContextPath()%>/gznx/preEditBSSD.do" + 
			  		"?childRepId=" + childRepId + 
			 		"&versionId=" + versionId + 
			 		"&reportName="+reportName+
			 		"&orgId="+orgId+
			 		"&opration=view";
		}
		
		function _back(){
		window.location="<%=appPath%>/afreportDefine.do";
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
				 	当前位置 >> 报表管理 >> 模板查看 >> 查看详细信息
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
								<th colspan="9" align="center" id="list">
									<strong>										
										<logic:present name="ReportName" scope="request"><%=reportName%></logic:present>
									</strong>
								</th>
							</tr>
				<TR class="middle" align="center">
					<TD width="20%" align="center">
						<b>模板名称</b>
					</TD>
					<td width="8%" align="center">
						<b>版本号</b>
					</td>
					<TD width="8%" align="center">
						<b>有效期</b>
					</TD>
					<td width="10%" align="center"> 
						<b>表内表间关系</b>		
					</td>
					<td width="8%" align="center"> 
						<b>版式校验</b>		
					</td>
					<TD width="10%" align="center">
						<b>汇总关系</b>
					</TD>
					<TD width="10%" align="center">
						<b>汇总公式</b>
					</TD>
					<TD width="10%" align="center">
						<b>上报机构</b>
					</TD>
					<td width="10%" align="center"> 
						<b>报送频度</b>		
					</td>

				</TR>
					<logic:present name="Records" scope="request">
						<logic:iterate id="item" name="Records">
							<TR bgcolor="#FFFFFF">
								<TD align="center" width="25%">
									<bean:write name="item" property="templateName"/>
								</TD>
								<TD align="center" width="8%">
									<bean:write name="item" property="versionId"/>
								</TD>
								<TD  align="center" width="8%">
									<bean:write name="item" property="endDate"/>
								</TD>
								<TD  align="center" width="8%">
									<a href="../../gznx/viewAFCellToFormu.do?childRepId=<bean:write name="item" property="templateId"/>&versionId=<bean:write name="item" property="versionId"/>&reportName=<bean:write name="item" property="templateName"/>">查看</a>
								</TD>
								<TD  align="center" width="8%">
									<a href="../../template/jiaoYanAction.do?childRepId=<bean:write name="item" property="templateId"/>&versionId=<bean:write name="item" property="versionId"/>&type=view">查看</a>
								</TD>
								<TD  align="center" width="8%">
									
									<a href="javascript:viewHZSD('<%=orgId%>','<bean:write name="item" property="templateId"/>','<bean:write name="item" property="versionId"/>','<bean:write name="item" property="templateName"/>')">查看</a>
								</TD>
								<TD  align="center" width="10%">
									
									<a href="javascript:viewHZGS('<%=orgId%>','<bean:write name="item" property="templateId"/>','<bean:write name="item" property="versionId"/>','<bean:write name="item" property="templateName"/>')">查看</a>
								</TD>
								
								<TD  align="center" width="10%">
									
									<a href="javascript:viewBSSD('<%=orgId%>','<bean:write name="item" property="templateId"/>','<bean:write name="item" property="versionId"/>','<bean:write name="item" property="templateName"/>')">查看</a>
								</TD>
								
								<TD  align="center" width="10%">
									<a href="../../gznx/viewAFActuRept.do?childRepId=<bean:write name="item" property="templateId"/>&versionId=<bean:write name="item" property="versionId"/>&reportName=<bean:write name="item" property="templateName"/>">查看</a>
								</TD>

							</TR>
						</logic:iterate>
					</logic:present>
					<logic:notPresent name="Records" scope="request">
							<tr bgcolor="#FFFFFF">
								<td colspan="8">
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
