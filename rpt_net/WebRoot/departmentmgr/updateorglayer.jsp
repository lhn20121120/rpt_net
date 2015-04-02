<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.util.FitechUtil" %>

<html:html locale="true">
<head>
	<html:base/>
		<title>机构层次设置</title>
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
				当前位置 >> 权限管理 >> 机构层次设置
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
		<br>
	    	<table width="90%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
		      <tr class="titletab">
		            <th align="center">
		            	机构部门设置
		            </th>
		      </tr>
		      <tr>
		      	<td bgcolor="#ffffff">
		      	    <html:form action="/updateOrgLayer" method="post">
		      	    <!--<html:hidden property="event" value="add"/>
			      	  --><table width="90%" border="0" align="center" cellpadding="4" cellspacing="1">
					      <tr bgcolor="#ffffff">
					      	<td height="5">
					      	</td>
				     	 </tr>
			<tr>	    		    		      
			   <td align="right"  width="15%">
			      原机构层次Id:
			        </td> 
			        <td width="30%">			     
			      <%String orglayerId = FitechUtil.getParameter(request,"orglayerId");%>
			      <input type ="text" class="input-text" name="oldorglayerId" value="<%=orglayerId%>" size="20" readonly>	         
			        </td>	
			         <td align="right" width="15%">
			         新机构层次Id:
			         </td>
			             <td width="30%">
				         	 	<%
				         	 		String orgId1 = FitechUtil.getParameter(request,"orgId");
				         	 	%>
				         	 	<input type="hidden" name="orgId" value="<%=orgId1%>">
				               	
				               	<html:select property="orglayerId">
			          <html:option value="0">总行Id</html:option>
			          <html:option value="1">省行Id</html:option>
			          <html:option value="2">市行Id</html:option>
			          <html:option value="3">县行Id</html:option>
			          <html:option value="4">其他</html:option>
			          </html:select>				               
				             </td>	
				             		 			  		             	   			
                         </tr>
		       	    			    
			      <tr> 
			      
			      <td align="right"  width="15%">
			      原机构层次:
			      </td> 			      
			      <td width="30%">
			      <%String orglayer = FitechUtil.getParameter(request," orglayer");%>
			       <input type ="text" class="input-text" name="oldorglayer" value="<%=orglayer%>" size="20" readonly>
			         </td> 
			         <td align="right" width="15%">
			         新机构层次:
			         </td>
			             <td width="30%">
				         	 	<% String orgId= FitechUtil.getParameter(request,"orgId");%>
				         	 	<input type="hidden" name="orgId" value="<%=orgId%>">
				     <html:select property="orglayer">
			          <html:option value="总行">总行</html:option>
			          <html:option value="省行">省行</html:option>
			          <html:option value="市行">市行</html:option>
			          <html:option value="县行">县行</html:option>
			          <html:option value="其他">其他</html:option>
			          </html:select>				               	
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
				            	<html:button property="back" value="返回" styleClass="input-button" onclick="window.location.assign('../viewOrgLayer.do')"/>
				            </td>
				            </tr>
				          </table>
				    </html:form>
		      	</td>
		      </tr>
		    </table>
	</body>
</html:html> 
