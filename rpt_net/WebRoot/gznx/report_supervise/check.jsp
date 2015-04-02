<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<%@ page import="com.cbrc.smis.form.MCellFormuForm,com.cbrc.smis.util.FitechUtil"%>
<jsp:useBean id="expressionBean" scope="page" class="com.cbrc.smis.other.Expression"/>
<html:html locale="true">
<%
		String flag_kp=String.valueOf(expressionBean.FLAG_KP);		
		String flag_bl=String.valueOf(expressionBean.FLAG_BL);
		String flag_bj=String.valueOf(expressionBean.FLAG_BJ);
		String flag_js=String.valueOf(expressionBean.FLAG_JS);
		String childRepId=null,versionId=null,reportName=null,reportStyle=null,templateId=null;
	if(request.getParameter("templateId")!=null) childRepId=(String)request.getParameter("templateId");
	if(request.getParameter("childRepId")!=null) childRepId=(String)request.getParameter("childRepId");
	if(request.getParameter("versionId")!=null) versionId=(String)request.getParameter("versionId");
	if(request.getAttribute("ReportName")!=null){  
		reportName=(String)request.getAttribute("ReportName");
		//// System.out.println("[bjgx.jsp(Attribute)]ReportName:" + reportName);
	}else{
		reportName=request.getParameter("reportName")==null?null:FitechUtil.getParameter(request,"reportName");		
		//// System.out.println("[bjgx.jsp(Request)]ReportName:" + reportName);
	}
	if(request.getParameter("reportStyle")!=null) reportStyle=(String)request.getParameter("reportStyle");
	
	if(childRepId!=null && versionId!=null && reportName!=null && reportStyle!=null){
		MCellFormuForm mCellForumForm = new MCellFormuForm();
		mCellForumForm.setChildRepId(childRepId);
		mCellForumForm.setVersionId(versionId);
		mCellForumForm.setReportName(reportName);
		mCellForumForm.setReportStyle(reportStyle);
		
		request.setAttribute("ObjForm",mCellForumForm);
	}	
	 request.setAttribute("reportName",reportName);
	 %>
	 <%
		/** 报表选中标志 **/
		String reportFlg = "0";
		
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		
		%>
	<head>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
		<link href="../../css/tree.css" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="../../js/tree/tree.js"></script>
		<script type="text/javascript" src="../../js/tree/defTreeFormat.js"></script>
		
		<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript">


			 
			 
			 /*逗号于校验公式中重复，所以采用双管道符进行分割，如再有问题，可改用其他符号 @author Yao*/
			 var SPLIT_SMYBOL_COMMA="<%=configBean.SPLIT_SYMBOL_COMMA%>";
			 
			
	
		
		
		
		 function _add_gx(){
			 	 var objForm=document.forms['CheckForm'];
			 	 
			 	 window.location="<%=request.getContextPath()%>/gznx/report_supervise/addcheck.jsp?" + 
			 	 	"childRepId=" + objForm.elements['childRepId'].value + 
			 	 	"&versionId=" + objForm.elements['versionId'].value + 
			 	 	"&reportName=" + objForm.elements['reportName'].value +
			 	 	"&reportStyle=" + objForm.elements['reportStyle'].value;
			 	 
			 	 
			 }
			 	 
			function _load_gx(){
			 	  var objForm=document.forms['CheckForm'];
			 	 
			 	 window.location="<%=request.getContextPath()%>/gznx/report_supervise/load_check.jsp?" + 
			 	 	"childRepId=" + objForm.elements['childRepId'].value + 
			 	 	"&versionId=" + objForm.elements['versionId'].value + 
			 	 	"&reportName=" + objForm.elements['reportName'].value +
			 	 	"&reportStyle=" + objForm.elements['reportStyle'].value;
			 }
			function toExcel(childRepId,versionId){
					if(${Records==null || Records=="[]"}){
						alert("暂无表达式，无需导出");
						return;
					}
					window.location="<%=request.getContextPath()%>/servlets/toReadExcelServlet?forflg=2&templateId=" + childRepId+"&versionId="+versionId;
			}
		 function history(){
			 	 window.location="<%=request.getContextPath()%>/afreportcheck.do" ;
			 }	 	  	  
			 	  
			/**
			  * 表单提交事件
			  */
			  function _submit(){
			  	var objCount=document.getElementById("rCount");
			  	var count=eval(objCount.value);
			  	var delCellFormuIds="";
			  	for(var i=0;i<count;i++){
			  		var objChk=eval("document.getElementById(\"chk" + i + "\")");
			  		if(objChk.checked==true)
			  			delCellFormuIds+=(isEmpty(delCellFormuIds)==true?"":SPLIT_SMYBOL_COMMA) + objChk.value;
			  	}
			  	
			  	if(isEmpty(delCellFormuIds)==true){
			  		alert("请选择要删除的表达式!\n");
			  		return false;
			  	}
			  	if(confirm("您确定要删除当前选中的表达式吗?\n")==false){
			  		return false;
			  	}
				
				document.forms['frmBJGX'].elements['cellFormuIds'].value=delCellFormuIds;
			  	return true;
			  }
			  
			  function back(){
				var objForm=document.forms['CheckForm'];
				window.location="<%=request.getContextPath()%>/afreportcheck.do";			  
			  }
			  
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
		  	  
		  	  function deleteFormula(){
		  	  
		  	  
		  	  }
		</script>
