<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
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
		<script language="javascript">
			
		</script>
	</head>
	<script language="javascript">
		<logic:present name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="<bean:write name='ApartPage' property='curPage'/>";
	    </logic:present>
	    <logic:notPresent name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="1";
	    </logic:notPresent>	
	    
	    var SPLIT_SYMBOL_COMMA="<%=Config.SPLIT_SYMBOL_COMMA%>";
	    
		function viewPdf(repInId){
			window.location="<%=request.getContextPath()%>/servlets/toExcelServlet?repInId=" + repInId; 
		}
		function _view_zxxg(repInId){
			var term = document.getElementById("term");
			var year = document.getElementById("year");
			var repName = document.getElementById("repName");
		    window.location="<%=request.getContextPath()%>/reportEdit.do?" + "repInId=" + repInId + "&curPage=" 
		    		+ curPage + "&term=" + term.value + "&year=" + year.value + "&repName=" + repName.value;
		}	
		function _view_sjbs(repInId){
		    window.location="<%=request.getContextPath()%>/upLoadOnLineReport.do?" + "repInId=" + repInId;
		}
		function _view_jyxx(repInId){
		    window.open("<%=request.getContextPath()%>/report/runDataJY.do?" + "repInId=" + repInId);
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
		 * �����ϱ����� 
		 * @return void
		 */
		function _sjbs(){
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
		  		alert("��ѡ��Ҫ�ϱ��ı���!\n");
		  		return;
		  	}else{	
		  		if(confirm("��ȷ��Ҫ�ϱ�������"))	  	  	  	
		  			window.location="<%=request.getContextPath()%>/groupUpLoadReport.do?repInIds=" + repInIds;		
		  	}
		}
		
		/**
		 * �ϱ�ȫ������
		 */
		function _group_sjbs(){
			var repName = document.getElementById("repName");
			var year = document.getElementById("year");
			var term = document.getElementById("term");
			var qry = "year=" + year.value + "&term=" + term.value + "&repName=" + repName.value;
			if(confirm("��ȷ��Ҫ�ϱ�����������"))
				window.location="<%=request.getContextPath()%>/groupUpLoadReport.do?" + qry;
		}
	</script>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<body style="TEXT-ALIGN: center">
			<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				��ǰλ�� >> ���ݱ��� >> �ϱ�����
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="4" border="0" width="98%" align="center">
		<html:form action="/viewOnLineSJBS" method="post" styleId="frm">
			<tr>
				<td>
					<fieldset id="fieldset">
						<table cellspacing="0" cellpadding="4" border="0" width="100%" align="left">
							<tr>
								<td height="3"></td>
							</tr>
							<tr>
								<td>
									�������ƣ�
									<html:text property="repName" size="25" styleClass="input-text" />
								</td>
								<td>
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
	</table>
	<logic:present name="records" scope="request">
		<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
			<tr>
				<td>
					<input type="button" value="�ϱ�����" class="input-button" onclick="_sjbs()">
					<input type="button" value="ȫ���ϱ�" class="input-button" onclick="_group_sjbs()">
				</td>
			</tr>
		</table>
	</logic:present>
	<TABLE cellSpacing="0" width="98%" border="0" align="center" cellpadding="4">
		<html:form action="/viewOnLineSJBS" method="post" styleId="frmChk">
			<TR>
				<TD>
					<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0"  class="tbcolor">
						<TR class="titletab">
							<th width="5%" align="center" valign="middle">
								<input type="checkbox" name="chkAll" onclick="_selAll()">
							</th>
							<th width="10%" align="center" valign="middle">������</th>
							<th width="28%" align="center" valign="middle">��������</th>
							<th width="10%" align="center" valign="middle">�汾��</th>
							<th width="10%" align="center" valign="middle">����ھ�</th>
							<th width="10%" align="center" valign="middle">����</th>
							<th width="7%" align="center" valign="middle">Ƶ��</th>
							<th width="10%" align="center" valign="middle">����ʱ��</th>
							<Th width="10%" align="center" valign="middle">����</Th>
						</tr>
						<%int i = 0;%>
						<logic:present name="records" scope="request">
							<logic:iterate id="viewReportIn" name="records">
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
										<bean:write name="viewReportIn" property="year"/>-<bean:write name="viewReportIn" property="term"/>
									</TD>
									<TD align="center">
										<logic:equal name="viewReportIn" property="checkFlag" value="0">
											<font color="blue">�ѱ�</font>
										</logic:equal>
										<logic:equal name="viewReportIn" property="checkFlag" value="1">
											<font color="blue">�ϸ�</font>
										</logic:equal>
										<logic:equal name="viewReportIn" property="checkFlag" value="-1">
											<!-- <font color="red">���ϸ�</font>&nbsp;--><img src="../image/sjsb.gif" border="0" title="���ݱ���" style="cursor:hand" onclick="_view_sjbs(<bean:write name='viewReportIn' property='repInId'/>)">
											<a href ="javascript:_view_jyxx(<bean:write name='viewReportIn' property='repInId'/>)">�鿴У��</a>									
										</logic:equal>																					
										<logic:equal name="viewReportIn" property="checkFlag" value="3">
											<!-- <font color="blue">δ��</font>&nbsp;--><img src="../image/sjsb.gif" border="0" title="���ݱ���" style="cursor:hand" onclick="_view_sjbs(<bean:write name='viewReportIn' property='repInId'/>)">
											<a href ="javascript:_view_jyxx(<bean:write name='viewReportIn' property='repInId'/>)">�鿴У��</a>									
										</logic:equal>
									</TD>
								</TR>
							</logic:iterate>
						</logic:present>
						<input type="hidden" name="countChk" value="<%=i%>">
						<logic:notPresent name="records" scope="request">
							<tr align="center">
								<td bgcolor="#ffffff" colspan="9" align="left">
									���޷��������ļ�¼
								</td>
							</tr>
						</logic:notPresent>
					</TABLE>
				</TD>
			</TR>
		</html:form>
	</TABLE>	
	<table cellSpacing="0" cellPadding="0" width="98%" border="0">
		<TR>
			<TD>
				<jsp:include page="../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../viewOnLineSJBS.do" />
				</jsp:include>
			</TD>
		</TR>
	</table>			
	</body>
</html:html>
