${message}
<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@page import="com.fitech.gznx.po.vOrgRel"%>
<%@page import="com.cbrc.smis.form.VorgRelForm"%>
<%@page import="com.cbrc.org.entity.SysFlag"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title></title>
    

	<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">
		function go_Back(){
			<%
				if(request.getAttribute("proram")!=null && !request.getAttribute("proram").toString().equals("") && request.getAttribute("proram").toString().equals("org_setting"))
				{
			%>
				location.href='<%=request.getContextPath() %>/system_mgr/OrgInfo/view.do';
			<%
				}else{
			%>
				location.href='<%=request.getContextPath() %>/searchVorgRelInitAction.do';
			<%
				}
			%>
		}
	</script>
  </head>
  
  
  <body>
    <%
    	vOrgRel orgRel =  (vOrgRel)request.getAttribute("vOrgRel");
    	List<vOrgRel> orgRelList = (List<vOrgRel>)request.getAttribute("orgRelList");
    	String orgId = request.getAttribute("orgId")==null?"":(String)request.getAttribute("orgId");
    	String orgNm = "";
    	String preOrgId = "";
    	String sysFlag = "";
    	String fromUrl = "";
    	if(orgRel!=null){
    		sysFlag = orgRel.getId().getSysFlag();
    		orgNm = orgRel.getOrgNm();
    		preOrgId = orgRel.getPreOrgid();
    		fromUrl = request.getContextPath()+"/updateVOrgRelAction.do";
    	}else{
    		fromUrl = request.getContextPath()+"/addVOrgRelAcion.do";
    	}
    	
    %>
    
    <table cellspacing="0" cellpadding="0" border="0" width="98%">
		<tr>
			<td height="5"></td>
		</tr>
		<tr>
			 <td>当前位置 >> 机构管理 >> 设置映射关系</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
    <br>
		
	    	<table width="80%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
		      <tr class="titletab">
		            <th align="center">
		            	设置映射关系
		            </th>
		      </tr>
		      <tr>
		      	<td bgcolor="#ffffff">
		      	    <form action="<%=fromUrl %>" method="post">
			      	  <table width="100%" border="0" align="center">
					  
			          <tr>
			         	 <td align="center" bgcolor="#ffffff" style="padding: 4px 4px 4px 4px">
			         			机构代号:<input name="id.orgId"  value="<%=orgId %>" <%=orgId!=null && !orgId.equals("")?"readonly=\"readonly\"":"" %>  class="input-button" />
			             </td> 
			             
			          </tr>
				
			      
				      <tr>
				      	<td align="center" bgcolor="#ffffff" style="padding: 4px 4px 4px 4px">
				      		映射名称:<input name="orgNm" value="<%=orgNm %>" class="input-button"/>
				      	</td>
				      </tr>
				      <tr>
				      	<td align="center" bgcolor="#ffffff" style="padding: 4px 4px 4px 4px">
				      		系统标志:
				      		<select name="id.sysFlag" class="input-button" style="width: 133px">
				      			<%
				      				List<SysFlag> flagList = (List<SysFlag>)request.getAttribute("flagList");
				      				if(flagList!=null && flagList.size()>0){
				      					for(SysFlag s :flagList){
				      			%>
				      				<option value="<%=s.getSysFlag() %>"><%=s.getSysFlag() %></option>
				      			<%
				      					}
				      				}
				      			%>
				      		</select>
				      	</td>
				      </tr>
				       <tr>
				      	<td align="center" bgcolor="#ffffff" style="padding: 4px 4px 4px 4px">
				      		上级机构:
				      		<select name="preOrgid" style="width: 133px">
				      			<option value="0">无</option>
				      			<%
				      				for(vOrgRel f : orgRelList){
				      					String select = "";
				      					if(f.getId().getOrgId()!=null && f.getId().getOrgId().equals(preOrgId))
				      						select = "selected='selected'";
				      					else
				      						select = "";
				      			%>
				      				<option value="<%=f.getId().getOrgId() %>" <%=select %>><%=f.getId().getOrgId() %></option>
				      			<%  } %>
				      		</select>
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
				            <input type="submit" value="<%=orgRel==null?"保存":"修改" %>" class="input-button"/>
				            	&nbsp;
				            	<html:button property="back" value="返回" styleClass="input-button" onclick="go_Back()"/>
				            </td>
				            </tr>
				          </table>
				    </form>
		      	</td>
		      </tr>
		    </table>
  </body>
</html>
