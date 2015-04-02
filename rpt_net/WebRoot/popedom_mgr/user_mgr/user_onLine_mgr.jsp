<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
<title>�����û���Ϣ</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript" src="../../js/jquery-1.4.2.js"></script>
	<script language="javascript">
		function deleteUser(userId){
			if(confirm("��ȷ��Ҫɾ�����û���Ϣ��?"))
				window.location = "../popedom_mgr/deleteOperator.do?userId="+userId;
		}
		function treeViewOperator(){
			window.open("<%=request.getContextPath()%>/orgNetInfoTree.do");
		}
		function _exit(sessionId,userName){
			var res = confirm("ȷ��Ҫע���û�"+userName+"��?");
			if(res){
				window.location= "<%=request.getContextPath()%>/popedom_mgr/viewOnlineOperator.do?m=onLineUserExit"+
				    "&sessionId="+sessionId;
			}
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
			 <td>��ǰλ�� &gt;&gt; ϵͳ���� &gt;&gt; �����û���Ϣ�鿴</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
	<br>
 <!--  	<table width="90%"  border="0" align="center">
 	<html:form action="/popedom_mgr/viewOperator" method="post" styleId="frm">
		<tr > 
		   <td>
				�û���(������)��
				<html:text property="userName" size="15" styleClass="input-text" />
				&nbsp;&nbsp;&nbsp;
				�������ƣ�
				<html:text property="orgName" size="15" styleClass="input-text" />
				&nbsp;
				<html:submit styleClass="input-button" value=" �� ѯ " />
			</td>
		<%--	<%
				if(!Config.PORTAL){
			 %>--%>
		    <td align="right">		    			    	
		    	<html:button property="addDept" value="�����û�" styleClass="input-button" onclick="location.assign('user_mgr/user_add.jsp')"/>
		    </td>	
		  <%--  <%
		    	}
		     %>	    --%>
		  </tr>
		  <tr >
		  	<td height="5">
		  	<td>
		  </tr>
	</html:form>	  
   </table>
   <br/>-->
  <table width="90%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
       <tr class="titletab">
       	   <th height="25" align="center" colspan="5">
       	   	�����û���Ϣ
       	   </th>
        </tr>
        <tr align="center" class="middle">
	          <td align="center" width="5%" >
	          	���
	          </td>
	          <td align="center" width="12%">
	          	�û���
	          </td>
	          <td align="center" width="12%">
	          	����
	          </td>
	          <td align="center" width="25%">
	          	�û�����
	          </td>
	          <td align="center" width="10%">
	          	����
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
				                	<bean:write name="item" property="userName"/>
				                </logic:notEmpty>
				             </td>
			                 <td bgcolor="#ffffff">			    
				              <bean:write name="item" property="operatorName"/>
			                </td>
			                 <td bgcolor="#ffffff">
				                <logic:notEmpty name="item" property="orgName">
				                	<bean:write name="item" property="orgName"/>
				                </logic:notEmpty>
				             </td>
			                 <td bgcolor="#ffffff">
				               	 <a href="javascript:;" onclick="_exit('<bean:write name="item" property="sessionId"/>',
				               	                '<bean:write name="item" property="userName"/>')">ע��</a>
			                </td>
			         </tr>
				</logic:iterate>
		  </logic:present>
		  <logic:notPresent name="Records" scope="request">
				<tr align="left">
					<td bgcolor="#ffffff" colspan="9">
						�����û���Ϣ
					</td>
				</tr>
			</logic:notPresent>
    </table>
    <table width="90%" border="0">
			<TR>
				<TD width="80%">&nbsp;<jsp:include page="../../apartpage.jsp" flush="true">
					
					<jsp:param name="url" value="../viewOnlineOperator.do?m=searchOnlineUser"/>
					</jsp:include>
				</TD>
			</tr>
	</table>
</body>
</html:html>
