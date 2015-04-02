
package com.fitech.export.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.cbrc.smis.adapter.StrutsExportReportDataDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.DateUtil;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.export.service.ExportQDReportDelegate;
import com.fitech.gznx.form.AFReportForm;

/**
 * 导出xml报表数据
 */
@SuppressWarnings({"rawtypes","unused"})
public class CreateQDXMLReportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private ServletContext context = null;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		context = config.getServletContext();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {        	
        		
        	//当前用户
        	Operator operator = (Operator) request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) ;
        	String srcFileName=Config.TEMP_DIR+File.separator+operator.getOperatorId().intValue() + DateUtil.getToday("yyyyMMddHHmmss");
    		Map<String, String> reportPathMap = new HashMap<String, String>() ;
    		
    		File src = new File(srcFileName) ;
    		if(src.exists()){
    			src.delete();
    		}
    		src.mkdirs() ;
    		
    		//得到币种、口径、单位等基础信息
    		Map baseMap = ExportQDReportDelegate.getBaseInfo() ;
    		
    		String repInIdString = request.getParameter("repInIds") != null ? request.getParameter("repInIds") : "";
    		
    		String dataragId = request.getParameter("dataRagId") != null ? request.getParameter("dataRagId") : "";
    		String tabulator = request.getParameter("Tabulator") != null ? request.getParameter("Tabulator") : "";
    		String auditor = request.getParameter("Auditor") != null ? request.getParameter("Auditor") : "";
    		String principal = request.getParameter("Principal") != null ? request.getParameter("Principal") : "";
    		String unit = request.getParameter("unit") != null ? request.getParameter("unit") : "";
   			String repInId = null;
    		String orgId = null;
    		String orgReportPath = null;
    		File file = null ;
    		ReportInForm reportInForm=null;
    		//如果为空，表示导出全部，否则导出选中
    		if(repInIdString==null||repInIdString.equals("")){
    	
    		}else{
    			String[] repInIds = repInIdString.split(",") ;
    			if(repInIds!=null){
    				for(int i=0;i<repInIds.length;i++){
    	       			
    					repInId = repInIds[i];
                		orgId =  ((AFReportForm)ExportQDReportDelegate.getReportIn(repInId)).getOrgId();
                		
                		//当前机构报文存放路径
                		orgReportPath = srcFileName + File.separator + orgId;
                		file = new File(orgReportPath) ;
                		if(!reportPathMap.containsKey(orgReportPath)){
                			reportPathMap.put(orgId, orgReportPath) ;
                			file.mkdirs() ;
                		}
                		
//                		reportInForm = StrutsReportInDelegate.getReportIn(new Integer(repInId));
//                		
//                		AfTemplate aftemplate = AFTemplateDelegate.getTemplate(reportInForm.getChildRepId(),reportInForm.getVersionId());
                		
                		int reportStyle = ExportQDReportDelegate.getReportStyle(repInId);
                		//清单类型模板
                		if(com.fitech.gznx.common.Config.REPORT_QD.equals(String.valueOf(reportStyle))){
                			createQDXMLFile(orgReportPath,baseMap, repInId,dataragId ,URLDecoder.decode(tabulator,"UTF-8") ,URLDecoder.decode(auditor,"UTF-8"),URLDecoder.decode(principal,"UTF-8"),URLDecoder.decode(unit,"UTF-8")) ;
                		}
    				}
    			}
    		}
    		
    		//生成zip压缩文件并提供下载
    		downzipFile(srcFileName,response) ;
    		
			//删除文件
    		new File(srcFileName+".zip").delete() ;
			casadeDelete(src);
        	
        }catch(Exception e){
        	e.printStackTrace();
        	ErrorOutPut(response) ;
        }

	}
	

	private void createQDXMLFile(String outPath,Map baseMap,String repId ,String dataragId ,String Tabulator ,String Auditor ,String Principal ,String unit){
		AFReportForm reportInForm = null;
		Map currMap = (Map) baseMap.get("MCurr") ;
		Map curUnitMap = (Map) baseMap.get("MCurUnit") ;
		Map rangeMap = (Map) baseMap.get("Range") ;
		try {
			reportInForm = ExportQDReportDelegate.getReportIn(repId);
			if(reportInForm != null)
			outPath = outPath+File.separator+reportInForm.getTemplateId().replaceFirst("QD", "") 
					+ "_" + reportInForm.getVersionId() + "_" 
					+ dataragId + "_"  + reportInForm.getCurId()+".xml" ;
		
			Document document = DocumentHelper.createDocument() ;
			Element root = document.addElement("CbrcReports", "http://www.cbrc.gov.cn/report/1104") ;

			Element report = root.addElement("Report")
										.addAttribute("RepCode",reportInForm.getTemplateId())
										.addAttribute("VersionId", reportInForm.getVersionId().substring(0, 3))
										.addAttribute("ReportDate", getMonthLastDayStr(Integer.parseInt(reportInForm.getYear()), Integer.parseInt(reportInForm.getTerm())))
										.addAttribute("Range",rangeMap.get(Integer.parseInt(dataragId)).toString())
										.addAttribute("Currency",currMap.get(Integer.parseInt(reportInForm.getCurId())).toString())
										.addAttribute("Unit", unit);
			
			Element info = report.addElement("Info") ;
			info.addElement("Tabulator").addText(Tabulator) ;
			info.addElement("Auditor").addText(Auditor) ;
			info.addElement("Principal") .addText(Principal) ;
			
			Element data = null ;
			Element row = null;
			Element cell = null;
//			int partCount = ExportQDReportDelegate.getdataPartCount(reportInForm.getTemplateId(),reportInForm.getVersionId()) ;
			//不分部分的表
//			if(partCount==0){	
			if(true){
				data = report.addElement("Data").addAttribute("part", "0") ;
				List <Object[]>cellList = ExportQDReportDelegate.getCellList(reportInForm.getTemplateId(), reportInForm.getRepId());
				
				if(cellList!=null&&cellList.size()>0){
					Object[] objects = null ;
//					int currentRow = -1 ;//初始化-1
					for(int n=1;n<cellList.size();n++){
						objects = (Object[]) cellList.get(n) ;
						row = data.addElement("Row").addAttribute("id",objects[1].toString()) ;
						for (int i = 2; i < objects.length-2; i++) {
							Object object = objects[i];
							cell = row.addElement("Cell").addAttribute("id", numbertoString(i-1)) ;
							cell.addText(null != objects[i] ? objects[i].toString() : "") ;
						}
//						currentRow = Integer.valueOf(objects[1].toString()) ;
					}
				}
			}else{
//				
//				List cellList = ExportQDReportDelegate.getCellListWithPart(reportInForm.getTemplateId(),reportInForm.getVersionId()) ;
//				if(cellList!=null&&cellList.size()>0){
//					Object[] objects = null ;
//					int currentRow = -1 ;//初始化-1
//					int currentPart = -1 ;
//					for(int n=0;n<cellList.size();n++){
//						
//						objects = (Object[]) cellList.get(n) ;
//						if(Integer.valueOf(objects[0].toString())!=currentPart){
//							data = report.addElement("Data").addAttribute("part", objects[0].toString()) ;
//						}
//						if(Integer.valueOf(objects[1].toString())!=currentRow){
//							row = data.addElement("Row").addAttribute("id",objects[1].toString()) ;
//						}
//						
//						cell = row.addElement("Cell").addAttribute("id", objects[2].toString()) ;
//						cell.addText(null != objects[3] ? objects[3].toString() : "") ;
//						
//						currentPart = Integer.valueOf(objects[0].toString()) ;
//						currentRow = Integer.valueOf(objects[1].toString()) ;
//					}
//				}
			}
			//输出: FileWriter出现乱码,此处使用FileOutputStream
			FileOutputStream fos = new FileOutputStream(new File(outPath)) ;
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			format.setNewLineAfterDeclaration(false);
			format.setTrimText(true) ;
	        XMLWriter writer = new XMLWriter(fos, format);
	        writer.write(document);
	        writer.close() ;
	        
		}catch(Exception e){
			e.printStackTrace() ;
		}
	}
	
	private String getMonthLastDayStr(int year, int month) {
        int lastDay = 31;
        if (month == 4 || month == 6 || month == 9 || month == 11)
            lastDay = 30;
        if (month == 2) {
            if (new GregorianCalendar().isLeapYear(year))
                lastDay = 29;
            else
                lastDay = 28;
        }
        return getDateStr(year, month, lastDay);
    }
	
	public static final int[] MONTH_ARRAY = { 0, Calendar.JANUARY,
        Calendar.FEBRUARY, Calendar.MARCH, Calendar.APRIL, Calendar.MAY,
        Calendar.JUNE, Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER,
        Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER };
	
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	private  String getDateStr(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, MONTH_ARRAY[month]);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return DATE_FORMAT.format(calendar.getTime());
    }
	
    private void casadeDelete(File src) {
		if(src.exists()){
			File[] files = src.listFiles();
			if(files!=null){
				for(int i=0;i<files.length;i++){
					if(files[i].isDirectory()){
						casadeDelete(files[i]) ;
						files[i].delete() ;
					}else{
						files[i].delete() ;
					}
				}
			}
			src.delete() ;
		}		
	}

    private void downzipFile(String srcFileName,HttpServletResponse response){
    	
    	String zipFileName = "REPORTS.zip";
    	try{
	    	System.out.println("压缩文件开始");
			Zip zip = new Zip();
			zip.setEncoding("GBK");
			zip.setBasedir(new File(srcFileName));// 设置压缩文件地址
			zip.setExcludes("*.zip"); // 不包括哪些文件或文件夹
			zip.setDestFile(new File(srcFileName+".zip"));// 压缩的目录
			Project p = new Project();
			zip.setProject(p);
			zip.execute();
			System.out.println("压缩文件结束");
			
			response.reset();
			response.setContentType("application/x-zip-compressed;name=\"" + zipFileName + "\""); 
			response.addHeader("Content-Disposition",
					"attachment; filename=\""
								+ FitechUtil.toUtf8String(zipFileName) + "\"");
			response.setHeader("Accept-ranges", "bytes");
								
			FileInputStream inStream = new FileInputStream(srcFileName + ".zip");
				
			int len;
			byte[] buffer = new byte[100];
				
			while((len = inStream.read(buffer)) > 0){
				response.getOutputStream().write(buffer,0,len);
			}
			
			inStream.close();
		
    	}catch(Exception e){
    		
			e.printStackTrace() ;
		}
		
		
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request,response);
	}

	/**
     * 错误输出
     * @param response
     */
	private void ErrorOutPut(HttpServletResponse response)
    {
        response.reset();
        response.setContentType("text/html;charset=GB2312");
        PrintWriter out = null;
        try 
        {
            out = response.getWriter();
        } 
        catch (IOException e1) 
        {
            e1.printStackTrace();
        }
        out.println("<font color=\"blue\">没有需要导出的报送数据文件!</font>");
        out.close();     
    }
	
 static String numbertoString(int n)  
	 
     {             
         String s = "" ;     // result  
         int r = 0 ;         // remainder  

         while(n != 0)  
         {  
             r = n % 26 ;  
             char ch = ' ' ;  
             if(r == 0)  
                 ch = 'Z' ;  
             else
                ch = (char)(r - 1 + 'A') ;  
                s = String.valueOf(ch) + s;
             if(s.equals("Z"))  
                 n = n / 26 - 1 ;  
             else 
                 n /= 26 ;  
         }  
         return s ;  
     }
 
 
 

}