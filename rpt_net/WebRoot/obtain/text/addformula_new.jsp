<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html:html locale="true">
  <head>
      
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript" src="../../js/func.js"></script>
    <script language="JavaScript" >
          function _submit()
			{
				
				var textname = document.getElementById('dataSourceEname');
				var organization = document.getElementById('organization');
				var excelname = document.getElementById('childReportId');
				var rowcolumn = document.getElementById('rowColumn');
				var separater = document.getElementById('splitChar');
				var version = document.getElementById('versionId');
				
				var formula=document.getElementById('formula');
				
				
//				 formula=selectf+column;
			//	 document.all.formula.value = selectf.value +"("+ column.value+")" ;
				
				if(formula.value=="")
				{
				alert("��ʽ����Ϊ�գ�");
					formula.focus();
					return false;
				}
				if(textname.value=="")
				{
					alert("text�ļ�������Ϊ�գ�");
					textname.focus();
					return false;
				}
				if(organization.value=="" || isNaN(organization.value))
				{
					alert("��������ȷ�Ļ����У�");
					organization.focus();
					return false;
				}
				if(excelname.value=="")
				{
					alert("excel���ֲ���Ϊ�գ�");
					excelname.focus();
					return false;
				}
				if (version.value=="" || isNaN(version.value))
				{
				alert("��������ȷ�İ汾�ţ�");
					version.focus();
					return false;
				}
				
				
				
				if(rowcolumn.value=="" || isNaN(rowcolumn.value))
				{
					alert("��������ȷ�����кţ�");
					rowcolumn.focus();
					return false;
				}
				if(separater.value=="")
				{
					alert("�ָ�������Ϊ�գ�");
					separater.focus();
					return false;
				}
				
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
<table border="0" width="100%" align="center">
		
		<tr>
			 <td>��ǰλ�� >> 	ETLȡ�� >> �ı�ȡ��</td>
			 
		</tr>
		<tr><td height="10"></td></tr>
	</table>
<table width="100%" height=100% border="0" cellspacing="0" cellpadding="0" align="center">
	
	<tr> 
    	<td align="right" valign="top">
   <html:form method="post" action="/obtain/text/addformula" enctype="multipart/form-data"  onsubmit="return _submit()">
			<table width="90%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
      				<tr id="tbcolor">
            			<th align="center">����ı�ȡ������</th>
      				</tr>
   					<tr>
       					 <td height="204" align="right" bgcolor="#FFFFFF">          
						<table width="100%"  border="0" >
									
									<TR>
										<TD align="right">
											 text�ļ���
										</TD>
										<TD>
											<html:text styleId="dataSourceEname" property="dataSourceEname" size="20" styleClass="input-text" />
										</TD>
										<TD align="right"> 
										
										������
										
										</TD>
										<TD>
										<html:text styleId="orgId" property="orgId" size="20" styleClass="input-text" />
										</TD>
										
										</TR>
										
										
										<TR>
										<TD align="right"> &nbsp;ģ��ID</TD>
										<TD>
											<html:text styleId="childReportId" property="childReportId" size="20" styleClass="input-text" />
										</TD>
										<TD align="right">����</TD>
										<TD>
											<html:text styleId="rowColumn" property="rowColumn" size="20" styleClass="input-text" />
										</TD></TR><tr>
										<td align="right">�汾��</td>
										<td>
										<html:text styleId="versionId" property="versionId" size="20" styleClass="input-text" />
										</td>
										<td align="right"> &nbsp;�ָ���</td>
										<td>
											<html:text styleId="splitChar" property="splitChar" size="20" styleClass="input-text" />
											
										</td></tr>
									
									<tr>
										<td align="right">
											 ��ʽ
										</td>
										<td colspan="3">
											<html:text styleId="formula" property="formula" size="20" style="width:100% " styleClass="input-text" />
										</td>
									</tr>
									<TR>
										<TD align="right">����</TD>
										<TD colspan="3">
											<html:textarea  property="des"   rows="4" style="width:100% "/></TD></TR>
									<tr>
										<td colspan="6" align="right">
											<html:submit value="ȷ��" styleClass="input-button" />
                   		 &nbsp;<html:button property="back" value="����" styleClass="input-button" onclick="history.back()" />
										</td>
									</tr>
								</table>
		  </td>
   </tr>    
 </table>
 </html:form>
</body>

</html:html>
