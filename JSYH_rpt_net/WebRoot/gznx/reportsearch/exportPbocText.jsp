<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ page import="com.cbrc.smis.other.Aditing"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
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
	String orgName = "";
	if(request.getAttribute("orgName")!=null && !request.getAttribute("orgName").toString().equals(""))
		orgName = request.getAttribute("orgName").toString();
	else
		orgName = operator != null ? operator.getOrgName() : "";
	String orgId = operator != null ? operator.getOrgId() : "";
	Map selectCheckOrgIds=(HashMap)request.getAttribute("selectCheckOrgIds");
	
%>
<jsp:useBean id="utilSubOrgForm" scope="page"
	class="com.fitech.net.form.UtilSubOrgForm" />
<jsp:setProperty property="childRepSearchPodedom" name="utilSubOrgForm"
	value="<%=childRepSearchPodedom%>" />
<jsp:useBean id="configBean" scope="page"
	class="com.cbrc.smis.common.Config" />
<jsp:useBean id="utilForm" scope="page"
	class="com.cbrc.smis.form.UtilForm" />
<jsp:useBean id="FormBean" scope="page"
	class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="reportFlg" name="FormBean"
	value="<%=reportFlg%>" />
<jsp:setProperty property="orgPodedom" name="FormBean" value="<%=childRepSearchPodedom%>"/>

	
<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="<%=request.getContextPath()%>/css/common.css"
		type="text/css" rel="stylesheet">
	<script language="javascript"
		src="<%=request.getContextPath()%>/js/func.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/js/tree/tree.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/js/tree/defTreeFormat.js"></script>
	<script language="javascript"
		src="<%=request.getContextPath()%>/js/func.js"></script>
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>
	<logic:present name="Message" scope="request">
		<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
	</logic:present>
	<script language="javascript">	
		var scriptReportFlg=<%=reportFlg%>;
		var SPLIT_SYMBOL_COMMA="<%=Config.SPLIT_SYMBOL_COMMA%>";
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
		 * 复选导出报表 
		 * @return void
		 */
		function _selExport(){
			var objForm=document.getElementById("frmChk");
			var count=objForm.elements['countChk'].value;

			//新版标志位
			var versionFlg = 0;
			//if(document.getElementById("versionFlg").checked==true){
			//	versionFlg = 1;
			//}
			<%
			if(request.getAttribute("Records")!=null &&((List)request.getAttribute("Records")).size()>0){
					
		    %>
			var repInIds="";
			var obj=null;
			for(var i=1;i<=count;i++){
				try{
					obj=eval("objForm.elements['chk" + i + "']");
					if(obj.checked==true){					
						repInIds+=(repInIds==""?"":",")+obj.value;
					}
				}catch(e){}
			}	
			if(repInIds==""){
				alert("请选择要导出的报表!\n");
				return;
			}
			if(confirm("是否录入说明?")){
					window.location="<%=request.getContextPath()%>/gznx/reportsearch/pbocexplane.jsp?repInIds=" +repInIds
						+"&orgId="+objForm.orgId.value
						+"&date="+objForm.date.value
						+"&repFreqId="+objForm.repFreqId.value
						+"&supplementFlag="+objForm.supplementFlag.value
						+"&versionFlg="+versionFlg
						+"&styleFlg=new";
			}else{
					window.location="<%=request.getContextPath()%>/exportPbocAFReport.do?repInIds=" +repInIds
						+"&orgId="+objForm.orgId.value
						+"&date="+objForm.date.value
						+"&repFreqId="+objForm.repFreqId.value
						+"&supplementFlag="+objForm.supplementFlag.value
						+"&versionFlg="+versionFlg;
					LockWindows.style.display="";
					hidd();
			}
			<%
		    }else{
			%>
				alert("暂无记录，无法下载");
			<%
				}
			%>
		}
			
		function _downLoadSingle(repInIds){
			var objForm=document.getElementById("frmChk");

			//新版标志位
			var versionFlg = 0;
			//if(document.getElementById("versionFlg").checked==true){
			//	versionFlg = 1;
			//}
			var repInId=repInIds;
			<%
			if(request.getAttribute("Records")!=null &&((List)request.getAttribute("Records")).size()>0){
					
		    %>
		    
			if(confirm("是否录入说明?")){
					window.location="<%=request.getContextPath()%>/gznx/reportsearch/pbocexplane.jsp?repInIds=" +repInId
						+"&orgId="+objForm.orgId.value
						+"&date="+objForm.date.value
						+"&repFreqId="+objForm.repFreqId.value
						+"&supplementFlag="+objForm.supplementFlag.value
						+"&versionFlg="+versionFlg;
						
			}else{
					window.location="<%=request.getContextPath()%>/exportPbocAFReport.do?repInIds=" +repInId
						+"&orgId="+objForm.orgId.value
						+"&date="+objForm.date.value
						+"&repFreqId="+objForm.repFreqId.value
						+"&supplementFlag="+objForm.supplementFlag.value
						+"&versionFlg="+versionFlg;				
			}
			<%
		    }else{
			%>
				alert("暂无记录，无法下载");
			<%
				}
			%>
		}
		
		
		function _downLoadAll(){
			var objForm=document.getElementById("frmChk");
			//新版标志位
			<%
				if(request.getAttribute("Records")!=null &&((List)request.getAttribute("Records")).size()>0){
						
			%>
			var versionFlg = 0;
			if(confirm("全部下载时需等待，确定要全部下载吗？")){
				if(confirm("是否录入说明?")){
					window.location="<%=request.getContextPath()%>/gznx/reportsearch/pbocexplane.jsp?orgId="+objForm.orgId.value
						+"&date="+objForm.date.value
						+"&repFreqId="+objForm.repFreqId.value
						+"&supplementFlag="+objForm.supplementFlag.value
						+"&versionFlg="+versionFlg;
				}else{
					window.location="<%=request.getContextPath()%>/exportPbocAFReport.do?orgId="+objForm.orgId.value
						+"&date="+objForm.date.value
						+"&repFreqId="+objForm.repFreqId.value
						+"&supplementFlag="+objForm.supplementFlag.value
						+"&versionFlg="+versionFlg;
					LockWindows.style.display="";
					hidd();
				}
			}else{
				disp();
			}
			<%
			    }else{
				%>
					alert("暂无记录，无法下载");
				<%
					}
				%>
		}
		function hidd(){

			checkTab.style.display="none";
		
		}
		
		function disp(){
			checkTab.style.display="";

		}



		function _returnOld(){

				window.location="<%=request.getContextPath()%>/exportRhAFReport.do";
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
		
	</script>
</head>
<body style="TEXT-ALIGN: center">

	<div id="LockWindows" style="display:none" class="black_overlay">
	<br><br><br><font color="red">操作执行中，请稍后……</font><br><br>
	<img src="../../image/loading.gif">
	</div>

	<table border="0" width="98%" align="center">
		<tr>
			<td height="3"></td>
		</tr>
		<tr>
			<td>
				当前位置 &gt;&gt; 报表处理 &gt;&gt; 报表报送
			</td>
		</tr>
	</table>
	<html:form action="/exportRhAFReport" method="POST" styleId="frmChk">
	<html:hidden property="styleFlg" value="new"/>	
	<html:hidden property="orgId"/>
	<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
		<tr><td>
		<fieldset id="fieldset">
		<table cellSpacing="0" cellPadding="4" width="100%" border="0"
			align="center" id="checkTab">
			<tr><td height="5"></td></tr>
			<tr>
				<td width="30%">
					&nbsp;报表时间：
					<html:text property="date" readonly="true" size="10"
						styleId="date" style="text" onclick="return showCalendar('date', 'y-mm-dd');"/>
					<img src="../../image/calendar.gif" border="0"
						onclick="return showCalendar('date', 'y-mm-dd');">
				</td>
				<td width="25%">
					报表编号：
					<html:text property="templateId" styleId="templateId" size="10" />
				</td>
				<td width="35%">
					报表名称：
					<html:text property="repName" styleId="repName" size="20" />
				</td>
				<td width="10%">
				</td>
			</tr>
			<tr>
				<td height="2"></td>
			</tr>
			<tr>
				<td height="25" align="left">
					&nbsp;报送机构：
					<html:text property="orgName" readonly="true" size="23" style="width:150px;cursor:hand" value="<%=orgName %>" onclick="return showTree1()" style="input-text" ></html:text>
					<div id="orgpreTree" style="left:316px;top:70px;width:150px; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
					<script type="text/javascript">
									<bean:write  name="FormBean"  property="orgReportPodedomTree" filter="false"/>
								    var tree1= new ListTree("orgpreTree", TREE2_NODES,DEF_TREE_FORMAT,"","treeOnClick1('#KEY#','#CAPTION#');");
							      	tree1.init();
					</script>
				 	</div>
				</td>
				<td height="25" align="left">
					报送频度：
					<html:select property="repFreqId" styleId="repFreqId">
						<html:option value="6">日</html:option>
						<html:option value="1">月</html:option>
						<html:option value="2">季</html:option>
						<html:option value="4">年</html:option>
						<html:option value="7">周(人行)</html:option>
						<html:option value="8">旬(人行)</html:option>
						<html:option value="10">快(人行)</html:option>
						<html:option value="91">年初结转（月）</html:option>
						<html:option value="92">年初结转（季）</html:option>
						<html:option value="93">年初结转（年）</html:option>
					</html:select>
				</td>
				<td height="25" align="left">
					报表批次：
					<html:select property="supplementFlag" styleId="supplementFlag">
						<html:option value="0">第零批</html:option>
						<html:option value="1">第一批</html:option>
						<html:option value="2">第二批</html:option>
						<html:option value="-999">全部</html:option>
					</html:select>
				</td>
				<td align="left">
					<html:submit styleClass="input-button" value=" 查 询 " />
				</td>
				
			</tr>
		</table>
		</fieldset>
		</td></tr>
		</table>
		<br/>
		<table cellSpacing="0" cellPadding="4" width="98%" border="0"
			align="center">
			<tr>
				<td>
					<input type="button" value="批量下载" class="input-button"
						onclick="_selExport()">
					&nbsp;&nbsp;&nbsp;
					<input type="button" value="全部下载" class="input-button"
						onclick="_downLoadAll()">
					&nbsp;&nbsp;&nbsp;
					<!-- <input type="checkbox" id="versionFlg"><font color="red">旧版格式</font> -->
				</td>
				<td align="center">
					<input type="button" value="旧    版" class="input-button" onclick="_returnOld()">
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
							<th colspan="10" align="center" id="list">
								<strong>报表列表</strong>
							</th>
						</tr>
						<TR class="middle">
							<td width="5%" align="center" valign="middle">
								<input type="checkbox" name="chkAll" onclick="_selAll()">
							</td>
							<TD class="tableHeader" align="left">
								报表编号
							</TD>
							<TD class="tableHeader" align="left">
								报表名称
							</TD>
							<TD class="tableHeader" align="left">
								报送机构
							</TD>
						</TR>
						<%
							int i = 0;
						%>
						<logic:present name="Records" scope="request">
							<logic:iterate id="item" name="Records">
								<TR bgcolor="#FFFFFF">
								
									<TD align="center">
										<%
											i++;
										%>
										<input type="checkbox" name="chk<%=i%>"
											value='<bean:write name="item" property="orgId"/>:<bean:write name="item" property="repInId"/>:<bean:write name="item" property="actuFreqID"/>:<bean:write name="item" property="templateId"/>:<bean:write name='item' property='versionId'/>:<bean:write name='item' property='batchId'/>'>
									</TD>
									<td align="center" width="8%" >
										<bean:write name="item" property="templateId" />
									</td>
									<td align="left">
										&nbsp;(<bean:write name="item" property="actuFreqName" />)&nbsp;<bean:write name="item" property="repName" />
									</td>
									<td align="left">
										<bean:write name="item" property="orgName" />
									</td>
								</TR>
							</logic:iterate>
						</logic:present>
						<input type="hidden" name="countChk" value="<%=i%>">
						<logic:notPresent name="Records" scope="request">
							<tr bgcolor="#FFFFFF">
								<td colspan="10">
									暂无记录
								</td>
							</tr>
						</logic:notPresent>
					</TABLE>
					
					<table cellSpacing="1" cellPadding="0" width="100%" border="0">
						<tr>
							<TD colspan="8" bgcolor="#FFFFFF">
							<jsp:include page="../../apartpage.jsp" flush="true"><jsp:param name="url" value="../../exportRhAFReport.do" />
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
