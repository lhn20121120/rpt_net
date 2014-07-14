<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="org.jfree.chart.*"%>
<%@ page import="org.jfree.chart.JFreeChart"%>
<%@ page import="org.jfree.chart.plot.*"%>
<%@ page import="org.jfree.chart.servlet.ServletUtilities"%>
<%@ page import="org.jfree.chart.entity.StandardEntityCollection"%>
<%@ page import="org.jfree.data.time.*"%>
<%@ page import="org.jfree.chart.renderer.xy.*"%>
<%@ page import="org.jfree.chart.axis.*"%>
<%@ page import="org.jfree.chart.plot.XYPlot"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.io.*"%>
<%@ page import="java.awt.Font"%>
<%@ page import="org.jfree.chart.title.TextTitle"%>
<%@ page import="java.awt.Color"%>
<%@ page import="com.fitech.net.bean.Figure"%>
<%@ page import="com.fitech.net.figure.WarnFigure"%>
<%@ page import="com.fitech.net.figure.Config"%>
<%@ page import="java.util.HashMap"%>


<%//��������

			String year = request.getParameter("year");
			String startMonth = request.getParameter("startMonth");
			String endMonth = request.getParameter("endMonth");
			String[] yearArr = null;
			int yearLength = 1;
			if (year != null && startMonth != null && endMonth != null) {
				yearArr = year.split(",");
				yearLength = yearArr.length;
			} else {
				year = "";
				startMonth = "1";
				endMonth = "12";
			}
			int max = Integer.parseInt(endMonth);
			int min = Integer.parseInt(startMonth);
			int maxMonth = (max > min) ? max : min;
			int minMonth = (max > min) ? min : max;
			max = maxMonth;
			min = minMonth;
			Figure fg = (Figure) request.getAttribute("figure");
			String title = fg.getTargetDefineName();
			String subtitleStr = title.split(Config.SPLIT_TITLE)[0] + "ָ�����";

			String allWarnMessageColor = fg.getAllWarnMessage();
			float currentValue = Float.parseFloat(fg.getCurrentValue());
			if (title.split(Config.SPLIT_TITLE)[1].equals("1"))
				title = title.split(Config.SPLIT_TITLE)[0] + "ָ��  ָ��ֵ������ʾ��";
			else
				title = title.split(Config.SPLIT_TITLE)[0] + "ָ��  ������ֵ������ʾ��";
			
			WarnFigure pie = new WarnFigure(title, allWarnMessageColor,currentValue);
			

			//����ͼ�ζ���
			JFreeChart chart = pie.initial();
			//����ͼƬ����ɫ
			chart.setBackgroundPaint(java.awt.Color.white);
			//��ȡ�����ʾ
			String outPrintInf = pie.getOutPrintInf();
			//����ͼƬ��������
			Font font = new Font("����", Font.CENTER_BASELINE, 20);
			TextTitle _title = new TextTitle(title);
			_title.setFont(font);
			chart.setTitle(_title);

			//�����ɵ�ͼƬ�ŵ���ʱĿ¼
			StandardEntityCollection sec = new StandardEntityCollection();
			ChartRenderingInfo info = new ChartRenderingInfo(sec);

			//500��ͼƬ���ȣ�300��ͼƬ�߶ȣ�session ΪHttpSession����
			String filename = ServletUtilities.saveChartAsPNG(chart, 600, 400,
					info, session);
			PlotRenderingInfo pf = info.getPlotInfo();
			//��ͼ
			PrintWriter pw = new PrintWriter(out);
			ChartUtilities.writeImageMap(pw, "map0", info, false);
			String graphURL = request.getContextPath()
					+ "/servlet/DisplayChart?filename=" + filename;
					// System.out.println(filename);
			pw.flush();

			//����ͼչʾ
			HashMap hmap = (HashMap) request.getAttribute("resultMap");
		    Integer freq=(Integer)request.getAttribute("freq");
		    String domain ="";
		//    boolean flag=false;
		    if(freq!=null){
		    	if(freq.intValue()==2){
		    		domain="��������";
		    	//	flag=true;
		    	}
		    	else if(freq.intValue()==1){
		    		domain = "�·�����";	
		    	}
		    }
			String title2 = "ָ�����Ԥ������ͼ";
			String range = "ָ��ֵ(%)";
			org.jfree.data.time.TimeSeries ca[] = new org.jfree.data.time.TimeSeries[yearLength];
			org.jfree.data.time.TimeSeriesCollection dataset = new org.jfree.data.time.TimeSeriesCollection();
			if (hmap != null) {
				for (int ys = 0; ys < yearLength; ys++) {
					String st = yearArr[ys] + "�걨��";		
				//	if(flag)			
				//		ca[ys] = new org.jfree.data.time.TimeSeries(st, Quarter.class);
				//	else
						ca[ys] = new org.jfree.data.time.TimeSeries(st, Month.class);
				}
		//		if(flag){
		//			min=1;
		//			max=4;
		//		}
			
				for (int mon = min; mon < max + 1; mon++)			
				 {		
			 	   com.fitech.net.form.ActuTargetResultForm form = null;
					for (int yes = 0; yes < yearLength; yes++) {
						if (hmap.get(yearArr[yes] + mon) != null) {
							form = (com.fitech.net.form.ActuTargetResultForm) hmap.get(yearArr[yes] + mon);
						//	  if(flag)
						//	  		ca[yes].add(new Quarter(mon,2000), form.getTargetResult().doubleValue());	  							  
						//	   else
								    ca[yes].add(new org.jfree.data.time.TimeSeriesDataItem(new org.jfree.data.time.Month(mon,2000),form.getTargetResult()));															
					
						}else{
						//		if(flag)
						//	  		ca[yes].add(new Quarter(mon,2000),0);	  							  
						//	   else
								    ca[yes].add(new org.jfree.data.time.TimeSeriesDataItem(new org.jfree.data.time.Month(mon,2000),0));															
								
						}
						
					}
				}
			    for (int yeas = 0; yeas < yearLength; yeas++) {
					dataset.addSeries(ca[yeas]);
				}
			}

			
			JFreeChart chart2 = ChartFactory.createTimeSeriesChart(title2,
					domain, range, dataset, true, true, false);
			XYPlot plot = chart2.getXYPlot();
			DateAxis axis = (DateAxis) plot.getDomainAxis();	
		//	plot.getRangeAxis().setLabelAngle(Math.PI / 2);
			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
			renderer.setShapesVisible(false);
		//	if(flag)
		//		axis.setDateFormatOverride(new SimpleDateFormat("��"));	  							  
		//	else
				axis.setDateFormatOverride(new SimpleDateFormat("M��"));			
			axis.setAutoTickUnitSelection(false);			
			axis.setTickUnit(new DateTickUnit(DateTickUnit.MONTH, 1));
			
			TextTitle subtitle = new TextTitle(subtitleStr, new Font("����",
					Font.BOLD, 17));
			chart2.addSubtitle(subtitle);
			chart2.setTitle(new TextTitle(title2, new Font("����", Font.ITALIC,
					19)));
			chart2.setBackgroundPaint(new java.awt.GradientPaint(0, 0,
					Color.white, 0, 1000, Color.blue));
			String fileName = ServletUtilities.saveChartAsPNG(chart2, 800, 500,
					null, session);
			PrintWriter writer = new PrintWriter(out);

			String url = request.getContextPath()
					+ "/servlet/DisplayChart?filename=" + fileName;
			writer.flush();

			%>

