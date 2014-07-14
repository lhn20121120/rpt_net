<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.fitech.net.hibernate.OrgNet"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ page import="com.fitech.net.adapter.StrutsOrgNetDelegate"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html lang="true">
<head>
	<html:base />

	<title>modBSFW.jsp</title>

	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">

	<script language="javascript">
			function validate()
			{
				
			}
			
			function _back(childRepId,versionId,upOrgId)
			{
			
				var formObj=document.getElementById("frmSel");
				var box=formObj.elements['orgIds'];
				
					
					
				
				
				window.location="<%=request.getContextPath()%>/template/viewMRepRange.do?childRepId="+formObj.childRepId.value+"&versionId="+formObj.versionId.value+"&orgId="+formObj.upOrgId.value
				           +"&curOrgId="+formObj.curOrgId.value  ;
			}
			
			function _allSelect()
			{
				var formObj=document.getElementById("frmSel");
				var box=formObj.elements['orgIds'];
				
					for(var i=0;i<document.frmSel.elements.length;i++)
					{
					var v=document.frmSel.elements[i];
					if(v.name=="orgIds")
					   v.checked=true;
					}
					
				
				
			}
			
			function _allCancel()
			{
				var formObj=document.getElementById("frmSel");
				var box=formObj.elements['orgIds'];
				
					for(var i=0;i<document.frmSel.elements.length;i++)
					{
					var v=document.frmSel.elements[i];
					if(v.name=="orgIds")
					v.checked=false;
					}
					
				
			}
			function _inOrg(orgId)
			{
			
			var formObj=document.getElementById("frmSel");
				var box=formObj.elements['orgIds'];
				var select=false;
				
										
							
						
						
						window.location="<%=request.getContextPath()%>/template/viewMRepRange.do?childRepId="+formObj.childRepId.value+"&versionId="+formObj.versionId.value+"&orgId="+orgId
						+"&curOrgId="+formObj.curOrgId.value;		
				
			}
	</script>
</head>
<%
	String childRepId = (String) request.getAttribute("childRepId");
	String versionId = (String) request.getAttribute("versionId");
	String orgId = (String) request.getAttribute("orgId");
	String curOrgId = (String) request.getAttribute("curOrgId");
	String upOrgId = "";
	upOrgId = StrutsOrgNetDelegate.getUpOrgId(orgId);
	request.setAttribute("childRepId", childRepId);
	request.setAttribute("versionId", versionId);
	int i = 1;
%>
<body>

	<html:form action="/template/mod/updateBSFW" method="post" styleId="frmSel">

		<TABLE cellSpacing="1" cellPadding="4" width="96%" border="0" align="center" class="tbcolor">
			<TR class="tbcolor1">
				<th align="center" id="list" height="30" colspan="2">
					<span style="FONT-SIZE: 11pt"> <logic:present name="ReportName" scope="request">
							《<bean:write name="ReportName" />》
						</logic:present>填报范围查看 </span>
				</th>
			</TR>

			<tr>
				<td align="left" bgcolor="#EEEEEE">
					填报范围所适用的金融机构：
				</td>

			</tr>
			<tr>

			</tr>
			<tr>
				<td bgcolor="#FFFFFF">

					<table width="90%" border="0" cellpadding="4" cellspacing="1" class="tbcolor2">

						<input type="hidden" name="childRepId" value="<%=childRepId%>">
						<input type="hidden" name="versionId" value="<%=versionId%>">
						<input type="hidden" name="curOrgId" value="<%=curOrgId%>">
						<input type="hidden" name="orgId" value="<%=orgId%>">

						<input type="hidden" name="upOrgId" value="<%=upOrgId%>">
						<logic:present name="lowerOrgList" scope="request">

							<logic:iterate id="item" name="lowerOrgList">
								<logic:notEmpty name="item" property="orgName">
									<%
									String url = "../../template/mod/preEditBSFW.do?childRepId=" + childRepId + "&versionId=" + versionId + "&orgId=";
									%>
									<%
									if ((i % 3) == 1) {
									%>
									<tr>
										<td align="left">

											<a href="javascript:_inOrg('<bean:write name="item" property="orgId"/>')"><bean:write name="item" property="orgName" />
											</a>
										</td>

										<%
										} else {
										%>
										<td align="left">


											<a href="javascript:_inOrg('<bean:write name="item" property="orgId"/>')"><bean:write name="item" property="orgName" />
											</a>
										</td>
										<%
										}
										%>
									
								</logic:notEmpty>
								<%
								if ((i % 3) == 0) {
								%>
								<tr></tr> 
								<%
								}
								%>
								<logic:empty name="item" property="orgName">
									<%
									for (int j = 1; j <= (3 - ((i - 1) % 3)); j++) {
									%>
									<td></td>
									<%
									}
									%>

								</logic:empty>
								<%
								i = i + 1;
								%>
							</logic:iterate>
							<tr></tr>
							<tr class="titletab" id="tbcolor" colspan="6">



								<td align="center" colspan="6">

									<%
										// System.out.println(orgId + curOrgId);
										if (orgId.trim().equals(curOrgId)) {
									%>
									<input type="button" property="back" value="返 回" class="input-button"
										onclick="window.location.assign('<%=request.getContextPath()%>/template/viewTemplateDetail.do?childRepId=<%=childRepId%>')" />
									<%
									} else {
									%>
									<input type="button" property="back" class="input-button" onclick="_back('<%=childRepId%>','<%=versionId%>','<%=upOrgId%>')" value="返 回" />
									<%
									}
									%>
								</td>


							</tr>
						</logic:present>
						<logic:notPresent name="lowerOrgList" scope="request">
							<tr colspan="8">
								<td colspan="8" align="center">
									暂无机构信息
								</td>
							</tr>
							<tr colspan="8">
								<td colspan="8" align="center">
									<%
									if (orgId.trim().equals(curOrgId)) {
									%>
									<input type="button" property="back" value="返回" class="input-button"
										onclick="window.location.assign('<%=request.getContextPath()%>/template/viewTemplateDetail.do?childRepId=<%=childRepId%>')" />
									<%
									} else {
									%>
									<input type="button" property="back" class="input-button" onclick="_back('<%=childRepId%>','<%=versionId%>','<%=upOrgId%>')" value="返 回" />
									<%
									}
									%>
								</td>
							</tr>
						</logic:notPresent>


					</table>

				</td>
			</tr>

		</table>
	</html:form>
</body>
</html:html>
