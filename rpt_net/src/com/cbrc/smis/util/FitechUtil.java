
package com.cbrc.smis.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionMapping;
import org.apache.tools.zip.ZipOutputStream;

import sun.misc.BASE64Encoder;

import com.cbrc.smis.adapter.StrutsReportDataDelegate;
import com.cbrc.smis.adapter.StrutsReportInDataDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.DownLoadDataToZip;
import com.cbrc.smis.form.ReportDataForm;
import com.cbrc.smis.hibernate.ReportInData;
/**
 * 常用方法类 
 * 
 * @author rds
 * @date 2005-11-13
 */
public class FitechUtil {	
	/**
	 * 日志
	 */
	private static FitechException log = new FitechException(FitechUtil.class);
	
	/**
	 * 非法的附件后缀名
	 */
	private static final String[] UNVALIDEXT = { "exe", "bat", "sh" };

	/**
	 * 星期
	 */
	private static final String[][] WEEK = { 
		{ "1", "星期一" }, 
		{ "2", "星期二" },
		{ "3", "星期三" }, 
		{ "4", "星期四" }, 
		{ "5", "星期五" }, 
		{ "6", "星期六" },
		{ "7", "星期日" } 
	};

	/**
	 * 加密算法名
	 */
	private static final String ALGORITH = "MD5";

