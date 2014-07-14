<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cbrc.org.form.MOrgClForm"%>
<html:html locale="true">
<head>
	<html:base />
	<title>异常数据变化标准</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">

	<%
	String curOrgId = (String) request.getAttribute("curOrgId");
	%>
	<script language="javascript">
	 function _NextOrg(childRepId,versionId,orgId)
			   {
			   
			   
				
				
			   window.location="<%=request.getContextPath()%>/template/viewAbnormityChange.do?"+
			   			"&childRepId="+childRepId+
			   			"&versionId="+versionId+
			   			"&orgId="+orgId+
			   			"&curOrgId="+"<%=curOrgId%>";
			   	
			   }
			   
			   
			   </script>
</head>



<body background="../image/total.gif">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message'/>");
			</script>
		</logic:greaterThan>
	</logic:present>

	<TABLE cellSpacing="0" cellPadding="1" width="96%" border="0" align="center">
		<tr>
			<td height="10"></td>
		</tr>
	</table>
	<TABLE cellSpacing="1" cellPadding="1" width="96%" border="0" class="tbcolor" align="center">
		<tr class="tbcolor1">
			<th colspan="8" align="center" id="list0" height="30">
				<span style="FONT-SIZE: 11pt"> 《<logic:present name="ReportName" scope="request">
						<bean:write name="ReportName" />
					</logic:present>》 </span>
			</th>
		</tr>
		<tr>
			<td colspan="8" height="25" valign="middle">
				<b>数据异常变化所适用的机构类型或金融机构：</b>
			</td>
		</tr>
		<tr>
			<td colspan="8" bgcolor="#FFFFFF">
				<table border="0" cellpadding="4" cellspacing="0" width="100%">
					<%
					int i = 1;
					%>
					<logic:present name="lowerOrgList" scope="request">

						<logic:iterate id="item" name="lowerOrgList">
							<logic:notEmpty name="item" property="orgName">
								<%
								if ((i % 3) == 1) {
								%>
								<tr>
									<td align="left">

										<a
											href="javascript:_NextOrg('<bean:write name="abnormityChangeForm" property="childRepId"/>','<bean:write name="abnormityChangeForm" property="versionId"/>','<bean:write name="item" property="orgId"/>')"><bean:write
												name="item" property="orgName" />
										</a>
									</td>

									<%
									} else {
									%>
									<td align="left">

										<a
											href="javascript:_NextOrg('<bean:write name="abnormityChangeForm" property="childRepId"/>','<bean:write name="abnormityChangeForm" property="versionId"/>','<bean:write name="item" property="orgId"/>')"><bean:write
												name="item" property="orgName" />
										</a>
									</td>
									<%
									}
									%>
								
								<%
							if ((i % 3) == 0) {
							%>
							</tr>
							<% 
							}
							%>	
							</logic:notEmpty>
						
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
					</logic:present>
					<logic:notPresent name="lowerOrgList" scope="request">
						<tr colspan="8">
							<td colspan="8" align="center">
								暂无机构信息
							</td>
						</tr>
					</logic:notPresent>
				</table>
			</td>
		</tr>
	</table>
	<table border="0" cellpadding="0" cellspacing="0" width="96%">
		<tr>
			<td height="10"></td>
		</tr>
	</table>
	<TABLE cellSpacing="1" cellPadding="1" width="96%" border="0" class="tbcolor" align="center">
		<TR>
			<td width="6%" align="center" rowspan="2" class="tableHeader">
				<b> 序号 </b>
			</td>
			<TD width="20%" align="center" rowspan="2" class="tableHeader">
				<b> 异常变化项目 </b>
			</TD>
			<TD width="32%" align="center" colspan="2" class="tableHeader">
				<b> 比上期 </b>
			</TD>
			<TD width="32%" align="center" colspan="2" class="tableHeader">
				<b> 比上年同期 </b>
			</TD>
		</TR>
		<TR>
			<TD width="16%" align="center" class="tableHeader">
				<b> 上升标准 </b>
			</TD>
			<TD width="16%" align="center" class="tableHeader">
				<b> 下降标准 </b>
			</TD>
			<TD width="16%" align="center" class="tableHeader">
				<b> 上升标准 </b>
			</TD>
			<TD width="16%" align="center" class="tableHeader">
				<b> 下降标准 </b>
			</TD>
		</TR>
		<logic:present name="Records" scope="request">
			<logic:iterate id="item" name="Records" indexId="index">
				<TR bgcolor="#FFFFFF" height="25">
					<TD width="2%" align="center">
						<%=((Integer) index).intValue() + 1%>
					</TD>
					<TD width="14%" align="center">
						<bean:write name="item" property="itemName" />
					</TD>
					<TD width="14%" align="center">
						<logic:present name="item" property="prevRiseStandard">
							<bean:write name="item" property="prevRiseStandard" />
						</logic:present>
						<logic:notPresent name="item" property="prevRiseStandard">
							0
						</logic:notPresent>
						%
					</TD>
					<TD width="14%" align="center">
						<logic:present name="item" property="prevFallStandard">
							<bean:write name="item" property="prevFallStandard" />
						</logic:present>
						<logic:notPresent name="item" property="prevFallStandard">
							0
						</logic:notPresent>
						%
					</TD>
					<TD width="14%" align="center">
						<logic:present name="item" property="sameRiseStandard">
							<bean:write name="item" property="sameRiseStandard" />
						</logic:present>
						<logic:notPresent name="item" property="sameRiseStandard">
							0
						</logic:notPresent>
						%
					</TD>
					<TD width="14%" align="center">
						<logic:present name="item" property="sameFallStandard">
							<bean:write name="item" property="sameFallStandard" />
						</logic:present>
						<logic:notPresent name="item" property="sameFallStandard">
							0
						</logic:notPresent>
						%
					</TD>

				</TR>
			</logic:iterate>
		</logic:present>
	</TABLE>
	<table border="0" cellpadding="0" cellspacing="4" width="90%" align="center">
		<tr>
			<td align="center">

				<input type="button" class="input-button" onclick="history.back()" value=" 返 回 ">

			</td>
		</tr>
	</table>

</body>
</html:html>
