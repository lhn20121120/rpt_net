<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.util.FitechUtil" %> 
<html:html locale="true">
<head>
	<html:base/>
		<title>�����޸�</title>
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
	<table border="0" width="80%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				��ǰλ�� >> �������� >> �����޸�
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<br>
	<table width="80%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
		      <tr class="titletab">
		      
		            <th align="center">
		            	���������޸�
		            </th>
		      </tr>
		  
		     <tr>
		      	<td bgcolor="#ffffff"> 	
	<html:form action="/updateRegionNet" method="post">
	<table width="100%" border="0" align="center">
			          <tr>
			         	 <td align="center" bgcolor="#ffffff" width="50%">
			         		 <%
				         	 	String regionName = FitechUtil.getParameter(request,"regionName");
				         	 %>  &nbsp;ԭ��������:<input type ="text" class="input-text" name="oldregionNm" value="<%=regionName%>" size="20" readonly>
			             </td> 
			             <td align="center" bgcolor="#ffffff" width="50%">
				         	 	<%
				         	 		String regionId = FitechUtil.getParameter(request,"regionId");
				         	 	%> &nbsp;<input type="hidden" name="regionId" value="<%=regionId%>">
				               	 �µ�������:<html:text property="regionName" styleClass="input-text" size="20"/>
				             </td>
			          </tr>
				      <tr>
				       <td align="center" bgcolor="#ffffff" width="50%">
				             <% String regionTypId = FitechUtil.getParameter(request,"regionTypId"); %> 
				              ԭ��������Id:<input type ="text" class="input-text" name="oldregionTypId" value="<%=regionTypId%>" size="20" readonly>
			             </td>
				      <td align="center" bgcolor="#ffffff" width="50%">
				      <% String regionId2 = FitechUtil.getParameter(request,"regionId");%>
				         	 	<input type="hidden" name="regionId2" value="<%=regionId2%>">
				               	�µ�������Id:<html:text property="regionTypId" styleClass="input-text" size="20"/>
				             </td>
				             
				      </tr>
				      
			         <tr>
				            <td colspan="4" align="right" bgcolor="#ffffff">
				            	<div id=location>
				            	</div>
				            </td>
				            </tr> 
				          <tr>
				            <td colspan="10" align="right" bgcolor="#ffffff">
				            	<html:submit value="����" styleClass="input-button"/>&nbsp;
				            	<html:button property="back" value="����" styleClass="input-button" onclick="window.location.assign('../regionnet.do')"/>
				            </td>
				            </tr>
				          </table>
				    </html:form>
		      	</td>
		      </tr>
		    </table>		   		
	</body>
</html:html> 
