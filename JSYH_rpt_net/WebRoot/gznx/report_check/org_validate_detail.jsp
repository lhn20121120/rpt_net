<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.*"%>
<%@ page import="com.fitech.gznx.po.OrgValiInfo" %>
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
	 
	<%
			List<OrgValiInfo> list = (List<OrgValiInfo>)request.getAttribute("valiInfoList");
	%> 
	<TABLE   cellSpacing="0" width="96%" border="0" align="center" cellpadding="4">
		<TR>
			<TD>
				<TABLE style="word-break:break-all" id = "PrintA" cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
					<tr class="titletab">
						<th colspan="6" align="center" id="list">
							<strong>
								总分校验信息
							</strong>
						</th>
					</tr>
					<TR class="middle">
					<TD class="tableHeader" width="12%" align="center">
							<b>
								机构名
							</b>
						</TD>
						<TD class="tableHeader" width="6%">
							<b>
								报表名
							</b>
						</TD>
						<TD class="tableHeader" width="12%" align="center">
							<b>
								指标ID
							</b>
						</TD>
						<TD class="tableHeader" width="" align="center">
							<b>
								校验项目
							</b>
						</TD>
						<TD class="tableHeader" width="15%" align="center">
							<b>
								值
							</b>
						</TD> 
						<TD class="tableHeader" width="12%" align="center">
							<b>
								校验结果
							</b>
						</TD>
					</TR>					
					<%
					if(list != null && list.size() > 0){
						for(int i=0;i<list.size();i++){ 
							OrgValiInfo ovi=(OrgValiInfo)list.get(i);
							if(ovi.getFlag().equals("0")){
					%>
						<tr bgcolor="#FFFFFF">
						<td align="center"><%=ovi.getOrg_name()%></td>
							<td align="center"><%=ovi.getTemplate_id()%></td>
							<td align="center"><%=ovi.getCell_pid()%></td>	 	
							<td align="center"><%=ovi.getRol_name()%></td>	 
							<td align="center"><%=ovi.getCell_data()%></td> 
							<td align="center">--</td>
						</tr>
					<%
						}else{
							%>	
					<tr bgcolor="#99CCCC">
					<td align="center"><%=ovi.getOrg_name()%></td>
							<td align="center"><%=ovi.getTemplate_id()%></td>
							<td align="center"><%=ovi.getCell_pid()%></td>	 
							<td align="center"><%=ovi.getRol_name()%></td>
							<td align="center"><%=ovi.getCell_data()%></td> 
							<td align="center"> <font color="red">未通过</font>  </td>
					</tr>
					<%
						}
							
						
						}						
					}else{
					%>
						<tr bgcolor="#FFFFFF">
							<td colspan="5">
								暂无校验信息
							</td>
						</tr>
						<%} %> 
				</TABLE>
			</TD>
		</TR>		
	</TABLE> 
	
	<table>
		<tr>
			<td align="center">
				<input type="button" onclick="javascript:window.close();" value="关闭窗口">			 	
			</td>
		</tr>
	</table>	
</body>
</html:html>
