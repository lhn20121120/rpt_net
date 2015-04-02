<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
	<head>
		<html:base/>
		<title>设定金融子行的填报范围</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" type="text/css" rel="stylesheet">

		<script language="javascript">
		  	var curPage = 0;
		  	//全选
		  	function _selectAll(){
		  		document.form2.selectOrgIds.checked = true;
		  	}
		  	//全取消
		  	function _cancelAll(){
		  		document.form2.selectOrgIds.checked = false;
		  	}
		  	
		  	function nextPage()
		  	{
		  		var curPage = "<bean:write name='ApartPage' property='nextPageUrl'/>";
		  		document.getElementById('curPage').value = curPage;
		  		document.form2.submit();
		  	}
		  	function prevPage()
		  	{
		  		var curPage = "<bean:write name='ApartPage' property='prevPageUrl'/>";
		  		curPage = "<bean:write name='ApartPage' property='prevPageUrl'/>";
		  		document.getElementById('curPage').value = curPage;
		  		document.form2.submit();
		  	}
		  	function lastPage()
		  	{
		  		curPage = "<bean:write name='ApartPage' property='lastPageUrl'/>";
		  		document.getElementById('curPage').value = curPage;
		  		document.form2.submit();
		  	}
		  	function firstPage()
		  	{
		  		curPage = "<bean:write name='ApartPage' property='firstPageUrl'/>";
		  		document.getElementById('curPage').value = curPage;
		  		document.form2.submit();		  	
		  	}
		  	
		  	/**
		  	 * 返回异常变化标准设定
		  	 */
		  	function _back()
		  	{
				 <logic:present name="ChildRepId" scope="request">
					var childRepId = "<bean:write name="ChildRepId"/>";
				</logic:present>
				<logic:present name="VersionId" scope="request">
					var versionId = "<bean:write name="VersionId"/>";
				</logic:present>
				<logic:present name="ReportName" scope="request">
					var reportName = "<bean:write name="ReportName"/>";
				</logic:present>
				<logic:present name="ReportStyle" scope="request">
					var reportStyle = "<bean:write name="ReportStyle"/>";
				</logic:present>
							
				<logic:notPresent name="ChildRepId" scope="request">
					var childRepId = "";
				</logic:notPresent>
				<logic:notPresent name="VersionId" scope="request">
					var versionId ="";
				</logic:notPresent>
				<logic:notPresent name="ReportName" scope="request">
					var reportName ="";
				</logic:notPresent>
				<logic:notPresent name="ReportStyle" scope="request">
					var reportStyle ="";
				</logic:notPresent>
			  
			  window.location="<%=request.getContextPath()%>/template/returnTBFW_mod.do?" + 
			  	  	"childRepId=" + childRepId +
			  	  	"&versionId=" + versionId + 
			  	  	"&reportStyle=" + reportStyle+
			  	  	"&reportName=" + reportName;;
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
	
	<html:form action="/template/viewTBFWOrgInfo_mod" method="post">
		<logic:present name="orgClsId">
			<input type="hidden" name="orgClsId" value="<bean:write name="orgClsId"/>"/>
		</logic:present>
		<logic:present name="orgClsName">
			<input type="hidden" name="orgClsName" value="<bean:write name="orgClsName"/>"/>
		</logic:present>
		<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
			<tr>
				<td align="center" width="100%">
					<table width="100%" border="0" cellpadding="4" cellspacing="0">
						<tr>
							<td align="center">
								请输入要查询的子行名称：<html:text styleClass="input-text" property="orgName" size="40"/>&nbsp;
								<input type="submit" class="input-button" value="查询"/>
							</td>
						</tr>
	</html:form>
						<tr>
							<td align="center">
								<fieldset id="fieldset">
									<legend>
										<strong>&nbsp;选择填报的金融子行类型</strong>
									</legend>
									<br>
									<table width="96%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
											<tr class="tableHeader">
												<td width="10%" align="center"></td>
												<td width="60%" align="center">子行名称</td>
												<td width="30%" align="center">子行类型</td>
											</tr>
									<html:form action="/template/operationTBFWOrgType_mod" method="post" styleId="form2">
										
										<input type="hidden" id="curPage" name="curPage" value="0"/>
										
										<logic:present name="orgClsId">
											<input type="hidden" name="orgClsId" value="<bean:write name="orgClsId"/>"/>
										</logic:present>
										<logic:present name="orgClsName">
											<input type="hidden" name="orgClsName" value="<bean:write name="orgClsName"/>"/>
										</logic:present>			
										<logic:present name="ChildRepId">
											<input type="hidden" name="childRepId" value="<bean:write name="ChildRepId"/>"/>
										</logic:present>
										<logic:present name="VersionId">
											<input type="hidden" name="versionId" value="<bean:write name="VersionId"/>"/>
										</logic:present>
										<logic:present name="ReportStyle">
											<input type="hidden" name="reportStyle" value="<bean:write name="ReportStyle"/>"/>
										</logic:present>
										<logic:present name="ReportName">
											<input type="hidden" name="reportName" value="<bean:write name="ReportName"/>"/>
										</logic:present>
															
										<logic:present name="Records" scope="request">
											<logic:iterate id="item" name="Records">
												<tr bgcolor="#FFFFFF">
													<td  align="center">
														<logic:equal name="item" property="checked" value="false">
															<INPUT type="checkbox" name="selectOrgIds" value="<bean:write name="item" property="orgId"/>"/>				
														</logic:equal>
														<logic:notEqual name="item" property="checked" value="false">
															<INPUT type="checkbox" name="selectOrgIds" value="<bean:write name="item" property="orgId"/>" checked />
														</logic:notEqual>
													</td>
													<td>
														<input type="hidden" name="orgIds" value="<bean:write name="item" property="orgId"/>"/>	
														<bean:write name="item" property="orgName"/>
													</td>
													<td align="center">
														<bean:write name="item" property="orgClsName"/>
													</td>
												</tr>
											</logic:iterate>
										
										</logic:present>	
										<logic:notPresent name="Records" scope="request">
											<tr align="left">
												<td bgcolor="#ffffff" colspan="5">
													无匹配记录
												</td>
											</tr>
										</logic:notPresent>
									</table>
									<table width="96%" border="0" align="center" cellpadding="4" cellspacing="0">	
										<tr>
											<td height="10"></td>
										</tr>
										
										<tr>
											<td>
												  <logic:present name="ApartPage" scope="request">
																<table width="99%" border="0" align="center" cellpadding="2" cellspacing="1">
																	<tr>
																		<td width="30%">
																			共<span class="apartpage_span"><bean:write name="ApartPage" property="count"/></span>条记录
																				&nbsp;第<span class="apartpage_span"><bean:write name="ApartPage" property="curPage"/></span>/<span class="apartpage_span"><bean:write name="ApartPage" property="pages"/></span>页
																		</td>
																		<td align="right" width="40%">
																			<logic:equal name="ApartPage" property="isCanBack" value="1">
																				<a href="javascript:firstPage()" class="apartpage">首页</a>&nbsp;
																				<a href="javascript:prevPage()" class="apartpage">上一页</a>&nbsp;
																			</logic:equal>
																			<logic:equal name="ApartPage" property="isCanForward" value="1">
																				<a href="javascript:nextPage()" class="apartpage">下一页</a>&nbsp;
																				<a href="javascript:lastPage()" class="apartpage">尾页</a>&nbsp;
																			</logic:equal>
																		</td>
																		<td align="right" width="30%">
																			转<input type="text" size="4" name="gotoPage">页&nbsp;
																			<input type="button" value="GO">
																		</td>
																	</tr>
																</table>
													   </logic:present>
												</td>
											</tr>
											<tr>
												<td align="right">
													<html:submit styleClass="input-button" value="确定"/>
													<html:button property="back" styleClass="input-button" onclick="_back()" value="返回报送范围设定"/>
												</td>
											</tr>
									</table>
									</html:form>
								</fieldset>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	
	</body>
</html:html>