<%@ page contentType="text/html;charset=GBK" %>
<%	String appmap = request.getContextPath();
	String printImage = "<img src='" + appmap + "/image/print.gif' border=no >";
	String excelImage = "<img src='" + appmap + "/image/excel.gif' border=no >";
	String pdfImage = "<img src='" + appmap + "/image/pdf.gif' border=no >";
   
	String firstPageImage = "<img src='" + appmap + "/image/firstpage.gif' border=no >";
	String lastPageImage = "<img src='" + appmap + "/image/lastpage.gif' border=no >";
	String nextPageImage = "<img src='" + appmap + "/image/nextpage.gif' border=no >";
	String prevPageImage = "<img src='" + appmap + "/image/prevpage.gif' border=no >";
	String submitImage = "<img src='" + appmap + "/image/savedata.gif' border=no >";
%>

<table id=titleTable width=100% cellspacing=0 cellpadding=0 border=0 ><tr>
	<td height="25" width=100% valign="middle"  style="font-size:13px" background="<%=appmap%>/image/toolbar-bg.gif">
		<table width="100%">
			<tr >
				
				<td width="100%" align="right" valign="middle"   style="font-size:12px; line-height:12px; margin:3px 0 0 0 ;" >&nbsp;
				<a href="#" onClick="report1_print();return false;"><%=printImage%></a>
				<a href="#" onClick="report1_saveAsExcel();return false;"><%=excelImage%></a>
				<a href="#" onClick="report1_saveAsPdf();return false;"><%=pdfImage%></a>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</tr>
	  </table>
	</td>
</table>