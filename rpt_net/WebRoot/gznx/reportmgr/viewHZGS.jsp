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
	String templateId=(String)request.getAttribute("templateId");
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
//			alert("222222222222----2");
			function goback() {
			// var form=document.forms['frmBJGX'];
//			alert("33333333333");
				window.location="<%=request.getContextPath()%>/viewAFTemplateDetail.do?templateId=<%=request.getAttribute("templateId")%>&versionId=<%=request.getAttribute("versionId")%>&bak2=1";
//				alert(window.location);
		   }
			function _query(){
				var frmHZGS=document.forms['frmHZGS'];
				var templateId=document.getElementById("templateId").value;
				var orgName=document.getElementById("orgName").value;
				frmHZGS.action="<%=request.getContextPath()%>/gznx/EditHZGS.do?templateId="+templateId+"&orgName="+orgName+"&opration=view&versionId=<%=versionId%>";
		//		alert(frmHZGS.action);
				frmHZGS.submit();
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
			<html:form action="/gznx/EditHZGS.do" method="POST" styleId="frmHZGS">
			<html:hidden property="templateId" value="<%=templateId%>" styleId="templateId"/>
			<div align="center">
				<table border="0" width="98%">
					<tr>
						<td height="10">
						</td>
					</tr>
					<tr>
						 <td>
						 	��ǰλ�� &gt;&gt; ������� &gt;&gt; ���ܹ�ʽ����
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
							<html:text property="orgName" size="20"  styleClass="input-text" styleId="orgName"/>
						</TD>
						<td width="10%" align="left">
							<input type="button" value="��  ѯ" styleClass="input-button" onclick="_query()"/>
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
														���ܹ�ʽ�趨
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
									
						</TD>
					</TR>
					<tr>
					<td align="center">
						
						<input type="button" value="��  ��" class="input-button" onclick="goback()" />
					</td>
					<td>
					</td>

				</tr>
				</TABLE>	
	   
			</html:form>
	
	
			
	</body>
	
</html:html>
