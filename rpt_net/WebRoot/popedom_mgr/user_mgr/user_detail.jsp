
<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
<title>用户信息管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
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
		 <table cellspacing="0" cellpadding="0" border="0" width="98%">
		<tr>
			<td height="5"></td>
		</tr>
		<tr>
			 <td>当前位置 >> 权限管理 >>用户详细信息</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
	<br>
<table width="100%" height=100% border="0" cellspacing="0" cellpadding="0" align="center">
	
	<tr> 
    	<td align="right" valign="top">
			<table width="90%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
      			<logic:present name="Detail" scope="request">
      				<tr class="titletab">
            			<th align="center">用户详细信息</th>
      				</tr>
   					<tr>
       					 <td height="204" align="right" bgcolor="#FFFFFF">          
						<table width="100%"  border="0" >
			                <tr> 
			                  <td colspan="6" align="right"><div id=location> 
                   			   <div align="left"><strong>帐户信息</strong></div>
                    </td>
                </tr>
                <tr> 
                  <td align="right">
                  	用户名
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="userName"/>" readonly>
                  </td>
                </tr>
                <tr> 
                  <td colspan="6" align="right"><div id=location> 
                      <div align="left"><strong>用户信息</strong></div>
                    </div></td>
                <tr> 
                  <td align="right">
                  		姓
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="firstName"/>" readonly>
                  </td>
                  <td align="right">
                  		名
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="lastName"/>" readonly>
                  </td>
                  <td align="right">
                  	性别
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="sex"/>" readonly>
                  </td>
                  
                 </tr>
                <tr> 
                  <td align="right">
                  		办公室电话
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="telephoneNumber"/>" readonly>
                  </td>
                  
                    <td align="right">
                  	手机号码
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="identificationNumber"/>" readonly>
                  </td>  
                  <td align="right">
                  	传真
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="fax"/>" readonly>
                  </td>
                </tr>
                <tr> 
                 <td align="right">
                  	 系统操作级别
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="title"/>" readonly>
                  </td>
                  <td align="right">
                  	主管行长
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="employeeType"/>" readonly>
                  </td>
                  <td align="right">
                  	所属部门
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="deptName"/>" readonly>
                  </td> 
                </tr>
                <tr> 
                  <td align="right">
                  	邮政编码
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="postalCode"/>" readonly>
                  </td>
                  <td align="right">
                  	电子邮件
                  </td>
                  <td >
                  	<input type="text" name="" size="20" class="input-text"  value="<bean:write name="Detail" property="mail"/>" readonly>
                  </td>
                  
                  <td align="right">
                  	部门领导
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="manager"/>" readonly>
                  </td>
                </tr>
                <tr> 
                
                <td align="right">通信地址</td>
                 <td colspan="5">
                  	<input type="text" name="" size="42"style="width:100% " class="input-text" value="<bean:write name="Detail" property="postalAddress"/>" readonly>
                  </td>
                </tr>
                <tr> 
                  <td align="right">
                  		备注1:
                  </td>
                  <td colspan="5">
                  		<input type="text" name="" size="42"style="width:100% " class="input-text" value="<bean:write name="Detail" property="address"/>" readonly>
                  </td>
                </tr>
                  <tr> 
                  <td align="right">
                  		备注2:
                  </td>
                  <td colspan="3">
                  	<input type="text" name="" size="42"style="width:100% " class="input-text" value="<bean:write name="Detail" property="employeeNumber"/>" readonly>
                  </td>   
                </tr>
             </logic:present>
                <tr> 
                  <td colspan="6" align="right">
                   		<input type="button" name="Submit" value="返回" class="input-button1" onClick="window.location.assign('../popedom_mgr/viewOperator.do')">
                   </td>
                </tr>
              </table>
		  </td>
   </tr>    
 </table>
</body>

</html:html>
