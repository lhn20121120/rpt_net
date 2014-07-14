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
			 * ����ģ���ļ���׺
			 */
		 var analysis_REPORT="<%=Config.analysis_REPORT%>";
		 //ģ�����Ƽ����
		 var reptNameCheckResult = false;
		 //ģ���ż����
		 var reptIdCheckResult = false;
		 
		 //���ύ��֤	 
		 function _submit(form)
		 {
		
		  	if(form.templateFile.value=="")
		  	{
			 	alert("��ѡ��Ҫ�����cptģ���ļ�!\n");
			 	return false;
			}
			else
			{
			 	if(getExt(form.templateFile.value)!=analysis_REPORT)
			 	{
			 	 	alert("������cpt��ʽ��ģ���ļ���");
			 		return false;
			 	}
			}
			if(form.ATName.value=="")
			{
				alert("������ģ�����ƣ�");
				form.ATName.focus();
				return false;
			}
			if(form.ATId.value=="")
			{
				alert("������ģ���ţ�");
				form.ATId.focus();
				return false;
			}
			if(reptNameCheckResult==false)
			{
				alert('��ģ�������Ѿ�����!');
				return false;
			}
			if(reptIdCheckResult==false)
			{
				alert('��ģ���ż��汾�Ѿ�����!');
				return false;
			}

			return true;
		 }
		 
		 
		 //����ļ�ʱ�Զ����ļ�������������
			function autoSetName()
			{
			    var fileArray = getFileNameAndExtendName();
			    //�ļ���
			    var fileName = fileArray[0];
			    //�ļ���չ��
			    var extendName = fileArray[1];
	            
	            if(extendName!=".cpt")
	            {
	            	alert("������cpt��ʽ��ģ���ļ���");
			    }
			    else	
			    {
			    	document.getElementById('ATName').value = fileName;			    
			    	document.getElementById("ATName").select();
			    }
		 	}
			
			/*����ļ������ļ���չ��*/
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
			
		//�����д��Ϣ
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
	  
	    //�ύ���-ģ��ID 
	    function checkReptId(reptId)
	    {
	    	var url = "<%=Config.WEBROOTULR%>/analysis/addAnalysisTemplate.do?validate=reptId";
			var param = "ATId="+reptId+"&radom="+Math.random();
			new Ajax.Request(url,{method: 'post',parameters:param,onComplete:validateReptIDHandler,onFailure: reptIdCheckError});
			//$("checkResult_reptId").innerHTML="<font color='#ff0000'>��Ϣ�����.....</font>";		  
	    } 
	    //�ύ���-ģ������
	    function checkReptName(reptName)
	    {
	    	var url = "<%=Config.WEBROOTULR%>/analysis/addAnalysisTemplate.do?validate=reptName";
			var param ="ATName="+reptName+"&radom="+Math.random();
			//����ת��
			param = encodeURI(param);
			param = encodeURI(param);
			new Ajax.Request(url,{method: 'post',parameters:param,onComplete:validateReptNameHandler,onFailure:reptNameCheckError});
			//$("checkResult_reptName").innerHTML="<font color='#ff0000'>��Ϣ�����.....</font>";	
	    }
	     
	    //��ѯʧ�ܴ������ģ��ID���汾��
	    function reptIdCheckError(request)
	    {
	         $("checkResult_reptId").innerHTML="<img src='../image/check_error.gif'/>ϵͳæ����д��Ϣ��δ��飡";
	          reptIdCheckResult = false;
	    }
	    //��ѯʧ�ܴ������ģ������
	    function reptNameCheckError(request)
	    {
	         $("checkResult_reptName").innerHTML="<img src='../image/check_error.gif'/>ϵͳæ����д��Ϣ��δ��飡";
             reptNameCheckResult = false;
	    }
	    
	     //ģ��ID��֤Handler
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
					  $("checkResult_reptId").innerHTML="<img src='../image/check_error.gif'/>��ģ�����Ѿ�����!";
					      reptIdCheckResult = false;
				  }
			}
			catch(e)
			{}
	    }
	    //ģ��������֤Handler
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
					  $("checkResult_reptName").innerHTML="<img src='../image/check_error.gif'/>��ģ�������Ѿ�����!";
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
					������������ģ��
				</th>
			</tr>
			<tr align="center" bgcolor="#ffffff">
				<td align="center">
					<table cellspacing="2" cellpadding="2" border="0" width="90%" align="center">
						<TR bgcolor="#ffffff">
							<TD>
								��ѡ���������ģ���ļ���
							</TD>
							<TD>
								<INPUT id="templateFile" class="input-text" type="file" size="50" name="templateFile" onchange="autoSetName()">
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								ģ�����ƣ�
							</TD>
							<TD>
								<html:text property="ATName" size="30" maxlength="50" styleClass="input-text" styleId="ATName" onblur="checkInfo('reptName')"/><label id="checkResult_reptName"></label>
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								ģ���ţ�
							</TD>
							<TD>
								<html:text property="ATId" size="10" maxlength="20" styleClass="input-text" styleId="ATId" onblur="checkInfo('reptId')"/><label id="checkResult_reptId"></label>
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								ģ�����ͣ�
							</TD>
							<TD>
								<html:select property="analyTypeID" ><html:optionsCollection name="utilFormBean" property="analysisTPType"/>
								</html:select>
							</TD>
						</TR>
						<TR>
							<TD align="center" colspan="2">
								<input type="submit" name="save" value="����ģ��">
							</TD>
						</TR>
						
					</table>
				</td>
			</tr>
		</table>
	</html:form>
</body>
</html:html>
