package com.fitech.net.Excel2Xml;

import java.io.File;
import java.util.List;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Excel2Xml4S3209 {
	/**
	 * 解析S3209
	 * @param excel
	 * @param year
	 * @param month
	 * @param name
	 * @return
	 * 
	 */	
	public static String year = "";
	public static String month = "";
	
	public static Document getS3209(String excel,Name name) {
		name.setRepId("S3209");
		Document document = null;
		try {
			File inputWorkbook;
			inputWorkbook = new File(excel);
			Workbook w1 = Workbook.getWorkbook(inputWorkbook);
			String col[] = null;
			SAXReader saxReader = new SAXReader();
			String modelurl = Util.getFullPathRelateClass("S3209.xml",
					P2P2Xml.class);
			document = saxReader.read(new File(modelurl));
			Element root = document.getRootElement();
			List subform1List = root.elements("Subform1");
			Element subform1 = (Element) subform1List.get(0);
			Element detailHeader = subform1.element("detailHeader1");
			Element total = subform1.element("total1");
			//detailHeader.element("fitechVersion").setText(name.getVersionid());
			Sheet s = w1.getSheet(0);
			Element temp = subform1.addElement("TEMP");
			Cell[] row = null;
			// 把报表分为几个阶段
			int pas = 0;
			for (int i = 0; i < s.getRows(); i++) {
				row = s.getRow(i);
				col = new String[row.length];
				int n = 0;
				switch (pas) {
				case 0:
					for (int j = 0; j < row.length; j++) {
						if (row[j].getType() != CellType.EMPTY) {
							col[j] = getValue(row[j]);
							if (col[j].indexOf("报送口径") > -1) {
								setElement(detailHeader, "fitechRange", col[j].substring(col[j].indexOf("报送口径") + 5));
	
								name.setRangId(Util.getRangeId(col[j].substring(col[j].indexOf("报送口径") + 5)));					
								continue;
							}			
							if (col[j].indexOf("报表日期") > -1) {
								year = "";
								month = "";
								if(col[j].indexOf("年") > -1 && col[j].indexOf("月") > -1){
									if(col[j].indexOf("：") > -1){
										String src = col[j];
										year = src.substring(src.indexOf("：")+1,src.indexOf("年")).trim();
										src = col[j];
										month = src.substring(src.indexOf("年")+1,src.indexOf("月")).trim();
									}else{
										String src = col[j];
										year = src.substring(src.indexOf("期")+1,src.indexOf("年")).trim();
										src = col[j];
										month = src.substring(src.indexOf("年")+1,src.indexOf("月")).trim();
									}
									setElement(detailHeader, "fitechSubmitYear", year);
									setElement(detailHeader, "fitechSubmitMonth", month);
								}	
								if(year.equals("") || month.equals(""))
									return null;
								continue;
							}

							if (col[j].trim().equals("其他"))
								pas = 1;
						}
					}

					break;
				case 1:
					for (int j = 0; j < row.length; j++) {
						col[j] = getValue(row[j]);
						if (row[0].getContents().indexOf("合计")>-1) {
							setElement(total, "COL4", row[3].getContents());
							setElement(total, "COL5", row[4].getContents());
							setElement(total, "COL6", row[5].getContents());
							setElement(total, "COL7", row[6].getContents());
							setElement(total, "COL8", row[7].getContents());
							subform1 = (Element) subform1List.get(1);
							detailHeader = subform1.element("detailHeader2");
							total = subform1.element("total2");
							i = i + 4;							
							pas = 2;
							j=row.length;
						} else {
							
							String cols[] = { "COL1", "COL2", "COL3", "COL4",
									"COL5", "COL6", "COL7", "COL8" };
							if (n == 0) {
								temp = subform1.addElement("detail1");
							}
							temp.addElement(cols[n] == null ? "" : cols[n])
									.setText(col[n] == null ? "" : col[n].trim());
							n++;

						}
					}
					break;
				case 2:
					for (int j = 0; j < row.length; j++) {
						col[j] = getValue(row[j]);
						if (row[0].getContents().indexOf("合计")>-1) {
							setElement(total, "COL4", row[3].getContents());
							setElement(total, "COL5", row[4].getContents());
							setElement(total, "COL6", row[5].getContents());
							setElement(total, "COL7", row[6].getContents());
							setElement(total, "COL8", row[7].getContents());
							subform1 = (Element) subform1List.get(2);
							detailHeader = subform1.element("detailHeader3");
							total = subform1.element("total3");
							i = i + 5;							
							pas = 3;
							j=row.length;
						} else {
							
							String cols[] = { "COL1", "COL2", "COL3", "COL4",
									"COL5", "COL6", "COL7", "COL8" };
							if (n == 0) {
								temp = subform1.addElement("detail2");
							}
							temp.addElement(cols[n] == null ? "" : cols[n])
									.setText(col[n] == null ? "" : col[n].trim());
							
							n++;
						}
					}
					break;
				case 3:
					for (int j = 0; j < row.length; j++) {
						col[j] = getValue(row[j]);
						if (row[0].getContents().indexOf("合计")>-1) {
							setElement(total, "COL4", row[3].getContents());
							setElement(total, "COL5", row[4].getContents());
							setElement(total, "COL6", row[5].getContents());
							setElement(total, "COL7", row[6].getContents());
							pas = 4;
							j=row.length;
						} else {
							String cols[] = { "COL1", "COL2", "COL3", "COL4",
									"COL5", "COL6", "COL7" };
							if (n == 0) {
								temp = subform1.addElement("detail3");
							}

							if (n < cols.length) {
								temp.addElement(cols[n] == null ? "" : cols[n])
										.setText(col[n] == null ? "" : col[n].trim());
							}
							n++;
						}
					}
					break;
				case 4:
					for (int j = 0; j < row.length; j++) {
						col[j] = getValue(row[j]);
						if (row[j].getType() != CellType.EMPTY) {
							col[j] = row[j].getContents();
							if (col[j].indexOf("填表人") > -1) {
								setElement(total, "fitechFiller", col[j]
										.substring(col[j].indexOf("填表人") + 4));
								continue;
							}
							if (col[j].indexOf("复核人") > -1) {
								setElement(total, "fitechChecker", col[j]
										.substring(col[j].indexOf("复核人") + 4));
								continue;
							}
							if (col[j].indexOf("负责人") > -1) {
								setElement(total, "fitechPrincipal", col[j]
										.substring(col[j].indexOf("负责人") + 4));
								continue;
							}
						}

					}

				}
			}

			temp = subform1.element("TEMP");
			subform1.remove(temp);
			w1.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	private static String getValue(Cell cell){
		String value = cell.getContents();
		
		if(cell.getType() == CellType.NUMBER){
			if(value.indexOf("\"") > -1){
				value = value.substring(value.indexOf("\"")+1);
				if(value.indexOf("\"") > -1)
					value = value.substring(value.indexOf("\"")+1);
			}
			else if(value.indexOf("$") > -1){
				value = value.substring(value.indexOf("$") + 1);
			}
			if(value.indexOf(",") > -1)
				value = value.replaceAll(",","");
		}else if(cell.getType() == CellType.NUMBER_FORMULA){
			if(value.indexOf("\"") > -1){
				value = value.substring(value.indexOf("\"")+1);
				if(value.indexOf("\"") > -1)
					value = value.substring(value.indexOf("\"")+1);
			}else if(value.indexOf("$") > -1){
				value = value.substring(value.indexOf("$") + 1);
			}
			if(value.indexOf(",") > -1)
				value = value.replaceAll(",","");
		}
		return value;
	}
	/**
	 * 给特殊标签赋值
	 * 
	 * @param element
	 */
	private static void setElement(Element p, String element, String value) {
		if(value==null||value.equals(""))
			return;
		Element e = p.element(element);
		e.setText(value);
	}

}
