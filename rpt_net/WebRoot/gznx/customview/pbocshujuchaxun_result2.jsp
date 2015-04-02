<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'shujuchaxun_result2.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	 <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/script/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/script/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/script/demo.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.4.2.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/script/jquery.easyui.min.js"></script>
	<style type="text/css">
		#innerFinalDIV td,#findLastDIV td{
			border:1px dotted #c4c4c4;
			border-top:0px;
			padding:4px 0px 4px 0px;
			border-right:0px;
			font-size:12px;
		}
		#innerFinalDIV th,#findLastDIV th{
			background-color:#F7F7F7;
			font-size:14px;
			font-family:宋体,Arial;
			font-weight:normal;
			border-left:1px dotted #c4c4c4;
			border-bottom:1px dotted #c4c4c4;
			padding:4px 4px 4px 4px;
		}
	</style>
	<script type="text/javascript">
		function changeBgColor(obj){
			var trName = $(obj).attr("name");
			
			$("#dataTable tr[name='"+trName+"']").css("backgroundColor","#FBEC88");
			$("#dataTable tr[name!='"+trName+"']").css("backgroundColor","white");
		}
		
		function changeToLavender(obj){
			obj.bgColor="lavender";
		}
		function changeToWhite(obj){
			
			obj.bgColor="#FFFFFF";
		}
	</script>
  </head>
  
  <body>
  <div id="findLastDIV" style="overflow: auto;width:100%;height:100%;border:none;position: absolute; ">
   <table style='width:100%;' id="dataTable" align='center' align='center' cellpadding='0' cellspacing='0' >
   							<tr>
								<th colspan="10" align="left" style="border-top:1px dotted #c4c4c4;border-right:1px dotted #c4c4c4;background-color: #E5EEFF">
									<a href="javascript:location.href='<%=request.getContextPath() %>/searchPBOCDataInitAction.do'" class="easyui-linkbutton"  plain="true" iconCls="icon-back">返回</a>
									
									<a href="<%=request.getContextPath() %>/downLoadSearchDataAction.do" class="easyui-linkbutton" plain="true" iconCls="icon-print">导出EXCEL</a>
								</th>
							</tr>
							<tr align='center' onmouseover="this.backgroundColor='#EEEEFF'" onmouseout="this.backgroundColor='white'">
								<th>报表名称</th>
								<th>单元格</th>
								<th>单元格名称</th>
								<th>机构</th>
								<th>币种</th>
								<th style="border-right:1px dotted #c4c4c4">期数</th>
								<th>本期</th>
								<th>上期</th>
								<th>年初</th>
								<th>去年同期</th>
								
							</tr>
							<%
								List<Object[]> objList = null;
								if(request.getAttribute("objList")!=null)
									objList = (List<Object[]>)request.getAttribute("objList");
								session.setAttribute("objList",objList);
								if(objList!=null && objList.size()>0){
									for(Object[] obj : objList){
										
							%>
									<!--  <tr name="<%=obj[1] %>" align='center' onmouseover="this.style.backgroundColor='#EEEEFF'" onmouseout="this.style.backgroundColor='white'" onclick="changeBgColor(this)">-->
									<tr name="<%=obj[1] %>" align='center' bgcolor="#FFFFFF"  onmouseover="changeToLavender(this)" onmouseout="changeToWhite(this)">
										<td><%=obj[8] %></td>
										<td><%=obj[9] %></td>
										<td><%=obj[1] %></td>
										<td><%=obj[0] %></td>
										<td><%=obj[3] %></td>
										<td><%=obj[2] %></td>
										<td><%=obj[4] %></td>
										<td><%=obj[5] %></td>
										<td><%=obj[6] %></td>
										<td><%=obj[7] %></td>
										
									</tr>
							<%
									}
								}else{
									
							%>
								<tr align='center' onmouseover="this.style.backgroundColor='#EEEEFF'" onmouseout="this.style.backgroundColor='white'">
									<td colspan="10">暂无信息</td>
								</tr>
							<%
								}
							%>
							
							
							<%-- 
							<tr align='center' onmouseover="this.style.backgroundColor='#EEEEFF'" onmouseout="this.style.backgroundColor='white'" onclick="checkedData(this)">
								<td>南京分行</td>
								<td>2.0</td>
								<td>3.0</td>
								<td>4.0</td>
								<td>3.0</td>
								<td>2.0</td>
								<td>5.0</td>
								<td>1.0</td>
							</tr>
							<tr align='center' onmouseover="this.style.backgroundColor='#EEEEFF'" onmouseout="this.style.backgroundColor='white'" onclick="checkedData(this)"> 
								<td>南京分行</td>
								<td>2.0</td>
								<td>3.0</td>
								<td>4.0</td>
								<td>4.0</td>
								<td>3.0</td>
								<td>2.0</td>
								<td>2.0</td>
							</tr>
							<tr align='center' onmouseover="this.style.backgroundColor='#EEEEFF'" onmouseout="this.style.backgroundColor='white'" onclick="checkedData(this)">
								<td>上海分行</td>
								<td>2.0</td>
								<td>11.0</td>
								<td>3.0</td>
								<td>5.0</td>
								<td>3.0</td>
								<td>5.0</td>
								<td>1.0</td>
							</tr>
							--%>
						</table>
						</div>
  </body>
</html>
