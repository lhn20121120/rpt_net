<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<html:html locale="true">
	<head>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="../../js/func.js"></script>
		<script language="javascript">
			function addreport(){
				window.location="<%=request.getContextPath()%>/template/add/addFZTemplate.jsp";
			}
		</script>
	</head>
	<body style="TEXT-ALIGN: center">
			
			<html:form action="/template/viewTemplate" method="POST">
			<div align="center">
				<table border="0"width="95%">
					<tr>
						<td height="8">
						</td>
					</tr>
					<tr>
						 <td>
						 	当前位置 >> 报表管理 >> 报表定义管理
						 </td>
					</tr>
					<tr>
						<td height="10"> 
						</td>
					</tr>
					 <br>
					<TR>
						<td>
							<fieldset id="fieldset">
							<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
								<tr>
										<td height="5"></td>
									</tr>
								<tr>
								<tr>
									<TD width="30%" align="right">
										报表名称：<html:text property="reportName" size="15"  styleClass="input-text"/>
									</TD>
									<TD width="20%" align="center">
										<html:submit value="查  询" styleClass="input-button"/>
									</TD>
									<td width="10%" align="left">
										
									</td>
									<td width="30%" align="center">
										<input type="button" name="addTemplateType" value="增加报表信息" onclick="javascript:addreport()" class="input-button">
									</td>
								</tr>
							</table>
							</fieldset>
						</td>
						
					</TR>
				</table>
		</div>
				<TABLE cellSpacing="0" width="95%" border="0" align="center" cellpadding="4">
				<tr align="right">
				   <br>
				  
				  </tr>
					<!-- <TR><TD><FONT color="#FF0000">红色显示为自定义模板</FONT></TD></TR>-->
					<TR>
						<TD>
							<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0"  class="tbcolor">
								<tr class="titletab">
									<th colspan="7" align="center" id="list">
										<strong>
											报表列表
										</strong>
									</th>
								</tr>
								<TR class="middle">
									<TD class="tableHeader" width="8%">
										<b>报表编号</b>
									</td>
									<TD class="tableHeader" width="40%">
										<b>报表名称</b>
									</TD>
									
									<TD class="tableHeader" width="8%">
										<b>版本号</b>
									</td>
									<TD class="tableHeader" width="18%">
										<b>报表类型</b>
									</TD>
									<TD class="tableHeader" width="8%">
										<b>货币单位</b>
									</td>
									<TD class="tableHeader" width="8%">
										<b>是否发布</b>
									</td>
									<TD class="tableHeader" width="10%">
										<b>详细信息</b>
									</TD>									
								</TR>
									<logic:present name="Records" scope="request">
											<logic:iterate id="item" name="Records">
												<TR bgcolor="#FFFFFF">
													<td align="center"><bean:write name="item" property="childRepId"/></td>
													<TD align="center">
														<logic:notEmpty name="item" property="reportName">
														<!-- 
															<a href="<%=request.getContextPath()%>/servlets/selfReadExcelServlet?childRepId=<bean:write name='item' property='childRepId'/>&versionId=<bean:write name='item' property='versionId'/>" >
														-->
															<!-- <a href="<%=request.getContextPath()%>/servlets/ReadReportControlServlet?childRepId=<bean:write name='item' property='childRepId'/>&versionId=<bean:write name='item' property='versionId'/>" >
															<bean:write name="item" property="reportName"/>
															</a>-->
															<logic:equal name="item" property="templateType" value="excel">
																<a href="<%=request.getContextPath()%>/servlets/selfReadExcelServlet?childRepId=<bean:write name='item' property='childRepId'/>&versionId=<bean:write name='item' property='versionId'/>" >
																	<FONT color="#FF00FF"><bean:write name="item" property="reportName"/></FONT>
																</a>
															</logic:equal>
															<logic:equal name="item" property="templateType" value="">
																<a href="<%=request.getContextPath()%>/servlets/selfReadExcelServlet?childRepId=<bean:write name='item' property='childRepId'/>&versionId=<bean:write name='item' property='versionId'/>">
																	<bean:write name="item" property="reportName"/>
																</a>
															</logic:equal>
														</logic:notEmpty>
														<logic:empty name="item" property="reportName">
															无
														</logic:empty>
													</TD>
													
													<td align="center"><bean:write name="item" property="versionId"/></td>
													<TD align="center">
														<logic:notEmpty name="item" property="repTypeName">
															<bean:write name="item" property="repTypeName"/>
														</logic:notEmpty>
														<logic:empty name="item" property="repTypeName">
															无
														</logic:empty>
													</TD>
													<td align="center"><bean:write name="item" property="curUnitName"/></td>
													<td align="center">
														<logic:equal name="item" property="isPublic" value="<%=String.valueOf(Config.IS_PUBLIC)%>">
															<a href="javascript:updateState('<bean:write name='item' property='childRepId'/>','<bean:write name='item' property='versionId'/>')" alt="重新发布"><font color="blue">已发布</font></a>
														</logic:equal>
														<logic:notEqual name="item" property="isPublic" value="<%=String.valueOf(Config.IS_PUBLIC)%>">
															<a href="javascript:yupdateState('<bean:write name='item' property='childRepId'/>','<bean:write name='item' property='versionId'/>')" alt="重新发布"><font color="blue">未发布</font></a>
														</logic:notEqual>
													</td>
													<TD align="center">
														<a href="../viewTemplateDetail.do?childRepId=<bean:write name="item" property="childRepId"/>&reportName=<bean:write name="item" property="reportName"/>&versionId=<bean:write name="item" property="versionId"/>">查看</a>
														<a href="../viewTemplateDetail.do?childRepId=<bean:write name="item" property="childRepId"/>&reportName=<bean:write name="item" property="reportName"/>&versionId=<bean:write name="item" property="versionId"/>&bak2=2">修改</a>
														
													</TD>
												</TR>
											</logic:iterate>
									</logic:present>
									<logic:notPresent name="Records" scope="request">
										<tr bgcolor="#FFFFFF">
											<td colspan="7">
												暂无记录
											</td>
										</tr>
									</logic:notPresent>
								</TABLE>
									<table cellSpacing="1" cellPadding="4" width="100%" border="0">
										<tr>
											<TD colspan="7" bgcolor="#FFFFFF">
											<jsp:include page="../../apartpage.jsp" flush="true">
												<jsp:param name="url" value="../viewTemplate.do" />
											</jsp:include>
											</TD>
										</tr>
									</table>
						</TD>
					</TR>
				</TABLE>	
	   
			</html:form>	
			
	</body>
	<script language="javascript">
		function updateState(child,version){
			if(confirm("是否将模板置为未发布?")){
				window.location="../template/viewTemplate.do?child="+child+"&version="+version+"&usingFlag=0";
			}
		
		}
		function yupdateState(child,version){
			if(confirm("是否将模板置为已发布?")){
				window.location="../template/viewTemplate.do?child="+child+"&version="+version+"&usingFlag=1";
			}
		
		}
	</script>
</html:html>
