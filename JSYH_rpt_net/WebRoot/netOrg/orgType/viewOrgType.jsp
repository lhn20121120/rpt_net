<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config"/>
<html:html locale="true">
	<head>
	<html:base/>
    <title>机构类型设定</title>    
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
    <link href="../../css/common.css" type="text/css" rel="stylesheet">
    
    <script language="javascript">
	    function goAddPage(){
	    	window.location.href="<%=request.getContextPath()%>/netOrg/orgType/orgTypeAdd.jsp";
	    }
	    
	    <logic:present name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="<bean:write name='ApartPage' property='curPage'/>";
	    </logic:present>
	    <logic:notPresent name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="1";
	    </logic:notPresent>
	    
	    /**
	     * 修改事件
	     *
	     * @param orgTypeId 机构类型ID
	     */
	     function _edit(orgTypeId){
	     	window.location="editOrgType.do?org_type_id=" + orgTypeId + "&curPage=" + curPage;
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
  <%
  com.cbrc.smis.security.Operator operator = (com.cbrc.smis.security.Operator) session.getAttribute(com.cbrc.smis.common.Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
  com.fitech.net.form.OrgNetForm orgForm=com.fitech.net.adapter.StrutsOrgNetDelegate.selectOne(operator.getOrgId(),true);
  pageContext.setAttribute("orgForm",orgForm);
  %>
    <table border="0" width="90%" align="center">
		<tr><td height="8"></td></tr>
		<tr>
			 <td>当前位置 >> 	机构管理 >> 机构类型设定</td>
		</tr>
		<tr><td height="10"></td></tr>
	</table>
	<html:form method="post" action="/viewOrgType">
	<TABLE border="0" width="80%" align="center" cellpadding="0" cellspacing="0">
		<TR>
			<TD align="left" valign="middle">关键字：</TD>
			<TD align="left" valign="middle">
				<html:text property="org_type_name" size="40" styleClass="input-text" />
			</TD>
			<TD align="right" valign="middle">
				<input type="submit" class="input-button" value="查询">
			</TD>
		</TR>
		<tr><td colspan="3" height="10"></td></tr>
	</TABLE>
  </html:form>
		<TABLE border="0" width="80%" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
			<TR class="tableHeader">
				<TD width="50" align="center" valign="middle">
					序号
				</TD>
				<TD width="350" align="center" valign="middle">
					机构类型名称
				</TD>
<%--				<TD width="100" align="center" valign="middle">--%>
<%--					操作--%>
<%--				</TD>--%>
			</TR>
			<logic:present name="Records" scope="request">
				<logic:iterate id="viewOrgType" name="Records" indexId="index">
					<TR bgcolor="#FFFFFF">
						<TD align="center" valign="bottom">
						<%=((Integer)index).intValue() + 1%>
						</TD>
						<TD align="center" valign="bottom">
							<FONT size="2">
							<bean:write name="viewOrgType" property="org_type_name"/>
							</FONT>
						</TD>
<%--						<TD align="center" valign="bottom">--%>
<%--							<FONT size="2">--%>
<%--								<a href="javascript:_edit(<bean:write name='viewOrgType' property='org_type_id'/>)">修改</a>--%>
<%--							</FONT>--%>
<%--						</TD>--%>
					</TR>
			</logic:iterate>
		</logic:present>
		
		<logic:notPresent name="Records" scope="request">
			<tr align="left">
				<td bgcolor="#ffffff" colspan="5">
						无匹配记录
				</td>
			</tr>
		</logic:notPresent>

    </TABLE>
	<TABLE align="center" border="0" width="80%">
		<TR>
			<TD>
				<jsp:include page="../../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../../viewOrgType.do"/>
				</jsp:include>
			</TD>
		</TR>
	</TABLE>
  </body>
</html:html>