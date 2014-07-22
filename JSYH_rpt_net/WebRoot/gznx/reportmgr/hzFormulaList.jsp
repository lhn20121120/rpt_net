<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%
	/** ����ѡ�б�־ **/
	String reportFlg = "0";	
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
	}
	String templateId=(String)request.getAttribute("childRepId");
	String versionId=(String)request.getAttribute("versionId");
	String reportName=(String)request.getAttribute("reportName");
	String orgId=(String)request.getAttribute("orgId");
	String next=(String)request.getAttribute("next");
	
%>
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="reportFlg" name="FormBean" value="<%=reportFlg%>"/>

<html:html locale="true">
	<head >
	<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="<%=request.getContextPath()%>/css/common.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="<%=request.getContextPath()%>/js/func.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/tree/tree.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/tree/defTreeFormat.js"></script>
		<script language="javascript" src="<%=request.getContextPath()%>/js/func.js"></script>
		<script language="javascript" src="<%=request.getContextPath()%>/js/jquery-1.4.2.js"></script>
		<script language="javascript">
		function goHZRule(templateId,versionId,reportName,orgId){
			
			window.location="<%=request.getContextPath()%>/gznx/HZRule.do?templateId="+templateId+"&versionId="+versionId+"&reportName="+reportName+"&orgId="+orgId;
		}
			

	

		
	

		function _submit(childRepId,versionId,reportName,orgId,opration){
			window.location="<%=request.getContextPath()%>/gznx/preSetBSSD.do?childRepId="+childRepId+"&versionId="+versionId+"&reportName="+reportName+"&orgId="+orgId+"&opration="+opration;
		}
		
		// add by ������
		function changeToLavender(obj){
			obj.bgColor="lavender";
		}
		function changeToWhite(obj){
			
			obj.bgColor="#FFFFFF";
		}
		</script>
	</head>
	<body style="TEXT-ALIGN: center">
			<logic:present name="Message" scope="request">
				<logic:greaterThan name="Message" property="size" value="0">
					<script language="javascript">
						alert("<bean:write name='Message' property='alertMsg'/>");
					</script>
				</logic:greaterThan>
			</logic:present>
			<html:form action="/gznx/hzformula.do" method="POST" >
			<html:hidden property="templateId" value="<%=templateId %>"/>
			<input type="hidden"  name="opration"  value="next"/> 
			<div align="center">
				<table border="0" width="98%">
					<tr>
						<td height="10">
						</td>
					</tr>
					<tr>
						 <td>
						 	��ǰλ�� &gt;&gt; �������� &gt;&gt; ���ܹ�ʽ����
						 </td>
					</tr>
					<tr>
						<td height="10"> 
						</td>
					</tr>
				</table>
				<table border="0" width="98%">	
					<TR>
						<TD width="20%" align="right">
							�������ƣ�
						</TD>
						<TD width="20%" align="left">
							<html:text property="orgName" size="20"  styleClass="input-text"/>
							
						</TD>
						<td width="10%" align="left">
							<html:submit value="��  ѯ" styleClass="input-button"/>
						</td>
						<td width="50%" align="center">
							
						</td>
					</TR>
				</table>
			</div>
				<TABLE cellSpacing="0" width="95%" border="0" align="center" cellpadding="4">
				<br>
					<TR>
						<TD>
							<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0"  class="tbcolor">
								<tr class="titletab">
									<th colspan="8" align="center" id="list">
										<strong>
											���ܹ�ʽ����
										</strong>
									</th>
								</tr>
								<TR class="middle">
									
									<TD class="tableHeader" width="8%">
										<b>���</b>
									</td>
									<TD class="tableHeader" width="37%">
										<b>��������</b>
									</TD>
									<TD class="tableHeader" width="7%">
										<b>���ܷ�ʽ</b>
									</TD>
			
									<TD class="tableHeader" width="8%">
										<b>����</b>
									</td>
																
								</TR>
								<%
									int i=1;
								%>
									<logic:present name="Records" scope="request">
											<logic:iterate id="item" name="Records">
												<TR bgcolor="#FFFFFF"  onmouseover="changeToLavender(this)" onmouseout="changeToWhite(this)">
													<td align="center"><%=i++%></td>
													<TD align="center">
														<logic:notEmpty name="item" property="orgName">
															<bean:write name="item" property="orgName"/>
														</logic:notEmpty>
														<logic:empty name="item" property="orgName">
															��
														</logic:empty>
													</TD>
													
													<td align="center">
														<logic:notEmpty name="item" property="collSchema">
															�������
														</logic:notEmpty>
														<logic:empty name="item" property="collSchema">
															����
														</logic:empty>
													</td>
													
													<%--
													<td align="center"><bean:write name="item" property="startDate"/></td>
													<td align="center"><bean:write name="item" property="endDate"/></td>
													<td align="center">
														<logic:equal name="item" property="usingFlag" value="<%=String.valueOf(Config.IS_PUBLIC)%>">
															<a href="javascript:updateunState('<bean:write name='item' property='templateId'/>','<bean:write name='item' property='versionId'/>')"  alt="δ����"><font color="blue">�ѷ���</font></a>
														</logic:equal>
														<logic:notEqual name="item" property="usingFlag" value="<%=String.valueOf(Config.IS_PUBLIC)%>">															
															<a href="javascript:updateState('<bean:write name='item' property='templateId'/>','<bean:write name='item' property='versionId'/>')"  alt="����">δ����</a>
														</logic:notEqual>
													</td>
													--%>
													<TD align="center">
														<a href="javascript:goHZRule('<bean:write name="item" property="templateId"/>','<bean:write name="item" property="versionId"/>','<bean:write name="item" property="reportName"/>','<bean:write name="item" property="orgId"/>')">���ܹ�ʽ�趨</a>
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
									<%--<table cellSpacing="1" cellPadding="4" width="100%" border="0">
										<tr>
											<TD colspan="7" bgcolor="#FFFFFF">
											<jsp:include page="../../apartpage.jsp" flush="true">
												<jsp:param name="url" value="../../gznx/hzformula.do" />
											</jsp:include>
											</TD>
										</tr>
									</table>
						--%></TD>
					</TR>
				</TABLE>	
	   
			</html:form>
			<logic:present name="next">
		<input type="button" class="input-button" name="save" value="��һ��"
		id="subButton" onclick="_submit('<%=templateId %>','<%=versionId %>','<%=reportName %>','<%=orgId %>','<%=next %>')">
		&nbsp;&nbsp;&nbsp;&nbsp;
	</logic:present>
	<logic:empty name="next">
		<input type="button" class="input-button" name="save" value="��   ��"
		id="subButton" onclick="">
		<input type="button" class="input-button" onclick="" value=" �� �� ">
	</logic:empty>
			
	</body>
	
</html:html>