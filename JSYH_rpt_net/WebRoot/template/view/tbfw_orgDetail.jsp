<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
	<head>
		<html:base/>
		<title>设定金融子行类型的填报范围</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" type="text/css" rel="stylesheet">
		
		<script language="javascript">
			function back()
			{
				window.location = "../viewMRepRange.do?childRepId=<bean:write name="ChildRepId"/>&versionId=<bean:write name="VersionId"/>&reportName=<bean:write name="ReportName"/>";
				
			}
		</script>
	
	</head>
	<body>
	
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	

		<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
			<tr>
				<td align="center" width="100%">
					<table width="100%" border="0" cellpadding="4" cellspacing="0">
						<tr>
							<td align="center">
								<fieldset id="fieldset">
									<legend>
										<strong>&nbsp;填报子行详细信息</strong>
									</legend>
									<br>
									<table width="96%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
											<tr class="titletab">
				
												<th width="60%" align="center">子行名称</th>
												<th width="30%" align="center">子行类型</th>
											</tr>
	
										<logic:present name="Records" scope="request">
											<logic:iterate id="item" name="Records">
												<tr bgcolor="#FFFFFF">
													<td align="center">
														<bean:write name="item" property="orgName"/>
													</td>
													<td align="center">
														<logic:present name="OrgClsName" scope="request">
															<bean:write name="OrgClsName"/>
														</logic:present>
														<logic:notPresent name="OrgClsName" scope="request">
															未知
														</logic:notPresent>
													</td>
												</tr>
											</logic:iterate>
										
										</logic:present>	
										<logic:notPresent name="Records" scope="request">
											<tr align="left">
												<td bgcolor="#ffffff" colspan="3">
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
											<TD width="80%">
												<jsp:include page="../../apartpage.jsp" flush="true">
													<jsp:param name="url" value="../view/viewTBFWOrgDetail.do" />
												</jsp:include>
											</TD>
										</tr>
											<tr>
												<td align="right">
													<html:button property="back" styleClass="input-button" onclick="back()" value="返回"/>
												</td>
											</tr>
									</table>
								</fieldset>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	
	</body>
</html:html>