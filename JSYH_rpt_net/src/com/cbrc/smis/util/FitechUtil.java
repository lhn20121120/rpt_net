
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
 * ���÷����� 
 * 
 * @author rds
 * @date 2005-11-13
 */
public class FitechUtil {	
	/**
	 * ��־
	 */
	private static FitechException log = new FitechException(FitechUtil.class);
	
	/**
	 * �Ƿ��ĸ�����׺��
	 */
	private static final String[] UNVALIDEXT = { "exe", "bat", "sh" };

	/**
	 * ����
	 */
	private static final String[][] WEEK = { 
		{ "1", "����һ" }, 
		{ "2", "���ڶ�" },
		{ "3", "������" }, 
		{ "4", "������" }, 
		{ "5", "������" }, 
		{ "6", "������" },
		{ "7", "������" } 
	};

	/**
	 * �����㷨��
	 */
	private static final String ALGORITH = "MD5";

	/**
	 * ���ַ�������������
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
	 * �����ڵĸ�ʽ���ַ���
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
	 * ���ʹ��MD5���ܵ��ַ�����Base64��ֵ
	 * 
	 * @param value String Ҫ���ܵ��ַ���
	 * @return String ���ܺ��Base64��
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
	 * ��׼������ʽ
	 */
	public static final String NORMALDATE = "yyyy-MM-dd";

	/**
	 * �й���׼��������ʽ
	 */
	public static final String CHINESEDATE = "yyyy��MM��dd��";

	/**
	 * ��ȡ���������
	 * 
	 * @param format ������ʽ
	 * @return String
	 */
	public static String getToday(String format) {
		if (format == null)
			return "";
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(new Date());
	}

