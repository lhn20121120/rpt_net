package com.fitech.net.Excel2Xml;

import java.io.File;
import java.util.List;

import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.cbrc.smis.system.cb.InputData;
import com.fitech.net.action.UploadFileActionold;

public class BillExcel2Xml {
	/**
	 * ʹ����û�кϼƵ��嵥ʽ����
	 * @param bill
	 * @param excel
	 * @param year
	 * @param month
	 * @param name
	 * @return
	 */
	
	public static String year = "";
	public static String month = "";
	
	protected static Document getGBill(BillBean bill,String excel,Name name) {

		name.setRepId(bill.getRepId());
		String[] detail = bill.getDetail();

		Document document = null;
		try {
			File inputWorkbook;
			inputWorkbook = new File(excel);
			Workbook w1 = Workbook.getWorkbook(inputWorkbook);

			String col[] = null;

			SAXReader saxReader = new SAXReader();
			String modelurl = Util.getFullPathRelateClass(bill.getModel(), P2P2Xml.class);
			document = saxReader.read(new File(modelurl));
			Element root = document.getRootElement();
			Element Subform = root.element("Subform1");
			Element temp = Subform.addElement("TEMP");
			Element detailHeader=Subform.element("detailHeader");
			Element total=Subform.element("total");

			for (int sheet = 0; sheet < w1.getNumberOfSheets(); sheet++) {

				Sheet s = w1.getSheet(sheet);
				Cell[] row = null;
				int n = 0;
				//ѭ��������־
				boolean enddetail = false;
				for (int i = 0; i < s.getRows(); i++) {
					row = s.getRow(i);
					col = new String[row.length];
					//д��detailHeader
					
					if(i<bill.getStartRow()){
						
						for (int j = 0; j < row.length; j++) {
							if (row[j].getType() != CellType.EMPTY) {
								col[j] = row[j].getContents();
								//�ж��Ƿ�Ϊ���Ϳھ�
								if (col[j].indexOf("���Ϳھ�") > -1) {
									setElement(detailHeader, "fitechRange", col[j]
											.substring(col[j].indexOf("���Ϳھ�") + 5));
//									//���ݷ���id
									name.setRangId(Util.getRangeId(col[j]
																		.substring(col[j].indexOf("���Ϳھ�") + 5)));					
									continue;
								}								
//								�ж��Ƿ�Ϊ��������
								if (col[j].indexOf("��������") > -1) {
									year = "";
									month = "";
									if(col[j].indexOf("��") > -1 && col[j].indexOf("��") > -1){
										if(col[j].indexOf("��") > -1){
											String src = col[j];
											year = src.substring(src.indexOf("��")+1,src.indexOf("��")).trim();
											src = col[j];
											month = src.substring(src.indexOf("��")+1,src.indexOf("��")).trim();
										}else{
											String src = col[j];
											year = src.substring(src.indexOf("��")+1,src.indexOf("��")).trim();
											src = col[j];
											month = src.substring(src.indexOf("��")+1,src.indexOf("��")).trim();
										}
										year = year.replaceAll("��","");
										year = year.trim().replaceAll("��","");
										month = month.replaceAll("��","");
										month = month.replaceAll("��","");
										setElement(detailHeader, "fitechSubmitYear", year);
										setElement(detailHeader, "fitechSubmitMonth", month);
									}	
									if(year.equals("") || month.equals("")){
									//	UploadFileActionold.messInfo = "������ʧ�ܣ������뱨�����ڣ�";
										InputData.messageInfo = "������ʧ�ܣ������뱨�����ڣ�";
										return null;
									}
									continue;
								}
//								�ж��Ƿ�Ϊ���ҵ�λ
								if (col[j].indexOf("���ҵ�λ") > -1) {
									setElement(detailHeader, "fitechUnit", col[j]
											.substring(col[j].indexOf("���ҵ�λ") + 5));
									continue;
								}
							}
						
						}
					}
					//��ʼд��detail��total
					if (i > bill.getStartRow() - 1) {
						row = s.getRow(i);
						col = new String[row.length];

						for (int j = 0; j < row.length; j++) {

							col[j] = row[j].getContents();
							
							//�ж�ѭ���Ƿ����
							if (col[j].indexOf(bill.getEndStr()) > -1) {
								enddetail = true;

							}
							
							if (!enddetail) {
							//��ʼд��ѭ��
								if (!detail[n % detail.length].equals("")) {
									if (n % detail.length == 0) {
										temp = Subform.addElement("detail");
									}
									Element dt = temp.addElement(detail[n
											% detail.length]);
									dt.setText(col[j]);
								
								} else {

								}
								n++;
							} else {
								//д��total
								
								//�ж��Ƿ�Ϊ��ע
								if(col[j].length()<20){
									if(!col[j].equals("")){
										if (col[j].indexOf("�����") > -1) {
											setElement(total, "fitechFiller", col[j]
													.substring(col[j].indexOf("�����") + 4));
											continue;
										}
										if (col[j].indexOf("������") > -1) {
											setElement(total, "fitechChecker", col[j]
													.substring(col[j].indexOf("������") + 4));
											continue;
										}
										if (col[j].indexOf("������") > -1) {
											setElement(total, "fitechPrincipal", col[j]
													.substring(col[j].indexOf("������") + 4));
											continue;
										}
									}
									
								}

							}

						}

					}

				}
			}
			temp = Subform.element("TEMP");
			Subform.remove(temp);
			w1.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return document;
	}
	/**
	 * ����s3201�ȱ���
	 * @param bill
	 * @param excel
	 * @param year
	 * @param month
	 * @param name
	 * @return
	 */
	public static Document getBill_1(Bill_1Bean bill,String excel,Name name) {
		name.setRepId(bill.getRepId());
		String[] detail = bill.getDetail();
		String[] totalstr=bill.getTotal();

		Document document = null;
		try {
			File inputWorkbook;
			inputWorkbook = new File(excel);
			Workbook w1 = Workbook.getWorkbook(inputWorkbook);

			String col[] = null;

			SAXReader saxReader = new SAXReader();
			String modelurl = Util.getFullPathRelateClass(bill.getModel(), P2P2Xml.class);
			document = saxReader.read(new File(modelurl));
			Element root = document.getRootElement();
			Element Subform = root.element("Subform1");
			Element temp = Subform.addElement("TEMP");
			Element detailHeader=Subform.element("detailHeader");
			Element total=Subform.element("total");

			for (int sheet = 0; sheet < w1.getNumberOfSheets(); sheet++) {

				Sheet s = w1.getSheet(sheet);
				Cell[] row = null;
				NumberCell numberCell = null;
				int n = 0;
				int t=0;
				//ѭ��������־
				boolean enddetail = false;
				//total��ʼ��־
				boolean starttotal=false;
				//total������־
				boolean endtotal=false;
				for (int i = 0; i < s.getRows(); i++) {
					row = s.getRow(i);
					col = new String[row.length];
					//д��detailHeader
					
					if(i<bill.getStartRow()){
						
						for (int j = 0; j < row.length; j++) {
							if (row[j].getType() != CellType.EMPTY) {
								col[j] = row[j].getContents();
								//�ж��Ƿ�Ϊ���Ϳھ�
								if (col[j].indexOf("���Ϳھ�") > -1) {
									setElement(detailHeader, "fitechRange", col[j]
											.substring(col[j].indexOf("���Ϳھ�") + 5));
//									//���ݷ���id
									name.setRangId(Util.getRangeId(col[j]
																		.substring(col[j].indexOf("���Ϳھ�") + 5)));					
									continue;
								}								
//								�ж��Ƿ�Ϊ��������
								if (col[j].indexOf("��������") > -1) {
									year = "";
									month = "";
									if(col[j].indexOf("��") > -1 && col[j].indexOf("��") > -1){
										if(col[j].indexOf("��") > -1){
											String src = col[j];
											year = src.substring(src.indexOf("��")+1,src.indexOf("��")).trim();
											src = col[j];
											month = src.substring(src.indexOf("��")+1,src.indexOf("��")).trim();
										}else{
											String src = col[j];
											year = src.substring(src.indexOf("��")+1,src.indexOf("��")).trim();
											src = col[j];
											month = src.substring(src.indexOf("��")+1,src.indexOf("��")).trim();
										}
										year = year.replaceAll("��","");
										year = year.trim().replaceAll("��","");
										month = month.replaceAll("��","");
										month = month.replaceAll("��","");
										setElement(detailHeader, "fitechSubmitYear", year);
										setElement(detailHeader, "fitechSubmitMonth", month);
									}	
									if(year.equals("") || month.equals("")){
									//	UploadFileActionold.messInfo = "������ʧ�ܣ������뱨�����ڣ�";
										InputData.messageInfo = "������ʧ�ܣ������뱨�����ڣ�";
										return null;
									}
									continue;
								}
//								�ж��Ƿ�Ϊ���ҵ�λ
								if (col[j].indexOf("���ҵ�λ") > -1) {
									setElement(detailHeader, "fitechUnit", col[j]
											.substring(col[j].indexOf("���ҵ�λ") + 5));
									continue;
								}
							}
						
						}
					}
					
					
					
					
					
					//��ʼд��detail��total
					if (i > bill.getStartRow() - 1) {
						row = s.getRow(i);if(row.length == 0) continue;
						col = new String[row.length];
						//�ж�ѭ���Ƿ����
						if(row[0].getContents().trim().equals("�ϼ�")||row[1].getContents().trim().equals("�ϼ�")){
							enddetail = true;
							starttotal=true;
						}
						if(!enddetail){
							for (int j = 0; j < row.length; j++) {
								col[j] = row[j].getContents();
								
								if(row[j].getType() == CellType.NUMBER){
									numberCell = (NumberCell)row[j];
									col[j] = String.valueOf(numberCell.getValue());
									
								}else if(row[j].getType() == CellType.NUMBER_FORMULA){
									if(col[j].indexOf("\"") > -1){
										col[j] = col[j].substring(col[j].indexOf("\"")+1);
										if(col[j].indexOf("\"") > -1)
											col[j] = col[j].substring(col[j].indexOf("\"")+1);
									}else if(col[j].indexOf("$") > -1){
										col[j] = col[j].substring(col[j].indexOf("$") + 1);
									}
									if(col[j].indexOf(",") > -1)
										col[j] = col[j].replaceAll(",","");
									if(col[j].indexOf("%") > -1 && col[j].indexOf("%") == col[j].length()-1){									
										try{
											col[j] = String.valueOf(Double.parseDouble(col[j].substring(0,col[j].indexOf("%"))));
										}catch(Exception ex){}									
									}
								}
								if(j >= detail.length) continue;
								//��ʼд��ѭ��
								if (!detail[n % detail.length].equals("")) {
									if (n % detail.length == 0) {
										temp = Subform.addElement("detail");
									}
									Element dt = temp.addElement(detail[n
											% detail.length]);
									dt.setText(col[j]);
								
								} else {

								}
								n++;
							}
														
						}
						
						//д��total
						if(starttotal&&!endtotal){
							for (int j = 0; j < row.length; j++) {
								col[j] = row[j].getContents();
								
								if(row[j].getType() == CellType.NUMBER){
									if(col[j].indexOf("\"") > -1){
										col[j] = col[j].substring(col[j].indexOf("\"")+1);
										if(col[j].indexOf("\"") > -1)
											col[j] = col[j].substring(col[j].indexOf("\"")+1);
									}
									else if(col[j].indexOf("$") > -1){
										col[j] = col[j].substring(col[j].indexOf("$") + 1);
									}
									if(col[j].indexOf(",") > -1)
										col[j] = col[j].replaceAll(",","");
								}else if(row[j].getType() == CellType.NUMBER_FORMULA){
									if(col[j].indexOf("\"") > -1){
										col[j] = col[j].substring(col[j].indexOf("\"")+1);
										if(col[j].indexOf("\"") > -1)
											col[j] = col[j].substring(col[j].indexOf("\"")+1);
									}else if(col[j].indexOf("$") > -1){
										col[j] = col[j].substring(col[j].indexOf("$") + 1);
									}
									if(col[j].indexOf(",") > -1)
										col[j] = col[j].replaceAll(",","");
								}
								if(t>=totalstr.length) continue;
								if (!totalstr[t].equals("")) {
									temp=total.element(totalstr[t]);
									temp.setText(col[j]);	
								}
								t++;

							}
							endtotal=true;
						}
						

						//if(starttotal&&endtotal){
							for (int j = 0; j < row.length; j++) {
								if (row[j].getType() != CellType.EMPTY) {
									col[j] = row[j].getContents();
									if (col[j].indexOf("�����") > -1) {
										setElement(total, "fitechFiller", col[j]
												.substring(col[j].indexOf("�����") + 4));
										continue;
									}
									if (col[j].indexOf("������") > -1) {
										setElement(total, "fitechChecker", col[j]
												.substring(col[j].indexOf("������") + 4));
										continue;
									}
									if (col[j].indexOf("������") > -1) {
										setElement(total, "fitechPrincipal", col[j]
												.substring(col[j].indexOf("������") + 4));
										continue;
									}
								}

							}
							
						//}

					}

				}
			}
			temp = Subform.element("TEMP");
			Subform.remove(temp);
			w1.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return document;
	}
	public static Document getS3208(String excel,Name name) {
		
		Document document = null;
		document= P2P2Xml.excel2xml_common(excel,name,-1,com.fitech.net.config.Config.FR_TEMPLATE,"");
		name.setRepId("S3208");
		return document;
	}
	/**
	 * ����S3209
	 * @param excel
	 * @param year
	 * @param month
	 * @param name
	 * @return
	 */
	public static Document getS3209(String excel,Name name) {
		name.setRepId("S3209");
		Document document = null;
		try{
			File inputWorkbook;
			inputWorkbook = new File(excel);
			Workbook w1 = Workbook.getWorkbook(inputWorkbook);
			String col[] = null;
			SAXReader saxReader = new SAXReader();
			String modelurl = Util.getFullPathRelateClass("S3209.xml", P2P2Xml.class);
			document = saxReader.read(new File(modelurl));
			Element root = document.getRootElement();
			List  subform1List=root.elements("Subform1");
			Element subform1=(Element)subform1List.get(0);
			Element detailHeader=subform1.element("detailHeader1");	
			Element total=subform1.element("total1");	
			Sheet s = w1.getSheet(0);
			Element temp = subform1.addElement("TEMP");
			Cell[] row = null;
			NumberCell numberCell = null;
			//�ѱ����Ϊ�����׶�
			int pas=0;
			for (int i = 0; i < s.getRows(); i++) {
				row = s.getRow(i);
				col = new String[row.length];
				
				if(row[0].getContents().equals("�ϼ�")){
						if(pas==1){
							setElement(total, "COL4",getContents(row[3],row[3].getContents()));
							setElement(total, "COL5",getContents(row[4],row[4].getContents()));
							setElement(total, "COL6",getContents(row[5],row[5].getContents()));
							setElement(total, "COL7",getContents(row[6],row[6].getContents()));
							setElement(total, "COL8",getContents(row[7],row[7].getContents()));							
							subform1=(Element)subform1List.get(1);
							detailHeader=subform1.element("detailHeader2");
							total=subform1.element("total2");
							
							Element e = subform1.element("detail2");
							subform1.remove(e);
							
							i=i+5;
							row = s.getRow(i);
							pas=2;
						}else
						if(pas==2){
							setElement(total, "COL4",getContents(row[3],row[3].getContents()));
							setElement(total, "COL5",getContents(row[4],row[4].getContents()));
							setElement(total, "COL6",getContents(row[5],row[5].getContents()));
							setElement(total, "COL7",getContents(row[6],row[6].getContents()));
							setElement(total, "COL8",getContents(row[7],row[7].getContents()));							
							subform1=(Element)subform1List.get(2);
							detailHeader=subform1.element("detailHeader3");
							total=subform1.element("total3");	
							
							Element e = subform1.element("detail3");
							subform1.remove(e);
							
							
							i=i+6;
							row = s.getRow(i);
							pas=3;
						}else
						if(pas==3){
							setElement(total, "COL4",getContents(row[3],row[3].getContents()));
							setElement(total, "COL5",getContents(row[4],row[4].getContents()));
							setElement(total, "COL6",getContents(row[5],row[5].getContents()));
							setElement(total, "COL7",getContents(row[6],row[6].getContents()));							
							pas=4;
						}
				}
				int n=0;
				for(int j=0;j<row.length;j++){
					col[j] = row[j].getContents();
					if(row[j].getType() == CellType.NUMBER){
						numberCell = (NumberCell)row[j];
						col[j] = String.valueOf(numberCell.getValue());
					}else if(row[j].getType() == CellType.NUMBER_FORMULA){
						if(col[j].indexOf("\"") > -1){
							col[j] = col[j].substring(col[j].indexOf("\"")+1);
							if(col[j].indexOf("\"") > -1)
								col[j] = col[j].substring(col[j].indexOf("\"")+1);
						}else if(col[j].indexOf("$") > -1){
							col[j] = col[j].substring(col[j].indexOf("$") + 1);
						}
						if(col[j].indexOf(",") > -1)
							col[j] = col[j].replaceAll(",","");
						if(col[j].indexOf("%") > -1 && col[j].indexOf("%") == col[j].length()-1){									
							try{
								col[j] = String.valueOf(Double.parseDouble(col[j].substring(0,col[j].indexOf("%"))));
							}catch(Exception ex){}									
						}
					}
					
					if(pas==0){
						if (row[j].getType() != CellType.EMPTY) {
							if (col[j].indexOf("���Ϳھ�") > -1) {
								setElement(detailHeader, "fitechRange", col[j]
										.substring(col[j].indexOf("���Ϳھ�") + 5));
	//							//���ݷ���id
								name.setRangId(Util.getRangeId(col[j]
																	.substring(col[j].indexOf("���Ϳھ�") + 5)));					
								continue;
							}								
	//						�ж��Ƿ�Ϊ��������
							if (col[j].indexOf("��������") > -1) {
								year = "";
								month = "";
								if(col[j].indexOf("��") > -1 && col[j].indexOf("��") > -1){
									if(col[j].indexOf("��") > -1){
										String src = col[j];
										year = src.substring(src.indexOf("��")+1,src.indexOf("��")).trim();
										src = col[j];
										month = src.substring(src.indexOf("��")+1,src.indexOf("��")).trim();
									}else{
										String src = col[j];
										year = src.substring(src.indexOf("��")+1,src.indexOf("��")).trim();
										src = col[j];
										month = src.substring(src.indexOf("��")+1,src.indexOf("��")).trim();
									}
									year = year.replaceAll("��","");
									year = year.trim().replaceAll("��","");
									month = month.replaceAll("��","");
									month = month.replaceAll("��","");
									setElement(detailHeader, "fitechSubmitYear", year);
									setElement(detailHeader, "fitechSubmitMonth", month);
								}	
								if(year.equals("") || month.equals("")){
								//	UploadFileActionold.messInfo = "������ʧ�ܣ������뱨�����ڣ�";
									InputData.messageInfo ="������ʧ�ܣ������뱨�����ڣ�";
									return null;
								}
								continue;
							}
	//						�ж��Ƿ�Ϊ���ҵ�λ
							if (col[j].indexOf("���ҵ�λ") > -1) {
								setElement(detailHeader, "fitechUnit", col[j]
										.substring(col[j].indexOf("���ҵ�λ") + 5));
								continue;
							}
							if(col[j].trim().equals("����")){
								pas=1;
								j = row.length;
								continue;
							}
						}
					}
					//��һ��ѭ��
					if(pas==1){
						String cols[]={"COL1","COL2","COL3","COL4","COL5","COL6","COL7","COL8"};
						if (n== 0) {
							temp = subform1.addElement("detail1");
						}
						if(n >= cols.length) continue;
						temp.addElement(cols[n] == null ? "" : cols[n])
								.setText(col[n] == null ? "" : col[n].trim());
						n++;
					}
					//��2��ѭ��
					if(pas==2){
						String cols[]={"COL1","COL2","COL3","COL4","COL5","COL6","COL7","COL8"};
						if (n== 0) {
							temp = subform1.addElement("detail2");
						}
						if(n >= cols.length) continue;
						temp.addElement(cols[n] == null ? "" : cols[n])
								.setText(col[n] == null ? "" : col[n].trim());
						n++;
					}
//					��3��ѭ��
					if(pas==3){
						String cols[]={"COL1","COL2","COL3","COL4","COL5","COL6","COL7"};
						if (n== 0) {
							temp = subform1.addElement("detail3");
						}
						if(n >= cols.length) continue;
						temp.addElement(cols[n] == null ? "" : cols[n])
								.setText(col[n] == null ? "" : col[n].trim());
						n++;
					}
					if(pas==4){
						if (row[j].getType() != CellType.EMPTY) {
							col[j] = row[j].getContents();
							if (col[j].indexOf("�����") > -1) {
								setElement(total, "fitechFiller", col[j]
										.substring(col[j].indexOf("�����") + 4));
								continue;
							}
							if (col[j].indexOf("������") > -1) {
								setElement(total, "fitechChecker", col[j]
										.substring(col[j].indexOf("������") + 4));
								continue;
							}
							if (col[j].indexOf("������") > -1) {
								setElement(total, "fitechPrincipal", col[j]
										.substring(col[j].indexOf("������") + 4));
								continue;
							}
						}
					}
					
				}
				
			}
			temp = subform1.element("TEMP");
			subform1.remove(temp);
			w1.close();	

			
		}catch(Exception e){
			e.printStackTrace();
		}
		return document;
	}
	public static Document getS3402(String excel,Name name) {
		name.setRepId("S3402");
		Document document = null;
		try{
			File inputWorkbook;
			inputWorkbook = new File(excel);
			Workbook w1 = Workbook.getWorkbook(inputWorkbook);
			String col[] = null;
			SAXReader saxReader = new SAXReader();
			String modelurl = Util.getFullPathRelateClass("S3402.xml", P2P2Xml.class);
			document = saxReader.read(new File(modelurl));
			Element root = document.getRootElement();
			List  subform1List=root.elements("Subform1");
			Element subform1=(Element)subform1List.get(0);
			Element detailHeader=subform1.element("detailHeader1");	
			Element total=subform1.element("total1");	
			Sheet s = w1.getSheet(0);
			Element temp = subform1.addElement("TEMP");
			Cell[] row = null;
			NumberCell numberCell = null;
			//�ѱ����Ϊ�����׶�
			int pas=0;
			for (int i = 0; i < s.getRows(); i++) {
				row = s.getRow(i);
				
				if(row[1].getContents().equals("�ϼ�")){
						if(pas==1){
							setElement(total, "COL4",getContents(row[3],row[3].getContents()));
							setElement(total, "COL9",getContents(row[8],row[8].getContents()));
							setElement(total, "COL10",getContents(row[9],row[9].getContents()));
							setElement(total, "COL11",getContents(row[10],row[10].getContents()));
							setElement(total, "COL12",getContents(row[11],row[11].getContents()));
							setElement(total, "COL13",getContents(row[12],row[12].getContents()));
							
							temp = subform1.element("TEMP");
							subform1.remove(temp);
							
							subform1=(Element)subform1List.get(1);
							detailHeader=subform1.element("detailHeader2");
							total=subform1.element("total2");	
							
							Element e = subform1.element("detail2");
							subform1.remove(e);
							
							i=i+6;
							row = s.getRow(i);
							pas=2;
						}else
						if(pas==2){
							setElement(total, "COL5",getContents(row[4],row[4].getContents()));
							setElement(total, "COL10",getContents(row[9],row[9].getContents()));
							setElement(total, "COL11",getContents(row[10],row[10].getContents()));
							setElement(total, "COL12",getContents(row[11],row[11].getContents()));
							setElement(total, "COL13",getContents(row[12],row[12].getContents()));
							setElement(total, "COL14",getContents(row[13],row[13].getContents()));
							pas=3;
						}
				}
				int n=0;
				for(int j=0;j<row.length;j++){
					col = new String[row.length];
					col[j] = row[j].getContents();
					if(row[j].getType() == CellType.NUMBER){
						numberCell = (NumberCell)row[j];
						col[j] = String.valueOf(numberCell.getValue());
						
					}else if(row[j].getType() == CellType.NUMBER_FORMULA){
						if(col[j].indexOf("\"") > -1){
							col[j] = col[j].substring(col[j].indexOf("\"")+1);
							if(col[j].indexOf("\"") > -1)
								col[j] = col[j].substring(col[j].indexOf("\"")+1);
						}else if(col[j].indexOf("$") > -1){
							col[j] = col[j].substring(col[j].indexOf("$") + 1);
						}
						if(col[j].indexOf(",") > -1)
							col[j] = col[j].replaceAll(",","");
						if(col[j].indexOf("%") > -1 && col[j].indexOf("%") == col[j].length()-1){									
							try{
								col[j] = String.valueOf(Double.parseDouble(col[j].substring(0,col[j].indexOf("%"))));
							}catch(Exception ex){}									
						}
					}
					
					if(pas==0){
						if (row[j].getType() != CellType.EMPTY) {
							if (col[j].indexOf("���Ϳھ�") > -1) {
								setElement(detailHeader, "fitechRange", col[j]
										.substring(col[j].indexOf("���Ϳھ�") + 5));
	//							//���ݷ���id
								name.setRangId(Util.getRangeId(col[j]
																	.substring(col[j].indexOf("���Ϳھ�") + 5)));					
								continue;
							}								
	//						�ж��Ƿ�Ϊ��������
							if (col[j].indexOf("��������") > -1) {
								year = "";
								month = "";
								if(col[j].indexOf("��") > -1 && col[j].indexOf("��") > -1){
									if(col[j].indexOf("��") > -1){
										String src = col[j];
										year = src.substring(src.indexOf("��")+1,src.indexOf("��")).trim();
										src = col[j];
										month = src.substring(src.indexOf("��")+1,src.indexOf("��")).trim();
									}else{
										String src = col[j];
										year = src.substring(src.indexOf("��")+1,src.indexOf("��")).trim();
										src = col[j];
										month = src.substring(src.indexOf("��")+1,src.indexOf("��")).trim();
									}
									year = year.replaceAll("��","");
									year = year.trim().replaceAll("��","");
									month = month.replaceAll("��","");
									month = month.replaceAll("��","");
									setElement(detailHeader, "fitechSubmitYear", year);
									setElement(detailHeader, "fitechSubmitMonth", month);
								}	
								if(year.equals("") || month.equals("")){
								//	UploadFileActionold.messInfo = "������ʧ�ܣ������뱨�����ڣ�";
									InputData.messageInfo = "������ʧ�ܣ������뱨�����ڣ�";
									return null;
								}
								continue;
							}
	//						�ж��Ƿ�Ϊ���ҵ�λ
							if (col[j].indexOf("���ҵ�λ") > -1) {
								setElement(detailHeader, "fitechUnit", col[j]
										.substring(col[j].indexOf("���ҵ�λ") + 5));
								continue;
							}
							if(col[j].trim().equals("��ʧ")){
								pas=1;
								j = row.length;
								continue;
							}
						}
					}
					//��һ��ѭ��
					if(pas==1){
						String cols[]={"COL1","COL2","COL3","COL4","COL5","COL6","COL7","COL8","COL9","COL10","COL11","COL12","COL13"};
						if (n== 0) {
							temp = subform1.addElement("detail1");
						}
						if(n >= cols.length) continue;
						temp.addElement(cols[n] == null ? "" : cols[n])
							.setText(col[n] == null ? "" : col[n].trim());
						n++;
					}
					//��2��ѭ��
					if(pas==2){
						String cols[]={"COL1","COL2","COL3","COL4","COL5","COL6","COL7","COL8","COL9","COL10","COL11","COL12","COL13","COL14"};
						if (n== 0) {
							temp = subform1.addElement("detail2");
						}
						if(n >= cols.length) continue;
						temp.addElement(cols[n] == null ? "" : cols[n])
							.setText(col[n] == null ? "" : col[n].trim());
						n++;
					}
					if(pas==3){
						if (row[j].getType() != CellType.EMPTY) {
							col[j] = row[j].getContents();
							if (col[j].indexOf("�����") > -1) {
								setElement(total, "fitechFiller", col[j]
										.substring(col[j].indexOf("�����") + 4));
								continue;
							}
							if (col[j].indexOf("������") > -1) {
								setElement(total, "fitechChecker", col[j]
										.substring(col[j].indexOf("������") + 4));
								continue;
							}
							if (col[j].indexOf("������") > -1) {
								setElement(total, "fitechPrincipal", col[j]
										.substring(col[j].indexOf("������") + 4));
								continue;
							}
						}
					}
					
				}
				
			}
				
			temp = subform1.element("TEMP");
			subform1.remove(temp);
			w1.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return document;
	}
	

	public static Document getS3403(String excel,Name name) {
		name.setRepId("S3403");
		Document document = null;
		document=P2P2Xml.excel2xml_common(excel,name,0,com.fitech.net.config.Config.FR_TEMPLATE,"");
		return document;
	}
	public static Document getS3600(String excel,Name name) {
		name.setRepId("S3600");
		Document document = null;
		try{
			File inputWorkbook;
			inputWorkbook = new File(excel);
			Workbook w1 = Workbook.getWorkbook(inputWorkbook);
			String col[] = null;
			SAXReader saxReader = new SAXReader();
			String modelurl = Util.getFullPathRelateClass("S3600.xml", P2P2Xml.class);
			document = saxReader.read(new File(modelurl));
			Element root = document.getRootElement();
			List  subform1List=root.elements("Subform1");
			Element subform1=(Element)subform1List.get(0);
			Element detailHeader=subform1.element("detailHeader1");	
			Element total=subform1.element("total1");	
			Sheet s = w1.getSheet(0);
			Element temp = subform1.addElement("TEMP");
			Cell[] row = null;
			NumberCell numberCell = null;
			//�ѱ����Ϊ�����׶�
			int pas=0;
			for (int i = 0; i < s.getRows(); i++) {
				row = s.getRow(i);
				
				if(row[0].getContents().equals("�ϼ�")){
						if(pas==1){
							setElement(total, "COL4",getContents(row[3],row[3].getContents()));
							setElement(total, "COL5",getContents(row[4],row[4].getContents()));
							setElement(total, "COL6",getContents(row[5],row[5].getContents()));
							setElement(total, "COL7",getContents(row[6],row[6].getContents()));
							setElement(total, "COL8",getContents(row[7],row[7].getContents()));		
							
							temp = subform1.element("TEMP");
							subform1.remove(temp);
							
							subform1=(Element)subform1List.get(1);
							detailHeader=subform1.element("detailHeader2");
							total=subform1.element("total2");	
							
							Element e = subform1.element("detail2");
							subform1.remove(e);
							
							i=i+5;
							row = s.getRow(i);
							pas=2;
						}else
						if(pas==2){
							setElement(total, "COL4",getContents(row[3],row[3].getContents()));
							setElement(total, "COL5",getContents(row[4],row[4].getContents()));
							setElement(total, "COL6",getContents(row[5],row[5].getContents()));
							setElement(total, "COL7",getContents(row[6],row[6].getContents()));
							setElement(total, "COL8",getContents(row[7],row[7].getContents()));							
							subform1=(Element)subform1List.get(2);
							detailHeader=subform1.element("detailHeader3");
							total=subform1.element("total3");	
							
							Element e = subform1.element("detail3");
							subform1.remove(e);
							
							i=i+6;
							row = s.getRow(i);
							pas=3;
						}else
						if(pas==3){
							setElement(total, "COL4",getContents(row[3],row[3].getContents()));
							setElement(total, "COL5",getContents(row[4],row[4].getContents()));
							setElement(total, "COL6",getContents(row[5],row[5].getContents()));
							setElement(total, "COL7",getContents(row[6],row[6].getContents()));					
							pas=4;
						}
				}
				int n=0;
				for(int j=0;j<row.length;j++){
					col = new String[row.length];
					col[j] = row[j].getContents();
					
					if(row[j].getType() == CellType.NUMBER){
						numberCell = (NumberCell)row[j];
						col[j] = String.valueOf(numberCell.getValue());
						
					}else if(row[j].getType() == CellType.NUMBER_FORMULA){
						if(col[j].indexOf("\"") > -1){
							col[j] = col[j].substring(col[j].indexOf("\"")+1);
							if(col[j].indexOf("\"") > -1)
								col[j] = col[j].substring(col[j].indexOf("\"")+1);
						}else if(col[j].indexOf("$") > -1){
							col[j] = col[j].substring(col[j].indexOf("$") + 1);
						}
						if(col[j].indexOf(",") > -1)
							col[j] = col[j].replaceAll(",","");
						if(col[j].indexOf("%") > -1 && col[j].indexOf("%") == col[j].length()-1){									
							try{
								col[j] = String.valueOf(Double.parseDouble(col[j].substring(0,col[j].indexOf("%"))));
							}catch(Exception ex){}									
						}
					}
					
					if(pas==0){
						if (row[j].getType() != CellType.EMPTY) {
							if (col[j].indexOf("���Ϳھ�") > -1) {
								setElement(detailHeader, "fitechRange", col[j]
										.substring(col[j].indexOf("���Ϳھ�") + 5));
	//							//���ݷ���id
								name.setRangId(Util.getRangeId(col[j]
																	.substring(col[j].indexOf("���Ϳھ�") + 5)));					
								continue;
							}								
	//						�ж��Ƿ�Ϊ��������
							if (col[j].indexOf("��������") > -1) {
								year = "";
								month = "";
								if(col[j].indexOf("��") > -1 && col[j].indexOf("��") > -1){
									if(col[j].indexOf("��") > -1){
										String src = col[j];
										year = src.substring(src.indexOf("��")+1,src.indexOf("��")).trim();
										src = col[j];
										month = src.substring(src.indexOf("��")+1,src.indexOf("��")).trim();
									}else{
										String src = col[j];
										year = src.substring(src.indexOf("��")+1,src.indexOf("��")).trim();
										src = col[j];
										month = src.substring(src.indexOf("��")+1,src.indexOf("��")).trim();
									}
									year = year.replaceAll("��","");
									year = year.trim().replaceAll("��","");
									month = month.replaceAll("��","");
									month = month.replaceAll("��","");
									setElement(detailHeader, "fitechSubmitYear", year);
									setElement(detailHeader, "fitechSubmitMonth", month);
								}	
								if(year.equals("") || month.equals("")){
								//	UploadFileActionold.messInfo = "������ʧ�ܣ������뱨�����ڣ�";
									InputData.messageInfo = "������ʧ�ܣ������뱨�����ڣ�";
									return null;
								}
								continue;
							}
	//						�ж��Ƿ�Ϊ���ҵ�λ
							if (col[j].indexOf("���ҵ�λ") > -1) {
								setElement(detailHeader, "fitechUnit", col[j]
										.substring(col[j].indexOf("���ҵ�λ") + 5));
								continue;
							}
							if(col[j].trim().equals("����")){
								pas=1;
								j = row.length;
								continue;
							}
						}
					}
					//��һ��ѭ��
					if(pas==1){
						String cols[]={"COL1","COL2","COL3","COL4","COL5","COL6","COL7","COL8"};
						if (n== 0) {
							temp = subform1.addElement("detail1");
						}
						if(n >= cols.length) continue;
						temp.addElement(cols[n] == null ? "" : cols[n])
							.setText(col[n] == null ? "" : col[n].trim());
						n++;
					}
					//��2��ѭ��
					if(pas==2){
						String cols[]={"COL1","COL2","COL3","COL4","COL5","COL6","COL7","COL8"};
						if (n== 0) {
							temp = subform1.addElement("detail2");
						}
						if(n >= cols.length) continue;
						temp.addElement(cols[n] == null ? "" : cols[n])
							.setText(col[n] == null ? "" : col[n].trim());
						n++;
					}
//					��3��ѭ��
					if(pas==3){
						String cols[]={"COL1","COL2","COL3","COL4","COL5","COL6","COL7"};
						if (n== 0) {
							temp = subform1.addElement("detail3");
						}
						if(n >= cols.length) continue;
						temp.addElement(cols[n] == null ? "" : cols[n])
							.setText(col[n] == null ? "" : col[n].trim());
						n++;
					}
					if(pas==4){
						if (row[j].getType() != CellType.EMPTY) {
							col[j] = row[j].getContents();
							if (col[j].indexOf("�����") > -1) {
								setElement(total, "fitechFiller", col[j]
										.substring(col[j].indexOf("�����") + 4));
								continue;
							}
							if (col[j].indexOf("������") > -1) {
								setElement(total, "fitechChecker", col[j]
										.substring(col[j].indexOf("������") + 4));
								continue;
							}
							if (col[j].indexOf("������") > -1) {
								setElement(total, "fitechPrincipal", col[j]
										.substring(col[j].indexOf("������") + 4));
								continue;
							}
						}
					}
					
				}
				
			}
				
			temp = subform1.element("TEMP");
			subform1.remove(temp);
			w1.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return document;
	}
	
	/**

	public static Document getS3800(String excel, String year, String month,
			Name name) {
		
		Document document = null;
		try{
			File inputWorkbook;
			inputWorkbook = new File(excel);
			Workbook w1 = Workbook.getWorkbook(inputWorkbook);
			String col[] = null;
			SAXReader saxReader = new SAXReader();
			String modelurl = Util.getFullPathRelateClass("S3800.xml", P2P2Xml.class);
			document = saxReader.read(new File(modelurl));
			Element root = document.getRootElement();
			List  subform1List=root.elements("Subform1");
			Element subform1=(Element)subform1List.get(0);
			Element detailHeader=subform1.element("detailHeader1");	
			Element total=subform1.element("total1");	
			Sheet s = w1.getSheet(0);
			Element temp = subform1.addElement("TEMP");
			Cell[] row = null;
			
			int x=1;
			S38Detail detail;
			S38SubDetail subDetail;
			for (int i = 1; i < s.getRows(); i++) {
				row = s.getRow(i);
				col = new String[row.length];
				for (int j = 0; j < row.length; j++) {
					if(i<6){
						if (row[j].getType() != CellType.EMPTY) {
							col[j] = row[j].getContents();
							if (col[j].indexOf("�����") > -1) {
								setElement(total, "fitechFiller", col[j]
										.substring(col[j].indexOf("�����") + 4));
								continue;
							}
							if (col[j].indexOf("������") > -1) {
								setElement(total, "fitechChecker", col[j]
										.substring(col[j].indexOf("������") + 4));
								continue;
							}
							if (col[j].indexOf("������") > -1) {
								setElement(total, "fitechPrincipal", col[j]
										.substring(col[j].indexOf("������") + 4));
								continue;
							}
						}
					
					}
					if(i>5){
						col[j] = row[j].getContents();
						if(col[j].indexOf("�ɶ����������")>-1){
							detail=new S38Detail();
						}
						x++;
					}
				}
						
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return document;
	}
	*/
	public static Document getS4400(String excel, String year, String month,
			Name name) {
		
		Document document = null;
		
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


	private static String getContents(Cell cell,String cellValue){
		if(cell.getType() == CellType.NUMBER){
			if(cellValue.indexOf("\"") > -1){
				cellValue = cellValue.substring(cellValue.indexOf("\"")+1);
				if(cellValue.indexOf("\"") > -1)
					cellValue = cellValue.substring(cellValue.indexOf("\"")+1);
			}
			else if(cellValue.indexOf("$") > -1){
				cellValue = cellValue.substring(cellValue.indexOf("$") + 1);
			}
			if(cellValue.indexOf(",") > -1)
				cellValue = cellValue.replaceAll(",","");
		}else if(cell.getType() == CellType.NUMBER_FORMULA){
			if(cellValue.indexOf("\"") > -1){
				cellValue = cellValue.substring(cellValue.indexOf("\"")+1);
				if(cellValue.indexOf("\"") > -1)
					cellValue = cellValue.substring(cellValue.indexOf("\"")+1);
			}else if(cellValue.indexOf("$") > -1){
				cellValue = cellValue.substring(cellValue.indexOf("$") + 1);
			}
			if(cellValue.indexOf(",") > -1)
				cellValue = cellValue.replaceAll(",","");
		}
		return cellValue;
	}

	public static void main(String args[]) {
		
	
	}

}
