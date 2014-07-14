<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import = "com.fitech.gznx.form.PlacardForm"%>
<html:html locale="true">
<html:base />
<head>
	<title>公告查看</title>
	<html:base/>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link href="../../css/tree.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="../../js/tree/tree.js"></script>
	<script type="text/javascript" src="../../js/tree/defTreeFormat.js"></script>
	<script language="javascript" src="../../js/func.js"></script>
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>
	<script language="javascript">
			//更新
			function updatePlacard(placardId)
			{
				document.getElementById("update_placardId").value=placardId;
				document.getElementById("updateForm").submit();
			}
			//查看
			function viewPlacard(placardId)
			{
				document.getElementById("view_placardId").value=placardId;
				document.getElementById("viewForm").submit();
			}
			//用户查看情况
			function viewState(placardId)
			{
				document.getElementById("viewState_placardId").value=placardId;
				document.getElementById("viewStateForm").submit();
			}
			
			//删除公告
			function deletePlacard(placardId)
			{
				if(confirm("您确定删除该公告信息吗?\n")==true)
				{
					document.getElementById("del_placardId").value=placardId;
					document.getElementById("deleteForm").submit();
				}
			}
			
			function _clearForm(){
				
				document.getElementById("title").value="";
				document.getElementById("startDate").value="";
				document.getElementById("endDate").value="";
			}
			
	</script>
	<%
			//取得查询条件
			String queryTerm = "";
			if (request.getAttribute("QueryTerm") != null)
				queryTerm = (String) request.getAttribute("QueryTerm");
			
			PlacardForm QueryplacardForm = (PlacardForm)request.getAttribute("queryPlacardForm");
	%>
</head>
<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
					alert("<bean:write name='Message' property='alertMsg'/>");
				</script>
		</logic:greaterThan>
	</logic:present>
	<table width="90%" border="0" align="center">
		<tr>
			<td height="20">
				当前位置 &gt;&gt; 信息公告 &gt;&gt; 公告查看
			<td>
		</tr>
		<tr>
			<td height="5">
			<td>
		</tr>
	</table>
	<table width="90%" border="0" align="center">
		<tr>
			<td>
				<html:form styleId="form1" action="/placard_mgr/viewPlacardAction" method="Post">
					<fieldset id="fieldset">
						<table width="95%" border="0" align="center">
							<tr>
			<td height="5">
			<td>
		</tr>
							<tr>
								<td align="center">
									公告标题:
									<html:text property="title" size="20" styleClass="input-text" style="text" value="<%=QueryplacardForm.getTitle()%>"/>
								</td>
								<td align="center">
									起始时间：
									<html:text property="startDate" size="10" styleClass="input-text" style="text" readonly="true" value="<%=QueryplacardForm.getStartDate()%>"/>
									<html:img border="0" src="../../image/calendar.gif" onclick="return showCalendar('startDate', 'y-mm-dd');" />
								</td>
								<td align="center">
									结束时间：
									<html:text property="endDate" size="10" styleClass="input-text" style="text" readonly="true" value="<%=QueryplacardForm.getEndDate()%>"/>
									<html:img border="0" src="../../image/calendar.gif" onclick="return showCalendar('endDate', 'y-mm-dd');" />
								</td>
								</tr>
								<tr>
								<td colspan="4" align="right">
									<html:submit styleClass="input-button" value="查  询" />
									&nbsp;
									<html:button property="cancel" value="取  消" styleClass="input-button" onclick="_clearForm()" />
									&nbsp;
									<input type="button" name="addPlacard" value="发布公告" class="input-button" onclick="location.assign('<%=request.getContextPath()%>/placard_mgr/addPlacardAction.do')" />
								</td>
							</tr>
						</table>
					</fieldset>
				</html:form>
			</td>
		</tr>
	</table>
	<table width="90%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
		<tr class="titletab">
			<th align="center" colspan="5">
				信息公告管理
			</th>
		</tr>
		<tr align="center" class="middle">
			<td align="center" width="30%">
				标题
			</td>
			<td align="center" width="20%">
				发布日期
			</td>
			<td align="center" width="15%">
				修 改
			</td>
			<td align="center" width="15%">
				删 除
			</td>
			<td align="center" width="20%">
				用户查询情况
			</td>
		</tr>
		<logic:present name="Records" scope="request">
			<logic:iterate id="item" name="Records" indexId="index">
				<tr bgcolor="#ffffff">
					<td width="20%" align="center">
						<logic:notEmpty name="item" property="title">
							<a href="javascript:viewPlacard('<bean:write name="item" property="placardId"/>')"><bean:write name="item" property="title" /></a>
						</logic:notEmpty>
					</td>
					<td colspan="1" bgcolor="#ffffff" align="center">
						<logic:notEmpty name="item" property="publicDate">
							<bean:write name="item" property="publicDate" />
						</logic:notEmpty>
					</td>
					<td bgcolor="#ffffff" align="center">
						<a href="javascript:updatePlacard('<bean:write name="item" property="placardId"/>')">修 改</a>
					</td>
					<td bgcolor="#ffffff" align="center">
						<a href="javascript:deletePlacard('<bean:write name="item" property="placardId"/>')">删 除</a>
					</td>
					<td bgcolor="#ffffff" align="center">
						<a href="javascript:viewState('<bean:write name="item" property="placardId"/>')"><img src="../../image/preview.gif" border="0" title="机构查看详情" style="cursor:hand"></a>
					</td>
				</tr>
			</logic:iterate>
		</logic:present>
				<logic:notPresent name="Records" scope="request">
			<tr align="left">
				<td bgcolor="#ffffff" colspan="9">
					无匹配记录
				</td>
			</tr>
		</logic:notPresent>
		<!-- 删除-->
		<html:form styleId="deleteForm" action="/placard_mgr/deletePlacardAction" method="post">
			<input type="hidden" id="del_placardId" name="placardId">
			<input type="hidden" name="queryTerm" value="<%=queryTerm%>">
		</html:form>
		<!-- 查看-->
		<html:form styleId="viewForm" action="/placard_mgr/viewPlacardDetailAction" method="post">
			<input type="hidden" id="view_placardId" name="placardId">
			<input type="hidden" name="flag" value="1">
			<input type="hidden" name="queryTerm" value="<%=queryTerm%>">
		</html:form>
		<!-- 更新-->
		<html:form styleId="updateForm" action="/placard_mgr/viewPlacardDetailAction" method="post">
			<input type="hidden" id="update_placardId" name="placardId">
			<input type="hidden" name="flag" value="2">
			<input type="hidden" name="queryTerm" value="<%=queryTerm%>">
		</html:form>
		<!-- 查看情况-->
		<html:form styleId="viewStateForm" action="/placard_mgr/viewPlacardDetailAction" method="post">
			<input type="hidden" id="viewState_placardId" name="placardId">
			<input type="hidden" name="flag" value="3">
			<input type="hidden" name="queryTerm" value="<%=queryTerm%>">
		</html:form>
</body>
</html:html>
