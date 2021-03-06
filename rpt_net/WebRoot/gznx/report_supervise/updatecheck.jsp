<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%	
	/** 报表选中标志 **/
	String reportFlg = "0";
	
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
	}		
%>
<html:html locale="true">
<head>
<html:base/>
	<title>校验公式更新</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" type="text/css" href="../css/pageControl.css" />
	<script type="text/javascript" src="../../js/prototype-1.4.0.js"></script>
	<link rel="stylesheet" type="text/css" media="all"
		href="../../js/jscalendar/calendar.css" title="Aqua" />
	<script type="text/javascript" src="../../js/jscalendar/calendar.js"></script>
	<script type="text/javascript"
		src="../../js/jscalendar/lang/calendar-zh.js"></script>
	<script type="text/javascript" src="../../js/jscalendar/pageInclude.js"></script>

	<script language="javascript">
		var scriptReportFlg=<%=reportFlg%>;
			function _submit(){
			//公式名称
			var formulaName = document.getElementById('formulaName');
			//公式
			var formulaValue = document.getElementById('formulaValue');
			if(formulaName.value=="")
			{
				alert('中文定义不能为空！');
				formulaName.focus();
				return false;
			}
			if(formulaValue.value=="")
			{
				alert('公式不能为空！');
				formulaValue.focus();
				return false;
			}
			return ture;
			}
			function back1(){

				document.getElementById('formulaId').value=null;
				if(scriptReportFlg=="1"){					
					document.getElementById('form1').action="<%=request.getContextPath()%>/template/mod/editBJGXInit.do";
				}else {
					document.getElementById('form1').action="<%=request.getContextPath()%>/gznx/editBJGXInit.do";
				}
				document.getElementById('form1').submit();
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
	<html:form styleId="form1" method="post" action="/gznx/updatesaveCheck.do"  onsubmit="return _submit()">
	<html:hidden name="reportCheckForm" property="formulaId"/>
	<html:hidden name="reportCheckForm" property="templateId"/>
	<input type="hidden" name="childRepId" value="<bean:write  name="reportCheckForm" property="templateId"/>">
	<html:hidden name="reportCheckForm" property="versionId"/>	
		<table border="0" cellspacing="0" cellpadding="4" width="100%" align="center">
			<tr><td height="8"></td></tr>
			<tr>
				<td>
					当前位置 >> 报表管理 >> 报表定义管理 >> 模板修改 >> 表内表间关系修改>> 修改校验公式
				</td>
			</tr>
			<tr><td height="10"></td></tr>		
		</table>
		<table cellpadding="4" cellspacing="1" border="0" width="100%" align="center" class="tbcolor">
			<tr class="titletab">
				<th align="center" colspan="2">
					更新表内表间表达公式
				</th>
			</tr>
			<tr align="center" bgcolor="#ffffff">
				<td align="center">
					<table cellspacing="2" cellpadding="2" border="0" width="70%" align="center">
						<tr bgcolor="#ffffff">
							<td align="right">
							公式类型：
							</td>
							<td >
							<html:select name="reportCheckForm" property="validateTypeId">
							<html:option value="1">表内校验</html:option>
							<html:option value="2">表间校验</html:option>
							</html:select>	
							</td>
							
						</tr>
						<tr bgcolor="#ffffff">
							<td align="right">
								公式定义
							</td>
							<td >
							<html:textarea name="reportCheckForm"  property="formulaValue" rows="5" cols="100"/>
								
							</td>
						</tr>
						<tr bgcolor="#ffffff">
							<TD align="right">
								表达式说明
							</TD>
							<TD >								
								<html:textarea  name="reportCheckForm" property="formulaName" rows="5" cols="100"/>	
							</TD>
						</tr>
						<tr>
						<td>
						</td>
						</tr>
						<tr>
						</tr>
						<TR>
							<TD align="center" colspan="2">
								<input type="submit" class="input-button" value=" 保 存 " />
								&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="button" name="back" value=" 返 回 "
												class="input-button" onclick="back1()" />
								
								</td>
								<td></td>
						</TR>
					</table>
				</td>

			</tr>
		</table>
	</html:form>

</body>
</html:html>
