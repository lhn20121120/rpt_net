<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
	<head>
		<html:base/>
		<title>超级用户登录</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<script language="javascript" src="js/func.js"></script>
		<jsp:include page="calendar.jsp" flush="true">
		<jsp:param name="path" value="/" />
	</jsp:include>
		<link href="css/globalStyle.css" type="text/css" rel="stylesheet">
		<style>
		.form{
		  font-family:宋体,Arial;
		  font-size: 9pt;
		  color: #003366;
		  border: 1px solid #6592A7;
		  padding-left: 1;
		  padding-right: 1;
		  padding-top: 3;
		  background-color: #E9F2F8;
		}
		.alert{
		  font-family:宋体,Arial;
		  font-size: 9pt;
		  color: red;
		}
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
				return true;
			} 
		</script>
  </head>
  <body bgcolor="#FFFFFF">
  <logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
  <p>　</p>
  <html:form styleId="form1" method="post" action="/userLogin" focus="userName" onsubmit="return _submit(this)">
  <!--
	  <table width="621" border="0" cellpadding="0" cellspacing="0" align="center">
	  	<tr height="25">
	  		<td height="76" colspan="2" align="left"><img border="0" src="image/login_gl1.gif" width="397" height="63">
			<p>　</tr>	
		<tr height="25">
		  <td height="76" colspan="2" align="right"><img src="image/login.jpg" width="621" height="193"></td>
		</tr>
		<tr height="25">
		  <td height="52" colspan="2" align="right" background="image/login2.jpg">
		  	<table width="214" height="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
				  <td width="117" height="29"><input type="text" name="userName" class="form" value=""  size="15" maxlength="20"></td>
				</tr>
				<tr>
				  <td width="117" height="29"><input type="password" name="password" class="form" size="15" value="" maxlength="40"></td>
				</tr>
			  </table>
			</td>
		</tr>
		<tr height="25">
		  <td height="75" colspan="2" align="right" background="image/login3.jpg"> 
			 <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
				  <td width="100%" height="10">&nbsp;&nbsp;&nbsp;<font class="alert"></font></td>
				</tr>
				<tr>
				  <td align="right">
	
					<input type="image" onclick="submitData" src="image/login4.jpg" width="63" height="26">&nbsp;
					&nbsp;&nbsp;&nbsp;</td>
				</tr>
			  </table>
			</td>
		</tr>
	  </table>
	
	-->
	<table border="0" cellpadding="0" cellspacing="0" width="778">
  <!-- fwtable fwsrc="未命名" fwbase="login" fwstyle="Dreamweaver" fwdocid = "1851182404" fwnested="1" -->
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
          <td width="362" height="142"><img name="login_r5_c1" src="image/login_r5_c1.jpg" width="362" height="142" border="0" alt=""></td>
          <td width="247" valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0" background="image/login_r5_c3.jpg">
            <!--DWLayoutTable-->
            <tr>
              <td width="74" height="37">&nbsp;</td>
              <td width="163">&nbsp;</td>
              <td width="10">&nbsp;</td>
            </tr>
            <tr>
              <td height="53">&nbsp;</td>
              <td valign="top">
                      <input name="userName" type="text" size="22" maxlength="20">
                      <br>
                      <input name="password" type="password" size="22" maxlength="40" >
                      <br>
                      				  
					  
              </td>
            <td>&nbsp;</td>
            </tr>
            <tr>
              <td height="52">&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
          </table></td>
          <td width="169"><!--
          <img name="login_r5_c4" src="image/login_r5_c4.jpg" width="163" height="142" border="0" alt="" onclick="submitData"></td>
        --><input type="image" onclick="submitData" src="image/login_r5_c4.jpg" width="163" height="142">
        </tr>
    </table></td>
  </tr>
</table>
  </html:form>
  
  <!--</form>-->
  </body>
</html:html>
