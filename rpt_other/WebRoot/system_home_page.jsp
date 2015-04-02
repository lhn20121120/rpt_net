<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
	String appPath = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ appPath + "/";
%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>综合报送平台</title>
		<script type="text/javascript" src="<%=request.getContextPath() %>/dwr/interface/msgInfoService.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/dwr/engine.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/dwr/util.js"></script>
		<link href="css/globalStyle.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript">
		//获取公告信息
		function getPubMsg(msgId){
			
			msgInfoService.findMsgInfoById(msgId,function(tmsgInfo){
				var  info=new Object();
				info.title=tmsgInfo.msgTitle;
				info.content=tmsgInfo.content;
				info.userName=tmsgInfo.userName;
				info.touserName=tmsgInfo.touserName;
				info.viewFileName=tmsgInfo.viewFileName;
				info.startTime=tmsgInfo.startTime;
				info.filePath="无附件";
				
				if(info.viewFileName!=null && info.viewFileName!="")
					info.filePath="<a href='tmsginfo!down.action?tmsgInfo.filename="+tmsgInfo.filename+"' alt='下载附件' style='cursor:hand;'>"+tmsgInfo.viewFileName+"</a>";
					window.showModalDialog("<%=request.getContextPath() %>/byg/showMsgInfo.jsp",info,"dialogWidth=700px;dialogHeight=400px;scroll:no;");
					
				
				
			})
			msgInfoService.isRead(msgId,function(flag){
				var flag=flag;
			})
		}
		
		
		
		
		</script>
	</head>
<body style="background-color:#FFFFFF;overflow:hidden;margin-top:5px;">
	<div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		  <tr>
		    <td width="8">
		    <span><img src="images/icon01.gif" /></span></td>
		    <td>
		    <span style="font-family:Arial;font-size:12px;font-weight:bold;font-style:normal;text-decoration:none;color:#EBAD06;">&nbsp;&nbsp;&nbsp;&nbsp;当前位置  >> 系统主页</span>
		    </td>
		  </tr>
		</table>
	</div>

