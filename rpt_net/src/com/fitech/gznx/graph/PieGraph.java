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
 * ��ͼ
 * @author jack
 *
 */
public class PieGraph {
	
	/**
	 * ����˵����������ͼ���ֵ�Xml�ļ�
	 *
	 */
	public static void resolveXml(PieStyle ps) {
		
		Element root = ps.getGraph();
		
		GraphDataSource gs = ps.getGs();

		try {
			
			/**
			 * �������ݲ���
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

					String datasetBaseSql = e.attributeValue("sql"); // �õ�XML�ļ��еĲ�ѯSQL
					
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
			 * ������ʽ
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
	 * ����˵�������ɱ�ͼ
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
			System.out.println("�۸�λ�� "+ps.getUnit());
			String tempUnit = "Ԫ";
			DecimalFormat df = new DecimalFormat("0.00");
			
			for (int i = 0; i < list.size(); i++) { // ����

				String[] strs = (String[]) list.get(i);

				String label = strs[0];

				double value = 0;

				if (!strs[1].equals("0")) {

					value = Double.parseDouble(strs[1]);
					
					if(tempUnit.equals("Ԫ"))
						;
					
					else if(tempUnit.equals("��Ԫ")){
						
						value = Double.parseDouble(df.format(value/10000));
						
					}
					
					else if(tempUnit.equals("����"))
						
						value = Double.parseDouble(df.format(value/1000000));
					
					else if(tempUnit.equals("��Ԫ"))
						
						value = Double.parseDouble(df.format(value/100000000));

				}
	
				dataset.setValue(label, value);
			}
			
			String total_value = "";
			
			Comparable total_label = null;
			
	
			
			if(graphName.equals("������Ŀ�ṹ")||graphName.equals("֧����Ŀ�ṹ")||graphName.equals("�ʲ��������")||graphName.equals("������ṹ���")||graphName.equals("��ҵͶ�����")){
				
				total_value = df.format(dataset.getValue(0));
				
				total_label = dataset.getKey(0);
				
				
			}
			
			if(graphName.equals("��ծ��������Ȩ�����")){
				
				total_label = "�ܸ�ծ+������Ȩ��";
				
				total_value = df.format(dataset.getValue(0).doubleValue()+dataset.getValue("������Ȩ��").doubleValue());
				
//				dataset.remove("�ܸ�ծ");
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
						true, true, false); // ����jfreechart����

			else

				jfreechart = ChartFactory.createPieChart(graphName, ds,
						true, true, false);

			/** *���ñ�ͼ����ʽ** */

			String[] tempbgColorRGB = ps.getBgColorRGB().split(",");

			jfreechart.setBackgroundPaint(new Color(Integer
					.parseInt(tempbgColorRGB[0]), Integer
					.parseInt(tempbgColorRGB[1]), Integer
					.parseInt(tempbgColorRGB[2]))); // ����ͼ�񱳾�ɫ

			/** ���ñ������ʽ* */
			Font ft = new Font(ps.getTtFont(), Font.BOLD, ps.getTtFontSize());

			TextTitle tt = new TextTitle(graphName, ft);

			String[] tempttColorRGB = ps.getTtColorRGB().split(",");

			tt.setPaint(new Color(Integer.parseInt(tempttColorRGB[0]), Integer
					.parseInt(tempttColorRGB[1]), Integer
					.parseInt(tempttColorRGB[2])));

			jfreechart.setTitle(tt);
			
			TextTitle subtitle  = null;
			
			if(graphName.equals("������Ŀ�ṹ")||graphName.equals("֧����Ŀ�ṹ")||graphName.equals("�ʲ��������")||graphName.equals("��ծ��������Ȩ�����")||graphName.equals("������ṹ���")||graphName.equals("��ҵͶ�����")){
				
				subtitle =new TextTitle("("+total_label+":"+total_value+ps.getUnit()+")", new Font("����", Font.PLAIN, 10));

				jfreechart.addSubtitle(subtitle);
				
			}

			PiePlot pieplot = (PiePlot) jfreechart.getPlot(); // ���ñ�ͼ������
			
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
				//{0}��ʾѡ�{1}��ʾ��ֵ��{2}��ʾ��ռ����
				pieplot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}:({1}, {2})"));
				pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}:({1},{2})"));
				
			}

			String[] temppieRange = ps.getPieRange().split(",");

			pieplot.setInsets(new RectangleInsets(Double
					.parseDouble(temppieRange[0]), Double
					.parseDouble(temppieRange[1]), Double
					.parseDouble(temppieRange[2]), Double
					.parseDouble(temppieRange[3])));

			pieplot.setNoDataMessage("û������");

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

				plot3D.setForegroundAlpha(Float.parseFloat(ps.getPieForegroundAlpha())); // ͸����

			}

			ChartUtilities.writeChartAsJPEG(output, 1.0f, jfreechart, ps.getWidth(),
					ps.getHeight(), null);
			
			if(graphName.equals("������Ŀ�ṹ")||graphName.equals("֧����Ŀ�ṹ")||graphName.equals("�ʲ��������")||graphName.equals("��ծ��������Ȩ�����")||graphName.equals("������ṹ���")||graphName.equals("��ҵͶ�����")){
				
				TextTitle subtitleTemp =new TextTitle("("+total_label+":"+total_value+ps.getUnit()+")", new Font("����", Font.PLAIN, 12));
				
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
	 * ����˵�����÷�������ת��type_str��ΪJfreeChart�ܹ�ʶ��ĸ�ʽ
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

					result = ""; //����
					break;

				case (1):

					result = "{0}: ({1}"+tempUnit+")"; //����+��ֵ
					break;

				case (2):

					result = "{0}: ({2})"; //����+�ٷֱ�
					break;

				case (3):

					result = "{0}: ({1}"+tempUnit+", {2})"; //����+��ֵ+�ٷֱ�
					break;

				case (4):

					result = "{2}"; //�ٷֱ�
					break;

				case (5):

					result = "{1}"; //��ֵ
				}
			}

		} catch (Exception e) {

			result = "";

		}

		return result;

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

				case (1):

					result = "Ԫ"; 
					break;

				case (2):

					result = "��Ԫ"; 
					break;

				case (3):

					result = "����"; 
					break;

				case (4):

					result = "��Ԫ"; 
					break;

			}
			

		} catch (Exception e) {

			result = "Ԫ";

		}

		return result;

	}
	
	
	public static void formateDataSet(DefaultPieDataset dataset,String graphName){
		
		if(graphName.equals("�ʲ��������")){
			
			double temp_1 = dataset.getValue("������׼������").doubleValue();
			
			temp_1 = temp_1 - dataset.getValue("������׼������temp").doubleValue();
			
			dataset.setValue("������׼������" , temp_1);
			
//			dataset.remove("������׼������temp");
			
			double temp_2 = dataset.getValue("�����ʲ�").doubleValue();
			
			temp_2 = temp_2 - dataset.getValue("�������").doubleValue();
			
			temp_2 = temp_2 - dataset.getValue("�м�֤ȯ��Ͷ��").doubleValue();
			
			temp_2 = temp_2 - dataset.getValue("������׼������").doubleValue();
			
			temp_2 = temp_2 - dataset.getValue("���뷵��֤ȯ").doubleValue();
			
			temp_2 = temp_2 - dataset.getValue("���ͬҵ").doubleValue();
			
			temp_2 = temp_2 - dataset.getValue("���ͬҵ").doubleValue();
			
			temp_2 = temp_2 - dataset.getValue("ί�д���").doubleValue();
			
			temp_2 = temp_2 - dataset.getValue("����ֽ�").doubleValue();
			
			dataset.setValue("�����ʲ�" , temp_2);
			
		}
		
		else if(graphName.equals("��ծ��������Ȩ�����")){
			
			double temp = dataset.getValue("������ծ").doubleValue();
			
			//System.out.println(temp);
			
			temp = temp - dataset.getValue("������").doubleValue();
			
			temp = temp - dataset.getValue("���������н��").doubleValue();
			
			temp = temp - dataset.getValue("�����ع�֤ȯ").doubleValue();
			
			temp = temp - dataset.getValue("ͬҵ���").doubleValue();
			
			temp = temp - dataset.getValue("ͬҵ���").doubleValue();
			
			temp = temp - dataset.getValue("ί�д��").doubleValue();
			
			//System.out.println(temp);
			
			dataset.setValue("������ծ" , temp);
			
		}
		
		else if(graphName.equals("��ҵͶ�����")){
			
			double temp = dataset.getValue("����ҵ").doubleValue();
			
			temp = temp - dataset.getValue("ũ���֡�������ҵ").doubleValue();
			
			temp = temp - dataset.getValue("�ɿ�ҵ").doubleValue();
			
			temp = temp - dataset.getValue("������ú����ˮ�������͹�Ӧҵ").doubleValue();
			
			temp = temp - dataset.getValue("����ҵ").doubleValue();
			
			temp = temp - dataset.getValue("��ͨ���䡢�ִ����ʵ�ͨ��ҵ").doubleValue();
			
			temp = temp - dataset.getValue("��Ϣ���䡢�������������ҵ").doubleValue();
			
			temp = temp - dataset.getValue("����������ҵ").doubleValue();
			
			temp = temp - dataset.getValue("ס�޺Ͳ���ҵ").doubleValue();
			
			temp = temp - dataset.getValue("����ҵ").doubleValue();
			
			temp = temp - dataset.getValue("���ز�ҵ").doubleValue();
			
			temp = temp - dataset.getValue("���޺��������ҵ").doubleValue();
			
			temp = temp - dataset.getValue("��ѧ�о�����������͵��ʿ���ҵ").doubleValue();
			
			temp = temp - dataset.getValue("ˮ���������͹�����ʩ����ҵ").doubleValue();
			
			temp = temp - dataset.getValue("����������������ҵ").doubleValue();
			
			temp = temp - dataset.getValue("����").doubleValue();
			
			temp = temp - dataset.getValue("��������ᱣ�Ϻ���ḣ��ҵ").doubleValue();
			
			temp = temp - dataset.getValue("�Ļ�������������ҵ").doubleValue();
			
			temp = temp - dataset.getValue("��������������֯").doubleValue();
			
			temp = temp - dataset.getValue("������֯").doubleValue();
			
			dataset.setValue("����ҵ" , temp);
			
		}
		
	}

}
