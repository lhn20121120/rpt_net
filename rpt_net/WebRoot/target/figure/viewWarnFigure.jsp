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


<%//传递数据

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
			String subtitleStr = title.split(Config.SPLIT_TITLE)[0] + "指标分析";

			String allWarnMessageColor = fg.getAllWarnMessage();
			float currentValue = Float.parseFloat(fg.getCurrentValue());
			if (title.split(Config.SPLIT_TITLE)[1].equals("1"))
				title = title.split(Config.SPLIT_TITLE)[0] + "指标  指标值分配显示表";
			else
				title = title.split(Config.SPLIT_TITLE)[0] + "指标  比上期值分配显示表";
			
			WarnFigure pie = new WarnFigure(title, allWarnMessageColor,currentValue);
			

			//创建图形对象
			JFreeChart chart = pie.initial();
			//设置图片背景色
			chart.setBackgroundPaint(java.awt.Color.white);
			//获取输出提示
			String outPrintInf = pie.getOutPrintInf();
			//设置图片标题属性
			Font font = new Font("黑体", Font.CENTER_BASELINE, 20);
			TextTitle _title = new TextTitle(title);
			_title.setFont(font);
			chart.setTitle(_title);

			//把生成的图片放到临时目录
			StandardEntityCollection sec = new StandardEntityCollection();
			ChartRenderingInfo info = new ChartRenderingInfo(sec);

			//500是图片长度，300是图片高度，session 为HttpSession对象
			String filename = ServletUtilities.saveChartAsPNG(chart, 600, 400,
					info, session);
			PlotRenderingInfo pf = info.getPlotInfo();
			//作图
			PrintWriter pw = new PrintWriter(out);
			ChartUtilities.writeImageMap(pw, "map0", info, false);
			String graphURL = request.getContextPath()
					+ "/servlet/DisplayChart?filename=" + filename;
					// System.out.println(filename);
			pw.flush();

			//曲线图展示
			HashMap hmap = (HashMap) request.getAttribute("resultMap");
		    Integer freq=(Integer)request.getAttribute("freq");
		    String domain ="";
		//    boolean flag=false;
		    if(freq!=null){
		    	if(freq.intValue()==2){
		    		domain="季度走势";
		    	//	flag=true;
		    	}
		    	else if(freq.intValue()==1){
		    		domain = "月份走势";	
		    	}
		    }
			String title2 = "指标分析预警趋势图";
			String range = "指标值(%)";
			org.jfree.data.time.TimeSeries ca[] = new org.jfree.data.time.TimeSeries[yearLength];
			org.jfree.data.time.TimeSeriesCollection dataset = new org.jfree.data.time.TimeSeriesCollection();
			if (hmap != null) {
				for (int ys = 0; ys < yearLength; ys++) {
					String st = yearArr[ys] + "年报表";		
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
		//		axis.setDateFormatOverride(new SimpleDateFormat("季"));	  							  
		//	else
				axis.setDateFormatOverride(new SimpleDateFormat("M月"));			
			axis.setAutoTickUnitSelection(false);			
			axis.setTickUnit(new DateTickUnit(DateTickUnit.MONTH, 1));
			
			TextTitle subtitle = new TextTitle(subtitleStr, new Font("黑体",
					Font.BOLD, 17));
			chart2.addSubtitle(subtitle);
			chart2.setTitle(new TextTitle(title2, new Font("隶书", Font.ITALIC,
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
		<title>3D饼图</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=request.getContextPath()%>/css/common.css" rel="stylesheet" type="text/css">
	</head>

	<body leftmargin="2" topmargin="0" marginwidth="0" marginheight="0">
		<TABLE name="tbl" id="tbl" cellSpacing="1" cellPadding="1" width="98%" border="0" class="tbcolor" align="center">
			<tr id="tbcolor">
				<th align="center">

					<strong> <font face="Arial" size="2"> 预警信息 </font> </strong>
				</th>
			</tr>
			<tr>
				<td align="right" bgcolor="#FFFFFF">
					<html:form action="/target/viewPreStandDetail" method="Post" styleId="form1">

						<table width="100%" border="0">

							<TR>
								<TD align="right">
									指标名称
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
									&nbsp;指标值
								</TD>
								<TD>
									<html:text styleId="allWarnMessage" property="allWarnMessage" size="20" styleClass="input-text" readonly="true" />

								</TD>
								<TD align="right">
									下限
								</TD>
								<TD>
									<html:text styleId="temp1" property="temp1" size="15" styleClass="input-text" style="text" readonly="true" />
								</TD>
							</TR>
							<tr>
								<td align="right">
									上限
								</td>
								<td>
									<html:text styleId="temp2" property="temp2" size="20" styleClass="input-text" readonly="true" />
								</td>
								<td align="right">
									&nbsp;颜色
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
									公式
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
								指标值
							</strong>
						</th>
					</tr>

					<TR class="middle">
					<TD class="tableHeader" width="5%">
							<b>
								年份
							</b>
						</TD>
					<% for(int mi=1;mi<=12;mi++) {%>
						<TD class="tableHeader" width="8%">
							<b>
								<%=mi%>月
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
