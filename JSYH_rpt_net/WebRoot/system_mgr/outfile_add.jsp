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
		<script language="javascript">
			parent.contents.location.href = "viewOutFile.do";	
		</script>
	</head>
	<body topmargin="0" marginheight="0" leftmargin="0" marginwidth="0">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
		<table cellspacing="0" cellpadding="0" border="0" width="85%" align="center">
		<tr><td height="8"></td></tr>
			<tr background="../image/inside_index_bg4.jpg">
				 <td>当前位置 >> 	系统管理 >> 发布信息管理</td>
			</tr>
			<tr>
				<td height="10"> 
				
				</td>
			<tr>
			<tr>
			  <td>
			  <div align="center">
			   <table width="90%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
			       <tr class="titletab">
				     <th align="center" colspan="4">上传文件</th>
			       </tr>
			       <tr bgcolor="#FFFFFF">
				     <td>
				       <html:form method="post" action="/system_mgr/newOutFile" enctype="multipart/form-data">			
                         <div align="center">
			                <html:file  property="infoFile" size="80" styleClass="input-button"/>
	                        <html:submit styleClass="input-button" value="上传"/>
                        </div>
                      </html:form>
                     </td>
			     </tr>
		        </table>
		       </div>
			   </td>
			</tr>
		</table>
	</body>
</html:html>