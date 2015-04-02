package com.fitech.gznx.graph;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

import com.fitech.gznx.common.Config;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.util.DateUtil;
/**
 * 折线图
 * @author jack
 *
 */
public class BreakLineGraph {
	
	
	
	/**
	 * 方法说明：解析Xml文件的折线图部分
	 * 
	 */
	public static void resolveXml(BreakLineStyle bls) {
		
		Element root = bls.getGraph();
		
		GraphDataSource gs = bls.getGs();

		try {
			
			/**
			 * 解析数据部分
			 */
			Element dataRoot = (Element) root.selectNodes("dataset").get(0);
			
			bls.setAlabel(((Element)root.selectNodes("alabel").get(0)).attributeValue("title"));
			
			String tempvlabel = ((Element)root.selectNodes("vlabel").get(0)).attributeValue("title");
			
			if(tempvlabel!=null && !tempvlabel.equals(""))
				
				bls.setVlabel(ColumnGraph.changeUnitFormate(tempvlabel));
			
			List itemList = dataRoot.selectNodes("item");
			
			if (itemList != null && itemList.size() != 0) {
				
				gs.setDataList(new ArrayList());

				for (int i = 0; i < itemList.size(); i++) {

					Element e = (Element) itemList.get(i);

					String datasetBaseSql = e.attributeValue("sql"); // 得到XML文件中的查询SQL

					BreakLineItem breakLineItem = new BreakLineItem();

					breakLineItem.setBaseSql(datasetBaseSql);

					breakLineItem.setLabel(e.attributeValue("label"));

					gs.getDataList().add(breakLineItem);

				}
				//this.conductBreakLine(orgId, ccyId); // 查询取数
			}
			
			/**
			 * 解析样式
			 */
			Element styleRoot = (Element) root.selectNodes("style")
					.get(0);
			
			String tempwidth = ((Element)styleRoot.selectNodes("graphWidth").get(0)).attributeValue("value");
			
			if(tempwidth!=null && !tempwidth.equals(""))
				
				bls.setWidth(Integer.parseInt(tempwidth));
					
			
			String tempheight = ((Element)styleRoot.selectNodes("graphHeight").get(0)).attributeValue("value");
			
			if(tempheight!=null && !tempheight.equals(""))
				
				bls.setHeight(Integer.parseInt(tempheight));
			
			String tempbgcolor = ((Element)styleRoot.selectNodes("bgColorRGB").get(0)).attributeValue("value");
			
			if(tempbgcolor!=null && !tempbgcolor.equals(""))
			
				bls.setBgColorRGB(tempbgcolor);
			
			String tempttFont = ((Element)styleRoot.selectNodes("ttFont").get(0)).attributeValue("value");
			
			if(tempttFont!=null && !tempttFont.equals(""))
				
				bls.setTtFont(tempttFont);
			
			String tempttFontSize = ((Element)styleRoot.selectNodes("ttFontSize").get(0)).attributeValue("value");
			
			if(tempttFontSize!=null && !tempttFontSize.equals(""))
				
				bls.setTtFontSize(Integer.parseInt(tempttFontSize));
			
			
			String tempttColorRGB = ((Element)styleRoot.selectNodes("ttColorRGB").get(0)).attributeValue("value");
			
			if(tempttColorRGB!=null && !tempttColorRGB.equals(""))
				
				bls.setTtColorRGB(tempttColorRGB);
			
			String tempblBgColorRGB = ((Element)styleRoot.selectNodes("blBgColorRGB").get(0)).attributeValue("value");
			
			if(tempblBgColorRGB!=null && !tempblBgColorRGB.equals(""))
				
				bls.setBlBgColorRGB(tempblBgColorRGB);
			
			String tempblInsets = ((Element)styleRoot.selectNodes("blInsets").get(0)).attributeValue("value");
				
			if(tempblInsets!=null && !tempblInsets.equals(""))
				
				bls.setBlInsets(tempblInsets);
			
			String tempblDomainGridlineColorRGB = ((Element)styleRoot.selectNodes("blDomainGridlineColorRGB").get(0)).attributeValue("value");
			
			if(tempblDomainGridlineColorRGB!=null && !tempblDomainGridlineColorRGB.equals(""))
				
				bls.setBlDomainGridlineColorRGB(tempblDomainGridlineColorRGB);
			
			String tempblRangeGridlineColorRGB = ((Element)styleRoot.selectNodes("blRangeGridlineColorRGB").get(0)).attributeValue("value");
			
			if(tempblRangeGridlineColorRGB!=null && !tempblRangeGridlineColorRGB.equals(""))
				
				bls.setBlRangeGridlineColorRGB(tempblRangeGridlineColorRGB);
			
			String tempblDomainTickBandColorRGB = ((Element)styleRoot.selectNodes("blDomainTickBandColorRGB").get(0)).attributeValue("value");
			
			if(tempblDomainTickBandColorRGB!=null && !tempblDomainTickBandColorRGB.equals(""))
				
				bls.setBlDomainTickBandColorRGB(tempblDomainTickBandColorRGB);
			
			String tempblRangeTickBandColorRGB = ((Element)styleRoot.selectNodes("blRangeTickBandColorRGB").get(0)).attributeValue("value");
			
			if(tempblRangeTickBandColorRGB!=null && !tempblRangeTickBandColorRGB.equals(""))
				
				bls.setBlRangeTickBandColorRGB(tempblRangeTickBandColorRGB);
			
			String tempisBaseShapesVisible = ((Element)styleRoot.selectNodes("isBaseShapesVisible").get(0)).attributeValue("value");
			
			if(tempisBaseShapesVisible!=null && tempisBaseShapesVisible.equals("y"))
				
				bls.setBaseShapesVisible(true);
			
			String tempisItemLabelVisible = ((Element)styleRoot.selectNodes("isItemLabelsVisible").get(0)).attributeValue("value");
			
			if(tempisItemLabelVisible!=null && tempisItemLabelVisible.equals("y"))
				
				bls.setItemLabelVisible(true);
			
			String tempblDomainAxisColor = ((Element)styleRoot.selectNodes("blDomainAxisColor").get(0)).attributeValue("value");
			
			if(tempblDomainAxisColor!=null && !tempblDomainAxisColor.equals(""))
				
				bls.setBlDomainAxisColor(tempblDomainAxisColor);
			
			String tempblDomainAxisLabelFont = ((Element)styleRoot.selectNodes("blDomainAxisLabelFont").get(0)).attributeValue("value");
			
			if(tempblDomainAxisLabelFont!=null && !tempblDomainAxisLabelFont.equals(""))
				
				bls.setBlDomainAxisLabelFont(tempblDomainAxisLabelFont);
			
			String tempblDomainAxisLabelFontSize = ((Element)styleRoot.selectNodes("blDomainAxisLabelFontSize").get(0)).attributeValue("value");
			
			if(tempblDomainAxisLabelFontSize!=null && !tempblDomainAxisLabelFontSize.equals(""))
				
				bls.setBlDomainAxisLabelFontSize(Integer.parseInt(tempblDomainAxisLabelFontSize));
			
			String tempblDomainAxisLabelFontColor = ((Element)styleRoot.selectNodes("blDomainAxisLabelFontColor").get(0)).attributeValue("value");
			
			if(tempblDomainAxisLabelFontColor!=null && !tempblDomainAxisLabelFontColor.equals(""))
				
				bls.setBlDomainAxisLabelFontColor(tempblDomainAxisLabelFontColor);
			
			String tempblDomainAxisTickLabelFont = ((Element)styleRoot.selectNodes("blDomainAxisTickLabelFont").get(0)).attributeValue("value");
			
			if(tempblDomainAxisTickLabelFont!=null && !tempblDomainAxisTickLabelFont.equals(""))
				
				bls.setBlDomainAxisTickLabelFont(tempblDomainAxisTickLabelFont);
			
			String tempblDomainAxisTickLabelFontSize = ((Element)styleRoot.selectNodes("blDomainAxisTickLabelFontSize").get(0)).attributeValue("value");
			
			if(tempblDomainAxisTickLabelFontSize!=null && !tempblDomainAxisTickLabelFontSize.equals(""))
				
				bls.setBlDomainAxisTickLabelFontSize(Integer.parseInt(tempblDomainAxisTickLabelFontSize));
			
			String tempblDomainAxisTickLabelFontColor = ((Element)styleRoot.selectNodes("blDomainAxisTickLabelFontColor").get(0)).attributeValue("value");
			
			if(tempblDomainAxisTickLabelFontColor!=null && !tempblDomainAxisTickLabelFontColor.equals(""))
				
				bls.setBlDomainAxisTickLabelFontColor(tempblDomainAxisTickLabelFontColor);
			
			String tempblRangeAxisColor = ((Element)styleRoot.selectNodes("blRangeAxisColor").get(0)).attributeValue("value");
			
			if(tempblRangeAxisColor!=null && !tempblRangeAxisColor.equals(""))
				
				bls.setBlRangeAxisColor(tempblRangeAxisColor);
			
			String tempblRangeAxisLabelFont = ((Element)styleRoot.selectNodes("blRangeAxisLabelFont").get(0)).attributeValue("value");
			
			if(tempblRangeAxisLabelFont!=null && !tempblRangeAxisLabelFont.equals(""))
				
				bls.setBlRangeAxisLabelFont(tempblRangeAxisLabelFont);
			
			String tempblRangeAxisLabelFontSize = ((Element)styleRoot.selectNodes("blRangeAxisLabelFontSize").get(0)).attributeValue("value");
			
			if(tempblRangeAxisLabelFontSize!=null && !tempblRangeAxisLabelFontSize.equals(""))
				
				bls.setBlRangeAxisLabelFontSize(Integer.parseInt(tempblRangeAxisLabelFontSize));
			
			String tempblRangeAxisLabelFontColor = ((Element)styleRoot.selectNodes("blRangeAxisLabelFontColor").get(0)).attributeValue("value");
			
			if(tempblRangeAxisLabelFontColor!=null && !tempblRangeAxisLabelFontColor.equals(""))
				
				bls.setBlRangeAxisLabelFontColor(tempblRangeAxisLabelFontColor);
			
			String tempblRangeAxisTickLabelFont = ((Element)styleRoot.selectNodes("blRangeAxisTickLabelFont").get(0)).attributeValue("value");
			
			if(tempblRangeAxisTickLabelFont!=null && !tempblRangeAxisTickLabelFont.equals(""))
				
				bls.setBlRangeAxisTickLabelFont(tempblRangeAxisTickLabelFont);
			
			String tempblRangeAxisTickLabelFontSize = ((Element)styleRoot.selectNodes("blRangeAxisTickLabelFontSize").get(0)).attributeValue("value");
			
			if(tempblRangeAxisTickLabelFontSize!=null && !tempblRangeAxisTickLabelFontSize.equals(""))
				
				bls.setBlRangeAxisTickLabelFontSize(Integer.parseInt(tempblRangeAxisTickLabelFontSize));
			
			String tempblRangeAxisTickLabelFontColor = ((Element)styleRoot.selectNodes("blRangeAxisTickLabelFontColor").get(0)).attributeValue("value");
			
			if(tempblRangeAxisTickLabelFontColor!=null && !tempblRangeAxisTickLabelFontColor.equals(""))
				
				bls.setBlRangeAxisTickLabelFontColor(tempblRangeAxisTickLabelFontColor);
			

		} catch (Exception e) {

			e.printStackTrace();

		}

	}
	
