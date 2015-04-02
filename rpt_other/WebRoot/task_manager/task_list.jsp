<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%><html:html locale="true">
<head>
<title>task_list</title>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="css/index.css" rel="stylesheet" type="text/css" />
<link href="css/common0.css" rel="stylesheet" type="text/css" />
<link href="css/common.css" rel="stylesheet" type="text/css" />
<link href="css/table.css" rel="stylesheet" type="text/css" />
<link href="css/globalStyle.css" rel="stylesheet" type="text/css" />
<link href="css/thd.css" rel="stylesheet" type="text/css" />
<link href="css/animate/theme/jquery-ui-1.8.11.redmond.css"
	rel="stylesheet" />
	
	
	<script type="text/javascript">
	function preUpdate(taskId){
		window.location.href="findInfoById_workTaskInfoAction.action?task.taskId="+taskId;
	}
	
	function delTask(taskId){
		if(confirm("确认删除吗?")){
			window.location.href="delTask_workTaskInfoAction.action?task.taskId="+taskId;
		}
	}
	
	//任务生效和停止
	function updateUsingFlagByTaskInfo(taskId,publicFlag){
		location.href="updateFlag_workTaskInfoAction.action?task.taskId="+taskId+"&&task.publicFlag="+publicFlag;
	}
	
	function createTask(){
		location.href="preAdd_workTaskInfoAction.action";
	}
	
	
	</script>
</head>
<body>
	<div style="width:100%; margin-bottom: -10px;">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td nowrap="nowrap">
					<div class="toolbar">
						<div class="tableinfo">
							<span><img src="images/icon01.gif" /> </span><b style="font-size:12px;">当前位置 >> 报表处理
								>> 任务定制</b>
						</div>
					</div></td>
			</tr>
		</table>
	</div>
	<br />


	<fieldset style="width:95%;">
		<legend>模板类型列表</legend>
		<div width="95%">
			<s:form action="findListByParam_workTaskInfoAction" id="from1" namespace="/" method="post">
				</br>
				<table width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<td width="1%"></td>
						<td width="20%">任务名称: <s:textfield 
								name="task1.taskName" /></td>
						</td>
						<td width="20%" align="left">频度
						 <s:select
								name="task1.freqId"  list="freqMap" listKey="key" listValue="value" headerKey="-99" headerValue="请选择频度">
							</s:select>
							</td>
						<td width="15%"><s:submit value="查     询" /></td>
						<td width="20%"><input type="button" value="新     增" onclick="createTask()"></td>
					</tr>
				</table>
		</div>
		<br />
		<table width="100%" border="1" cellspacing="0" cellpadding="5">
			<tr height="30px">
				<th width="7%">序号</th>
				<th width="17%">任务名称</th>
				<th width="8%">任务频度</th>
				<th width="17%">任务开始时间</th>
				<th width="17%">任务结束时间</th>
				<th width="10%">触发方式</th>
				<th width="10%">是否生效</th>
				<th width="20%">操作</th>
			</tr>
		
			<s:if test="taskList==null ||taskList.size==0">
			<tr>
			<td colspan="7">暂无任务</td>
			</tr>
			</s:if>
			<s:iterator value="tasklist" id="task" status="status">
			<tr align="center" height="30px">
				<td><s:property value="#status.index+1" /></td>
				<td><s:property value="#task.taskName" /></td>
				<td>
				<s:if test="#task.freqId=='10days'">
				旬
				</s:if>
				<s:elseif test="#task.freqId=='day'">
				日
				</s:elseif>
				<s:elseif test="#task.freqId=='halfyear'">
				半年
				</s:elseif>
				<s:elseif test="#task.freqId=='month'">
				月
				</s:elseif>
				<s:elseif test="#task.freqId=='once'">
				非定时
				</s:elseif>
				<s:elseif test="#task.freqId=='pbc_10days'">
				旬(人行)
				</s:elseif>
				<s:elseif test="#task.freqId == 'pbc_day'">
				日(人行)
				</s:elseif>
				<s:elseif test="#task.freqId == 'pbc_month'">
				月(人行月快报)
				</s:elseif>
				<s:elseif test="#task.freqId=='season'">
				季
				</s:elseif>
				<s:elseif test="#task.freqId=='week'">
				季
				</s:elseif>
				<s:elseif test="#task.freqId=='season'">
				周
				</s:elseif>
				<s:elseif test="#task.freqId=='year'">
				年
				</s:elseif>
				<s:elseif test="#task.freqId=='yearbegincarry'">
				年初结转
				</s:elseif>
				<s:else>
				--
				</s:else>
				</td>
				<td><s:date name="#task.startDate"  format="yyyy-MM-dd"/></td>
				<td><s:date name="#task.endDate" format="yyyy-MM-dd"/></td>
				<td>
					<s:property value="#task.trrigerId"/>
				</td>
				<td>
				<s:if test="#task.publicFlag==1">
				是
				</s:if>
				<s:elseif test="#task.publicFlag==0">
				否
				</s:elseif>
				<s:else>--</s:else>
				</td>
				<td>
				<img src="images/go.gif" alt="修改"
						onclick="preUpdate(<s:property value='#task.taskId' />)"
						 style="cursor:hand"/>&nbsp;&nbsp;&nbsp;&nbsp;
						<%-- 
					<img src="images/icons/full-time.jpg" title="设定有效期"
							onclick="updateUsingTime(<s:property value='#task.taskId' />)"
						 	style="cursor: hand" />&nbsp;&nbsp;&nbsp;&nbsp;
					
					<img src="images/del.gif" title="删除"
					onclick="deleteTask(<s:property value='#task.taskId' />)"
					style="cursor: hand" /> &nbsp;&nbsp; &nbsp;&nbsp; --%>
					<s:if test="#task.publicFlag==1">
    			<img src="images/delete.gif" title="生效状态，单击可停止"  style="cursor: hand"
    				onclick="javascript:updateUsingFlagByTaskInfo(<s:property value='#task.taskId'/>,0)"/>
    				&nbsp;&nbsp;&nbsp;
    		</s:if>
    		<s:else>
    			<img src="images/btn011.gif" title="停止状态，单击可生效" style="cursor: hand"
    				onclick="javascript:updateUsingFlagByTaskInfo(<s:property value='#task.taskId'/>,1)"/>
    				&nbsp;&nbsp;&nbsp;
    		</s:else>
					
					
					<img src="images/del.png" alt="删除"
						onclick="delTask(<s:property value='#task.taskId' />)"
						 style="cursor:hand"/>&nbsp;&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
			</s:iterator>
		</table>
	</fieldset>
</s:form>
</body>
</html:html>
