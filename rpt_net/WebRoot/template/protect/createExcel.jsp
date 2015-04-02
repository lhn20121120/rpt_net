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
	<title>生成报表</title>
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
	/*创建Excel*/	
	function createExcel()
	{
		var isAllRepObj=document.getElementById('frm').isAllRep;
		var childRepIdObj = document.getElementById('childRepId');	
		var versionObj = document.getElementById('version');
		var reptDateObj = document.getElementById('reptDate');
		var isSepOrgObj = document.getElementById('isSepOrg');
		
		/*是否是全部生成*/
		var isAllRep;
		/*是否分机构生成*/
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
			alert("请输入报表编号!\n");
			childRepIdObj.focus();
			return;
		}
		if(version=="")
		{
			alert("请输入版本号!\n");
			versionObj.focus();
			return false;
		}
		if(reptDate=="")
		{
			alert("请输入报表时间!\n");
			reptDateObj.focus();
			return;
		}
		var url = "<%=request.getContextPath()%>/template/protect/createExcel.do?isAllRep=" +isAllRep+"&childRepId="+childRepId+"&version="+version+"&reptDate="+reptDate+"&isSepOrg="+isSepOrg;
		window.location=url;
	
	}
	
	/*
		显示单个报表信息
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
							<strong>报表生成</strong>
						</div>
					</div>
				</td>
			</tr>
			<tr align="center" bgcolor="#ffffff">
				<td>
					<INPUT type="radio" name="isAllRep" value="true" checked onclick="displayReportInfo('false')" />
					全部生成EXCEL &nbsp;&nbsp;&nbsp;&nbsp;
					<INPUT type="radio" name="isAllRep" value="false" onclick="displayReportInfo('true')" />
					生成单个EXCEL&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="checkbox" id="isSepOrg" name="isSepOrg" checked>分机构生成
					
				</td>
			</tr>
			<tr id="tr_sigleReport" align="center" style="display:none" bgcolor="#ffffff">
				<td>
					报表名称:
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
					报表时间:
					<input class="input-text" name="reptDate" id="reptDate" type="text" class="input-text">
					<img border="0" src="../../image/calendar.gif" onclick="return showCalendar('reptDate', 'y-mm-dd');" />
				</td>
			</tr>

			<tr align="center" bgcolor="#ffffff">
				<td>
					版本号:
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
					<input type="button" value="生 成 报 表" class="input-button" onclick="createExcel()">
				</td>
			</tr>
		</table>
	</form>


</body>
</html:html>
