<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page import="com.fitech.model.worktask.security.Operator"%>
<%@page import="com.fitech.model.worktask.common.WorkTaskConfig"%>
<%@page import="com.fitech.framework.core.common.Config"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//System.out.println("path:"+path);
//System.out.println("basePath:"+basePath);
Operator operator = null;
	if (session.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME) != null)
		operator = (Operator) session
				.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
	//String userId=String.valueOf(operator.getOperatorId());
	String 	userId= operator.getOperatorId()+"";//测试用
	//System.out.println("userId:"+userId);
	
	
	String  jsonPath = Config.WEBROOTPATH ;
%>
<html>
<head>
<title>任务节点更新</title>
 <base href="<%=basePath%>">
<meta http-equiv="content-type" content="text/html; charset=utf-8" />

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
	<script language="javascript" src="scripts/progressBar.js"></script>
	
	<script type="text/javascript">
	var  progressBar=new ProgressBar("正在处理，请稍后........");
	//$(function(){
	//	var orgId = "5b0c9112300";	
	//	var node = $('#tt2').tree(orgId);
		//alert(node.id);
		//$('#tt2').tree('getChecked')
	//})
	
	
	function getChecked(){
		var nodes = $('#tt2').tree('getChecked');
		var s = '';
		for(var i=0; i<nodes.length; i++){
			if (s != '') s += ',';
			s += nodes[i].id;
		}
		return s;
	}
	
	function getChecked1(){
		var nodes = $('#tt1').tree('getChecked');
		var s = '';
		for(var i=0; i<nodes.length; i++){
			if (s != '') s += ',';
			s += nodes[i].id;
		}
		return s;
	}
	
	
	function doSearch(){
		var templateId = getChecked1();
		var orgId = getChecked();
	//	alert(templateId+"*************"+orgId);
		if(orgId==''){
			alert("请选择机构!");
			return;
		}
		if(templateId==''){
			alert("请选择模板!");
			return;
		}if(templateId!=''){
			var flag=false;
					 /*	$.ajax({   
						          type : "post",   
						          url : "/rpt_other/isExistTemplateEdit_workTaskNodeAction.action",   
								  data: "templateIds="+templateId+"&taskId="+document.getElementById("task.taskId").value,
						          async : false,   
						          success : function(data){   
										if(data==undefined || parseInt(data)==1){
											alert("所选报表已在其他任务中存在");
											flag=true;
											
										}
						          }   
					          });
					 	if(flag){
					 		return;
					 	}*/
				 }
		$('#object_orgIds').val(orgId);
		$('#object_templateIds').val(templateId);
		$('#form1').submit();
		progressBar.show();
	}
	
	$(function(){
		$('#tt2').tree({
			checkbox: true,
			cascadeCheck:false,
			
			url: '<%=request.getContextPath()%>/json/org_tree_data_<%=userId%>.json',
			
			onClick:function(node){
				$(this).tree('toggle', node.target);
			},
			onContextMenu: function(e, node){
				e.preventDefault();
				$('#tt2').tree('select', node.target);
				//$('#mm').menu('show', {
				//	left: e.pageX,
				//	top: e.pageY
				//});
			}
		});
		
		$('#tt1').tree({
			checkbox: true,
			cascadeCheck:false,
			
			url: '<%=request.getContextPath()%>/json/template_tree_data_<%=userId%>.json',
			
			onClick:function(node){
				$(this).tree('toggle', node.target);
			},
			onContextMenu: function(e, node){
				e.preventDefault();
				$('#tt1').tree('select', node.target);
				$('#mm').menu('show', {
					left: e.pageX,
					top: e.pageY
				});
			}
		});

		
	});
 

	
	</script>
