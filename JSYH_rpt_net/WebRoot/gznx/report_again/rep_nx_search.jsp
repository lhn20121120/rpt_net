<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.security.Operator,com.cbrc.smis.common.Config"%>
<%@ page import="com.cbrc.smis.other.Expression"%>

<%
	/** 报表选中标志 **/
	String reportFlg = "0";
	
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
	}
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepVerifyPodedom = operator != null ? operator.getChildRepCheckPodedom()
	 		+" and viewOrgRep.childRepId in (select tmpl.id.templateId from AfTemplate tmpl where tmpl.templateType=" + reportFlg +")" : "";
	
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

%>
<jsp:useBean id="utilSubOrgForm" scope="page" class="com.fitech.net.form.UtilSubOrgForm" />
<jsp:setProperty property="childRepVerifyPodedom" name="utilSubOrgForm" value="<%=childRepVerifyPodedom%>"/>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm"/>
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="orgPodedom" name="FormBean" value="<%=childRepVerifyPodedom%>"/>
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
	
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>
	<SCRIPT language="javascript">
		<logic:present name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
		   	var curPage="<bean:write name='ApartPage' property='curPage'/>";
		   	var term="<bean:write name='ApartPage' property='term'  filter="false"/>";
		</logic:present>
		<logic:notPresent name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
			var curPage="1";
			var term="";
		</logic:notPresent>
		/**
		 * 审核通过标识
		 */
		var FLAG_OK=1;
		/**
		 * 审核不通过标识
		 */ 
		var FLAG_NO=-1;
		/**
		 * 表内校验标识
		 */
		var FLAG_BL="<%=Expression.FLAG_BL%>";
		/**
		 * 表间校验标识
		 */
		var FLAG_BJ="<%=Expression.FLAG_BJ%>";
		/**
		 * 逗号分隔符
		 */
		var SPLIT_SYMBOL_COMMA="<%=Config.SPLIT_SYMBOL_COMMA%>";     
		    		  	 
		/**
	     * 新增重报事件
		 */
		function _add(repInId){
			var objFrm=document.forms['frm'];
				
			var qry="allFlags=1" +
					"&repId=" + repInId + 					
					"&templateId=" + objFrm.elements['templateId'].value + 
					"&repName=" + objFrm.elements['repName'].value + 
		  			"&bak1=" + objFrm.elements['bak1'].value + 
		  	  		"&orgId=" + objFrm.elements['orgId'].value + 
		  	  		"&repFreqId=" + objFrm.elements['repFreqId'].value + 		  	  			
					"&curPage=" + curPage;
			
			if(objFrm.elements['date'].value != "")
				qry += "&date=" + objFrm.elements['date'].value;

			location.href="<%=request.getContextPath()%>/report/reportAgainInitNX.do?" + qry;
		}
		
		/**
		 * 全选操作
		 *
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
		 * 批量重报
		 *
		 */
		function _batchSet(){
			var objForm=document.getElementById("frmChk");
			var count=objForm.elements['countChk'].value;
		  	  	
			var repInIds="";
			var obj=null;
			for(var i=1;i<=count;i++){
				try{
					obj=eval("objForm.elements['chk" + i + "']");
					if(obj.checked==true){
						repInIds+=(repInIds==""?"":SPLIT_SYMBOL_COMMA) + obj.value;
					}
				}catch(e){}
			}	

			if(repInIds==""){
				alert("请选择要设定重报的报表!\n");
				return;
			}else{
				var objFrm=document.forms['frm'];
				var allFlags="";
		  	  		
				var qry="repInIds=" + repInIds + 
						"&templateId=" + objFrm.elements['templateId'].value + 
						"&repName=" + objFrm.elements['repName'].value + 
		  				"&bak1=" + objFrm.elements['bak1'].value + 
						"&orgId=" + objFrm.elements['orgId'].value + 
						"&repFreqId=" + objFrm.elements['repFreqId'].value + 
						"&curPage=" + curPage;

				if(objFrm.elements['date'].value != "")
					qry += "&date=" + objFrm.elements['date'].value;
		  	  		
				window.location="<%=request.getContextPath()%>/gznx/report_again/reportAgainBatch.jsp?" + qry;
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
				当前位置 &gt;&gt; 报表处理 &gt;&gt; 重报管理 &gt;&gt; 报表查询
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="4" border="0" width="98%" align="center">
		<html:form action="/report/repNXSearch" method="post" styleId="frm">
		<html:hidden property="bak1"/>
		<html:hidden property="orgId"/>
			<tr>
				<td>
					<fieldset id="fieldset">
						<table cellspacing="0" cellpadding="4" border="0" width="100%" align="center">
							<tr>
								<td height="5"></td>
							</tr>
							<tr>
								<td height="25">&nbsp;
									报表编号：
									<html:text property="templateId" maxlength="6" size="6" styleClass="input-text"/>
								</td>
								
								<td height="25" align="left">
									报表名称：
									<html:text property="repName" size="23" styleClass="input-text" />
									
								</td>
								<td height="25" align="left">
									报送频度：
									<html:select property="repFreqId">
										<html:option value="-999">--全部--</html:option>
										<html:optionsCollection name="utilForm" property="repFreqs" label="label" value="value" />
									</html:select>
									
								</td>
							</tr>																	
							<tr>
								<td height="25" align="left">&nbsp;
									报表时间：
									<html:text property="date" styleClass="input-text" size="10" styleId="date1"
											readonly="true" onclick="return showCalendar('date1', 'y-mm-dd');" />
									<img border="0" src="<%=basePath%>image/calendar.gif"
											onclick="return showCalendar('date1', 'y-mm-dd');">
								</td>
								<!-- 
								<td height="25" align="left">
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
								<td height="25" align="left">
									报表机构：
									<html:text property="orgName" readonly="true" size="23" style="width:150px;cursor:hand" onclick="return showTree1()" style="input-text" ></html:text>
									<div id="orgpreTree" style="left:316px;top:70px;width:150px; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
									<script type="text/javascript">
										<bean:write  name="FormBean"  property="orgReportPodedomTree" filter="false"/>
									    var tree1= new ListTree("tree1", TREE2_NODES,DEF_TREE_FORMAT,"","treeOnClick1('#KEY#','#CAPTION#');");
								      	tree1.init();
								 	</script>
								 	</div>
									
								</td>
								
								<td><html:submit styleClass="input-button" value=" 查 询 " /></td>
							</tr>							
						</table>
					</fieldset>
				</td>
			</tr>
			<input type="hidden" name="allFlags" value="1">
		</html:form>
	</table>
				 
	<logic:present name="form" scope="request">
		<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
			<html:form action="/dateQuary/saveCheckRepNX" method="post" styleId="frm1">
				<tr>
					<td>
						<input type="button" value="批量重报" class="input-button" onclick="_batchSet()">
					</td>
				</tr>				
			</html:form>
		</table>
	</logic:present>
	
	<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
		<html:form action="/dateQuary/saveCheckRepNX" method="post" styleId="frmChk">
			<tr>
				<td>
					<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
						<TR class="titletab">
							<th width="4%" align="center" valign="middle">
								<input type="checkbox" name="chkAll" onclick="_selAll()">
							</th>
							<th width="6%" align="center" valign="middle">
								报表编号
							</th>
							<th width="29%" align="center" valign="middle">
								报表名称
							</th>
							<%-- 
							<th width="12%" align="center" valign="middle">
								报表口径
							</th>
							--%>
							<th width="10%" align="center" valign="middle">
								币种
							</th>
							<th width="8%" align="center" valign="middle">
								报送频度
							</th>
							<th width="10%" align="center" valign="middle">
								报表时间
							</th>
							<th width="15%" align="center" valign="middle">
								报送机构
							</th>							
							<th width="6%" align="center" valign="middle">
								重报设定
							</th>							
						</tr>
						<%int i = 0;%>
						<logic:present name="form" scope="request">
							<logic:iterate id="viewReportIn" name="form">
								<%i++;%>
								<TR bgcolor="#FFFFFF">
									<td align="center">
										<input type="checkbox" name="chk<%=i%>" value="<bean:write name="viewReportIn" property="repInId"/>">
									</td>
									<TD align="center">
										<bean:write name="viewReportIn" property="childRepId" />
									</TD>
									<TD align="center">
<%-- 										<a href="<%=request.getContextPath()%>/servlets/toExcelServlet?repInId=<bean:write name='viewReportIn' property='repInId'/>" target="_blank"></a>--%>
											<bean:write name="viewReportIn" property="repName" />
									</TD>
									<%-- 
									<TD align="center">
										<bean:write name="viewReportIn" property="dataRgTypeName" />
									</TD>
									 --%>
									<TD align="center">
										<bean:write name="viewReportIn" property="currName"/>
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="actuFreqName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="year" />-<bean:write name="viewReportIn" property="term" />-<bean:write name="viewReportIn" property="day" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="orgName" />
									</TD>									
									<TD align="center">
										<a href="javascript:_add(<bean:write name='viewReportIn' property='repInId'/>)">重报</a>
									</td>
								</TR>
							</logic:iterate>
						</logic:present>
						<input type="hidden" name="countChk" value="<%=i%>">
						<logic:notPresent name="form" scope="request">
							<tr align="center">
								<td bgcolor="#ffffff" colspan="10" align="left">
									暂无符合条件的记录
								</td>
							</tr>
						</logic:notPresent>
					</table>
				</td>
			</tr>
		</html:form>
	</table>
	<table cellSpacing="0" cellPadding="0" width="98%" border="0">
		<TR>
			<TD>
				<jsp:include page="../../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../../report/repNXSearch.do" />
				</jsp:include>
			</TD>
		</TR>
	</table>
</body>
</html:html>
