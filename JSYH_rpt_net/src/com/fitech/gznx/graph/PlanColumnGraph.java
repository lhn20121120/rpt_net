package com.fitech.gznx.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.geom.Arc2D;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;

public class PlanColumnGraph {
	
	/**
	 * ����˵��������Xml�ļ�����״ͼ����
	 *
	 */
	public static void resolveXml(ColumnStyle cs) {

		Element root = cs.getGraph();
		
		GraphDataSource gs = cs.getGs();
		
		try {
			
			/**
			 * �������ݲ���
			 */
			Element dataRoot = (Element) root.selectNodes("dataset").get(0);
			
			String tempvlabel = ((Element)root.selectNodes("vlabel").get(0)).attributeValue("title");
			
			if(tempvlabel!=null && !tempvlabel.equals(""))
				
				cs.setVlabel(changeUnitFormate(tempvlabel));
			
			List itemList = dataRoot.selectNodes("item");
			
			if (itemList != null && itemList.size() != 0) {
			
			cs.setTypeCount(itemList.size()); // ��ʼ������
			
			gs.setDataList(new ArrayList());

			if(itemList!=null && itemList.size()!=0){
				
				for(int i=0;i<itemList.size();i++){
					
					Element e = (Element) itemList.get(i);

					String datasetBaseSql = e.attributeValue("sql"); // �õ�XML�ļ��еĲ�ѯSQL
					
					String[] params = new String[3];

					params[0] = e.attributeValue("label");

					params[1] = "0";
					
					params[2] = datasetBaseSql;

					gs.getDataList().add(params);
				}
				
			}
			
			}
			
			/**
			 * ������ʽ
			 */
			Element styleRoot = (Element) root.selectNodes("style")
					.get(0);
			
			String tempwidth = ((Element)styleRoot.selectNodes("graphWidth").get(0)).attributeValue("value");
			
			if(tempwidth!=null && !tempwidth.equals(""))
				
				cs.setWidth(Integer.parseInt(tempwidth));
					
			
			String tempheight = ((Element)styleRoot.selectNodes("graphHeight").get(0)).attributeValue("value");
			
			if(tempheight!=null && !tempheight.equals(""))
				
				cs.setHeight(Integer.parseInt(tempheight));
			
			String tempbgcolor = ((Element)styleRoot.selectNodes("bgColorRGB").get(0)).attributeValue("value");
			
			if(tempbgcolor!=null && !tempbgcolor.equals(""))
			
				cs.setBgColorRGB(tempbgcolor);
			
			String tempttFont = ((Element)styleRoot.selectNodes("ttFont").get(0)).attributeValue("value");
			
			if(tempttFont!=null && !tempttFont.equals(""))
				
				cs.setTtFont(tempttFont);
			
			String tempttFontSize = ((Element)styleRoot.selectNodes("ttFontSize").get(0)).attributeValue("value");
			
			if(tempttFontSize!=null && !tempttFontSize.equals(""))
				
				cs.setTtFontSize(Integer.parseInt(tempttFontSize));
			
			
			String tempttColorRGB = ((Element)styleRoot.selectNodes("ttColorRGB").get(0)).attributeValue("value");
			
			if(tempttColorRGB!=null && !tempttColorRGB.equals(""))
				
				cs.setTtColorRGB(tempttColorRGB);
			
			String tempcBgColorRGB = ((Element)styleRoot.selectNodes("cBgColorRGB").get(0)).attributeValue("value");
			
			if(tempcBgColorRGB!=null && !tempcBgColorRGB.equals(""))
				
				cs.setCBgColorRGB(tempcBgColorRGB);
			
			String tempcInsets = ((Element)styleRoot.selectNodes("cInsets").get(0)).attributeValue("value");
				
			if(tempcInsets!=null && !tempcInsets.equals(""))
				
				cs.setCInsets(tempcInsets);
			
			String tempcRangeGridlineColorRGB = ((Element)styleRoot.selectNodes("cRangeGridlineColorRGB").get(0)).attributeValue("value");
			
			if(tempcRangeGridlineColorRGB!=null && !tempcRangeGridlineColorRGB.equals(""))
				
				cs.setCRangeGridlineColorRGB(tempcRangeGridlineColorRGB);
			
			String tempcForegroundAlpha = ((Element)styleRoot.selectNodes("cForegroundAlpha").get(0)).attributeValue("value");
			
			if(tempcForegroundAlpha!=null && !tempcForegroundAlpha.equals(""))
				
				cs.setCForegroundAlpha(tempcForegroundAlpha);
			
			String tempcDomainAxisColor = ((Element)styleRoot.selectNodes("cDomainAxisColor").get(0)).attributeValue("value");
			
			if(tempcDomainAxisColor!=null && !tempcDomainAxisColor.equals(""))
				
				cs.setCDomainAxisColor(tempcDomainAxisColor);
			
			String tempcDomainAxisLabelFont = ((Element)styleRoot.selectNodes("cDomainAxisLabelFont").get(0)).attributeValue("value");
			
			if(tempcDomainAxisLabelFont!=null && !tempcDomainAxisLabelFont.equals(""))
				
				cs.setCDomainAxisLabelFont(tempcDomainAxisLabelFont);
			
			String tempcDomainAxisLabelFontSize = ((Element)styleRoot.selectNodes("cDomainAxisLabelFontSize").get(0)).attributeValue("value");
			
			if(tempcDomainAxisLabelFontSize!=null && !tempcDomainAxisLabelFontSize.equals(""))
				
				cs.setCDomainAxisLabelFontSize(Integer.parseInt(tempcDomainAxisLabelFontSize));
			
			String tempcDomainAxisLabelFontColor = ((Element)styleRoot.selectNodes("cDomainAxisLabelFontColor").get(0)).attributeValue("value");
			
			if(tempcDomainAxisLabelFontColor!=null && !tempcDomainAxisLabelFontColor.equals(""))
				
				cs.setCDomainAxisLabelFontColor(tempcDomainAxisLabelFontColor);
			
			String tempcDomainAxisTickLabelFont = ((Element)styleRoot.selectNodes("cDomainAxisTickLabelFont").get(0)).attributeValue("value");
			
			if(tempcDomainAxisTickLabelFont!=null && !tempcDomainAxisTickLabelFont.equals(""))
				
				cs.setCDomainAxisTickLabelFont(tempcDomainAxisTickLabelFont);
			
			String tempcDomainAxisTickLabelFontSize = ((Element)styleRoot.selectNodes("cDomainAxisTickLabelFontSize").get(0)).attributeValue("value");
			
			if(tempcDomainAxisTickLabelFontSize!=null && !tempcDomainAxisTickLabelFontSize.equals(""))
				
				cs.setCDomainAxisTickLabelFontSize(Integer.parseInt(tempcDomainAxisTickLabelFontSize));
			
			String tempcDomainAxisTickLabelFontColor = ((Element)styleRoot.selectNodes("cDomainAxisTickLabelFontColor").get(0)).attributeValue("value");
			
			if(tempcDomainAxisTickLabelFontColor!=null && !tempcDomainAxisTickLabelFontColor.equals(""))
				
				cs.setCDomainAxisTickLabelFontColor(tempcDomainAxisTickLabelFontColor);
			
			String tempcRangeAxisColor = ((Element)styleRoot.selectNodes("cRangeAxisColor").get(0)).attributeValue("value");
			
			if(tempcRangeAxisColor!=null && !tempcRangeAxisColor.equals(""))
				
				cs.setCRangeAxisColor(tempcRangeAxisColor);
			
			String tempcRangeAxisLabelFont = ((Element)styleRoot.selectNodes("cRangeAxisLabelFont").get(0)).attributeValue("value");
			
			if(tempcRangeAxisLabelFont!=null && !tempcRangeAxisLabelFont.equals(""))
				
				cs.setCRangeAxisLabelFont(tempcRangeAxisLabelFont);
			
			String tempcRangeAxisLabelFontSize = ((Element)styleRoot.selectNodes("cRangeAxisLabelFontSize").get(0)).attributeValue("value");
			
			if(tempcRangeAxisLabelFontSize!=null && !tempcRangeAxisLabelFontSize.equals(""))
				
				cs.setCRangeAxisLabelFontSize(Integer.parseInt(tempcRangeAxisLabelFontSize));
			
			String tempcRangeAxisLabelFontColor = ((Element)styleRoot.selectNodes("cRangeAxisLabelFontColor").get(0)).attributeValue("value");
			
			if(tempcRangeAxisLabelFontColor!=null && !tempcRangeAxisLabelFontColor.equals(""))
				
				cs.setCRangeAxisLabelFontColor(tempcRangeAxisLabelFontColor);
			
			String tempcRangeAxisTickLabelFont = ((Element)styleRoot.selectNodes("cRangeAxisTickLabelFont").get(0)).attributeValue("value");
			
			if(tempcRangeAxisTickLabelFont!=null && !tempcRangeAxisTickLabelFont.equals(""))
				
				cs.setCRangeAxisTickLabelFont(tempcRangeAxisTickLabelFont);
			
			String tempcRangeAxisTickLabelFontSize = ((Element)styleRoot.selectNodes("cRangeAxisTickLabelFontSize").get(0)).attributeValue("value");
			
			if(tempcRangeAxisTickLabelFontSize!=null && !tempcRangeAxisTickLabelFontSize.equals(""))
				
				cs.setCRangeAxisTickLabelFontSize(Integer.parseInt(tempcRangeAxisTickLabelFontSize));
			
			String tempcRangeAxisTickLabelFontColor = ((Element)styleRoot.selectNodes("cRangeAxisTickLabelFontColor").get(0)).attributeValue("value");
			
			if(tempcRangeAxisTickLabelFontColor!=null && !tempcRangeAxisTickLabelFontColor.equals(""))
				
				cs.setCRangeAxisTickLabelFontColor(tempcRangeAxisTickLabelFontColor);
			
			String tempisC3D  = ((Element)styleRoot.selectNodes("isC3D").get(0)).attributeValue("value");
			
			
			if(tempisC3D!=null && tempisC3D.equals("y"))
				
				cs.setC3D(true);
			
			else cs.setC3D(false);
				
			String tempcWallPaint = ((Element)styleRoot.selectNodes("cWallPaint").get(0)).attributeValue("value");
			
			if(tempcWallPaint!=null && !tempcWallPaint.equals(""))
				
				cs.setCWallPaint(tempcWallPaint);
			
			String tempcItemMargin = ((Element)styleRoot.selectNodes("cItemMargin").get(0)).attributeValue("value");
			
			if(tempcItemMargin!=null && !tempcItemMargin.equals(""))
				
				cs.setCItemMargin(tempcItemMargin);
			
			String tempcLabelPositions = ((Element)styleRoot.selectNodes("cLabelPositions").get(0)).attributeValue("value");
			
			if(tempcLabelPositions!=null && !tempcLabelPositions.equals(""))
				
				cs.setCLabelPositions(tempcLabelPositions);
				

		} catch (Exception e) {

			e.printStackTrace();

		}

	}
	
