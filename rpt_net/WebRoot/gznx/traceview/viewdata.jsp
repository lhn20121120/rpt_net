<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@page import="java.util.List"%>
<%@page import="com.cbrc.smis.other.Aditing"%>
<%@page import="com.cbrc.org.form.AFDataTraceForm"%>
<%@page import="com.cbrc.smis.common.ApartPage"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	Operator operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepReportPodedom = operator != null ? operator.getChildRepReportPopedom() : "";
	String orgId = operator != null ? operator.getOrgId() : "";
	String selectOrgId = orgId;
	
	String curpage = (String)request.getAttribute("curPage");
	
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	
			/** 报表选中标志 **/
	String reportFlg = "0";
			
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
	}
%>

<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="reportFlg" name="FormBean" value="<%=reportFlg%>"/>
<jsp:setProperty property="orgPodedom" name="FormBean" value="<%=childRepReportPodedom%>"/>
<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="<%=basePath%>/css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="<%=basePath%>/js/func.js"></script>
	<script language="javascript" src="<%=basePath%>/js/prototype-1.4.0.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/tree/tree.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/tree/defTreeFormat.js"></script>
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>
	
	<style type="text/css">
		
	</style>
	<SCRIPT language="javascript">
	
	

 		  var _year ;	
		  var _term ;		
	        	

		//显示,关闭树型菜单
		function showTree(){
			if(document.getElementById('orgTree').style.visibility =='hidden'){
			    var textname = document.getElementById('selectedTypeName');
				document.getElementById("orgTree").style.top = getObjectTop(textname)+20;
				document.getElementById("orgTree").style.left = getObjectLeft(textname);
				
				document.getElementById("orgTree").style.height = "200";
				document.getElementById("orgTree").style.visibility = "visible";   // 显示树型菜单
			}
	
			else if(document.getElementById("orgTree").style.visibility == "visible"){
				document.getElementById("orgTree").style.height = "0";
				document.getElementById('orgTree').style.visibility="hidden";      //关闭树型菜单
			}
		}
		function treeOnClick1(id,value)
	{
		document.getElementById('orgId').value = id;
		document.getElementById('orgName').value = value;
		document.getElementById("orgpreTree").style.height = "0";
		document.getElementById('orgpreTree').style.visibility="hidden"; 
	}
	function showTree1(){
		if(document.getElementById('orgpreTree').style.visibility =='hidden'){
		    var textname = document.getElementById('orgName');
			document.getElementById("orgpreTree").style.top = getObjectTop(textname)+20;
			document.getElementById("orgpreTree").style.left = getObjectLeft(textname);
			
			document.getElementById("orgpreTree").style.height = "200";
			document.getElementById("orgpreTree").style.visibility = "visible";   // 显示树型菜单
		}

		else if(document.getElementById("orgpreTree").style.visibility == "visible"){
			document.getElementById("orgpreTree").style.height = "0";
			document.getElementById('orgpreTree').style.visibility="hidden";      //关闭树型菜单
		}
	}
	
		function closeTree(objTxt,objTree){	  
		   var obj = event.srcElement;
		   if(obj!=objTxt && obj!=objTree){
		     
		     objTree.style.height = "0";
		     objTree.style.visibility="hidden";      //关闭树型菜单
		   }
		}
		
		//距离文本框的水平相对位置
		function getObjectLeft(e)   
		{   
			var l=e.offsetLeft;   
			while(e=e.offsetParent)   
				l += e.offsetLeft;   
			return   l;   
		}   
		//距离文本框的垂直相对位置
		function getObjectTop(e)   
		{   
			var t=e.offsetTop;   
			while(e=e.offsetParent)   
				t += e.offsetTop;   
			return   t;   
		}

		//导出excel
		function viewAFdataTraceToExcel(){
			var count = "<%=((ApartPage)request.getAttribute(Config.APART_PAGE_OBJECT)).getCount()%>";
			if(count=="0" || count==""){
				alert("暂无数据，无法生成Excel");
				return;
			}
			var frmForm = document.getElementById("frm");
			frmForm.action="<%=basePath%>viewDataTraceToExcelAction.do";
			frmForm.submit();
		}
		//多条件查询
		function search(){
			var frmForm = document.getElementById("frm");
			frmForm.action="<%=basePath%>viewDataTraceAction.do";
			frmForm.submit();
		}
	</SCRIPT>
</head>
<% 
	com.cbrc.smis.system.cb.InputData.isOnLine = null;
	com.cbrc.smis.system.cb.InputData.messageInfo = "";
