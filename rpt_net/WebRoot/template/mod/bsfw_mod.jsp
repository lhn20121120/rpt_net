<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.List,com.cbrc.org.form.MOrgClForm,com.cbrc.smis.util.FitechUtil" %>

<html:html locale="true">
	<head>
		<html:base/>
		<title>报表填报范围设定</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
		
		<script language="javascript">
						
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
			/**
			 * 机构详细的设定
			 *
			 * @param orgCls String 机构类别
			 * @param orgClsName String 机构类别名称
			 * @return void
			 */
			 function _set_detail(orgCls,orgClsName)
			 {
			 
			   window.location="<%=request.getContextPath()%>/template/viewTBFWOrgInfo_mod.do?orgClsId=" + orgCls + 
			    "&orgClsName=" + orgClsName+
			    "&childRepId=" + childRepId + 
			   	  "&versionId=" +versionId+ 
			   	  "&reportStyle=" + reportStyle+
			   	   "&reportName=" + reportName+
			   	   "&flag=2";
			 }
		 	/**
			  * 显示某机构类别下的用户已选择的机构列表
			  *
			  * @param orgCls String 机构类别
			  * @param orgClsName String 机构类别名称
			  * @return void
			  */
			  function _show_detail(orgCls,orgClsName){
			    window.location="<%=request.getContextPath()%>/template/viewSelectedOrg.do?orgClsId=" + orgCls + 
			      "&orgClsName=" + orgClsName+
			       "&childRepId=" + childRepId + 
			   	  "&versionId=" +versionId+ 
			   	  "&reportStyle=" + reportStyle+
			   	   "&reportName=" + reportName+"&flag=2";
			   	   
			  }
		</script>
	</head>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<body background="../image/total.gif">
		<html:form action="/template/mod/updateTBFW" method="post">
		
		<logic:present name="ChildRepId" scope="request">
				<input type="hidden" name="childRepId" value="<bean:write name="ChildRepId"/>">
		</logic:present>
		<logic:present name="VersionId" scope="request">
			<input type="hidden" name="versionId" value="<bean:write name="VersionId"/>">
		</logic:present>
		
		
		<TABLE cellSpacing="1" cellPadding="4" width="96%" border="0" align="center" class="tbcolor">
			<TR class="tbcolor1">
				<th align="center" id="list" height="30">
					<span style="FONT-SIZE: 11pt">
					 	<logic:present name="ReportName" scope="request">
							《<bean:write name="ReportName"/>》
						</logic:present>填报范围设定
					</span>
				</th>
			</TR>
			
			<tr>
				<td align="left" bgcolor="#EEEEEE">
					请选择报表的范围:
				</td>
				</tr>
			
			<tr>
				<td bgcolor="#FFFFFF">
					<table border="0" cellpadding="4" cellspacing="0" width="100%">
					
					<logic:present name="Records" scope="request">
						<bean:size name="Records" id="Count" />
						<%int i = 0;%>
						<logic:iterate id="item" name="Records" scope="request" indexId="index">
							<%if (i == 0) {
							%>
							<tr>
								<%}%>
								<td align="left">
									<logic:equal name="item" property="selAll" value="1">
										<img src="../../image/notselected.gif" border="0">
									</logic:equal>
									<logic:equal name="item" property="selAll" value="0">
										<input type="checkbox" id="selectOrgClsIds" name="selectOrgClsIds" value="<bean:write name='item' property='orgClsId'/>">
									</logic:equal>
										<a href="javascript:_set_detail('<bean:write name="item" property="orgClsId"/>','<bean:write name="item" property="orgClsNm"/>')">
											<bean:write name="item" property="orgClsNm" />
										</a>
									&nbsp;
									<logic:equal name="item" property="selAll" value="1">
										<a href="javascript:_show_detail('<bean:write name="item" property="orgClsId"/>','<bean:write name="item" property="orgClsNm"/>')">
											<img src="../../image/card.jpg" border="0" title="查看详细">
										</a>
									</logic:equal>
								</td>
								<%if (i == 2) {
				i = 0;

			%>
							</tr>
							<%} else {
				i += 1;
			}

			%>
						</logic:iterate>
						<input type="hidden" id="count" name="count" value="<%=Count==null?0:((Integer)Count).intValue()%>" />
						<%if (i == 1) {
				out.println("<td></td><td></td></tr>\n");
			}
			if (i == 2) {
				out.println("<td></td></tr>\n");
			}

		%>
					</logic:present>
								</table>
								
							</td>
						</tr>
						</TABLE>
						
					
					<tr><td colspan="3" height="20"></td></tr>
					<tr>
						<td align="center" bgcolor="#FFFFFF" colspan="3">
							<html:submit styleClass="input-text" value=" 保 存 " />&nbsp;&nbsp;
							<!-- <input class="input-button" onclick="alert('暂无')" type="button" value=" 返 回 ">-->
						</td>
					</tr>
		<table></table>	
		</html:form>			
		<table border="0" cellpadding="2" cellspacing="0" width="96%">
			<tr>
				<td height="5"></td>
			</tr>
			<tr>
				<td align="left">
				注：点击机构类型，可以具体设定当前类型下的金融机构的填报范围。
				</td>
			</tr>
		</table>

	</body>
</html:html>
