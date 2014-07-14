<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm" />

<html:html locale="true">
<head>
	<html:base />
	<title>���ɱ���</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
</head>
<jsp:include page="../../calendar.jsp" flush="true">
	<jsp:param name="path" value="../../" />
</jsp:include>
<script>
	/*����Excel*/	
	function createExcel()
	{
		var isAllRepObj=document.getElementById('frm').isAllRep;
		var childRepIdObj = document.getElementById('childRepId');	
		var versionObj = document.getElementById('version');
		var reptDateObj = document.getElementById('reptDate');
		var isSepOrgObj = document.getElementById('isSepOrg');
		
		/*�Ƿ���ȫ������*/
		var isAllRep;
		/*�Ƿ�ֻ�������*/
		var isSepOrg;
		
		var childRepId = childRepIdObj.value;
		var version = versionObj.value;
		var reptDate = reptDateObj.value;
				
		for(var i=0;i<isAllRepObj.length;i++)
		{
		 	if(isAllRepObj[i].checked==true) 
		 	{
		 		isAllRep = isAllRepObj[i].value;
		 		
		 	}
		}

		if(isSepOrgObj.checked==true)
			isSepOrg = 'true';
		else
			isSepOrg = 'false';

		if(isAllRep=='false' && childRepId=="")
		{	  	
			alert("�����뱨����!\n");
			childRepIdObj.focus();
			return;
		}
		if(version=="")
		{
			alert("������汾��!\n");
			versionObj.focus();
			return false;
		}
		if(reptDate=="")
		{
			alert("�����뱨��ʱ��!\n");
			reptDateObj.focus();
			return;
		}
		var url = "<%=request.getContextPath()%>/template/protect/createExcel.do?isAllRep=" +isAllRep+"&childRepId="+childRepId+"&version="+version+"&reptDate="+reptDate+"&isSepOrg="+isSepOrg;
		window.location=url;
	
	}
	
	/*
		��ʾ����������Ϣ
	*/
	function displayReportInfo(flag)
	{
		var tr_sigleReport = document.getElementById('tr_sigleReport');
		if(flag=='true')
			tr_sigleReport.style.display='';
		else if(flag=='false')
			tr_sigleReport.style.display= 'none';
			
	
	}
      
</script>
<body background="../../image/total.gif">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<br>
	<form name="frm">
		<table border="0" cellspacing="0" cellpadding="7" width="90%" align="center" class="tbcolor">
			<tr bgcolor="#ffffff">
				<td align="center">
					<div id=location>
						<div align="center">
							<strong>��������</strong>
						</div>
					</div>
				</td>
			</tr>
			<tr align="center" bgcolor="#ffffff">
				<td>
					<INPUT type="radio" name="isAllRep" value="true" checked onclick="displayReportInfo('false')" />
					ȫ������EXCEL &nbsp;&nbsp;&nbsp;&nbsp;
					<INPUT type="radio" name="isAllRep" value="false" onclick="displayReportInfo('true')" />
					���ɵ���EXCEL&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="checkbox" id="isSepOrg" name="isSepOrg" checked>�ֻ�������
					
				</td>
			</tr>
			<tr id="tr_sigleReport" align="center" style="display:none" bgcolor="#ffffff">
				<td>
					��������:
					<select name="childRepId" style="WIDTH: 300px">
						<%List list = utilForm.getRepList();
							if (list != null && list.size() != 0)
							{
								for (int i = 0; i < list.size(); i++)
								{
									org.apache.struts.util.LabelValueBean item = (org.apache.struts.util.LabelValueBean) list
											.get(i);
				
									%>
									<option value="<%=item.getValue()%>">
										<%=item.getLabel()%>
									</option>
									<%
								}
							}
				
						%>
					</select>
				</td>
			</tr>
			<tr align="center" bgcolor="#ffffff">
				<td>
					����ʱ��:
					<input class="input-text" name="reptDate" id="reptDate" type="text" class="input-text">
					<img border="0" src="../../image/calendar.gif" onclick="return showCalendar('reptDate', 'y-mm-dd');" />
				</td>
			</tr>

			<tr align="center" bgcolor="#ffffff">
				<td>
					�汾��:
					<input class="input-text" name="version" id="version" type="text" class="input-text" value="0690" />
				</td>
			</tr>
			<tr bgcolor="#ffffff">
				<td>
					<div id=location></div>
				</td>
			</tr>
			<tr align="center" bgcolor="#ffffff">
				<td>
					<input type="button" value="�� �� �� ��" class="input-button" onclick="createExcel()">
				</td>
			</tr>
		</table>
	</form>


</body>
</html:html>
