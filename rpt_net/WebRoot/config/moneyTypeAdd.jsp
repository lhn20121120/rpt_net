<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
	<head>
	<html:base/>
    <title>币种设定</title>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
    <link href="../css/common.css" type="text/css" rel="stylesheet">
  </head>  
  <body>
	<table border="0" width="90%" align="center">
		<tr><td height="8"></td></tr>
		<tr>
			 <td>当前位置 >> 	参数设定 >> 币种新增</td>
		</tr>
		<tr><td height="10"></td></tr>
	</table>
		<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
		<html:form action="/config/InsertCurr" method="post">
				<table border="0" width="50%" align="center" cellspacing="1" cellpadding="4" class="tbcolor">
					<TR class="tableHeader" >
						<TD width="15%" valign="bottom">
						币种设定
						</TD>
					</TR>
					
					<TR align="center" valign="middle" bgcolor="#FFFFFF">
					  <TD>
						<table border="0" width="100%" align="center" cellspacing="1" cellpadding="4">
						  <TR>
						    <TD align="center">币种名称：<html:text property="curName" size="20" styleClass="input-text" maxlength="20"/>
						    </TD>
				          </TR>
					           <TR>
					             <TD align="center">
				                   <html:submit styleClass="input-button"  value="新增"/>
				                 </TD>
				               </TR>
				        </table>
				      </TD>
					</TR>
				</table>
		</html:form>	
	</body>
</html:html>

