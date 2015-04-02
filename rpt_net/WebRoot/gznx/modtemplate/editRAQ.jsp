<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.util.FitechUtil"%>
<%@ page import="com.cbrc.smis.adapter.StrutsMChildReportDelegate"%>
<%@ page import="com.cbrc.smis.common.Config"%>

<jsp:useBean id="utilFormBean" scope="page" class="com.cbrc.smis.form.UtilForm" />
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config"/>
<%
	Integer reportStyle = null;
	if(request.getAttribute("reportStyle")!=null) 
		reportStyle=Integer.valueOf(request.getAttribute("reportStyle").toString());

%>
<html:html locale="true">
	<head>
		<html:base/>
		<title>�޸�ģ����Ϣ</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">		
		<script language="javascript" src="../../js/comm.js"></script>
		<script language="javascript" src="../../js/func.js"></script>
		<jsp:include page="../../calendar.jsp" flush="true">
			<jsp:param name="path" value="../../" />
		</jsp:include>
		<script language="javascript">
		 var EXT_RAQ='<%=Config.EXT_RAQ%>';
			/**
			 * ���ύ�¼�
			 */
			 function _submit(form){
				 	if(form.templateFile.value!="")
				  	{				 	
					 	if(getExt(form.templateFile.value)!=EXT_RAQ)
					 	{
					 	 	alert("������RAQ��ʽ��ģ���ļ���");
					 		return false;
					 	}
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
			 /**
			  * �����¼�
			  */
			  function _back(){
				  var childRepId = document.getElementById('childRepId').value;
				  var versionId = document.getElementById('versionId').value;
			  //yql 1.14
			  	window.location="<%=request.getContextPath()%>/template/viewTemplateDetail.do?childRepId="+ childRepId +"&versionId="+ versionId +"&bak2=2";
			  	
			  }
			   function  setqdfile(){
			 	var dd = document.getElementById('reportStyle');
			 	if(dd.checked ){
			 		document.getElementById('tr_qdf').style.display='';
			 	} else{
			 		document.getElementById('tr_qdf').style.display='none'
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
	<html:form method="post" action="/template/updateRAQ" enctype="multipart/form-data" onsubmit="return _submit(this)">
		 <logic:present name="mChildReportForm" scope="request">
		
		<table border="0" cellspacing="0" cellpadding="4" width="100%" align="center">
			<tr><td height="8"></td></tr>
			<tr>
				<td>
					��ǰλ�� >> ������� >> ��������� >> ģ���޸�>> �޸ı���ģ����Ϣ
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
								<INPUT id="templateFile" class="input-text" type="file" size="30" name="templateFile" >
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
						<tr bgcolor="#ffffff">
							<td >
								�Ƿ��嵥ʽ����
							</td>
							<td>
							<html:checkbox property="reportStyle" value="2" onclick="setqdfile()"></html:checkbox>								
							</td>
						</tr>
						<TR bgcolor="#ffffff">
							<TD>
								ģ���ţ�
							</TD>
							<TD>								
								<html:text property="childRepId" size="10" maxlength="6" readonly="true" styleClass="locked-text" />
								<font color="red">*�����޸�</font>
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								ģ��汾��
							</TD>
							<TD>								
								<html:text property="versionId" size="10" maxlength="6" readonly="true" styleClass="locked-text" />
								<font color="red">*�����޸�</font>
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								ģ�����ƣ�
							</TD>
							<TD>
								<html:text property="reportName" size="50" maxlength="250" styleClass="input-text" />
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								ģ�����ͣ�
							</TD>
							<TD>
								<html:select property="repTypeId" ><html:optionsCollection name="utilFormBean" property="repTypes"/>
								</html:select>
							</TD>
						</TR>

						<TR bgcolor="#ffffff">
							<TD>
								���ҵ�λ��
							</TD>
							<TD>
								<html:select property="curUnit"><html:optionsCollection name="utilFormBean" property="curUnits" />
								</html:select>
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
						<tr bgcolor="#FFFFFF">
							<td align="center">
								<input type="submit" value="ȷ��" class="input-button" id="subButton">&nbsp;&nbsp;
								<input type="button" value="����" class="input-button" onclick="_back()">
							</td>
						</tr>
					</table>

					<input type="hidden" name="reportStyle" value="<%=reportStyle%>">
					
				</td>
			</tr>			
		</table>
		</logic:present>
		</html:form>
		<br><br><br><br><br>
		<logic:notPresent name="mChildReportForm" scope="request">
			��Ǹ!����δ֪����!
		</logic:notPresent>		
	</body>
</html:html>