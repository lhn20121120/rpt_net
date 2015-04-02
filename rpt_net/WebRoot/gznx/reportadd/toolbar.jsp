<%@ page contentType="text/html;charset=GBK" %>
<%	String appmap = request.getContextPath();
	String printImage = "<img src='" + appmap + "/image/print.gif' border=no >";
	String excelImage = "<img src='" + appmap + "/image/excel.gif' border=no >";
	String pdfImage = "<img src='" + appmap + "/image/pdf.gif' border=no >";   
%>

<table id=titleTable width=100% cellspacing=0 cellpadding=0 border=0 ><tr>
	<td height="25" width=100% valign="middle"  style="font-size:13px" background="<%=appmap%>/image/toolbar-bg.gif">
		<table width="100%">
			<tr >
				
				<td width="100%" align="right" valign="middle"   style="font-size:12px; line-height:12px; margin:3px 0 0 0 ;" >&nbsp;
				<!-- 由于操作系统安全设定原因，不能正常使用
				 a href="#" onClick="report1_print();return false;"><%=printImage%></a>
				-->
				<a href="#" onClick="report1_saveAsExcel();return false;"><%=excelImage%></a>
			<!-- 	<a href="#" onClick="report1_saveAsPdf();return false;"><%--=pdfImage--%></a> -->
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</tr>
	  </table>
	</td>
</table>