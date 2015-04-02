<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page import="com.fitech.model.worktask.security.Operator"%>
<%@page import="com.fitech.model.worktask.common.WorkTaskConfig"%>
<%@page import="com.fitech.framework.core.common.Config"%>
<%
String path = request.getContextPath();

String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
Operator operator = null;
if (session.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME) != null)
	operator = (Operator) session
			.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
String 	userId= operator.getOperatorId()+"";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <meta http-equiv="content-type" content="text/html; charset=utf-8" />

	
    <link rel="stylesheet" type="text/css" href="<%=basePath%>scripts/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="<%=basePath%>scripts/themes/icon.css"/>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>scripts/demo.css"/>
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.4.2.js"></script>
	<script type="text/javascript" src="<%=basePath%>scripts/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>scripts/easyui-lang-zh_CN.js"></script>
	
	
	
		    <link href="css/index.css" rel="stylesheet" type="text/css" />
<link href="css/common0.css" rel="stylesheet" type="text/css" />
<link href="css/common.css" rel="stylesheet" type="text/css" />
<link href="css/table.css" rel="stylesheet" type="text/css" />
<link href="css/globalStyle.css" rel="stylesheet"
	type="text/css" />
<link href="css/thd.css" rel="stylesheet" type="text/css" />
<link href="css/animate/theme/jquery-ui-1.8.11.redmond.css"
	rel="stylesheet" />
	
	 <link rel="stylesheet" type="text/css" href="scripts/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="scripts/themes/icon.css"/>
	<link rel="stylesheet" type="text/css" href="scripts/demo.css"/>
	<script type="text/javascript" src="js/jquery-1.4.2.js"></script>
	<script type="text/javascript" src="scripts/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="scripts/easyui-lang-zh_CN.js"></script>
    
    <title>流程节点新增</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
		<style type="text/css">
		
			#addNode{
				position:absolute;
			    top:150;
			    left:300;
		    	
	    		
<%--				border:1px solid #008800;--%>
			}
			#nodeName{
			
				position:absolute;
				left:300;
				top:100;
			}
			
			td , label{
				
				font-family:Arial;
				font-size:13px;
				
				font-style:normal;
				text-decoration:none
			}
<%--		   legend{
		   
		   		font-family:Arial;
				font-size:15px;
				
				font-style:normal;
				text-decoration:none
		   }--%>
		
			.button{
				padding:5px 0;   
				width:80;
				height:10;
				border:1 solid;
<%--				background: url('images/main/input_backimage.png') no-repeat;--%>
				cursor:pointer;
			
			}
<%--			#templateDiv{
			
				 
				background-color:#FFFFFF; 
				height: 230px ;
				overflow:auto;
				margin-left: 10 
				;margin-right: 10 ;
				margin-bottom: 10 ;
				BORDER-BOTTOM-STYLE: groove; 
				BORDER-RIGHT-STYLE: groove; 
				WIDTH: 220px; 
				BORDER-TOP-STYLE: groove; 
				
			
			}
			#orgDiv{
