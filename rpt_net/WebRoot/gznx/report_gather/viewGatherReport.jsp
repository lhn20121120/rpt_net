<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	/** 报表选中标志 **/
	String reportFlg = "3";
	
//	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
//		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
//	}
	
	Operator operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepReportPodedom = operator != null ? operator.getChildRepReportPopedom()
		+" and viewOrgRep.childRepId in (select tmpl.id.templateId from AfTemplate tmpl where tmpl.templateType='" + reportFlg +"')" : "";
	String orgId = operator != null ? operator.getOrgId() : "";
	String selectOrgId = orgId;
	
	String curpage = request.getAttribute("curPage") !=null?(String)request.getAttribute("curPage"):"";
	
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
			
	String date = request.getAttribute("date").toString();
%>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm"/>
<jsp:useBean id="utilSubOrgForm" scope="page" class="com.fitech.net.form.UtilSubOrgForm" />
<jsp:setProperty property="childRepReportPodedom" name="utilSubOrgForm" value="<%=childRepReportPodedom%>" />
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
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript" src="<%=Config.WEBROOTULR%>/js/prototype-1.4.0.js"></script>
	<link href="../../css/calendar-blue.css" type="text/css" rel="stylesheet">	
	<script language="javascript" src="../../js/func.js"></script>
