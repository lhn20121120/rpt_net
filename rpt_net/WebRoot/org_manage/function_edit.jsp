<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
		<title>���з�����Ϣ���</title>
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
	
		<html:form action="/orgmanage/updatefunction" method="post" >
	    	<table width="80%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
		      <tr class="titletab">
		            <th align="center">  ϵͳ�����޸�</th>
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
			         	 
			             <td align="center" bgcolor="#ffffff"> ϵͳ������
									<INPUT type="hidden" name="id" Value="<bean:write name="Records" property="id"/>">
									��:<input type="text" name="functionName"  class="input-text" Value="<bean:write name="Records" property="functionName"/>">
			            	
			             </td> 
			             <td align="center" bgcolor="#ffffff">
			               	ϵͳ�������� :<input type="text" name="functionDes"  class="input-text" Value="<bean:write name="Records" property="functionDes"/>">
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
			            	<html:submit value="����" styleClass="input-button"/>
			            	<html:button property="back" value="����" styleClass="input-button" onclick="window.location.assign('../orgmanage/viewfunction.do')"/>
			            </td>
			            </tr>
			         </table>
			   </logic:present>
			  <logic:notPresent name="Records" scope="request">
			         	û�л�����Ϣ
			         
			  </logic:notPresent>
		      	</td>
		      </tr>
		      </table>
		      
	    </html:form>
		
	</body>
</html:html> 
