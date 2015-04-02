<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>待办任务</title>
	
	 <link href="<%=request.getContextPath() %>/css/index.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath() %>/css/common0.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath() %>/css/table.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath() %>/css/globalStyle.css" rel="stylesheet" type="text/css" /> 
    <script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.4.min.js"></script>
    <script language="javascript" src="<%=request.getContextPath() %>/scripts/progressBar.js"></script>
	<script type="text/javascript">
	var progressBar=new ProgressBar("正在跳转，请稍后........");
		$(function(){
			w_width=$(window).width();
		    w_height=$(window).height();
		
			
			$("#fm").submit(function(){
				if($("#returndesc").val()==""){
					alert("请输入退回原因!");
					return false;
				}
				<s:if test="taskType==@com.fitech.model.worktask.common.WorkTaskConfig@TASK_TYPE_DJSH && 'custom'==@com.fitech.model.worktask.common.WorkTaskConfig@WORK_TASK_COND_TYPE_ID">
					if($("#nodeNames").val()==""){
						alert("请选择退回任务阶段!");
						return false;
					}
	  			</s:if>
	  			<s:if test="repType!=null && repType==1">
					if($("#repDay").val()==""){
						alert("请输入重报时间!");
						return false;
					}
					if(isNaN($("#repDay").val())){
						alert("请输入正确的数值!");
						$("#repDay").focus();
						return false;
					}
	  			</s:if>
	  			progressBar.show();
			});
		})
		
		function getId(id){
			return document.getElementById(id);
		}
		
		function showTaskFlowing(){
			$.post(
					"<%=request.getContextPath()%>/pendingTaskAction!searchNodeInfos.action",
					{"nodeIds":<s:property value="nodeIds"/>},
					function(rv){
						var count =1;
						var content = "";
						if(rv!=undefined && rv.length>0){
							getId("content").innerHTML="";
							content+="<img src='images/search.png'/><span class='taskSpan' style='color:#2194E0;margin-left:2px;'>选择任务阶段</span><br><br>";
							for(var i in rv){
								if(rv[i].taskNodeName!=undefined){
									content +='&nbsp;&nbsp;<span class="taskSpan" onclick="spanclick('+rv[i].nodeId+',\''+rv[i].taskNodeName+'\')" style="color:#2194E0;cursor:hand;">['+rv[i].taskNodeName+']</span>&nbsp;&nbsp;';
									var num = rv.length;
									//alert(i+"---"+(num-1));
									if(i<(num-1)){
										content+="<img src='images/pright.gif'>";
									}			
									if(count%4==0){
										content +="<br><br>";		
									}
									count++;
								}
							}
							getId("taskFlowing").style.display='block';
							getId("content").innerHTML=content;
							layerAction("taskFlowing");
						}
					}
			);
		}

		function spanclick(nodeId,nodeName){
			getId("taskFlowing").style.display='none';
			getId("tasknodeId").value=nodeId;
			getId("nodeNames").value=nodeName;
			/*var nodeNameValue = getId("nodeNames").value;
			var nodeIdValue = getId("nodeIds").value;
			if(nodeNameValue.indexOf(",")==0){
				getId("nodeNames").value = nodeNameValue.substr(1);
			}
			if(nodeIdValue.indexOf(",")==0){
				getId("nodeIds").value = nodeIdValue.substr(1);
			}*/
		} 
		
		var w_width;
		var w_height;
		
		
		 function layerAction(id){
			 layer=$("#"+id)
			 var topErr;
			 var l_width=layer.width();
			 var l_height=layer.height();
		     var left=(w_width-l_width)/2;
			var top=(w_height-l_height)/2;
		    var bodyHeight=$("body").height();
			if(bodyHeight<w_height){bodyHeight=w_height}
			topErr=$(window).scrollTop();
			layer.css({"left":left,"top":top+topErr});
		    //layer.show();
		    //cBg.height(bodyHeight);
		    //cBg.show();
		  }
		
	</script>
	<style type="text/css">
	#taskFlowing{
		width:502px;
		height:140px;
		margin:auto;
		margin: 0px auto; 
		border:1 solid #CCCCCC;
	/*	background-image:url(/images/task.jpg");*/
		position:absolute;
		background-color:#FFFFFF;
		z-index:1;
		white-space:nowrap;
		padding:3px;
	}
	#taskFlowing div{
		width:500px;
		padding:5px;
		height:138px;
		margin:0px;
		text-align:left;
	/*	color:#8ba1aa;
	margin-top:45px;*/
		/*color:#ebad06;*/
		/*overflow-y:scroll;*/
		overflow:auto;
		border:1 solid #CCCCCC;
	}
	.taskSpan{
		font-family:Arial;font-size:12px;font-weight:bold;font-style:normal;text-decoration:none;
	}
	.selectClass{
		width: 100px;vertical-align: middle;
	}
