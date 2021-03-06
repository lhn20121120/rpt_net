<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm"/>
<jsp:useBean id="utilFormOrg" scope="page" class="com.cbrc.org.form.UtilForm" />
<%
	String orgSrhName="",orgClsId="",curPage="",where="";
	
	if(request.getAttribute("OrgSrhName")!=null){
		orgSrhName=(String)request.getAttribute("OrgSrhName");
		where+=(where.equals("")?"":"&") + "orgSrhName=" + orgSrhName;
	}else{
		if(request.getParameter("orgSrhName")!=null){
			orgSrhName=(String)request.getParameter("orgSrhName");
			where+=(where.equals("")?"":"&") + "orgSrhName=" + orgSrhName;
		}
	}
	if(request.getParameter("orgClsId")!=null){
	 	orgClsId=(String)request.getParameter("orgClsId");
	 	where+=(where.equals("")?"":"&") + "OrgClsId=" + orgClsId;
	}
	if(request.getParameter("curPage")!=null){
		curPage=(String)request.getParameter("curPage");
		where+=(where.equals("")?"":"&") + "curPage=" + curPage;
	}
%>
<html:html locale="true">
<head>
	<html:base/>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript">
		/**
  		 * 返回事件
 		 */
		 function _back(){
			window.location="<%=request.getContextPath()%>/template/org/ViewOrg.do?" + 
				"orgSrhName=<%=orgSrhName%>" +
				"&orgClsId=<%=orgClsId%>" + 
				"&curPage=<%=curPage%>";
		 }
	</script>
</head>
<body bgcolor="#FFFFFF">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table border="0" cellpadding="0" cellspacing="0" width="96%">
		<tr><td height="5"></td></tr>
		<tr>
			<td>
				当前位置 >> 模板管理 >> 子行所属频度类别设定 >> 按子行类型设置
			</td>
		</tr>
	</table>

	<html:form method="post" action="/template/org/OrgClsOATSet">
	<TABLE cellSpacing="1" cellPadding="4" width="80%" border="0" class="tbcolor" align="center">
		<TR class="middle">
			<TD class="tableHeader" align="center">设置子行所属频度类别</td>
		</TR>
		<tr>
			<td  bgcolor="#FFFFFF">
				<table border="0" cellspacing="0" cellpadding="4" width="100%">
					<tr>
						<td>子行类别：</td>
						<td>
							<html:select property="orgClsId">
								<html:optionsCollection name="utilFormOrg" property="orgCls"/>
							</html:select>	
						</td>
					</tr>
					<tr>
						<td>频度类别：</td>
						<td>
							<html:select property="OATId">
								<html:optionsCollection name="utilForm" property="orgActuTypes"/>
							</html:select>				
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<input type="submit" value=" 确定 " class="input-button">&nbsp;
							<input type="button" value=" 返回 " class="input-button" onclick="_back()">
						</td>
					</tr>
					<input type="hidden" name="orgSrhName" value="<%=orgSrhName%>">
					<input type="hidden" name="orgClsId" value="<%=orgClsId%>">
					<input type="hidden" name="curPage" value="<%=curPage%>">
				</table>
			</td>
		</tr>
	</table>
	</html:form>
</body>
</html:html>
