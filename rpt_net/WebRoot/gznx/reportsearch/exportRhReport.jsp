<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config" %>
<%@ page import="com.cbrc.smis.other.Aditing"%>
<%@ page import="com.cbrc.smis.security.Operator" %>
<%@ page import="java.util.List"%>

<%
	Operator operator = (Operator) session
			.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepSearchPodedom = operator != null ? operator
			.getChildRepSearchPopedom() : "";

	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	/** 报表选中标志 **/
	String reportFlg = "0";
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null) {
		reportFlg = (String) session
				.getAttribute(Config.REPORT_SESSION_FLG);
	}
	String orgId = operator != null ? operator.getOrgId() : "";
%>
<jsp:useBean id="utilSubOrgForm" scope="page" class="com.fitech.net.form.UtilSubOrgForm" />
<jsp:setProperty property="childRepSearchPodedom" name="utilSubOrgForm" value="<%=childRepSearchPodedom%>"/>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm"/>
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="reportFlg" name="FormBean" value="<%=reportFlg%>"/>


<html:html locale="true">
	<head>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="<%=request.getContextPath()%>/css/common.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="<%=request.getContextPath()%>/js/func.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/tree/tree.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/tree/defTreeFormat.js"></script>
		<script language="javascript" src="<%=request.getContextPath()%>/js/func.js"></script>
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

		function rhExport(){
			var form=document.getElementById("frmChk");
			var repInIds="";
			var obj=null;
			if(form.date.value==""){
				alert("请输入报表时间!");
				form.date.focus();
			}else if(form.orgId.value==""){
				alert("请选择机构!");
				form.orgId.focus();
			}else if(form.batchId.value==""){
				alert("请选择报表批次!");
				form.batchId.focus();
			}else if(form.repName.value==""){
				alert("请选择要导出的报表!");
				form.repName.focus();
			}else {		
				try{
					var repInIds="";
					var nameList = document.getElementById('repName');
					var idLeft = nameList.selectedIndex;
					 while(idLeft>-1){
					 	repInIds+=(repInIds==""?"":",")+form.orgId.value+":"+nameList.options[idLeft].value+":"+form.batchId.value;
					 	nameList.options[idLeft].selected = false;
					 	idLeft=nameList.selectedIndex;
					} 								
				}catch(e){}
						
				if(confirm("是否录入说明?")){
					window.location="<%=request.getContextPath()%>/gznx/reportsearch/pbocexplane.jsp?repInIds=" + repInIds+"&orgId="+form.orgId.value+"&date="+form.date.value;
				}else{
					window.location="<%=request.getContextPath()%>/exportPbocAFReport.do?repInIds=" + repInIds+"&orgId="+form.orgId.value+"&date="+form.date.value;
				}			
			}
			}		
	</script>
	</head>
	<body style="TEXT-ALIGN: center">
		<table border="0" width="90%" align="center">
			<tr><td height="3"></td></tr>
			<tr>
				<td>
					当前位置 >> 报表查询 >> 人行格式导出
				</td>
			</tr>
		</table>
		<html:form action="/exportPbocAFReport.do" method="POST" styleId="frmChk" >

		<table cellSpacing="0" cellPadding="4" width="98%" border="0"
			align="center">
			<tr>
				<td>
					<fieldset id="fieldset">
						<table cellSpacing="0" cellPadding="4" width="98%" border="0"
							align="center">
							<tr>
								<td height="5" bgcolor="#ffffff"></td>
							</tr>

							<tr>
								<td align="right" width="300">
									人行报表：
								</td>
								<td align="left" valign="middle" >
												<html:select styleId="repName" multiple="multiple" property="repName"
													size="10" style="width: 80%">
													<%
														List list = (List) request.getAttribute("list");
																for (int i = 0; i < list.size(); i++) {
																	Aditing ad = (Aditing) list.get(i);
																	String measureId = ad.getActuFreqID()
																			+ ":" + ad.getChildRepId() + ":"
																			+ ad.getVersionId() + ":" + ad.getCurId();
																	String measureName = "("+ad.getActuFreqName()+")"+ad.get_repName();	
																								
													%>
													<html:option value="<%=measureId%>">
														<%=measureName%>
													</html:option>
													<%
														}
													%>
												</html:select>
								</td>
							</tr>
							
							<tr>
							<td height="25" align="right" width="300">
									报送机构：
							</td>
							<td height="25" align="left" >
									<html:select property="orgId" size="1" styleId="orgId">
										<html:option value=""></html:option>
										<html:optionsCollection name="utilSubOrgForm" property="childRepSearch"/>
									</html:select>		
							</td>
							</tr>	
													
							<tr>
								<td height="25" align="right" width="300">
									报表时间：
								</td>
								<td height="25" align="left">
									<html:text property="date" readonly="true" size="10" 
										styleId="date" style="text" onclick="return showCalendar('date', 'y-mm-dd');"/>
									<img src="../../image/calendar.gif" border="0"
										onclick="return showCalendar('date', 'y-mm-dd');">
								</td>
							</tr>
																						
							<tr>
								<td height="25" align="right" width="300">
									报表批次：
								</td>
								<td height="25" align="left">
									<html:select styleId="batchId" property="batchId">
										<html:option value="-999">全部</html:option>
										<html:option value="1">第一批</html:option>
										<html:option value="2">第二批</html:option>
									</html:select>
								</td>
							</tr>														

							<tr>
								<td align="right" valign="middle">
									<input type="button" value="人行数据导出" class="input-button"
										onclick="rhExport()">
								</td>
							</tr>
						</table>
					</fieldset>
				</td>		
			</tr>
		</table>		
		</html:form>
	</body>
</html:html>
