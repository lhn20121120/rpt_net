<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" type="text/css" rel="stylesheet">
	
</head>
<body>
	<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				��ǰλ�� &gt;&gt; ����ͳ��&gt;&gt; ETL��� &gt;&gt;�鿴������־
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	
	
	<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
	<logic:present name="jobLog" scope="request">
			<tr>
				<td>
					<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
						<TR class="titletab" bgcolor="#FFFFFF">
							
							<td width="25%" align="center" valign="middle">
								��������
							</td>
							<td align="center">
										<bean:write name="jobLog" property="repName"/>
							</td>
						</TR>
						<tr class="titletab" bgcolor="#FFFFFF">
							<td width="25%" align="center" valign="middle">
								�ϱ�����
							</td>
							<td align="center">
										<bean:write name="jobLog" property="id.org.orgName"/>
						   </td>
						</tr>
						<tr class="titletab" bgcolor="#FFFFFF">
							<td width="25%" align="center" valign="middle">
								����ʱ��
							</td>
							<td align="center"> 
							<bean:write name="jobLog" property="id.year"/>
										-									
							<bean:write name="jobLog" property="id.term"/>
							</td>
					    </tr>
					    <tr class="titletab" bgcolor="#FFFFFF"> 
							<td width="25%" align="center" valign="middle">
								ETL��ȡ��ʼʱ��
							</td>
							<td align="center">
										<bean:write name="jobLog" property="actStTm"/>
							</td>
						</tr>
						<tr class="titletab" bgcolor="#FFFFFF">
							<td width="25%" align="center" valign="middle">
								ETL��ȡ����ʱ��
							</td>
							<td align="center">
										<bean:write name="jobLog" property="actEndTm"/>
							</td>
						</tr>
						<tr class="titletab" bgcolor="#FFFFFF">
							<td width="25%" align="center" valign="middle">
								��־
							</td>
							<td align="center">
								<bean:write name="jobLog" property="jobLog"/>
							</td>												
						<tr>		
						<tr class="titletab" bgcolor="#FFFFFF">
							<td width="25%" align="center" valign="middle">
								״̬
							</td>
							<td align="center">
								<bean:write name="jobLog" property="jobSts"/>
							</td>												
						<tr>		
			</logic:present>
			<logic:notPresent name="jobLog" scope="request">
				<tr class="titletab" bgcolor="#FFFFFF">
					<td align="center" colspan="2">
						�Բ��𣬳����ˣ���Ҫ�鿴�����ݲ����ڣ�
					</td>
				</tr>
			</logic:notPresent>		
						
	</table>
</body>
</html:html>
