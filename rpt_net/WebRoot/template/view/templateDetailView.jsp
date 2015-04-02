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
		function viewMRepRange(orgId,childRepId,versionId,reportName){
			window.location="<%=request.getContextPath()%>/template/view/viewPreBSFW.do" + 
			  		"?childRepId=" + childRepId + 
			 		"&versionId=" + versionId + 
			 		"&reportName="+reportName+
			 		"&curPage=" + _curPage+
			 		"&orgId="+orgId;
		}
		
		/**
		  *查看异常变化
		  *
		  */			
		function viewPreYCBH(childRepId,versionId){
			window.location="<%=request.getContextPath()%>/template/view/viewPreYCBH.do" + 
			  		"?childRepId=" + childRepId + 
			 		"&versionId=" + versionId + 
			 		"&curPage=" + _curPage;
		}
	</script>
	<body>
		<TABLE cellSpacing="0" width="95%" border="0" align="center" cellpadding="4">
			<tr>
				<td height="8">
				</td>
			</tr>
			<tr>
				<td>
				 	当前位置 >> 报表管理 >> 报表定义管理>> 模板查看 
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
								<th colspan="6" align="center" id="list">
									查看报表信息
								</th>
							</tr>
				<TR class="middle" align="center">
					<TD width="30%" align="center">
						<b>模板名称</b>
					</TD>
					<td width="10%" align="center">
						<b>版本号</b>
					</td>
					<TD width="15%" align="center">
						<b>有效期</b>
					</TD>
					<td width="15%" align="center"> 
						<b>表内表间关系</b>		
					</td>
					<TD width="10%" align="center">
						<b>报送范围</b>
					</TD>
					<td width="10%" align="center"> 
						<b>报送频度</b>		
					</td>
<%--					<td width="10%" align="center"> --%>
<%--						<b>异常变化</b>		--%>
<%--					</td>				--%>
				</TR>
					<logic:present name="Records" scope="request">
						<logic:iterate id="item" name="Records">
							<TR bgcolor="#FFFFFF">
								<TD align="center" width="30%">
									<bean:write name="item" property="reportName"/>
								</TD>
								<TD align="center" width="10%">
									<bean:write name="item" property="versionId"/>
								</TD>
								<TD  align="center" width="15%">
									<bean:write name="item" property="endDate"/>
								</TD>
								<TD  align="center" width="15%">
									<a href="../viewMCellToFormu.do?childRepId=<bean:write name="item" property="childRepId"/>&versionId=<bean:write name="item" property="versionId"/>&reportName=<bean:write name="item" property="reportName"/>">查看</a>
								</TD>
								<TD  align="center" width="10%">
									<!-- a href="../viewMRepRange.do?curOrgId=<%=orgId%>&orgId=<%=orgId%>&childRepId=<bean:write name="item" property="childRepId"/>&versionId=<bean:write name="item" property="versionId"/>&reportName=<bean:write name="item" property="reportName"/>">查看</a-->									
									<a href="javascript:viewMRepRange('<%=orgId%>','<bean:write name="item" property="childRepId"/>','<bean:write name="item" property="versionId"/>','<bean:write name="item" property="reportName"/>')">查看</a>
								</TD>
								<TD  align="center" width="10%">
									<a href="../viewMActuRept.do?childRepId=<bean:write name="item" property="childRepId"/>&versionId=<bean:write name="item" property="versionId"/>&reportName=<bean:write name="item" property="reportName"/>">查看</a>
								</TD>
<%--								<TD  align="center" width="10%">--%>
<%--									<!-- <a href="../viewAbnormityChange.do?curOrgId=<%=orgId%>&orgId=<%=orgId%>&childRepId=<bean:write name="item" property="childRepId"/>&versionId=<bean:write name="item" property="versionId"/>&reportName=<bean:write name="item" property="reportName"/>">查看</a-->--%>
<%--									<a href="javascript:viewPreYCBH('<bean:write name="item" property="childRepId"/>','<bean:write name="item" property="versionId"/>')">查看</a>--%>
<%--								</TD>											--%>
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
									<table cellSpacing="1" cellPadding="4" width="100%" border="0">
										<tr>
											<TD colspan="7">
											<jsp:include page="../../apartpage.jsp" flush="true">
												<jsp:param name="url" value="../../template/viewTemplate.do" />
											</jsp:include>
											</TD>
										</tr>
								</table>
								<table border="0" cellpadding="0" cellspacing="4" width="100%" align="center">
									<tr>
										<td align="center">
											<input type="button" class="input-button" onclick="history.back()" value=" 返 回 ">
										</td>
									</tr>
								</table>
						</TD>
					</TR>
				</TABLE>		
	</body>
</html:html>
