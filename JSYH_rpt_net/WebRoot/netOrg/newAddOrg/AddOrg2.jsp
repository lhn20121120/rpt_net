<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
	<html:base />
	<title>机构列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
<link href="<%=request.getContextPath() %>/css/common.css" type="text/css" rel="stylesheet">
<%
String orgType=request.getParameter("orgType");
String curPage=request.getParameter("curPage");
String preOrgId=request.getParameter("preOrgId");
com.fitech.net.form.OrgNetForm orgForm=com.fitech.net.adapter.StrutsOrgNetDelegate.selectOne(preOrgId,true);			
%>

<script>

function newAddOrg(){
  window.parent.location.assign("<%=request.getContextPath()%>/insertOrgNet.do");
}

	function _submit(form){		    
			if(form.org_id.value.Trim()==""){
				alert("请输入机构编码！");
				form.org_id.focus();
				return false;
			}
			if(CheckNumber(form.org_id.value.Trim()) == false)  //非法字符
			{
				alert("对不起,机构编码只能是数字或字母!");
				form.org_id.focus();
				return false;
			}
		 
			if(form.org_id.value.Trim().indexOf("*!*")>=0)  //非法字符
			{
				alert("对不起,机构编码不能包含 *!*  ！");
				form.org_id.focus();
				return false;
			}
			
			if(form.org_name.value.Trim()==""){
				alert("请输入机构名称！");
				form.org_name.focus();
				return false;
			}
			
		//检查
			function Check( reg, str ){
				if( reg.test( str ) ){
					return true;
				}
				return false;
			}
			// 检查数字
			function CheckNumber( str ){
				// var reg = /^\d*(?:$|\.\d*$)/;
			     var reg = /^[A-Za-z0-9]+$/;
				return Check( reg, str );
			}
		}
		
		function _back()
		{
			window.location.href='<%=request.getContextPath()%>/org/selectOrgNet.do?curPage=<%=curPage%>';
		}
		String.prototype.Trim=function(){
			return this.replace(/(^\s*)|(\s*$)/g,"");
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
<html:form action="/insertOrgNet" method="post" onsubmit="return _submit(this)">

	<table cellSpacing="1" cellPadding="4" width="100%" border="0" align="center" bgcolor="#ffffff" class="tbcolor">
		<TR class="tbcolor1">
			<th align="center" height="30" colspan="2">
				<span style="FONT-SIZE: 11pt"> 新增机构</span>
			</th>
		</TR>

		<TR align="center" valign="middle" bgcolor="#FFFFFF">
			<TD>
				<div style="width:100%; height:350;background-color:#f5f5f5;border :1px solid Silver; overflow:auto;">

					<table border="0" width="100%" align="center" cellspacing="1" cellpadding="7">
						<TR>
							<TD align="right">
								机构编码：
							</TD>
							<TD>
								<html:text property="org_id" size="20" styleClass="input-text" maxlength="17" />
							</TD>
						</TR>
						<TR>
							<TD align="right">
								机构名称：
							</TD>
							<TD>
								<html:text property="org_name" size="20" styleClass="input-text" maxlength="20" />
							</TD>
						</TR>

						<TR>
							<TD align="right">
								机构类型：
							</TD>
							<TD>
							<%if(orgType!=null&&orgType.equals("2")){ %>
							一级分行 
							<%}; if(orgType!=null&&orgType.equals("3")){ %>
							二级分行 
							<%};%>
							</TD>
						</TR>

						<TR>
							<TD align="right">
								上级机构：
							</TD>
							<TD>
								<%=orgForm.getOrg_name() %>
							</TD>
						</TR>			
<input type=hidden name=region_id value='104'/>
<input type=hidden name=curPage value='<%=curPage %>'/>
<input type=hidden name=org_type_id value='<%=orgType %>'/>
<input type=hidden name=pre_org_id value='<%=preOrgId %>'/>
						
						<TR>
							<TD align="center" colspan="2">
								<html:submit styleClass="input-button" value="确定" />
								&nbsp;

								<input type="button" class="input-button" onclick="_back()" value="返回" />
							</TD>
						</TR>
					</table>
				</div>
			</TD>
		</TR>
	</table>
</html:form>
</body>
</html:html>


