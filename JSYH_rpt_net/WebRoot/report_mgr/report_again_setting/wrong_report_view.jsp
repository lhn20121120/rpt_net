<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<script language="javascript">
	/**
	 * 报表重报设定事件
	 */
	 function report_repeat_set(repInId){
	 	window.location="report_again_set.jsp?repInId=" + repInId;
	 }
</script>
<html:html locale="true">
	<head>
	<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" type="text/css" rel="stylesheet">
	
	</head>
	<body>
		<div align="center">
		<table cellSpacing="0" cellPadding="0" width="98%" border="1">
			<tr>
				<td vAlign="top" width="48%" background="../../image/barbk.jpg" height="35">
					<p align="center"><IMG height="34" src="../../image/column4.jpg" width="128" border="0"></p>
				</td>
				<td vAlign="top" width="13%" background="../../image/barbk.jpg" height="35">
					<p align="center"><IMG height="34" src="../../image/column5.jpg" width="90" align="top" border="0"></p>
				</td>
				<td vAlign="top" width="11%" background="../../image/barbk.jpg" height="35">
					<p align="center"><IMG height="34" src="../../image/column6.jpg" width="60" border="0"></p>
				</td>
				<TD vAlign="top" width="8%" background="../../image/barbk.jpg" height="35">
					<p align="center"><FONT face="宋体"><IMG height="34" src="../../image/column7.jpg" width="60" border="0"></FONT></p>
				</TD>
				<td vAlign="top" background="../../image/barbk.jpg" height="35"><p align="center"><FONT face="宋体"><IMG alt="" src="../../image/column8.jpg"></FONT></p>
				</td>
				<td vAlign="top" align="center" background="../../image/barbk.jpg" height="35"><IMG height="34" src="../../image/column11.jpg" width="60" border="0"></td>
			</tr>
			<TR>
				<TD vAlign="middle" noWrap align="left" height="30">
					<p align="center"><font face="Arial" color="#000000" size="2"><A href="../../data/G01.pdf" target="_about"><font face="Arial" size="2">G01 资产负债项目统计表</font></A></font></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><FONT face="Arial" color="#000000" size="2">2004/8/22</FONT></TD>
					<TD vAlign="middle" noWrap align="center" height="30">
					<font face="宋体" size="2">北京工行</font></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><strong><font face="宋体" color="#ff0000" size="3">×</font></strong></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><font face="Arial" size="2"><A href="javascript:window.parent.contents.location.assign('../detail_view.jsp')"><font size="2">查看详细属性</font></A></font></TD>
					<TD vAlign="middle" noWrap align="center" height="30">
						<input type="button" value="重报" onclick="report_repeat_set(1)">
					</TD>
			</TR>
			<TR>
				<TD vAlign="middle" noWrap align="left" height="30">
					<p align="center"><font face="Arial" color="#000000" size="2"><A href="../../data/G01.pdf" target="_about"><font face="Arial" size="2">G01 资产负债项目统计表</font></A></font></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><FONT face="Arial" color="#000000" size="2">2004/8/22</FONT></TD>
					<TD vAlign="middle" noWrap align="center" height="30">
					<font face="宋体" size="2">北京工行</font></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><strong><font face="宋体" color="#ff0000" size="3">×</font></strong></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><font face="Arial" size="2"><A href="javascript:window.parent.contents.location.assign('../detail_view.jsp')"><font size="2">查看详细属性</font></A></font></TD>
					<TD vAlign="middle" noWrap align="center" height="30">
						<input type="button" value="重报" onclick="report_repeat_set(2)">
					</TD>
			</TR>			
			<TR>
				<TD vAlign="middle" noWrap align="left" height="30">
					<p align="center"><font face="Arial" color="#000000" size="2"><A href="../../data/G01.pdf" target="_about"><font face="Arial" size="2">G01 资产负债项目统计表</font></A></font></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><FONT face="Arial" color="#000000" size="2">2004/8/22</FONT></TD>
					<TD vAlign="middle" noWrap align="center" height="30">
					<font face="宋体" size="2">北京工行</font></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><strong><font face="宋体" color="#ff0000" size="3">×</font></strong></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><font face="Arial" size="2"><A href="javascript:window.parent.contents.location.assign('../detail_view.jsp')"><font size="2">查看详细属性</font></A></font></TD>
					<TD vAlign="middle" noWrap align="center" height="30">
						<input type="button" value="重报" onclick="report_repeat_set(3)">
					</TD>
			</TR>			
			<TR>
				<TD vAlign="middle" noWrap align="left" height="30">
					<p align="center"><font face="Arial" color="#000000" size="2"><A href="../../data/G01.pdf" target="_about"><font face="Arial" size="2">G01 资产负债项目统计表</font></A></font></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><FONT face="Arial" color="#000000" size="2">2004/8/22</FONT></TD>
					<TD vAlign="middle" noWrap align="center" height="30">
					<font face="宋体" size="2">北京工行</font></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><strong><font face="宋体" color="#ff0000" size="3">×</font></strong></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><font face="Arial" size="2"><A href="javascript:window.parent.contents.location.assign('../detail_view.jsp')"><font size="2">查看详细属性</font></A></font></TD>
					<TD vAlign="middle" noWrap align="center" height="30">
						<input type="button" value="重报" onclick="report_repeat_set(4)">
					</TD>
			</TR>			
			<TR>
				<TD vAlign="middle" noWrap align="left" height="30">
					<p align="center"><font face="Arial" color="#000000" size="2"><A href="../../data/G01.pdf" target="_about"><font face="Arial" size="2">G01 资产负债项目统计表</font></A></font></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><FONT face="Arial" color="#000000" size="2">2004/8/22</FONT></TD>
					<TD vAlign="middle" noWrap align="center" height="30">
					<font face="宋体" size="2">北京工行</font></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><strong><font face="宋体" color="#ff0000" size="3">×</font></strong></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><font face="Arial" size="2"><A href="javascript:window.parent.contents.location.assign('../detail_view.jsp')"><font size="2">查看详细属性</font></A></font></TD>
					<TD vAlign="middle" noWrap align="center" height="30">
						<input type="button" value="重报" onclick="report_repeat_set(5)">
					</TD>
			</TR>			
			<TR>
				<TD vAlign="middle" noWrap align="left" height="30">
					<p align="center"><font face="Arial" color="#000000" size="2"><A href="../../data/G01.pdf" target="_about"><font face="Arial" size="2">G01 资产负债项目统计表</font></A></font></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><FONT face="Arial" color="#000000" size="2">2004/8/22</FONT></TD>
					<TD vAlign="middle" noWrap align="center" height="30">
					<font face="宋体" size="2">北京工行</font></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><strong><font face="宋体" color="#ff0000" size="3">×</font></strong></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><font face="Arial" size="2"><A href="javascript:window.parent.contents.location.assign('../detail_view.jsp')"><font size="2">查看详细属性</font></A></font></TD>
					<TD vAlign="middle" noWrap align="center" height="30">
						<input type="button" value="重报" onclick="report_repeat_set(6)">
					</TD>
			</TR>			
			<TR>
				<TD vAlign="middle" noWrap align="left" height="30">
					<p align="center"><font face="Arial" color="#000000" size="2"><A href="../../data/G01.pdf" target="_about"><font face="Arial" size="2">G01 资产负债项目统计表</font></A></font></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><FONT face="Arial" color="#000000" size="2">2004/8/22</FONT></TD>
					<TD vAlign="middle" noWrap align="center" height="30">
					<font face="宋体" size="2">北京工行</font></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><strong><font face="宋体" color="#ff0000" size="3">×</font></strong></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><font face="Arial" size="2"><A href="javascript:window.parent.contents.location.assign('../detail_view.jsp')"><font size="2">查看详细属性</font></A></font></TD>
					<TD vAlign="middle" noWrap align="center" height="30">
						<input type="button" value="重报" onclick="report_repeat_set(7)">
					</TD>
			</TR>			
			<TR>
				<TD vAlign="middle" noWrap align="left" height="30">
					<p align="center"><font face="Arial" color="#000000" size="2"><A href="../../data/G01.pdf" target="_about"><font face="Arial" size="2">G01 资产负债项目统计表</font></A></font></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><FONT face="Arial" color="#000000" size="2">2004/8/22</FONT></TD>
					<TD vAlign="middle" noWrap align="center" height="30">
					<font face="宋体" size="2">北京工行</font></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><strong><font face="宋体" color="#ff0000" size="3">×</font></strong></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><font face="Arial" size="2"><A href="javascript:window.parent.contents.location.assign('../detail_view.jsp')"><font size="2">查看详细属性</font></A></font></TD>
					<TD vAlign="middle" noWrap align="center" height="30">
						<input type="button" value="重报" onclick="report_repeat_set(8)">
					</TD>
			</TR>			
			<TR>
				<TD vAlign="middle" noWrap align="left" height="30">
					<p align="center"><font face="Arial" color="#000000" size="2"><A href="../../data/G01.pdf" target="_about"><font face="Arial" size="2">G01 资产负债项目统计表</font></A></font></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><FONT face="Arial" color="#000000" size="2">2004/8/22</FONT></TD>
					<TD vAlign="middle" noWrap align="center" height="30">
					<font face="宋体" size="2">北京工行</font></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><strong><font face="宋体" color="#ff0000" size="3">×</font></strong></TD>
					<TD vAlign="middle" noWrap align="center" height="30"><font face="Arial" size="2"><A href="javascript:window.parent.contents.location.assign('../detail_view.jsp')"><font size="2">查看详细属性</font></A></font></TD>
					<TD vAlign="middle" noWrap align="center" height="30">
						<input type="button" value="重报" onclick="report_repeat_set(9)">
					</TD>
			</TR>		
		</table>
		<table cellSpacing="0" cellPadding="0" width="98%" border="0">
			<TR>
				<TD>
					<jsp:include page="../../apartpage.jsp" flush="true">
				  		<jsp:param name="url" value=""/>
				  	</jsp:include>
				</TD>
			</tr>
		</table>
		</div>
	</body>
</html:html>