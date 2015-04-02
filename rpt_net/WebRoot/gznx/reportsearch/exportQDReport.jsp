<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<%
	Operator operator = (Operator) session
			.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepSearchPodedom = operator != null ? operator
			.getChildRepSearchPopedom() : "";

	/** 报表选中标志 **/
	String reportFlg = "0";
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null) {
		reportFlg = (String) session
				.getAttribute(Config.REPORT_SESSION_FLG);
	}

	String selectdate = request.getAttribute("date") != null ? request
			.getAttribute("date").toString() : "";
	String showmenu = (String) request.getAttribute("showmenu");
	if (showmenu == null)
		showmenu = "1";
%>
<jsp:useBean id="utilForm" scope="page"
	class="com.cbrc.smis.form.UtilForm" />
<jsp:useBean id="FormBean" scope="page"
	class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="reportFlg" name="FormBean"
	value="<%=reportFlg%>" />
<jsp:setProperty property="orgPodedom" name="FormBean"
	value="<%=childRepSearchPodedom%>" />
<html:html locale="true">
<head>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="0">
<link href="../../css/common.css" type="text/css" rel="stylesheet">
<script language="javascript" src="../../js/func.js"></script>
<script language="javascript" src="../../js/prototype-1.4.0.js"></script>
<script type="text/javascript" src="../../js/tree/tree.js"></script>
<script type="text/javascript" src="../../js/tree/defTreeFormat.js"></script>
<jsp:include page="../../calendar.jsp" flush="true">
	<jsp:param name="path" value="../../" />
</jsp:include>

