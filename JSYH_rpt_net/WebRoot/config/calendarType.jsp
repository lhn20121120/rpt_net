<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
	<head>
	<html:base/>
    <title>���������趨</title>    
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
			 <td>��ǰλ�� >> 	�����趨 >> ���������趨</td>
		</tr>
		<tr><td height="10"></td></tr>
	</table>
	<html:form method="post" action="/config/ViewCalendarType">
		<TABLE border="0" width="80%" align="center" cellpadding="0" cellspacing="0">
		<TR>
			<TD align="left" valign="middle">�ؼ��֣�</TD>
			<TD align="left" valign="middle">
				<input type="text" size="40" class="input-text" value=""/>
			</TD>
			<TD align="center" valign="middle">
				<input type="submit" class="input-button" value="��ѯ">&nbsp;
				<input type="button" class="input-button" value="����">
			</TD>
		</TR>
		<tr><td colspan="3" height="10"></td></tr>
	</TABLE>
	
		<TABLE border="0" width="80%" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
			<TR class="tableHeader">
				<TD width="50" align="center" valign="bottom">
					����ֵ
				</TD>
				<TD width="350" align="center" valign="bottom">
					������������
				</TD>
				<TD width="100" align="center" valign="bottom">
					����
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD align="center" valign="bottom">
					1
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						������
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						<a href="">�༭</a>&nbsp;<a href="">�޸�</a>
					</FONT>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD align="center" valign="bottom">
					2
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						�ǹ�����
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						�༭&nbsp;�޸�
					</FONT>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD align="center" valign="bottom">
					3
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						������
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						�༭&nbsp;�޸�
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
						������
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						�༭&nbsp;�޸�
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
						������
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						�༭&nbsp;�޸�
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
						������
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						�༭&nbsp;�޸�
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
						������
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						�༭&nbsp;�޸�
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
						������
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						�༭&nbsp;�޸�
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
					������
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						�༭&nbsp;�޸�
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
					������
					</FONT>
				</TD>
				<TD align="center" valign="bottom">
					<FONT size="2">
						�༭&nbsp;�޸�
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