<%--				background-color:#FFFFFF;  
				overflow:auto;
				height: 230px ;
			
				margin-left: 10 
				;margin-right: 10 ;
				margin-bottom: 10 ;
				BORDER-BOTTOM-STYLE: groove; 
				BORDER-RIGHT-STYLE: groove; 
				WIDTH: 220px; 
				BORDER-TOP-STYLE: groove; 
				
			
			}--%>
			select{
			
				width: 150;
				background-color:#FFFFFF; 
			
			}
			font{
			 	color:orange;
				font-family:Arial;
				font-size:13px;
				font-style:normal;
				text-decoration:none
			}
			
			
		</style>
		<script type="text/javascript">
		var  nodeNames='<s:property value="nodeNamesStr"/>';
		var  nodeNamesArr=nodeNames.split(",");
				$(function(){
		$('#orgTree').tree({
			checkbox: true,
			cascadeCheck:true,
			
			url: '<%=request.getContextPath()%>/json/edit_org_tree_data_<%=userId%>.json',
			
			onClick:function(node){
				$(this).tree('toggle', node.target);
			},
			onContextMenu: function(e, node){
				e.preventDefault();
				$('#orgTree').tree('select', node.target);
				//$('#mm').menu('show', {
				//	left: e.pageX,
				//	top: e.pageY
				//});
			}
		});
		
		$('#templateTree').tree({
			checkbox: true,
			cascadeCheck:true,
			
			url: '<%=request.getContextPath()%>/json/edit_template_tree_data_<%=userId%>.json',
			
			onClick:function(node){
				$(this).tree('toggle', node.target);
			},
			onContextMenu: function(e, node){
				e.preventDefault();
				$('#templateTree').tree('select', node.target);
				$('#mm').menu('show', {
					left: e.pageX,
					top: e.pageY
				});
			}
		});

		
	});
		
			//获取机构区选中的机构id
			function getCheckedOrgTree(){
				var nodes = $('#orgTree').tree('getChecked');
				var s = '';
				for(var i=0; i<nodes.length; i++){
					if (s != '') s += ',';
					s += nodes[i].id;
				}
				return s ;
			}
			//获取模板去选中的模板id
			function getCheckedTemplateTree(){
				var nodes = $('#templateTree').tree('getChecked');
				var s = '';
				for(var i=0; i<nodes.length; i++){
					if (s != '') s += ',';
					s += nodes[i].id;
				}
				return s;
			}
		
		//输入验证
			function validateNodeInfo(){
				var flag=false;
				var nodeName=document.getElementById("nodeDefineVo.nodeName").value;
				
				var nodeTime=document.getElementById("nodeDefineVo.nodeTime").value;
				
				var conductType=document.getElementsByName("nodeInfoVo.conductType");
				
				var roleId=document.getElementById("nodeInfoVo.roleId").value;
				
				var reg=/^[0-9]\d*$/;
				if(nodeName==''){
					alert("节点名不能为空");
					flag=true;
					return flag;
				}
				for(var i=0;i<nodeNamesArr.length;i++){
					
					if(nodeName==nodeNamesArr[i]){
						alert("节点名已存在");
						flag=true;
						return flag;
					}
				}
				 if(nodeTime==''){
					alert("报送时间不能为空");
					flag=true;
					return flag;
				}
				 if(reg.test(nodeTime)==false){
					alert("报送时间必须是正整数");
					flag=true;
					return flag;
				}
				  if(nodeTime>=100000){
					alert("报送时间不得大于五位数");
					flag=true;
					return flag;
				}
			 if(conductType!=null){
					var count=0;
					for(var i=0;i<conductType.length;i++){
						if(conductType[i].checked==false){
							count++;
						}
					}
					if(count==conductType.length){
						alert("请选择处理类型");
						flag=true;
						return flag;
					}
					
				}
				if(roleId==-1){
					alert("请选择参与角色");
					flag=true;
					return flag;
				}
				if(getCheckedTemplateTree()==''){
					alert("请选择报表");
					flag=true;
					return flag;
				}
				 if(getCheckedOrgTree()==''){
					alert("请选择机构");
					flag=true;
					return flag;
				}
				 if(getCheckedTemplateTree()!=''){
					 	$.ajax({   
						          type : "post",   
						          url : "/rpt_other/editWorkTaskNodeInfo!isExistTemplate.action",   
								  data: "templateIds="+getCheckedTemplateTree()+"&taskId="+document.getElementById("nodeInfoVo.taskId").value,
						          async : false,   
						          success : function(data){   
										if(data==undefined || parseInt(data)==1){
											alert("所选报表已在其他任务中存在");
											flag=true;
											return flag;
										}
						          }   
					          });
				 }
			
				return flag;
			}
		
		function addNodeInfo(){
			if(validateNodeInfo()){
				return;
			}else{
				document.getElementById("templateIds").value=getCheckedTemplateTree();
				document.getElementById("orgIds").value=getCheckedOrgTree();
				
				document.forms[0].action="<%=request.getContextPath()%>/workTaskNodeDefineAction!saveNodeInfo.action?type=next";
				document.forms[0].submit();
			}
		}
	function addComplete(){
		if(validateNodeInfo()){
				return;
			}else{
				document.getElementById("templateIds").value=getCheckedTemplateTree();
				document.getElementById("orgIds").value=getCheckedOrgTree();
				document.forms[0].action="<%=request.getContextPath()%>/workTaskNodeDefineAction!saveNodeInfo.action?type=complete";
				document.forms[0].submit();
			}
	}
	function backAddNodeInfo(){
			if(validateNodeInfo()){
				return;
			}else{
				document.getElementById("templateIds").value=getCheckedTemplateTree();
				document.getElementById("orgIds").value=getCheckedOrgTree();
				document.forms[0].submit();
			}
		}
	
	function  goBackNodeList(){
		var taskId=document.getElementById("nodeInfoVo.taskId").value;
		window.location="<%=request.getContextPath()%>/workTaskNodeListAction!displayNodeList.action?type=back&taskId="+taskId;
	}
	function init(){
		document.getElementById("nodeDefineVo.nodeName").focus();
	}
		</script>
  </head>
  
 <body  onload="init()" style="  background: url('images/main/body_image.png') ">
	


	<div style="width:100%;margin-top: 0px;height:50px;">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td nowrap="nowrap">
					<div class="toolbar">
						<div class="tableinfo">
							<span><img src="images/icon01.gif" /> </span><span style="font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#EBAD06;">当前位置
								>> 系统管理 >> 任务定义 >> 流程定义</span>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>

	<div id="nodeName">
		<table >
			<tr>
				<td></td>
			</tr>
		</table>
	</div>
	<div  id="addNode" style="width:400;margin-top:-40px;  ">
	 
		<form  action="editWorkTaskNodeInfo!backAddNode.action" method="post" >
			<input type="hidden" name="nodeInfoVo.orgIds" id="orgIds"/>
			<input type="hidden" name="nodeInfoVo.templateIds" id="templateIds"/>
			<fieldset style="width:600px;border: 1 solid  ">
				<legend >流程定义</legend>

				<br />

				<table border="0" cellspacing="0" cellpadding="0"
					style="width: 100%">
					<tr>
						<td align="center" width="100">节点名称</td>
						<td> <span >
						<s:hidden name="nodeInfoVo.nodeId" id="nodeInfoVo.nodeId"/>
						<s:hidden name="nodeInfoVo.preNodeId" id="nodeInfoVo.preNodeId"/>
						<s:hidden name="nodeInfoVo.taskId" id="nodeInfoVo.taskId"  />
						<input style=" background-color:#FFFFFF;"  name="nodeInfoVo.nodeName" id="nodeDefineVo.nodeName"/>  
						 </span></td>
						<td >报送时间</td>
						<td> <span ><input style="background-color:#FFFFFF;"  name="nodeInfoVo.nodeTime"  id="nodeDefineVo.nodeTime" /></span></td>
					</tr>
				</table>

					<table border="0" cellspacing="20" cellpadding="10"
						style="width: 100%">
						<tr>
							<td width="50%">
								<fieldset style="width: 95%;height: 250px;align:center;  ">
									<legend>机构区</legend>
									</br>
										<div id="orgDiv" iconCls="icon-reload" split="true"  title="机构" style="width: 89%;height: 230px ;background: ; margin-left: 25 ;margin-bottom: 10 ;OVERFLOW: auto;">
										<ul class="easyui-tree" id="orgTree"></ul>
									
									</div>

								</fieldset></td>
							<td width="50%">
								<fieldset style="width: 95%;height: 250px ;align:center;">
									<legend>报表区</legend>
									</br>
									<div id="templateDiv" iconCls="icon-reload" split="true" style="width: 89%;height: 230px ;background: ; margin-left: 25 ;margin-bottom: 10 ;OVERFLOW: auto;">
										<ul class="easyui-tree" id="templateTree" ></ul>
									</div>
								</fieldset>
							</td>
						</tr>
						<tr>
							<td colspan="2" height="50px">
								<fieldset style="width: 100%; height:70px;">
									<legend>处理类型</legend>
									</br>
									<table style="width: 100%;">
										<tr>
										<td align="center"><input type="radio" name="nodeInfoVo.conductType"  value="fill" id="fill">
												<label for="fill">&nbsp; &nbsp;填 &nbsp; &nbsp;报 &nbsp; &nbsp;</label><img
												src="images/bianji.jpg" align="center"></img>
											</td>

											<td align="center"><input type="radio" name="nodeInfoVo.conductType" value="check" id="check">
												<label for="check">&nbsp; &nbsp;复 &nbsp; &nbsp;核 &nbsp; &nbsp;</label><img
												src="images/main/u57_normal.gif" align="center"></img>
											</td>
											</td>
										</tr>


									</table>
								</fieldset></td>
						</tr>
					</table>
	<table border="0" cellspacing="20" cellpadding="10"
						style="width: 100%">
			<tr>
			<td width="50%" height="50">
				<fieldset style="width:550px;height:70px;" align="center" >
					<legend>参与角色</legend>
					</br>&nbsp; &nbsp; &nbsp; &nbsp; <label>角色列表</label> ：<img src="images/m5.gif"></img>
						<s:if test="roleList!=null&&roleList.size()>0">
								<select name="nodeInfoVo.roleId" id="nodeInfoVo.roleId">
									<option  value="-1">-----请选择角色-----</option>
							<s:iterator id="r" value="roleList">
									<option  value='<s:property value="#r.roleId"/>' ><s:property value="#r.roleName"/></option>
									
							</s:iterator>
								</select>
						
						</s:if>
						<s:else>
							
					     <select name="nodeInfoVo.roleId" id="nodeInfoVo.roleId">
							
							<option  value="-1">-----请选择角色-----</option>
						
					     </select>
						</s:else>
					
					</br>
				</fieldset>

			</td>
				</tr>
