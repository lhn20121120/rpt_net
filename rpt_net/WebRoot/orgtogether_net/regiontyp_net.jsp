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
		function deleteRegionTyp(regionTypNo)
		{
			if(confirm("��ȷ��Ҫɾ���õ���������Ϣ��?"))
				window.location = "./deleteRegionTypNet.do?regionTypId="+regionTypNo;
			
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
 	<table width="90%"  border="0" align="center">
		  <tr >
		  	<td height="20">
		  	<td>
		  </tr>
		  <tr>
		    <td align="right">
		    	<html:button property="aa" value="���ӵ�������" styleClass="input-button" onclick="location.assign('./orgtogether_net/regiontypnet_add.jsp')"/>
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
       	   	���������б�
       	   </th>
        </tr>
        <tr align="center" class="middle">
              <td align="center" width="5%" >
	          	���
	          </td>    
	          <td align="center" width="15%" >
	          	��������Id
	          </td>
	          <td align="center">
	          	������������
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
				                <logic:notEmpty name="item" property="regionTypId">
				                	<bean:write name="item" property="regionTypId"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="regionTypId">
				                	δ֪
				                </logic:empty>
			                </td>
			                
			               
			                
			                <td bgcolor="#ffffff">
				                <logic:notEmpty name="item" property="regionTypNm">
				                	<bean:write name="item" property="regionTypNm"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="regionTypNm">
				                	δ֪
				                </logic:empty>
			                </td>
			    <td bgcolor="#ffffff">
			    <a href="../orgtogether_net/regiontypnet_update.jsp?regionTypNm=<bean:write name="item" property="regionTypNm"/>&regionTypId=<bean:write name="item" property="regionTypId"/>">�޸�</a>
			                </td>
			                <td bgcolor="#ffffff">
			                	<a href="javascript:deleteRegionTyp('<bean:write name="item" property="regionTypId"/>')">ɾ��</a>
			                </td>              
			         </tr>
				</logic:iterate>
		  </logic:present>
		  <logic:notPresent name="Records" scope="request">
				<tr align="left">
					<td bgcolor="#ffffff" colspan="6">
						���޵�����Ϣ
					</td>
				</tr>
			</logic:notPresent>
    </table>
    <table width="90%" border="0">
			<TR>
				<TD width="80%">
					<jsp:include page="../../apartpage.jsp" flush="true">
						<jsp:param name="url" value="../regionTypNet.do"/>
					</jsp:include>
				</TD>
			</tr>
	</table>
</body>
</html:html>
