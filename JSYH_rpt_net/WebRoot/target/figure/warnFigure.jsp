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
 //��������
 String    title =request.getParameter("targetName");
 String subtitleStr = title.split(Config.SPLIT_TITLE)[0] + "ָ�����";
 String    allWarnMessageColor=request.getParameter("allWarnMessageColor");
 float    currentValue=Float.parseFloat(request.getParameter("currentValue"));
 if(title.split(Config.SPLIT_TITLE)[1].equals("1"))
    	      title=title.split(Config.SPLIT_TITLE)[0]+"ָ��  ָ��ֵ������ʾ��";
 else
    	      title=title.split(Config.SPLIT_TITLE)[0]+"ָ��  ������ֵ������ʾ��";
    
 WarnFigure         pie=new WarnFigure(title,allWarnMessageColor,currentValue);

 //����ͼ�ζ���
 JFreeChart chart = pie.initial(); 
 //����ͼƬ����ɫ
 chart.setBackgroundPaint(java.awt.Color.white); 
 //��ȡ�����ʾ
 String outPrintInf=pie.getOutPrintInf();
 //����ͼƬ��������
 Font font = new Font("����",Font.CENTER_BASELINE,20);
 TextTitle _title = new TextTitle(title);
 _title.setFont(font);
 chart.setTitle(_title); 
 
 //�����ɵ�ͼƬ�ŵ���ʱĿ¼
 StandardEntityCollection sec = new StandardEntityCollection(); 
 ChartRenderingInfo info = new ChartRenderingInfo(sec);
 	
 //500��ͼƬ���ȣ�300��ͼƬ�߶ȣ�session ΪHttpSession����
 String filename = ServletUtilities.saveChartAsPNG(chart, 600, 400, info, session);
 PlotRenderingInfo pf=info.getPlotInfo();
 //��ͼ
 PrintWriter pw = new PrintWriter(out);
 ChartUtilities.writeImageMap(pw, "map0", info, false);
 String graphURL = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
 pw.flush();
 
 //����ͼչʾ
 	java.util.List list = (java.util.List)request.getAttribute("resultList");
 	String title2 = "ָ�����Ԥ������ͼ";
	String domain = "�·�����";
	String range = "ָ��ֵ(%)";
		
	org.jfree.data.time.TimeSeries ca = new org.jfree.data.time.TimeSeries("2006�걨��");
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
		
	TextTitle subtitle =new TextTitle(subtitleStr,new Font("����",Font.BOLD,17));
	chart2.addSubtitle(subtitle);
	chart2.setTitle(new TextTitle(title2,new Font("����",Font.ITALIC,19)));
	chart2.setBackgroundPaint(new java.awt.GradientPaint(0,0,Color.white,0,1000,Color.blue));
	String fileName = ServletUtilities.saveChartAsJPEG(chart2,800,500,null,session);
	PrintWriter writer = new PrintWriter(out);
		
	String url = request.getContextPath() + "/servlet/DisplayChart?filename=" + fileName;
	writer.flush();           
%> 
<html>
<head>
<title>3D��ͼ</title>
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
