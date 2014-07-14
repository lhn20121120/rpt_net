<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config" %>
<%@ page import="com.cbrc.smis.security.Operator" %>
<%
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepReportPodedom = operator != null ? operator.getChildRepReportPopedom() : "";
	String orgId = operator != null ? operator.getOrgId() : "";
	String selectOrgId = request.getAttribute("orgId") != null ? request.getAttribute("orgId").toString() : orgId;
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	
%>
<jsp:useBean id="utilSubOrgForm" scope="page" class="com.fitech.net.form.UtilSubOrgForm" />
<jsp:setProperty property="childRepReportPodedom" name="utilSubOrgForm" value="<%=childRepReportPodedom%>"/>
<jsp:setProperty property="orgId" name="utilSubOrgForm" value="<%=orgId%>"/>

<script language="javascript">	
	var SPLIT_SYMBOL_COMMA="<%=Config.SPLIT_SYMBOL_COMMA%>";
	function _submit(form){
				if(form.date.value==""){
					alert("请输入报表时间!");
					form.date.focus();
					return false;
				}	
	}	
	
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
     * 有选择下载数据文件
	 */
	function _downLoadTemplateCheck(){
		var dates = document.getElementById("date").value;		
		var year = dates.split("-")[0];
		var term = dates.split("-")[1];
		//var year = document.getElementById("year");
		//var term = document.getElementById("setDate");		
		var objForm=document.getElementById("frmChk");
		var count=objForm.elements['countChk'].value;
		var repNames="";
		var obj=null;
		for(var i=1;i<=count;i++){
			try{
				obj=eval("objForm.elements['chk" + i + "']");
				if(obj.checked==true){
					repNames+=(repNames==""?"":SPLIT_SYMBOL_COMMA) + obj.value;
				}
			}catch(e){}
		}	
	  	if(repNames==""){
			alert("请选择要下载的数据文件!\n");
			return;
		}else{
			window.location="<%=request.getContextPath()%>/servlets/downLoadTemplateServlet?repNames=" + repNames + "&orgId=<%=selectOrgId%>&year=" + year+ "&term=" + term;		
		}
	}	
	
	/**
	 * 下载单个数据文件
	 */
	function _downLoadSingle(repName){		
		var dates = document.getElementById("date").value;
		var year = dates.split("-")[0];
		var term = dates.split("-")[1];
		//var year = document.getElementById("year");
		//var term = document.getElementById("setDate");
		window.location="<%=request.getContextPath()%>/servlets/downLoadTemplateServlet?repNames=" + repName + "&orgId=<%=selectOrgId%>&year=" + year + "&term=" + term;		
	}
	
	/**
	 * 下载全部
	 */
	function _downLoadAll(){	
		var reportName = "<logic:present name='reportName'><bean:write name='reportName'/></logic:present>";
		var dates = document.getElementById("date").value;
		var year = dates.split("-")[0];
		var term = dates.split("-")[1];
		//var year = document.getElementById("year");
		//var term = document.getElementById("setDate");
		window.location="<%=request.getContextPath()%>/servlets/downLoadTemplateServlet?repNames="+reportName+"&year=" + year + "&term=" + term + "&orgId=<%=selectOrgId%>" + "&type=downAll";
	}
	
	//设置代报机构下列框的数据
<%--	function setSelectData(){	--%>
<%--		var orgId = document.getElementById('orgId');				--%>
<%--		for(var i=0;i<orgId.length;i++){--%>
<%--	  		if(orgId.options[i].value == "<%=selectOrgId%>"){		--%>
<%--	  			orgId.selectedIndex = i;--%>
<%--	  			break;--%>
<%--	  		}--%>
<%--	  	}				--%>
<%--	}--%>
</script>

