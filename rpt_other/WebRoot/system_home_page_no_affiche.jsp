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
	</head>
<body style="background-color:#EEF5F9;overflow:hidden;margin-top:5px;">
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
<br />
<div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" style="margin-left:25px;margin-top:8px;">
	<tr>
		<td width="74%" height="20px" style="font-size:13px;font-weight:bold;"><font color="#FF9933" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/main/u0_normal.gif"/>&nbsp;待办任务</font></td>
		<td width="1%"></td>
		<td width="24%" style="font-size:13px; font-weight:bold; "><font color="#2194E0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/main/u53_normal.gif"/>个人信息</font></td>
	</tr>
	<tr>
		<td  height="370px">
			<div style="width:96%;height:360px;overflow:auto;margin:0px;padding:0px;text-align:center;margin-top:-2px; margin-left:-10px;" >
				<p style="margin-right:700px"><img src="images/main/u7_normal.png"></img></p>
			    <p style="margin-top:-23px;"><img src="images/main/u6_line.png" style="width: 785px;"></img></p>
				<div style="width:94%;height:310px;margin-top:-20px;overflow:auto; background-color:#FFFFFF; border:1px #CECECE solid;"> 
				<s:if test="bussiness == null">
				<table cellspacing="0" width="85%" cellpadding="0" border="0">
						<tr>
							<td height="30px;" style="font-size:13px;font-weight:bold;"><span style="font-family:Arial;font-style:normal;text-decoration:none;color:#FF9933;">银监条线</span><img src="images/main/u26_line.png"/></td><br/>
						</tr>
						<s:iterator value="indexYJTasks">
								<tr>
										<s:if test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WAIT">
											<td height="30px;" style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;color:#2194E0;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_YJTX"/>" style="color:#2194E0;font-size:13px;text-decoration: underline;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务待处理</a></span>
											</td>
										</s:if>
										<s:elseif test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REFU">
											<td height="30px;" style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;color:#FF9933;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_YJTX"/>" style="color:#FF9933;font-size:13px;text-decoration: underline;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务审核不通过</a></span>
											</td>
										</s:elseif>
										<s:else>
											<td height="30px;" style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;color:#FF9933;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_YJTX"/>" style="color:#FF9933;font-size:13px;text-decoration: underline;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务待处理</a></span>
											</td>
										</s:else>
									</tr>
							</s:iterator>
							<s:if test="indexYJTasks==null || indexYJTasks.size()==0">
								<tr>
									<td height="20px;" style="font-size:13px; font-weight:bold;">
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;color:#2194E0;margin-left:60px;">
														暂无任务处理!!!
														</span>
									</td>
								</tr>
							</s:if>
			</table>
				
	
			<table cellspacing="0" width="85%" cellpadding="0" border="0" >
						<tr>
							<td height="30px;" style="font-size:15px;"><span style="font-family:Arial;font-size:13px; font-weight:bold; font-style:normal;text-decoration:none;color:#FF9933;">人行条线</span><img src="images/main/u26_line.png"/></td><br/>
						</tr>
						<s:iterator value="indexRHTasks">
								<tr>
										<s:if test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WAIT">
											<td height="30px;" style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_RHTX"/>" style="color:#2194E0;font-size:13px;text-decoration: underline;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务待处理</a></span>
											</td>
										</s:if>
										<s:elseif test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REFU">
											<td height="30px;" style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;color:#FF9933;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_RHTX"/>" style="color:#FF9933;font-size:13px;text-decoration: underline;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务审核不通过</a></span>
											</td>
										</s:elseif>
										<s:else>
												<td height="30px;" style="font-size:14px;"> 
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;">
													<a href='<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_RHTX"/>' style="color:#FF9933;font-size:13px;text-decoration: underline;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务待处理</a></span>
												</td>
										</s:else>
									</tr>
							</s:iterator>
						<s:if test="indexRHTasks==null || indexRHTasks.size()==0">
							<tr>
								<td height="20px;" style="font-size:13px;font-weight:bold;">
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;color:#2194E0;margin-left:60px;">
													暂无任务处理!!!
													</span>
								</td>
							</tr>
						</s:if>
			</table>
			<table cellspacing="0" width="85%" cellpadding="0" border="0" >
						<tr>
							<td height="30px;" style="font-size:15px;"><span style="font-family:Arial;font-size:13px; font-weight:bold; font-style:normal;text-decoration:none;color:#FF9933;">其他条线</span><img src="images/main/u26_line.png"/></td><br/>
						</tr>
						<s:iterator value="indexQTTasks">
								<tr>
										<s:if test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WAIT">
											<td height="30px;" style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_QTTX"/>" style="color:#2194E0;font-size:13px;text-decoration: underline;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务待处理</a></span>
											</td>
										</s:if>
										<s:elseif test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REFU">
											<td height="30px;" style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;color:#FF9933;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_QTTX"/>" style="color:#FF9933;font-size:13px;text-decoration: underline;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务审核不通过</a></span>
											</td>
										</s:elseif>
										<s:else>
												<td height="30px;" style="font-size:14px;"> 
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;">
													<a href='<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_QTTX"/>' style="color:#FF9933;font-size:13px;text-decoration: underline;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务待处理</a></span>
												</td>
										</s:else>
									</tr>
							</s:iterator>
						<s:if test="indexQTTasks==null || indexQTTasks.size()==0">
							<tr>
								<td height="20px;" style="font-size:13px;font-weight:bold;">
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
							<td height="30px;" style="font-size:13px;font-weight:bold;"><span style="font-family:Arial;font-style:normal;text-decoration:none;color:#FF9933;">银监条线</span><img src="images/main/u26_line.png"/></td><br/>
						</tr>
						<s:iterator value="indexYJTasks">
								<tr>
										<s:if test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WAIT">
											<td height="30px;" style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;text-decoration:underline;color:#2194E0;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_YJTX"/>" style="color:#2194E0;font-size:13px;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务待处理</a></span>
											</td>
										</s:if>
										<s:elseif test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REFU">
											<td height="30px;" style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;text-decoration:underline;color:#FF9933;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_YJTX"/>" style="color:#FF9933;font-size:13px;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务审核不通过</a></span>
											</td>
										</s:elseif>
										<s:else>
											<td height="30px;" style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;text-decoration:underline;color:#FF9933;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_YJTX"/>" style="color:#FF9933;font-size:13px;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务待处理</a></span>
											</td>
										</s:else>
									</tr>
							</s:iterator>
							<s:if test="indexYJTasks==null || indexYJTasks.size()==0">
								<tr>
									<td height="20px;" style="font-size:13px; font-weight:bold;">
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
							<td height="30px;" style="font-size:15px;"><span style="font-family:Arial;font-size:13px; font-weight:bold; font-style:normal;text-decoration:none;color:#FF9933;">人行条线</span><img src="images/main/u26_line.png"/></td><br/>
						</tr>
						<s:iterator value="indexRHTasks">
								<tr>
										<s:if test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WAIT">
											<td height="30px;" style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;text-decoration:underline;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_RHTX"/>" style="color:#2194E0;font-size:13px;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务待处理</a></span>
											</td>
										</s:if>
										<s:elseif test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REFU">
											<td height="30px;" style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;text-decoration:underline;color:#FF9933;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_RHTX"/>" style="color:#FF9933;font-size:13px;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务审核不通过</a></span>
											</td>
										</s:elseif>
										<s:else>
												<td height="30px;" style="font-size:14px;"> 
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;text-decoration:underline;">
													<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_RHTX"/>" style="color:#FF9933;font-size:13px;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务待处理</a></span>
												</td>
										</s:else>
									</tr>
							</s:iterator>
						<s:if test="indexRHTasks==null || indexRHTasks.size()==0">
							<tr>
								<td height="20px;" style="font-size:13px;font-weight:bold;">
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
							<td height="30px;" style="font-size:15px;"><span style="font-family:Arial;font-size:13px; font-weight:bold; font-style:normal;text-decoration:none;color:#FF9933;">人行条线</span><img src="images/main/u26_line.png"/></td><br/>
						</tr>
						<s:iterator value="indexQTTasks">
								<tr>
										<s:if test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WAIT">
											<td height="30px;" style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;text-decoration:underline;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_QTTX"/>" style="color:#2194E0;font-size:13px;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务待处理</a></span>
											</td>
										</s:if>
										<s:elseif test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REFU">
											<td height="30px;" style="font-size:14px;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;text-decoration:underline;color:#FF9933;">
												<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_QTTX"/>" style="color:#FF9933;font-size:13px;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务审核不通过</a></span>
											</td>
										</s:elseif>
										<s:else>
												<td height="30px;" style="font-size:14px;"> 
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<img src="images/main/u13_normal.gif"/>&nbsp;&nbsp;<span style="font-family:Arial;font-weight:normal;font-style:normal;text-decoration:underline;">
													<a href="<%=request.getContextPath() %>/pendingTaskAction.action?tVo.taskTerm=<s:date name="taskDate" format="yyyy-MM-dd"/>&tVo.nodeFlag=<s:property value="nodeFlag"/>&type=index&tVo.busiLine=<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_QTTX"/>" style="color:#FF9933;font-size:13px;"><s:date name="taskDate" format="yyyy-MM-dd"/>期：<s:property value="taskNum"/>个任务待处理</a></span>
												</td>
										</s:else>
									</tr>
							</s:iterator>
						<s:if test="indexQTTasks==null || indexQTTasks.size()==0">
							<tr>
								<td height="20px;" style="font-size:13px;font-weight:bold;">
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
		<td height="350px"> 
		<div style="width:96%;height:360px;overflow:auto;margin:0px;padding:0px;text-align:center; margin-top:-3px; margin-left:-10px;" >
		<p style="margin-left:-150px;"><img src="images/main/u7_normal.png"></img></p>
		<p style="margin-top:-23;margin-left:-50px;"><img src="images/main/u25_line.png"></img></p>
		<div style="overflow:auto;border:1px solid #CECECE;width:195px;height:315px;margin-top:-20px; margin-left:-50px;">
		<table cellspacing="0" width="98%" cellpadding="0" border="0" height="100%">
						
						<tr height="14px;">
							<td style="font-family:Arial;font-size:13px;font-style:normal;text-decoration:none;color:#2194E0;">&nbsp;&nbsp;&nbsp;登录ID：<s:property value="op.operatorId"/></td>
						</tr>

						<tr height="14px;">
							<td style="font-family:Arial;font-size:13px;font-style:normal;text-decoration:none;color:#2194E0;">&nbsp;&nbsp;&nbsp;用户名：<s:property value="op.userName"/></td>
						</tr>
						<tr height="14px;">
							<td style="font-family:Arial;font-size:13px;font-style:normal;text-decoration:none;color:#2194E0;">&nbsp;&nbsp;&nbsp;所属机构：<s:property value="op.orgName"/></td>
						</tr>
						<s:iterator value="op.roles">
							<tr height="14px;">
								<td style="font-family:Arial;font-size:13px;font-style:normal;text-decoration:none;color:#2194E0;">&nbsp;&nbsp;&nbsp;所属角色：<s:property value="value"/></td>							
							</tr>
						</s:iterator>
						<tr height="14px;">
							<td style="font-family:Arial;font-size:13px;font-style:normal;text-decoration:none;color:#2194E0;">&nbsp;&nbsp;&nbsp;联系电话：<s:property value="op.telephone"/></td>
						</tr>
						
					</table>
					</div>
					</div>
		</td>
	</tr>