	/***
	 * 生成折线图
	 * @param graphName
	 * @param outpath
	 */
	public static void createGraph(BreakLineStyle bls, String outpath ,String Bigoutpath) {
		
		GraphDataSource gs = bls.getGs();
		
		String graphName = bls.getGraph().attributeValue("title");

		TimeSeriesCollection dataset = new TimeSeriesCollection();

		try {

			OutputStream output = new FileOutputStream(new File(outpath));
			
			OutputStream Bigoutput = new FileOutputStream(new File(Bigoutpath));
			
			List tempdataList = gs.getDataList();
			
			String tempV = bls.getVlabel();

			for (int i = 0; i < tempdataList.size(); i++) {

				BreakLineItem item = (BreakLineItem)tempdataList.get(i);

				Map map = item.getMap();

				Iterator iterator = map.keySet().iterator();

				TimeSeries ts = null;

				if (bls.getFreq() == Config.FREQ_MONTH.intValue()) // 如果时按月查询
				{
					ts = new TimeSeries(item.getLabel(), Month.class); // 每个ts对象为一个折线项
				}
				else if(bls.getFreq() == Config.FREQ_DAY.intValue())
				{
					ts = new TimeSeries(item.getLabel(),Day.class);
				}
				DecimalFormat df = new DecimalFormat("0.00");
				while (iterator.hasNext()) {
					
					String repDate = (String)iterator.next();
					
					String value = (String) map.get(repDate); // 找出其对应的值
					if(!StringUtil.isEmpty(value)){
						double tempvalue = Double.parseDouble(value);
						
						if(tempV.equals("单位：元"))
						{
							;
						}
						else if(tempV.equals("单位：万元"))
							
							tempvalue = Double.parseDouble(df.format(tempvalue/10000));
						
						else if(tempV.equals("单位：百万"))
							
							tempvalue = Double.parseDouble(df.format(tempvalue/1000000));
						
						else if(tempV.equals("单位：亿元"))
							
							tempvalue = Double.parseDouble(df.format(tempvalue/100000000));
						
						String year = DateUtil.getYear(repDate);
						String month = DateUtil.getMonth(repDate);
						String day = DateUtil.getDay(repDate);
						
						if (bls.getFreq() == Config.FREQ_MONTH.intValue())
						{
							Month monthObj = new Month(Integer.parseInt(month),Integer.parseInt(year));
							ts.add(monthObj, tempvalue);
						}
						else
						{
							Day dayObj = new Day(Integer.parseInt(day),Integer.parseInt(month),Integer.parseInt(year));
							ts.add(dayObj,tempvalue);
						}
					}
					
				}
				if (ts != null)
					dataset.addSeries(ts); // 添加折线项
			}

			XYDataset xydataset = (XYDataset) dataset;

			JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(
					graphName, bls.getAlabel(), tempV, xydataset, true, true, true);

			/** *设置折线图的样式*** */

			String[] tempbgColorRGB = bls.getBgColorRGB().split(",");

			jfreechart.setBackgroundPaint(new Color(Integer
					.parseInt(tempbgColorRGB[0]), Integer.parseInt(tempbgColorRGB[1]),
					Integer.parseInt(tempbgColorRGB[2]))); // 设置图像背景色

			/** 设置标题的样式* */
			Font ft = new Font(bls.getTtFont(), Font.BOLD,
					bls.getTtFontSize());

			TextTitle tt = new TextTitle(graphName, ft);

			String[] tempttColorRGB = bls.getTtColorRGB().split(",");

			tt.setPaint(new Color(Integer.parseInt(tempttColorRGB[0]), Integer
					.parseInt(tempttColorRGB[1]), Integer.parseInt(tempttColorRGB[2])));

			jfreechart.setTitle(tt);

			XYPlot xyplot = (XYPlot) jfreechart.getPlot(); // 获得 plot：XYPlot！！

//			NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis(); // 得到纵坐标对象

//			numberaxis.setLabelAngle(1.6);
			
			xyplot.setNoDataMessage("没有数据！");

			String[] tempblBgColorRGB = bls.getBlBgColorRGB().split(",");

			xyplot.setBackgroundPaint(new Color(Integer
					.parseInt(tempblBgColorRGB[0]), Integer
					.parseInt(tempblBgColorRGB[1]), Integer
					.parseInt(tempblBgColorRGB[2])));

			String[] tempblInsets = bls.getBlInsets().split(",");

			xyplot.setInsets(new RectangleInsets(Double
					.parseDouble(tempblInsets[0]), Double.parseDouble(tempblInsets[1]),
					Double.parseDouble(tempblInsets[2]), Double
							.parseDouble(tempblInsets[3])));

			String[] tempblDomainGridlineColorRGB = bls.getBlDomainGridlineColorRGB()
					.split(",");

			xyplot.setDomainGridlinePaint(new Color(Integer
					.parseInt(tempblDomainGridlineColorRGB[0]), Integer
					.parseInt(tempblDomainGridlineColorRGB[1]), Integer
					.parseInt(tempblDomainGridlineColorRGB[2])));

			String[] tempblRangeGridlineColorRGB = bls.getBlRangeGridlineColorRGB()
					.split(",");

			xyplot.setRangeGridlinePaint(new Color(Integer
					.parseInt(tempblRangeGridlineColorRGB[0]), Integer
					.parseInt(tempblRangeGridlineColorRGB[1]), Integer
					.parseInt(tempblRangeGridlineColorRGB[2])));

			if (!bls.getBlDomainTickBandColorRGB().equals("")) {

				String[] tempblDomainTickBandColorRGB = bls.getBlDomainTickBandColorRGB()
						.split(",");

				xyplot.setDomainTickBandPaint(new Color(Integer
						.parseInt(tempblDomainTickBandColorRGB[0]), Integer
						.parseInt(tempblDomainTickBandColorRGB[1]), Integer
						.parseInt(tempblDomainTickBandColorRGB[2])));

			}

			if (!bls.getBlRangeTickBandColorRGB().equals("")) {

				String[] tempblRangeTickBandColorRGB = bls.getBlRangeTickBandColorRGB()
						.split(",");

				xyplot.setRangeTickBandPaint(new Color(Integer
						.parseInt(tempblRangeTickBandColorRGB[0]), Integer
						.parseInt(tempblRangeTickBandColorRGB[1]), Integer
						.parseInt(tempblRangeTickBandColorRGB[2])));

			}

			XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot
					.getRenderer();

			xylineandshaperenderer
					.setShapesVisible(bls.isBaseShapesVisible()); // 数据点可见
			
			
			ItemLabelPosition   p   =   new   ItemLabelPosition(   
                      ItemLabelAnchor.INSIDE12,   TextAnchor.CENTER,   
                      TextAnchor.TOP_CENTER,   30.0);
  
			//显示数据点的值
			if(bls.isItemLabelVisible())
			{
				xylineandshaperenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
				xylineandshaperenderer.setBaseItemLabelsVisible(true);
				xylineandshaperenderer.setItemLabelAnchorOffset(-10D);
				xylineandshaperenderer.setPositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER));
				xylineandshaperenderer.setNegativeItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
		
			}
			
