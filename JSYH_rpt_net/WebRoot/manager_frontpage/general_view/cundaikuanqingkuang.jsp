<%@ page contentType="text/html;charset=gb2312"%>
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
		<title>��������</title>
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="Thu,   01   Dec   1900   16:00:00   GMT">
	</head>
	<body>
		<%
	//			Operator operator = null;
	//			if(session.getAttribute(Config.OPERATOR_SESSION_NAME)!=null)
	//			{
	//				operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_NAME);
	//			}
	//			else
	//				operator = new Operator();
					
				String imagePath = request.getContextPath()+"/temp/leading/"+"null";
				
			GraphDataSource gs  = (GraphDataSource)session.getAttribute("gs");
			
			Table tb = new Table();
				
			Table.initMap(gs,tb);
			
			StringBuffer param = new StringBuffer("");
			//���Ϊ������
			
			String saveAsName = "tempReport_.raq";
		

			%>
		<table width="98%" align="center">
			<tr>
				<td align="left">
					<img title="�������ͼ" src="<%=imagePath%>_cunkuanjiegouqingkuang.jpg?random=<%=random.nextDouble()%>" style="cursor:hand;" onclick="return showBigImg('<%=imagePath%>_cunkuanjiegouqingkuang','<%=random.nextDouble()%>')">
				</td>
			</tr>
			<tr>
				<td align="left">
					<img title="�������ͼ" src="<%=imagePath%>_cundaikuanbiandong.jpg?random=<%=random.nextDouble()%>" style="cursor:hand;" onclick="return showBigImg('<%=imagePath%>_cundaikuanbiandong','<%=random.nextDouble()%>')">
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
						params="<%=param.toString()%>" 
						submit="�ύ������" 
						needOfflineInput="false" 
						needImportExcel="false" 
						importExcelLabel="����excel" 
						saveAsName="<%=saveAsName.toString()%>" 
						needDirectPrint="false" 
						needScroll="false" 
						scrollWidth="99%"
						scrollHeight="99%">
					</report:html>
				</td>
				
			</tr>
		</table>
	<br>
	</body>
</html>


<script language=javascript ></script>