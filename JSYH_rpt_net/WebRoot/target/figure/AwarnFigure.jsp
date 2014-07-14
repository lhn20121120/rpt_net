<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="com.fitech.net.figure.Config"%>
<% 
 //传递数据
 String graphURL = request.getContextPath() + "/servlets/figureGenerate?title="+request.getParameter("targetName")
                  +"&allWarnMessageColor="+request.getParameter("allWarnMessageColor")
                  +"&currentValue="+request.getParameter("currentValue");
            
%> 
<html>
<head>
<title>3D饼图</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

</head>

<body leftmargin="2" topmargin="0" marginwidth="0" marginheight="0">
<br>

<table width="100%" border="0" align="center" cellpadding="1" cellspacing="1" class="border">
  <tr> 
    <td><img src="<%=graphURL %>" align="center" width=500 height=300 border=0 "/></td>
  </tr>
</table>

</body>
</html>
