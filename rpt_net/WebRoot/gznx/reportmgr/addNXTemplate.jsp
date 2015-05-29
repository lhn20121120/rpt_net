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
		 
		 //���ύ��֤	 
		 function _submit(form)
		 {
			var subButton=document.getElementById("subButton");
		  	if(form.reportFile.value=="")
		  	{
			 	alert("��ѡ��Ҫ�����ģ���ļ�!\n");
			 	return false;
			}
			else
			{
			 	if(getExt(form.reportFile.value)!=EXT_RAQ)
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
			

			if(form.templateName.value=="")
			{
				alert("������ģ�����ƣ�");
				form.templateName.focus();
				return false;
			}

			if(form.templateId.value=="")
			{
				alert("������ģ���ţ�");
				form.templateId.focus();
				return false;
			}
			if(CheckNumber(form.templateId.value) == false)  //�Ƿ��ַ�
			{
				alert("�Բ���,ģ����ֻ�������ֻ���ĸ!");
				form.templateId.focus();
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

			if(form.startDate.value=="")
			{
				alert("��������ʼʱ�䣡");
				form.startDate.focus();
				return false;
			}
			if(form.endDate.value=="")
			{
				alert("���������ʱ�䣡");
				form.endDate.focus();
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
				var filePath = document.getElementById('reportFile').value;
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
	    	var url = "<%=request.getContextPath() %>/template/searchTemplateVersion.do?validate=reptId&childRepId="+reptId+"&versionId="+versionId;
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
	<html:form method="post" action="/afReportAdd" enctype="multipart/form-data" onsubmit="return _submit(this)">
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
				<th align="center" colspan="2">
					��������ģ��
				</th>
			</tr>
			<tr align="center" bgcolor="#ffffff">
				<td align="center">
					<table cellspacing="2" cellpadding="2" border="0" width="60%" align="center">
						<tr bgcolor="#ffffff" >
							<td >
								�Ƿ��嵥ʽ����
								<html:checkbox property="reportStyle" value="2" onclick="setqdfile()"></html:checkbox>
							</td>
							<td>
								&nbsp;&nbsp;&nbsp;�Ƿ�¼��<html:checkbox property="isReport" value="1"/>
							</td>						
						</tr>
						<TR bgcolor="#ffffff">
							<TD>
								��ѡ��ģ���ļ���
							</TD>
							<TD>
								<INPUT id="reportFile" class="input-text" type="file" size="30" name="reportFile" onchange="autoSetName()">
								
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
								<html:text property="templateName" size="30" maxlength="250" styleClass="input-text" styleId="templateName" /><label id="checkResult_reptName"></label>
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								ģ���ţ�
							</TD>
							<TD>
								<html:text property="templateId" size="10" maxlength="10" styleClass="input-text" styleId="templateId" onblur="checkInfo('reptId')"/>
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								�汾ID��
							</TD>
							<TD>
								<html:text property="versionId" size="10" onblur="checkInfo('reptId')" maxlength="10" styleClass="input-text" styleId="versionId" /><label id="checkResult_reptId"></label>
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
						<% if(reportFlg.equals("2")){ %>
						<tr bgcolor="#ffffff">
							<td >
								�������Σ�
							</td>
							<td>
								<html:select property="supplementFlag">
									<option value=""> </option>
									<option value="0">������</option>					
									<option value="1">��һ��</option>
									<option value="2">�ڶ���</option>									
								</html:select>
							</td>
						</tr>
						<% }if(reportFlg.equals("3")){ %>
						<tr bgcolor="#ffffff">
							<td >
								ģ��У��:
							</td>
							<td>
								<html:select property="schemeId">
									<%for(int i = 0 ;i<list.size();i++){%>
									<option value="<%=list.get(i).getValidateSchemeId()%>"><%=list.get(i).getValidateSchemeName()%></option>
									<%} %>
								</html:select>
							</td>
						</tr>
						<%} %>

						<tr bgcolor="#ffffff">
							<td >
								��ʼ���ڣ�
							</td>
							<td>
								<html:text property="startDate" size="10" value="" readonly="true" styleId="startDate" style="text"/>
								<img src="../../image/calendar.gif" border="0" onclick="return showCalendar('startDate', 'y-mm-dd');">						
							</td>
						</tr>
						<tr bgcolor="#ffffff">
							<td >
								��ֹ���ڣ�
							</td>
							<td>
							<html:text property="endDate" size="10" value="" readonly="true" styleId="endDate" style="text"/>
							<img src="../../image/calendar.gif" border="0" onclick="return showCalendar('endDate', 'y-mm-dd');">							
							</td>
						</tr>
						<tr>
						<td>
						</td>
						</tr>
						<TR>
							<TD align="center" colspan="2">
								<input type="submit" name="save" value="����ģ��" id="subButton">&nbsp;&nbsp;
								<input type="button" value="����" onclick="location.href='<%=Config.WEBROOTULR %>/afreportDefine.do'" >
							</TD>
						</TR>
					</table>
				</td>
				<% if(!reportFlg.equals("2")){ %>
				<TD width="30%">
					<div align="center">
						<b>������ַ�Χѡ��</b>
					</div>
					<br />
					<table width="100%" border="0" height="300" align="center" cellpadding="0"
						cellspacing="1" class="tbcolor">
						<tr>
						<td width="100%">
							<div  id="treeObj_org"
								style="width:100%; height:300;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;">
									<script type="text/javascript">
										<bean:write  name="utilFormBean"  property="treeCurrContent" filter="false"/>
									    var currList= new ListTree("currList", TREE1_NODES, DEF_TREE_FORMAT,"currList");
								      	currList.init();								      	
								 	</script>
									</div>
								</td>
							</tr>
						</table>
					</TD>
					<% } %>
			</tr>
		</table>
	</html:form>
</body>
</html:html>
