<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config"/>
<html:html locale="true">
	<head>
	<html:base/>
    <title>机构设定</title>    
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
		var orgIdStr="<%=orgIds%>";    //子机构ID串
		//显示树
		function createTree()
		{
			tree=new dhtmlXTreeObject("treeObj","100%","100%",0);
			tree.setImagePath("../../image/treeImgs/");
			tree.enableCheckBoxes(1);
			tree.enableThreeStateCheckboxes(true);
		//	tree.setOnDblClickHandler(treeOnDBClick);
			tree.loadXML("../../xml/orgInfo.xml");
			
			
		}
		
		//树节点双击事件 取得机构ID
		function treeOnDBClick(id)
		{
			var OrgID=id;	
			var orgIds="<%=orgIds%>";    //子机构ID串
			var regionId="*!*";
			var index=OrgID.indexOf(regionId);
			if(index>=0){
				alert("对不起!您不能操作地区!");
				return ;
			}
			var index=orgIds.indexOf(OrgID);
			if(OrgID!="")
			{
			 if(confirm("是否要修改该机构?")){
				 if(index<0){
					alert("对不起!您没有权限修改该机构!");
					return ;
				}else{
				 	window.location="<%=request.getContextPath()%>/editOrgNet.do?org_id="+ OrgID;
			 	}
			 }
					
			}	
		}	
		//是否选中了地区
        function isRegion(orgId){
       		
        }
		//新增机构
		function goAddPage(){
	    	window.location.href="<%=request.getContextPath()%>/netOrg/newAddOrg/newAddFrame.jsp?flag=newAdd";
	    }
	    //修改机构
	    function updatePage(){
	  	    var  orgId=tree.getAllChecked();   //取得选中的机构ID
            var regionId="*!*";
			var index=orgId.indexOf(regionId);
			if(index>=0){
				alert("对不起!您不能操作地区!");
				return ;
			}			
	        var isOneId=orgId.indexOf(",!,");  //判断是否选择了多个机构
	        if(orgId.length<=0 || isOneId>=0){
		    	alert("请选择一个机构进行操作!")
		    	return ;
		    }else{
				var index=orgIdStr.indexOf(orgId);
				if(index<0){
					alert("对不起!您没有权限修改该机构!");
					return ;
				}else if(confirm("是否要修改该机构?")){
					 window.location="<%=request.getContextPath()%>/editOrgNet.do?org_id="+ orgId;
				}
			}
	    }
	    //删除机构
	    function deletePage(){
		    var  orgIds=tree.getAllChecked();   //取得选中的机构ID
		     var regionId="*!*";
			var index=orgIds.indexOf(regionId);
			if(index>=0){
				alert("对不起!您不能操作地区!");
				return ;
			}
	        var isOneId=orgIds.indexOf(",!,");  //判断是否选择了多个机构
	        if(orgIds.length<=0 || isOneId>=0){
		    	alert("请选择一个机构进行操作!")
		    	return ;
		    }else if(tree.hasChildren(orgIds)>0){  //是否有下级机构
	    		alert("当前机构存在下一级子机构，不能删除!!!");
	    		return ;
	    	}else{
				var index=orgIdStr.indexOf(orgIds);
				if(index<0){
					alert("对不起!您没有权限删除该机构!");
					return ;
				 }else if(confirm("你确认要删除该机构吗？")){
					 window.location="<%=request.getContextPath()%>/deleteOrgNet.do?org_id="+ orgIds;
				 }
			}
	    }
	    //添加报送范围
	    function AddBSFW(){
	       var  orgId=tree.getAllChecked();   //取得选中的机构ID
            var regionId="*!*";
			var index=orgId.indexOf(regionId);   
			if(index>=0){
				alert("对不起!您不能操作地区!");
				return ;
			}
	        var isOneId=orgId.indexOf(",!,");  //判断是否选择了多个机构
	        if(orgId.length<=0 || isOneId>=0){
		    	alert("请选择一个机构进行操作!")
		    	return ;
		    }else{
				var index=orgIdStr.indexOf(orgId);  // 判断是否是该机构创建的子机构 
				if(index<0){
					alert("对不起!您没有权限操作该机构!");
					return ;
				}else  if(confirm("是否要给该机构设定报送范围?")){
					 window.location="<%=request.getContextPath()%>/showOrgBSFW.do?org_id="+ orgId;
				}
			}
	    }
	     //ETL机构
	    function ETLOrg(){
	       var  orgId=tree.getAllChecked();   //取得选中的机构ID
            var regionId="*!*";
			var index=orgId.indexOf(regionId);   
			if(index>=0){
				alert("对不起!您不能操作地区!");
				return ;
			}
	        var isOneId=orgId.indexOf(",!,");  //判断是否选择了多个机构
	        if(orgId.length<=0 || isOneId>=0){
		    	alert("请选择一个机构进行操作!")
		    	return ;
		    }else{
				var index=orgIdStr.indexOf(orgId);  // 判断是否是该机构创建的子机构 
				if(index<0){
					alert("对不起!您没有权限操作该机构!");
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
			 <td>当前位置 >> 	系统管理 >> 机构设定</td>
		</tr>
		<tr><td height="10"></td></tr>
	</table>
	
	<form action="#" method="post" styleId="frmSel">
		<table cellSpacing="1" cellPadding="4" width="96%" border="0" align="center" bgcolor="#ffffff" class="tbcolor">
			<TR class="tbcolor1">
				<th align="center" height="30" colspan="2">
					<span style="FONT-SIZE: 11pt"> 机构信息</span>
				</th>
			</TR>
			<tr>
				<td colspan="2" align="left" bgcolor="#EEEEEE">
					机构信息树:
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
					<input type="button" class="input-button" value="新增机构" onclick="goAddPage()" />
					&nbsp;&nbsp;
					<input type="button" class="input-button" value="修改机构" onclick="updatePage()" />
					&nbsp;&nbsp;
					<input type="button" class="input-button" value="删除机构" onclick="deletePage()" />
					&nbsp;&nbsp;
					<input type="button" class="input-button" value="设定报送范围" onclick="AddBSFW()" />
					<%if(com.cbrc.smis.common.Config.BANK_NAME.equals("CZBANK")){ %>
					&nbsp;&nbsp;
					<input type="button" class="input-button" value="设定ETL数据范围" onclick="ETLOrg()" />
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