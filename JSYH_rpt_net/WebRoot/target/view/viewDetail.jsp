<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<%
	String year = request.getAttribute("year") != null ? request.getAttribute("year").toString() : null;
	String term = request.getAttribute("term") != null ? request.getAttribute("term").toString() : null;
	String businessId = request.getAttribute("businessId") != null ? request.getAttribute("businessId").toString() : null;
	String targetDefineName = request.getAttribute("targetDefineName") != null ? request.getAttribute("targetDefineName").toString() : null;
	String qry = "";
	if(year != null && !year.equals("")) qry += ((qry.equals("")?"":"&")+"year="+year);
	if(term != null && !term.equals("")) qry += ((qry.equals("")?"":"&")+"month="+term);
	if(businessId != null && !businessId.equals("")) qry += ((qry.equals("")?"":"&")+"businessId="+businessId);
	if(targetDefineName != null && !targetDefineName.equals("")) qry += ((qry.equals("")?"":"&")+"targetDefineName="+targetDefineName);
%>
<head>
	<html:base/>
	<title> ָ�궨��</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../"/>
	</jsp:include>
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript">
		function _back(){
			window.location="<%=request.getContextPath()%>/target/viewTargetGenerate.do?<%=qry%>";
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
	<table>
		<tr>
			<td height="10"></td>
		</tr>
	</table>
	<TABLE name="tbl" id="tbl" cellSpacing="1" cellPadding="1" width="96%" border="0" class="tbcolor" align="center">
		<tr id="tbcolor">
			<th align="center">
				<strong>
					<font face="Arial" size="2">ָ����Ϣ</font>
				</strong>
			</th>
		</tr>
		<tr>
			<td height="204" align="right" bgcolor="#FFFFFF">   
				<html:form action="/target/insertTargetDefine" method="Post" styleId="form1" >
					<table width="100%"  border="0" >
						<TR>
							<TD align="right">
								ָ������
							</TD>
							<TD>
								<html:text styleId="defineName" property="defineName" size="20" styleClass="input-text" readonly="true" />
							</TD>
							<TD align="right">
								ָ��汾
							</TD>
							<TD>
								<html:text styleId="version" property="version" size="20" styleClass="input-text" readonly="true" />
							</TD>
						</TR>
						<TR>
							<TD align="right"> &nbsp;ָ��ҵ������</TD>
							<TD>
								<html:text styleId="businessName" property="businessName" size="20" styleClass="input-text" readonly="true" />
							</TD>
							<TD align="right">��ʼʱ��</TD>
							<TD>
								<html:text property="startDate" size="15" styleClass="input-text" style="text" readonly="true" />
							</TD>
						</TR>
						<tr>
							<td align="right">ָ������</td>
							<td>
								<html:text styleId="normalName" property="normalName" size="20" styleClass="input-text"  readonly="true"/>
							</td>
							<td align="right"> &nbsp;����ʱ��</td>
							<td>
								<html:text property="endDate" size="15" styleClass="input-text" style="text" readonly="true" readonly="true" />
							</td>
						</tr>
						<tr>
							<td align="right">��ʽ����</td>
							<td colspan="3">
								<html:text  property="formula" size="70" styleClass="input-text"  readonly="true" />
							</td>
						</tr>
						<tr>
							<td align="right">
								 ���ɷ���
							</td>
							<td colspan="3">
								<html:textarea  property="law"   rows="3" style="width:100% "  readonly="true" />
							</td>
						</tr>
						<TR>
							<TD align="right">ָ������</TD>
							<TD colspan="3">
								<html:textarea  property="des"   rows="4" style="width:100% "  readonly="true" />
							</TD>
						</TR>
						
					</table>
				</html:form>
			</td>
		</tr>    
	</table>
	<br/>
	<TABLE name="tbl" id="tbl" cellSpacing="1" cellPadding="1" width="96%" border="0" class="tbcolor" align="center">
		<tr id="tbcolor">
			<th align="center">
				<strong>
					<font face="Arial" size="2">Ԥ����Ϣ</font>
				</strong>
			</th>
		</tr>
	</table>
	<TABLE name="tbl" id="tbl" cellSpacing="1" cellPadding="1" width="96%" border="0" class="tbcolor" align="center" >
		<TR>
			<td width="6%" align="center"  class="tableHeader">
				<b>���</b>
			</td>
			<td width="20%" align="center"  class="tableHeader">
				<b>����</b>
			</td>
			<td width="32%" align="center"  class="tableHeader">
				<b>����</b>
			</td>
			<td width="32%" align="center"  class="tableHeader">
				<b>��ɫ</b>
			</td>
			
		</TR>
		
		<logic:present name="Warn" scope="request">	
			<logic:iterate id="item" name="Warn" indexId="index">
				<TR bgcolor="#FFFFFF" height="25">
					<TD width="2%" align="center">
						<%=((Integer)index).intValue() + 1%>
					</TD>
					<TD width="14%" align="center">
					    <logic:present name="item"  property="upLimit">
							<bean:write name="item" property="upLimit"/>
						</logic:present>
						
						
					</TD>
					<TD width="14%" align="center">
						<logic:present name="item"  property="downLimit">
							<bean:write name="item" property="downLimit"/>
						</logic:present>
						
					</TD>
					<TD width="14%" align="center">
						<logic:present name="item" property="color">				
							<label style='background:<bean:write name="item" property="color"/>'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
						</logic:present>		
					</TD>
					
				</TR>
			</logic:iterate>
		</logic:present>
	</TABLE>
	<table width="96%">
		<tr>
   			<td> 
				<strong>
					<font face="Arial" size="2">
						��������Ϣ  
					</font>
				</strong>
   
			</td>
		</tr>
	</table>
    <TABLE name="tbl" id="tbl" cellSpacing="1" cellPadding="1" width="96%" border="0" class="tbcolor" align="center" >
		<TR>
			<td width="6%" align="center"  class="tableHeader">
				<b>
					���
				</b>
			</td>
			<td width="20%" align="center"  class="tableHeader">
				<b>
					����
				</b>
			</td>
			<td width="32%" align="center"  class="tableHeader">
				<b>
				����
				</b>
			</td>
			<td width="32%" align="center"  class="tableHeader">
				<b>
					��ɫ
				</b>
			</td>
			
		</TR>
		
		<logic:present name="PreStandard" scope="request">	
			<logic:iterate id="item" name="PreStandard" indexId="index">
				<TR bgcolor="#FFFFFF" height="25">
					<TD width="2%" align="center">
						<%=((Integer)index).intValue() + 1%>
					</TD>
					<TD width="14%" align="center">
					    <logic:present name="item"  property="upLimit">
							<bean:write name="item" property="upLimit"/>
						</logic:present>
						
						
					</TD>
					<TD width="14%" align="center">
						<logic:present name="item"  property="downLimit">
							<bean:write name="item" property="downLimit"/>
						</logic:present>
						
					</TD>
					<TD width="14%" align="center">
						<logic:present name="item" property="color">
						
							<label style='background:<bean:write name="item" property="color"/>'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
						</logic:present>
						
					</TD>
					
				</TR>
			</logic:iterate>
		</logic:present>
	</TABLE>
	<table width="96%">
		<tr>
			<td colspan="6" align="right">
				<html:button property="back" value="��  ��" styleClass="input-button" onclick="window.history.back()" />
			</td>
		</tr>
    </table>
    <br>
</body>
</html:html>