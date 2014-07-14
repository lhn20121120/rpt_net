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
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript" src="../../js/prototype-1.4.0.js"></script>
	<script type="text/javascript">
			/**
			 * ����ģ���ļ���׺
			 */
		 var EXT_RAQ="<%=Config.EXT_RAQ%>";
		 //ģ�����Ƽ����
		 var reptNameCheckResult = false;
		 //ģ���ż����
		 var reptIdCheckResult = false;
		 
		 //���ύ��֤	 
		 function _submit(form)
		 {
			var subButton=document.getElementById("subButton");
		  	if(form.templateFile.value=="")
		  	{
			 	alert("��ѡ��Ҫ�����ģ���ļ�!\n");
			 	return false;
			}
			else
			{
			 	if(getExt(form.templateFile.value)!=EXT_RAQ)
			 	{
			 	 	alert("������RAQ��ʽ��ģ���ļ���");
			 		return false;
			 	}
			}
			
			if(form.reportStyle.checked){
				if(form.qdreportFile.value=="")
			  	{
				 	alert("��ѡ��Ҫ�����ģ���ļ�!\n");
				 	return false;
				}
				else
				{
				 	if(getExt(form.qdreportFile.value)!=EXT_RAQ)
				 	{
				 	 	alert("������RAQ��ʽ��ģ���ļ���");
				 		return false;
				 	}
				}
			}
			
			if(form.reportName.value=="")
			{
				alert("������ģ�����ƣ�");
				form.reportName.focus();
				return false;
			}
			if(form.childRepId.value=="")
			{
				alert("������ģ���ţ�");
				form.childRepId.focus();
				return false;
			}
			if(CheckNumber(form.childRepId.value) == false)  //�Ƿ��ַ�
			{
				alert("�Բ���,ģ����ֻ�������ֻ���ĸ!");
				form.childRepId.focus();
				return false;
			}
			
			if(form.versionId.value=="")
			{
				alert("������ģ��汾�ţ�");
				form.versionId.focus();
				return false;
			}
			
			if(CheckNumber(form.versionId.value) == false)  //�Ƿ��ַ�
			{
				alert("�Բ���,ģ��汾��ֻ�������ֻ���ĸ!");
				form.versionId.focus();
				return false;
			}
			
			//���
			function Check( reg, str ){
				if( reg.test( str ) ){
					return true;
				}
				return false;
			}
			// �������
			function CheckNumber( str ){
				// var reg = /^\d*(?:$|\.\d*$)/;
			     var reg = /^[A-Za-z0-9]+$/;
				return Check( reg, str );
			}
 			
			subButton.disabled=true;
			return true;
		 }
		 function  setqdfile(){
		 	var dd = document.getElementById('reportStyle');
		 	if(dd.checked ){
		 		document.getElementById('tr_qdf').style.display='';
		 	} else{
		 		document.getElementById('tr_qdf').style.display='none'
		 	}
		 }
		 
		 window.onload = function(){
		 	setqdfile();
		 }
		 
		 //����ļ�ʱ�Զ����ļ�������������
			function autoSetName()
			{
			    var fileArray = getFileNameAndExtendName();
			    //�ļ���
			    var fileName = fileArray[0];
			    //�ļ���չ��
			    var extendName = fileArray[1];
	            
	            if(extendName!=".raq")
	            {
	            	alert("������raq��ʽ��ģ���ļ���");
			    }
			    else	
			    {
			    	document.getElementById('reportName').value = fileName;			    
			    	document.getElementById("reportName").select();
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
			  var reptId = $("childRepId").value;
			  var versionId = $("versionId").value;
			  var reptName = $("reportName").value;
			  if(checkObj=='reptId'&&reptId!='' && versionId !='')
			  {
			  	 checkReptId(reptId,versionId);
			  }
			  else if(checkObj=='reptName'&&reptName!='')
			  {
				  checkReptName(reptName);
			  }
	     }
	  
	    //�ύ���-ģ��ID���汾�� 
	    function checkReptId(reptId,versionId)
	    {
	    	var url = "<%=Config.WEBROOTULR%>/template/saveFZTmpt.do?validate=reptId&childRepId="+reptId+"&versionId="+versionId;
			var param = "radom="+Math.random();
			new Ajax.Request(url,{method: 'post',parameters:param,onComplete:validateReptIDHandler,onFailure: reptIdCheckError});
			//$("checkResult_reptId").innerHTML="<font color='#ff0000'>��Ϣ�����.....</font>";		  
	    } 
	    //�ύ���-ģ������
	    function checkReptName(reptName)
	    {
	    	var url = "<%=Config.WEBROOTULR%>/template/saveFZTmpt.do?validate=reptName";
			var param ="reportName="+reptName+"&radom="+Math.random();
			//����ת��
			param = encodeURI(param);
			param = encodeURI(param);
			new Ajax.Request(url,{method: 'post',parameters:param,onComplete:validateReptNameHandler,onFailure:reptNameCheckError});
			//$("checkResult_reptName").innerHTML="<font color='#ff0000'>��Ϣ�����.....</font>";	
	    }
	     
	    //��ѯʧ�ܴ������ģ��ID���汾��
	    function reptIdCheckError(request)
	    {
	         //$("checkResult_reptId").innerHTML="<img src='../../image/check_error.gif'/>ϵͳæ����д��Ϣ��δ��飡";
	          reptIdCheckResult = false;
	    }
	    //��ѯʧ�ܴ������ģ������
	    function reptNameCheckError(request)
	    {
	         //$("checkResult_reptName").innerHTML="<img src='../../image/check_error.gif'/>ϵͳæ����д��Ϣ��δ��飡";
             reptNameCheckResult = true;
	    }
	    
	     //ģ��ID��֤Handler
		function validateReptIDHandler(request)
		{
			try
			{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
				if(result == 'false')  
				  {
				     $("checkResult_reptId").innerHTML="<img src='../../image/check_right.gif'/>";
				     reptIdCheckResult = true;
				  }	
				  else if(result == 'true')
				  {
					  $("checkResult_reptId").innerHTML="<img src='../../image/check_error.gif'/>��ģ���ż��汾�Ѿ�����!";
					      reptIdCheckResult = false;
				  }
			}
			catch(e)
			{}
	    }
	    //ģ��������֤Handler
		function validateReptNameHandler(request)
		{

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
	<html:form method="post" action="/template/saveFZTmpt" enctype="multipart/form-data" onsubmit="return _submit(this)">
		<html:hidden property="isReport" value="2"/>
		<table border="0" cellspacing="0" cellpadding="4" width="100%" align="center">
			<tr><td height="8"></td></tr>
			<tr>
				<td>
					��ǰλ�� >> ������� >> ��������ģ��
				</td>
			</tr>
			<tr><td height="10"></td></tr>		
		</table>
		<table cellpadding="4" cellspacing="1" border="0" width="100%" align="center" class="tbcolor">
			<tr class="titletab">
				<th align="center">
					��������ģ��
				</th>
			</tr>
			<tr align="center" bgcolor="#ffffff">
				<td align="center">
					<table cellspacing="2" cellpadding="2" border="0" width="60%" align="center">
						<TR bgcolor="#ffffff">
							<TD>
								��ѡ��ģ���ļ���
							</TD>
							<TD>
								<INPUT id="templateFile" class="input-text" type="file" size="30" name="templateFile" onchange="autoSetName()">
							</TD>
						</TR>
						<TR id="tr_qdf" bgcolor="#ffffff" style="display:none">
							<TD>								
							</TD>
							<TD>
								<INPUT id="qdreportFile" class="input-text" type="file" size="30" name="qdreportFile" >
								�������ѯģ��)
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								ģ�����ƣ�
							</TD>
							<TD>
								<html:text property="reportName" size="30" maxlength="250" styleClass="input-text" styleId="reportName" /><label id="checkResult_reptName"></label>
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								ģ���ţ�
							</TD>
							<TD>
								<html:text property="childRepId" size="10" maxlength="10" styleClass="input-text" styleId="childRepId" onblur="checkInfo('reptId')"/>
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								�汾�ţ�
							</TD>
							<TD>
								<html:text property="versionId" size="10" maxlength="4" styleClass="input-text" styleId="versionId" onblur="checkInfo('reptId')"/><label id="checkResult_reptId"></label>
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								ģ�����ͣ�
							</TD>
							<TD>
								<html:select property="repTypeId" styleId="repTypeId"><html:optionsCollection name="utilFormBean" property="repTypes"/>
								</html:select>
							</TD>
						</TR>

						<TR bgcolor="#ffffff">
							<TD>
								���ҵ�λ��
							</TD>
							<TD>
								<html:select property="reportCurUnit" styleId="reportCurUnit"><html:optionsCollection name="utilFormBean" property="curUnits" />
								</html:select>
							</TD>
						</TR>
						<tr bgcolor="#ffffff">
							<td >
								�������ȼ���
							</td>
							<td>
								<html:select property="priorityFlag">
									<option value="1">һ��</option>					
									<option value="2" >����</option>
									<option value="3" >����</option>
									<option value="4" >�ļ�</option>
									<option value="5" >�弶</option>
								</html:select>
							</td>
						</tr>
						
						<tr bgcolor="#ffffff">
							<td >
								�Ƿ��嵥ʽ����
							</td>
							<td>
							<html:checkbox property="reportStyle" value="2" onclick="setqdfile()"></html:checkbox>								
							</td>
						</tr>
						<tr bgcolor="#ffffff">
							<td>
								ƴ�ӻ��ܶ�Ӧ������
							</td>
							<td>
								<html:text property="joinTemplateId" size="10" maxlength="6"/>��Ϊ��
							</td>
						</tr>
						<TR>
							<TD align="center" colspan="2">
								<input type="submit" name="save" value="����ģ��" id="subButton" >&nbsp;&nbsp;
								<input type="button" name="save" value="�� ��" onclick="location.href='<%=Config.WEBROOTULR %>/template/viewTemplate.do'">
							</TD>
							<td>
															</td>
						</TR>
					</table>
				</td>
			</tr>
		</table>
	</html:form>
</body>
</html:html>
