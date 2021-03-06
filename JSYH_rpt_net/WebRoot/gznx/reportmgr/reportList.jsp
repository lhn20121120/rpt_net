<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%
	/** 报表选中标志 **/
	String reportFlg = "0";	
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
	}		
%>
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
		<script language="javascript">
			function addreport(){
				window.location="<%=request.getContextPath()%>/gznx/reportmgr/addNXTemplate.jsp";
			}
			
			function treeOnClick(id,value)
			{
				document.getElementById('templateType').value = id;
				document.getElementById('templateTypeName').value = value;
				document.getElementById("orgTree").style.height = "0";
				document.getElementById('orgTree').style.visibility="hidden"; 
			}
			

		//显示,关闭树型菜单
		function showTree(){
		if(document.getElementById('orgTree').style.visibility =='hidden'){
		    var textname = document.getElementById('selectedTypeName');
			document.getElementById("orgTree").style.top = getObjectTop(textname)+20;
			document.getElementById("orgTree").style.left = getObjectLeft(textname);
			
			document.getElementById("orgTree").style.height = "200";
			document.getElementById("orgTree").style.visibility = "visible";   // 显示树型菜单
		}

		else if(document.getElementById("orgTree").style.visibility == "visible"){
			document.getElementById("orgTree").style.height = "0";
			document.getElementById('orgTree').style.visibility="hidden";      //关闭树型菜单
		}
	}
	
		function closeTree(objTxt,objTree){	  
		   var obj = event.srcElement;
		   if(obj!=objTxt && obj!=objTree){
		     
		     objTree.style.height = "0";
		     objTree.style.visibility="hidden";      //关闭树型菜单
		   }
		}
		
		//距离文本框的水平相对位置
		function getObjectLeft(e)   
		{   
			var l=e.offsetLeft;   
			while(e=e.offsetParent)   
				l += e.offsetLeft;   
			return   l;   
		}   
		//距离文本框的垂直相对位置
		function getObjectTop(e)   
		{   
			var t=e.offsetTop;   
			while(e=e.offsetParent)   
				t += e.offsetTop;   
			return   t;   
		}
		</script>
	</head>
	<body style="TEXT-ALIGN: center">
			
			<html:form action="/afreportDefine" method="POST">
			<input type="hidden" name="templateType" value="<bean:write  name="form"  property="templateType"/>"/>
			<div align="center">
				<table border="0" width="98%">
					<tr>
						<td height="10">
						</td>
					</tr>
					<tr>
						 <td>
						 	当前位置 &gt;&gt; 报表管理 &gt;&gt; 报表定义管理
						 </td>
					</tr>
					<tr>
						<td height="10"> 
						</td>
					</tr>
				</table>
				<table border="0" width="98%">	
					<TR>
						<TD width="20%" align="right">
							报表名称：
						</TD>
						<TD width="20%" align="left">
							<html:text property="templateName" size="20"  styleClass="input-text"/>
						</TD>
						<td width="30%" align="left">
							<html:submit value="查  询" styleClass="input-button"/>
						</td>
						<td width="30%" align="center">
							<input type="button" name="addTemplateType" value="增加报表信息" onclick="javascript:addreport()" class="input-button">
							<%--<logic:equal value="admin" name="Operator" property="userName" scope="session">
								<input type="button" style="visibility:visble" value="导入全部校验信息" onclick="javascript:window.location='utilAction.do';" class="input-button" />
							</logic:equal>--%>
						</td>
					</TR>
				</table>
			</div>
				<TABLE cellSpacing="0" width="95%" border="0" align="center" cellpadding="4">
				<br>
					<TR>
						<TD>
							<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0"  class="tbcolor">
								<tr class="titletab">
									<th colspan="8" align="center" id="list">
										<strong>
											报表列表
										</strong>
									</th>
								</tr>
								<TR class="middle">
									<TD class="tableHeader" width="8%">
										<b>报表编号</b>
									</td>
									<TD class="tableHeader" width="37%">
										<b>报表名称</b>
									</TD>
									<TD class="tableHeader" width="7%">
										<b>版本号</b>
									</TD>
			
									<TD class="tableHeader" width="8%">
										<b>起始时间</b>
									</td>
									<TD class="tableHeader" width="8%">
										<b>结束时间</b>
									</td>
									<TD class="tableHeader" width="5%">
										<b>是否发布</b>
									</td>
									<TD class="tableHeader" width="12%">
										<b>详细操作</b>
									</TD>									
								</TR>
									<logic:present name="Records" scope="request">
											<logic:iterate id="item" name="Records">
												<TR bgcolor="#FFFFFF">
													<td align="center"><bean:write name="item" property="templateId"/></td>
													<TD align="center">
														<logic:notEmpty name="item" property="templateName">
															<bean:write name="item" property="templateName"/>
														</logic:notEmpty>
														<logic:empty name="item" property="templateName">
															无
														</logic:empty>
													</TD>
													
													<td align="center"><bean:write name="item" property="versionId"/></td>
													
													<td align="center"><bean:write name="item" property="startDate"/></td>
													<td align="center"><bean:write name="item" property="endDate"/></td>
													<td align="center">
														<logic:equal name="item" property="usingFlag" value="<%=String.valueOf(Config.IS_PUBLIC)%>">
															<a href="javascript:updateunState('<bean:write name='item' property='templateId'/>','<bean:write name='item' property='versionId'/>')"  alt="未发布"><font color="blue">已发布</font></a>
														</logic:equal>
														<logic:notEqual name="item" property="usingFlag" value="<%=String.valueOf(Config.IS_PUBLIC)%>">															
															<a href="javascript:updateState('<bean:write name='item' property='templateId'/>','<bean:write name='item' property='versionId'/>')"  alt="发布">未发布</a>
														</logic:notEqual>
													</td>
													<TD align="center">
														<a href="<%=request.getContextPath()%>/viewAFTemplateDetail.do?templateId=<bean:write name="item" property="templateId"/>&templateName=<bean:write name="item" property="templateName"/>&versionId=<bean:write name="item" property="versionId"/>&bak2=1">查看</a>
														<a href="<%=request.getContextPath()%>/viewAFTemplateDetail.do?templateId=<bean:write name="item" property="templateId"/>&templateName=<bean:write name="item" property="templateName"/>&versionId=<bean:write name="item" property="versionId"/>&bak2=2">修改</a>
														<a href="javascript:deletetemplate('<bean:write name='item' property='templateId'/>','<bean:write name='item' property='versionId'/>')" >删除</a>
													</TD>
												</TR>
											</logic:iterate>
									</logic:present>
									<logic:notPresent name="Records" scope="request">
										<tr bgcolor="#FFFFFF">
											<td colspan="8">
												暂无记录
											</td>
										</tr>
									</logic:notPresent>
								</TABLE>
									<table cellSpacing="1" cellPadding="4" width="100%" border="0">
										<tr>
											<TD colspan="7" bgcolor="#FFFFFF">
											<jsp:include page="../../apartpage.jsp" flush="true">
												<jsp:param name="url" value="../../afreportDefine.do" />
											</jsp:include>
											</TD>
										</tr>
									</table>
						</TD>
					</TR>
				</TABLE>	
	   
			</html:form>	
			
	</body>
	<script language="javascript">
		function updateState(child,version){
			if(confirm("是否将模板置为发布?")){
				window.location="<%=request.getContextPath()%>/afreportDefine.do?child="+child+"&version="+version;
			}
		}
		function updateunState(child,version){
			if(confirm("是否将模板置为未发布?")){
				window.location="<%=request.getContextPath()%>/afreportDefine.do?fabuflg=1&child="+child+"&version="+version;
			}
		}
		function deletetemplate(child,version){
			if(confirm("是否删除该模板？")){
				window.location="<%=request.getContextPath()%>/viewAFTemplateDetail.do?templateId="+child+"&versionId="+version+"&bak2=3";
			}
		}
	</script>
</html:html>
