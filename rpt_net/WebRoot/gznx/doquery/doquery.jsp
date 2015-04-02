<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
	<title>sql执行</title>
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
			//内容
			var  contents = document.getElementById('contents');
			
			if(contents.value=="")
			{
				alert('内容不能为空！');
				contents.focus();
				return false;
			}
			
			if(contents.value.length > 1000)
			{
				alert('内容请控制在1000字之内，现在的字数为：'+contents.value.length+"个。")
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
					sql执行
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
								内 容 :
							</td>
							<td colspan="1" bgcolor="#ffffff" align="left">
							
								<textarea name="contents" class="input-text" rows="10" cols="100" id="contents"></textarea>
							
								（1000字以内）
							</td>
						</tr>
						
						<tr>
							<TD height="12"></TD>
						</tr>
						<tr>
							<td colspan="4" align="center" bgcolor="#ffffff">
								<html:submit property="submit" value="执行sql" styleClass="input-button" />
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
