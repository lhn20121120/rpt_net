<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.List,com.cbrc.smis.form.ReportInInfoForm,com.cbrc.smis.other.Expression"%>
<jsp:useBean id="utilFormOrg" scope="page" class="com.cbrc.org.form.UtilForm" />
<html:html locale="true">
<head>
	<title>����ֱ��ͷ�Χ�����趨</title>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../js/func.js"></script>
	
	<SCRIPT language="javascript">
		function _submit(form){
			if(isEmpty(form.orgId.value)){
				alert("���������ID��");
				form.orgId.focus();
				return false;
			}
			if(form.orgClsId.value == "0"){
				alert("��ѡ������������ƣ�");
				form.orgClsId.focus();
				return false;
			}
			return true;
		}
		String.prototype.Trim=function()
		{
			return this.replace(/(^\s*)|(\s*$)/g,"");
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
	<br/></br>
	<div align="center"><p>����ֱ��ͷ�Χ�����趨</p></div>
	<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center">
		<html:form action="/insert_range" method="post" styleId="frm" onsubmit="return _submit(this)">
			<tr>
				<td>
					<table cellspacing="0" cellpadding="0" border="0" width="38%" align="center">
						<tr>
							<td>����ID��</td>
							<td><html:text property="orgId" styleClass="input-text" size="17" value=""/><br/>
							</td>
						</tr>
						<tr>
							<td>�����������ƣ�</td>
							<td>
								<html:select property="orgClsId">
									<html:option value="0">--ѡ�������������--</html:option>
									<html:optionsCollection name="utilFormOrg" property="orgCls" value="value" label="label" />
								</html:select><br/>
							</td>
						</tr>
						<tr><br/>
							<td align="center">
								<html:submit styleClass="input-button" value="���ɱ��ͷ�Χ" />
							</td>
						</tr>
				  </table>
				</td>
			</tr>
		</html:form>
	</table>
</body>
</html:html>