<html>
	<head>
		<title>3D��ͼ</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=request.getContextPath()%>/css/common.css" rel="stylesheet" type="text/css">
	</head>

	<body leftmargin="2" topmargin="0" marginwidth="0" marginheight="0">
		<TABLE name="tbl" id="tbl" cellSpacing="1" cellPadding="1" width="98%" border="0" class="tbcolor" align="center">
			<tr id="tbcolor">
				<th align="center">

					<strong> <font face="Arial" size="2"> Ԥ����Ϣ </font> </strong>
				</th>
			</tr>
			<tr>
				<td align="right" bgcolor="#FFFFFF">
					<html:form action="/target/viewPreStandDetail" method="Post" styleId="form1">

						<table width="100%" border="0">

							<TR>
								<TD align="right">
									ָ������
								</TD>
								<TD>
									<html:text styleId="targetDefineName" property="targetDefineName" size="20" styleClass="input-text" readonly="true" />
								</TD>
								<TD align="right">
									&nbsp;
								</TD>

							</TR>
							<TR>
								<TD align="right">
									&nbsp;ָ��ֵ
								</TD>
								<TD>
									<html:text styleId="allWarnMessage" property="allWarnMessage" size="20" styleClass="input-text" readonly="true" />

								</TD>
								<TD align="right">
									����
								</TD>
								<TD>
									<html:text styleId="temp1" property="temp1" size="15" styleClass="input-text" style="text" readonly="true" />
								</TD>
							</TR>
							<tr>
								<td align="right">
									����
								</td>
								<td>
									<html:text styleId="temp2" property="temp2" size="20" styleClass="input-text" readonly="true" />
								</td>
								<td align="right">
									&nbsp;��ɫ
								</td>
								<TD>
									<logic:present name="actuTargetResultForm" property="color">
										<label style="background:<bean:write name="actuTargetResultForm" property="color"/>">
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										</label>
									</logic:present>
								</TD>
							</tr>
							<tr>
								<td align="right">
									��ʽ
								</td>
								<td colspan="3">
									<html:text property="preFormula" size="20" styleClass="input-text" style="width:79.5% " readonly="true" />
								</td>
							</tr>

						</table>

					</html:form>
				</td>
			</tr>
		</table>
		<br />
				<TABLE width="100%">
					<tr>
						<td align="center">
							<img src="<%=url%>" align="center" border=0 usemap="#<%=fileName %>" />
						</td>
					</tr>
				</TABLE>
				<br />
				<br />
				

