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
		<link href="../../css/common.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="../../js/func.js"></script>
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
		<font face="宋体">
				<p style="MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px">
				</p>
				<html:form method="post" action="/template/uploadTmpt" enctype="multipart/form-data" onsubmit="return _submit(this)">
				<table border="0" cellspacing="0" cellpadding="4" width="96%" align="center">
					<tr><td height="8"></td></tr>
					<tr>
						<td>
							当前位置 >> 报表管理 >> 新增PDF模板
						</td>
					</tr>
					<tr><td height="10"></td></tr>		
				</table>
				<TABLE id="Table1" cellSpacing="0" width="96%" border="0" cellpadding="4" align="center">
					<TR>
						<TD align="center" height="16" colspan="2">
							<P align="left"></P>
						</TD>
					</TR>
					<TR>
						<TD width="27%">
							<font face="宋体">
								<SPAN style="FONT-SIZE: 9pt">请选择模板文件：</SPAN></font>
						</TD>
						<TD>
							<font face="宋体">
								<SPAN style="FONT-SIZE: 9pt">
									<INPUT class="input-text" type="file" size="76" name="templateFile" style="FLOAT: right"></SPAN></font>
						</TD>
						<TD align="right" colspan="2">
							<input class="input-button" type="submit" value=" 载入 " >
						</TD>
					</TR>
				</TABLE>
				</html:form>	
				<TABLE cellSpacing="0" width="96%" border="0" align="center" cellpadding="4">
					<TR>
						<TD>
							<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
								<TR>
									<TD class="tableHeader" width="50%"><b>报表模板名称</b></TD>
									<TD class="tableHeader" width="15%"><b>报表类型</b></TD>
									<TD colspan="2" class="tableHeader" width="20%">
										<b>版本号</b></TD>
									<TD colspan="2" class="tableHeader" width="15%">
										<b>货币单位</b></TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD align="center" width="50%">
										-</TD>
									<TD align="center" width="15%">
										-</TD>
									<TD colspan="2" align="center" width="20%">
										-</TD>
									<TD colspan="2" align="center" width="15%">
										-</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
				</TABLE>
				
	</body>
</html:html>
