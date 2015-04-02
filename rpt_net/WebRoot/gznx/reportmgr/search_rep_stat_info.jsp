<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.security.Operator,com.cbrc.smis.common.Config"%>
<%
	/** 报表选中标志 **/
	String reportFlg = "0";	
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
	}
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepSearchPodedom = "";
	if(operator != null){
	if(reportFlg.equals("0")){
		childRepSearchPodedom = operator.getChildRepSearchPopedom();
	}else{
		childRepSearchPodedom = operator.getChildRepSearchPopedom()
			+" and viewOrgRep.childRepId in (select tmpl.id.templateId from AfTemplate tmpl where tmpl.templateType=" + reportFlg +")";
	}		
	}
	
	
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<jsp:useBean id="utilSubOrgForm" scope="page" class="com.fitech.net.form.UtilSubOrgForm" />
<jsp:setProperty property="childRepSearchPodedom" name="utilSubOrgForm" value="<%=childRepSearchPodedom%>"/>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm"/>
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="orgPodedom" name="FormBean" value="<%=childRepSearchPodedom%>"/>
<jsp:setProperty property="reportFlg" name="FormBean" value="<%=reportFlg%>"/>
<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="<%=request.getContextPath()%>/css/common.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="<%=request.getContextPath()%>/js/func.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/tree/tree.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/tree/defTreeFormat.js"></script>
		<script language="javascript" src="<%=request.getContextPath()%>/js/func.js"></script>
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>

	<SCRIPT language="javascript">
		<logic:present name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="<bean:write name='ApartPage' property='curPage'/>";
	    </logic:present>			
	    <logic:notPresent name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="1";
	    </logic:notPresent>    
	      
		/**
		 * 表单提交验证（验证报表查询日期是否有效）
		 * 
		 * @author jcm
		 * @date 2008-01-15
		 */
		function _submit(form){

			if(form.date.value == ''){
				alert("请输入正确的报表时间！");
				form.term.focus();
				return false;
			}
			
			return true;
		}
		
		/**
		 * 浏览Excel报表数据
		 * 
		 * @author jcm
		 * @date 2008-01-15
		 */
		function viewExcel(repInId){
			window.location="<%=request.getContextPath()%>/servlets/toExcelServlet?repInId=" + repInId; 
		}
		/**
	     * 全部导出操作
		 */
		function _allExport(){
			var objFrm=document.forms['frm'];
			
		  	var qry="childRepId=" + objFrm.elements['childRepId'].value + 
	 	  			"&repName=" + objFrm.elements['repName'].value + 
	  	  			"&frOrFzType=" + objFrm.elements['frOrFzType'].value + 	
	  	  			"&orgId=" + objFrm.elements['orgId'].value +   	  		
		  	  		"&repFreqId=" + objFrm.elements['repFreqId'].value + 
		  	  		"&curPage=" + curPage;
			  	
		  	if(objFrm.elements['year'].value != "")
		  		qry += "&year=" + objFrm.elements['year'].value;
		  	if(objFrm.elements['term'].value != "")
		  		qry += "&term=" + objFrm.elements['term'].value;
		  	if(objFrm.elements['checkFlag'].value != '-999')
		  		qry += "&checkFlag=" + objFrm.elements['checkFlag'].value;
		  		
			if(confirm("您确定要导出全部数据文件吗?\n")==true){
				window.location="<%=request.getContextPath()%>/servlets/createSubOrgReportServlet?"+qry;
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
					else{						
						eval("formObj.elements['chk" + i + "'].checked=true");
					}
				}catch(e){}
			}
		}
		  	 		  	 
		/**
		 * 复选导出报表 
		 * @return void
		 */
		function _selExport(){
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
				window.location="<%=request.getContextPath()%>/servlets/createSubOrgReportServlet?repInIds=" + repInIds;
			}
		}
		/**
	     * 全部导出结果操作
		 */
		function _Export(){
			var objFrm=document.forms['frm'];
			if(!isEmpty(objFrm.elements['year'].value) && !CheckNum(objFrm.elements['year'].value)){
				alert("请输入正确的报表的查询年份!\n");		  	 		
				objFrm.elements['year'].focus();
				return;
			}
			if(!isEmpty(objFrm.elements['term'].value) && !CheckNum(objFrm.elements['term'].value)){
				alert("请输入正确的报表的查询月份!\n");		  	 		
				objFrm.elements['term'].focus();
				return;
			}
			if(isNaN(objFrm.elements['year'].value)){ 
			   alert("请输入正确的报表时间！"); 
			   objFrm.elements['year'].focus(); 
			   return ; 
			}
			if(isNaN(objFrm.elements['term'].value)){ 
			   alert("请输入正确的报表时间！"); 
			   objFrm.elements['term'].focus(); 
			   return ; 
			}
			if(objFrm.elements['term'].value <1 || objFrm.elements['term'].value >12){
				alert("请输入正确的报表时间！");
				objFrm.elements['term'].focus();
				return ;
			}
		  	var qry="childRepId=" + objFrm.elements['childRepId'].value + 
	 	  			"&repName=" + objFrm.elements['repName'].value + 
	  	  			"&frOrFzType=" + objFrm.elements['frOrFzType'].value + 	
	  	  			"&orgId=" + objFrm.elements['orgId'].value +   	  		
		  	  		"&repFreqId=" + objFrm.elements['repFreqId'].value + 
		  	  		"&curPage=" + curPage;
			  	
		  	if(objFrm.elements['year'].value != "")
		  		qry += "&year=" + objFrm.elements['year'].value;
		  	if(objFrm.elements['term'].value != "")
		  		qry += "&term=" + objFrm.elements['term'].value;
		  	if(objFrm.elements['checkFlag'].value != '-999')
		  		qry += "&checkFlag=" + objFrm.elements['checkFlag'].value;
		  		
			window.location="<%=request.getContextPath()%>/reportSearch/viewReportStatNX.do?method=export&"+qry;
			
		}	
			
			function viewPdf(repInId,templateId,versionId,curId,repFreqId,year,term,day,orgId){
				 window.open("<%=request.getContextPath()%>/editAFReport.do?statusFlg=1&repInId=" + repInId+"&templateId="+templateId+"&versionId="+versionId+"&curId="+curId+"&repFreqId="+repFreqId+"&year="+year+"&term="+term+"&day="+day+"&orgId="+orgId); 
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
	</SCRIPT>
</head>
<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 &gt;&gt; 报表查询 &gt;&gt; 报表查询
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center">
		<html:form action="/reportSearch/viewReportStatNX" method="post" styleId="frm" onsubmit="return _submit(this)">
			<html:hidden property="bak1"/>
			<html:hidden property="isLeader"/>
			<html:hidden property="orgId"/>
			
			<tr>
				<td>
					<fieldset id="fieldset">
						<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
							<tr>
								<td height="5"></td>
							</tr>
							<tr>
								<td height="20">&nbsp;
									报表编号：
									<html:text property="templateId" maxlength="10" size="10" styleClass="input-text"/>
								</td>
								
								
								<td height="15" align="left">
									报表名称：
									<html:text property="repName" size="23" styleClass="input-text" />
									
								</td>
								<td height="10" align="left">
									报表状态：
									<html:select property="checkFlag" size="1">
										<html:option value="-999">--全部--</html:option>
										<!-- 暂时用-10 -->
										<html:option value="-10">未上传</html:option>
										<html:option value="0">未审签</html:option>
										<html:option value="1">审签通过</html:option>
										<html:option value="-1,-5">审签未通过</html:option>
									</html:select>			
								</td>
								<td height="20" align="left">
									报表批次：
									<html:select property="supplementFlag" styleId="supplementFlag">
										<html:option value="-999">--全部--</html:option>
										<html:option value="0">第零批&nbsp;&nbsp;</html:option>
										<html:option value="1">第一批</html:option>
										<html:option value="2">第二批</html:option>
									</html:select>
							   </td>
							   <td height="20" align="left">
									任务列表：
									<html:select property="taskId" styleId="taskId">
										<html:option value="-999">--全部--</html:option>
										<logic:iterate id="viewReportInInfo" name="taskInfo">
										<option value="<bean:write name='viewReportInInfo' property='id.taskId'/>"><bean:write name='viewReportInInfo' property='id.taskName'/></option>
										</logic:iterate>
									</html:select>
									<script type="text/javascript">
										var se = document.getElementById("taskId");
										var ops = se.options;
										var taskId = "<bean:write name='afReportForm' property='taskId'/>";
										for(i=0;i<ops.length;i++){
											if(ops[i].value==taskId){
												ops[i].selected=true;
											}
										}
									</script>
								</td>
							</tr>
							<tr><td height="2"></td></tr>											
							<tr>
								<td height="20" align="left">&nbsp;
									报表时间：
									<html:text property="date" styleClass="input-text" size="10" styleId="date1"
											readonly="true" onclick="return showCalendar('date1', 'y-mm-dd');" />
									<img border="0" src="<%=basePath%>image/calendar.gif"
											onclick="return showCalendar('date1', 'y-mm-dd');">
								</td>
								<td height="15" align="left">
									报表机构：
									<html:text property="orgName" readonly="true" size="23" style="width:150px;cursor:hand" onclick="return showTree1()" style="input-text" ></html:text>
									<div id="orgpreTree" style="left:316px;top:70px;width:150px; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
									<script type="text/javascript">
										<bean:write  name="FormBean"  property="orgReportPodedomTree" filter="false"/>
									    var tree1= new ListTree("orgpreTree", TREE2_NODES,DEF_TREE_FORMAT,"","treeOnClick1('#KEY#','#CAPTION#');");
								      	tree1.init();
								 	</script>
								 	</div>
									
								</td>
								<!-- 
								<%if(!reportFlg.equals("0")){ %>
								<td height="25" align="left">
									报表类型：
									<html:text property="templateTypeName" styleId="selectedTypeName" size="10" style="width:150px;cursor:hand" onclick="return showTree()" styleClass="input-text" ></html:text>
									<div id="orgTree" style="left:316px;top:70px;width:150px; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
									<script type="text/javascript">
										<bean:write  name="FormBean"  property="templateTypeTree" filter="false"/>
									    var tree= new ListTree("orgTree", TREE1_NODES,DEF_TREE_FORMAT,"","treeOnClick('#KEY#','#CAPTION#');");
								      	tree.init();
								 	</script>
								 	</div>	
								</td>
								<% } %>
								 -->
								<td height="10" align="left">
									报送频度：
									<html:select property="repFreqId">
										<html:option value="-999">--全部--</html:option>
										<html:optionsCollection name="utilForm" property="repFreqs" label="label" value="value" />
									</html:select>
									
								</td>
								<td height="20" align="left">
									强制上报：
									<html:select property="isFlag">
										<html:option value="all">--全部--</html:option>
										<html:option value="force">强制报送</html:option>
										<html:option value="normal">正常报送</html:option>
									</html:select>
								</td>
<%-- 								<td height="25" align="left">
								模板类型：
									<html:select property="isReport" >
										<html:option value="-999" >--全部类型--</html:option>
										<html:optionsCollection name="utilForm" property="templateTypes" label="label" value="value" />
									</html:select>			
								</td>
								--%>	
								<!-- 
								<td height="25" align="left">
									报表状态：
									<html:select property="checkFlag" size="1">
										<html:option value="-999">--全部--</html:option>
										
										<html:option value="-10">未报送</html:option>
										<html:option value="0">未复核</html:option>
										<html:option value="1">复核通过</html:option>
										<html:option value="-1">复核未通过</html:option>
									</html:select>			
								</td>
								 -->
								 <td align="left"><span style="width:70px;"></span>
								<html:submit styleClass="input-button" value=" 查 询 " />
								<!--  <input type="button" value="导出结果" class="input-button" onclick="_Export()">	 -->
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
	<br />
	<!-- 
	<logic:present name="Records" scope="request">
		<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
			<tr>
				<td>
					<input type="button" value="复选导出" class="input-button" onclick="_selExport()">
					&nbsp;
					<input type="button" value="全部导出" class="input-button" onclick="_allExport()">					
				</td>
			</tr>
		</table>
	</logic:present>
	<logic:notPresent name="Records" scope="request">
		<br/>
	</logic:notPresent>
	 -->
	<table border="0" cellpadding="0" cellspacing="0" width="98%" align="center">
		<html:form action="/reportSearch/searchReport" method="post" styleId="frmChk">
			<tr>
				<td>
					<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
						<TR class="titletab">
						<!--  
							<th align="center" valign="middle">
								<input type="checkbox" name="chkAll" onclick="_selAll()">
							</th>
							-->
							<th width="8%"align="center" valign="middle">
								报表编号
							</th>
							<th  width="35%"  align="center" valign="middle">
								报表名称
							</th>
							<!-- 
							<th align="center" valign="middle">
								版本号
							</th>
							 -->
							<th width="7%" align="center" valign="middle">
								币种
							</th>
							<th width="5%" align="center" valign="middle">
								频度
							</th>
							<th width="10%" align="center" valign="middle">
								报表时间
							</th>
							<th width="15%" align="center" valign="middle">
								报送机构
							</th>
							<!-- 
							<th width="7%" align="center" valign="middle">
								报表类型
							</th>
							<th width="7%" align="center" valign="middle">
								模板类型
							</th>
							 -->
							 <Th width="6%" align="center" valign="middle">
								强制上报
							</Th>
							<Th width="6%" align="center" valign="middle">
								状态
							</Th>
						</TR>
						<%int i = 0;%>
						<logic:present name="Records" scope="request">
							<logic:iterate id="viewReportInInfo" name="Records">
								
								<TR bgcolor="#FFFFFF"  onmouseover="changeToLavender(this)" onmouseout="changeToWhite(this)">
								<!-- 
								<td align="center">
								<logic:notEmpty name="viewReportInInfo" property="checkFlag">
								<logic:lessThan name="viewReportInInfo" property="checkFlag" value="2">
								<%i++;%>
								<input type="checkbox" name="chk<%=i%>" id="chk<%=i%>" value="<bean:write name="viewReportInInfo" property="repInId"/>:<bean:write name="viewReportInInfo" property="orgId"/>">
								</logic:lessThan>
								</logic:notEmpty>	
								</td>
								 -->
									<TD align="center">
										<bean:write name="viewReportInInfo" property="childRepId"/>
									</TD>
									<TD align="center">
									<logic:notEmpty name='viewReportInInfo' property='repInId'>	
									<a href="javascript:viewPdf('<bean:write name='viewReportInInfo' property='repInId'/>',
									'<bean:write name='viewReportInInfo' property='childRepId'/>',
									'<bean:write name='viewReportInInfo' property='versionId'/>',
									'<bean:write name='viewReportInInfo' property='curId'/>',
									'<bean:write name='viewReportInInfo' property='actuFreqID'/>',
									'<bean:write name='viewReportInInfo' property='year'/>',
									'<bean:write name='viewReportInInfo' property='term'/>',
									'<bean:write name='viewReportInInfo' property='day'/>',
									'<bean:write name='viewReportInInfo' property='orgId'/>')">			
										<bean:write name="viewReportInInfo" property="repName" /></a>
									</logic:notEmpty>
									<logic:empty name='viewReportInInfo' property='repInId'>	
										<bean:write name="viewReportInInfo" property="repName" />
									</logic:empty>	
									</TD>
									<!-- 
									<TD align="center">
										<bean:write name="viewReportInInfo" property="versionId" />
									</TD>
									 -->
									<TD align="center">
										<bean:write name="viewReportInInfo" property="currName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="actuFreqName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="year" />-<bean:write name="viewReportInInfo" property="term" />-<bean:write name="viewReportInInfo" property="day" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="orgName" />
									</TD>
									<TD align="center">
											<logic:present name="viewReportInInfo" property="isFlag">
												<logic:equal name="viewReportInInfo" property="isFlag" value = "-1">
													是
												</logic:equal>
												<logic:equal name="viewReportInInfo" property="isFlag" value = "0">
													是
												</logic:equal>
												<logic:equal name="viewReportInInfo" property="isFlag" value = "1">
													是
												</logic:equal>
												<logic:equal name="viewReportInInfo" property="isFlag" value = "5">
													否
												</logic:equal>
											</logic:present>
									</TD>
									<TD align="center">
									<logic:empty name="viewReportInInfo" property="checkFlag">未上传</logic:empty>
									<logic:notEmpty name="viewReportInInfo" property="checkFlag">
										<logic:equal name="viewReportInInfo" property="checkFlag" value="0"><font face="宋体" color="#000000" size="2">未审签</font></logic:equal>
										<logic:equal name="viewReportInInfo" property="checkFlag" value="1"><font size="2" color="#33CC33">审签通过</font></logic:equal>
										<logic:equal name="viewReportInInfo" property="checkFlag" value="-1"><font face="宋体" color="#FF0000" size="2">审签未通过</font></logic:equal>
										<logic:equal name="viewReportInInfo" property="checkFlag" value="-5"><font face="宋体" color="#FF0000" size="2">审签未通过</font></logic:equal>
										<logic:greaterThan  name="viewReportInInfo" property="checkFlag" value="1">未上传</logic:greaterThan>
									</logic:notEmpty>
									</TD>
								</TR>
							</logic:iterate>
						</logic:present>
						<logic:notPresent name="Records" scope="request">
							<tr align="left">
								<td bgcolor="#ffffff" colspan="10">
									暂无符合条件的记录
								</td>
							</tr>
						</logic:notPresent>
					</table>
				</td>
			</tr>
			<input type="hidden" name="countChk" value="<%=i%>">
		</html:form>
	</table>
	<table cellSpacing="0" cellPadding="0" width="98%" border="0">
		<TR>
			<TD>
				<jsp:include page="../../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../../reportSearch/viewReportStatNX.do" />
				</jsp:include>
			</TD>
		</TR>
	</table>
</body>
</html:html>
