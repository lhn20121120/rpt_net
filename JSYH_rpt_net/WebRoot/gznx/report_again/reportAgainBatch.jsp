<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
	String repInIds = request.getParameter("repInIds") != null ? request.getParameter("repInIds") : "";
	String templateId = request.getParameter("templateId") != null ? request.getParameter("templateId") : "";
	/**若是WebLogic则需要先进行转码，再作为参数传递*/
	//String repName = request.getParameter("repName")!=null ? new String(request.getParameter("repName").getBytes("iso-8859-1"),"gb2312") : null;
	/**若是WebSphere则不要进行转码，直接作为参数传递*/
	String repName = request.getParameter("repName");
	String bak1 = request.getParameter("bak1") != null ? request.getParameter("bak1") : "";
	String repFreqId = request.getParameter("repFreqId") != null ? request.getParameter("repFreqId") : "";
	String date = request.getParameter("date") != null ? request.getParameter("date") : "";	
	String orgId = request.getParameter("orgId") != null ? request.getParameter("orgId") : "";
	String curPage = request.getParameter("curPage") != null ? request.getParameter("curPage") : "";
%>
<html:html locale="true">
<head>
	<html:base />
	<title>新增重报</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript">
		/**
		 * 返回上级页面事件
		 */
		function _back(){
		 	var _date = "<%=date%>";
			 	
		 	var qry = "childRepId=<%=templateId%>" + 
		 			  "&repName=<%=repName%>" + 
		 			  "&frOrFzType=<%=bak1%>" + 
		 			  "&repFreqId=<%=repFreqId%>" + 
		 			  "&orgId=<%=orgId%>" + 
		 			  "&curPage=<%=curPage%>";
			 	

		  	if(_date.value != "")
		  		qry += "&date=" + _date;
			  			
		 	window.location="<%=request.getContextPath()%>/report/repNXSearch.do?" + qry;			 		
		 }
			
		/**
	     * 表单提交验证
		 */
		function _validate(form){
			if(form.cause.value.length == 0){
				alert("请输入重报的原因!\n");
				return false;
			}
			document.getElementById("smSave").disabled = true;
			return true;
		}
		</script>
</head>
<body>

	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>


	<table cellspacing="0" cellpadding="0" border="0" width="98%">
		<tr>
			<td height="5"></td>
		</tr>
		<tr>
			<td>
				当前位置 &gt;&gt; 报表处理 &gt;&gt; 重报管理 &gt;&gt; 批量重报设置
			</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>

	<br>
	<br>

	<html:form action="/report/batchAgainNX.do" method="post" onsubmit="return _validate(this)">
		<table cellSpacing="1" cellPadding="4" border="0" width="80%" class="tbcolor">
			<tr height="30">
				<td background="../../image/barbk.jpg" align="center" colspan="2">
					报表批量重报设置
				</td>							
			</tr>
			  <tr bgcolor="#FFFFFF">
				<td valign="top">
					批量重报原因：
				</td>
				<td>
					<html:textarea property="cause" cols="80" rows="10" />					
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td colspan="2">			  
					<html:errors/>
			    </td>
			</tr>
		</table>

		<table cellSpacing="0" cellPadding="0" border="0" width="80%">
			<tr>
				<td height="10"></td>
			</tr>
			<tr>
				<td align="center">
					<html:submit styleId="smSave" value="保  存" styleClass="input-button" />
					&nbsp;
					<input type="button" class="input-button" onclick="_back()" value="返  回">
				</td>
			</tr>
		</table>
		<input type="hidden" name="repInIds" value="<%=repInIds%>"/>
		<input type="hidden" name="templateId" value="<%=templateId%>"/>
		<input type="hidden" name="repName" value="<%=repName%>"/>
		<input type="hidden" name="bak1" value="<%=bak1%>"/>
		<input type="hidden" name="repFreqId" value="<%=repFreqId%>"/>
		<input type="hidden" name="orgId" value="<%=orgId%>"/>
		<input type="hidden" name="curPage" value="<%=curPage%>"/>
		<%if(date != null && !date.equals("")){%>
			<input type="hidden" name="date" value="<%=date%>"/>
		<%}%>
	</html:form>
</body>
</html:html>
