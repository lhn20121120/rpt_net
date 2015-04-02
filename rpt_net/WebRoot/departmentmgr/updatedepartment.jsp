<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.util.FitechUtil" %>

<html:html locale="true">
<head>
	<html:base/>
		<title>机构部门设置</title>
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
				当前位置 >> 权限管理 >> 部门设置
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
		      	    <html:form action="/updateDepartmentMgr" method="post">
		      	    <!--<html:hidden property="event" value="add"/>
			      	  --><table width="90%" border="0" align="center" cellpadding="4" cellspacing="1">
					      <tr bgcolor="#ffffff">
					      	<td height="5">
					      	</td>
				     	 </tr>
			<tr>	    		    		      
			   <td align="right"  width="15%">
			      原部门Id:
			        </td> 
			        <td width="30%">			     
			      <%String departmentId = FitechUtil.getParameter(request,"departmentId");%>
			      <input type ="text" class="input-text" name="olddepartmentId" value="<%=departmentId%>" size="20" readonly>	         
			        </td>	
			         <td align="right" width="15%">
			         新部门Id:
			         </td>
			             <td width="30%">
				         	 	<%
				         	 		String orgId1 = FitechUtil.getParameter(request,"orgId");
				         	 	%>
				         	 	<input type="hidden" name="orgId" value="<%=orgId1%>">
				               	<html:text property="departmentId" styleClass="input-text" size="20"/>				               
				             </td>	
				             		 			  		             	   			
                         </tr>
		       	    			    
			      <tr> 
			      
			      <td align="right"  width="15%">
			      原部门名称:
			      </td> 			      
			      <td width="30%">
			      <%String deptName = FitechUtil.getParameter(request,"deptName");%>
			       <input type ="text" class="input-text" name="olddeptName" value="<%=deptName%>" size="20" readonly>
			         </td> 
			         <td align="right" width="15%">
			         新部门名称:
			         </td>
			             <td width="30%">
				         	 	<%
				         	 		String orgId= FitechUtil.getParameter(request,"orgId");
				         	 	%>
				         	 	<input type="hidden" name="orgId" value="<%=orgId%>">
				              <html:text property="deptName" styleClass="input-text" size="20"/>				               	
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
				            	<html:button property="back" value="返回" styleClass="input-button" onclick="window.location.assign('../viewDepartmentMgr.do')"/>
				            </td>
				            </tr>
				          </table>
				    </html:form>
		      	</td>
		      </tr>
		    </table>
	</body>
</html:html> 
