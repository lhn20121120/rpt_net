<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config"/>
<html:html locale="true">
	<head>
	<html:base/>
    <title>取数公式指标设定 </title>    
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
 	<link href="../../css/common.css" rel="stylesheet" type="text/css">
    
    <script language="javascript">
	    function goAddPage()
	    {
	    	window.location.href="<%=request.getContextPath()%>/template/add/addEtlIndex.jsp";
	    	
	    //	window.location.href="vparameterAdd.jsp";
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
	     * @param vpId 参数表ID
	     */
	     function _edit(indexName){
	     	window.location="EditEtlIndex.do?indexName=" + indexName + "&curPage=" + curPage;
	     }
	     //删除提示
	     function _delete(indexName)
	     {
	     	if(confirm("你确认要删除该指标公式吗？"))
	     		window.location = "deleteEtlIndex.do?indexName=" + indexName;	
	     		
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
    <table border="0" width="90%" align="center">
		<tr><td height="8"></td></tr>
		<tr>
			 <td>当前位置 >> 	参数设定 >> 取数公式指标设定</td>
		</tr>
		<tr><td height="10"></td></tr>
	</table>
	<html:form method="post" action="template/ViewTargetFormual">
	<TABLE border="0" width="90%" align="center" cellpadding="0" cellspacing="0">
		<TR>
			<TD align="left" valign="right">名称：</TD>
			<TD align="left" valign="middle">
				<html:text property="indexName" size="40" styleClass="input-text" />
			</TD>
			<TD align="center" valign="middle">
				<input type="submit" class="input-button" value="查询">&nbsp;
				<input type="button" class="input-button" value="新增" onclick="goAddPage()"/>
			</TD>
		</TR>
		
		<tr><td colspan="7" height="10"></td></tr>
	</TABLE>
		<TABLE border="0" width="90%" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
			<TR class="tableHeader">
				<TD width="5%" align="center" valign="middle">
					序号
				</TD>				
				<TD width="15%" align="center" valign="middle">
					指标名
				</TD>
				<TD width="50%" align="center" valign="middle">
					指标值
				</TD>
				<TD width="20%" align="center" valign="middle">
					指标描述
				</TD>
				<TD width="10%" align="center" valign="middle">
					操作
				</TD>
			</TR>
			<logic:present name="Records" scope="request">
				<logic:iterate id="etlIndex" name="Records" indexId="index">
					<TR bgcolor="#FFFFFF">
						<TD align="center" valign="bottom">
							<%=((Integer)index).intValue() + 1%>
						</TD>						
						<TD align="center" valign="bottom">
							<FONT size="2">
							<bean:write name="etlIndex" property="indexName"/>
							</FONT>
						</TD>
						<TD align="left" valign="bottom">
							<FONT size="2">
							<bean:write name="etlIndex" property="formual"/>
							</FONT>
						</TD>
						<TD align="left" valign="bottom">
							<FONT size="2">
							<bean:write name="etlIndex" property="desc"/>
							</FONT>
						</TD>
						<TD align="center" valign="bottom">
							<FONT size="2">
								<a href="javascript:_edit('<bean:write name='etlIndex' property='indexName'/>')">修改</a>&nbsp;&nbsp;
								<a href="javascript:_delete('<bean:write name="etlIndex" property="indexName"/>')">删除</a>
							</FONT>
						</TD>
					</TR>
			</logic:iterate>
		</logic:present>
		
		<logic:notPresent name="Records" scope="request">
			<tr align="left">
				<td bgcolor="#ffffff" colspan="7">无匹配记录</td>
			</tr>
		</logic:notPresent>
      </html:form>
    </TABLE>
	<TABLE align="center" border="0" width="90%">
		<TR>
			<TD>
				<jsp:include page="../../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../ViewTargetFormual.do"/>
				</jsp:include>
			</TD>
		</TR>
	</TABLE>
  </body>
</html:html>