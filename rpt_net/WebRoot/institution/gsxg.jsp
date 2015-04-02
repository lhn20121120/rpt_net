<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.form.MCellFormuForm,com.cbrc.smis.util.FitechUtil,com.cbrc.smis.common.Config"%>
<%@ page import="java.util.List,com.cbrc.smis.form.MCellToFormuForm"%>
<jsp:useBean id="expressionBean" scope="page" class="com.cbrc.smis.other.Expression" />
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>gszs.jsp</title>
<link href="<%=request.getContextPath()%>/css/table.css"
	rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath() %>/css/common.css" type="text/css" rel="stylesheet">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/ShowCalendar.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<link
	href="<%=request.getContextPath()%>/css/jqueryUI/themes/default/easyui.css"
	rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/css/jqueryUI/themes/icon.css"
	rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery.easyui.min.js"
	type="text/javascript"></script>
<script type="text/javascript">

/**
 * 公式类型:表内校验
 */
 var CELL_CHECK_INNER="<%=configBean.CELL_CHECK_INNER%>";
/**
 * 公式类型:表间校验
 */
 var CELL_CHECK_INNER="<%=configBean.CELL_CHECK_BETWEEN%>";
/**
 *分隔符1
 */
 var SPLIT_SMYBOL_COMMA="<%=configBean.SPLIT_SYMBOL_COMMA%>";
 /**
 *分隔符2
 */
 var SPLIT_SMYBOL_ESP="<%=configBean.SPLIT_SYMBOL_ESP%>";
 
