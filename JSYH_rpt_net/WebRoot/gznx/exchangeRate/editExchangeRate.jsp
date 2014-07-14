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
<title>���ʹ���</title>
<link href="../../css/common.css" type="text/css" rel="stylesheet">
<link href="../../css/sub.css" type="text/css" rel="stylesheet">
<script language="javascript" src="../../js/func.js"></script>
<jsp:include page="../../calendar.jsp" flush="true">
	<jsp:param name="path" value="../../" />
</jsp:include>
<script type="text/javascript" language="javascript">
	/**
	 * ���ύ��֤
	 */
		function _validate(form)
		{
			if(isEmpty(form.extRate.value)==true){
				alert("[����ֵ]������Ϊ��!\n");
				form.extRate.focus();
				return false;
			}else{
				if(_checkNum(form.extRate.value)==false){
					alert("��������ȷ����ֵ!\n");
					form.extRate.select();
					form.extRate.focus();
					return false;					
				}
			}
			
			return true;
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
			<td class="toptable_td2"><br>ϵͳ���� &gt;&gt; ���ʹ��� &gt;&gt; �޸Ļ���</td>
		  </tr>
		</table>
	</td>    
  </tr>
</table>
<br>
<html:form action="sysManage/exchangeRate/exchangeRateMgr.do?method=update" onsubmit="return _validate(this)">
 <logic:present name="exchangeRate" scope="request">
<table width="60%" border="0" cellspacing="1" cellpadding="3" class="tbcolor">
  <tr class="titletab">
    <th class="t1_head_center" colspan="2">�޸Ļ�����Ϣ</th>
    <html:hidden property="erId" name="exchangeRate"/>
  </tr>
 
  <tr bgcolor="#FFFFFF">
    <td class="t2_row1" align="right" width="20%">���ڣ�</td>
    <td class="t2_row1"><html:text property="rateDate" name="exchangeRate" size="10" readonly="true" styleClass="inputReadOnly"/>&nbsp;</td>
  </tr>
  <tr bgcolor="#FFFFFF">
    <td class="t2_row2" align="right">���֣�</td>
    <td class="t2_row2">
    <html:hidden property="sourceVcId" name="exchangeRate" />
    <html:text property="sourceVc.VCcyCnname" name="exchangeRate" size="10" readonly="true" styleClass="inputReadOnly"/>
    	<%--<logic:present name="currencyLst" scope="request">
				<html:select property="sourceVcId">
					<html:options collection="currencyLst" property="vcId" labelProperty="vcCnnm"/>
				</html:select>
  		</logic:present>--%>
    </td>
  </tr>
  <tr bgcolor="#FFFFFF">
    <td class="t2_row1" align="right">����ֵ��</td>
    <td class="t2_row1"><html:text property="extRate" name="exchangeRate" size="20"/></td>
  </tr>
  <tr bgcolor="#FFFFFF">
    <td class="t2_row2" align="right">���֣�</td>
    <td class="t2_row2">
    <html:hidden property="targetVcId" name="exchangeRate" />
    <html:text property="targetVc.VCcyCnname" name="exchangeRate" size="10" readonly="true" styleClass="inputReadOnly"/>
 
  	<%--<logic:present name="currencyLst" scope="request">
  			<html:select property="targetVcId">
  				<html:options collection="currencyLst" property="vcId" labelProperty="vcCnnm"/>
  			</html:select>
  		</logic:present>
  		--%>
    </td>
  </tr>
  <tr bgcolor="#FFFFFF">
    <td class="td_footer1" colspan="2"></td>    
  </tr>
</table>
<table width="60%" border="0" cellspacing="0" cellpadding="0" class="space2">
  <tr>
    <td><br>��ע��ԭ���ֺͻ�����ֵĵ�λ��Ϊ<font color="red">1</font>��</td>
  </tr>
</table>
<table width="60%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="center"><br><br>
    <input type="submit" value="ȷ ��" class="input-button">&nbsp;&nbsp;
    <input type="button" value="�� ��" class="input-button" onClick="window.location.href('exchangeRateMgr.do?method=viewExchangeRate<logic:notEmpty name='paramMap' scope='request'><logic:iterate id='param' name='paramMap'>&<bean:write name='param' property='key'/>=<bean:write name='param' property='value'/></logic:iterate></logic:notEmpty>');">
    </td>
  </tr>  
</table>
</logic:present>
</html:form>
<br><br><br><br><br>
<logic:notPresent name="exchangeRate" scope="request">
	��Ǹ!����δ֪����!
	<input name="" type="button" value="�� ��" class="input-button" onClick="window.location.href('exchangeRateMgr.do?method=viewExchangeRate');">
</logic:notPresent>
		
</center>
</body>
</html>

