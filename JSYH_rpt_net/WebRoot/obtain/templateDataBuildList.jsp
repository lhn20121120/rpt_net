<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
        <head>	
	    <title>ģ���б�</title>
	    <meta http-equiv="Content-Type" content="text/html; charset=gbk">
	    <meta http-equiv="Pragma" content="no-cache">
	    <meta http-equiv="Cache-Control" content="no-cache">
	    <meta http-equiv="Expires" content="0">
	    <LINK REL="StyleSheet" HREF="<bean:write name="css"/>" TYPE="text/css">
	</head>
	<body>	
    <table width="95%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
    	<tr class="titletab">
        	<td>
      			<table width="100%" border="0" align="center" cellpadding="4" cellspacing="1">	
      		    	<html:form action="/templateDataBuildList" method="Post" styleId="form1">			      
      		       		<tr>
      		          		<td align="right">ģ�����ƣ�<html:text property="reportName" styleClass="input-text" size="28"/></td>
      		          		<td>�汾�ţ�<html:text property="versionId" styleClass="input-text" size="10"/></td>
      		          		<td><html:submit styleClass="input-button" value=" �� ѯ "/></td>
      		       		</tr>
      		       </html:form>
      		    </table>
            </td>
        </tr>
              <tr>
      	         <td bgcolor="#ffffff">
      		    <table width="100%" border="0" align="center" cellpadding="4" cellspacing="1">				      
      		       <tr class="titletab">
				<th colspan="5" align="center" id="list">
					<strong>
						����ģ���б�
					</strong>
				</th>
			</tr>
			<TR class="middle">
				<TD class="tableHeader" width="5%">
					<b>���</b>
				</TD>
				<TD class="tableHeader" width="30%">
					<b>����ģ������</b>
				</TD>
				<TD class="tableHeader" width="10%">
					<b>�汾��</b>
				</TD>
				<TD class="tableHeader" width="20%">
					<b>ȡ��ģʽ</b>
				</TD>
				<TD class="tableHeader" width="15%">
					<b>����</b>
				</TD>									
			</TR>
			<logic:iterate id="omList" indexId="ind" name="omList">
			<tr bgcolor="#FFFFFF">
			   <td height="5" align="center">
			      <bean:write name="omList" property="repID"/>
			   </td>	
			   <td height="5" align="left">
			         &nbsp;
			      <bean:write name="omList" property="excelName"/>
			   </td>
			   <td height="5" align="center">
			      <bean:write name="omList" property="versionID"/>
			   </td>	
			   <td height="5" align="center">
			      <bean:write name="omList" property="mode"/>
			   </td>	
			   <td height="5" align="center">
			      <a href="templateDataBuildPre.do?repID=<bean:write name="omList" property="repID"/>&versisonID=<bean:write name="omList" property="versionID"/>">
			         ��������
			      </a>

			   </td>
			</tr>
			</logic:iterate>	
      		    </table>
      		 </td>
      	      </tr>    
           </table>
	</body>    
</html:html>