			NumberAxis numberAxis = (NumberAxis)xyplot.getRangeAxis();
			if(tempV.equals("比例"))
			{
				numberAxis.setTickUnit(new NumberTickUnit(0.05));
				numberAxis.setNumberFormatOverride(NumberFormat.getPercentInstance());
			}
			else
			{
				numberAxis.setAutoRangeIncludesZero(true);
			}
			

			//xylineandshaperenderer.setBasePositiveItemLabelPosition();
			//numberAxis.setNumberFormatOverride("0.00%");
		
			/*xylineandshaperenderer.setItemLabelGenerator(new StandardXYItemRenderer("{2}",new DecimalFormat("0.00"), new DecimalFormat(
			"0.00%")));
			*/
		
			DateAxis  dateaxis = (DateAxis)xyplot.getDomainAxis();
			if (bls.getFreq() == Config.FREQ_MONTH.intValue())
			{
				dateaxis.setTickUnit(new DateTickUnit(DateTickUnit.MONTH, 1, new SimpleDateFormat("yyyy-MM")));
			}
			else
			{	
				dateaxis.setTickUnit(new DateTickUnit(DateTickUnit.MONTH, 1, new SimpleDateFormat("yyyy-MM")));
				//dateaxis.setTickUnit(new DateTickUnit(DateTickUnit.DAY, 1, new SimpleDateFormat("yyyy-MM-dd")));
			}
			dateaxis.setVerticalTickLabels(true);   
			//dateaxis.setsetLabelAngle(30);
			
