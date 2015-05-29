<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@page import="com.fitech.gznx.service.AfTemplateValiScheDelegate"%>
<%@page import="com.fitech.gznx.po.AfValidateScheme"%>
<%@page import="java.util.List"%>
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
	List<AfValidateScheme> list = (List<AfValidateScheme>)AfTemplateValiScheDelegate.findAfValidateScheme();
%>
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="reportFlg" name="FormBean" value="<%=reportFlg%>"/>
<jsp:useBean id="utilFormBean"  scope="page" class="com.cbrc.smis.form.UtilForm" ></jsp:useBean>
<html:html locale="true">
<head>
	<html:base/>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" type="text/css" href="../css/pageControl.css" />
	<link href="../../css/calendar-blue.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="../../js/prototype-1.4.0.js"></script>
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link href="../../css/tree.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="../../js/tree/tree.js"></script>
	<script type="text/javascript" src="../../js/tree/defTreeFormat.js"></script>
	<script language="javascript" src="../../js/func.js"></script>
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>
	
	<script type="text/javascript">
			/**
			 * 报表模板文件后缀
			 */
		 var EXT_RAQ="<%=Config.EXT_RAQ%>";
		 //模版名称检查结果
		 var reptNameCheckResult = false;
		 //模版编号检查结果
		 var reptIdCheckResult = false;
		 
		 //表单提交验证	 
		 function _submit(form)
		 {
			var subButton=document.getElementById("subButton");
		  	if(form.reportFile.value=="")
		  	{
			 	alert("请选择要载入的模板文件!\n");
			 	return false;
			}
			else
			{
			 	if(getExt(form.reportFile.value)!=EXT_RAQ)
			 	{
			 	 	alert("请载入RAQ格式的模版文件！");
			 		return false;
			 	}
			}
			
			if(form.reportStyle.checked){
				if(form.qdreportFile.value=="")
			  	{
				 	alert("请选择要载入的模板文件!\n");
				 	return false;
				}
				else
				{
				 	if(getExt(form.qdreportFile.value)!=EXT_RAQ)
				 	{
				 	 	alert("请载入RAQ格式的模版文件！");
				 		return false;
				 	}
				}
			}
			

			if(form.templateName.value=="")
			{
				alert("请输入模版名称！");
				form.templateName.focus();
				return false;
			}

			if(form.templateId.value=="")
			{
				alert("请输入模版编号！");
				form.templateId.focus();
				return false;
			}
			if(CheckNumber(form.templateId.value) == false)  //非法字符
			{
				alert("对不起,模版编号只能是数字或字母!");
				form.templateId.focus();
				return false;
			}
			
			if(form.versionId.value=="")
			{
				alert("请输入模版版本号！");
				form.versionId.focus();
				return false;
			}
			
			if(CheckNumber(form.versionId.value) == false)  //非法字符
			{
				alert("对不起,模版版本号只能是数字或字母!");
				form.versionId.focus();
				return false;
			}

			if(form.startDate.value=="")
			{
				alert("请输入起始时间！");
				form.startDate.focus();
				return false;
			}
			if(form.endDate.value=="")
			{
				alert("请输入结束时间！");
				form.endDate.focus();
				return false;
			}
			
			//检查
			function Check( reg, str ){
				if( reg.test( str ) ){
					return true;
				}
				return false;
			}
			// 检查数字
			function CheckNumber( str ){
				// var reg = /^\d*(?:$|\.\d*$)/;
			     var reg = /^[A-Za-z0-9]+$/;
				return Check( reg, str );
			}
 
			subButton.disabled=true;
			return true;
		 }
		 
		 function  setqdfile(){
		 	var dd = document.getElementById('reportStyle');
		 	if(dd.checked ){
		 		document.getElementById('tr_qdf').style.display='';
		 	} else{
		 		document.getElementById('tr_qdf').style.display='none';
		 	}
		 }
		 
		 window.onload = function(){
		 	 setqdfile();
		 }
		 
		 //添加文件时自动把文件名填入名称中
			function autoSetName()
			{
			    var fileArray = getFileNameAndExtendName();
			    //文件名
			    var fileName = fileArray[0];
			    //文件扩展名
			    var extendName = fileArray[1];
	            
	            if(extendName!=".raq")
	            {
	            	alert("请载入raq格式的模版文件！");
			    }
			    else	
			    {
			    	document.getElementById('templateName').value = fileName;			    
			    	document.getElementById("templateName").select();
			    }
		 	}
			
			/*获得文件名和文件扩展名*/
			function getFileNameAndExtendName()
			{
				var filePath = document.getElementById('reportFile').value;
	            var a = filePath.lastIndexOf("\\")+1;
	            var b = filePath.length;
	            var c = filePath.lastIndexOf(".");
	           
	            var fileName;
	          	var extendName;          	
	            
	            if(c!=-1)
	            {
	            	fileName = filePath.substring(a,c);
	            	extendName =filePath.substring(c);
	            }
	            else
	            {
	            	firstName= filePath.substring(a,b);
	            	extendName="";
	            }
	           
	            var fileArray = new Array();
	            fileArray[0] = fileName;
	            fileArray[1] = extendName;	 
	                       
	            return fileArray;
			}
			
			//检查填写信息
		function checkInfo(checkObj)
		{
			  var reptId = $("templateId").value;
			  var versionId = $("versionId").value;
			  var reptName = $("templateName").value;
			  if(checkObj=='reptId'&&reptId!='' && versionId !='')
			  {
			  	 checkReptId(reptId,versionId);
			  }
			  else if(checkObj=='reptName'&&reptName!='')
			  {
				  //checkReptName(reptName);
			  }
	     }
	     
	       //提交检查-模版ID，版本号 
	    function checkReptId(reptId,versionId)
	    {
	    	var url = "<%=request.getContextPath() %>/template/searchTemplateVersion.do?validate=reptId&childRepId="+reptId+"&versionId="+versionId;
			var param = "radom="+Math.random();
			new Ajax.Request(url,{method: 'post',parameters:param,onComplete:validateReptIDHandler,onFailure: reptIdCheckError});
			//$("checkResult_reptId").innerHTML="<font color='#ff0000'>信息检查中.....</font>";		  
	    } 
	
	     //模版ID验证Handler
		function validateReptIDHandler(request)
		{
			try
			{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
				if(result == 'false')  
				  {
				     $("checkResult_reptId").innerHTML="<img src='../../image/check_right.gif'/>";
				     reptIdCheckResult = true;
				  }	
				  else if(result == 'true')
				  {
					  $("checkResult_reptId").innerHTML="<img src='../../image/check_error.gif'/>该模版编号及版本已经存在!";
					      reptIdCheckResult = false;
				  }
			}
			catch(e)
			{}
	    }
	    
	    //查询失败处理－检查模版ID，版本号
	    function reptIdCheckError(request)
	    {
	         //$("checkResult_reptId").innerHTML="<img src='../../image/check_error.gif'/>系统忙，填写信息尚未检查！";
	          reptIdCheckResult = false;
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
	<html:form method="post" action="/afReportAdd" enctype="multipart/form-data" onsubmit="return _submit(this)">
		<table border="0" cellspacing="0" cellpadding="4" width="100%" align="center">
			<tr><td height="8"></td></tr>
			<tr>
				<td>
					当前位置 >> 报表管理 >> 新增报表模板
				</td>
			</tr>
			<tr><td height="10"></td></tr>		
		</table>
		<table cellpadding="4" cellspacing="1" border="0" width="100%" align="center" class="tbcolor">
			<tr class="titletab">
				<th align="center" colspan="2">
					新增报表模板
				</th>
			</tr>
			<tr align="center" bgcolor="#ffffff">
				<td align="center">
					<table cellspacing="2" cellpadding="2" border="0" width="60%" align="center">
						<tr bgcolor="#ffffff" >
							<td >
								是否清单式报表：
								<html:checkbox property="reportStyle" value="2" onclick="setqdfile()"></html:checkbox>
							</td>
							<td>
								&nbsp;&nbsp;&nbsp;是否补录：<html:checkbox property="isReport" value="1"/>
							</td>						
						</tr>
						<TR bgcolor="#ffffff">
							<TD>
								请选择模板文件：
							</TD>
							<TD>
								<INPUT id="reportFile" class="input-text" type="file" size="30" name="reportFile" onchange="autoSetName()">
								
							</TD>
						</TR>
						
						<TR id="tr_qdf" bgcolor="#ffffff" style="display:none">
							<TD>								
							</TD>
							<TD>
								<INPUT id="qdreportFile" class="input-text" type="file" size="30" name="qdreportFile" >
								（载入查询模板)
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								模板名称：
							</TD>
							<TD>
								<html:text property="templateName" size="30" maxlength="250" styleClass="input-text" styleId="templateName" /><label id="checkResult_reptName"></label>
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								模板编号：
							</TD>
							<TD>
								<html:text property="templateId" size="10" maxlength="10" styleClass="input-text" styleId="templateId" onblur="checkInfo('reptId')"/>
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								版本ID：
							</TD>
							<TD>
								<html:text property="versionId" size="10" onblur="checkInfo('reptId')" maxlength="10" styleClass="input-text" styleId="versionId" /><label id="checkResult_reptId"></label>
							</TD>
						</TR>
						<tr bgcolor="#ffffff">
							<td >
								报表优先级：
							</td>
							<td>
								<html:select property="priorityFlag">
									<option value="1">一级</option>					
									<option value="2" >二级</option>
									<option value="3" >三级</option>
									<option value="4" >四级</option>
									<option value="5" >五级</option>
								</html:select>
							</td>
						</tr>
						<% if(reportFlg.equals("2")){ %>
						<tr bgcolor="#ffffff">
							<td >
								报表批次：
							</td>
							<td>
								<html:select property="supplementFlag">
									<option value=""> </option>
									<option value="0">第零批</option>					
									<option value="1">第一批</option>
									<option value="2">第二批</option>									
								</html:select>
							</td>
						</tr>
						<% }if(reportFlg.equals("3")){ %>
						<tr bgcolor="#ffffff">
							<td >
								模板校验:
							</td>
							<td>
								<html:select property="schemeId">
									<%for(int i = 0 ;i<list.size();i++){%>
									<option value="<%=list.get(i).getValidateSchemeId()%>"><%=list.get(i).getValidateSchemeName()%></option>
									<%} %>
								</html:select>
							</td>
						</tr>
						<%} %>

						<tr bgcolor="#ffffff">
							<td >
								起始日期：
							</td>
							<td>
								<html:text property="startDate" size="10" value="" readonly="true" styleId="startDate" style="text"/>
								<img src="../../image/calendar.gif" border="0" onclick="return showCalendar('startDate', 'y-mm-dd');">						
							</td>
						</tr>
						<tr bgcolor="#ffffff">
							<td >
								终止日期：
							</td>
							<td>
							<html:text property="endDate" size="10" value="" readonly="true" styleId="endDate" style="text"/>
							<img src="../../image/calendar.gif" border="0" onclick="return showCalendar('endDate', 'y-mm-dd');">							
							</td>
						</tr>
						<tr>
						<td>
						</td>
						</tr>
						<TR>
							<TD align="center" colspan="2">
								<input type="submit" name="save" value="保存模版" id="subButton">&nbsp;&nbsp;
								<input type="button" value="返回" onclick="location.href='<%=Config.WEBROOTULR %>/afreportDefine.do'" >
							</TD>
						</TR>
					</table>
				</td>
				<% if(!reportFlg.equals("2")){ %>
				<TD width="30%">
					<div align="center">
						<b>报表币种范围选择</b>
					</div>
					<br />
					<table width="100%" border="0" height="300" align="center" cellpadding="0"
						cellspacing="1" class="tbcolor">
						<tr>
						<td width="100%">
							<div  id="treeObj_org"
								style="width:100%; height:300;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;">
									<script type="text/javascript">
										<bean:write  name="utilFormBean"  property="treeCurrContent" filter="false"/>
									    var currList= new ListTree("currList", TREE1_NODES, DEF_TREE_FORMAT,"currList");
								      	currList.init();								      	
								 	</script>
									</div>
								</td>
							</tr>
						</table>
					</TD>
					<% } %>
			</tr>
		</table>
	</html:form>
</body>
</html:html>
