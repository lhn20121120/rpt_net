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
			<td>报送子行：
				<logic:present name="ReportOrg" scope="request">
					<bean:write name="ReportOrg" scope="request"/>
				</logic:present>
			</td>
			<td>币种：
				<logic:present name="currName" scope="request">
					<bean:write name="currName" scope="request"/>
				</logic:present>
			</td>
			<td align="right">报表日期：
				<logic:present name="ReportDate" scope="request">
					<bean:write name="ReportDate" scope="request"/>
				</logic:present>
			</td>
		</tr>
	</table>
	<TABLE   cellSpacing="0" width="96%" border="0" align="center" cellpadding="4">
		<TR>
			<TD>
				<TABLE style="word-break:break-all" id = "PrintA" cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
					<tr class="titletab">
						<th colspan="6" align="center" id="list">
							<strong>
								表内表间关系校验信息
							</strong>
						</th>
					</tr>
					<TR class="middle">
						<TD class="tableHeader" width="31%">
							<b>
								关系表达式
							</b>
						</TD>
						<TD class="tableHeader" width="12%" align="center">
							<b>
								校验类别
							</b>
						</TD>
						<TD class="tableHeader" width="12%" align="center">
							<b>
								校验结果
							</b>
						</TD>
						<TD class="tableHeader" width="15%" align="center">
							<b>
								左值<font color="0000CD"><br>(末尾数为容差)</font>
							</b>
						</TD>
						<TD class="tableHeader" width="15%" align="center">
							<b>
								右值
							</b>
						</TD>
						<TD class="tableHeader" width="15%" align="center">
							<b>
								左右差值
							</b>
						</TD>
						
					</TR>
					<logic:present name="Records" scope="request">
						<logic:iterate id="item" name="Records">
						<tr bgcolor="#FFFFFF">
							<td><bean:write name="item" property="cellFormuView"/></td>						
							<td align="center"><bean:write name="item" property="validateTypeName"/></td>
							<td align="center">
								<logic:equal name="item" property="result" value="1">
									通过
								</logic:equal>
								<logic:notEqual name="item" property="result" value="1">
									<font color="red">未通过</font>
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
							<td colspan="6">
								暂无校验信息
							</td>
						</tr>
					</logic:notPresent>
				</TABLE>
			</TD>
		</TR>
		<tr>
		<td align="center">
			 <input type="button" onclick="javascript:window.close();" value="关闭窗口">
			 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<!--  <input type="button" onclick="javascript:AllAreaExcel();" value="导出结果至Excel"> -->
			</td>
			
			</tr>
	</TABLE>

	<SCRIPT LANGUAGE="javascript">
 //指定页面区域内容导入Excel
 function AllAreaExcel() 
 {
	try {
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
	} catch (e) {
	alert("error");
	}
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