<TABLE cellSpacing="0" width="90%" border="0" align="center" cellpadding="6" >
		 <TR>
			 <TD>
				<TABLE cellSpacing="1" cellPadding="6" width="90%" border="0" class="tbcolor"  align="center">
					<tr class="titletab">
						<th colspan="13" align="center" id="list">
							<strong>
								ָ��ֵ
							</strong>
						</th>
					</tr>

					<TR class="middle">
					<TD class="tableHeader" width="5%">
							<b>
								���
							</b>
						</TD>
					<% for(int mi=1;mi<=12;mi++) {%>
						<TD class="tableHeader" width="8%">
							<b>
								<%=mi%>��
							</b>
						</TD>
						<%} %>
					</TR>
					<%for (int k=0; k<yearLength;k++){ %>
					<TR bgcolor="#FFFFFF">
						<td>
							<%=yearArr[k] %>
						</td>
						<% for(int m=1;m<=12;m++) {%>
							<td>
							<%	
								 	   com.fitech.net.form.ActuTargetResultForm form = null;
											if (hmap.get(yearArr[k] + m) != null) {
												form = (com.fitech.net.form.ActuTargetResultForm) hmap.get(yearArr[k] + m);
												%>
														<%=form.getTargetResult()%>															
												<%
											}else{
											%>
												0													
											<%
											}
									%>
							</td>
						<%} %>
						</tr>
					<%} %>	
				</TABLE>
			</td>
		 </tr>
 
	 </TABLE>

				<%--<table width="90%" border="0" align="center"   >
					<tr>
						<td align="center" rowspan=11>
							<img src="<%=graphURL%>" align="center" border=0 usemap="#<%=filename %>" />
						</td>						
					</tr>				
					<%out.println(outPrintInf);%>
					<tr><th><br></th></tr>
					<tr><th><br></th></tr>
					<tr><th><br></th></tr>
					<tr><th><br></th></tr>
				
					
				
					
				</table>
	--%></body>
</html>
