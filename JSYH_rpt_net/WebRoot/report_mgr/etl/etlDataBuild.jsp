<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.security.Operator" %>
<%@ page import="com.cbrc.smis.common.Config" %>
<%
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
//	String childRepSearchPodedom = operator != null ? operator.getChildRepSearchPopedom() : "";
	String orgId = operator != null ? operator.getOrgId() : "";
%>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm" />
<jsp:useBean id="utilSubOrgForm" scope="page" class="com.fitech.net.form.UtilSubOrgForm" />
<jsp:setProperty property="orgId" name="utilSubOrgForm" value="<%=orgId%>"/>

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

		var yearObj = document.getElementById('year');
		var monthObj = document.getElementById('month');
		var isOrgLevelObj = document.getElementById('isOrgLevel');
		
		/*�Ƿ���ȫ������*/
		var isAllRep;
		/*�Ƿ�ֻ�������*/
		var isOrgLevel=isOrgLevelObj.value;
		var childRepId = childRepIdObj.value;
		var year = yearObj.value;
		var month = monthObj.value;
		
 
				 
				if(year==""  ||  month==""){
					alert("�����뱨��ʱ��!");
					return ;
				}
				if(isNaN(year)){ 
				   alert("��������ȷ�ı���ʱ�䣡"); 
				   return ; 
				}
				if(isNaN(month)){ 
				   alert("��������ȷ�ı���ʱ�䣡"); 
				   return ; 
				}
				 
				if(month <1 || month >12){
					alert("��������ȷ�ı���ʱ�䣡");
					return ;
				}
		for(var i=0;i<isAllRepObj.length;i++)
		{
		 	if(isAllRepObj[i].checked==true) 
		 	{
		 		isAllRep = isAllRepObj[i].value;
		 		
		 	}
		}

		if(isAllRep=='false' && childRepId=="")
		{	  	
			alert("�����뱨����!\n");
			childRepIdObj.focus();
			return;
		}
	
		var url = "<%=request.getContextPath()%>/template/protect/etlDataBuild.do?isAllRep=" +isAllRep+"&childRepId="+childRepId+"&year="+year+"&month="+month+"&isOrgLevel="+isOrgLevel;
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
					����ȫ��EXCEL &nbsp;&nbsp;&nbsp;&nbsp;
					<INPUT type="radio" name="isAllRep" value="false" onclick="displayReportInfo('true')" />
					���ɵ���EXCEL&nbsp;&nbsp;&nbsp;&nbsp;
					<!--<input type="checkbox" id="isSepOrg" name="isSepOrg" checked>-->
					���ɻ���:
					<select name="isOrgLevel" >
						<%
						  if(com.fitech.net.adapter.StrutsOrgNetDelegate.isMaxOrgNet(orgId))
						 {
							 %>
									<option value="allOrg">
										----���л���----
									</option>
							 <%
						
						
						}
						
						List orglist = utilSubOrgForm.getSubCurrOrgs();
							if (orglist != null && orglist.size() != 0)
							{
								for (int i = 0; i < orglist.size(); i++)
								{
									org.apache.struts.util.LabelValueBean item = (org.apache.struts.util.LabelValueBean) orglist
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
			<tr id="tr_sigleReport" align="center" style="display:none" bgcolor="#ffffff">
				<td>
					��������:
					<select name="childRepId" style="WIDTH: 400px">
						<%List list = utilForm.getReportList();
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
					����ʱ��:&nbsp;���
<%--					<input class="input-text" name="reptDate" id="reptDate" type="text" class="input-text">--%>
<%--					<img border="0" src="../../image/calendar.gif" onclick="return showCalendar('reptDate', 'y-mm-dd');" />--%>
						<input type="text" name="year" id="year" maxlength="4" size="10" value="" class="input-text">
						&nbsp;����
						<input type="text" name="month" id="month" maxlength="2" size="5" value="" class="input-text">
						
				</td>
			</tr>

<%--			<tr align="center" bgcolor="#ffffff">--%>
<%--				<td>--%>
<%--					�汾��:--%>
<%--					<input class="input-text" name="version" id="version" type="text" class="input-text" value="0690" />--%>
<%--				</td>--%>
<%--			</tr>--%>
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
