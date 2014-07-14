
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
  </head>
  
  <body>
  <%
  String year=(String)request.getAttribute("year");
  String mon=(String)request.getAttribute("mon");
  %>
    <table width="90%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
    <tr class="titletab" id="tbcolor"> 
    	<th height="24" align="center" id="list" colspan="8">��ʾ������Ϣ</th>
  	</tr>
	    <tr class="middle">
		    <td align="center" width="30%">Ӧ������</td>
		    <td align="center">Ӧ������</td>
		    <td align="center">�������</td>
		    <td align="center" width="15%">ƴ�ӻ���������</td>
		    <td align="center" width="15%">ƴ�ӻ��������������</td>
	    </tr>
	    <logic:present name="Records" scope="request">
			<logic:iterate id="viewReportIn" name="Records">
			    <tr bgcolor="#FFFFFF">
			    	<td align="center">
						<bean:write name="viewReportIn" property="orgName" />
		    		</td>
			    	<td align="center">
						<bean:write name="viewReportIn" property="childRepId" />_<bean:write name="viewReportIn" property="versionId" />
		    		</td>
		    		<td align="center">
		    			<logic:equal name="viewReportIn" property="isPass" value="0"><font color="#FF0000">δ����</font></logic:equal>
		    			<logic:equal name="viewReportIn" property="isPass" value="1"><font color="#00CC00">�ѱ���</font></logic:equal>
		    		</td>
		    		<logic:empty name="viewReportIn" property="why">
		    		<td align="center">
		    			--
		    		</td>
		    		<td align="center">
		    			--
		    		</td>
		    		</logic:empty>
		    		<logic:notEmpty name="viewReportIn" property="why">
		    		<td align="center">
		    				<bean:write name="viewReportIn" property="why" />
		    		</td>
		    		<td align="center">
		    			<logic:equal name="viewReportIn" property="times" value="0"><font color="#FF0000">δ����</font></logic:equal>
		    			<logic:equal name="viewReportIn" property="times" value="1"><font color="#00CC00">�ѱ���</font></logic:equal>
		    		</td>
		    		</logic:notEmpty>
		    	</tr>
	    	</logic:iterate>
	    </logic:present>
	    <logic:notPresent name="Records" scope="request">
						<tr align="left">
							<td bgcolor="#ffffff" colspan="8">
								���޷��������ļ�¼
							</td>
						</tr>
		</logic:notPresent>
    </table>
    <div style="text-align:left; margin-top:10px; color:red; width: 90%">
    *�����Ӧ������Ϊƴ�ӻ��ܱ�����ƴ�ӻ�������������ʾ��Ӧ��ƴ�ӻ������������Ϣ��ͬʱ��ʾƴ�ӻ���������ı������������Ϊ�ա�
    </div>
    <br>
    <table>
    	<tr>
	    	<td>
	    	<input type="button" value=" �رմ��� " class="input-button" onclick="javascript:window.close();">
	    	<%--<a href="../viewCollectData.do?year=<%=year%>&term=<%=mon%>">��  ��</a> --%>
	    	</td>
	    </tr>
    </table>
  </body>
</html:html>
