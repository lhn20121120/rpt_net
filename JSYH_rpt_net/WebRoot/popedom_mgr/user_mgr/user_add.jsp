
<%@ page language="java" pageEncoding="GBK"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ page import="com.fitech.net.common.StringTool"%>
<%
	Operator operator=(Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String orgId=operator.getOrgId();
	String subOrgIds = operator.getSubOrgIds();
%>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.auth.util.AuthUtil" >
<jsp:setProperty name="utilForm" property="orgId" value="<%=orgId%>" /> 
<jsp:setProperty name="utilForm" property="subOrgIds" value="<%=subOrgIds%>"/>
</jsp:useBean>

<html:html locale="true">
<head>
	<html:base/>
<title>用户信息管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript">
		function validate(){
			//用户名
			var userName = document.getElementById('userName');
			//密码
			var password = document.getElementById('password');
			//重复密码
			var password1 = document.getElementById('password1');
			//姓
			var fristName = document.getElementById('fristName');
			//名
			var lastName = document.getElementById('lastName');
			//电话
			var telephoneNumber = document.getElementById('telephoneNumber');
			//性别
			var sex = document.getElementById('sex');
			//年龄
			var age = document.getElementById('age');
			//传真
			var fax = document.getElementById('fax');
			//职务
			var title = document.getElementById('title');
			//职称
			var employeeType = document.getElementById('employeeType');
			//属于部门
			var departmentId = document.getElementById('departmentId');
			//邮政编码
			var postalCode = document.getElementById('postalCode');
			//电子邮件				
			var mail = document.getElementById('mail');
			//主管领导
			var manager = document.getElementById('manager');
			//身份证号
			var identificationNumber = document.getElementById('identificationNumber');
			//工作证号
			var employeeNumber = document.getElementById('employeeNumber');
			//工作地址				
			var address = document.getElementById('address');
			//通信地址
			var postalAddress = document.getElementById('postalAddress');
				
			if(userName.value.Trim()==""){
				alert('用户名不能为空！');
				userName.focus();
				return false;
			}
            else //--------------------------gongming 2008-07-25---------
            {
                var length = userName.value.length;
                var name = userName.value;
                var sum = 0;
                for(i=0;i< length;i++)
                {
                  if ((name.charCodeAt(i)>=0) && (name.charCodeAt(i)<=128))
                    sum = sum + 1;
                  else
                    sum = sum + 2;
                }
                if(sum > 20)
                {
                  alert('用户名称长度过长!');
                  userName.focus();
                  return false;
                }
            }
            //----------------------------------------------------------------
			if(password.value==""){
				alert("密码不能为空！");
				password.focus();
				return false;					
			}
			if(password.value!=password1.value){
				alert('两次输入密码不一致！');
				password.focus();
				return false;
			}		
				
<%--			if(telephoneNumber.value!=telephoneNumber.value){--%>
<%--				alert('办公室电话不能为空！');--%>
<%--				password.focus();--%>
<%--				return false;--%>
<%--			}		--%>
			if(telephoneNumber.value.Trim()!=""){
				if(CheckTel(telephoneNumber.value)==false){
					alert("请输入正确格式的电话号码！");
					telephoneNumber.focus();
					return false;
				}
			}
			if(fax.value.Trim()!=""){
				if(CheckTel(fax.value)==false){
					alert("请输入正确格式的传真号码！");
					fax.focus();
					return false;
				}
			}
			if(postalCode.value.Trim()!=""){
				if(CheckZip(postalCode.value)==false){
					alert("请输入正确格式的邮政编码！");
					postalCode.focus();
					return false;
				}
			}
<%--			if(identificationNumber.value.Trim()!=""){--%>
<%--				if(CheckNum15(identificationNumber.value)==false && CheckNum18(identificationNumber.value)==false){--%>
<%--					alert("请输入正确格式的身份证号码！15位或18位！");--%>
<%--					identificationNumber.focus();--%>
<%--					return false;--%>
<%--				}--%>
<%--			}--%>
			
				if(identificationNumber.value.Trim()!=""){
				if(CheckTel(identificationNumber.value)==false){
					alert("请输入正确的手机号码!");
					identificationNumber.focus();
					return false;
				}
			}
			if(mail.value.Trim()!=""){
				if(CheckEmail(mail.value)==false){
					alert("请输入正确格式的E-Mail！");
					mail.focus();
					return false;
				}
			}		
			
						
			return true;
		}
			
		//检查
		function Check( reg, str ){
			if( reg.test( str ) ){
				return true;
			}
			return false;
		}
		// 检查数字
		function CheckNumber( str ){
			var reg = /^\d*(?:$|\.\d*$)/;
			return Check( reg, str );
		}
		//检查邮编
		function CheckZip( str ){
			var reg = /^\d{6}$/;
			return Check( reg, str );
		}
		// 检查Email
		function CheckEmail( str ){
			var reg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
			return Check( reg, str );
		}
		//检查电话，传真
		function CheckTel(str){
			var reg =/^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/;
			return Check( reg, str );
		}
		// 15位身份证号
		function CheckNum15( str ){
			var reg = /^\d{15}$/;
			return Check( reg, str );
		}			
		// 18位身份证号
		function CheckNum18( str ){
			var reg = /^\d{17}(?:\d|x)$/;
			return Check( reg, str );
		}			
		function goBack(){
			window.location="<%=request.getContextPath()%>/popedom_mgr/viewOperator.do";
		}
		String.prototype.Trim=function(){
			return this.replace(/(^\s*)|(\s*$)/g,"");
		}	
		
		//设置用户机构下列框的数据
		function setSelectData(){			
			//用户所属机构
			var orgId = document.getElementById('orgId');				
			for(var i=0;i<orgId.length;i++){
		  		if(orgId.options[i].value == "<%=orgId%>"){		
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
			 <td>当前位置 >> 权限管理 >>添加用户信息</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
	<br>
	<table width="100%" height=100% border="0" cellspacing="0" cellpadding="0" align="center">	
		<tr> 
    		<td align="right" valign="top">
    		<html:form action="/popedom_mgr/insertOperator" method="Post" styleId="form1" onsubmit="return validate()">
				<table width="90%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
      				<tr id="tbcolor">
            			<th align="center">添加用户信息</th>
      				</tr>
   					<tr>
       					<td height="204" align="right" bgcolor="#FFFFFF">       
							<table width="100%"  border="0">
								<tr> 
									<td colspan="6" align="right"><div id=location> 
										<div align="left"><strong>帐户信息</strong></div>
	                    		 	</td>                    		 
								</tr>
	                			<tr> 
									<td align="right">用户名：</td>
	                  				<td>
	                  					<html:text styleId="userName" property="userName" size="20" styleClass="input-text" maxlength="20"/>
	                    				<strong><font color="#FF0000">*</font></strong> <strong> </strong>
	                  				</td>
	                  
	                  				<td align="right">用户机构：</td>
	                  				<td>
	                  					<html:select styleId="orgId" property="orgId">
		                          			<html:optionsCollection name="utilForm" property="subOrgListWithIds" label="label" value="value" />
		                    			</html:select>
		                    			<strong><font color="#FF0000">*</font></strong> <strong> </strong>                    				
	                  				</td>
	                			</tr>
	                			<tr> 
									<td align="right">密码：</td>
	                  				<td> 
	                  					<html:password styleId="password" property="password" size="20" maxlength="20" styleClass="input-text" />
	                  	 				<strong><font color="#FF0000">*</font></strong> <strong> </strong>
	                  				</td>                  
	                  				<td align="right">重复密码：</td>
	                  				<td colspan="3">
	                  					<html:password styleId="password1" property="password1" size="20" maxlength="20" styleClass="input-text" />
	                     				<strong><font color="#FF0000">*</font></strong> <strong> </strong>
	                  				</td>
	                			</tr>
	                			<tr> 
	                  				<td colspan="6" align="right">
	                  					<div id=location><div align="left"><strong>用户信息</strong></div></div>
	                  				</td>
	                			<tr> 
	                  				<td align="right">姓</td>
	                  				<td>
	                  					<html:text styleId="fristName" property="firstName" size="10" styleClass="input-text" maxlength="50" />
	                  				</td>
	                  				<td align="right">名</td> 
	                  				<td>
	                  					<html:text styleId="lastName" property="lastName" size="10" styleClass="input-text" maxlength="50"/>
	                  				</td>                  
	                 				<td align="right">性别</td>
	                  				<td>
	                  					<html:select styleId="sex" property="sex">
		                      				<html:option value="男">男</html:option>
		                      				<html:option value="女">女</html:option>
	                    				</html:select>
	                  				</td>
	              				</tr>
	                			<tr>         
	        						<td align="right">办公室电话</td>
	                  				<td>
	                  					<html:text styleId="telephoneNumber" property="telephoneNumber" size="20" maxlength="20" styleClass="input-text" />
<%--	                  					<strong><font color="#FF0000">*</font></strong> <strong> </strong>--%>
	                  				</td>                  
	                    			<td align="right">手机号码</td>
	                  				<td>
	                  					<html:text styleId="identificationNumber" property="identificationNumber" size="20" maxlength="18" styleClass="input-text" />
	                   				</td>
	                  				<td align="right">传真</td>
	                  				<td>
	                  					<html:text styleId="fax" property="fax" size="20" maxlength="20" styleClass="input-text" />
	                  				</td>
	                			</tr>
	                			<tr> 
	                				<!--<td align="right">部门领导</td>
	                  				<td>
	                  					<html:text styleId="manager" property="manager" size="20" styleClass="input-text" />
	                  					<strong><font color="#FF0000">*</font></strong> <strong> </strong>
	                  				</td>	                				 
	                  				-->
	                  				<td align="right">系统操作级别</td>
	                  				<td>
	                  					<html:text styleId="title" property="title" size="20" maxlength="10" styleClass="input-text" />
	                  				</td>
	                  				<td align="right">主管行长</td>
	                  				<td>
	                  					<html:text styleId="employeeType" property="employeeType" size="10" maxlength="20" styleClass="input-text" />
	                  				</td>
	                  				<td align="right">所属部门</td>
	                  				<td>
	                  					<html:select styleId="departmentId" property="departmentId">
	                          				<html:optionsCollection name="utilForm" property="deptList" label="label" value="value" />
	                    				</html:select>
	                  				</td>   
	                			</tr>
	                			<tr> 
	                				<td align="right">邮政编码</td>
	                  				<td>
	                  					<html:text styleId="postalCode" property="postalCode" size="20" maxlength="20" styleClass="input-text" />
	                  				</td>
	                  				<td align="right">电子邮件</td>
	                   				<td>
	                 	 				<html:text styleId="mail" property="mail" size="20" styleClass="input-text" maxlength="50" />
	                  				</td>                  
	                  				
	                			</tr>
				                <tr> 
					                <td align="right">通信地址</td>
					                <td colspan="5">
				                  		<html:text styleId="postalAddress" property="postalAddress" size="20" maxlength="25" styleClass="input-text" style="width:100% "/>
				                  	</td>
				                </tr>
	                			<tr> 
									<td align="right">备注1:</td>
	                  				<td colspan="5">
	                  					<html:text styleId="address" property="address" size="20" maxlength="50" styleClass="input-text" style="width:100% "/>
	                  				</td>
	                			</tr>
	                  			<tr> 
	                  				<td align="right">备注2:</td>
	                   				<td colspan="5">
	                  					<html:text styleId="employeeNumber" property="employeeNumber" size="20" maxlength="10" styleClass="input-text" style="width:100% "/>
	                  				</td>
	                			</tr>
	                			<script language="javascript">
                					setSelectData();
                				</script>
					  			<tr> 
	                  				<td colspan="6" align="right">
	                  					<html:submit value="保存" styleClass="input-button"/>&nbsp;
	                   					<html:button property="back" value="返回" styleClass="input-button" onclick="goBack()"/>
	                   				</td>
	                			</tr>
              				</table>							
		  				</td>
   					</tr>    
				</table>
			</html:form>
			</td>
		</tr>
	</table>
</body>
</html:html>
