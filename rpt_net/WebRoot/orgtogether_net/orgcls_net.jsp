<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
<title>���������Ϣ</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript">
		function deleteOrgClsNet(orgclsId)
		{
			if(confirm("��ȷ��Ҫɾ���û���������Ϣ��?"))
				window.location="./deleteorgClsNet.do?orgClsId="+orgclsId;  
			
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
	<table border="0" width="90%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				��ǰλ�� >> �������͹��� >> ����������ҳ
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
 	<table width="95%"  border="0" align="center">
		  <tr >
		  	<td height="20">
		  	<td>
		  </tr>
		  <tr>
		    <td align="right">
		    
		    	<html:button property="addMOrgClsnet" value="���ӻ�������" styleClass="input-button" onclick="location.assign('./orgtogether_net/orgclsnet_add.jsp')"/>
		    </td>
		  </tr>
		  <tr>
		  	<td height="5">
		  	<td>
		  </tr>
   </table>
  <table width="90%" border="0" cellpadding="6" cellspacing="1" class="tbcolor">
       <tr class="titletab">
       	   <th height="25" align="center" colspan="6">
       	   	���������б�</th>
        </tr>
        <tr align="center" class="middle">
	          <td align="center" width="5%" >
	          	���
	          </td>
	          <td align="center" width="15%" >
	          	������
	          </td>
	          <td align="center" width="40%">
	          	������������
	          </td>
			  <td align="center" width="10%">
	          	��������
	          </td>
	          <td align="center" width="15%">
	          	�޸�
	          </td>
	          <td align="center" width="15%">
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
				                <logic:notEmpty name="item" property="orgClsId">
				                	<bean:write name="item" property="orgClsId"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="orgClsId">
				                	δ֪
				                </logic:empty>
			                </td>
			                 <td bgcolor="#ffffff">
				                <logic:notEmpty name="item" property="orgClsNm">
				                	<bean:write name="item" property="orgClsNm"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="orgClsNm">
				                	δ֪
				                </logic:empty>
			                </td>
			                 <td bgcolor="#ffffff">
				                <logic:notEmpty name="item" property="orgClsType">
				                	<bean:write name="item" property="orgClsType"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="orgClsType">
				                	δ֪
				                </logic:empty>
			                </td>
			                <td bgcolor="#ffffff">
			    <a href="../orgtogether_net/orgclsnet_update.jsp?orgClsNm=<bean:write name="item" property="orgClsNm"/>
			    &orgClsId=<bean:write name="item" property="orgClsId"/>
			    &orgClsType=<bean:write name="item" property="orgClsType"/>">�޸�</a>
			                </td>
			                <td bgcolor="#ffffff">
			                	<a href="javascript:deleteOrgClsNet('<bean:write name="item" property="orgClsId"/>')">ɾ��</a>
			                </td>
			         </tr>
				</logic:iterate>
		  </logic:present>
		  <logic:notPresent name="Records" scope="request">
				<tr align="left">
					<td bgcolor="#ffffff" colspan="6">
						���޻���������Ϣ
					</td>
				</tr>
			</logic:notPresent>
						
    </table>
    <table width="70%" border="0">
			<TR>
				<TD width="80%">
					<jsp:include page="../../apartpage.jsp" flush="true">
						<jsp:param name="url" value="../orgclsNet.do"/>
					</jsp:include>
				</TD>
			</tr>
	</table>
</body>
</html:html>