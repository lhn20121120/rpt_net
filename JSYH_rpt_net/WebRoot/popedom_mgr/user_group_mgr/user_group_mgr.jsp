<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.common.Config" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
<title>用户组信息管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript">
		function deleteUserGrp(userGrpId)
		{
			if(confirm("你确定要删除该用户组信息吗?"))
				window.location = "../popedom_mgr/deleteMUserGrp.do?userGrpId="+userGrpId;
			
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
			 <td>当前位置 >> 系统管理 >> 用户组管理</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
	
 	<table width="90%"  border="0" align="center">
		  <tr>
		  	<td>
				<font color="red">用户组权限基于机构报送范围设定<br>
				添加用户组权限前请先在“模板定义-修改-报送范围修改”或“机构管理-报表范围”中设置完毕机构报送范围
		    	</font>
		    </td>
		    <td align="right">
		    	<html:button property="addDept" value="增加用户组" styleClass="input-button" onclick="location.assign('user_group_mgr/user_group_add.jsp')"/>
		    </td>
		  </tr>
		  <tr >
		  	<td height="5">
		  	<td>
		  </tr>
   </table>
  <table width="90%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
       <tr class="titletab">
       	   <th height="25" align="center" colspan="6">
       	   	用户组基本信息
       	   </th>
        </tr>
        <tr align="center" class="middle">
	          <td align="center" width="5%">
	          	序号
	          </td>
	          <td align="center" width="25%">
	          	用户组名称
	          </td>
	          <td align="center" width="10%">
	          	修改
	          </td>
	          <td align="center" width="10%">
	          	删除
	          </td>
	          <td align="center" width="50%">
	          	权限分配
	          </td>	          
         </tr>
		  <logic:present name="Records" scope="request">
				<logic:iterate id="item" name="Records" indexId="index">
					 <tr align="center" >
			                <td bgcolor="#ffffff">
			                	<%=((Integer)index).intValue() + 1%>
			                </td>
			                <td bgcolor="#ffffff">
				                <logic:notEmpty name="item" property="userGrpNm">
				                	<bean:write name="item" property="userGrpNm"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="userGrpNm">
				                	未知
				                </logic:empty>
			                </td>
			                <td bgcolor="#ffffff">
			                <a href="../user_group_mgr/user_group_update.jsp?userGrpNm=<bean:write name="item" property="userGrpNm"/>&userGrpId=<bean:write name="item" property="userGrpId"/>">修改</a>
			                </td>
			                <td bgcolor="#ffffff">
			                	<a href="javascript:deleteUserGrp(<bean:write name="item" property="userGrpId"/>)">删除</a>
			                </td>
			                <td bgcolor="#ffffff">
			                   	<%-- <a href="../viewUserGrpOrgPopedom.do?powType=<%=Config.POWERTYPEVERIFY%>&userGrpNm=<bean:write name="item" property="userGrpNm"/>&userGrpId=<bean:write name="item" property="userGrpId"/>&curPage=<bean:write name="ApartPage" property="curPage"/>">复核权限</a>
			                	&nbsp;--%>	                	
			                	<a href="../viewUserGrpOrgPopedom.do?powType=<%=Config.POWERTYPEREPORT%>&userGrpNm=<bean:write name="item" property="userGrpNm"/>&userGrpId=<bean:write name="item" property="userGrpId"/>&curPage=<bean:write name="ApartPage" property="curPage"/>">报送权限</a>
			                	&nbsp;&nbsp;&nbsp;
			                	<a href="../viewUserGrpOrgPopedom.do?powType=<%=Config.POWERTYPECHECK%>&userGrpNm=<bean:write name="item" property="userGrpNm"/>&userGrpId=<bean:write name="item" property="userGrpId"/>&curPage=<bean:write name="ApartPage" property="curPage"/>">审核权限</a>
			                	&nbsp;&nbsp;&nbsp;
			                	<a href="../viewUserGrpOrgPopedom.do?powType=<%=Config.POWERTYPESEARCH%>&userGrpNm=<bean:write name="item" property="userGrpNm"/>&userGrpId=<bean:write name="item" property="userGrpId"/>&curPage=<bean:write name="ApartPage" property="curPage"/>">查看权限</a>

			                </td>			                
			         </tr>
				</logic:iterate>
		  </logic:present>
		  <logic:notPresent name="Records" scope="request">
				<tr align="left">
					<td bgcolor="#ffffff" colspan="5">
						暂无用户组信息
					</td>
				</tr>
			</logic:notPresent>
    </table>
    <table width="90%" border="0">
			<TR>
				<TD width="80%">
					<jsp:include page="../../apartpage.jsp" flush="true">
						<jsp:param name="url" value="../viewMUserGrp.do"/>
					</jsp:include>
				</TD>
			</tr>
	</table>
</body>
</html:html>
