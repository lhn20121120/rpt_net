<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepSearchPodedom = operator != null ? operator.getChildRepSearchPopedom() : "";
	/** ����ѡ�б�־ **/
	String reportFlg = "0";	
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
	}		
%>
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="reportFlg" name="FormBean" value="<%=reportFlg%>"/>
<jsp:setProperty property="orgPodedom" name="FormBean" value="<%=childRepSearchPodedom%>"/>
<html:html locale="true">
	<head>
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
		<script language="javascript">
		function _view(templateId,templateName,versionId){
			var templateType = document.getElementById('templateType').value;
			var templateName = document.getElementById('templateName').value;
			window.location="<%=request.getContextPath()%>/viewCustomAFReportDetail.do?templateId="
																					+templateId+"&templateName="
																					+templateName+"&versionId="
																					+versionId+"&backQry="+templateType+"_"+templateName;
		}
				
		function treeOnClick(id,value)
			{
				document.getElementById('templateType').value = id;
				document.getElementById('templateTypeName').value = value;
				document.getElementById("orgTree").style.height = "0";
				document.getElementById('orgTree').style.visibility="hidden"; 
			}
			

		//��ʾ,�ر����Ͳ˵�
		function showTree(){
		if(document.getElementById('orgTree').style.visibility =='hidden'){
		    var textname = document.getElementById('selectedTypeName');
			document.getElementById("orgTree").style.top = getObjectTop(textname)+20;
			document.getElementById("orgTree").style.left = getObjectLeft(textname);
			
			document.getElementById("orgTree").style.height = "200";
			document.getElementById("orgTree").style.visibility = "visible";   // ��ʾ���Ͳ˵�
		}

		else if(document.getElementById("orgTree").style.visibility == "visible"){
			document.getElementById("orgTree").style.height = "0";
			document.getElementById('orgTree').style.visibility="hidden";      //�ر����Ͳ˵�
		}
	}
	
		function closeTree(objTxt,objTree){	  
		   var obj = event.srcElement;
		   if(obj!=objTxt && obj!=objTree){
		     
		     objTree.style.height = "0";
		     objTree.style.visibility="hidden";      //�ر����Ͳ˵�
		   }
		}
		
		//�����ı����ˮƽ���λ��
		function getObjectLeft(e)   
		{   
			var l=e.offsetLeft;   
			while(e=e.offsetParent)   
				l += e.offsetLeft;   
			return   l;   
		}   
		//�����ı���Ĵ�ֱ���λ��
		function getObjectTop(e)   
		{   
			var t=e.offsetTop;   
			while(e=e.offsetParent)   
				t += e.offsetTop;   
			return   t;   
		}
		</script>
	</head>
	<body style="TEXT-ALIGN: center">
			
			<html:form action="/customViewAFReport" method="POST">
			<input type="hidden" name="templateType" value="<bean:write  name="form"  property="templateType"/>"/>
			<div align="center">
				<table border="0"width="100%">
					<tr>
						<td height="8">
						</td>
					</tr>
					<tr>
						 <td>
						 	��ǰλ�� >> �����ѯ >> ����ѯ
						 </td>
					</tr>
					<tr>
						<td height="10"> 
						</td>
					</tr>
					</table>
					<table cellSpacing="0" cellPadding="4" width="100%" border="0" align="center">
					<tr>
					<td>
					<fieldset id="fieldset">					
						<table cellSpacing="0" cellPadding="4" width="100%" border="0" align="center">
						<TR>
				
						<TD width="10%" align="right">
							�������ƣ�
						</TD>
						<TD width="20%" align="center">
							<html:text property="templateName" size="28"  styleClass="input-text"/>
						</TD>
						<td width="20%" align="center">
							<html:submit value="��  ѯ" styleClass="input-button"/>
						</td>
					</TR>
					</table>		
					</fieldset>		
				</td>
				</tr>			
				</table>
			</div>
				<TABLE cellSpacing="0" width="100%" border="0" align="center" cellpadding="4">
				
					
					<TR>
						<TD>
							<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0"  class="tbcolor">
								<tr class="titletab">
									<th colspan="8" align="center" id="list">
										<strong>
											�����б�
										</strong>
									</th>
								</tr>
								<TR class="middle">
									<TD class="tableHeader" width="8%">
										<b>������</b>
									</td>
									<TD class="tableHeader" width="33%">
										<b>��������</b>
									</TD>
																
									<TD class="tableHeader" width="15%">
										<b>�汾ID</b>
									</TD>
									<TD class="tableHeader" width="10%">
										<b>��ʼʱ��</b>
									</td>
									<TD class="tableHeader" width="10%">
										<b>����ʱ��</b>
									</td>
									<TD class="tableHeader" width="12%">
										<b>����</b>
									</TD>									
								</TR>
									<logic:present name="Records" scope="request">
											<logic:iterate id="item" name="Records">
												<TR bgcolor="#FFFFFF">
												<td align="center"><bean:write name="item" property="templateId"/></td>
													<TD align="center">
														<logic:notEmpty name="item" property="templateName">
															<bean:write name="item" property="templateName"/>
														</logic:notEmpty>
														<logic:empty name="item" property="templateName">
															��
														</logic:empty>
													</TD>
													
													<!--  <td align="center"><bean:write name="item" property="versionId"/></td> -->
													<TD align="center">
														<bean:write name="item" property="versionId"/>
													</TD>
													<td align="center"><bean:write name="item" property="startDate"/></td>
													<td align="center"><bean:write name="item" property="endDate"/></td>
														<TD align="center">
													<%--	<a href="../../viewCustomAFReportDetail.do?templateId=<bean:write name="item" property="templateId"/>&templateName=<bean:write name="item" property="templateName"/>&versionId=<bean:write name="item" property="versionId"/>">�鿴</a>
													--%>
															<a href="JavaScript:_view('<bean:write name="item" property="templateId"/>','<bean:write name="item" property="templateName"/>','<bean:write name="item" property="versionId"/>')">�鿴</a>
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
												<jsp:param name="url" value="../../customViewAFReport.do" />
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