<SCRIPT language="javascript">
		var scriptReportFlg=<%=reportFlg%>;
		var SPLIT_SYMBOL_COMMA="<%=Config.SPLIT_SYMBOL_COMMA%>";
		
		function _submit(form){
			if(form.date.value==""){
				alert("请输入报表时间!");
				form.year.focus();
				return false;
			}
		}	
	
		/**
		 * 复选导出excel报表 
		 * @return void
		 */
		function _selExport(){
			var objForm=document.getElementById("frmChk");
			var count=objForm.elements['countChk'].value;
			var dataRagId  = document.getElementById("dataRagId").value;
			if(dataRagId=="-99"){
				alert("请选择报送口径！")
				return;
			}
			var repInIds="";
			var obj=null;
			for(var i=1;i<=count;i++){
				try{
					obj=eval("objForm.elements['chk" + i + "']");
					if(obj.checked==true){					
						repInIds+=(repInIds==""?"":",") + obj.value;
					}
				}catch(e){}
			}	
			if(repInIds==""){
				alert("请选择要导出的报表!\n");
				return;
			}else{
				if(confirm("您确定要导出excel数据文件吗?\n")==true){
					window.location="<%=request.getContextPath()%>/servlets/downloadExcelQDServlet?repInIds=" + repInIds +"&dataRagId="+dataRagId;
				}
			}
		}
			
			function _selAllExport(){
				var objFrm=document.getElementById("frmChk");
				if(${Records==null || Records==""}){
						alert("无相应记录，无法执行导出");
						return;
				}
				if(scriptReportFlg=="1"){
				  	var qry="templateId=" + objFrm.elements['templateId'].value + 
			 	  			"&repName=" + objFrm.elements['repName'].value + 
			  	  			"&orgId=" + objFrm.elements['orgId'].value +   	  		
				  	  		"&repFreqId=" + objFrm.elements['repFreqId'].value +
				  	  		"&checkFlag=1";
					var datel = objFrm.elements['date'].value;
					
					if(datel != ""){
						var obj = datel.split("-");
				  		qry += "&year=" + obj[0];
				  		qry += "&term=" + obj[1];
					}
				
					if(confirm("您确定要导出全部数据文件吗?\n")==true){
						window.location="<%=request.getContextPath()%>/servlets/createSubOrgReportServlet?"+qry;
					}
				}else{
					var qry="templateId=" + objFrm.elements['templateId'].value + 
			 	  			"&repName=" + objFrm.elements['repName'].value + 
			  	  			"&orgId=" + objFrm.elements['orgId'].value +   	  		
				  	  		"&repFreqId=" + objFrm.elements['repFreqId'].value ;
					
					if(objFrm.elements['date'].value != ""){
				  		qry += "&date=" + objFrm.elements['date'].value;
					}
					
					if(objFrm.elements['bak1'].value != ""){
				  		qry += "&bak1=" + objFrm.elements['bak1'].value;
					}
	
					if(confirm("您确定要导出全部数据文件吗?\n")==true){
						window.location="<%=request.getContextPath()%>/servlets/createNXReportServlet?"+qry;
					}
				
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
			function _downLoadPrint(){
				var objForm=document.getElementById("frmChk");
				var count=objForm.elements['countChk'].value;
				var repInIds="";
				var obj=null;
				for(var i=1;i<=count;i++){
					try{
						obj=eval("objForm.elements['chk" + i + "']");
						if(obj.checked==true){					
							repInIds+=(repInIds==""?"":",") + obj.value;
						}
					}catch(e){}
				}	
				if(repInIds==""){
					alert("请选择要打印的报表!\n");
					return;
				}else{	
					window.open("<%=request.getContextPath()%>/exportPrintReport.do?repInIds=" + repInIds+"&date=<%=selectdate%>");
					
				}
			
			}

			function treeOnClick(id,value)
			{
				document.getElementById('bak1').value = id;
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
		
		
		/**
		 * 复选导出XML报表 
		 * @return void
		 */
		function _selExportXML(){
			var objForm=document.getElementById("frmChk");
			var count=objForm.elements['countChk'].value;
			var repInIds="";
			var obj=null;
			for(var i=1;i<=count;i++){
				try{
					obj=eval("objForm.elements['chk" + i + "']");
					if(obj.checked==true){					
						repInIds+=(repInIds==""?"":",") + obj.value;
					}
				}catch(e){}
			}	
			if(repInIds==""){
				alert("请选择要导出的报表!\n");
				return;
			}else{
				window.location="<%=request.getContextPath()%>/servlets/createSubOrgXMLReportServlet?repInIds="+repInIds;
			}
		}
		
		function _selAllExportXML(){
			var objFrm=document.getElementById("frmChk");
			if(${Records==null || Records==""}){
					alert("无相应记录，无法执行导出");
					return;
			}
		  	var qry="templateId=" + objFrm.elements['templateId'].value + 
	 	  			"&repName=" + objFrm.elements['repName'].value + 
	  	  			"&orgId=" + objFrm.elements['orgId'].value +   	  		
		  	  		"&repFreqId=" + objFrm.elements['repFreqId'].value +
		  	  		"&checkFlag=1";
			var datel = objFrm.elements['date'].value;
			
			if(datel != ""){
				var obj = datel.split("-");
		  		qry += "&year=" + obj[0];
		  		qry += "&term=" + obj[1];
			}
		
			if(confirm("您确定要导出全部数据文件吗?\n")==true){
				
				window.location="<%=request.getContextPath()%>/servlets/createSubOrgXMLReportServlet?"+qry;
			}
		}
		
		function exportQDreport(){
			var dataRagId  = document.getElementById("dataRagId").value;
			if(dataRagId=="-99"){
				alert("请选择报送口径！")
				return;
			}
			var unit  = document.getElementById("unit").value;
			var tabulator  = document.getElementById("Tabulator").value;
			var auditor  = document.getElementById("Auditor").value;
			var principal  = document.getElementById("Principal").value;
			if(tabulator==""||auditor==""||principal==""||unit==""){
				alert("请填写单位 ，填报人，审核人，负责人");
				return ;
			}
			var objForm=document.getElementById("frmChk");
			var count=objForm.elements['countChk'].value;
			var repInIds="";
			var obj=null;
			for(var i=1;i<=count;i++){
				try{
					obj=eval("objForm.elements['chk" + i + "']");
					if(obj.checked==true){					
						repInIds+=(repInIds==""?"":",") + obj.value;
					}
				}catch(e){}
			}	
			if(repInIds==""){
				alert("请选择要导出的报表!\n");
				return;
			}else{
				var ss  = encodeURI(encodeURI("repInIds=" + repInIds+"&dataRagId="+dataRagId+"&Tabulator="+tabulator+"&Auditor="+auditor+"&Principal="+principal+"&unit="+unit));
				if(confirm("您确定要导出全部数据文件吗?\n")==true){
					window.location="<%=request.getContextPath()%>/servlets/createQDXMLReportServlet?"+ss;
				}
			}
		}
		
	</script>
</head>
<body>
	<logic:present name="Message" scope="request">
		<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
	</logic:present>
	<table border="0" width="98%" align="center">
		<tr>
			<td height="3"></td>
		</tr>
		<tr>
			<td>当前位置 &gt;&gt; 报表查询 &gt;&gt; 报表导出</td>
		</tr>
	</table>
	<br>
	<html:form action="/exportQDtXml.do" method="POST" styleId="frmChk"
		onsubmit="return _submit(this)">
		<html:hidden property="orgId" />
		<html:hidden property="bak1" />
		

		<table cellSpacing="0" cellPadding="0" width="98%" border="0"
			align="center">
			<tr>
				<td>
					<fieldset id="fieldset">
						<table cellspacing="0" cellpadding="0" border="0" width="100%"
							align="center">
							<tr>
								<td height="25">&nbsp;报表编号： <html:text
										property="templateId" maxlength="10" size="10"
										styleClass="input-text" />
								</td>


								<td height="25" align="left">报表名称： <html:text
										property="repName" size="23" styleClass="input-text" />

								</td>
								<td height="25" align="left">报送频度： <html:select
										property="repFreqId">
										<html:option value="-999">--全部--</html:option>
										<html:optionsCollection name="utilForm" property="repFreqs"
											label="label" value="value" />
									</html:select>
								</td>

							</tr>

							<tr>
								<td>&nbsp;报表时间： <html:text property="date" readonly="true"
										size="10" styleId="date" style="text"
										onclick="return showCalendar('date', 'y-mm-dd');" /> <img
									src="../../image/calendar.gif" border="0"
									onclick="return showCalendar('date', 'y-mm-dd');">
								</td>
								<!-- 
								<td height="25" align="left">
									报表类型：
									<html:text property="templateTypeName" styleId="selectedTypeName" size="10" readonly="true" style="width:150px;cursor:hand" onclick="return showTree()" styleClass="input-text" ></html:text>
									<div id="orgTree" style="left:316px;top:70px;width:200px; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
									<script type="text/javascript">
										<bean:write  name="FormBean"  property="templateTypeTree" filter="false"/>
									    var tree= new ListTree("orgTree", TREE1_NODES,DEF_TREE_FORMAT,"","treeOnClick('#KEY#','#CAPTION#');");
								      	tree.init();
								 	</script>
								 	</div>										
								</td>
								 -->
								<td height="23" align="left">报表机构： <html:text
										property="orgName" readonly="true" size="23"
										style="width:150px;cursor:hand" onclick="return showTree1()"
										style="input-text"></html:text>
									<div id="orgpreTree"
										style="left: 316px; top: 70px; width: 150px; height: 0; background-color: #f5f5f5; border: 1px solid Silver;; overflow: auto; VISIBILITY: hidden; position: absolute; z-index: 2;">
										<script type="text/javascript">
										<bean:write  name="FormBean"  property="orgReportPodedomTree" filter="false"/>
									    var tree1= new ListTree("orgpreTree", TREE2_NODES,DEF_TREE_FORMAT,"","treeOnClick1('#KEY#','#CAPTION#');");
								      	tree1.init();
								 	</script>
									</div>
								</td>
								<td>
								报表口径：<select id="dataRagId">
								<option value="-99">==请选择报表口径==</option>
								<option value="1">境内</option>
								<option value="2">法人</option>
								<option value="3">并表</option>
								</select>
								</td>
								<td align="left"><html:submit styleClass="input-button"
										value=" 查 询 " />&nbsp;&nbsp;&nbsp;&nbsp;</td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>
		</table>
		
		<table cellSpacing="0" cellPadding="4" width="98%" border="0"
			align="center">
			<tr>
				<td><input type="button" value="复选导出Excel" class="input-button"
					onclick="_selExport()">&nbsp; &nbsp;&nbsp;&nbsp; <%
 	if ("1".equals(showmenu)) {
 %> <!-- <input type="button" value="全部导出Excel" class="input-button"
					onclick="_selAllExport()">&nbsp; &nbsp;&nbsp;&nbsp; --> <%
 	}
 %> <!-- 	<input type="button" value="复选打印" class="input-button" onclick="_downLoadPrint()">	
					 --> <%
 	if ("1".equals(reportFlg)) {
 %> <%
 	if ("1".equals(showmenu)) {
 %>  <%
 	}
 %> <input type="button" value="清单报表导出XML" class="input-button"
					onclick="exportQDreport()">&nbsp; &nbsp;&nbsp;&nbsp; <%
 	}
 %>


				</td>
				<td>			单位：<input type="text" id="unit"/>
								填报人：<input type="text" id="Tabulator"/>
								审核人：<input type="text" id="Auditor"/>
								负责人：<input type="text" id="Principal"/>
								</td>
			</tr>
		</table>
		<TABLE cellSpacing="0" width="98%" border="0" align="center"
			cellpadding="4">
			<TR>
				<TD>
					<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0"
						class="tbcolor">
						<tr class="titletab">
							<th colspan="10" align="center" id="list"><strong>模板列表</strong></th>
						</tr>
						<TR class="middle">
							<td width="5%" align="center" valign="middle"><input
								type="checkbox" name="chkAll" onclick="_selAll()"></td>

							<TD class="tableHeader" width="10%">报表编号</td>
							<TD class="tableHeader" width="25%">模板名称</TD>
							<TD class="tableHeader" width="8%">版本号</td>
							<TD class="tableHeader" width="12%">机构</td>
							<%-- <%
								if (reportFlg.equals("1")) {
							%>
							<TD class="tableHeader" width="8%">口径</td>
							<%
								}
							%> --%>
							<TD class="tableHeader" width="8%">币种</td>
							<TD class="tableHeader" width="8%">频度</td>
							<td class="tableHeader" width="10%">时间</td>

						</TR>
						<%
							int i = 0;
						%>
						<logic:present name="Records" scope="request">
							<logic:iterate id="item" name="Records">

								<TR bgcolor="#FFFFFF" onmouseover="changeToLavender(this)"
									onmouseout="changeToWhite(this)">
									<TD align="center">
										<%
											i++;
										%> <input type="checkbox" name="chk<%=i%>"
										value='<bean:write name="item" property="repInId"/>'>

									</TD>

									<td align="center"><bean:write name="item"
											property="childRepId" /></td>
									<TD align="center"><bean:write name="item"
											property="repName" /></TD>
									<td align="center"><bean:write name="item"
											property="versionId" /></td>
									<td align="center"> <bean:write name="item"
											property="orgName" /> </td>
									<%-- <%
										if (reportFlg.equals("1")) {
									%>
									<td align="center"> <bean:write name="item"
											property="dataRgTypeName" /> </td>
									<%
										}
									%> --%>
									<TD align="center"><bean:write name="item"
											property="currName" /></TD>
									<td align="center"> <bean:write name="item"
											property="actuFreqName" /></td>
									<TD align="center"><bean:write name="item" property="year" />
										- <bean:write name="item" property="term" /> <%
 	if (!reportFlg.equals("1")) {
 %>
										- <bean:write name="item" property="day" /> <%
 	}
 %></TD>

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
							<TD colspan="8" bgcolor="#FFFFFF"><jsp:include
									page="../../apartpage.jsp" flush="true">
									<jsp:param name="url" value="../../exportAFReport.do" />
								</jsp:include></TD>
						</tr>
					</table>
				</TD>
			</TR>
		</TABLE>
	</html:form>
</body>
</html:html>
