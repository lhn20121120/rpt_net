<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
<title>������Ϣ����</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript">
		function deleteOrg(orgtNo)
		{
			if(confirm("��ȷ��Ҫɾ����������Ϣ��?"))
				window.location = "../delete.do?Id="+orgNo;
			
		}
	</script>
</head>

<body >
	
 <table width="95%"  border="0" align="center">
		  <tr >
		  	<td height="20">
		  	<td>
		  </tr>
		  <tr>
		    <td align="right">
		    	<html:button property="addorg" value="��������" styleClass="input-button" onclick="location.assign('FitechOrg/AddOrg.jsp')"/>
		    </td>
		  </tr>
		  <tr >
		  	<td height="5">
		  	<td>
		  </tr>
   </table>
  <table width="90%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
       <tr class="titletab">
       	   <th height="25" align="center" colspan="9">
       	   	���л�����Ϣ
       	   </th>
        </tr>
        <tr align="center" class="middle">
	          <td align="center" width="8%" >
	          ��������
	          </td>
	          <td align="center" width="15%">
	          	��������
	          </td>
	          <td align="center" width="12%">
	            ���з�������
	          </td>
	          <td align="center" width="10%">
	          �޸�
	          </td>
	          <td align="center" width="15%">
	          ɾ��
	          </td>    
         </tr>
		   <logic:present name="Records" scope="request">
				<logic:iterate id="item" name="Records" >
					<tr>
						<td bgcolor="#FFFFFF" align="center">
							<bean:write name="item" property="orgName"/>
						</td>
						<td bgcolor="#FFFFFF" align="center">
							<bean:write name="item" property="orgType"/>
						</td>
						<td bgcolor="#FFFFFF" align="center">
							<bean:write name="item" property="orgClsName"/>
						</td>
						<td bgcolor="#ffffff" align="center">
				            <a href="../Update.do?Id=<bean:write name="item" property="orgId"/>">�޸�</a>
				        </td>
				        <td bgcolor="#ffffff" align="center">
				            <a href="javascript:deleteOrg('<bean:write name="item" property="orgId"/>')">ɾ��</a>
				        </td>
			    </tr>
		      </logic:iterate>
         </logic:present>
    </table>
   <br>
<br>
<div class="head" align="center">
   ��<bean:write name="PAGER" property="currentPage"/>ҳ&nbsp;
   ��<bean:write name="PAGER" property="totalPages"/>ҳ&nbsp;
   <html:link action="FitechOrgname.do?method=queryWithPage&amp;pageMethod=first" paramName="PAGER" paramProperty="currentPage" paramId="currentPage">��ҳ</html:link>
   <html:link action="FitechOrgname.do?method=queryWithPage&amp;pageMethod=previous" paramName="PAGER" paramProperty="currentPage" paramId="currentPage">��һҳ</html:link>
   <html:link action="FitechOrgname.do?method=queryWithPage&amp;pageMethod=next" paramName="PAGER" paramProperty="currentPage" paramId="currentPage">��һҳ</html:link>
   <html:link action="FitechOrgname.do?method=queryWithPage&amp;pageMethod=last" paramName="PAGER" paramProperty="currentPage" paramId="currentPage">βҳ</html:link>
</div>
</body>
</html:html>