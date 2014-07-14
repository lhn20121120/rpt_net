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
		<link href="../css/common.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="../../js/func.js"></script>
	</head>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<body style="TEXT-ALIGN: center">
			<div align="center">
			<br>
			<br>
				<TABLE cellSpacing="0" width="95%" border="0" align="center" cellpadding="4">
					<TR>
						<td><INPUT class="input-button" id="Button3" type="button" value=" �����ʷ���� " name="butBack" onclick="clearHistory()"></td>
      		       </TR>
					<TR>
						<TD>
							<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0"  class="tbcolor">
								<tr class="titletab">
									<th colspan="7" align="center" id="list">
										<strong>
											ģ���б�
										</strong>
									</th>
								</tr>
								<TR class="middle">
									<TD class="tableHeader" width="40%">
										<b>ģ������</b>
									</TD>
									<TD class="tableHeader" width="10%">
										���
									</td>
									<TD class="tableHeader" width="10%">
										�汾��
									</td>
									<TD class="tableHeader" width="20%">
										<b>��������</b>
									</TD>
									<TD class="tableHeader" width="10%">
										<b>ȡ������</b>
									</TD>
									<!-- <TD class="tableHeader" width="20%">
										<b>�������</b>
									</TD>-->
									</TR>
									<logic:present name="Records" scope="request">
											<logic:iterate id="item" name="Records">
												<TR bgcolor="#FFFFFF">
													<TD align="center">
														<logic:notEmpty name="item" property="reportName">
															<a href="<%=request.getContextPath()%>/servlets/ReadPDFServlet?childRepId=<bean:write name='item' property='childRepId'/>&versionId=<bean:write name='item' property='versionId'/>" target="_blank">
															<bean:write name="item" property="reportName"/>
															</a>
														</logic:notEmpty>
														<logic:empty name="item" property="reportName">
															��
														</logic:empty>
													</TD>
													<td align="center"><bean:write name="item" property="childRepId"/></td>
													<td align="center"><bean:write name="item" property="versionId"/></td>
													<TD align="center">
														<logic:notEmpty name="item" property="repTypeName">
															<bean:write name="item" property="repTypeName"/>
														</logic:notEmpty>
														<logic:empty name="item" property="repTypeName">
															��
														</logic:empty>
													</TD>
													<!-- <td align="center"><bean:write name="item" property="templateType"/></td>-->
													<TD align="center">
														<a href="<bean:write name="obtianUrl"/>?repID=<bean:write name="item" property="childRepId"/>&versionID=<bean:write name="item" property="versionId"/>">ȡ������</a>
													</TD>
													<!-- <TD align="center">
														<a href="javascript:clearHistory('<bean:write name='item' property='childRepId'/>','<bean:write name='item' property='versionId'/>')">�����ʷ����</a>
													</TD>-->
													</TR>
											</logic:iterate>
													
									</logic:present>
									<logic:notPresent name="Records" scope="request">
										<tr bgcolor="#FFFFFF">
											<td colspan="7">
												���޼�¼
											</td>
										</tr>
									</logic:notPresent>
								</TABLE>
									<table cellSpacing="1" cellPadding="4" width="100%" border="0">
										<tr>
											<TD colspan="7" bgcolor="#FFFFFF">
											<jsp:include page="../apartpage.jsp" flush="true">
												<jsp:param name="url" value="../viewTemplateNet.do" />
											</jsp:include>
											</TD>
										</tr>
									</table>
						</TD>
					</TR>
				</TABLE>	
					
	</body>
</html:html>
<script language="javascript">
	function clearHistory(childRepId,versionID){
		window.location="<%=request.getContextPath()%>/clearHistory.do?childRepId="+childRepId+"&versionId="+versionID;
	}
</script>