%>
<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>

  <label   id="prodress1" >
	<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 &gt;&gt; 报表查询 &gt;&gt; 痕迹查询
			</td>
		</tr>
	</table>
	<br>
	<table cellspacing="0" cellpadding="4" border="0" width="98%" align="center">
		<form action="<%=basePath%>viewDataTraceAction.do" method="post" id="frm">
			<tr>
				<td>
				<fieldset id="fieldset">
					<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
						<tr>
								<td height="7"></td>
							</tr>
						<tr>
							<td height="25">				
							&nbsp;报表期数：
								<input name="reportTerm" class="input-text" value="${afReportForm.reportTerm}" size="10" id="date1"
										readonly="true" onclick="return showCalendar('date1', 'y-mm-dd');" />
								<img border="0" src="<%=basePath%>image/calendar.gif"
										onclick="return showCalendar('date1', 'y-mm-dd');">
							</td>
							<td>
								报表名称：<input name="repName" value="${afReportForm.repName}" />
							</td>
							<td>报表机构：<input type="hidden" name="orgId" id="orgId" value="${orgId}"/>
								<input name="orgName" id="orgName" readonly="true" size="15" style="width:150px;cursor:hand" onclick="return showTree1()" styleClass="input-text"  value="${orgName}" />
								<div id="orgpreTree" style="left:316px;top:70px;width:150px; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
								<script type="text/javascript">
									<bean:write  name="FormBean"  property="orgReportPodedomTree" filter="false"/>
								    var tree1= new ListTree("orgpreTree", TREE2_NODES,DEF_TREE_FORMAT,"","treeOnClick1('#KEY#','#CAPTION#');");
							      	tree1.init();
							 	</script>
							</td>
							<td>
								报表条线:<select name="reportFlag" disabled="disabled" >
									<option selected="selected">--全部--</option>
									<logic:present name="Reportflg">
										<logic:equal value="1" name="Reportflg">
											<option selected="selected">银监</option>
										</logic:equal>
										<logic:equal value="2" name="Reportflg">
											<option selected="selected">人行</option>
										</logic:equal>
										<logic:equal value="3" name="Reportflg">
											<option selected="selected">其他</option>
										</logic:equal>
									</logic:present>
									<logic:notPresent name="Reportflg">
										<option>银监</option>
										<option>人行</option>
										<option>其他</option>
									</logic:notPresent>
								</select>
							</td>
							<td height="23" align="left">
								开始时间：
								<input name="beginDate" class="input-text" size="10" id="date2" value="${afReportForm.beginDate}"
										readonly="true" onclick="return showCalendar('date2', 'y-mm-dd');" />
								<img border="0" src="<%=basePath%>image/calendar.gif"
										onclick="return showCalendar('date2', 'y-mm-dd');">
							</td>
							<td>
								结束时间：
								<input name="endDate" class="input-text" size="10" id="date3" value="${afReportForm.endDate}"
										readonly="true" onclick="return showCalendar('date3', 'y-mm-dd');" />
								<img border="0" src="<%=basePath%>image/calendar.gif"
										onclick="return showCalendar('date3', 'y-mm-dd');">
							</td>
							
							<td>
								<input type="button" class="input-button" value=" 查 询 " onclick="search()" />
								
							</td>
						</tr>
					</table>
					</fieldset>
				</td>
			</tr>
			<tr>
				<td height="4"></td>
			</tr>
		</form>
	</table>
	<br />	
	<table border="0" cellpadding="0" cellspacing="0" width="98%" align="center">
		<tr>
			<input class="input-button" onclick="viewAFdataTraceToExcel()" type="button" value="导出Excel">
		</tr>
		<tr>
			<td>
			<br/>
				<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
					<tr class="middle" align="center">
						<th>报表名称</th>
						<th>业务项名称</th>
						<th>期数</th>
						<th style='width:50px'>修改人</th>
						<th style='width:200px'>修改时间</th>
						<th style='width:100px'>原始值</th>
						<th style='width:50px'>调整值</th>
						<th style='width:60px'>调整结果</th>
						<th style='width:150px;border-right:0px'>备注</th>
					</tr>
					<%
						List<AFDataTraceForm> formList = (List<AFDataTraceForm>)request.getAttribute("formList");
						if(formList!=null && formList.size()>0){
							for(AFDataTraceForm f : formList){
					%>
						<tr bgcolor="#FFFFFF" align="center">
							<td><%=f.getRepName() %></td>
							<td><%=f.getCellName() %></td>
							<td><%=f.getReportTerm() %></td>
							<td><%=f.getUsername() %></td>
							<td><%=f.getDateTime() %></td>
							<td><%=f.getOriginalData() %></td>
							<td><%=f.getChangeData() %></td>
							<td><%=f.getFinalData() %></td>
							<td><%=f.getDescTrace() %></td>
						</tr>
					<%
							}
						}else{
					%>
						<tr bgcolor="#FFFFFF">
							<td colspan="8">暂无数据</td>
						</tr>
					<%	
						}
					%>
					
				</table>
			</td>
		</tr>
	</table>
	<table cellSpacing="0" cellPadding="0" width="98%" border="0">
		<TR>
			<TD>
				<jsp:include page="../../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../../viewDataTraceAction.do" />
				</jsp:include>
			</TD>
		</TR>
	</table>
	</label>
</body>
</html:html>
