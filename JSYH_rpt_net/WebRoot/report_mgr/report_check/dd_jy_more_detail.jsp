<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config,java.util.*,com.cbrc.smis.form.DataValidateInfoForm"%>
<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
</head>
<body  style="TEXT-ALIGN: center" background="../../image/total.gif">
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
			String ReportDateRegName = request.getAttribute("ReportDateRegName"+i) != null ? request.getAttribute("ReportDateRegName"+i).toString() : null;
			
			
			if(reportName == null || reportName.equals("")) continue;
			
			List list = request.getAttribute("Records"+i) != null ? (List)request.getAttribute("Records"+i) : new ArrayList();
	%>
	<TABLE cellSpacing="0" width="96%" border="0" align="center" cellpadding="4">
		<tr>
			<td align="center" colspan="2"><%=reportName%></td>
		</tr>
		<tr>
			<td>报送子行：<%=ReportOrg%></td>
			<td>币种：<%=currName%></td>
			<td>报表口径：<%=ReportDateRegName%></td>
			<td align="right">报表日期：<%=reportDate%></td>
		</tr>
	</table>
	<TABLE   cellSpacing="0" width="96%" border="0" align="center" cellpadding="4">
		<TR>
			<TD>
				<TABLE style="word-break:break-all" id = "PrintA" cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
					<tr class="titletab">
						<th colspan="7" align="center" id="list">
							<strong>
								表内表间关系校验信息
							</strong>
						</th>
					</tr>
					<TR class="middle">
						<TD class="tableHeader" width="">
							<b>
								校验公式
							</b>
						</TD>
						<TD class="tableHeader" width="">
							<b>
								关系表达式
							</b>
						</TD>
						<TD class="tableHeader" width="5%" align="center">
							<b>
								校验类别
							</b>
						</TD>
						<TD class="tableHeader" width="5%" align="center">
							<b>
								校验结果
							</b>
						</TD>
						<TD class="tableHeader" width="10%" align="center">
							<b>
								左值<font color="0000CD"><br>(末尾数为容差)</font>
							</b>
						</TD>
						<TD class="tableHeader" width="12%" align="center">
							<b>
								右值
							</b>
						</TD>
						<TD class="tableHeader" width="12%" align="center">
							<b>
								左右差值
							</b>
						</TD>													
					</TR>					
					<%
					if(list != null && list.size() > 0){
						for(int j=0;j<list.size();j++){
							DataValidateInfoForm dataValidateInfoForm = (DataValidateInfoForm)list.get(j);
							String cellFormu = dataValidateInfoForm.getCellFormu();
							String CellFormuView = dataValidateInfoForm.getCellFormuView();
							String validateTypeName = dataValidateInfoForm.getValidateTypeName();
							int result = dataValidateInfoForm.getResult().intValue();
							//新增共同显示问题
							cellFormu = dataValidateInfoForm.getCause()!=null ? 
								dataValidateInfoForm.getCause()+"："+ cellFormu : cellFormu;
							//显示值
							String source = dataValidateInfoForm.getSourceValue()!=null ?
											dataValidateInfoForm.getSourceValue() : "";
							String target = dataValidateInfoForm.getTargetValue()!=null ?
											dataValidateInfoForm.getTargetValue() : "";
							String difference = dataValidateInfoForm.getDifference()!=null ?
											dataValidateInfoForm.getDifference() : "";
					%>
						<tr bgcolor="#FFFFFF">
							<td><%=cellFormu%></td>
							<td><%=CellFormuView%></td>				
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
							<td><%=source%></td>						
							<td><%=target%></td>		
							<td><%=difference%></td>				
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
	
	<table>
		<tr>
			<td align="center">
				<input type="button" onclick="javascript:window.close();" value="关闭窗口">			 	
			</td>
			
		</tr>
	</table>	
</body>
</html:html>
