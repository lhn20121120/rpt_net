<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.util.FitechUtil"%>
<%
	String repInIds = request.getParameter("repInIds");
	String flag = request.getParameter("flag");
	String childRepId = request.getParameter("childRepId");
	/**若是WebLogic则需要先进行转码，再进行参数传递*/
	String repName = request.getParameter("repName")!=null ? new String(request.getParameter("repName").getBytes("iso-8859-1"),"gb2312") : null;
	/**若是WebSphere则不要进行转码，直接作为参数传递*/
	//String repName = request.getParameter("repName");
	String frOrFzType = request.getParameter("frOrFzType");
	String year = request.getParameter("year");
	String term = request.getParameter("term");
	String orgId = request.getParameter("orgId");
	String repFreqId = request.getParameter("repFreqId");
	String curPage = request.getParameter("curPage");
	String allFlags = request.getParameter("allFlags");
%>
<html:html locale="true">
<head>
	<html:base />
	<title>
		报表重报
	</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript">
		/**
		 * 返回事件
		 */			
		function _goBack(){
			var qry = "&childRepId=<%=childRepId%>" + 
					  "&repName=<%=repName%>" + 
					  "&frOrFzType=<%=frOrFzType%>" +
					  "&year=<%=year%>" + 
					  "&term=<%=term%>" +
					  "&orgId=<%=orgId%>" + 
					  "&repFreqId=<%=repFreqId%>" +
					  "&curPage=<%=curPage%>";
						
			window.location="<%=request.getContextPath()%>/dateQuary/manualCheckRep.do?" + qry;				
		}
		
		/**
		 * 表单提交验证事件
		 */
		function _submit(form){
			if(isEmpty(form.cause.value)==true){
				alert("请输入重报原因!\n");
				form.cause.focus();
				return false;
			}
			return true;
		}
	</script>
</head>
<jsp:include page="../../calendar.jsp" flush="true">
	<jsp:param name="path" value="../../" />
</jsp:include>
<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<center>
		<table cellspacing="0" cellpadding="0" border="0" width="98%">
			<tr>
				<td height="5"></td>
			</tr>
			<tr>
				<td>
					当前位置 >> 报表管理 >> 报表审核 >> 重报设定
				</td>
			</tr>
			<tr>
				<td height="5"></td>
			</tr>
		</table>
		<br>
		<br>
		<br>
		<br>
		<html:form action="/dateQuary/saveCheckRep" method="post" onsubmit="return _submit(this)">
			<table cellSpacing="1" cellPadding="4" border="0" width="80%" class="tbcolor">
				<tr height="30">
					<td background="../../image/barbk.jpg" align="center" colspan="2">
						重报设定
					</td>
				</tr>
				<TR bgcolor="#FFFFFF">
					<TD>
						重报原因：
					</TD>
					<td>
						<html:textarea property="cause" cols="80" rows="10"></html:textarea>
					</td>
				</tr>
			</table>
			<table cellSpacing="0" cellPadding="0" border="0" width="80%">
				<tr>
					<td height="10"></td>
				</tr>
				<tr>
					<td align="center">
						<input type="button" class="input-button" onclick="_goBack()" value=" 返 回 ">
						&nbsp;
						<html:submit value=" 保存 " styleClass="input-button" />
					</td>
				</tr>
			</table>
			<input type="hidden" name="repInIds" value="<%=repInIds%>">
		  	<input type="hidden" name="flag" value="<%=flag%>">
		  	<input type="hidden" name="childRepId" value="<%=childRepId%>">
		  	<input type="hidden" name="repName" value="<%=repName%>">
		  	<input type="hidden" name="frOrFzType" value="<%=frOrFzType%>">
		  	<input type="hidden" name="year" value="<%=year%>">
		  	<input type="hidden" name="term" value="<%=term%>">
		  	<input type="hidden" name="orgId" value="<%=orgId%>">
		  	<input type="hidden" name="repFreqId" value="<%=repFreqId%>"> 
		  	<input type="hidden" name="allFlags" value="<%=allFlags%>"> 
		  	<input type="hidden" name="curPage" value="<%=curPage%>">
	</html:form>
	</center>
</body>
</html:html>
