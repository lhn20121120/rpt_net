<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.List,com.cbrc.smis.form.ReportInInfoForm,com.cbrc.smis.other.Expression"%>
<jsp:useBean id="utilFormOrg" scope="page" class="com.fitech.net.common.UtilForm" />
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />

<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../js/func.js"></script>
	<jsp:include page="../calendar.jsp" flush="true">
		<jsp:param name="path" value="../" />
	</jsp:include>

	<SCRIPT language="javascript">
		<logic:present name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="<bean:write name='ApartPage' property='curPage'/>";
	    </logic:present>
	    <logic:notPresent name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="1";
	    </logic:notPresent>
	   
		  	/**
		  	 * �ύ��֤
		  	 */
		  	 function _submit(form){
		  	 	if(!isEmpty(form.year.value) && !CheckNum(form.year.value)){
		  	 		alert("��������ȷ�ı���Ĳ�ѯ���!\n");
		  	 		form.year.select();
		  	 		form.year.focus();
		  	 		return false;
		  	 	}
		  	 	if(!isEmpty(form.term.value) && !CheckNum(form.term.value)){
		  	 		alert("��������ȷ�ı���Ĳ�ѯ�·�!\n");
		  	 		form.term.select();
		  	 		form.term.focus();
		  	 		return false;
		  	 	}
		  	 	return true;
		  	 }
		  	 function viewPdf(repInId){
				 window.location="<%=request.getContextPath()%>/servlets/toExcelServlet?repInId=" + repInId; 
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
				��ǰλ�� >> ͳ�Ʒ��� >> �쳣�仯�鿴
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center">
		<html:form action="/analysis/viewAnalyReportYCBH" method="post" styleId="frm" onsubmit="return _submit(this)">
			<tr>
				<td>
					<fieldset id="fieldset">
					<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
						<tr>
							<td height="5"></td>
						</tr>
						<tr>						
							<td>
								�������ƣ�
								<html:text property="repName" size="25" styleClass="input-text" />
							</td>

							<td height="25">&nbsp;
								����ʱ�䣺
								<html:text property="year" maxlength="4" size="6" styleClass="input-text" />
								��
								<html:text property="term" maxlength="2" size="4" styleClass="input-text" />
								��
							</td>
							<td>
								<html:submit styleClass="input-button" value=" �� ѯ " />
							</td>
						</tr>
					</table>
					</fieldset>
				</td>
			</tr>
		</html:form>
	</table><br/>

						
	<table border="0" cellpadding="6" cellspacing="0" width="98%" align="center">
		<html:form action="/analysis/viewAnalyReportYCBH" method="post">
			<tr>
				<td>
				<TABLE cellSpacing="1" cellPadding="6" width="100%" border="0" class="tbcolor">
					<tr class="titletab">
						<th colspan="6" align="center" id="list">
							<strong>
								�쳣�仯��Ϣ
							</strong>
						</th>
					</tr>
					<TR class="middle">
						<TD class="tableHeader" width="36%">
							<b>
								��������
							</b>
						</TD>
						<TD class="tableHeader" width="9%">
							<b>
								���-����
							</b>
						</TD>
						<TD class="tableHeader" width="7%">
							<b>
								��Ԫ��
							</b>
						</TD>
						<TD class="tableHeader" width="10%">
							<b>
								����
							</b>
						</TD>
						<TD class="tableHeader" width="10%">
							<b>
								����������/�½�
							</b>
						</TD>
						
						<TD class="tableHeader" width="10%">
							<b>
								������ͬ������/�½�
							</b>
						</TD>
					</TR>
					<logic:present name="Records" scope="request">
						<logic:iterate id="item" name="Records">
						<TR bgcolor="#FFFFFF">
						
							<TD align="center">
								<a href="javascript:viewPdf(<bean:write name='item' property='repInId'/>)">
									<bean:write name="item" property="repName"/>
								</a>
							</TD>
							
							<td>
								<bean:write name="item" property="year"/>-<bean:write name="item" property="term"/>
							</td>
							<td align="center"><bean:write name="item" property="cellName"/></td>
							<td align="center"><bean:write name="item" property="reportValue"/></td>
							<td align="center">
							
							<logic:notEmpty name="item" property="thanPrevRise">
								<font color="red">����<bean:write name="item" property="thanPrevRise"/>%</font>
							</logic:notEmpty>
							<logic:notEmpty name="item" property="thanPrevFall">
								<font color="red">�½�<bean:write name="item" property="thanPrevFall"/>%</font>
							</logic:notEmpty>

							</td>
							
							<td align="center">
							<logic:notEmpty name="item" property="thanSameRise">
								<font color="red">����<bean:write name="item" property="thanSameRise"/>%</font>
							</logic:notEmpty>
							<logic:notEmpty name="item" property="thanSameFall">
								<font color="red">�½�<bean:write name="item" property="thanSameFall"/>%
							</logic:notEmpty>
							
							
							
							
							</td>
							
						</tr>	
						</logic:iterate>
					</logic:present>
					<logic:notPresent name="Records" scope="request">
						<tr bgcolor="#FFFFFF">
							<td colspan="6">
								�����쳣�仯��Ϣ
							</td>
						</tr>
					</logic:notPresent>
				</TABLE>
				</td>
			</tr>
		</html:form>
	</table>
	<table cellSpacing="0" cellPadding="0" width="98%" border="0">
		<TR>
			<TD>
				<jsp:include page="../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../analysis/viewAnalyReportYCBH.do" />
				</jsp:include>
			</TD>
		</TR>
	</table>
</body>
</html:html>
