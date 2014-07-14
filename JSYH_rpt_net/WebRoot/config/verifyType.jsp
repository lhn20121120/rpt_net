<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config"/>
<html:html locale="true">
	<head>
	<html:base/>
    <title>У������趨</title>    
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
    <link href="../css/common.css" type="text/css" rel="stylesheet">
    
    <script language="javascript">
	    function goAddPage()
	    {
	    	window.location.href="verifyTypeAdd.jsp";
	    }
	    
	    <logic:present name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="<bean:write name='ApartPage' property='curPage'/>";
	    </logic:present>
	    <logic:notPresent name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="1";
	    </logic:notPresent>
	    
	    /**
	     * �޸��¼�
	     *
	     * @param validateTypeId У������ID
	     */
	     function _edit(validateTypeId){
	     	window.location="EditCurVerifyType.do?validateTypeId=" + validateTypeId + "&curPage=" + curPage;
	     }
	     
	     //ɾ����ʾ
	     function _delete(validateTypeId)
	     {
	     	if(confirm("��ȷ��Ҫɾ����У�������"))
	     		window.location = "DeleteCurVerifyType.do?validateTypeId="+validateTypeId;	
	     		
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
    <table border="0" width="90%" align="center">
		<tr><td height="8"></td></tr>
		<tr>
			 <td>��ǰλ�� >> 	�����趨 >> У������趨</td>
		</tr>
		<tr><td height="10"></td></tr>
	</table>
	<html:form method="post" action="/config/ViewCurVerifyType">
	<TABLE border="0" width="80%" align="center" cellpadding="0" cellspacing="0">
		<TR>
			<TD align="left" valign="middle">�ؼ��֣�</TD>
			<TD align="left" valign="middle">
				<html:text property="validateTypeName" size="40" styleClass="input-text" />
			</TD>
			<TD align="center" valign="middle">
				<input type="submit" class="input-button" value="��    ѯ">&nbsp;
<%--				<input type="button" class="input-button" value="����" onclick="goAddPage()"/>--%>
			</TD>
		</TR>
		<tr><td colspan="3" height="10"></td></tr>
	</TABLE>
  </html:form>
		<TABLE border="0" width="80%" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
			<TR class="tableHeader">
				<TD width="15%" align="center" valign="middle">
					���
				</TD>
				<TD width="60%" align="center" valign="middle">
					У���������
				</TD>
<%--				<TD width="25%" align="center" valign="middle">--%>
<%--					����--%>
<%--				</TD>--%>
			</TR>
			<logic:present name="Records" scope="request">
				<logic:iterate id="viewValidateType" name="Records" indexId="index">
					<TR bgcolor="#FFFFFF">
						<TD align="center" valign="bottom">
							<%=((Integer)index).intValue() + 1%>
						</TD>
						<TD align="center" valign="bottom">
							
							<bean:write name="viewValidateType" property="validateTypeName"/>
							
						</TD>
<%--						<TD align="center" valign="bottom">--%>
<%--							--%>
<%--								<a href="javascript:_edit(<bean:write name='viewValidateType' property='validateTypeId'/>)">�޸�</a>&nbsp;&nbsp;--%>
<%--								<a href="javascript:_delete(<bean:write name="viewValidateType" property="validateTypeId"/>)">--%>
<%--								 ɾ��--%>
<%--								</a>--%>
<%--							--%>
<%--						</TD>--%>
					</TR>
			</logic:iterate>
		</logic:present>
		
		<logic:notPresent name="Records" scope="request">
			<tr align="left">
				<td bgcolor="#ffffff" colspan="5">
						��ƥ���¼
				</td>
			</tr>
		</logic:notPresent>
      
    </TABLE>
	<TABLE align="center" border="0" width="80%">
		<TR>
			<TD>
				<jsp:include page="../apartpage.jsp" flush="true">
					<jsp:param name="url" value="ViewCurVerifyType.do"/>
				</jsp:include>
			</TD>
		</TR>
	</TABLE>
  </body>
</html:html>