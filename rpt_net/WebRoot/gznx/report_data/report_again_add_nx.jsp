<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String repInIds = request.getParameter("repInIds");
	String flag = request.getParameter("flag");
	String templateId = request.getParameter("templateId");
	/**����WebLogic����Ҫ�Ƚ���ת�룬�ٽ��в�������*/
	//String repName = request.getParameter("repName")!=null ? new String(request.getParameter("repName").getBytes("iso-8859-1"),"gb2312") : null;
	/**����WebSphere��Ҫ����ת�룬ֱ����Ϊ��������*/
	String repName = request.getParameter("repName");
	String date = request.getParameter("date");
	String bak1 = request.getParameter("bak1");
	String orgId = request.getParameter("orgId");
	String repFreqId = request.getParameter("repFreqId");
	String curPage = request.getParameter("curPage");
	String allFlags = request.getParameter("allFlags");
	String workTaskTerm = request.getParameter("workTaskTerm");
	String workTaskOrgId = request.getParameter("workTaskOrgId");
	String workTaskTemp = request.getParameter("workTaskTemp");
%>
<html:html locale="true">
<head>
	<html:base />
	<title>
		�����ر������ˣ�
	</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript">
		/**
		 * �����¼�
		 */			
		function _goBack(){
			var qry = "&templateId=<%=templateId%>" + 
					  "&repName=<%=repName%>" + 
					  "&bak1=<%=bak1%>" +
					  "&date=<%=date%>" +
					  "&orgId=<%=orgId%>" + 
					  "&repFreqId=<%=repFreqId%>" +
					  "&curPage=<%=curPage%>"+
					  "&workTaskTerm=<%=workTaskTerm%>"+
					  "&workTaskOrgId=<%=workTaskOrgId%>"+
					  "&workTaskTemp=<%=workTaskTemp%>";
						
			window.location="<%=request.getContextPath()%>/dateQuary/manualCheckRepNXRecheck.do?" + qry;				
		}
		
		/**
		 * ���ύ��֤�¼�
		 */
		function _submit(form){
			if(isEmpty(form.cause.value)==true){
				alert("�������ر�ԭ��!\n");
				form.cause.focus();
				return false;
			}
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
	<center>
		<table cellspacing="0" cellpadding="0" border="0" width="98%">
			<tr>
				<td height="5"></td>
			</tr>
			<tr>
				<td>
					��ǰλ�� &gt;&gt; ������ &gt;&gt; ������� &gt;&gt; �ر��趨
				</td>
			</tr>
			<tr>
				<td height="5"></td>
			</tr>
		</table>
		<br>
		<br>
		<br>
		<br>
		<html:form action="/dateQuary/saveCheckRepRecheckNX" method="post" onsubmit="return _submit(this)">
			<table cellSpacing="1" cellPadding="4" border="0" width="80%" class="tbcolor">
				<tr height="30">
					<td background="../../image/barbk.jpg" align="center" colspan="2">
						�ر��趨
					</td>
				</tr>
				<TR bgcolor="#FFFFFF">
					<TD>
						�ر�ԭ��
					</TD>
					<td>
						<html:textarea property="cause" cols="80" rows="10"></html:textarea>
					</td>
				</tr>
			</table>
			<table cellSpacing="0" cellPadding="0" border="0" width="80%">
				<tr>
					<td height="10"></td>
				</tr>
				<tr>
					<td align="center">
						<input type="button" class="input-button" onclick="_goBack()" value=" �� �� ">
						&nbsp;
						<html:submit value=" ���� " styleClass="input-button" />
					</td>
				</tr>
			</table>
			<input type="hidden" name="repInIds" value="<%=repInIds%>">
		  	<input type="hidden" name="flag" value="<%=flag%>">
		  	<input type="hidden" name="templateId" value="<%=templateId%>">
		  	<input type="hidden" name="repName" value="<%=repName%>">
		  	<input type="hidden" name="date" value="<%=date%>">
		  	<input type="hidden" name="bak1" value="<%=bak1%>">
		  	<input type="hidden" name="orgId" value="<%=orgId%>">
		  	<input type="hidden" name="repFreqId" value="<%=repFreqId%>"> 
		  	<input type="hidden" name="allFlags" value="<%=allFlags%>"> 
		  	<input type="hidden" name="curPage" value="<%=curPage%>">
		  	<input type="hidden" name="workTaskTerm" value="<%=workTaskTerm%>">
		  	<input type="hidden" name="workTaskOrgId" value="<%=workTaskOrgId%>">
		  	<input type="hidden" name="workTaskTemp" value="<%=workTaskTemp%>">
		  	<input type="hidden" name="updateFlag" value="<%=request.getParameter("updateFlag")%>"/>
	</html:form>
	</center>
</body>
</html:html>