</style>
</head>

<body onload="init()">
<div id="taskFlowing" style="display:none;">
	<div id="content"></div>
</div>
<div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td nowrap="nowrap">
    <div class="toolbar">
    <div class="tableinfo"><span><img src="<%=request.getContextPath() %>/images/icon01.gif" alt="列表" /></span><b style="font-size:12px;">当前位置 >>报表处理 >>退回原因</b></div>
    </div>
    </td>
  </tr>
</table>
</div>
<div style="width:60%;border:0 solid black;height:400px;padding:10px;">
<fieldset style="background-color:white;height:330px;" >
<legend style="color:red;">退回原因输入栏</legend>
<form id="fm" action="<%=request.getContextPath() %>/pendingTaskAction!saveBackTask.action" method="post">
		  	<input type="hidden" name="pendingTaskQueryConditions.orgName" value="<s:property value="pendingTaskQueryConditions.orgName"/>"/>
		  	<input type="hidden" name="pendingTaskQueryConditions.orgId" value="<s:property value="pendingTaskQueryConditions.orgId"/>"/>
		  	<input type="hidden" name="pendingTaskQueryConditions.condTypeId" value="<s:property value="pendingTaskQueryConditions.condTypeId"/>"/>
			<input type="hidden" name="pendingTaskQueryConditions.freqId" value="<s:property value="pendingTaskQueryConditions.freqId"/>"/>
			<input type="hidden" name="pendingTaskQueryConditions.nodeFlag" value="<s:property value="pendingTaskQueryConditions.nodeFlag"/>"/>
			<input type="hidden"  name="pendingTaskQueryConditions.busiLine" value="<s:property value="pendingTaskQueryConditions.busiLine"/>"/>
			<input type="hidden" name="pendingTaskQueryConditions.taskTerm" value="<s:property value="pendingTaskQueryConditions.taskTerm"/>"/>
			<input type="hidden" name="pendingTaskQueryConditions.taskId" value="<s:property value="pendingTaskQueryConditions.taskId"/>"/>
<table width="95%" border="0" cellspacing="0" cellpadding="0"  align="center">
	<tr height="10px"><td colspan="3"></td></tr>
	<tr height="250px">
   		<td colspan="3" align="center">
   			<input name="str" type="hidden" value="<s:property value="str"/>"/>
   			<input type="hidden" name="nodeIds" value="<s:property value="nodeIds"/>"/>
   			<textarea id="returndesc" name="pendingTaskQueryConditions.returnDesc" style="width:95%;height:95%;" rows="" cols=""></textarea>
   		</td>
  </tr>
  <tr height="40px">
  	<td colspan="3" >
  		<s:if test="repType!=null && repType==1">
  			&nbsp;&nbsp;&nbsp;&nbsp;
  			<s:if test="@com.fitech.model.worktask.common.WorkTaskConfig@REPORT_TIME_UNIT=='hour'">重报时间(小时):</s:if>
  			<s:else>重报时间(天):</s:else>
  			<input type="text" name="repDay" id="repDay"/>
  		</s:if>
  		<s:if test="taskType==@com.fitech.model.worktask.common.WorkTaskConfig@TASK_TYPE_DJSH && 'custom'==@com.fitech.model.worktask.common.WorkTaskConfig@WORK_TASK_COND_TYPE_ID">
  			&nbsp;&nbsp;&nbsp;&nbsp;
  			<input type="hidden" id="tasknodeId" name="taskNodeIds"/>
	  		<input type="text" id="nodeNames" style="width:180px;height:25px;border:1 solid #CCCCCC;"/> <a href="javascript:;" onclick="showTaskFlowing()" style="color:blue;">[选择任务退回阶段]</a>
  			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  		</s:if>
	  	<input name="button" type="submit" class="searchbtn" value="确定" />
  	</td>
  </tr>
  </table>
</form>
　</fieldset>
</div>
</body>
</html>