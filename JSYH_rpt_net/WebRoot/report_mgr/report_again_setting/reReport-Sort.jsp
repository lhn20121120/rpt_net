<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm" />
<jsp:useBean id="utilFormOrg" scope="page" class="com.fitech.net.common.UtilForm" />

<html:html locale="true">
	<head>
		<html:base/>		
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">		
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
		<script language="javascript">
			/**
			 * �����ر��¼�
			 */
			 function _add(){
			 	parent.contents.location="<%=request.getContextPath()%>/report_mgr/report_again_mgr/report_again_add.jsp";
			 }
		</script>
	</head>
<jsp:include page="../../calendar.jsp" flush="true">
  <jsp:param name="path" value="../../"/>
</jsp:include>
	<body topmargin="0" marginheight="0" leftmargin="0" marginwidth="0">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<html:form action="/selReportAgainSetting" method="post">
	  <table border="0" width="98%" align="center">
		 <tr><td height="8"></td></tr>
		 <tr>
			 <td>��ǰλ�� >> 	���ݲ�ѯ >> �����ر��趨</td>
		</tr>
    	</table>
		<table cellspacing="0" cellpadding="0" border="0" width="98%">
			<tr>
				<td colspan="3"></td>
			</tr>
			<tr>
				<td colspan="3">
					<table border="0" cellspacing="0" cellpadding="2" width="100%">
						<tr>
							<td>
							<table border="0" cellspacing="0" cellpadding="0" width="100%">
							<tr>		
							<td>�������У�<html:text property="orgName" size="20" styleClass="input-text"/></td>
							<td>
								���ͷ�Χ��<html:select property="childRepId">
									<html:option value="0">�����б������У�</html:option>
									<html:optionsCollection name="utilFormOrg" property="orgCls"/>
								</html:select>
							</td>
							<td>
								ʱ�䣺<html:text property="startDate" maxlength="10" styleClass="input-text" size="10" onclick="return showCalendar('startDate', 'y-mm-dd');" readonly="true"/>
								-
								<html:text property="endDate" maxlength="10" styleClass="input-text" size="10" onclick="return showCalendar('endDate', 'y-mm-dd');" readonly="true"/>
							</td>
							</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td>
							<table border="0" cellspacing="0" cellpadding="0" width="100%">
							<tr>
							<td>ģ�壺<html:select property="repName">
										<html:option value="0">�����б������ƣ�</html:option>
									    <html:optionsCollection name="utilForm" property="repList"/>
								</html:select>
							</td>
							<td align="right">
								<input type="submit" class="input-button" value=" �� ѯ " >
							</td>
							</tr>
							</table>
							</td>
						</tr>
					</table>
					
				</td>
			</tr>
		</table>
		</html:form>
		<table width="98%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
			<tr class="titletab">
				<th width="45%" align="center">��������</th>
				<th width="15%"  align="center">�ϱ�ʱ��</th>
				<th width="20%"  align="center">����</th>
				<th width="10%"  align="center">״̬</th>
				<!-- 
				<th width="15%"  align="center">��ϸ��Ϣ</th>
				-->
				<th width="10%"  align="center">�ر�</th>
			</tr>
			<logic:present name="Records" scope="request">
				<logic:iterate id="item" name="Records">
		    <TR>
				<TD  bgcolor="#ffffff" align="center">
					<a href="<%=request.getContextPath()%>/servlets/ReadPDFReportServlet?repInId=<bean:write name='item' property='repInId'/>" target="_blank">
						<bean:write name="item" property="repName"/>
					</a>
				</TD>
				<TD bgcolor="#ffffff" align="center">
					<bean:write name="item" property="reportDate"/>
				</TD>
				<TD bgcolor="#ffffff" align="center">
					<bean:write name="item" property="orgName"/>
				</TD>
				<TD bgcolor="#ffffff" align="center">
					<font color="red">���δͨ��</font>
				</TD>
				<!-- 
				<TD bgcolor="#ffffff" align="center">
					��ϸ��Ϣ
				</TD>
				-->
				<TD bgcolor="#ffffff" align="center">
					<a href="../../editReportAgainSetting.do?repInId=<bean:write name="item" property="repInId"/>" >�ر�</a>
				</TD>
			</TR>
			</logic:iterate>
		</logic:present>
		
		<logic:notPresent name="Records" scope="request">
			<tr align="left" >
				<td bgcolor="#ffffff" colspan="5">
					�������ر��趨��¼
				</td>
			</tr>
		</logic:notPresent>			
		</table>
		<TABLE  cellSpacing="0" cellPadding="0" width="98%" border="0">
			<TR>
				<TD>
					<jsp:include page="../../apartpage.jsp" flush="true">
				  		<jsp:param name="url" value="../../selReportAgainSetting.do"/>
				  	</jsp:include>
				</TD>
			</tr>
		</TABLE>
		</body>
</html:html>