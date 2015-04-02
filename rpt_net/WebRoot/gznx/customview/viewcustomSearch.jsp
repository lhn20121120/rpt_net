<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base /> 
	<title>自定义查询</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/globalStyle.css" rel="stylesheet" type="text/css">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	
	<script language="javascript">

	</script>
</head>

<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table width="100%" border="0" align="center" cellpadding="4" cellspacing="0">
		<tr>
			<td height="30" colspan="2">
				当前位置 >> 报表查询 >> 自定义查询
			<td>
		</tr>
	</table>
	<br>
	<table width="100%" border="0" cellpadding="4" cellspacing="1" class="tbcolor" align="center">
		<tr class="titletab">
			<th height="25" align="center" colspan="11">
				指标信息
			</th>
		</tr>
		<tr align="center" class="middle">
			<td align="center" width="3%">
				序号
			</td>
			<td align="center" width="6%">
				指标ID
			</td>
			<td align="center" width="20%">
				指标名称
			</td>
			<td align="center" width="12%">
				机构
			</td>
			<td align="center" width="7%">
				币种
			</td>
			<td align="center" width="6%">
				频度
			</td>
			<td align="center" width="10%">
				时间
			</td>
			<td align="center" width="12%">
				本期值
			</td>
			
			
		</tr>
		<logic:present name="Records" scope="request">
			<logic:iterate id="item" name="Records" indexId="index">
				<tr align="center">
					<td bgcolor="#ffffff">
						<%=((Integer) index).intValue() + 1%>
					</td>
					<td bgcolor="#ffffff">						
							<bean:write name="item" property="cellId" />						
					</td>
					<td bgcolor="#ffffff" align="left">						
							<bean:write name="item" property="cellName" />						
					</td>
					<td bgcolor="#ffffff">						
							<bean:write name="item" property="orgName" />						
					</td>
					<td bgcolor="#ffffff">						
							<bean:write name="item" property="curName" />						
					</td>
					<td bgcolor="#ffffff">						
							<bean:write name="item" property="repFreqName" />						
					</td>
					<td bgcolor="#ffffff">						
							<bean:write name="item" property="celldate" />						
					</td>
					<td bgcolor="#ffffff" align="right">						
							<bean:write name="item" property="cellData" />						
					</td>					
				</tr>
			</logic:iterate>
		</logic:present>
		<logic:notPresent name="Records" scope="request">
			<tr align="left">
				<td bgcolor="#ffffff" colspan="11">
					暂无指标信息
				</td>
			</tr>
		</logic:notPresent>
	</table>
	<TABLE width="100%" align="center">
		<tr>
			<TD bgcolor="#ffffff" align="center" colspan="15">
				<INPUT type="button" class="input-button" value="返回" onclick="javaScript:history.back();">
			</TD>
		</tr>
	</TABLE>
	
</body>
</html:html>
