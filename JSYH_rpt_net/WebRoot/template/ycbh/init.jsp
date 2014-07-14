<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<html:html locale="true">
	<head>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="../../js/func.js"></script>
		<script language="javascript">
			<logic:present name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
				var _curPage="<bean:write name='<%=Config.APART_PAGE_OBJECT%>' property='curPage' scope='request'/>";
			</logic:present>
			<logic:notPresent name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
				var _curPage="0";
			</logic:notPresent>

			 /**
			  * 异常变化标准修改事件
			  *
			  * @param childRepId 子报表ID
			  * @param versionId 版本号
			  * @return void
			  */
			  function _ycbh_mod(childRepId,versionId){
			  	window.location="<%=request.getContextPath()%>/template/ycbh/editYCBHInit.do" +
			  		"?childRepId=" + childRepId + 
			 		"&versionId=" + versionId + 
			 		"&curPage=" + _curPage;
			  }
		</script>
	</head>
	<body style="TEXT-ALIGN: center" background="../../image/total.gif">
		<logic:present name="Message" scope="request">
			<logic:greaterThan name="Message" property="size" value="0">
				<script language="javascript">
					alert("<bean:write name='Message' property='alertMsg'/>");
				</script>
			</logic:greaterThan>
		</logic:present>
		<P style="MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px"></P>
		<P style="MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px"></P>
				<p style="MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px"></p>
		<html:form action="/template/ycbh/viewMChildReport" method="POST">
			<table cellspacing="0" cellpadding="0" border="0" style="BACKGROUND-IMAGE: url(../../image/inside_index_bg4.jpg)"
				height="48" width="100%">
					<TR>
						<TD width="111">
							<img border="0" src="../../image/inside_index_mid15.jpg" width="111" height="48"></TD>
								<TD width="9" valign="bottom">
									　</TD>
								<TD width="3" background="../../image/inside_index_bg4_split.jpg">
								</TD>
								<TD width="106" valign="bottom">
									<p align="right" style="margin-top: 0px; margin-bottom: -2px">模板名称：</p>
									<p align="right" style="margin-top: -2px; margin-bottom: -2px">　</p>
								</TD>
								<TD width="250" valign="bottom">
									<p align="center" style="margin-top: 0px; margin-bottom: -2px"><font face="Arial">
											<span style="FONT-SIZE: 9pt">
												<input class="input-text" id="Text1" type="text" size="28" name="reportName"></span></font></p>
									<p align="center" style="margin-top: -2px; margin-bottom: -2px">　</p>
								</TD>
								<TD width="78" valign="bottom">
									<p align="right" style="margin-top: 0px; margin-bottom: -2px">版本号：</p>
									<p align="right" style="margin-top: 0px; margin-bottom: -2px">　</p>
								</TD>
								<TD width="68" valign="bottom">
									<p align="center" style="margin-top: 0px; margin-bottom: -2px">
										<INPUT class="input-text" id="Text2" type="text" size="10" name="versionId"></p>
									<p align="center" style="margin-top: 0; margin-bottom: -2px">
										　</p>
								</TD>
								<TD width="224" valign="middle" align="right">
									<INPUT class="input-button" id="Button3" type="submit" value=" 查 询 " name="Button1">		
								</TD>
					</TR>

			</table>
		
				<TABLE cellSpacing="0" width="96%" border="0" align="center" cellpadding="4">
					<TR>
						<TD>
							<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0"  class="tbcolor">
								<tr class="titletab">
									<th colspan="7" align="center" id="list">
										<strong>
											报表模板列表
										</strong>
									</th>
								</tr>
								<TR class="middle">
									<TD class="tableHeader" width="10%">
										<b>编号</b>
									</TD>
									<TD class="tableHeader" width="40%">
										<b>报表模板名称</b>
									</TD>
									<TD class="tableHeader" width="10%">
										<b>版本号</b>
									</TD>
									<TD class="tableHeader" width="10%">
										<b>报表类型</b>
									</TD>
									<TD class="tableHeader" width="10%">
										<b>货币单位</b>
									</TD>
									<TD class="tableHeader" width="10%">
										<b>是否发布</b>
									</TD>
									<TD class="tableHeader" width="10%">
										<b>操作</b>
									</TD>									
								</TR>
									<logic:present name="Records" scope="request">
											<logic:iterate id="item" name="Records">
												<TR bgcolor="#FFFFFF">
													<TD  align="center" width="10%">
														<logic:notEmpty name="item" property="childRepId">
															<bean:write name="item" property="childRepId"/>
														</logic:notEmpty>
														<logic:empty name="item" property="childRepId">
															无
														</logic:empty>
													</TD>
													<TD align="center" width="30%">
														<logic:notEmpty name="item" property="reportName">
															<a href="<%=request.getContextPath()%>/servlets/selfReadExcelServlet?childRepId=<bean:write name='item' property='childRepId'/>&versionId=<bean:write name='item' property='versionId'/>" >
																<bean:write name="item" property="reportName"/>
															</a>
														</logic:notEmpty>
														<logic:empty name="item" property="reportName">
															无
														</logic:empty>
													</TD>
													<TD align="center" width="10%">
														<logic:notEmpty name="item" property="versionId">
															<bean:write name="item" property="versionId"/>
														</logic:notEmpty>
														<logic:empty name="item" property="versionId">
															无
														</logic:empty>
													</TD>
													<TD align="center" width="10%">
														<logic:notEmpty name="item" property="repTypeName">
															<bean:write name="item" property="repTypeName"/>
														</logic:notEmpty>
														<logic:empty name="item" property="repTypeName">
															无
														</logic:empty>
													</TD>
													<TD  align="center" width="10%">
														<logic:notEmpty name="item" property="curUnitName">
															<bean:write name="item" property="curUnitName"/>
														</logic:notEmpty>
														<logic:empty name="item" property="curUnitName">
															无
														</logic:empty>
													</TD>
													<TD  align="center" width="10%">
														<logic:equal name="item" property="isPublic" value="0">
															未发布
														</logic:equal>
														<logic:equal name="item" property="isPublic" value="1">
															已发布
														</logic:equal>
													</TD>
													<TD align="center" width="20%">
														<a href="javascript:_ycbh_mod('<bean:write name="item" property="childRepId"/>','<bean:write name="item" property="versionId"/>')"><img src="../../image/ycbh_mod.gif" border="0" title="异常变化标准修改"></a>
													</TD>
												</TR>
											</logic:iterate>
									</logic:present>
									<logic:notPresent name="Records" scope="request">
										<tr bgcolor="#FFFFFF">
											<td colspan="7">
												无匹配记录
											</td>
										</tr>
									</logic:notPresent>
								</TABLE>
									<table cellSpacing="1" cellPadding="4" width="100%" border="0">
										<tr>
											<TD colspan="7" bgcolor="#FFFFFF">
											<jsp:include page="../../apartpage.jsp" flush="true">
												<jsp:param name="url" value="viewMChildReport.do" />
											</jsp:include>
											</TD>
										</tr>
									</table>
						</TD>
					</TR>
				</TABLE>	
			</html:form>		
	</body>
</html:html>
