
<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
<title>�û���Ϣ����</title>
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
			 <td>��ǰλ�� >> Ȩ�޹��� >>�û���ϸ��Ϣ</td>
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
            			<th align="center">�û���ϸ��Ϣ</th>
      				</tr>
   					<tr>
       					 <td height="204" align="right" bgcolor="#FFFFFF">          
						<table width="100%"  border="0" >
			                <tr> 
			                  <td colspan="6" align="right"><div id=location> 
                   			   <div align="left"><strong>�ʻ���Ϣ</strong></div>
                    </td>
                </tr>
                <tr> 
                  <td align="right">
                  	�û���
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="userName"/>" readonly>
                  </td>
                </tr>
                <tr> 
                  <td colspan="6" align="right"><div id=location> 
                      <div align="left"><strong>�û���Ϣ</strong></div>
                    </div></td>
                <tr> 
                  <td align="right">
                  		��
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="firstName"/>" readonly>
                  </td>
                  <td align="right">
                  		��
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="lastName"/>" readonly>
                  </td>
                  <td align="right">
                  	�Ա�
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="sex"/>" readonly>
                  </td>
                  
                 </tr>
                <tr> 
                  <td align="right">
                  		�칫�ҵ绰
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="telephoneNumber"/>" readonly>
                  </td>
                  
                    <td align="right">
                  	�ֻ�����
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="identificationNumber"/>" readonly>
                  </td>  
                  <td align="right">
                  	����
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="fax"/>" readonly>
                  </td>
                </tr>
                <tr> 
                 <td align="right">
                  	 ϵͳ��������
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="title"/>" readonly>
                  </td>
                  <td align="right">
                  	�����г�
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="employeeType"/>" readonly>
                  </td>
                  <td align="right">
                  	��������
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="deptName"/>" readonly>
                  </td> 
                </tr>
                <tr> 
                  <td align="right">
                  	��������
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="postalCode"/>" readonly>
                  </td>
                  <td align="right">
                  	�����ʼ�
                  </td>
                  <td >
                  	<input type="text" name="" size="20" class="input-text"  value="<bean:write name="Detail" property="mail"/>" readonly>
                  </td>
                  
                  <td align="right">
                  	�����쵼
                  </td>
                  <td>
                  	<input type="text" name="" size="20" class="input-text" value="<bean:write name="Detail" property="manager"/>" readonly>
                  </td>
                </tr>
                <tr> 
                
                <td align="right">ͨ�ŵ�ַ</td>
                 <td colspan="5">
                  	<input type="text" name="" size="42"style="width:100% " class="input-text" value="<bean:write name="Detail" property="postalAddress"/>" readonly>
                  </td>
                </tr>
                <tr> 
                  <td align="right">
                  		��ע1:
                  </td>
                  <td colspan="5">
                  		<input type="text" name="" size="42"style="width:100% " class="input-text" value="<bean:write name="Detail" property="address"/>" readonly>
                  </td>
                </tr>
                  <tr> 
                  <td align="right">
                  		��ע2:
                  </td>
                  <td colspan="3">
                  	<input type="text" name="" size="42"style="width:100% " class="input-text" value="<bean:write name="Detail" property="employeeNumber"/>" readonly>
                  </td>   
                </tr>
             </logic:present>
                <tr> 
                  <td colspan="6" align="right">
                   		<input type="button" name="Submit" value="����" class="input-button1" onClick="window.location.assign('../popedom_mgr/viewOperator.do')">
                   </td>
                </tr>
              </table>
		  </td>
   </tr>    
 </table>
</body>

</html:html>
