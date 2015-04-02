<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/demo.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.4.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript">
		var nodeTest = "";
		$(function(){
			$('#tt2').tree({
				checkbox: false,
				url: 'js/tablename.json',
				onClick:function(node){
					$(this).tree('toggle', node.target);
					//alert('you click '+node.id);
				},
				onContextMenu: function(e, node){
					e.preventDefault();
					$('#tt2').tree('select', node.target);
					nodeTest = node.text;
					if(node.id!="tablename"){
						$('#mm').menu('show', {
							left: e.pageX,
							top: e.pageY
						});
					}
					
				}
			});
		});
		
		function hrefUrl(url){	
			window.parent.mainFrame.location.href=url+"?tablename="+nodeTest;
		}
	</script>
  </head>
  
  <body>
  	
    <ul id="tt2"></ul>
    <div id="mm" class="easyui-menu" style="width:120px;">
    	<div onclick="hrefUrl('<%=request.getContextPath() %>/getResultSetAction!getListBySQL.action')">
    		<span>查询数据</span></a>
    	</div>
		<div>
			<span>创建脚本</span>
			<div style="width:150px;">
				<div onclick="hrefUrl('<%=request.getContextPath() %>/getResultSetAction!getInsertSQL.action')">INSERT SQL</div>
				<div>CREATE TABLE SQL</div>			
			</div>
		</div>
	</div>
  </body>
</html>
