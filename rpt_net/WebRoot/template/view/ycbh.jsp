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
	<title>
		�쳣���ݱ仯��׼
	</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
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
				<span style="FONT-SIZE: 11pt">
					 	��<logic:present name="ReportName" scope="request">
							<bean:write name="ReportName"/>
						</logic:present>��
				</span>
			</th>
		</tr>
		<tr>
			<td colspan="8" height="25" valign="middle">
				<b>�����쳣�仯�����õĻ������ͻ���ڻ�����</b>
			</td>
		</tr>
		<tr>
			<td colspan="8" bgcolor="#FFFFFF">
				<table border="0" cellpadding="4" cellspacing="0" width="100%">
					<%
									List list = (List)request.getAttribute("OrgClsNames");
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
																<a href="<%=request.getContextPath()%>/template/viewMOrg.do?orgClsId=<%=((MOrgClForm)list.get(i)).getOrgClsId()%>&orgClsName=<%=((MOrgClForm)list.get(i)).getOrgClsNm()%>"/><%=((MOrgClForm)list.get(i)).getOrgClsNm()%></a>
																				
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
												��ƥ���¼��
											</TD>
										</TR>
									<%
										
										}
															
									%>
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
				<b>
					���
				</b>
			</td>
			<TD width="20%" align="center" rowspan="2" class="tableHeader">
				<b>
					�쳣�仯��Ŀ
				</b>
			</TD>
			<TD width="32%" align="center" colspan="2" class="tableHeader">
				<b>
					������
				</b>
			</TD>
			<TD width="32%" align="center" colspan="2" class="tableHeader">
				<b>
					������ͬ��
				</b>
			</TD>
		</TR>
		<TR>
			<TD width="16%" align="center" class="tableHeader">
				<b>
					������׼
				</b>
			</TD>
			<TD width="16%" align="center" class="tableHeader">
				<b>
					�½���׼
				</b>
			</TD>
			<TD width="16%" align="center" class="tableHeader">
				<b>
					������׼
				</b>
			</TD>
			<TD width="16%" align="center" class="tableHeader">
				<b>
					�½���׼
				</b>
			</TD>
		</TR>
		<logic:present name="Records" scope="request">	
			<logic:iterate id="item" name="Records" indexId="index">
				<TR bgcolor="#FFFFFF" height="25">
					<TD width="2%" align="center">
						<%=((Integer)index).intValue() + 1%>
					</TD>
					<TD width="14%" align="center">
						<bean:write name="item" property="itemName"/>
					</TD>
					<TD width="14%" align="center">
						<logic:present name="item"  property="prevRiseStandard">
							<bean:write name="item" property="prevRiseStandard"/>
						</logic:present>
						<logic:notPresent name="item" property="prevRiseStandard">
							0
						</logic:notPresent>%
					</TD>
					<TD width="14%" align="center">
						<logic:present name="item" property="prevFallStandard">
							<bean:write name="item" property="prevFallStandard"/>
						</logic:present>
						<logic:notPresent name="item" property="prevFallStandard">
							0
						</logic:notPresent>%
					</TD>
					<TD width="14%" align="center">
						<logic:present name="item" property="sameRiseStandard">
							<bean:write name="item" property="sameRiseStandard"/>
						</logic:present>
						<logic:notPresent name="item" property="sameRiseStandard">
							0
						</logic:notPresent>%
					</TD>
					<TD width="14%" align="center">
						<logic:present name="item" property="sameFallStandard">
							<bean:write name="item" property="sameFallStandard"/>
						</logic:present>
						<logic:notPresent name="item" property="sameFallStandard">
							0
						</logic:notPresent>%
					</TD>
					
				</TR>
			</logic:iterate>
		</logic:present>
	</TABLE>
		<table border="0" cellpadding="0" cellspacing="4" width="90%" align="center">
			<tr>
				<td align="center">
					<input type="button" class="input-button" onclick="history.back()" value=" �� �� ">
				</td>
			</tr>
		</table>
	
</body>
</html:html>