</table>
	<table border="0" cellspacing="20" cellpadding="10"
						style="width: 100%">
			<tr>
			<td width="50%" height="50">
				<fieldset style="width:550px;height:70px;" align="center" >
					<legend>查关联任务设置</legend>
					</br>&nbsp; &nbsp; &nbsp; &nbsp; <label>选择任务</label> ：<img src="images/icons/document-library.png"></img>
					
					<s:if test="relationTaskList!=null&&relationTaskList.size()>0">
								<select name="nodeInfoVo.relationTaskId" id="nodeInfoVo.relationTaskId">
									<option  value="-1">---请选择关联任务---</option>
							<s:iterator id="r" value="relationTaskList">
									<option  value='<s:property value="#r.taskId"/>' ><s:property value="#r.taskName"/></option>
									
							</s:iterator>
								</select>
						
						</s:if>
						<s:else>
							
					     <select name="nodeInfoVo.relationTaskId" id="nodeInfoVo.relationTaskId">
							
							<option  value="-1">---请选择关联任务---</option>
						
					     </select>
						</s:else>
					 </br></br>
					<font>&nbsp; &nbsp; &nbsp; &nbsp;！这里选择的关联任务将以子任务的形式显示出现在待处理任务下方</font>
					</br></br>
				</fieldset>
			</td>
				</tr>
</table>
				<table >
				<tr>
					<td width="250" align="center" valign="middle">
						<span class="button" onclick="backAddNodeInfo()">完    成</span></td>
					<td width="250" align="center" valign="middle">
						<span class="button" onclick="goBackNodeList()">返    回</span></td>
				</tr>
				</table>
				
			<br/>
			<br/>
		
</fieldset>
</from>
	</div>
</body>
</html>
