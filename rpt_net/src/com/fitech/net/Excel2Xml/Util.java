package com.fitech.net.Excel2Xml;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.cbrc.smis.common.Config;

public class Util {
	/**
	 * 转换int为excel列对应的字符
	 * @param col
	 * @return
	 */
	public static String indexCol(int col)
	{
		if(col<26)
		{
			return String.valueOf((char)('A'+col));
		}
		else
		{
			int x1=col/26-1+'A';
			int x2=col%26+'A';
			return String.valueOf((char)x1)+String.valueOf((char)x2);
		}		
	}	
	/**
	 * * 这个方法可以通过与某个类的class文件的相对路径来获取文件或目录的绝对路径。 通常在程序中很难定位某个相对路径，特别是在B/S应用中。 
	 * 通过这个方法，我们可以根据我们程序自身的类文件的位置来定位某个相对路径。 *
	 * 比如：某个txt文件相对于程序的Test类文件的路径是../../resource/test.txt， *
	 * 那么使用本方法Path.getFullPathRelateClass("../../resource/test.txt",Test.class) *
	 * 得到的结果是txt文件的在系统中的绝对路径。 * *
	 * 
	 * @param relatedPath 相对路径 
	 * @param cls 用来定位的类 
	 * @return 相对路径所对应的绝对路径 *
	 * @throws IOException  因为本方法将查询文件系统，所以可能抛出IO异常
	 */
	public static String getFullPathRelateClass(String relatedPath, Class cls)
			throws IOException {
		String path = null;
		if (relatedPath == null) {
			throw new NullPointerException();
		}
		String clsPath = getPathFromClass(cls);
		File clsFile = new File(clsPath);
		//String tempPath = clsFile.getParent() + File.separator + relatedPath;
		String tempPath = Config.WEBROOTPATH + "WEB-INF" + File.separator + "classes" + File.separator 
				+ "com" + File.separator + "fitech" + File.separator + "net" + File.separator + "Excel2Xml" 
				+ File.separator + "config" + File.separator + relatedPath;
		File file = new File(tempPath);
		path = file.getCanonicalPath();
		return path;
	}
	/**
	 * * 获取一个类的class文件所在的绝对路径。 这个类可以是JDK自身的类，也可以是用户自定义的类，或者是第三方开发包里的类。 *
	 * 只要是在本程序中可以被加载的类，都可以定位到它的class文件的绝对路径。 * *
	 * 
	 * @param cls *
	 *            一个对象的Class属性 *
	 * @return 这个类的class文件位置的绝对路径。 如果没有这个类的定义，则返回null。
	 */
	protected static String getPathFromClass(Class cls) throws IOException {
		String path = null;
		if (cls == null) {
			throw new NullPointerException();
		}
		URL url = getClassLocationURL(cls);
		if (url != null) {
			path = url.getPath();
			if ("jar".equalsIgnoreCase(url.getProtocol())) {
				try {
					path = new URL(path).getPath();
				} catch (MalformedURLException e) {
				}
				int location = path.indexOf("!/");
				if (location != -1) {
					path = path.substring(0, location);
				}
			}
			File file = new File(path);
			path = file.getCanonicalPath();
		}
		return path;
	}
	/** * 获取类的class文件位置的URL。这个方法是本类最基础的方法，供其它方法调用。 */
	private static URL getClassLocationURL(final Class cls) {
		if (cls == null)
			throw new IllegalArgumentException("null input: cls");
		URL result = null;
		final String clsAsResource = cls.getName().replace('.', '/').concat(
				".class");
		final ProtectionDomain pd = cls.getProtectionDomain();

		if (result != null) {
			if ("file".equals(result.getProtocol())) {
				try {
					if (result.toExternalForm().endsWith(".jar")
							|| result.toExternalForm().endsWith(".zip"))
						result = new URL("jar:".concat(result.toExternalForm())
								.concat("!/").concat(clsAsResource));
					else if (new File(result.getFile()).isDirectory())
						result = new URL(result, clsAsResource);
				} catch (MalformedURLException ignore) {
				}
			}
		}

		final ClassLoader clsLoader = cls.getClassLoader();
		result = clsLoader != null ? clsLoader.getResource(clsAsResource)
				: ClassLoader.getSystemResource(clsAsResource);

		return result;
	}
	
	
	/**
	 * 读取config.xml得model
	 * 付值给specialrepbean
	 */
	protected static List getSpRepBean(){
		List re=new ArrayList();
		try {
			String config=getFullPathRelateClass("config.xml",Util.class);
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new File(config));
			Element root = document.getRootElement();
			for (Iterator i = root.elementIterator("model"); i.hasNext();) {
				Element e= (Element) i.next();
				SpecialRepBean rep=new SpecialRepBean();
				rep.setRow(e.attributeValue("row"));
				rep.setTitle(e.attributeValue("title"));
				rep.setCol(e.attributeValue("col"));
				List partList=new ArrayList();
				for (Iterator j = e.elementIterator("part"); j.hasNext();) {
					Element e1= (Element) j.next();
					Part part=new Part();
					part.setId(e1.attributeValue("id"));
					part.setSubtitle(e1.elementText("subtitle"));
					part.setStartRow(e1.elementTextTrim("startRow"));
					
					partList.add(part);
				
				}
				rep.setPartList(partList);
				re.add(rep);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return re;
	}
	/**
	 * 读取config.xml得bill
	 * 付值给BillBean
	 */
	protected static List getBillBean(){
		List re=new ArrayList();
		try {
			String config=getFullPathRelateClass("config.xml",Util.class);
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new File(config));
			Element root = document.getRootElement();
			for (Iterator i = root.elementIterator("bill"); i.hasNext();) {
				Element e= (Element) i.next();
				BillBean bill=new BillBean();
				bill.setDetail(e.elementText("detail").split(","));
				bill.setEndStr(e.elementText("endstr"));
				bill.setRepId(e.attributeValue("id"));
				bill.setModel(e.attributeValue("model"));
				bill.setStartRow(Integer.parseInt(e.elementText("startrow")));
				bill.setTitle(e.attributeValue("title"));
				
				
				re.add(bill);
							
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return re;
	}
	/**
	 * 读取config.xml得bill_1
	 * 付值给BillBean
	 */
	protected static List getBill_1Bean(){
		List re=new ArrayList();
		try {
			String config=getFullPathRelateClass("config.xml",Util.class);
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new File(config));
			Element root = document.getRootElement();
			for (Iterator i = root.elementIterator("bill_1"); i.hasNext();) {
				Element e= (Element) i.next();
				Bill_1Bean bill=new Bill_1Bean();
				bill.setDetail(e.elementText("detail").split(","));
				bill.setTotal(e.elementText("total").split(","));
				bill.setSubtitle(e.elementText("subtitle"));
				bill.setRepId(e.attributeValue("id"));
				bill.setModel(e.attributeValue("model"));
				bill.setStartRow(Integer.parseInt(e.elementText("startrow")));
				bill.setTitle(e.attributeValue("title"));
				
				
				re.add(bill);
							
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return re;
	}
	/**
	 * 得到数据范围id
	 * @param rangname
	 * @return
	 */
	protected static String getRangeId(String rangname){
		
		String re="";
		if(rangname==null||rangname.equals(""))
			return re;
		try{
			String config=getFullPathRelateClass("config.xml",Util.class);
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new File(config));
			Element root = document.getRootElement();
			for (Iterator i = root.elementIterator("range"); i.hasNext();) {
				Element e= (Element) i.next();
				if(rangname.trim().equals(e.elementTextTrim("name"))){
					re=e.elementTextTrim("id");
					return re;
				}
					
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return re;
	}
	/**
	 * 得到版本id
	 * @param rangname
	 * @return
	 */
	protected static String getVersionId(){
		
		String re="";
		try{
			//String config=getFullPathRelateClass("config.xml",Util.class);
			String config=getFullPathRelateClass("config.xml",Util.class);
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new File(config));
			
			Element root = document.getRootElement();
			re=root.elementText("version");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return re;
	}
	/**
	 * 得到excel的大标题
	 * @param excel
	 * @return
	 */
	public  String getTitle(String excel){
		String re="";
		try{
			File inputWorkbook;	
			inputWorkbook = new File(excel);
			Workbook w1 = Workbook.getWorkbook(inputWorkbook);
		
			Sheet s = w1.getSheet(0);
			Cell[] row = s.getRow(0);
			if (row!=null&&row.length>0&&row[0].getType() != CellType.EMPTY) {
				re=row[0].getContents().replaceAll(" ", "");
				if(re.indexOf("GF04利润表")>-1){
					row=s.getRow(2);
					if(row!=null&&row.length>0&&row[0].getType() != CellType.EMPTY){
						if(row[0].getContents().indexOf("附注项目")>-1){
							re="GF04利润表附注";
						}
					}
				}
			}else {
				row=s.getRow(1);
				if (row!=null&&row.length>2&&row[1].getType() != CellType.EMPTY) {
					re=row[1].getContents().replaceAll(" ", "");
				}
				
			}
			w1.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return re;
		
	}

	
	/**
	 * 得到excel小标题
	 * @param cell
	 * @return
	 */
	public  String getSubtitle(String excel){
		String re="";
		String col="0";
		String row="3";
		try{
			File inputWorkbook;
			inputWorkbook = new File(excel);
			Workbook w1 = Workbook.getWorkbook(inputWorkbook);
			Sheet s = w1.getSheet(0);
			Cell[] row1 = s.getRow(Integer.parseInt(row)-1);
			if (row1[Integer.parseInt(col)].getType() != CellType.EMPTY) {
				re=row1[Integer.parseInt(col)].getContents().trim();
			}
			w1.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return re;
		
	}
	/**
	 * 得到大标题和小标题用&分割
	 * @param cell
	 * @return
	 */
	public  String getTitleS(String excel){
		String re="";
		String col="0";
		String row="3";
		String title="";
		String subTitle="";
		try{;
			File inputWorkbook;
			inputWorkbook = new File(excel);
			Workbook w1 = Workbook.getWorkbook(inputWorkbook);
			Sheet s = w1.getSheet(0);
			
			
			
			Cell[] rowTitle = s.getRow(0);
			if (rowTitle!=null&&rowTitle.length>0&&rowTitle[0].getType() != CellType.EMPTY) {
				title=rowTitle[0].getContents().replaceAll(" ", "");
				if(title.indexOf("GF04利润表")>-1){
					rowTitle=s.getRow(2);
					if(rowTitle!=null&&rowTitle.length>0&&rowTitle[0].getType() != CellType.EMPTY){
						if(rowTitle[0].getContents().indexOf("附注项目")>-1){
							title="GF04利润表附注";
						}
					}
				}
			}else {
				rowTitle=s.getRow(1);
				if (rowTitle!=null&&rowTitle.length>2&&rowTitle[1].getType() != CellType.EMPTY) {
					title=rowTitle[1].getContents().replaceAll(" ", "");
				}
				
			}
			
			
			
			Cell[] row1 = s.getRow(Integer.parseInt(row)-1);
			if (row1[Integer.parseInt(col)].getType() != CellType.EMPTY) {
				subTitle=row1[Integer.parseInt(col)].getContents().trim();
			}
			w1.close();
			re=title+"&"+subTitle;
		}catch(Exception e){
			e.printStackTrace();
		}
		return re;
		
	}
	/**
	 * 获得报送口径
	 * 
	 * @param excel 报表文件名
	 * @return String 报送口径
	 */
	public static String getDataRangeDes(String excel){
		String dataRange = null;
		try{
			File inputWorkbook = new File(excel);
			Workbook w1 = Workbook.getWorkbook(inputWorkbook);
			Sheet s = w1.getSheet(0);
			Cell[] row = s.getRow(1);
			String content = null;
			
			if(row != null && row[0].getType() != CellType.EMPTY){
				content = row[0].getContents();
				if(content != null && content.indexOf("报送口径") > -1){
					dataRange = content.substring(content.indexOf("报送口径") + 5);
				}
			}else{
				row = s.getRow(2);
				if(row != null && row[0].getType() != CellType.EMPTY){
					content = row[0].getContents();
					if(content != null && content.indexOf("报送口径") > -1){
						dataRange = content.substring(content.indexOf("报送口径") + 5);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return dataRange;
	}
	
	/**
	 * 得到excel小标题
	 * @param cell
	 * @return
	 */
	protected static String getfitechCurr(String excel){
		String re="";
		String col="0";
		String row="4";
		try{
			File inputWorkbook;
			inputWorkbook = new File(excel);
			Workbook w1 = Workbook.getWorkbook(inputWorkbook);
			Sheet s = w1.getSheet(0);
			Cell[] row1 = s.getRow(Integer.parseInt(row)-1);
			if (row1[Integer.parseInt(col)].getType() != CellType.EMPTY) {
				re=row1[Integer.parseInt(col)].getContents().trim();
			}
			w1.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return re;
		
	}
	 public static String ex_chinese(String s)
	    {
	        if(s == null)
	            s = "";
	        else
	            try
	            {
	                s = new String(s.getBytes("iso-8859-1"), "gb2312");
	            }
	            catch(Exception exception) { }
	        return s;
	    }
		/**
		 * 转化编码 把输出到Excel的编码转换为gb2312
		 * @param toEncoded
		 * @param encoding
		 * @return
		 */
		public static String getUnicode(String toEncoded,String encoding)
		{
			//// System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		  String retString="";
		  if(toEncoded.equals("")||toEncoded.trim().equals(""))
		  {
		    return toEncoded;
		  }
		  try
		  {
		  byte[] b=toEncoded.getBytes(encoding);
		  sun.io.ByteToCharConverter  convertor=sun.io.ByteToCharConverter.getConverter(encoding);
		  char [] c=convertor.convertAll(b);
		  for(int i=0;i<c.length;i++)
		  {
		    retString+=String.valueOf(c[i]);
		  }
		  }catch(java.io.UnsupportedEncodingException usee)
		  {
		   // System.out.println("不支持"+encoding+"编码方式");
		   usee.printStackTrace();
		  }catch(sun.io.MalformedInputException mfie)
		  {
		   // System.out.println("输入参数无效!!!");
		   mfie.printStackTrace();
		  }
		  return retString;
		}
	/**
	 * 测试函数
	 * @param args
	 */
	
	public static void main(String args[]){
		// System.out.println(getTitle("E:\\TEMP\\G01附注 第三部分：存贷款明细报表（一）.xls"));
	}

}
