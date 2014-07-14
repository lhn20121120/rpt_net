<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
	<html:base />
	<title>��Ӳ���</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/globalStyle.css" rel="stylesheet" type="text/css">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<link href="../../css/calendar-blue.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="../../calendar/calendar.js"></script>
	<script type="text/javascript" src="../../calendar/calendar-cn.js"></script>
	<script language="javascript" src="../../calendar/calendar-func.js"></script>
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript">
			function validate()
			{
				var codeTypeId = document.getElementById('codeTypeId');
				var codeTypeValue = document.getElementById('codeTypeValue');
				var codeId = document.getElementById('codeId');
				var codeValue = document.getElementById('codeValue');
				var effectiveDate = document.getElementById('effectiveDate');
				
				if(codeTypeId.value.Trim()=="")
				{	
					alert("�������ֵ�����ID��");
					codeTypeId.focus();
					return false;		
				}
				if(CheckNumber(codeTypeId.value)==false)
				{
					alert("�ֵ�����IDӦ��Ϊ���֣�");
					codeTypeId.focus();
					return false;
				}
				if(codeTypeValue.value.Trim()=="")
				{	
					alert("�������ֵ�����ֵ��");
					codeTypeValue.focus();
					return false;		
				}
				if(codeId.value.Trim()=="")
				{	
					alert("�������ֵ�ID��");
					codeId.focus();
					return false;		
				}
				if(!CheckNumber(codeId.value.Trim()))
				{
					alert("�ֵ�ID����Ϊ����");
					return false;
				}
				if(codeValue.value.Trim()=="")
				{	
					alert("�������ֵ�ֵ��");
					codeValue.focus();
					return false;		
				}
				if(effectiveDate.value=="")
				{	
					alert("��������Ч���ڣ�");
					effectiveDate.focus();
					return false;
				}
				return true;		
			}
			
			String.prototype.Trim=function(){
				return this.replace(/(^\s*)|(\s*$)/g,"");
				}
				
			//���
			function Check( reg, str )
			{
				 if( reg.test( str ) )
				 {
				  	return true;
				 }
				 return false;
			}
			// �������
			function CheckNumber( str )
			{
				 var reg = /^\d*(?:$|\.\d*$)/;
				 return Check( reg, str );
			}
	</script>
</head>

<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table width="90%" border="0" align="center" cellpadding="4" cellspacing="0">
		<tr>
			<td height="30" colspan="2">
				��ǰλ�� >> ϵͳ���� >> ϵͳ�ֵ���� >> ��Ӳ���
			<td>
		</tr>
		<tr>
			<td>
				<table width="100%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
					<tr class="titletab">
						<th align="center">
							��Ӳ���
						</th>
					</tr>
					<tr>
						<td bgcolor="#ffffff">
							<html:form action="/system_mgr/addCodeLib" method="post" onsubmit="return validate()">
								<br>
								<table width="100%" border="0" align="center" cellpadding="4" cellspacing="1">
									<tr>
										<td align="right" bgcolor="#ffffff" width="45%">
											�ֵ�����ID:
										</td>
										<td align="left" bgcolor="#ffffff" colspan="3">
											<html:text styleId="codeTypeId" property="codeTypeId" styleClass="input-text" size="30" maxlength="10" />
										</td>
									</tr>
									<tr>
										<td align="right" bgcolor="#ffffff" width="45%">
											�ֵ�����ֵ:
										</td>
										<td align="left" bgcolor="#ffffff" colspan="3">
											<html:text styleId="codeTypeValue" property="codeTypeValue" styleClass="input-text" size="30" maxlength="15" />
										</td>
									</tr>
									<tr>
										<td align="right" bgcolor="#ffffff">
											�ֵ�ID:
										</td>
										<td align="left" bgcolor="#ffffff" colspan="3">
											<html:text styleId="codeId" property="codeId" styleClass="input-text" size="30" maxlength="10" />
										</td>
									</tr>
									<tr>
										<td align="right" bgcolor="#ffffff">
											�ֵ�ֵ:
										</td>
										<td align="left" bgcolor="#ffffff" colspan="3">
											<html:text styleId="codeValue" property="codeValue" styleClass="input-text" size="30" maxlength="15" />
										</td>
									</tr>
									<tr>
										<td align="right" bgcolor="#ffffff" width="20%">
											��Ч����:
										</td>
										<td align="left" width="40%" colspan="2">
											<input type="text" name="effectiveDate" size="10" class="input-text" readonly="true">
											<img src="../../image/calendar.gif" border="0" onclick="return showCalendar('effectiveDate', 'y-mm-dd');">
										</td>
									</tr>
									<tr>
										<td align="right" bgcolor="#ffffff">
											�Ƿ������޸�:
										</td>
										<td align="left" bgcolor="#ffffff" colspan="3">
											<SELECT id="isModi" name="isModi">
												<OPTION value="1">
													�����޸�
												</OPTION>
												<OPTION value="0">
													�������޸�
												</OPTION>
											</SELECT>
										</td>
									</tr>
									<tr>
										<td colspan="4" align="right">
											<div id=location>
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="4" align="right" bgcolor="#ffffff">
											<html:submit value="����" styleClass="input-button" />
											&nbsp;
											<input type="button" value="����" class="input-button" onclick="window.location.assign('../../system_mgr/viewCodeLib.do')" />
										</td>
									</tr>
								</table>
								</html:form>
						</td>
					</tr>
				</table>
				
			</td>
		</tr>
	</table>
</body>
</html:html>
