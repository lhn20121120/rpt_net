<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ page import="com.cbrc.smis.other.Expression"%>

<html:html locale="true">
<head>
	<html:base/>
<title>�û���Ϣ����</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript">
		<logic:present name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="<bean:write name='ApartPage' property='curPage'/>";
	    </logic:present>
	    <logic:notPresent name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="1";
	    </logic:notPresent>
	    
		function treeViewOperator(){
			window.open("<%=request.getContextPath()%>/orgNetInfoTree.do");
		}
		function resetPWD(userId){
			var objFrm=document.forms['frm'];
			var userName=objFrm.elements['userName'].value;
			if(confirm("��ȷ��Ҫ���ø��û��ĵ�¼������\n")==true){
				window.location="<%=request.getContextPath()%>/system_mgr/resetPwd.do?userId=" + userId 
					+ "&userName=" + userName + "&curPage=" + curPage;			  	  				
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
			 <td>��ǰλ�� &gt;&gt; ϵͳ���� &gt;&gt; ��������</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
    <br>
 	<table width="90%"  border="0" align="center">
 	<html:form action="/system_mgr/viewResetPwd" method="post" styleId="frm">
		 
		  <tr> 
		   <td>
				�û���������������
				<html:text property="userName" size="15" styleClass="input-text" />
				&nbsp;&nbsp;&nbsp;
				�û�������
				<html:text property="orgName" size="15" styleClass="input-text" />
				&nbsp;
				<html:submit styleClass="input-button" value=" �� ѯ " />
			</td>
		  </tr>
		  <tr >
		  	<td height="5">
		  	<td>
		  </tr>
	</html:form>	  
   </table>
  <table width="90%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
       <tr class="titletab">
       	   <th height="25" align="center" colspan="9">
       	   	�û�������Ϣ
       	   </th>
        </tr>
        <tr align="center" class="middle">
	          <td align="center" width="8%" >
	          	���
	          </td>
	          <td align="center" width="15%">
	          	�û���
	          </td>
	          <td align="center" width="12%">
	          	����
	          </td>
	          <td align="center" width="15%">
	          	�û�����
	          </td>
	          <td align="center" width="10%">
	          	�칫�ҵ绰
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
			                	<input type="button" name="viewResetPwd" value="��������" onclick="resetPWD(<bean:write name="item" property="userId"/>)" class="input-button">
			                </td>
			         </tr>
				</logic:iterate>
		  </logic:present>
		  <logic:notPresent name="Records" scope="request">
				<tr align="left">
					<td bgcolor="#ffffff" colspan="6">
						�����û���Ϣ
					</td>
				</tr>
			</logic:notPresent>
    </table>
    <table width="90%" border="0">
			<TR>
				<TD width="80%">
					<jsp:include page="../apartpage.jsp" flush="true">
						<jsp:param name="url" value="../system_mgr/viewResetPwd.do"/>
					</jsp:include>
				</TD>
			</tr>
	</table>
</body>
</html:html>
