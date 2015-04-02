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
			 	parent.contents.location="report_again_add.jsp";
			 }
		</script>
	</head>
<jsp:include page="../../calendar.jsp" flush="true">
  <jsp:param name="path" value="../../"/>
</jsp:include>
	<body topmargin="0" marginheight="0" leftmargin="0" marginwidth="0">
	<html:form action="/searchReport" method="post">
		<table cellspacing="0" cellpadding="0" border="0" width="98%">
			<tr background="../../image/inside_index_bg4.jpg">
				<td align="left" vAlign="top" width="115"><img src="../../image/query.gif"></td>
				<td background="../../image/search_title_bg.jpg">&nbsp;</td>
				<td align="right" vAlign="middle">
					<input type="button" class="input-button" value=" 查 询 " style="float: right">
				</td>
			</tr>
			<tr>
				<td colspan="3" height="1" background="../../image/line_bg.gif"></td>
			</tr>
			<tr>
				<td colspan="3">
					<table border="0" cellspacing="0" cellpadding="2" width="100%" bgcolor="#FFFFFF">
						<tr>
							<td>报送子行：<html:text property="orgId" size="17" styleClass="input-text"/></td>
							<td colspan="2">模板：<html:select property="repName">
										<html:option value="0">（所有报表名称）</html:option>
									    <html:optionsCollection name="utilForm" property="repList"/>
								</html:select>
							</td>
						</tr>
						<tr>
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
							<td align="right">
								<input type="button" class="input-button" value="新增重报" onclick="_add()">
							</td>
						</tr>
					</table>
					
				</td>
			</tr>
		</table>
		</html:form>
		</body>
</html:html>