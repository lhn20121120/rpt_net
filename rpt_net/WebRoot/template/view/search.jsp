<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
	<head>
		<html:base/>
		<title>����ģ���������</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">		
	</head>
	<body topmargin="0" marginheight="0" leftmargin="0" marginwidth="0">
		<table cellspacing="0" cellpadding="0" border="0" style="BACKGROUND-IMAGE: url(../../image/inside_index_bg4.jpg)"
			height="48" width="100%">
						<TR>
							<TD width="111">
								<img border="0" src="../../image/inside_index_mid15.jpg" width="111" height="48"></TD>
							<TD width="9" valign="bottom">
								��</TD>
							<TD width="3" background="../../image/inside_index_bg4_split.jpg">
							</TD>
							<TD width="106" valign="bottom">
								<p align="right" style="margin-top: 0px; margin-bottom: -2px">ģ�����ƣ�</p>
								<p align="right" style="margin-top: -2px; margin-bottom: -2px">��</p>
							</TD>
							<TD width="187" valign="bottom">
								<p align="center" style="margin-top: 0px; margin-bottom: -2px"><font face="Arial">
										<span style="FONT-SIZE: 9pt">
											<input class="input-text" id="Text1" type="text" size="28" name="Text1"></span></font></p>
								<p align="center" style="margin-top: -2px; margin-bottom: -2px">��</p>
							</TD>
							<TD width="78" valign="bottom">
								<p align="right" style="margin-top: 0px; margin-bottom: -2px">�汾�ţ�</p>
								<p align="right" style="margin-top: 0px; margin-bottom: -2px">��</p>
							</TD>
							<TD width="68" valign="bottom">
								<p align="center" style="margin-top: 0px; margin-bottom: -2px">
									<INPUT class="input-text" id="Text2" type="text" size="10" name="Text1"></p>
								<p align="center" style="margin-top: 0; margin-bottom: -2px">
									��</p>
							</TD>
							<TD width="107" valign="bottom">
								<p align="right" style="margin-top: 0px; margin-bottom: -2px">����ʽ��</p>
								<p align="right" style="margin-top: 0px; margin-bottom: -2px">��</p>
							</TD>
							<TD width="110" valign="bottom">
								<p align="center" style="margin-top: 0px; margin-bottom: -2px">
										<SELECT id="Select1" name="Select1" class="input-text">
											<OPTION value="" selected>ģ������</OPTION>
											<OPTION value="">�汾��</OPTION>
											<OPTION value="">��Ч���ޣ���</OPTION>
											<OPTION value="">��Ч���ޣ�ֹ��</OPTION>
										</SELECT></p>
								<p align="center" style="margin-top: 0; margin-bottom: -2px">
									��</p>
							</TD>
							<TD width="224" valign="middle">
								<p align="center" style="margin-top: 0px; margin-bottom: -2px">
								<INPUT class="input-button" id="Button3" type="button" value=" �� ѯ " name="Button1" onclick="javascript:window.parent.contents.location.assign('main.content.moban.query.htm')">
								<INPUT class="input-button" id="Button4" type="button" value=" �� �� " name="Button2" onclick="javascript:window.parent.contents.location.assign('main.content.moban.add.htm')">
								<p align="center" style="margin-top: 0; margin-bottom: -2px">
							</TD>
						</TR>
		</table>
	</body>
</html:html>
