<%@ page contentType="text/html;charset=GBK"%>
<jsp:directive.page import="com.cbrc.smis.common.Config" />
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<title>在线报送</title>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<LINK REL="StyleSheet" HREF="<bean:write name="css"/>" TYPE="text/css">
	<script language="javascript" src="<%=Config.WEBROOTULR%>/js/prototype-1.4.0.js"></script>

	<script type="text/javascript">
		//报表路径	
		var fileURL = "<logic:present name='ReportURL'><bean:write name='ReportURL'/></logic:present>";
		//报表ID
		var repInId = "<logic:present name='RepInId'><bean:write name='RepInId'/></logic:present>";
		//报表文件名
		var fileName = "<logic:present name='FileName'><bean:write name='FileName'/></logic:present>";
		//查询参数
		var requestParam = "<logic:present name='RequestParam'><bean:write name='RequestParam'/></logic:present>";
		//报表的实际路径
		var excelPath = "<logic:present name='ExcelPath'><bean:write name='ExcelPath'/></logic:present>";		
	    //是否可以校验
	    var enableValidate = false;
	    //是否可以报送
	    var enableSend = false;
	    
	    //保存报表
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
			//window.open('<%=request.getContextPath()%>/help/excel.htm','newwindow','height=420,width=510,top=70,left=42,toolbar=no,menubar=no,scrollbars=yes,resizable=no,location=no,status=no'); //弹出窗口
				//alert('系统忙，请稍后再试...！');
			}
			return saveResult;
		}
		  //保存报表
	    function saveReportButton()
	    {  
			var result = saveReport();     
			if(enableValidate==true){
				var excelParam = result.split(",");
				fileName = excelParam[0];
				fileURL = excelParam[1];
				excelPath = excelParam[2];
				alert('保存成功！');
			}
		}
		
		//自动填零
		function autoFillZero(){
			try{
				document.all.FramerControl1.DsoHttpInit();		
				document.all.FramerControl1.DsoHttpPost('<%=Config.WEBROOTULR%>report/autoFillZeroReport.do?fileName='
					+fileName+"&repInId="+repInId+"&excelPath="+excelPath);
				window.setTimeout("openDoc()",2000) ;
			//	document.all.FramerControl1.Open(fileURL,true,'Excel.Sheet');   //重新打开已填零的Excel				
			}catch(e){
				alert('系统忙，请稍后再试...!');
			}			
		}
		
		//校验报表
		function validateReport()
		{
			 try
		 	 {
		 	 	saveReport();
			  	if(enableValidate==false||repInId=='')
			  	{
			  		alert('请先保存报表！');
			  		return;
			  	}
			  	
			  	var validateURL = "<%=Config.WEBROOTULR%>report/validateOnLineReport.do?repInId="+repInId; 
			    var param = "radom="+Math.random();
			   	new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:validateReportHandler,onFailure: reportError});
			   	prodress.style.display = "" ;
		   	}
		   	catch(e)
		   	{
		   		alert('系统忙，请稍后再试...！');
		   	}
		}				
		
		//校验Handler
		function validateReportHandler(request)
		{
			try
			{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
				if(result == 'false')  
				  {
				  	 if(confirm('校验失败！是否需要查看校验信息?'))
				        window.open("<%=request.getContextPath()%>/report/viewDataJYInfo.do?" + "repInId=" + repInId,'校验结果','scrollbars=yes,height=600,width=500,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
				  }	
				  else if(result == 'true')
				  {
					 alert('校验通过！');
				  }
				  prodress.style.display = "none" ;
				  enableValidate = false;
			}
			catch(e)
			{
				prodress.style.display = "none" ;
			}
	    }
	    
	    //查询失败处理
	    function reportError(request)
	    {
	        alert('系统忙，请稍后再试...！');
	    }
		//报表报送           吴昊注  07-10-18
<%--		function sendReport()--%>
<%--		{--%>
<%--			if(enableSend==false)--%>
<%--			{--%>
<%--				alert('请先保存报表！');--%>
<%--			  	return;--%>
<%--			}--%>
<%--			if(confirm('确定报送该报表？'))--%>
<%--			{--%>
<%--		    	closeDoc();--%>
<%--		    	window.location="<%=request.getContextPath()%>/upLoadOnLineReport.do?" +requestParam+"&repInId=" + repInId ;--%>
<%--			}--%>
<%--		} --%>
		
		
		function sendReport()
		{
			
			if(enableSend==false)
			{
				alert('请先保存报表！');
			  	return;
			}
			if(confirm('确定报送该报表？')){
			 	try
			 	 {
				  	reportInId=repInId;
				  	var upReportURL ="<%=Config.WEBROOTULR%>upLoadOnLineReport.do?" +requestParam+"&repInId=" + repInId ;
				    var param = "radom="+Math.random();
				   	new Ajax.Request(upReportURL,{method: 'post',parameters:param,onComplete:upReportHandler,onFailure: reportError});
			   	}
			   	catch(e)
			   	{
			   		alert('系统忙，请稍后再试...！');
			   	}
			}
		} 
		//报送Handler
		function upReportHandler(request)
		{
			try
			{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
					
				  if(result == 'true')
				  {
					 alert('报送成功！');	
					 closeDoc()
					 window.location="<%=request.getContextPath()%>/viewDataReport.do?" + requestParam; 
				  }
				  else  if(result == 'BJ_VALIDATE_NOTPASS')
				  {
				     alert("表间校验不通过，不能上报该报表！");
				  }else if( result == 'BN_VALIDATE_NOTPASS'){
				  
				 	 alert("表内校验不通过，不能上报该报表！");
				  }
				  else{
				 	 alert('系统忙，请稍后再试...！');
				  
				  }
			}
			catch(e)
			{}
	    }
		
		//另存为
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
		
		//离线载入报表
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
         		alert('文件格式错误！请载入Excel文件！');
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
		
		//打开文件
		function openDoc()
		{
	 
			if(fileURL=='')
				alert('系统忙，请稍后再试...！');
			else
			{
				try
				{
					document.all.FramerControl1.Open(fileURL,true);
				//document.getElementById('FramerControl1').Open("D:\\aa.xls");
					
				}
				catch(e)
				{
					alert('系统忙，请稍后再试aaa...！');
				}
			}
		}
		//关闭文件
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
		//返回
		function back()
		{
			closeDoc();
			window.location= "<%=request.getContextPath()%>/viewDataReport.do?"+requestParam;
		}
		//比对
		function check(){
			//window.location="<%=request.getContextPath()%>/viewCheckReport.do?reportID="+repInId;
			window.open("<%=request.getContextPath()%>/viewCheckReport.do?reportID="+repInId+"&checkID="+repInId,"Check");
		}

<%--	function FramerControl1_NotifyCtrlReady() {--%>
<%--        alert("事件 NotifyCtrlReady 触发 ");--%>
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
				在线/离线报送
			</th>
		</tr> 
		<tr>
			<td bgcolor="#ffffff">
				&nbsp;
				<input type="file" id="openfile" style="display:none">
				<INPUT class="input-button" id="ButtonSave" type="button" value=" 保  存 " name="ButtonSave" onclick="saveReportButton()">
				&nbsp;
				<INPUT class="input-button" id="ButtonJY" type="button" value=" 校  验 " name="ButtonJY" onclick="validateReport()">
				&nbsp;
				<INPUT class="input-button" id="ButtonSend" type="button" value=" 报  送 " name="ButtonSend" onclick="sendReport()">
				&nbsp;
				<INPUT class="input-button" id="ButtonUpload" name="ButtonUpload" type="button" value=" 载  入 " onclick="uploadFile()">
				&nbsp;
				<INPUT class="input-button" id="saveAs" type="button" value=" 另存为 " onclick="saveAsDoc()">
				
				<logic:notEmpty name="Operator" property="subOrgIds">
					&nbsp;
					<INPUT class="input-button" id="back" type="button" value=" 数据比对 " name="butBack" onclick="check()">
				</logic:notEmpty>
				
				&nbsp;
				<INPUT class="input-button" id="ButtonFill" type="button" value=" 自动填零 " onclick="autoFillZero()">
				&nbsp;
				<INPUT class="input-button" id="back" type="button" value=" 返  回 " name="butBack" onclick="back()">
				
			</td>
		</tr>
		<tr id="prodress" style="display:none">
			<td bgcolor="#FFFFFF" >
					&nbsp;&nbsp;&nbsp;<span class="txt-main" style="color:#FF3300">正在校验,请稍候...</span>
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
