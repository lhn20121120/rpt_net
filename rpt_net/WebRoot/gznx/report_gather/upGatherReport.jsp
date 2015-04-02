<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
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

	<SCRIPT language="javascript">  
	    var EXT_EXCEL="<%=configBean.EXT_EXCEL%>";	    	
	    var EXT_ZIP="<%=configBean.EXT_ZIP%>";
	    var requestParam = "<logic:present name='RequestParam'><bean:write name='RequestParam'/></logic:present>";	  


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
		
		//返回
		function back()
		{
			window.location.href= "<%=request.getContextPath()%>/viewGatherReport.do?"+requestParam;
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
				当前位置 &gt;&gt; 数据报送 &gt;&gt; 数据采集上报
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
					<html:form method="post" action="/report/uploadGatherFile" enctype="multipart/form-data" onsubmit="return _submit2(this)">
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
