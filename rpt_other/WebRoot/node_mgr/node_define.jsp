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
	<script language="javascript" src="scripts/progressBar.js"></script>
    
    
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
	<%--		#templateDiv{
			
				 
				 
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
		var  progressBar=new ProgressBar("正在跳转，请稍后........");
		var  nodeNames='<s:property value="nodeNamesStr"/>';
		var  nodeNamesArr=nodeNames.split(",");
				$(function(){
				
		$('#orgTree').tree({
			checkbox: true,
			cascadeCheck:false,
	
			url: '<%=request.getContextPath()%>/json/<s:property value="orgTreeFileName"/>',
			onLoadSuccess:function(node,data){
				<s:if test="nodeDefineVo!=null&&#session.nodeList.size>=1">
					removeClick();
				</s:if>
			},
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
			cascadeCheck:false,
			
			url: '<%=request.getContextPath()%>/json/<s:property value="templateTreeFileName"/>',
			onLoadSuccess:function(node,data){
				<s:if test="nodeDefineVo!=null&&#session.nodeList.size>=1">
					removeClick();
				</s:if>
			},
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
	<s:if test="nodeDefineVo!=null">
		document.getElementById("nodeDefineVo.nodeName").select();
	</s:if>
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
				
				var conductType=document.getElementsByName("nodeDefineVo.conductType");
				
				var roleId=document.getElementById("nodeDefineVo.roleId").value;
				
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
				}if(getCheckedTemplateTree()!=''){
					 	$.ajax({   
						          type: "post",   
						          url: "/rpt_other/workTaskNodeDefineAction!isExistTemplate.action",   
								  data: "templateIds="+getCheckedTemplateTree()+"&orgIds="+getCheckedOrgTree(),
						          async:false,   
						          success: function(data){ 
					 					var param=data.split('=');
										if(parseInt(param[0])==1){
											alert("在【"+param[3]+"】任务中已为【"+param[1]+"】分配了报表【"+param[2]+"】");
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
				progressBar.show();
			}
		}
	function addComplete(){
		if(validateNodeInfo()){
				return;
			}else{
				document.getElementById("templateIds").value=getCheckedTemplateTree();
				document.getElementById("orgIds").value=getCheckedOrgTree();
				document.forms[0].action="<%=request.getContextPath()%>/workTaskNodeDefineAction!saveNodeInfo.action?type=complete";
				progressBar.show();
				document.forms[0].submit();
			}
	}
	//跳转到角色管理页面
	function roleManager(){
		
		window.location="/rpt_net/popedom_mgr/viewRole.do";
	}
	function initData(){
		var radioArry=document.getElementsByName("nodeDefineVo.conductType");
		
		for(var i=0;i<radioArry.length;i++){
			
			if('<s:property value="nodeDefineVo.conductType"/>'==radioArry[i].value){
				radioArry[i].checked=true;
			}
	
		}
		document.getElementById("nodeDefineVo.nodeName").focus();
	}
	//如果增加节点失败，则退出 ,清空session中存储的节点信息 
	function quit(){
		window.location="<%=request.getContextPath()%>/workTaskNodeDefineAction!quit.action";
	}
	function getAllNode(){
		var node = $('#orgTree').tree('getRoot');
	}
	
	function back(){
		window.location="<%=request.getContextPath()%>/workTaskNodeDefineAction!back.action";
	}
	
	function removeClick(){
		$('.tree-checkbox0').unbind("click");
		$('.tree-checkbox1').unbind("click");
		$('.tree-checkbox2').unbind("click");
	}
	function backspace(evt){
		
		evt=evt?evt:window.event;
		if((evt.keyCode==8&&evt.srcElement.tagName=="INPUT"&&evt.srcElement.type=="text"&&evt.srcElement.readOnly==true)||(evt.keyCode==8&&evt.srcElement.tagName!="INPUT"&&evt.srcElement.type!="text")){
			evt.returnValue=false;
		}
	}
		</script>
  </head>
  
 <body  onkeydown="backspace(event)" style="  background: url('images/main/body_image.png') " >
	<div style="width:100%;margin-top: 0px;height:50px;">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td nowrap="nowrap">
					<div class="toolbar">
						<div class="tableinfo">
							<span><img src="images/icon01.gif" /> </span><span style="font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#EBAD06;">当前位置
								>> 报表处理 >> 任务定制 >> 流程定义</span>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div id="nodeName" style="margin-top: -40px;height:100px;">
		<table >
			<tr>
			<s:set name="count" value="nodeNameList.size"></s:set>
			<s:if test="nodeNameList!=null&&nodeNameList.size()>0"   >
				<s:iterator id="n" value="nodeNameList" status="s"   >
					<td >
						<span align="center" style="font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color: red">[<s:property value="#n.nodeName" />]</span>
					</td>	
					<s:if test="(#count-#s.index-1)>0">
							<td>&nbsp;&nbsp;<img alt="" src="images/pright.gif">&nbsp;&nbsp;</td>
					
					</s:if>
					<s:if test="#s.index>0&&(#s.index%5)==0">
					</tr><tr>
				</s:if>		
				</s:iterator>
			</s:if>
			</tr>
		</table>
	</div>
	<div  id="addNode" style="width:400;margin-top:-40px;  ">
	 
		<form  action="workTaskNodeDefineAction!saveNodeInfo.action" method="post" >
			<input type="hidden" name="nodeDefineVo.orgIds" id="orgIds"/>
			<input type="hidden" name="nodeDefineVo.templateIds" id="templateIds"/>
			<fieldset style="width:600px; ">
				<legend >流程定义</legend>

				<br />

				<table border="0" cellspacing="0" cellpadding="0"
					style="width: 100%">
					<tr>
						<td align="center" width="100">节点名称</td>
						<td> <span >
						<input type="hidden" name="nodeDefineVo.nodeId"/>
						<input type="hidden" name="nodeDefineVo.taskId" id="nodeDefineVo.taskId"  value="<s:property value='#session.nodeTaskIdFlow'/>">
<%--						<input style=" background-color:#FFFFFF;"  name="nodeDefineVo.nodeName" id="nodeDefineVo.nodeName"/>  --%>
							<s:textfield cssStyle="background-color:#FFFFFF;" name="nodeDefineVo.nodeName" id="nodeDefineVo.nodeName"></s:textfield>
						 </span></td>
						<td >报送时间</td>
						<td> <span >
<%--						<input style="background-color:#FFFFFF;"  name="nodeDefineVo.nodeTime"  id="nodeDefineVo.nodeTime" />--%>
						<s:if test="nodeDefineVo!=null && nodeDefineVo.nodeTime!=null&&#session.nodeList.size>=1">
							<!--<s:textfield  cssStyle="background-color:#FFFFFF;" name="nodeDefineVo.nodeTime" id="nodeDefineVo.nodeTime"></s:textfield>-->
							<input readonly="readonly" name="nodeDefineVo.nodeTime" id="nodeDefineVo.nodeTime" value="<s:property value="nodeDefineVo.nodeTime"/>"/>
						</s:if>
						<s:else>
							<input name="nodeDefineVo.nodeTime" id="nodeDefineVo.nodeTime" value="<s:property value="nodeDefineVo.nodeTime"/>"/>
							<!--<s:textfield cssStyle="background-color:#FFFFFF;" name="nodeDefineVo.nodeTime" id="nodeDefineVo.nodeTime"></s:textfield>-->
						</s:else>
						</span></td>
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
								<fieldset style="width: 95%;height: 250px ;align:center; ">
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
								<fieldset style="width: 100%; height:70px; ">
									<legend>处理类型</legend>
									</br>
									<table style="width: 100%">
										<tr>
										<td align="center"><input type="radio" name="nodeDefineVo.conductType"  value="fill" id="fill">
								<label for="fill">&nbsp; &nbsp;填 &nbsp; &nbsp;报 &nbsp; &nbsp;</label><img
												src="images/bianji.jpg" align="center"></img>
											</td>

											<td align="center"><input type="radio" name="nodeDefineVo.conductType" value="check" id="check">
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
			<td width="50%" height="50px">
				<fieldset style="width:550px;height:70px" align="center" >
					<legend>参与角色</legend>
					</br>&nbsp; &nbsp; &nbsp; &nbsp; <label>角色列表</label> ：<img src="images/m5.gif"></img>
						<%--<s:if test="roleList!=null&&roleList.size()>0">
								<select name="nodeDefineVo.roleId" id="nodeDefineVo.roleId">
									<option  value="-1">-----请选择角色-----</option>
							<s:iterator id="r" value="roleList">
									<option  value='<s:property value="#r.roleId"/>' ><s:property value="#r.roleName"/></option>
									
							</s:iterator>
								</select>
						
						</s:if>
						<s:else>
							
					     <select name="nodeDefineVo.roleId" id="nodeDefineVo.roleId">
							
							<option  value="-1">-----请选择角色-----</option>
						
					     </select>
						</s:else>--%>
						<s:select name="nodeDefineVo.roleId"  id="nodeDefineVo.roleId" list="roleList" headerKey="-1"  headerValue="---请选择角色---"  listKey="roleId" listValue="roleName"  ></s:select>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button"  value="定义角色" onclick="roleManager()"/>
					</br>
				</fieldset>
			</td>
				</tr>
</table>
				
	<table border="0" cellspacing="20" cellpadding="10"
						style="width: 100%">
			<tr>
			<td width="50%" height="50px">
				<fieldset style="width:550px;height:50px;" align="center" >
					<legend>查关联任务设置</legend>
					</br>&nbsp; &nbsp; &nbsp; &nbsp; <label>选择任务</label> ：<img src="images/icons/document-library.png"></img><%--
					
					<s:if test="relationTaskList!=null&&relationTaskList.size()>0">
								<select name="nodeDefineVo.relationTaskId" id="nodeDefineVo.relationTaskId">
									<option  value="-1">---请选择关联任务---</option>
							<s:iterator id="r" value="relationTaskList">
									<option  value='<s:property value="#r.taskId"/>' ><s:property value="#r.taskName"/></option>
									
							</s:iterator>
								</select>
						
						</s:if>
						<s:else>
							
					     <select name="nodeDefineVo.relationTaskId" id="nodeDefineVo.relationTaskId">
							
							<option  value="-1">---请选择关联任务---</option>
						
					     </select>
						</s:else>
					 --%>
					 <s:select name="nodeDefineVo.relationTaskId"  id="nodeDefineVo.relationTaskId" list="relationTaskList" headerKey="-1"  headerValue="---请选择任务---"  listKey="taskId" listValue="taskName"  ></s:select></br>
					 </br></br>
					<font>&nbsp; &nbsp; &nbsp; &nbsp;！这里选择的关联任务将以子任务的形式显示出现在待处理任务下方</font>
					</br></br>
				</fieldset>
					</td>
				</tr>
</table>
				<table border="0" cellspacing="20" cellpadding="10"
						style="width: 100%">
				<tr>
					<td width="170" align="center" valign="middle">
						<span class="button" onclick="quit()">退出</span></td>
						<td width="170" align="center" valign="middle">
						<span class="button" onclick="back()">后退</span></td>
					<td width="170" align="center" valign="middle">
						<span class="button" onclick="addNodeInfo()">下一步</span></td>
					<td width="160" align="center" valign="middle">
						<span class="button" onclick="addComplete()">完成</span></td>
				</tr>
				</table>
				
			<br/>
			<br/>
		
</fieldset>
</from>
	</div>
</body>
</html>
