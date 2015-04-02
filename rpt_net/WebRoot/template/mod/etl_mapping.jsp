<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm" />
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
			
		</script>
	</head>
	<body style="TEXT-ALIGN: center">
			
			<html:form action="/template/protect/etlDataBuild" method="POST">
			<div align="center">
				<table border="0"width="95%">
					<tr height="10">
						<td>					
						</td>
					</tr>
					<TR>
						<TD width="35%" align="right">
							模板名称：
						</TD>
						<TD width="25%" align="center">
							<html:text property="reportName" size="28"  styleClass="input-text"/>
						</TD>
						<td width="40%">
							<html:submit value="查  询" styleClass="input-button"/>
						</td>
					</TR>
				</table>
		</div>
				<TABLE cellSpacing="0" width="95%" border="0" align="center" cellpadding="4">
					<!-- <TR><TD><FONT color="#FF0000">红色显示为自定义模板</FONT></TD></TR>-->
					<TR>
						<TD>
							<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0"  class="tbcolor">
								<tr class="titletab">
									<th colspan="7" align="center" id="list">
										<strong>
											模板列表
										</strong>
									</th>
								</tr>
								<TR class="middle">
									<TD width="6%" class="tableHeader">
										<b>序号</b>
									</TD>
									<TD class="tableHeader" width="56%">
										<b>模板名称</b>
									</TD>
									<TD class="tableHeader" width="8%">
										<b>编号</b>
									</td>
									<TD class="tableHeader" width="8%">
										<b>版本号</b>
									</td>
									<TD class="tableHeader" width="20%">
										<b>关联ExcelID</b>
									</TD>
									<TD class="tableHeader" width="8%">
										<b>操作</b>
									</TD>									
								</TR>

									<logic:present name="Records" scope="request">
											<logic:iterate id="item" name="Records"  indexId="index">
												<TR bgcolor="#FFFFFF">
												<TD align="center">
													<%=((Integer) index).intValue() + 1%>
												</TD>
													<TD align="center">
														<logic:notEmpty name="item" property="reportName">
  
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
													<td align="center"><bean:write name="item" property="childRepId"/></td>
													<td align="center"><bean:write name="item" property="versionId"/></td>		
													<TD align="center">
														<html:select property="excelId">
															<html:option value="-1">--请选择关联ExcelID--</html:option>
															<html:optionsCollection name="utilForm" property="excelReportList" />
														</html:select>
													</TD>
													
													 <TD align="center">
														<INPUT class="input-button" id="ButtonSave" type="button" value=" 保  存 " name="ButtonSave"
															onclick="saveETLMapping('<bean:write name="item" property="childRepId"/>','<bean:write name="item" property="versionId"/>','parValues<%=((Integer) index).intValue() + 1%>')">
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
</html:html>
