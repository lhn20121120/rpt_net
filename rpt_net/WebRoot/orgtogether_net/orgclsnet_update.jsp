<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.util.FitechUtil" %> 
<html:html locale="true">
<head>
	<html:base/>
		<title>子行分类信息修改</title>
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
		            	机构分类信息修改
		            </th>
		      </tr>
		      <tr>
		      	<td bgcolor="#ffffff">
		      	    <html:form action="/orgClsNet_update" method="post">
			      	  <table width="100%" border="0" align="center">
					      <tr bgcolor="#ffffff">
					      	<td height="5">
					      	</td>
				     	 </tr>
			          <tr>
			         	 <td align="center" bgcolor="#ffffff" width="50%">
			         		 <%
				         	 	String orgClsNm = FitechUtil.getParameter(request,"orgClsNm");
				         	 %>
			         	 	
			               	原机构分类名称:<input type ="text" class="input-text" name="oldorgClsNm" value="<%=orgClsNm%>" size="20" readonly>
			             </td> 
			             <td align="center" bgcolor="#ffffff" width="50%">
				         	 	<%
				         	 		String orgClsId = FitechUtil.getParameter(request,"orgClsId");
				         	 	%>
				         	 	<input type="hidden" name="orgClsId" value="<%=orgClsId%>">
				               	新机构分类名称:<html:text property="orgClsNm" styleClass="input-text" size="20"/>
				             </td>
			          </tr>
				
			          <tr>			          
				            <td  align="center" bgcolor="#ffffff" width="50%">
				            	 <%String orgClsType = FitechUtil.getParameter(request,"orgClsType");%>
				         	 原机构分类类型:<INPUT type="text" class="input-text" name="oldorgClsType" value="<%=orgClsType%>" size="20" readonly>
				            </td>
				           
			              			 
				         	 <td align="center" bgcolor="#ffffff" width="50%">	
				         	 <%
				         	 		String orgClsId2 = FitechUtil.getParameter(request,"orgClsId");
				         	 	%>
				         	 	<input type="hidden" name="orgClsId" value="<%=orgClsId2%>">
				         	 	新机构分类类型:<html:text property="orgClsType" styleClass="input-text" size="20"/>		         	 				         	 	
				               	
				             </td>
				                       
				          </tr>
				          <tr>
				            <td colspan="4" align="right" bgcolor="#ffffff">
				            	<div id=location>
				            	</div>
				            </td>
				            </tr>
				          <tr>
				            <td colspan="4" align="right" bgcolor="#ffffff">
				            	<html:submit value="保存" styleClass="input-button"/>&nbsp;
				            	<html:button property="back" value="返回" styleClass="input-button" onclick="window.location.assign('../orgclsNet.do')"/>
				            </td>
				            </tr>
				          </table>
				    </html:form>
		      	</td>
		      </tr>
		    </table>
		    
		
	</body>
</html:html> 
