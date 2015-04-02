<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
	<title>
		日志管理
	</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" rel="stylesheet" type="text/css">

</head>
<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table width="98%" border="0" cellspacing="0" cellpadding="0" align="center" height="216">
		<tr>
			<td height="25" id="location" colspan="2">
				<p></p>
			</td>
		</tr>
		<tr>
			<td height="123" valign="top" colspan="2">
				<div align="center">
					
					<table width="94%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
						
						<tr class="titletab" id="tbcolor">
							<th colspan="5" align="center" id="list">
								<strong>
									生成仓库文件日志列表
								</strong>
							</th>
						</tr>
						<tr align="center" class="middle">
							<td width="20%">
								日志类型
							</td>
							<td width="60%">
								日志内容
							</td>
							<td width="20%">
								发生时间
							</td>
						</tr>
							<logic:present name="Records" scope="request">
								<logic:iterate id="item" name="Records">
									<tr align="center">
										<td bgcolor="#ffffff">
											<bean:write name="item" property="logType" />
										</td>
										<td bgcolor="#ffffff">
											<bean:write name="item" property="operation" />
										</td>
										<td bgcolor="#ffffff">
											<bean:write name="item" property="logTime" />
										</td>
									</tr>
								</logic:iterate>
							</logic:present>
							<logic:notPresent name="Records" scope="request">
								<tr align="left">
									<td bgcolor="#ffffff" colspan="3">
										无日志
									</td>
								</tr>
							</logic:notPresent>
					</table>
					<table width="94%" border="0">
						<tr>
							<td height="5">
								<div align="center">
								<table width="94%" border="0">
									<TR>
										<TD width="100%">
											<jsp:include page="../apartpage.jsp" flush="true">
												<jsp:param name="url" value="viewDb2XmlLog.do" />
											</jsp:include>
										</TD>							
									</tr>
								</table>
								</div>
							</td>
						</tr>
					</table>
				</div>
			</td>
		</tr>	
	</table>
</body>
</html:html>

