<%@ page contentType="text/html;charset=GBK"%>
<jsp:directive.page import="com.cbrc.smis.common.Config" />
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<title>���߱���</title>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<LINK REL="StyleSheet" HREF="<bean:write name="css"/>" TYPE="text/css">
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
			try
			{			   
				// document.all.FramerControl1.HttpInit();
				document.all.FramerControl1.DsoHttpInit();
				// document.all.FramerControl1.HttpAddPostCurrFile("reportFile", repInId);
				document.all.FramerControl1.DsoHttpAddPostCurrFile("reportFile", repInId); 
				//  saveResult = document.all.FramerControl1.HttpPost('<%=Config.WEBROOTULR%>/report/saveOnLineReport.do?repInId='+repInId+'&fileName='+fileName); 
				saveResult = document.all.FramerControl1.DsoHttpPost('<%=Config.WEBROOTULR%>report/saveOnLineReport.do?repInId='+repInId+'&fileName='+fileName);						
				//   document.location.reload();
				enableValidate = true;
				enableSend = true;
				
			}
			catch(e)
			{
			//window.open('<%=request.getContextPath()%>/help/excel.htm','newwindow','height=420,width=510,top=70,left=42,toolbar=no,menubar=no,scrollbars=yes,resizable=no,location=no,status=no'); //��������
				//alert('ϵͳæ�����Ժ�����...��');
			}
			return saveResult;
		}
		  //���汨��
	    function saveReportButton()
	    {  
			var result = saveReport();     
			if(enableValidate==true){
				var excelParam = result.split(",");
				fileName = excelParam[0];
				fileURL = excelParam[1];
				excelPath = excelParam[2];
				alert('����ɹ���');
			}
		}
		
		//�Զ�����
		function autoFillZero(){
			try{
				document.all.FramerControl1.DsoHttpInit();		
				document.all.FramerControl1.DsoHttpPost('<%=Config.WEBROOTULR%>report/autoFillZeroReport.do?fileName='
					+fileName+"&repInId="+repInId+"&excelPath="+excelPath);
				window.setTimeout("openDoc()",2000) ;
			//	document.all.FramerControl1.Open(fileURL,true,'Excel.Sheet');   //���´��������Excel				
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
			  	if(enableValidate==false||repInId=='')
			  	{
			  		alert('���ȱ��汨��');
			  		return;
			  	}
			  	
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
		//������           ���ע  07-10-18
<%--		function sendReport()--%>
<%--		{--%>
<%--			if(enableSend==false)--%>
<%--			{--%>
<%--				alert('���ȱ��汨��');--%>
<%--			  	return;--%>
<%--			}--%>
<%--			if(confirm('ȷ�����͸ñ���'))--%>
<%--			{--%>
<%--		    	closeDoc();--%>
<%--		    	window.location="<%=request.getContextPath()%>/upLoadOnLineReport.do?" +requestParam+"&repInId=" + repInId ;--%>
<%--			}--%>
<%--		} --%>
		
		
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
					 closeDoc()
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
		
		//���Ϊ
		function saveAsDoc()
		{
			try
			{
				document.all.FramerControl1.showdialog(3);
			}
			catch(e)
			{
			}
			
		}
		
		//�������뱨��
		function uploadFile()
		{
		   	 $('openfile').click(); 
         	 var filePath = $('openfile').value;
         	 var openType ='';
         	if(filePath=='')
         	{
         		return;
         	}
         	if(filePath.lastIndexOf('.xls')<0)
         	{
         		alert('�ļ���ʽ����������Excel�ļ���');
         		return;	  
			}
			try
			{
				document.all.FramerControl1.Open(filePath,true,'Excel.Sheet');
			}
			catch(e)
			{
			}
		}
		
		//���ļ�
		function openDoc()
		{
	 
			if(fileURL=='')
				alert('ϵͳæ�����Ժ�����...��');
			else
			{
				try
				{
					document.all.FramerControl1.Open(fileURL,true);
				//document.getElementById('FramerControl1').Open("D:\\aa.xls");
					
				}
				catch(e)
				{
					alert('ϵͳæ�����Ժ�����aaa...��');
				}
			}
		}
		//�ر��ļ�
		function closeDoc()
		{
			try
			{
				document.all.FramerControl1.Close();    
			}
			catch(e)
			{
			}
		}
		//����
		function back()
		{
			closeDoc();
			window.location= "<%=request.getContextPath()%>/viewDataReport.do?"+requestParam;
		}
		//�ȶ�
		function check(){
			//window.location="<%=request.getContextPath()%>/viewCheckReport.do?reportID="+repInId;
			window.open("<%=request.getContextPath()%>/viewCheckReport.do?reportID="+repInId+"&checkID="+repInId,"Check");
		}

<%--	function FramerControl1_NotifyCtrlReady() {--%>
<%--        alert("�¼� NotifyCtrlReady ���� ");--%>
<%--	}--%>

	</script>




<%--<SCRIPT LANGUAGE=javascript FOR=FramerControl1 EVENT=NotifyCtrlReady>
<!--
FramerControl1_NotifyCtrlReady()
//-->
</SCRIPT>
	

--%></head>
   <body onload="openDoc();" onunload="closeDoc();">
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
				<INPUT class="input-button" id="ButtonUpload" name="ButtonUpload" type="button" value=" ��  �� " onclick="uploadFile()">
				&nbsp;
				<INPUT class="input-button" id="saveAs" type="button" value=" ���Ϊ " onclick="saveAsDoc()">
				
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

				 <script language="javascript" src="<%=request.getContextPath()%>/js/createDSOFrame.jsp"></script> 

						</TD>
					</tr>
				</table>
			</td>
		</tr>
	</table>

</body>
</html:html>
