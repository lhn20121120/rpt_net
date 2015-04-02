<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="utilFormOrg" scope="page" class="com.cbrc.org.form.UtilForm" />
<%
	String orgSrhName="",orgClsId="",curPage="",where="";
	
	if(request.getAttribute("OrgSrhName")!=null){
		orgSrhName=(String)request.getAttribute("OrgSrhName");
		where+=(where.equals("")?"":"&") + "orgSrhName=" + orgSrhName;
	}
	if(request.getAttribute("OrgClsId")!=null){
	 	orgClsId=(String)request.getAttribute("OrgClsId");
	 	where+=(where.equals("")?"":"&") + "orgClsId=" + orgClsId;
	}
	if(request.getAttribute("CurPage")!=null){
		curPage=(String)request.getAttribute("CurPage");
		where+=(where.equals("")?"":"&") + "curPage=" + curPage;
	}
	
	String setWhere="",OATSetWhere="";
	if(!where.equals("")){
		setWhere="&" + where;
		OATSetWhere="?" + where;
	}
%>
<html:html locale="true">
<head>
	<html:base/>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript">
		/**
		 * ���û���������Ƶ�����
		 *
		 * @param orgName String ��������
		 * @param orgCls String ��������
		 * @param orgId String ��������
		 * @return void
		 */
		 function _set(orgName,orgCls,orgId){
		 	window.location="orgOATSet.jsp?orgName=" + orgName + 
		 		"&orgClsName=" + orgCls + 
		 		"&orgId=" + orgId + "<%=setWhere%>";
		 }
		 
		/**
		 * ������������û���������Ƶ�����
		 */ 
		 function _OATSet(){
		 	window.location="orgClsOATSet.jsp" + "<%=OATSetWhere%>";
		 }
	</script>
</head>
<body bgcolor="#FFFFFF">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table border="0" cellpadding="0" cellspacing="0" width="96%">
		<tr><td height="5"></td></tr>
		<tr>
			<td>
				��ǰλ�� >> ģ����� >> ��������Ƶ������趨
			</td>
		</tr>
	</table>
	<html:form method="post" action="/template/org/SearchOrg">
	<TABLE cellSpacing="0" cellPadding="0" width="96%" border="0" align="center">
		<TR class="middle">
			<td>
				<fieldset id="fieldset">
				<LEGEND>��ѯ����</LEGEND>
				<TABLE cellSpacing="1" cellPadding="4" width="96%" border="0" align="center">
					<tr>
						<td>�������ƣ�<html:text property="orgSrhName" size="40" styleClass="input-text"/></td>
						<td>�������ͣ�
							<html:select property="orgClsId">
								<option value=""></option>
								<html:optionsCollection name="utilFormOrg" property="orgCls"/>
							</html:select>
						</td>
						<td align="center"><input type="submit" value=" ��ѯ " class="input-button"></td>
					</tr>
				</table>
				</FIELDSET>
			</td>
		</TR>
	</table>
	</html:form>
	<TABLE cellSpacing="1" cellPadding="4" width="96%" border="0" align="center">
		<tr>
			<td align="right"><input type="button" value="�������������" class="input-button" onclick="_OATSet()"></td>
		</tr>
	</table>
	<TABLE cellSpacing="1" cellPadding="4" width="96%" border="0" class="tbcolor" align="center">
		<TR class="middle">
			<TD class="tableHeader" align="center" width="40%">
				��������
			</td>
			<TD class="tableHeader" align="center" width="30%">
				����
			</td>
			<TD class="tableHeader" align="center" width="15%">
				Ƶ�����
			</td>
			<TD class="tableHeader" align="center" width="15%">
				����
			</td>
		</TR>
		<logic:present name="Records" scope="request">
			<%int i = 1;%>
			<logic:iterate id="item" name="Records">
				<tr bgcolor="#FFFFFF">
					<td align="center">
						<bean:write name="item" property="orgName"/>
					</td>
					<td align="center">
						<bean:write name="item" property="orgClsName"/>
					</td>
					<td align="center">
						<bean:write name="item" property="OATName"/>
					</td>
					<td align="center">
						<a href="javascript:_set('<bean:write name="item" property="orgName"/>','<bean:write name="item" property="orgClsName"/>','<bean:write name="item" property="orgId"/>')">����</a>
					</td>
				</tr>
			</logic:iterate>
		</logic:present>
		<logic:notPresent name="Records" scope="request">
			<tr>
				<td colspan="4" bgcolor="#FFFFFF">
					����Ƶ�������Ϣ
				</td>
			</tr>
		</logic:notPresent>
	</table>
	<table cellSpacing="1" cellPadding="4" width="96%" border="0">
		<tr>
			<TD bgcolor="#FFFFFF">
				<jsp:include page="../../apartpage.jsp" flush="true">
					<jsp:param name="url" value="ViewOrg.do" />
				</jsp:include>
			</TD>
		</tr>
	</table>
</body>
</html:html>
