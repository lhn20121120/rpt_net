<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="orgNetForm" scope="request" class="com.fitech.net.form.OrgNetForm" />
<jsp:useBean id="roleForm" scope="session" class="com.cbrc.auth.form.RoleForm" />
<html:html locale="true">
<head>
	<html:base />
	<title>登录分行系统</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" rel="stylesheet" type="text/css">
</head>

<script>
	function getOrgIdValue(){
		var orgName=document.getElementById('orgName');   
		
		window.location="<%=request.getContextPath()%>/system_mgr/viewAllRole.do?orgId="+orgName.value;     
            return false;
       
    }
   
    function newLogin(){
    	var roleId=document.getElementById('roleName'); 
    	if(roleId.value==""){
    	 alert("请选择角色!");
    	 return;	
    	}
    	 
 	window.top.location="<%=request.getContextPath()%>/system_mgr/searchUserName.do?" + 
		     		"roleId=" + roleId.value;
		 //  closeWindow();
		     		
	}
	function closeWindow(){
		window.opener=null;
		window.top.close();
    }	
    
</script>

<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<br>
	<table width="80%" border="0" align="center">
		<tr>
			<td align="center">
				<strong>登录分行系统:</strong> &nbsp;
				
			</td>
		</tr>
		<tr>
			<td>
				<div id=location>
					
				</div>
			</td>
		</tr>
	</table>
	<html:form  action="/system_mgr/viewAllRole.do" method="post">
		<table width="60%" border="0" align="center">
			<tr>
				<td width="50%" align="center" valign="middle">
					分行机构:
				</td>
			</tr>
			<tr>
				<td align="center">
					<html:select styleId="orgName" name="orgNetForm" property="org_name" onchange="getOrgIdValue()" style="width:250">				
						<logic:present name="Records" scope="request">
						<html:option value="-1">--请选择分行机构--</html:option>
							<html:optionsCollection name="Records" label="label" value="value" />
						</logic:present>
					</html:select>
				</td>
			</tr>
			<tr>
			</tr>
			<tr>
			</tr>
			<tr>
			</tr>
			<tr>
				<td width="50%" align="center" valign="middle">
					角色:
				</td>
			</tr>
			<tr>
				<td align="center">
					<html:select styleId="roleName" property="allRole" size="15" style="width:250">

					<logic:equal name="isFlag" value="isSearch">
						<logic:present name="AllRole" scope="request">							
							<html:optionsCollection name="AllRole" label="label" value="value" />
						</logic:present>
					</logic:equal>

					</html:select>
					
				</td>
			</tr>

		</table>

		<table width="80%" border="0" align="center">
			<tr>
				<td>
					<div id=location></div>
				</td>
			</tr>
			<tr>
				<td align="right">
					<html:button property="add" value="登录分行系统" styleClass="input-button" onclick="newLogin()" />
					&nbsp;
				</td>
			</tr>
		</table>
	</html:form>
</body>
</html:html>
