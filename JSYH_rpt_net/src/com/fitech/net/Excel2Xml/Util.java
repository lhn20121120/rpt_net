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
	 * ת��intΪexcel�ж�Ӧ���ַ�
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
	 * * �����������ͨ����ĳ�����class�ļ������·������ȡ�ļ���Ŀ¼�ľ���·���� ͨ���ڳ����к��Ѷ�λĳ�����·�����ر�����B/SӦ���С� 
	 * ͨ��������������ǿ��Ը������ǳ�����������ļ���λ������λĳ�����·���� *
	 * ���磺ĳ��txt�ļ�����ڳ����Test���ļ���·����../../resource/test.txt�� *
	 * ��ôʹ�ñ�����Path.getFullPathRelateClass("../../resource/test.txt",Test.class) *
	 * �õ��Ľ����txt�ļ�����ϵͳ�еľ���·���� * *
	 * 
	 * @param relatedPath ���·�� 
	 * @param cls ������λ���� 
	 * @return ���·������Ӧ�ľ���·�� *
	 * @throws IOException  ��Ϊ����������ѯ�ļ�ϵͳ�����Կ����׳�IO�쳣
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
	 * * ��ȡһ�����class�ļ����ڵľ���·���� ����������JDK������࣬Ҳ�������û��Զ�����࣬�����ǵ���������������ࡣ *
	 * ֻҪ���ڱ������п��Ա����ص��࣬�����Զ�λ������class�ļ��ľ���·���� * *
	 * 
	 * @param cls *
	 *            һ�������Class���� *
	 * @return ������class�ļ�λ�õľ���·���� ���û�������Ķ��壬�򷵻�null��
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
	/** * ��ȡ���class�ļ�λ�õ�URL����������Ǳ���������ķ������������������á� */
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
	 * ��ȡconfig.xml��model
	 * ��ֵ��specialrepbean
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
	 * ��ȡconfig.xml��bill
	 * ��ֵ��BillBean
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
	 * ��ȡconfig.xml��bill_1
	 * ��ֵ��BillBean
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
	 * �õ����ݷ�Χid
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
	 * �õ��汾id
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
	 * �õ�excel�Ĵ����
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
				if(re.indexOf("GF04�����")>-1){
					row=s.getRow(2);
					if(row!=null&&row.length>0&&row[0].getType() != CellType.EMPTY){
						if(row[0].getContents().indexOf("��ע��Ŀ")>-1){
							re="GF04�����ע";
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
	 * �õ�excelС����
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
	 * �õ�������С������&�ָ�
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
				if(title.indexOf("GF04�����")>-1){
					rowTitle=s.getRow(2);
					if(rowTitle!=null&&rowTitle.length>0&&rowTitle[0].getType() != CellType.EMPTY){
						if(rowTitle[0].getContents().indexOf("��ע��Ŀ")>-1){
							title="GF04�����ע";
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
	 * ��ñ��Ϳھ�
	 * 
	 * @param excel �����ļ���
	 * @return String ���Ϳھ�
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
				if(content != null && content.indexOf("���Ϳھ�") > -1){
					dataRange = content.substring(content.indexOf("���Ϳھ�") + 5);
				}
			}else{
				row = s.getRow(2);
				if(row != null && row[0].getType() != CellType.EMPTY){
					content = row[0].getContents();
					if(content != null && content.indexOf("���Ϳھ�") > -1){
						dataRange = content.substring(content.indexOf("���Ϳھ�") + 5);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return dataRange;
	}
	
	/**
	 * �õ�excelС����
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
		 * ת������ �������Excel�ı���ת��Ϊgb2312
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
		   // System.out.println("��֧��"+encoding+"���뷽ʽ");
		   usee.printStackTrace();
		  }catch(sun.io.MalformedInputException mfie)
		  {
		   // System.out.println("���������Ч!!!");
		   mfie.printStackTrace();
		  }
		  return retString;
		}
	/**
	 * ���Ժ���
	 * @param args
	 */
	
	public static void main(String args[]){
		// System.out.println(getTitle("E:\\TEMP\\G01��ע �������֣��������ϸ����һ��.xls"));
	}

}
