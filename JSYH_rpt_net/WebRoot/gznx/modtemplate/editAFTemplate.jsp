<%@ page language="java" pageEncoding="GB2312"%>
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
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="reportFlg" name="FormBean" value="<%=reportFlg%>"/>
<jsp:useBean id="utilFormBean"  scope="page" class="com.cbrc.smis.form.UtilForm" ></jsp:useBean>
<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" type="text/css" href="../css/pageControl.css" />
	<link href="../../css/calendar-blue.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="../../js/prototype-1.4.0.js"></script>
	<script type="text/javascript" src="../../calendar/calendar.js	"></script>
	<script type="text/javascript" src="../../calendar/calendar-cn.js"></script>
	<script language="javascript" src="../../calendar/calendar-func.js"></script>
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link href="../../css/tree.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="../../js/tree/tree.js"></script>
	<script type="text/javascript" src="../../js/tree/defTreeFormat.js"></script>
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript" src="../../js/jquery-1.4.2.js"></script>
	
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
		 	if(form.reportFile.value!="" && getExt(form.reportFile.value)!=EXT_RAQ)
		 	{
		 	 	alert("请载入RAQ格式的模版文件！");
		 		return false;
		 	}
			if(form.templateName.value=="")
			{
				alert("请输入模版名称！");
				form.templateName.focus();
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
			document.getElementById("subButton").disabled=true;
			return true;
		 }
		 
		 
		 //添加文件时自动把文件名填入名称中
			function autoSetName()
			{
			    var fileArray = getFileNameAndExtendName();
			    //文件扩展名
			    var extendName = fileArray[1];
	            
	            if(extendName!=".raq")
	            {
	            	alert("请载入raq格式的模版文件！");
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
			
			function _back(tenplateId,versionId){
				window.location="<%=request.getContextPath()%>/viewAFTemplateDetail.do?templateId="+tenplateId+"&versionId="+versionId+"&bak2=2";
			}
			
			function  setqdfile(){
			 	var dd = document.getElementById('reportStyle');
			 	if(dd.checked ){
			 		document.getElementById('tr_qdf').style.display='';
			 	} else{
			 		document.getElementById('tr_qdf').style.display='none'
			 	}
		 	}

		 	function setisReport(){
			 	var isReport = document.getElementById('isReport');
			 	if(isReport.checked ){
			 		isReport.value="1";
			 	}else
				 	isReport.value="2";
		 	}

		 	window.onload = function(){
			 	var reportStyle = "${afTemplateForm.reportStyle}";
			 	if(reportStyle=="2"){//清单报表
			 		document.getElementById('tr_qdf').style.display='';
			 		
			 		//var newTr_1 = $("<tr bgcolor='#ffffff'><td>新建数据表：</td><td><input type='checkbox' name='dropQDTableCheck' value='1' onclick='isDropQD(this)' /></td></tr>");
			 		//$("#isQDTR").after(newTr_1);
				}else
					document.getElementById('tr_qdf').style.display='none'
			}

			function isDropQD(obj){
				if($(obj).attr("checked")){
					if($("#reportFile").val()!="" && $("#qdreportFile").val()!="")
						$(obj).attr("checked",true);
					else{
						alert("请确保模板信息完整");
						$(obj).attr("checked",false);
					}
				}
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
	<html:form method="post" action="/gznx/afTemplateEdit.do?bak2=update" enctype="multipart/form-data" onsubmit="return _submit(this)">
		
		<table border="0" cellspacing="0" cellpadding="4" width="100%" align="center">
			<tr><td height="8"></td></tr>
			<tr>
				<td>
					当前位置 >> 报表管理 >> 修改报表模板
				</td>
			</tr>
			<tr><td height="10"></td></tr>		
		</table>
		<table cellpadding="4" cellspacing="1" border="0" width="100%" align="center" class="tbcolor" id="addTable">
			<tr class="titletab">
				<th align="center" colspan="2">
					修改报表模板
				</th>
			</tr>
			<tr align="center" bgcolor="#ffffff">
				<td align="center">
					<table cellspacing="2" cellpadding="2" border="0" width="70%" align="center">
						<TR bgcolor="#ffffff">
							<TD>
								请选择模板文件：
							</TD>
							<TD>
								<INPUT id="reportFile" class="input-text" type="file" size="30" name="reportFile" id="reportFile">
								（可以不选）
							</TD>
						</TR>
						<TR id="tr_qdf" bgcolor="#ffffff" style="display:none">
							<TD>								
							</TD>
							<TD>
								<INPUT id="qdreportFile" class="input-text" type="file" size="30" name="qdreportFile" id="qdreportFile" />
								
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								模板名称：
							</TD>
							<TD>
								<html:text property="templateName" size="30" maxlength="250" styleClass="input-text" styleId="templateName" />
								<html:hidden property="versionId" />
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								模板编号：
							</TD>
							<TD>								
								<html:text property="templateId" size="10" maxlength="6" readonly="true" styleClass="input-text" styleId="templateId" style="background-color:#E6E4E4" />
							</TD>
						</TR>	
						<TR bgcolor="#ffffff">
							<TD>
								版本号：
							</TD>
							<TD>								
								<html:text property="versionId" size="10" maxlength="6" readonly="true" styleClass="input-text" styleId="versionId" style="background-color:#E6E4E4" />
							</TD>
						</TR>						
						<tr bgcolor="#ffffff">
							<td >
								报表优先级：
							</td>
							<td>
								<html:select property="priorityFlag" styleId="priorityFlag">
									<html:option value="1">一级</html:option>
									<html:option value="2">二级</html:option>
									<html:option value="3">三级</html:option>
									<html:option value="4">四级</html:option>
									<html:option value="5">五级</html:option>			
								</html:select>
							</td>
						</tr>
						<% if(reportFlg.equals("2")){ %>
						<tr bgcolor="#ffffff">
							<td >
								报表批次：
							</td>
							<td>
								<html:select property="supplementFlag" styleId="supplementFlag">
									<html:option value=""> 	</html:option>
									<html:option value="0">第零批</html:option>				
									<html:option value="1" >第一批</html:option>
									<html:option value="2" >第二批</html:option>								
								</html:select>
							</td>
						</tr>
						<% } %>
						<tr bgcolor="#ffffff" id="isQDTR">
							<td >
								是否清单式报表：
							</td>
							<td>
								<html:checkbox property="reportStyle" value="2"  onclick="setqdfile()"></html:checkbox>	
							</td>
						</tr>
						<tr bgcolor="#ffffff">
							<td>
								是否补录：
							</td>
							<td>
								<html:checkbox property="isReport"  value="1"  onclick="setisReport()"></html:checkbox>						
							</td>
						</tr>
						<tr bgcolor="#ffffff">
							<td >
								起始日期：
							</td>
							<td>
								<html:text property="startDate" size="10"  readonly="true" styleId="startDate" style="text"/>
								<img src="../../image/calendar.gif" border="0" onclick="return showCalendar('startDate', 'y-mm-dd');">						
							</td>
						</tr>
						<tr bgcolor="#ffffff">
							<td >
								终止日期：
							</td>
							<td>
							<html:text property="endDate" size="10"  readonly="true" styleId="endDate" style="text"/>
							<img src="../../image/calendar.gif" border="0" onclick="return showCalendar('endDate', 'y-mm-dd');">							
							</td>
						</tr>
						<tr>
						<td>
						</td>
						</tr>
						<tr>
						</tr>
						<TR>
							<TD align="center" colspan="2">
								<input type="submit" name="save" value="保存模版" id="subButton">
								&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="button" onclick="_back('<bean:write name="afTemplateForm" property="templateId"/>','<bean:write name="afTemplateForm" property="versionId"/>')" value=" 返 回 ">
							
								</td>
								<td></td>
						</TR>
					</table>
				</td>
				<% if(!reportFlg.equals("2")){ %>
				<TD width="30%">
					<div align="center">
						<b>币种范围选择</b>
					</div>
					<br />
					<table width="100%" border="0" height="300" align="center" cellpadding="0"
						cellspacing="1" class="tbcolor">
						<tr>
						<td width="100%">
							<div  id="treeObj_org"
								style="width:100%; height:300;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;">
									<script type="text/javascript">
										<bean:write  name="afTemplateForm"  property="treeCurrContent" filter="false"/>
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
