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
<body  style="TEXT-ALIGN: center" background="../../image/total.gif">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
					alert("<bean:write name='Message' property='alertMsg'/>");
				</script>
		</logic:greaterThan>
	</logic:present>
	
	<TABLE cellSpacing="0" width="96%" border="0" align="center" cellpadding="4">
		<tr>
			<td align="center" colspan="2">
				<logic:present name="ReportName" scope="request">
					<b><bean:write name="ReportName" scope="request"/></b>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td>�������У�
				<logic:present name="ReportOrg" scope="request">
					<bean:write name="ReportOrg" scope="request"/>
				</logic:present>
			</td>
			<td>���֣�
				<logic:present name="currName" scope="request">
					<bean:write name="currName" scope="request"/>
				</logic:present>
			</td>
			<td align="right">�������ڣ�
				<logic:present name="ReportDate" scope="request">
					<bean:write name="ReportDate" scope="request"/>
				</logic:present>
			</td>
		</tr>
	</table>
	<TABLE   cellSpacing="0" width="96%" border="0" align="center" cellpadding="4">
		<TR>
			<TD>
				<TABLE  style="word-break:break-all" id = "PrintA" cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
					<tr class="titletab">
						<th colspan="7" align="center" id="list">
							<strong>
								���ڱ���ϵУ����Ϣ
							</strong>
						</th>
					</tr>
					<TR class="middle">
						<TD class="tableHeader" width="">
							<b>
								У�鹫ʽ
							</b>
						</TD>
						<TD class="tableHeader" width="">
							<b>
								��ϵ���ʽ
							</b>
						</TD>
						<TD class="tableHeader" width="5%" align="center">
							<b>
								У�����
							</b>
						</TD>
						<TD class="tableHeader" width="5%" align="center">
							<b>
								У����
							</b>
						</TD>
						<TD class="tableHeader" width="12%" align="center">
							<b>
								��ֵ
							</b>
						</TD>
						<TD class="tableHeader" width="12%" align="center">
							<b>
								��ֵ
							</b>
						</TD>
						<TD class="tableHeader" width="10%" align="center">
							<b>
								���Ҳ�ֵ
							</b>
						</TD>
					</TR>
					<logic:present name="Records" scope="request">
						<logic:iterate id="item" name="Records">
						<tr bgcolor="#FFFFFF">
							<td><bean:write name="item" property="cellFormu"/></td>
							<td><bean:write name="item" property="cellFormuView"/></td>						
							<td align="center"><bean:write name="item" property="validateTypeName"/></td>
							<td align="center">
								<logic:equal name="item" property="result" value="1">
									ͨ��
								</logic:equal>
								<logic:notEqual name="item" property="result" value="1">
									<font color="red">δͨ��</font>
								</logic:notEqual>
							</td>
							<td><bean:write name="item" property="sourceValue"/></td>
							<td><bean:write name="item" property="targetValue"/></td>
							<td><bean:write name="item" property="difference"/></td>
						</tr>
						</logic:iterate>
					</logic:present>
					<logic:notPresent name="Records" scope="request">
						<tr bgcolor="#FFFFFF">
							<td colspan="3">
								����У����Ϣ
							</td>
						</tr>
					</logic:notPresent>
				</TABLE>
			</TD>
		</TR>
		<tr>
			<td align="center">
			 <input type="button" onclick="javascript:window.close();" value="�رմ���">
			 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 <input type="button" onclick="javascript:AllAreaExcel();" value="���������Excel">
			</td>
			
			</tr>
	</TABLE>

	<SCRIPT LANGUAGE="javascript">
 //ָ��ҳ���������ݵ���Excel
 function AllAreaExcel() 
 {
	var oXL = false;
	try {
		oXL = new ActiveXObject("Excel.Application");
	} catch (e) {
	 
	}

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
  sel.moveToElementText(PrintA);
  sel.select();
  sel.execCommand("Copy");
  oSheet.Paste();
  oXL.Visible = true;
 }
 function ExcelExport()    
{    
   
    var oXL = false;
	try {
		oXL = new ActiveXObject("Excel.Application");
	} catch (e) {
	  alert(123);
	}
    var workbook = oXL.Workbooks.Add();    
     var sheet = workbook.ActiveSheet;    
     var sel = document.body.createTextRange();    
        
     //��table�е������Ƶ�sel��    
     sel.moveToElementText(PrintA);    
        
     sel.select(); //ѡ��sel����������    
     sel.execCommand("Copy");//����sel�е�����     
            
      sheet.Columns("A").ColumnWidth =35;//�����п�   
      sheet.Columns("B").ColumnWidth =35;   
      sheet.Rows(1).RowHeight = 35;//���ñ�ͷ��   
           
     //��sel�����ݿ�����sheet��������   
     sheet.Paste();            
     oXL.Visible = true;    
     //ͨ����ӡ��ֱ�ӽ�Excel���ݴ�ӡ����   
     sheet.Printout;   
     oXL.UserControl = true;    
}    
	</script>
</body>
</html:html>
