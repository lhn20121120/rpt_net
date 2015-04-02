<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.util.FitechUtil"%>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config"/>
<%
	String childRepId="",versionId="",reportName="",reportStyle="";
	if(request.getParameter("childRepId")!=null) childRepId=(String)request.getParameter("childRepId");
	if(request.getParameter("versionId")!=null) versionId=(String)request.getParameter("versionId");

	if(request.getParameter("reportName")!=null) reportName=FitechUtil.getParameter(request,"reportName");
	if(com.cbrc.smis.common.Config.WEB_SERVER_TYPE==0){
		 reportName=new String(reportName.getBytes("iso-8859-1"), "gb2312");
	 }
	if(request.getParameter("reportStyle")!=null) reportStyle=(String)request.getParameter("reportStyle");
%>
<html:html locale="true">
	<head>
		<html:base/>
		<title>ѡ����ڡ�����ϵ���ʽ�����ļ�</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">		
		<script language="javascript" src="../../js/comm.js"></script>
		<script language="javascript" src="../../js/func.js"></script>
		<script language="javascript">
			/**
			 * ���ڱ���ϵ���ʽ�ļ���׺��
			 */ 
			 var EXT_TXT="<%=configBean.EXT_TXT%>";
			/**
			 * ���ύ�¼�
			 */
			 function _submit(form){
			 	if(form.expressionFile.value==""){
			 		alert("��ѡ��Ҫ���ڱ���ϵ���ʽ�����ļ�!\n");
			 		return false;
			 	}else{
			 		if(getExt(form.expressionFile.value)!=EXT_TXT){
			 			alert("ѡ��ı���ģ���ļ���˳��" + EXT_TXT + "�ļ�!");
			 			return false;
			 		}
			 	}
			 	
			 	return true;
			 }
			 /**
			  * �����¼�
			  */
			  function _back(){
			  	var form=document.forms['mCellForumForm'];
			  	window.location="addbjgx.jsp?childRepId=" + form.elements['childRepId'].value +
			  		"&versionId=" + form.elements['versionId'].value + 
			  		"&reportName=" + form.elements['reportName'].value +
			  		"&reportStyle=" + form.elements['reportStyle'].value;
			  }
		</script>
	</head>
	<body>
		<center>
		<table border="0" cellspacing="0" cellpadding="4" width="60%" align="center">
					<tr><td height="8"></td></tr>
					<tr>
						<td>
							��ǰλ�� >> ������� >> ���ڱ���ϵ�趨 
						</td>
					</tr>
					<tr><td height="10"></td></tr>		
		</table>
		<table border="0" cellpadding="4" cellspacing="1" width="60%" class="tbcolor">
			<tr>
				<td align="left" bgcolor="#D0D6EE">
					��ѡ����ڱ���ϵ���ʽ�����ļ���
				</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF">
					<html:form method="post" action="/gznx/uploadBJGX.do?flag=add" enctype="multipart/form-data" onsubmit="return _submit(this)">
					<table border="0" cellpadding="4" cellspacing="0" width="100%">
						<tr bgcolor="#FFFFFF">
							<td align="center">
								<input type="file" name="expressionFile" size="60" class="input-text">
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td align="center">
								<input type="submit" value="ȷ��" class="input-button">&nbsp;&nbsp;
								<input type="button" value="����" class="input-button" onclick="_back()">
							</td>
						</tr>
					</table>
					<input type="hidden" name="childRepId" value="<%=childRepId%>">
					<input type="hidden" name="versionId" value="<%=versionId%>">
					<input type="hidden" name="reportName" value="<%=reportName%>">
					<input type="hidden" name="reportStyle" value="<%=reportStyle%>">
					</html:form>
				</td>
			</tr>
		</table>
		</center>
	</body>
</html:html>