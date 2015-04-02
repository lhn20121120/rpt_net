<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.security.Operator" %>
<%@ page import="com.cbrc.smis.common.Config" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepReportPodedom = operator != null ? operator.getChildRepReportPopedom() : "";
	String orgId = operator != null ? operator.getOrgId() : "";
	String selectOrgId = request.getAttribute("orgId") != null ? request.getAttribute("orgId").toString() : orgId;
%>
<jsp:useBean id="utilSubOrgForm" scope="page" class="com.fitech.net.form.UtilSubOrgForm" />
<jsp:setProperty property="childRepReportPodedom" name="utilSubOrgForm" value="<%=childRepReportPodedom%>"/>
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

	<%
		String year = request.getAttribute("year") != null ? request.getAttribute("year").toString() : "";
		String term = request.getAttribute("term") != null ? request.getAttribute("term").toString() : "";
	%>
	<SCRIPT language="javascript">
		<logic:present name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="<bean:write name='ApartPage' property='curPage'/>";
	    </logic:present>
	    <logic:notPresent name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="1";
	    </logic:notPresent>	  
	    var EXT_EXCEL="<%=configBean.EXT_EXCEL%>";	    	
	    var EXT_ZIP="<%=configBean.EXT_ZIP%>";
	    	  
		  	function _submit(form){
				if(form.year.value==""){
					alert("请输入报表时间!");
					form.year.focus();
					return false;
				}
				if(form.setDate.value==""){
					alert("请输入报表时间！");
					form.setDate.focus();
					return false;
				}
				if(isNaN(form.year.value)){ 
				   alert("请输入正确的报表时间！"); 
				   form.year.focus(); 
				   return false; 
				}
				if(isNaN(form.setDate.value)){ 
				   alert("请输入正确的报表时间！"); 
				   form.setDate.focus(); 
				   return false; 
				}
				if(form.setDate.value <1 || form.setDate.value >12){
					alert("请输入正确的报表时间！");
					form.setDate.focus();
					return false;
				}
			}
			function _view_XSYY(repInId){
		     	window.open("<%=request.getContextPath()%>/report/viewJYNotPassInfo.do?" + "repInId=" + repInId);
		    }	
				    
		    function _submit2(form){			
				if(form.formFile.value==""){
					alert("上传文件不能为空");
					form.formFile.focus();
					return false;
				}
				if(getExt(form.formFile.value)!=EXT_EXCEL && getExt(form.formFile.value)!=EXT_ZIP){
			 		alert("选择的报送文件必须是Excel或Zip包文件!");
			 		form.formFile.focus();
			 		return false;
			 	}
				var year_kj = document.getElementById("year");	
				var term_kj = document.getElementById("setDate");
				var version_kj = document.getElementById("versionId");	
				version_kj.value = year_kj.value + "_" + term_kj.value + "_<%=selectOrgId%>_" + curPage;
				return true;
			}
			//设置代报机构下列框的数据
