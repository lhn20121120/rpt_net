<%@ page contentType="text/html;charset=GBK"%>
<jsp:directive.page import="com.cbrc.smis.common.Config" />
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>

<head>
	<title>在线报送</title>
	<html:base/>
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
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
			saveResult = SaveDocument();
			if(saveResult)
			{
				enableValidate = true;
				enableSend = true;
			}
			return saveResult;
		}
		  //保存报表
	    function saveReportButton()
	    {  
			var result = saveReport();    

			if(enableValidate==true)
			{
				
				alert('保存成功！');
			}
		}
		
		//自动填零
		function autoFillZero()
		{
			try{
				var url = "<%=Config.WEBROOTULR%>report/autoFillZeroReport.do?fileName="
						+fileName+"&repInId="+repInId+"&excelPath="+excelPath;
				 var param = "radom="+Math.random();
				 new Ajax.Request(url,{method: 'post',parameters:param});
				window.setTimeout("Load()",2000) ; //重新打开已填零的Excel	
		      			
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
			  //	if(enableValidate==false||repInId=='')
			  //	{
			  	//	alert('请先保存报表！');
			  		//return;
			  //	}
			  	
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
					 UnLoad();
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
			
		//返回
		function back()
		{
			UnLoad();
			window.location= "<%=request.getContextPath()%>/viewDataReport.do?"+requestParam;
		}
		//比对
		function check(){
			if(enableSend==false)
			{
				alert('请先保存报表！');
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
		//作用：显示操作状态
	function StatusMsg(mString){
	  StatusBar.innerText=mString;
	}
	
	//作用：载入iWebOffice
	function Load(){
	  try{
	  document.all.WebOffice.WebUrl="<%=mServerUrl%>";
	
	  document.all.WebOffice.FileName= fileName ;
	  document.all.WebOffice.FileType=".xls";
	  document.all.WebOffice.EditType="2";
	  document.all.WebOffice.UserName="test";
	  document.all.WebOffice.RecordID= repInId;
	  document.all.WebOffice.WebOpen();  	//打开该文档
	  StatusMsg(document.all.WebOffice.Status);
	  }catch(e){}
	}
	
	//作用：退出iWebOffice
	function UnLoad(){
	  try{
	  if (!document.all.WebOffice.WebClose()){
	     StatusMsg(document.all.WebOffice.Status);
	  }else{
	     StatusMsg("关闭文档...");
	  }
	  }catch(e){}
	}
		//作用：保存文档
	function SaveDocument(){
	  //webform.WebOffice.FileName=webform.FileName.value;			//如果用户更改了文件名称
	  //webform.WebOffice.WebSetMsgByName("SUBJECT",webform.Subject.value);	//将主题信息打包,以便后台能够取出
	  if (!document.all.WebOffice.WebSave()){
	     StatusMsg(document.all.WebOffice.Status);
	     return false;
	  }else{
	     StatusMsg(document.all.WebOffice.Status);
	     
	     return true;
	  }
	}
	
	//作用：打开本地文件
	function WebOpenLocal(){
	  try{
	    document.all.WebOffice.WebOpenLocal();
	    StatusMsg(document.all.WebOffice.Status);
	  }catch(e){}
	}
	
	//作用：存为本地文件
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
				<INPUT class="input-button" id="ButtonUpload" name="ButtonUpload" type="button" value=" 载  入 " onclick="WebOpenLocal()">
				&nbsp;
				<INPUT class="input-button" id="saveAs" type="button" value=" 另存为 " onclick="WebSaveLocal()">
				
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

<OBJECT id="WebOffice" width="100%" height="650" classid="clsid:23739A7E-5741-4D1C-88D5-D50B18F7C347" codebase="iWebOffice2003.ocx#version=6,6,0,0" >
</OBJECT>
							</TD>
					</tr>
						<tr>
				          <td bgcolor=menu height='20'>
				                <div id=StatusBar>状态栏</div>
				          </td>
				        </tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
