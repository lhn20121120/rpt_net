<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.List,com.cbrc.smis.form.ReportInInfoForm,com.cbrc.smis.other.Expression"%>
<jsp:useBean id="utilFormOrg" scope="page" class="com.fitech.net.common.UtilForm" />
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<%
String selectedFlag = request.getAttribute("SelectedFlag") == null ? "5" : (String) request.getAttribute("SelectedFlag");
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
	    
	    var isOnLine = "<%=com.cbrc.smis.system.cb.InputData.isOnLine%>";
	    var messageInfo = "<%=com.cbrc.smis.system.cb.InputData.messageInfo%>";
	    
	    if(isOnLine != null && isOnLine == "true"){
	    	alert(messageInfo);
	    }
	    	/**
		     * 表内校验标识
		     */
		     var FLAG_BL="<%=Expression.FLAG_BL%>";
		     
		    /**
		     * 表间校验标识
		     */
		     var FLAG_BJ="<%=Expression.FLAG_BJ%>";
		     		    
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
		  	 	if(form.term.value!=''&&(form.term.value <1 || form.term.value >12)){
						alert("请输入正确的报表时间！");
						form.term.focus();
						return false;
				}
		  	 	return true;
		  	 }
		  	 function viewPdf(repInId){
				 window.location="<%=request.getContextPath()%>/servlets/toExcelServlet?repInId=" + repInId; 
			 }
			 function why(why){
		  	 	window.open ("<%=request.getContextPath()%>/template/data_report/tip.jsp?reason="+why,"", "height=250, width=250, top=0,left=0,toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");		  	 	  	 		  	 	
		  	 }
		  	 function _view_zxxg(repInId,year,term){
		     	window.location="<%=request.getContextPath()%>/failedReportOnLineEdit.do?" + "repInId=" + repInId + "&curPage=" + curPage + "&year=" + year + "&term=" + term;
		     }
		     function _view_XSYY(repInId){
		     	window.open("<%=request.getContextPath()%>/report/viewJYNotPassInfo.do?" + "repInId=" + repInId);
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
	<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 >> 信息查询 >> 查看本级报表数据
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center">
		<html:form action="/reportSearch/searchMyRep" method="post" styleId="frm" onsubmit="return _submit(this)">
			<tr>
				<td>
					<fieldset id="fieldset">
						<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
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
								</td>
								<td>
									<html:submit styleClass="input-button" value=" 查 询 " />
								</td>
							</tr>
						</table>
						<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center">
							<tr>
								<td align="left">
									<input type="radio" name="allFlags" value="0" />
									未审核
									<strong> <font face="宋体" color="#000000" size="2"> ○ </font> </strong> &nbsp;
									<input type="radio" name="allFlags" value="-1" />
									审核未通过
									<strong> <font face="宋体" color="#FF0000" size="2"> × </font> </strong> &nbsp;
									<input type="radio" name="allFlags" value="1" />
									审核通过
									<strong> <font size="2" color="#33CC33"> √ </font> </strong> &nbsp;
									<input type="radio" name="allFlags" value="4" />
									异常记录
									<font color="#FF0000" style="font-size: 14pt"> <strong> ? </strong> </font> &nbsp;
									<input type="radio" name="allFlags" value="5" />
									全部记录
								</td>
								<td>
								</td>
							</tr>
							<tr>
								<td height="5"></td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>
		</html:form>
	</table>
	<br />
	<script language="javascript">
		var _selFlag="<%=selectedFlag%>";
		var _selIndex=4;
		if(_selFlag=="0") _selIndex=0;
		if(_selFlag=="-1") _selIndex=1;
		if(_selFlag=="1") _selIndex=2;
		if(_selFlag=="4") _selIndex=3;
		
		document.forms['frm'].elements['allFlags'][_selIndex].checked=true;
	</script>

	<table border="0" cellpadding="0" cellspacing="0" width="98%" align="center">
		<html:form action="/reportSearch/viewReport" method="post">
			<tr>
				<td>
					<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
						<TR class="titletab">
							<th width="8%" align="center" valign="middle">
								报表编号
							</th>
							<th width="30%" align="center" valign="middle">
								报表名称
							</th>
							<th width="8%" align="center" valign="middle">
								版本号
							</th>
							<th width="10%" align="center" valign="middle">
								报表口径
							</th>
							<th width="10%" align="center" valign="middle">
								币种
							</th>
							<th width="5%" align="center" valign="middle">
								频度
							</th>
							<th width="9%" align="center" valign="middle">
								报表时间
							</th>

							<Th width="5%" align="center" valign="middle">
								状态
							</Th>
						</TR>

						<logic:present name="Records" scope="request">
							<logic:iterate id="viewReportInInfo" name="Records">
								<TR bgcolor="#FFFFFF">
									<TD align="center">
										<bean:write name="viewReportInInfo" property="childRepId" />
									</TD>
									<TD align="center">
										<input type="hidden" name="repInIdArray" value="<bean:write name="viewReportInInfo" property="repInId"/>" />
										<a href="javascript:viewPdf(<bean:write name='viewReportInInfo' property='repInId'/>)"> <bean:write name="viewReportInInfo" property="repName" /> </a>
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="versionId" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="dataRgTypeName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="currName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="actuFreqName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="year" />
										-
										<bean:write name="viewReportInInfo" property="term" />
									</TD>
									<TD align="center">
										<logic:equal name="viewReportInInfo" property="checkFlag" value="0">
											<font face="宋体" color="#000000" size="2">○</font>
										</logic:equal>
										<logic:equal name="viewReportInInfo" property="checkFlag" value="1">
											<strong><font size="2" color="#33CC33">√</font> </strong>
										</logic:equal>
										<logic:equal name="viewReportInInfo" property="checkFlag" value="-1">
											<logic:notEqual name="viewReportInInfo" property="tblInnerValidateFlag" value="-1">
												<a href="javascript:why('<bean:write name="viewReportInInfo" property="why"/>')" title='<bean:write name="viewReportInInfo" property="why"/>'> <strong><font
														face="宋体" color="#FF0000" size="2">×</font> </strong> </a>
											</logic:notEqual>
											<logic:equal name="viewReportInInfo" property="tblInnerValidateFlag" value="-1">
												<a href="javascript:_view_XSYY(<bean:write name='viewReportInInfo' property='repInId'/>)" title='<bean:write name="viewReportInInfo" property="why"/>'> <strong><font
														face="宋体" color="#FF0000" size="2">×</font> </strong> </a>
											</logic:equal>
											<!-- <img src="../../image/zxxg.gif" border="0" title="在线修改" style="cursor:hand" onclick="_view_zxxg(<bean:write name='viewReportInInfo' property='repInId'/>,<bean:write name="viewReportInInfo" property="year" />,<bean:write name="viewReportInInfo" property="term" />)"-->
										</logic:equal>
										<logic:equal name="viewReportInInfo" property="checkFlag" value="4"><strong><font face="宋体" color="#FF0000" size="2">？</font></strong></logic:equal>
							
									</TD>
								</TR>
							</logic:iterate>
						</logic:present>
						<logic:notPresent name="Records" scope="request">
							<tr align="left">
								<td bgcolor="#ffffff" colspan="8">
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
					<jsp:param name="url" value="../../reportSearch/searchMyRep.do" />
				</jsp:include>
			</TD>
		</TR>
	</table>
</body>
</html:html>
