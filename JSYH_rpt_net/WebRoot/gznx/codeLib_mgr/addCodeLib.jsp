<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
	<html:base />
	<title>添加参数</title>
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
					alert("请输入字典类型ID！");
					codeTypeId.focus();
					return false;		
				}
				if(CheckNumber(codeTypeId.value)==false)
				{
					alert("字典类型ID应该为数字！");
					codeTypeId.focus();
					return false;
				}
				if(codeTypeValue.value.Trim()=="")
				{	
					alert("请输入字典类型值！");
					codeTypeValue.focus();
					return false;		
				}
				if(codeId.value.Trim()=="")
				{	
					alert("请输入字典ID！");
					codeId.focus();
					return false;		
				}
				if(!CheckNumber(codeId.value.Trim()))
				{
					alert("字典ID必须为数字");
					return false;
				}
				if(codeValue.value.Trim()=="")
				{	
					alert("请输入字典值！");
					codeValue.focus();
					return false;		
				}
				if(effectiveDate.value=="")
				{	
					alert("请输入生效日期！");
					effectiveDate.focus();
					return false;
				}
				return true;		
			}
			
			String.prototype.Trim=function(){
				return this.replace(/(^\s*)|(\s*$)/g,"");
				}
				
			//检查
			function Check( reg, str )
			{
				 if( reg.test( str ) )
				 {
				  	return true;
				 }
				 return false;
			}
			// 检查数字
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
				当前位置 >> 系统管理 >> 系统字典管理 >> 添加参数
			<td>
		</tr>
		<tr>
			<td>
				<table width="100%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
					<tr class="titletab">
						<th align="center">
							添加参数
						</th>
					</tr>
					<tr>
						<td bgcolor="#ffffff">
							<html:form action="/system_mgr/addCodeLib" method="post" onsubmit="return validate()">
								<br>
								<table width="100%" border="0" align="center" cellpadding="4" cellspacing="1">
									<tr>
										<td align="right" bgcolor="#ffffff" width="45%">
											字典类型ID:
										</td>
										<td align="left" bgcolor="#ffffff" colspan="3">
											<html:text styleId="codeTypeId" property="codeTypeId" styleClass="input-text" size="30" maxlength="10" />
										</td>
									</tr>
									<tr>
										<td align="right" bgcolor="#ffffff" width="45%">
											字典类型值:
										</td>
										<td align="left" bgcolor="#ffffff" colspan="3">
											<html:text styleId="codeTypeValue" property="codeTypeValue" styleClass="input-text" size="30" maxlength="15" />
										</td>
									</tr>
									<tr>
										<td align="right" bgcolor="#ffffff">
											字典ID:
										</td>
										<td align="left" bgcolor="#ffffff" colspan="3">
											<html:text styleId="codeId" property="codeId" styleClass="input-text" size="30" maxlength="10" />
										</td>
									</tr>
									<tr>
										<td align="right" bgcolor="#ffffff">
											字典值:
										</td>
										<td align="left" bgcolor="#ffffff" colspan="3">
											<html:text styleId="codeValue" property="codeValue" styleClass="input-text" size="30" maxlength="15" />
										</td>
									</tr>
									<tr>
										<td align="right" bgcolor="#ffffff" width="20%">
											生效日期:
										</td>
										<td align="left" width="40%" colspan="2">
											<input type="text" name="effectiveDate" size="10" class="input-text" readonly="true">
											<img src="../../image/calendar.gif" border="0" onclick="return showCalendar('effectiveDate', 'y-mm-dd');">
										</td>
									</tr>
									<tr>
										<td align="right" bgcolor="#ffffff">
											是否允许修改:
										</td>
										<td align="left" bgcolor="#ffffff" colspan="3">
											<SELECT id="isModi" name="isModi">
												<OPTION value="1">
													可以修改
												</OPTION>
												<OPTION value="0">
													不可以修改
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
											<html:submit value="保存" styleClass="input-button" />
											&nbsp;
											<input type="button" value="返回" class="input-button" onclick="window.location.assign('../../system_mgr/viewCodeLib.do')" />
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
