
<%@ page language="java" pageEncoding="gb2312"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ page import="java.util.List"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html lang="true">
  <head>
    <html:base />
    
    <title>汇总-机构报送情况</title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">    
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link href="../../css/common.css" type="text/css" rel="stylesheet">
  </head>
  
  <body>
  <%
  String date=(String)request.getAttribute("date");
  %>
  <br/>
    <table width="90%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
    <tr class="titletab" id="tbcolor"> 
    	<th height="24" align="center" id="list" colspan="8">需汇总机构报送信息</th>
  	</tr>
	    <tr class="middle">
		    <td align="center">应报机构</td>
		    <td align="center">应报报表</td>
		    <td align="center">报送情况</td>
		    
	    </tr>
	    <logic:present name="Records" scope="request">
			<logic:iterate id="viewReportIn" name="Records">
			    <tr bgcolor="#FFFFFF">
			    	<td align="center">
						<bean:write name="viewReportIn" property="orgName" />
		    		</td>
			    	<td align="center">
						<bean:write name="viewReportIn" property="childRepId" />
		    		</td>
		    		<td align="center">
		    			<logic:notEqual name="viewReportIn" property="isPass" value="1"><font color="#FF0000">未报送</font></logic:notEqual>
		    			<logic:equal name="viewReportIn" property="isPass" value="1"><font color="#00CC00">已报送</font></logic:equal>
		    		</td>
		    	</tr>
	    	</logic:iterate>
	    </logic:present>
	    <logic:notPresent name="Records" scope="request">
						<tr align="left">
							<td bgcolor="#ffffff" colspan="8">
								暂无符合条件的记录
							</td>
						</tr>
		</logic:notPresent>
    </table>
    <table>
    	<tr>
	    	<!-- <td><a href="<%=request.getContextPath()%>/viewCollectNX.do?date=<%=date%>">返  回</a></td> -->
	    	<td><input type="button" class="input-button" onclick="javascript:window.close();" value="关闭窗口"></td>
	    </tr>
    </table>
  </body>
</html:html>
