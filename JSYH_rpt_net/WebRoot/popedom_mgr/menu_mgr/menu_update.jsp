<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.util.FitechUtil"%>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.auth.util.ToolMenuUtil">
</jsp:useBean>
<html:html locale="true">
<head>
	<html:base />
	<title>修改功能菜单</title>
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

	<table width="80%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
		<tr class="titletab">
			<th align="center">
				功能菜单修改
			</th>
		</tr>
		<tr>
			<td bgcolor="#ffffff">
				<html:form action="/popedom_mgr/updateToolSetting" method="post" onsubmit="return validate()">
					<br>
					<table width="100%" border="0" align="center" cellpadding="4" cellspacing="1">						
						<tr>
							<td align="center" bgcolor="#ffffff">
								<bean:parameter id="MenuId" name="menuId" />
								<input type="hidden" name="menuId" value="<bean:write name="MenuId"/>">
							</td>
						</tr>
						<tr>
							<td align="right" bgcolor="#ffffff" width="20%">
								功能菜单名称:
							</td>
							<td align="left" bgcolor="#ffffff" width="30%">
								<%//String menuName = FitechUtil.getParameter(request,"menuName");
									String menuName = request.getParameter("menuName");
									String functionName = request.getParameter("functionName");
		
									
									if(com.cbrc.smis.common.Config.WEB_SERVER_TYPE==1&&request.getParameter("covert")!=null){
										menuName=new String(menuName.getBytes("iso-8859-1"), "gb2312");
										functionName=new String(functionName.getBytes("iso-8859-1"), "gb2312");
					         		 }
								%>
								<input type="text" name="menuName" value="<%=menuName%>" class="input-text" size="40">
							</td>
						</tr>
						<tr>
							<td align="right" bgcolor="#ffffff">
								功能名称:
							</td>
							<td align="left" bgcolor="#ffffff">
								
								<input type="text" name="functionName" value="<%=functionName%>" class="input-text" size="40" >
							</td>
						</tr>
						<tr>
							<td align="right" bgcolor="#ffffff">
								URL:
							</td>
							<td align="left" bgcolor="#ffffff" colspan="3">
								<bean:parameter id="Url" name="url" />
								<input type="text" name="url" value="<bean:write name="Url"/>" class="input-text" size="80">
							</td>
						</tr>						
						<tr>
							<td colspan="4" align="right" bgcolor="#ffffff">
								<div id=location>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="right" bgcolor="#ffffff">
								<html:submit value="保存" styleClass="input-button" />
								&nbsp;
								<html:button property="back" value="返回" styleClass="input-button" onclick="_goBack()" />
							</td>
						</tr>
					</table>
			</td>
		</tr>
	</table>

	</html:form>

</body>
</html:html>
