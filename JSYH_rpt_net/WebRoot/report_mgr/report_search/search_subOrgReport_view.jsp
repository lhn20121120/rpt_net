<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.security.Operator" %>
<%@ page import="com.cbrc.smis.common.Config" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepSearchPodedom = operator != null ? operator.getChildRepSearchPopedom() : "";
	String orgId = operator != null ? operator.getOrgId() : "";
%>
<jsp:useBean id="utilSubOrgForm" scope="page" class="com.fitech.net.form.UtilSubOrgForm" />
<jsp:setProperty property="childRepSearchPodedom" name="utilSubOrgForm" value="<%=childRepSearchPodedom%>"/>
<jsp:setProperty property="orgId" name="utilSubOrgForm" value="<%=orgId%>"/>
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
	    	
	    	var SPLIT_SYMBOL_COMMA="<%=Config.SPLIT_SYMBOL_COMMA%>";
		  	/**
		  	 * 表单提交验证
		  	 */
		  	function _submit(form){
		  		if(form.year.value.Trim() != ""){
		  			if(isNaN(form.year.value)){ 
					   alert("请输入正确的报表时间！"); 
					   form.year.focus(); 
					   return false; 
					}
		  		}				
				if(form.term.value.Trim() != ""){
					if(isNaN(form.term.value)){ 
					   alert("请输入正确的报表时间！"); 
					   form.term.focus(); 
					   return false; 
					}
					if(form.term.value <1 || form.term.value >12){
						alert("请输入正确的报表时间！");
						form.term.focus();
						return false;
					}
				}
				
				if(form.times.value.Trim() != ""){
					if(isNaN(form.times.value)){ 
					   alert("请输入正确的报表时间！"); 
					   form.times.focus(); 
					   return false; 
					}
					if(form.times.value <1 || form.times.value >12){
						alert("请输入正确的报表时间！");
						form.times.focus();
						return false;
					}
				}
				return true;
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
		  	 function viewPdf(repInId){
				 window.location="<%=request.getContextPath()%>/servlets/toExcelServlet?repInId=" + repInId; 
			 }
			 
			 /**
		  	  * 批量导出子行报表 
		  	  * @return void
		  	  */
		     function _dcsubOrgReportCheck(){
		  	  	var objForm=document.getElementById("frmChk");
		  	  	var count=objForm.elements['countChk'].value;
		  	  	var repInIds="";
		  	  	var obj=null;
		  	  	for(var i=1;i<=count;i++){
		  	  		try{
			  	  		obj=eval("objForm.elements['chk" + i + "']");
			  	  		if(obj.checked==true){
			  	  			repInIds+=(repInIds==""?"":SPLIT_SYMBOL_COMMA) + obj.value;
			  	  		}
		  	  		}catch(e){}
		  	  	}	
		  	  	if(repInIds==""){
		  	  		alert("请选择要导出的报表!\n");
		  	  		return;
		  	  	}else{		  	  	  	
		  	  		window.location="<%=request.getContextPath()%>/servlets/createSubOrgReportServlet?repInIds=" + repInIds;		
		  	  	}
		  	 }
		  	 /**
		  	  * 导出全部子行报表
		  	  */
		  	 function _dcqbsj(){
		  	 	var year = document.getElementById("year");
		  	 	var term = document.getElementById("term");
		  	 	var times = document.getElementById("times");
		  	 	var orgId = document.getElementById("orgId");
		  	 	var repName = document.getElementById("repName");
		  	 	var qry = "year="+year.value+"&tterm="+term.value+"&ttimes="+times.value+"&orgId="+orgId.value+"&repName="+repName.value;				  	 	 	
		  	 	window.location="<%=request.getContextPath()%>/servlets/createSubOrgReportServlet?" + qry;		
		  	 }
		  	 
		  	 String.prototype.Trim=function(){
				return this.replace(/(^\s*)|(\s*$)/g,"");
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
				当前位置 >> 信息查询 >> 查看分支机构报表数据
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center">
		<html:form action="/reportGer/viewSubOrgReport" method="post" styleId="frm" onsubmit="return _submit(this)">
			<tr>
				<td>
					<fieldset id="fieldset">
					<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
						<tr>
							<td height="5"></td>
						</tr>
						<tr>
							<td height="25">&nbsp;
								报送机构：
								<html:select property="orgId">
									<html:option value="0">----请选择报送子行----</html:option>
									<html:optionsCollection name="utilSubOrgForm" property="childOrgs"/>
								</html:select>
							</td>
							
							<td>
								报表名称：
								<html:text property="repName" size="25" styleClass="input-text" />
							</td>
							<td>
								<html:submit styleClass="input-button" value=" 查 询 " />
							</td>
							<td>&nbsp;
							</td>
						</tr>
					</table>
					<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
						<tr>
							<td height="25">&nbsp;
								报表时间：
								<html:text property="year" maxlength="4" size="6" styleClass="input-text" />
								年
								<html:text property="term" maxlength="2" size="4" styleClass="input-text" />
								月起
								<html:text property="times" maxlength="2" size="4" styleClass="input-text" />
								月止
							</td>
						</tr>
						<tr>
							<td height="3"></td>
						</tr>
					</table>
					</fieldset>
				</td>
			</tr>
		</html:form>
	</table>
	<logic:present name="form" scope="request">
		<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
			<tr>
				<td>
					<input type="button" value="导出子行报表数据" class="input-button" onclick="_dcsubOrgReportCheck()">
					<input type="button" value="导出全部报表数据" class="input-button" onclick="_dcqbsj()">
				</td>
			</tr>
		</table>
	</logic:present>
	<table border="0" cellpadding="0" cellspacing="0" width="98%" align="center">
		<html:form action="/reportGer/viewSubOrgReport" method="post" styleId="frmChk">
			<tr>
				<td>
					<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
						<TR class="titletab">
							<th width="5%" align="center" valign="middle">
								<input type="checkbox" name="chkAll" onclick="_selAll()">
							</th>
							<th width="10%" align="center" valign="middle">
								报表编号
							</th>
							<th width="25%" align="center" valign="middle">
								报表名称
							</th>
							<th width="10%" align="center" valign="middle">
								版本号
							</th>
							<th width="10%" align="center" valign="middle">
								报表口径
							</th>
							<th width="8%" align="center" valign="middle">
								币种
							</th>
							<th width="5%" align="center" valign="middle">
								频度
							</th>
							<th width="9%" align="center" valign="middle">
								报表时间
							</th>
							<th width="17%" align="center" valign="middle">
								上报子行
							</th>
						</TR>
						<%int i = 0;%>
						<logic:present name="Records" scope="request">
							<logic:iterate id="viewReportInInfo" name="Records">
								<%i++;%>
								<TR bgcolor="#FFFFFF">
									<TD align="center">
										<logic:notEqual name="viewReportInInfo" property="checkFlag" value="-1">
											<input type="checkbox" name="chk<%=i%>" value="<bean:write name="viewReportInInfo" property="repInId"/>:<bean:write name="viewReportInInfo" property="orgId"/>">
										</logic:notEqual>
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="childRepId" />
									</TD>
									<TD align="center">
										<input type="hidden" name="repInIdArray" value="<bean:write name="viewReportInInfo" property="repInId"/>" />
										<a href="javascript:viewPdf(<bean:write name='viewReportInInfo' property='repInId'/>)">
											<bean:write name="viewReportInInfo" property="repName" />
										</a>
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
										<bean:write name="viewReportInInfo" property="year" />-<bean:write name="viewReportInInfo" property="term" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="orgName" />
									</TD>
								</TR>
							</logic:iterate>
						</logic:present>
						<input type="hidden" name="countChk" value="<%=i%>">
						<logic:notPresent name="Records" scope="request">
							<tr align="left">
								<td bgcolor="#ffffff" colspan="9">
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
					<jsp:param name="url" value="../../reportGer/viewSubOrgReport.do" />
				</jsp:include>
			</TD>
		</TR>
	</table>
</body>
</html:html>
