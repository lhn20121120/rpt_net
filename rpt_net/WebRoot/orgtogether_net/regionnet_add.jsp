<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
	<head>
		<html:base/>
		<title>��������</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../css/common.css" rel="stylesheet" type="text/css">
	</head>
<script language="javascript">
			function validate()
			{
				var txtRoleName = document.getElementById('regionId');
				if(txtRoleName.value=="")
				{
					alert("����ID����Ϊ�գ�");
					txtRoleName.focus();
					return false;
				}
				else
				{�� alert("��ӳɹ�");
					return true;
					}
			}									
	</script>
	<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table border="0" width="80%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				��ǰλ�� >> �������� >> ��������
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	
	<html:form action="/addRegionNet" method="post">
		<table cellSpacing="1" cellPadding="4" border="0" width="80%" class="tbcolor">
			<tr class="titletab">
		            <th align="center" colspan="10" height="25">
		            	����������Ϣ
		            </th>
		      </tr>		
			 <tr>
			  <td align="center" bgcolor="#ffffff">
			    &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;����Id:<html:text property="regionId"   styleClass="input-text"/>
			  </td> 
			  <td align="center" bgcolor="#ffffff">
			   ��������Id:<html:text property="regionTypId"   styleClass="input-text"/>
			   </td> 
			</tr>
			       <tr>
			        <td align="center" bgcolor="#ffffff" >
			            ������������:<html:text property="regionName"  styleClass="input-text"/>
			        </td> 
			         <td align="center" bgcolor="#ffffff" >
			           
			        </td>       
			        </tr>
		             
		             <tr>
			            <td colspan="12" align="right" bgcolor="#ffffff">
			            	<div id=location>
			            	</div>
			            </td>
			            </tr>
			          <tr>
			  <td colspan="12" align="right" bgcolor="#ffffff">			            				
	           <html:submit value="����" styleClass="input-button"/>&nbsp;
	           <html:button property="back" value="����" styleClass="input-button" 
	           onclick="window.location.assign('../regionnet.do')"/>
				</td>
			</tr>
		</table>
	</html:form>	
	</body>
</html:html>