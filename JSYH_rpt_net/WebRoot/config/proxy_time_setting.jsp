<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
	<head>
	<html:base/>
		<title>������ϵ�趨</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../css/common.css" type="text/css" rel="stylesheet">
		<jsp:include page="../calendar.jsp" flush="true">
		  <jsp:param name="path" value="../"/>
		</jsp:include>
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
		<table cellspacing="0" cellpadding="2" border="0" width="85%">
			<TBODY>
				<tr>
					<td colspan="3" height="5"></td>
				</tr>
				<tr background="../image/search_title_bg.gif">
					<td>
						��ǰλ�� >> �����趨 >> ����ʱ���趨
					</td>
				</tr>
				<tr>
					<td colspan="3" height="10"></td>
				</tr>
			</TBODY>
		</table>
		<html:form action="" method="POST">
	
		<table border="0" width="50%">
			<tr>
				<td>
					<table cellSpacing="1" cellPadding="4" width="100%" border="0" align="center" class="tbColor">
						<tr >
							<td width="100%" align="center" colspan="2" ><strong>����ʱ���趨</strong></td>
						</tr>
						<tr  bgcolor="#FFFFFF">
							<td width="30%" align="center">�ϱ���������:</td>
							<td width="70%" align="center">
								<logic:present name="" scope="request">
									<logic:notEmpty name="" property="">
										<bean:write name="" property=""/>
									</logic:notEmpty>
									<logic:empty name="" scope="request">
										��
									</logic:empty>
								</logic:present>
							</td>
						</tr>
						<tr  bgcolor="#FFFFFF">
							<td width="30%" align="center">��������:</td>
							<td width="70%" align="center">
								<logic:present name="" scope="request">
									<logic:notEmpty name="" property="">
										<bean:write name="" property=""/>
									</logic:notEmpty>
									<logic:empty name="" scope="request">
										��
									</logic:empty>
								</logic:present>
							</td>
						</tr>
						<tr  bgcolor="#FFFFFF">
							<td width="30%" align="center">��ʼʱ��:</td>
							<td width="70%" align="center">
								<logic:present name="" scope="request">
									<input type="text" class="input-text" name="startDate" value="<bean:write name="" property=""/>" readonly/><html:img border="0" src="../image/calendar.gif" onclick="return showCalendar('startDate', 'y-mm-dd');"/>
								</logic:present>
								<logic:notPresent name="" scope="request">
									<html:text property="startDate" size="10" styleClass="input-text" style="text" readonly="true" /><html:img border="0" src="../image/calendar.gif" onclick="return showCalendar('startDate', 'y-mm-dd');"/>
								</logic:notPresent>
							</td>
						</tr>
						<tr  bgcolor="#FFFFFF">
							<td width="30%" align="center">��ֹʱ��:</td>
							<td width="70%" align="center">
								<logic:present name="" scope="request">
									<input type="text" class="input-text" name="endDate" value="<bean:write name="" property=""/>" readonly/><html:img border="0" src="../image/calendar.gif" onclick="return showCalendar('endDate', 'y-mm-dd');"/>
								</logic:present>
								<logic:notPresent name="" scope="request">
									<html:text property="endDate" size="10" styleClass="input-text" style="text" readonly="true" /><html:img border="0" src="../image/calendar.gif" onclick="return showCalendar('endDate', 'y-mm-dd');"/>
								</logic:notPresent>
								
							</td>
						</tr>			
					</table>			
				</td>
			</tr>
			<tr>
				<td>
					<table border="0" width="100%" align="center">
						<tr bgcolor="#ffffff">
							<td height="1">
							<td>
						</tr>
						<tr bgcolor="#ffffff">
							<td align="right"  colspan="2">
								<html:submit styleClass="input-button" value="����"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
			</html:form>
		</center>
	</body>
</html:html>