<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.common.Config" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
<title>�û�����Ϣ����</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript">
		function deleteUserGrp(userGrpId)
		{
			if(confirm("��ȷ��Ҫɾ�����û�����Ϣ��?"))
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
			 <td>��ǰλ�� >> ϵͳ���� >> �û������</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
	
 	<table width="90%"  border="0" align="center">
		  <tr>
		  	<td>
				<font color="red">�û���Ȩ�޻��ڻ������ͷ�Χ�趨<br>
				����û���Ȩ��ǰ�����ڡ�ģ�嶨��-�޸�-���ͷ�Χ�޸ġ��򡰻�������-����Χ����������ϻ������ͷ�Χ
		    	</font>
		    </td>
		    <td align="right">
		    	<html:button property="addDept" value="�����û���" styleClass="input-button" onclick="location.assign('user_group_mgr/user_group_add.jsp')"/>
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
       	   	�û��������Ϣ
       	   </th>
        </tr>
        <tr align="center" class="middle">
	          <td align="center" width="5%">
	          	���
	          </td>
	          <td align="center" width="25%">
	          	�û�������
	          </td>
	          <td align="center" width="10%">
	          	�޸�
	          </td>
	          <td align="center" width="10%">
	          	ɾ��
	          </td>
	          <td align="center" width="50%">
	          	Ȩ�޷���
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
				                	δ֪
				                </logic:empty>
			                </td>
			                <td bgcolor="#ffffff">
			                <a href="../user_group_mgr/user_group_update.jsp?userGrpNm=<bean:write name="item" property="userGrpNm"/>&userGrpId=<bean:write name="item" property="userGrpId"/>">�޸�</a>
			                </td>
			                <td bgcolor="#ffffff">
			                	<a href="javascript:deleteUserGrp(<bean:write name="item" property="userGrpId"/>)">ɾ��</a>
			                </td>
			                <td bgcolor="#ffffff">
			                   	<%-- <a href="../viewUserGrpOrgPopedom.do?powType=<%=Config.POWERTYPEVERIFY%>&userGrpNm=<bean:write name="item" property="userGrpNm"/>&userGrpId=<bean:write name="item" property="userGrpId"/>&curPage=<bean:write name="ApartPage" property="curPage"/>">����Ȩ��</a>
			                	&nbsp;--%>	                	
			                	<a href="../viewUserGrpOrgPopedom.do?powType=<%=Config.POWERTYPEREPORT%>&userGrpNm=<bean:write name="item" property="userGrpNm"/>&userGrpId=<bean:write name="item" property="userGrpId"/>&curPage=<bean:write name="ApartPage" property="curPage"/>">����Ȩ��</a>
			                	&nbsp;&nbsp;&nbsp;
			                	<a href="../viewUserGrpOrgPopedom.do?powType=<%=Config.POWERTYPECHECK%>&userGrpNm=<bean:write name="item" property="userGrpNm"/>&userGrpId=<bean:write name="item" property="userGrpId"/>&curPage=<bean:write name="ApartPage" property="curPage"/>">���Ȩ��</a>
			                	&nbsp;&nbsp;&nbsp;
			                	<a href="../viewUserGrpOrgPopedom.do?powType=<%=Config.POWERTYPESEARCH%>&userGrpNm=<bean:write name="item" property="userGrpNm"/>&userGrpId=<bean:write name="item" property="userGrpId"/>&curPage=<bean:write name="ApartPage" property="curPage"/>">�鿴Ȩ��</a>

			                </td>			                
			         </tr>
				</logic:iterate>
		  </logic:present>
		  <logic:notPresent name="Records" scope="request">
				<tr align="left">
					<td bgcolor="#ffffff" colspan="5">
						�����û�����Ϣ
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
