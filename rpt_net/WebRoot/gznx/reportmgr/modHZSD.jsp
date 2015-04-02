<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.fitech.net.hibernate.OrgNet"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ page import="com.fitech.net.adapter.StrutsOrgNetDelegate"%>
<%
	String orgfilename = (String) request.getAttribute("xmlorgname");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html lang="true">
<head>
	<html:base />

	<title>汇总设定</title>

	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script src="../../js/Tree_for_xml.js">
</script>

	<style rel="STYLESHEET" type="text/css">
.defaultTreeTable {
	margin: 0;
	padding: 0;
	border: 0;
}

.containerTableStyle {
	overflow: auto;
}

.standartTreeRow {
	font-family: Verdana, Geneva, Arial, Helvetica, sans-serif;
	font-size: 14px;
	-moz-user-select: none;
}

.selectedTreeRow {
	background-color: navy;
	color: white;
	font-family: Verdana, Geneva, Arial, Helvetica, sans-serif;
	font-size: 14x;
	-moz-user-select: none;
}

.standartTreeImage {
	width: 14x;
	height: 1px;
	overflow: hidden;
	border: 0;
	padding: 0;
	margin: 0;
}

.hiddenRow {
	width: 1px;
	overflow: hidden;
}

.dragSpanDiv {
	font-size: 12px;
	border: thin solid 1 1 1 1;
}
</style>
	<script language="javascript">
function _back(childRepId, versionId, upOrgId) {
	var orgIds = "";
	var formObj = document.getElementById("frmSel");
	var box = formObj.elements['orgIds'];

	for ( var i = 0; i < document.frmSel.elements.length; i++) {
		var v = document.frmSel.elements[i];

		if (v.name == "orgIds" && v.checked)
			orgIds = orgIds + "," + v.value;
	}
	window.location = "<%=request.getContextPath()%>/template/mod/preEditHZSD.do?childRepId="
			+ formObj.childRepId.value + "&versionId=" + formObj.versionId.value + "&orgId=" + formObj.upOrgId.value
			+ "&orgIds=" + orgIds;
}

/**
 * 返回事件
 *1.14 yql
 */
function goback() {
	// var form=document.forms['frmBJGX'];
window.location="<%=request.getContextPath()%>/viewAFTemplateDetail.do?templateId=<%=request.getAttribute("childRepId")%>&versionId=<%=request.getAttribute("versionId")%>&bak2=2";
		   }			
		

//是否级联选择
	function checkbox_jl(){
		if(document.getElementById('checkbox_jl_org').checked==true){
			tree2.enableThreeStateCheckboxes(true);
		}else{
			tree2.enableThreeStateCheckboxes(false);
		}
	}
	</script>
</head>

<body>
	<%
		String childRepId = (String) request.getAttribute("childRepId");
			String versionId = (String) request.getAttribute("versionId");
			String orgId = (String) request.getAttribute("orgId");
			String curOrgId = (String) request.getAttribute("curOrgId");
			String upOrgId = "";
			upOrgId = StrutsOrgNetDelegate.getUpOrgId(orgId);
			request.setAttribute("childRepId", childRepId);
			request.setAttribute("versionId", versionId);
			int i = 1;
	%>

	<html:form action="/template/mod/updateBSFW.do" method="post" styleId="frmSel">
		<table border="0" cellspacing="0" cellpadding="4" width="96%" align="center">
			<tr>
				<td height="8"></td>
			</tr>
			<tr>
				<td>
					当前位置 >> 报表管理 >> 模板维护 >> 汇总关系设定
				</td>
			</tr>
			<tr>
				<td height="10"></td>
			</tr>
		</table>
		<TABLE cellSpacing="1" cellPadding="4" width="96%" border="0" align="center" class="tbcolor">
			<TR class="tbcolor1">
				<th align="center" id="list" height="30" colspan="2">
					<span style="FONT-SIZE: 11pt"> <logic:present name="ReportName" scope="request">
							《<bean:write name="ReportName" />》
						</logic:present>汇总关系设定 </span>
				</th>
			</TR>
				
			<tr>
				<td>
					级联选择：
					<input type="checkbox" name="checkbox_jl_org" onclick="return checkbox_jl()">
				</td>
			</tr>
			
			<tr>
				<td align="left" bgcolor="#EEEEEE">
					请选择报表汇总机构:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</tr>

			<logic:present name="isNull" scope="request">
				<tr colspan="8">
					<td colspan="8" align="center">
						暂无机构信息
					</td>
				</tr>
				<tr>
					<td align="center">
						<input type="button" value="返  回" class="input-button" onclick="goback();" />
					</td>
				</tr>
			</logic:present>
			<logic:notPresent name="isNull" scope="request">
				<tr>
					<td valign="top">
						<div id="treeboxbox_tree2" style="width: 100%; height: 300; background-color: #f5f5f5; border: 1px solid Silver;; overflow: auto;"></div>
					</td>
				</tr>
				<tr>
					<td align="center">
						<input type="button" value="保  存" class="input-button" onclick="getAllCheckedId()">
						&nbsp;
						<input type="button" value="返  回" class="input-button" onclick="goback();" />
					</td>
					<td>
					</td>

				</tr>
			</logic:notPresent>
		</TABLE>
	</html:form>

	<script>
function getAllCheckedId() {
	var checkId = tree2.getAllChecked();
	if (checkId.replace(/(^[\s]*)|([\s]*$)/g, "") == '') {
		alert("请选择汇总范围!");
		return;
	} else {//gongming 2008-07-26
		window.location = "<%=request.getContextPath()%>/gznx/updateHZSD.do?orgIds=" + checkId
				+ "&childRepId=<%=childRepId%>&versionId=<%=versionId%>";
	}
}
tree2 = new dhtmlXTreeObject("treeboxbox_tree2", "100%", "100%", 0);
tree2.setImagePath("../../image/treeImgs/");
tree2.enableCheckBoxes(1);
tree2.enableThreeStateCheckboxes(false);
//	<%=Config.WEBROOTPATH%>+"xml\\tree.xml"
tree2.loadXML("<%=request.getContextPath()%>/xml/<%=orgfilename%>");
</script>
	<br>
	<br>
</body>
</html:html>
