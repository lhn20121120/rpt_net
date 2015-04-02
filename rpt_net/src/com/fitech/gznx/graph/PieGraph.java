package com.fitech.gznx.graph;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RectangleInsets;

/**
 * 饼图
 * @author jack
 *
 */
public class PieGraph {
	
	/**
	 * 方法说明：解析饼图部分的Xml文件
	 *
	 */
	public static void resolveXml(PieStyle ps) {
		
		Element root = ps.getGraph();
		
		GraphDataSource gs = ps.getGs();

		try {
			
			/**
			 * 解析数据部分
			 */
			Element dataRoot = (Element) root.selectNodes("dataset").get(0);
			
			if(dataRoot.selectNodes("unit")!=null && dataRoot.selectNodes("unit").size()!=0){
//				System.out.println("setUnit:"+((Element)dataRoot.selectNodes("unit").get(0)).attributeValue("title").toString());
				ps.setUnit(changeUnitFormate(((Element)dataRoot.selectNodes("unit").get(0)).attributeValue("title").toString()));
				
			}
			
			ps.setSectionColorList(new ArrayList());
			
			List itemList = dataRoot.selectNodes("item");
			
			if(itemList!=null && itemList.size()!=0){
				
				gs.setDataList(new ArrayList());
				
				for(int i=0;i<itemList.size();i++){
					
					Element e = (Element) itemList.get(i);

					String datasetBaseSql = e.attributeValue("sql"); // 得到XML文件中的查询SQL
					
					String color  = e.attributeValue("color");
					
					if(color!=null && !color.equals("")){
						
						ps.getSectionColorList().add(color);
						
					}

					String[] params = new String[3];

					params[0] = e.attributeValue("label");

					params[1] = "0";
					
					params[2] = datasetBaseSql;

					gs.getDataList().add(params);
				}
				
			}
			
			/**
			 * 解析样式
			 */
			Element styleRoot = (Element) root.selectNodes("style")
					.get(0);
			
			String tempwidth = ((Element)styleRoot.selectNodes("graphWidth").get(0)).attributeValue("value");
			
			if(tempwidth!=null && !tempwidth.equals(""))
				
				ps.setWidth(Integer.parseInt(tempwidth));
					
			
			String tempheight = ((Element)styleRoot.selectNodes("graphHeight").get(0)).attributeValue("value");
			
			if(tempheight!=null && !tempheight.equals(""))
				
				ps.setHeight(Integer.parseInt(tempheight));
			
			String tempbgcolor = ((Element)styleRoot.selectNodes("bgColorRGB").get(0)).attributeValue("value");
			
			if(tempbgcolor!=null && !tempbgcolor.equals(""))
			
				ps.setBgColorRGB(tempbgcolor);
			
			String tempttFont = ((Element)styleRoot.selectNodes("ttFont").get(0)).attributeValue("value");
			
			if(tempttFont!=null && !tempttFont.equals(""))
				
				ps.setTtFont(tempttFont);
			
			String tempttFontSize = ((Element)styleRoot.selectNodes("ttFontSize").get(0)).attributeValue("value");
			
			if(tempttFontSize!=null && !tempttFontSize.equals(""))
				
				ps.setTtFontSize(Integer.parseInt(tempttFontSize));
			
			
			String tempttColorRGB = ((Element)styleRoot.selectNodes("ttColorRGB").get(0)).attributeValue("value");
			
			if(tempttColorRGB!=null && !tempttColorRGB.equals(""))
				
				ps.setTtColorRGB(tempttColorRGB);
			
			String temppieBgColorRGB = ((Element)styleRoot.selectNodes("pieBgColorRGB").get(0)).attributeValue("value");
			
			if(temppieBgColorRGB!=null && !temppieBgColorRGB.equals(""))
				
				ps.setPieBgColorRGB(temppieBgColorRGB);
			
			String temppieLebleBgColorRGB = ((Element)styleRoot.selectNodes("pieLebleBgColorRGB").get(0)).attributeValue("value");
				
			if(temppieLebleBgColorRGB!=null && !temppieLebleBgColorRGB.equals(""))
				
				ps.setPieLebleBgColorRGB(temppieLebleBgColorRGB);
			
			String temppieLebleLinkColorRGB = ((Element)styleRoot.selectNodes("pieLebleLinkColorRGB").get(0)).attributeValue("value");
			
			if(temppieLebleLinkColorRGB!=null && !temppieLebleLinkColorRGB.equals(""))
				
				ps.setPieLebleLinkColorRGB(temppieLebleLinkColorRGB);
			
			String temppieLebleFont = ((Element)styleRoot.selectNodes("pieLebleFont").get(0)).attributeValue("value");
			
			if(temppieLebleFont!=null && !temppieLebleFont.equals(""))
				
				ps.setPieLebleFont(temppieLebleFont);
			
			String temppieLebleFontSize = ((Element)styleRoot.selectNodes("pieLebleFontSize").get(0)).attributeValue("value");
			
			if(temppieLebleFontSize!=null && !temppieLebleFontSize.equals(""))
				
				ps.setPieLebleFontSize(Integer.parseInt(temppieLebleFontSize));
			
			String temppieLebleGenerator = ((Element)styleRoot.selectNodes("pieLebleGenerator").get(0)).attributeValue("value");
			
			if(temppieLebleGenerator!=null && !temppieLebleGenerator.equals(""))
				
				ps.setPieLebleGenerator(changeFormate(temppieLebleGenerator ,ps));
			
			String temppieLebleFontColorRGB = ((Element)styleRoot.selectNodes("pieLebleFontColorRGB").get(0)).attributeValue("value");
			
			if(temppieLebleFontColorRGB!=null && !temppieLebleFontColorRGB.equals(""))
				
				ps.setPieLebleFontColorRGB(temppieLebleFontColorRGB);
			
			String temppieLebleShadowColorRGB = ((Element)styleRoot.selectNodes("pieLebleShadowColorRGB").get(0)).attributeValue("value");
			
			if(temppieLebleShadowColorRGB!=null && !temppieLebleShadowColorRGB.equals(""))
				
				ps.setPieLebleShadowColorRGB(temppieLebleShadowColorRGB);
			
			String temppieLegendLabelGenerator = ((Element)styleRoot.selectNodes("pieLegendLabelGenerator").get(0)).attributeValue("value");
			
			if(temppieLegendLabelGenerator!=null && !temppieLegendLabelGenerator.equals(""))
				
				ps.setPieLegendLabelGenerator(changeFormate(temppieLegendLabelGenerator,ps));
			
			String temppieRange = ((Element)styleRoot.selectNodes("pieRange").get(0)).attributeValue("value");
			
			if(temppieRange!=null && !temppieRange.equals(""))
				
				ps.setPieRange(temppieRange);
			
			String tempis3DPie = ((Element)styleRoot.selectNodes("is3DPie").get(0)).attributeValue("value");
			
			if(tempis3DPie!=null && tempis3DPie.equals("y"))
				
				ps.setIs3DPie(true);
			
			else 
				
				ps.setIs3DPie(false);
			
			String tempisCircular = ((Element)styleRoot.selectNodes("isCircular").get(0)).attributeValue("value");
			
			if(tempisCircular!=null && tempisCircular.equals("y"))
				
				ps.setCircular(true);
			
			else
				
				ps.setCircular(false);
			
			String temppiedept = ((Element)styleRoot.selectNodes("piedept").get(0)).attributeValue("value");
			
			if(temppiedept!=null && !temppiedept.equals(""))
				
				ps.setPiedept(temppiedept);
			
			String temppieForegroundAlpha = ((Element)styleRoot.selectNodes("pieForegroundAlpha").get(0)).attributeValue("value");
			
			if(temppieForegroundAlpha!=null && !temppieForegroundAlpha.equals(""))
				
				ps.setPieForegroundAlpha(temppieForegroundAlpha);
			
			String temppieStartAngle = ((Element)styleRoot.selectNodes("pieStartAngle").get(0)).attributeValue("value");
			
			if(temppieStartAngle!=null && !temppieStartAngle.equals(""))
				
				ps.setPieStartAngle(temppieStartAngle);
			
			String temppieOutlineColorRGB = ((Element)styleRoot.selectNodes("pieOutlineColorRGB").get(0)).attributeValue("value"); 
			
			if(temppieOutlineColorRGB!=null && !temppieOutlineColorRGB.equals(""))
				
				ps.setPieOutlineColorRGB(temppieOutlineColorRGB);
			

		} catch (Exception e) {

			e.printStackTrace();

		}

	}
	
