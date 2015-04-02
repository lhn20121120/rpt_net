<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head> 
    <title></title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    

  </head>
<frameset rows="53,*" framespacing="0" frameborder="no" border="0" scrolling="auto">
	
	<!--<frame src="byg_taskConfigTree2.html" name="leftFrame" scrolling="no" noresize="noresize" id="leftFrame" title="leftFrame" />
	<frame src="sjcq.html" name="mainFrame" id="mainFrame" title="mainFrame">
	</frame>-->
	
	<frame src="byg_head.jsp" name="topFrame" scrolling="no" noresize="noresize" id="topFrame" title="topFrame" />
	<frameset cols="320,*" frameborder="no" border="0" framespacing="0">
		<frame src="<%=basePath%>getResultSetAction!findTableName.action" name="leftFrame" scrolling="yes" noresize="noresize" id="leftFrame" title="leftFrame" />
		<frameset rows="65,*" frameborder="no"  border="0" framespacing="0" >
			<frame src="righthead.jsp" name="rightheadFrame" id="rightheadFrame" title="rightheadFrame" scrolling="no" noresize="noresize" />
			<frame src="rightindex.jsp" name="rightindexFrame" id="rightindexFrame" title="rightindexFrame" scrolling="auto" style="overflow-x: auto" noresize="noresize" />
		</frameset>
	</frameset>
</frameset><noframes></noframes>
<body>
</body>

</html>