<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
<title>��������</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript">
		function deleteOrgnet(OrgId)
		{
			if(confirm("��ȷ��Ҫɾ���û�����?"))
				window.location = "./deleteOrgnet.do?orgId="+OrgId;
			
		}
	</script>
</head>

<body>
<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				��ǰλ�� >> �������� >> ������ҳ
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	
				<table border="0"width="100%">
				<html:form action="/vieworgnet" method="POST">
					<tr height="10">
						<td>					
						</td>
					</tr>
					<TR>
						<TD width="35%" align="right">
							����Id��
						</TD>
						<TD width="25%" align="center">
							<html:text property="orgId" size="28"  styleClass="input-text"/>
						</TD>
						<td width="40%">
							<html:submit value="��ѯ" styleClass="input-button"/>
						</td>
					</TR>
					</html:form>
				</table>
 <table width="100%"  border="0" align="center">		  
		  <tr>
		    <td align="right">		    
		    	<html:button property="addOrgnet" value="���ӻ���" styleClass="input-button" onclick="location.assign('./orgtogether_net/orgnet_add.jsp')"/>
		    </td>
		  </tr>
		  <tr>
		  	<td height="5">
		  	<td>
		  </tr>
   </table>
  <table width="100%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
       <tr class="titletab">
       	   <th height="25" align="center" colspan="15">
       	   	������Ϣ�б�
       	   </th>
        </tr>
        <tr align="center" class="middle">
               <td align="center" width="12%" >
	         ����Id
	          </td>
	          <td align="center">
	         ��������
	          </td>
	          <td align="center" width="5%">
	          	��������
	          </td>
	          <!--<td align="center" width="10%">
	            ��������
	          </td>
	          -->
	          <td align="center" width="5%">
	            ��������
	          </td>
	          <td align="center" width="5%">
	            ��������ID
	          </td>
	          
	          <td align="center" width="5%">
	            ��ͬ����
	          </td>
	          <td align="center" width="6%">
	            Ƶ�����ID
	          </td>
	          <td align="center" width="10%">
	           �ϼ�����ID
	          </td>
	          <td align="center" width="6%">
	           �ӻ���
	          </td>
	          <td align="center" width="8%">
	            ����ID
	          </td>
	           <td align="center" width="6%">
	            ��������
	          </td>
	          <td align="center" width="6%">
	            �������
	          </td>
	          <td align="center" width="5%">
	          �޸�
	          </td>
	          <td align="center" width="5%">
	          ɾ��
	          </td>    
         </tr>
		   <logic:present name="Records" scope="request">
				<logic:iterate id="item" name="Records" >
					<tr>
					    <td bgcolor="#FFFFFF" align="center">
							<bean:write name="item" property="orgId"/>
						</td>
						<td bgcolor="#FFFFFF" align="center">
							<bean:write name="item" property="orgName"/>
						</td>
						<td bgcolor="#FFFFFF" align="center">
							<bean:write name="item" property="orgType"/>
						</td>
						<!--<td bgcolor="#FFFFFF" align="center">
							<bean:write name="item" property="orgCode"/>
						</td>
						-->
						<td bgcolor="#FFFFFF" align="center">
							<bean:write name="item" property="orgCode"/>
						</td>
						<td bgcolor="#FFFFFF" align="center">
							<bean:write name="item" property="orgClsId"/>
						</td>
						
						
						
						<td bgcolor="#FFFFFF" align="center">							
							<logic:equal name="item" property="isCorp" value="0">
							 ��
						    </logic:equal>
						    <logic:notEqual name="item" property="isCorp" value="0">
							 ��
						    </logic:notEqual>
						</td>
						<td bgcolor="#FFFFFF" align="center">
							<bean:write name="item" property="oat_Id"/>
						</td>
						<td bgcolor="#FFFFFF" align="center">
							<bean:write name="item" property="parent_Org_Id"/>
						</td>
						<td bgcolor="#FFFFFF" align="center">						
			    <a href="../viewChildorg.do?orgId=<bean:write name="item" property="orgId"/>
			    &parent_Org_Id=<bean:write name="item" property="parent_Org_Id"/>">�鿴</a>
						</td>
						<td bgcolor="#FFFFFF" align="center">
							<bean:write name="item" property="regionId"/>
						</td>
						<td bgcolor="#FFFFFF" align="center">
							<bean:write name="item" property="deptName"/>
						</td>
						<td bgcolor="#FFFFFF" align="center">
							<bean:write name="item" property="orglayer"/>
						</td>
                <td bgcolor="#ffffff">
			    <a href="../orgtogether_net/orgnet_update.jsp?orgId=<bean:write name="item" property="orgId"/>
			    &orgName=<bean:write name="item" property="orgName"/>
			    &orgCode=<bean:write name="item" property="orgCode"/>
			    &orgClsId=<bean:write name="item" property="orgClsId"/>
			    &orgType=<bean:write name="item" property="orgType"/>">�޸�</a>
			    </td>						
				        <td bgcolor="#ffffff" align="center">
				            <a href="javascript:deleteOrgnet('<bean:write name="item" property="orgId"/>')">ɾ��</a>
				        </td>
			    </tr>
		      </logic:iterate>
         </logic:present>
		 <logic:notPresent name="Records" scope="request">
				<tr align="left">
					<td bgcolor="#ffffff" colspan="15">
						���޻�����Ϣ
					</td>
				</tr>
			</logic:notPresent>
    </table>
   <table width="100%" border="0">
			<TR>
				<TD width="80%">
					<jsp:include page="../../apartpage.jsp" flush="true">
						<jsp:param name="url" value="../vieworgnet.do"/>
					</jsp:include>
				</TD>
			</tr>
	</table>
</body>
</html:html>