<%--			function setSelectData(){	--%>
<%--				var orgId = document.getElementById('orgId');				--%>
<%--				for(var i=0;i<orgId.length;i++){--%>
<%--			  		if(orgId.options[i].value == "<%=selectOrgId%>"){		--%>
<%--			  			orgId.selectedIndex = i;--%>
<%--			  			break;--%>
<%--			  		}--%>
<%--			  	}				--%>
<%--			}--%>
			function _view_sjbs(){
				var repInIds = "";
				var count = document.getElementById("count").value;
				if(count != 0){
					for(var i=1;i<=count;i++){
						if(repInIds == "")
							repInIds = document.getElementById("repInId"+i).value;
						else
							repInIds = repInIds + "," + document.getElementById("repInId"+i).value;
					}
				}
		    	window.location="<%=request.getContextPath()%>/upLoadOnLineReport.do?" + "repInId=" + "&reportId=" + repInIds + "&year=<%=year%>&setDate=<%=term%>&curPage=" + curPage + "&orgId=<%=selectOrgId%>";
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
				当前位置 >> 数据报送 >> 数据上报
			</td>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="0" border="0" width="90%" align="center">
		<html:form action="/viewDataReport.do" method="post" styleId="frm" onsubmit="return _submit(this)">
			<tr>
				<td>
					<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
						<tr>							
							<td>
								报表时间：
								<html:text property="year" maxlength="4" size="7" styleClass="input-text" value="<%=year%>"/>
								年
								<html:text property="setDate" maxlength="2" size="5" styleClass="input-text" value="<%=term%>"/>
								月
							</td>
<%--							<td height="25">&nbsp;--%>
<%--								报送机构：--%>
<%--								<html:select property="orgId">										--%>
<%--									<html:optionsCollection name="utilSubOrgForm" property="reportOrgs"/>--%>
<%--								</html:select>--%>
<%--							</td>												--%>
							<td>
								<html:submit styleClass="input-button" value=" 查 询 " />
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="4"></td>
			</tr>
		</html:form>
		<br/>
		<table cellSpacing="1" cellPadding="4" border="0" width="90%" class="tbcolor" align="center">
			<tr class="titletab"><th align="center" colspan="4">数据报送</th></tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%">
					<html:form method="post" action="/template/uploadFile" enctype="multipart/form-data" onsubmit="return _submit2(this)">
						<div align="center">
							<html:file  property="formFile" size="80" styleClass="input-button"/>
							<html:hidden property="versionId"/>
							<html:submit styleClass="input-button" value="载入"/>
                        </div>
                    </html:form>
                </td>
            </tr>
		</table>
	</table>	
	<p/><br/>
	<table border="0" cellpadding="0" cellspacing="0" width="90%" align="center">
		<tr>
			<td>
				<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
					<TR class="titletab">
						<th width="10%" align="center" valign="middle">编号</th>
						<th width="33%" align="center" valign="middle">报表名称</th>
						<th width="10%" align="center" valign="middle">版本号</th>
						<th width="10%" align="center" valign="middle">币种</th>
						<th width="15%" align="center" valign="middle">报表口径</th>
						<th width="5%" align="center" valign="middle">频度</th>
						<th width="9%" align="center" valign="middle">报表时间</th>							
						<Th width="11%" align="center" valign="middle">状态</Th>
					</TR>
					
					<logic:present name="Records" scope="request">
					<%
						int i=0;
					%>
						<logic:iterate id="viewReportIn" name="Records">
						<%
							i++;
						%>
							<TR bgcolor="#FFFFFF">
								<TD align="center"><bean:write name="viewReportIn" property="childRepId" /></TD>
								<TD align="center"><bean:write name="viewReportIn" property="repName" /></TD>
								<TD align="center"><bean:write name="viewReportIn" property="versionId" /></TD>
								<TD align="center"><bean:write name="viewReportIn" property="currName" /></TD>
								<TD align="center"><bean:write name="viewReportIn" property="dataRgTypeName" /></TD>
								<TD align="center"><bean:write name="viewReportIn" property="actuFreqName" /></TD>
								<TD align="center"><bean:write name="viewReportIn" property="year" />-<bean:write name="viewReportIn" property="term" />
								</TD>									
								<TD align="center">
									<logic:equal name="viewReportIn" property="checkFlag" value="-1">
										<a href="javascript:_view_XSYY(<bean:write name='viewReportIn' property='repInId'/>)">校验不通过</a>
									</logic:equal>
									<logic:equal name="viewReportIn" property="checkFlag" value="0">校验通过</logic:equal>
								</TD>								
							</TR>
							<input type="hidden" name="repInId<%=i%>" value="<bean:write name='viewReportIn' property='repInId'/>">
						</logic:iterate>
						<input type="hidden" name="count" value="<%=i%>">
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
		<script language="javascript">
        //	setSelectData();
        </script>
	</table>
	<table border="0" cellpadding="0" cellspacing="0" width="90%" align="center">
		<TR><TD>&nbsp;</TD></TR>
		<TR>
			<TD>
				<logic:notPresent name="isShowBS" scope="request">
			      	<tr>
				    	<td colspan="4">
				    		<DIV align="right">				    			
				    			<input class="input-button" type="reset" value="报  送" onclick="_view_sjbs()"/>
				    		</DIV></td>
				    </tr>		
				</logic:notPresent>
			</TD>
		</TR>
	</table>
</body>
</html:html>
