<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%
	/** 报表选中标志 **/
	String reportFlg = "3";
	
//	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
//		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
//	}
	
	Operator operator = (Operator) session
			.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepReportPodedom = operator != null ? operator.getChildRepReportPopedom()
		+" and viewOrgRep.childRepId in (select tmpl.id.templateId from AfTemplate tmpl where tmpl.templateType='" + reportFlg +"')" : "";
	String orgId = operator != null ? operator.getOrgId() : "";
	String selectOrgId = request.getAttribute("orgId") != null ? request
			.getAttribute("orgId").toString()
			: orgId;

	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
			
	String date = request.getAttribute("date").toString();
%>

<jsp:useBean id="utilSubOrgForm" scope="page" class="com.fitech.net.form.UtilSubOrgForm" />
<jsp:setProperty property="childRepReportPodedom" name="utilSubOrgForm" value="<%=childRepReportPodedom%>" />
<jsp:setProperty property="orgId" name="utilSubOrgForm" value="<%=orgId%>" />
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="orgPodedom" name="FormBean" value="<%=childRepReportPodedom%>"/>
<jsp:setProperty property="reportFlg" name="FormBean" value="<%=reportFlg%>"/>

<html:html locale="true">
<head>
	<html:base />

	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="<%=Config.WEBROOTULR%>/css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="<%=Config.WEBROOTULR%>/js/comm.js"></script>
	<script language="javascript" src="<%=Config.WEBROOTULR%>/js/func.js"></script>
	<script language="javascript" src="<%=Config.WEBROOTULR%>/js/prototype-1.4.0.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/tree/tree.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/tree/defTreeFormat.js"></script>
	<jsp:include page="../../calendar.jsp" flush="true">
	  <jsp:param name="path" value="../../"/>
	</jsp:include>
	<script language="javascript">	
	var SPLIT_SYMBOL_COMMA="<%=Config.SPLIT_SYMBOL_COMMA%>";
	
	/**
	 * 验证报送时间
	 */
	function _submit(form){
		if(form.date.value==""){
			alert("请选择报送时间!");
			form.date.focus();
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
	
	/**
     * 有选择下载数据文件
	 */
	function _downLoadTemplateCheck(){
	
		var objForm=document.getElementById("frmChk");
		var count=objForm.elements['countChk'].value;
		var repNames="";
		var obj=null;
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
			window.location="<%=request.getContextPath()%>/servlets/downLoadGatherReportServlet?repNames=" + repNames + "&orgId=<%=selectOrgId%>&date=<%=date%>&type=select";		
		}
	}	
	
	/**
	 * 下载单个数据文件
	 */
	function _downLoadSingle(repName){

		window.location="<%=request.getContextPath()%>/servlets/downLoadGatherReportServlet?repNames=" + repName + "&orgId=<%=selectOrgId%>&date=<%=date%>&type=notAll";		
	}
	
	/**
	 * 下载全部
	 */
	function _downLoadAll(){	
		//var reportName = "<logic:present name='reportName'><bean:write name='reportName'/></logic:present>";
		
		var repName = document.getElementById("repName").value;
		var bak1 = document.getElementById("bak1").value;
		var orgId = document.getElementById("orgId").value;
		
		window.location="<%=request.getContextPath()%>/servlets/downLoadGatherReportServlet?repNames="+repName
		+ "&orgId=" + orgId
		+ "&bak1=" + bak1
		+ "&date=<%=date%>&type=downAll";
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
	</script>

</head>

<body style="TEXT-ALIGN: center">

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
			<td>
				当前位置 &gt;&gt; 数据补录 &gt;&gt; 模板下载
			</td>
		</tr>
		<tr>
			<td height="3"></td>
		</tr>
	</table>
	<table cellSpacing="0" cellPadding="4" width="98%" border="0"
		align="center">
		<html:form action="/report/reportDownloadList.do" method="post"
			onsubmit="return _submit(this)" >
			<html:hidden property="bak1"/>
			<html:hidden property="orgId"/>
			<tr>
				<td>
					<fieldset id="fieldset">
						<table cellSpacing="0" cellPadding="4" width="98%" border="0"
							align="center">
							
							<tr>
								<td>
									&nbsp;报表时间：
									<html:text property="date" styleClass="input-text" size="10"
										styleId="date1" readonly="true"
										onclick="return showCalendar('date1', 'y-mm-dd');" />
									<img border="0" src="<%=basePath%>image/calendar.gif"
										onclick="return showCalendar('date1', 'y-mm-dd');">
								</td>

								<%--
									String org = (String) request.getAttribute("orgId");
								
								<td height="25">
									<input type="hidden" value="<%=org%>">
								</td>
								--%>
								<td>
									报表名称：
									<html:text property="repName" size="25"
										styleClass="input-text" />
								</td>
								<td height="25" align="left">
									&nbsp;报表机构：
									<html:text property="orgName" readonly="true" size="10" style="width:150px;cursor:hand" onclick="return showTree1()" style="input-text" ></html:text>
									<div id="orgpreTree" style="left:316px;top:70px;width:150px; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
									<script type="text/javascript">
										<bean:write  name="FormBean"  property="orgReportPodedomTree" filter="false"/>
									    var tree1= new ListTree("tree1", TREE2_NODES,DEF_TREE_FORMAT,"","treeOnClick1('#KEY#','#CAPTION#');");
								      	tree1.init();
								 	</script>
								 	</div>		
								</td>
								
								<td>
									<html:submit styleClass="input-button" value=" 查 询 " />
								</td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>
		</html:form>
	</table>
	<table cellSpacing="0" cellPadding="4" width="98%" border="0"
		align="center">
		<tr>
			<td>
				<input type="button" value="复选下载" class="input-button"
					onclick="_downLoadTemplateCheck()">
				&nbsp;
				<input type="button" value="下载全部" class="input-button"
					onclick="_downLoadAll()">
			</td>
		</tr>
	</table>
	<html:form action="/template/templateDownload.do" method="POST"
		styleId="frmChk">
		<TABLE cellSpacing="0" width="98%" border="0" align="center"
			cellpadding="4">
			<TR>
				<TD>
					<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0"
						class="tbcolor">
						<tr class="titletab">
							<th colspan="8" align="center" id="list">
								<strong>模板列表</strong>
							</th>
						</tr>
						<TR class="middle">
							<td width="5%" align="center" valign="middle">
								<input type="checkbox" name="chkAll" onclick="_selAll()">
							</td>
							<TD class="tableHeader" width="10%">
								机构名称
							</td>
							<TD class="tableHeader" width="8%">
								报表编号
							</td>
							<TD class="tableHeader" width="40%">
								模板名称
							</TD>
							<!-- 
							<TD class="tableHeader" width="5%">
								版本号
							</td>
							 -->
							<TD class="tableHeader" width="7%">
								币种
							</td>
							<TD class="tableHeader" width="5%">
								频度
							</td>
							<TD class="tableHeader" width="7%">
								期数
							</td>
							<TD class="tableHeader" width="10%">
								下载
							</td>
						</TR>
						<%
							int i = 0;
						%>
						<logic:present name="Records" scope="request">
							<logic:iterate id="item" name="Records">
								<%
									i++;
								%>
								<TR bgcolor="#FFFFFF"  onmouseover="changeToLavender(this)" onmouseout="changeToWhite(this)">
									<TD align="center">
										<input type="checkbox" name="chk<%=i%>"
											value='<bean:write name="item" property="templateId"/>_<bean:write name="item" property="versionId"/>_<bean:write name="item" property="orgId" />_<bean:write name="item" property="curId" />_<bean:write name="item" property="actuFreqID" />'>
									</TD>
									<td align="center">
										<bean:write name="item" property="orgName" />
									</td>
									<td align="center">
										<bean:write name="item" property="templateId" />
									</td>
									<TD align="center">
										<bean:write name="item" property="repName" />
									</TD>
									<!-- 
									<td align="center">
										<bean:write name="item" property="versionId" />
									</td>
									 -->
									<td align="center">
										<bean:write name="item" property="currName" />
									</td>
									<td align="center">
										<bean:write name="item" property="actuFreqName" />
									</td>
									<td align="center">
										<bean:write name="item" property="year" />-<bean:write name="item" property="term" />-<bean:write name="item" property="day" />
									</td>
									<td align="center">
										<a href="javascript:_downLoadSingle('<bean:write name="item" property="templateId"/>_<bean:write name="item" property="versionId"/>_<bean:write name="item" property="orgId" />_<bean:write name="item" property="curId" />_<bean:write name="item" property="actuFreqID" />')">下载</a>
									</td>
								</TR>
							</logic:iterate>
						</logic:present>
						<input type="hidden" name="countChk" value="<%=i%>">
						<logic:notPresent name="Records" scope="request">
							<tr bgcolor="#FFFFFF">
								<td colspan="8">
									暂无记录
								</td>
							</tr>
						</logic:notPresent>
					</TABLE>

					<table cellSpacing="1" cellPadding="0" width="100%" border="0">
						<tr>
							<TD colspan="8" bgcolor="#FFFFFF">
								<jsp:include page="../../apartpage.jsp" flush="true">
								<jsp:param name="url" value="../../report/reportDownloadList.do" />
								</jsp:include>
							</TD>
						</tr>
					</table>
				</TD>
			</TR>
		</TABLE>
	</html:form>
</body>
</html:html>
