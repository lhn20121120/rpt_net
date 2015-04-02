<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.fitech.net.hibernate.OrgNet"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ page import="com.fitech.net.adapter.StrutsOrgNetDelegate"%>
<%@ page import="com.fitech.gznx.po.PreOrg,java.util.List"%>


<%
	
//	String orgfilename = (String) request.getAttribute("xmlorgname");
	String orgfilename = (String) request.getAttribute("FileName");
	String opType=(String)request.getAttribute("optype");
	request.setAttribute("hzgsMap",Config.HZGS_LIST);

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
	<link href="<%=request.getContextPath()%>/resource/css/progressBar.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/resource/js/progressBar.js"></script>
	<script src="../../js/Tree_for_xml.js"></script>
	<script src="../../js/jquery-1.4.2.js"></script>

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
	window.location = "<%=request.getContextPath()%>/template/mod/preSetHZSD.do?childRepId="
			+ formObj.childRepId.value + "&versionId=" + formObj.versionId.value + "&orgId=" + formObj.upOrgId.value
			+ "&orgIds=" + orgIds;
}

			/**
			 * 返回事件
			 *1.14 yql
			 */
			function goback() {
				var optype=document.getElementById("optype").value
				
				if(optype!=null&&optype!=''&&optype!='null'){
							
			          	    window.location="<%=request.getContextPath()%>/gznx/EditHZGS.do?templateId="+document.getElementById("template_id").value+"&versionId="+document.getElementById("version_id").value+"&reportName="+document.getElementById("reportName").value+"&orgId="+document.getElementById("org_id").value+"&opration=edit";
			           	  
                }else{
			                
			           	    window.location="<%=request.getContextPath()%>/gznx/hzformula.do?childRepId="+document.getElementById("template_id").value+"&versionId="+document.getElementById("version_id").value+"&reportName="+document.getElementById("reportName").value+"&templateId="+document.getElementById("template_id").value+"&orgId="+document.getElementById("org_id").value+"&orgName="+document.getElementById("org_name").value+"&opration=next";
			         }
			}			
				

//是否级联选择
	function checkbox_jl(){
		if(document.getElementById('checkbox_jl_org').checked==true){
			tree2.enableThreeStateCheckboxes(true);
		}else{
			tree2.enableThreeStateCheckboxes(false);
		}
	}
	function saveRule(){
		var url='';
		var template_id=document.getElementById("template_id").value;
//		alert(template_id);
		
		var version_id=document.getElementById("version_id").value;
		
		var org_id=document.getElementById("org_id").value;
	
		var org_name=document.getElementById("org_name").value;
	
		var coll_formula=document.getElementById("coll_formula").value;
//		alert(coll_formula);
		var coll_schema=document.getElementById("collSchema").value;
		var reportName=document.getElementById("reportName").value;
		var hz_style=document.getElementById("hzStyle").value;
		var optype=document.getElementById("optype").value;
		var preOrgId=document.getElementById("preOrgId").value;
//		alert(preOrgId);
		
		if(coll_schema=='HZTJH'){
			 url="<%=request.getContextPath()%>/gznx/saveHZRule.do?templateId="+template_id+"&versionId="+version_id+"&orgId="+org_id+"&orgName="+org_name+"&reportName="+reportName+"&collSchema="+coll_schema+"&hzStyle="+hz_style+"&collFormula="+coll_formula+"&preOrgId="+preOrgId;
			if(optype!=''&&optype!=null){
				url+="&optype="+optype;
			}
			window.location=url;
//			alert(window.location);                                                      
		}
		else {
			coll_formula=getAllCheckedId();
			if(coll_formula==''||coll_formula==null){
				alert('请选择机构');
				return;
			}
			url="<%=request.getContextPath()%>/gznx/saveHZRule.do?templateId="+template_id+"&versionId="+version_id+"&orgId="+org_id+"&orgName="+org_name+"&reportName="+reportName+"&collSchema="+coll_schema+"&hzStyle="+hz_style+"&collFormula="+coll_formula+"&preOrgId="+preOrgId;
//			alert(coll_formula);
			if(optype!=''&&optype!=null){
				url+="&optype="+optype
			}
			window.location=url;
		}
	}
	
	
	</script>
</head>

<body onload="disableDiv()">
	
