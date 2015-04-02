<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="utilFormBean" scope="page" class="com.cbrc.smis.form.UtilForm"/>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config"/>
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
			 * ����ģ���ļ���׺
			 */
			 var EXT_PDF="<%=configBean.EXT_PDF%>";
			/**
			 * ���ύ�¼�
			 */
			 function _submit(form){
			 	if(form.templateFile.value==""){
			 		alert("��ѡ��Ҫ����ı���ģ���ļ�!\n");
			 		return false;
			 	}else{
			 		if(getExt(form.templateFile.value)!=EXT_PDF){
			 			alert("ѡ��ı���ģ���ļ���˳��PDF�ļ�!");
			 			return false;
			 		}
			 	}
			 	return true;
			 }
		
			/**
			 * ���汨��ģ���¼�
			 */
			function _onsubmit() {
				if(document.getElementById('tmpFileName').value==""){
					alert("���������뱨��ģ���ļ�!\n");
					return false;
				}
								
				if(document.getElementById('childRepId').value==""){
					alert("�����뱨��ı��!\n");
					document.getElementById('childRepId').focus();
					return false;
				}
				var objReportStyle=document.getElementById("reportStyle");
				if(typeof(objReportStyle)!="undefined"){
					if(document.getElementById("reportTitle").value==""){
						alert("�����뱨�������!\n");
						document.getElementById("reportTitle").focus();
						return false;
					}
					if(document.getElementById("versionId").value==""){
						alert("�����뱨��汾��!\n");
						document.getElementById("reportTitle").focus();
						return false;
					}
				}
				if(document.getElementById('repTypeId').value==""){
					alert("��ѡ�񱨱������������!\n");
					return false;
				}
				
				return true;
			}
		</script>
	</head>
	<body style="TEXT-ALIGN: center" background="../../image/total.gif">
		<logic:present name="Message" scope="request">
			<logic:greaterThan name="Message" property="size" value="0">
				<script language="javascript">
					alert("<bean:write name='Message' property='alertMsg'/>");
				</script>
			</logic:greaterThan>
		</logic:present>
		<P style="MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px"></P>
		<P style="MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px"></P>
		<font face="����">
			<span style="FONT-SIZE: 9pt">
				<p style="MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px">
				</p>
				<html:form method="post" action="/template/uploadTmpt" enctype="multipart/form-data" onsubmit="return _submit(this)">
				<TABLE id="Table1" cellSpacing="0" width="96%" border="0" cellpadding="4" align="center">
					<TR>
						<TD align="center" height="16" colspan="2">
							<P align="left"></P>
						</TD>
					</TR>
					<TR>
						<TD width="27%">
							<font face="����">
								<SPAN style="FONT-SIZE: 9pt">��ѡ��ģ���ļ���</SPAN></font>
						</TD>
						<TD>
							<font face="����">
								<SPAN style="FONT-SIZE: 9pt">
									<INPUT class="input-text" type="file" size="96" name="templateFile" style="FLOAT: right"></SPAN></font>
						</TD>
					</TR>
					<TR>
						<TD align="right" colspan="2">
							<input class="input-button" type="submit" value=" ���� " >
						</TD>
					</TR>
				</TABLE>
				</html:form>
				<html:form action="/template/saveTmpt" method="post" onsubmit="return _onsubmit()">
				<TABLE cellSpacing="0" border="0" width="96%" cellpadding="4" align="center">
					<TR>
						<TD colspan="4">
							<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
								<TR>
									<TD class="tableHeader" width="10%">������</TD>
									<TD class="tableHeader" width="54%">��������</TD>
									<TD class="tableHeader" width="20%">�������� </td>
									<TD class="tableHeader" width="8%">�汾��</TD>
									<TD class="tableHeader" width="8%">���ҵ�λ</TD>
									
								</TR>
								<TR bgcolor="#FFFFFF">
									<td align="center">
										<html:text property="childRepId" styleId="childRepId" size="5" maxlength="5" styleClass="input-text"/>
									</td>
									<TD>
										<html:text property="reportTitle" size="50" maxlength="150" styleClass="input-text"/>
									</TD>
									<td align="center">
										<html:select property="repTypeId" styleId="repTypeId">
											<html:optionsCollection name="utilFormBean" property="repTypes"/>
										</html:select>
									</td>
									<TD align="center">
										<html:text property="versionId" size="4" maxlength="4" styleClass="input-text"/>
									</TD>
									<TD align="center">
										<html:select property="reportCurUnit">
											<html:optionsCollection name="utilFormBean" property="curUnits"/>
										</html:select>
									</TD>
								</TR>
								</TABLE>
						</TD>
					</TR>
					<TR>
						<TD height="29" align="right" colspan="4">
							<input class="input-button" type="submit" id="btnSave" name="btnSave" value="����ģ��" onclick="return _onsubmit()">&nbsp;&nbsp;
							<!-- <input class="input-button" onclick="_goto('init.jsp')" type="button" value=" �� �� ">-->
						</TD>
					</TR>
				</TABLE>
			</span>
			</font>
				<logic:present name="TmpFileName" scope="request">
					<input type="hidden" name="tmpFileName" id="tmpFileName" value="<bean:write name='TmpFileName' scope='request'/>">
				</logic:present>
				<logic:notPresent name="TmpFileName" scope="request">
					<input type="hidden" name="tmpFileName" id="tmpFileName" value="">
				</logic:notPresent>
				<logic:present name="ReportStyle" scope="request">
					<input type="hidden" name="reportStyle" id="reportStyle" value="<bean:write name='ReportStyle'/>">
				</logic:present>
			</html:form>	
			<script language="javascript">
				var objFile=document.getElementById("tmpFileName");
				if(objFile.value==""){
					document.getElementById('btnSave').disabled="true";
				}
				
				var objChildRepId=document.getElementById("childRepId");
				if(typeof(objChildRepId)!="undefined"){
					objChildRepId.focus();
				}
			</script>
	</body>
</html:html>
