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
			 * 报表模板文件后缀
			 */
			 var EXT_PDF="<%=configBean.EXT_PDF%>";
			 
			/**
			 * 表单提交事件
			 */
			 function _submit(form){
			 	if(form.templateFile.value==""){
			 		alert("请选择要载入的报表模板文件!\n");
			 		return false;
			 	}else{
			 		if(getExt(form.templateFile.value)!=EXT_PDF){
			 			alert("选择的报表模板文件必顺是PDF文件!");
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
							<font face="宋体">
								<SPAN style="FONT-SIZE: 9pt">请选择要签名的文件：</SPAN></font>
						</TD>
						<TD>
							<font face="宋体">
								<SPAN style="FONT-SIZE: 9pt">
									<INPUT class="input-text" type="file" size="96" name="templateFile" style="FLOAT: right"></SPAN></font>
						</TD>
					</TR>
					<TR>
						<TD align="right" colspan="2">
							<input class="input-button" type="submit" value=" 载入 " >
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
										<b>文件路径</b>
								  </TD>
								</TR>
							</TABLE>
							<TABLE width="100%" cellSpacing="1" class="tbcolor">
								<TR bgcolor="#FFFFFF" cellSpacing="1">
									<TD align="center" colspan="2">	
									<input name="box" type="checkbox" value="zhang"  checked></TD>
									<TD width="94%" colspan="2" align="left">D:\报表\报表G00.xml</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD align="center" colspan="2">	
									<input name="box" type="checkbox" value="zhang"  checked></TD>
									<TD width="94%" colspan="2" align="left">D:\报表\报表G01.xml</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD align="center" colspan="2">	
									<input name="box" type="checkbox" value="zhang"  checked></TD>
									<TD width="94%" colspan="2" align="left">D:\报表\报表G02.xml</TD>
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
					    <TD colspan="2"align="left"><input class="input-button" type="submit" value="签名" >
							
						</TD>
						<TD colspan="2" width="92%"align="left">&nbsp;
						</TD>
					</TR>
				</TABLE>
				
	</body>
</html:html>
