<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%
	String repId = request.getParameter("repId") != null ? request.getParameter("repId") : "";
	String allFlags = request.getParameter("allFlags") != null ? request.getParameter("allFlags") : "";
	String templateId = request.getParameter("templateId") != null ? request.getParameter("templateId") : "";
	String repName = request.getParameter("repName") != null ? request.getParameter("repName") : "";
	String bak1 = request.getParameter("bak1") != null ? request.getParameter("bak1") : "";
	String repFreqId = request.getParameter("repFreqId") != null ? request.getParameter("repFreqId") : "";
	String date = request.getParameter("date") != null ? request.getParameter("date") : "";
	String orgId = request.getParameter("orgId") != null ? request.getParameter("orgId") : "";
	String curPage = request.getParameter("curPage") != null ? request.getParameter("curPage") : "";
%>
<html:html locale="true">
	<head>
		<html:base/>
		<title>�����ر�</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">		
		<link href="../../css/common.css" type="text/css" rel="stylesheet">
		<script language="javascript">
			/**
			 * �����ϼ�ҳ���¼�
			 */
			 function _back(){
			 	var _date = "<%=date%>";
			 	
			 	var qry = "allFlags=<%=allFlags%>" + 
			 			  "&templateId=<%=templateId%>" + 
			 			  "&repName=<%=repName%>" + 
			 			  "&bak1=<%=bak1%>" + 
			 			  "&repFreqId=<%=repFreqId%>" + 
			 			  "&orgId=<%=orgId%>" + 
			 			  "&curPage=<%=curPage%>";
			 	
			  	if(_date != "")
			  		qry += "&date=" + _date;
			  			
			 	window.location="<%=request.getContextPath()%>/report/repNXSearch.do?" + qry;			 		
			 }
			 
			 /**
			  * ���ύ��֤
			  */
			  function _validate(form){
			  	if(form.cause.value==""){
			  		alert("�������ر���ԭ��!\n");
			  		return false;
			  	}
			  	document.getElementById("smSave").disabled = true;
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
	
	
	<table cellspacing="0" cellpadding="0" border="0" width="98%">
		<tr>
			<td height="5"></td>
		</tr>
		<tr>
			 <td>��ǰλ�� &gt;&gt; ������� &gt;&gt; �ر����� &gt;&gt; ǿ���ر�����</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
	
	<br><br>
	
		<html:form action="/report/reportAgainNX"  method="post" onsubmit="return _validate(this)">
		<table cellSpacing="1" cellPadding="4" border="0" width="80%" class="tbcolor">
			<tr height="30">
				<td background="../../image/barbk.jpg" align="center" colspan="2">�����ر�����</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%">��������</td>
				<td width="30%">
					<logic:present name="ReportInForm" scope="request">
						<bean:write name="ReportInForm" property="repName"/>
					</logic:present>
				</td>
			</tr>
			<TR bgcolor="#FFFFFF">
				<TD>�ϱ�������</TD>
				<TD>
					<logic:present name="ReportInForm" scope="request">
						<bean:write name="ReportInForm" property="orgName"/>
					</logic:present>
				</TD>				
			</TR>
			
			<TR bgcolor="#FFFFFF">	
				<TD>����������</TD>
				<TD>
					<logic:present name="ReportInForm" scope="request">
						<bean:write name="ReportInForm" property="year"/>��<bean:write name="ReportInForm" property="term"/>��<bean:write name="ReportInForm" property="day"/>��
					</logic:present>
				</TD>
			</TR>
			
			<TR bgcolor="#FFFFFF">
				<TD>�ر�ԭ��</TD>
				<td><html:textarea  property="cause"  cols="80" rows="10"/></td>
			</tr>
		</table>
		
		<table cellSpacing="0" cellPadding="0" border="0" width="80%">
			<tr><td height="10"></td></tr>
			<tr>
				<td align="center">
					<html:submit styleId="smSave" value="��  ��" styleClass="input-button"/>&nbsp;
					<input type="button" class="input-button" onclick="_back()" value="��  ��">
					
				</td>
			</tr>
		</table>
		
		<input type="hidden" name="repId" value="<%=repId%>"/>
		<input type="hidden" name="allFlags" value="<%=allFlags%>"/>
		<input type="hidden" name="templateId" value="<%=templateId%>"/>
		<input type="hidden" name="repName" value="<%=repName%>"/>
		<input type="hidden" name="bak1" value="<%=bak1%>"/>
		<input type="hidden" name="repFreqId" value="<%=repFreqId%>"/>
		<input type="hidden" name="orgId" value="<%=orgId%>"/>
		<input type="hidden" name="curPage" value="<%=curPage%>"/>
		<%if(date != null && !date.equals("")){%>
			<input type="hidden" name="date" value="<%=date%>"/>
		<%}%>
	</html:form>
	</body>
</html:html>