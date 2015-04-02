<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.util.FitechUtil" %>

<html:html locale="true">
<head>
	<html:base/>
		<title>机构信息修改</title>
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
				当前位置 >> 机构管理 >> 机构修改
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
		            	机构信息修改
		            </th>
		      </tr>
		      <tr>
		      	<td bgcolor="#ffffff">
		      	    <html:form action="/updateOrgnet" method="post">
		      	    <html:hidden property="event" value="add"/>
			      	  <table width="90%" border="0" align="center" cellpadding="4" cellspacing="1">
					      <tr bgcolor="#ffffff">
					      	<td height="5">
					      	</td>
				     	 </tr>
			<tr>	    		    		      
			   <td align="right"  width="15%">
			      原机构类型:
			        </td> 
			        <td width="30%">			     
			      <%String orgType = FitechUtil.getParameter(request,"orgType");%>
			      <input type ="text" class="input-text" name="oldorgType" value="<%=orgType%>" size="20" readonly>	         
			        </td>
			  <td align="right" width="15%">
			             	新机构类型:
			             </td>
			             <td width="30%">
				         	 	<%
				         	 		String orgId3 = FitechUtil.getParameter(request,"orgId");
				         	 	%>
				         	 	<input type="hidden" name="orgId" value="<%=orgId3%>">
				               <html:text property="orgType" styleClass="input-text" size="20"/>
				             </td>	 
				             </tr>	 	         	
			 <tr>
			   <td align="right" width="15%">
			               	原机构名称:
			      </td> 
			       <td width="30%">
			      <%	String orgName = FitechUtil.getParameter(request,"orgName");%>
			      <input type ="text" class="input-text" name="oldorgName" value="<%=orgName%>" size="20" readonly>
			             </td>
			             <td align="right" width="15%">
			             	新机构名称:
			             </td>
			             <td width="30%">
				         	 	<%
				         	 		String orgId = FitechUtil.getParameter(request,"orgId");
				         	 	%>
				         	 	<input type="hidden" name="orgClsId" value="<%=orgId%>">
				               <html:text property="orgName" styleClass="input-text" size="20"/>
				             </td>		   			
				             </tr>
				              <tr> 
			      <td align="right"  width="15%">
			      原机构代码:
			      </td> 			      
			      <td width="30%">
			      <%	String orgCode = FitechUtil.getParameter(request,"orgCode");%>
			       <input type ="text" class="input-text" name="oldorgCode" value="<%=orgCode%>" size="20" readonly>
			         </td> 
			         <td align="right" width="15%">
			         新机构代码:
			         </td>
			             <td width="30%">
				         	 	<%
				         	 		String orgId2 = FitechUtil.getParameter(request,"orgId");
				         	 	%>
				         	 	<input type="hidden" name="orgClsId" value="<%=orgId%>">
				               	<html:text property="orgCode" styleClass="input-text" size="20"/>
				             </td>			       			      
			        			       
			      </tr>
		       <tr>    
			        <td align="right"  width="15%">
			        原机构分类Id:
			        </td>
			        <td width="30%">
			      <%	String orgClsId = FitechUtil.getParameter(request,"orgClsId");%>
			       <input type ="text" class="input-text" name="oldorgClsId" value="<%=orgClsId%>" size="20" readonly>
			         </td>
			         <td align="right"  width="15%">
			         新机构分类Id:
			         </td>
			      
			         <td width="30%">
				         	 	<%
				         	 		String orgId4 = FitechUtil.getParameter(request,"orgId");
				         	 	%>
				         	 	<input type="hidden" name="orgClsId" value="<%=orgId4%>">
				               	<html:text property="orgClsId" styleClass="input-text" size="20"/>
				             </td>         
			        </tr>	    			    
			     
			     <tr>
				            <td colspan="4" align="right" bgcolor="#ffffff">
				            	<div id=location>
				            	</div>
				            </td>
				            </tr>
			        
			        <tr>
			        <td align="right"  width="15%">
			        地区Id:
			        </td>
		<td width="30%">
			           <html:text property="regionId"  styleClass="input-text"/>
			        </td>
			        <td align="right"  width="15%">
			         上级机构Id:	
			        </td >
			             		       
			          <td width="30%">  
			          <html:text property="parent_Org_Id"  styleClass="input-text"/>
			        </td>			         			          
			      </tr>	
			      
			      <tr>
			      <td align="right" width="15%">
			      是同法人:
			      </td>
			         <td width="30%">  <html:select property="isCorp">
			          <html:option value="1">是</html:option>
			          <html:option value="0">否</html:option>
			          </html:select>
			        </td>
			        <td align="right"  width="15%">
			      频度类别Id:
			        </td> 
			        <td width="30%">
			          <html:select property="oat_Id">
			          <html:option value="1">频度一类</html:option>
			          <html:option value="2">频度二类</html:option>
			          <html:option value="3">频度三类</html:option>
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
				            	<html:button property="back" value="返回" styleClass="input-button" onclick="window.location.assign('../vieworgnet.do')"/>
				            </td>
				            </tr>
				          </table>
				    </html:form>
		      	</td>
		      </tr>
		    </table>
	</body>
</html:html> 
