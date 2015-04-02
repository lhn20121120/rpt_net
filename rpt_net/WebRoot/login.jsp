<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@page import="com.fitech.gznx.common.Config"%>
<%@page import="com.fitech.gznx.common.DateUtil"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.text.SimpleDateFormat,java.util.Calendar;" %>

<html:html>
<%
	Calendar calendar = Calendar.getInstance();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String date = formatter.format(calendar.getTime());
	date = DateUtil.getLastMonth(date);
%>
	<head>
	<html:base/>
		<title>用户登录</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<script language="javascript" src="js/func.js"></script>
		<link href="css/globalStyle.css" type="text/css" rel="stylesheet">
		<jsp:include page="calendar.jsp" flush="true">
		<jsp:param name="path" value="" />
		</jsp:include>
		
		<style type="text/css">
			
		</style>
		<script language="javascript">
			function submitData()
			{
				document.form1.submit();
				
			}
			
			/**
			 * 表单提交事件
			 */ 
			function _submit(form){

				if(isEmpty(form.userName.value)==true){
					alert("请输入用户名!\n");
					form.userName.focus();
					return	false;
				}
				if(isEmpty(form.password.value)==true){
					alert("请输入密码!\n");
					form.password.focus();
					return false;
				}
				if(isEmpty(form.loginDate.value)==true){
					alert("请输入日期!\n");
					form.loginDate.focus();
					return false;
				}

				return true;
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
  <p><br></p><p>　</p>
  <html:form styleId="form1" method="post" action="/userLogin" focus="userName" onsubmit="return _submit(this)">
 	
	<table border="0" cellpadding="0" cellspacing="0" width="778">
  <tr>
    <td><img name="login_r1_c1" src="image/login_r1_c1.jpg" width="778" height="14" border="0" alt=""></td>
  </tr>
  <tr>
    <td><table align="left" border="0" cellpadding="0" cellspacing="0" width="778">
        <tr>
          <td><img name="login_r2_c1" src="image/login_r2_c1.jpg" width="156" height="72" border="0" alt=""></td>
          <td><img name="login_r2_c2" src="image/login_r2_c2.jpg" width="622" height="72" border="0" alt=""></td>
        </tr>
    </table></td>
  </tr>
  <tr>
    <td><img name="login_r3_c1" src="image/login_r3_c1.jpg" width="778" height="144" border="0" alt=""></td>
  </tr>
  <tr>
    <td><img name="login_r4_c1" src="image/login_r4_c1.jpg" width="778" height="91" border="0" alt=""></td>
  </tr>
  <tr>
    <td><table align="left" border="0" cellpadding="0" cellspacing="0" width="778">
      <!--DWLayoutTable-->
        <tr>
          <td width="362" height="150"><img name="login_r5_c1" src="image/login_r5_c1.jpg" width="362" height="150" border="0" alt=""></td>
          <td width="247" valign="top">
	          <table width="100%" border="0" cellpadding="0" cellspacing="0" background="image/login_r5_c3.jpg">
	            <!--DWLayoutTable-->
	            <tr>
	              <td width="74" height="37">&nbsp;</td>
	              <td width="170">&nbsp;</td>
	              <td width="10">&nbsp;</td>
	            </tr>
	            <tr>
	              <td height="35">&nbsp;</td>
	              <td valign="top" >
	                      <input name="userName" type="text" size="20" maxlength="20"></td>
	              <td>&nbsp;</td>
	            </tr>
	            <tr>
	              <td height="35">&nbsp;</td>
	              <td valign="top" >
	              	<input type="password" name="password" size="20" maxlength="40" autocomplete="off"/>
	              </td>
	              <td>&nbsp;</td>
	            </tr>
	            <tr>
	              <td height="20">&nbsp;</td>
	              <td valign="top" >
                    <input name="loginDate" id="loginDate" type="text" readonly="readonly" size="18" maxlength="20" value="<%=date %>" onclick="return showCalendar('loginDate', 'y-mm-dd');"/>
                    <img src="image/calendar.gif" border="0" onclick="return showCalendar('loginDate', 'y-mm-dd');">
	              </td>
	              <td>&nbsp;</td>
	            </tr>
	            <tr style="height: 30px">
	              <td>&nbsp;</td>
	              <td><a href="../rpt_net_cunzhen/login.jsp" style="font-size: 14px;text-decoration: underline;">村镇银行登录</a></td>
	              <td>&nbsp;</td>
	            </tr>
	          </table>
          </td>
          <td width="169" align="center">
          	<input type="image" onclick="submitData" src="image/login_r5_c4.jpg" width="163" height="122">
          </td>
        </tr>
    </table></td>
    
  </tr>
</table>
  </html:form>

  </body>
</html:html>
