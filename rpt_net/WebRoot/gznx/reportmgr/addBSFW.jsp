<%@ page language="java" pageEncoding="GBK"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.fitech.net.adapter.StrutsOrgNetDelegate"%>
<%
String orgfilename = (String)request.getAttribute("xmlorgname");
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html lang="true">
  <head>
    <html:base />
    
    <title></title>
    
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">		
    	<link href="<%=request.getContextPath()%>/css/common.css" type="text/css" rel="stylesheet">
    	<script src="<%=request.getContextPath()%>/js/Tree_for_xml.js"></script>
   	   
		<style rel="STYLESHEET" type="text/css">
			.defaultTreeTable{margin : 0;padding : 0;border : 0;}
			.containerTableStyle { overflow : auto;}
			.standartTreeRow{	font-family : Verdana, Geneva, Arial, Helvetica, sans-serif; 	font-size : 14px; -moz-user-select: none; }
			.selectedTreeRow{ background-color : navy; color:white; font-family : Verdana, Geneva, Arial, Helvetica, sans-serif; 		font-size : 14x;  -moz-user-select: none;  }
			.standartTreeImage{ width:14x; height:1px;  overflow:hidden; border:0; padding:0; margin:0; }			
			.hiddenRow { width:1px;   overflow:hidden;  }
			.dragSpanDiv{ 	font-size : 12px; 	border: thin solid 1 1 1 1; }
</style>

  </head>
  
  <body>
    <%
 
    String childRepId=(String)request.getAttribute("childRepId");
    String versionId=(String)request.getAttribute("versionId");    
    String orgId=(String)request.getAttribute("orgId");
    String curOrgId=(String)request.getAttribute("curOrgId");
    String upOrgId="";
    upOrgId=StrutsOrgNetDelegate.getUpOrgId(orgId);
    request.setAttribute("childRepId",childRepId);
    request.setAttribute("versionId",versionId);   
    int i=1;  
    %>
	
    <html:form action="/gznx/insertBSFW" method="post" styleId="frmSel">
	    <table border="0" cellspacing="0" cellpadding="4" width="96%" align="center">
					<tr><td height="8"></td></tr>
					<tr>
						<td>
							当前位置 >> 报表管理 >> 填报范围设定
						</td>
					</tr>
		</table>
	    <TABLE cellSpacing="1" cellPadding="4" width="96%" border="0" align="center" class="tbcolor">
			<TR class="tbcolor1" >
				<th align="center" id="list" height="30" colspan="2">
					<span style="FONT-SIZE: 11pt">
					 	<logic:present name="ReportName" scope="request">
							《<bean:write name="ReportName"/>》
						</logic:present>填报范围设定
					</span>
				</th>
			</TR>
			
			<tr>
				<td align="left" bgcolor="#EEEEEE">
					 请选择报表的范围:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;			
		</tr>
			
			<logic:present name="isNull" scope="request">
				<tr colspan="8">
				  	<td colspan="8" align="center">
				  	   暂无机构信息
				  	</td>
			  	</tr>
			  	 <tr>				
					   <td align="center">
					       <input type="button" property="back" value="返  回" class="input-button" onclick="window.location.assign('../../template/viewMChildReport.do')"/>		  
					    </td>					  
			    	</tr>    
			 </logic:present>
			 <logic:notPresent name="isNull" scope="request">
				 <tr>
					<td valign="top">
						<div id="treeboxbox_tree2" style="width:100%; height:300;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;"></div>
					</td>			
		    	</tr>
		    	<tr>
					    <td align="center">
					    	<input type="button" value="保  存" class="input-button"  onclick="getAllCheckedId()">&nbsp;
<%--		      			    <input type="button" property="back" value="返  回" class="input-button" onclick="window.location.assign('<%=request.getContextPath() %>/afreportDefine.do')"/>		  --%>
					    </td>
					    <td>
					    </td>
					    
			    	</tr>    
			 </logic:notPresent> 	
		
		   </TABLE>
		   
    </html:form>
	
	<script>	
	function getAllCheckedId(){
				var checkId=tree2.getAllChecked();	
				if(checkId.replace(/(^[\s]*)|([\s]*$)/g, "")==''){
					alert("请选择报表的范围!");
					return;
				}else {
				window.location="<%=request.getContextPath()%>/gznx/insertBSFW.do?orgIds="+checkId+"&childRepId="+'<%=childRepId%>'+"&versionId="+'<%=versionId%>';
				}
	}		
			tree2=new dhtmlXTreeObject("treeboxbox_tree2","100%","100%",0);
			tree2.setImagePath("<%=request.getContextPath()%>/image/treeImgs/");
			tree2.enableCheckBoxes(1);
			tree2.enableThreeStateCheckboxes(true);
			tree2.loadXML("<%=request.getContextPath()%>/xml/<%=orgfilename%>");
			
					
	</script>
<br><br>
  </body>
</html:html>
