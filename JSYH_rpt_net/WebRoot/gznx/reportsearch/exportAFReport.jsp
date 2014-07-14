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
	String orgId = operator != null ? operator.getOrgId() : "";
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
	<script language="javascript" src="<%=Config.WEBROOTULR%>/js/prototype-1.4.0.js"></script>
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

			//旧版标志位
			var versionFlg = 0;
			if(document.getElementById("versionFlg").checked==true){
				versionFlg = 1;
			}
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
						+"&versionFlg="+versionFlg;
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

			//旧版标志位
			var versionFlg = 0;
			if(document.getElementById("versionFlg").checked==true){
				versionFlg = 1;
			}
			<%
				if(request.getAttribute("Records")!=null &&((List)request.getAttribute("Records")).size()>0){
						
			%>
			var repInId=repInIds;
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
				//旧版标志位
			var versionFlg = 0;
			if(document.getElementById("versionFlg").checked==true){
				versionFlg = 1;
			}
			<%
				if(request.getAttribute("Records")!=null &&((List)request.getAttribute("Records")).size()>0){
						
			%>
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
		
			//机构校验
		function org_validate_Select(){
		 	var count = document.getElementById("countChk").value; 
			<%
			if(request.getAttribute("Records")!=null &&((List)request.getAttribute("Records")).size()>0){
					
		    %>
			var checkObj = null;
			var repInIds = "";
			for(var i=1;i<=count;i++){
				try{
					checkObj = eval("document.getElementById('chk" + i + "')");
					if(checkObj.checked == true)
						repInIds+=(repInIds==""?"":",") + checkObj.value.split(":")[1];
				}catch(e){}				
			} 
			if(repInIds==""){
		  	  	alert("请选择要批量校验的报表!\n");
		  	  	return;
			}else{		  	  		
		  	  	if(confirm("您确定要进行批量校验吗?\n")==true){
					
						var validateURL = "<%=request.getContextPath()%>/report/validateOrgReport.do?repInIds="+repInIds; 
						var param = "radom="+Math.random(); 
				try{
						new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:validateOrgHandler,onFailure: reportError}); 
					}catch(e){
						alert('系统忙，请稍后再试...！');
						return;
				}  	  	  				
				}
			}
			<%
		    }else{
			%>
				alert("暂无记录，无法校验");
			<%
				}
			%>
		}
		
		//机构校验Handler
		function validateOrgHandler(request){ 
		var checkObj = null;
		var count = document.getElementById("countChk").value;
		var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;
		var repInIds = ""; 
		for(var i=1;i<=count;i++){
				try{
					checkObj = eval("document.getElementById('chk" + i + "')");
					if(checkObj.checked == true)
						repInIds+=(repInIds==""?"":",") +  checkObj.value.split(":")[1];
				}catch(e){}	
		} 
		
		
		if(result == 'true'){
				alert('校验通过！');	   	     
			}else{
					 if(confirm('校验未通过！是否需要查看校验信息?')){
				        window.open("<%=request.getContextPath()%>/report/viewOrgValidateInfo.do?" + "repInIds=" + repInIds,'校验结果','scrollbars=yes,height=600,width=500,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
				      }
			} 
			 
	    }
	    
	    //失败处理
	    function reportError(request){
	        alert('系统忙，请稍后再试...！X');
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
	<html:form action="/exportRhAFReport.do" method="POST" styleId="frmChk">
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
					<html:select styleId="orgId" property="orgId">							
						<html:optionsCollection name="utilSubOrgForm" property="rhOrgId" />
						<html:option value="'-999'">全部</html:option>
					</html:select>
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
					<input type="checkbox" id="versionFlg"><font color="red">旧版格式</font>
					<!-- <input class="input-button" onclick="org_validate_Select()" type="button" value="总分校验">
					&nbsp;&nbsp;&nbsp;-->
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
