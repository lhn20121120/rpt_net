<%@ page language="java" pageEncoding="GB2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%
  String url="";
  if(request.getParameter("url")!=null){
    String tmp=(String)request.getParameter("url");
    url=tmp + 
      (tmp.indexOf('?')<0?"?":(tmp.lastIndexOf('?')<tmp.length()-1?"&":""));
  }else{
	  url="?";
	}
%>

  <logic:present name="BpartPage" scope="request">
			<table width="100%" border="0" align="center" cellpadding="2" cellspacing="1">
				<tr>
					<td>
						共<span class="apartpage_span"><bean:write name="BpartPage" property="count"/></span>条记录
							&nbsp;第<span class="apartpage_span"><bean:write name="BpartPage" property="curPage"/></span>/<span class="apartpage_span"><bean:write name="BpartPage" property="pages"/></span>页
					</td>
					<td align="right">
						<logic:equal name="BpartPage" property="isCanBack" value="1">
							<a href="<%=url%><bean:write name='BpartPage' property='firstPageUrl'/>" class="apartpage">首页</a>&nbsp;
							<a href="<%=url%><bean:write name='BpartPage' property='prevPageUrl'/>" class="apartpage">上一页</a>&nbsp;
						</logic:equal>
						<logic:equal name="BpartPage" property="isCanForward" value="1">
							<a href="<%=url%><bean:write name='BpartPage' property='nextPageUrl'/>" class="apartpage">下一页</a>&nbsp;
							<a href="<%=url%><bean:write name='BpartPage' property='lastPageUrl'/>" class="apartpage">尾页</a>&nbsp;
						</logic:equal>
					</td>
					<!--td align="right" width="30%">
						转<input type="text" size="4" name="curPage">页&nbsp;
						<input name="提交" type="submit" value="GO">
					</td-->
				</tr>
			</table>
   </logic:present>   