<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm" />
<jsp:useBean id="utilFormOrg" scope="page" class="com.fitech.net.common.UtilForm" />

<html:html locale="true">
	<head>
		<html:base/>		
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">		
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
		<script language="javascript">
			/**
			 * 新增重报事件
			 */
			 function _add(){
			 	parent.contents.location="<%=request.getContextPath()%>/report_mgr/report_again_mgr/report_again_add.jsp";
			 }
		</script>
	</head>
<jsp:include page="../../calendar.jsp" flush="true">
  <jsp:param name="path" value="../../"/>
</jsp:include>
	<body topmargin="0" marginheight="0" leftmargin="0" marginwidth="0">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<html:form action="/selReportAgainSetting" method="post">
	  <table border="0" width="98%" align="center">
		 <tr><td height="8"></td></tr>
		 <tr>
			 <td>当前位置 >> 	数据查询 >> 报表重报设定</td>
		</tr>
    	</table>
		<table cellspacing="0" cellpadding="0" border="0" width="98%">
			<tr>
				<td colspan="3"></td>
			</tr>
			<tr>
				<td colspan="3">
					<table border="0" cellspacing="0" cellpadding="2" width="100%">
						<tr>
							<td>
							<table border="0" cellspacing="0" cellpadding="0" width="100%">
							<tr>		
							<td>报送子行：<html:text property="orgName" size="20" styleClass="input-text"/></td>
							<td>
								报送范围：<html:select property="childRepId">
									<html:option value="0">（所有报送子行）</html:option>
									<html:optionsCollection name="utilFormOrg" property="orgCls"/>
								</html:select>
							</td>
							<td>
								时间：<html:text property="startDate" maxlength="10" styleClass="input-text" size="10" onclick="return showCalendar('startDate', 'y-mm-dd');" readonly="true"/>
								-
								<html:text property="endDate" maxlength="10" styleClass="input-text" size="10" onclick="return showCalendar('endDate', 'y-mm-dd');" readonly="true"/>
							</td>
							</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td>
							<table border="0" cellspacing="0" cellpadding="0" width="100%">
							<tr>
							<td>模板：<html:select property="repName">
										<html:option value="0">（所有报表名称）</html:option>
									    <html:optionsCollection name="utilForm" property="repList"/>
								</html:select>
							</td>
							<td align="right">
								<input type="submit" class="input-button" value=" 查 询 " >
							</td>
							</tr>
							</table>
							</td>
						</tr>
					</table>
					
				</td>
			</tr>
		</table>
		</html:form>
		<table width="98%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
			<tr class="titletab">
				<th width="45%" align="center">报表名称</th>
				<th width="15%"  align="center">上报时间</th>
				<th width="20%"  align="center">子行</th>
				<th width="10%"  align="center">状态</th>
				<!-- 
				<th width="15%"  align="center">详细信息</th>
				-->
				<th width="10%"  align="center">重报</th>
			</tr>
			<logic:present name="Records" scope="request">
				<logic:iterate id="item" name="Records">
		    <TR>
				<TD  bgcolor="#ffffff" align="center">
					<a href="<%=request.getContextPath()%>/servlets/ReadPDFReportServlet?repInId=<bean:write name='item' property='repInId'/>" target="_blank">
						<bean:write name="item" property="repName"/>
					</a>
				</TD>
				<TD bgcolor="#ffffff" align="center">
					<bean:write name="item" property="reportDate"/>
				</TD>
				<TD bgcolor="#ffffff" align="center">
					<bean:write name="item" property="orgName"/>
				</TD>
				<TD bgcolor="#ffffff" align="center">
					<font color="red">审核未通过</font>
				</TD>
				<!-- 
				<TD bgcolor="#ffffff" align="center">
					详细信息
				</TD>
				-->
				<TD bgcolor="#ffffff" align="center">
					<a href="../../editReportAgainSetting.do?repInId=<bean:write name="item" property="repInId"/>" >重报</a>
				</TD>
			</TR>
			</logic:iterate>
		</logic:present>
		
		<logic:notPresent name="Records" scope="request">
			<tr align="left" >
				<td bgcolor="#ffffff" colspan="5">
					暂无需重报设定记录
				</td>
			</tr>
		</logic:notPresent>			
		</table>
		<TABLE  cellSpacing="0" cellPadding="0" width="98%" border="0">
			<TR>
				<TD>
					<jsp:include page="../../apartpage.jsp" flush="true">
				  		<jsp:param name="url" value="../../selReportAgainSetting.do"/>
				  	</jsp:include>
				</TD>
			</tr>
		</TABLE>
		</body>
</html:html>