<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
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
	<TABLE   id = "PrintB"   cellSpacing="0" width="96%" border="0" align="center" cellpadding="4">
		<TR>
			<TD>
				<TABLE id = "PrintA" cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
					<tr class="titletab">
						<th colspan="7" align="center" id="list">
							<strong> 校验不通过原因 </strong>
						</th>
					</tr>
					<logic:present name="Records" scope="request">
						<logic:iterate id="item" name="Records">
							<tr bgcolor="#FFFFFF">
								<td>
									<bean:write name="item" property="cellFormuView" />
								</td>

								<td align="center">

									<font color="red">校验未通过</font>

								</td>
							</tr>
						</logic:iterate>
					</logic:present>
					<tr>
					</tr>
					
				</TABLE>
			</TD>
		</TR>
		<tr>
			<td align="center">
			 <input type="button" onclick="javascript:window.close();" value="关闭窗口">
			 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 <input type="button" onclick="javascript:AllAreaExcel();" value="导出页面Excel">
			</td>
					</tr>
	</TABLE>
	<SCRIPT LANGUAGE="javascript">
 //指定页面区域内容导入Excel
 function AllAreaExcel() 
 {
  var oXL = new ActiveXObject("Excel.Application"); 
  var oWB = oXL.Workbooks.Add(); 
  var oSheet = oWB.ActiveSheet; 
  var Lenr = PrintA.rows.length;
  for (i=0;i<Lenr;i++) 
  { 
   var Lenc = PrintA.rows(i).cells.length; 
   for (j=0;j<Lenc;j++) 
   { 
    oSheet.Cells(i+1,j+1).value = PrintA.rows(i).cells(j).innerText; 
   } 
  } 
  oXL.Visible = true; 
 }
  function AllAreaExcel1() 
 {
  var oXL = new ActiveXObject("Excel.Application"); 
  var oWB = oXL.Workbooks.Add(); 
  var oSheet = oWB.ActiveSheet;  
  var sel=document.body.createTextRange();
  sel.moveToElementText(PrintB);
  sel.select();
  sel.execCommand("Copy");
  oSheet.Paste();
  oXL.Visible = true;
 }
	</script>
</body>
</html:html>
