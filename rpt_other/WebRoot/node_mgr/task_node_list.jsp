<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  
    
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
<%--	<link href="css/common_node.css" type="text/css" rel="stylesheet">--%>
	<link href="css/index.css" rel="stylesheet" type="text/css" />
<link href="css/common0.css" rel="stylesheet" type="text/css" />
<link href="css/common.css" rel="stylesheet" type="text/css" />
<link href="css/table.css" rel="stylesheet" type="text/css" />
<link href="css/globalStyle.css" rel="stylesheet" type="text/css" />
<link href="css/thd.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="scripts/progressBar.js"></script>
<link href="css/animate/theme/jquery-ui-1.8.11.redmond.css"
	rel="stylesheet" />
	<style type="text/css">
	
	
	</style>
	<script type="text/javascript">
	var  progressBar=new ProgressBar("正在跳转，请稍后........");
	
		function modNodeInfo(taskId,nodeId,busiLineId){
			<s:if test="#request.taskTypeId!=null && #request.taskTypeId=='zfbs'">
  				window.location="<%=request.getContextPath()%>/editWorkTaskCollectNodeInfoAction!editNodeInfo.action?busiLineId="+busiLineId+"&taskId="+taskId+"&nodeId="+nodeId;
 			</s:if>
			<s:else>
				window.location="<%=request.getContextPath()%>/editWorkTaskNodeInfo!editNodeInfo.action?busiLineId="+busiLineId+"&taskId="+taskId+"&nodeId="+nodeId;
			</s:else>
			progressBar.show();
			
			
		}
		function preAddNodeInfo(taskId,nodeId,preNodeId,busiLineId){
		<s:if test="#request.taskTypeId!=null && #request.taskTypeId=='zfbs'">
  				window.location="<%=request.getContextPath()%>/editWorkTaskCollectNodeInfoAction!preAddNodeInfo.action?busiLineId="+busiLineId+"&taskId="+taskId+"&nodeId="+nodeId+"&preNodeId="+preNodeId;
 			</s:if>
			<s:else>
				window.location="<%=request.getContextPath()%>/editWorkTaskNodeInfo!editNodeInfo.action?busiLineId="+busiLineId+"&taskId="+taskId+"&nodeId="+nodeId;
			</s:else>
			
			progressBar.show();
		}
		function backAddNodeInfo(taskId,nodeId,preNodeId,busiLineId){
		<s:if test="#request.taskTypeId!=null && #request.taskTypeId=='zfbs'">
  				window.location="<%=request.getContextPath()%>/editWorkTaskCollectNodeInfoAction!backAddNodeInfo.action?busiLineId="+busiLineId+"&taskId="+taskId+"&nodeId="+nodeId+"&preNodeId="+preNodeId;
 			</s:if>
			<s:else>
				window.location="<%=request.getContextPath()%>/editWorkTaskNodeInfo!editNodeInfo.action?busiLineId="+busiLineId+"&taskId="+taskId+"&nodeId="+nodeId;
			</s:else>
			progressBar.show();
		}
		
		function goBackTaskInfo(taskId){
			//alert(taskId);
			window.location.href="findInfoById_workTaskInfoAction.action?task.taskId="+taskId;
		}
	
	
	
	</script>

  </head>
  
  <body>
 	<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td><div >
							<span><img src="images/icon01.gif" /> </span><span style="font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#EBAD06;">当前位置
								>> 任务定制 >> 任务节点列表 </span>
						</div>
				
			</td>
		</tr>
	</table>
	
	<table border="0" cellpadding="0" cellspacing="0" width="98%" align="center">
		<tr>
			<td colspan="7" align="right" height="20" ><input class="input-button" type="button" value="返    回" onclick="goBackTaskInfo('<s:property value='#request.taskInfoListId' />')"/></td>
		
		</tr>
		</table>
		<br/>
			 	<table border="1" cellpadding="0" cellspacing="0" width="98%"  align="center" >
			 		<tr  height="40">
									<th align="center" colspan="7">
										任务节点信息
									</th>
								</tr>
								<TR class="middle" height="30">
									
									<th align="center" valign="middle"  width="5%">
										节点编号
									</th>
									<th align="center" valign="middle"  width="15%">
										任务名称
									</th>
								
									<th align="center" valign="middle" width="10%">
										节点名称
									</th>
								
									
									<th align="center" valign="middle" width="9%">
										报送时间
									</th>
									<th align="center" valign="middle" width="9%">
										处理类型
									</th>
									<th align="center" valign="middle" width="9%">
										角色
									</th>
									<th align="center" valign="middle" width="20%">
										状态(操作)
									</th>
			
								</TR>
								
								<s:if test="taskNodeList!=null&&taskNodeList.size()>0">
									<s:iterator id="tn" value="taskNodeList" status="s" >
										<TR bgcolor="#FFFFFF" height="30">
												<TD align="center">
												<s:property value="#s.index+1" />
												</TD>
												<TD align="center">
												<s:property value="#tn.taskName" />
												</TD>
												<TD align="center">
												<s:property value="#tn.nodeName" />
												</TD>
												
												<TD align="center">
												<s:property value="#tn.nodeTime" />
												</TD>
												<TD align="center">
												<s:property value="#tn.conductTypeName" />
												</TD>
												<TD align="center">
												<s:property value="#tn.roleName" />
												</TD>
												<TD align="center">
												<a href="javascript:modNodeInfo('<s:property value="#tn.taskId"/>','<s:property value="#tn.nodeId"/>','<s:property value="#tn.busiLineId"/>')" >修改</a><%--
												&nbsp;&nbsp;&nbsp;
												<a href="javascript:preAddNodeInfo('<s:property value="#tn.taskId"/>','<s:property value="#tn.nodeId"/>','<s:property value="#tn.preNodeId"/>','<s:property value="#tn.busiLineId"/>')" >插入</a>
												&nbsp;&nbsp;&nbsp;
												<a href="javascript:backAddNodeInfo('<s:property value="#tn.taskId"/>','<s:property value="#tn.nodeId"/>','<s:property value="#tn.preNodeId"/>','<s:property value="#tn.busiLineId"/>')" >新增</a>
												
												--%></TD>
										</TR>
									</s:iterator>
								</s:if>
								<s:else>
								<TR bgcolor="#FFFFFF" >
											<TD align="center" colspan="5">
												该任务暂时没有节点信息
											</TD>
									</TR>
								</s:else>
			 	</table>

  </body>
</html>
