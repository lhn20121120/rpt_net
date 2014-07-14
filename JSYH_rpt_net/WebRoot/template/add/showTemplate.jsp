<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String contextPath = request.getContextPath();
	String url = contextPath + "/template/queryTemplate.do";
%>
<html>
	<HEAD>
		<title>
			ģ���ѯ
		</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../css/common.css" type="text/css" rel="stylesheet">
	</HEAD>
	<BODY>
		<table border="0" width="90%" align="center">
			<tr>
				<td height="8"></td>
			</tr>
			<tr>
				<td>
					��ǰλ�� &gt;&gt; ģ����� &gt;&gt; ����ģ�� &gt;&gt; ģ���ѯ
				</td>
			</tr>
			<tr>
				<td height="10"></td>
			</tr>
		</table>
		<form action="<%=url%>" target="contents">
		<TABLE border="0" width="90%" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
			<TR class="tableHeader">
				<TD width="33%" align="center" valign="middle">
					ģ������
				</TD>
				<TD width="10%" align="center" valign="middle">
					�汾��
				</TD>
				<TD width="10%" align="center" valign="middle">
					���
				</TD>
				<TD width="16%" align="center" valign="middle">
					��ʼ����
				</TD>
				<TD width="16%" align="center" valign="middle">
					��������
				</TD>
				<TD width="15%" align="center" valign="middle">
					�Ƿ񷢲�
				</TD>
			</TR>

			<logic:present name="aps" scope="session">
				<logic:notEqual name="aps" property="recordCount" scope="session" value="0">
					<logic:iterate id="template" name="ts" scope="request">
						<TR bgcolor="#FFFFFF">
							<TD width="33%" align="center" valign="middle">
								<bean:write name="template" property="reportName" />
							</TD>
							<TD width="10%" align="center" valign="middle">
								<bean:define id="comp_id" name="template" property="comp_id" />
								<bean:write name="comp_id" property="versionId" />
							</TD>
							<TD width="10%" align="center" valign="middle">
								<bean:write name="comp_id" property="childRepId" />
							</TD>
							<TD width="16%" align="center" valign="middle">
								<bean:write name="template" property="startDate" />
							</TD>
							<TD width="16%" align="center" valign="middle">
								<bean:write name="template" property="endDate" />
							</TD>

							<TD width="15%" align="center" valign="middle">
								<logic:equal name="template" property="isPublic" value="1">
							     �Ѿ�����
							</logic:equal>
								<logic:notEqual name="template" property="isPublic" value="1">
							     û�з���
							</logic:notEqual>
							</TD>
						</TR>
					</logic:iterate>
				</logic:notEqual>
			</logic:present>
		</TABLE>
		<br>
		<br>
		<logic:present name="aps" scope="session">
			<logic:equal name="aps" property="recordCount" scope="session" value="0">
				<center>
				<font color="#ff8000">
					���β�ѯ����ƥ��ļ�¼!
			    </font>
				</center>
			</logic:equal>
		</logic:present>
		<TABLE align="center" border="0" width="80%">
			<TR>
				<TD>
					<!------------------------- ��ҳ�����(begin) ---------------------------->
					<table width="99%" border="0" align="center" cellpadding="2" cellspacing="1">
						<tr>
							<td width="30%">
								��
								<span class="apartpage_span">
									<bean:write name="aps" property="recordCount" scope="session" />
								</span>
								����¼ &nbsp;��
								<span class="apartpage_span">
									<bean:write name="aps" property="pageVisiting" scope="session" />
								</span>
								/
								<span class="apartpage_span">
									<bean:write name="aps" property="pageCount" scope="session" />
								</span>
								ҳ
							</td>
							<td align="right" width="40%">
								<a href="<%=url%>?apartType=t" class="apartpage">
									<logic:equal name="aps" property="isFirstPage" value="0">
									��ҳ
                                    </logic:equal>
								</a>
								&nbsp;
								<a href="<%=url%>?apartType=f" class="apartpage">
									<logic:equal name="aps" property="isFirstPage" value="0">
									��һҳ
                                   </logic:equal>
								</a>
								&nbsp;
								<a href="<%=url%>?apartType=n" class="apartpage">
									<logic:equal name="aps" property="isEndPage" value="0">
									��һҳ
                                   </logic:equal>
								</a>
								&nbsp;
								<a href="<%=url%>?apartType=e" class="apartpage">
									<logic:equal name="aps" property="isEndPage" value="0">
									βҳ
                                   </logic:equal>
								</a>
								&nbsp;
							</td>
							<td align="right" width="30%">
								ת
								<input type="text" size="4" name="apartType">
								ҳ&nbsp;
								<input name="�ύ" type="submit" value="GO">
							</td>
						</tr>
					</table>
					<!----------------------------- ��ҳ�����(end) ------------------------->
				</TD>
			</TR>
		</TABLE>
		</form>
	</BODY>
</html>
