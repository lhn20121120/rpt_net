<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.security.Operator" %>
<%@ page import="com.cbrc.smis.common.Config" %>
<%@page import="com.cbrc.auth.form.OperatorForm"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html>
  <script language="javascript">
  	function init(){
  		var form1=document.getElementById("form1");
  //		var user=document.getElementsByName("userInfoBean.userInfo.userId");
  //		var pas=document.getElementsByName("userInfoBean.userInfo.Password");
  //		alert(user[0].value);
  //		alert(pas[0].value);
  		form1.submit();
  	}
  </script>
  <body bgcolor="#FFFFFF">
	<form id="form1" action="<%=Config.FITOSA_URL%>" method="POST">  
	<%
		OperatorForm user=(OperatorForm)session.getAttribute("OPERATORFORM");
		
	%>
	<input type="hidden" name="userInfoBean.userInfo.userId" value="<%=user.getUserName()%>"/>
	<input type="hidden" name="userInfoBean.userInfo.Password" value="<%=user.getPassword()%>"/>
	</form>
  </body>
  <script language="javascript">
   init();
  </script>
   
</html>