			/** 设置横轴的样式** */
			Axis axis = xyplot.getDomainAxis();
			String[] tempblDomainAxisColor = bls.getBlDomainAxisColor()
					.split(",");
		
			axis.setAxisLinePaint(new Color(Integer
					.parseInt(tempblDomainAxisColor[0]), Integer
					.parseInt(tempblDomainAxisColor[1]), Integer
					.parseInt(tempblDomainAxisColor[2])));

			axis.setLabelFont(new Font(bls.getBlDomainAxisLabelFont(),
					Font.PLAIN, bls.getBlDomainAxisLabelFontSize()));

			String[] tempblDomainAxisLabelFontColor = bls.getBlDomainAxisLabelFontColor()
					.split(",");

			axis.setLabelPaint(new Color(Integer
					.parseInt(tempblDomainAxisLabelFontColor[0]), Integer
					.parseInt(tempblDomainAxisLabelFontColor[1]), Integer
					.parseInt(tempblDomainAxisLabelFontColor[2])));

			axis.setTickLabelFont(new Font(
					bls.getBlDomainAxisTickLabelFont(), Font.PLAIN,
					bls.getBlDomainAxisTickLabelFontSize()));

			String[] tempblDomainAxisTickLabelFontColor = bls.getBlDomainAxisTickLabelFontColor()
					.split(",");

