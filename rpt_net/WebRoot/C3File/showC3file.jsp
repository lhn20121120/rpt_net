
<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
 <html:html>
  <head>
  <html:base/>
  	<title>
	C3报表查询
	</title>
	 <base href="<%=basePath%>">
    <base href="http://127.0.0.1:7001/SMIS_IN/C3File/showC3file.jsp">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" rel="stylesheet" type="text/css">
<jsp:include page="../calendar.jsp" flush="true">
		<jsp:param name="path" value="../" />
</jsp:include>
  </head>
  <script Language="JavaScript">
	function _submit(form)
	{
	if(form._orgName.value==""||form.startDate.value=="")
	{
	alert ("请输入要查询的机构名称或时间范围！");
	return false;
	}
	else
	return true;
}	
</script> 
  <body>
	<html:form action="/Orgname.do" styleId="form1" onsubmit="return _submit(this)">
		<tr>
			<td height="25" id="location" colspan="2">
				<p></p>
			</td>
		</tr>
		
<br>
<br>
<br>
<br>
		<tr>
				<div align="center">
						
								<td width="30%" align="center">
									机构名称：
										 <html:text property="_orgName" size="30" styleClass="input-text" style="text"/></td>
														
								<td width="30%" align="center">
									查看时间：
								</td>
								<td width="30%" align="left">
									<html:text property="startDate" size="10" styleClass="input-text" style="text" />
									<html:img border="0" src="../image/calendar.gif" onclick="return showCalendar('startDate', 'y-mm-dd');"/>
								</td>
								<td width="20%" aligin="left"><html:submit styleClass="input-button" value="查询" />
								</td>
								

    <br>
    <div align="left">	
    	<logic:present name="Records" scope="request">	
			
 <table border="1" width="100%" height="88">
 <tr align="center" class="middle">
				<td width="20%" height="30"><b>迟报报表名称</b></td>
				<td width="4%"><b>频度</b></td>
				<td width="10%"><b>报送范围</b></td>
				<td width="20%"><b>错报报表名称</b></td>
				<td width="4%"><b>频度</b></td>
				<td width="10%"><b>报送范围</b></td>
				<td width="20%"><b>漏报报表名称</b></td>
				<td width="4%"><b>频度</b></td>
				<td width="10%"><b>报送范围</b></td>
</tr>
  
  <logic:iterate id="item" name="Records">	
<tr>
    <td bgcolor="#FFFFFF"><bean:write name="item" property="later_reportname" /></td>
    <td bgcolor="#FFFFFF"><bean:write name="item" property="later_repfreqname" /></td>
     <td bgcolor="#FFFFFF"><bean:write name="item" property="later_dataname" /></td>
    <td bgcolor="#FFFFFF"><bean:write name="item" property="error_reportname" /></td>
    <td bgcolor="#FFFFFF"><bean:write name="item" property="error_repfreqname" /></td>
     <td bgcolor="#FFFFFF"><bean:write name="item" property="error_dataname" /></td>
    <td bgcolor="#FFFFFF"><bean:write name="item" property="leave_reportname" /></td>
    <td bgcolor="#FFFFFF"><bean:write name="item" property="leave_repfreqname" /></td>
     <td bgcolor="#FFFFFF"><bean:write name="item" property="leave_dataname" /></td>
      </tr>
      </logic:iterate>  
</table>
<br>
<br>
<div class="head" align="center">
   第<bean:write name="PAGER" property="currentPage"/>页&nbsp;
   共<bean:write name="PAGER" property="totalPages"/>页&nbsp;
   <html:link action="Orgname1.do?method=queryWithPage&amp;pageMethod=first" paramName="PAGER" paramProperty="currentPage" paramId="currentPage">首页</html:link>
   <html:link action="Orgname1.do?method=queryWithPage&amp;pageMethod=previous" paramName="PAGER" paramProperty="currentPage" paramId="currentPage">上一页</html:link>
   <html:link action="Orgname1.do?method=queryWithPage&amp;pageMethod=next" paramName="PAGER" paramProperty="currentPage" paramId="currentPage">下一页</html:link>
   <html:link action="Orgname1.do?method=queryWithPage&amp;pageMethod=last" paramName="PAGER" paramProperty="currentPage" paramId="currentPage">尾页</html:link>
</div>
 </logic:present>
 <logic:present name="Messages" scope="request">
  <logic:greaterThan name="Messages" property="flag" value="-1">
  <div align="center"><br><br>
      <font color="red" size="3"><bean:write name='Messages' property='alertMsg'/></font>
  </div>
  </logic:greaterThan>
</logic:present>
</html:form>	
</body>
</html:html>

  
	