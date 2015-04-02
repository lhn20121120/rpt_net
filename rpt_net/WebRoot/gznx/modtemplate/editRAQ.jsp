<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.util.FitechUtil"%>
<%@ page import="com.cbrc.smis.adapter.StrutsMChildReportDelegate"%>
<%@ page import="com.cbrc.smis.common.Config"%>

<jsp:useBean id="utilFormBean" scope="page" class="com.cbrc.smis.form.UtilForm" />
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config"/>
<%
	Integer reportStyle = null;
	if(request.getAttribute("reportStyle")!=null) 
		reportStyle=Integer.valueOf(request.getAttribute("reportStyle").toString());

%>
<html:html locale="true">
	<head>
		<html:base/>
		<title>修改模板信息</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">		
		<script language="javascript" src="../../js/comm.js"></script>
		<script language="javascript" src="../../js/func.js"></script>
		<jsp:include page="../../calendar.jsp" flush="true">
			<jsp:param name="path" value="../../" />
		</jsp:include>
		<script language="javascript">
		 var EXT_RAQ='<%=Config.EXT_RAQ%>';
			/**
			 * 表单提交事件
			 */
			 function _submit(form){
				 	if(form.templateFile.value!="")
				  	{				 	
					 	if(getExt(form.templateFile.value)!=EXT_RAQ)
					 	{
					 	 	alert("请载入RAQ格式的模版文件！");
					 		return false;
					 	}
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
			 /**
			  * 返回事件
			  */
			  function _back(){
				  var childRepId = document.getElementById('childRepId').value;
				  var versionId = document.getElementById('versionId').value;
			  //yql 1.14
			  	window.location="<%=request.getContextPath()%>/template/viewTemplateDetail.do?childRepId="+ childRepId +"&versionId="+ versionId +"&bak2=2";
			  	
			  }
			   function  setqdfile(){
			 	var dd = document.getElementById('reportStyle');
			 	if(dd.checked ){
			 		document.getElementById('tr_qdf').style.display='';
			 	} else{
			 		document.getElementById('tr_qdf').style.display='none'
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
	<html:form method="post" action="/template/updateRAQ" enctype="multipart/form-data" onsubmit="return _submit(this)">
		 <logic:present name="mChildReportForm" scope="request">
		
		<table border="0" cellspacing="0" cellpadding="4" width="100%" align="center">
			<tr><td height="8"></td></tr>
			<tr>
				<td>
					当前位置 >> 报表管理 >> 报表定义管理 >> 模板修改>> 修改报表模板信息
				</td>
			</tr>
			<tr><td height="10"></td></tr>		
		</table>
		<table cellpadding="4" cellspacing="1" border="0" width="100%" align="center" class="tbcolor">
			<tr class="titletab">
				<th align="center">
					新增报表模板
				</th>
			</tr>
			<tr align="center" bgcolor="#ffffff">
				<td align="center">
					<table cellspacing="2" cellpadding="2" border="0" width="60%" align="center">
						<TR bgcolor="#ffffff">
							<TD>
								请选择模板文件：
							</TD>
							<TD>
								<INPUT id="templateFile" class="input-text" type="file" size="30" name="templateFile" >
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
						<tr bgcolor="#ffffff">
							<td >
								是否清单式报表：
							</td>
							<td>
							<html:checkbox property="reportStyle" value="2" onclick="setqdfile()"></html:checkbox>								
							</td>
						</tr>
						<TR bgcolor="#ffffff">
							<TD>
								模板编号：
							</TD>
							<TD>								
								<html:text property="childRepId" size="10" maxlength="6" readonly="true" styleClass="locked-text" />
								<font color="red">*不可修改</font>
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								模板版本：
							</TD>
							<TD>								
								<html:text property="versionId" size="10" maxlength="6" readonly="true" styleClass="locked-text" />
								<font color="red">*不可修改</font>
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								模板名称：
							</TD>
							<TD>
								<html:text property="reportName" size="50" maxlength="250" styleClass="input-text" />
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								模板类型：
							</TD>
							<TD>
								<html:select property="repTypeId" ><html:optionsCollection name="utilFormBean" property="repTypes"/>
								</html:select>
							</TD>
						</TR>

						<TR bgcolor="#ffffff">
							<TD>
								货币单位：
							</TD>
							<TD>
								<html:select property="curUnit"><html:optionsCollection name="utilFormBean" property="curUnits" />
								</html:select>
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
						<tr bgcolor="#FFFFFF">
							<td align="center">
								<input type="submit" value="确定" class="input-button" id="subButton">&nbsp;&nbsp;
								<input type="button" value="返回" class="input-button" onclick="_back()">
							</td>
						</tr>
					</table>

					<input type="hidden" name="reportStyle" value="<%=reportStyle%>">
					
				</td>
			</tr>			
		</table>
		</logic:present>
		</html:form>
		<br><br><br><br><br>
		<logic:notPresent name="mChildReportForm" scope="request">
			抱歉!发生未知错误!
		</logic:notPresent>		
	</body>
</html:html>