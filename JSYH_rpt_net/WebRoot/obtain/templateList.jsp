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
                       		      <form name="form1" method="post" action="xxxx.do">
                 
      		    <table width="100%" border="0" align="center" cellpadding="4" cellspacing="1">				      
      		       <tr>
      		          <td align="right">
      		             ģ�����ƣ�<input class="input-text" type="text" size="28" name="reportName">
      		          </td>
      		          <td>
      		             �汾�ţ�<INPUT class="input-text" id="Text2" type="text" size="10" name="versionId">
      		          </td>
      		          <td>
      		             <INPUT class="input-button" id="Button3" type="submit" value=" �� ѯ " name="Button1">
      		          </td>
      		          
      		       </tr>
      		     
      		    </table>
      		     </form>
                 </td>
              </tr>
              <tr>
      	         <td bgcolor="#ffffff">
      		    <table width="100%" border="0" align="center" cellpadding="4" cellspacing="1">				      
      		       <tr class="titletab">
				<th colspan="7" align="center" id="list">
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
					<b>��������</b>
				</TD>
				<TD class="tableHeader" width="10%">
					<b>���ҵ�λ</b>
				</TD>
				<TD class="tableHeader" width="10%">
					<b>�Ƿ񷢲�</b>
				</TD>
				<TD class="tableHeader" width="15%">
					<b>����</b>
				</TD>									
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD align="center">G1302</TD>
				<TD align="center">					
					<a href="/SMIS_IN_CA/selfReadExcelServlet?childRepId=G1302&versionId=0512" >
						G13���ʮ�Ҵμ�����������
					</a>
				</TD>
				<TD align="center">0512</TD>
				<TD align="center">��������-���÷���</TD>
				<TD align="center">��Ԫ</TD>
				<TD align="center">δ����</TD>
				<td align="center">
				   <a href="templateConfigurePre.do">��������Դ</a>
				   <a href="templateDataBuild.do">��������</a>
				</td>
			 </tr>	
			<TR bgcolor="#FFFFFF">
				<TD align="center">G1302</TD>
				<TD align="center">					
					<a href="/SMIS_IN_CA/selfReadExcelServlet?childRepId=G1302&versionId=0512" >
						G13���ʮ�Ҵμ�����������
					</a>
				</TD>
				<TD align="center">0512</TD>
				<TD align="center">��������-���÷���</TD>
				<TD align="center">��Ԫ</TD>
				<TD align="center">δ����</TD>
				<td align="center">
				   <a href="templateConfigurePre.do">��������Դ</a>
				</td>
			 </tr>	
			<TR bgcolor="#FFFFFF">
				<TD align="center">G1302</TD>
				<TD align="center">					
					<a href="/SMIS_IN_CA/selfReadExcelServlet?childRepId=G1302&versionId=0512" >
						G13���ʮ�Ҵμ�����������
					</a>
				</TD>
				<TD align="center">0512</TD>
				<TD align="center">��������-���÷���</TD>
				<TD align="center">��Ԫ</TD>
				<TD align="center">δ����</TD>
				<td align="center">
				   <a href="templateConfigurePre.do">��������Դ</a>
				</td>
			 </tr>	
			<TR bgcolor="#FFFFFF">
				<TD align="center">G1302</TD>
				<TD align="center">					
					<a href="/SMIS_IN_CA/servlets/selfReadExcelServlet?childRepId=G1302&versionId=0512" >
						G13���ʮ�Ҵμ�����������
					</a>
				</TD>
				<TD align="center">0512</TD>
				<TD align="center">��������-���÷���</TD>
				<TD align="center">��Ԫ</TD>
				<TD align="center">δ����</TD>
				<td align="center">
				   <a href="templateConfigurePre.do">��������Դ</a>
				</td>
			 </tr>	
			<TR bgcolor="#FFFFFF">
				<TD align="center">G1302</TD>
				<TD align="center">					
					<a href="/SMIS_IN_CA/servlets/selfReadExcelServlet?childRepId=G1302&versionId=0512" >
						G13���ʮ�Ҵμ�����������
					</a>
				</TD>
				<TD align="center">0512</TD>
				<TD align="center">��������-���÷���</TD>
				<TD align="center">��Ԫ</TD>
				<TD align="center">δ����</TD>
				<td align="center">
				   <a href="templateConfigurePre.do">��������Դ</a>
				</td>
			 </tr>	
      		    </table>
      		 </td>
      	      </tr>    
           </table>
	</body>    
</html:html>