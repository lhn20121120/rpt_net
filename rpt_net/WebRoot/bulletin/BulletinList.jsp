<%@ page language="java" pageEncoding="GB2312"%>
<jsp:directive.page import="java.sql.Connection"/>
<jsp:directive.page import="com.cbrc.smis.dao.DBConn"/>
<jsp:directive.page import="java.sql.ResultSet"/>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
	<html:base />
	<title>公告管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" type="text/css" rel="stylesheet">
<script type="text/javascript">
function _new(){
			window.location="<%=request.getContextPath()%>/bulletin/BulletinNew.jsp" ; 
		}
		function _mod(id){
			window.location="<%=request.getContextPath()%>/bulletin/BulletinModfy.jsp?bullId="+id ; 
		}
		function _delete(id){
		if(confirm('确定要删除该公告吗？'))
			window.location="<%=request.getContextPath()%>/bulletin/BulletinDo.jsp?type=delete&bullId="+id ; 
		}
		function _view(id){
	window.open('<%=request.getContextPath()%>/bulletin/BulletinView.jsp?bullId='+id,'newwindow','height=420,width=510,top=70,left=42,toolbar=no,menubar=no,scrollbars=yes,resizable=no,location=no,status=no'); //弹出窗口
}
</script>
</head>
<body>



	<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 >> 公告管理
			</td>
			<td>
				<input type="button"  class="input-button" value="新增公告" onclick="_new()">
			</td>
			
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>

	
	<br />
	<form name="form1">
		
		<table width="98%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
			<tr class="titletab" id="tbcolor">
				<th height="24" align="center" id="list" colspan="10">
					公告列表
				</th>
			</tr>
			<tr>
			<TR class="middle">
				<TD  align="center">
					<strong>公告标题</strong>
				</TD>
				<TD  align="center">
					<strong>日期</strong>
				</TD>
				<TD  align="center">
					<strong>状态</strong>
				</TD>
				<TD  align="center">
					<strong>操作</strong>
				</TD>
				
			</TR>
				<%

				DBConn dbconn=new DBConn();
				try{
	
		Connection conn=dbconn.openSession().connection();
		ResultSet rs=conn.createStatement().executeQuery("select BULL_ID,BULL_TITLE,BULL_STATE,ADD_TIME from BULLETIN order by ADD_TIME desc");
		while(rs.next()){
			
		%>

					<tr bgcolor="#FFFFFF">
						<td align="center">
							<%=rs.getString("BULL_TITLE") %>
						</td>
						<td align="center">
						<%=rs.getString("ADD_TIME") %>
						</td>
						<td align="center">
						<%=rs.getString("BULL_STATE").equals("1")?"登陆提示":"登陆不提示" %>
						</td>
						<td align="center">
						<input type="button"  class="input-button" value="查看" onclick="_view(<%=rs.getString("BULL_ID") %>)">
						<input type="button"  class="input-button" value="修改" onclick="_mod(<%=rs.getString("BULL_ID") %>)">
						<input type="button"  class="input-button" value="删除" onclick="_delete(<%=rs.getString("BULL_ID") %>)">
						</td>
					</tr>

<%
		}
		}catch(Exception e){
			e.printStackTrace();
} finally{
	dbconn.closeSession();
}%>
		</table>
		<br />
	</form>


</body>
</html:html>
