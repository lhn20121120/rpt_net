<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@page import="java.util.List"%>
<%@page import="com.fitech.gznx.po.vOrgRel"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
<title>映射关系管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript">
		function deleteDept(deptNo)
		{
			if(confirm("你确定要删除该部门信息吗?"))
				window.location = "../popedom_mgr/deleteDepartment.do?departmentId="+deptNo;
			
		}
		function addRel(){
			location.href='<%=request.getContextPath()%>/searchVorgRelAction.do';
		}
	</script>
	
</head>

<body >
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table cellspacing="0" cellpadding="0" border="0" width="98%">
		<tr>
			<td height="5"></td>
		</tr>
		<tr>
			 <td>当前位置 >> 系统管理 >> 映射关系管理</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
    <br>
 	<table width="95%"  border="0" align="center">
 
		  <tr>
		    <td align="right">
		    	<html:button property="addDept" value="增加映射关系" styleClass="input-button" onclick="addRel()"/>
		    </td>
		  </tr>
		  <tr >
		  	<td height="5">
		  	<td>
		  </tr>
   </table>
  <table width="90%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
       <tr class="titletab">
       	   <th height="25" align="center" colspan="5">
       	   	映射信息
       	   </th>
        </tr>
        <tr align="center" class="middle">
	          <td align="center" >
	          	编号
	          </td>
	          <td align="center">
	          	机构名称
	          </td>
	          <td align="center" >
	          	系统标志
	          </td>
	          <td align="center">
	          	上级机构
	          </td>
	          <td align="center">
	          	操作
	          </td>
         </tr>
         <%
         	List<vOrgRel> relList = (List<vOrgRel>)request.getAttribute("relList");
         	if(relList!=null && relList.size()>0){
         		for(vOrgRel r : relList){		
         %>
         	 <tr align="center" >
         	 	<td bgcolor="#ffffff">
         	 		<%=r.getId().getOrgId() %>
         	 	</td>
         	 	<td bgcolor="#ffffff">
         	 		<%=r.getOrgNm() %>
         	 	</td>
         	 	<td bgcolor="#ffffff">
         	 		<%=r.getId().getSysFlag() %>
         	 	</td>
         	 	<td bgcolor="#ffffff">
         	 		<%=r.getPreOrgid() %>
         	 	</td>
         	 	<td bgcolor="#ffffff">
         	 		<a href="../../searchVorgRelAction.do?orgId=<%=r.getId().getOrgId()%>">修改</a>
         	 		&nbsp;&nbsp;
         	 		<a href="../../deleteVorgRelAction.do?id.orgId=<%=r.getId().getOrgId()%>&&id.sysFlag=<%=r.getId().getSysFlag() %>">删除</a>
         	 	</td>
         	 </tr>
         <%
         		}
         	}else{   	
         %>
         	<tr align="left">
					<td bgcolor="#ffffff" colspan="5">
						暂无映射信息
					</td>
			</tr>
         <%
         	}
         %>
    </table>
    <table width="70%" border="0">
			<TR>
				<TD width="80%">
					<jsp:include page="../../apartpage.jsp" flush="true">
						<jsp:param name="url" value="../../searchVorgRelInitAction.do"/>
					</jsp:include> 
				</TD>
			</tr>
	</table>
</body>
</html:html>
