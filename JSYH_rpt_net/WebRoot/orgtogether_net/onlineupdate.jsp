<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<%@ page import="com.cbrc.smis.common.Config"%>
<html:html locale="true">
	<head>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../css/common.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="../../js/func.js"></script>
		<script language="javascript">
			
		</script>
	</head>
	<script language="javascript">
		<logic:present name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="<bean:write name='ApartPage' property='curPage'/>";
	    </logic:present>
	    <logic:notPresent name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="1";
	    </logic:notPresent>	
	    
		function viewPdf(repInId){
			window.location="<%=request.getContextPath()%>/servlets/toExcelServlet?repInId=" + repInId; 
		}
		function _view_zxxg(repInId){
			var term = document.getElementById("HiddenTerm");
			var year = document.getElementById("HiddenYear");
			
		    window.location="<%=request.getContextPath()%>/reportEdit.do?" + "repInId=" + repInId + "&curPage=" 
		    		+ curPage + "&term=" + term.value + "&year=" + year.value ;
		}	
		function _view_sjbs(repInId){
		    window.location="<%=request.getContextPath()%>/upLoadOnLineReport.do?" + "repInId=" + repInId;
		}
		function _view_jyxx(repInId){
		    window.open("<%=request.getContextPath()%>/report/runDataJY.do?" + "repInId=" + repInId);
		}	
	</script>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<body style="TEXT-ALIGN: center">
			<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 >> 数据报送 >> 数据调整
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="4" border="0" width="98%" align="center">
		<html:form action="/viewonlineupdate" method="post" styleId="frm">
			<tr>
				<td>
					<fieldset id="fieldset">
						<table cellspacing="0" cellpadding="4" border="0" width="100%" align="left">
							<tr>
								<td height="3"></td>
							</tr>
							<tr>
								<td>
									报表名称：
									<html:text property="repName" size="25" styleClass="input-text" />
								</td>
								<td>
									报表时间：
									<html:text property="year" maxlength="4" size="6" styleClass="input-text" />
									年
									<html:text property="term" maxlength="2" size="4" styleClass="input-text" />
									月
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
	<TABLE cellSpacing="0" width="98%" border="0" align="center" cellpadding="4">
		<TR>
			<TD>
				<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0"  class="tbcolor">
					<TR class="titletab">
						<th width="8%" align="center" valign="middle">报表编号</th>
						<th width="28%" align="center" valign="middle">报表名称</th>
						<th width="8%" align="center" valign="middle">版本号</th>
						<th width="10%" align="center" valign="middle">报表口径</th>
						<th width="10%" align="center" valign="middle">币种</th>
						<th width="8%" align="center" valign="middle">频度</th>
						<th width="10%" align="center" valign="middle">报表时间</th>
						<Th width="8%" align="center" valign="middle">报表状态</Th>
						<Th width="10%" align="center" valign="middle">操作</Th>
					</tr>
					<logic:present name="records" scope="request">
						<logic:iterate id="viewReportIn" name="records">
							<TR bgcolor="#FFFFFF">
								<TD align="center">
									<bean:write name="viewReportIn" property="childRepId" />
								</TD>
								<TD align="center">
									<a href="javascript:viewPdf(<bean:write name='viewReportIn' property='repInId'/>)">
										<bean:write name="viewReportIn" property="repName" />
										<input type="hidden" id="reportName" name="reportName" value='<bean:write name="viewReportIn" property="repName" />'>
									</a>
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="versionId" />
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="dataRgTypeName" />
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="currName" />
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="actuFreqName" />
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="year"/>-<bean:write name="viewReportIn" property="term"/>
									<input type="hidden" id="HiddenYear" name="HiddenYear" value='<bean:write name="viewReportIn" property="year" />'>
									<input type="hidden" id="HiddenTerm" name="HiddenTerm" value='<bean:write name="viewReportIn" property="term" />'>
								</TD>
								<TD align="center">
								   <logic:equal name="viewReportIn" property="isCollected" value="1"><font color="red">已调整</font></logic:equal>
								   <logic:equal name="viewReportIn" property="isCollected" value="0">未调整</logic:equal>
								</TD>								
								<TD align="center">
									<img src="../image/zxxg.gif" border="0" title="手工调平" style="cursor:hand" onclick="_view_zxxg(<bean:write name='viewReportIn' property='repInId'/>)">									
									<a href ="javascript:_view_jyxx(<bean:write name='viewReportIn' property='repInId'/>)">查看校验</a>									
								</TD>
							</TR>
						</logic:iterate>
					</logic:present>
					
					<logic:notPresent name="records" scope="request">
						<tr align="center">
							<td bgcolor="#ffffff" colspan="9" align="left">
								暂无符合条件的记录
							</td>
						</tr>
					</logic:notPresent>
				</TABLE>
			</TD>
		</TR>
	</TABLE>	
	<table cellSpacing="0" cellPadding="0" width="98%" border="0">
		<TR>
			<TD>
				<jsp:include page="../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../viewonlineupdate.do" />
				</jsp:include>
			</TD>
		</TR>
	</table>			
	</body>
</html:html>
