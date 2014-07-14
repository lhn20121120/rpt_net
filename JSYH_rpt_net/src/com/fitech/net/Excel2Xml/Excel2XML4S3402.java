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

public class Excel2XML4S3402 {
	
	public static String year = "";
	public static String month = "";
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
			//把报表分为几个阶段
			int pas=0;
			int k=0;
			for (int i = 0; i < s.getRows(); i++) {
				row = s.getRow(i);
				
				
				col = new String[row.length];
				
				
				if(row[1].getContents().equals("合计")){
						if(pas==1){
							setElement(total, "COL4",row[3].getContents());							
							setElement(total, "COL5",row[4].getContents());
							setElement(total, "COL6",row[5].getContents());
							setElement(total, "COL7",row[6].getContents());
							setElement(total, "COL8",row[7].getContents());
							
							subform1=(Element)subform1List.get(1);
							detailHeader=subform1.element("detailHeader2");
							total=subform1.element("total2");	
							i=i+5;
							row = s.getRow(i);
							col = new String[row.length]; 
							pas=2;
						}else
						if(pas==2){							
							setElement(total, "COL4",row[3].getContents());														
							setElement(total, "COL9",row[8].getContents());
							setElement(total, "COL10",row[9].getContents());
							setElement(total, "COL11",row[10].getContents());
							setElement(total, "COL12",row[11].getContents());
							setElement(total, "COL13",row[12].getContents());
							setElement(total, "COL14",row[13].getContents());
							pas=3;
						}
				}
				int n=0;
				
				for(int j=0;j<row.length;j++){										
					if(pas==0){
						if (row[j].getType() != CellType.EMPTY) {
							
							col[j] = row[j].getContents();							
							if (col[j].indexOf("报送口径") > -1) {
								setElement(detailHeader, "fitechRange", col[j]
										.substring(col[j].indexOf("报送口径") + 5));
	//							//数据范文id
								name.setRangId(Util.getRangeId(col[j]
																	.substring(col[j].indexOf("报送口径") + 5)));					
								continue;
							}								
	//						判断是否为报表日期
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
//								if(year.equals("") || month.equals(""))
//									return null;
								continue;
							}
	//						判断是否为货币单位
							if (col[j].indexOf("货币单位") > -1) {
								setElement(detailHeader, "fitechUnit", col[j]
										.substring(col[j].indexOf("货币单位") + 5));
								continue;
							}
							if(col[j].trim().equals("损失"))
							{
								pas=1;
								continue;
							}
						}
					}
					
					//第一个循环
					if(pas==1){
						String[] cols={"COL1","COL2","COL3","COL4","COL5","COL6","COL7","COL8","COL9","COL10","COL11","COL12","COL13"};
						if (n == 0) {
							temp = subform1.addElement("detail1");		
						}						
					//	temp.addElement(cols[n]);		
						
						if(row[n].getContents().trim().length()!=0)
						{
							temp.addElement(cols[n]).setText(row[n].getContents());
						}
						temp.addElement(cols[n]);					
						
						n++;					
					}
					//第2个循环
					if(pas==2){
						String cols[]={"COL1","COL2","COL3","COL4","COL5","COL6","COL7","COL8","COL9","COL10","COL11","COL12","COL13","COL14"};
						if (n== 0) {							
							temp = subform1.addElement("detail2");
						}						
						if(row[n].getContents().trim().length()!=0)
						{
							temp.addElement(cols[n]).setText(row[n].getContents());
						}
						temp.addElement(cols[n]);						
						
						n++;										
					}
					if(pas==3){						
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
			subform1.remove(temp);
			w1.close();
		}catch(Exception e){
			e.printStackTrace();
		}		
		return document;
		
	}
	private static void setElement(Element p, String element, String value) {
		if(value==null||value.equals(""))
			return;
		Element e = p.element(element);
		e.setText(value);
	}

}
