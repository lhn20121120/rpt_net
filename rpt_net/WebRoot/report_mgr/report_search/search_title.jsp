<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
	<head>
		<title>Title</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
	</head>
	<body topmargin="0" marginheight="0" leftmargin="0" marginwidth="0" onkeypress="keyEnter()">
		<table class="tabButton" cellspacing="0" cellpadding="0" border="0" style="border-width:0px; BACKGROUND-IMAGE:url('../../image/inside_index_bg4.jpg'); WIDTH:806px; " height="74">
				<TBODY>
					<tr>
						<td align="left" height="50%" vAlign="top" width="121" colSpan="1">
							<p>
							<img height="48" alt="" src="../../image/query.gif" width="115" align="left"></td>
						<td align="center" height="50%" vAlign="bottom" width="116" background="inside_index_bg4.jpg">
							��</td>
						<td align="center" height="50%" vAlign="bottom" width="355" colspan="2" background="../../image/inside_index_bg4.jpg">
							<p align="left">��</td>
						<td align="center" vAlign="bottom" colspan="2" background="../../image/inside_index_bg4.jpg">
							<input name="OK" type="button" class="input-button" id="OK" value=" �� ѯ " style="float: right"></td>
					</tr>
					<tr><td height="3" background="../../image/inside_index_bg4.jpg"></td></tr>
					<tr>
						<td align="left" height="53%" width="121" background="../../image/inside_index_bg4.jpg">
							<p align="center">���ͻ�����</td>
						<td align="left" height="53%" width="116" background="../../image/inside_index_bg4.jpg">
							<input name="T1" size="17" style="float: left"></td>
						<td align="left" height="53%" width="42" background="../../image/inside_index_bg4.jpg">
							<p align="center">ģ�壺</td>
						<td align="left" height="53%" width="313" background="../../image/inside_index_bg4.jpg">
								<select style='Z-INDEX:-1' name="select1" size="1" class="input-text">
									<option selected>������ģ�壩</option>
									<OPTION value="">G01�ʲ���ծ��Ŀͳ�Ʊ�</OPTION>
									<OPTION value="">G01�ʲ���ծ��Ŀͳ�Ʊ�ע</OPTION>
									<OPTION value="">G02����ҵ��ͳ�Ʊ�</OPTION>
									<OPTION value="">G04�����</OPTION>
									<OPTION value="">G05��������</OPTION>
									<OPTION value="">G11�ʲ������弶���������</OPTION>
									<OPTION value="">G12��������Ǩ�������</OPTION>
									<OPTION value="">G13���ʮ�ҹ�ע��/�μ���/������/��ʧ����������</OPTION>
									<OPTION value="">G14���ż��������</OPTION>
									<OPTION value="">G15����ʮ�ҹ������������������</OPTION>
									<OPTION value="">G16 ��ծ�ʲ����������</OPTION>
									<OPTION value="">����</OPTION>
									<OPTION value=""></OPTION>
								</select></td>
						<td align="left" height="53%" width="61" background="../../image/inside_index_bg4.jpg">
							<p align="center">���ͷ�Χ��</td>
						<td align="left" height="53%" width="153" background="../../image/inside_index_bg4.jpg">
							<SELECT style="WIDTH: 153; HEIGHT: 19" name="select2" size="1" class="input-text">
								<OPTION selected>�����л�����</OPTION>
								<OPTION>����������</OPTION>
								<OPTION>������ҵ����</OPTION>
								<OPTION>�ɷ�����ҵ����</OPTION>
								<OPTION>������ҵ����</OPTION>
								<OPTION>ũ����ҵ����</OPTION>
								<OPTION>ũ���������</OPTION>
								<OPTION>���ʷ��˻���</OPTION>
								<OPTION>������з���</OPTION>
								<OPTION>����������</OPTION>
								<option>ũ��������</option>
								<option>��ҵ���Ų���˾</option>
								<option>����Ͷ�ʹ�˾</option>
								<option>�������޹�˾</option>
								<option>�������ڹ�˾</option>
								<option>�����������</option>
								<OPTION>�����ʲ�����˾</OPTION>
							</SELECT></td>
					</tr>
			</TBODY>
		</table>
		<table class="tabButton" cellspacing="0" cellpadding="0" border="0" style="border-width:0px; BACKGROUND-IMAGE:url('inside_index_bg4.jpg'); WIDTH:805px; HEIGHT:36px; " id="table1">
			<tr>
				<td align="center" width="244">
					ʱ�䣺<input name="startDate" type="text" maxlength="10" class="input-text" size="10">
					-&nbsp;<input name="endDate" type="text" maxlength="10" class="input-text" size="10">
				</td>
				<td align="center">
					<FONT face="����"><INPUT id="Radio9" type="radio" value="Radio20" name="RadioGroup">&nbsp;�����¼</FONT><strong><font face="����" color="#FF0000" size="2">��</font></strong><FONT face="����">&nbsp;<INPUT id="Radio5" type="radio" value="Radio16" name="RadioGroup">&nbsp;��ȷ��¼</FONT><strong><font face="����" color="#33CC33" size="2">��</font></strong><FONT face="����">&nbsp;<INPUT id="Radio6" type="radio" value="Radio17" name="RadioGroup">&nbsp;����¼<strong><font size="2" color="#FF0000">��</font></strong>&nbsp;<INPUT id="Radio7" type="radio" value="Radio18" name="RadioGroup">&nbsp;©����¼<strong><font size="2" color="#FF0000">��</font></strong>&nbsp;<INPUT id="Radio10" type="radio" value="Radio21" name="RadioGroup">&nbsp;�쳣��¼<font color="#FF0000" style="font-size: 9pt"><strong>��</strong></font>&nbsp;<INPUT id="Radio8" type="radio" value="Radio19" name="RadioGroup" CHECKED>&nbsp;ȫ����¼</FONT>
				</td>
			</tr>
		</table>
		
	</body>
</html:html>
