<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	Operator operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepReportPodedom = operator != null ? operator.getChildRepReportPopedom() : "";
	String orgId = operator != null ? operator.getOrgId() : "";
	String selectOrgId = request.getAttribute("orgId") != null ? request.getAttribute("orgId").toString() : orgId;
	String curpage =(String) request.getAttribute("curPage") != null ? request.getAttribute("curPage").toString() : "1";
	Integer systemSchemaFlag = Config.SYSTEM_SCHEMA_FLAG;
	String backQry = "";
	if (session.getAttribute("backQry") != null){
		backQry = (String) session.getAttribute("backQry");
	}
	
%>
<jsp:useBean id="utilSubOrgForm" scope="page" class="com.fitech.net.form.UtilSubOrgForm" /> 
<jsp:setProperty property="childRepReportPodedom" name="utilSubOrgForm" value="<%=childRepReportPodedom%>" />
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript" src="<%=Config.WEBROOTULR%>/js/prototype-1.4.0.js"></script>
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>

	<SCRIPT language="javascript">  
	    var EXT_EXCEL="<%=configBean.EXT_EXCEL%>";	    	
	    var EXT_ZIP="<%=configBean.EXT_ZIP%>";
	    var requestParam = "<logic:present name='RequestParam'><bean:write name='RequestParam'/></logic:present>";	  
	    
	    var reportInId = null;
		  	function _submit(form){
				if(form.year.value==""){
					alert("�����뱨��ʱ��!");
					form.year.focus();
					return false;
				}
				if(form.setDate.value==""){
					alert("�����뱨��ʱ�䣡");
					form.setDate.focus();
					return false;
				}
				if(isNaN(form.year.value)){ 
				   alert("��������ȷ�ı���ʱ�䣡"); 
				   form.year.focus(); 
				   return false; 
				}
				if(isNaN(form.setDate.value)){ 
				   alert("��������ȷ�ı���ʱ�䣡"); 
				   form.setDate.focus(); 
				   return false; 
				}
				if(form.setDate.value <1 || form.setDate.value >12){
					alert("��������ȷ�ı���ʱ�䣡");
					form.setDate.focus();
					return false;
				}
			}
			function why(why){
		  	 	window.open ("<%=request.getContextPath()%>/template/data_report/tip.jsp?reason="+why,why, "height=250, width=250, top=0,left=0,toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");		  	 	  	 		  	 	
		  	}
			function _sjbs(){
		     	window.location="<%=request.getContextPath()%>/template/intoTem.do?curPage=" + curPage + "&orgId=<%=selectOrgId%>";
		    }
		    function _SJJY(){
		     	window.location="<%=request.getContextPath()%>/report/dataLDJY.do?curPage=" + curPage + "&orgId=<%=selectOrgId%>";
		    }	
		    function _submit2(form){			
				if(form.formFile.value==""){
					alert("�ϴ��ļ�����Ϊ��");
					form.formFile.focus();
					return false;
				}
				if(getExt(form.formFile.value)!=EXT_EXCEL){
			 		alert("ѡ��ı����ļ�������Excel�ļ�!");
			 		form.formFile.focus();
			 		return false;
			 	}
			    var childRepId ="<bean:write name='aditing' property='childRepId'/>";
			    var version_Id ="<bean:write name='aditing' property='versionId'/>";
			    var year ="<bean:write name='aditing' property='year'/>";
			    var term ="<bean:write name='aditing' property='term'/>";
			    var curId ="<bean:write name='aditing' property='curId'/>";
			    var orgId ="<bean:write name='aditing' property='orgId'/>";
			    var dataRangeId ="<bean:write name='aditing' property='dataRangeId'/>";
				var actuFreqID ="<bean:write name='aditing' property='actuFreqID'/>";
				var version_kj = document.getElementById("versionId");	
				var curPage = document.getElementById("curPage");	
				version_kj.value = childRepId + "_" + version_Id + "_" + year + "_" + term+ "_" + curId + "_" + orgId + "_" + dataRangeId + "_" + actuFreqID+"_"+requestParam;
		
				return true;
				
			}
			
				//У�鱨��
		function validateReport(repInId)
		{
			 try
		 	 {
			  	reportInId=repInId;
			  	var validateURL = "<%=request.getContextPath()%>/report/validateOnLineReport.do?repInId="+repInId; 
			    var param = "radom="+Math.random();
			   	new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:validateReportHandler,onFailure: reportError});
		   	}
		   	catch(e)
		   	{
		   		alert('ϵͳæ�����Ժ�����...��');
		   	}
		}
		
		//У��Handler
		function validateReportHandler(request)
		{
			try
			{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
				if(result == 'false')  
				  {
				     if(confirm('У��ʧ�ܣ��Ƿ���Ҫ�鿴У����Ϣ?'))
				        window.open("<%=request.getContextPath()%>/report/viewDataJYInfo.do?" + "repInId=" + reportInId,'У����','scrollbars=yes,height=600,width=500,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');

				  }	
				  else if(result == 'true')
				  {
					 alert('У��ͨ����');	
				  }
			}
			catch(e)
			{}
	    }
	    
	    //ʧ�ܴ���
	    function reportError(request)
	    {
	        alert('ϵͳæ�����Ժ�����...��');
	    }
	    
	    //������ 
		function sendReport(repInId)
		{
			if(confirm('ȷ�����͸ñ���')){
			 	try
			 	 {
				  	reportInId=repInId;
				  	var upReportURL ="<%=request.getContextPath()%>/upLoadOnLineReport.do?" +requestParam+"&repInId=" + repInId ;
				    var param = "radom="+Math.random();
				   	new Ajax.Request(upReportURL,{method: 'post',parameters:param,onComplete:upReportHandler,onFailure: reportError});
			   	}
			   	catch(e)
			   	{
			   		alert('ϵͳæ�����Ժ�����...��');
			   	}
			}
		} 
		//����Handler
		function upReportHandler(request)
		{
			try
			{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
					
				  if(result == 'true')
				  {
					 alert('���ͳɹ���');	
					 if(backQuery!=""){
						window.location.href= "<%=request.getContextPath()%>/viewDataReport.do?"+backQuery;
					 }else{
						window.location.href= "<%=request.getContextPath()%>/viewDataReport.do?"+requestParam;
					 }
				  }
				  else  if(result == 'BJ_VALIDATE_NOTPASS')
				  {
				     alert("���У�鲻ͨ���������ϱ��ñ���");
				  }else if( result == 'BN_VALIDATE_NOTPASS'){
				  
				 	 alert("����У�鲻ͨ���������ϱ��ñ���");
				  }
				  else{
				 	 alert('ϵͳæ�����Ժ�����...��');
				  
				  }
			}
			catch(e)
			{}
	    }
		 function viewPdf(repInId){
				 window.location="<%=request.getContextPath()%>/servlets/toExcelServlet?repInId=" + repInId; 
			 }
			 
		var backQuery = "<%=backQry%>";
			 
			//����
		function back()
		{
			if(backQuery!=""){
				window.location.href= "<%=request.getContextPath()%>/viewDataReport.do?"+backQuery;
			}else{
				window.location.href= "<%=request.getContextPath()%>/viewDataReport.do?"+requestParam;
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
	<table border="0" width="90%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				��ǰλ�� &gt;&gt; �����ϱ� &gt;&gt; ���������ϱ�
			</td>
		</tr>
	</table>


	<table cellspacing="0" cellpadding="0" border="0" width="90%" align="center">
		<table cellSpacing="1" cellPadding="4" border="0" width="90%" class="tbcolor" align="center">
			<tr class="titletab">
				<th align="center" colspan="4">
					���ݱ���
				</th>
			</tr>

			<logic:present name="aditing" scope="request">
				<tr bgcolor="#FFFFFF" align="center">
					<td>
						��������
						<bean:write name="aditing" property="repName" />
					</td>
				</tr>
			</logic:present>

			<tr bgcolor="#FFFFFF">
				<td width="20%">
					<html:form method="post" action="/template/uploadFile" enctype="multipart/form-data" onsubmit="return _submit2(this)">
						<div align="center">
							<html:file property="formFile" size="80" styleClass="input-button" />
							<html:hidden property="versionId" />
							<html:hidden property="curPage" value="<%=curpage %>"/>
							<html:submit styleClass="input-button" value="����" />
						</div>
					</html:form>
				</td>
			</tr>
		</table>
	</table>
	<p />
		<br />
	<table border="0" cellpadding="0" cellspacing="0" width="90%" align="center">
		<tr>
			<td>
				<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
					<TR class="titletab">
						<th width="8%" align="center" valign="middle">
							���
						</th>
						<th width="30%" align="center" valign="middle">
							��������
						</th>
						<th width="10%" align="center" valign="middle">
							�汾��
						</th>
						<th width="8%" align="center" valign="middle">
							����
						</th>
						<th width="15%" align="center" valign="middle">
							����ھ�
						</th>
						<th width="5%" align="center" valign="middle">
							Ƶ��
						</th>
						<th width="8%" align="center" valign="middle">
							����ʱ��
						</th>
						<th width="10%" align="center" valign="middle">
							����
						</th>
						<Th width="10%" align="center" valign="middle">
							״̬
						</Th>
					</TR>

					<logic:present name="aditing" scope="request">
						<TR bgcolor="#FFFFFF">

							<TD align="center">
								<bean:write name="aditing" property="childRepId" />
							</TD>
							<logic:present name="notshow" scope="request">
								<TD align="center">
									<a href="javascript:viewPdf('<bean:write name="aditing" property="repInId"/>')"><bean:write name="aditing" property="repName" />
									</a>
								</TD>
							</logic:present>
							<logic:notPresent name="notshow" scope="request">
								<TD align="center">
									<bean:write name="aditing" property="repName" />
								</TD>
							</logic:notPresent>
							<TD align="center">
								<bean:write name="aditing" property="versionId" />
							</TD>
							<TD align="center">
								<bean:write name="aditing" property="currName" />
							</TD>
							<TD align="center">
								<bean:write name="aditing" property="dataRgTypeName" />
							</TD>
							<TD align="center">
								<bean:write name="aditing" property="actuFreqName" />
							</TD>
							<TD align="center">
								<bean:write name="aditing" property="year" />
								-
								<bean:write name="aditing" property="term" />
							</TD>
							<TD align="center">
								<bean:write name="aditing" property="orgName" />
							</TD>
					
							<%
								String flag = (String) request.getAttribute("flag");
								if (flag != null && flag.trim().equals("true"))
								{
							%>
								<TD align="center">
									δ����
								</TD>
							<%
								}
								else
								{
							%>
								<TD align="center">
									<logic:equal name="aditing" property="checkFlag" value="0">
										<span class="txt-main" style="color:#FF3300">����ʧ��</span>
									</logic:equal>
									<logic:equal name="aditing" property="checkFlag" value="3">����ɹ�</logic:equal>
								</TD>
							<%
							}
							%>
						</TR>

					</logic:present>
					<logic:notPresent name="aditing" scope="request">
						<tr align="left">
							<td bgcolor="#ffffff" colspan="8">
								���޷��������ļ�¼
							</td>
						</tr>
					</logic:notPresent>
				</table>
			</td>
		</tr>

	</table>
	<table border="0" cellpadding="0" cellspacing="0" width="90%" align="center">
		<TR>
			<TD>
				&nbsp;
			</TD>
		</TR>
		<TR>
			<TD>
			
				<logic:present name="notshow" scope="request">
					<tr>
						<td colspan="4">
							<DIV align="right">
								<input class="input-button" type="reset" value="У  ��" onclick="validateReport('<bean:write name="aditing" property="repInId"/>')" />
								&nbsp;
								<input class="input-button" type="reset" value="��  ��" onclick="sendReport('<bean:write name="aditing" property="repInId"/>')" />
								&nbsp;
								<INPUT class="input-button" id="back" type="button" value=" ��  �� " name="butBack" onclick="back()">
							</DIV>
						</td>
					</tr>
				</logic:present>
				<logic:notPresent name="notshow" scope="request">
					<tr>
						<td colspan="4">
							<DIV align="right">
							<%if(systemSchemaFlag ==0){%>
								<INPUT class="input-button" id="back" type="button" value=" ��  �� " name="butBack" onclick="back()">
							<%} %>
							</DIV>
						</td>
					</tr>
				</logic:notPresent>

		</TD>
		</TR>
	</table>


</body>
<script>
	//block
	//   document.getElementById("isShowZT").style.display="none";
	</script>
</html:html>
