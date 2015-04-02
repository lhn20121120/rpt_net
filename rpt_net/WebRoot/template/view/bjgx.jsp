<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.form.MCellFormuForm,com.cbrc.smis.util.FitechUtil"%>

<html:html locale="true">
<%
	String reportName = "";
	if (request.getAttribute("ReportName") != null) {
		reportName = (String) request.getAttribute("ReportName");
	} else {
		if (request.getParameter("reportName") != null)
			//reportName=FitechUtil.getParameter(request,"reportName");
			reportName = request.getParameter("reportName");

	}
%>
<head>
	<html:base /> 
	<title>���ڡ�����ϵ�趨</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link href="../../css/easyui.css" rel="stylesheet" type="text/css"/>
	<link href="../../css/table.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="../../js/jquery-1.4.4.min.js"></script>
	<script language="javascript">
		function goBack()
		{
			window.location = "./viewTemplateDetail.do?childRepId=<bean:write name="ChildRepId"/>&reportName=<bean:write name="ReportName"/>";
		}
	</script>
	<script src="../../js/jquery.easyui.min.js" type="text/javascript"></script>
	
</head>
<body background="../../image/total.gif">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
					alert("<bean:write name='Message'/>");
				</script>
		</logic:greaterThan>
	</logic:present>
	<p class="suoyinlan" align="left">��ǰλ��<span>&gt;&gt;</span>�������<span>&gt;&gt;</span>���������<span>&gt;&gt;</span>ģ��鿴<span>&gt;&gt;</span>�鿴���ڱ���ϵ
	<div class="easyui-tabs">
	<div title="���У��" style="padding:10px 20px">
				<table border="0" cellspacing="0" cellpadding="4" width="95%" align="center">
		<tr><td height="8"></td></tr>
			
			<tr><td height="10"></td></tr>		
		<tr>
			<td>

				<table id="tbl" border="0" cellpadding="4" cellspacing="1" width="95%" class="bgcolor" align="center">
					<TR class="tbcolor1">
						<th colspan="4" align="center" id="listJG" height="30">
							<span style="FONT-SIZE: 11pt"> ��
								<logic:present name="ReportName" scope="request">
									<%=reportName%>									
								</logic:present>�����ڱ���ϵ
							</span>
						</th>
					</TR>
					<tr>
						<td class="tableHeader" width="8%">
							���
						</td>
						<td class="tableHeader" width="50%">
							���ʽ
						</td>
						<td class="tableHeader" width="30%">
							չʾ��
						</td>
						<td class="tableHeader" width="12%">
							����
						</td>
					</tr>
					<logic:present name="RecordJGs" scope="request">
						<logic:iterate id="item" name="RecordJGs" indexId="index">
							<tr bgcolor="#FFFFFF">
								<td align="center">
									<%=((Integer) index).intValue() + 1%>
								</td>
								<td align="center">
									<bean:write name="item" property="cellFormu" />
								</td>
								<td align="center">
									<bean:write name="item" property="cellFormuView" />
								</td>
								<td align="center">
									<logic:equal name="item" property="formuType" value="1">
										<font color="#008066">����У��</font>
									</logic:equal>
									<logic:equal name="item" property="formuType" value="2">
										<font color="#CC0000">���У��</font>
									</logic:equal>
								</td>
							</tr>
						</logic:iterate>
					</logic:present>
					<logic:notPresent name="RecordJGs" scope="request">
						<tr>
							<td bgcolor="#FFFFFF" colspan="3">
								�ޱ��ڱ��У��
							</td>
						</tr>
					</logic:notPresent>
				</table>
				
				<table cellSpacing="1" cellPadding="4" width="90%" border="0" align="center">
					<tr>
						<TD colspan="7" bgcolor="#FFFFFF">
							<jsp:include page="../../bpartpage.jsp" flush="true">
								<jsp:param name="url" value="../viewMCellToFormu.do" />
							</jsp:include>
						</TD>
					</tr>
				</table>
				<table border="0" cellpadding="0" cellspacing="4" width="90%" align="center">
					<tr>
						<td align="center">
							<input type="button" class="input-button" onclick="history.back(-1)" value=" �� �� ">
						</td>
					</tr>
				</table>
				
				
			</td>
		</tr>

	</table></div>
	<div title="�Զ���У��" style="padding:10px 20px">			
	<table border="0" cellspacing="0" cellpadding="4" width="95%" align="center">
		<tr><td height="8"></td></tr>
			
			<tr><td height="10"></td></tr>		
		<tr>
			<td>

				<table id="tbl" border="0" cellpadding="4" cellspacing="1" width="95%" class="bgcolor" align="center">
					<TR class="tbcolor1">
						<th colspan="4" align="center" id="list" height="30">
							<span style="FONT-SIZE: 11pt"> ��
								<logic:present name="ReportName" scope="request">
									<%=reportName%>									
								</logic:present>�����ڱ���ϵ
							</span>
						</th>
					</TR>
					<tr>
						<td class="tableHeader" width="8%">
							���
						</td>
						<td class="tableHeader" width="50%">
							���ʽ
						</td>
						<td class="tableHeader" width="30%">
							չʾ��
						</td>
						<td class="tableHeader" width="12%">
							����
						</td>
					</tr>
					<logic:present name="Records" scope="request">
						<logic:iterate id="item" name="Records" indexId="index">
							<tr bgcolor="#FFFFFF">
								<td align="center">
									<%=((Integer) index).intValue() + 1%>
								</td>
								<td align="center">
									<bean:write name="item" property="cellFormu" />
								</td>
								<td align="center">
									<bean:write name="item" property="cellFormuView" />
								</td>
								<td align="center">
									<logic:equal name="item" property="formuType" value="1">
										<font color="#008066">����У��</font>
									</logic:equal>
									<logic:equal name="item" property="formuType" value="2">
										<font color="#CC0000">���У��</font>
									</logic:equal>
								</td>
							</tr>
						</logic:iterate>
					</logic:present>
					<logic:notPresent name="Records" scope="request">
						<tr>
							<td bgcolor="#FFFFFF" colspan="3">
								�ޱ��ڱ��У��
							</td>
						</tr>
					</logic:notPresent>
				</table>
				
				<table cellSpacing="1" cellPadding="4" width="90%" border="0" align="center">
					<tr>
						<TD colspan="7" bgcolor="#FFFFFF">
							<jsp:include page="../../apartpage.jsp" flush="true">
								<jsp:param name="url" value="../viewMCellToFormu.do" />
							</jsp:include>
						</TD>
					</tr>
				</table>
				<table border="0" cellpadding="0" cellspacing="4" width="90%" align="center">
					<tr>
						<td align="center">
							<input type="button" class="input-button" onclick="history.back(-1)" value=" �� �� ">
						</td>
					</tr>
				</table>
				
				
			</td>
		</tr>

	</table>
	</div>
</body>
</html:html>
