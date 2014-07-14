package com.fitech.net.Excel2Xml;

import java.io.File;
import java.io.FileWriter;

import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.system.cb.InputData;
/**
 * 生成点式方法类
 * 
 * @author GEN
 * 
 */
public class P2P2Xml {
	/**
	 * 把普通的excel生成xml
	 * 
	 * @param excel
	 * @param xml
	 * @param v
	 *            行变量 用于调节excel和pdf的行差
	 * @return
	 */
	public static String year = "";

	public static String month = "";

	public static Document excel2xml_common(String excel, Name name, int v, Integer template_type,String subtitle) {
		Document document = null;
		try {
			File inputWorkbook;
			inputWorkbook = new File(excel);
			Workbook w1 = Workbook.getWorkbook(inputWorkbook);

			String col[] = null;

			SAXReader saxReader = new SAXReader();
			String modelurl = Util.getFullPathRelateClass("model.xml", P2P2Xml.class);
			document = saxReader.read(new File(modelurl));
			Element root = document.getRootElement();
			Element p = root.element("P1");
			int titleRow=0;
			int titleCol=0;
			for (int sheet = 0; sheet < w1.getNumberOfSheets(); sheet++) {

				Sheet s = w1.getSheet(sheet);
				Cell[] row = null;
				NumberCell numberCell = null;
				if(template_type.equals(com.fitech.net.config.Config.FZ_TEMPLATE)){
				// 添加小标题
					Element subtitleElement = p.addElement("fitechSubtitle");
					subtitleElement.setText(subtitle);
				}
				/** 判断模板类型 */
				if(template_type.equals(com.fitech.net.config.Config.FZ_TEMPLATE)) { // 判断是否为报送口径			
						setElement(p, "fitechRange", com.fitech.net.config.Config.reportRange);
						// 数据范围id
						name.setRangId(Util.getRangeId( com.fitech.net.config.Config.reportRange));				
				}

				for (int i = 0; i < s.getRows(); i++) {

					row = s.getRow(i);

					//如果不是的一行第一列为标题的话则是第二行第二列
					if(i==0){
						if (row!=null&&row.length>0&&row[0].getType() != CellType.EMPTY) {
							
						}else {
							titleRow=1;
							titleCol=1;
							
						}
					}
					col = new String[row.length];
					for (int j = 0; j < row.length; j++) {
						if (row[j].getType() != CellType.EMPTY) {
							col[j] = row[j].getContents();

							// pdfxml与excel行号相差1 k为pdfxml的行号
							int k = i + 1 + v;

							// 判断是否是标题
							if (i == titleRow&&j==titleCol) {
								if(titleRow!=0&&row!=null&&row.length>2&&row[1].getType() != CellType.EMPTY){
									setElement(p, "fitechTitle", col[j].replaceAll(" ", ""));
									name.setRepId(StrutsMChildReportDelegate.getChildRepId(col[j].trim()));
								}else{
									//G04没有子报表ID只好如此
									String temp=null;
									if( col[j].indexOf("GF04利润表")>-1){
										Cell[] row1=s.getRow(2);
										if(row1!=null&&row1.length>0&&row1[0].getType() != CellType.EMPTY){
											if(row1[0].getContents().indexOf("附注项目")>-1){
												temp="GF04利润表附注";
											}
										}
									}
									if(temp==null){
										temp=col[j];
									}
									setElement(p, "fitechTitle", temp);
									name.setRepId(StrutsMChildReportDelegate.getChildRepId(temp.trim()));
								}
								
								continue;
							}
						
								if (col[j].indexOf("报送口径") > -1) {
									setElement(p, "fitechRange", col[j].substring(col[j].indexOf("报送口径") + 5));
									// 数据范围id
									name.setRangId(Util.getRangeId(col[j].substring(col[j].indexOf("报送口径") + 5)));
									continue;
								}								
							
						
								if (col[j].indexOf("报表日期") > -1) {
									year = "";
									month = "";
									if (col[j].indexOf("年") > -1 && col[j].indexOf("月") > -1) {
										if (col[j].indexOf("报表日期：") > -1) {
											String src = col[j];
											year = src.substring(src.indexOf("报表日期：") +"报表日期：".length(), src.indexOf("年")).trim();
											src = col[j];
											month = src.substring(src.indexOf("年") + 1, src.indexOf("月")).trim();
										} else {
											String src = col[j];
											year = src.substring(src.indexOf("期") + 1, src.indexOf("年")).trim();
											src = col[j];
											month = src.substring(src.indexOf("年") + 1, src.indexOf("月")).trim();
										}
										year = year.replaceAll("　", "");
										year = year.trim().replaceAll("　", "");
										month = month.replaceAll("　", "");
										month = month.trim().replaceAll("　", "");
										setElement(p, "fitechSubmitYear", year);
										setElement(p, "fitechSubmitMonth", month);
										if (year.equals("") || month.equals("")) {
											//UploadFileActionold.messInfo = "报表报送失败，请输入报送日期！";
											InputData.messageInfo = "报表报送失败，请输入报表日期！";
											return null;
										}
									}
									if(template_type.equals(com.fitech.net.config.Config.FR_TEMPLATE)) {
										continue;
									}
								}
														
							
							
							// 判断是否为货币单位
							if (col[j].indexOf("货币单位") > -1) {
								setElement(p, "fitechUnit", col[j].substring(col[j].indexOf("货币单位") + 5));
								continue;
							}

							// 是否为填报人、复核人、负责人
							if (col[j].indexOf("填表人") > -1) {
								String tbr = col[j].substring(col[j].indexOf("填表人") + 4);
								//中软那些鸟人把填表人和复核人放到一个格子里，搞得老子还得判断长度
								if(tbr.length()>5){
									tbr=tbr.substring(0, 5);
								}
								setElement(p, "fitechFiller", tbr);
								tbr = tbr.replaceAll("　", "");
								tbr = tbr.trim().replaceAll("　", "");
								/*
								 * if(tbr.equals("") ){
								 * UploadFileAction.messInfo = "报表报送失败，请输入填表人！";
								 * InputData.messageInfo = "报表报送失败，请输入填表人！";
								 * return null; }
								 */
								if(template_type.equals(com.fitech.net.config.Config.FR_TEMPLATE)) {
									continue;
								}
							}
							
							
							
							if (col[j].indexOf("复核人") > -1) {
								String fhr = col[j].substring(col[j].indexOf("填表人") + 4);
								
								if(fhr.length()>5){
									fhr=fhr.substring(0, 5);
								}
								setElement(p, "fitechChecker", fhr);
								if(template_type.equals(com.fitech.net.config.Config.FR_TEMPLATE)) {
									continue;
								}
							}
							if (col[j].indexOf("负责人") > -1) {
								setElement(p, "fitechPrincipal", col[j].substring(col[j].indexOf("负责人") + 4));
								if(template_type.equals(com.fitech.net.config.Config.FR_TEMPLATE)) {
									continue;
								}
							}

							if (p.element("fitechVersion") != null) {
								Element e = p.element("fitechVersion");
								e.setText(name.getVersionid());
							}
							
							if (row[j].getType() == CellType.NUMBER) {
							//	numberCell = (NumberCell) row[j];
							//	col[j] = String.valueOf(numberCell.getValue());
								col[j] = row[j].getContents();
							} else if (row[j].getType() == CellType.NUMBER_FORMULA) {
								if (col[j].indexOf("\"") > -1) {
									col[j] = col[j].substring(col[j].indexOf("\"") + 1);
									if (col[j].indexOf("\"") > -1)
										col[j] = col[j].substring(col[j].indexOf("\"") + 1);
								} else if (col[j].indexOf("$") > -1) {
									col[j] = col[j].substring(col[j].indexOf("$") + 1);
								}
								if (col[j].indexOf(",") > -1)
									col[j] = col[j].replaceAll(",", "");
								if (col[j].indexOf("%") > -1 && col[j].indexOf("%") == col[j].length() - 1) {
									try {
										col[j] = String.valueOf(Double.parseDouble(col[j].substring(0, col[j].indexOf("%"))));
									} catch (Exception ex) {
									}
								}
							}
							// // System.out.println("####col["+j+"]="+col[j]);
							Element g = p.addElement(Util.indexCol(j) + k);
							g.setText(col[j]);
						}
					}
				}
			}
			w1.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return document;
	}

	/**
	 * 把拆分报表得excel生成xml
	 * 
	 * @param excel
	 * @param xml
	 * @return
	 */
	public static Document excel2xml_special(String excel, Name name, Part part) {
		Document document = null;
		try {
			File inputWorkbook;
			inputWorkbook = new File(excel);
			Workbook w1 = Workbook.getWorkbook(inputWorkbook);

			String col[] = null;

			SAXReader saxReader = new SAXReader();
			String modelurl = Util.getFullPathRelateClass("model.xml", P2P2Xml.class);
			document = saxReader.read(new File(modelurl));
			Element root = document.getRootElement();
			Element p = root.element("P1");

			for (int sheet = 0; sheet < w1.getNumberOfSheets(); sheet++) {

				Sheet s = w1.getSheet(sheet);
				NumberCell numberCell = null;
				Cell[] row = null;
				for (int i = 0; i < s.getRows(); i++) {

					row = s.getRow(i);
					col = new String[row.length];
					for (int j = 0; j < row.length; j++) {
						if (row[j].getType() != CellType.EMPTY) {
							col[j] = row[j].getContents();

							// pdfxml与excel行号相差1 k为pdfxml的行号

							int k = i + 1 + Integer.parseInt(part.getStartRow());

							// 判断是否是标题
							if (i == 0) {
								setElement(p, "fitechTitle", col[j]);
								continue;
							}

						
								if (col[j].indexOf("报送口径") > -1) {

									setElement(p, "fitechRange", col[j].substring(col[j].indexOf("报送口径") + 5));

									// 数据范围id
									name.setRangId(Util.getRangeId(col[j].substring(col[j].indexOf("报送口径") + 5)));
									continue;
								}
								
							

							// 判断是否为小标题
							if (col[j].trim().equals(part.getSubtitle())) {
								Element subtitle = p.addElement("fitechSubtitle");
								subtitle.setText(col[j].trim());

								continue;
							}
							// 判断是否是多币种
							if (col[j].indexOf("币种") > -1) {
								setElement(p, "fitechCurr", col[j].substring(col[j].indexOf("币种") + 3));
								continue;
							}

							// 判断是否为报表日期
							if (col[j].indexOf("报表日期") > -1) {
								year = "";
								month = "";
								if (col[j].indexOf("年") > -1 && col[j].indexOf("月") > -1) {
									if (col[j].indexOf("：") > -1) {
										String src = col[j];
										year = src.substring(src.indexOf("：") + 1, src.indexOf("年")).trim();
										src = col[j];
										month = src.substring(src.indexOf("年") + 1, src.indexOf("月")).trim();
									} else {
										String src = col[j];
										year = src.substring(src.indexOf("期") + 1, src.indexOf("年")).trim();
										src = col[j];
										month = src.substring(src.indexOf("年") + 1, src.indexOf("月")).trim();
									}
									year = year.replaceAll("　", "");
									year = year.trim().replaceAll("　", "");
									month = month.replaceAll("　", "");
									month = month.replaceAll("　", "");
									setElement(p, "fitechSubmitYear", year);
									setElement(p, "fitechSubmitMonth", month);
								}
								if (year.equals("") || month.equals("")) {
									//UploadFileActionold.messInfo = "报表报送失败，请输入报表日期！";
									InputData.messageInfo = "报表报送失败，请输入报表日期！";
									return null;
								}

								continue;
							}
							// 判断是否为货币单位
							if (col[j].indexOf("货币单位") > -1) {
								setElement(p, "fitechUnit", col[j].substring(col[j].indexOf("货币单位") + 5));
								continue;
							}

							// 是否为填报人、复核人、负责人

							if (col[j].indexOf("填表人") > -1) {
								String tbr = col[j].substring(col[j].indexOf("填表人") + 4);
								setElement(p, "fitechFiller", tbr);
								tbr = tbr.replaceAll("　", "");
								tbr = tbr.trim().replaceAll("　", "");
								/*
								 * if(tbr.equals("")){ UploadFileAction.messInfo =
								 * "报表报送失败，请输入填表人！"; InputData.messageInfo =
								 * "报表报送失败，请输入填表人！"; return null; }
								 */
								continue;
							}
							if (col[j].indexOf("复核人") > -1) {

								setElement(p, "fitechChecker", col[j].substring(col[j].indexOf("复核人") + 4));
								continue;
							}
							if (col[j].indexOf("负责人") > -1) {

								setElement(p, "fitechPrincipal", col[j].substring(col[j].indexOf("负责人") + 4));
								continue;
							}

							if (row[j].getType() == CellType.NUMBER) {
								numberCell = (NumberCell) row[j];
								// // System.out.println("**********col["+j+"]="+numberCell.getValue());
								col[j] = String.valueOf(numberCell.getValue());
							} else if (row[j].getType() == CellType.NUMBER_FORMULA) {
								if (col[j].indexOf("\"") > -1) {
									col[j] = col[j].substring(col[j].indexOf("\"") + 1);
									if (col[j].indexOf("\"") > -1)
										col[j] = col[j].substring(col[j].indexOf("\"") + 1);
								} else if (col[j].indexOf("$") > -1) {
									col[j] = col[j].substring(col[j].indexOf("$") + 1);
								}
								if (col[j].indexOf(",") > -1)
									col[j] = col[j].replaceAll(",", "");
								if (col[j].indexOf("%") > -1 && col[j].indexOf("%") == col[j].length() - 1) {
									try {
										col[j] = String.valueOf(Double.parseDouble(col[j].substring(0, col[j].indexOf("%"))));
									} catch (Exception ex) {
									}
								}

							}

							Element g = p.addElement(Util.indexCol(j) + k);
							g.setText(col[j]);

						}

					}

				}
			}
			w1.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return document;

	}

	/**
	 * 给特殊标签赋值
	 * 
	 * @param element
	 */
	private static void setElement(Element p, String element, String value) {
		if (value == null || value.equals(""))
			return;
		Element e = p.element(element);
		e.setText(value);
	}

	/**
	 * 生成xml
	 * 
	 * @param xmlurl
	 *            生成xml的绝对路径
	 */
	private static void makexml(Document document, String xmlurl) {
		try {
			/** 将document中的内容写入文件中 */
			OutputFormat format = OutputFormat.createPrettyPrint();
			/** 指定XML编码 */
			format.setEncoding("GBK");

			FileWriter fileWriter = new FileWriter(new File(xmlurl));

			XMLWriter writer = new XMLWriter(fileWriter, format);
			writer.write(document);
			writer.close();

			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String args[]) {

	}

}
