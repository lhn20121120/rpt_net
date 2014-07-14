<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
	<html:base/>
<title>�������</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript">
		function deleteOrg(Id)
		{
			if(confirm("��ȷ��Ҫɾ����ϵͳ������?"))
				window.location = "../orgmanage/deletefunction.do?id="+Id;
			
		}
		function viewOrg(id)
		{
		window.location="../orgmanage/view_topic_org.do?id="+id;
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
	
 	<table width="95%"  border="0" align="center">
		  <tr >
		  	<td height="20">
		  	<td>
		  </tr>
		  <tr>
		    <td align="right">
		    
		    	<html:button property="addMOrgCls" value="����ϵͳ������" styleClass="input-button" onclick="location.assign('../org_manage/function_add.jsp')"/>
		    </td>
		  </tr>
		  <tr >
		  	<td height="5">
		  	<td>
		  </tr>
   </table>
  <table width="90%" border="0" cellpadding="5" cellspacing="1" class="tbcolor">
       <tr class="titletab">
       	   <th height="25" align="center" colspan="5"> ϵͳ�����б�</th>
        </tr>
        <tr align="center" class="middle">
	          <td align="center" width="10%" >
	          	���
	          </td>
	          <td align="center" width="25%" >
	          	 ϵͳ��������
	           </td>
	          
			  <td align="center" width="25%">
	          	ϵͳ��������
	          </td>
	          <td align="center" width="20%">
	          	�޸�
	          </td>
	          <td align="center" width="20%">
	          	ɾ��
	          </td>
	         
         </tr>		  
		<logic:present name="Records" scope="request">
				<logic:iterate id="item" name="Records" indexId="index">
					 <tr align="center" >
			                <td bgcolor="#ffffff">
			                	<%=((Integer)index).intValue() + 1%>
			                </td>
			                <td bgcolor="#ffffff">
				                <logic:notEmpty name="item" property="functionName">
				                	<bean:write name="item" property="functionName"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="functionName">
				                	δ֪
				                </logic:empty>
			                </td>
			                 <td bgcolor="#ffffff">
				                <logic:notEmpty name="item" property="functionDes">
				                	<bean:write name="item" property="functionDes"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="functionDes">
				                	δ֪
				                </logic:empty>
			                </td>
			                 
			                <td bgcolor="#ffffff">
			    <a href="../orgmanage/Editfunction.do?id=<bean:write name="item" property="id"/>">�޸�</a>
			                </td>
			                <td bgcolor="#ffffff">
			                	<a href="javascript:deleteOrg('<bean:write name="item" property="id"/>')">ɾ��</a>
			                </td>
			               
			         </tr>
				</logic:iterate>
		  </logic:present>
		  <logic:notPresent name="Records" scope="request">
				<tr align="left">
					<td bgcolor="#ffffff" colspan="4">
						������Ϣ
					</td>
				</tr>
			</logic:notPresent>
						
    </table>
    <table width="70%" border="0">
			<TR>
				<TD width="80%">
					<jsp:include page="../apartpage.jsp" flush="true">
						<jsp:param name="url" value="../orgmanage/vieworg.do"/>
					</jsp:include>
				</TD>
			</tr>
	</table>
</body>
</html:html>