	/**
	 * 将字符串解析成日期
	 * 
	 * @param _date String
	 * @return Date
	 */
	public static Date parseDate(String _date) {
		if (_date == null || _date.equals("") || _date.length() < 8) {
			return null;
		} else {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				return dateFormat.parse(_date);
			} catch (ParseException pe) {
				return null;
			}
		}
	}
	
	/**
	 * 获日期的格式化字符串
	 * 
	 * @param date Date 
	 * @return String
	 */
	public static String getDateString(Date date){
		if(date==null) return "";
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}
	
	/**
	 * 获得使用MD5加密的字符串的Base64串值
	 * 
	 * @param value String 要加密的字符串
	 * @return String 加密后的Base64串
	 */
	public static String hashMD5String(String value) {
		String result = "";

		try {
			MessageDigest md = MessageDigest.getInstance(ALGORITH);
			byte[] md5Value = md.digest(value.getBytes());
			BASE64Encoder encode = new BASE64Encoder();
			result = encode.encode(md5Value);
		} catch (NoSuchAlgorithmException nae) {
			new FitechException(nae);
		}

		return result;
	}
	
	/**
	 * 标准日期样式
	 */
	public static final String NORMALDATE = "yyyy-MM-dd";

	/**
	 * 中国标准的日期样式
	 */
	public static final String CHINESEDATE = "yyyy年MM月dd日";

	/**
	 * 获取当天的日期
	 * 
	 * @param format 日期样式
	 * @return String
	 */
	public static String getToday(String format) {
		if (format == null)
			return "";
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(new Date());
	}

	/**
	 * 获取当天是周几
	 * 
	 * @return String
	 */
	public static String getDay() {
		String _day = "";
		Date date = new Date();
		int day = date.getDay();
		for (int i = 0; i < WEEK.length; i++) {
			if (WEEK[i][0].equals(String.valueOf(day)) == true) {
				_day = WEEK[i][1];
				break;
			}
		}
		return _day;
	}

	/**
	 * getParameter方法<br>
	 * 对获取的提交值做GB2312编码转换<br>
	 * 
	 * @param request HttpServletRequest
	 * @param paramName String 变量名
	 * @return String
	 */
	public static String getParameter(HttpServletRequest request,String paramName) {
		if (paramName == null)
			return "";
		if (request.getParameter(paramName) == null)
			return "";
		try {
			//return request.getParameter(paramName);
			return new String(((String) request.getParameter(paramName)).getBytes("ISO8859-1"), "GB2312");
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 判断用户上传的附件是否是正确
	 * 
	 * @author rds
	 * 
	 * @param fileName String 上传附件的文件名
	 * @return boolean 上传的附件不合法，返回false；否则，返回true
	 */
	public static boolean isValidAffix(String fileName) {
		if (fileName == null)
			return false;

		String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

		for (int i = 0; i < UNVALIDEXT.length; i++) {
			if (UNVALIDEXT[i].toLowerCase().equals(ext)) {
				return false;
			}
		}

		return true;
	}
	
	/**
	 * 将日期字串格式化成时间串
	 * 
	 * @param date String 时间字符串
	 * @return Date 转化成功返回Date ，否则返回null
	 */
	public static Date formatToTimestamp(String date) {
		if (date != null && !date.equals("")) {
			SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
			try 
			{
				return FORMAT.parse(date);
			} 
			catch (ParseException e) 
			{
				e.printStackTrace();
				return null;
			}
		}
		
		return null;
	}
	
	/**
	 * 把request和session 范围内的属性删除
	 * @param mapping 
	 * @param request
	 */
	
	public static void removeAttribute(ActionMapping mapping,HttpServletRequest request)
	{
		   HttpSession session=request.getSession();
		   if(mapping.getAttribute() != null) 
		   {
			   if("request".equals(mapping.getScope()))
			        request.removeAttribute(mapping.getAttribute());
			   else
			      session.removeAttribute(mapping.getAttribute());
		   }
	}
	
	/**
	 * 写文件操作
	 * 
	 * @param in InputStream 文件流
	 * @param ext String 文件后缀名
	 * @param boolean true 写文件成功，返回写入的文件名；否则，返回null
	 */
	public static String writeFile(InputStream in,String ext){
		String tmpFileName=String.valueOf(System.currentTimeMillis()) + "." + ext;
		
		BufferedOutputStream bout=null;
		
		try{
			File file=new File(Config.TEMP_DIR + tmpFileName);
			bout=new BufferedOutputStream(new FileOutputStream(file));
			
			byte[] line=new byte[1024];
			while(in.read(line)!=-1){
				bout.write(line);
			}
		}catch(IOException ioe){
			log.printStackTrace(ioe);
			tmpFileName=null;
		}catch(Exception e){
			log.printStackTrace(e);
			tmpFileName=null;
		}finally{
			if(bout!=null) 
				try{
					bout.close();
				}catch(IOException ioe){
					log.printStackTrace(ioe);
				}
		}
		
		return tmpFileName;
	}
	
	/**
	 * 写文件操作
	 * 
	 * @param content String 文件内容
	 * @param ext String 文件后缀名
	 * @param boolean true 写文件成功，返回写入的文件名；否则，返回null
	 */
	public static String writeFile(String content,String ext){
		if(content==null || ext==null) return null;
		
		String tmpFileName=String.valueOf(System.currentTimeMillis()) + "." + ext;
		
		BufferedOutputStream bout=null;
		
		try{
			File file=new File(Config.TEMP_DIR + tmpFileName);
			bout=new BufferedOutputStream(new FileOutputStream(file));
			
			bout.write(content.getBytes());
		}catch(IOException ioe){
			log.printStackTrace(ioe);
			tmpFileName=null;
		}catch(Exception e){
			log.printStackTrace(e);
			tmpFileName=null;
		}finally{
			if(bout!=null) 
				try{
					bout.close();
				}catch(IOException ioe){
					log.printStackTrace(ioe);
				}
		}
		
		return tmpFileName;
	}
	
	/**
	 * 读文件操作
	 * 
	 * @param fileName String 文件名
	 * @reture InputStream 读取文件失败，返回null
	 */
	public static InputStream readFile(String fileName){
		BufferedInputStream bin=null;
		
		try{
			bin=new BufferedInputStream(new FileInputStream(
					new File(Config.TEMP_DIR + fileName)));
		}catch(IOException ioe){
			log.printStackTrace(ioe);
		}catch(Exception e){
			log.printStackTrace(e);
		}
		
		return bin;
	}
	public static InputStream readExcelFile(String fileName){
		BufferedInputStream bin=null;
		
		try{
			bin=new BufferedInputStream(new FileInputStream(
					new File(Config.WEBROOTPATH + fileName)));
		}catch(IOException ioe){
			log.printStackTrace(ioe);
		}catch(Exception e){
			log.printStackTrace(e);
		}
		
		return bin;
	}
	
	/**
	 * 删除文件操作
	 * 
	 * @param fileName String 要删除的文件
	 * @return boolean 删除成功，返回true;否则，返回false
	 */
	public static boolean deleteFile(String fileName){
		
		try{
			File file=new File(Config.TEMP_DIR + fileName);
			file.deleteOnExit();
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 根据关系表达式获取目标单元的名称
	 * 
	 * @param expression String 关系表达式
	 * @return String
	 */
	public static String getCellName(String expression){
		String cellName=null;
		
		if(expression==null) return cellName;
		
		if(expression.indexOf(Config.SPLIT_SYMBOL_EQUAL)>1){
			cellName=expression.substring(1,expression.indexOf(Config.SPLIT_SYMBOL_EQUAL)-1);
		}
		
		return cellName;
	}
	
	/**
	 * 读取实际数据报表的XML内容，并将其转换成PDF式的XML文件
	 * 
	 * @author rds
	 * @serialData 2005-12-18 12:07
	 * 
	 * @param repInId 实际数据报表ID
	 * @return InputStream 返回XML内容；内容为空是，返回null
	 * @exception Exception
	 */
	public static InputStream readRepInXML(Integer repInId) throws Exception{
		/*String xmlFileName=null;
		
		String xml="",_xml="";*/
		
		/*File file1=new File(Config.WEBROOTPATH + "WEB-INF" + Config.FILESEPARATOR + "G1600.xml");
		// System.out.println(file1.getName());
		StrutsReportInDataDelegate.insertReortInData(file1,new Integer(602));*/
		
		ReportInData reportInData=StrutsReportInDataDelegate.getReportInData(repInId);
		
		if(reportInData==null){
			return null;
		}
		
		try{
			//writeFile(reportInData.getXml().getBinaryStream(),Config.EXT_XML);
			//BufferedInputStream in=new BufferedInputStream(reportInData.getXml().getBinaryStream());
			
			/*BufferedReader reader=new BufferedReader(new InputStreamReader(reportInData.getXml().getBinaryStream()));
			String line="";
			while((line=reader.readLine())!=null){
				_xml+=line;
			}*/
			/*String tmpFile=writeFile(reportInData.getXml().getBinaryStream(),Config.EXT_XML);
			
			if(tmpFile==null){
				// System.out.println("tmpFile is null!");
				return null;
			}else{
				// System.out.println("tmpFile:" + tmpFile);
				BufferedReader reader=new BufferedReader(new FileReader(new File(Config.TEMP_DIR + tmpFile)));
				String line="";
				while((line=reader.readLine())!=null){
					xml+=line;
				}
			}*/
			
			/*String header="<?xfa generator='XFA2_0' APIVersion='2.2.5030.0'?> " + 
				"<xdp:xdp xmlns:xdp='http://ns.adobe.com/xdp/'>" + 
				"<xfa:datasets xmlns:xfa='http://www.xfa.org/schema/xfa-data/1.0/'><xfa:data>";
			// System.out.println("====================================================================");
			// System.out.println(_xml);
			int pos=_xml.indexOf(">",0);
			// System.out.println("====================================================================");

			if(pos<0) return null;			
			xml=_xml.substring(0,pos+1) + header + _xml.substring(pos+1);
			// System.out.println(xml);*/
			
			com.cbrc.smis.form.ReportInForm reportInForm=StrutsReportInDelegate.getReportIn(repInId);
			if(reportInForm==null){
				// System.out.println("reportInForm is null");
				return null;
			}			
			
			ReportDataForm reportData=StrutsReportDataDelegate.getReportData(reportInForm.getChildRepId(),reportInForm.getVersionId());
			if(reportData==null) return null;
			
			String childRepId=reportData.getChildRepId();
			String versionId=reportData.getVersionId();
			String pdfFileName=childRepId + "_" + versionId + "." + Config.EXT_PDF;
			
			File file=new File(Config.PDF_TEMPLATE_PATH + pdfFileName);
			if(file==null){
				// System.out.println("file is null");
				return null;
			}
			
			if(!file.exists()){
				ReportDataForm reportDataForm=StrutsReportDataDelegate.getReportData(childRepId,versionId);
				if(reportDataForm==null){
					// System.out.println("reportDataForm is null");
					return null;
				}
				if(writePDF(reportDataForm.getPdf().getBinaryStream(),pdfFileName)==false) return null;
			}
			
			String modelName=Config.PDF_TEMPLATE_URL + pdfFileName;
			setXDPTailer(modelName);
			
			//String tailer="</xfa:data></xfa:datasets><pdf href='" + modelName + "' xmlns='http://ns.adobe.com/xdp/pdf/'/></xdp:xdp>";
			
			/*
			xml+=tailer;
			xml=xml.replaceAll("'","\"");
			// System.out.println("====================================================================");
			// System.out.println(xml);
			
			
			xmlFileName=writeFile(xml,Config.EXT_XML);*/
		}catch(Exception sqle){
			log.printStackTrace(sqle);
			return null;
		}

		return reportInData.getXml().getBinaryStream();
	}
	
	/**
	 * 根据字节流写PDF文件
	 * 
	 * @author rds
	 * @serialField 2005-12-18 14:11
	 * 
	 * @param in InputStream  PDF字节流
	 * @param pdfFileName String 写入的文件名
	 * @return boolean 写入成功，返回true；否则，返回false
	 */
	private static boolean writePDF(InputStream in,String pdfFileName){
		boolean result=false;
		
		if(in==null || pdfFileName==null) return result;
			
		try{
			File file=new File(Config.PDF_TEMPLATE_PATH + pdfFileName);
			BufferedOutputStream bout=new BufferedOutputStream(new FileOutputStream(file));
			
			byte[] line=new byte[1024];
			while(in.read(line)!=-1){
				bout.write(line);
			}
			
			bout.close();
			result=true;
		}catch(IOException ioe){
			log.printStackTrace(ioe);
		}
		
		return result;
	}
	
///**
// * 把日期转换成Long
// * @author 曹发根
// * @param date
// * @return
// */
//    public static long date2long(Date date){
//    	   
//  	  return Long.parseLong(toSimpleDateFormat(date,"yyyyMMddHHmmss"));
//    }
//    /**
//     * 把日期转换为字符串
//     * @param date
//     * @param simpleDateFormat
//     * @return
//     */
//    public static String toSimpleDateFormat(Date date, String simpleDateFormat) {
//        SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat);
//        if (date == null) {
//          return null;
//        }
//        else {
//          return format.format(date);
//        }
//    }
    /**
     * 转换时间格式
     * @throws ParseException
     * @author 曹发根
     */
    public static String Formatter(Date date) throws ParseException {
		//Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(date);
	    return dateString;
	}
    /**
     * 得到当前时间
     * @throws ParseException
     * @author 曹发根
     */
    public static Date getNowTime() throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat  formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(calendar.getTime());
		return formatter.parse(dateString);
	}
    
    /**
     * 把文件名转换为日期名
     * @author 曹发根
     * @param fullname
     * @param recordTime
     * @return
     * @throws ParseException
     */
	public static  String getName(String fullname,Date recordTime) throws ParseException{
		int i=fullname.lastIndexOf(".");
        String type = "";
        if(i>0)
		 type =fullname.substring(i);
		return Formatter(recordTime)+type;
	}

	/**
	 * 获取参数的值
	 * 
	 * @param req HttpServletRequest 
	 * @param name String 
	 * @return String 
	 */
	public static String getRequestParameter(HttpServletRequest req, String name) {
		String value = req.getParameter(name);
		if (value == null) {
			if (req.getAttribute(name) != null)
				value = req.getAttribute(name).toString();
			else
				return null;
		}
		return value;
	}
	
	/**
	 * XDP文件的头部分
	 */
	private final static String XDP_HEADER="<?xfa generator=\"XFA2_0\" APIVersion=\"2.2.5030.0\"?>" + 
	"<xdp:xdp xmlns:xdp=\"http://ns.adobe.com/xdp/\">" + 
	"<xfa:datasets xmlns:xfa=\"http://www.xfa.org/schema/xfa-data/1.0/\"><xfa:data>";
	
	/**
	 * 获得XDP文件的头信息 
	 */
	public static String getXDPHeader(){
		return XDP_HEADER;
	}
	
	/**
	 * XDP文件的尾部分
	 */
	private static String XDP_TAILER="";
	/**
	 * 设置XDP文件的尾信息
	 * 
	 * @param modelName PDF模板名
	 * @return void
	 */
	public static void setXDPTailer(String modelName){
		XDP_TAILER="</xfa:data></xfa:datasets><pdf href=\"" + modelName + 
			"\" xmlns=\"http://ns.adobe.com/xdp/pdf/\"/></xdp:xdp>";
	}
	/**
	 * 获取XDP文件的尾信息
	 */
	public static String getXDPTailer(){
		return XDP_TAILER;
	}
	

	
	/**
	 * 将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名.
	 * 
	 * @param s 原文件名
	 * @return String 重新编码后的文件名
	 */
	public static String toUtf8String(String fileName) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < fileName.length(); i++) {
			char c = fileName.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 复制文件
	 * 
	 * @param sourceFilePath
	 *            源文件路径
	 * @param targetFilePath
	 *            目标文件路径
	 * @return 是否复制成功
	 */
	public static boolean copyFile(String sourceFilePath, String targetFilePath) throws Exception
	{
		//// System.out.println("sourceFilePath==" + sourceFilePath + "targetFilePath==" + targetFilePath);
		if (sourceFilePath == null || sourceFilePath.equals("") || targetFilePath == null
				|| targetFilePath.equals(""))
		{
			return false;
		}

		boolean result = false;
		/* 如果源文件和目标文件相同，则不做操作，默认操作成功 */
		if (sourceFilePath.trim().equals(targetFilePath.trim()))
			result = true;
		else
		{
			File sourceFile = new File(sourceFilePath);
			File targetFile = new File(targetFilePath);
			result = copyFile(sourceFile,targetFile);
		}
		return result;
	}

	
	/**
	 * 
	 * 方法说明:该方法用于将一个原文件拷贝为一个目的文件
	 * 
	 * @author Yao
	 * @date 2007-9-9
	 * @param srcFile
	 * @param destFile
	 * @return
	 */
	public static boolean copyFile(File sourceFile, File targetFile) throws Exception
	{
		boolean result = false;

			BufferedInputStream bin = null;
			BufferedOutputStream bout = null;
			try
			{
				/* 读文件 */
				bin = new BufferedInputStream(new FileInputStream(sourceFile));
				/* 写文件 */
				bout = new BufferedOutputStream(new FileOutputStream(targetFile));

				byte[] line = new byte[1024];
				while (bin.read(line) != -1)
				{
					bout.write(line);
				}
				result = true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (bin != null)
					bin.close();
				if (bout != null)
					bout.close();
			}

		return result;
	}

	
	/**
	 * 写文件操作
	 * 
	 * @param in InputStream 文件流
	 * @param ext String 写成的文件名称
	 * @param boolean true 写文件成功，返回写入的文件名；否则，返回null
	 */
	public static boolean copyFile(InputStream in,String targetFile)
	{
			
		BufferedOutputStream bout=null;
		boolean result = false;
		try
		{
			File file=new File(targetFile);
			bout=new BufferedOutputStream(new FileOutputStream(file));
			
			byte[] line=new byte[1024];
			while(in.read(line)!=-1)
			{
				bout.write(line);
			}
			result = true;
		}
		catch(Exception ioe)
		{
			log.printStackTrace(ioe);
			result = false;
		}
		finally
		{
			if(bout!=null) 
				try{
					in.close();
					bout.close();
				}catch(IOException ioe){
					result = false;
					log.printStackTrace(ioe);
				}
		}
		
		return result;
	}
	
	/**
	 * 两个浮点数相除,小数点后保留4位
	 * 
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static Double doubleDivide(Double num1, Double num2)
	{
		if (num1 == null || num2 == null)
			return null;
		BigDecimal number1 = new BigDecimal(String.valueOf(num1.doubleValue()));
		BigDecimal number2 = new BigDecimal(String.valueOf(num2.doubleValue()));
		return new Double(number1.divide(number2,4,BigDecimal.ROUND_HALF_UP).doubleValue());

	}
	/**
	 *<p>描述:文件加密</p>
	 *<p>参数:srcPath 源文件路径;tarPath加密后得文件路径</p>
	 *<p>日期：2007-12-5</p>
	 *<p>作者：曹发根</p>
	 */
	public static void encryptFile(String srcPath,String tarPath){
		File file=new File(srcPath);//原文件
		if(!file.exists()||tarPath==null||tarPath.equals(""))return;
		File tagetFile=new File(tarPath);//加密后文件
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(file);
			FileOutputStream outstream=new FileOutputStream(tagetFile);
			byte b[]=new byte[1024];
			int i=0;
			outstream.write(b);
			while((i=inputStream.read(b,0,1024))>0){
				
				outstream.write(b,0,i);
				
			}
			outstream.close();
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 *<p>描述:文件解密</p>
	 *<p>参数:srcPath 源文件路径;tarPath解密后得文件路径</p>
	 *<p>日期：2007-12-5</p>
	 *<p>作者：曹发根</p>
	 */
	public static void decryptFile(String srcPath,String tarPath){
		File file=new File(srcPath);//原文件
		File tagetFile=new File(tarPath);//加密后文件
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(file);
			FileOutputStream outstream=new FileOutputStream(tagetFile);

			byte c[]=new byte[1024];
			int h=0;
			int j=0;
			while((h=inputStream.read(c,0,1024))>0){
				if(j>0)outstream.write(c,0,h);
				j++;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * title:该方法用于一个不会重复的实体名称 author:chenbing date:2008-2-23
	 * 
	 * @return 实体文件名
	 */
	public static String getObjectName() {
		String result = "user_"
				+ String.valueOf(Calendar.getInstance().getTimeInMillis())
				+ new Random().nextInt();
		return result;
	}

	/**
	 * 测试方法
	 */
	public static void main(String[] args){
		//	byte arr[]="<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes();
		// System.out.println(new String(arr,0,8));
		// System.out.println(new String(arr,9,29));
		//decryptFile("d:\\GF1200_081_1_1.fit","d:\\GF1200_081_1_1.xls");
		//源码打包路径:(包含java源代码)
		String srcPath ="D:\\rpt_net";
		
		//class文件打包路径(经tomcat编译后的目录)
		String classPath="";
		
		//请不要把两个项目拷贝到一个目录下打包
		//将class文件 或源码打包
		/**
		 * 参数
		 * 0:classpath
		 * 1:srcPath
		 */
		LoadDataZiporDeleteFile(classPath,srcPath);
	}
	
	/**
	 * 删除指定项目下所有的cvs文件夹
	 * @param directoryName
	 */
	public static void deleteSpecifyDirectory(String directoryName,File file){
		if(file!=null){
			File[] dirs = file.listFiles();
			if(dirs!=null && dirs.length>0){
				for(File dir : dirs){
					if(dir.isDirectory()){
						//是否是cvs文件夹，是就删除
						if(dir.getName().equalsIgnoreCase(directoryName)){
							deleteDirectory(dir.getPath());
						}else{
							File[] subdirs = dir.listFiles();
							if(subdirs!=null && subdirs.length>0){
								for(File subdir : subdirs){
									if(subdir.getName().equalsIgnoreCase(directoryName)){
										deleteDirectory(subdir.getPath());
									}else{
										if(subdir.listFiles()!=null && subdir.listFiles().length>0)
											deleteSpecifyDirectory(directoryName,subdir);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 将指定文件打包成zip格式
	 * @param inputFileName
	 */
	public static void LoadDataZiporDeleteFile(String classPath,String srcPath){
		//class文件打包
		if(classPath!=null && !classPath.equals("")){
			try {
				boolean res = deleteFiles(classPath);
				if(res)
					zip(classPath,classPath+"_class.zip");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(classPath+".zip打包失败!");
			}
		}
		//源码打包
		if(srcPath!=null && !srcPath.equals("")){
			try {
				boolean res = deleteSrcFile(srcPath);
				if(res)
					zip(srcPath,srcPath+"_src.zip");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("src:"+srcPath+".zip打包失败!");
			}
		}
	}
	
	/**
	 * 删除部分项目源文件
	 * @param fileName
	 * @return
	 */
	public static boolean deleteSrcFile(String fileName){
		String message = "src:";
		boolean res= false;
		try {
			File file = new File(fileName);
			if(!file.exists()){System.out.println(fileName+"文件不存在!");}
			File[] dirs = file.listFiles();
			for(File dir : dirs){
				//如果是文件
				if(dir.isFile()){
					dir.delete();
					System.out.println(message+dir.getName()+"删除成功!");
				}
				//如果是目录
				if(dir.isDirectory()){
					if(dir.getName().equalsIgnoreCase("cvs")
							|| dir.getName().equalsIgnoreCase(".settings")){
						String msg = deleteDirectory(dir.getPath())?message+dir.getName()+"删除成功!":message+dir.getName()+"删除失败!";
						System.out.println(msg);
					}else if(dir.getName().equalsIgnoreCase("WebRoot")){
						deleteFiles(dir.getPath());
					}else if(dir.getName().equalsIgnoreCase("src")){
						File[] files = dir.listFiles();
						for(File f : files){
							if(f.isFile()){
								System.out.println(message+f.getName()+"删除成功!");
								f.delete();
							}
						}
					}
				}
			}
			deleteSpecifyDirectory("cvs",file);
			res=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}	
	
	/**
	 * 删除给定目录的指定文件
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFiles(String fileName){
		boolean res=false;
		File file=new File(fileName);
		String dels[]=new String[]{"top.jsp","web.xml","report_config.xml","reportConfig.xml","temp","reportFiles","templateFiles","lib",".tld",".properties"};
		if(file.exists()){
			File[] dirs = file.listFiles();
			for(File dir : dirs){
				if(dir.isFile()){
					if(!dir.getName().equalsIgnoreCase(dels[0])){
						dir.delete();
					}
					System.out.println(dir.getName()+"删除成功!");
				}
				if(dir.isDirectory()){
					if(dir.getName().equalsIgnoreCase("reportFiles")){
						String msg = deleteDirectory(dir.getPath())?dir.getName()+"删除成功!":dir.getName()+"删除失败!";
						System.out.println(msg);
					}else if(dir.getName().equalsIgnoreCase("templateFiles")){
						String msg = deleteDirectory(dir.getPath())?dir.getName()+"删除成功!":dir.getName()+"删除失败!";
						System.out.println(msg);
					}else if(dir.getName().equalsIgnoreCase("WEB-INF")){
						File[] subdirs = dir.listFiles();
						for(File subdir : subdirs){
							if(subdir.isFile()){
								if(subdir.getName().indexOf(".tld")!=-1 ||
										subdir.getName().indexOf(".properties")!=-1
										||subdir.getName().equalsIgnoreCase("web.xml") 
										||subdir.getName().equalsIgnoreCase("report_config.xml")
										||subdir.getName().equalsIgnoreCase("reportConfig.xml")){
									subdir.delete();
									System.out.println(subdir.getName()+"删除成功!");
								}
							}
							if(subdir.isDirectory()){
								if(subdir.getName().equalsIgnoreCase("lib")){
									String msg = deleteDirectory(subdir.getPath())?subdir.getName()+"删除成功!":subdir.getName()+"删除失败!";
									System.out.println(msg);
								}
								if(subdir.getName().equalsIgnoreCase("classes")){
									File[] lastSubDirs = subdir.listFiles();
									for(File lastSubDir : lastSubDirs){
										if(lastSubDir.isFile()){
											lastSubDir.delete();
											System.out.println(lastSubDir.getName()+"删除成功!");
										}
									}
								}
							}
						}
					}
				}
			}
			res = true ;
		}else{
			System.out.println(fileName+"文件不存在!");
		}
		return res;
	}
	/**
	 * 删除目录下的所有文件
	 * @param sPath
	 * @return
	 */
	public static boolean deleteDirectory(String sPath) {  
		boolean flag=false;
	     //如果sPath不以文件分隔符结尾，自动添加文件分隔符   
	    if (!sPath.endsWith(File.separator)) {   
	        sPath = sPath + File.separator;   
	    }  
	    File dirFile = new File(sPath);   
	    //如果dir对应的文件不存在，或者不是一个目录，则退出   
	    if (!dirFile.exists() || !dirFile.isDirectory()) {   
	        return false;   
	    }   
	    flag = true;   
	    //删除文件夹下的所有文件(包括子目录)   
	    File[] files = dirFile.listFiles();   
	    for (int i = 0; i < files.length; i++) {   
	        //删除子文件   
	        if (files[i].isFile()) {   
	            File sFile=new File(files[i].getAbsolutePath());
	            if(sFile.exists())
	            	sFile.delete();
	        } //删除子目录   
	        else {   
	            flag = deleteDirectory(files[i].getAbsolutePath());   
	            if (!flag) break;   
	        }   
	    }   
	    if (!flag) return false;   
	    //删除当前目录   
	    if (dirFile.delete()) {   
	        return true;   
	    } else {   
	        return false;   
	    }   
	}  

	public static void zip(String inputFileName,String zipFileName) throws Exception {
        File inputFile =  new File(inputFileName);
        if(inputFile.exists()){
	        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
	        zip(out, inputFile, "");
	        System.out.println(zipFileName+"打包成功!");
	        out.close();
        }else
        	System.out.println(inputFileName+"不存在!");
    }
	/**
	 * 将指定目录的所有文件进行打包
	 * @param out
	 * @param f
	 * @param base
	 * @throws Exception
	 */
    private static void zip(ZipOutputStream out, File f, String base) throws Exception {
        if (f.isDirectory()) {
           File[] fl = f.listFiles();
           out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));
           base = base.length() == 0 ? "" : base + "/";
           for (int i = 0; i < fl.length; i++) {
           zip(out, fl[i], base + fl[i].getName());
         }
        }else {
           out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
           FileInputStream in = new FileInputStream(f);
           int b;
           System.out.println(base);
           while ( (b = in.read()) != -1) {
            out.write(b);
         }
         in.close();
       }
    }
	
}

