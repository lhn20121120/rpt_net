<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="com.cbrc.smis.security.Operator,com.cbrc.smis.common.Config"%>

<%
	/** 报表选中标志 **/
	String reportFlg = "0";
	
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
	}
	
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepVerifyPodedom = operator != null ? operator.getChildRepCheckPodedom()
	 		+" and viewOrgRep.childRepId in (select tmpl.id.templateId from AfTemplate tmpl where tmpl.templateType='" + reportFlg +"')" : "";
	
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
		<html:base/>		
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="<%=request.getContextPath()%>/css/common.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="<%=request.getContextPath()%>/js/func.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/tree/tree.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/tree/defTreeFormat.js"></script>
		<script language="javascript" src="<%=request.getContextPath()%>/js/func.js"></script>
		<script language="javascript">
			/**
			 * 新增重报事件
			 */
			 function _add(){
				location.href="<%=request.getContextPath()%>/report/repNXSearch.do";
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
	<jsp:include page="../../calendar.jsp" flush="true">
	  <jsp:param name="path" value="../../"/>
	</jsp:include>
	<body topmargin="0" marginheight="0" leftmargin="0" marginwidth="0">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
		<html:form action="/selReportAgainNX" method="post">
		<html:hidden property="bak1"/>
		<html:hidden property="orgId"/>
		<table cellspacing="0" cellpadding="0" border="0" width="98%">
			<tr>
				<td height="5"></td>
			</tr>
			<tr>
				 <td>当前位置 &gt;&gt; 报表处理 &gt;&gt; 报表重报</td>
			</tr>
			<tr>
				<td height="5"></td>
			</tr>
			<tr>
				<td>
					<fieldset id="fieldset">
						<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
							<tr>
								<td height="5"></td>
							</tr>
							<tr>
								<td height="25">&nbsp;
									报表编号：
									<html:text property="templateId" maxlength="10" size="10" styleClass="input-text"/>
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
							<tr><td height="2"></td></tr>											
							<tr>
								<td height="25" align="left">&nbsp;
									报表时间：
									<html:text property="date" value="<%=((String) session.getAttribute(Config.USER_LOGIN_DATE)) %>" styleClass="input-text" size="10" styleId="date1"
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
									    var tree= new ListTree("orgTree", TREE1_NODES,DEF_TREE_FORMAT,"","treeOnClick('#KEY#','#CAPTION#');");
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
									    var tree1= new ListTree("orgpreTree", TREE2_NODES,DEF_TREE_FORMAT,"","treeOnClick1('#KEY#','#CAPTION#');");
								      	tree1.init();
								 	</script>
								 	</div>	
									
								</td>
								<td height="25" align="left">
									报表批次：
									<html:select property="supplementFlag" styleId="supplementFlag">
										<html:option value="-999">全部</html:option>
										<html:option value="1">第一批</html:option>
										<html:option value="0">第零批&nbsp;&nbsp;</html:option>
										<html:option value="2">第二批</html:option>
									</html:select>
							   </td>	
								
								<td align="left">
									<input type="submit" class="input-button" value=" 查 询 ">&nbsp;
									<input type="button" class="input-button" value="新增重报" onclick="_add()">
								</td>
							</tr>
							<tr>
								<td height="3"></td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>
			
		</table>
		</html:form>
		<table cellSpacing="1" cellPadding="4" width="98%" border="0"  class="tbcolor">
			<tr>
				<th width="6%" align="center">编号</th>
				<th width="22%" align="center">报表名称</th>
				<%-- <th width="6%" align="center">版本号</th>--%>
				<th width="10%" align="center">报送机构</th>
				<%--<th width="10%" align="center">报表口径</th>--%>
				<th width="8%" align="center">币种</th>
				<th width="6%" align="center">报送频度</th>
				<th width="9%" align="center">报表时间</th>				
				<th width="9%" align="center">重报设定时间</th>
				<th width="13%" align="center">重报原因</th>
			</tr>
			<logic:present name="Records" scope="request">
				<logic:iterate id="item" name="Records">
			    <TR bgColor="#FFFFFF" onmouseover="changeToLavender(this)" onmouseout="changeToWhite(this)">
			      <TD  align="center">
			      	<bean:write name="item" property="childRepId"/>
			      </TD>
				  <TD  align="center">
					<bean:write name="item" property="repName"/>
				  </TD>
				  <%--
				  <TD bgcolor="#ffffff" align="center">
			      	<bean:write name="item" property="versionId"/>
			      </TD>
			      --%>
			      <TD  align="center">
					<bean:write name="item" property="orgName"/>
				  </TD>
				  <%--
				  <TD bgcolor="#ffffff" align="center">
					<bean:write name="item" property="dataRangeDes"/>
				  </TD>
				  --%>
			      <TD  align="center">
			      	<bean:write name="item" property="currName"/>
			      </TD>
			      <TD  align="center">
			      	<bean:write name="item" property="actuFreqName"/>
			      </TD>
				  <TD  align="center">
			      	<bean:write name="item" property="reportDate"/>
			      </TD>
				  <TD  align="center"> 
				  	<bean:write name="item" property="setAgainDate" format="yyyy-MM-dd"/>
				  </TD>
				  <TD  align="center">
				  	<bean:write name="item" property="cause"/>
				  </TD>
				</TR>
			</logic:iterate>
		</logic:present>
		<logic:notPresent name="Records" scope="request">
			<tr align="left">
				<td bgcolor="#ffffff" colspan="10">
						无匹配记录
				</td>
			</tr>
		</logic:notPresent>
		</table>
		
		<TABLE  cellSpacing="0" cellPadding="0" width="98%" border="0">
			<TR>
				<TD>
					<jsp:include page="../../apartpage.jsp" flush="true">
				  		<jsp:param name="url" value="../../selReportAgainNX.do"/>
				  	</jsp:include>
				</TD>
			</tr>
		</TABLE>
		</body>
</html:html>