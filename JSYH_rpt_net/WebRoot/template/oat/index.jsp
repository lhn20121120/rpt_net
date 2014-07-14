<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>

<html:html locale="true">
<head>
	<html:base/>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript">
		/**
		 * 新增事件
		 */
		 function _add(){
		 	window.location="add.jsp";
		 }
		
		function deleteOAT(OATNo)
		{
			if(confirm("你确定要删除该频度类别信息吗?"))
				window.location = "../oat/OATDelete.do?OATId="+OATNo;
			
		}
	
	</script>
</head>
<body bgcolor="#FFFFFF">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
					alert("<bean:write name='Message' property='alertMsg'/>");
				</script>
		</logic:greaterThan>
	</logic:present>
	<table border="0" cellpadding="0" cellspacing="0" width="96%">
		<tr><td height="5"></td></tr>
		<tr>
			<td height="10">
				当前位置 >> 模板管理 >> 频度类别设定
			</td>
		</tr>
		<tr><td height="5"></td></tr>
		<tr>
			<td align="right">
				<input type="button" value=" 新 增 " class="input-button" onclick="_add()">&nbsp;
			</td>
		</tr>
		<tr><td height="5"></td></tr>
	</table>
	<TABLE  cellSpacing="1" cellPadding="4" width="96%" border="0" class="tbcolor" align="center">
		<TR class="middle">
			<TD align="center" width="10%">
				序号
			</td>
			<TD align="center" width="30%">
				频度类别名称
			</td>
			<TD align="center" nowrap>
				备注
			</td>
			<TD align="center" width="20%">
				操作
			</td>
		</TR>
		<logic:present name="Records" scope="request">
			<%int i = 1;%>
			<logic:iterate id="item" name="Records">
				<tr  align="center" >
					<td bgcolor="#ffffff">
						<%out.println(i++);%>
					</td>
					<td bgcolor="#ffffff">
						<bean:write name="item" property="OATName" />
					</td>
					<td bgcolor="#ffffff">
						<bean:write name="item" property="OATMemo" />
					</td>
					<td bgcolor="#ffffff">
						<a href="../oat/OATViewUpdate.do?OATId=<bean:write name="item" property="OATId"/>">修改</a>
						&nbsp;
						<a href="javascript:deleteOAT(<bean:write name="item" property="OATId"/>)">删除</a>
					</td>
				</tr>
			</logic:iterate>
		</logic:present>
		<logic:notPresent name="Records" scope="request">
			<tr>
				<td colspan="4" bgcolor="#FFFFFF">
					暂无频度类别信息
				</td>
			</tr>
		</logic:notPresent>
	</table>
	<table cellSpacing="1" cellPadding="4" width="96%" border="0">
		<tr>
			<TD bgcolor="#FFFFFF">
				<jsp:include page="../../apartpage.jsp" flush="true">
					<jsp:param name="url" value="OATInit.do" />
				</jsp:include>
			</TD>
		</tr>
	</table>
</body>
</html:html>
