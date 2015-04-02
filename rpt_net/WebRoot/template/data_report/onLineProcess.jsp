<%@ page contentType="text/html;charset=GBK"%>
<jsp:directive.page import="com.cbrc.smis.common.Config" />
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>

<head>
	<title>���߱���</title>
	<html:base/>
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript" src="<%=Config.WEBROOTULR%>/js/prototype-1.4.0.js"></script>

	<script type="text/javascript">
		//����·��	
		var fileURL = "<logic:present name='ReportURL'><bean:write name='ReportURL'/></logic:present>";
		//����ID
		var repInId = "<logic:present name='RepInId'><bean:write name='RepInId'/></logic:present>";
		//�����ļ���
		var fileName = "<logic:present name='FileName'><bean:write name='FileName'/></logic:present>";
		//��ѯ����
		var requestParam = "<logic:present name='RequestParam'><bean:write name='RequestParam'/></logic:present>";
		//�����ʵ��·��
		var excelPath = "<logic:present name='ExcelPath'><bean:write name='ExcelPath'/></logic:present>";		
	    //�Ƿ����У��
	    var enableValidate = false;
	    //�Ƿ���Ա���
	    var enableSend = false;
	    
	    //���汨��
	    function saveReport()
	    { 
	    	var saveResult;
			saveResult = SaveDocument();
			if(saveResult)
			{
				enableValidate = true;
				enableSend = true;
			}
			return saveResult;
		}
		  //���汨��
	    function saveReportButton()
	    {  
			var result = saveReport();    

			if(enableValidate==true)
			{
				
				alert('����ɹ���');
			}
		}
		
		//�Զ�����
		function autoFillZero()
		{
			try{
				var url = "<%=Config.WEBROOTULR%>report/autoFillZeroReport.do?fileName="
						+fileName+"&repInId="+repInId+"&excelPath="+excelPath;
				 var param = "radom="+Math.random();
				 new Ajax.Request(url,{method: 'post',parameters:param});
				window.setTimeout("Load()",2000) ; //���´��������Excel	
		      			
			}catch(e){
				alert('ϵͳæ�����Ժ�����...!');
			}			
		}
		
		//У�鱨��
		function validateReport()
		{
			 try
		 	 {
		 	 	saveReport();
			  //	if(enableValidate==false||repInId=='')
			  //	{
			  	//	alert('���ȱ��汨��');
			  		//return;
			  //	}
			  	
			  	var validateURL = "<%=Config.WEBROOTULR%>report/validateOnLineReport.do?repInId="+repInId; 
			    var param = "radom="+Math.random();
			   	new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:validateReportHandler,onFailure: reportError});
			   	prodress.style.display = "" ;
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
				        window.open("<%=request.getContextPath()%>/report/viewDataJYInfo.do?" + "repInId=" + repInId,'У����','scrollbars=yes,height=600,width=500,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
				  }	
				  else if(result == 'true')
				  {
					 alert('У��ͨ����');
				  }
				  prodress.style.display = "none" ;
				  enableValidate = false;
			}
			catch(e)
			{
				prodress.style.display = "none" ;
			}
	    }
	    
	    //��ѯʧ�ܴ���
	    function reportError(request)
	    {
	        alert('ϵͳæ�����Ժ�����...��');
	    }		
		
		function sendReport()
		{
			
			if(enableSend==false)
			{
				alert('���ȱ��汨��');
			  	return;
			}
			if(confirm('ȷ�����͸ñ���')){
			 	try
			 	 {
				  	reportInId=repInId;
				  	var upReportURL ="<%=Config.WEBROOTULR%>upLoadOnLineReport.do?" +requestParam+"&repInId=" + repInId ;
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
					 UnLoad();
					 window.location="<%=request.getContextPath()%>/viewDataReport.do?" + requestParam; 
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
			
		//����
		function back()
		{
			UnLoad();
			window.location= "<%=request.getContextPath()%>/viewDataReport.do?"+requestParam;
		}
		//�ȶ�
		function check(){
			if(enableSend==false)
			{
				alert('���ȱ��汨��');
			  	return;
			}
			window.open("<%=request.getContextPath()%>/viewCheckReport.do?reportID="+repInId+"&checkID="+repInId,"Check");
		}
		
		
	<%
		  String mHttpUrlName=request.getRequestURI();
		  String mScriptName=request.getServletPath();
		  String mServerName="/servlet/WebOfficeServ";
		  
		  String mServerUrl="http://"+request.getServerName()+":"+request.getServerPort()+mHttpUrlName.substring(0,mHttpUrlName.lastIndexOf(mScriptName))+mServerName;
	%>
		//���ã���ʾ����״̬
	function StatusMsg(mString){
	  StatusBar.innerText=mString;
	}
	
	//���ã�����iWebOffice
	function Load(){
	  try{
	  document.all.WebOffice.WebUrl="<%=mServerUrl%>";
	
	  document.all.WebOffice.FileName= fileName ;
	  document.all.WebOffice.FileType=".xls";
	  document.all.WebOffice.EditType="2";
	  document.all.WebOffice.UserName="test";
	  document.all.WebOffice.RecordID= repInId;
	  document.all.WebOffice.WebOpen();  	//�򿪸��ĵ�
	  StatusMsg(document.all.WebOffice.Status);
	  }catch(e){}
	}
	
	//���ã��˳�iWebOffice
	function UnLoad(){
	  try{
	  if (!document.all.WebOffice.WebClose()){
	     StatusMsg(document.all.WebOffice.Status);
	  }else{
	     StatusMsg("�ر��ĵ�...");
	  }
	  }catch(e){}
	}
		//���ã������ĵ�
	function SaveDocument(){
	  //webform.WebOffice.FileName=webform.FileName.value;			//����û��������ļ�����
	  //webform.WebOffice.WebSetMsgByName("SUBJECT",webform.Subject.value);	//��������Ϣ���,�Ա��̨�ܹ�ȡ��
	  if (!document.all.WebOffice.WebSave()){
	     StatusMsg(document.all.WebOffice.Status);
	     return false;
	  }else{
	     StatusMsg(document.all.WebOffice.Status);
	     
	     return true;
	  }
	}
	
	//���ã��򿪱����ļ�
	function WebOpenLocal(){
	  try{
	    document.all.WebOffice.WebOpenLocal();
	    StatusMsg(document.all.WebOffice.Status);
	  }catch(e){}
	}
	
	//���ã���Ϊ�����ļ�
	function WebSaveLocal(){
	  try{
	    document.all.WebOffice.WebSaveLocal();
	    StatusMsg(document.all.WebOffice.Status);
	  }catch(e){}
	}
	
	</script>



</head>
   <body onload="Load()" onunload="UnLoad()">
	<br>
	<table width="100%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
		<tr class="titletab">
			<th align="center">
				����/���߱���
			</th>
		</tr>
		<tr>
			<td bgcolor="#ffffff">
				&nbsp;
				<input type="file" id="openfile" style="display:none">
				<INPUT class="input-button" id="ButtonSave" type="button" value=" ��  �� " name="ButtonSave" onclick="saveReportButton()">
				&nbsp;
				<INPUT class="input-button" id="ButtonJY" type="button" value=" У  �� " name="ButtonJY" onclick="validateReport()">
				&nbsp;
				<INPUT class="input-button" id="ButtonSend" type="button" value=" ��  �� " name="ButtonSend" onclick="sendReport()">
				&nbsp;
				<INPUT class="input-button" id="ButtonUpload" name="ButtonUpload" type="button" value=" ��  �� " onclick="WebOpenLocal()">
				&nbsp;
				<INPUT class="input-button" id="saveAs" type="button" value=" ���Ϊ " onclick="WebSaveLocal()">
				
				<logic:notEmpty name="Operator" property="subOrgIds">
					&nbsp;
					<INPUT class="input-button" id="back" type="button" value=" ���ݱȶ� " name="butBack" onclick="check()">
				</logic:notEmpty>
				
				&nbsp;
				<INPUT class="input-button" id="ButtonFill" type="button" value=" �Զ����� " onclick="autoFillZero()">
				&nbsp;
				<INPUT class="input-button" id="back" type="button" value=" ��  �� " name="butBack" onclick="back()">
				
			</td>
		</tr>
		<tr id="prodress" style="display:none">
			<td bgcolor="#FFFFFF" >
					&nbsp;&nbsp;&nbsp;<span class="txt-main" style="color:#FF3300">����У��,���Ժ�...</span>
			</td>
		</tr>
		<tr>
			<td bgcolor="#ffffff">
				<table width="100%" border="0" align="center" cellpadding="4" cellspacing="1">
					<tr>
						<TD width="100%" height="100%" align=center>

<OBJECT id="WebOffice" width="100%" height="650" classid="clsid:23739A7E-5741-4D1C-88D5-D50B18F7C347" codebase="iWebOffice2003.ocx#version=6,6,0,0" >
</OBJECT>
							</TD>
					</tr>
						<tr>
				          <td bgcolor=menu height='20'>
				                <div id=StatusBar>״̬��</div>
				          </td>
				        </tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
