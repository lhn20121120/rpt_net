<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>特殊校验管理-add</title>
<link href="<%=request.getContextPath() %>/css/common.css" type="text/css" rel="stylesheet">
<link href="<%=request.getContextPath() %>/css/sub.css" type="text/css" rel="stylesheet">
<script language="javascript" src="<%=request.getContextPath() %>/js/func.js"></script>

<script language="javascript">
	/**
	 * 表单提交验证
	 */
		function validForm()
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

		
		function _checkNum(InputValue)
		{
		    var reg=/^([.,0-9])+$/
			  var isValid
				
				isValid=reg.exec(InputValue)
				if (!isValid)
				{
					window.alert('汇率值应输入数字！');
					return false
				}
				return true
		} 

</script>
</head>

<body>
<center>
<table width="98%" border="0" cellspacing="0" cellpadding="0" >
  <tr>
    <td align="left">
		<table border="0" cellspacing="0" cellpadding="0" height="100%">
		  <tr>
			<td class="toptable_td2"><br>系统管理 &gt;&gt; 特殊校验管理 &gt;&gt; 新增校验</td>
		  </tr>
		</table>
	</td>    
  </tr>
</table>
<br>
<html:form action="/sysManage/specialValMgr.do?method=add">
<table width="60%" border="0" cellspacing="1" cellpadding="3" class="tbcolor">
  <tr class="titletab">
    <th class="t1_head_center" colspan="2">新增校验信息</th>
  </tr>
  <tr bgcolor="#FFFFFF">
    <td class="t2_row1" align="right" width="20%">名称：</td>
    <td><html:text property="valName" size="20" /></td>
  </tr>
  <tr  bgcolor="#FFFFFF">
    <td align="right">公式：</td>
    <td><html:text property="valFormula" size="20" /></td>
  </tr>
  <tr bgcolor="#FFFFFF">
    <td align="right">描述：</td>
    <td><html:text property="valDes" size="20"/></td>    
  </tr>
  <tr  bgcolor="#FFFFFF">
    <td class="td_footer1" colspan="2"></td>    
  </tr>
</table>
<table width="60%" border="0" cellspacing="0" cellpadding="0" >
  <tr>
    <td><br>备注：</td>
  </tr>
</table>
<table width="60%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="center"><br><br>
    <input type="submit" value="确  定" class="input-button" onClick="return validForm()">&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" value="返  回" class="input-button" onClick="window.location.href('specialValMgr.do?method=viewSpecVal<logic:notEmpty name='paramMap' scope='request'><logic:iterate id='param' name='paramMap'>&<bean:write name='param' property='key'/>=<bean:write name='param' property='value'/></logic:iterate></logic:notEmpty>');">
    </td>
  </tr>
</table>
</html:form>
</center>
<br><br><br><br><br>
</body>
</html>
