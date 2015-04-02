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
			 * 报表模板文件后缀
			 */
			 var EXT_EXCEL="<%=configBean.EXT_EXCEL%>";
			/**
			 * 表单提交事件
			 */
			 function _submit(form){
			 	if(form.templateFile.value==""){
			 		alert("请选择要载入的报表模板文件!\n");
			 		return false;
			 	}else{
			 		if(getExt(form.templateFile.value)!=EXT_EXCEL){
			 			alert("选择的报表模板文件必顺是EXCEL文件!");
			 			return false;
			 		}
			 	}
			 	return true;
			 }
		
			/**
			 * 返回新增非1104模板页面
			 */
			function _goto(forward){
			    window.location="<%=request.getContextPath()%>/template/add/" + forward;
			}
			
			/**
			 * 保存报表模板事件
			 */
			function _onsubmit() {
				if(document.getElementById('reportTitle').value.Trim() == ""){
					alert("请您先载入报有模板文件!\n");
					return false;
				}
				if(document.getElementById('childRepId').value.Trim() == ""){
					alert("请输入报表的编号!\n");
					return false;
				}
				if(document.getElementById('versionId').value.Trim() == ""){
					alert("请输入报表的版本号!\n");
					return false;
				}
				if(document.getElementById('repTypeId').value.Trim() == ""){
					alert("请选择报表的所属的类型!\n");
					return false;
				}
				if(document.getElementById('startRow').value.Trim() == ""){
					alert("开始行不能为空!\n");
					return false;
				}
				if(document.getElementById('startCol').value.Trim() == ""){
					alert("开始列不能为空!\n");
					return false;
				}
				if(document.getElementById('endRow').value.Trim() == ""){
					alert("结束行不能为空!\n");
					return false;
				}
				if(document.getElementById('endCol').value.Trim() == ""){
					alert("结束列不能为空!\n");
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
		<font face="宋体">
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
							<font face="宋体">
								<SPAN style="FONT-SIZE: 9pt">请选择模板文件：</SPAN></font>
						</TD>
						<TD>
							<font face="宋体">
								<SPAN style="FONT-SIZE: 9pt">
									<INPUT class="input-text" type="file" size="96" name="templateFile" style="FLOAT: right"></SPAN></font>
						</TD>
						<TD align="right" colspan="2">
							<input class="input-button" type="submit" value=" 载入 " >
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
									<TD class="tableHeader" width="10%">报表编号</TD>
									<TD class="tableHeader" width="40%">报表名称</TD>
									<TD class="tableHeader" width="20%">报表类型 </td>
									<TD class="tableHeader" width="10%">版本号</TD>
									<TD class="tableHeader" width="10%">货币单位</TD>
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
									<TD class="tableHeader">开始行</TD>
									<TD class="tableHeader">开始列</TD>
									<TD class="tableHeader">结束行</TD>
									<TD class="tableHeader">结束列</TD>
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
							<input class="input-button" type="submit" id="btnSave" name="btnSave" value="保存模板" onclick="return _onsubmit()">&nbsp;&nbsp;
							<input class="input-button" onclick="_goto('Not1104init.jsp')" type="button" value=" 返 回 ">
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
