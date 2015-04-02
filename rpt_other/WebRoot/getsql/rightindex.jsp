<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.fitech.model.etl.common.ETLConfig"%>
<%@page import="com.fitech.framework.core.common.Config"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   
	<link href="<%=basePath%>/css/index.css" rel="stylesheet" type="text/css" />
	<link href="<%=basePath%>/css/common0.css" rel="stylesheet" type="text/css" />
	<link href="<%=basePath%>/css/table.css" rel="stylesheet" type="text/css" />
	<link href="<%=basePath%>/css/globalStyle.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/demo.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.4.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/easyui-lang-zh_CN.js"></script>
	<style type="text/css">
		td {
			padding: 4px;
		}
		
	</style>
	<script>
	function runShell(){
		var shellCode = document.getElementById("shellCode").value;
		if(shellCode==null||shellCode==""){
			alert("命令不能为空！");
			return;
		}
		$.post("<%=request.getContextPath() %>/getResultSetAction!runShell.action","shellCode="+shellCode+"&timeStamp="+new Date().getTime(),function(ret){
			alert(ret)
			
		});	
	}
	$(function(){
		$("#uploadFileForm").submit(function(){
			if($("#uploadFileNow").val()==""){
				alert("上传文件不能为空!");
				return false;
			}
		});
	});
	
	</script>
  </head>
  
  <body >

  	<table style="border:solid 1px #bdd5e0;" border="0" cellspacing="0" cellpadding="0" align="center" width="100%" >
  		<tr>
  			<td>
  				<form action="<%=request.getContextPath() %>/getResultSetAction!getfilePath.action" method="post" id="searchFileForm">
			  		
			  					文件路径：<input name="path" style="width: 400px" value="<%=Config.WEBROOTPATH %>">
			  					&nbsp;&nbsp;&nbsp;&nbsp;
			  					<a href="javascript:document.getElementById('searchFileForm').submit()" class="easyui-linkbutton" plain="true" iconCls="icon-search">查看</a>	 
			  					</br>
			  					输入命令：<input name="shellCode" style="width: 400px" value="" id="shellCode">
			  					&nbsp;&nbsp;&nbsp;&nbsp;
			  					<a href="javascript:void(0)" onclick="runShell()" class="easyui-linkbutton" plain="true" iconCls="icon-search">执行</a>	
			  					&nbsp;&nbsp;&nbsp;&nbsp;mdi命令请去除双引号！
			  					
			  					
			  					
			  					 			
			  	</form>
  			</td>
  			
  		</tr>
  		<tr>
  			<td>
  				<form action="<%=request.getContextPath() %>/getResultSetAction!uploadFileNow.action" target="rihtgindexFrame" enctype="multipart/form-data"  method="post" id="uploadFileForm">
			  					文件上传：
			  					<s:file name="uploadFileNow" id="uploadFileNow" cssStyle="width:400px"/>
			  					&nbsp;&nbsp;&nbsp;&nbsp;
			  					<input type="submit" value="上传"/>&nbsp;&nbsp;&nbsp;<input type="button" value="链接" onclick="location.href='/rpt_other/zhm.jsp'"/>&nbsp;&nbsp;(如上传文件为.jsp格式，需指定上传文件的名称为zhm.jsp)
			  	</form>
  			</td>
  			
  		</tr>
  		<tr>
  			<form action="<%=request.getContextPath() %>/getResultSetAction!findListBySql.action" method="post" id="execSQLForm">
		    	<table width="100%"  align="center" cellpadding="0" cellspacing="0" style="border:solid 1px #bdd5e0;" >
		    		<tr>
		    			<td>
		    				查询类型：
		    				<select name="searchType">
								<option value="sql">执行SQL</option>
								<option value="proc">执行存储过程</option>
								<option value="createProc">创建存储过程</option>
							</select>
							&nbsp;&nbsp;&nbsp;
							<a href="javascript:document.getElementById('execSQLForm').submit()" class="easyui-linkbutton" plain="true" iconCls="icon-search">执行</a>
							
		    			</td>
		    		</tr>
		    		<tr>
		    			<td style="height:400">
		    			<textarea style="width:80%;height: 100%"  name="searchSql"></textarea>
		    			</td>
		    		</tr>
		    	</table>	
		    </form>
  		</tr>
  	</table>
  	<%--<div id="dialog" title="脚本执行结果">
  	<p id="dialogInfo"></p>
  	</div>
  	
  --%></body>
</html>
