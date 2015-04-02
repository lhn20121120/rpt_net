<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.List,com.cbrc.org.form.MOrgClForm,com.cbrc.smis.util.FitechUtil" %>

<html:html locale="true">
	<head>
		<html:base/>
		<title>报表填报范围</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
		<script language="javascript">
			function viewDetail(orgClsId,orgClsName)
			{
				document.getElementById("orgClsId").value=orgClsId;
				document.getElementById("orgClsName").value=orgClsName;
				document.getElementById("viewDetailForm").submit();
				
			}
		</script>
	</head>
	
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	
	<body background="../image/total.gif">
	<br>
		<TABLE cellSpacing="1" cellPadding="4" width="96%" border="0" align="center" class="tbcolor">
			<TR class="tbcolor1">
				<th align="center" id="list" height="30">
					<span style="FONT-SIZE: 11pt">
					 	《<logic:present name="ReportName" scope="request">
							<bean:write name="ReportName"/>
						</logic:present>》
					</span>
				</th>
			</TR>
			<tr>
				<td align="left" bgcolor="#EEEEEE">
					<b>填报范围所适用的机构类型或金融机构：</b>
				</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF">
					<table border="0" cellpadding="4" cellspacing="0" width="100%">
					
								<%
									List list = (List)request.getAttribute("Records");
										if(list!=null && list.size()!=0)
										{
												for(int i=0;i<list.size();i++)
												{
														if(i%3==0)
														{
								%>
										<tr >
								<%
														}
								%>
											<td align="center" bgcolor="#ffffff" height="30">
								<%
														if(list!=null && list.size()!=0)
														{
															if(list.get(i)!=null)
															{
												%>
													<a href="javascript:viewDetail('<%=((MOrgClForm)list.get(i)).getOrgClsId()%>','<%=((MOrgClForm)list.get(i)).getOrgClsNm()%>')"><%=((MOrgClForm)list.get(i)).getOrgClsNm()%></a>
																				
												<%
															}
														}
											
																		
									%>
											</td>
									<%
															if(i%3==2)
															{
									%>
										</tr>
									<%
															}
												}
													
										}
										else
										{
									%>
										<TR>
											<TD>
												无匹配记录！
											</TD>
										</TR>
									<%
										
										}
															
									%>
						</table>
					</td>
				</tr>
				
			</TABLE>		
		<table border="0" cellpadding="2" cellspacing="0" width="96%">
			<tr>
				<td height="5"></td>
			</tr>
			<tr>
				<td align="left">
				注：点击机构类型，可以具体查看当前类型下的金融机构的填报范围。
				</td>
			</tr>
			<tr>
				<td align="center" colspan="3">
					<input class="input-button" onclick="history.back()" type="button" value=" 返 回 ">
				</td>
			</tr>
			
			
		</table>
		<form action="../view/viewTBFWOrgDetail.do" method="Post" id="viewDetailForm">
			<input type="hidden" name="orgClsId" id="orgClsId">
			<input type="hidden" name="orgClsName" id="orgClsName">
			
			<logic:present name="ReportName" scope="request">
				<input type="hidden" name="childRepId" id="childRepId" value="<bean:write name="ChildRepId"/>">
			</logic:present>
			<logic:present name="ReportName" scope="request">
				<input type="hidden" name="versionId" id="versionId" value="<bean:write name="VersionId"/>">
			</logic:present>
			<logic:present name="ReportName" scope="request">
				<input type="hidden" name="reportName" id="reportName" value="<bean:write name="ReportName"/>">
			</logic:present>	
		</form>
	</body>
</html:html>