			axis.setTickLabelPaint(new Color(Integer
					.parseInt(tempblDomainAxisTickLabelFontColor[0]), Integer
					.parseInt(tempblDomainAxisTickLabelFontColor[1]), Integer
					.parseInt(tempblDomainAxisTickLabelFontColor[2])));

			/** 设置纵轴的样式** */
			axis = xyplot.getRangeAxis();
			
			String[] tempblRangeAxisColor = bls.getBlRangeAxisColor()
					.split(",");

			axis.setAxisLinePaint(new Color(Integer
					.parseInt(tempblRangeAxisColor[0]), Integer
					.parseInt(tempblRangeAxisColor[1]), Integer
					.parseInt(tempblRangeAxisColor[2])));

			axis.setLabelFont(new Font(bls.getBlRangeAxisLabelFont(),
					Font.PLAIN, bls.getBlRangeAxisLabelFontSize()));

			String[] tempblRangeAxisLabelFontColor = bls.getBlRangeAxisLabelFontColor()
					.split(",");

			axis.setLabelPaint(new Color(Integer
					.parseInt(tempblRangeAxisLabelFontColor[0]), Integer
					.parseInt(tempblRangeAxisLabelFontColor[1]), Integer
					.parseInt(tempblRangeAxisLabelFontColor[2])));

			axis.setTickLabelFont(new Font(
					bls.getBlRangeAxisTickLabelFont(), Font.PLAIN,
					bls.getBlRangeAxisTickLabelFontSize()));

			String[] tempblRangeAxisTickLabelFontColor = bls.getBlRangeAxisTickLabelFontColor()
					.split(",");

			axis.setTickLabelPaint(new Color(Integer
					.parseInt(tempblRangeAxisTickLabelFontColor[0]), Integer
					.parseInt(tempblRangeAxisTickLabelFontColor[1]), Integer
					.parseInt(tempblRangeAxisTickLabelFontColor[2])));

			//xylineandshaperenderer.setShapesFilled(true); // 数据点被填充即不是空心点

			ChartUtilities.writeChartAsJPEG(output, 1.0f, jfreechart, bls.getWidth(),
					bls.getHeight(), null);
			
			ChartUtilities.writeChartAsJPEG(Bigoutput, 1.0f, jfreechart, 800,
					450, null);

			output.close();
			
			Bigoutput.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

}
