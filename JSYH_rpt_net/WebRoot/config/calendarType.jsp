<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
	<head>
	<html:base/>
    <title>日历类型设定</title>    
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
    <link href="../css/common.css" type="text/css" rel="stylesheet">
  </head>
  
  <body>
    <table border="0" width="90%" align="center">
		<tr><td height="8"></td></tr>
		<tr>
			 <td>当前位置 >> 	参数设定 >> 日历类型设定</td>
		</tr>
		<tr><td height="10"></td></tr>
	</table>
	<html:form method="post" action="/config/ViewCalendarType">
		<TABLE border="0" width="80%" align="center" cellpadding="0" cellspacing="0">
		<TR>
			<TD align="left" valign="middle">关键字：</TD>
			<TD align="left" valign="middle">
				<input type="text" size="40" class="input-text" value=""/>
			</TD>
			<TD align="center" valign="middle">
				<input type="submit" class="input-button" value="查询">&nbsp;
				<input type="button" class="input-button" value="新增">
			</TD>
		</TR>
		<tr><td colspan="3" height="10"></td></tr>
	</TABLE>
	
		<TABLE border="0" width="80%" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
			<TR class="tableHeader">
				<TD width="50" align="center" valign="bottom">
					类型值
				</TD>
				<TD width="350" align="center" valign="bottom">
					日历类型名称
				</TD>
				<TD width="100" align="center" valign="bottom">
					操作
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD align="center" valign="bottom">
					1
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						工作日
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						<a href="">编辑</a>&nbsp;<a href="">修改</a>
					</FONT>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD align="center" valign="bottom">
					2
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						非工作日
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						编辑&nbsp;修改
					</FONT>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD align="center" valign="bottom">
					3
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						。。。
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						编辑&nbsp;修改
					</FONT>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD align="center" valign="bottom">
					<FONT size="2">
						4
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						。。。
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						编辑&nbsp;修改
					</FONT>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD align="center" valign="bottom">
					<FONT size="2">
						5
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						。。。
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						编辑&nbsp;修改
					</FONT>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD align="center" valign="bottom">
					<FONT size="2">
						6
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						。。。
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						编辑&nbsp;修改
					</FONT>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD align="center" valign="bottom">
					<FONT size="2">
						7
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						。。。
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						编辑&nbsp;修改
					</FONT>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD align="center" valign="bottom">
					<FONT size="2">
						8
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						。。。
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						编辑&nbsp;修改
					</FONT>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD align="center" valign="bottom">
					<FONT size="2">
						9
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
					。。。
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						编辑&nbsp;修改
					</FONT>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD align="center" valign="bottom">
					<FONT size="2">
						10
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
					。。。
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						编辑&nbsp;修改
					</FONT>
				</TD>
			</TR>
		</TABLE>
		
		<table align="center" border="0" width="80%" cellspacing="0" cellpadding="0">
			<TR>
				<TD>
					<jsp:include page="../apartpage.jsp" flush="true">
					  	<jsp:param name="url" value=""/>
					</jsp:include>
				</TD>
			</TR>
		</table>
		</html:form>
  </body>
</html:html>

