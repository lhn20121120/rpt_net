<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.jfree.data.general.DefaultPieDataset"%> 
<%@ page import="org.jfree.chart.*"%> 
<%@ page import="org.jfree.chart.JFreeChart"%> 
<%@ page import="org.jfree.chart.plot.*"%> 
<%@ page import="org.jfree.chart.servlet.ServletUtilities"%> 
<%@ page import="org.jfree.chart.urls.StandardPieURLGenerator"%> 
<%@ page import="org.jfree.chart.entity.StandardEntityCollection"%> 
<%@ page import="java.io.*"%> 
<%@ page import="java.awt.Font"%> 
<%@ page import="org.jfree.chart.title.TextTitle"%> 
<%@ page import="java.awt.Color"%>
<%@ page import="org.jfree.chart.labels.StandardPieSectionLabelGenerator"%>
<%@ page import="java.awt.Graphics2D"%>
<%@ page import="java.awt.geom.Rectangle2D"%>
<%@ page import="javax.imageio.ImageIO"%>
<%@ page import="java.awt.image.BufferedImage"%>

<%@ page import="com.fitech.net.figure.WarnFigure"%>
<%@ page import="com.fitech.net.figure.Config"%>

<% 
 //传递数据
 String    title =request.getParameter("targetName");
 String subtitleStr = title.split(Config.SPLIT_TITLE)[0] + "指标分析";
 String    allWarnMessageColor=request.getParameter("allWarnMessageColor");
 float    currentValue=Float.parseFloat(request.getParameter("currentValue"));
 if(title.split(Config.SPLIT_TITLE)[1].equals("1"))
    	      title=title.split(Config.SPLIT_TITLE)[0]+"指标  指标值分配显示表";
 else
    	      title=title.split(Config.SPLIT_TITLE)[0]+"指标  比上期值分配显示表";
    
 WarnFigure         pie=new WarnFigure(title,allWarnMessageColor,currentValue);

 //创建图形对象
 JFreeChart chart = pie.initial(); 
 //设置图片背景色
 chart.setBackgroundPaint(java.awt.Color.white); 
 //获取输出提示
 String outPrintInf=pie.getOutPrintInf();
 //设置图片标题属性
 Font font = new Font("黑体",Font.CENTER_BASELINE,20);
 TextTitle _title = new TextTitle(title);
 _title.setFont(font);
 chart.setTitle(_title); 
 
 //把生成的图片放到临时目录
 StandardEntityCollection sec = new StandardEntityCollection(); 
 ChartRenderingInfo info = new ChartRenderingInfo(sec);
 	
 //500是图片长度，300是图片高度，session 为HttpSession对象
 String filename = ServletUtilities.saveChartAsPNG(chart, 600, 400, info, session);
 PlotRenderingInfo pf=info.getPlotInfo();
 //作图
 PrintWriter pw = new PrintWriter(out);
 ChartUtilities.writeImageMap(pw, "map0", info, false);
 String graphURL = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
 pw.flush();
 
 //曲线图展示
 	java.util.List list = (java.util.List)request.getAttribute("resultList");
 	String title2 = "指标分析预警趋势图";
	String domain = "月份走势";
	String range = "指标值(%)";
		
	org.jfree.data.time.TimeSeries ca = new org.jfree.data.time.TimeSeries("2006年报表");
	if(list != null){
		com.fitech.net.form.ActuTargetResultForm temp = (com.fitech.net.form.ActuTargetResultForm)list.get(0);
		Float lowResult = temp.getTargetResult();
		for(int i=1;i<list.size();i++){
			temp = (com.fitech.net.form.ActuTargetResultForm)list.get(i);
			if(temp.getTargetResult().floatValue() < lowResult.floatValue()){
				lowResult = temp.getTargetResult();
			}
		}
		// System.out.println(lowResult);
		for(int mon=0;mon<12;mon++){
			com.fitech.net.form.ActuTargetResultForm form = null;
			for(int i=0;i<list.size();i++){
				form = (com.fitech.net.form.ActuTargetResultForm)list.get(i);
				if(form.getMonth().intValue() == (mon+1)){
					break;
				}	
			}
			if(form != null)
				ca.add(new org.jfree.data.time.TimeSeriesDataItem(new org.jfree.data.time.Day(1,mon+1,2006),form.getTargetResult()));
			else
				ca.add(new org.jfree.data.time.TimeSeriesDataItem(new org.jfree.data.time.Day(1,mon+1,2006),0));
		}
	}
	
				
	org.jfree.data.time.TimeSeriesCollection dataset = new org.jfree.data.time.TimeSeriesCollection();
	dataset.addSeries(ca);
	JFreeChart chart2 = ChartFactory.createTimeSeriesChart(title2,domain,range,dataset,true,true,false);
		
	TextTitle subtitle =new TextTitle(subtitleStr,new Font("黑体",Font.BOLD,17));
	chart2.addSubtitle(subtitle);
	chart2.setTitle(new TextTitle(title2,new Font("隶书",Font.ITALIC,19)));
	chart2.setBackgroundPaint(new java.awt.GradientPaint(0,0,Color.white,0,1000,Color.blue));
	String fileName = ServletUtilities.saveChartAsJPEG(chart2,800,500,null,session);
	PrintWriter writer = new PrintWriter(out);
		
	String url = request.getContextPath() + "/servlet/DisplayChart?filename=" + fileName;
	writer.flush();           
%> 
<html>
<head>
<title>3D饼图</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

</head>

<body leftmargin="2" topmargin="0" marginwidth="0" marginheight="0">
<br>
<!-- <TABLE width="100%">
	<tr>
		<td align="center"><img src="<%=url%>" align="center"  border=0  usemap="#<%=fileName %>"/></td>       	
	</tr>			 
</TABLE>-->
<table width="90%" border="0" align="center"  >
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
</body>
</html>