	/***
	 * ������״ͼ
	 * @param graphName
	 * @param outpath
	 */
	public static void createGraph(ColumnStyle cs, String outpath ,String bigOutPath) {
		
		GraphDataSource gs = cs.getGs();
		
		String graphName = cs.getGraph().attributeValue("title");

		try {

			OutputStream output = new FileOutputStream(outpath);
			
			OutputStream Bigoutput = new FileOutputStream(bigOutPath);
			
			DecimalFormat df = new DecimalFormat("0.00");
			
			List list = gs.getDataList();
			
			DefaultCategoryDataset  dataset = null;
			
			String tempV = cs.getVlabel();
			
			if(list!=null &&list.size()!=0)
				
				dataset = new DefaultCategoryDataset();

			for (int i = 0; i < list.size(); i++) { // ����
				
				if(i%2==0){
					
					String[] strs = (String[]) list.get(i);

					String label = strs[0];

					double value = 0;

					if (!strs[1].equals("0")) {
	
						value = Double.parseDouble(strs[1]);
						
						if(tempV.equals("��λ��Ԫ"))
							;
						
						else if(tempV.equals("��λ����Ԫ"))
							
							value = value/10000;
							
						else if(tempV.equals("��λ������"))
							
							value = value/1000000;
						
						else if(tempV.equals("��λ����Ԫ"))
							
							value = value/100000000;
	
					}
				
					dataset.addValue(value,"�ƻ�",label);
					
					String[] _strs = (String[]) list.get(i+1);

					double _value = 0;

					if (!_strs[1].equals("0")) {
	
						_value = Double.parseDouble(_strs[1]);
						
						if(tempV.equals("��λ��Ԫ"))
							;
						
						else if(tempV.equals("��λ����Ԫ"))
							
							_value = _value/10000;
							
						else if(tempV.equals("��λ������"))
							
							_value = _value/1000000;
						
						else if(tempV.equals("��λ����Ԫ"))
							
							_value = _value/100000000;
	
					}
				
					dataset.addValue(_value,"�ƻ�������",label);
					
				}
				else
					
					continue;
					
			}

			JFreeChart chart = null;

			if (cs.isC3D())

				chart = ChartFactory.createBarChart3D(graphName, null, tempV,
						dataset, PlotOrientation.VERTICAL, true, false, true); // �õ���״ͼ����

			else

				chart = ChartFactory.createBarChart(graphName, null, null,
						dataset, PlotOrientation.VERTICAL, true, false, true); // �õ���״ͼ����

			/** *������״ͼ����ʽ** */

			String[] tempbgColorRGB = cs.getBgColorRGB().split(",");

			chart.setBackgroundPaint(new Color(Integer
					.parseInt(tempbgColorRGB[0]), Integer
					.parseInt(tempbgColorRGB[1]), Integer
					.parseInt(tempbgColorRGB[2]))); // ����ͼ�񱳾�ɫ

			/** ���ñ������ʽ* */
			Font ft = new Font(cs.getTtFont(), Font.BOLD, cs.getTtFontSize());

			TextTitle tt = new TextTitle(graphName, ft);

			String[] tempttColorRGB = cs.getTtColorRGB().split(",");

			tt.setPaint(new Color(Integer.parseInt(tempttColorRGB[0]), Integer
					.parseInt(tempttColorRGB[1]), Integer
					.parseInt(tempttColorRGB[2])));

			chart.setTitle(tt);

			CategoryPlot plot = chart.getCategoryPlot();
			
			plot.setNoDataMessage("û������");

			String[] tempcBgColorRGB = cs.getCBgColorRGB().split(",");

			plot.setBackgroundPaint(new Color(Integer
					.parseInt(tempcBgColorRGB[0]), Integer
					.parseInt(tempcBgColorRGB[1]), Integer
					.parseInt(tempcBgColorRGB[2])));

			String[] tempcInsets = cs.getCInsets().split(",");

			plot.setInsets(new RectangleInsets(Double
					.parseDouble(tempcInsets[0]), Double
					.parseDouble(tempcInsets[1]), Double
					.parseDouble(tempcInsets[2]), Double
					.parseDouble(tempcInsets[3])));

			String[] tempcRangeGridlineColorRGB = cs.getCRangeGridlineColorRGB()
					.split(",");

			plot.setRangeGridlinePaint(new Color(Integer
					.parseInt(tempcRangeGridlineColorRGB[0]), Integer
					.parseInt(tempcRangeGridlineColorRGB[1]), Integer
					.parseInt(tempcRangeGridlineColorRGB[2])));

			plot.setForegroundAlpha(Float.parseFloat(cs.getCForegroundAlpha()));

			CategoryAxis domainAxis = plot.getDomainAxis();

			String[] tempcDomainAxisColor = cs.getCDomainAxisColor().split(",");

			domainAxis.setAxisLinePaint(new Color(Integer
					.parseInt(tempcDomainAxisColor[0]), Integer
					.parseInt(tempcDomainAxisColor[1]), Integer
					.parseInt(tempcDomainAxisColor[2])));

			domainAxis.setLabelFont(new Font(cs.getCDomainAxisLabelFont(), Font.PLAIN,
					cs.getCDomainAxisLabelFontSize()));

			String[] tempcDomainAxisLabelFontColor = cs.getCDomainAxisLabelFontColor()
					.split(",");

			domainAxis.setLabelPaint(new Color(Integer
					.parseInt(tempcDomainAxisLabelFontColor[0]), Integer
					.parseInt(tempcDomainAxisLabelFontColor[1]), Integer
					.parseInt(tempcDomainAxisLabelFontColor[2])));

			domainAxis.setTickLabelFont(new Font(cs.getCDomainAxisTickLabelFont(),
					Font.PLAIN, cs.getCDomainAxisTickLabelFontSize()));

			String[] tempcDomainAxisTickLabelFontColor = cs.getCDomainAxisTickLabelFontColor()
					.split(",");

			domainAxis.setTickLabelPaint(new Color(Integer
					.parseInt(tempcDomainAxisTickLabelFontColor[0]), Integer
					.parseInt(tempcDomainAxisTickLabelFontColor[1]), Integer
					.parseInt(tempcDomainAxisTickLabelFontColor[2])));

			ValueAxis rangeAxis = plot.getRangeAxis();

			rangeAxis.setUpperMargin(0.15);// ������ߵ�һ�� Item ��ͼƬ���˵ľ���

			rangeAxis.setLowerMargin(0.15);// ������͵�һ�� Item ��ͼƬ�׶˵ľ���

			String[] tempcRangeAxisColor = cs.getCRangeAxisColor().split(",");

			rangeAxis.setAxisLinePaint(new Color(Integer
					.parseInt(tempcRangeAxisColor[0]), Integer
					.parseInt(tempcRangeAxisColor[1]), Integer
					.parseInt(tempcRangeAxisColor[2])));

			rangeAxis.setLabelFont(new Font(cs.getCRangeAxisLabelFont(), Font.PLAIN,
					cs.getCRangeAxisLabelFontSize()));

			String[] tempcRangeAxisLabelFontColor = cs.getCRangeAxisLabelFontColor()
					.split(",");

			rangeAxis.setLabelPaint(new Color(Integer
					.parseInt(tempcRangeAxisLabelFontColor[0]), Integer
					.parseInt(tempcRangeAxisLabelFontColor[1]), Integer
					.parseInt(tempcRangeAxisLabelFontColor[2])));

			rangeAxis.setTickLabelFont(new Font(cs.getCRangeAxisTickLabelFont(),
					Font.PLAIN, cs.getCRangeAxisTickLabelFontSize()));

			String[] tempcRangeAxisTickLabelFontColor = cs.getCRangeAxisTickLabelFontColor()
					.split(",");

			rangeAxis.setTickLabelPaint(new Color(Integer
					.parseInt(tempcRangeAxisTickLabelFontColor[0]), Integer
					.parseInt(tempcRangeAxisTickLabelFontColor[1]), Integer
					.parseInt(tempcRangeAxisTickLabelFontColor[2])));

			if (cs.isC3D()) {
				

				BarRenderer3D renderer = new BarRenderer3D();

				String[] tempcWallPaint = cs.getCWallPaint().split(",");

				renderer.setWallPaint(new Color(Integer
						.parseInt(tempcWallPaint[0]), Integer
						.parseInt(tempcWallPaint[1]), Integer
						.parseInt(tempcWallPaint[2])));

				renderer.setItemMargin(Float.parseFloat(cs.getCItemMargin()));
				
				renderer.setSeriesPaint(0,Color.BLUE);
				
				renderer.setFillPaint(Color.RED , true);
				
//				renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}",new DecimalFormat("0.00"), new DecimalFormat(
//				"0.00%")));

				renderer.setItemLabelFont(new Font("����", Font.TRUETYPE_FONT, 10));

				renderer.setItemLabelsVisible(true);
				
				plot.setRenderer(renderer);

			}

			else {

				BarRenderer renderer = new BarRenderer();

				renderer.setItemMargin(Float.parseFloat(cs.getCItemMargin()));
				
//				renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}",new DecimalFormat("0.00"), new DecimalFormat(
//				"0.00%")));

				renderer.setItemLabelFont(new Font("����", Font.TRUETYPE_FONT, 8));

				renderer.setItemLabelsVisible(true);

				plot.setRenderer(renderer);

			}
			
			if(cs.getCLabelPositions()!=null && !cs.getCLabelPositions().equals(""))
				
				domainAxis.setCategoryLabelPositions(CategoryLabelPositions
						.createUpRotationLabelPositions(Double
								.parseDouble(cs.getCLabelPositions())));

			ChartUtilities.writeChartAsJPEG(output, 1.0f, chart, cs.getWidth(), cs.getHeight(),
					null);
			
			
			ChartUtilities.writeChartAsJPEG(Bigoutput, 1.0f, chart, 800, 450,
					null);

			output.close();
			
			Bigoutput.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}
	
	
	/**
	 * ����˵�����÷�������ת��type_str��ΪJfreeChart�ܹ�ʶ��ĸ�ʽ
	 * @param type_str
	 * @return
	 */
	public static String changeUnitFormate(String unit_str) {

		String result = "";
		
		int unit = Integer.parseInt(unit_str); 

		try {

				switch (unit) {
				
				case (0):
					
					result = "����";
					break;

				case (1):

					result = "��λ��Ԫ"; 
					break;

				case (2):

					result = "��λ����Ԫ"; 
					break;

				case (3):

					result = "��λ������"; 
					break;

				case (4):

					result = "��λ����Ԫ"; 
					break;

			}
			

		} catch (Exception e) {

			result = "��λ��Ԫ";

		}

		return result;

	}
	
	 


}
