<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<jsp:useBean id="utilFormBean" scope="page" class="com.cbrc.smis.form.UtilForm" />
<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript" src="../js/func.js"></script>
	<script language="javascript" src="../js/prototype-1.4.0.js"></script>
	<script type="text/javascript">
			/**
			 * 报表模板文件后缀
			 */
		 var analysis_REPORT="<%=Config.analysis_REPORT%>";
		 //模版名称检查结果
		 var reptNameCheckResult = false;
		 //模版编号检查结果
		 var reptIdCheckResult = false;
		 
		 //表单提交验证	 
		 function _submit(form)
		 {
		
		  	if(form.templateFile.value=="")
		  	{
			 	alert("请选择要载入的cpt模板文件!\n");
			 	return false;
			}
			else
			{
			 	if(getExt(form.templateFile.value)!=analysis_REPORT)
			 	{
			 	 	alert("请载入cpt格式的模版文件！");
			 		return false;
			 	}
			}
			if(form.ATName.value=="")
			{
				alert("请输入模版名称！");
				form.ATName.focus();
				return false;
			}
			if(form.ATId.value=="")
			{
				alert("请输入模版编号！");
				form.ATId.focus();
				return false;
			}
			if(reptNameCheckResult==false)
			{
				alert('该模版名称已经存在!');
				return false;
			}
			if(reptIdCheckResult==false)
			{
				alert('该模版编号及版本已经存在!');
				return false;
			}

			return true;
		 }
		 
		 
		 //添加文件时自动把文件名填入名称中
			function autoSetName()
			{
			    var fileArray = getFileNameAndExtendName();
			    //文件名
			    var fileName = fileArray[0];
			    //文件扩展名
			    var extendName = fileArray[1];
	            
	            if(extendName!=".cpt")
	            {
	            	alert("请载入cpt格式的模版文件！");
			    }
			    else	
			    {
			    	document.getElementById('ATName').value = fileName;			    
			    	document.getElementById("ATName").select();
			    }
		 	}
			
			/*获得文件名和文件扩展名*/
			function getFileNameAndExtendName()
			{
				var filePath = document.getElementById('templateFile').value;
	            var a = filePath.lastIndexOf("\\")+1;
	            var b = filePath.length;
	            var c = filePath.lastIndexOf(".");
	           
	            var fileName;
	          	var extendName;          	
	            
	            if(c!=-1)
	            {
	            	fileName = filePath.substring(a,c);
	            	extendName =filePath.substring(c);
	            }
	            else
	            {
	            	firstName= filePath.substring(a,b);
	            	extendName="";
	            }
	           
	            var fileArray = new Array();
	            fileArray[0] = fileName;
	            fileArray[1] = extendName;	 
	                       
	            return fileArray;
			}
			
		//检查填写信息
		function checkInfo(checkObj)
		{
			  var reptId = $("ATId").value;
			  var reptName = $("ATName").value;
			
			  if(checkObj=='reptId'&&reptId!='')
			  {
			  	 checkReptId(reptId);
			  }
			  else if(checkObj=='reptName'&&reptName!='')
			  {
				  checkReptName(reptName);
			  }
	     }
	  
	    //提交检查-模版ID 
	    function checkReptId(reptId)
	    {
	    	var url = "<%=Config.WEBROOTULR%>/analysis/addAnalysisTemplate.do?validate=reptId";
			var param = "ATId="+reptId+"&radom="+Math.random();
			new Ajax.Request(url,{method: 'post',parameters:param,onComplete:validateReptIDHandler,onFailure: reptIdCheckError});
			//$("checkResult_reptId").innerHTML="<font color='#ff0000'>信息检查中.....</font>";		  
	    } 
	    //提交检查-模版名称
	    function checkReptName(reptName)
	    {
	    	var url = "<%=Config.WEBROOTULR%>/analysis/addAnalysisTemplate.do?validate=reptName";
			var param ="ATName="+reptName+"&radom="+Math.random();
			//中文转码
			param = encodeURI(param);
			param = encodeURI(param);
			new Ajax.Request(url,{method: 'post',parameters:param,onComplete:validateReptNameHandler,onFailure:reptNameCheckError});
			//$("checkResult_reptName").innerHTML="<font color='#ff0000'>信息检查中.....</font>";	
	    }
	     
	    //查询失败处理－检查模版ID，版本号
	    function reptIdCheckError(request)
	    {
	         $("checkResult_reptId").innerHTML="<img src='../image/check_error.gif'/>系统忙，填写信息尚未检查！";
	          reptIdCheckResult = false;
	    }
	    //查询失败处理－检查模版名称
	    function reptNameCheckError(request)
	    {
	         $("checkResult_reptName").innerHTML="<img src='../image/check_error.gif'/>系统忙，填写信息尚未检查！";
             reptNameCheckResult = false;
	    }
	    
	     //模版ID验证Handler
		function validateReptIDHandler(request)
		{
			try
			{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
				if(result == 'false')  
				  {
				     $("checkResult_reptId").innerHTML="<img src='../image/check_right.gif'/>";
				     reptIdCheckResult = true;
				  }	
				  else if(result == 'true')
				  {
					  $("checkResult_reptId").innerHTML="<img src='../image/check_error.gif'/>该模版编号已经存在!";
					      reptIdCheckResult = false;
				  }
			}
			catch(e)
			{}
	    }
	    //模版名称验证Handler
		function validateReptNameHandler(request)
		{
			try
			{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
				if(result == 'false')  
				  {
				     $("checkResult_reptName").innerHTML="<img src='../image/check_right.gif'/>";
				     reptNameCheckResult = true;
				  }	
				  else if(result == 'true')
				  {
					  $("checkResult_reptName").innerHTML="<img src='../image/check_error.gif'/>该模版名称已经存在!";
					      reptNameCheckResult = false;
				  }
			}
			catch(e)
			{}
	    }
	    
	</script>
</head>
<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
					alert("<bean:write name='Message' property='alertMsg'/>");
				</script>
		</logic:greaterThan>
	</logic:present>
	<br>
	<html:form method="post" action="/analysis/addAnalysisTemplate" enctype="multipart/form-data" onsubmit="return _submit(this)">
		<table cellpadding="4" cellspacing="1" border="0" width="100%" align="center" class="tbcolor">
			<tr class="titletab">
				<th align="center">
					新增分析报表模板
				</th>
			</tr>
			<tr align="center" bgcolor="#ffffff">
				<td align="center">
					<table cellspacing="2" cellpadding="2" border="0" width="90%" align="center">
						<TR bgcolor="#ffffff">
							<TD>
								请选择分析报表模板文件：
							</TD>
							<TD>
								<INPUT id="templateFile" class="input-text" type="file" size="50" name="templateFile" onchange="autoSetName()">
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								模板名称：
							</TD>
							<TD>
								<html:text property="ATName" size="30" maxlength="50" styleClass="input-text" styleId="ATName" onblur="checkInfo('reptName')"/><label id="checkResult_reptName"></label>
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								模板编号：
							</TD>
							<TD>
								<html:text property="ATId" size="10" maxlength="20" styleClass="input-text" styleId="ATId" onblur="checkInfo('reptId')"/><label id="checkResult_reptId"></label>
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								模板类型：
							</TD>
							<TD>
								<html:select property="analyTypeID" ><html:optionsCollection name="utilFormBean" property="analysisTPType"/>
								</html:select>
							</TD>
						</TR>
						<TR>
							<TD align="center" colspan="2">
								<input type="submit" name="save" value="保存模版">
							</TD>
						</TR>
						
					</table>
				</td>
			</tr>
		</table>
	</html:form>
</body>
</html:html>