<div>
<table width="100%"  border="0" cellpadding="0" cellspacing="0" style="margin-top:8px;margin-left:15px;height:90%">
	<tr>
		<td width="74%" style="font-size:13px;font-weight:bold;"><font color="#FF9933" style="margin-left:15px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/main/u0_normal.gif"/>&nbsp;待办任务</font></td>
		<td width="1%"></td>
		<td width="24%" style="font-size:13px; font-weight:bold; "><font color="#2194E0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/main/u53_normal.gif"/>个人信息</font></td>
	</tr>
	<tr height="45%">
		<td>
			<div style="width:96%;overflow:auto;margin:0px;padding:0px;text-align:center;margin-top:-2px; margin-left:15px;height:100%" >
				<p style="margin-right:700px"><img src="images/main/u7_normal.png"></img></p>
			    <p style="margin-top:-23px;"><img src="images/main/u6_line.png" style="width: 770px;"></img></p>
				<div style="width:94%;margin-top:-20px;height:90%;overflow:auto; background-color:#FFFFFF; border:1px #CECECE solid;"> 
			<br/>
			<s:if test="bussiness == null">
				<table cellspacing="0" width="85%" cellpadding="0" border="0" style="height:30%">
						<tr>
							<td style="font-size:13px;font-weight:bold;"><span style="font-family:Arial;font-style:normal;text-decoration:none;color:#FF9933;">银监条线</span><img src="images/main/u26_line.png"/></td>
						</tr>
						<s:iterator value="indexYJTasks">
								<tr>
										<s:if test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WAIT">
											<td  style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;color:#2194E0;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_YJTX"/>" style="color:#2194E0;font-size:13px;text-decoration: underline;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务等待</a></span>
											</td>
										</s:if>
										<s:elseif test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REWA">
											<td  style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;color:#2194E0;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_YJTX"/>" style="color:#2194E0;font-size:13px;text-decoration: underline;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务退回等待</a></span>
											</td>
										</s:elseif>
										<s:elseif test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REFU">
											<td  style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;color:#FF9933;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_YJTX"/>" style="color:#FF9933;font-size:13px;text-decoration: underline;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务审核不通过</a></span>
											</td>
										</s:elseif>
										<s:else>
											<td  style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;color:#FF9933;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_YJTX"/>" style="color:#FF9933;font-size:13px;text-decoration: underline;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务待处理</a></span>
											</td>
										</s:else>
									</tr>
							</s:iterator>
							<s:if test="indexYJTasks==null || indexYJTasks.size()==0">
								<tr>
									<td  style="font-size:13px; font-weight:bold;">
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;color:#2194E0;margin-left:60px;">
														暂无任务处理!!!
														</span>
									</td>
								</tr>
							</s:if>
			</table>
				
	
			<table cellspacing="0" width="85%" cellpadding="0" border="0" style="height:30%">
						<tr>
							<td style="font-size:15px;"><span style="font-family:Arial;font-size:13px; font-weight:bold; font-style:normal;text-decoration:none;color:#FF9933;">人行条线</span><img src="images/main/u26_line.png"/></td>
						</tr>
						<s:iterator value="indexRHTasks">
								<tr>
										<s:if test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WAIT">
											<td  style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_RHTX"/>" style="color:#2194E0;font-size:13px;text-decoration: underline;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务等待</a></span>
											</td>
										</s:if>
										<s:elseif test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REWA">
											<td  style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_RHTX"/>" style="color:#2194E0;font-size:13px;text-decoration: underline;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务退回等待</a></span>
											</td>
										</s:elseif>
										<s:elseif test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REFU">
											<td  style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;color:#FF9933;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_RHTX"/>" style="color:#FF9933;font-size:13px;text-decoration: underline;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务审核不通过</a></span>
											</td>
										</s:elseif>
										<s:else>
												<td  style="font-size:14px;"> 
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;">
													<a href='<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_RHTX"/>' style="color:#FF9933;font-size:13px;text-decoration: underline;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务待处理</a></span>
												</td>
										</s:else>
									</tr>
							</s:iterator>
						<s:if test="indexRHTasks==null || indexRHTasks.size()==0">
							<tr>
								<td  style="font-size:13px;font-weight:bold;">
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;color:#2194E0;margin-left:60px;">
													暂无任务处理!!!
													</span>
								</td>
							</tr>
						</s:if>
			</table>
			<table cellspacing="0" width="85%" cellpadding="0" border="0" style="height:30%">
						<tr>
							<td style="font-size:15px;"><span style="font-family:Arial;font-size:13px; font-weight:bold; font-style:normal;text-decoration:none;color:#FF9933;">其他条线</span><img src="images/main/u26_line.png"/></td>
						</tr>
						<s:iterator value="indexQTTasks">
								<tr>
										<s:if test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WAIT">
											<td  style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_QTTX"/>" style="color:#2194E0;font-size:13px;text-decoration: underline;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务等待</a></span>
											</td>
										</s:if>
										<s:elseif test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REWA">
											<td  style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_QTTX"/>" style="color:#2194E0;font-size:13px;text-decoration: underline;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务退回等待</a></span>
											</td>
										</s:elseif>
										<s:elseif test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REFU">
											<td  style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;color:#FF9933;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_QTTX"/>" style="color:#FF9933;font-size:13px;text-decoration: underline;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务审核不通过</a></span>
											</td>
										</s:elseif>
										<s:else>
												<td  style="font-size:14px;"> 
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;">
													<a href='<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_QTTX"/>' style="color:#FF9933;font-size:13px;text-decoration: underline;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务待处理</a></span>
												</td>
										</s:else>
									</tr>
							</s:iterator>
						<s:if test="indexQTTasks==null || indexQTTasks.size()==0">
							<tr>
								<td  style="font-size:13px;font-weight:bold;">
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;color:#2194E0;margin-left:60px;">
													暂无任务处理!!!
													</span>
								</td>
							</tr>
						</s:if>
			</table>
			</s:if>
			<s:if test="bussiness == @com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_YJTX">
			<table cellspacing="0" width="85%" cellpadding="0" border="0">
						<tr>
							<td style="font-size:13px;font-weight:bold;"><span style="font-family:Arial;font-style:normal;text-decoration:none;color:#FF9933;">银监条线</span><img src="images/main/u26_line.png"/></td>
						</tr>
						<s:iterator value="indexYJTasks">
								<tr>
										<s:if test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WAIT">
											<td  style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;text-decoration:underline;color:#2194E0;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_YJTX"/>" style="color:#2194E0;font-size:13px;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务等待</a></span>
											</td>
										</s:if>
										<s:elseif test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REWA">
											<td  style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;text-decoration:underline;color:#2194E0;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_YJTX"/>" style="color:#2194E0;font-size:13px;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务退回等待</a></span>
											</td>
										</s:elseif>
										<s:elseif test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REFU">
											<td  style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;text-decoration:underline;color:#FF9933;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_YJTX"/>" style="color:#FF9933;font-size:13px;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务审核不通过</a></span>
											</td>
										</s:elseif>
										<s:else>
											<td style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;text-decoration:underline;color:#FF9933;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_YJTX"/>" style="color:#FF9933;font-size:13px;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务待处理</a></span>
											</td>
										</s:else>
									</tr>
							</s:iterator>
							<s:if test="indexYJTasks==null || indexYJTasks.size()==0">
								<tr>
									<td  style="font-size:13px; font-weight:bold;">
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;color:#2194E0;margin-left:60px;">
														暂无任务处理!!!
														</span>
									</td>
								</tr>
							</s:if>
			</table>
			</s:if>
			<s:if test="bussiness == @com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_RHTX">
			<table cellspacing="0" width="85%" cellpadding="0" border="0" >
						<tr>
							<td style="font-size:15px;"><span style="font-family:Arial;font-size:13px; font-weight:bold; font-style:normal;text-decoration:none;color:#FF9933;">人行条线</span><img src="images/main/u26_line.png"/></td>
						</tr>
						<s:iterator value="indexRHTasks">
								<tr>
										<s:if test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WAIT">
											<td style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;text-decoration:underline;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_RHTX"/>" style="color:#2194E0;font-size:13px;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务等待</a></span>
											</td>
										</s:if>
										<s:elseif test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REWA">
											<td style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;text-decoration:underline;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_RHTX"/>" style="color:#2194E0;font-size:13px;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务退回等待</a></span>
											</td>
										</s:elseif>
										<s:elseif test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REFU">
											<td style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;text-decoration:underline;color:#FF9933;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_RHTX"/>" style="color:#FF9933;font-size:13px;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务审核不通过</a></span>
											</td>
										</s:elseif>
										<s:else>
												<td style="font-size:14px;"> 
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;text-decoration:underline;">
													<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_RHTX"/>" style="color:#FF9933;font-size:13px;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务待处理</a></span>
												</td>
										</s:else>
									</tr>
							</s:iterator>
						<s:if test="indexRHTasks==null || indexRHTasks.size()==0">
							<tr>
								<td style="font-size:13px;font-weight:bold;">
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;color:#2194E0;margin-left:60px;">
													暂无任务处理!!!
													</span>
								</td>
							</tr>
						</s:if>
			</table>
			</s:if>
			<s:if test="bussiness == @com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_QTTX">
			<table cellspacing="0" width="85%" cellpadding="0" border="0" >
						<tr>
							<td style="font-size:15px;"><span style="font-family:Arial;font-size:13px; font-weight:bold; font-style:normal;text-decoration:none;color:#FF9933;">其他条线</span><img src="images/main/u26_line.png"/></td>
						</tr>
						<s:iterator value="indexQTTasks">
								<tr>
										<s:if test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WAIT">
											<td  style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;text-decoration:underline;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_QTTX"/>" style="color:#2194E0;font-size:13px;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务等待</a></span>
											</td>
										</s:if>
										<s:elseif test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REWA">
											<td  style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;text-decoration:underline;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_QTTX"/>" style="color:#2194E0;font-size:13px;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务退回等待</a></span>
											</td>
										</s:elseif>
										<s:elseif test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REFU">
											<td  style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;text-decoration:underline;color:#FF9933;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_QTTX"/>" style="color:#FF9933;font-size:13px;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务审核不通过</a></span>
											</td>
										</s:elseif>
										<s:else>
												<td style="font-size:14px;"> 
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;text-decoration:underline;">
													<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_QTTX"/>" style="color:#FF9933;font-size:13px;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务待处理</a></span>
												</td>
										</s:else>
									</tr>
							</s:iterator>
						<s:if test="indexQTTasks==null || indexQTTasks.size()==0">
							<tr>
								<td  style="font-size:13px;font-weight:bold;">
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;color:#2194E0;margin-left:60px;">
													暂无任务处理!!!
													</span>
								</td>
							</tr>
						</s:if>
			</table>
			</s:if>
			</div>
			</div>
		</td>
		<td></td>
		<td rowspan="3" > 
		<div style="width:96%;overflow:auto;margin:0px;padding:0px;border:0 solid red;text-align:center; margin-top:-3px; margin-left:-10px;height:100%" >
		<p style="margin-left:-150px;"><img src="images/main/u7_normal.png"></img></p>
		<p style="margin-top:-23;margin-left:-50px;"><img src="images/main/u25_line.png"></img></p>
		<div style="overflow:auto;border:1px solid #CECECE;height:95%;width:195px;margin-top:-20px; margin-left:-50px;">
		<table cellspacing="0" width="98%" cellpadding="0" border="0" style="height:100%;">
						
						<tr>
							<td style="font-family:Arial;font-size:13px;font-style:normal;text-decoration:none;color:#2194E0;">&nbsp;&nbsp;&nbsp;登录ID：<s:property value="op.operatorId"/></td>
						</tr>

						<tr>
							<td style="font-family:Arial;font-size:13px;font-style:normal;text-decoration:none;color:#2194E0;">&nbsp;&nbsp;&nbsp;用户名：<s:property value="op.userName"/></td>
						</tr>
						<tr >
							<td style="font-family:Arial;font-size:13px;font-style:normal;text-decoration:none;color:#2194E0;">&nbsp;&nbsp;&nbsp;所属机构：<s:property value="op.orgName"/></td>
						</tr>
						<s:iterator value="op.roles">
							<tr >
								<td style="font-family:Arial;font-size:13px;font-style:normal;text-decoration:none;color:#2194E0;">&nbsp;&nbsp;&nbsp;所属角色：<s:property value="value"/></td>							
							</tr>
						</s:iterator>
						<tr >
							<td style="font-family:Arial;font-size:13px;font-style:normal;text-decoration:none;color:#2194E0;">&nbsp;&nbsp;&nbsp;联系电话：<s:property value="op.telephone"/></td>
						</tr>
						
					</table>
					</div>
					</div>
		</td>
	</tr>
	<tr>
		<td width="74%"  style="font-size:13px;font-weight:bold;"><font color="#FF9933" style="margin-left:15px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/archives.png"/>&nbsp;公告信息</font></td>
		<td width="1%"></td>
		<td width="24%" style="font-size:13px; font-weight:bold; "></td>
	</tr>
	<tr height="45%">
		<td >
			<div style="width:96%;overflow:auto;margin:0px;padding:0px;text-align:center;margin-top:-2px; margin-left:15px;height:100%" >
				<p style="margin-right:700px"><img src="images/main/u7_normal.png"></img></p>
			    <p style="margin-top:-23px;"><img src="images/main/u6_line.png" style="width: 770px;"></img></p>
				<div style="width:94%;margin-top:-20px;height:90%;overflow:auto; background-color:#FFFFFF; border:1px #CECECE solid;"> 
					<table cellspacing="0" width="90%" cellpadding="5"  border="0" style="margin-top: 30px;">
