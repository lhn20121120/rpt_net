<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.util.FitechUtil"%>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config"/>
<%
	String childRepId="",versionId="",reportName="",reportStyle="",curPage="0";
	if(request.getParameter("childRepId")!=null) childRepId=(String)request.getParameter("childRepId");
	if(request.getParameter("versionId")!=null) versionId=(String)request.getParameter("versionId");

	if(request.getParameter("reportName")!=null) reportName=(String)request.getParameter("reportName");
	
	if(request.getParameter("reportStyle")!=null) reportStyle=(String)request.getParameter("reportStyle");
	if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
%>
<html:html locale="true">
	<head>
		<html:base/>
		<title>选择表内、表间关系表达式定义文件</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">		
		<script language="javascript" src="../../js/comm.js"></script>
		<script language="javascript" src="../../js/func.js"></script>
		<script language="javascript">
			/**
			 * 表内表间关系表达式文件后缀名
			 */ 
			 var EXT_TXT="<%=configBean.EXT_TXT%>";
			/**
			 * 表单提交事件
			 */
			 function _submit(form){
			 	if(form.expressionFile.value==""){
			 		alert("请选择要表内表间关系表达式定义文件!\n");
			 		return false;
			 	}else{
			 		if(getExt(form.expressionFile.value)!=EXT_TXT){
			 			alert("选择的报表模板文件必顺是" + EXT_TXT + "文件!");
			 			return false;
			 		}
			 	}
			 	
			 	return true;
			 }
			 /**
			  * 返回事件
			  */
			  function _back(){
			  	var form=document.forms['mCellForumForm'];
			  	window.location="<%=request.getContextPath()%>/template/mod/editBJGXInit.do?" +
			  		"childRepId=" + form.elements['childRepId'].value +
			  		"&versionId=" + form.elements['versionId'].value + 
			  		"&reportName=" + form.elements['reportName'].value +
			  		"&reportStyle=" + form.elements['reportStyle'].value +
			  		"&curPage=" + form.elements["curPage"].value;
			  }
		</script>
	</head>
	<body>
		<center>
		<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
		</logic:present>
		<table border="0" cellspacing="0" cellpadding="4" width="60%" align="center">
			<tr><td height="8"></td></tr>
			<tr>
				<td>
					当前位置 >> 报表管理 >> 报表定义管理 >> 模板修改 >> 表内表间关系修改>> 导入表达式
				</td>
			</tr>
			<tr><td height="10"></td></tr>	
		</table>	
		<table border="0" cellpadding="4" cellspacing="1" width="60%" class="tbcolor">
			<tr class="titletab">
				<th colspan="4" align="center">
				
					<%=reportName%>
				</th>
			</tr>
			<tr>
				<td align="left" bgcolor="#D0D6EE">
					请选择表内表间关系表达式定义文件：
				</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF">
					<html:form method="post" action="/template/uploadBJGX.do?flag=mod" enctype="multipart/form-data" onsubmit="return _submit(this)">
					<table border="0" cellpadding="4" cellspacing="0" width="100%">
						<tr bgcolor="#FFFFFF">
							<td align="center">
								<input type="file" name="expressionFile" size="60" class="input-text">
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td align="center">
								<input type="submit" value="确定" class="input-button">&nbsp;&nbsp;
								<input type="button" value="返回" class="input-button" onclick="_back()">
							</td>
						</tr>
					</table>
					<input type="hidden" name="childRepId" value="<%=childRepId%>">
					<input type="hidden" name="versionId" value="<%=versionId%>">
					<input type="hidden" name="reportName" value="<%=reportName%>">
					<input type="hidden" name="reportStyle" value="<%=reportStyle%>">
					<input type="hidden" name="curPage" value="<%=curPage%>">
					</html:form>
				</td>
			</tr>
		</table>
		</center>
	</body>
</html:html>