<%@ page language="java" pageEncoding="GBK"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
<title>用户信息管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript">
		function deleteUser(userId){
			if(confirm("你确定要删除该用户信息吗?"))
				window.location = "../popedom_mgr/deleteOperator.do?userId="+userId;
		}
		function treeViewOperator(){
			window.open("<%=request.getContextPath()%>/orgNetInfoTree.do");
		}
	</script>
	<%
	  String curPage=request.getParameter("curPage");
	
	%>
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
			 <td>当前位置 &gt;&gt; 系统管理 &gt;&gt; 用户管理</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
	<br>
 	<table width="90%"  border="0" align="center">
 	<html:form action="/popedom_mgr/viewOperator" method="post" styleId="frm">
		<tr > 
		   <td>
				用户名(或姓名)：
				<html:text property="userName" size="15" styleClass="input-text" />
				&nbsp;&nbsp;&nbsp;
				机构名称：
				<html:text property="orgName" size="15" styleClass="input-text" />
				&nbsp;
				<html:submit styleClass="input-button" value=" 查 询 " />
			</td>
		    <td align="right">		    			    	
		    	<html:button property="addDept" value="增加用户" styleClass="input-button" onclick="location.assign('user_mgr/user_add.jsp')"/>
		    </td>	    
		  </tr>
		  <tr >
		  	<td height="5">
		  	<td>
		  </tr>
	</html:form>	  
   </table>
   <br/>
  <table width="90%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
       <tr class="titletab">
       	   <th height="25" align="center" colspan="9">
       	   	用户基本信息
       	   </th>
        </tr>
        <tr align="center" class="middle">
	          <td align="center" width="5%" >
	          	序号
	          </td>
	          <td align="center" width="12%">
	          	用户名
	          </td>
	          <td align="center" width="12%">
	          	姓名
	          </td>
	          <td align="center" width="25%">
	          	用户机构
	          </td>
	          <td align="center" width="10%">
	          	办公室电话
	          </td>
	          <td align="center" width="8%">
	          	修改
	          </td>
	          <td align="center" width="8%">
	          	删除
	          </td>
	          <td align="center"  colspan="2" width="20%">
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
				                <logic:notEmpty name="item" property="userName">
				                	<a href="../viewUserDetail.do?userId=<bean:write name="item" property="userId"/>">
				                		<bean:write name="item" property="userName"/>
				                	</a>
				                </logic:notEmpty>
				             </td>
			                 <td bgcolor="#ffffff">			    
				                <bean:write name="item" property="firstName"/><bean:write name="item" property="lastName"/>
			                </td>
			                 <td bgcolor="#ffffff">
				                <logic:notEmpty name="item" property="orgName">
				                	<bean:write name="item" property="orgName"/>
				                </logic:notEmpty>
				             </td>
			                 <td bgcolor="#ffffff">
				                <logic:notEmpty name="item" property="telephoneNumber">
				                	<bean:write name="item" property="telephoneNumber"/>
				                </logic:notEmpty>
			                </td>
			                <td bgcolor="#ffffff">
                           <logic:notEqual value="A0476" name="item" property="userName">
                              <a href="../viewUpdateOperator.do?userId=<bean:write name="item" property="userId"/>&curPage=<%=curPage%>">修改</a>
                           </logic:notEqual> 
			                <logic:equal value="${Operator.operatorId}" name="item" property="userId">
                                <logic:equal value="A0476" name="item" property="userName">
                                	<%-- 1.18 yql <a href="../viewUpdateOperator.do?userId=<bean:write name="item" property="userId"/>">修改</a> --%>
                                    <a href="../viewUpdateOperator.do?userId=<bean:write name="item" property="userId"/>&curPage=<%=curPage%>">修改</a>
                                </logic:equal>
                            </logic:equal>
			                </td>
			                <td bgcolor="#ffffff">                                
                                <logic:notEqual value="${Operator.operatorId}" name="item" property="userId">
                                  <logic:notEqual value="A0476" name="item" property="userName">
                                      <a href="javascript:deleteUser(<bean:write name="item" property="userId"/>)">删除</a>
                                  </logic:notEqual>                        
                                </logic:notEqual>
			                </td>
			                <td bgcolor="#ffffff">
			                	<a href="../viewMUserToGrp.do?userName=<bean:write name="item" property="userName"/>&userId=<bean:write name="item" property="userId"/>&curPage=<%=curPage%>">用户组分配</a>
			                </td>
			                <td bgcolor="#ffffff">
			                	<a href="../viewUserRole.do?userName=<bean:write name="item" property="userName"/>&userId=<bean:write name="item" property="userId"/>&curPage=<%=curPage%>">角色分配</a>
			                </td>
			         </tr>
				</logic:iterate>
		  </logic:present>
		  <logic:notPresent name="Records" scope="request">
				<tr align="left">
					<td bgcolor="#ffffff" colspan="9">
						暂无用户信息
					</td>
				</tr>
			</logic:notPresent>
    </table>
    <table width="90%" border="0">
			<TR>
				<TD width="80%">&nbsp;<jsp:include page="../../apartpage.jsp" flush="true">
					
					<jsp:param name="url" value="../viewOperator.do"/>
					</jsp:include>
				</TD>
			</tr>
	</table>
</body>
</html:html>
