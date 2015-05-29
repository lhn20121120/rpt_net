<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.security.Operator" %>
<%@ page import="com.cbrc.smis.common.Config" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);	
	String orgId = operator != null ? operator.getOrgId() : "";
	String selectOrgId = request.getAttribute("orgId") != null ? request.getAttribute("orgId").toString() : orgId;
	String year = request.getParameter("year") != null ? request.getParameter("year").toString() : request.getAttribute("year").toString();
	String term = request.getParameter("term") != null ? request.getParameter("term").toString() : request.getAttribute("term").toString();
	String curPage = request.getParameter("curPage") != null ? request.getParameter("curPage").toString():"1";
%>
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
		<script language="javascript" src="<%=request.getContextPath() %>/js/prototype-1.4.0.js"></script>
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>

	<SCRIPT language="javascript">	
	   var BLJY = 'BN_VALIDATE_NOTPASS';
	   var BJJY = 'BJ_VALIDATE_NOTPASS';	 
	    var EXT_EXCEL="<%=configBean.EXT_EXCEL%>";	    	
	    var EXT_ZIP="<%=configBean.EXT_ZIP%>";
	    var requestParam = "year=<%=year%>&term=<%=term%>&curPage=<%=curPage%>";  	
	   	var reportInId = null;	   	
			
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
			if(isNaN(form.year.value)){ 
			   alert("��������ȷ�ı���ʱ�䣡"); 
			   form.year.focus(); 
			   return false; 
			}
			if(isNaN(form.term.value)){ 
			   alert("��������ȷ�ı���ʱ�䣡"); 
			   form.term.focus(); 
			   return false; 
			}
			if(form.term.value <1 || form.term.value >12){
				alert("��������ȷ�ı���ʱ�䣡");
				form.term.focus();
				return false;
			}
		}
		
		function _submit2(form){			
			if(form.formFile.value==""){
				alert("�ϴ��ļ�����Ϊ��");
				form.formFile.focus();
				return false;
			}
			if(getExt(form.formFile.value)!=EXT_ZIP){
			 	alert("ѡ��ı����ļ�������Zip��!");
			 	form.formFile.focus();
			 	return false;
			}			
			var year_kj = document.getElementById("year");	
			var term_kj = document.getElementById("term");
			var version_kj = document.getElementById("versionId");	
			version_kj.value = year_kj.value + "_" + term_kj.value + "_<%=selectOrgId%>_" + <%=curPage%>;
			prodress1.style.display = "none" ;
		    prodress.style.display = "" ;
			return true;
		}
						
		//У�鱨��
		function validateReport(){
			var reportInIds = "";
			var countRep = document.getElementById("countRep").value;			
			var obj = null;
			for(var i=1;i<=countRep;i++){
		  	  	obj = document.getElementById("repId"+i);			  	
			  	reportInIds+=(reportInIds==""?"":",") + obj.value;
		  	}
		  	try{
				var validateURL = "<%=request.getContextPath()%>/report/validateOnLineReport.do?repInIds="+reportInIds; 
				var param = "radom="+Math.random();
				new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:validateReportHandler,onFailure: reportError});
		   	}catch(e){
		   		alert('ϵͳæ�����Ժ�����...��');
		   		return;
			}	  	
		}
		
	    //У��Handler
		function validateReportHandler(request){
			try{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
				if(result == 'true'){
				  	alert('У��ͨ����');				     
				}else{
					 if(confirm('У��ʧ�ܣ��Ƿ���Ҫ�鿴У����Ϣ?'))
				        window.open("<%=request.getContextPath()%>/report/viewMoreDataJYInfo.do?" + "repInIds=" + result,'У����','scrollbars=yes,height=600,width=500,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
				}
			}catch(e){}
	    }
	    	    
	    //ʧ�ܴ���
	    function reportError(request){
	        alert('ϵͳæ�����Ժ�����...��');
	    }
	    
	    //������ 
		function sendReport(){
			if(confirm('ȷ�����͸ñ���')){
				var reportInIds = "";
				var countRep = document.getElementById("countRep").value;			
				var obj = null;
				for(var i=1;i<=countRep;i++){
			  	  	obj = document.getElementById("repId"+i);			  	
				  	reportInIds+=(reportInIds==""?"":",") + obj.value;
			  	}
			 	try{				  	
				  	var upReportURL ="<%=request.getContextPath()%>/upLoadOnLineMoreReport.do?&repInIds=" + reportInIds ;
				    var param = "radom="+Math.random();
				   	new Ajax.Request(upReportURL,{method: 'post',parameters:param,onComplete:upReportHandler,onFailure: reportError});
			   	}catch(e){
			   		alert('ϵͳæ�����Ժ�����...��');
			   	}
			}
		}
		
		//����Handler
		function upReportHandler(request){
			try{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;
				alert(result);
				if(result == 'true'){
					alert('���ͳɹ���');	
					window.location="<%=request.getContextPath()%>/viewDataReport.do?"+requestParam; 
				}else{
				   var str = '';
				    if(result.indexOf(BLJY)>-1){
				       str+='����';
				    }
				    if(result.indexOf(BJJY)>-1){
				       str+='���';
				    }
					alert(str+"У��δͨ���������ϱ���");					
				}
			}catch(e){}
		}
		//����
		function back()
{
			window.location.href= "<%=request.getContextPath()%>/viewDataReport.do?"+requestParam;
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
	
	<label id="prodress" style="display:none">
			<span class="txt-main" style="color:#FF3300">�����������룬���Ժ�......</span>
		</label>
  <label   id="prodress1" >
	<table border="0" width="90%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				��ǰλ�� >> ���ݱ��� >> �����ϱ� >> ��������
			</td>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="0" border="0" width="90%" align="center">
		<html:form action="/viewDataReport.do" method="post" styleId="frm" onsubmit="return _submit(this)">
			<tr>
				<td>
					<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
						<tr>							
							<td>
								����ʱ�䣺
								<html:text property="year" maxlength="4" size="7" styleClass="input-text" value="<%=year%>"/>
								��
								<html:text property="term" maxlength="2" size="5" styleClass="input-text" value="<%=term%>"/>
								��
							</td>																	
							<td>
								<html:submit styleClass="input-button" value=" �� ѯ " />
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="4"></td>
			</tr>
		</html:form>
		<br/>
		<table cellSpacing="1" cellPadding="4" border="0" width="90%" class="tbcolor" align="center">
			<tr class="titletab"><th align="center" colspan="4">���ݱ���</th></tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%">
					<html:form method="post" action="/template/uploadMoreFile" enctype="multipart/form-data" onsubmit="return _submit2(this)">
						<div align="center">
							<html:file  property="formFile" size="80" styleClass="input-button"/>
							<html:hidden property="versionId"/>
							<html:submit styleClass="input-button" value="����"/>
                        </div>
                    </html:form>
                </td>
            </tr>
		</table>
	</table>
	<p/><br/>
	<table border="0" cellpadding="0" cellspacing="0" width="90%" align="center">
		<tr>
			<td>
				<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
					<TR class="titletab">
						<th width="10%" align="center" valign="middle">���</th>
						<th width="33%" align="center" valign="middle">��������</th>
						<th width="10%" align="center" valign="middle">�汾��</th>
						<th width="10%" align="center" valign="middle">����</th>
						<th width="15%" align="center" valign="middle">����ھ�</th>
						<th width="5%" align="center" valign="middle">Ƶ��</th>
						<th width="9%" align="center" valign="middle">����ʱ��</th>							
						<Th width="11%" align="center" valign="middle">״̬</Th>
					</TR>
					<%
						int i = 0;
					%>
					<logic:present name="Records" scope="request">
						<logic:iterate id="viewReportIn" name="Records">
							<%i++;%>
							<TR bgcolor="#FFFFFF">					
								<TD align="center"><bean:write name="viewReportIn" property="childRepId" /></TD>
								<TD align="center"><bean:write name="viewReportIn" property="repName" /></TD>
								<TD align="center"><bean:write name="viewReportIn" property="versionId" /></TD>
								<TD align="center"><bean:write name="viewReportIn" property="currName" /></TD>
								<TD align="center"><bean:write name="viewReportIn" property="dataRgTypeName" /></TD>
								<TD align="center"><bean:write name="viewReportIn" property="actuFreqName" /></TD>
								<TD align="center"><bean:write name="viewReportIn" property="year" />-<bean:write name="viewReportIn" property="term" />
								</TD>									
								<TD align="center">
									<logic:equal name="viewReportIn" property="checkFlag" value="0">
										<span class="txt-main" style="color:#FF3300">����ʧ��</span>
									</logic:equal>
									<logic:equal name="viewReportIn" property="checkFlag" value="3">����ɹ�</logic:equal>
								</TD>
								<input type="hidden" name="repId<%=i%>" value='<bean:write name="viewReportIn" property="repInId"/>'>										
							</TR>
						</logic:iterate>
					</logic:present>
					<input type="hidden" name="countRep" value="<%=i%>">
					<logic:notPresent name="Records" scope="request">
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
		<TR><TD>&nbsp;</TD></TR>
		<TR>
			<TD>
				<logic:present name="notshow" scope="request">
			      	<tr>
				    	<td colspan="4">
				    		<DIV align="right">				    			
				    			<input class="input-button" type="reset" value="У  ��" onclick="validateReport()" />
								&nbsp;
								<input class="input-button" type="reset" value="��  ��" onclick="sendReport()" />
								&nbsp;
								<INPUT class="input-button" id="back" type="button" value=" ��  �� " name="butBack" onclick="back()">
								
				    		</DIV></td>
				    </tr>		
				</logic:present>
			</TD>
		</TR>
	</table>
	</lable>
</body>
</html:html>
