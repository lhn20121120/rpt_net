<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config"/>
<jsp:useBean id="utilParameterForm" scope="page" class="com.fitech.net.form.UtilParameterForm" />
<html:html locale="true">
	<head>
	<html:base/>
    <title>�������趨</title>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
    <link href="../css/common.css" type="text/css" rel="stylesheet">
  </head>  
  <body>
  	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table border="0" width="90%" align="center">
		<tr><td height="8"></td></tr>
		<tr>
			 <td>��ǰλ�� >> 	�����趨 >> �������޸�</td>
		</tr>
		<tr><td height="10"></td></tr>
	</table>
		<html:form action="/config/UpdateVParam" method="post">
		  <table border="0" width="100%" align="center" cellspacing="1" cellpadding="4" class="tbcolor" class="tbcolor">
			 <TR class="tableHeader" >
				<TD width="15%" align="center" valign="bottom">
				  �������趨
				</TD>
			 </TR>
					 <TR align="center" valign="middle" bgcolor="#FFFFFF">
					   <TD>
						<table border="0" width="100%" align="center" cellspacing="1" cellpadding="4">
						  <TR>
						    <TD align="left">���Ӣ������&nbsp;<html:text property="vpTableid" size="20" styleClass="input-text" maxlength="20"/></TD>
						    <TD align="left">�����������<html:text property="vpTabledesc" size="20" styleClass="input-text" maxlength="20"/></TD>
						    <TD align="left">������ͣ�
						      <html:select styleId="vttId" property="vttId">
						        <html:option value="1">��ʵ��</html:option>
						        <html:option value="2">ά�ȱ�</html:option>
						        <html:option value="3">ָ���</html:option>
						      </html:select>
							</TD>
				          </TR>
				          <TR>
				            <TD align="left">�ֶ�Ӣ������&nbsp;<html:text property="vpColumnid" size="20" styleClass="input-text" maxlength="20"/></TD>
							<TD align="left">�ֶ���������<html:text property="vpColumndesc" size="20" styleClass="input-text" maxlength="20"/></TD>
							<TD align="left">�ֶ����ͣ�
							  <html:select styleId="vpColtype" property="vpColtype">
						        <html:option value="0">����</html:option>
						        <html:option value="1">�ַ���</html:option>
						      </html:select>
							</TD>
				          </TR>
				          <TR>
					        <TD align="left" colspan="2">&nbsp;&nbsp;�ϼ��ֶΣ�
						      <html:select styleId="pre_vpId" property="pre_vpId">
						        <html:option value="-1">&nbsp;</html:option>
						        <html:optionsCollection name="utilParameterForm" property="params"/>
						      </html:select>
					        </TD>
					        <TD align="left">&nbsp;&nbsp;&nbsp;&nbsp;˵����&nbsp;<html:text property="vpNote" size="20" styleClass="input-text" maxlength="20"/></TD>
					      </TR>
				          <TR>
				            <TD align="right" colspan="3">
				              <html:submit styleClass="input-button" value="ȷ��"/>
				              <html:hidden property="vpId"/>
				                <logic:present name="<%=configBean.CUR_PAGE_OBJECT%>" scope="request">
				                  <input type="hidden" name="curPage" value="<bean:write name='<%=configBean.CUR_PAGE_OBJECT%>'/>">
				                </logic:present>
						     </TD>
					       </TR>
						</table>
						</TD>
					</TR>
			 </table>
		</html:form>		
  	</body>
</html:html>

