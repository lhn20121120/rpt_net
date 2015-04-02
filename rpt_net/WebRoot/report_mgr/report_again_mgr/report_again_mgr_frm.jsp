<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm" />

<html:html locale="true">
	<head>
		<html:base/>		
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">		
		<link href="../../css/common.css" type="text/css" rel="stylesheet">
		<script language="javascript">
			/**
			 * 新增重报事件
			 */
			 function _add(){
			 	//parent.contents.location="<%=request.getContextPath()%>/report_mgr/report_again_mgr/report_again_add.jsp";
			 //parent.contents.location="<%=request.getContextPath()%>/report_mgr/report_again_mgr/report_again_add.jsp";
			 	parent.contents.location="<%=request.getContextPath()%>/report_mgr/report_again_mgr/repSearch.jsp";
			 
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
	<html:form action="/selForseReportAgain" method="post">
		<table cellspacing="0" cellpadding="0" border="0" width="98%">
			<tr>
				<td height="5"></td>
			</tr>
			<tr>
				 <td>当前位置 >> 	数据审核 >> 报表重报设定</td>
			</tr>
			<tr>
				<td height="5"></td>
			</tr>
			<tr>
				<td colspan="3">
					<table border="0" cellspacing="0" cellpadding="2" width="100%">
						<tr>
							<td>
							<table border="0" cellspacing="0" cellpadding="0" width="80%">
							<tr>
							<td>报送子行：<html:text property="orgName" size="20" styleClass="input-text"/></td>
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
							<td colspan="2">模板：<html:select property="repName">
										<html:option value="0">（所有报表名称）</html:option>
									    <html:optionsCollection name="utilForm" property="repList"/>
								</html:select>
							</td>
							<td align="right">
								<input type="submit" class="input-button" value=" 查 询 ">&nbsp;
								<input type="button" class="input-button" value="新增重报" onclick="_add()">
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
		<table cellSpacing="1" cellPadding="4" width="98%" border="0" class="tbcolor">
			<tr class="titletab">
				<th width="34%" align="center" valign="middle">报 表 名</th>
				<th width="22%" align="center" valign="middle">重报子行</th>
				<th width="12%" align="center" valign="middle">重报设定时间</th>
				<th align="center" valign="middle">重报原因</th>
			</tr>
			<logic:present name="Records" scope="request">
				<logic:iterate id="item" name="Records">
		    <TR bgcolor="#FFFFFF">
			  <TD align="center">
			  	<bean:write name="item" property="repName"/>
			  </TD>
			  <TD align="center">
				<bean:write name="item" property="orgName"/>
			  </TD>
			  <TD align="center">
			    <bean:write name="item" property="setDateStr"/>
			  </TD>
			  <TD align="center">
				<bean:write name="item" property="cause"/>
			  </TD>
			</TR>
			</logic:iterate>
		</logic:present>		
		<logic:notPresent name="Records" scope="request">
			<tr align="left">
				<td bgcolor="#ffffff" colspan="5">
					无匹配记录
				</td>
			</tr>
		</logic:notPresent>			
		</table>
		<TABLE  cellSpacing="0" cellPadding="0" width="98%" border="0">
			<TR>
				<TD>
					<jsp:include page="../../apartpage.jsp" flush="true">
				  		<jsp:param name="url" value="../../selForseReportAgain.do"/>
				  	</jsp:include>
				</TD>
			</tr>
		</TABLE>
		</body>
</html:html>