	/**
	 * 方法说明：生成饼图
	 * @param graphName
	 * @param outpath
	 */
	public static void createGraph(PieStyle ps, String outpath , String Bigoutpath) {
		
		GraphDataSource gs = ps.getGs();
		
		String graphName = ps.getGraph().attributeValue("title");
		
		try {

			OutputStream output = new FileOutputStream(new File(outpath));
			
			OutputStream Bigoutput = new FileOutputStream(new File(Bigoutpath));

			DefaultPieDataset dataset = new DefaultPieDataset();

			List list = gs.getDataList();
			//String tempUni = ps.getUnit();
			System.out.println("价格单位： "+ps.getUnit());
			String tempUnit = "元";
			DecimalFormat df = new DecimalFormat("0.00");
			
			for (int i = 0; i < list.size(); i++) { // 置数

				String[] strs = (String[]) list.get(i);

				String label = strs[0];

				double value = 0;

				if (!strs[1].equals("0")) {

					value = Double.parseDouble(strs[1]);
					
					if(tempUnit.equals("元"))
						;
					
					else if(tempUnit.equals("万元")){
						
						value = Double.parseDouble(df.format(value/10000));
						
					}
					
					else if(tempUnit.equals("百万"))
						
						value = Double.parseDouble(df.format(value/1000000));
					
					else if(tempUnit.equals("亿元"))
						
						value = Double.parseDouble(df.format(value/100000000));

				}
	
				dataset.setValue(label, value);
			}
			
			String total_value = "";
			
			Comparable total_label = null;
			
	
			
			if(graphName.equals("收入项目结构")||graphName.equals("支出项目结构")||graphName.equals("资产总体情况")||graphName.equals("存款余额结构情况")||graphName.equals("行业投放情况")){
				
				total_value = df.format(dataset.getValue(0));
				
				total_label = dataset.getKey(0);
				
				
			}
			
			if(graphName.equals("负债及所有者权益情况")){
				
				total_label = "总负债+所有者权益";
				
				total_value = df.format(dataset.getValue(0).doubleValue()+dataset.getValue("所有者权益").doubleValue());
				
//				dataset.remove("总负债");
			}
			
			DefaultPieDataset ds = new DefaultPieDataset();
			for (int i=0 ;i<dataset.getItemCount();i++){
				if(dataset.getKey(i).equals(total_label))
					continue;
				ds.setValue(dataset.getKey(i), dataset.getValue(i));
			}
			
			
			
			formateDataSet(ds,graphName);
			
			JFreeChart jfreechart = null;

			if (ps.isIs3DPie())

				jfreechart = ChartFactory.createPieChart3D(graphName, ds,
						true, true, false); // 定义jfreechart对象

			else

				jfreechart = ChartFactory.createPieChart(graphName, ds,
						true, true, false);

			/** *设置饼图的样式** */

			String[] tempbgColorRGB = ps.getBgColorRGB().split(",");

			jfreechart.setBackgroundPaint(new Color(Integer
					.parseInt(tempbgColorRGB[0]), Integer
					.parseInt(tempbgColorRGB[1]), Integer
					.parseInt(tempbgColorRGB[2]))); // 设置图像背景色

			/** 设置标题的样式* */
			Font ft = new Font(ps.getTtFont(), Font.BOLD, ps.getTtFontSize());

			TextTitle tt = new TextTitle(graphName, ft);

			String[] tempttColorRGB = ps.getTtColorRGB().split(",");

			tt.setPaint(new Color(Integer.parseInt(tempttColorRGB[0]), Integer
					.parseInt(tempttColorRGB[1]), Integer
					.parseInt(tempttColorRGB[2])));

			jfreechart.setTitle(tt);
			
			TextTitle subtitle  = null;
			
			if(graphName.equals("收入项目结构")||graphName.equals("支出项目结构")||graphName.equals("资产总体情况")||graphName.equals("负债及所有者权益情况")||graphName.equals("存款余额结构情况")||graphName.equals("行业投放情况")){
				
				subtitle =new TextTitle("("+total_label+":"+total_value+ps.getUnit()+")", new Font("宋体", Font.PLAIN, 10));

				jfreechart.addSubtitle(subtitle);
				
			}

			PiePlot pieplot = (PiePlot) jfreechart.getPlot(); // 设置饼图的属性
			
			pieplot.setIgnoreNullValues(true);
			
			List tempSectionList = ps.getSectionColorList();
			
			if(tempSectionList!=null && tempSectionList.size()!=0){
				
				for(int i=0;i<tempSectionList.size();i++){
					
					String sectionColorStr = (String)tempSectionList.get(i);
					
					String[] tempColor =  sectionColorStr.split(",");
					
					pieplot.setSectionPaint(i,new Color(Integer.parseInt(tempColor[0]),Integer.parseInt(tempColor[1]),Integer.parseInt(tempColor[2])));
					
				}
				
			}

			String[] temppieBgColorRGB = ps.getPieBgColorRGB().split(",");

			pieplot.setBackgroundPaint(new Color(Integer
					.parseInt(temppieBgColorRGB[0]), Integer
					.parseInt(temppieBgColorRGB[1]), Integer
					.parseInt(temppieBgColorRGB[2])));

			String[] temppieLebleBgColorRGB = ps.getPieLebleBgColorRGB().split(",");

			pieplot.setLabelBackgroundPaint(new Color(Integer
					.parseInt(temppieLebleBgColorRGB[0]), Integer
					.parseInt(temppieLebleBgColorRGB[1]), Integer
					.parseInt(temppieLebleBgColorRGB[2])));

			String[] temppieLebleLinkColorRGB = ps.getPieLebleLinkColorRGB().split(",");

			pieplot.setLabelLinkPaint(new Color(Integer
					.parseInt(temppieLebleLinkColorRGB[0]), Integer
					.parseInt(temppieLebleLinkColorRGB[1]), Integer
					.parseInt(temppieLebleLinkColorRGB[2])));

			pieplot.setLabelFont(new Font(ps.getPieLebleFont(), Font.PLAIN,
					ps.getPieLebleFontSize()));

			if (!ps.getPieLebleGenerator().equals(""))

				pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator(
						ps.getPieLebleGenerator(), new DecimalFormat(),
						new DecimalFormat("0.00%")));

			String[] temppieLebleFontColorRGB = ps.getPieLebleFontColorRGB().split(",");

			pieplot.setLabelPaint(new Color(Integer
					.parseInt(temppieLebleFontColorRGB[0]), Integer
					.parseInt(temppieLebleFontColorRGB[1]), Integer
					.parseInt(temppieLebleFontColorRGB[2])));

			String[] temppieLebleShadowColorRGB = ps.getPieLebleShadowColorRGB()
					.split(",");

			pieplot.setLabelShadowPaint(new Color(Integer
					.parseInt(temppieLebleShadowColorRGB[0]), Integer
					.parseInt(temppieLebleShadowColorRGB[1]), Integer
					.parseInt(temppieLebleShadowColorRGB[2])));

			if (!ps.getPieLegendLabelGenerator().equals("")){
				//{0}表示选项，{1}表示数值，{2}表示所占比例
				pieplot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}:({1}, {2})"));
				pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}:({1},{2})"));
				
			}

			String[] temppieRange = ps.getPieRange().split(",");

			pieplot.setInsets(new RectangleInsets(Double
					.parseDouble(temppieRange[0]), Double
					.parseDouble(temppieRange[1]), Double
					.parseDouble(temppieRange[2]), Double
					.parseDouble(temppieRange[3])));

			pieplot.setNoDataMessage("没有数据");

			pieplot.setIgnoreNullValues(true);

			pieplot.setCircular(ps.isCircular());

			pieplot.setStartAngle(Double.parseDouble(ps.getPieStartAngle()));

			String[] temppieOutlineColorRGB = ps.getPieOutlineColorRGB().split(",");

			pieplot.setOutlinePaint(new Color(Integer
					.parseInt(temppieOutlineColorRGB[0]), Integer
					.parseInt(temppieOutlineColorRGB[1]), Integer
					.parseInt(temppieOutlineColorRGB[2])));

			if (ps.isIs3DPie()) {

				PiePlot3D plot3D = (PiePlot3D) jfreechart.getPlot();
				;

				plot3D.setDepthFactor(Double.parseDouble(ps.getPiedept()));

				plot3D.setForegroundAlpha(Float.parseFloat(ps.getPieForegroundAlpha())); // 透明度

			}

			ChartUtilities.writeChartAsJPEG(output, 1.0f, jfreechart, ps.getWidth(),
					ps.getHeight(), null);
			
			if(graphName.equals("收入项目结构")||graphName.equals("支出项目结构")||graphName.equals("资产总体情况")||graphName.equals("负债及所有者权益情况")||graphName.equals("存款余额结构情况")||graphName.equals("行业投放情况")){
				
				TextTitle subtitleTemp =new TextTitle("("+total_label+":"+total_value+ps.getUnit()+")", new Font("宋体", Font.PLAIN, 12));
				
				jfreechart.removeSubtitle(subtitle);

				jfreechart.addSubtitle(subtitleTemp);
				
			}
			
			ChartUtilities.writeChartAsJPEG(Bigoutput, 1.0f, jfreechart, 800,
					450, null);

			output.close();
			
			Bigoutput.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}
	
	/**
	 * 方法说明：该方法用于转换type_str成为JfreeChart能够识别的格式
	 * @param type_str
	 * @return
	 */
	public static String changeFormate(String type_str,PieStyle ps) {

		String result = "";
		
		String tempUnit = ps.getUnit();

		try {

			if (type_str != null) {

				int type = Integer.parseInt(type_str);

				switch (type) {

				case (0):

					result = ""; //名称
					break;

				case (1):

					result = "{0}: ({1}"+tempUnit+")"; //名称+数值
					break;

				case (2):

					result = "{0}: ({2})"; //名称+百分比
					break;

				case (3):

					result = "{0}: ({1}"+tempUnit+", {2})"; //名称+数值+百分比
					break;

				case (4):

					result = "{2}"; //百分比
					break;

				case (5):

					result = "{1}"; //数值
				}
			}

		} catch (Exception e) {

			result = "";

		}

		return result;

	}
	
	/**
	 * 方法说明：该方法用于转换type_str成为JfreeChart能够识别的格式
	 * @param type_str
	 * @return
	 */
	public static String changeUnitFormate(String unit_str) {

		String result = "";
		
		int unit = Integer.parseInt(unit_str); 

		try {

				switch (unit) {

				case (1):

					result = "元"; 
					break;

				case (2):

					result = "万元"; 
					break;

				case (3):

					result = "百万"; 
					break;

				case (4):

					result = "亿元"; 
					break;

			}
			

		} catch (Exception e) {

			result = "元";

		}

		return result;

	}
	
	
	public static void formateDataSet(DefaultPieDataset dataset,String graphName){
		
		if(graphName.equals("资产总体情况")){
			
			double temp_1 = dataset.getValue("在人行准备金存款").doubleValue();
			
			temp_1 = temp_1 - dataset.getValue("在人行准备金存款temp").doubleValue();
			
			dataset.setValue("在人行准备金存款" , temp_1);
			
//			dataset.remove("在人行准备金存款temp");
			
			double temp_2 = dataset.getValue("其他资产").doubleValue();
			
			temp_2 = temp_2 - dataset.getValue("各项贷款").doubleValue();
			
			temp_2 = temp_2 - dataset.getValue("有价证券及投资").doubleValue();
			
			temp_2 = temp_2 - dataset.getValue("在人行准备金存款").doubleValue();
			
			temp_2 = temp_2 - dataset.getValue("买入返售证券").doubleValue();
			
			temp_2 = temp_2 - dataset.getValue("存放同业").doubleValue();
			
			temp_2 = temp_2 - dataset.getValue("拆放同业").doubleValue();
			
			temp_2 = temp_2 - dataset.getValue("委托贷款").doubleValue();
			
			temp_2 = temp_2 - dataset.getValue("库存现金").doubleValue();
			
			dataset.setValue("其他资产" , temp_2);
			
		}
		
		else if(graphName.equals("负债及所有者权益情况")){
			
			double temp = dataset.getValue("其他负债").doubleValue();
			
			//System.out.println(temp);
			
			temp = temp - dataset.getValue("各项存款").doubleValue();
			
			temp = temp - dataset.getValue("向中央银行借款").doubleValue();
			
			temp = temp - dataset.getValue("卖出回购证券").doubleValue();
			
			temp = temp - dataset.getValue("同业存放").doubleValue();
			
			temp = temp - dataset.getValue("同业拆借").doubleValue();
			
			temp = temp - dataset.getValue("委托存款").doubleValue();
			
			//System.out.println(temp);
			
			dataset.setValue("其他负债" , temp);
			
		}
		
		else if(graphName.equals("行业投放情况")){
			
			double temp = dataset.getValue("制造业").doubleValue();
			
			temp = temp - dataset.getValue("农、林、牧、渔业").doubleValue();
			
			temp = temp - dataset.getValue("采矿业").doubleValue();
			
			temp = temp - dataset.getValue("电力、煤气和水的生产和供应业").doubleValue();
			
			temp = temp - dataset.getValue("建筑业").doubleValue();
			
			temp = temp - dataset.getValue("交通运输、仓储及邮电通信业").doubleValue();
			
			temp = temp - dataset.getValue("信息传输、计算机服务和软件业").doubleValue();
			
			temp = temp - dataset.getValue("批发和零售业").doubleValue();
			
			temp = temp - dataset.getValue("住宿和餐饮业").doubleValue();
			
			temp = temp - dataset.getValue("金融业").doubleValue();
			
			temp = temp - dataset.getValue("房地产业").doubleValue();
			
			temp = temp - dataset.getValue("租赁和商务服务业").doubleValue();
			
			temp = temp - dataset.getValue("科学研究、技术服务和地质勘查业").doubleValue();
			
			temp = temp - dataset.getValue("水利、环境和公共设施管理业").doubleValue();
			
			temp = temp - dataset.getValue("居民服务和其他服务业").doubleValue();
			
			temp = temp - dataset.getValue("教育").doubleValue();
			
			temp = temp - dataset.getValue("卫生、社会保障和社会福利业").doubleValue();
			
			temp = temp - dataset.getValue("文化、体育和娱乐业").doubleValue();
			
			temp = temp - dataset.getValue("公共管理和社会组织").doubleValue();
			
			temp = temp - dataset.getValue("国际组织").doubleValue();
			
			dataset.setValue("制造业" , temp);
			
		}
		
	}

}
