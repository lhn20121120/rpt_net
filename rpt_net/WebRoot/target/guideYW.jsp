<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config"/>
<html:html locale="true">
	<head>
	<html:base/>
    <title>ָ��ҵ�� </title>    
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
    <link href="../css/common.css" type="text/css" rel="stylesheet">
    
    <script language="javascript">
	    function goAddPage()
	    {
	    	window.location.href="guideYWAdd.jsp";
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
	     * @param curId ����ID
	     */
	     function _edit(normalId){
	     	window.location="editTargetNormal.do?normalId=" + normalId + "&curPage=" + curPage;
	     }
	     //ɾ����ʾ
	     function _delete(normalId)
	     {
	     	if(confirm("��ȷ��Ҫɾ����ָ��ҵ����"))
	     		window.location = "deleteTagetNormal.do?normalId="+normalId;	
	     		
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
			 <td>��ǰλ�� >> 	ָ����� >> ָ��ҵ��</td>
		</tr>
		<tr><td height="10"></td></tr>
	</table>
	<html:form method="post" action="/target/viewTargetNormal.do">
	<TABLE border="0" width="80%" align="center" cellpadding="0" cellspacing="0">
		<TR>
			<TD align="left" valign="middle">�ؼ��֣�</TD>
			<TD align="left" valign="middle">
				<html:text property="normalName" size="40" styleClass="input-text" />
			</TD>
			<TD align="center" valign="middle">
				<input type="submit" class="input-button" value="��ѯ">&nbsp;
				<input type="button" class="input-button" value="����" onclick="goAddPage()"/>
			</TD>
		</TR>
		
		<tr><td colspan="3" height="10"></td></tr>
	</TABLE>
		<TABLE border="0" width="80%" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
			<TR class="tableHeader">
				<TD width="15%" align="center" valign="middle">
					���
				</TD>
				<TD width="60%" align="center" valign="middle">
					ָ��ҵ������
				</TD>
				<TD width="25%" align="center" valign="middle">
					����
				</TD>
			</TR>
			<logic:present name="Records" scope="request">
				<logic:iterate id="targetNormal" name="Records"  indexId="index">				
					<TR bgcolor="#FFFFFF">						
		
						<TD align="center" valign="bottom">
							<%=((Integer)index).intValue() + 1%>
						</TD>
						
						<TD align="center" valign="bottom">
							
								<bean:write name="targetNormal" property="normalName"/>
							
						</TD>						
						<TD align="center" valign="bottom">
							
								<a href="javascript:_edit(<bean:write name='targetNormal' property='normalId'/>)">�޸�</a>&nbsp;&nbsp;
<%--								<a href="javascript:_delete(<bean:write name="targetNormal" property="normalId"/>)"> ɾ��</a>--%>
							
						</TD>
					</TR>		
				</logic:iterate>			
		</logic:present>		
		<logic:notPresent name="Records" scope="request">
			<tr align="center">
				<td bgcolor="#ffffff" colspan="9" align="left">
					���޷��������ļ�¼
				</td>
			</tr>
		</logic:notPresent>
      </html:form>
    </TABLE>
	<TABLE align="center" border="0" width="80%">
		<TR>
			<TD>
				<jsp:include page="../apartpage.jsp" flush="true">
					<jsp:param name="url" value="viewTargetNormal.do"/>
				</jsp:include>
			</TD>
		</TR>
	</TABLE>
  </body>
</html:html>