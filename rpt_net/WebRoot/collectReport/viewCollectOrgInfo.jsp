
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
    
    <title>showChildOrg.jsp</title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">    
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link href="../css/common.css" type="text/css" rel="stylesheet">
    <script type="text/javascript">
    	
	    function viewPdf(repInId,templateId,versionId,curId,repFreqId,year,term,orgId){
			 window.open("<%=request.getContextPath()%>/editAFReport.do?statusFlg=1&repInId=" + repInId+"&templateId="+templateId+"&versionId="+versionId+"&curId="+curId+"&repFreqId="+repFreqId+"&year="+year+"&term="+term+"&orgId="+orgId); 
		}
	    
    </script>
  </head>
  
  <body>
  <%
  String year=(String)request.getAttribute("year");
  String mon=(String)request.getAttribute("mon");
  %>
    <table width="90%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
    <tr class="titletab" id="tbcolor"> 
    	<th height="24" align="center" id="list" colspan="8">显示机构信息</th>
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
						<logic:equal name="viewReportIn" property="isPass" value="1">
			  		 <a style="color:blue;" href="javascript:viewPdf('${viewReportIn.repInId }','${viewReportIn.childRepId }',
			    		'${viewReportIn.versionId }','${viewReportIn.curId }','${viewReportIn.actuFreqID }',
			    		'${param.year }','${param.mon }','${viewReportIn.orgId }')">
						<bean:write name="viewReportIn" property="childRepId" />_<bean:write name="viewReportIn" property="versionId" />
						</a>
			    	</logic:equal>
			    	<logic:equal name="viewReportIn" property="isPass" value="0">
			    			<bean:write name="viewReportIn" property="childRepId" />_<bean:write name="viewReportIn" property="versionId" />
			    		</logic:equal>
		    		</td>
		    		<td align="center">
		    			<logic:equal name="viewReportIn" property="isPass" value="0"><font color="#FF0000">未报送</font></logic:equal>
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
    <br>
    <table>
    	<tr>
	    	<td>
	    	<input type="button" value=" 关闭窗口 " class="input-button" onclick="javascript:window.close();">
	    	<%--<a href="../viewCollectData.do?year=<%=year%>&term=<%=mon%>">返  回</a> --%>
	    	</td>
	    </tr>
    </table>
  </body>
</html:html>
