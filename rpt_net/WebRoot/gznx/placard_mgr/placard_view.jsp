<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.common.Config" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.fitech.gznx.form.PlacardUserViewForm"%>
<html:html locale="true">
<html:base />
<head>
	<title>����鿴</title>
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
			
			//�鿴
			function viewPlacard(placardId)
			{
				document.getElementById("view_placardId").value=placardId;
				document.getElementById("viewForm").submit();
			}
			
			function setViewState(viewState){
			
				if(viewState=='9999'||viewState==null){
					document.getElementById("viewState").selectedIndex = 0;
				}
				
				else if(viewState=='0'){
					
					document.getElementById("viewState").selectedIndex = 1;
				
				}
				
				else if(viewState=='1'){
					
					document.getElementById("viewState").selectedIndex = 2;
				
				}
			
			}
			
			function _clearForm(){
			
				document.getElementById("title").value = "";
				document.getElementById("startDate").value = "";
				document.getElementById("endDate").value = "";
				document.getElementById("viewState").selectedIndex = 0;
			
			}
			
	</script>
	<%//ȡ�ò�ѯ����
			String queryTerm = "";
			if (request.getAttribute("QueryTerm") != null)
				queryTerm = (String) request.getAttribute("QueryTerm");
			
			PlacardUserViewForm QueryplacardUserViewForm = (PlacardUserViewForm)request.getAttribute("queryPlacardForm");
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
				��ǰλ�� &gt;&gt; ��Ϣ���� &gt;&gt; ����鿴
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
				<html:form styleId="form1" action="/placard_mgr/viewPlacardUserViewAction" method="Post">
					<fieldset id="fieldset">
						<table width="95%" border="0" align="center">
							<tr>
								<td height="5" />
								</td>
							</tr>
							<tr>
								
								<td align="left">
									�������:
									<html:text property="title" size="20" styleClass="input-text" style="text" value="<%=QueryplacardUserViewForm.getTitle() %>"/>
								</td>
								<td align="center">
									��ʼʱ�䣺
									<html:text property="startDate" size="10" styleClass="input-text" style="text" readonly="true" value="<%=QueryplacardUserViewForm.getStartDate() %>"/><html:img border="0" src="../../image/calendar.gif" onclick="return showCalendar('startDate', 'y-mm-dd');" />
								</td>
								<td align="center">
									����ʱ�䣺
									<html:text property="endDate" size="10" styleClass="input-text" style="text" readonly="true" value="<%=QueryplacardUserViewForm.getEndDate() %>"/><html:img border="0" src="../../image/calendar.gif" onclick="return showCalendar('endDate', 'y-mm-dd');" />
								</td>
								</tr>
								<tr>
								<td align="left">
									�鿴״̬:
									<html:select property="viewState">
										<html:option value="<%=String.valueOf(Config.FLAG_ALL)%>">
											ȫ������
										</html:option>
										<html:option value="0">
											��δ�鿴
										</html:option>
										<html:option value="1">
											�Ѿ��鿴
										</html:option>
									</html:select>
								</td>
								<td colspan="2" align="right">
									<html:submit styleClass="input-button" value="��  ѯ" />
									&nbsp;
									<html:button property="cancel" value="ȡ  ��" styleClass="input-button" onclick="_clearForm()" />
								</td>
							</tr>
						</table>
					</fieldset>
					<script language="javascript">
						setViewState("<%=QueryplacardUserViewForm.getViewState() %>")
					</script>
				</html:form>
			</td>
		</tr>
	</table>
	<table width="90%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
		<tr class="titletab">
			<th align="center" colspan="5">
				��Ϣ����鿴
			</th>
		</tr>
		<tr align="center" class="middle">
			<td align="center" width="30%">
				����
			</td>
			<td align="center" width="20%">
				��������
			</td>
			<td align="center" width="15%">
				������
			</td>
			<td align="center" width="15%">
				״̬
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
						<logic:notEmpty name="item" property="publicUserId">
							<bean:write name="item" property="publicUserId" />
						</logic:notEmpty>
					</td>
					<td bgcolor="#ffffff" align="center">
						<logic:equal name="item" property="viewState" value="0">
							<font color="#FF0000">δ�鿴</font>
						</logic:equal>
						<logic:equal name="item" property="viewState" value="1">
							<font color="#7FFF00">�Ѳ鿴</font>
						</logic:equal>
					</td>
				</tr>
			</logic:iterate>
		</logic:present>
		<logic:notPresent name="Records" scope="request">
			<tr align="left">
				<td bgcolor="#ffffff" colspan="9">
					��ƥ���¼
				</td>
			</tr>
		</logic:notPresent>
		<!-- �鿴-->
		<html:form styleId="viewForm" action="/placard_mgr/viewPlacardDetailAction" method="post">
			<input type="hidden" id="view_placardId" name="placardId">
			<input type="hidden" name="flag" value="4">
			<input type="hidden" name="queryTerm" value="<%=queryTerm%>">
		</html:form>
</body>
</html:html>
