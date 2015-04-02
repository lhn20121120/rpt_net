<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/demo.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.4.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/easyui-lang-zh_CN.js"></script>
	
	  <script type="text/javascript"> 
         function changePage() 
         { 
            parent.rightindexFrame.location.href("rightindex.jsp"); 
          } 
     </script> 
  </head>
  
  <body>
    <table  width="100%" border="0" cellpadding="0" cellspacing="0" style="background-color:#fafafa;border :1px solid Silver; height:40px;padding:0px;margin:0px">
		<tr>
			<td >
				&nbsp;&nbsp;&nbsp;<a href="javascript:changePage()" class="easyui-linkbutton"  plain="true" iconCls="icon-back" style="border:1px solid;margin-top:4px">返回</a>&nbsp;
				
			</td>
		</tr>
	</table>
  </body>
</html>