<html:html locale="true">
	<head>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="../../js/func.js"></script>
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>
		<logic:present name="Message" scope="request">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:present>
	
	</head>
	<body style="TEXT-ALIGN: center">
		<table border="0" width="90%" align="center">
			<tr><td height="3"></td></tr>
			<tr>
				<td>
					当前位置 >> 数据采集 >> 报表下载
				</td>
			</tr>
		</table>
		<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
			<html:form action="/template/templateDownloadList.do" method="post" onsubmit="return _submit(this)">	
			<tr>
				<td>
					<fieldset id="fieldset">					
						<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">				
							<tr>
							<%--							
								<td>
									报表时间：
									<html:text property="year" maxlength="4" size="7" styleClass="input-text" />
									年
									<html:text property="setDate" maxlength="2" size="5" styleClass="input-text" />
									月
								</td>
							--%>	
								<td height="25">				
							&nbsp;报表时间：
								<html:text property="date" styleClass="input-text" size="10" styleId="date1"
										readonly="true" onclick="return showCalendar('date1', 'y-mm');" />
								<img border="0" src="<%=basePath%>image/calendar.gif"
										onclick="return showCalendar('date1', 'y-mm');">
							</td>
								
								<%
								 String org=(String)request.getAttribute("orgId");
								%>
								<td height="25">
									
									<input type="hidden" value="<%=org%>">
									
								</td>	
								
								<td>
								报表名称：
								<html:text property="repName" size="25" styleClass="input-text" />
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
		<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
			<tr>
				<td>
					<input type="button" value="复选下载" class="input-button" onclick="_downLoadTemplateCheck()">&nbsp;
					<input type="button" value="下载全部" class="input-button" onclick="_downLoadAll()">							
				</td>
			</tr>
		</table>
		<html:form action="/template/templateDownload.do" method="POST" styleId="frmChk">						
			<TABLE cellSpacing="0" width="98%" border="0" align="center" cellpadding="4">					
				<TR>
					<TD>
						<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
							<tr class="titletab">
								<th colspan="8" align="center" id="list"><strong>模板列表</strong></th>
							</tr>
							<TR class="middle">
								<td width="5%" align="center" valign="middle">
									<input type="checkbox" name="chkAll" onclick="_selAll()">
								</td>
								<TD class="tableHeader" width="10%">机构名称</td>
								<TD class="tableHeader" width="8%">报表编号</td>	
								<TD class="tableHeader" width="40%">模板名称</TD>		
								<TD class="tableHeader" width="7%">报表时间</TD>															
								<TD class="tableHeader" width="5%">版本号</td>
								<TD class="tableHeader" width="10%">报表口径</td>	
								<TD class="tableHeader" width="10%">下载</td>																	
							</TR>
							<%int i = 0;%>
							<logic:present name="Records" scope="request">
								<logic:iterate id="item" name="Records">
									<%i++;%>
									<TR bgcolor="#FFFFFF">
										<TD align="center">
											<input type="checkbox" name="chk<%=i%>" value='<bean:write name="item" property="childRepId"/>_<bean:write name="item" property="versionId"/>_<bean:write name="item" property="dataRgId" />_<bean:write name="item" property="orgId" />_<bean:write name="item" property="actuFreqID" />'>					
										</TD>												
										<td align="center"><bean:write name="item" property="orgName"/></td>	
										<td align="center"><bean:write name="item" property="childRepId"/></td>											
										<TD align="center"><bean:write name="item" property="repName" /></TD>
										<TD align="center"><bean:write name="item" property="year" />-<bean:write name="item" property="term" /></TD>
										<td align="center"><bean:write name="item" property="versionId"/></td>
										<td align="center"><bean:write name="item" property="dataRgTypeName"/></td>
										<td align="center"><a href="javascript:_downLoadSingle('<bean:write name="item" property="childRepId"/>_<bean:write name="item" property="versionId"/>_<bean:write name="item" property="dataRgId" />_<bean:write name="item" property="orgId" />_<bean:write name="item" property="actuFreqID" />')">下载</a></td>
									</TR>
								</logic:iterate>															
							</logic:present>
							<input type="hidden" name="countChk" value="<%=i%>">											
							<logic:notPresent name="Records" scope="request">
								<tr bgcolor="#FFFFFF">
									<td colspan="8">暂无记录</td>
								</tr>
							</logic:notPresent>									
						</TABLE>
								
						<table cellSpacing="1" cellPadding="0" width="100%" border="0">
							<tr>
								<TD colspan="8" bgcolor="#FFFFFF">
									<jsp:include page="../../apartpage.jsp" flush="true">
										<jsp:param name="url" value="../templateDownloadList.do" />
									</jsp:include>
								</TD>
							</tr>
						</table>
					</TD>
				</TR>
			</TABLE>	
		</html:form>	
<%--		<script language="javascript">--%>
<%--        	setSelectData();--%>
<%--        </script>--%>
	</body>
</html:html>
