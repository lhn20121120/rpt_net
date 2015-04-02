<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.List,com.cbrc.smis.form.ReportInInfoForm,com.cbrc.smis.other.Expression"%>
<jsp:useBean id="utilFormOrg" scope="page" class="com.fitech.net.common.UtilForm" />
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<%String selectedFlag = request.getAttribute("SelectedFlag") == null ? "5"
					: (String) request.getAttribute("SelectedFlag");
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
	    /**
		     * ����У���ʶ
		     */
		     var FLAG_BL="<%=Expression.FLAG_BL%>";
		     
		    /**
		     * ���У���ʶ
		     */
		     var FLAG_BJ="<%=Expression.FLAG_BJ%>";
		     
		    /**
		     * �鿴����У����ϸ
		     */
		     function _view_bnjy(repInId){
		     	window.open("<%=request.getContextPath()%>/report/report_check/viewJYDetail.do?" + 
		     		"flag=" + FLAG_BL + "&repInId=" + repInId);
		     }
		     
		    /**
		     * �鿴���У����ϸ
		     */ 
		     function _view_bjjy(repInId){
		     	window.open("<%=request.getContextPath()%>/report/report_check/viewJYDetail.do?" + 
		     		"flag=" + FLAG_BJ + "&repInId=" + repInId);
		     }
		     
		    /**
		     * �쳣�仯��ϸ
		     */
		     function _view_ycbh(repInId){
		     	window.open("<%=request.getContextPath()%>/report/report_check/viewYCBHDetail.do?" + 
		     		"repInId=" + repInId);
		     }
		  	/**
		  	 * ����ύ��֤
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
				��ǰλ�� >> ������� >> ����鿴
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center">
		<html:form action="/reportSearch/searchRep" method="post" styleId="frm" onsubmit="return _submit(this)">
			<tr>
				<td>
					<fieldset id="fieldset">
					<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
						<tr>
							<td height="5"></td>
						</tr>
						<tr>
							<td height="25">&nbsp;
								���ͻ�����
								<logic:notEmpty name="orgLst">
										<html:select property="orgId">
											<option value="">
												ȫ��
											</option>
											<html:options collection="orgLst" property="orgId" labelProperty="orgName" />
										</html:select>
									</logic:notEmpty>
							</td>
							<td>
								�������ƣ�
								<html:text property="repName" size="25" styleClass="input-text" />
							</td>

							<td>
							</td>
							<td>
								<html:submit styleClass="input-button" value=" �� ѯ " />
							</td>
						</tr>
					</table>
					<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
						<tr>
							<td height="25">&nbsp;
								����ʱ�䣺
								<html:text property="year" maxlength="4" size="6" styleClass="input-text" />
								��
								<html:text property="term" maxlength="2" size="4" styleClass="input-text" />
								��
							</td>
							<td align="center">
								<input type="radio" name="allFlags" value="0" />
								δ���
								<strong>
									<font face="����" color="#000000" size="2">
										��
									</font>
								</strong>
								&nbsp;
								<input type="radio" name="allFlags" value="-1" />
								���δͨ��
								<strong>
									<font face="����" color="#FF0000" size="2">
										��
									</font>
								</strong>
								&nbsp;
								<input type="radio" name="allFlags" value="1" />
								���ͨ��
								<strong>
									<font size="2" color="#33CC33">
										��
									</font>
								</strong>
								&nbsp;
								<input type="radio" name="allFlags" value="4" />
								�쳣��¼
								<font color="#FF0000" style="font-size: 14pt">
									<strong>
										?
									</strong>
								</font>
								&nbsp;
								<input type="radio" name="allFlags" value="5" />
								ȫ����¼
							</td>
						</tr>
						<tr>
							<td height="3"></td>
						</tr>
					</table>
					</fieldset>
				</td>
			</tr>
		</html:form>
	</table><br/>
	<script language="javascript">
		var _selFlag="<%=selectedFlag%>";
		var _selIndex=4;
		if(_selFlag=="0") _selIndex=0;
		if(_selFlag=="-1") _selIndex=1;
		if(_selFlag=="1") _selIndex=2;
		if(_selFlag=="4") _selIndex=3;
		
		document.forms['frm'].elements['allFlags'][_selIndex].checked=true;
	</script>

	<table border="0" cellpadding="0" cellspacing="0" width="98%" align="center">
		<html:form action="/reportSearch/viewReport" method="post">
			<tr>
				<td>
					<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
						<TR class="titletab">
							<th width="8%" align="center" valign="middle">
								������
							</th>
							<th width="29%" align="center" valign="middle">
								��������
							</th>
							<th width="8%" align="center" valign="middle">
								�汾��
							</th>
							<th width="12%" align="center" valign="middle">
								����ھ�
							</th>
							<th width="10%" align="center" valign="middle">
								����
							</th>
							<th width="5%" align="center" valign="middle">
								Ƶ��
							</th>
							<th width="9%" align="center" valign="middle">
								����ʱ��
							</th>
							<th width="15%" align="center" valign="middle">
								�ϱ�����
							</th>
							<Th width="5%" align="center" valign="middle">
								״̬
							</Th>
						</TR>

						<logic:present name="Records" scope="request">
							<logic:iterate id="viewReportInInfo" name="Records">
								<TR bgcolor="#FFFFFF">
									<TD align="center">
										<bean:write name="viewReportInInfo" property="childRepId"/>
									</TD>
									<TD align="center">
										<input type="hidden" name="repInIdArray" value="<bean:write name="viewReportInInfo" property="repInId"/>" />
										<a href="javascript:viewPdf(<bean:write name='viewReportInInfo' property='repInId'/>)">
											<bean:write name="viewReportInInfo" property="repName" />
										</a>
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="versionId" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="dataRgTypeName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="currName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="actuFreqName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="year" />-<bean:write name="viewReportInInfo" property="term" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="orgName" />
									</TD>
									<TD align="center">
										<logic:equal name="viewReportInInfo" property="checkFlag" value="0"><font face="����" color="#000000" size="2">��</font></logic:equal>
										<logic:equal name="viewReportInInfo" property="checkFlag" value="1"><strong><font size="2" color="#33CC33">��</font></strong></logic:equal>
										<logic:equal name="viewReportInInfo" property="checkFlag" value="-1"><strong><font face="����" color="#FF0000" size="2">��</font></strong></logic:equal>
										<logic:equal name="viewReportInInfo" property="checkFlag" value="4"><strong><font face="����" color="#FF0000" size="2">��</font></strong></logic:equal>
							
									</TD>
								</TR>
							</logic:iterate>
						</logic:present>
						<logic:notPresent name="Records" scope="request">
							<tr align="left">
								<td bgcolor="#ffffff" colspan="9">
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
					<jsp:param name="url" value="../../reportSearch/searchRep.do" />
				</jsp:include>
			</TD>
		</TR>
	</table>
</body>
</html:html>
