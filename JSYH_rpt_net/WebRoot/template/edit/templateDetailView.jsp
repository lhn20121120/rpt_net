<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<% 

String reportName = "";
	if (request.getAttribute("ReportName") != null) {
		reportName = (String) request.getAttribute("ReportName");
	} else {
		if (request.getParameter("reportName") != null)
			//reportName=FitechUtil.getParameter(request,"reportName");
			reportName = request.getParameter("reportName");

	}
  Operator operator=(Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String orgId=operator.getOrgId();

%>
<html:html locale="true">
	<head>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" type="text/css" rel="stylesheet">		
	</head>
	<script language="javascript">
		<logic:present name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
			var _curPage="<bean:write name='<%=Config.APART_PAGE_OBJECT%>' property='curPage' scope='request'/>";
		</logic:present>
		<logic:notPresent name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
			var _curPage="0";
		</logic:notPresent>
		
			/**
			 * ���ڱ���ϵ�޸��¼�
			 *
			 * @param childRepId �ӱ���ID
			 * @param versionId �汾��
			 * @return void
			 */
			 function _bjgx_mod(childRepId,versionId){
			 	window.location="<%=request.getContextPath()%>/template/mod/editBJGXInit.do" + 
			 		//1.14 yql ��templateId��ΪchildRepId
			 		"?childRepId=" + childRepId + 
			 		"&versionId=" + versionId + 
			 		"&curPage=" + _curPage;
			 }
			 /**
			 * ����ģ���޸��¼�
			 *
			 * @param childRepId �ӱ���ID
			 * @param versionId �汾��
			 * @return void
			 */
			 function _bbmb_mod(childRepId,versionId){
			 	window.location="<%=request.getContextPath()%>/template/viewTemplateRAQ.do" + 
			 		"?childRepId=" + childRepId + 
			 		"&versionId=" + versionId + 
			 		"&curPage=" + _curPage;
			 }
			 /**
			  * ���ͷ�Χ�޸��¼�
			  *
			  * @param childRepId �ӱ���ID
			  * @param versionId �汾��
			  * @return void
			  */
			  function _bsfw_mod(childRepId,versionId,reportName,orgId){
			  	window.location="<%=request.getContextPath()%>/template/mod/preEditBSFW.do" + 
			  		"?childRepId=" + childRepId + 
			 		"&versionId=" + versionId + 
			 		"&reportName="+reportName+
			 		"&curPage=" + _curPage+
			 		"&orgId="+orgId;
			  }
			 /**
			  * ����Ƶ���޸��¼�
			  *
			  * @param childRepId �ӱ���ID
			  * @param versionId �汾��
			  * @return void
			  */
			  function _bspl_mod(childRepId,versionId){
			  	window.location="<%=request.getContextPath()%>/template/mod/editBSPLInit.do" + 
			  		"?childRepId=" + childRepId + 
			 		"&versionId=" + versionId + 
			 		"&curPage=" + _curPage;
			  }
			function _back(){
				window.location="<%=request.getContextPath()%>/template/viewTemplate.do";
			}
		
	</script>
	<body>
		<logic:present name="Message" scope="request">
			<logic:greaterThan name="Message" property="size" value="0">
				<script language="javascript">
						alert("<bean:write name='Message' property='alertMsg'/>");
					</script>
			</logic:greaterThan>
		</logic:present>
		<TABLE cellSpacing="0" width="95%" border="0" align="center" cellpadding="4">
			<tr>
				<td height="8">
				</td>
			</tr>
			<tr>
				<td>
				 	��ǰλ�� >> ������� >> ��������� >> ģ���޸�
				</td>
			</tr>
			<tr>
				<td height="10"> 
				</td>
			</tr>
				<TR>
					<TD>
						<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0"  class="tbcolor">
							<tr class="titletab">
								<th colspan="8" align="center" id="list">
									�޸ı�����Ϣ
								</th>
							</tr>
				<TR class="middle" align="center">
					<TD width="30%" align="center">
						<b>ģ������</b>
					</TD>
					<td width="10%" align="center">
						<b>�汾��</b>
					</td>
					<TD width="10%" align="center">
						<b>��Ч��</b>
					</TD>
					<TD width="15%" align="center">
						<b>����ģ��</b>
					</TD>
					<td width="15%" align="center"> 
						<b>���ڱ���ϵ</b>		
					</td>
					<TD width="10%" align="center">
						<b>���ͷ�Χ</b>
					</TD>
					<td width="10%" align="center"> 
						<b>����Ƶ��</b>		
					</td>

				</TR>
					<logic:present name="Records" scope="request">
						<logic:iterate id="item" name="Records">
							<TR bgcolor="#FFFFFF">
								<TD align="center" width="30%">
									<bean:write name="item" property="reportName"/>
								</TD>
								<TD align="center" width="10%">
									<bean:write name="item" property="versionId"/>
								</TD>
								<TD  align="center" width="10%">
									<bean:write name="item" property="endDate"/>
								</TD>
								<TD  align="center" width="15%">
									<a href="javascript:_bbmb_mod('<bean:write name="item" property="childRepId"/>','<bean:write name="item" property="versionId"/>')">�޸�</a>
								</TD>
								<TD  align="center" width="15%">
									<a href="javascript:_bjgx_mod('<bean:write name="item" property="childRepId"/>','<bean:write name="item" property="versionId"/>')">�޸�</a>
								</TD>
								<TD  align="center" width="10%">									
									<a href="javascript:_bsfw_mod('<bean:write name="item" property="childRepId"/>','<bean:write name="item" property="versionId"/>','<bean:write name="item" property="reportName"/>','<%=orgId%>')">�޸�</a>
								</TD>
								<TD  align="center" width="10%">
									<a href="javascript:_bspl_mod('<bean:write name="item" property="childRepId"/>','<bean:write name="item" property="versionId"/>')">�޸�</a>
								</TD>
							</TR>
						</logic:iterate>
					</logic:present>
					<logic:notPresent name="Records" scope="request">
							<tr bgcolor="#FFFFFF">
								<td colspan="6">
									��ƥ���¼
								</td>
							</tr>
					</logic:notPresent>
					</TABLE>
						<table cellSpacing="1" cellPadding="4" width="100%" border="0">
							<tr>
								<TD colspan="7">
								<jsp:include page="../../apartpage.jsp" flush="true">
									<jsp:param name="url" value="../../template/viewTemplate.do" />
								</jsp:include>
								</TD>
							</tr>
					</table>
					<table border="0" cellpadding="0" cellspacing="4" width="100%" align="center">
						<tr>
							<td align="center">
								<input type="button" class="input-button" onclick="_back()" value=" �� �� ">
							</td>
						</tr>
					</table>
				</TD>
			</TR>
		</TABLE>		
	</body>
</html:html>
