<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.common.ApartPage" %>
<%@ page import="com.cbrc.smis.common.Config" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%--<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>--%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>����У�����</title>
<link href="<%=request.getContextPath() %>/css/common.css" type="text/css" rel="stylesheet">
<link href="<%=request.getContextPath() %>/css/sub.css" type="text/css" rel="stylesheet">
<script language="javascript" src="<%=request.getContextPath() %>/js/func.js"></script>

<script type="text/javascript" language="javascript">
	function _delete(){
		var cbObjs=document.getElementsByName('speValId');
		var length = cbObjs.length;
		var checked = false;
		for(var i = 0; i < length; i++){
			if( cbObjs[i].checked == true)
				checked = true;
		}
		
		if (checked != true){
			window.alert('��ѡ��Ҫɾ���Ļ��ʣ�');
			return;
			}
		if(window.confirm('ȷ��Ҫɾ��������')){
			document.forms[1].submit();
		}
	}
	function checkAll()
	{
		var checkId = document.getElementById("checkId");
		var erId = eval(document.getElementsByName("speValId"));
		
		if(checkId.checked == true)
		{
			for(var i=0;i < erId.length; i ++)
			{
				erId[i].checked = true;
			}
		}
		if(checkId.checked == false)
		{
			for(var i=0;i < erId.length; i ++)
			{
				erId[i].checked = false;
			}
		}
	}
	
	/**
	 * ���ύ��֤
	 */
	 function _validate(form){
	  /* var sDateVal=form.startDate.value;
	   var eDateVal=form.endDate.value;
	   			   
	   if(_trim(sDateVal)!="" && _trim(eDateVal)!=""){
	     var sDate=_getDate(sDateVal);
	   	 var eDate=_getDate(eDateVal);
	   	 if(sDate>eDate){
		     alert("��ѯ[��ʼ����]��������[��ֹ����],������ѡ��!\n");			     
		     return false;
	     }
	   }
	   */
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
<table width="98%" align="center" border="0" cellspacing="0" cellpadding="0" >
  <tr>
    <td>
		<table border="0" cellspacing="0" cellpadding="0" height="100%">
		  <tr>
			<td >��ǰλ�� &gt;&gt; ϵͳ���� &gt;&gt; ����У�����</td>
		  </tr>
		</table>
	</td>    
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr><td height="5"></td></tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="3">
  <tr>
  	<td>
  	<html:form action="/sysManage/specialValMgr.do?method=query" onsubmit="return _validate(this)">
  		<table width="100%" border="0" cellspacing="1" cellpadding="3" align="center" class="schTbl">
  			<tr>
  				<td class="schTr">
  				<table width="100%" border="0" cellspacing="0" cellpadding="3">
  					<tr>
  						<td>
		  				���ƣ�<html:text property="valName" size="20" />
		  				</td>
		  				<td>
		  					<html:submit value="��  ѯ" styleClass="input-button"/>
		  				</td>
		  			</tr>
		  		</table>
		  		</td>
  			</tr>
  		</table>
  	</html:form>
  	</td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="1" cellpadding="3" class="tbcolor">
  <tr class="titletab">
  	<th align="left" colspan="4">
  		&nbsp;У����Ϣ�б�
  	</th>
    <th align="right" colspan="3">
    	<%-- 
    	<input type="button" value="��  ��" class="input-button" onClick="window.location.href('specialValMgr.do?method=toAdd<logic:notEmpty name='paramMap' scope='request'><logic:iterate id='param' name='paramMap'>&<bean:write name='param' property='key'/>=<bean:write name='param' property='value'/></logic:iterate></logic:notEmpty>');">
    	<input type="button" value="ɾ  ��" class="input-button" onClick="_delete()" >
    	--%>
    </th>
  </tr>
  <html:form action="/sysManage/specialValMgr.do?method=delete">
  <tr class="middle">
    <td align="center" width="10%"><input type="checkbox" name="checkId" onclick="checkAll()">���</td>
    <td align="center" width="10%">У������</td>
    <td align="center" width="10%">����</td>
    <td align="center" width="10%">��ʽ</td>
    <td align="center" width="30%">����</td>
    <td align="center" width="30%">����</td>
  </tr>
  <logic:present name="specValInfoLst" scope="request">
  	<%
		int index = 0;
		if(request.getAttribute("apartPage") != null)
		{
				ApartPage apartPage = (ApartPage)request.getAttribute("apartPage");
				if(apartPage != null)
					index =(apartPage.getCurPage() -1) * Config.PER_PAGE_ROWS;
		}
	%>
  	<logic:iterate id="specValidateInfo" name="specValInfoLst" scope="request">
  		 <tr bgcolor="#FFFFFF" align="center">
		    <td class="td_row1_left" align="left">&nbsp;
		    	<html:multibox property="speValId">
		    		<bean:write name="specValidateInfo" property="speValId"/>
		    	</html:multibox>
		    	<%=++index%>
		    </td>
		    <td class="td_row1" align="center">
			    <logic:notEmpty name="specValidateInfo" property="valFlag" >
			    <logic:equal name="specValidateInfo" property="valFlag" value="1">��һУ��</logic:equal>
			    <logic:equal name="specValidateInfo" property="valFlag" value="2">���У��</logic:equal>
			    </logic:notEmpty>
			    <logic:empty  name="specValidateInfo" property="valFlag">--</logic:empty>
		    </td>
		    <td class="td_row1"><bean:write name="specValidateInfo" property="valName"/></td>
		    <td class="td_row1">
		     <%--
		    	<html:link action="/sysManage/specialValMgr.do?method=toUpdate" paramId="speValId" paramName="specValidateInfo" paramProperty="speValId" name="paramMap">
		   	 
		    		<bean:write name="specValidateInfo" property="valFormula"/>
		    	</html:link>
		    --%>
		    	<bean:write name="specValidateInfo" property="valFormula"/>
		    </td>
		    
		    <td class="td_row1"><bean:write name="specValidateInfo" property="valDes"/></td>    
 		    <td class="td_row1"><bean:write name="specValidateInfo" property="bak1"/></td>    
 		 </tr>
  	</logic:iterate>  	
  </logic:present>
  	<logic:present name="apartPage" scope="request">
  		<input type="hidden" name="curPage" value="<bean:write name='apartPage' property='curPage'/>">
  	</logic:present>
  	<%-- 
  	<logic:present name="rate" property="sourceVcId" scope="request">
  		<bean:define id="currency" name="rate" property="sourceVcId"/>
  		<html:hidden property="currCode" name="currency"/>
  	</logic:present>
  	<logic:present name="rate" property="rateDate" scope="request">
  		<html:hidden property="rateDate" name="rate"/>	
  	</logic:present>
	--%>
  	<logic:notPresent name="specValInfoLst" scope="request">
		<tr bgcolor="#FFFFFF">
			<td colspan=6 class="td_row1_left">�޼�¼��Ϣ��</td>
		</tr>
	</logic:notPresent>
	</html:form>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td height="5"></td></tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
	 <td><jsp:include page="/gznx/specVal/apartpage.jsp" flush="false"/>
	 </td>
  </tr>
</table>
</center>
</body>
</html>

