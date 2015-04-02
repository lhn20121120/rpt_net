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
<title>特殊校验管理</title>
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
			window.alert('请选择要删除的汇率！');
			return;
			}
		if(window.confirm('确定要删除汇率吗？')){
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
	 * 表单提交验证
	 */
	 function _validate(form){
	  /* var sDateVal=form.startDate.value;
	   var eDateVal=form.endDate.value;
	   			   
	   if(_trim(sDateVal)!="" && _trim(eDateVal)!=""){
	     var sDate=_getDate(sDateVal);
	   	 var eDate=_getDate(eDateVal);
	   	 if(sDate>eDate){
		     alert("查询[起始日期]必须晚于[截止日期],请重新选择!\n");			     
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
			<td >当前位置 &gt;&gt; 系统管理 &gt;&gt; 特殊校验管理</td>
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
		  				名称：<html:text property="valName" size="20" />
		  				</td>
		  				<td>
		  					<html:submit value="查  询" styleClass="input-button"/>
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
  		&nbsp;校验信息列表
  	</th>
    <th align="right" colspan="3">
    	<%-- 
    	<input type="button" value="新  增" class="input-button" onClick="window.location.href('specialValMgr.do?method=toAdd<logic:notEmpty name='paramMap' scope='request'><logic:iterate id='param' name='paramMap'>&<bean:write name='param' property='key'/>=<bean:write name='param' property='value'/></logic:iterate></logic:notEmpty>');">
    	<input type="button" value="删  除" class="input-button" onClick="_delete()" >
    	--%>
    </th>
  </tr>
  <html:form action="/sysManage/specialValMgr.do?method=delete">
  <tr class="middle">
    <td align="center" width="10%"><input type="checkbox" name="checkId" onclick="checkAll()">序号</td>
    <td align="center" width="10%">校验类型</td>
    <td align="center" width="10%">名称</td>
    <td align="center" width="10%">公式</td>
    <td align="center" width="30%">描述</td>
    <td align="center" width="30%">样例</td>
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
			    <logic:equal name="specValidateInfo" property="valFlag" value="1">单一校验</logic:equal>
			    <logic:equal name="specValidateInfo" property="valFlag" value="2">组合校验</logic:equal>
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
			<td colspan=6 class="td_row1_left">无记录信息！</td>
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

