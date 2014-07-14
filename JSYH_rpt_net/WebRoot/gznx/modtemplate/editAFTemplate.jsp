<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
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
	
	%>
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="reportFlg" name="FormBean" value="<%=reportFlg%>"/>
<jsp:useBean id="utilFormBean"  scope="page" class="com.cbrc.smis.form.UtilForm" ></jsp:useBean>
<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" type="text/css" href="../css/pageControl.css" />
	<link href="../../css/calendar-blue.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="../../js/prototype-1.4.0.js"></script>
	<script type="text/javascript" src="../../calendar/calendar.js	"></script>
	<script type="text/javascript" src="../../calendar/calendar-cn.js"></script>
	<script language="javascript" src="../../calendar/calendar-func.js"></script>
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link href="../../css/tree.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="../../js/tree/tree.js"></script>
	<script type="text/javascript" src="../../js/tree/defTreeFormat.js"></script>
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript" src="../../js/jquery-1.4.2.js"></script>
	
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
		 	if(form.reportFile.value!="" && getExt(form.reportFile.value)!=EXT_RAQ)
		 	{
		 	 	alert("������RAQ��ʽ��ģ���ļ���");
		 		return false;
		 	}
			if(form.templateName.value=="")
			{
				alert("������ģ�����ƣ�");
				form.templateName.focus();
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
			document.getElementById("subButton").disabled=true;
			return true;
		 }
		 
		 
		 //����ļ�ʱ�Զ����ļ�������������
			function autoSetName()
			{
			    var fileArray = getFileNameAndExtendName();
			    //�ļ���չ��
			    var extendName = fileArray[1];
	            
	            if(extendName!=".raq")
	            {
	            	alert("������raq��ʽ��ģ���ļ���");
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
			
			function _back(tenplateId,versionId){
				window.location="<%=request.getContextPath()%>/viewAFTemplateDetail.do?templateId="+tenplateId+"&versionId="+versionId+"&bak2=2";
			}
			
			function  setqdfile(){
			 	var dd = document.getElementById('reportStyle');
			 	if(dd.checked ){
			 		document.getElementById('tr_qdf').style.display='';
			 	} else{
			 		document.getElementById('tr_qdf').style.display='none'
			 	}
		 	}

		 	function setisReport(){
			 	var isReport = document.getElementById('isReport');
			 	if(isReport.checked ){
			 		isReport.value="1";
			 	}else
				 	isReport.value="2";
		 	}

		 	window.onload = function(){
			 	var reportStyle = "${afTemplateForm.reportStyle}";
			 	if(reportStyle=="2"){//�嵥����
			 		document.getElementById('tr_qdf').style.display='';
			 		
			 		//var newTr_1 = $("<tr bgcolor='#ffffff'><td>�½����ݱ�</td><td><input type='checkbox' name='dropQDTableCheck' value='1' onclick='isDropQD(this)' /></td></tr>");
			 		//$("#isQDTR").after(newTr_1);
				}else
					document.getElementById('tr_qdf').style.display='none'
			}

			function isDropQD(obj){
				if($(obj).attr("checked")){
					if($("#reportFile").val()!="" && $("#qdreportFile").val()!="")
						$(obj).attr("checked",true);
					else{
						alert("��ȷ��ģ����Ϣ����");
						$(obj).attr("checked",false);
					}
				}
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
	<html:form method="post" action="/gznx/afTemplateEdit.do?bak2=update" enctype="multipart/form-data" onsubmit="return _submit(this)">
		
		<table border="0" cellspacing="0" cellpadding="4" width="100%" align="center">
			<tr><td height="8"></td></tr>
			<tr>
				<td>
					��ǰλ�� >> ������� >> �޸ı���ģ��
				</td>
			</tr>
			<tr><td height="10"></td></tr>		
		</table>
		<table cellpadding="4" cellspacing="1" border="0" width="100%" align="center" class="tbcolor" id="addTable">
			<tr class="titletab">
				<th align="center" colspan="2">
					�޸ı���ģ��
				</th>
			</tr>
			<tr align="center" bgcolor="#ffffff">
				<td align="center">
					<table cellspacing="2" cellpadding="2" border="0" width="70%" align="center">
						<TR bgcolor="#ffffff">
							<TD>
								��ѡ��ģ���ļ���
							</TD>
							<TD>
								<INPUT id="reportFile" class="input-text" type="file" size="30" name="reportFile" id="reportFile">
								�����Բ�ѡ��
							</TD>
						</TR>
						<TR id="tr_qdf" bgcolor="#ffffff" style="display:none">
							<TD>								
							</TD>
							<TD>
								<INPUT id="qdreportFile" class="input-text" type="file" size="30" name="qdreportFile" id="qdreportFile" />
								
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								ģ�����ƣ�
							</TD>
							<TD>
								<html:text property="templateName" size="30" maxlength="250" styleClass="input-text" styleId="templateName" />
								<html:hidden property="versionId" />
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								ģ���ţ�
							</TD>
							<TD>								
								<html:text property="templateId" size="10" maxlength="6" readonly="true" styleClass="input-text" styleId="templateId" style="background-color:#E6E4E4" />
							</TD>
						</TR>	
						<TR bgcolor="#ffffff">
							<TD>
								�汾�ţ�
							</TD>
							<TD>								
								<html:text property="versionId" size="10" maxlength="6" readonly="true" styleClass="input-text" styleId="versionId" style="background-color:#E6E4E4" />
							</TD>
						</TR>						
						<tr bgcolor="#ffffff">
							<td >
								�������ȼ���
							</td>
							<td>
								<html:select property="priorityFlag" styleId="priorityFlag">
									<html:option value="1">һ��</html:option>
									<html:option value="2">����</html:option>
									<html:option value="3">����</html:option>
									<html:option value="4">�ļ�</html:option>
									<html:option value="5">�弶</html:option>			
								</html:select>
							</td>
						</tr>
						<% if(reportFlg.equals("2")){ %>
						<tr bgcolor="#ffffff">
							<td >
								�������Σ�
							</td>
							<td>
								<html:select property="supplementFlag" styleId="supplementFlag">
									<html:option value=""> 	</html:option>
									<html:option value="0">������</html:option>				
									<html:option value="1" >��һ��</html:option>
									<html:option value="2" >�ڶ���</html:option>								
								</html:select>
							</td>
						</tr>
						<% } %>
						<tr bgcolor="#ffffff" id="isQDTR">
							<td >
								�Ƿ��嵥ʽ����
							</td>
							<td>
								<html:checkbox property="reportStyle" value="2"  onclick="setqdfile()"></html:checkbox>	
							</td>
						</tr>
						<tr bgcolor="#ffffff">
							<td>
								�Ƿ�¼��
							</td>
							<td>
								<html:checkbox property="isReport"  value="1"  onclick="setisReport()"></html:checkbox>						
							</td>
						</tr>
						<tr bgcolor="#ffffff">
							<td >
								��ʼ���ڣ�
							</td>
							<td>
								<html:text property="startDate" size="10"  readonly="true" styleId="startDate" style="text"/>
								<img src="../../image/calendar.gif" border="0" onclick="return showCalendar('startDate', 'y-mm-dd');">						
							</td>
						</tr>
						<tr bgcolor="#ffffff">
							<td >
								��ֹ���ڣ�
							</td>
							<td>
							<html:text property="endDate" size="10"  readonly="true" styleId="endDate" style="text"/>
							<img src="../../image/calendar.gif" border="0" onclick="return showCalendar('endDate', 'y-mm-dd');">							
							</td>
						</tr>
						<tr>
						<td>
						</td>
						</tr>
						<tr>
						</tr>
						<TR>
							<TD align="center" colspan="2">
								<input type="submit" name="save" value="����ģ��" id="subButton">
								&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="button" onclick="_back('<bean:write name="afTemplateForm" property="templateId"/>','<bean:write name="afTemplateForm" property="versionId"/>')" value=" �� �� ">
							
								</td>
								<td></td>
						</TR>
					</table>
				</td>
				<% if(!reportFlg.equals("2")){ %>
				<TD width="30%">
					<div align="center">
						<b>���ַ�Χѡ��</b>
					</div>
					<br />
					<table width="100%" border="0" height="300" align="center" cellpadding="0"
						cellspacing="1" class="tbcolor">
						<tr>
						<td width="100%">
							<div  id="treeObj_org"
								style="width:100%; height:300;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;">
									<script type="text/javascript">
										<bean:write  name="afTemplateForm"  property="treeCurrContent" filter="false"/>
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
