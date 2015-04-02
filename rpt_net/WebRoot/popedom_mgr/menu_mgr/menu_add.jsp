<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.auth.util.ToolMenuUtil">
</jsp:useBean>
<%
  request.setCharacterEncoding("GB2312");
  response.setCharacterEncoding("GB2312");
 %>
<html:html locale="true">
<head>
	<html:base />
	<title>添加功能菜单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">

	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript">
			function validate()
			{
				var txtMenuName = document.getElementById('menuName');
				var txtFunctionName = document.getElementById('functionName');
				var txtUrl = document.getElementById('url');
				var txtPriorId = document.getElementById('priorId');
				
				if(txtMenuName.value.Trim()=="")
				{	
					alert("请输入功能菜单名称！");
					txtMenuName.focus();
					return false;					
				}
				if(txtFunctionName.value.Trim()=="")
				{	
					alert("请输入功能名称！");
					txtFunctionName.focus();
					return false;
				}
				if(txtUrl.value.Trim()=="")
				{	
					alert("请输入URL！");
					txtUrl.focus();
					return false;
				}            
				return true;		
			}
			function _goBack(){
				window.location="<%=request.getContextPath()%>/popedom_mgr/viewToolSetting.do";
			}
			String.prototype.Trim=function(){
				return this.replace(/(^\s*)|(\s*$)/g,"");
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
	<br>

	<table width="90%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
		<tr class="titletab">
			<th align="center">
				功能菜单添加
			</th>
		</tr>
		<tr>
			<td bgcolor="#ffffff">
				<html:form action="/popedom_mgr/insertToolSetting" method="post" onsubmit="return validate()">
					<br>
					<table width="100%" border="0">						
						<tr>
							<td align="right" bgcolor="#ffffff">
								功能菜单名称:
							</td>
							<td align="left" bgcolor="#ffffff">
								<html:text property="menuName" styleClass="input-text" size="40" />
							</td>

						</tr>

						<tr>
							<td align="right" bgcolor="#ffffff">
								功能名称:
							</td>
							<td align="left" bgcolor="#ffffff">
								<html:text property="functionName" styleClass="input-text" size="40" />
							</td>
						</tr>
						<tr>
							<td align="right" bgcolor="#ffffff">
								URL:
							</td>
							<td align="left" bgcolor="#ffffff" colspan="2">
								<html:text property="url" size="83" styleClass="input-text" />
							</td>
						</tr>						
					</table>

					<table width="100%" border="0" align="center">
						<tr>
							<td colspan="6" align="right">
								<div id=location>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="right" bgcolor="#ffffff">
								<html:submit value="保存" styleClass="input-button" />
								&nbsp;
								<html:button property="back" value="返回" styleClass="input-button" onclick="javascript:_goBack()" />
							</td>
						</tr>
					</table>
					</html:form>
			</td>
		</tr>
	</table>

	

</body>
</html:html>