<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<html:form action="/gznx/saveHZRule.do" method="post" styleId="frmSel">
		<table border="0" cellspacing="0" cellpadding="4" width="96%" align="center">
			
			<%--<input type="hidden" id="template_id" value="<bean:write name="form" property="template_id" />"/>
			<input type="hidden" id="version_id" value="<bean:write name="form" property="version_id" />"/>
			<input type="hidden" id="org_id" value="<bean:write name="form" property="org_id" />"/>
			<input type="hidden" id="org_name" value="<bean:write name="form" property="org_name" />"/>
			<input type="hidden" id="coll_formula" value="<bean:write name="form" property="coll_formula" />"/>
			<input type="hidden" id="reportName" value="<bean:write name="form" property="reportName" />"/>
			--%><input type="hidden" id="optype" value="<%=opType%>"/>
			<html:hidden property="template_id" styleId="template_id"/>
			<html:hidden property="version_id" styleId="version_id"/>
			<html:hidden property="org_id" styleId="org_id"/>
			<html:hidden property="org_name" styleId="org_name"/>
			<html:hidden property="coll_formula" styleId="coll_formula"/>
			<html:hidden property="reportName" styleId="reportName"/>
	
			
			
			<tr>
				<td>
					当前位置 >> 报表管理 >> 模板添加 >> 汇总公式设定
				</td>
				
			</tr>
			<tr>
				<td height="8">&nbsp;&nbsp;</td>
			</tr>
	
		</table>
		<TABLE cellSpacing="1" cellPadding="4" width="90%" border="0" align="center" class="tbcolor">
		<TR class="tbcolor1">
				<th align="center" id="list" height="30" colspan="2">
					<span style="FONT-SIZE: 11pt"> 汇总方式<html:select property="hz_style"  styleId="hzStyle">
						<html:option value="0">轧差汇总</html:option>
						<html:option value="1">加总</html:option>
					</html:select>
						汇总公式:<html:select property="preOrgId" styleId="preOrgId">
						<%
							List<PreOrg> preOrgList=(List)request.getAttribute("preOrgList");
						%>
						<html:options collection="preOrgList" property="orgId" labelProperty="orgName"/>
						
						</html:select>-sum(单元格值,机构 in (
						<html:select property="coll_schema" onchange="changeCollType()"  styleId="collSchema">
							<html:option value="HZTJH">同级行</html:option>
							<html:option value="CUSTOM_ORG">自定义</html:option>
						</html:select>
						) and 机构!=本机构)</span>
				</th>
			</TR>
				
				<tr>
				<td  height="6" align="left" >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
				<tr>
					<td valign="top">
						<div id="treeboxbox_tree2" style="width:1100;height:300; background-color: #f5f5f5; border: 1px solid Silver;; overflow: auto;"></div>
<%--						<div id="HZTJH" style="width:100%;display:block;height: 300; background-color: #f5f5f5; border: 1px solid Silver;; overflow: auto;"></div>--%>
					</td>
				</tr>
				<tr>
					<td align="center">
						<input type="button" value="保存" class="input-button" onclick="saveRule()">
						
						&nbsp;
						<input type="button" value="返  回" class="input-button" onclick="goback();" />
					</td>
					<td>
					</td>

				</tr>
			
		</TABLE>
	</html:form>

	<script>
	var tree2;
	var progressBar=new ProgressBar("treeboxbox_tree2");
	var collSchema=document.getElementById("collSchema").value;
	function getAllCheckedId() {
		var checkId = tree2.getAllChecked();
		return checkId;
	}


	function  changeCollType(){
		if(document.getElementById("collSchema").value=="HZTJH"){
			document.getElementById("treeboxbox_tree2").innerHTML="";
			tree2 = new dhtmlXTreeObject("treeboxbox_tree2", "100%", "100%", 0);
			tree2.setImagePath("../../image/treeImgs/");
			tree2.enableCheckBoxes(0);
			tree2.enableThreeStateCheckboxes(false);
//			tree2.loadXML("<%=request.getContextPath()%>/xml/<%=orgfilename%>");
			tree2.loadXML("<%=request.getContextPath()%>/tree_xml/org_tree/<%=orgfilename%>");
			progressBar.show();
		}
		else {
			document.getElementById("treeboxbox_tree2").innerHTML="";
			tree2 = new dhtmlXTreeObject("treeboxbox_tree2", "100%", "100%", 0);
			tree2.setImagePath("../../image/treeImgs/");
			tree2.enableCheckBoxes(1);
			tree2.enableThreeStateCheckboxes(false);
//			tree2.loadXML("<%=request.getContextPath()%>/xml/<%=orgfilename%>");
			tree2.loadXML("<%=request.getContextPath()%>/tree_xml/org_tree/<%=orgfilename%>");
			progressBar.close();
		}
	}
	function disableDiv(){
		if(collSchema=='HZTJH'){
			document.getElementById("treeboxbox_tree2").innerHTML="";
			tree2 = new dhtmlXTreeObject("treeboxbox_tree2", "100%", "100%", 0);
			tree2.setImagePath("../../image/treeImgs/");
			tree2.enableCheckBoxes(0);
			tree2.enableThreeStateCheckboxes(false);
	//		tree2.loadXML("<%=request.getContextPath()%>/xml/<%=orgfilename%>");
			tree2.loadXML("<%=request.getContextPath()%>/tree_xml/org_tree/<%=orgfilename%>");
			progressBar.show();
		}else{
			document.getElementById("treeboxbox_tree2").innerHTML="";
			tree2 = new dhtmlXTreeObject("treeboxbox_tree2", "100%", "100%", 0);
			tree2.setImagePath("../../image/treeImgs/");
			tree2.enableCheckBoxes(1);
			tree2.enableThreeStateCheckboxes(false);
//			tree2.loadXML("<%=request.getContextPath()%>/xml/<%=orgfilename%>");
			tree2.loadXML("<%=request.getContextPath()%>/tree_xml/org_tree/<%=orgfilename%>");
			progressBar.close();
		}
		
	}
</script>
	<br>
	<br>
</body>
</html:html>
