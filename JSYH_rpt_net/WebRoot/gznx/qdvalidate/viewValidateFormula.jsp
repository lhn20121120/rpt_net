<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String templateId="",versionId="";
	if(request.getAttribute("templateId")!=null) templateId=(String)request.getAttribute("templateId");
	if(request.getAttribute("versionId")!=null) versionId=(String)request.getAttribute("versionId");

%>
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.ColNumFormUtil" />
<jsp:setProperty property="templateId" name="FormBean" value="<%=templateId%>"/>
<jsp:setProperty property="versionId" name="FormBean" value="<%=versionId%>"/>
<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript" src="../../js/prototype-1.4.0.js"></script>
	<script type="text/javascript">

		 
		 //���ύ��֤	 
		 function _submit(form)
		 {
		
			if(form.formuValue.value=="")
			{
				alert("������У�鹫ʽ��");
				form.reportName.focus();
				return false;
			}


			return true;
		 }
 			/**
			  * �����¼�
			  * 1.14 yql
			  */
			  function _back(){

			  	var form=document.getElementById("form1");
			  	window.location="<%=request.getContextPath()%>/gznx/viewQDValidateList.do?templateId=" + form.elements['templateId'].value +
			  		"&versionId=" + form.elements['versionId'].value ;
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
	<html:form method="post" action="/gznx/editQDValidate.do?vFlg=update"  enctype="multipart/form-data" styleId="form1" onsubmit="return _submit(this)">
		<html:hidden property="templateId"/>
		<html:hidden property="versionId"/>
		<html:hidden property="formulaId"/>		
		
		<table border="0" cellspacing="0" cellpadding="4" width="100%" align="center">
			<tr><td height="8"></td></tr>
			<tr>
				<td>
					��ǰλ�� >> ������� >> ����У�鹫ʽ
				</td>
			</tr>
			<tr><td height="10"></td></tr>		
		</table>
		<table cellpadding="4" cellspacing="1" border="0" width="100%" align="center" class="tbcolor">
			<tr class="titletab">
				<th align="center">
					�޸�У�鹫ʽ
				</th>
			</tr>
			<tr align="center" bgcolor="#ffffff">
				<td align="center">
					<table cellspacing="2" cellpadding="2" border="0" width="90%" align="center">
					<TR bgcolor="#ffffff">
							<TD>
								У�����ͣ�
							</TD>
							<TD>
								<html:select name="form1" property="formuType" styleId="formuType">
								<html:option value="1">���ڵ�һУ��</html:option>
								<html:option value="2">�������У��</html:option>
								<html:option value="3">������У��</html:option>
								</html:select>
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								�кţ�
							</TD>
							<TD>
								<html:select name="form1" property="colNum" styleId="colNum">
									<html:optionsCollection name="FormBean" property="colNumList"/>
								</html:select>
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								������
							</TD>
							<TD>
								
								<html:text name="form1" property="colName" size="20" maxlength="100" styleClass="input-text" styleId="colName" />
							</TD>
						</TR>
						
						<TR bgcolor="#ffffff">
							<TD>
								У�����ƣ�
							</TD>
							<TD>
								<html:text name="form1" property="formuName" size="30" maxlength="100" styleClass="input-text" styleId="formuName" />
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								У�鹫ʽ��
							</TD>
							<TD>
								<html:text name="form1" property="formuValue" size="30" maxlength="100" styleClass="input-text" styleId="formuValue" />
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								��ʽ˵����
							</TD>
							<TD>
								<html:textarea name="form1" property="formuDes" cols="80" rows="10" ></html:textarea>
							</TD>
						</TR>

						<TR bgcolor="#ffffff">
							<TD>
								������ʾ��Ϣ:
							</TD>
							<TD>
								<html:textarea name="form1" property="validateMessage" cols="80" rows="10" ></html:textarea>								
							</TD>
						</TR>
					
						<TR>
							<TD align="center" colspan="2">
								<input type="submit" name="save" value="����">
								<input type="button" value="����" class="input-button" onclick="_back()">
							</TD>
						</TR>
					</table>
				</td>
			</tr>
		</table>
	</html:form>
</body>
</html:html>
