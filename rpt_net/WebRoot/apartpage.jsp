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

  <logic:present name="ApartPage" scope="request">
			<table width="100%" border="0" align="center" cellpadding="2" cellspacing="1">
				<tr>
					<td>
						��<span class="apartpage_span"><bean:write name="ApartPage" property="count"/></span>����¼
							&nbsp;��<span class="apartpage_span"><bean:write name="ApartPage" property="curPage"/></span>/<span class="apartpage_span"><bean:write name="ApartPage" property="pages"/></span>ҳ
					</td>
					<td align="right">
						<logic:equal name="ApartPage" property="isCanBack" value="1">
							<a href="<%=url%><bean:write name='ApartPage' property='firstPageUrl'/>" class="apartpage">��ҳ</a>&nbsp;
							<a href="<%=url%><bean:write name='ApartPage' property='prevPageUrl'/>" class="apartpage">��һҳ</a>&nbsp;
						</logic:equal>
						<logic:equal name="ApartPage" property="isCanForward" value="1">
							<a href="<%=url%><bean:write name='ApartPage' property='nextPageUrl'/>" class="apartpage">��һҳ</a>&nbsp;
							<a href="<%=url%><bean:write name='ApartPage' property='lastPageUrl'/>" class="apartpage">βҳ</a>&nbsp;
						</logic:equal>
					</td>
					<!--td align="right" width="30%">
						ת<input type="text" size="4" name="curPage">ҳ&nbsp;
						<input name="�ύ" type="submit" value="GO">
					</td-->
				</tr>
			</table>
   </logic:present>   