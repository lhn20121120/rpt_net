<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.form.UtilMDataRGTypeForm" %>
<%@ page import="com.cbrc.smis.form.MDataRgTypeForm" %>
<%@ page import="com.cbrc.smis.hibernate.MChildReport" %>
<%@ page import="com.cbrc.smis.hibernate.MDataRgType" %>
<%@ page import="com.cbrc.smis.hibernate.MRepRange" %>
<%@ page import="com.cbrc.smis.hibernate.MActuRep" %>
<%@ page import="com.cbrc.smis.hibernate.ReportIn"%>
<%@ page import="com.cbrc.smis.form.ReportInForm"%>
<%@ page import="java.util.*" %>

<jsp:useBean id="utilOrgForm" scope="page" class="com.cbrc.smis.form.UtilOrgForm" />

<html:html locale="true">
<head>
	<html:base />
	<title>机构报送情况统计</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../../js/func.js"></script>
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>
</head>
<%
	List rList = null;
	ReportInForm reportInForm = null;	
	if(request.getAttribute("dataRecords") != null){
		rList = (List)request.getAttribute("dataRecords");
	}
	if(request.getAttribute("form") != null){
		reportInForm = (ReportInForm)request.getAttribute("form");
	}
	reportInForm.getOrgName();
	reportInForm.getYear();
	reportInForm.getTerm();
	List dataRanges = UtilMDataRGTypeForm.getInstance().getDataRanges();
	if(dataRanges == null ) dataRanges = new ArrayList();
	int[] dataRangeIDs = null;
	int[] numCount = null;
%>
<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table border="0" width="96%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 >> 信息查询 >> 报送统计
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	
	<TABLE cellSpacing="0" width="98%" border="0" align="center" cellpadding="4">
		<TR>
			<TD>
				<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0"  class="tbcolor">
					<tr class="titletab">
						<th colspan="<%=dataRanges.size()*2+4%>" align="center" id="list">
							<font size="3"><strong>
								<%=reportInForm.getYear()%>年<%=reportInForm.getTerm()%>月<%=reportInForm.getOrgName()%>报送情况统计</strong>
							</font>
						</th>
					</tr>
					<tr class="tableHeader">
						<td colspan="4" align="center"><strong>报表编号、名称、版本号、币种</strong></td>
						<td colspan="<%=dataRanges.size()*2%>" align="center"><strong>报送口径</strong></td>
					</tr>
					<TR class="middle">
						<TD width="5%" rowspan="2" align="center"><strong>编号</strong></TD>
						<TD rowspan="2" align="center"><strong>报表名称</strong></TD>
						<TD width="5%" rowspan="2" align="center"><strong>版本号</strong></TD>
						<TD width="5%" rowspan="2" align="center"><strong>币种</strong></TD>
						<%
							if(dataRanges != null && dataRanges.size() > 0){
								dataRangeIDs = new int[dataRanges.size()];
								numCount = new int[dataRanges.size()*2];
								for(int i=0;i<dataRanges.size();i++){
									MDataRgTypeForm mdrt = (MDataRgTypeForm)dataRanges.get(i);
									dataRangeIDs[i] = mdrt.getDataRangeId().intValue();		
									%><TD width="10%" colspan="2" class="middle" align="center"><strong><%=mdrt.getDataRgDesc()%></strong></TD><%
								}
							}
						%>
					</TR>
					<TR class="middle">
					<%
						if(dataRanges != null && dataRanges.size() >0){
							for(int i=0;i<dataRanges.size();i++){
								%>
								<TD align="center"><strong>应报</strong></TD>
								<TD align="center"><strong>实际</strong></TD>
								<%
							}
						}
					%>
					</TR>
					<%
						if(rList != null && rList.size() > 0){
							for(Iterator iter=rList.iterator();iter.hasNext();){
								MRepRange mRepRange = (MRepRange)iter.next();
								MChildReport mChildReport = mRepRange.getMChildReport();
								Set actuRepSet = mChildReport.getMActuReps();
								Set reportInSet = mRepRange.getReportIns();
								
								%>
								<TR bgcolor="#FFFFFF">
									<TD align="center" width="5%"><%=mChildReport.getComp_id().getChildRepId()%></TD>
									<TD align="center"><%=mChildReport.getReportName()%></TD>
									<TD align="center" width="5%"><%=mChildReport.getComp_id().getVersionId()%></TD>
									<TD align="center" width="5%"><%=mChildReport.getTemplateType()%></TD>
								<%
								
								
								for(int i=0;i<dataRangeIDs.length;i++){
									boolean bool_actu = false;
									for(Iterator iter1=actuRepSet.iterator();iter1.hasNext();){
										MActuRep mActuRep = (MActuRep)iter1.next();
										int data_Range_ID = mActuRep.getMDataRgType().getDataRangeId().intValue();
										if(data_Range_ID == dataRangeIDs[i]){
											bool_actu = true;
											numCount[i*2]++;
											break;
										}
									}
									boolean bool_report = false;
									for(Iterator iter2=reportInSet.iterator();iter2.hasNext();){
										ReportIn reportIn = (ReportIn)iter2.next();
										int data_Range_ID = reportIn.getMDataRgType().getDataRangeId().intValue();
										if(data_Range_ID == dataRangeIDs[i]){
											bool_report = true;
											numCount[i*2+1]++;
											break;
										}
									}
									if(bool_actu){
									%><TD align="center">1</TD><%
									}else{
									%><TD align="center" bgcolor="#CCCCCC">&nbsp;</TD><%
									}
									if(bool_report){
									%><TD align="center">1</TD><%
									}else{
									%><TD align="center" bgcolor="#CCCCCC">&nbsp;</TD><%
									}
								}
								%></TR><%
							}
							%>
							<TR bgcolor="#FFFFFF">
								<TD colspan="4" align="center"><strong><font color="red">合         计：</font></strong></TD>
								<%
									if(dataRanges != null && dataRanges.size() > 0){
										for(int i=0;i<dataRanges.size()*2;i++){
											if(numCount[i] != 0){
											%><TD align="center"><strong><font color="red"><%=numCount[i]%></font></strong></TD><%
											}else{
											%><TD align="center" bgcolor="#CCCCCC">&nbsp;</TD><%
											}																								
										}
									}
								%>
							</TR>
							<%
						}else{
						%><tr bgcolor="#FFFFFF"><td colspan="<%=dataRanges.size()*2+4%>">无匹配记录</td></tr><%
						}
					%>
				</TABLE>
			</TD>
		</TR>	
	</TABLE>
</body>
</html:html>
