<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@page import="com.fitech.gznx.service.AfTemplateValiScheDelegate"%>
<%@page import="com.fitech.gznx.po.AfValidateScheme"%>
<%@page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%
	/** ����ѡ�б�־ **/
	String reportFlg = "0";
	
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
	}
	List<AfValidateScheme> list = (List<AfValidateScheme>)AfTemplateValiScheDelegate.findAfValidateScheme();
%>
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="reportFlg" name="FormBean" value="<%=reportFlg%>"/>
<jsp:useBean id="utilFormBean"  scope="page" class="com.cbrc.smis.form.UtilForm" ></jsp:useBean>
<html:html locale="true">
<head>
	<html:base/>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" type="text/css" href="../css/pageControl.css" />
	<link href="../../css/calendar-blue.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="../../js/prototype-1.4.0.js"></script>
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link href="../../css/tree.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="../../js/tree/tree.js"></script>
	<script type="text/javascript" src="../../js/tree/defTreeFormat.js"></script>
	<script language="javascript" src="../../js/func.js"></script>
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>
	
	<script type="text/javascript">
			/**
			 * ����ģ���ļ���׺
			 */
		 var EXT_RAQ="<%=Config.EXT_RAQ%>";
		 //ģ�����Ƽ����
		 var reptNameCheckResult = false;
		 //ģ���ż����
		 var reptIdCheckResult = false;
		 
		 function  setqdfile(){
		 	var dd = document.getElementById('reportStyle');
		 	if(dd.checked ){
		 		document.getElementById('tr_qdf').style.display='';
		 	} else{
		 		document.getElementById('tr_qdf').style.display='none';
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
			    	document.getElementById('templateName').value = fileName;			    
			    	document.getElementById("templateName").select();
			    }
		 	}
			
			/*����ļ������ļ���չ��*/
			function getFileNameAndExtendName()
			{
				var filePath = document.getElementById('zipFile').value;
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
			  var reptId = $("templateId").value;
			  var versionId = $("versionId").value;
			  var reptName = $("templateName").value;
			  if(checkObj=='reptId'&&reptId!='' && versionId !='')
			  {
			  	 checkReptId(reptId,versionId);
			  }
			  else if(checkObj=='reptName'&&reptName!='')
			  {
				  //checkReptName(reptName);
			  }
	     }
	     
	       //�ύ���-ģ��ID���汾�� 
	    function checkReptId(reptId,versionId)
	    {
	    	var url = "<%=Config.WEBROOTULR%>/template/searchTemplateVersion.do?validate=reptId&childRepId="+reptId+"&versionId="+versionId;
			var param = "radom="+Math.random();
			new Ajax.Request(url,{method: 'post',parameters:param,onComplete:validateReptIDHandler,onFailure: reptIdCheckError});
			//$("checkResult_reptId").innerHTML="<font color='#ff0000'>��Ϣ�����.....</font>";		  
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
	    
	    //��ѯʧ�ܴ������ģ��ID���汾��
	    function reptIdCheckError(request)
	    {
	         //$("checkResult_reptId").innerHTML="<img src='../../image/check_error.gif'/>ϵͳæ����д��Ϣ��δ��飡";
	          reptIdCheckResult = false;
	    }
	    function _submit(object){
	    	var fileArray = getFileNameAndExtendName();
		    //�ļ���
		    var fileName = fileArray[0];
		    //�ļ���չ��
		    var extendName = fileArray[1];
		    if(extendName.toLowerCase()!='.zip'){
			    alert('��ѡ����ȷ��zip�ļ���');
			    return false;
		    }
	    	document.getElementById("zairu").disabled=true;
	    	document.getElementById("fanhui").disabled=true;
	    	return true;
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
	<html:form method="post" action="/gznx/reportsearch/afDataImportAction" enctype="multipart/form-data" onsubmit="return _submit(this)">
		<table border="0" cellspacing="0" cellpadding="4" width="100%" align="center">
			<tr><td height="8"></td></tr>
			<tr>
				<td>
					��ǰλ�� >> ������ >> ������
				</td>
			</tr>
			<tr><td height="10"></td></tr>		
		</table>
		<table cellpadding="4" cellspacing="1" border="0" width="100%" align="center" class="tbcolor">
			<tr class="titletab">
				<th align="center" colspan="2">
					��������
				</th>
			</tr>
			<tr align="center" bgcolor="#ffffff">
				<td align="center">
					<table cellspacing="2" cellpadding="2" border="0" width="60%" align="center">
						<TR bgcolor="#ffffff" height="80">
							<TD>
								��ѡ��<B>ZIP</B>�ļ���
							</TD>
							<TD>
								<INPUT id="zipFile" class="input-text" type="file" size="30" name="zipFile" id="zipFile">
								
							</TD>
						</TR>
						<TR>
							<TD align="center" colspan="2">
								<input id="zairu" class="input-button" type="submit" name="save" value=" ���� "  id="subButton">&nbsp;&nbsp;
								<input id="fanhui" class="input-button" type="button" value=" ���� " onclick="location.href='<%=Config.WEBROOTULR %>/afreportDefine.do'" >
							</TD>
						</TR>
					</table>
				</td>
			</tr>
		</table>
	</html:form>
</body>
</html:html>
