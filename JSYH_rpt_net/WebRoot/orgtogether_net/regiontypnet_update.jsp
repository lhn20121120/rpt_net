<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.util.FitechUtil" %> 

<html:html locale="true">
<head>
	<html:base/>
		<title>地区类型修改</title>
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
		            	地区类型修改
		            </th>
		      </tr>
		      <tr>
		      	<td bgcolor="#ffffff">
		      	    <html:form action="/updateRegionTypNet" method="post">
			      	  <table width="100%" border="0" align="center">
					      <tr bgcolor="#ffffff">
					      	<td height="5">
					      	</td>
				     	 </tr>
			          <tr>
			         	 <td align="center" bgcolor="#ffffff" width="50%">
			         		 <%
				         	 	String regionTypNm = FitechUtil.getParameter(request,"regionTypNm");
				         	 %>
			         	 	
			               	原地区类型名称:<input type ="text" class="input-text" name="oldregionTypNm" value="<%=regionTypNm%>" size="20" readonly>
			             </td> 
			             <td align="center" bgcolor="#ffffff" width="50%">
				         	 	<%
				         	 		String regionTypId = FitechUtil.getParameter(request,"regionTypId");
				         	 	%>
				         	 	<input type="hidden" name="regionTypId" value="<%=regionTypId%>">
				               	新子行分类名称:<html:text property="regionTypNm" styleClass="input-text" size="20"/>
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
				            	<html:button property="back" value="返回" styleClass="input-button" onclick="window.location.assign('../regionTypNet.do')"/>
				            </td>
				            </tr>
				          </table>
				    </html:form>
		      	</td>
		      </tr>
		    </table>
		    
		
	</body>
</html:html> 
