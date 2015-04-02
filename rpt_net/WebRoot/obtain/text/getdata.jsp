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
		function _calculate(id)
		{
		window.location="../text/getdataformula.do?id="+id;
		}
		function _getAll()
		{
		window.location="../text/getalldataformula.do";
		
		}
		function _edit()
		{
		
		window.location="../text/viewformula2.do";
		
		}
		function  _removeAll()
		{
			window.location="../text/clearexcel.do";
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
 <html:form method="post" action="/obtain/text/viewformula" >
  <fieldset id="fieldset" align="center" valign="center">
  <table width="95%" height="54"  border="0" cellpadding="0" cellspacing="0" height="54">
  <tr>
				<td width="25%" height="54">
					ģ��ID
					<input type="text" name="childReportId" size="17">

				</td>
				<td width="24%">
					�汾��
					<input type="text" name="versionId" size="17">



				</td>
				<td>
					<input class="input-button" type="submit" name="Submit" value="��ѯ">
				</td>
				<td>
					<input class="input-button" type="button" name="s" value="ȡ��" onclick="return _getAll()">
				</td>
				<TD> &nbsp;<INPUT class="input-button" type="button" value="�����ʷ����" name="�����ʷ����" onclick="return _removeAll()"></TD>
				<td>
					&nbsp;<input class="input-button" type="button" name="s" value="�༭ҳ��" onclick="return _edit()">
				</td>
			</tr>
</table>
</fieldset>
 </html:form>
 <TABLE cellSpacing="1" cellPadding="4" width="100%" border="0"  class="tbcolor">
                                <tr class="titletab">
									<th colspan="9" align="center" id="list">
										<strong>
											��ȡ����б�
										</strong>
									</th>
								</tr>
        <tr align="center" class="middle">
	          <td class="tableHeader" align="center" width="15%" >ģ��ID</td>
	          <td class="tableHeader" align="center" width="5%">�汾��</td>
	          <td class="tableHeader" align="center" width="15%">text��</td>
	          <td class="tableHeader" align="center" width="15%">��ʽ</td>
	          <td class="tableHeader" align="center" width="5%">���к�</td>
	          <td class="tableHeader" align="center" width="5%">������</td>
	          <td class="tableHeader" align="center" width="5%">�ָ���</td>
	          <td class="tableHeader" align="center" width="4%">״̬��Ϣ</td>
	          <td class="tableHeader" align="center" width="4%">ȡ��</td>
	          
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
													<td align="center"><a href="javascript:_calculate('<bean:write name="item" property="id"/>')">ȡ��</a></td>
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
													<jsp:param name="url" value="../../obtain/text/viewformula.do" />
												     </jsp:include>	
												  <%
												  }
													
												 %>
												    
											</TD>
										</tr>
									</table>
									
  </body>
</html:html>