</table>

	<!-- <table width="100%" cellspacing="0">
	<tr>
			<td style="vertical-align: top">
			<div style="position: relative;">
				<div style="padding-top:15px;padding-left:30px;border-left:1px solid #000000;border-bottom:1px solid #000000;border-right:1px solid #000000;">
					<table border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td><img src="images/main/u0_normal.gif"/>
							<span style="font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#FF9933;">待办任务</span></td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td colspan="2"><img src="images/main/u3_normal.png"/><br/><img src="images/main/u2_line.png"/></td>
						</tr>
					</table>
				</div>
				<div  style=" position:absolute;top:200;left:50;    padding-top:15px;padding-left:30px;background-image:url('images/main/u4_normal.png');background-repeat:no-repeat;   border-left:1px solid #000000;border-bottom:1px solid #000000;border-right:1px solid #000000;padding-top:15px;padding-left:30px;height:300px;overflow:auto;width:800px;">
					<table>
						<tr>
							<td><span style="font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#FF9933;">银监条线</span><img src="images/main/u26_line.png"/></td><br/>
						</tr>
						<tr>
							<td><img src="images/main/u13_normal.gif"/><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:underline;color:#FF9933;">|1104季报|2012-12期：1个任务待处理</span></td>
						</tr>
						<tr>
							<td><img src="images/main/u13_normal.gif"/><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:underline;color:#FF9933;">|1104季报|2012-12期：1个任务待处理</span></td>
						</tr>
						<tr>
							<td><img src="images/main/u13_normal.gif"/><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:underline;color:#FF9933;">|1104季报|2012-12期：1个任务待处理</span></td>
						</tr>
						<tr>
							<td><img src="images/main/u13_normal.gif"/><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:underline;color:#FF9933;">|1104季报|2012-12期：1个任务待处理</span></td>
						</tr>
						<tr>
							<td><img src="images/main/u13_normal.gif"/><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:underline;color:#FF9933;">|1104季报|2012-12期：1个任务待处理</span></td>
						</tr>
						
							<tr>
							<td><span style="font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#FF9933;">人行条线</span><img src="images/main/u26_line.png"/></td><br/>
						</tr>
						<tr>
							<td><img src="images/main/u13_normal.gif"/><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:underline;color:#FF9933;">|1104季报|2012-12期：1个任务待处理</span></td>
						</tr>
						<tr>
							<td><img src="images/main/u13_normal.gif"/><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:underline;color:#FF9933;">|1104季报|2012-12期：1个任务待处理</span></td>
						</tr>
						<tr>
							<td><img src="images/main/u13_normal.gif"/><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:underline;color:#FF9933;">|1104季报|2012-12期：1个任务待处理</span></td>
						</tr>
						<tr>
							<td><img src="images/main/u13_normal.gif"/><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:underline;color:#FF9933;">|1104季报|2012-12期：1个任务待处理</span></td>
						</tr>
						<tr>
							<td><img src="images/main/u13_normal.gif"/><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:underline;color:#FF9933;">|1104季报|2012-12期：1个任务待处理</span></td>
						</tr>
					</table>
				</div>
			</div>
			</td>
			<td style="vertical-align: top">
				<div style="padding-top:15px;padding-left:30px;width:300px;">
					<table border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td><img src="images/main/u53_normal.gif"/>
							<span style="font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#FF9933;">个人信息</span></td>
							<td>&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="2"><img  src="images/main/u3_normal.png"/><br/><img  src="images/main/u25_line.png"/></td>
						</tr>
					</table>
				</div>
				<div style="background-image: url('images/main/u23_normal.png');border-left:1px solid #000000;border-bottom:1px solid #000000;border-right:1px solid #000000;padding-top:15px;padding-left:10px;height: 300px;overflow:auto;width:190px;">
					<table cellspacing="30" cellpadding="0" style="">
						<tr>
							<td style="font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#2194E0;">登录ID：XXX</td>
						</tr>
						
						<tr>
							<td style="font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#2194E0;"><span style="font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#2194E0;">用户名：XXX</span></td>
						</tr>
						<tr>
							<td style="font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#2194E0;"><span style="font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#2194E0;">所属机构：XXX</span></td>
						</tr>
						<tr>
							<td style="font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#2194E0;"><span style="font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#2194E0;">所属角色：XXX</span></td>
						</tr>
						<tr>
							<td style="font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#2194E0;"><span style="font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#2194E0;">联系电话：XXX</span></td>
						</tr>
					</table>
				</div>
			</td>
	</tr>
	</table> -->						
</body>
</html>
