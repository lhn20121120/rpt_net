<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>汇率管理-add</title>
<link href="../../css/common.css" type="text/css" rel="stylesheet">
<link href="../../css/sub.css" type="text/css" rel="stylesheet">
<script language="javascript" src="../../js/func.js"></script>
<jsp:include page="../../calendar.jsp" flush="true">
	<jsp:param name="path" value="../../" />
</jsp:include>

<script language="javascript">
	/**
	 * 表单提交验证
	 */
		function validForm()
		{
			var date = document.getElementById('rateDate');
			var code1 = document.getElementById('sourceVcId');
			var code2 = document.getElementById('targetVcId');
			if(	code1.value == code2.value)
			{
				window.alert('原币种和兑换币种不能相同！');
				code1.focus();
				return false;
			}
			if(date.value.length > 0)
			{					
				var rate = document.getElementById('extRate');
				if(rate.value.length > 0)
				{
					if(!_checkNum(rate.value)){
						return false;
					}
					return true;
				}
				window.alert('汇率值未输入！');
				rate.focus();
				return false;
			}
			window.alert('日期未输入！');
			date.focus();
			return false;
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
			<td class="toptable_td2"><br>系统管理 &gt;&gt; 汇率管理 &gt;&gt; 新增汇率</td>
		  </tr>
		</table>
	</td>    
  </tr>
</table>
<br>
<html:form action="/sysManage/exchangeRate/exchangeRateMgr.do?method=add">
<table width="60%" border="0" cellspacing="1" cellpadding="3" class="tbcolor">
  <tr class="titletab">
    <th class="t1_head_center" colspan="2">新增汇率信息</th>
  </tr>
  <tr bgcolor="#FFFFFF">
    <td class="t2_row1" align="right" width="20%">日期：</td>
    <td>
	    <html:text property="rateDate" size="10" readonly="true" styleClass="inputReadOnly" 
	     styleId="rateDate" onclick="return showCalendar('rateDate', 'y-mm-dd');"/>
	    &nbsp;<img src="../../image/calendar.gif" onclick="return showCalendar('rateDate', 'y-mm-dd');">
    </td>
  </tr>
  <tr  bgcolor="#FFFFFF">
    <td align="right">原币种：</td>
    <td>
  	 	<logic:present name="currencyLst" scope="request">
  			<html:select property="sourceVcId">
  				<html:options collection="currencyLst" property="VCcyId" labelProperty="VCcyCnname"/>
  			</html:select>
  		</logic:present>
    </td>
  </tr>
  <tr bgcolor="#FFFFFF">
    <td align="right">汇率值：</td>
    <td><html:text property="extRate" size="20"/></td>    
  </tr>
  <tr bgcolor="#FFFFFF">
    <td align="right">兑换币种：</td>
    <td>
  		 <logic:present name="currencyLst" scope="request">
  			<html:select property="targetVcId">
  				<html:options collection="currencyLst" property="VCcyId" labelProperty="VCcyCnname"/>
  			</html:select>
  		</logic:present>
    </td>
  </tr>
  <tr  bgcolor="#FFFFFF">
    <td class="td_footer1" colspan="2"></td>    
  </tr>
</table>
<table width="60%" border="0" cellspacing="0" cellpadding="0" >
  <tr>
    <td><br>备注：原币种和换算币种的单位都为<font color="red">1</font>。</td>
  </tr>
</table>
<table width="60%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="center"><br><br>
    <input type="submit" value="确  定" class="input-button" onClick="return validForm()">&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" value="返  回" class="input-button" onClick="window.location.href('exchangeRateMgr.do?method=viewExchangeRate<logic:notEmpty name='paramMap' scope='request'><logic:iterate id='param' name='paramMap'>&<bean:write name='param' property='key'/>=<bean:write name='param' property='value'/></logic:iterate></logic:notEmpty>');">
    </td>
  </tr>
</table>
</html:form>
</center>
<br><br><br><br><br>
</body>
</html>
