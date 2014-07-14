<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<html:html locale="true">
  <head>
     <html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" type="text/css" rel="stylesheet">
		
		
		
		<script language="JavaScript" type="text/JavaScript">
		function _delete(id)
		{
		if(confirm("��ȷ��Ҫɾ����?"))
		window.location="../text/deleteformula.do?id="+id;
		}
		function _edit(id)
		{
		window.location="../text/editformula.do?id="+id;
		}
		function _add()
		{
		window.location="./addformula_new.jsp";
		}
		function _get()
		{
		window.location="../text/viewformula.do";
		}
		</SCRIPT>
  </head>
  <body>
  <logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
 <html:form method="post" action="/obtain/text/viewformula2" >
 <fieldset id="fieldset" align="center" valign="center">
  <table width="95%" height="40"  border="0" cellpadding="0" cellspacing="0">
  <tr>
				<td width="25%" height="54">
					 ģ��ID
					<input type="text" name="childReportId" >
				</td>
				<td width="24%">
					 �汾��
					<input type="text" name="versionId" >
					<logic:present name="ApartPage" scope="request">
 
  </logic:present>
				</td>
				<td >
					<input class="input-button" type="submit" name="Submit" value="��ѯ">
				</td>
				<TD>
					<INPUT class="input-button" type="Button" name="����" value="����" onclick="return _add()"/>
					
				</TD>
				<TD>
					<INPUT class="input-button" type="Button" name="ȡ��" value="ȡ��" onclick="return _get()"/>
					
				</TD>
			</tr>
</table>
</fieldset>
</html:form>
 <TABLE cellSpacing="1" cellPadding="4" width="100%" border="0"  class="tbcolor">
       <tr class="titletab">
									<th colspan="10" align="center" id="list">
										<strong>
											�ı���ȡ�����б�
										</strong>
									</th>
								</tr>
        <tr align="center" class="middle">
	          <td class="tableHeader" align="center" width="15%" >ģ��ID</td>
	          <td class="tableHeader" align="center" width="4%">�汾��</td>
	          <td class="tableHeader" align="center" width="12%">text��</td>
	          <td class="tableHeader" align="center" width="15%">��ʽ</td>
	          <td class="tableHeader" align="center" width="5%">���к�</td>
	          <td class="tableHeader" align="center" width="5%">������</td>
	          <td class="tableHeader" align="center" width="5%">�ָ���</td>
	          <td class="tableHeader" align="center" width="6%">״̬��Ϣ</td>
	          <td class="tableHeader" align="center" width="4%">ɾ��</td>
	          <td class="tableHeader" align="center" width="4%">�޸�</td>
       </tr>
		  <logic:present name="formulas" scope="request">
											<logic:iterate id="item" name="formulas">
												<TR bgcolor="#FFFFFF">
													<TD align="center">
														<logic:notEmpty name="item" property="childReportId">
															
															<bean:write name="item" property="childReportId"/>
														
														</logic:notEmpty>
														<logic:empty name="item" property="childReportId">
															 ��
														</logic:empty>
													</TD>
													<td align="center"><bean:write name="item" property="versionId"/></td>
													<td align="center"><bean:write name="item" property="dataSourceEname"/></td>
													<TD align="center">
														<logic:notEmpty name="item" property="formula">
															<bean:write name="item" property="formula"/>
														</logic:notEmpty>
														<logic:empty name="item" property="formula">
															 ��
														</logic:empty>
													</TD>
													<td align="center"><bean:write name="item" property="rowColumn"/></td>
													<td align="center"><bean:write name="item" property="orgId"/></td>
													<td align="center"><bean:write name="item" property="splitChar"/></td>
													<td align="center"><bean:write name="item" property="flag"/></td>
												<td  align="center" ><a href="javascript:_delete('<bean:write name="item" property="id"/>')">ɾ��</a></td>
	                                            <td  align="center" ><a href="javascript:_edit('<bean:write name="item" property="id"/>')">�޸�</a></td>
												</TR>
											</logic:iterate>
									</logic:present>
									<logic:notPresent name="formulas" scope="request">
										<tr bgcolor="#FFFFFF">
											<td colspan="10">
												 ���޼�¼
											</td>
										</tr>
									</logic:notPresent>
									
</table>
                                  <table cellSpacing="1" cellPadding="4" width="100%" border="0">
										<tr>
											<TD colspan="7" bgcolor="#FFFFFF">
											
											 <%
												    String R=(String)request.getAttribute("term");
												    // System.out.println(R);
												    if(R!=null)
												    {
												    %>
												    <jsp:include page="../../apartpage.jsp" flush="true">
													<jsp:param name="url" value='<%=R%>' />
												     </jsp:include>	
												    
												    <%
												   
												   }
												  else
												  {
												  %>
												  <jsp:include page="../../apartpage.jsp" flush="true">
													<jsp:param name="url" value="../../obtain/text/viewformula2.do" />
												     </jsp:include>	
												  <%
												  }
													
												 %>
												    
												
											
											
											
											</TD>
										</tr>
									</table>
  </body>
</html:html>
