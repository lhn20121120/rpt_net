<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
	<title>�û�������Ϣ</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">	
</head>

<body >
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
		
 	<table width="95%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
 		<tr class="titletab">
			<th height="25" align="center" colspan="7">
       	   		�û�������Ϣ
			</th>
        </tr>
        <tr align="center" class="middle">
			<td align="center" width="10%" >���</td>
			<td align="center" width="15%">�û���</td>
			<td align="center" width="15%">����</td>
			<td align="center" width="15%">ϵͳ��������</td>
			<td align="center" width="15%">�����쵼</td>
			<td align="center" width="15%">�绰����</td>
			<td align="center" width="15%">�����ʼ�</td>
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
						<logic:notEmpty name="item" property="title">
				           	<bean:write name="item" property="title"/>
						</logic:notEmpty>
					</td>
					<td bgcolor="#ffffff">
						<logic:notEmpty name="item" property="manager">
				        	<bean:write name="item" property="manager"/>
						</logic:notEmpty>
					</td>
					<td bgcolor="#ffffff">
						<logic:notEmpty name="item" property="telephoneNumber">
							<bean:write name="item" property="telephoneNumber"/>
						</logic:notEmpty>
					</td>
					<td bgcolor="#ffffff">
						<logic:notEmpty name="item" property="mail">
							<bean:write name="item" property="mail"/>
						</logic:notEmpty>
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
</body>
</html:html>
