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
				当前位置 &gt;&gt; 数据统计&gt;&gt; ETL监控 &gt;&gt;查看出错日志
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
								报表名称
							</td>
							<td align="center">
										<bean:write name="jobLog" property="repName"/>
							</td>
						</TR>
						<tr class="titletab" bgcolor="#FFFFFF">
							<td width="25%" align="center" valign="middle">
								上报子行
							</td>
							<td align="center">
										<bean:write name="jobLog" property="id.org.orgName"/>
						   </td>
						</tr>
						<tr class="titletab" bgcolor="#FFFFFF">
							<td width="25%" align="center" valign="middle">
								报表时间
							</td>
							<td align="center"> 
							<bean:write name="jobLog" property="id.year"/>
										-									
							<bean:write name="jobLog" property="id.term"/>
							</td>
					    </tr>
					    <tr class="titletab" bgcolor="#FFFFFF"> 
							<td width="25%" align="center" valign="middle">
								ETL抽取开始时间
							</td>
							<td align="center">
										<bean:write name="jobLog" property="actStTm"/>
							</td>
						</tr>
						<tr class="titletab" bgcolor="#FFFFFF">
							<td width="25%" align="center" valign="middle">
								ETL抽取结束时间
							</td>
							<td align="center">
										<bean:write name="jobLog" property="actEndTm"/>
							</td>
						</tr>
						<tr class="titletab" bgcolor="#FFFFFF">
							<td width="25%" align="center" valign="middle">
								日志
							</td>
							<td align="center">
								<bean:write name="jobLog" property="jobLog"/>
							</td>												
						<tr>		
						<tr class="titletab" bgcolor="#FFFFFF">
							<td width="25%" align="center" valign="middle">
								状态
							</td>
							<td align="center">
								<bean:write name="jobLog" property="jobSts"/>
							</td>												
						<tr>		
			</logic:present>
			<logic:notPresent name="jobLog" scope="request">
				<tr class="titletab" bgcolor="#FFFFFF">
					<td align="center" colspan="2">
						对不起，出错了，您要查看的数据不存在！
					</td>
				</tr>
			</logic:notPresent>		
						
	</table>
</body>
</html:html>
