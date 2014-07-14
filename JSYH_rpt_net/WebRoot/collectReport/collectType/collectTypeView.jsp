<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.security.Operator" %>
<%@ page import="com.cbrc.smis.common.Config" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<jsp:useBean id="utilSubOrgForm" scope="page" class="com.fitech.net.form.UtilSubOrgForm" />

<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../../js/func.js"></script>
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>

	<SCRIPT language="javascript">
		<logic:present name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="<bean:write name='ApartPage' property='curPage'/>";
	    </logic:present>
	    <logic:notPresent name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="1";
	    </logic:notPresent>
	    			 
		  	 function _addCollectType(){
		  	 	var orgId = document.getElementById("orgId");
		  	 	if(orgId.value == 0){
		  	 		alert("��ѡ����ܷ�ʽ����������");
		  	 		orgId.focus();
		  	 		return;
		  	 	}
		  	 	window.location="<%=request.getContextPath()%>/collectType/viewCollectTypeOrgReport.do?orgId=" + orgId.value;
		  	 }
		  	 
		  	 //ɾ����ʾ
		     function _delete(collectId){
		     	if(confirm("��ȷ��Ҫɾ���û��ܷ�ʽ��"))
		     		window.location = "<%=request.getContextPath()%>/collectType/deleteCollectType.do?collectId="+ collectId +"&curPage=" + curPage;	
		     }
		     
		     //�޸�
		     function _edit(collectId,orgId){
		     	window.location = "<%=request.getContextPath()%>/collectType/editCollectType.do?collectId="+ collectId + "&orgId=" + orgId +"&curPage=" + curPage;	
		     }
	</SCRIPT>
</head>
<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table border="0" width="90%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				��ǰλ�� >> ���ݻ��� >> ���ܷ�ʽ�趨
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="0" border="0" width="90%" align="center">
		<html:form action="/collectType/viewCollectType" method="post" styleId="frm" onsubmit="return _submit(this)">
			<tr>
				<td>
					<fieldset id="fieldset">
					<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
						<tr>
							<td height="5"></td>
						</tr>
						<tr>
							<td height="25">&nbsp;
								���ܷ�ʽ����������
								<html:select property="orgId">
									<html:option value="0">----��ѡ����������----</html:option>
									<html:optionsCollection name="utilSubOrgForm" property="notLowerOrgs"/>
								</html:select>
							</td>
							
							<td>
								���ܷ�ʽ���ƣ�
								<html:text property="collectName" size="25" styleClass="input-text" />
							</td>
							<td>
								<html:submit styleClass="input-button" value=" �� ѯ " />								
								<INPUT type="button" class="input-button" onClick="javascript:_addCollectType()" value="�� ��">
							</td>
							<td>&nbsp;
							</td>
						</tr>
					</table>					
					</fieldset>
				</td>
			</tr>
		</html:form>
	</table>
	<br>
	<table border="0" cellpadding="0" cellspacing="0" width="90%" align="center">
		<html:form action="/collectType/viewCollectType" method="post" styleId="frmChk">
			<tr>
				<td>
					<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
						<TR class="titletab">
							<th width="10%" align="center" valign="middle">
								�������
							</th>							
							<th width="" align="center" valign="middle">
								���ܷ�ʽ����
							</th>
							<th width="20%" align="center" valign="middle">
								���ܷ�ʽ��������
							</th>
							<th width="15%" align="center" valign="middle">
								����
							</th>							
						</TR>						
						<logic:present name="Records" scope="request">
							<logic:iterate id="collectType" name="Records">								
								<TR bgcolor="#FFFFFF">									
									<TD align="center">
										<bean:write name="collectType" property="collectId" />
									</TD>
									<TD align="center">
										<a href=""  title="�鿴���ܷ�ʽ��ϸ��Ϣ"><bean:write name="collectType" property="collectName" /></a>
									</TD>
									<TD align="center">
										<bean:write name="collectType" property="orgName" />
									</TD>
									<TD align="center">
										<a href="javascript:_edit(<bean:write name="collectType" property="collectId" />,'<bean:write name="collectType" property="orgId" />')">�޸�</a>&nbsp;&nbsp;
										<a href="javascript:_delete(<bean:write name="collectType" property="collectId" />)">ɾ��</a>
									</TD>							
								</TR>
							</logic:iterate>
						</logic:present>						
						<logic:notPresent name="Records" scope="request">
							<tr align="left">
								<td bgcolor="#ffffff" colspan="4">
									���޷��������ļ�¼
								</td>
							</tr>
						</logic:notPresent>
					</table>
				</td>
			</tr>			
		</html:form>
	</table>
	<table cellSpacing="0" cellPadding="0" width="90%" border="0">
		<TR>
			<TD>
				<jsp:include page="../../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../../collectType/viewCollectType.do" />
				</jsp:include>
			</TD>
		</TR>
	</table>
</body>
</html:html>
