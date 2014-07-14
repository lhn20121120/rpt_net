<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
		<title>子行分类信息添加</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../css/common.css" rel="stylesheet" type="text/css">
</head>
	<body>
	
	
		<html:form action="/orgmanage/updateeorg" method="post" enctype="multipart/form-data">
	    	<table width="80%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
		      <tr class="titletab">
		            <th align="center">  主题报送机构</th>
		      </tr>
		      <tr>
		      	<td bgcolor="#ffffff">
		      	  
		      	<logic:present name="Records" scope="request">
		      		<table width="100%" border="0" align="center" cellpadding="4" cellspacing="1">
				      
				  
				      
				      <tr bgcolor="#ffffff">
				      	<td height="5">
				      	</td>
				      </tr>
			          
			          <tr>
			         	 <td align="center" bgcolor="#ffffff">机构ID:<input type="text" name="id"  class="input-text" Value="<bean:write name="Records" property="id"/>">
			             </td> 
			             <td align="center" bgcolor="#ffffff"> 名称:<input type="text" name="name"  class="input-text" Value="<bean:write name="Records" property="name"/>">
			             </td> 
			             <td align="center" bgcolor="#ffffff">
			               	代表机构:<input type="text" name="rep_org"  class="input-text" Value="<bean:write name="Records" property="rep_org"/>">
			             </td> 
			          </tr>
			          <tr >
			            <td colspan="4" align="right" bgcolor="#ffffff">
			            	<div id=location>
			            	</div>
			            </td>
			            </tr>
			          <tr>
			            <td colspan="4" align="right" bgcolor="#ffffff">
			            	<html:submit value="保存" styleClass="input-button"/>
			            	<html:button property="back" value="返回" styleClass="input-button" onclick="window.location.assign('../orgmanage/vieworg.do')"/>
			            </td>
			            </tr>
			         </table>
			   </logic:present>
			  <logic:notPresent name="Records" scope="request">
			         	没有机构信息
			         
			  </logic:notPresent>
		      	</td>
		      </tr>
		      </table>
		      
	    </html:form>
		
	</body>
</html:html> 
