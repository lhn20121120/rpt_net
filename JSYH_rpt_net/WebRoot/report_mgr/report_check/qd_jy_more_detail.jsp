<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config,java.util.*,com.cbrc.smis.form.QDDataValidateInfoForm"%>
<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
</head>
<body style="TEXT-ALIGN: center" background="../../image/total.gif">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
					alert("<bean:write name='Message' property='alertMsg'/>");
				</script>
		</logic:greaterThan>
	</logic:present>
	<%
		int count = 0;
		count = request.getAttribute("count") != null ? Integer.parseInt(request.getAttribute("count").toString()) : 0;
		
		for(int i=0;i<count;i++){
			String reportDate = request.getAttribute("ReportDate"+i) != null ? request.getAttribute("ReportDate"+i).toString() : "";
			String currName = request.getAttribute("currName"+i) != null ? request.getAttribute("currName"+i).toString() : "";
			String reportName = request.getAttribute("ReportName"+i) != null ? request.getAttribute("ReportName"+i).toString() : null;
			String ReportOrg = request.getAttribute("ReportOrg"+i) != null ? request.getAttribute("ReportOrg"+i).toString() : null;
			if(reportName == null || reportName.equals("")) continue;
			
			List list = request.getAttribute("Records"+i) != null ? (List)request.getAttribute("Records"+i) : new ArrayList();
	%>
		<TABLE cellSpacing="0" width="96%" border="0" align="center" cellpadding="4">
		<tr>
			<td align="center" colspan="2"><%=reportName%></td>
		</tr>
		<tr>
			<td>报送子行：<%=ReportOrg%></td>
			<td align="right">报表日期：<%=reportDate%></td>
		</tr>
	</table>
	<TABLE cellSpacing="0" width="96%" border="0" align="center" cellpadding="4">
		<TR>
			<TD>
				<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
					<tr class="titletab">
						<th colspan="4" align="center" id="list">
							<strong>
								表内表间关系校验信息
							</strong>
						</th>
					</tr>
					<TR class="middle">
						<TD class="tableHeader" width="40%">
							<b>
								关系表达式
							</b>
						</TD>
						<TD class="tableHeader" width="20%">
							<b>
								校验类别
							</b>
						</TD>
						<TD class="tableHeader" width="10%">
							<b>
								校验结果
							</b>
						</TD>
						<TD class="tableHeader" width="30%">
							<b>
								未通过原因
							</b>
						</TD>
					</TR>
					<logic:present name="Records" scope="request">
						<logic:iterate id="item" name="Records">
						<tr bgcolor="#FFFFFF">
							<td><bean:write name="item" property="cellFormView"/></td>
							<td align="center"><bean:write name="item" property="validateTypeName"/></td>
							<td align="center">
								<logic:equal name="item" property="result" value="1">
									通过
								</logic:equal>
								<logic:notEqual name="item" property="result" value="1">
									<font color="red">未通过</font>
								</logic:notEqual>
							</td>
							<td><bean:write name="item" property="cause"/></td>
						</tr>
						</logic:iterate>
					</logic:present>
					<logic:notPresent name="Records" scope="request">
						<tr bgcolor="#FFFFFF">
							<td colspan="4">
								暂无校验信息
							</td>
						</tr>
					</logic:notPresent>
					
					<%
					if(list != null && list.size() > 0){
						for(int j=0;j<list.size();j++){
							QDDataValidateInfoForm qdDataValidateInfoForm = (QDDataValidateInfoForm)list.get(j);							
							String cellFormu = qdDataValidateInfoForm.getCellFormuView();
							String validateTypeName = qdDataValidateInfoForm.getValidateTypeName();
							int result = qdDataValidateInfoForm.getResult().intValue();
							String cause = qdDataValidateInfoForm.getCause();
					%>
						<tr bgcolor="#FFFFFF">
							<td><%=cellFormu%></td>						
							<td align="center"><%=validateTypeName%></td>
							<td align="center">
								<%
									if(result == 1){
									%>通过<%
									}else{
									%><font color="red">未通过</font><%
									}
								%>								
							</td>
							<td align="center"><%=cause%></td>
						</tr>
					<%
						}						
					}else{
					%>
						<tr bgcolor="#FFFFFF">
							<td colspan="3">
								暂无校验信息
							</td>
						</tr>
					<%
					}
					%>
					
					
					
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	<%
		}
	%>
</body>
</html:html>
