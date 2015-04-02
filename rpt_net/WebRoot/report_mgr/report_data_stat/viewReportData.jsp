<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.security.Operator,com.cbrc.smis.common.Config"%>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm"/>
<%
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	com.fitech.net.form.OrgNetForm orgForm=com.fitech.net.adapter.StrutsOrgNetDelegate.selectOne(operator.getOrgId(),true);			
%>

<html:html locale="true">
	<head>
	<html:base />
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="../../js/func.js"></script>
		<SCRIPT language="javascript">
			<logic:present name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
		    	var curPage="<bean:write name='ApartPage' property='curPage'/>";
		    </logic:present>
		    <logic:notPresent name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
		    	var curPage="1";
		    </logic:notPresent>
		    
		    var SPLIT_SYMBOL_COMMA="<%=Config.SPLIT_SYMBOL_COMMA%>";
		    
		    /**
		     * �ύ��ѯ��֤
		     */
			function _submit(form){
				if(form.year.value==""){
					alert("�����뱨��ʱ��!");
					form.year.focus();
					return false;
				}
				if(form.term.value==""){
					alert("�����뱨��ʱ�䣡");
					form.term.focus();
					return false;
				}
				if(!isEmpty(form.year.value) && !CheckNum(form.year.value)){
					alert("��������ȷ�ı���Ĳ�ѯ���!\n");
					form.year.focus();
					return false;
				}
		  		if(!isEmpty(form.term.value) && !CheckNum(form.term.value)){
		  			alert("��������ȷ�ı���Ĳ�ѯ�·�!\n");
		  			form.term.focus();
		  			return false;
		  		}
		  		if(form.term.value <1 || form.term.value >12){
					alert("��������ȷ�ı���ʱ�䣡");
					form.term.focus();
					return false;
				}	  			  	
		  		return true;
			}
			
			function viewPdf(repInId){
				window.location="<%=request.getContextPath()%>/servlets/toExcelServlet?repInId=" + repInId; 
			}
			
			/**
			 * ����ȫ�������ļ�
			 */
			function _scsbwj(){
				var objFrm=document.forms['frm'];
				
		  	  	var qry="childRepId=" + objFrm.elements['childRepId'].value + 
		  	  			"&repName=" + objFrm.elements['repName'].value + 
		  	  			"&frOrFzType=" + objFrm.elements['frOrFzType'].value + 			  	  		
			  	  		"&repFreqId=" + objFrm.elements['repFreqId'].value + 
			  	  		"&curPage=" + curPage;
			  	if(objFrm.elements['year'].value != "")
			  		qry += "&year=" + objFrm.elements['year'].value;
			  	if(objFrm.elements['term'].value != "")
			  		qry += "&term=" + objFrm.elements['term'].value;
			  						
				var encrypt=<% if(orgForm.getOrg_type_id().equals(new Integer(1))){
		  	  	  		out.print("-1");}else out.print(Config.ENCRYPT);%>;
		  	  	
		  	  	if(confirm("��ȷ��Ҫ����ȫ�������ļ���?\n")==true){
					window.location="<%=request.getContextPath()%>/servlets/downLoadReportServlet?"+qry+"&encrypt="+encrypt;
				}
			}	
			
			/**
		  	  * ȫѡ����
		  	  */
			 function _selAll(){
		  	 	var formObj=document.getElementById("frmChk");
		  	  	var count=formObj.elements['countChk'].value;
		  	  	
		  	  	for(var i=1;i<=count;i++){
		  	  		try{
			  	  		if(formObj.elements['chkAll'].checked==false)
			  	  			eval("formObj.elements['chk" + i + "'].checked=false");
			  	  		else
			  	  			eval("formObj.elements['chk" + i + "'].checked=true");
		  	  		}catch(e){}
		  	  	}
		  	 }
		  	 		  	 
			/**
		  	  * �����������б��� 
		  	  * @return void
		  	  */
		     function _xzscsbwj(){
		  	  	var objForm=document.getElementById("frmChk");
		  	  	var count=objForm.elements['countChk'].value;
		  	  	var repInIds="";
		  	  	var obj=null;
		  	  	for(var i=1;i<=count;i++){
		  	  		try{
			  	  		obj=eval("objForm.elements['chk" + i + "']");
			  	  		if(obj.checked==true){
			  	  			repInIds+=(repInIds==""?"":SPLIT_SYMBOL_COMMA) + obj.value;
			  	  		}
		  	  		}catch(e){}
		  	  	}	
		  	  	if(repInIds==""){
		  	  		alert("��ѡ��Ҫ�����ı���!\n");
		  	  		return;
		  	  	}else{			
		  	  	  	var encrypt=<% if(orgForm.getOrg_type_id().equals(new Integer(1))){
		  	  	  		out.print("-1");}else out.print(Config.ENCRYPT);%>;
		  	  		window.location="<%=request.getContextPath()%>/servlets/downLoadReportServlet?repInIds=" + repInIds+"&encrypt="+encrypt;		
		  	  	}
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
	<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				��ǰλ�� >> ��Ϣ��ѯ >> ���ݵ���
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	
	
	<table cellspacing="0" cellpadding="4" border="0" width="98%" align="center">
		<html:form action="/viewReport" method="post" styleId="frm" onsubmit="return _submit(this)">
			<tr>
				<td>
					<fieldset id="fieldset">
						<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
							<tr>
								<td height="5"></td>
							</tr>
							<tr>
								<td height="25">&nbsp;
									�����ţ�
									<html:text property="childRepId" maxlength="6" size="6" styleClass="input-text"/>
								</td>
								<td height="25" align="left">
									�������ƣ�
									<html:text property="repName" size="25" styleClass="input-text" />
								</td>															
							</tr>
							<tr><td height="2"></td></tr>											
							<tr>
								<td height="25">&nbsp;
									����ʱ�䣺
									<html:text property="year" maxlength="4" size="4" styleClass="input-text" />
									��
									<html:text property="term" maxlength="2" size="2" styleClass="input-text" />
									��
								</td>
								<td height="25" align="left">
									ģ�����ͣ�
									<html:select property="frOrFzType" size="1">
										<html:option value="-999">--ȫ��--</html:option>
										<html:option value="1">����ģ��</html:option>
										<html:option value="2">��֧ģ��</html:option>
									</html:select>																													
								</td>
								<td hight="25" align="left">
									����Ƶ�ȣ�
									<html:select property="repFreqId">
										<html:option value="-999">--ȫ��--</html:option>
										<html:optionsCollection name="utilForm" property="repFreqs" label="label" value="value" />
									</html:select>
								</td>
								<td><html:submit styleClass="input-button" value=" �� ѯ " /></td>
							</tr>
							<tr>
								<td height="3"></td>
							</tr>
						</table>						
					</fieldset>
				</td>
			</tr>
		</html:form>
	</table>
	<logic:present name="form" scope="request">
		<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
			<tr>
				<td>
					<input type="button" value="��ѡ����" class="input-button" onclick="_xzscsbwj()">
					<input type="button" value="ȫ������" class="input-button" onclick="_scsbwj()">
				</td>				
			</tr>
		</table>
	</logic:present>
	<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
		<html:form action="/viewReport" method="post" styleId="frmChk">		
			<tr>
				<td>
					<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
						<TR class="titletab">
							<th width="5%" align="center" valign="middle">
								<input type="checkbox" name="chkAll" onclick="_selAll()">
							</th>
							<th width="10%" align="center" valign="middle">
								������
							</th>
							<th width="35%" align="center" valign="middle">
								��������
							</th>
							<th width="10%" align="center" valign="middle">
								�汾��
							</th>
							<th width="10%" align="center" valign="middle">
								����ھ�
							</th>
							<th width="10%" align="center" valign="middle">
								����
							</th>
							<th width="10%" align="center" valign="middle">
								Ƶ��
							</th>
							<th width="10%" align="center" valign="middle">
								����ʱ��
							</th>
						</tr>
						<%int i = 0;%>
						<logic:present name="form" scope="request">
							<logic:iterate id="viewReportIn" name="form">
								<%i++;%>
								<TR bgcolor="#FFFFFF">
									<TD align="center">
										<input type="checkbox" name="chk<%=i%>" value="<bean:write name="viewReportIn" property="repInId"/>">										
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="childRepId" />
									</TD>
									<TD align="center">
										<a href="javascript:viewPdf(<bean:write name='viewReportIn' property='repInId'/>)">
											<bean:write name="viewReportIn" property="repName" />
										</a>
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="versionId" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="dataRgTypeName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="currName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="actuFreqName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="year" />-<bean:write name="viewReportIn" property="term" />
									</TD>
								</TR>
							</logic:iterate>
						</logic:present>
						<input type="hidden" name="countChk" value="<%=i%>">
						<logic:notPresent name="form" scope="request">
							<tr align="center">
								<td bgcolor="#ffffff" colspan="8" align="left">
									���޷��������ļ�¼
								</td>
							</tr>
						</logic:notPresent>
					</table>
				</td>
			</tr>
		</html:form>
	</table>
	
	<table cellSpacing="0" cellPadding="0" width="98%" border="0">
		<TR>
			<TD>
				<jsp:include page="../../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../../viewReport.do" />
				</jsp:include>
			</TD>
		</TR>
	</table>
</body>
</html:html>
