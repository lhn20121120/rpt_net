<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="java.util.List"%>
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
		<script language="javascript" scr="../../js/comm.js"></script>
		<script language="javascript">
			/**
			 * ����ģ���ļ���׺
			 */
			 var EXT_EXCEL="<%=configBean.EXT_EXCEL%>";
			/**
			 * ���ύ�¼�
			 */
			 function _submit(form){
			 	if(form.templateFile.value==""){
			 		alert("��ѡ��Ҫ����ı���ģ���ļ�!\n");
			 		return false;
			 	}else{
			 		if(getExt(form.templateFile.value)!=EXT_EXCEL){
			 			alert("ѡ��ı���ģ���ļ���˳��EXCEL�ļ�!");
			 			return false;
			 		}
			 	}
			 	return true;
			 }
		
			/**
			 * ����������1104ģ��ҳ��
			 */
			function _goto(forward){
			    window.location="<%=request.getContextPath()%>/template/add/" + forward;
			}
			
			/**
			 * ���汨��ģ���¼�
			 */
			function _onsubmit() {
				if(document.getElementById('reportTitle').value.Trim() == ""){
					alert("���������뱨��ģ���ļ�!\n");
					return false;
				}
				if(document.getElementById('childRepId').value.Trim() == ""){
					alert("�����뱨��ı��!\n");
					return false;
				}
				if(document.getElementById('versionId').value.Trim() == ""){
					alert("�����뱨��İ汾��!\n");
					return false;
				}
				if(document.getElementById('repTypeId').value.Trim() == ""){
					alert("��ѡ�񱨱������������!\n");
					return false;
				}
				if(document.getElementById('startRow').value.Trim() == ""){
					alert("��ʼ�в���Ϊ��!\n");
					return false;
				}
				if(document.getElementById('startCol').value.Trim() == ""){
					alert("��ʼ�в���Ϊ��!\n");
					return false;
				}
				if(document.getElementById('endRow').value.Trim() == ""){
					alert("�����в���Ϊ��!\n");
					return false;
				}
				if(document.getElementById('endCol').value.Trim() == ""){
					alert("�����в���Ϊ��!\n");
					return false;
				}
				return true;
			}
			String.prototype.Trim=function(){
				return this.replace(/(^\s*)|(\s*$)/g,"");
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
				<html:form method="post" action="/template/uploadNot1104Tmpt" enctype="multipart/form-data" onsubmit="return _submit(this)">
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
						<TD align="right" colspan="2">
							<input class="input-button" type="submit" value=" ���� " >
						</TD>
					</TR>
				</TABLE>
				</html:form>
				<html:form action="/template/saveNot1104Tmpt" method="post" onsubmit="return _onsubmit()">
				<TABLE cellSpacing="0" border="0" width="96%" cellpadding="4" align="center">
					<TR>
						<TD colspan="4">
							<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
								<TR>
									<TD class="tableHeader" width="10%">������</TD>
									<TD class="tableHeader" width="40%">��������</TD>
									<TD class="tableHeader" width="20%">�������� </td>
									<TD class="tableHeader" width="10%">�汾��</TD>
									<TD class="tableHeader" width="10%">���ҵ�λ</TD>
								</TR>
								
								<TR bgcolor="#FFFFFF" align="center" width="100%">
									<td align="center">
										<input type="text" name="childRepId" size="10">
									</td>
									<TD>
										<logic:present name="ReportTitle" scope="request">	
											<bean:write name="ReportTitle" scope="request"/>
											<input type="hidden" name="reportTitle" value="<bean:write name='ReportTitle' scope='request'/>">
										</logic:present>
									</TD>
									<td align="center">
										<html:select property="repTypeId" styleId="repTypeId">
											<html:optionsCollection name="utilFormBean" property="repTypes"/>
										</html:select>
									</td>
									<TD align="center">
											<input type="text" name="versionId" size="7" maxsize="5">
									</TD>
									<TD align="center">
										<logic:present name="ReportCurUnit" scope="request">
											<bean:write name="ReportCurUnit" scope="request"/>
											<input type="hidden" name="reportCurUnit" value="<bean:write name='ReportCurUnit' scope='request'/>">
										</logic:present>
									</TD>
								</TR>
								<tr>
									<TD class="tableHeader">��ʼ��</TD>
									<TD class="tableHeader">��ʼ��</TD>
									<TD class="tableHeader">������</TD>
									<TD class="tableHeader">������</TD>
									<TD class="tableHeader">&nbsp;</TD>
								</tr>
								<TR bgcolor="#FFFFFF" align="center" width="100%">
									<TD align="center">
											<input type="text" name="startRow" size="3">
									</TD>
									<TD align="center">
											<input type="text" name="startCol" size="3">
									</TD>
									<TD align="center">
											<input type="text" name="endRow" size="3">
									</TD>
									<TD align="center">
											<input type="text" name="endCol" size="3">
									</TD>
									<td>&nbsp;</td>
								</TR>
								</TABLE>
						</TD>
					</TR>
					<TR>
						<TD height="29" align="right" colspan="4">
							<input class="input-button" type="submit" id="btnSave" name="btnSave" value="����ģ��" onclick="return _onsubmit()">&nbsp;&nbsp;
							<input class="input-button" onclick="_goto('Not1104init.jsp')" type="button" value=" �� �� ">
						</TD>
					</TR>
				</TABLE>
			</span>
			</font>
				<logic:present name="ReportName" scope="request">
					<input type="hidden" name="tmpFileName" value="<bean:write name='ReportName' scope='request'/>">
				</logic:present>
				<logic:present name="ReportTitle" scope="request">	
					<input type="hidden" name="reportName" value="<bean:write name='ReportTitle' scope='request'/>">
				</logic:present>
			</html:form>	
			<script language="javascript">
				//var objFile=document.getElementById("tmpFileName");
				//if(objFile.value==""){
					//document.getElementById('btnSave').disabled="true";
				//}
			</script>
	</body>
</html:html>
