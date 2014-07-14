<%@ page contentType="text/html;charset=GBK" %>
<%	String appmap = request.getContextPath();
	String printImage = "<img src='" + appmap + "/image/print.gif' border=no >";
	String excelImage = "<img src='" + appmap + "/image/excel.gif' border=no >";
	String pdfImage = "<img src='" + appmap + "/image/pdf.gif' border=no >"; 
	String firstPageImage = "<img src='" + appmap + "/image/firstpage.gif' border=no >";
	String lastPageImage = "<img src='" + appmap + "/image/lastpage.gif' border=no >";
	String nextPageImage = "<img src='" + appmap + "/image/nextpage.gif' border=no >";
	String prevPageImage = "<img src='" + appmap + "/image/prevpage.gif' border=no >";	
%>

<table id=titleTable width=100% cellspacing=0 cellpadding=0 border=0 ><tr>
	<td height="25" width=100% valign="middle"  style="font-size:13px" background="<%=appmap%>/image/toolbar-bg.gif">
		<table width="100%">
			<tr >
				<td width=53% align="left"  style="font-size:13px" >&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td width="47%" align="center" valign="middle" style="font-size:15px" >µÚ<span id="c_page_span"></span>Ò³/¹²<span id="t_page_span"></span>Ò³&nbsp;&nbsp;
				<%--<a href="#" onClick="report1_print();return false;"><%=printImage%></a>
				--%><a href="#" onClick="report1_saveAsExcel();return false;"><%=excelImage%></a>
				<a href="#" onClick="report1_saveAsPdf();return false;"><%=pdfImage%></a>
				<a href="#" onClick="try{report1_toPage( 1 );}catch(e){}return false;"><%=firstPageImage%></a>
				<a href="#" onClick="try{report1_toPage(report1_getCurrPage()-1);}catch(e){}return false;"><%=prevPageImage%></a>
				<a href="#" onClick="try{report1_toPage(report1_getCurrPage()+1);}catch(e){}return false;"><%=nextPageImage%></a>
				<a href="#" onClick="try{report1_toPage(report1_getTotalPage());}catch(e){}return false;"><%=lastPageImage%></a>
			  </td>
			</tr>
	  </table>
	</td>
</table>