<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
	<head>
		<html:base/>
		<title></title>
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../css/common.css" rel="stylesheet" type="text/css">
	</head>
	<body topmargin="0" marginheight="0" leftmargin="0" marginwidth="0">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
		<table cellspacing="0" cellpadding="0" border="0" width="98%">
		<tr><td height="8"></td></tr>
			<tr background="../image/inside_index_bg4.jpg">
				 <td>当前位置 >> 	信息交换 >> 信息上传</td>
			</tr>
			<tr>
				<td colspan="3" height="1" background="../image/line_bg.gif"></td>
			</tr>
			<tr>
			   <td><br><br><br>
			   <table cellSpacing="1" cellPadding="4" border="0" width="80%" class="tbcolor" align="center">
			       <tr class="titletab">
				     <th align="center" colspan="4">上传文件</th>
			       </tr>
			       <tr bgcolor="#FFFFFF">
				     <td width="20%">
				       <html:form method="post" action="/upInfoFiles" enctype="multipart/form-data">			
                        	 <div align="center">
                        	      <html:file  property="infoFile" size="80" styleClass="input-button"/>
		                          <html:submit styleClass="input-button" value="上传"/>
                        	</div>
                      </html:form>
                     </td>
			     </tr>
		        </table>
			   </td>
			</tr>
		</table>
	</body>
</html:html>