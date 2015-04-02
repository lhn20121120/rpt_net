<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

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
			<logic:present name="ChildRepId" scope="request">
				var childRepId = "<bean:write name="ChildRepId"/>";
			</logic:present>
			<logic:present name="VersionId" scope="request">
				var versionId = "<bean:write name="VersionId"/>";
			</logic:present>
			<logic:present name="ReportName" scope="request">
				var reportName = "<bean:write name="ReportName"/>";
			</logic:present>
			<logic:present name="ReportStyle" scope="request">
				var reportStyle = "<bean:write name="ReportStyle"/>";
			</logic:present>
					
			<logic:notPresent name="ChildRepId" scope="request">
				var childRepId = "";
			</logic:notPresent>
			<logic:notPresent name="VersionId" scope="request">
				var versionId ="";
			</logic:notPresent>
			<logic:notPresent name="ReportName" scope="request">
				var reportName ="";
			</logic:notPresent>
			<logic:notPresent name="ReportStyle" scope="request">
				var reportStyle ="";
			</logic:notPresent>
			
			/**
			 * 删除事件
			 */
			function _delRow(orgId)
			{
				if(confirm("你确定要删除该子行吗？")==true){
					window.location="<%=request.getContextPath()%>/template/deleteOrgFromSession.do?deleteOrgId=" + orgId + 
					  "&orgClsId=<bean:write name='orgClsId'/>" + 
					  "&orgClsName=<bean:write name='orgClsName'/>" + 
					  "&childRepId=" + childRepId + 
					  "&versionId=" + versionId + 
					  "&reportStyle=" + reportStyle+
					   "&reportName=" + reportName+"&flag=1";
				}
			}
			
			/**
			 * 返回异常变化设定
			 */
			function _back(){
				window.location="<%=request.getContextPath()%>/template/returnTBFW.do?" + 
					"childRepId=" + childRepId + 
					"&versionId=" + versionId + 
					"&reportStyle=" + reportStyle+
					 "&reportName=" + reportName;
			}
	  	</script>
	
	</head>
	<body>
		<table width="90%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
					<tr class="titletab">
						<th colspan="7" align="center" id="list">
							<strong>
								已选择子行列表
							</strong>
						</th>
					</tr>
					<tr class="middle">
							<td width="55%" class="headTitle">子行名称</td>
							<td width="35%" class="headTitle">子行类型</td>
							<td width="10%" class="headTitle">删除</td>
					</tr>
					<logic:present name="Records" scope="request">
						<logic:iterate id="item" name="Records">
									<tr bgcolor="#FFFFFF">
										<td>
											<bean:write name="item" property="orgName"/>
										</td>
										<td align="center">
											<bean:write name="orgClsName"/>
										</td>
										<td align="center">
											<img src="../../image/del.gif" border="0" onclick="_delRow('<bean:write name="item" property="orgId"/>')" style="cursor:hand" title="删除"/>
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
				<table width="90%" border="0" align="center" cellpadding="4" cellspacing="0">
					<tr><td height="10"></td></tr>
					<tr>
						<td align="right">
							<html:button property="ok" styleClass="input-button" value="确定" onclick="return _back()"/>
						</td>
					</tr>
			</table>
		</body>
</html:html>