	/**
	 * ��ȡ�������ܼ�
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
	 * getParameter����<br>
	 * �Ի�ȡ���ύֵ��GB2312����ת��<br>
	 * 
	 * @param request HttpServletRequest
	 * @param paramName String ������
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
	 * �ж��û��ϴ��ĸ����Ƿ�����ȷ
	 * 
	 * @author rds
	 * 
	 * @param fileName String �ϴ��������ļ���
	 * @return boolean �ϴ��ĸ������Ϸ�������false�����򣬷���true
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
	 * �������ִ���ʽ����ʱ�䴮
	 * 
	 * @param date String ʱ���ַ���
	 * @return Date ת���ɹ�����Date �����򷵻�null
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
	 * ��request��session ��Χ�ڵ�����ɾ��
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
	 * д�ļ�����
	 * 
	 * @param in InputStream �ļ���
	 * @param ext String �ļ���׺��
	 * @param boolean true д�ļ��ɹ�������д����ļ��������򣬷���null
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
	 * д�ļ�����
	 * 
	 * @param content String �ļ�����
	 * @param ext String �ļ���׺��
	 * @param boolean true д�ļ��ɹ�������д����ļ��������򣬷���null
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
	 * ���ļ�����
	 * 
	 * @param fileName String �ļ���
	 * @reture InputStream ��ȡ�ļ�ʧ�ܣ�����null
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
	 * ɾ���ļ�����
	 * 
	 * @param fileName String Ҫɾ�����ļ�
	 * @return boolean ɾ���ɹ�������true;���򣬷���false
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
	 * ���ݹ�ϵ���ʽ��ȡĿ�굥Ԫ������
	 * 
	 * @param expression String ��ϵ���ʽ
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
	 * ��ȡʵ�����ݱ����XML���ݣ�������ת����PDFʽ��XML�ļ�
	 * 
	 * @author rds
	 * @serialData 2005-12-18 12:07
	 * 
	 * @param repInId ʵ�����ݱ���ID
	 * @return InputStream ����XML���ݣ�����Ϊ���ǣ�����null
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
	 * �����ֽ���дPDF�ļ�
	 * 
	 * @author rds
	 * @serialField 2005-12-18 14:11
	 * 
	 * @param in InputStream  PDF�ֽ���
	 * @param pdfFileName String д����ļ���
	 * @return boolean д��ɹ�������true�����򣬷���false
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
// * ������ת����Long
// * @author �ܷ���
// * @param date
// * @return
// */
//    public static long date2long(Date date){
//    	   
//  	  return Long.parseLong(toSimpleDateFormat(date,"yyyyMMddHHmmss"));
//    }
//    /**
//     * ������ת��Ϊ�ַ���
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
     * ת��ʱ���ʽ
     * @throws ParseException
     * @author �ܷ���
     */
    public static String Formatter(Date date) throws ParseException {
		//Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(date);
	    return dateString;
	}
    /**
     * �õ���ǰʱ��
     * @throws ParseException
     * @author �ܷ���
     */
    public static Date getNowTime() throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat  formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(calendar.getTime());
		return formatter.parse(dateString);
	}
    
    /**
     * ���ļ���ת��Ϊ������
     * @author �ܷ���
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
	 * ��ȡ������ֵ
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
	 * XDP�ļ���ͷ����
	 */
	private final static String XDP_HEADER="<?xfa generator=\"XFA2_0\" APIVersion=\"2.2.5030.0\"?>" + 
	"<xdp:xdp xmlns:xdp=\"http://ns.adobe.com/xdp/\">" + 
	"<xfa:datasets xmlns:xfa=\"http://www.xfa.org/schema/xfa-data/1.0/\"><xfa:data>";
	
	/**
	 * ���XDP�ļ���ͷ��Ϣ 
	 */
	public static String getXDPHeader(){
		return XDP_HEADER;
	}
	
	/**
	 * XDP�ļ���β����
	 */
	private static String XDP_TAILER="";
	/**
	 * ����XDP�ļ���β��Ϣ
	 * 
	 * @param modelName PDFģ����
	 * @return void
	 */
	public static void setXDPTailer(String modelName){
		XDP_TAILER="</xfa:data></xfa:datasets><pdf href=\"" + modelName + 
			"\" xmlns=\"http://ns.adobe.com/xdp/pdf/\"/></xdp:xdp>";
	}
	/**
	 * ��ȡXDP�ļ���β��Ϣ
	 */
	public static String getXDPTailer(){
		return XDP_TAILER;
	}
	

	
	/**
	 * ���ļ����еĺ���תΪUTF8����Ĵ�,�Ա�����ʱ����ȷ��ʾ�����ļ���.
	 * 
	 * @param s ԭ�ļ���
	 * @return String ���±������ļ���
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
	 * �����ļ�
	 * 
	 * @param sourceFilePath
	 *            Դ�ļ�·��
	 * @param targetFilePath
	 *            Ŀ���ļ�·��
	 * @return �Ƿ��Ƴɹ�
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
		/* ���Դ�ļ���Ŀ���ļ���ͬ������������Ĭ�ϲ����ɹ� */
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
	 * ����˵��:�÷������ڽ�һ��ԭ�ļ�����Ϊһ��Ŀ���ļ�
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
				/* ���ļ� */
				bin = new BufferedInputStream(new FileInputStream(sourceFile));
				/* д�ļ� */
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
	 * д�ļ�����
	 * 
	 * @param in InputStream �ļ���
	 * @param ext String д�ɵ��ļ�����
	 * @param boolean true д�ļ��ɹ�������д����ļ��������򣬷���null
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
	 * �������������,С�������4λ
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
	 *<p>����:�ļ�����</p>
	 *<p>����:srcPath Դ�ļ�·��;tarPath���ܺ���ļ�·��</p>
	 *<p>���ڣ�2007-12-5</p>
	 *<p>���ߣ��ܷ���</p>
	 */
	public static void encryptFile(String srcPath,String tarPath){
		File file=new File(srcPath);//ԭ�ļ�
		if(!file.exists()||tarPath==null||tarPath.equals(""))return;
		File tagetFile=new File(tarPath);//���ܺ��ļ�
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
	 *<p>����:�ļ�����</p>
	 *<p>����:srcPath Դ�ļ�·��;tarPath���ܺ���ļ�·��</p>
	 *<p>���ڣ�2007-12-5</p>
	 *<p>���ߣ��ܷ���</p>
	 */
	public static void decryptFile(String srcPath,String tarPath){
		File file=new File(srcPath);//ԭ�ļ�
		File tagetFile=new File(tarPath);//���ܺ��ļ�
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
	 * title:�÷�������һ�������ظ���ʵ������ author:chenbing date:2008-2-23
	 * 
	 * @return ʵ���ļ���
	 */
	public static String getObjectName() {
		String result = "user_"
				+ String.valueOf(Calendar.getInstance().getTimeInMillis())
				+ new Random().nextInt();
		return result;
	}

	/**
	 * ���Է���
	 */
	public static void main(String[] args){
		//	byte arr[]="<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes();
		// System.out.println(new String(arr,0,8));
		// System.out.println(new String(arr,9,29));
		//decryptFile("d:\\GF1200_081_1_1.fit","d:\\GF1200_081_1_1.xls");
		//Դ����·��:(����javaԴ����)
		String srcPath ="D:\\rpt_net";
		
		//class�ļ����·��(��tomcat������Ŀ¼)
		String classPath="";
		
		//�벻Ҫ��������Ŀ������һ��Ŀ¼�´��
		//��class�ļ� ��Դ����
		/**
		 * ����
		 * 0:classpath
		 * 1:srcPath
		 */
		LoadDataZiporDeleteFile(classPath,srcPath);
	}
	
	/**
	 * ɾ��ָ����Ŀ�����е�cvs�ļ���
	 * @param directoryName
	 */
	public static void deleteSpecifyDirectory(String directoryName,File file){
		if(file!=null){
			File[] dirs = file.listFiles();
			if(dirs!=null && dirs.length>0){
				for(File dir : dirs){
					if(dir.isDirectory()){
						//�Ƿ���cvs�ļ��У��Ǿ�ɾ��
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
	 * ��ָ���ļ������zip��ʽ
	 * @param inputFileName
	 */
	public static void LoadDataZiporDeleteFile(String classPath,String srcPath){
		//class�ļ����
		if(classPath!=null && !classPath.equals("")){
			try {
				boolean res = deleteFiles(classPath);
				if(res)
					zip(classPath,classPath+"_class.zip");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(classPath+".zip���ʧ��!");
			}
		}
		//Դ����
		if(srcPath!=null && !srcPath.equals("")){
			try {
				boolean res = deleteSrcFile(srcPath);
				if(res)
					zip(srcPath,srcPath+"_src.zip");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("src:"+srcPath+".zip���ʧ��!");
			}
		}
	}
	
	/**
	 * ɾ��������ĿԴ�ļ�
	 * @param fileName
	 * @return
	 */
	public static boolean deleteSrcFile(String fileName){
		String message = "src:";
		boolean res= false;
		try {
			File file = new File(fileName);
			if(!file.exists()){System.out.println(fileName+"�ļ�������!");}
			File[] dirs = file.listFiles();
			for(File dir : dirs){
				//������ļ�
				if(dir.isFile()){
					dir.delete();
					System.out.println(message+dir.getName()+"ɾ���ɹ�!");
				}
				//�����Ŀ¼
				if(dir.isDirectory()){
					if(dir.getName().equalsIgnoreCase("cvs")
							|| dir.getName().equalsIgnoreCase(".settings")){
						String msg = deleteDirectory(dir.getPath())?message+dir.getName()+"ɾ���ɹ�!":message+dir.getName()+"ɾ��ʧ��!";
						System.out.println(msg);
					}else if(dir.getName().equalsIgnoreCase("WebRoot")){
						deleteFiles(dir.getPath());
					}else if(dir.getName().equalsIgnoreCase("src")){
						File[] files = dir.listFiles();
						for(File f : files){
							if(f.isFile()){
								System.out.println(message+f.getName()+"ɾ���ɹ�!");
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
	 * ɾ������Ŀ¼��ָ���ļ�
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
					System.out.println(dir.getName()+"ɾ���ɹ�!");
				}
				if(dir.isDirectory()){
					if(dir.getName().equalsIgnoreCase("reportFiles")){
						String msg = deleteDirectory(dir.getPath())?dir.getName()+"ɾ���ɹ�!":dir.getName()+"ɾ��ʧ��!";
						System.out.println(msg);
					}else if(dir.getName().equalsIgnoreCase("templateFiles")){
						String msg = deleteDirectory(dir.getPath())?dir.getName()+"ɾ���ɹ�!":dir.getName()+"ɾ��ʧ��!";
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
									System.out.println(subdir.getName()+"ɾ���ɹ�!");
								}
							}
							if(subdir.isDirectory()){
								if(subdir.getName().equalsIgnoreCase("lib")){
									String msg = deleteDirectory(subdir.getPath())?subdir.getName()+"ɾ���ɹ�!":subdir.getName()+"ɾ��ʧ��!";
									System.out.println(msg);
								}
								if(subdir.getName().equalsIgnoreCase("classes")){
									File[] lastSubDirs = subdir.listFiles();
									for(File lastSubDir : lastSubDirs){
										if(lastSubDir.isFile()){
											lastSubDir.delete();
											System.out.println(lastSubDir.getName()+"ɾ���ɹ�!");
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
			System.out.println(fileName+"�ļ�������!");
		}
		return res;
	}
	/**
	 * ɾ��Ŀ¼�µ������ļ�
	 * @param sPath
	 * @return
	 */
	public static boolean deleteDirectory(String sPath) {  
		boolean flag=false;
	     //���sPath�����ļ��ָ�����β���Զ�����ļ��ָ���   
	    if (!sPath.endsWith(File.separator)) {   
	        sPath = sPath + File.separator;   
	    }  
	    File dirFile = new File(sPath);   
	    //���dir��Ӧ���ļ������ڣ����߲���һ��Ŀ¼�����˳�   
	    if (!dirFile.exists() || !dirFile.isDirectory()) {   
	        return false;   
	    }   
	    flag = true;   
	    //ɾ���ļ����µ������ļ�(������Ŀ¼)   
	    File[] files = dirFile.listFiles();   
	    for (int i = 0; i < files.length; i++) {   
	        //ɾ�����ļ�   
	        if (files[i].isFile()) {   
	            File sFile=new File(files[i].getAbsolutePath());
	            if(sFile.exists())
	            	sFile.delete();
	        } //ɾ����Ŀ¼   
	        else {   
	            flag = deleteDirectory(files[i].getAbsolutePath());   
	            if (!flag) break;   
	        }   
	    }   
	    if (!flag) return false;   
	    //ɾ����ǰĿ¼   
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
	        System.out.println(zipFileName+"����ɹ�!");
	        out.close();
        }else
        	System.out.println(inputFileName+"������!");
    }
	/**
	 * ��ָ��Ŀ¼�������ļ����д��
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

