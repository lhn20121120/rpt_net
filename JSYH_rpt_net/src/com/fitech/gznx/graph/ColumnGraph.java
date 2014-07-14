package com.fitech.gznx.graph;

import java.awt.Color;
import java.awt.Font;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

import com.fitech.gznx.util.FitechUtil;

/**
 * 柱状图
 * @author jack
 *
 */
public class ColumnGraph
{

	/**
	 * 方法说明：解析Xml文件的柱状图部分
	 *
	 */
	public static void resolveXml(ColumnStyle cs)
	{

		Element root = cs.getGraph();

		GraphDataSource gs = cs.getGs();

		try
		{

			/**
			 * 解析数据部分
			 */
			Element dataRoot = (Element) root.selectNodes("dataset").get(0);

			cs.setIsContailMe(dataRoot.attributeValue("isContailMe"));

			String tempvlabel = ((Element) root.selectNodes("vlabel").get(0)).attributeValue("title");

			if (tempvlabel != null && !tempvlabel.equals(""))

				cs.setVlabel(changeUnitFormate(tempvlabel));

			List serialList = dataRoot.selectNodes("serial");

			List itemList = dataRoot.selectNodes("item");

			if ((serialList != null && serialList.size() != 0) || (itemList != null && itemList.size() != 0))
			{
 
				gs.setDataList(new ArrayList());

				if (cs.getIsContailMe().equals("y"))
				{

					SerialByColumn sbc = new SerialByColumn();

//					sbc.setLabel(gs.getOrgName());
					sbc.setLabel("所有组织机构：");
//					sbc.setValue(gs.getOrgId());
					sbc.setValue("fitech");

//					gs.getDataList().add(sbc);

				} 

			}

			if (serialList != null && serialList.size() != 0)
			{

				for (int i = 0; i < serialList.size(); i++)
				{

					Element e = (Element) serialList.get(i);

					String sql1 = e.attributeValue("sql");
					String sql = GraphDataSource.getSql(sql1, gs);
					System.out.println(sql);
					Map orgInfoMap = FitechUtil.jdbcQueryUtilOfMap(sql);
					if(orgInfoMap!=null && !orgInfoMap.isEmpty())
					{
						Set entrySet = orgInfoMap.entrySet();
						Iterator it = entrySet.iterator();
						while(it.hasNext())
						{
							SerialByColumn sbc = new SerialByColumn();
							
							Map.Entry item = (Map.Entry)it.next();
							sbc.setLabel((String)item.getValue());
							sbc.setValue((String)item.getKey());
							System.out.println("****"+(String)item.getKey()+"******"+(String)item.getValue());
							gs.getDataList().add(sbc);
							
						}
					}
					//sbc.setLabel(e.attributeValue("label"));

					//sbc.setValue(e.attributeValue("value"));

					//System.out.println(e.attributeValue("label")+"@"+e.attributeValue("value"));

					//gs.getDataList().add(sbc);
				}

			}

			if (itemList != null && itemList.size() != 0)
			{

				cs.setTypeCount(itemList.size()); // 初始化分类

				if (itemList != null && itemList.size() != 0)
				{

					for (int i = 0; i < gs.getDataList().size(); i++)
					{

						Map map = new HashMap();

						List lis = new ArrayList();

						for (int j = 0; j < itemList.size(); j++)
						{

							Element e = (Element) itemList.get(j);

							String[] strs = new String[2];

							strs[0] = e.attributeValue("sql");

//							strs[1] = e.attributeValue("0");
							strs[1] = "key";

							map.put(e.attributeValue("label").toString(), strs);

							lis.add(e.attributeValue("label").toString());

						}

						SerialByColumn sbc = (SerialByColumn) gs.getDataList().get(i);

						sbc.setMap(map);

						sbc.setList(lis);

					}

				}

			}

			/**
			 * 解析样式
			 */
			Element styleRoot = (Element) root.selectNodes("style").get(0);

			String tempwidth = ((Element) styleRoot.selectNodes("graphWidth").get(0)).attributeValue("value");

			if (tempwidth != null && !tempwidth.equals(""))

				cs.setWidth(Integer.parseInt(tempwidth));

			String tempheight = ((Element) styleRoot.selectNodes("graphHeight").get(0))
					.attributeValue("value");

			if (tempheight != null && !tempheight.equals(""))

				cs.setHeight(Integer.parseInt(tempheight));

			String tempbgcolor = ((Element) styleRoot.selectNodes("bgColorRGB").get(0))
					.attributeValue("value");

			if (tempbgcolor != null && !tempbgcolor.equals(""))

				cs.setBgColorRGB(tempbgcolor);

			String tempttFont = ((Element) styleRoot.selectNodes("ttFont").get(0)).attributeValue("value");

			if (tempttFont != null && !tempttFont.equals(""))

				cs.setTtFont(tempttFont);

			String tempttFontSize = ((Element) styleRoot.selectNodes("ttFontSize").get(0))
					.attributeValue("value");

			if (tempttFontSize != null && !tempttFontSize.equals(""))

				cs.setTtFontSize(Integer.parseInt(tempttFontSize));

			String tempttColorRGB = ((Element) styleRoot.selectNodes("ttColorRGB").get(0))
					.attributeValue("value");

			if (tempttColorRGB != null && !tempttColorRGB.equals(""))

				cs.setTtColorRGB(tempttColorRGB);

			String tempcBgColorRGB = ((Element) styleRoot.selectNodes("cBgColorRGB").get(0))
					.attributeValue("value");

			if (tempcBgColorRGB != null && !tempcBgColorRGB.equals(""))

				cs.setCBgColorRGB(tempcBgColorRGB);

			String tempcInsets = ((Element) styleRoot.selectNodes("cInsets").get(0)).attributeValue("value");

			if (tempcInsets != null && !tempcInsets.equals(""))

				cs.setCInsets(tempcInsets);

			String tempcRangeGridlineColorRGB = ((Element) styleRoot.selectNodes("cRangeGridlineColorRGB")
					.get(0)).attributeValue("value");

			if (tempcRangeGridlineColorRGB != null && !tempcRangeGridlineColorRGB.equals(""))

				cs.setCRangeGridlineColorRGB(tempcRangeGridlineColorRGB);

			String tempcForegroundAlpha = ((Element) styleRoot.selectNodes("cForegroundAlpha").get(0))
					.attributeValue("value");

			if (tempcForegroundAlpha != null && !tempcForegroundAlpha.equals(""))

				cs.setCForegroundAlpha(tempcForegroundAlpha);

			String tempcDomainAxisColor = ((Element) styleRoot.selectNodes("cDomainAxisColor").get(0))
					.attributeValue("value");

			if (tempcDomainAxisColor != null && !tempcDomainAxisColor.equals(""))

				cs.setCDomainAxisColor(tempcDomainAxisColor);

			String tempcDomainAxisLabelFont = ((Element) styleRoot.selectNodes("cDomainAxisLabelFont").get(0))
					.attributeValue("value");

			if (tempcDomainAxisLabelFont != null && !tempcDomainAxisLabelFont.equals(""))

				cs.setCDomainAxisLabelFont(tempcDomainAxisLabelFont);

			String tempcDomainAxisLabelFontSize = ((Element) styleRoot
					.selectNodes("cDomainAxisLabelFontSize").get(0)).attributeValue("value");

			if (tempcDomainAxisLabelFontSize != null && !tempcDomainAxisLabelFontSize.equals(""))

				cs.setCDomainAxisLabelFontSize(Integer.parseInt(tempcDomainAxisLabelFontSize));

			String tempcDomainAxisLabelFontColor = ((Element) styleRoot.selectNodes(
					"cDomainAxisLabelFontColor").get(0)).attributeValue("value");

			if (tempcDomainAxisLabelFontColor != null && !tempcDomainAxisLabelFontColor.equals(""))

				cs.setCDomainAxisLabelFontColor(tempcDomainAxisLabelFontColor);

			String tempcDomainAxisTickLabelFont = ((Element) styleRoot
					.selectNodes("cDomainAxisTickLabelFont").get(0)).attributeValue("value");

			if (tempcDomainAxisTickLabelFont != null && !tempcDomainAxisTickLabelFont.equals(""))

				cs.setCDomainAxisTickLabelFont(tempcDomainAxisTickLabelFont);

			String tempcDomainAxisTickLabelFontSize = ((Element) styleRoot.selectNodes(
					"cDomainAxisTickLabelFontSize").get(0)).attributeValue("value");

			if (tempcDomainAxisTickLabelFontSize != null && !tempcDomainAxisTickLabelFontSize.equals(""))

				cs.setCDomainAxisTickLabelFontSize(Integer.parseInt(tempcDomainAxisTickLabelFontSize));

			String tempcDomainAxisTickLabelFontColor = ((Element) styleRoot.selectNodes(
					"cDomainAxisTickLabelFontColor").get(0)).attributeValue("value");

			if (tempcDomainAxisTickLabelFontColor != null && !tempcDomainAxisTickLabelFontColor.equals(""))

				cs.setCDomainAxisTickLabelFontColor(tempcDomainAxisTickLabelFontColor);

			String tempcRangeAxisColor = ((Element) styleRoot.selectNodes("cRangeAxisColor").get(0))
					.attributeValue("value");

			if (tempcRangeAxisColor != null && !tempcRangeAxisColor.equals(""))

				cs.setCRangeAxisColor(tempcRangeAxisColor);

			String tempcRangeAxisLabelFont = ((Element) styleRoot.selectNodes("cRangeAxisLabelFont").get(0))
					.attributeValue("value");

			if (tempcRangeAxisLabelFont != null && !tempcRangeAxisLabelFont.equals(""))

				cs.setCRangeAxisLabelFont(tempcRangeAxisLabelFont);

			String tempcRangeAxisLabelFontSize = ((Element) styleRoot.selectNodes("cRangeAxisLabelFontSize")
					.get(0)).attributeValue("value");

			if (tempcRangeAxisLabelFontSize != null && !tempcRangeAxisLabelFontSize.equals(""))

				cs.setCRangeAxisLabelFontSize(Integer.parseInt(tempcRangeAxisLabelFontSize));

			String tempcRangeAxisLabelFontColor = ((Element) styleRoot
					.selectNodes("cRangeAxisLabelFontColor").get(0)).attributeValue("value");

			if (tempcRangeAxisLabelFontColor != null && !tempcRangeAxisLabelFontColor.equals(""))

				cs.setCRangeAxisLabelFontColor(tempcRangeAxisLabelFontColor);

			String tempcRangeAxisTickLabelFont = ((Element) styleRoot.selectNodes("cRangeAxisTickLabelFont")
					.get(0)).attributeValue("value");

			if (tempcRangeAxisTickLabelFont != null && !tempcRangeAxisTickLabelFont.equals(""))

				cs.setCRangeAxisTickLabelFont(tempcRangeAxisTickLabelFont);

			String tempcRangeAxisTickLabelFontSize = ((Element) styleRoot.selectNodes(
					"cRangeAxisTickLabelFontSize").get(0)).attributeValue("value");

			if (tempcRangeAxisTickLabelFontSize != null && !tempcRangeAxisTickLabelFontSize.equals(""))

				cs.setCRangeAxisTickLabelFontSize(Integer.parseInt(tempcRangeAxisTickLabelFontSize));

			String tempcRangeAxisTickLabelFontColor = ((Element) styleRoot.selectNodes(
					"cRangeAxisTickLabelFontColor").get(0)).attributeValue("value");

			if (tempcRangeAxisTickLabelFontColor != null && !tempcRangeAxisTickLabelFontColor.equals(""))

				cs.setCRangeAxisTickLabelFontColor(tempcRangeAxisTickLabelFontColor);

			String tempisC3D = ((Element) styleRoot.selectNodes("isC3D").get(0)).attributeValue("value");

			if (tempisC3D != null && tempisC3D.equals("y"))

				cs.setC3D(true);

			else
				cs.setC3D(false);

			String tempcWallPaint = ((Element) styleRoot.selectNodes("cWallPaint").get(0))
					.attributeValue("value");

			if (tempcWallPaint != null && !tempcWallPaint.equals(""))

				cs.setCWallPaint(tempcWallPaint);

			String tempcItemMargin = ((Element) styleRoot.selectNodes("cItemMargin").get(0))
					.attributeValue("value");

			if (tempcItemMargin != null && !tempcItemMargin.equals(""))

				cs.setCItemMargin(tempcItemMargin);

			String tempcLabelPositions = ((Element) styleRoot.selectNodes("cLabelPositions").get(0))
					.attributeValue("value");

			if (tempcLabelPositions != null && !tempcLabelPositions.equals(""))

				cs.setCLabelPositions(tempcLabelPositions);

		}
		catch (Exception e)
		{

			e.printStackTrace();

		}

	}