/**
 * 关系表达式定义文件后缀
 */
 var EXT_TXT="<%=configBean.EXT_TXT%>";
	/**
	 * 导入表内表间关系表达式事件
	 */
	 function _load_gx(){
	 	 //openDialog("load_gx.jsp");
	 	 
	 	 var objForm=document.forms['frmBJGX'];
	 	 window.location="<%=request.getContextPath()%>/gznx/modtemplate/load_gx.jsp?" + 
	 	 	"childRepId=" + objForm.elements['childRepId'].value + 
	 	 	"&versionId=" + objForm.elements['versionId'].value + 
	 	 	"&reportName=" + encodeURI(objForm.elements['reportName'].value,true) +
	 	 	"&reportStyle=" + objForm.elements['reportStyle'].value +
	 	 	"&curPage=" + objForm.elements['curPage'].value;
	 }
	 
	 /**
	  * 表单提交事件
	  */
	  function _submit(){
	  	var objCount=document.getElementById("rCount");
	  	var count=eval(objCount.value);
	  	var formulaIds="";
	  	for(var i=0;i<count;i++){
	  		var objChk=eval("document.getElementById(\"chk" + i + "\")");
	  		if(objChk.checked){
	  			if(formulaIds!=""){
	  				formulaIds+= ","
	  			}
	  			formulaIds+= objChk.value;
	  		}
	  	}
	  	if(formulaIds==""){
	  		alert("请选择要删除的表达式!\n");
	  		return false;
	  	}
	  	if(confirm("您确定要删除当前选中的表达式吗?\n")==false){
	  		return false;
	  	}
		document.forms['frmBJGX'].elements['cellFormuIds'].value=formulaIds;
	  	return true;
	  }
	  
	  /**
	   * 返回事件
	   */
	   function _back(templateId,versionId){
	   	 var form=document.forms['frmBJGX'];
	   	 window.location="<%=request.getContextPath()%>/viewAFTemplateDetail.do?templateId="+templateId+"&versionId="+versionId+"&bak2=2";
	   }
	   
	   /**
	     * 全选操作
		 */			 
		  /**
	  	  * 全选操作
	  	  */
	  	  function _selAll(){
	  	  	var objSelAll=document.getElementById('chkAll');
	  	  	var count=document.getElementById('rCount').value;
	  	  	var objChk=null;
	  	  	for(var i=0;i<count;i++){
	  	  		objChk=eval("document.getElementById('chk" + i + "')");
	  	  		if(objSelAll.checked==false){
	  	  			objChk.checked=false;
	  	  		}else{
	  	  			objChk.checked=true;
	  	  		}
	  	  	}
	  	  }
  	  /*
  	  *新增表内表间校验公式
  	  */
  	 function _addValidateFormu(tempId,versionId,reportName){
  	 	var url="<%=request.getContextPath()%>/gznx/ToValidateFormu.do?templateId="+tempId+"&versionId="+versionId+"&reportName="+reportName;
  	 	window.location=url;
  	 }
  		  		   

 function downFile(tempId,versionId){
  		window.location="<%=request.getContextPath()%>/gznx/downBJGXInit.do?" + "childRepId="
				+ tempId + "&versionId=" + versionId;
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
	<p class="suoyinlan">
		当前位置<span>&gt;&gt;</span>报表管理<span>&gt;&gt;</span>模板查看<span>&gt;&gt;</span>查看详细信息<span>&gt;&gt;</span>修改表内表间关系
	</p>
	<div class="easyui-tabs">
		<!-- 口径1 -->
		<div title="监管校验" style="padding: 10px 20px">
		<table border="0" cellspacing="1" class="tbcolor">
				<tr>
					<th width="10%" align="center">序号</th>
					<th align="center">表达式</th>
					<th width="30%" align="center">展示列</th>
					<th width="10%" align="center">类型</th>
				</tr>
				<logic:present name="standardList" scope="request">
					<logic:iterate id="item" name="standardList" indexId="index">
						<tr bgcolor="#FFFFFF">
							<td align="center"><%=((Integer) index).intValue() + 1%></td>
							<td align="center"><bean:write name="item"
									property="formulaValue" /></td>
							<td align="center"><bean:write name="item"
									property="formulaName" /></td>
							<td align="center"><logic:equal name="item"
									property="validateTypeId" value="1">
									<font color="#008066">表内校验</font>
								</logic:equal> <logic:equal name="item" property="validateTypeId" value="2">
									<font color="#CC0000">表间校验</font>
								</logic:equal></td>
						</tr>
						
					</logic:iterate>
					
				</logic:present>
				<logic:notPresent name="standardList" scope="request">
					<tr>
						<td bgcolor="#FFFFFF" colspan="4">无校验公式</td>
					</tr>
				</logic:notPresent>
			</table>
		</div>
		<div title="自定义校验" style="padding: 10px 20px">
			<table border="0" cellspacing="1" class="tbcolor">
				<tr>
					<th align="center"><input type="checkbox" id="chkAll" onclick="_selAll()">全选</th>
					<th width="10%" align="center">序号</th>
					<th align="center">表达式</th>
					<th width="30%" align="center">展示列</th>
					<th width="10%" align="center">类型</th>
				</tr>
				<logic:present name="zidingyiList" scope="request">
				<%int count = 0;%>
					<logic:iterate id="item" name="zidingyiList" indexId="index">
						<tr bgcolor="#FFFFFF">
							<td align="center">
							<input type="checkbox" name="chk<bean:write name='index'/>" id="chk<bean:write name='index'/>" value="<bean:write name='item' property='formulaId'/>">
							</td>
							<td align="center"><%=((Integer) index).intValue() + 1%></td>
							<td align="center"><bean:write name="item"
									property="formulaValue" /></td>
							<td align="center"><bean:write name="item"
									property="formulaName" /></td>
							<td align="center"><logic:equal name="item"
									property="validateTypeId" value="1">
									<font color="#008066">表内校验</font>
								</logic:equal> <logic:equal name="item" property="validateTypeId" value="2">
									<font color="#CC0000">表间校验</font>
								</logic:equal></td>
						</tr>
						<%count++;%>
					</logic:iterate>
				<input type="hidden" name="rCount" id="rCount" value="<%=count%>">
				
				</logic:present>
				<logic:notPresent name="zidingyiList" scope="request">
					<tr>
						<td bgcolor="#FFFFFF" colspan="4">无校验公式</td>
					</tr>
				</logic:notPresent>
			</table>
			<form name="frmBJGX" method="post"
				action="<%=request.getContextPath()%>/gznx/deleteBJGX.do"
				onsubmit="return _submit(this)">
				<table border="0" cellpadding="0" cellspacing="4" width="90%"
					align="center">
					<tr>
						<td align="center"><!-- <input type="button" value="导入表达式"
							class="input-button" onclick="_load_gx()"> &nbsp;&nbsp;  -->
							<input type="button" value="导出表达式" class="input-button" onclick="downFile('${templateId}','${ versionId}')">
							&nbsp;&nbsp;
							 <input type="button" value=" 新 增 " class="input-button" onclick="_addValidateFormu('${templateId}','${ versionId}','${reportName} ')">
							&nbsp;&nbsp; 
							<logic:present name="zidingyiList" scope="request">
								<input type="submit" value=" 删 除 " class="input-button">
							</logic:present> &nbsp;&nbsp;
							 <input type="button" class="input-button" onclick="_back('${templateId}','${ versionId}')" value=" 返 回 ">
						</td>
					</tr>
				</table>
				<input type="hidden" name="templateId" value="${templateId}">
				<input type="hidden" name="versionId" value="${ versionId}">
				<input type="hidden" name="reportName" value="${ reportName}">
				<input type="hidden" name="cellFormuIds" value="">
			</form>
		</div>
	</div>
</body>
</html>