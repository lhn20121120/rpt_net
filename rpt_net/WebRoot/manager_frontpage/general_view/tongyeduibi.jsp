<%@ page contentType="text/html;charset=gb2312"%>

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
		<title>存贷款情况</title>
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="Thu,   01   Dec   1900   16:00:00   GMT">
	</head>
	<body>
		<%OperatorLead operator = new OperatorLead();

			String imagePath = request.getContextPath() + "/temp/leading/" + operator.getSessionId();
			
			GraphDataSource gs  = (GraphDataSource)session.getAttribute("gs");
			
			Table tb = new Table();
				
			Table.initMap(gs,tb);
			
			StringBuffer param = new StringBuffer("");
			//另存为的名称
			
			
			
			%>
		<br>
		<br>
		<table width="98%" align="center">
			<tr>
				<td width="100%" vAlign="top">
					<img title="点击看大图" src="<%=imagePath%>_cunkuantongyeduibi.jpg?random=<%=random.nextDouble()%>" style="cursor:hand;" onclick="return showBigImg('<%=imagePath%>_cunkuantongyeduibi','<%=random.nextDouble()%>')">
				</td>
			</tr>
			<tr>
				<td width="100%" vAlign="top">
					<img title="点击看大图" src="<%=imagePath%>_daikuantongyeduibi.jpg?random=<%=random.nextDouble()%>" style="cursor:hand;" onclick="return showBigImg('<%=imagePath%>_daikuantongyeduibi','<%=random.nextDouble()%>')">
				</td>
			</tr>
			<tr>
				<td height="250" align="left">
					<report:html 
						name="report1" 
						beanName="rdName" 
						srcType="defineBean" 
						funcBarLocation="top" 
						needPageMark="false" 
						functionBarColor="#12632256" 
						funcBarFontFace="宋体" 
						funcBarFontSize="12pt" 
						funcBarFontColor="#808040" 
						needSaveAsExcel="false"
						needSaveAsPdf="false" 
						needSaveAsText="false" 
						needSaveAsWord="false" 
						printLabel="" 
						separator="|" 
						needPrint="false" 
						pageMarkLabel="页号{currpage}/{totalPage}" 
						generateParamForm="no" 
						displayNoLinkPageMark="false" 
						canModifyBeforePrint="false"
						params="<%=param.toString()%>" 
						submit="" 
						needOfflineInput="false" 
						needImportExcel="false" 
						importExcelLabel="导入excel" 
					
						needDirectPrint="false" 
						needScroll="false" 
						scrollWidth="99%"
						scrollHeight="99%">
					</report:html>
				</td>
				
			</tr>
		</table>
	</body>
</html>


<script language=javascript ></script>