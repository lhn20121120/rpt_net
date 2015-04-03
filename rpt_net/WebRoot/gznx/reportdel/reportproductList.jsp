<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config" %>
<%@ page import="com.cbrc.smis.security.Operator" %>
<%

	String reportFlg = "0";	
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
	}
	
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepSearchPopedom = operator != null ? operator.getChildRepSearchPopedom()
		+" and viewOrgRep.childRepId in (select tmpl.id.templateId from AfTemplate tmpl where tmpl.templateType='" + reportFlg +"')" : "";
	String orgId = operator != null ? operator.getOrgId() : "";
	String selectOrgId = request.getAttribute("orgId") != null ? request.getAttribute("orgId").toString() : orgId;

%>

<jsp:useBean id="utilFormBean" scope="page" class="com.cbrc.smis.form.UtilForm" />
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="reportFlg" name="FormBean" value="<%=reportFlg%>"/>
<jsp:setProperty property="orgPodedom" name="FormBean" value="<%=childRepSearchPopedom%>"/>
<html:html locale="true">
	<head>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" type="text/css" href="../css/pageControl.css" />
	<script type="text/javascript" src="../../js/prototype-1.4.0.js"></script>
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link href="../../css/tree.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="../../js/tree/tree.js"></script>
	<script type="text/javascript" src="../../js/tree/defTreeFormat.js"></script>
	<script language="javascript" src="../../js/func.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.4.2.js"></script>
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>
	
	<logic:present name="Message" scope="request">
		<script language="javascript">
			alert("<bean:write name='Message' property='alertMsg'/>");
		</script>
	</logic:present>

	</head>
	<script language="javascript">
	var SPLIT_SYMBOL_COMMA="<%=Config.SPLIT_SYMBOL_COMMA%>";
	var TOAEXCEL = false;
	function _submit(form){
		if(form.year.value==""){
			alert("请输入报表时间!");
			form.year.focus();
			return false;
		}
		if(form.month.value==""){
			alert("请输入报表时间！");
			form.month.focus();
			return false;
		}
		if(form.day.value==""){
			alert("请输入报表时间！");
			form.day.focus();
			return false;
		}
		if(isNaN(form.year.value)){ 
			alert("请输入正确的报表时间！"); 
			form.year.focus(); 
			return false; 
		}
		if(isNaN(form.month.value)){ 
		   alert("请输入正确的报表时间！"); 
		   form.month.focus(); 
		   return false; 
		}
		if(isNaN(form.day.value)){ 
		   alert("请输入正确的报表时间！"); 
		   form.day.focus(); 
		   return false; 
		}
		if(form.month.value <1 || form.month.value >12){
			alert("请输入正确的报表时间！");
			form.month.focus();
			return false;
		}
		if(form.day.value <1 || form.day.value >31){
			alert("请输入正确的报表时间！");
			form.day.focus();
			return false;
		}	
	}	
	
	/**
     * 全选操作
	 */			 
	function _selAll(){
		var formObj=document.getElementById("frmChk");
		var count=formObj.elements['countChk'].value;
		  	  	
		for(var i=1;i<=count;i++){
			try{
				if(formObj.elements['chkAll'].checked==false)
			  	  	eval("formObj.elements['chk" + i + "'].checked=false");
				else
			  	  	eval("formObj.elements['chk" + i + "'].checked=true");
			}catch(e){}
		}
	}
	
	var productFlag = "";

	/**
     * 有选择下载数据文件
	 */
	function _downLoadTemplateCheck(){
	
		var date = document.getElementById("date");
		var objForm=document.getElementById("frmChk");
		var count=objForm.elements['countChk'].value;
		var repNames="";
		var obj=null;
		
		productFlag = "sel";

		for(var i=1;i<=count;i++){
			try{
				obj=eval("objForm.elements['chk" + i + "']");
				if(obj.checked==true){
					repNames+=(repNames==""?"":SPLIT_SYMBOL_COMMA) + obj.value;
				}
			}catch(e){}
		}	
	  	if(repNames==""){
	  		if(TOAEXCEL)		
				TOAEXCEL = false;
			alert("请选择要生成的数据文件!\n");
			return;
		}else{
			location.href="<%=request.getContextPath()%>/servlets/downLoadNXReportServlet?repNames=" + repNames + "&orgId=<%=selectOrgId%>&date=" + date.value+"&toaexcel="+TOAEXCEL;
			if(TOAEXCEL)		
				TOAEXCEL = false;	   
		}
	}	
	
	/**
	 * 下载单个数据文件
	 */
	function _downLoadSingle(repName){		

		var date = document.getElementById("date");
				  	
		location.href="<%=request.getContextPath()%>/servlets/downLoadNXReportServlet?repNames=" + repName + "&orgId=<%=selectOrgId%>&date=" + date.value+"&toaexcel="+TOAEXCEL;
		   
	}
	
	/**
	 * 下载全部
	 */
	function _downLoadAll(){	
		
		var date = document.getElementById("date");
		var repName = document.getElementById("repName").value;
		var templateType = document.getElementById("templateType");
		var repfreqId = document.getElementById("repFreqId").value;
		if(${Records==null || Records==""})
		{
			alert("暂无报表记录，无法生成数据");
			return;
		}
		window.location="<%=request.getContextPath()%>/servlets/downLoadNXReportServlet?repName="+repName+"&date=" + date.value +  "&orgId=<%=selectOrgId%>" + "&templateType="+templateType.value+"&repFreqId="+repfreqId+"&type=downAll"+"&toaexcel="+TOAEXCEL;   
	}
	

	/**
     * 有选择下载数据文件
	 */
	function _productTemplate(){
	
		var date = document.getElementById("date");
		var objForm=document.getElementById("frmChk");
		var count=objForm.elements['countChk'].value;
		var repNames="";
		var obj=null;
		
		productFlag = "sel";

		for(var i=1;i<=count;i++){
			try{
				obj=eval("objForm.elements['chk" + i + "']");
				if(obj.checked==true){
					repNames+=(repNames==""?"":SPLIT_SYMBOL_COMMA) + obj.value;
				}
			}catch(e){}
		}	
	  	if(repNames==""){
			alert("请选择要下载的数据文件!\n");
			return;
		}else{
			try{
				//if(count>1){
				//	if(confirm("是否要合为一个excel?"))
				//		TOAEXCEL = true;
				//	else
						TOAEXCEL = false;
				//}
				LockWindows.style.display="";
				freqDiv.style.display="none";
			  	var upReportURL ="<%=request.getContextPath()%>/productReport.do?repNames=" + repNames + "&orgId=<%=selectOrgId%>&date=" + date.value;
			    var param = "radom="+Math.random();
			   	new Ajax.Request(upReportURL,{method: 'post',parameters:param,onComplete:upReportHandler,onFailure: reportError});
		   	}catch(e){
		   		alert('系统忙，请稍后再试...！');
		   	}
		}
	}	
		
	/**
	 * 下载全部
	 */
	function _productAll(){	
		
		var date = document.getElementById("date");
		var repName = document.getElementById("repName").value;
		var templateType = document.getElementById("templateType");
		var repfreqId = document.getElementById("repFreqId").value;
		
		productFlag = "all";
		
		try{
			LockWindows.style.display="";
			freqDiv.style.display="none";
			//if(confirm("是否要合为一个excel?"))
			//	TOAEXCEL = true;
			//else
				TOAEXCEL = false;
			
		  	var upReportURL ="<%=request.getContextPath()%>/productReport.do?repName="+repName+"&date=" + date.value +  "&orgId=<%=selectOrgId%>" + "&templateType="+templateType.value+"&repFreqId="+repfreqId+"&type=downAll";
		    var param = "radom="+Math.random();
		   	new Ajax.Request(upReportURL,{method: 'post',parameters:param,onComplete:upReportHandler,onFailure: reportError});
	   	}catch(e){
	   		alert('系统忙，请稍后再试...！');
	   	}
	}

	/*
	*同步上报数据
	*/
	function synsData(){
		var date = document.getElementById("date");
		var objForm=document.getElementById("frmChk");
		var count=objForm.elements['countChk'].value;
		var repNames="";
		var obj=null;
		
		productFlag = "sel";

		for(var i=1;i<=count;i++){
			try{
				obj=eval("objForm.elements['chk" + i + "']");
				if(obj.checked==true){
					repNames+=(repNames==""?"":SPLIT_SYMBOL_COMMA) + obj.value;
				}
			}catch(e){}
		}
	  	if(repNames==""){
			alert("请选择要同步的数据文件!\n");
			return;
		}else{
			try{
				var isAddTrace = false;
				if(confirm("是否保留数据调整"))
					isAddTrace = true;
				
				LockWindows.style.display="";
				freqDiv.style.display="none";
			  	var upReportURL ="<%=request.getContextPath()%>/synsDataAction.do?repNames=" + repNames + "&orgId=<%=selectOrgId%>&date=" + date.value +"&isaddtrace="+isAddTrace+"&radom="+Math.random();
			    var param = "radom="+Math.random();
			    $.post(upReportURL,function(result){
			    	LockWindows.style.display="none";
					freqDiv.style.display="block";
					document.getElementById("synsResult").value=result;
					document.getElementById("synsResultForm").submit();
			    	//var res = result.responseXML.getElementsByTagName('result')[0].firstChild.data;
			    	//window.open("<%=request.getContextPath()%>/gznx/reportdel/viewSynsDataResult.jsp?repInIds=" + result,'同步结果','scrollbars=yes,height=600,width=600,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
				    //alert(result);
				 });
			   //	new Ajax.Request(upReportURL,{method: 'post',parameters:param,onComplete:synsDataHandler,onFailure: reportError});
		   	}catch(e){
		   		alert('系统忙，请稍后再试...！');
		   	}
		}
	}

	function synsDataHandler(request){
		alert(result);
		//var result = request.getElementsByTagName('result')[0].firstChild.data;
		LockWindows.style.display="none";
		freqDiv.style.display="";
		
	}
	
	//报送Handler
	function upReportHandler(request){
		try{
			var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;
			if(result == '[true]'){
				//alert(productFlag);
				//document.getElementById("form1").submit();
				LockWindows.style.display="none";
				freqDiv.style.display="";

				if(productFlag=='all'){
					return _downLoadAll();
				}else{
					return _downLoadTemplateCheck();
				}
				
			}else{
				LockWindows.style.display="none";
				freqDiv.style.display="";
				alert(result);
			}
		}catch(e){}
	}
		//失败处理
	    function reportError(request)
	    {
	        alert('系统忙，请稍后再试...！');
	    }

	
	    function treeOnClick(id,value)
		{
			document.getElementById('templateType').value = id;
			document.getElementById('templateTypeName').value = value;
			document.getElementById("orgTree").style.height = "0";
			document.getElementById('orgTree').style.visibility="hidden"; 
		}

			

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
	
	function clostTree1(){
		if(document.getElementById('orgpreTree').style.visibility =='visible'){
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
		// add by 王明明
		function changeToLavender(obj){
			obj.bgColor="lavender";
		}
		function changeToWhite(obj){
			
			obj.bgColor="#FFFFFF";
		}
	</script>


	<body style="TEXT-ALIGN: center">
	
	<div id="LockWindows" style="display:none" class="black_overlay">
	<br><br><br><font color="red">操作执行中，请稍后……</font><br><br>
	<img src="../../image/loading.gif">
	</div>
	
		<table border="0" width="98%" align="center">
			
			<tr>
				<td>
					当前位置 >> 数据采集 >> 报表生成
				</td>
			</tr>
			<tr>
			<td height="4"></td>
			</tr>
		</table>
		<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
			<html:form action="/afReportProduct.do" styleId="form1" method="post" onsubmit="return _submit(this)">
			<html:hidden property="templateType"/>
			<html:hidden property="orgId"/>
			<tr>
				<td>
					<fieldset id="fieldset">					
						<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">				
							<tr>							
								<td>
									报表时间：
									<html:text property="date" readonly="true" size="10"  styleId="date"
									 	onclick="clostTree1();return showCalendar('date', 'y-mm-dd');" />
									<img src="../../image/calendar.gif" border="0" onclick="return showCalendar('date', 'y-mm-dd');">
									
								</td>
								
								<%
								 String org=(String)request.getAttribute("orgId");
								%>
				
								<td>
								报表名称：
								<html:text property="repName" size="23" styleClass="input-text" />
								
								</td>
								
								</tr>
								<tr>
								<td height="25" align="left">
									报表机构：
									<html:text property="orgName" readonly="true" size="10" style="width:150px;cursor:hand" onclick="return showTree1()" styleClass="input-text" ></html:text>
									<div id="orgpreTree" style="left:316px;top:70px;width:150px; height:0;background-color:#f5f5f5;border :1px solid Silver; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
									<script type="text/javascript">
										<bean:write  name="FormBean"  property="orgReportPodedomTree" filter="false"/>
									    var tree1= new ListTree("tree1", TREE2_NODES,DEF_TREE_FORMAT,"","treeOnClick1('#KEY#','#CAPTION#');");
								      	tree1.init();
								 	</script>
								 	</div>
								</td>	
								<td>
								报表频度：
								<div id="freqDiv" style="position:absolute;">
									<html:select property="repFreqId">
										<html:option value="-999">--全部--</html:option>
										<html:optionsCollection name="utilFormBean" property="repFreqs" label="label" value="value" />
									</html:select>
								</div>
								</td>					
						<!-- 		<td>
								报表类型：
								<html:text property="templateTypeName" styleId="selectedTypeName" size="10" style="width:150px;cursor:hand" onclick="return showTree()" styleClass="input-text" ></html:text>
									<div id="orgTree" style="left:316px;top:70px;width:150px; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
									<script type="text/javascript">
										<bean:write  name="FormBean"  property="templateTypeTree" filter="false"/>
									    var tree= new ListTree("tree", TREE1_NODES,DEF_TREE_FORMAT,"","treeOnClick('#KEY#','#CAPTION#');");
								      	tree.init();
								 	</script>
								 	</div>
								</td>	
								 -->													
								<td>
									<html:submit styleClass="input-button" value=" 查 询 " />
								</td>
								
							</tr>	
							<tr>
								<td height="3"></td>
							</tr>			
						</table>		
					</fieldset>		
				</td>
				</tr>				
			</html:form>
		</table>
		
		<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
			<tr>
				<td>
				 	<input type="button" value="复选生成" class="input-button" onclick="return _productTemplate()">
					&nbsp;
					<input type="button" value="生成全部" class="input-button" onclick="return _productAll()">
					&nbsp;
					<%
						if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT) && Config.SYSTEM_SCHEMA_FLAG==0){
					%>
					<input type="button" value="同步数据" class="input-button" onclick="return synsData()">
					<%
						}
					%>
					
					<%-- 
					&nbsp;&nbsp;∷&nbsp;&nbsp;
					<input type="button" value="复选下载" class="input-button" onclick="_downLoadTemplateCheck()" />&nbsp;
					<input type="button" value="下载全部" class="input-button" onclick="_downLoadAll()" />
					--%>
				</td>
			</tr>
		</table>
		<TABLE cellSpacing="0" width="98%" border="0" align="center" cellpadding="4">	
		<html:form action="/afReportProduct.do" method="POST" styleId="frmChk">						
							
				<TR>
					<TD>
						<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
							<tr class="titletab">
								<th colspan="11" align="center" id="list"><strong>报表列表</strong></th>
							</tr>
							<TR class="middle">
								<td width="5%" align="center" valign="middle">
									<input type="checkbox" name="chkAll" onclick="_selAll()">
								</td>
								
								<TD class="tableHeader" width="10%">报表编号</td>	
								<TD class="tableHeader" width="22%">报表名称</TD>																
								<TD class="tableHeader" width="8%">版本号</td>
								<TD class="tableHeader" width="8%">币种</td>
								
								<TD class="tableHeader" width="10%">机构名称</td>
								<% if(reportFlg.equals("1")){%>
								<TD class="tableHeader" width="10%">报表口径</td>	
								<% } else { %>
								<TD class="tableHeader" width="10%">报表币种</td>
								<% } %>
								<TD class="tableHeader" width="8%">报表频度</td>	
								<td class="tableHeader" width="12%">报表时间</td>
								<%--<TD class="tableHeader" width="10%">报表状态</td> --%>											
							</TR>
							<%int i = 0;%>
							<logic:present name="Records" scope="request">
								<logic:iterate id="item" name="Records">
									
									<TR bgcolor="#FFFFFF"  onmouseover="changeToLavender(this)" onmouseout="changeToWhite(this)">
										<TD align="center">											
											<%i++;%>
											<input type="checkbox" name="chk<%=i%>" value='<bean:write name="item" property="childRepId"/>_<bean:write name="item" property="versionId"/>_<bean:write name="item" property="dataRgId" />_<bean:write name="item" property="actuFreqID" />_<bean:write name="item" property="curId" />_<bean:write name="item" property="orgId" />'>											
										</TD>												
										
										<td align="center"><bean:write name="item" property="childRepId"/></td>											
										<TD align="center"><bean:write name="item" property="repName" /></TD>
										<td align="center"><bean:write name="item" property="versionId"/></td>
										<td align="center"><bean:write name="item" property="currName"/></td>
										<td align="center"><bean:write name="item" property="orgName"/></td>
										<% if(reportFlg.equals("1")){%>	
										<td align="center"><bean:write name="item" property="dataRgTypeName"/></td>
										<% } else { %>
										<td align="center"><bean:write name="item" property="currName"/></td>
										<% } %>
										<td align="center"><bean:write name="item" property="actuFreqName"/></td>
										<TD align="center">
											<bean:write name="item" property="year" />
											-
											<bean:write name="item" property="term" />
											-
											<bean:write name="item" property="day" />
										</TD>
										<%--<TD align="center" colspan="2">
											<!-- 已提交复核 -->
											<logic:notEqual name="item" property="checkFlag" value="3">已生成</logic:notEqual>
											<!-- 未填报 -->
											<logic:equal name="item" property="checkFlag" value="3">
												未生成												
											</logic:equal>
										</TD>
										 --%>																																				
									</TR>
								</logic:iterate>
							</logic:present>
							<input type="hidden" name="countChk" value="<%=i%>">											
							<logic:notPresent name="Records" scope="request">
								<tr bgcolor="#FFFFFF">
									<td colspan="10">暂无记录</td>
								</tr>
							</logic:notPresent>									
						</TABLE>
								
						<table cellSpacing="1" cellPadding="0" width="100%" border="0">
							<tr>
								<TD colspan="8" bgcolor="#FFFFFF">
									<jsp:include page="../../apartpage.jsp" flush="true">
										<jsp:param name="url" value="../../afReportProduct.do" />
									</jsp:include>
								</TD>
							</tr>
						</table>
					</TD>
				</TR>
			
			<logic:present name="ObjForm" scope="request">				
				<input type="hidden" name="day" value="<bean:write name='ObjForm' property='day'/>" />
				<input type="hidden" name="year" value="<bean:write name='ObjForm' property='year'/>" />
				<input type="hidden" name="month" value="<bean:write name='ObjForm' property='month'/>" />
			</logic:present>
		</html:form>
	</TABLE>
	<form action="<%=request.getContextPath() %>/gznx/reportdel/viewSynsDataResult.jsp" id="synsResultForm" style="display: none;" method="post" target="_blank">
		<input type="hidden" name="synsResult" id="synsResult"/>
	
	</form>
	</body>
</html:html>
