<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.util.FitechUtil"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%
	String reportName = "";
	if (request.getAttribute("ReportName") != null) {
		reportName = (String) request.getAttribute("ReportName");
	} else {
		if (request.getParameter("reportName") != null){			
			reportName = request.getParameter("reportName");			
		}
	}	
	reportName = new String(reportName.getBytes("iso-8859-1"), "gb2312");
	String versionId = request.getParameter("versionId");
	String childRepId = request.getParameter("childRepId");
	String reportStyle = request.getParameter("reportStyle");
	String curPage = request.getParameter("curPage");

	if (reportName != null)
		request.setAttribute("ReportName", reportName);
	if (versionId != null)
		request.setAttribute("VersionId", versionId);
	if (childRepId != null)
		request.setAttribute("ChildRepId", childRepId);
	if (reportStyle != null)
		request.setAttribute("ReportStyle", reportStyle);
	if (curPage != null)
		request.setAttribute("curPage", curPage);
%>
<html:html locale="true">
<head>
	<html:base />
	<title>设定异常数据变化标准</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript">
		<!--
			/**
			 * 表单提交事件，验证表单的合法性
			 *
			 * @param this object 表单
			 * @return boolean 验证通过，返回true；否则，返回false
			 */
			 function _submit(form){
			 var IsDisplay=document.getElementById("isShowTextarea").style.display;
			 	if(isEmpty(form.startDate.value)==true){
			 		alert("请选择报表的起始日期!\n");
			 		return false;
			 	}
			 	if(isEmpty(form.endDate.value)==true){
			 		alert("请选择报表的结束日期!\n");
			 		return false;
			 	}
			
			 	if(isEmpty(form.endDate.value)==false){
			 		if(form.startDate.value>form.endDate.value){
			 			alert("报表的结束日期不能小于起始日期!\n");
			 			return false;
			 		}	
			 	}
			 
			 	if(IsDisplay=="block"){		
			 		var testvalue=document.getElementById("templateLogID");
				 	if(testvalue.value==""){
				 		alert("请输入日志!\n");				 	
				 		return false;
			 		}
			 	}
			 	
			 	if(confirm("你确定要发布当前的报表模板吗?")==false){
			 		return false;
			 	}
			 	
			 	
			 	
			 	return true;
			 }
			
			/**
			 * 返回事件
			 */ 
			 function _back(){
			 	 var form=document.forms['mChildRep'];
	  		 	 window.location="<%=request.getContextPath()%>/template/viewMChildReport.do?" +
	  		 		"curPage=" + form.elements['curPage'].value;
			 }
		//-->
		
		</script>

</head>
<body background="../image/total.gif">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<html:form method="post" action="/template/mod/bpfb" onsubmit="return _submit(this)">
		<table border="0" cellspacing="0" cellpadding="4" width="60%" align="center">
			<tr><td height="8"></td></tr>
			<tr>
				<td>
					当前位置 >> 报表管理 >> 模板维护 >> 模板发布
				</td>
			</tr>
			<tr><td height="10"></td></tr>		
		</table>
		<table cellSpacing="1" cellPadding="1" width="60%" border="0" class="tbcolor">
			<tr class="tbcolor1">
				<th colspan="8" align="center" id="list0" height="30" valign="middle">
					<P style="MARGIN-TOP: 0px; MARGIN-BOTTOM: -2px">
						<SPAN style="FONT-SIZE: 12pt"> <STRONG> 报表发布 </STRONG> </SPAN>
					</P>
				</th>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF">

					<table border="0" cellspacing="0" cellpadding="4" width="100%">
						<tr>
							<td colspan="2">
								报表名称：
								<logic:present name="ReportName" scope="request">
									<bean:write name="ReportName" />
<%--									<%=reportName%>--%>
								
								</logic:present>
							</td>
							<logic:present name="VersionId" scope="request">
								<input type="hidden" name="versionId" value="<bean:write name='VersionId'/>">
							</logic:present>
							<logic:present name="ChildRepId" scope="request">
								<input type="hidden" name="childRepId" value="<bean:write name='ChildRepId'/>">
							</logic:present>
							<logic:present name="ReportStyle" scope="request">
								<input type="hidden" name="reportStyle" value="<bean:write name='ReportStyle'/>">
							</logic:present>
						</tr>
						<tr>
							<td width="20%">
								开始日期：
							</td>
							<td>
								<input type="text" name="startDate" class="input-button" size="10" maxlength="10" readonly>
								&nbsp;
								<img border="0" src="../../image/calendar.gif" onclick="return showCalendar('startDate', 'y-mm-dd');">
							</td>
						</tr>
						<tr>
							<td>
								结束日期：
							</td>
							<td>
								<input type="text" name="endDate" class="input-button" size="10" maxlength="10" readonly>
								&nbsp;
								<img border="0" src="../../image/calendar.gif" onclick="return showCalendar('endDate', 'y-mm-dd');">
							</td>
						</tr>
					</table>
					<div id="isShowTextarea" style="display:none">
						<table>
							<tr></tr>
							<tr></tr>
							<tr>
								<td>
									模板发布日志:
								</td>
							</tr>
							<tr>
								<td>
									<TEXTAREA class="templateLog" name="formatTempId" id="templateLogID" rows="5" cols="78"></TEXTAREA>
								</td>

							</tr>
						</table>
					</div>
			<tr>
				<td colspan="2" align="center">
					<html:submit styleClass="input-button" value="发布" />

					&nbsp;&nbsp;
					<input type="button" value="返回" class="input-button" onclick="_back()">
				</td>
			</tr>

		</table>
		<logic:present name="curPage" scope="request">
			<input type="hidden" name="curPage" value="<bean:write name='curPage'/>">
		</logic:present>
		<logic:notPresent name="curPage" scope="request">
			<input type="hidden" name="curPage" value="0">
		</logic:notPresent>
		<%
		if (Config.TEMPLATE_PUT.get(childRepId + "_" + versionId) != null) {
		%>
		<script>
				document.getElementById("isShowTextarea").style.display="block";
				
			</script>
		<%
		}
		%>

	</html:form>

</body>
</html:html>

