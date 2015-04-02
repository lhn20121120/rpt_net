<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ page import="com.fitech.net.common.StringTool"%>
<%
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String orgId = operator.getOrgId();
	String subOrgIds = operator.getSubOrgIds();
%>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.auth.util.AuthUtil" >
	<jsp:setProperty name="utilForm" property="orgId" value="<%=orgId%>" /> 
	<jsp:setProperty name="utilForm" property="subOrgIds" value="<%=subOrgIds%>"/>
</jsp:useBean>
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
		function validate(){
		
			//�û���
			var userName = document.getElementById('userName');
			//����
			var password = document.getElementById('password');
			//�ظ�����
			var password1 = document.getElementById('password1');
			//��
			var fristName = document.getElementById('firstName');
			//��
			var lastName = document.getElementById('lastName');
			//�绰
			var telephoneNumber = document.getElementById('telephoneNumber');
			//�Ա�
			var sex = document.getElementById('sex');
			//����
			var age = document.getElementById('age');
			//����
			var fax = document.getElementById('fax');
			//ְ��
			var title = document.getElementById('title');
			//ְ��
			var employeeType = document.getElementById('employeeType');
			//���ڲ���
			var departmentId = document.getElementById('departmentId');
			//��������
			var postalCode = document.getElementById('postalCode');
			//�����ʼ�				
			var mail = document.getElementById('mail');
			//�����쵼
			var manager = document.getElementById('manager');
			//����֤��
			var identificationNumber = document.getElementById('identificationNumber');
			//����֤��
			var employeeNumber = document.getElementById('employeeNumber');
			//������ַ				
			var address = document.getElementById('address');
			//ͨ�ŵ�ַ
			var postalAddress = document.getElementById('postalAddress');
			
			if(password.value!=password1.value){
				alert('�����������벻һ�£�');
				password.focus();
				return false;
			}	
			
			if(userName.value.Trim()==""){
				alert('�û�������Ϊ�գ�');
				userName.focus();
				return false;
			}
			if(fristName.value==""){
				alert("�ղ���Ϊ��!");
				fristName.focus();
				return false;
			}	
			if(lastName.value==""){
				alert("������Ϊ��!");
				lastName.focus();
				return false;
			}

<%--			if(telephoneNumber.value!=telephoneNumber.value){--%>
<%--				alert('�칫�ҵ绰����Ϊ�գ�');--%>
<%--				password.focus();--%>
<%--				return false;--%>
<%--			}		--%>
			if(telephoneNumber.value.Trim()!=""){
				if(CheckTel(telephoneNumber.value)==false){
					alert("��������ȷ��ʽ�ĵ绰���룡");
					telephoneNumber.focus();
					return false;
				}
			}
			if(fax.value.Trim()!=""){
				if(CheckTel(fax.value)==false){
					alert("��������ȷ��ʽ�Ĵ�����룡");
					fax.focus();
					return false;
				}
			}
			if(postalCode.value.Trim()!=""){
				if(CheckZip(postalCode.value)==false){
					alert("��������ȷ��ʽ���������룡");
					postalCode.focus();
					return false;
				}
			}
<%--			if(identificationNumber.value.Trim()!=""){--%>
<%--				if(CheckNum15(identificationNumber.value)==false && CheckNum18(identificationNumber.value)==false){--%>
<%--					alert("��������ȷ��ʽ������֤���룡15λ��18λ��");--%>
<%--					identificationNumber.focus();--%>
<%--					return false;--%>
<%--				}--%>
<%--			}--%>
			
				if(identificationNumber.value.Trim()!=""){
				if(CheckTel(identificationNumber.value)==false){
					alert("��������ȷ���ֻ�����!");
					identificationNumber.focus();
					return false;
				}
			}
			if(mail.value.Trim()!=""){
				if(CheckEmail(mail.value)==false){
					alert("��������ȷ��ʽ��E-Mail��");
					mail.focus();
					return false;
				}
			}		
			
						
			return true;
		}
			
		//���
		function Check( reg, str ){
			if( reg.test( str ) ){
				return true;
			}
			return false;
		}
		// �������
		function CheckNumber( str ){
			var reg = /^\d*(?:$|\.\d*$)/;
			return Check( reg, str );
		}
		//����ʱ�
		function CheckZip( str ){
			var reg = /^\d{6}$/;
			return Check( reg, str );
		}
		// ���Email
		function CheckEmail( str ){
			var reg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
			return Check( reg, str );
		}
		//���绰������
		function CheckTel(str){
			var reg =/^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/;
			return Check( reg, str );
		}
		// 15λ����֤��
		function CheckNum15( str ){
			var reg = /^\d{15}$/;
			return Check( reg, str );
		}			
		// 18λ����֤��
		function CheckNum18( str ){
			var reg = /^\d{17}(?:\d|x)$/;
			return Check( reg, str );
		}			
	 
		function goBack(){
			window.location="<%=request.getContextPath()%>/popedom_mgr/viewOperator.do?curPage=<%=request.getAttribute("curPage")%>";
		}
		String.prototype.Trim=function(){
			return this.replace(/(^\s*)|(\s*$)/g,"");
		}	
		//������ͬ������
		function setSelectData(){
			//���ݿ��е��Ա�
			var sexValue = "<bean:write name="Detail" property="sex"/>";				
			//�Ա�
			var sex = document.getElementById('sex');
			if(sexValue=="Ů")
				sex.selectedIndex = 1;
				
			//���ڲ���
			var departmentList = document.getElementById('departmentId');				
			for(var i=0;i<departmentList.length;i++){
		  		if(departmentList.options[i].value == "<bean:write name="Detail" property="departmentId"/>"){		
		  			departmentList.selectedIndex = i;
		  			break;
		  		}
		  	}
		  	
		  	//�û���������
			var orgId = document.getElementById('orgId');				
			for(var i=0;i<orgId.length;i++){
		  		if(orgId.options[i].value == "<bean:write name="Detail" property="orgId"/>"){		
		  			orgId.selectedIndex = i;
		  			break;
		  		}
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
			 <td>��ǰλ�� >> Ȩ�޹��� >>�û���Ϣ�޸�</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
	<br>
	<html:form action="/popedom_mgr/updateOperator" method="Post"  onsubmit="return validate()">
		<table width="100%" height=100% border="0" cellspacing="0" cellpadding="0" align="center">	
			<tr> 
	    		<td align="right" valign="top">
					<table width="90%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
	      				<logic:present name="Detail" scope="request">
		      				<tr class="titletab">
		            			<th align="center">�û���Ϣ�޸�</th>	
		      				</tr>
	   						<tr>
								<td height="204" align="right" bgcolor="#FFFFFF">          
									<table width="100%"  border="0">
										<tr> 
				                  			<td colspan="6" align="right"><div id=location> 
	                   			   				<div align="left"><strong>�ʻ���Ϣ</strong></div>
	                    					</td>
	                					</tr>
	                					<tr> 
	                  						<td align="right">�û���</td>
	                  						<td>
							                  	<input type="hidden" name="userId" value="<bean:write name="Detail" property="userId"/>">
							                  	<input type="text" name="userName" size="20" class="input-text" value="<bean:write name="Detail" property="userName"/>" readonly="readonly" style="color: #c8c8c8">
							                  	<strong><font color="#FF0000">*</font></strong> <strong> </strong>
	                  						</td>
	                  						<td align="right">�û�������</td>
	                  						<td>
	                  							<html:select styleId="orgId" property="orgId">
	                  								<html:optionsCollection name="utilForm" property="subOrgListWithIds" label="label" value="value" />
		                    					</html:select>	
		                    					<strong><font color="#FF0000">*</font></strong> <strong> </strong>	                    				
	                  						</td>	                  						
	                					</tr>
	                 					<tr> 
	                  						<td align="right">���룺</td>
	                  						<td> 
	                  						<%if(Config.PORTAL){
	                  							%>
	                  							<html:password disabled="true" styleId="password" property="password" size="20" maxlength="20" styleClass="input-text" />
	                  							<%
	                  						}else{
	                  						%>
	                  						<html:password styleId="password" property="password" size="20" maxlength="20" styleClass="input-text" />
	                  						<%
	                  						}
	                  					 %>
	                  							                  
	                  							<strong><font color="#FF0000">*</font></strong> <strong> </strong>
	                  						</td>                  
	                  						<td align="right">�ظ����룺</td>
	                  						<td colspan="3">
	                  						
	                  						<%if(Config.PORTAL){
	                  							%>
	                  							<html:password disabled="true" styleId="password1" property="password1" size="20" maxlength="20" styleClass="input-text" />
	                  							<%
	                  						}else{
	                  						%>
	                  						<html:password styleId="password1" property="password1" size="20" maxlength="20" styleClass="input-text" />
	                  						<%
	                  						}
	                  					 %>
	                     						<strong><font color="#FF0000">*�粻�޸������뱣������Ϊ�գ�</font></strong> <strong> </strong>
	                  						</td>
	                					</tr>
	                					<tr> 
	                  						<td colspan="6" align="right"><div id=location><div align="left"><strong>�û���Ϣ</strong></div></div></td>
	                					<tr> 
	                  						<td align="right">��</td>
	                  						<%if(Config.PORTAL){ %>
	                  						<td>
	                  							<input readonly="readonly" type="text" name="firstName" size="20" maxlength="25" class="input-text" value="<bean:write name="Detail" property="firstName"/>">
	                  							<strong><font color="#FF0000">*</font></strong>
	                  						</td>
	                  						<%}else{ %>
	                  						<td>
	                  							<input type="text" name="firstName" size="20" maxlength="25" class="input-text" value="<bean:write name="Detail" property="firstName"/>">
	                  							<strong><font color="#FF0000">*</font></strong>
	                  						</td>
	                  						<%} %>
	                  						<td align="right">��</td>
	                  						<%if(Config.PORTAL){ %>
	                  							<td>
	                  							<input readonly="readonly" type="text" name="lastName" size="20" maxlength="25" class="input-text" value=" ">
	                  							<strong><font color="#FF0000">*</font></strong>
	                  						</td>
	                  						<%}else{ %>
	                  						<td>
	                  							<input type="text" name="lastName" size="20" maxlength="25" class="input-text" value="<bean:write name="Detail" property="lastName"/>">
	                  							<strong><font color="#FF0000">*</font></strong>
	                  						</td>
	                  						<%} %>
	                  						<td align="right">�Ա�</td>
	                  						<td>
	                  							<html:select styleId="sex" property="sex">
	                      							<html:option value="��">��</html:option>
	                      							<html:option value="Ů">Ů</html:option>
	                    						</html:select>
	                  						</td>
	                 					</tr>
	                					<tr> 
	                						<td align="right">�칫�ҵ绰</td>
	                  						<td>
	                  							<input type="text" name="telephoneNumber" size="20" maxlength="20" class="input-text" value="<bean:write name="Detail" property="telephoneNumber"/>">
<%--	                  							<strong><font color="#FF0000">*</font></strong> <strong> </strong>--%>
	                  						</td>                  
	                  						<td align="right">�ֻ�����</td>
	                  						<%if(Config.PORTAL) {%>
	                  						
	                  						<td>
	                  							<input  readonly="readonly" type="text" name="identificationNumber" size="20" maxlength="18" class="input-text" value="<bean:write name="Detail" property="identificationNumber"/>">
	                  							<strong><font color="#FF0000">*</font></strong>
	                  						</td>
	                  						
	                  						<%}else{ %>
	                  						
	                  						<td>
	                  							<input type="text" name="identificationNumber" size="20" maxlength="18" class="input-text" value="<bean:write name="Detail" property="identificationNumber"/>">
	                  							<strong><font color="#FF0000">*</font></strong>
	                  						</td>
	                  						<%} %>
	                  						<td align="right">����</td>
	                  						<td>
	                  							<input type="text" name="fax" size="20" maxlength="20" class="input-text" value="<bean:write name="Detail" property="fax"/>">
	                  						</td>
	                					</tr>               
	                					<tr> 
	                  						<td align="right">ϵͳ��������</td>
	                  						<td>
	                  							<input type="text" name="title" size="20" maxlength="10" class="input-text" value="<bean:write name="Detail" property="title"/>">
	                  						</td>
	                  						<td align="right">�����г�</td>
	                  						<td>
	                  							<input type="text" name="employeeType" size="20" maxlength="10" class="input-text" value="<bean:write name="Detail" property="employeeType"/>">
	                  						</td>
	                  						<td align="right">��������</td>
	                  						<td>
	                  							<html:select styleId="departmentId" property="departmentId">
	                         						<html:optionsCollection name="utilForm" property="deptList" label="label" value="value" />
	                    						</html:select>
	                  						</td> 
	                					</tr>
	                					<tr> 
	                  						<td align="right">��������</td>
	                  						<td>
	                  							<input type="text" name="postalCode" size="20" maxlength="20" class="input-text" value="<bean:write name="Detail" property="postalCode"/>">
	                  						</td>
	                  						<td align="right">�����ʼ�</td>
	                  						<td >
	                  							<input type="text" name="mail" size="20" maxlength="50" class="input-text"  value="<bean:write name="Detail" property="mail"/>">
	                  						</td>
	                  						<!--<td align="right">�����쵼</td>
	                  						<td>
	                  							<input type="text" name="manager" size="20" class="input-text" value="<bean:write name="Detail" property="manager"/>">
	                  						</td>
	                					--></tr>
	                					<tr> 
											<td align="right">ͨ�ŵ�ַ</td>
											<td colspan="5">
	                  							<input type="text" name="postalAddress" size="42" maxlength="25" style="width:100% " class="input-text" value="<bean:write name="Detail" property="postalAddress"/>">
	                  						</td>
	                					</tr>
	                					<tr> 
	                  						<td align="right">��ע1:</td>
	                  						<td colspan="5">
	                  							<input type="text" name="address" size="42" maxlength="50" style="width:100% " class="input-text" value="<bean:write name="Detail" property="address"/>">
	                  						</td>
	                					</tr>
	                  					<tr> 
	                  						<td align="right">��ע2:</td>
	                  						<td colspan="5">
	                  							<input type="text" name="employeeNumber" size="42" maxlength="10" style="width:100% " class="input-text" value="<bean:write name="Detail" property="employeeNumber"/>">
	                  						</td> 
	                					</tr>
	                					<tr> 
					                  		<td colspan="6" align="right">
					                  			<html:submit value="����" styleClass="input-button"/>
					                   			<input type="button" name="Submit" value="����" class="input-button" onClick="javascript:goBack()">
					                   		</td>
					                	</tr>
				                	</table>
				                </td>
							</tr>						
			                <script language="javascript">
			                	setSelectData();
			                </script>
	             		</logic:present>
	                	
					</table>
				</td>
			</tr>    
		</table>
	</html:form>
</body>
</html:html>