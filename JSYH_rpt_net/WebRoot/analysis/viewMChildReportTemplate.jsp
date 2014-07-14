<%@ page language="java" pageEncoding="GB2312"  %>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<html:html locale="true">
	<head>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../css/common.css" type="text/css" rel="stylesheet">
		<%
			if(session.getAttribute("SelectedOrgIds")!=null)
				session.removeAttribute("SelectedOrgIds");
		%>
		<%
			Operator operator=(Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			String orgId="";
			if(operator!=null){
				orgId=operator.getOrgId();
			}
		%>
		<script language="javascript" src="../js/func.js"></script>
		<script language="javascript">
			<logic:present name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
				var _curPage="<bean:write name='<%=Config.APART_PAGE_OBJECT%>' property='curPage' scope='request'/>";
			</logic:present>
			<logic:notPresent name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
				var _curPage="0";
			</logic:notPresent>
			
			/**取数excel
			 * @param childRepId 子报表ID
			 * @param versionId 版本号
			 * @return void
			 */
			function _obtainExcel(childRepId, versionId){
			 	window.location="<%=request.getContextPath()%>/obtain/templateConfigurePre.do" + 
			 		"?childRepId=" + childRepId + 
			 		"&versionId=" + versionId + 
			 		"&curPage=" + _curPage;
			}
			/**取数txt
			 * @param childRepId 子报表ID
			 * @param versionId 版本号
			 * @return void
			 */
			function _obtainTxt(childRepId, versionId){
			 	window.location="<%=request.getContextPath()%>/obtain/templateConfigurePre.do" + 
			 		"?childRepId=" + childRepId + 
			 		"&versionId=" + versionId + 
			 		"&curPage=" + _curPage;
			}
			
			
			
			 /**
			  * 异常变化标准修改事件
			  *
			  * @param childRepId 子报表ID
			  * @param versionId 版本号
			  * @return void
			  */
			  function _ycbh_mod(childRepId,versionId){
			  	window.location="<%=request.getContextPath()%>//analysis/editReportYCBH.do" +
			  		"?childRepId=" + childRepId + 
			 		"&versionId=" + versionId + 
			 		"&curPage=" + _curPage;
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
	<body style="TEXT-ALIGN: center" >
		
			<table border="0" width="90%" align="center">
			<tr><td height="3"></td></tr>
			<tr>
				<td>
					当前位置 >> 统计分析 >> 异常变化设定
				</td>
			</tr>
		</table>

		
		<table cellSpacing="0" cellPadding="4" width="96%" border="0" align="center">
			<tr>
				<td>
					<fieldset id="fieldset">	
						<html:form action="/analysis/viewMChildReport" method="POST">
							<table cellSpacing="0" cellPadding="4" width="96%" border="0" align="center" height="5">	
								<tr>
									<td height="3"> 
										报表名称：
										<input class="input-text" id="Text1" type="text" size="28" name="reportName">
									 </td>	
								
									 <td>
									 	<INPUT class="input-button" id="Button3" type="submit" value=" 查 询 " name="Button1">	
									 </td>
								 </tr>							
							</table>
						</html:form>
					 </fieldset>		
				</td>
				</tr>				
		</table>
		
		
		
				<TABLE cellSpacing="0" width="96%" border="0" align="center" cellpadding="4">
		
					<TR>
						<TD>
							<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0"  class="tbcolor">
								<tr class="titletab">
									<th colspan="5" align="center" id="list">
										<strong>
											报表模板列表
										</strong>
									</th>
								</tr>
								<TR class="middle">
									<TD class="tableHeader" width="5%">
										<b>编号</b>
									</TD>
									<TD class="tableHeader" width="40%">
										<b>报表模板名称</b>
									</TD>
									<TD class="tableHeader" width="5%">
										<b>版本</b>
									</TD>
									<TD class="tableHeader" width="15%">
										<b>报表类型</b>
									</TD>
									<TD class="tableHeader" width="10%">
										<b>操作</b>
									</TD>									
								</TR>
									<logic:present name="Records" scope="request">
											<logic:iterate id="item" name="Records">
												<TR bgcolor="#FFFFFF">
													<TD  align="center">
														<logic:notEmpty name="item" property="childRepId">
															<bean:write name="item" property="childRepId"/>
														</logic:notEmpty>
														<logic:empty name="item" property="childRepId">
															无
														</logic:empty>
													</TD>
													<TD align="left">
														<logic:notEmpty name="item" property="reportName">
															<logic:equal name="item" property="templateType" value="excel">
																<a href="<%=request.getContextPath()%>/servlets/selfReadExcelServlet?childRepId=<bean:write name='item' property='childRepId'/>&versionId=<bean:write name='item' property='versionId'/>" >
																	<FONT color="#FF00FF"><bean:write name="item" property="reportName"/></FONT>
																</a>
															</logic:equal>
															<logic:equal name="item" property="templateType" value="">
																<a href="<%=request.getContextPath()%>/servlets/selfReadExcelServlet?childRepId=<bean:write name='item' property='childRepId'/>&versionId=<bean:write name='item' property='versionId'/>" >
																	<bean:write name="item" property="reportName"/>
																</a>
															</logic:equal>
														</logic:notEmpty>
														<logic:empty name="item" property="reportName">
															无
														</logic:empty>
													</TD>
													<TD align="center">
														<logic:notEmpty name="item" property="versionId">
															<bean:write name="item" property="versionId"/>
														</logic:notEmpty>
														<logic:empty name="item" property="versionId">
															无
														</logic:empty>
													</TD>
													<TD align="center">
														<logic:notEmpty name="item" property="repTypeName">
															<bean:write name="item" property="repTypeName"/>
														</logic:notEmpty>
														<logic:empty name="item" property="repTypeName">
															无
														</logic:empty>
													</TD>
													
													<TD align="center">&nbsp;
														
														<a href="javascript:_ycbh_mod('<bean:write name="item" property="childRepId"/>','<bean:write name="item" property="versionId"/>')">异常变化设定</a>&nbsp;

													</TD>
												</TR>
											</logic:iterate>
									</logic:present>
									<logic:notPresent name="Records" scope="request">
										<tr bgcolor="#FFFFFF">
											<td colspan="5">
												无匹配记录
											</td>
										</tr>
									</logic:notPresent>
								</TABLE>
									<table cellSpacing="1" cellPadding="4" width="100%" border="0">
										<tr>
											<TD colspan="6" bgcolor="#FFFFFF">
											<jsp:include page="../apartpage.jsp" flush="true">
												<jsp:param name="url" value="viewMChildReport.do" />
											</jsp:include>
											</TD>
										</tr>
									</table>
						</TD>
					</TR>
				</TABLE>			
	</body>
</html:html>
