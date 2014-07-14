<%@ page contentType="text/html;charset=GBK" language="java"%>

<html>
	<head>

		<title>用户登录</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<script language="javascript" src="resource/js/func.js"></script>
		<link href="resource/css/globalStyle.css" type="text/css" rel="stylesheet">
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
		}td img {display: block;}
		body {
	margin-top: 90px;
	background-image: url(image/bg2.jpg);
}td img {display: block;}
        </style>
		<script type="text/javascript">
			function doload(obj){
				document.getElementById('userId').select();
			}
		</script>
	</head>
	<body bgcolor="#FFFFFF" onload="doload(this)">
		<s:form action="userlogin" method="post" id="loginForm">
	<div >
<table width="550" height="327" border="0" background="images/SIM-IN3.jpg">
  <tr>
    <td ><div style="margin-left:300px;margin-top:240px;width:230px;height:80px;z-index:1;">
      <table width="100%" height="100%" border="0" >
        <tr>
          <td width="50%"><s:textfield name="loginName" size="14"
				id="userId" maxlength="20" cssClass="input-text" value='15020005'
				 onkeydown="if(event.keyCode==13) event.keyCode=9"></s:textfield></td>
		   <td><a href="findMessageCommon.action?fileName=findClientMgrId.xls" target="_blank"
		    onkeydown="if(event.keyCode==13)event.keyCode=9">客户经理编号查看</a></td>
        </tr>
        <tr>
          <td><s:password name="passWord" size="14"
				id="pwd" maxlength="40" cssClass="input-text"  value='aaa'></s:password></td>
		  <td><!-- <a href="findMessageCommon.action?fileName=userNote.doc">用户手册下载</a> --></td>
        </tr>
		<tr>
          <td align="right"><input type="image" onclick=""
			src="images/login_r4_c3.jpg" width="62" height="23"></td>
		<td>&nbsp;</td>
        </tr>
      </table>
    </div></td>
  </tr>
</table>
</div>
		</s:form>
		<script type="text/javascript">
		  window.moveTo(0,0);   
          window.resizeTo(screen.availWidth + 7,screen.availHeight);   
		</script>
	</body>
</html>
