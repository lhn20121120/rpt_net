<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
	<head>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="../../js/func.js"></script>
	</head>
	<body style="TEXT-ALIGN: center">
		<html:form action="report_view/ViewAFTemplateAction.do" method="POST" >
			<div align="center">
				<table border="0"width="95%">
					<tr>
						<td height="8">
						</td>
					</tr>
					<tr>
						 <td>
				��ǰλ�� &gt;&gt; �쵼��ҳ  &gt;&gt; ��Ӫ״��һ��  &gt;&gt; �������
							 </td>
					</tr>
					<tr>
						<td height="10"> 
						</td>
					</tr>
					<TR>
						<td align="right" width="45%">
						<TD width="20%" align="right">
							�������ƣ�
						</TD>
						<TD width="15%" align="left">
							<html:text property="templateName" size="28"  styleClass="input-text"/>
						</TD>
						<td  width="25%" align="left">
							<html:submit value="��  ѯ" styleClass="input-button" />
						</td>
					</TR>
				</table>
				</div>
				<TABLE cellSpacing="0" width="95%" border="0" align="center" cellpadding="4">				
					<TR>
						<TD>
							<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0"  class="tbcolor">
								<tr class="titletab">
									<th colspan="8" align="center" id="list">
										<strong>
											ģ���б�
										</strong>
								</th>
								<TR class="middle">
								<TD class="tableHeader" width="8%">
										<b>����ID </b>
									</TD>
									<TD class="tableHeader" width="8%">
										<b>�汾��</b>
									</td>
									<TD class="tableHeader" width="37%">
										<b>��������</b>
									</TD>
									<TD class="tableHeader" width="15%">
										<b>��������</b>
									</TD>
									<TD class="tableHeader" width="8%">
										<b>��ʼʱ��</b>
									</td>
									<TD class="tableHeader" width="8%">
										<b>����ʱ��</b>
									</td>
									<TD class="tableHeader" width="8%">
										<b>��ϸ��Ϣ</b>
									</TD>									
								</TR>					
								<logic:present name="Records" scope="request">
											<logic:iterate id="item" name="Records">
									
										<TR bgcolor="#FFFFFF">
									
												<td align="center"><bean:write name="template" property="templateId"/></td>
												<td align="center"><bean:write name="template" property="versionId"/></td>
											
											<TD align="center">
											<bean:write name="template" property="templateName"/>												
											<TD align="center">
												<logic:notEmpty name="template" property="templateType">
													<logic:notEmpty name="template" property="templateType">
													<bean:write name="template" property="templateType"/>
													</logic:notEmpty>
												</logic:notEmpty>
												<logic:empty name="template" property="templateType">
													��
												</logic:empty>
											</TD>
											<td align="center">
													<bean:write name="template" property="startDate"/>
													</td>
											<td align="center">
													<bean:write name="template" property="endDate"/>
												</td>
											
											<TD align="center">
												<input type="button" name="addTemplateType" value="�鿴" onclick="javascript:viewtemplate('<bean:write name="template" property="templateId"/>','<bean:write name="template" property="versionId"/>')" class="input-button">
												<a href=" <%=request.getContextPath()%>/manager_frontpage/report_view/new.jsp?templateId=<bean:write name="template" property="templateId"/>&versionId=<bean:write name="template" property="versionId"/>">�鿴</a> 
											</TD>
										</TR>
									</logic:iterate>
								</logic:present>
								<logic:notPresent name="Records" scope="request">
										<tr bgcolor="#FFFFFF">
											<td colspan="8">
												���޼�¼
											</td>
										</tr>
									</logic:notPresent>
							</TABLE>
							<table cellSpacing="1" cellPadding="4" width="100%" border="0">
								<tr>
									<TD colspan="7" bgcolor="#FFFFFF">
									<jsp:include page="../../apartpage.jsp" flush="true">
										<jsp:param name="url" value="<%=request.getContextPath()%>/report_view/ViewAFTemplateAction.do" />
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
