<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
<title>������������</title>
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
	<br>
 	<table border="0"width="100%">
				<html:form action="/viewDepartmentMgr" method="POST">
					<tr height="10">
						<td>					
						</td>
					</tr>
					<TR>
						<TD width="35%" align="right">
							�������ƣ�
						</TD>
						<TD width="25%" align="center">
							<html:text property="orgName" size="28"  styleClass="input-text"/>
						</TD>
						<td width="40%">
							<html:submit value="��ѯ" styleClass="input-button"/>
						</td>
					</TR>
					</html:form>
				</table>
				<br>
				<br>
  <table width="90%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
       <tr class="titletab">
       	   <th height="25" align="center" colspan="8">
       	   	������������
       	   </th>
        </tr>
        <tr align="center" class="middle">
	          <td align="center" width="5%" >
	          	���
	          </td>
	          <td align="center" width="20%">
	          	����ID
	          </td>
	          <td align="center" width="20%">
	          	��������
	          </td>
	          <td align="center" width="20%">
	          	����Id
	          </td>
	          <td align="center" width="20%">
	          	��������
	          </td>
	          <td align="center" width="10%">
	          	��������
	          </td><!--
	          <td align="center" width="10%">
	          	ɾ��
	          </td>
         --></tr>
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
				                	δ֪
				                </logic:empty>
			                </td>
			                
			                <td bgcolor="#ffffff">
				                <logic:notEmpty name="item" property="orgName">
				                	<bean:write name="item" property="orgName"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="orgName">
				                	δ֪
				                </logic:empty>
			                </td>
			                 <td bgcolor="#ffffff">
				                <logic:notEmpty name="item" property="departmentId">
				                	<bean:write name="item" property="departmentId"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="departmentId">
				                	δ֪
				                </logic:empty>
			                </td>
			                <td bgcolor="#ffffff">
				                <logic:notEmpty name="item" property="deptName">
				                	<bean:write name="item" property="deptName"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="deptName">
				                	δ֪
				                </logic:empty>
			                </td>	
			                	                
			    <td bgcolor="#ffffff">
			    <a href="../departmentmgr/updatedepartment.jsp?orgId=<bean:write name="item" property="orgId"/>
			    &departmentId=<bean:write name="item" property="departmentId"/>
			    &deptName=<bean:write name="item" property="deptName"/>">��������</a>
			    </td>
			                <!--
			                <td bgcolor="#ffffff">
			                	<a href="javascript:deleteDept(<bean:write name="item" property="departmentId"/>)">ɾ��</a>
			                </td>
			                 -->
			         </tr>
				</logic:iterate>
		  </logic:present>
		  <logic:notPresent name="Records" scope="request">
				<tr align="left">
					<td bgcolor="#ffffff" colspan="8">
						���޲�����Ϣ
					</td>
				</tr>
			</logic:notPresent>
    </table>
    <table width="70%" border="0">
			<TR>
				<TD width="80%">
					<jsp:include page="../../apartpage.jsp" flush="true">
						<jsp:param name="url" value="../viewDepartmentMgr.do"/>
					</jsp:include>
				</TD>
			</tr>
	</table>
</body>
</html:html>
