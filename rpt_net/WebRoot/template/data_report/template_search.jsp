<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.List,com.cbrc.smis.form.ReportInInfoForm,com.cbrc.smis.other.Expression"%>
<jsp:useBean id="utilFormOrg" scope="page" class="com.fitech.net.common.UtilForm" />
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<%String selectedFlag = request.getAttribute("SelectedFlag") == null ? "5"
					: (String) request.getAttribute("SelectedFlag");
%>
<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../../js/func.js"></script>
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
		  	 * 表间提交验证
		  	 */
		  	 function _submit(form){
		  	 	if(!isEmpty(form.year.value) && !CheckNum(form.year.value)){
		  	 		alert("请输入正确的报表的查询年份!\n");
		  	 		form.year.select();
		  	 		form.year.focus();
		  	 		return false;
		  	 	}
		  	 	if(!isEmpty(form.term.value) && !CheckNum(form.term.value)){
		  	 		alert("请输入正确的报表的查询月份!\n");
		  	 		form.term.select();
		  	 		form.term.focus();
		  	 		return false;
		  	 	}
		  	 	return true;
		  	 }	
		  	 
		  	 function why(why)
		  	 {		  	 	
		  	 	window.open ("data_report/why.html", why, "height=100, width=100, top=0,left=0,toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no") 			  	 	  	 		  	 	
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
				当前位置 >> 数据上报 >> 信息查询
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="0" border="0" width="90%" align="center">
		<html:form action="/template/searchTem.do" method="post" styleId="frm" onsubmit="return _submit(this)">
			<tr>
				<td>
					<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
						<tr>							
							<td>
								报表名称：
								<html:text property="repName" size="25" styleClass="input-text" />
							</td>							
							<td>
								<html:submit styleClass="input-button" value=" 查 询 " />
							</td>
						</tr>
					</table>
					<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
						<tr>														
							<td>
								报表时间：
								<html:text property="year" maxlength="4" size="4" styleClass="input-text" />
								年
								<html:text property="term" maxlength="2" size="2" styleClass="input-text" />
								月
							</td>							
						</tr>
						<tr>
							<td height="5"></td>
						</tr>
					</table>
				</td>
			</tr>
		</html:form>
	</table>	

	<table border="0" cellpadding="0" cellspacing="0" width="90%" align="center">
		<html:form action="/reportSearch/viewReport" method="post">
			<tr>
				<td>
					<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
						<TR class="titletab">
							<th width="43%" align="center" valign="middle">
								报表名称
							</th>
							<th width="9%" align="center" valign="middle">
								版本号
							</th>
							<th width="13%" align="center" valign="middle">
								报表口径
							</th>
							<th width="5%" align="center" valign="middle">
								频度
							</th>
							<th width="15%" align="center" valign="middle">
								报表时间
							</th>							
							<Th width="15%" align="center" valign="middle">
								状态
							</Th>
							<!-- 
							<th width="15%" align="center" valign="middle">
								详细信息
							</th>
							-->
						</TR>

						<logic:present name="Records" scope="request">
							<logic:iterate id="viewReportInInfo" name="Records">
								<TR bgcolor="#FFFFFF">
									<TD align="center">
										<input type="hidden" name="repInIdArray" value="<bean:write name="viewReportInInfo" property="repInId"/>" />
										
											<bean:write name="viewReportInInfo" property="repName" />
										
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="versionId" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="dataRgTypeName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="actuFreqName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="year" />-<bean:write name="viewReportInInfo" property="term" />
									</TD>									
									<TD align="center">
										<logic:equal name="viewReportInInfo" property="checkFlag" value="0">已报</logic:equal>
										<logic:equal name="viewReportInInfo" property="checkFlag" value="1">全格</logic:equal>
										<logic:equal name="viewReportInInfo" property="checkFlag" value="-1"><a href="javascript:why('<bean:write name="viewReportInInfo" property="why"/>')">不合格</a></logic:equal>
									</TD>									
								</TR>
							</logic:iterate>
						</logic:present>
						<logic:notPresent name="Records" scope="request">
							<tr align="left">
								<td bgcolor="#ffffff" colspan="6">
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
					<jsp:param name="url" value="../../template/searchTem.do" />
				</jsp:include>
			</TD>
		</TR>
	</table>
</body>
</html:html>
