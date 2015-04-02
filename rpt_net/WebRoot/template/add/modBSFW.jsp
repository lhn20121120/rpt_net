
<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.fitech.net.hibernate.OrgNet"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ page import="com.fitech.net.adapter.StrutsOrgNetDelegate"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html lang="true">
  <head>
    <html:base />
    
    <title>modBSFW.jsp</title>
    
   <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
    <link href="../../css/common.css" type="text/css" rel="stylesheet">
    
    <%
 
    String childRepId=(String)request.getAttribute("childRepId");
    String versionId=(String)request.getAttribute("versionId");
    String orgId=(String)request.getAttribute("orgId");
    String curOrgId=(String)request.getAttribute("curOrgId");
    String reportName=(String)request.getAttribute("reportName");
    // System.out.println(reportName);
    String reportStyle=(String)request.getAttribute("reportStyle");
   
    String    upOrgId=StrutsOrgNetDelegate.getUpOrgId(orgId.trim());
   
    request.setAttribute("childRepId",childRepId);
    request.setAttribute("versionId",versionId);
    request.setAttribute("reportStyle",reportStyle);
    request.setAttribute("reportName",reportName);
    int i=1;
    %>
    <script language="javascript">
			function validate()
			{
				
			}
			
			function _back(childRepId,versionId,upOrgId)
			{
			var orgIds="";
				var formObj=document.getElementById("frmSel");
				var box=formObj.elements['orgIds'];
				          for(var i=0;i<document.frmSel.elements.length;i++)
							{
								var v=document.frmSel.elements[i];
							
							  if (v.name=="orgIds" && v.checked)
							        orgIds=orgIds+","+v.value;
							}
				 
				        <%
					  	if(orgId.trim().equals(curOrgId)){
					  	%>
					  	alert("已是当前机构！");
					  	return false;
					  	<%
					  	}else{
					  	%>
					  	upOrgId="<%=upOrgId%>";
					  	<%
					  	}
					  	%>
				window.location="<%=request.getContextPath()%>/template/mod/pEditBSFW.do?childRepId="+formObj.childRepId.value+"&versionId="+formObj.versionId.value+"&orgId="+
						
				              upOrgId
				             +"&orgIds="+orgIds;
			}
			
			function _allSelect()
			{
				var formObj=document.getElementById("frmSel");
				var box=formObj.elements['orgIds'];
				
					for(var i=0;i<document.frmSel.elements.length;i++)
					{
					var v=document.frmSel.elements[i];
					if(v.name=="orgIds")
					   v.checked=true;
					}
				
			}
			
			function _allCancel()
			{
				var formObj=document.getElementById("frmSel");
				var box=formObj.elements['orgIds'];
				
					for(var i=0;i<document.frmSel.elements.length;i++)
					{
					var v=document.frmSel.elements[i];
					if(v.name=="orgIds")
					v.checked=false;
					}
			}
			function _inOrg(orgId)
			{
			var formObj=document.getElementById("frmSel");
				var box=formObj.elements['orgIds'];
				var select=false;
				for(var j=0;j<document.frmSel.elements.length;j++)
				{
				   if(document.frmSel.elements[j].value==orgId)
				   select=document.frmSel.elements[j].checked;
				}
				
				if(select)
				{
							var orgIds="";
						
						
							for(var i=0;i<document.frmSel.elements.length;i++)
							{
								var v=document.frmSel.elements[i];
							
							  if (v.name=="orgIds" && v.checked)
							        orgIds=orgIds+","+v.value;
							}
							
							window.location="<%=request.getContextPath()%>/template/mod/pEditBSFW.do?childRepId="+formObj.childRepId.value+"&versionId="+formObj.versionId.value+"&orgId="+orgId
							+"&orgIds="+orgIds;
				}
			    else
			    {
			      alert("请先选中复选框");
			    }
			}
	</script>
  </head>
  
  <body>
    
    <html:form action="/template/mod/updateBSFW" method="post" styleId="frmSel">
    
    <TABLE cellSpacing="1" cellPadding="4" width="96%" border="0" align="center" class="tbcolor">
			<TR class="tbcolor1" >
				<th align="center" id="list" height="30" colspan="2">
					<span style="FONT-SIZE: 11pt">
					 	<logic:present name="ObjForm" scope="request">
									《<bean:write name="ObjForm" property="reportName"/>》
								</logic:present>
								填报范围设定
					</span>
				</th>
			</TR>
			
			<tr>
				<td align="left" bgcolor="#EEEEEE">
					请选择报表的范围:
				</td>
				<td align="center" bgcolor="#EEEEEE">
				<input type="button" property="allSelect" value="全部选中" class="input-button" onclick="_allSelect()"/>
	            	<input type="button" property="allCancel" value="全部取消" class="input-button" onclick="_allCancel()"/>
				</td>
			</tr>
			<tr>
			
			</tr>
			<tr>
				<td bgcolor="#FFFFFF"  colspan="2">
	    <table width="90%" border="0" cellpadding="4" cellspacing="1" class="tbcolor2">
		    
		    <input type="hidden" name="childRepId" value="<%=childRepId%>">
		    <input type="hidden" name="versionId" value="<%=versionId%>">
		    <input type="hidden" name="reportStyle" value="<%=reportStyle%>">
		    <input type="hidden" name="reportName" value="<%=reportName%>">
		    <input type="hidden" name="curOrgId" value="<%=curOrgId%>">
		    <input type="hidden" name="orgId" value="<%=orgId%>">
		    
		    <input type="hidden" name="upOrgId" value="<%=upOrgId%>">
		    <logic:present name="lowerOrgList" scope="request">

			    <logic:iterate id="item" name="lowerOrgList">
						    <logic:notEmpty name="item" property="orgName">
						    <%
								String url="../../template/mod/pEditBSFW.do?childRepId="+childRepId+"&versionId="+versionId+"&orgId=";
							%>
						    <%
						    if((i % 3)==1){
						    %>
						    <tr>
								<td  align="left">
									<logic:equal name="item" property="preOrgId" value="true">
									<input type="checkbox" name="orgIds" value="<bean:write name="item" property="orgId"/>" checked/>
								</logic:equal>
								<logic:notEqual name="item" property="preOrgId" value="true">
									<input type="checkbox" name="orgIds" value="<bean:write name="item" property="orgId"/>" />
								</logic:notEqual>
								
									<a href="javascript:_inOrg('<bean:write name="item" property="orgId"/>')"><bean:write name="item" property="orgName"/></a>
								</td>
							
							<%
							}else{
							%>
							<td  align="left">
							    <logic:equal name="item" property="preOrgId" value="true">
									<input type="checkbox" name="orgIds" value="<bean:write name="item" property="orgId"/>" checked/>
								</logic:equal>
								<logic:notEqual name="item" property="preOrgId" value="true">
									<input type="checkbox" name="orgIds" value="<bean:write name="item" property="orgId"/>" />
								</logic:notEqual>
								
								
								<a href="javascript:_inOrg('<bean:write name="item" property="orgId"/>')"><bean:write name="item" property="orgName"/></a>
							</td>
							<%}%>
							</logic:notEmpty>
							<%if((i % 3)==0){%>
								
							<%}%>
							<logic:empty name="item" property="orgName">
							<%
							for(int j=1;j<=(3-((i-1)%3));j++){
							%>
							<td></td>
							<%
							}
							%>
							
							</logic:empty>
						<%
						i=i+1;
						%>
			    	</logic:iterate>
			    <tr></tr>
			    <tr class="titletab" id="tbcolor">
				    <td align="center">
				    	
				    </td>
				    <td></td>
				   <td></td>
				</tr>
			   <tr class="titletab" id="tbcolor">
				   <td></td>
				    <td align="center">
				    	<input type="submit" value="保存" class="input-button"/>&nbsp;
	                   	<%
					  	if(orgId.trim().equals(curOrgId)){
					  	%>
					  	   <input type="button" property="back" class="input-button"  onclick="_back('<%=childRepId%>','<%=versionId%>','<%=curOrgId%>','<%=reportStyle%>','<%=reportName%>')" value="返 回"/>
					  	<%
					  	}else{
					  	%>
					  		<input type="button" property="back" class="input-button"  onclick="_back('<%=childRepId%>','<%=versionId%>','<%=upOrgId%>','<%=reportStyle%>','<%=reportName%>')" value="返 回"/>
					  	<%
					  	}
					  	%>
				    </td>
				    <td></td>
		    	</tr>	    
		    </logic:present>
		    <logic:notPresent name="lowerOrgList" scope="request">
				<tr colspan="8">
				  	<td colspan="8" align="center">
				  	   暂无机构信息
				  	</td>
			  	</tr>
			  	<tr colspan="8">
				  	<td colspan="8" align="center">
				  	<%
				  	if(orgId.trim().equals(curOrgId)){
				  	%>
				  	  <input type="button" property="back" class="input-button"  onclick="_back('<%=childRepId%>','<%=versionId%>','<%=curOrgId%>','<%=reportStyle%>','<%=reportName%>')" value="返 回"/>
				  	<%
				  	}else{
				  	%>
				  		<input type="button" property="back" class="input-button"  onclick="_back('<%=childRepId%>','<%=versionId%>','<%=upOrgId%>','<%=reportStyle%>','<%=reportName%>')" value="返 回"/>
				  	<%
				  	}
				  	%>
				  	</td>
			  	</tr>
			</logic:notPresent>
			   
			   
	    </table>
	    </td>
		</tr>
					
		</table>	
    </html:form>
  </body>
</html:html>
