<%@ page language="java" pageEncoding="GBK"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base />
	<title>��־����</title>
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
	//��ϸ��Ϣ
	function viewLog(taskDate){
		var url="<%=request.getContextPath()%>/system_mgr/viewLogIn.do?logTypeId=50&userName=Day_Report&operation="+taskDate;
		window.open(url);
	}
	//����
	function reRun(taskDate,flag){
		if(flag==2){//ȷ��ִ�гɹ��������Ƿ�����ִ��
			if(!confirm("�Ѿ�ִ�гɹ�����������ʱ���Զ�����ԭ�����ݣ��Ƿ������")){
				return;
			}
		}
		
		window.location.href="<%=request.getContextPath()%>/dayTask.do?method=reRun&taskDate="+taskDate;
	}
  	</script>
</head>
<body>

	<%-- ������ʾ��Ϣ ��ʼ--%>
	<logic:present name="Message" scope="request">
		<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
	</logic:present>
	<%-- ������ʾ��Ϣ ����--%>
	<%-- ��ǰλ����ʾ��Ϣ ��ʼ--%>
	<table border="0" width="98%" align="center">
		<tr>
			<td height="3"></td>
		</tr>
		<tr>
			<td>
				��ǰλ�� &gt;&gt; �������� &gt;&gt; �����ѯ &gt;&gt; �ձ����ѯ
			</td>
		</tr>
	</table>
	<%-- ��ǰλ����ʾ��Ϣ ����--%>
	<%-- ��ѯ���� ��ʼ--%>
	<html:form action="/dayTask.do?method=view" method="post" style="margin:0px;">
		<fieldset id="fieldset" style="margin: 0px; padding: 0px; width: 98%">
			<table cellSpacing="0" cellPadding="4" width="100%" border="0" align="left">
				<tr>
					<td width="35%">
						&nbsp;����ʱ�䣺
						<html:text property="queryStartTaskDate" size="10" readonly="true" />
						<html:img border="0" src="../image/calendar.gif" onclick="return showCalendar('queryStartTaskDate', 'y-mm-dd');" />
						-
						<html:text property="queryEndTaskDate" size="10" readonly="true" />
						<html:img border="0" src="../image/calendar.gif" onclick="return showCalendar('queryEndTaskDate', 'y-mm-dd');" />
					</td>
					<td>
						&nbsp;�����ţ�
						<html:text property="queryTemplateId" size="20" />
					</td>
					<td>
						&nbsp;����汾��
						<html:text property="queryVersionId" size="20" />
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;�������ƣ�
						<html:text property="queryTemplateName" size="20" />
					</td>
					<td>
						&nbsp;����״̬��
						<html:select styleId="queryFlag" property="queryFlag">
							<html:option value="-999">--����״̬--</html:option>
							<html:option value="0">δִ��</html:option>
							<html:option value="1">����ִ��</html:option>
							<html:option value="2">ִ�гɹ�</html:option>
							<html:option value="-1">ִ��ʧ��</html:option>
						</html:select>
					</td>
					<td align="center">
						<html:submit styleClass="input-button" value=" ��  ѯ " />
						&nbsp;&nbsp;
					</td>
				</tr>
			</table>
		</fieldset>
	</html:form>
	<%-- ��ѯ���� ����--%>
	<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
		<tr>
			<td align="left">
				<!--<input type="button" name="doTaskAgain" value="������������" class="input-button" onclick="doTaskAgain();"> -->
			</td>
		</tr>
	</table>
	<%-- �б�������ʾ ��ʼ--%>
	<table cellSpacing="1" width="98%" border="0" align="center" cellpadding="4" class="tbcolor">
		<tr>
			<th colspan="12">
				�ձ���������־�б�
			</th>
		</tr>
		<tr>
			<td class="tableHeader" width="8%">
				��������
			</td>
			<td class="tableHeader" width="8%">
				������
			</td>
			<td class="tableHeader" width="">
				ģ������
			</td>
			<td class="tableHeader" width="8%">
				�汾��
			</td>
			<td class="tableHeader" width="8%">
				����
			</td>
			<td class="tableHeader" width="5%">
				Ƶ��
			</td>
			<td class="tableHeader" width="8%">
				����״̬
			</td>
			<td class="tableHeader" width="12%">
				��ʼʱ��
			</td>
			<td class="tableHeader" width="12%">
				����ʱ��
			</td>
			<td class="tableHeader" width="10%">
				����
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
							δִ��
						</logic:equal>
						<logic:equal name="item" property="flag" value="1">
							����ִ��
						</logic:equal>
						<logic:equal name="item" property="flag" value="2">
							ִ�гɹ�
						</logic:equal>
						<logic:equal name="item" property="flag" value="-1">
							ִ��ʧ��
						</logic:equal>
					</td>
					<td>
						<bean:write name="item" property="startDate" />
					</td>
					<td>
						<bean:write name="item" property="endDate" />
					</td>
					<td>
						<a href="javascript:viewLog('<bean:write name='item' property='taskDate' />');" style="color: blue">��ϸ��Ϣ</a>
						<input type="button" name="bthRun" value="����" class="input-button" onclick="reRun('<bean:write name='item' property='taskDate' />',<bean:write name="item" property="flag" />);">
					</td>
				</tr>
			</logic:iterate>
		</logic:present>
		<logic:notPresent name="Records" scope="request">
			<tr bgcolor="#FFFFFF">
				<td colspan="12">
					���޼�¼
				</td>
			</tr>
		</logic:notPresent>
	</table>
	<%-- �б�������ʾ ����--%>
	<%-- ��ҳ ��ʼ--%>
	<table cellSpacing="1" cellPadding="0" width="98%" border="0">
		<tr>
			<td>
				<jsp:include page="../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../dayTask.do?method=view" />
				</jsp:include>
			</td>
		</tr>
	</table>
	<%-- ��ҳ ����--%>
	<br>

</body>
</html:html>

