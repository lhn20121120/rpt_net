package com.cbrc.smis.system.cb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class ChangeFormu {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
//		String s = "　2006　";
//		String t = " 2006 ";
//		
//		Calendar calendar = Calendar.getInstance();
//		StringBuffer buffer = new StringBuffer();
//		buffer.append(calendar.get(Calendar.YEAR)).append(calendar.get(Calendar.MONTH)+1).append(calendar.get(Calendar.DATE))
//		.append(calendar.get(Calendar.HOUR_OF_DAY)).append(calendar.get(Calendar.MINUTE)).append(calendar.get(Calendar.SECOND))
//		.append(calendar.get(Calendar.MILLISECOND));
//		// System.out.println(buffer.toString());
//		
//		String test = "'0909'";
//		test = test.substring(test.indexOf("'")+1);
//		// System.out.println(test);
//		test = test.substring(0,test.indexOf("'"));
//		// System.out.println(test);
//		
//		DecimalFormat   format   =   new   java.text.DecimalFormat("#,##0.000000000");   
//		     
//		 double strCell = Double.parseDouble("1234568734509.908971298");//aCell.getNumericCellValue(); 
//		      
//		 String temp = format.format(strCell);
//		
//		 // System.out.println(temp);
//		 
//		 
//		 strCell = Double.parseDouble("1234568734509.90");
//		 
//		 temp = format.format(strCell);
//		 
//		 // System.out.println(temp);
//		FileInputStream fileInStream = null;
//		HSSFWorkbook sourceWb = null;
//		HSSFSheet sheet = null;
//		String fileName = "D:\\G0100_0514.xls";
//		String[] ARRCOLS={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"};
//		
//		try {
//			fileInStream = new FileInputStream(fileName);
//			POIFSFileSystem srcFile = new POIFSFileSystem(fileInStream);
//			sourceWb = new HSSFWorkbook(srcFile);
//			if (sourceWb.getNumberOfSheets() > 0) {
//				sheet = sourceWb.getSheetAt(0);
//			}
//			fileInStream.close();
//		}catch (FileNotFoundException e) {
//			
//			e.printStackTrace();
//		} catch (IOException e) {
//			
//			e.printStackTrace();
//		}	
//		
//		HSSFRow row = null;
//	    HSSFCell cell = null;
//	   
//	    
//	    for(Iterator iter=sheet.rowIterator();iter.hasNext();){
//	    	row = (HSSFRow)iter.next();
//		  
//		    for(short i=row.getFirstCellNum(),n=row.getLastCellNum();i<n;i++){
//		    	
//		    	cell = (HSSFCell)row.getCell(i);
//		    	
//		    	if(cell == null) continue;
//		    	
//		    	if(cell.getCellType() == HSSFCell.CELL_TYPE_BLANK){
//		    		
//		    	}
//		    	
//		    }
//	    }
//	    
//		
//	    
//	    try {
//			FileOutputStream stream = new FileOutputStream(fileName);
//			try {
//				sourceWb.write(stream);
//				stream.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		//3782973.63/(24013953.08*2%+5774949.77*25%+26899951.17*50%+42317094.95*100%)
//		Formula formula = new Formula("3782973.63/(24013953.08*0.02+5774949.77*0.25+26899951.17*0.5+42317094.95*1)");
//		double d = formula.getResult();
//		// System.out.println(d);
		
		
	    
//		String title = "指标分析预警超势图";
//		String domain = "月份走势";
//		String range = "指标值";
//		String subtitleStr = "XXXX指标分析";
//		
//
//		TimeSeries ca = new TimeSeries("2006");
//		RegularTimePeriod c;
//		//for(int i=1995;i<=1999;i++){
//			//ca.add(new TimeSeriesDataItem(new Day(1,i,2005),new Double(500+Math.random()*100)));
//			for(int mon=0;mon<12;mon++){
//				ca.add(new TimeSeriesDataItem(new Day(1,mon+1,2006),new Double(500+Math.random()*100)));
//			}
//		//}
//		
//		//TimeSeries ca2 = new TimeSeries("2005");
//		//XYSeries series1 = new XYSeries("First");
//		//for(int i=1;i<=12;i++){
//			//ca2.add(new TimeSeriesDataItem(new Day(1,i,2006),new Double(500+Math.random()*100)));
//			//series1.add(i,new Double(500+Math.random()*100));
//			//for(int mon=0;mon<12;mon++){
//				//ca.add(new TimeSeriesDataItem(new Day(1,mon+1,i),new Double(500+Math.random()*100)));
//			//}
//		//}
//		
//		TimeSeriesCollection dataset = new TimeSeriesCollection();
//		//XYSeriesCollection dataset2 = new XYSeriesCollection();
//
//		dataset.addSeries(ca);
//		//dataset.addSeries(ca2);
//		//dataset2.addSeries(series1);
//		
//		JFreeChart chart = ChartFactory.createTimeSeriesChart(title,domain,range,dataset,true,true,false);
//		
//		TextTitle subtitle =new TextTitle(subtitleStr,new Font("黑体",Font.BOLD,12));
//		chart.addSubtitle(subtitle);
//		chart.setTitle(new TextTitle(title,new Font("隶书",Font.ITALIC,15)));
//		chart.setBackgroundPaint(new GradientPaint(0,0,Color.white,0,1000,Color.blue));
//		String fileName = "c:\\quxian.jpeg";
//		
//		try {
//			ChartUtilities.saveChartAsJPEG(new File(fileName),100,chart,600,600);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		String str = "2.00300000009E9";
//		
//		double s = Double.parseDouble(str) + Double.parseDouble(str);
//		// System.out.println(s);
//		
//		CategoryDataset dataset = ChangeFormu.getDataSet();
//		JFreeChart chart = ChartFactory.createBarChart3D("水果产量图","水果","产量",dataset,PlotOrientation.VERTICAL,true,false,false);
//		
//		FileOutputStream fos = null;
//		try {
//			fos = new FileOutputStream("c:\\fruit.jpg");
//			ChartUtilities.writeChartAsJPEG(fos,100,chart,400,300,null);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally{
//			try {
//				fos.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		
//		String fileName = "c:\\pdf_formu.txt";
//		String fileName2 = "c:\\new_formu.txt";
//		
//		try {
//			BufferedReader inStream = new BufferedReader(new FileReader(fileName));
//			StringBuffer strBuffer = new StringBuffer();
//			while(true){
//				String string = inStream.readLine();
//				if(string == null) break;
//				strBuffer.append(string).append("\n");
//			}
//			String txtStr = strBuffer.toString();
//			for(int n=48;n>=6;n--){
//				int numLen = String.valueOf(n).length();
//				for(int i=0;i<txtStr.length();i++){
//					String s = String.valueOf(txtStr.charAt(i));
//					if(s.equals("D") || s.equals("E") || s.equals("F")){
//						if(numLen == 1){
//							try{
//								if(i+1 < txtStr.length() && String.valueOf(txtStr.charAt(i+2)).equals(".")){
//									String s1 = String.valueOf(txtStr.charAt(i+1));
//									try{
//										int num = Integer.parseInt(s1);
//										if(num == n){
//											String backStr = new String(txtStr);
//											String pre = backStr.substring(0,i+1);
//											String sub = backStr.substring(i+2);
//											txtStr = pre + (num+1) + sub;
//										}else 
//											continue;
//										
//									}catch(Exception ex){
//										continue;
//									}
//								}else
//									continue;
//							}catch(Exception ex){
//								continue;
//							}
//							
//						}if(numLen == 2){
//							try{
//								if(i+2 < txtStr.length() && String.valueOf(txtStr.charAt(i+3)).equals(".")){
//									String s1 = String.valueOf(txtStr.charAt(i+1));
//									String s2 = String.valueOf(txtStr.charAt(i+2));
//									try{
//										int num = Integer.parseInt(s1+s2);
//										if(num == n){
//											String backStr = new String(txtStr);
//											String pre = backStr.substring(0,i+1);
//											String sub = backStr.substring(i+3);
//											txtStr = pre + (num+1) + sub;
//										}else 
//											continue;
//										
//									}catch(Exception ex){
//										continue;
//									}
//								}else
//									continue;
//							}catch(Exception ex){
//								continue;
//							}
//						}
//					}
//				}
//			}
//			
//			inStream.close();
//			BufferedWriter outStream = new BufferedWriter(new FileWriter(fileName2));
//			String[] lines = txtStr.split("\n");
//			for(int i=0;i<lines.length;i++){
//				outStream.write(lines[i]+"\n");
//			}
//			outStream.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
//		Connection conn = new FitechConnection().getConnect();
//		Statement stmt=null;
//		ResultSet rs=null;				
//		try {
//			String hql = "select CUR_NAME from M_CURR";		
//			stmt = conn.createStatement();
//			rs = stmt.executeQuery(hql);
//			
//			List result = new ArrayList();
//			int index = 10000;
//			while(rs.next()){
//				String str = rs.getString(1);
//				result.add("insert into M_REP_FREQ(REP_FREQ_ID, REP_FREQ_NAME) values(" + index + ",'" + str + "')");
//				index++;
//			}
//			
//			if(result.size() > 0){
//				for(int i=0;i<result.size();i++){
//					String sql = (String)result.get(i);
//					stmt.execute(sql);
//				}
//				conn.commit();
//			}
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally{
//			try{
//				if(conn != null) conn.close();
//				if(stmt != null) stmt.close();
//			}catch(Exception ex){
//				ex.printStackTrace();
//			}
//		}
		
//		float f = 9999999f;
//		// System.out.println(f);
				
		
		try {
			InputStream in = new FileInputStream(new File("D:\\ETL.xml"));
			
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List  list = new ArrayList();
		List resultList = new ArrayList();
		
		try {
			BufferedReader read = new BufferedReader(new FileReader("D:\\validator.xml"));
			try {				
				String line = "";
				while((line=read.readLine()) != null){
					if(line.indexOf("errorCode=\"") > -1){						
						list.add(line.substring(line.indexOf("errorCode=\"")+"errorCode=\"".length(),line.indexOf("errorCode=\"")+"errorCode=\"".length()+4));
					}
				}
				read.close();
				
				for(int i=0;i<list.size();i++){
					for(int j=i+1;j<list.size();j++){
						int a = Integer.parseInt((String)list.get(i));
						int b = Integer.parseInt((String)list.get(j));
						if(a > b ){
							String s = (String)list.get(i);
							list.set(i,list.get(j));
							list.set(j,s);
						}							
					}
				}
//				read = new BufferedReader(new FileReader("D:\\企业征信数据接口规范（mo）.doc"));
//				while((line=read.readLine()) != null){
//					for(int i=0;i<list.size();i++){
//						String errorCode = (String)list.get(i);
//						if(line.indexOf(errorCode) > -1){
//							resultList.add(errorCode + ":   " + line + "\n");
//						}
//					}
//				}
				
				
				
				read.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static CategoryDataset getDataSet(){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(100,"北京","苹果");
		dataset.addValue(100,"上海","苹果");
		dataset.addValue(100,"广州","苹果");
		dataset.addValue(100,"南京","苹果");		
		
		dataset.addValue(200,"北京","梨子");
		dataset.addValue(200,"上海","梨子");
		dataset.addValue(200,"广州","梨子");
		dataset.addValue(200,"南京","梨子");
		
		dataset.addValue(300,"北京","葡萄");
		dataset.addValue(300,"上海","葡萄");
		dataset.addValue(300,"广州","葡萄");
		dataset.addValue(300,"南京","葡萄");
		
		dataset.addValue(400,"北京","香蕉");
		dataset.addValue(400,"上海","香蕉");
		dataset.addValue(400,"广州","香蕉");
		dataset.addValue(400,"南京","香蕉");
		
		dataset.addValue(500,"北京","荔枝");
		dataset.addValue(500,"上海","荔枝");
		dataset.addValue(500,"广州","荔枝");
		//dataset.addValue(500,"南京","苹果");
				
		return dataset;
	}

}
