<%@ page language="java" pageEncoding="GB2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<html:html locale="true">
<head>
	<html:base />
	<title>机构报送情况统计（银监会报表）</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="../../js/prototype-1.4.0.js"></script>
	
	<script type="text/javascript">
		<logic:present name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
			var curPage="<bean:write name='ApartPage' property='curPage'/>";
		</logic:present>
	    <logic:notPresent name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="1";
	    </logic:notPresent>
		   
	    //查看下级机构
	    function viewsubOrg(orgId){
	    	if($('img_'+orgId).src.indexOf('add.gif')>0){     		
				$('tr_'+orgId).style.display="";
     	 		$('img_'+orgId).src="<%=request.getContextPath() %>/image/subtract.gif";
     	 	}else {
				$('tr_'+orgId).style.display="none";
				$('img_'+orgId).src="<%=request.getContextPath() %>/image/add.gif";
     	 	}
	    }
	    
	    //查看报送统计详细
	    function  _viewOrgReportStat(orgId,orgName){
	    	var objFrm=document.forms['frm'];
	    	var qry="orgId=" + orgId +
	    			"&orgName=" + orgName + 
	    			"&year=" + objFrm.elements['year'].value +
	    			"&term=" + objFrm.elements['term'].value;
	    			
	    	window.open("<%=request.getContextPath()%>/searchDataStat.do?" + qry);
	    }
	</script>
</head>
<body>
<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
  <label   id="prodress1" >
	<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 >> 报表查询 >> 报送统计（银监会报表）
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center">		
		<html:form method="post" styleId="frm" action="/reportStatistics.do" >
			<tr>
				<td>
					<fieldset id="fieldset" height="40">
					<br/>
						<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center">
							<tr>
								<td align="left" >
									机构名称：<html:text property="org_name" size="20" styleClass="input-text" />
								</td>
								<td height="25">&nbsp;
									报表时间：
									<html:text property="year" maxlength="4" size="4" styleClass="input-text" />
									年
									<html:text property="term" maxlength="2" size="2" styleClass="input-text" />
									月
								</td>
								<td >
									<input class="input-button" type="submit" name="Submit" value="查  询">									
								</td>
							</tr>
						</table>
					</fieldset>
				</td>				
			</tr>
		</html:form>
	</table>
	<br/>
		<table width="98%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
			<tr class="titletab" id="tbcolor">
				<th height="24" align="center" id="list" colspan="10">
					机构报送情况统计
				</th>
			</tr>
			<tr>
			<TR class="middle">
				<TD align="center" width="10%">
					<strong>机构编号</strong>
				</TD>
				<TD align="center">
					<strong>机构名称</strong>
				</TD>
				<TD align="center" width="15%">
					<strong>机构类别</strong>
				</TD>
				<TD align="center" width="15%">
					<strong>应报/已报</strong>
				</TD>
				<TD align="center" width="15%">
					<strong>操作</strong>
				</TD>
			</TR>
			<%
				java.util.List orgList = request.getAttribute("form") != null ? (java.util.List)request.getAttribute("form") : null;
				if(orgList != null && orgList.size() > 0){
					for(int i=0;i<orgList.size();i++){
						com.fitech.net.form.OrgNetForm orgNetForm = (com.fitech.net.form.OrgNetForm)orgList.get(i);
						if(orgNetForm != null){
							%>
							<tr bgcolor="#FFFFFF">
								<td align=left>
									<img id='img_<%=orgNetForm.getOrg_id()%>' src="<%=request.getContextPath() %>/image/add.gif" 
										onclick="javascript:viewsubOrg('<%=orgNetForm.getOrg_id()%>');"/>									
									<%=orgNetForm.getOrg_id()%>
								</td>
								<td align="center"><%=orgNetForm.getOrg_name()%></td>
								<td align="center"><%=orgNetForm.getOrg_type_name()%></td>
								<td align="center">
									<%
										if(orgNetForm.getYbReportNum().intValue() == orgNetForm.getBsReportNum().intValue()){
									%><%=orgNetForm.getYbReportNum()%>/<font color="red"><%=orgNetForm.getBsReportNum()%><%
										}else{
									%><%=orgNetForm.getYbReportNum()%>/<font color="red"><%=orgNetForm.getBsReportNum()%></font><%
										}
									%>
								</td>
								<td align="center">
									<input type="button" class="input-button" value="查看详细" onclick="_viewOrgReportStat('<%=orgNetForm.getOrg_id()%>','<%=orgNetForm.getOrg_name()%>')">
								</td>				
							</tr>
							<tr id='tr_<%=orgNetForm.getOrg_id()%>' bgcolor="#FFFFFF" style="display:none">
								<td align="center" colspan=5>
									<div id='div_<%=orgNetForm.getOrg_id()%>'>
										<table width="98%" border="0"  cellspacing="1" class="tbcolor">		
											<%
												java.util.List subOrgList = orgNetForm.getSubOrgList();
												if(subOrgList != null && subOrgList.size() > 0){
													for(int j=0;j<subOrgList.size();j++){
														com.fitech.net.form.OrgNetForm subOrgNetForm = (com.fitech.net.form.OrgNetForm)subOrgList.get(j);
											%>
											<tr bgcolor="#FFFFFF">
												<td align="center" width="15%"><%=subOrgNetForm.getOrg_id()%></td>
												<td align="center"><%=subOrgNetForm.getOrg_name()%></td>
												<td align="center" width="15%"><%=subOrgNetForm.getOrg_type_name()%></td>
												<td align="center" width="15%">
													<%
														if(subOrgNetForm.getYbReportNum().intValue() == subOrgNetForm.getBsReportNum().intValue()){
													%><%=subOrgNetForm.getYbReportNum()%>/<font color="red"><%=subOrgNetForm.getBsReportNum()%><%
														}else{
													%><%=subOrgNetForm.getYbReportNum()%>/<font color="red"><%=subOrgNetForm.getBsReportNum()%></font><%
														}
													%>
												</td>
												<td align="center" width="15%">
													<input type="button" class="input-button" value="查看详细" onclick="_viewOrgReportStat('<%=orgNetForm.getOrg_id()%>','<%=subOrgNetForm.getOrg_name()%>')">
												</td>
											</tr>
											<%
													}
												}else{
											%>
											<tr bgcolor="#FFFFFF">
												<td colspan="5">
													暂无分行机构信息
												</td>
											</tr>
											<%
												}
											%>
										</table>
									</div>
								</td>
							</tr>
							<%
						}
					}
				}else{
				%>
					<tr bgcolor="#FFFFFF">
						<td colspan="5">
							暂无报送情况统计信息
						</td>
					</tr>
				<%
				}
			%>
		</table>
		<table cellSpacing="1" cellPadding="4" width="98%" border="0">
			<tr>
				<TD colspan="7" bgcolor="#FFFFFF">
					<jsp:include page="../../apartpage.jsp" flush="true">
						<jsp:param name="url" value="../../reportStatistics.do" />
					</jsp:include>	
				</TD>
			</tr>
		</table>
</body>
</html:html>
