<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="0">
<title>特殊校验管理</title>
<link href="<%=request.getContextPath() %>/css/common.css" type="text/css" rel="stylesheet">
<link href="<%=request.getContextPath() %>/css/sub.css" type="text/css" rel="stylesheet">
<script language="javascript" src="<%=request.getContextPath() %>/js/func.js"></script>

<script type="text/javascript" language="javascript">
	/**
	 * 表单提交验证
	 */
		function _validate(form)
		{

			var vNam = document.getElementById('valName');
			var vFor = document.getElementById('valFormula');
			var vDes = document.getElementById('valDes');
			
			if(	vNam.value.trim().length == 0)
			{
				window.alert('名称不可为空！');
				code1.focus();
				return false;
			}
			
			if(	vFor.value.trim().length == 0)
			{
				window.alert('公式不可为空！');
				code1.focus();
				return false;
			}
			
			if(	vDes.value.trim().length == 0)
			{
				window.alert('描述不可为空！');
				code1.focus();
				return false;
			}

		}
</script>
</head>
	
	
<body>
	<logic:present name="messages" scope="request">
		<logic:greaterThan name="messages" property="size" value="0">
			<script language="javascript">
					alert("<bean:write name='messages' property='alertMsg'/>");
				</script>
		</logic:greaterThan>
	</logic:present>
<center>
<table width="98%" border="0" cellspacing="0" cellpadding="0" class="toptable">
  <tr>
    <td align="left">
		<table border="0" cellspacing="0" cellpadding="0" height="100%">
		  <tr>
			<td class="toptable_td2"><br>系统管理 &gt;&gt; 特殊校验管理 &gt;&gt; 修改校验信息</td>
		  </tr>
		</table>
	</td>    
  </tr>
</table>
<br>
<html:form action="sysManage/specialValMgr.do?method=update" onsubmit="return _validate(this)">
 <logic:present name="specValidateInfo" scope="request">
<table width="60%" border="0" cellspacing="1" cellpadding="3" class="tbcolor">
  <tr class="titletab">
    <th class="t1_head_center" colspan="2">修改校验信息</th>
    <html:hidden property="speValId" name="specValidateInfo"/>
  </tr>
 
  <tr bgcolor="#FFFFFF">
    <td class="t2_row1" align="right" width="20%">名称：</td>
    <td class="t2_row1"><html:text property="valName" name="specValidateInfo" size="20"/></td>
  </tr>
  <tr bgcolor="#FFFFFF">
    <td class="t2_row1" align="right">公式：</td>
    <td class="t2_row1"><html:text property="valFormula" name="specValidateInfo" size="20"/></td>
  </tr>
  <tr bgcolor="#FFFFFF">
    <td class="t2_row2" align="right">描述：</td>
    <td class="t2_row1"><html:text property="valDes" name="specValidateInfo" size="20"/></td>
  </tr>
  <tr bgcolor="#FFFFFF">
    <td class="td_footer1" colspan="2"></td>    
  </tr>
</table>
<table width="60%" border="0" cellspacing="0" cellpadding="0" class="space2">
  <tr>
    <td><br>备注：</td>
  </tr>
</table>
<table width="60%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="center"><br><br>
    <input type="submit" value="确 定" class="input-button">&nbsp;&nbsp;
    <input type="button" value="返 回" class="input-button" onClick="window.location.href('specialValMgr.do?method=viewSpecVal<logic:notEmpty name='paramMap' scope='request'><logic:iterate id='param' name='paramMap'>&<bean:write name='param' property='key'/>=<bean:write name='param' property='value'/></logic:iterate></logic:notEmpty>');">
    </td>
  </tr>  
</table>
</logic:present>
</html:form>
<br><br><br><br><br>
<logic:notPresent name="specValidateInfo" scope="request">
	抱歉!发生未知错误!
	<input name="" type="button" value="返 回" class="input-button" onClick="window.location.href('specialValMgr.do?method=viewSpecVal');">
</logic:notPresent>
		
</center>
</body>
</html>