</head>
<body>

	<div style="width:100%">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td nowrap="nowrap">
					<div class="toolbar">
						<div class="tableinfo">
							<span><img src="images/icon01.gif" /> </span><b style="font-size:12px;">当前位置
								>> 报表处理>> 任务修改 >> 流程修改</b>
						</div>
					</div></td>
			</tr>
		</table>
	</div>
	<s:form action="editHzNodeInfo_workTaskNodeAction" id="form1" method="post" namespace="/">
	<s:hidden name="task.taskId" id="task.taskId"></s:hidden>
	<div align="center">
		<fieldset style="width:600px ">
			<legend>流程定义</legend>

			<br />

			<table border="0" cellspacing="20" cellpadding="10"
				style="width: 100%">
				<%-- <tr>
					<td >节点名称&nbsp;&nbsp;&nbsp; <s:textfield name="node.nodeName" value="" />
					</td>
				</tr> --%>


				<tr>
					<td width="50%">
						<fieldset style="width: 100%;height: 250px ;align:center">
							<legend>机构区</legend>
							</br>
								<div id="jigouqu"  region="east" iconCls="icon-reload" title="机构" split="true" style="width: 89%;height: 230px ;background: ; margin-left: 25 ;margin-bottom: 10 ;OVERFLOW: auto;" >
						<ul  class="easyui-tree" id="tt2" style="width: 200"></ul>
						</div>
						</fieldset></td>
					<td width="50%">
						<fieldset style="width: 100%;height: 250px">
							<legend>报表区</legend>
</br>
							<div id="baobiaoqu" style="width: 89%;height: 230px ;background:  ;margin-left: 25 ;margin-bottom: 10 ;OVERFLOW: auto;">
							
								<ul  class="easyui-tree" id="tt1" style="width: 200"></ul>
							</div>
						</fieldset>
					</td>
				</tr>
				<%-- <tr>
					<td colspan="2" height="50px">
						<fieldset style="width:100% ;height: 40px"" >
							<legend>参与角色</legend>
							</br>&nbsp; &nbsp; &nbsp; &nbsp; 角色列表 &nbsp;&nbsp;<img src="images/m5.gif" />&nbsp;&nbsp;
							<s:select name="role.id.roleId" id="roleMap"  list="roleMap" listKey="key" listValue="value"> 
							</s:select>
							</br></br>
						</fieldset></td>
				</tr> --%>
					<tr>
						<td colspan="2" height="50px">
							<fieldset style="width: 100%;height: 80px ">
								<legend>处理类型</legend>
								</br>
								<table style="width: 100%">
									<tr>
									<td>
									&nbsp; &nbsp;填 &nbsp; &nbsp;报 &nbsp; &nbsp;<s:select name="roleVo.tb_roleId" id="roleMap"  list="roleMap" listKey="key" listValue="value"> 
									</s:select> 
									</td>
									<td>
									&nbsp; &nbsp;复 &nbsp; &nbsp;核 &nbsp; &nbsp; &nbsp;<s:select name="roleVo.fh_roleId" id="roleMap"  list="roleMap" listKey="key" listValue="value"> 
									</s:select> 
									</td>
									
									</tr>
								</table>
							</fieldset></td>
					</tr>

				<tr>
					<td colspan="2" height="50px">
						<fieldset style="width:100% ;height: 40px"" >
							<legend>报送时间</legend>
							</br>
							<table style="width: 100% " >
							<tr height="20px" align="center">
							<td width="33%">
								总行
								<s:textfield name="zTime" size="10" ></s:textfield>
								
							</td>
							<td width="33%">
								分行
								<s:textfield name="fTime" size="10"  ></s:textfield>
							</td>
							<td width="33%">
								支行
								<s:textfield name="cTime" size="10" ></s:textfield>
							</td>
							</tr>
							</table>
</br>
						</fieldset></td>
				</tr>
				
				<tr>
					<td colspan="2" align="center">
				<s:hidden id="object_orgIds" name="orgIds"></s:hidden>
				<s:hidden id="object_templateIds" name="templateIds"></s:hidden>
						<button onclick="doSearch()">完成</button></td>
				</tr>
			</table>
		</fieldset>
	</div>
</s:form>
</body>