<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
	<head>
		<html:base/>
		<title>�޸�����</title>	
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../css/common.css" rel="stylesheet" type="text/css">
		<script language="javascript">
			function checkPwd(){
				//������
				var passwordOld = document.getElementById('passwordOld');
				//������
				var password = document.getElementById('password');
				//�ظ�����
				var password1 = document.getElementById('password1');
								
				if(password.value.length < 6){
					alert("Ϊ�����İ�ȫ����������Ҫ6~20λ��ĸ�����֣�");
					password.focus();
					return false;
				}
				if(password1.value.length < 6){
					alert("Ϊ�����İ�ȫ����������Ҫ6~20λ��ĸ�����֣�");
					password1.focus();
					return false;
				}
				if(password.value != password1.value){
					alert("����������������벻һ��!");
					password1.focus();
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
	<table cellspacing="0" cellpadding="0" border="0" width="98%">
		<tr>
			<td height="5"></td>
		</tr>
		<tr>
			 <td>��ǰλ�� >> ϵͳ���� >> �޸�����</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
	<br>
		<br>
		<html:form action="/popedom_mgr/updatePwdOperator" method="post" styleId="form1" onsubmit="return checkPwd()" >
	    <table width="80%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
		    
		      <tr class="titletab">
		            <th align="center">���ĵ�ǰ�û�����</th>
		      </tr>
		   
		      <tr>
			      <td bgcolor="#ffffff">
			      <table width="100%" border="0" align="center">
						  <tr bgcolor="#ffffff">
					      	<td height="7">
					      	</td>
					      </tr>		      
			          	  <tr>
				      	  	<td align="right">ԭ��¼���룺</td>
		                  	<td> 
			                  	<html:password styleId="passwordOld" property="passwordOld" size="20" styleClass="input-text" maxlength="20" value=""/>
		                  	</td>
				          </tr>
				          <tr>
				          	<td align="right">�����룺</td>
			                <td>
			                  	<html:password styleId="password" property="password" size="20" styleClass="input-text" maxlength="20" value=""/>
			                </td>
				          </tr>
				          <tr>
				          	<td align="right">ȷ�������룺</td>
		                  	<td>
			                  	<html:password styleId="password1" property="password1" size="20" styleClass="input-text" maxlength="20" value=""/>
		                  	</td>
				          </tr>
				          <tr>
				              <td colspan="2" align="right" bgcolor="#ffffff">
				            	 <html:submit value="ȷ ��" styleClass="input-button"/>
				              </td>
				          </tr>
   	
			      	</table>
			      </td> 
		       </tr>
		      </table>
        </html:form>
	</body>
</html:html>
