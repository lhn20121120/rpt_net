<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base /> 
	<title>�Զ����ѯ</title>
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
				��ǰλ�� >> �����ѯ >> �Զ����ѯ
			<td>
		</tr>
	</table>
	<br>
	<table width="100%" border="0" cellpadding="4" cellspacing="1" class="tbcolor" align="center">
		<tr class="titletab">
			<th height="25" align="center" colspan="11">
				ָ����Ϣ
			</th>
		</tr>
		<tr align="center" class="middle">
			<td align="center" width="3%">
				���
			</td>
			<td align="center" width="6%">
				ָ��ID
			</td>
			<td align="center" width="20%">
				ָ������
			</td>
			<td align="center" width="12%">
				����
			</td>
			<td align="center" width="7%">
				����
			</td>
			<td align="center" width="6%">
				Ƶ��
			</td>
			<td align="center" width="10%">
				ʱ��
			</td>
			<td align="center" width="12%">
				����ֵ
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
					����ָ����Ϣ
				</td>
			</tr>
		</logic:notPresent>
	</table>
	<TABLE width="100%" align="center">
		<tr>
			<TD bgcolor="#ffffff" align="center" colspan="15">
				<INPUT type="button" class="input-button" value="����" onclick="javaScript:history.back();">
			</TD>
		</tr>
	</TABLE>
	
</body>
</html:html>
