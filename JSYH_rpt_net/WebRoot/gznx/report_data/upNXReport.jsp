<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	Operator operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepReportPodedom = operator != null ? operator.getChildRepReportPopedom() : "";
	String orgId = operator != null ? operator.getOrgId() : "";
	String selectOrgId = request.getAttribute("orgId") != null ? request.getAttribute("orgId").toString() : orgId;
	String curpage =(String) request.getAttribute("curPage") != null ? request.getAttribute("curPage").toString() : "1";
	
	//String backQry = request.getAttribute("backQry").toString();
	//System.out.println(request.getAttribute("RequestParam").toString());

%>
<jsp:useBean id="utilSubOrgForm" scope="page" class="com.fitech.net.form.UtilSubOrgForm" /> 
<jsp:setProperty property="childRepReportPodedom" name="utilSubOrgForm" value="<%=childRepReportPodedom%>" />
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
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
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>
	<style type="text/css">
		#t1 td{
			height: 25px;
			border-right:1 solid #cccccc;
			border-bottom:1 solid #cccccc;
		}
	</style>
	<SCRIPT language="javascript">  
	    var EXT_EXCEL="<%=configBean.EXT_EXCEL%>";	    	
	    var EXT_ZIP="<%=configBean.EXT_ZIP%>";
	    var requestParam = "<logic:present name='RequestParam'><bean:write name='RequestParam'/></logic:present>";	  
	    <%--
	    var reportInId = null;
		  	function _submit(form){
				if(form.date.value==""){
					alert("请输入报表时间!");
					form.date.focus();
					return false;
				}
			}
			--%>
			function why(why){
		  	 	window.open ("<%=request.getContextPath()%>/template/data_report/tip.jsp?reason="+why,why, "height=250, width=250, top=0,left=0,toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");		  	 	  	 		  	 	
		  	}
			function _sjbs(){
		     	window.location="<%=request.getContextPath()%>/template/intoTem.do?curPage=" + curPage + "&orgId=<%=selectOrgId%>";
		    }
		    function _SJJY(){
		     	window.location="<%=request.getContextPath()%>/report/dataLDJY.do?curPage=" + curPage + "&orgId=<%=selectOrgId%>";
		    }	
		    function _submit2(form){			
				if(form.formFile.value==""){
					alert("上传文件不能为空");
					form.formFile.focus();
					return false;
				}
				if(getExt(form.formFile.value)!=EXT_EXCEL){
			 		alert("选择的报送文件必须是Excel文件!");
			 		form.formFile.focus();
			 		return false;
			 	}
			    var childRepId ="<bean:write name='aditing' property='childRepId'/>";
			    var version_Id ="<bean:write name='aditing' property='versionId'/>";
			    var date ="<bean:write name='aditing' property='year'/>"+"-"+"<bean:write name='aditing' property='term'/>"+"-"+"<bean:write name='aditing' property='day'/>";
			    var curId ="<bean:write name='aditing' property='curId'/>";
			    var orgId ="<bean:write name='aditing' property='orgId'/>";
			<%--    var dataRangeId ="<bean:write name='aditing' property='dataRangeId'/>"; --%>
				var actuFreqID ="<bean:write name='aditing' property='actuFreqID'/>";
				var version_kj = document.getElementById("versionId");	
				var curPage = document.getElementById("curPage");	
				version_kj.value = childRepId + "_" + version_Id + "_" + date + "_" + curId + "_" + orgId + "_" + actuFreqID + "_"+requestParam;
		
				return true;
				
			}
			
		//校验报表
		function validateReport(repInId)
		{
			 try
		 	 {
			  	reportInId=repInId;
			  	var validateURL = "<%=request.getContextPath()%>/report/validateOnLineReportNX.do?repInId="+repInId; 
			    var param = "radom="+Math.random();
			   	new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:validateReportHandler,onFailure: reportError});
		   	}
		   	catch(e)
		   	{
		   		alert('系统忙，请稍后再试...！');
		   	}
		}
		
		//校验Handler
		function validateReportHandler(request)
		{
			try
			{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
				if(result == 'false')  
				  {
				     if(confirm('校验失败！是否需要查看校验信息?'))
				        window.open("<%=request.getContextPath()%>/report/viewValidateInfo.do?" + "repInId=" + reportInId,'校验结果','scrollbars=yes,height=600,width=500,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');

				  }	
				  else if(result == 'true')
				  {
					 alert('校验通过！');	
				  }else if(result == 'noData'){
					  alert("填报的单元格集合没有数据，请检查模板和excel");
				  }
			}
			catch(e)
			{}
	    }
	    
	    //失败处理
	    function reportError(request)
	    {
	        alert('系统忙，请稍后再试...！');
	    }
	    
	    //报表报送 
		function sendReport(repInId)
		{
			if(confirm('确定报送该报表？')){
			 	try
			 	 {
				  	reportInId=repInId;
				  	var upReportURL ="<%=request.getContextPath()%>/upLoadOnLineNXReport.do?" +requestParam+"&repInId=" + repInId ;
				    var param = "radom="+Math.random();
				   	new Ajax.Request(upReportURL,{method: 'post',parameters:param,onComplete:upReportHandler,onFailure: reportError});
			   	}
			   	catch(e)
			   	{
			   		alert('系统忙，请稍后再试...！');
			   	}
			}
		} 
		//报送Handler
		function upReportHandler(request)
		{
			try
			{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
					
				  if(result == 'true')
				  {
					 alert('报送成功！');	
					 window.location="<%=request.getContextPath()%>/viewNXDataReport.do?"+requestParam; 
				  }
				  else  if(result == 'BJ_VALIDATE_NOTPASS')
				  {
				     alert("表间校验不通过，不能上报该报表！");
				  }else if( result == 'BN_VALIDATE_NOTPASS'){
				  
				 	 alert("表内校验不通过，不能上报该报表！");
				  }
				  else{
				 	 alert('系统忙，请稍后再试...！');
				  
				  }
			}
			catch(e)
			{}
	    }
		 function viewPdf(repInId){
				 window.location="<%=request.getContextPath()%>/servlets/toExcelServlet?repInId=" + repInId; 
			 }
		
		//返回
		function back()
		{
			window.location.href= "<%=request.getContextPath()%>/viewNXDataReport.do?"+requestParam;
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
	<table border="0" width="90%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 &gt;&gt; 数据报送 &gt;&gt; 数据上报
			</td>
		</tr>
	</table>


	<table cellspacing="0" cellpadding="0" border="0" width="90%" align="center">
		<table cellSpacing="1" cellPadding="4" border="0" width="90%" class="tbcolor" align="center">
			<tr class="titletab">
				<th align="center" colspan="4">
					数据报送
				</th>
			</tr>

			<logic:present name="aditing" scope="request">
				<tr bgcolor="#FFFFFF" align="center">
					<td>
						报表名：
						<bean:write name="aditing" property="repName" />
					</td>
				</tr>
			</logic:present>

			<tr bgcolor="#FFFFFF">
				<td width="20%">
					<html:form method="post" action="/report/uploadFileNX" enctype="multipart/form-data" onsubmit="return _submit2(this)">
						<div align="center">
							<html:file property="formFile" size="80" styleClass="input-button" />
							<html:hidden property="versionId" />
							<html:hidden property="curPage" value="<%=curpage %>"/>
							<html:submit styleClass="input-button" value="载入" />
						</div>
					</html:form>
				</td>
			</tr>
		</table>
	</table>
	<p />
		<logic:present name="uploadErrors" scope="request">
		<table id="t1" cellspacing="0" cellpadding="0" border="0"
					width="60%" align="center" class="tbcolor" style="border:1 solid #cccccc;border-right:0px;border-right:0px;border-bottom:0px;">
					<TR class="middle" align="center" height="20" style="background-color:white;">
						<TD colspan="2">
							<b>错误的单元格</b>
						</TD>
					</TR>
					<TR class="middle" align="center" height="20">
						<TD width="25%" align="center">
							<b>单元格名称</b>
						</TD>
						<td width="70%" align="center">
							<b>单元格值</b>
						</td>
					</TR>
					
						<logic:iterate id="item" name="uploadErrors">
							<tr height='20' align='center' bgcolor='#FFFFFF'>
								<td><font color="red"><bean:write name="item" property="key"/></font></td>
								<td><font color="red"><bean:write name="item" property="value"/></font></td>
							</tr>
						</logic:iterate>
				</table>
				</logic:present>
		<br />
	<table border="0" cellpadding="0" cellspacing="0" width="90%" align="center">
		<tr>
			<td>
				<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
					<TR class="titletab">
						<th width="10%" align="center" valign="middle">
							编号
						</th>
						<th width="30%" align="center" valign="middle">
							报表名称
						</th>
						<!-- 
						<th width="10%" align="center" valign="middle">
							版本号
						</th>
						 -->
						<th width="10%" align="center" valign="middle">
							币种
						</th>
						<th width="5%" align="center" valign="middle">
							频度
						</th>
						<th width="10%" align="center" valign="middle">
							机构
						</th>						
						<th width="10%" align="center" valign="middle">
							报表时间
						</th>
						<Th width="15%" align="center" valign="middle">
							状态
						</Th>
					</TR>

					<logic:present name="aditing" scope="request">
						<TR bgcolor="#FFFFFF">

							<TD align="center">
								<bean:write name="aditing" property="childRepId" />
							</TD>
							<logic:present name="notshow" scope="request">
								<TD align="center">
									<bean:write name="aditing" property="repName" />
								</TD>
							</logic:present>
							<logic:notPresent name="notshow" scope="request">
								<TD align="center">
									<bean:write name="aditing" property="repName" />
								</TD>
							</logic:notPresent>
							<!-- 
							<TD align="center">
								<bean:write name="aditing" property="versionId" />
							</TD>
							 -->
							<TD align="center">
								<bean:write name="aditing" property="currName" />
							</TD>
							<TD align="center">
								<bean:write name="aditing" property="actuFreqName" />
							</TD>
							<TD align="center">
								<bean:write name="aditing" property="orgName" />
							</TD>
							<TD align="center">
								<bean:write name="aditing" property="year" />-<bean:write name="aditing" property="term" />-<bean:write name="aditing" property="day" />
							</TD>
					
										<%
											String flag = (String) request.getAttribute("flag");
											if (flag != null && flag.trim().equals("true"))
											{
										%>
										<TD align="center">
											未载入
										</TD>

										<%
											}
											else
											{
										%>
										<TD align="center">
											<logic:equal name="aditing" property="checkFlag" value="0">
												<span class="txt-main" style="color:#FF3300">载入失败</span>
											</logic:equal>
											<logic:equal name="aditing" property="checkFlag" value="3">载入成功</logic:equal>
										</TD>
										<%
										}
										%>
						</TR>

					</logic:present>
					<logic:notPresent name="aditing" scope="request">
						<tr align="left">
							<td bgcolor="#ffffff" colspan="8">
								暂无符合条件的记录
							</td>
						</tr>
					</logic:notPresent>
				</table>
			</td>
		</tr>

	</table>
	<table border="0" cellpadding="0" cellspacing="0" width="90%" align="center">
		<TR>
			<TD>
				&nbsp;
			</TD>
		</TR>
		<TR>
			<TD>
			
				<logic:present name="notshow" scope="request">
					<tr>
						<td colspan="4">
							<DIV align="right">
								<input class="input-button" type="reset" value="校  验" onclick="validateReport('<bean:write name="aditing" property="repInId"/>')" />
								&nbsp;
								<input class="input-button" type="reset" value="报  送" onclick="sendReport('<bean:write name="aditing" property="repInId"/>')" />
								&nbsp;
								<INPUT class="input-button" id="back" type="button" value=" 返  回 " name="butBack" onclick="back()">
							</DIV>
						</td>
					</tr>
				</logic:present>
				<logic:notPresent name="notshow" scope="request">
					<tr>
						<td colspan="4">
							<DIV align="right">
								<INPUT class="input-button" id="back" type="button" value=" 返  回 " name="butBack" onclick="back()">
			
							</DIV>
						</td>
					</tr>
				</logic:notPresent>

		</TD>
		</TR>
	</table>


</body>
<script>
	//block
	//   document.getElementById("isShowZT").style.display="none";
	</script>
</html:html>
