<%@ page contentType="text/html;charset=gb2312" errorPage=""%>

<%@ page session="true" import="java.lang.StringBuffer"%>
<%@ page import="com.fitech.gznx.common.Config"%>
<%@ page import="com.fitech.gznx.security.OperatorLead"%>
<%@ page import="com.fitech.gznx.graph.Table"%>
<%@ page import="com.fitech.gznx.graph.GraphDataSource"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="random" scope="page" class="java.util.Random" />
<html>
	<head>
		<title>���������</title>

	</head>
	<body>
		<%OperatorLead operator =  new OperatorLead();
			String imagePath = request.getContextPath() + "/temp/leading/" + operator.getSessionId();
			GraphDataSource gs = (GraphDataSource) session.getAttribute("gs");

			Table tb = new Table();

			Table.initMap(gs, tb);
			StringBuffer param = new StringBuffer();
			String saveAsName = "";

			
%>
		<table width="98%" align="center">
			<tr>
				<td>
					<img title="�������ͼ" src="<%=imagePath%>_liudongxingbiandong.jpg?random=<%=random.nextDouble()%>" style="cursor:hand;" onclick="return showBigImg('<%=imagePath%>_liudongxingbiandong','<%=random.nextDouble()%>')">
				</td>
				<td>
					&nbsp;&nbsp;
				</td>
				<td>
				</td>
			</tr>
		</table>
		<br>
		<table width="98%" align="center">
			<tr>
				<td vAlign="top" align="center">
					<report:html name="report1" 
						beanName="rdName" 
						srcType="defineBean" 
						funcBarLocation="top" 
						needPageMark="yes" 
						functionBarColor="#12632256" 
						funcBarFontFace="����" 
						funcBarFontSize="12pt" 
						funcBarFontColor="#808040" 
						needSaveAsExcel="false"
						needSaveAsPdf="false" 
						needSaveAsText="false" 
						needSaveAsWord="false" 
						printLabel="" 
						separator="|" 
						needPrint="false" 
						pageMarkLabel="ҳ��{currpage}/{totalPage}" 
						generateParamForm="no" 
						displayNoLinkPageMark="false" 
						canModifyBeforePrint="false"
						params="<%=param.toString() %>" 
						submit="�ύ������" 
						needOfflineInput="yes" 
						needImportExcel="false" 
						importExcelLabel="����excel" 
						saveAsName="<%=saveAsName.toString()%>" 
						needDirectPrint="false" 
						needScroll="yes" 
						scrollWidth="99%" 
						scrollHeight="99%"></report:html>
				</td>
			</tr>
		</table>
	</body>
</html>

<script language=javascript ></script>