</head>
<body background="../../image/total.gif">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	
	<table border="0" cellspacing="0" cellpadding="4" width="98%" align="center">		
		<tr><td height="8"></td></tr>
		<tr>
			<td>
				当前位置 >> 报表管理 >> 校验关系管理
			</td>
		</tr>
		<tr><td height="10"></td></tr>		
		<tr>
			<td>
				<fieldset id="fieldset">
					<div id="server">
					<table id="tbl" border="0" cellpadding="4" cellspacing="1" width="100%" class="bgcolor" align="center">
						<tr class="titletab">
							<th colspan="5" align="center">
								<strong>									
										表内表间表达公式 
								</strong>
							</th>
						</tr>
						<tr>
						<!-- 注释掉勾选操作
							<td class="tableHeader" width="8%">
								<input type="checkbox" id="chkAll" onclick="_selAll()">
							</td>
							 -->
							<td class="tableHeader" width="8%">
								<b>序号</b>
							</td>
							<td class="tableHeader" width="35%">
								<b>表达公式</b>
							</td>
							<td class="tableHeader" width="42%">
								<b>表达式说明</b>
							</td>
							<td class="tableHeader" width="15%">
								<b>校验类别</b>
							</td>
							
							
						</tr>
							<logic:present name="Records" scope="request">
							<%int count = 0;%>
							<logic:iterate id="item" name="Records" scope="request" indexId="index">
								<tr bgcolor="#FFFFFF">
								<!-- 
									<td align="center">
										<input type="checkbox" name="chk<bean:write name='index'/>" id="chk<bean:write name='index'/>" value="<bean:write name='item' property='formulaId'/>">
									</td>
								 -->
									<td align="center">
									<%=((Integer) index).intValue() + 1%>
									</td>								
									<td align="center" bgcolor="#ffffff">
										<bean:write name="item" property="formulaValue"/>
									</td>
									<td align="center" bgcolor="#ffffff">
										<bean:write name="item" property="formulaName"/>
									</td>
									<td align="center" bgcolor="#ffffff">
										<bean:write name="item" property="validateTypeName"/>

										
									</td>
									
								</tr>
								<%count++;%>
							</logic:iterate>
							<input type="hidden" name="rCount" id="rCount" value="<%=count%>">
						</logic:present>
						<logic:notPresent name="Records" scope="request">
						<tr align="left">
							<td bgcolor="#ffffff" colspan="10">
							暂无校验信息
							</td>
						</tr>
						</logic:notPresent>
					</table>
					</div>
						<form name="frmBJGX" method="post" action="<%=request.getContextPath()%>/deletecheck.do" onsubmit="return _submit(this)">
						<table border="0" cellpadding="0" cellspacing="4" width="90%" align="center">
							<tr>
								<td align="center">  
									<input type="button" class="input-button" onclick="toExcel('<%=childRepId %>','<%=versionId %>')" value=" 导出excel ">
							 	    &nbsp;&nbsp;
							 	     <logic:present name="<%=Config.RECORDS%>" scope="request">
                                     <!-- <input type="submit" value=" 删 除 " class="input-button"> -->
                                    </logic:present>
									&nbsp;&nbsp;
									<input type="button" class="input-button" onclick="back()" value=" 返 回 "/>
									
								</td>
							</tr>
						</table>
						<input type="hidden" name="cellFormuIds" value="">
						<logic:present name="ObjForm" scope="request">
							<input type="hidden" name="childRepId" value="<bean:write name='ObjForm' property='childRepId'/>">
							<input type="hidden" name="versionId" value="<bean:write name='ObjForm' property='versionId'/>">
							<input type="hidden" name="reportName" value="<bean:write name='ObjForm' property='reportName'/>" />
							<input type="hidden" name="reportStyle" value="<bean:write name='ObjForm' property='reportStyle'/>"/>
						</logic:present>
						<logic:notPresent name="ObjForm" scope="request">
							<html:hidden property="childRepId" />
							<html:hidden property="versionId" />
							<html:hidden property="reportName" />
							<html:hidden property="reportStyle"/>
						</logic:notPresent>
						<input type="hidden" name="expression" value="">
					</form>
						
				</fieldset>
			</td>
		</tr>

	</table>
</body>
</html:html>