	/***
	 * 生成柱状图
	 * @param graphName
	 * @param outpath
	 */
	public static void createGraph(ColumnStyle cs, String outpath, String bigOutPath)
	{

		GraphDataSource gs = cs.getGs();

		String graphName = cs.getGraph().attributeValue("title");

		try
		{

			OutputStream output = new FileOutputStream(outpath);

			OutputStream Bigoutput = new FileOutputStream(bigOutPath);

			List datas = gs.getDataList();

			double[][] data = new double[datas.size()][cs.getTypeCount()];

			String[] rowKeys = new String[datas.size()]; // 机构

			String[] columnKeys = new String[0];

			boolean noDataFlag = true;

			DecimalFormat df = new DecimalFormat("0.00");

			String tempV = cs.getVlabel();

			if (datas != null && datas.size() != 0)
			{

				for (int i = 0; i < datas.size(); i++)
				{

					SerialByColumn sbc = (SerialByColumn) datas.get(i);

					List list = sbc.getList();

					columnKeys = new String[list.size()]; // 插入序列号

					Map map = sbc.getMap();

					for (int k = 0; k < list.size(); k++)
					{

						String[] strs = (String[]) map.get((String) list.get(k));

						//System.out.println("data"+k+"="+Double.parseDouble(strs[1]));

						if (!(Double.parseDouble(strs[1]) == 0.0))
						{

							noDataFlag = false;

							break;
						}

					}

					for (int j = 0; j < list.size(); j++)
					{

						String[] strs = (String[]) map.get((String) list.get(j));

						if (tempV.equals("单位：元"))

							data[i][j] = Double.parseDouble(strs[1]);

						else if (tempV.equals("单位：万元"))

							data[i][j] = Double.parseDouble(strs[1]) / 10000;

						else if (tempV.equals("单位：百万"))

							data[i][j] = Double.parseDouble(strs[1]) / 1000000;

						else if (tempV.equals("单位：亿元"))

							data[i][j] = Double.parseDouble(strs[1]) / 100000000;

						columnKeys[j] = (String) list.get(j);

					}

					rowKeys[i] = sbc.getLabel(); // 分类项

				}

			}

			CategoryDataset dataset = null;

			if (noDataFlag || (columnKeys.length == 0 && rowKeys.length == 0))

				dataset = null;

			else

				dataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data); // 得到数据对象			

			JFreeChart chart = null;

			if (cs.isC3D())

				chart = ChartFactory.createBarChart3D(graphName, null, cs.getVlabel(), dataset,
						PlotOrientation.VERTICAL, true, false, true); // 得到柱状图对象

			else

				chart = ChartFactory.createBarChart(graphName, null, null, dataset, PlotOrientation.VERTICAL,
						true, false, true); // 得到柱状图对象

			/** *设置柱状图的样式** */

			String[] tempbgColorRGB = cs.getBgColorRGB().split(",");

			chart.setBackgroundPaint(new Color(Integer.parseInt(tempbgColorRGB[0]), Integer
					.parseInt(tempbgColorRGB[1]), Integer.parseInt(tempbgColorRGB[2]))); // 设置图像背景色

			/** 设置标题的样式* */
			Font ft = new Font(cs.getTtFont(), Font.BOLD, cs.getTtFontSize());

			TextTitle tt = new TextTitle(graphName, ft);

			String[] tempttColorRGB = cs.getTtColorRGB().split(",");

			tt.setPaint(new Color(Integer.parseInt(tempttColorRGB[0]), Integer.parseInt(tempttColorRGB[1]),
					Integer.parseInt(tempttColorRGB[2])));

			chart.setTitle(tt);

			CategoryPlot plot = chart.getCategoryPlot();

			plot.setNoDataMessage("没有数据");

			String[] tempcBgColorRGB = cs.getCBgColorRGB().split(",");

			plot.setBackgroundPaint(new Color(Integer.parseInt(tempcBgColorRGB[0]), Integer
					.parseInt(tempcBgColorRGB[1]), Integer.parseInt(tempcBgColorRGB[2])));

			String[] tempcInsets = cs.getCInsets().split(",");

			plot.setInsets(new RectangleInsets(Double.parseDouble(tempcInsets[0]), Double
					.parseDouble(tempcInsets[1]), Double.parseDouble(tempcInsets[2]), Double
					.parseDouble(tempcInsets[3])));

			String[] tempcRangeGridlineColorRGB = cs.getCRangeGridlineColorRGB().split(",");

			plot
					.setRangeGridlinePaint(new Color(Integer.parseInt(tempcRangeGridlineColorRGB[0]), Integer
							.parseInt(tempcRangeGridlineColorRGB[1]), Integer
							.parseInt(tempcRangeGridlineColorRGB[2])));

			plot.setForegroundAlpha(Float.parseFloat(cs.getCForegroundAlpha()));

			CategoryAxis domainAxis = plot.getDomainAxis();

			String[] tempcDomainAxisColor = cs.getCDomainAxisColor().split(",");

			domainAxis.setAxisLinePaint(new Color(Integer.parseInt(tempcDomainAxisColor[0]), Integer
					.parseInt(tempcDomainAxisColor[1]), Integer.parseInt(tempcDomainAxisColor[2])));

			domainAxis.setLabelFont(new Font(cs.getCDomainAxisLabelFont(), Font.PLAIN, cs
					.getCDomainAxisLabelFontSize()));

			String[] tempcDomainAxisLabelFontColor = cs.getCDomainAxisLabelFontColor().split(",");

			domainAxis.setLabelPaint(new Color(Integer.parseInt(tempcDomainAxisLabelFontColor[0]), Integer
					.parseInt(tempcDomainAxisLabelFontColor[1]), Integer
					.parseInt(tempcDomainAxisLabelFontColor[2])));

			domainAxis.setTickLabelFont(new Font(cs.getCDomainAxisTickLabelFont(), Font.PLAIN, cs
					.getCDomainAxisTickLabelFontSize()));

			String[] tempcDomainAxisTickLabelFontColor = cs.getCDomainAxisTickLabelFontColor().split(",");

			domainAxis.setTickLabelPaint(new Color(Integer.parseInt(tempcDomainAxisTickLabelFontColor[0]),
					Integer.parseInt(tempcDomainAxisTickLabelFontColor[1]), Integer
							.parseInt(tempcDomainAxisTickLabelFontColor[2])));

			ValueAxis rangeAxis = plot.getRangeAxis();

			rangeAxis.setUpperMargin(0.15);// 设置最高的一个 Item 与图片顶端的距离

			rangeAxis.setLowerMargin(0.15);// 设置最低的一个 Item 与图片底端的距离

			String[] tempcRangeAxisColor = cs.getCRangeAxisColor().split(",");

			rangeAxis.setAxisLinePaint(new Color(Integer.parseInt(tempcRangeAxisColor[0]), Integer
					.parseInt(tempcRangeAxisColor[1]), Integer.parseInt(tempcRangeAxisColor[2])));

			rangeAxis.setLabelFont(new Font(cs.getCRangeAxisLabelFont(), Font.PLAIN, cs
					.getCRangeAxisLabelFontSize()));

			String[] tempcRangeAxisLabelFontColor = cs.getCRangeAxisLabelFontColor().split(",");

			rangeAxis.setLabelPaint(new Color(Integer.parseInt(tempcRangeAxisLabelFontColor[0]), Integer
					.parseInt(tempcRangeAxisLabelFontColor[1]), Integer
					.parseInt(tempcRangeAxisLabelFontColor[2])));

			rangeAxis.setTickLabelFont(new Font(cs.getCRangeAxisTickLabelFont(), Font.PLAIN, cs
					.getCRangeAxisTickLabelFontSize()));

			String[] tempcRangeAxisTickLabelFontColor = cs.getCRangeAxisTickLabelFontColor().split(",");

			rangeAxis.setTickLabelPaint(new Color(Integer.parseInt(tempcRangeAxisTickLabelFontColor[0]),
					Integer.parseInt(tempcRangeAxisTickLabelFontColor[1]), Integer
							.parseInt(tempcRangeAxisTickLabelFontColor[2])));

			if (cs.isC3D())
			{

				BarRenderer3D renderer = new BarRenderer3D();

				String[] tempcWallPaint = cs.getCWallPaint().split(",");

				renderer.setWallPaint(new Color(Integer.parseInt(tempcWallPaint[0]), Integer
						.parseInt(tempcWallPaint[1]), Integer.parseInt(tempcWallPaint[2])));

				//renderer.setItemMargin(Float.parseFloat(cs.getCItemMargin()));
				renderer.setItemMargin(0.3);
//				renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}",
//						new DecimalFormat("0.00"), new DecimalFormat("0.00%")));

				renderer.setItemLabelFont(new Font("宋体", Font.TRUETYPE_FONT, 11));
				renderer.setItemLabelAnchorOffset(10D);
				renderer.setItemLabelsVisible(true);
				renderer.setPositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
				renderer.setNegativeItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
				//renderer.setPositiveItemLabelPositionFallback(new ItemLabelPosition(ItemLabelAnchor.INSIDE4, TextAnchor.CENTER));
				//renderer.setNegativeItemLabelPositionFallback(new ItemLabelPosition(ItemLabelAnchor.INSIDE4, TextAnchor.CENTER));
				plot.setRenderer(renderer);
			}

			else
			{

				BarRenderer renderer = new BarRenderer();

				renderer.setItemMargin(Float.parseFloat(cs.getCItemMargin()));

//				renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}",
//						new DecimalFormat("0.00"), new DecimalFormat("0.00%")));

				renderer.setItemLabelFont(new Font("宋体", Font.PLAIN, 11));

				renderer.setItemLabelAnchorOffset(10D);
				renderer.setItemLabelsVisible(true);
				renderer.setPositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
				renderer.setNegativeItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
			
				plot.setRenderer(renderer);

			}

			if (cs.getCLabelPositions() != null && !cs.getCLabelPositions().equals(""))

				domainAxis.setCategoryLabelPositions(CategoryLabelPositions
						.createUpRotationLabelPositions(Double.parseDouble(cs.getCLabelPositions())));

			ChartUtilities.writeChartAsJPEG(output, 1.0f, chart, cs.getWidth(), cs.getHeight(), null);

			ChartUtilities.writeChartAsJPEG(Bigoutput, 1.0f, chart, 800, 450, null);
			//ChartRenderingInfo   info   =   new   ChartRenderingInfo(new   StandardEntityCollection());
			//ChartUtilities.writeImageMap(pw,outpath,   info,true);   

			output.close();

			Bigoutput.close();

		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	/**
	 * 方法说明：该方法用于转换type_str成为JfreeChart能够识别的格式
	 * @param type_str
	 * @return
	 */
	public static String changeUnitFormate(String unit_str)
	{

		String result = "";

		int unit = Integer.parseInt(unit_str);

		try
		{

			switch (unit)
			{

				case (0):

					result = "比例";
					break;

				case (1):

					result = "单位：元";
					break;

				case (2):

					result = "单位：万元";
					break;

				case (3):

					result = "单位：百万";
					break;

				case (4):

					result = "单位：亿元";
					break;

			}

		}
		catch (Exception e)
		{

			result = "单位：元";

		}

		return result;

	}

}
