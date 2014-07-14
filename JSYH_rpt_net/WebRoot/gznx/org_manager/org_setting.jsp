<%@ page language="java" pageEncoding="GBK"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base />
	<title>��������</title>
	<%
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
	%>
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link href="../../css/globalStyle.css" type="text/css" rel="stylesheet">
	<script src="../../js/Tree_for_xml.js"></script>
	<script language="javascript">
		//��ʾ��
			function createTree_org()
			{
				var fileName ="<logic:present name="FileName" scope="request"><bean:write name="FileName"/></logic:present>";
				tree=new dhtmlXTreeObject("treeObj_org","100%","100%",0);
				tree.setImagePath("../../image/treeImgs/");
				tree.enableCheckBoxes(1);
				tree.loadXML("../../tree_xml/org_tree/"+fileName);
			}
			
			//ȡ��ѡ�еĻ�������Ŀ
			function getOrgNum(){
				var checkId=tree.getAllChecked();
				
				if(checkId==null||checkId==''){
				return 0;
				}
				
				var arrOrg = checkId.split(",");
				return arrOrg.length;
			}
			
			//���ӻ���
			function addOrg(){
				<%--
				���û��ѡ���κλ����������ӵĻ���Ϊ���ڵ�
				���ѡ����һ�������������ӵĻ���Ϊѡ��������ӻ���
				--%>
				if(getOrgNum()==0){
					if(confirm('ȷ���������������')){
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
					alert('���ӻ���ʱ��ѡ��Ļ�����Ŀ���ܴ���1��');
				}
			}
			
			//ɾ������
			function deleteOrg(){
				if(getOrgNum()==0){
					alert("��ѡ����Ҫɾ���Ļ�����");
				}else if(getOrgNum()>1){
					alert("ÿ��ֻ��ɾ��һ��������");
				}else{
					if(confirm('ȷ��ɾ��������')){
						document.getElementById('orgInfoForm').action="../../system_mgr/OrgInfo/delete.do";
						document.getElementById('orgId').value=tree.getAllChecked();
						document.getElementById('orgInfoForm').submit();
					}
				}
			}
			//�鿴����
			function viewOrg(){
				if(getOrgNum()==0){
					alert("��ѡ����Ҫ�鿴�Ļ�����");
				}else if(getOrgNum()>1){
					alert("ÿ��ֻ�ܲ鿴һ��������");
				}else{
					document.getElementById('orgInfoForm').action="../../system_mgr/OrgInfo/beforeEdit.do?orgId="+tree.getAllChecked();
					document.getElementById('orgId').value=tree.getAllChecked();
					document.getElementById('type').value="detail";
					document.getElementById('orgInfoForm').submit();
				}
			}
			//�޸Ļ���
			function updateOrg(){
				if(getOrgNum()==0){
					alert("��ѡ����Ҫ�޸ĵĻ�����");
				}else if(getOrgNum()>1){
					alert("ÿ��ֻ���޸�һ��������");
				}else{
					document.getElementById('orgInfoForm').action="../../system_mgr/OrgInfo/beforeEdit.do";
					document.getElementById('orgId').value=tree.getAllChecked();
					document.getElementById('type').value="update";
					document.getElementById('orgInfoForm').submit();
				}
			}
			//��Ϊһ���ڵ�
			function setFirstNode(){
				if(getOrgNum()==0){
					alert("��ѡ����Ҫ���õĻ�����");
				}else if(getOrgNum()>1){
					alert("ÿ��ֻ������һ��������");
				}else{
					document.getElementById('orgInfoForm').action="../../system_mgr/OrgInfo/setFirstNode.do";
					document.getElementById('orgId').value=tree.getAllChecked();
					document.getElementById('orgInfoForm').submit();
				}
			}
			//���ͷ�Χ
			function _range(){
				if(getOrgNum()==0){
					alert("��ѡ����Ҫ�޸ĵĻ�����");
				}else if(getOrgNum()>1){
					alert("ÿ��ֻ���޸�һ��������");
				}else{	
					window.location="<%=request.getContextPath()%>/showOrgBSFW.do?org_id="+ tree.getAllChecked();
				}
			}

			//����ӳ���ϵ
			function setOrgRel(){
				if(getOrgNum()==0){
					alert("��ѡ����Ҫ���õĻ�����");
				}else if(getOrgNum()>1){
					alert("ÿ��ֻ������һ��������");
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
				��ǰλ�� &gt;&gt; ϵͳ���� &gt;&gt; ��������
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
		<input type="button" name="addOrg" value="�鿴����" Class="input-button"
			onclick="viewOrg();" />
		&nbsp;&nbsp;
		<input type="button" name="addOrg" value="���ӻ���" Class="input-button"
			onclick="addOrg();" />
		&nbsp;&nbsp;
		<input type="button" name="addOrg" value="�޸Ļ���" Class="input-button"
			onclick="updateOrg();" />
		&nbsp;&nbsp;
		<input type="button" name="addOrg" value="ɾ������" Class="input-button"
			onclick="deleteOrg();" />
		<!-- 
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" name="addOrg" value="�����������" Class="input-button"
			onclick="setFirstNode();" />
		&nbsp;&nbsp;
		 -->
		 &nbsp;&nbsp;
		 <input type="button"   class="input-button" value="����Χ" 
		 	onclick="_range()">
		 &nbsp;&nbsp;
		 <input type="button" class="input-button"  value="����ӳ���ϵ"  onclick="setOrgRel()"/>
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
