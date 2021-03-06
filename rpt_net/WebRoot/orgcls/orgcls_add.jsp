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
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<br>
		 
	    	<table width="80%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
		      <tr class="titletab">
		            <th align="center">
		            	子行分类信息添加
		            </th>
		      </tr>
		      <tr>
		      	<td bgcolor="#ffffff">
		      		<table width="100%" border="0" align="center" cellpadding="4" cellspacing="1">
				      
				      <html:form action="/orgcls/insertorgCls" method="post">
				      
				      <tr bgcolor="#ffffff">
				      	<td height="5">
				      	</td>
				      </tr>
			          
			          <tr>
			         	 <td align="center" bgcolor="#ffffff">
			               	分类编号:<html:text property="orgClsId" styleClass="input-text"/>
			             </td> 
			             <td align="center" bgcolor="#ffffff">
			               	子行分类名称:<html:text property="orgClsNm" styleClass="input-text"/>
			             </td> 
			             <td align="center" bgcolor="#ffffff">
			               	子行类型:<html:text property="orgClsType" styleClass="input-text"/>
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
			            	<html:submit value="保存" styleClass="input-button"/>&nbsp;
			            	<html:button property="back" value="返回" styleClass="input-button" onclick="window.location.assign('../orgcls/orgCls.do')"/>
			            </td>
			            </tr>
			         </table>
		      	</td>
		      </tr>
		      </table>
		      
	    </html:form>
		
	</body>
</html:html> 
