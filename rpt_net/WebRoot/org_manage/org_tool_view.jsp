<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
<title> 机构功能分配</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript">
		
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
	
 	<table width="95%"  border="0" align="center">
		  <tr >
		  	<td height="20">
		  	<td>
		  </tr>
		  <tr>
		    <td align="right">
		    	<html:button property="addDept" value="机构功能分配批量分配" styleClass="input-button" onclick="location.assign('./role_mgr/role_add.jsp')"/>
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
       	   	机构基本信息
       	   </th>
        </tr>
        <tr align="center" class="middle">
	          <td align="center" width="15%" >
	          	序号
	          </td>
	          <td align="center" width="40%">
	          	机构ID
	          </td>
	          <td align="center" width="15%">
	          	机构名称
	          </td>
	          
	          <td align="center" width="15%">
	          	功能分配
	          </td>
         </tr>
		  <logic:present name="Records" scope="request">
				<logic:iterate id="item" name="Records" indexId="index">
					 <tr align="center" >
			                <td bgcolor="#ffffff">
			                	<%=((Integer)index).intValue() + 1%>
			                </td>
			                <td bgcolor="#ffffff">
				                <logic:notEmpty name="item" property="orgId">
				                	<bean:write name="item" property="orgId"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="orgId">
				                	未知
				                </logic:empty>
			                </td>
			                <td bgcolor="#ffffff">
			                
			                <bean:write name="item" property="orgName"/>
			                </td>
			                
			                <td bgcolor="#ffffff">
			                	<a href="../orgmanage/org_tool_add.do?id=<bean:write name="item" property="orgId"/>&orgName=<bean:write name="item" property="orgName"/>">功能分配</a>
			                </td>
			         </tr>
				</logic:iterate>
		  </logic:present>
		  <logic:notPresent name="Records" scope="request">
				<tr align="left">
					<td bgcolor="#ffffff" colspan="5">
						暂无机构信息
					</td>
				</tr>
			</logic:notPresent>
    </table>
    <table width="70%" border="0">
			<TR>
				<TD width="80%">
					<jsp:include page="../apartpage.jsp" flush="true">
						<jsp:param name="url" value="../orgmanage/org_tool_view.do"/>
					</jsp:include>
				</TD>
			</tr>
	</table>
</body>
</html:html>
