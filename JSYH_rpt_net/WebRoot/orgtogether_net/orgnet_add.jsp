<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="utilForm" scope="page" class="com.fitech.net.common.UtilForm" />

<html:html locale="true">
	<head>
		<html:base/>
		<title>新增机构</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../css/common.css" rel="stylesheet" type="text/css">
	</head>
<script language="javascript"><!--
			function validate()
			{
				var orgId = document.getElementById('orgId');
				var orgClsId= document.getElementById('orgClsId');
				var regionId= document.getElementById('regionId');				
				var parentOrgId= document.getElementById('parent_Org_Id');
				
				if(orgId.value=="")
				{   
					alert('机构id不能为空！');
					orgId.focus();
					return false;
				}
				if(orgClsId.value=="")
				{
					alert('机构分类id不能为空！');
					orgClsId.focus();
					return false;
				}
				
				if(regionId.value=="")
				{
					alert('地区id不能为空！');
					regionId.focus();
					return false;
				}			
				
				return true;					
			}									
	--></script>
	<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table border="0" width="95%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 >> 机构管理 >> 机构增加
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	
	<html:form action="/addorgnet" method="post" onsubmit="return validate()">
	    <html:hidden property="event" value="add"/>
		<table cellSpacing="1" cellPadding="4" border="0" width="95%" class="tbcolor">
			<tr class="titletab">
		            <th align="center" colspan="10" height="25">
		            	新增机构
		            </th>
		      </tr>
		    <tr>
			<td bgcolor="#ffffff">
			<table width="95%" border="0" align="center" cellpadding="4" cellspacing="1">
			
			 <tr>
			  <td align="right" width="15%">
			    机构Id:   
			  </td> 
			  <td width="30%">
                  	<html:text property="orgId" styleClass="input-text"/>                    
                  </td>
			  <td align="right" width="15%">
			    机构名称:
			   </td> 
			   <td  width="30%">
			    <html:text property="orgName"   styleClass="input-text"/>
			   </td>
			</tr>
			
			    <tr>
			     <td align="right" width="15%">
			            机构类型:
			        </td>      
			        <td  width="30%">
			         <html:text property="orgType" styleClass="input-text" />
			        </td>
			         <td align="right" width="15%">
			            机构代码:
			        </td>
			        <td width="30%">
			            <html:text property="orgCode"  styleClass="input-text"/>
			        </td>
			        
			        </tr>			    
			      <tr>   
			     
			        <td align="right" width="15%">
			            机构分类:
			        </td>
			        <td  width="30%"> 
			        <html:select property="orgClsId">
					     <html:optionsCollection name="utilForm" property="orgCls" label="label" value="value" />
					</html:select>
			        <!--<html:text property="orgClsId" styleClass="input-text" />-->
			        </td>
			        <td align="right" width="15%">  
			        上级机构Id:
			        </td>
			        <td  width="30%"> 
			        <html:text property="parent_Org_Id"  styleClass="input-text"/>
			        </td>
			        </tr>
			        
			        <tr>
			        <td align="right" width="15%">
			            地区:
			        </td>
			        <td width="30%">
			        <html:select property="regionId">
					     <html:optionsCollection name="utilForm" property="region" label="label" value="value" />
					</html:select>
			             <!--<html:text property="regionId"  styleClass="input-text"/>-->
			        </td>
			      <td align="right" width="15%">
			         是同法人: 
			        </td>
			      
			       <td width="30%">  
			       <html:select property="isCorp">
			          <html:option value="1">是</html:option>
			          <html:option value="0">否</html:option>
			          </html:select>
			        </td>		       
			          			         			          
			      </tr>	
			      <tr>
			      <td align="right" width="15%">
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
		  	            <td colspan="12" align="right" bgcolor="#ffffff">
			            	<div id=location>
			            	</div>
			            </td>
			            </tr>
			          <tr>
			      <td colspan="12" align="right" bgcolor="#ffffff">			            				
	              <html:submit value="保存" styleClass="input-button"/>&nbsp;
	              <html:button property="back" value="返回" styleClass="input-button" 
	                    onclick="window.location.assign('../vieworgnet.do')"/>
                  </td>
                </table>
			  </td>
			</tr>
		</table>
	</html:form>	
	</body>
</html:html>