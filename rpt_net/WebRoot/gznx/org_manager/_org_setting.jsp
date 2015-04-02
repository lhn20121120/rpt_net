<%@ page language="java" import="java.util.*" pageEncoding="GB2312"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ page import="com.cbrc.smis.common.Config,java.util.List,org.apache.struts.util.LabelValueBean"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <html:base/>
    
    <title>��������</title>
    
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" type="text/css" href="../css/pageControl.css" />
	<script type="text/javascript" src="../../js/prototype-1.4.0.js"></script>

	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link href="../../css/tree.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="../../js/tree/tree.js"></script>
	<script type="text/javascript" src="../../js/tree/defTreeFormat.js"></script>
	<link href="../../css/globalStyle.css" rel="stylesheet" type="text/css">

	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<script src="../../js/Tree_for_xml.js"></script>

	
	<%
		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
			operator = (Operator) session
			.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		else
			operator = new Operator();
		// String orgTreePath = operator.getOrgTreeByUserWithOrgPath();
	%>
	
		<script language="javascript">

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
					if(confirm('ȷ������һ���ڵ���')){
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
					document.getElementById('orgInfoForm').action="../../system_mgr/OrgInfo/beforeEdit.do";
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
	</script>
  </head>
  
  <body>
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
					style="width:100%; height:300;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;">
				<script type="text/javascript">
						<bean:write  name="QueryForm"  property="orgTreeContent" filter="false"/>
					    var tree= new ListTree("tree", TREE1_NODES,DEF_TREE_FORMAT,"orgGetId");
				      	tree.init();
				</script>
				</div>
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
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" name="addOrg" value="��Ϊһ���ڵ�" Class="input-button"
			onclick="setFirstNode();" />
		&nbsp;&nbsp;
	</div>
	<br />
	<br />
	<form name="orgInfoForm" method="post" action="" id="orgInfoForm">
		<input type="hidden" id="orgId" name="orgId" value="">
		<input type="hidden" id="parentOrgId" name="parentOrgId" value="">
		<input type="hidden" id="type" name="type" value="">
	</form>
	
  </body>
</html>
