<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
	<title>sqlִ��</title>
	<html:base/>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="<%=request.getContextPath()%>/css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript" src="<%=request.getContextPath()%>/js/func.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/prototype-1.4.0.js"></script>
	<script language="javascript">
		function validate()
		{
			//����
			var  contents = document.getElementById('contents');
			
			if(contents.value=="")
			{
				alert('���ݲ���Ϊ�գ�');
				contents.focus();
				return false;
			}
			
			if(contents.value.length > 1000)
			{
				alert('�����������1000��֮�ڣ����ڵ�����Ϊ��'+contents.value.length+"����")
				contents.focus();
				return false;
			}			
		}

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

		<form method="post" name="sqlQueryForm" action="<%=request.getContextPath()%>/doexcuteSql.do">
		
		<table width="90%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
			<tr class="titletab">
				<th align="center">
					sqlִ��
				</th>
			</tr>
			<tr>
				<td bgcolor="#ffffff" align="center">
					<table width="100%" border="0" align="left" cellpadding="2" cellspacing="1">
						<tr bgcolor="#ffffff">
							<td height="5">
							</td>
						</tr>
		
						<tr bgcolor="#ffffff">
							<td valign="top" align="right">
								�� �� :
							</td>
							<td colspan="1" bgcolor="#ffffff" align="left">
							
								<textarea name="contents" class="input-text" rows="10" cols="100" id="contents"></textarea>
							
								��1000�����ڣ�
							</td>
						</tr>
						
						<tr>
							<TD height="12"></TD>
						</tr>
						<tr>
							<td colspan="4" align="center" bgcolor="#ffffff">
								<html:submit property="submit" value="ִ��sql" styleClass="input-button" />
								&nbsp;								
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		</form>
</body>
</html:html>
