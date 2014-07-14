<%@ page language="java" pageEncoding="GBK"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base />
	<title>机构管理</title>
	<%
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
	%>
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link href="../../css/globalStyle.css" type="text/css" rel="stylesheet">
	<script src="../../js/Tree_for_xml.js"></script>
	<script language="javascript">
		//显示树
			function createTree_org()
			{
				var fileName ="<logic:present name="FileName" scope="request"><bean:write name="FileName"/></logic:present>";
				tree=new dhtmlXTreeObject("treeObj_org","100%","100%",0);
				tree.setImagePath("../../image/treeImgs/");
				tree.enableCheckBoxes(1);
				tree.loadXML("../../tree_xml/org_tree/"+fileName);
			}
			
			//取得选中的机构的数目
			function getOrgNum(){
				var checkId=tree.getAllChecked();
				
				if(checkId==null||checkId==''){
				return 0;
				}
				
				var arrOrg = checkId.split(",");
				return arrOrg.length;
			}
			
			//增加机构
			function addOrg(){
				<%--
				如果没有选择任何机构，则增加的机构为根节点
				如果选择了一个机构，则增加的机构为选择机构的子机构
				--%>
				if(getOrgNum()==0){
					if(confirm('确定增加虚拟机构吗？')){
						document.getElementById('orgInfoForm').action="../../system_mgr/OrgInfo/beforeAdd.do";
						document.getElementById('orgInfoForm').submit();
					}
				}
				else if(getOrgNum()==1){
					document.getElementById('parentOrgId').value=tree.getAllChecked();
					document.getElementById('orgInfoForm').action="../../system_mgr/OrgInfo/beforeAdd.do";
					document.getElementById('orgInfoForm').submit();
				}
				else{
					alert('增加机构时，选择的机构数目不能大于1！');
				}
			}
			
			//删除机构
			function deleteOrg(){
				if(getOrgNum()==0){
					alert("请选择需要删除的机构！");
				}else if(getOrgNum()>1){
					alert("每次只能删除一个机构！");
				}else{
					if(confirm('确定删除机构吗？')){
						document.getElementById('orgInfoForm').action="../../system_mgr/OrgInfo/delete.do";
						document.getElementById('orgId').value=tree.getAllChecked();
						document.getElementById('orgInfoForm').submit();
					}
				}
			}
			//查看机构
			function viewOrg(){
				if(getOrgNum()==0){
					alert("请选择需要查看的机构！");
				}else if(getOrgNum()>1){
					alert("每次只能查看一个机构！");
				}else{
					document.getElementById('orgInfoForm').action="../../system_mgr/OrgInfo/beforeEdit.do?orgId="+tree.getAllChecked();
					document.getElementById('orgId').value=tree.getAllChecked();
					document.getElementById('type').value="detail";
					document.getElementById('orgInfoForm').submit();
				}
			}
			//修改机构
			function updateOrg(){
				if(getOrgNum()==0){
					alert("请选择需要修改的机构！");
				}else if(getOrgNum()>1){
					alert("每次只能修改一个机构！");
				}else{
					document.getElementById('orgInfoForm').action="../../system_mgr/OrgInfo/beforeEdit.do";
					document.getElementById('orgId').value=tree.getAllChecked();
					document.getElementById('type').value="update";
					document.getElementById('orgInfoForm').submit();
				}
			}
			//设为一级节点
			function setFirstNode(){
				if(getOrgNum()==0){
					alert("请选择需要设置的机构！");
				}else if(getOrgNum()>1){
					alert("每次只能设置一个机构！");
				}else{
					document.getElementById('orgInfoForm').action="../../system_mgr/OrgInfo/setFirstNode.do";
					document.getElementById('orgId').value=tree.getAllChecked();
					document.getElementById('orgInfoForm').submit();
				}
			}
			//报送范围
			function _range(){
				if(getOrgNum()==0){
					alert("请选择需要修改的机构！");
				}else if(getOrgNum()>1){
					alert("每次只能修改一个机构！");
				}else{	
					window.location="<%=request.getContextPath()%>/showOrgBSFW.do?org_id="+ tree.getAllChecked();
				}
			}

			//设置映射关系
			function setOrgRel(){
				if(getOrgNum()==0){
					alert("请选择需要设置的机构！");
				}else if(getOrgNum()>1){
					alert("每次只能设置一个机构！");
				}else{
					document.getElementById('orgInfoForm').action="../../searchVorgRelAction.do?proram=org_setting";
					document.getElementById('orgId').value=tree.getAllChecked();
					document.getElementById('orgInfoForm').submit();
				}
			}	
	</script>
</head>
<body style="background: white;">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table width="98%" border="0" align="center">
		<tr height="30">
			<td>
				当前位置 &gt;&gt; 系统管理 &gt;&gt; 机构管理
			<td>
		</tr>
	</table>
	<br />
	<table width="90%" border="0" align="center" cellpadding="0"
		cellspacing="1" class="tbcolor">
		<tr>
			<td width="100%">
				<div id="treeObj_org"
					style="width:100%; height:350;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;"></div>
			<td>
		</tr>
	</table>
	<br />
	<div align="center">
		<input type="button" name="addOrg" value="查看机构" Class="input-button"
			onclick="viewOrg();" />
		&nbsp;&nbsp;
		<input type="button" name="addOrg" value="增加机构" Class="input-button"
			onclick="addOrg();" />
		&nbsp;&nbsp;
		<input type="button" name="addOrg" value="修改机构" Class="input-button"
			onclick="updateOrg();" />
		&nbsp;&nbsp;
		<input type="button" name="addOrg" value="删除机构" Class="input-button"
			onclick="deleteOrg();" />
		<!-- 
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" name="addOrg" value="新增虚拟机构" Class="input-button"
			onclick="setFirstNode();" />
		&nbsp;&nbsp;
		 -->
		 &nbsp;&nbsp;
		 <input type="button"   class="input-button" value="报表范围" 
		 	onclick="_range()">
		 &nbsp;&nbsp;
		 <input type="button" class="input-button"  value="设置映射关系"  onclick="setOrgRel()"/>
	</div>
	<br />
	<br />
	<form name="orgInfoForm" method="post" action="" id="orgInfoForm">
		<input type="hidden" id="orgId" name="orgId" value="">
		<input type="hidden" id="parentOrgId" name="parentOrgId" value="">
		<input type="hidden" id="type" name="type" value="">
	</form>
	<script language="javascript">
		createTree_org();
	</script>


</body>
</html:html>
