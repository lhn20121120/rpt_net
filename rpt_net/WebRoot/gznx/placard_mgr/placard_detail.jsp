<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html>
	<head>
		<title>公告查看</title>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="<%=request.getContextPath()%>/css/common.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/css/tree.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="../../js/tree/tree.js"></script>
	<script type="text/javascript" src="../../js/tree/defTreeFormat.js"></script>
	<script language="javascript" src="../../js/func.js"></script>
	</head>
	<body>
		<script language="javascript">
		</script>
		<table width="95%" border="0" align="center">
			<tr>
				<td height="20">
					当前位置 &gt;&gt; 信息公告 &gt;&gt; 信息公告详情
				<td>
			</tr>
			<tr>
				<td height="5">
				<td>
			</tr>
		</table>
		<table width="90%" border="0" cellpadding="4" cellspacing="1" class="tbcolor" align="center">
			<tr class="titletab">
				<th align="center" colspan="6">
					公告详情
				</th>
			</tr>
			<tr>
				<td bgcolor="#ffffff" align="center">
					<table width="100%" border="0" align="left" cellpadding="2" cellspacing="1">
						<tr bgcolor="#ffffff">
							<td width="20%" align="right">
								标 题 :
							</td>
							<td width="80%" align="left">
								<input type="text" name="title" class="input-text" size="102" maxlength="20" value="<bean:write name='Records' property='title'/>" readonly>
							</td>
						</tr>
						<tr bgcolor="#ffffff">
							<td valign="top" align="right">
								内 容 :
							</td>
							<td colspan="1" bgcolor="#ffffff" align="left">
								<textarea name="contents" id="contents" class="input-text" rows="10" cols="100" readonly><bean:write name='Records' property='contents' /></textarea>
							</td>
						</tr>
						<tr bgcolor="#ffffff">
							<td align="right">
								附件:
							</td>
							<td align="left">
								<logic:notEmpty name="Records" property="fileId">
									<a href="<%=request.getContextPath()%>/DownloadBlobAction.do?fileId=<bean:write name='Records' property='fileId'/>"><bean:write name="Records" property="fileName" />(<bean:write name="Records" property="fileSizeStr" />)</a>
								</logic:notEmpty>
								<logic:empty name="Records" property="fileId">无</logic:empty>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="right" bgcolor="#ffffff">
								<INPUT type="button" name="back" value="返回" class="input-button" onclick="window.location.assign('../placard_mgr/viewPlacardAction.do')">
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>
