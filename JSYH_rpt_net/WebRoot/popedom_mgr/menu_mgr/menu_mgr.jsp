<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
<title>功能模块管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript">
		function deleteMenu(menuNo)
		{
			if(confirm("你确定要删除该功能模块信息吗?"))
				window.location = "../popedom_mgr/deleteToolSetting.do?menuId="+menuNo;
			
		}
		//修改
		function updateMenu(menuId,menuName,functionName,url)
		{
			document.getElementById("menuId").value=menuId;
			document.getElementById("menuName").value=menuName;
			document.getElementById("functionName").value=functionName;
			document.getElementById("url").value=url;
			document.getElementById("updateform").submit();
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
	
 	<table width="100%"  border="0" align="center">
		  <tr >
		  	<td height="20">
		  	<td>
		  </tr>
		  <tr>
		    <td align="right">
		    	<html:button property="addMenu" value="增加功能模块" styleClass="input-button" onclick="location.assign('menu_mgr/menu_add.jsp')"/>
		    </td>
		  </tr>
		  <tr >
		  	<td height="5">
		  	<td>
		  </tr>
   </table>
  <table width="95%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
       <tr class="titletab">
       	   <th height="25" align="center" colspan="6">
       	   	功能模块基本信息
       	   </th>
        </tr>
        <tr align="center" class="middle">
	          <td align="center" width="7%" >
	          	序号
	          </td>
	          <td align="center" width="23%">
	          	功能模块名称
	          </td>
	          <td align="center" width="15%">
	          	功能名称
	          </td>
	          <td align="center" width="35%">
	          	功能页面
	          </td>
	          <td align="center" width="10%">
	          	修改
	          </td>
	          <td align="center" width="10%">
	          	删除
	          </td>
         </tr>
		  <logic:present name="Records" scope="request">
				<logic:iterate id="item" name="Records" indexId="index">
					 <tr align="center" >
			                <td bgcolor="#ffffff">
			                	<%=((Integer)index).intValue() + 1%>
			                </td>
			                <td bgcolor="#ffffff">
				                <logic:notEmpty name="item" property="menuName">
				                	<bean:write name="item" property="menuName"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="menuName">
				                	未知
				                </logic:empty>
			                </td>
			                <td bgcolor="#ffffff">
				                <logic:notEmpty name="item" property="functionName">
				                	<bean:write name="item" property="functionName"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="functionName">
				                	未知
				                </logic:empty>
			                </td>
			                <td bgcolor="#ffffff">
				                <logic:notEmpty name="item" property="url">
				                	<bean:write name="item" property="url"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="url">
				                	未知
				                </logic:empty>
			                </td>
			                <td bgcolor="#ffffff">
			                	<a href="javascript:updateMenu(<bean:write name="item" property="menuId"/>,'<bean:write name="item" property="menuName"/>','<bean:write name="item" property="functionName"/>','<bean:write name="item" property="url"/>')">修改</a>
			                </td>
			                <td bgcolor="#ffffff">	
			                	<a href="javascript:deleteMenu(<bean:write name="item" property="menuId"/>)">删除</a>
			                </td>
			         </tr>
				</logic:iterate>
		  </logic:present>
		 
		  <logic:notPresent name="Records" scope="request">
				<tr align="left">
					<td bgcolor="#ffffff" colspan="6">
						暂无功能模块信息
					</td>
				</tr>
			</logic:notPresent>
    </table>
    
    <form action="../menu_mgr/menu_update.jsp" method="POST" id="updateform">
		  	<input type="hidden" name="menuId" id="menuId">
		  	<input type="hidden" name="covert" id="covert">
		  	<input type="hidden" name="menuName" id="menuName">
		  	<input type="hidden" name="functionName" id="functionName">
		  	<input type="hidden" name="url" id="url">
	</form>
    
    <table width="95%" border="0">
			<TR>
				<TD width="80%">
					<jsp:include page="../../apartpage.jsp" flush="true">
						<jsp:param name="url" value="../viewToolSetting.do"/>
					</jsp:include>
				</TD>
			</tr>
	</table>
</body>
</html:html>
