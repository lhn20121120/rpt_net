<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config"/>
<html:html locale="true">
	<head>
	<html:base/>
    <title>�����趨</title>    
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
    <link href="../../css/common.css" type="text/css" rel="stylesheet">
    <script src="../../js/Org_Tree_for_xml.js"></script>
 <style rel="STYLESHEET" type="text/css">
			.defaultTreeTable{margin : 0;padding : 0;border : 0;}
			.containerTableStyle { overflow : auto;}
			.standartTreeRow{	font-family : Verdana, Geneva, Arial, Helvetica, sans-serif; 	font-size : 14px; -moz-user-select: none; }
			.selectedTreeRow{ background-color : navy; color:white; font-family : Verdana, Geneva, Arial, Helvetica, sans-serif; 		font-size : 14x;  -moz-user-select: none;  }
			.standartTreeImage{ width:14x; height:1px;  overflow:hidden; border:0; padding:0; margin:0; }			
			.hiddenRow { width:1px;   overflow:hidden;  }
			.dragSpanDiv{ 	font-size : 12px; 	border: thin solid 1 1 1 1; }
	</style>
  <% 
     String orgIds=(String)request.getAttribute("orgIds");
     
   %>
	<script language="javascript">				
		var orgIdStr="<%=orgIds%>";    //�ӻ���ID��
		//��ʾ��
		function createTree()
		{
			tree=new dhtmlXTreeObject("treeObj","100%","100%",0);
			tree.setImagePath("../../image/treeImgs/");
			tree.enableCheckBoxes(1);
			tree.enableThreeStateCheckboxes(true);
		//	tree.setOnDblClickHandler(treeOnDBClick);
			tree.loadXML("../../xml/orgInfo.xml");
			
			
		}
		
		//���ڵ�˫���¼� ȡ�û���ID
		function treeOnDBClick(id)
		{
			var OrgID=id;	
			var orgIds="<%=orgIds%>";    //�ӻ���ID��
			var regionId="*!*";
			var index=OrgID.indexOf(regionId);
			if(index>=0){
				alert("�Բ���!�����ܲ�������!");
				return ;
			}
			var index=orgIds.indexOf(OrgID);
			if(OrgID!="")
			{
			 if(confirm("�Ƿ�Ҫ�޸ĸû���?")){
				 if(index<0){
					alert("�Բ���!��û��Ȩ���޸ĸû���!");
					return ;
				}else{
				 	window.location="<%=request.getContextPath()%>/editOrgNet.do?org_id="+ OrgID;
			 	}
			 }
					
			}	
		}	
		//�Ƿ�ѡ���˵���
        function isRegion(orgId){
       		
        }
		//��������
		function goAddPage(){
	    	window.location.href="<%=request.getContextPath()%>/netOrg/newAddOrg/newAddFrame.jsp?flag=newAdd";
	    }
	    //�޸Ļ���
	    function updatePage(){
	  	    var  orgId=tree.getAllChecked();   //ȡ��ѡ�еĻ���ID
            var regionId="*!*";
			var index=orgId.indexOf(regionId);
			if(index>=0){
				alert("�Բ���!�����ܲ�������!");
				return ;
			}			
	        var isOneId=orgId.indexOf(",!,");  //�ж��Ƿ�ѡ���˶������
	        if(orgId.length<=0 || isOneId>=0){
		    	alert("��ѡ��һ���������в���!")
		    	return ;
		    }else{
				var index=orgIdStr.indexOf(orgId);
				if(index<0){
					alert("�Բ���!��û��Ȩ���޸ĸû���!");
					return ;
				}else if(confirm("�Ƿ�Ҫ�޸ĸû���?")){
					 window.location="<%=request.getContextPath()%>/editOrgNet.do?org_id="+ orgId;
				}
			}
	    }
	    //ɾ������
	    function deletePage(){
		    var  orgIds=tree.getAllChecked();   //ȡ��ѡ�еĻ���ID
		     var regionId="*!*";
			var index=orgIds.indexOf(regionId);
			if(index>=0){
				alert("�Բ���!�����ܲ�������!");
				return ;
			}
	        var isOneId=orgIds.indexOf(",!,");  //�ж��Ƿ�ѡ���˶������
	        if(orgIds.length<=0 || isOneId>=0){
		    	alert("��ѡ��һ���������в���!")
		    	return ;
		    }else if(tree.hasChildren(orgIds)>0){  //�Ƿ����¼�����
	    		alert("��ǰ����������һ���ӻ���������ɾ��!!!");
	    		return ;
	    	}else{
				var index=orgIdStr.indexOf(orgIds);
				if(index<0){
					alert("�Բ���!��û��Ȩ��ɾ���û���!");
					return ;
				 }else if(confirm("��ȷ��Ҫɾ���û�����")){
					 window.location="<%=request.getContextPath()%>/deleteOrgNet.do?org_id="+ orgIds;
				 }
			}
	    }
	    //��ӱ��ͷ�Χ
	    function AddBSFW(){
	       var  orgId=tree.getAllChecked();   //ȡ��ѡ�еĻ���ID
            var regionId="*!*";
			var index=orgId.indexOf(regionId);   
			if(index>=0){
				alert("�Բ���!�����ܲ�������!");
				return ;
			}
	        var isOneId=orgId.indexOf(",!,");  //�ж��Ƿ�ѡ���˶������
	        if(orgId.length<=0 || isOneId>=0){
		    	alert("��ѡ��һ���������в���!")
		    	return ;
		    }else{
				var index=orgIdStr.indexOf(orgId);  // �ж��Ƿ��Ǹû����������ӻ��� 
				if(index<0){
					alert("�Բ���!��û��Ȩ�޲����û���!");
					return ;
				}else  if(confirm("�Ƿ�Ҫ���û����趨���ͷ�Χ?")){
					 window.location="<%=request.getContextPath()%>/showOrgBSFW.do?org_id="+ orgId;
				}
			}
	    }
	     //ETL����
	    function ETLOrg(){
	       var  orgId=tree.getAllChecked();   //ȡ��ѡ�еĻ���ID
            var regionId="*!*";
			var index=orgId.indexOf(regionId);   
			if(index>=0){
				alert("�Բ���!�����ܲ�������!");
				return ;
			}
	        var isOneId=orgId.indexOf(",!,");  //�ж��Ƿ�ѡ���˶������
	        if(orgId.length<=0 || isOneId>=0){
		    	alert("��ѡ��һ���������в���!")
		    	return ;
		    }else{
				var index=orgIdStr.indexOf(orgId);  // �ж��Ƿ��Ǹû����������ӻ��� 
				if(index<0){
					alert("�Բ���!��û��Ȩ�޲����û���!");
					return ;
				}else {
					 window.location="<%=request.getContextPath()%>/netOrg/orgNet/ETLOrg.jsp?orgId="+ orgId;
				}
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
    <table border="0" width="90%" align="center">
		<tr><td height="8"></td></tr>
		<tr>
			 <td>��ǰλ�� >> 	ϵͳ���� >> �����趨</td>
		</tr>
		<tr><td height="10"></td></tr>
	</table>
	
	<form action="#" method="post" styleId="frmSel">
		<table cellSpacing="1" cellPadding="4" width="96%" border="0" align="center" bgcolor="#ffffff" class="tbcolor">
			<TR class="tbcolor1">
				<th align="center" height="30" colspan="2">
					<span style="FONT-SIZE: 11pt"> ������Ϣ</span>
				</th>
			</TR>
			<tr>
				<td colspan="2" align="left" bgcolor="#EEEEEE">
					������Ϣ��:
				</td>
			</tr>
			<tr bgcolor="#ffffff">
				<td valign="top" colspan="2">
					<div id="treeObj" style="width:100%; height:300;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;"></div>
				</td>
			</tr>
		
		</table>
		<table>
			<tr>
				<td>
					<input type="button" class="input-button" value="��������" onclick="goAddPage()" />
					&nbsp;&nbsp;
					<input type="button" class="input-button" value="�޸Ļ���" onclick="updatePage()" />
					&nbsp;&nbsp;
					<input type="button" class="input-button" value="ɾ������" onclick="deletePage()" />
					&nbsp;&nbsp;
					<input type="button" class="input-button" value="�趨���ͷ�Χ" onclick="AddBSFW()" />
					<%if(com.cbrc.smis.common.Config.BANK_NAME.equals("CZBANK")){ %>
					&nbsp;&nbsp;
					<input type="button" class="input-button" value="�趨ETL���ݷ�Χ" onclick="ETLOrg()" />
					<%} %>
				</td>
			</tr>
		</table>
	</form>
		<script language="javascript">	
			createTree();
		</script>
	
	
  </body>
</html:html>