<%--						<tr height="30px">--%>
<%--							<th width="15%">发件人</th>--%>
<%--							 <th width="60%">标题</th>--%>
<%--							<th width="25%">发送时间</th>--%>
<%--						</tr>--%>
						<s:if test="msgInfoList!=null&&msgInfoList.size()>0">
							<s:iterator value="msgInfoList" id="m">
								<tr  style="cursor: hand;" onclick='getPubMsg(<s:property value="#m.msgId"/>)'>
<%--									<td width="15%"><s:property value="#m.userName"/></td>--%>
									<td align="left" style="color:#2194E0;text-decoration: underline;padding-left:20px;" width="30%"><s:property value="#m.msgTitle"/></td>
									<td align="left" style="color:#2194E0;" width="70%"><s:property value="#m.startTime"/></td>
								</tr>
							</s:iterator>
						</s:if>
						<s:if test="msgInfoList==null||msgInfoList.size()==0">
							<tr>
								<td  colspan="2"  style="font-size:13px;font-weight:bold;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;color:#2194E0;margin-left:60px;">暂无未读消息!!!</span>
								</td>
								
							</tr>
						</s:if>
							
					</table>	
			
			</div>
			</div>
		</td>
		<td></td>
		<td> 
		
		</td>
	</tr>
</table>

</body>
</html>
