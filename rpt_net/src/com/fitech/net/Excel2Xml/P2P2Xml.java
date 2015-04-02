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
 * ���ɵ�ʽ������
 * 
 * @author GEN
 * 
 */
public class P2P2Xml {
	/**
	 * ����ͨ��excel����xml
	 * 
	 * @param excel
	 * @param xml
	 * @param v
	 *            �б��� ���ڵ���excel��pdf���в�
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
				// ���С����
					Element subtitleElement = p.addElement("fitechSubtitle");
					subtitleElement.setText(subtitle);
				}
				/** �ж�ģ������ */
				if(template_type.equals(com.fitech.net.config.Config.FZ_TEMPLATE)) { // �ж��Ƿ�Ϊ���Ϳھ�			
						setElement(p, "fitechRange", com.fitech.net.config.Config.reportRange);
						// ���ݷ�Χid
						name.setRangId(Util.getRangeId( com.fitech.net.config.Config.reportRange));				
				}

				for (int i = 0; i < s.getRows(); i++) {

					row = s.getRow(i);

					//������ǵ�һ�е�һ��Ϊ����Ļ����ǵڶ��еڶ���
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

							// pdfxml��excel�к����1 kΪpdfxml���к�
							int k = i + 1 + v;

							// �ж��Ƿ��Ǳ���
							if (i == titleRow&&j==titleCol) {
								if(titleRow!=0&&row!=null&&row.length>2&&row[1].getType() != CellType.EMPTY){
									setElement(p, "fitechTitle", col[j].replaceAll(" ", ""));
									name.setRepId(StrutsMChildReportDelegate.getChildRepId(col[j].trim()));
								}else{
									//G04û���ӱ���IDֻ�����
									String temp=null;
									if( col[j].indexOf("GF04�����")>-1){
										Cell[] row1=s.getRow(2);
										if(row1!=null&&row1.length>0&&row1[0].getType() != CellType.EMPTY){
											if(row1[0].getContents().indexOf("��ע��Ŀ")>-1){
												temp="GF04�����ע";
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
						
								if (col[j].indexOf("���Ϳھ�") > -1) {
									setElement(p, "fitechRange", col[j].substring(col[j].indexOf("���Ϳھ�") + 5));
									// ���ݷ�Χid
									name.setRangId(Util.getRangeId(col[j].substring(col[j].indexOf("���Ϳھ�") + 5)));
									continue;
								}								
							
						
								if (col[j].indexOf("��������") > -1) {
									year = "";
									month = "";
									if (col[j].indexOf("��") > -1 && col[j].indexOf("��") > -1) {
										if (col[j].indexOf("�������ڣ�") > -1) {
											String src = col[j];
											year = src.substring(src.indexOf("�������ڣ�") +"�������ڣ�".length(), src.indexOf("��")).trim();
											src = col[j];
											month = src.substring(src.indexOf("��") + 1, src.indexOf("��")).trim();
										} else {
											String src = col[j];
											year = src.substring(src.indexOf("��") + 1, src.indexOf("��")).trim();
											src = col[j];
											month = src.substring(src.indexOf("��") + 1, src.indexOf("��")).trim();
										}
										year = year.replaceAll("��", "");
										year = year.trim().replaceAll("��", "");
										month = month.replaceAll("��", "");
										month = month.trim().replaceAll("��", "");
										setElement(p, "fitechSubmitYear", year);
										setElement(p, "fitechSubmitMonth", month);
										if (year.equals("") || month.equals("")) {
											//UploadFileActionold.messInfo = "������ʧ�ܣ������뱨�����ڣ�";
											InputData.messageInfo = "������ʧ�ܣ������뱨�����ڣ�";
											return null;
										}
									}
									if(template_type.equals(com.fitech.net.config.Config.FR_TEMPLATE)) {
										continue;
									}
								}
														
							
							
							// �ж��Ƿ�Ϊ���ҵ�λ
							if (col[j].indexOf("���ҵ�λ") > -1) {
								setElement(p, "fitechUnit", col[j].substring(col[j].indexOf("���ҵ�λ") + 5));
								continue;
							}

							// �Ƿ�Ϊ��ˡ������ˡ�������
							if (col[j].indexOf("�����") > -1) {
								String tbr = col[j].substring(col[j].indexOf("�����") + 4);
								//������Щ���˰�����˺͸����˷ŵ�һ�������������ӻ����жϳ���
								if(tbr.length()>5){
									tbr=tbr.substring(0, 5);
								}
								setElement(p, "fitechFiller", tbr);
								tbr = tbr.replaceAll("��", "");
								tbr = tbr.trim().replaceAll("��", "");
								/*
								 * if(tbr.equals("") ){
								 * UploadFileAction.messInfo = "������ʧ�ܣ�����������ˣ�";
								 * InputData.messageInfo = "������ʧ�ܣ�����������ˣ�";
								 * return null; }
								 */
								if(template_type.equals(com.fitech.net.config.Config.FR_TEMPLATE)) {
									continue;
								}
							}
							
							
							
							if (col[j].indexOf("������") > -1) {
								String fhr = col[j].substring(col[j].indexOf("�����") + 4);
								
								if(fhr.length()>5){
									fhr=fhr.substring(0, 5);
								}
								setElement(p, "fitechChecker", fhr);
								if(template_type.equals(com.fitech.net.config.Config.FR_TEMPLATE)) {
									continue;
								}
							}
							if (col[j].indexOf("������") > -1) {
								setElement(p, "fitechPrincipal", col[j].substring(col[j].indexOf("������") + 4));
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
	 * �Ѳ�ֱ����excel����xml
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

							// pdfxml��excel�к����1 kΪpdfxml���к�

							int k = i + 1 + Integer.parseInt(part.getStartRow());

							// �ж��Ƿ��Ǳ���
							if (i == 0) {
								setElement(p, "fitechTitle", col[j]);
								continue;
							}

						
								if (col[j].indexOf("���Ϳھ�") > -1) {

									setElement(p, "fitechRange", col[j].substring(col[j].indexOf("���Ϳھ�") + 5));

									// ���ݷ�Χid
									name.setRangId(Util.getRangeId(col[j].substring(col[j].indexOf("���Ϳھ�") + 5)));
									continue;
								}
								
							

							// �ж��Ƿ�ΪС����
							if (col[j].trim().equals(part.getSubtitle())) {
								Element subtitle = p.addElement("fitechSubtitle");
								subtitle.setText(col[j].trim());

								continue;
							}
							// �ж��Ƿ��Ƕ����
							if (col[j].indexOf("����") > -1) {
								setElement(p, "fitechCurr", col[j].substring(col[j].indexOf("����") + 3));
								continue;
							}

							// �ж��Ƿ�Ϊ��������
							if (col[j].indexOf("��������") > -1) {
								year = "";
								month = "";
								if (col[j].indexOf("��") > -1 && col[j].indexOf("��") > -1) {
									if (col[j].indexOf("��") > -1) {
										String src = col[j];
										year = src.substring(src.indexOf("��") + 1, src.indexOf("��")).trim();
										src = col[j];
										month = src.substring(src.indexOf("��") + 1, src.indexOf("��")).trim();
									} else {
										String src = col[j];
										year = src.substring(src.indexOf("��") + 1, src.indexOf("��")).trim();
										src = col[j];
										month = src.substring(src.indexOf("��") + 1, src.indexOf("��")).trim();
									}
									year = year.replaceAll("��", "");
									year = year.trim().replaceAll("��", "");
									month = month.replaceAll("��", "");
									month = month.replaceAll("��", "");
									setElement(p, "fitechSubmitYear", year);
									setElement(p, "fitechSubmitMonth", month);
								}
								if (year.equals("") || month.equals("")) {
									//UploadFileActionold.messInfo = "������ʧ�ܣ������뱨�����ڣ�";
									InputData.messageInfo = "������ʧ�ܣ������뱨�����ڣ�";
									return null;
								}

								continue;
							}
							// �ж��Ƿ�Ϊ���ҵ�λ
							if (col[j].indexOf("���ҵ�λ") > -1) {
								setElement(p, "fitechUnit", col[j].substring(col[j].indexOf("���ҵ�λ") + 5));
								continue;
							}

							// �Ƿ�Ϊ��ˡ������ˡ�������

							if (col[j].indexOf("�����") > -1) {
								String tbr = col[j].substring(col[j].indexOf("�����") + 4);
								setElement(p, "fitechFiller", tbr);
								tbr = tbr.replaceAll("��", "");
								tbr = tbr.trim().replaceAll("��", "");
								/*
								 * if(tbr.equals("")){ UploadFileAction.messInfo =
								 * "������ʧ�ܣ�����������ˣ�"; InputData.messageInfo =
								 * "������ʧ�ܣ�����������ˣ�"; return null; }
								 */
								continue;
							}
							if (col[j].indexOf("������") > -1) {

								setElement(p, "fitechChecker", col[j].substring(col[j].indexOf("������") + 4));
								continue;
							}
							if (col[j].indexOf("������") > -1) {

								setElement(p, "fitechPrincipal", col[j].substring(col[j].indexOf("������") + 4));
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
	 * �������ǩ��ֵ
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
	 * ����xml
	 * 
	 * @param xmlurl
	 *            ����xml�ľ���·��
	 */
	private static void makexml(Document document, String xmlurl) {
		try {
			/** ��document�е�����д���ļ��� */
			OutputFormat format = OutputFormat.createPrettyPrint();
			/** ָ��XML���� */
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
