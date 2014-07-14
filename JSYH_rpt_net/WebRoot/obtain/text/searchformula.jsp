<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
  <html:form method="post" action="/obtain/text/viewformula" >
  <table width="68%" height="54"  border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="25%" height="54">模板名字    
    <input type="text" name="reportname"  size="17"></td>
    <td width="24%">版本号
<input type="text" name="versionId" size="17">    
      </td>
    <td width="19%">
      <input type="submit" name="Submit" value="查询">
    </td>
  </tr>
</table>
</html:form>
  </body>
</html>
