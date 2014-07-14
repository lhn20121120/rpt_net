<%@ page language="java" pageEncoding="GBK"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base />
	<title>日志管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">

	<link href="<%=request.getContextPath()%>/css/common.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/css/calendar-win2k-2.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="<%=request.getContextPath()%>/js/func.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/calendar/calendar.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/calendar/calendar-cn.js"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/calendar/calendar-func.js"></script>
	<script language="javascript">
	//详细信息
	function viewLog(taskDate){
		var url="<%=request.getContextPath()%>/system_mgr/viewLogIn.do?logTypeId=50&userName=Day_Report&operation="+taskDate;
		window.open(url);
	}
	//重跑
	function reRun(taskDate,flag){
		if(flag==2){//确认执行成功的任务是否重新执行
			if(!confirm("已经执行成功的任务重跑时会自动覆盖原有数据，是否继续？")){
				return;
			}
		}
		
		window.location.href="<%=request.getContextPath()%>/dayTask.do?method=reRun&taskDate="+taskDate;
	}
  	</script>
</head>
<body>

	<%-- 错误提示信息 开始--%>
	<logic:present name="Message" scope="request">
		<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
	</logic:present>
	<%-- 错误提示信息 结束--%>
	<%-- 当前位置提示信息 开始--%>
	<table border="0" width="98%" align="center">
		<tr>
			<td height="3"></td>
		</tr>
		<tr>
			<td>
				当前位置 &gt;&gt; 其他报表 &gt;&gt; 报表查询 &gt;&gt; 日报表查询
			</td>
		</tr>
	</table>
	<%-- 当前位置提示信息 结束--%>
	<%-- 查询条件 开始--%>
	<html:form action="/dayTask.do?method=view" method="post" style="margin:0px;">
		<fieldset id="fieldset" style="margin: 0px; padding: 0px; width: 98%">
			<table cellSpacing="0" cellPadding="4" width="100%" border="0" align="left">
				<tr>
					<td width="35%">
						&nbsp;报表时间：
						<html:text property="queryStartTaskDate" size="10" readonly="true" />
						<html:img border="0" src="../image/calendar.gif" onclick="return showCalendar('queryStartTaskDate', 'y-mm-dd');" />
						-
						<html:text property="queryEndTaskDate" size="10" readonly="true" />
						<html:img border="0" src="../image/calendar.gif" onclick="return showCalendar('queryEndTaskDate', 'y-mm-dd');" />
					</td>
					<td>
						&nbsp;报表编号：
						<html:text property="queryTemplateId" size="20" />
					</td>
					<td>
						&nbsp;报表版本：
						<html:text property="queryVersionId" size="20" />
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;报表名称：
						<html:text property="queryTemplateName" size="20" />
					</td>
					<td>
						&nbsp;生成状态：
						<html:select styleId="queryFlag" property="queryFlag">
							<html:option value="-999">--所有状态--</html:option>
							<html:option value="0">未执行</html:option>
							<html:option value="1">正在执行</html:option>
							<html:option value="2">执行成功</html:option>
							<html:option value="-1">执行失败</html:option>
						</html:select>
					</td>
					<td align="center">
						<html:submit styleClass="input-button" value=" 查  询 " />
						&nbsp;&nbsp;
					</td>
				</tr>
			</table>
		</fieldset>
	</html:form>
	<%-- 查询条件 结束--%>
	<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
		<tr>
			<td align="left">
				<!--<input type="button" name="doTaskAgain" value="重新生成数据" class="input-button" onclick="doTaskAgain();"> -->
			</td>
		</tr>
	</table>
	<%-- 列表数据显示 开始--%>
	<table cellSpacing="1" width="98%" border="0" align="center" cellpadding="4" class="tbcolor">
		<tr>
			<th colspan="12">
				日报表生成日志列表
			</th>
		</tr>
		<tr>
			<td class="tableHeader" width="8%">
				报表期数
			</td>
			<td class="tableHeader" width="8%">
				报表编号
			</td>
			<td class="tableHeader" width="">
				模板名称
			</td>
			<td class="tableHeader" width="8%">
				版本号
			</td>
			<td class="tableHeader" width="8%">
				币种
			</td>
			<td class="tableHeader" width="5%">
				频度
			</td>
			<td class="tableHeader" width="8%">
				生成状态
			</td>
			<td class="tableHeader" width="12%">
				开始时间
			</td>
			<td class="tableHeader" width="12%">
				结束时间
			</td>
			<td class="tableHeader" width="10%">
				操作
			</td>
		</tr>
		<logic:present name="Records" scope="request">
			<logic:iterate id="item" name="Records" indexId="id">
				<tr bgcolor="#FFFFFF" align="center">
					<td>
						<bean:write name="item" property="taskDate" />
					</td>
					<td>
						<bean:write name="item" property="templateId" />
					</td>
					<td>
						<bean:write name="item" property="templateName" />
					</td>
					<td>
						<bean:write name="item" property="versionId" />
					</td>
					<td>
						<bean:write name="item" property="curName" />
					</td>
					<td>
						<bean:write name="item" property="repFreqName" />
					</td>
					<td>
						<logic:equal name="item" property="flag" value="0">
							未执行
						</logic:equal>
						<logic:equal name="item" property="flag" value="1">
							正在执行
						</logic:equal>
						<logic:equal name="item" property="flag" value="2">
							执行成功
						</logic:equal>
						<logic:equal name="item" property="flag" value="-1">
							执行失败
						</logic:equal>
					</td>
					<td>
						<bean:write name="item" property="startDate" />
					</td>
					<td>
						<bean:write name="item" property="endDate" />
					</td>
					<td>
						<a href="javascript:viewLog('<bean:write name='item' property='taskDate' />');" style="color: blue">详细信息</a>
						<input type="button" name="bthRun" value="重跑" class="input-button" onclick="reRun('<bean:write name='item' property='taskDate' />',<bean:write name="item" property="flag" />);">
					</td>
				</tr>
			</logic:iterate>
		</logic:present>
		<logic:notPresent name="Records" scope="request">
			<tr bgcolor="#FFFFFF">
				<td colspan="12">
					暂无记录
				</td>
			</tr>
		</logic:notPresent>
	</table>
	<%-- 列表数据显示 结束--%>
	<%-- 分页 开始--%>
	<table cellSpacing="1" cellPadding="0" width="98%" border="0">
		<tr>
			<td>
				<jsp:include page="../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../dayTask.do?method=view" />
				</jsp:include>
			</td>
		</tr>
	</table>
	<%-- 分页 结束--%>
	<br>

</body>
</html:html>

