<%@ page language="java" pageEncoding="GB2312"%>
<%@ page import="com.cbrc.smis.common.ApartPage"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<link href="<%=request.getContextPath()%>css/common.css" type="text/css" rel="stylesheet">
<script language="javascript" src="../../js/func.js"></script>

<logic:present name="apartPage" scope="request">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td class="pages">
				共 <bean:write name="apartPage" property="count" /> 条记录&nbsp;
				当前第
				<bean:write name="apartPage" property="curPage" />
				/
				<bean:write name="apartPage" property="pages" />
				页
			</td>
			<td align="right" class="pages">
				<logic:equal name="apartPage" property="isCanBack" value="1">
					<a href="<bean:write name='apartPage' property='firstPageUrl'/>" class="apartpage">
						上一页<!-- <img src="<%=request.getContextPath()%>/image/firstpage.gif" /> --> 
					</a>
					<a href="<bean:write name='apartPage' property='prevPageUrl'/>" class="apartpage">
						起始页<!-- <img src="<%=request.getContextPath()%>/image/prevpage.gif" /> -->
					</a> 
				</logic:equal>

				<%
						ApartPage apartPage = (ApartPage) request
						.getAttribute("apartPage");
						if (apartPage != null) {
							int curPage = apartPage.getCurPage();
							int pages = apartPage.getPages();

							int fromIndex = 1;
							
							if((curPage-2) > 0 && pages > 5){
								
								if((curPage + 2) <= pages)
									fromIndex = curPage - 2;
								else if((curPage + 1) <= pages)
									fromIndex = curPage - 3;
								else if(curPage == pages)
									fromIndex = curPage - 4;
									
								if( fromIndex <= 0)
									fromIndex = 1;
							}
							if ( pages - fromIndex >= 5)
								pages = fromIndex + 4;
							for (int i = fromIndex; i <= pages; i++) {
								if ( i == curPage) {
							%>
							<span class="pages_red"> 
								<a	href="<bean:write name='url'/><%=i%>"><%=i%></a>
							 </span>
							<%
						} else {
							%>
								<a href="<bean:write name='url'/><%=i%>"><%=i%></a>
							<%
						}
					}
				}
				%>
				<logic:equal name="apartPage" property="isCanForward" value="1">
					<a href="<bean:write name='apartPage' property='nextPageUrl'/>" class="apartpage">
						下一页<!-- <img src="<%=request.getContextPath()%>/image/nextpage.gif" /> -->
					</a>
					<a href="<bean:write name='apartPage' property='lastPageUrl'/>" class="apartpage">
						最后页<!-- <img src="<%=request.getContextPath()%>/image/lastpage.gif" /> -->
					</a>
				</logic:equal>
				&nbsp;
	  		<%--转<input type="input" size="4" name="goPage" id="goPage">页&nbsp;
	  		<input type="button" value="GO" onclick="_go()"> --%>
			</td>
		</tr>
	</table>
	<script language="javascript">
	 var pages="<bean:write name='apartPage' property='pages'/>";	
	 /**
	  * 分页操作
	  */
	 function _apartPage(page){
	 	 window.location="<bean:write name='apartPage' property='term'/>" +
	 	   "&curPage=" + page; 
	 }
	</script>
</logic:present>
	<logic:notPresent name="apartPage" scope="request">
	<table width="99%" border="0" align="center" cellpadding="2" cellspacing="1">
		<tr>
			<td width="30%">
				共<span class="apartpage_span">0</span>条记录 
			</td>
		</tr>
	</table>
</logic:notPresent>