<script type="text/javascript" src="../../js/tree/tree.js"></script>
	<script type="text/javascript" src="../../js/tree/defTreeFormat.js"></script>

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

 		  var _year ;	
		  var _term ;		
	    
	    	var EXT_EXCEL="<%=configBean.EXT_EXCEL%>";	    	
	    	var EXT_ZIP="<%=configBean.EXT_ZIP%>";
	    	
	    	var isOnLine = "<%=com.cbrc.smis.system.cb.InputData.isOnLine%>";
	    	var messageInfo = "<%=com.cbrc.smis.system.cb.InputData.messageInfo%>";
	    
	    	if(isOnLine != null && isOnLine == "true"){
	    		alert(messageInfo);
	    	}
	    		    	    	
		  	function _submit(form){
				if(form.date.value==""){
					alert("请输入报表时间!");
					form.date.focus();
					return false;
				}

			}
			
		    //离线报送
		    function _view_LSBS(childRepId,versionId,year,term,day,curId,actuFreqID,orgId){
		    
			    var repNames = document.getElementById("repName").value;
			    var orgIds = document.getElementById("orgId").value;
			    var repFreqIds = document.getElementById("repFreqId").value;
			    
		    	 window.location="<%=request.getContextPath()%>/offLineGatherReport.do?" + 
		    	 "childRepId=" + childRepId + 
		    	 "&versionId=" + versionId + 
		    	 "&year=" + year+ "&term=" + term+ "&day=" + day +
		    	 "&curId=" + curId + 
		    	 "&curPage=" + curPage + 
		    	 "&flag=" + true + 
		    	 "&actuFreqID=" + actuFreqID + 
		    	 "&orgId=" + orgId + 
		    	 "&backQry=<%=date%>_"+repNames+"_"+orgIds+"_"+repFreqIds;		    
		    }
		    
		    function viewPdf(repInId,year,term,day,orgId){
		
			var repNames = document.getElementById("repName").value;
			    var orgIds = document.getElementById("orgId").value;
			    var repFreqIds = document.getElementById("repFreqId").value;

			window.location = "<%=request.getContextPath()%>/editFXAFReport.do?statusFlg=2&repInId=" + repInId
							+ "&year="+year
							+ "&term="+term
							+ "&day="+day
							+ "&orgId="+orgId
					    	+  "&backQry=<%=date%>_"+repNames+"_"+orgIds+"_"+repFreqIds+"_"+curPage;	    
			; 
		}
		function editExcel(templateId,versionId,curId,repFreqId,year,term,day,orgId){		
				var repNames = document.getElementById("repName").value;
			    var orgIds = document.getElementById("orgId").value;
			    var repFreqIds = document.getElementById("repFreqId").value;
				window.location = "<%=request.getContextPath()%>/editFXAFReport.do?statusFlg=3&templateId=" + templateId+
									"&versionId="+versionId+
									"&curId="+curId+
									"&repFreqId="+repFreqId+
									"&year="+year+
									"&term="+term+
									"&day="+day+
									"&orgId="+orgId+
									 "&backQry=<%=date%>_"+repNames+"_"+orgIds+"_"+repFreqIds+"_"+curPage;	    
		}

  			var reportInId = null;

		    //失败处理
		    function reportError(request){
		        alert('系统忙，请稍后再试...！X');
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
	<label id="prodress" style="display:none">
			<span class="txt-main" style="color:#FF3300">正在校验报表,请稍后......</span>
		</label>
  	<label id="prodress1" >
	<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 &gt;&gt; 数据采集 &gt;&gt; 数据采集
			</td>
		</tr>
	</table>
	<br>
	<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center">
		<html:form action="/viewGatherReport.do" method="post" styleId="frm" onsubmit="return _submit(this)">
		<html:hidden property="orgId"/>
			<tr>
				<td>
				<fieldset id="fieldset">
				<br/>
					<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
						<tr>
							<td height="25">				
							&nbsp;报表时间：
								<html:text property="date" styleClass="input-text" size="10" styleId="date1"
										readonly="true" onclick="return showCalendar('date1', 'y-mm-dd');" />
								<img border="0" src="<%=basePath%>image/calendar.gif"
										onclick="return showCalendar('date1', 'y-mm-dd');">
							</td>
							<td height="25">
								报表名称：
								<html:text property="repName" size="25" styleClass="input-text" />
							</td>
						</tr>
						<tr>
							<td height="25" align="left">
								&nbsp;报表机构：
							<html:text property="orgName" readonly="true" size="10" style="width:150px;cursor:hand" onclick="return showTree1()" styleClass="input-text" ></html:text>
									<div id="orgpreTree" style="left:316px;top:70px;width:150px; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
									<script type="text/javascript">
										<bean:write  name="FormBean"  property="orgReportPodedomTree" filter="false"/>
									    var tree1= new ListTree("tree1", TREE2_NODES,DEF_TREE_FORMAT,"","treeOnClick1('#KEY#','#CAPTION#');");
								      	tree1.init();
								 	</script>
								 	</div>
							</td>
							<td height="25" align="left">
								报送频度：
								<html:select property="repFreqId">
									<html:option value="-999">--全部--</html:option>
									<html:optionsCollection name="utilForm" property="repFreqs" label="label" value="value" />
								</html:select>
							</td>
							<td align="right">
								<html:submit styleClass="input-button" value=" 查 询 " />&nbsp;&nbsp;&nbsp;&nbsp;
								<!-- <input class="input-button" onclick="_view_PLBS()" type="button" value="批量报送">&nbsp;&nbsp;	 -->
							</td>
						</tr>
					</table>
				</fieldset>
				</td>
			</tr>
			<tr>
				<td height="4"></td>
			</tr>
		</html:form>
	</table>
	<br/>	
	<table border="0" cellpadding="0" cellspacing="0" width="98%" align="center">
		<tr>
			<td>
				<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
					<tr class="titletab">
						<th align="center" colspan="8">
							数据报送
						</th>
					</tr>
					<TR class="middle">
						<td align="center" valign="middle"  width="8%">
							编号
						</td>
						<td align="center" valign="middle"  width="38%">
							报表名称
						</td>
						<!-- 
						<td align="center" valign="middle" width="5%" NOWRAP>
							版本号
						</td>
						 -->
						<td align="center" valign="middle" width="12%">
							机构
						</td>
						<td align="center" valign="middle" width="9%">
							币种
						</td>
						<td align="center" valign="middle" width="8%" NOWRAP>
							频度
						</td>
						<td align="center" valign="middle" width="10%">
							报表时间
						</td>
						<Td align="center" valign="middle" width="15%">
							状态(操作)
						</Td>

					</TR>

					<logic:present name="Records" scope="request">
						<logic:iterate id="viewReportIn" name="Records">							
							<TR bgcolor="#FFFFFF"  onmouseover="changeToLavender(this)" onmouseout="changeToWhite(this)">
								<TD align="center">
									<bean:write name="viewReportIn" property="childRepId" />
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="repName" />
								</TD>
								<!-- 
								<TD align="center">
									<bean:write name="viewReportIn" property="versionId" />
								</TD>
								 -->
								<TD align="center">
									<bean:write name="viewReportIn" property="orgName" />
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="currName" />
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="actuFreqName" />
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="year" />-<bean:write name="viewReportIn" property="term" />-<bean:write name="viewReportIn" property="day" />
								</TD>
								<TD align="center" colspan="2">

									<logic:equal name="viewReportIn" property="checkFlag" value="3">									
										<font color="#0000FF">无数据</font>&nbsp;
										<logic:notEqual name="viewReportIn" property="reportStyle" value="2">
 										<input class="input-button" onclick="editExcel('<bean:write name="viewReportIn" property="childRepId"/>',
							   					'<bean:write name="viewReportIn" property="versionId"/>',
							   					'<bean:write name='viewReportIn' property='curId' />',
							   					'<bean:write name='viewReportIn' property='actuFreqID' />',
							   					'<bean:write name="viewReportIn" property="year" />',
							   					'<bean:write name="viewReportIn" property="term" />',
							   					'<bean:write name='viewReportIn' property='day' />',
							   					'<bean:write name='viewReportIn' property='orgId'/>')" type="button" value="在线"> 
										</logic:notEqual>
										<input class="input-button" onclick="_view_LSBS('<bean:write name="viewReportIn" property="childRepId"/>',
											'<bean:write name="viewReportIn" property="versionId"/>',
											'<bean:write name="viewReportIn" property="year" />',
											'<bean:write name="viewReportIn" property="term" />',
											'<bean:write name='viewReportIn' property='day' />',
											'<bean:write name='viewReportIn' property='curId' />',
											'<bean:write name='viewReportIn' property='actuFreqID' />',
	 									   	'<bean:write name='viewReportIn' property='orgId'/>')" type="button" value="载入">
									
									</logic:equal>
									<!-- 未校验 -->
									<logic:notEqual name="viewReportIn" property="checkFlag" value="3">										
										<font color="#FF0000">有数据</font>&nbsp;
										<logic:notEqual name="viewReportIn" property="reportStyle" value="2">
										<input class="input-button" onclick="viewPdf('<bean:write name="viewReportIn" property="repInId"/>',
											'<bean:write name="viewReportIn" property="year" />',
											'<bean:write name="viewReportIn" property="term" />',
											'<bean:write name='viewReportIn' property='day' />',
											'<bean:write name='viewReportIn' property='orgId'/>')" type="button" value="修改">
									 	</logic:notEqual>
										<input class="input-button" onclick="_view_LSBS('<bean:write name="viewReportIn" property="childRepId"/>',
											'<bean:write name="viewReportIn" property="versionId"/>',
											'<bean:write name="viewReportIn" property="year" />',
											'<bean:write name="viewReportIn" property="term" />',
											'<bean:write name='viewReportIn' property='day' />',
											'<bean:write name='viewReportIn' property='curId' />',
											'<bean:write name='viewReportIn' property='actuFreqID' />',
	 									   	'<bean:write name='viewReportIn' property='orgId'/>')" type="button" value="载入">
											
									</logic:notEqual>

								</TD>
							</TR>
						</logic:iterate>
					</logic:present>
					<logic:notPresent name="Records" scope="request">
						<tr align="left">
							<td bgcolor="#ffffff" colspan="11">
								暂无符合条件的记录
							</td>
						</tr>
					</logic:notPresent>
				</table>
			</td>
		</tr>
	</table>
	<table cellSpacing="0" cellPadding="0" width="100%" border="0">
		<TR>
			<TD>
				<jsp:include page="../../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../../viewGatherReport.do" />
				</jsp:include>
			</TD>
		</TR>
	</table>
	</label>
</body>
</html:html>
