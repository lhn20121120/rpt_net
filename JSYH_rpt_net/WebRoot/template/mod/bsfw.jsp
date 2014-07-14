<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config"/>
<html:html locale="true">
	<head>
		<html:base/>
		<title>设定金融子行的填报范围</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" type="text/css" rel="stylesheet">
		
		
		<script language="javascript">
		  	
		<logic:present name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="<bean:write name='ApartPage' property='curPage'/>";
	    </logic:present>
	    <logic:notPresent name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="1";
	    </logic:notPresent>
		  	
	  	</script>	
	</head>
	<body>
	<br><br>
			<%=request.getAttribute("VersionId")%>
			<%=request.getAttribute("ChildRepId")%>
				<%=request.getAttribute("orgClsId")%>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
						<fieldset id="fieldset">
									<legend>
										<strong>&nbsp;选择填报的金融子行</strong>
									</legend>
									<br>
								<html:form action="/template/mod/operationOrgType" method="post">
									<table width="96%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
											<tr class="tableHeader">
												<td width="10%" align="center"></td>
												<td width="60%" align="center">子行名称</td>
												<td width="30%" align="center">子行类型</td>
											</tr>
									
										<logic:present name="Records" scope="request">
											<logic:iterate id="item" name="Records">
												<tr bgcolor="#FFFFFF">
													<td  align="center">
														
															<INPUT type="checkbox" name="selectOrgIds" value="<bean:write name="item" property="orgId"/>"/>	
															<input type="hidden" name="childRepId" value="<bean:write name="ChildRepId" scope="request"/>">
															<input type="hidden" name="versionId" value="<bean:write name="VersionId" scope="request"/>">
													</td>
													<td>
														<input type="hidden" name="orgIds" value="<bean:write name="item" property="orgId"/>"/>	
														<bean:write name="item" property="orgName"/>
													</td>
													<td align="center">
														<bean:write name="orgClsName" scope="request"/>
														<input type="hidden" name="orgClsId" value="<bean:write name="orgClsId" scope="request"/>"/>
														
													</td>
												</tr>
											</logic:iterate>
										
										</logic:present>	
										<logic:notPresent name="Records" scope="request">
											<tr align="left">
												<td bgcolor="#ffffff" colspan="5">
													无匹配记录
												</td>
											</tr>
										</logic:notPresent>
									</table>
							
									<table width="96%" border="0" align="center" cellpadding="4" cellspacing="0">	
										<tr>
											<td height="10"></td>
										</tr>
										<tr>
											<td align="right">
												<html:submit styleClass="input-button" value="保存"/>
													
											</td>
										</tr> 
									
									</table>
									<TABLE align="center" border="0" width="80%">
										<TR>
											<TD><jsp:include page="../../apartpage.jsp" flush="true"><jsp:param name="url" value="../../template/mod/viewMOrg.do"/>
												</jsp:include>
											</TD>
										</TR>
									</TABLE>
									<logic:present name="curPage" scope="request">
										<input type="hidden" name="curPage" scope="request">
									</logic:present>
								</html:form>
							</fieldset>
						</body>
</html:html>
															