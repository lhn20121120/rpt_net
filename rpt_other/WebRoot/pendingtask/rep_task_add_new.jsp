<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>重报任务（批量报表）</title>
	
	<link href="<%=request.getContextPath() %>/css/index.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath() %>/css/common0.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath() %>/css/table.css" rel="stylesheet" type="text/css" />
	
    <script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.4.min.js"></script>
	<script type="text/javascript">
		$(function(){
			$("#fm").submit(function(){
				if($("#returndesc").val()==""){
					alert("请输入重报原因!");
					return false;
				}
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
			});
		})
	</script>
</head>

<body onload="init()">
<div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td nowrap="nowrap">
    <div class="toolbar">
    <div class="tableinfo"><span><img src="<%=request.getContextPath() %>/images/icon01.gif" alt="列表" /></span><b style="font-size:12px;">当前位置 >>报表处理 >>重报原因</b></div>
    </div>
    </td>
  </tr>
</table>
</div>
<div style="width:60%;"><fieldset style="background-color:white;" >
<legend>重报原因输入栏</legend>
<form id="fm" action="<%=request.getContextPath() %>/pendingTaskAction!newCommiRepTask.action" method="post">
<table width="90%" border="0" cellspacing="0" cellpadding="0"   align="center">
	<tr height="10px"><td colspan="3"></td></tr>
	<tr height="200px">
   		<td colspan="3">
   			<input name="str" type="hidden" value="<s:property value="str"/>"/>
   			<input type="hidden" name="taskName" value="<s:property value='taskName'/>"/>
   			<input type="hidden" name="taskTaget" id="taskTaget" value="<s:property value='taskTaget'/>" />
			<input type="hidden" name="tasktemplateIds"  value="<s:property value='tasktemplateIds'/>"/>
			<input type="hidden" name="wMoni.id.taskMoniId" value="<s:property value='wMoni.id.taskMoniId'/>"/>
			<input type="hidden" name="wMoni.id.nodeId" value="<s:property value='wMoni.id.nodeId'/>"/>
			<input type="hidden" name="wMoni.id.orgId" value="<s:property value='wMoni.id.orgId'/>"/>
   			<textarea id="returndesc" name="pendingTaskQueryConditions.returnDesc" style="width:100%;height:90%;" rows="" cols=""></textarea>
   		</td>
  </tr>
  <tr height="20px">
  	<td colspan="3" >
  	<s:if test="repType!=null && repType==1">
  	&nbsp;&nbsp;&nbsp;重报时间:
  	<input type="text" name="repDay" id="repDay"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  	</s:if>
  	<input name="btn" type="submit" class="searchbtn" value="确定" /></td> 
  </tr>
  </table>
</form>
　</fieldset>
</div>
</body>
</html>