<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>gszs.jsp</title>
<link href="<%=request.getContextPath() %>/css/table.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath() %>/css/common.css" type="text/css" rel="stylesheet">

<script type="text/javascript" src="<%=request.getContextPath() %>/js/ShowCalendar.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.4.4.min.js"></script>
<link href="<%=request.getContextPath() %>/css/jqueryUI/themes/default/easyui.css"
	rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath() %>/css/jqueryUI/themes/icon.css" rel="stylesheet"
	type="text/css" />
<script src="<%=request.getContextPath() %>/js/jquery.easyui.min.js" type="text/javascript"></script>
</head>

<body>
	<p class="suoyinlan">
		当前位置<span>&gt;&gt;</span>报表管理<span>&gt;&gt;</span>模板查看<span>&gt;&gt;</span>查看详细信息<span>&gt;&gt;</span>查看表内表间关系
	</p>
	<div class="easyui-tabs" >
		<!-- 口径1 -->
		<div title="监管校验" style="padding: 10px 20px" >
			<!-- <table border="0" cellspacing="1" class="tab_content"> -->
			<table border="0" cellspacing="1" class="tbcolor">
				<tr >
					<th width="10%" align="center">序号</th>
					<th align="center">表达式</th>
					<th width="30%" align="center">展示列</th>
					<th width="10%" align="center">类型</th>
				</tr>
				<logic:present name="standardList" scope="request">
						<logic:iterate id="item" name="standardList" indexId="index">
							<tr bgcolor="#FFFFFF">
								<td align="center">
									<%=((Integer) index).intValue() + 1%>
								</td>
								<td align="center">
									<bean:write name="item" property="formulaValue" />
								</td>
								<td align="center">
									<bean:write name="item" property="formulaName" />
								</td>
								<td align="center">
									<logic:equal name="item" property="validateTypeId" value="1">
										<font color="#008066">表内校验</font>
									</logic:equal>
									<logic:equal name="item" property="validateTypeId" value="2">
										<font color="#CC0000">表间校验</font>
									</logic:equal>
								</td>
							</tr>
						</logic:iterate>
					</logic:present>
					<logic:notPresent name="standardList" scope="request">
						<tr>
							<td bgcolor="#FFFFFF" colspan="4">
								无校验公式
							</td>
						</tr>
					</logic:notPresent>
			</table>
		</div>
		<div title="自定义校验" style="padding: 10px 20px">
			<!-- <table border="0" cellspacing="1" class="tab_content"> -->
			<table border="0" cellspacing="1" class="tbcolor">
				<tr >
					<th width="10%" align="center">序号</th>
					<th align="center">表达式</th>
					<th width="30%" align="center">展示列</th>
					<th width="10%" align="center">类型</th>
				</tr>
				<logic:present name="zidingyiList" scope="request">
						<logic:iterate id="item" name="zidingyiList" indexId="index">
							<tr bgcolor="#FFFFFF">
								<td align="center">
									<%=((Integer) index).intValue() + 1%>
								</td>
								<td align="center">
									<bean:write name="item" property="formulaValue" />
								</td>
								<td align="center">
									<bean:write name="item" property="formulaName" />
								</td>
								<td align="center">
									<logic:equal name="item" property="validateTypeId" value="1">
										<font color="#008066">表内校验</font>
									</logic:equal>
									<logic:equal name="item" property="validateTypeId" value="2">
										<font color="#CC0000">表间校验</font>
									</logic:equal>
								</td>
							</tr>
						</logic:iterate>
					</logic:present>
					<logic:notPresent name="zidingyiList" scope="request">
						<tr>
							<td bgcolor="#FFFFFF" colspan="4">
								无校验公式
							</td>
						</tr>
					</logic:notPresent>
			</table>
		</div>
	</div>
</body>
</html>