<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config"/>
<html:html locale="true">
	<head>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">

		<link href="../css/common.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="../js/func.js"></script>

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
		</script>
	</head>
	<body style="TEXT-ALIGN: center" background="../image/total.gif">
		<logic:present name="Message" scope="request">
			<logic:greaterThan name="Message" property="size" value="0">
				<script language="javascript">
					alert("<bean:write name='Message' property='alertMsg'/>");
				</script>
			</logic:greaterThan>
		</logic:present>

				<html:form method="post" action="/template/uploadTmpt" enctype="multipart/form-data" onsubmit="return _submit(this)">
				<TABLE id="Table1" cellSpacing="0" width="98%" border="0" cellpadding="4" align="center">
					<TR>
						<TD align="center" height="16" colspan="2">
							<P align="left"></P>
						</TD>
					</TR>
					<TR>
						<TD width="30%">
							<font face="����">
								<SPAN style="FONT-SIZE: 9pt">��ѡ��Ҫǩ�����ļ���</SPAN></font>
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
				<TABLE cellSpacing="1" width="90%" border="0" align="center" cellpadding="4">
					<TR>
						<TD>
							<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
								<TR>
									<TD width="100%" colspan="2" align="center" class="tableHeader">
										<b>�ļ�·��</b>
								  </TD>
								</TR>
							</TABLE>
							<TABLE width="100%" cellSpacing="1" class="tbcolor">
								<TR bgcolor="#FFFFFF" cellSpacing="1">
									<TD align="center" colspan="2">	
									<input name="box" type="checkbox" value="zhang"  checked></TD>
									<TD width="94%" colspan="2" align="left">D:\����\����G00.xml</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD align="center" colspan="2">	
									<input name="box" type="checkbox" value="zhang"  checked></TD>
									<TD width="94%" colspan="2" align="left">D:\����\����G01.xml</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD align="center" colspan="2">	
									<input name="box" type="checkbox" value="zhang"  checked></TD>
									<TD width="94%" colspan="2" align="left">D:\����\����G02.xml</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD align="center" colspan="2">	
									<input name="box" type="checkbox" value="zhang" ></TD>
									<TD width="94%" colspan="2" align="left">&nbsp;</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD align="center" colspan="2">	
									<input name="box" type="checkbox" value="zhang" ></TD>
									<TD width="94%" colspan="2" align="left">&nbsp;</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD align="center" colspan="2">	
									<input name="box" type="checkbox" value="zhang" ></TD>
									<TD width="94%" colspan="2" align="left">&nbsp;</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD align="center" colspan="2" >	
									<input name="box" type="checkbox" value="zhang" ></TD>
									<TD width="94%" colspan="2" align="left">&nbsp;</TD>
								</TR>
						  </TABLE>
						</TD>
					</TR>
				</TABLE>
				<TABLE id="Table1" cellSpacing="0" width="85%" border="0" cellpadding="4" align="center">
					
					<TR>
					    <TD colspan="2"align="left"><input class="input-button" type="submit" value="ǩ��" >
							
						</TD>
						<TD colspan="2" width="92%"align="left">&nbsp;
						</TD>
					</TR>
				</TABLE>
				
	</body>
</html:html>
