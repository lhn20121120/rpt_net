<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.cbrc.smis.util.FitechUtil"%>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config"/>
<%
	String templateId="",versionId="",reportName="",reportStyle="";
	if(request.getParameter("templateId")!=null) templateId=(String)request.getParameter("templateId");
	if(request.getParameter("versionId")!=null) versionId=(String)request.getParameter("versionId");

	if(request.getParameter("reportName")!=null) reportName=FitechUtil.getParameter(request,"reportName");
	if(com.cbrc.smis.common.Config.WEB_SERVER_TYPE==1){
		 reportName=new String(reportName.getBytes("iso-8859-1"), "gb2312");
	 }
	if(request.getParameter("reportStyle")!=null) reportStyle=(String)request.getParameter("reportStyle");
%>
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.ColNumFormUtil" />
<jsp:setProperty property="templateId" name="FormBean" value="<%=templateId%>"/>
<jsp:setProperty property="versionId" name="FormBean" value="<%=versionId%>"/>
<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript" src="../../js/prototype-1.4.0.js"></script>
	<script type="text/javascript">

		 
		 //表单提交验证	 
		 function _submit(form)
		 {
		
			if(form.formuValue.value=="")
			{
				alert("请输入校验公式！");
				form.reportName.focus();
				return false;
			}


			return true;
		 }
 			/**
			  * 返回事件
			  * 1.14 yql
			  */
			  function _back(){

			  	var form=document.getElementById("form1");
			  	window.location="<%=request.getContextPath()%>/gznx/viewQDValidateList.do?templateId=" + form.elements['templateId'].value +
			  		"&versionId=" + form.elements['versionId'].value ;
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
	<html:form method="post" action="/gznx/addQDValidate"  styleId="form1" onsubmit="return _submit(this)">
		<input type="hidden" name="templateId" value="<%=templateId%>">
		<input type="hidden" name="versionId" value="<%=versionId%>">
		<input type="hidden" name="reportName" value="<%=reportName%>">
		<input type="hidden" name="reportStyle" value="<%=reportStyle%>">
		<table border="0" cellspacing="0" cellpadding="4" width="100%" align="center">
			<tr><td height="8"></td></tr>
			<tr>
				<td>
					当前位置 >> 报表管理 >> 新增校验公式
				</td>
			</tr>
			<tr><td height="10"></td></tr>		
		</table>
		<table cellpadding="4" cellspacing="1" border="0" width="100%" align="center" class="tbcolor">
			<tr class="titletab">
				<th align="center">
					新增报表模板
				</th>
			</tr>
			<tr align="center" bgcolor="#ffffff">
				<td align="center">
					<table cellspacing="2" cellpadding="2" border="0" width="90%" align="center">
					<TR bgcolor="#ffffff">
							<TD>
								校验类型：
							</TD>
							<TD>
								<html:select property="formuType" styleId="formuType">
								<html:option value="1">表内单一校验</html:option>
								<html:option value="2">表内组合校验</html:option>
								<html:option value="3">表间组合校验</html:option>
								</html:select>
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								列号：
							</TD>
							<TD>
								<html:select property="colNum" styleId="colNum">
									<html:optionsCollection name="FormBean" property="colNumList"/>
								</html:select>
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								列名：
							</TD>
							<TD>
								<html:text property="colName" size="20" maxlength="100" styleClass="input-text" styleId="colName" />
							</TD>
						</TR>
						
						<TR bgcolor="#ffffff">
							<TD>
								校验名称：
							</TD>
							<TD>
								<html:text property="formuName" size="30" maxlength="100" styleClass="input-text" styleId="formuName" />
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								校验公式：
							</TD>
							<TD>
								<html:text property="formuValue" size="30" maxlength="100" styleClass="input-text" styleId="formuValue" />
							</TD>
						</TR>
						<TR bgcolor="#ffffff">
							<TD>
								公式说明：
							</TD>
							<TD>
								<html:textarea property="formuDes" cols="80" rows="10" ></html:textarea>
							</TD>
						</TR>

						<TR bgcolor="#ffffff">
							<TD>
								出错提示信息:
							</TD>
							<TD>
								<html:textarea property="validateMessage" cols="80" rows="10" ></html:textarea>								
							</TD>
						</TR>
					
						<TR>
							<TD align="center" colspan="2">
								<input type="submit" name="save" value="保存">
								<input type="button" value="返回" class="input-button" onclick="_back()">
							</TD>
						</TR>
					</table>
				</td>
			</tr>
		</table>
	</html:form>
</body>
</html:html>
