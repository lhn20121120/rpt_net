
package com.cbrc.smis.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.adapter.StrutsReportInInfoDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.DateUtil;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.form.ReportInInfoForm;
import com.cbrc.smis.jdbc.FitechConnection;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechUtil;
import com.cbrc.smis.util.StakeholdersUtil;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFTemplateDelegate;

/**
 * ����xml��������
 */
@SuppressWarnings({"rawtypes","unused"})
public class CreateSubOrgXMLReportServlet extends HttpServlet {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
     public static final int[] MONTH_ARRAY = { 0, Calendar.JANUARY,
       Calendar.FEBRUARY, Calendar.MARCH, Calendar.APRIL, Calendar.MAY,
       Calendar.JUNE, Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER,
       Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER };

    private static final long serialVersionUID = 1L;
    
    private ServletContext context = null;
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        context = config.getServletContext();
    }
    
    
        public static String getMonthLastDayStr(int year, int month) {
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
        
        
        
        /**
         * ȡ�����ڵ�yyyy-MM-dd��ʽ
         *
         * @param year
         * @param month
         * @param day
         * @return
         */
        public static String getDateStr(int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, MONTH_ARRAY[month]);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            return DATE_FORMAT.format(calendar.getTime());
        }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {           
                
            //��ǰ�û�
            Operator operator = (Operator) request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) ;
            String srcFileName=Config.TEMP_DIR+File.separator+operator.getOperatorId().intValue() + DateUtil.getToday("yyyyMMddHHmmss");
            Map<String, String> reportPathMap = new HashMap<String, String>() ;
            
            File src = new File(srcFileName) ;
            if(src.exists()){
                src.delete();
            }
            src.mkdirs() ;
            
            //�õ����֡��ھ�����λ�Ȼ�����Ϣ
            Map baseMap = StrutsExportReportDataDelegate.getBaseInfo() ;
            
            String repInIdString = request.getParameter("repInIds") != null ? request.getParameter("repInIds") : "";
            
            String repInId = null;
            String orgId = null;
            String orgReportPath = null;
            File file = null ;
            ReportInForm reportInForm=null;
            //���Ϊ�գ���ʾ����ȫ�������򵼳�ѡ��
            if(repInIdString==null||repInIdString.equals("")){
                
                ReportInInfoForm reportInInfoForm = new ReportInInfoForm();
                
                reportInInfoForm.setChildRepId(request.getParameter("childRepId") != null ? request.getParameter("childRepId") : null);
                reportInInfoForm.setRepName(request.getParameter("repName") != null ? request.getParameter("repName") : null);              
                reportInInfoForm.setYear(request.getParameter("year") != null ? new Integer(request.getParameter("year")) : null);
                reportInInfoForm.setTerm(request.getParameter("term") != null ? new Integer(request.getParameter("term")) : null);
                reportInInfoForm.setOrgId(request.getParameter("orgId") != null ? request.getParameter("orgId") : null);
                reportInInfoForm.setFrOrFzType(request.getParameter("frOrFzType") != null ? request.getParameter("frOrFzType") : null);
                reportInInfoForm.setRepFreqId(request.getParameter("repFreqId") != null ? new Integer(request.getParameter("repFreqId")) : null);
                reportInInfoForm.setCheckFlag(request.getParameter("checkFlag") != null ? new Short(request.getParameter("checkFlag")) : null);
                
                List list = StrutsReportInInfoDelegate.searchReportRecord(reportInInfoForm,operator);
                
                if(list!=null){
                    for(int r=0,max=list.size();r<max;r++){
                        reportInForm = (ReportInForm) list.get(r) ;
                        repInId = reportInForm.getRepInId().toString() ;
                        orgId = reportInForm.getOrgId() ;
                        
                        //��ǰ�������Ĵ��·��
                        orgReportPath = srcFileName + File.separator + orgId;
                        file = new File(orgReportPath) ;
                        if(!reportPathMap.containsKey(orgReportPath)){
                            reportPathMap.put(orgId, orgReportPath) ;
                            file.mkdirs() ;
                        }
                        
                        reportInForm = StrutsReportInDelegate.getReportIn(new Integer(repInId));
                        
                        AfTemplate aftemplate = AFTemplateDelegate.getTemplate(reportInForm.getChildRepId(),reportInForm.getVersionId());
                        
                        //�嵥����ģ��
                        if(aftemplate.getReportStyle() != null && 
                                com.fitech.gznx.common.Config.REPORT_QD.equals(String.valueOf(aftemplate.getReportStyle()))){
                            
                            createQDXMLFile(orgReportPath,baseMap, reportInForm) ;
                            
                        }else{//��Ե�����ģ��
                            
                            createXmlFile(orgReportPath,baseMap, reportInForm) ;
                        }
                    }
                }
            }else{
                String[] repInIds = repInIdString.split(",") ;
                if(repInIds!=null){
                    for(int i=0;i<repInIds.length;i++){
                        
                        repInId = repInIds[i].split(":")[0];
                        orgId = repInIds[i].split(":")[1];
                        
                        //��ǰ�������Ĵ��·��
                        orgReportPath = srcFileName + File.separator + orgId;
                        file = new File(orgReportPath) ;
                        if(!reportPathMap.containsKey(orgReportPath)){
                            reportPathMap.put(orgId, orgReportPath) ;
                            file.mkdirs() ;
                        }
                        
                        reportInForm = StrutsReportInDelegate.getReportIn(new Integer(repInId));
                        
                        AfTemplate aftemplate = AFTemplateDelegate.getTemplate(reportInForm.getChildRepId(),reportInForm.getVersionId());
                        
                        //�嵥����ģ��
                        if(aftemplate.getReportStyle() != null 
                                && com.fitech.gznx.common.Config.REPORT_QD.equals(String.valueOf(aftemplate.getReportStyle()))){
                            
                            createQDXMLFile(orgReportPath,baseMap, reportInForm) ;
                            
                        }else{//��Ե�����ģ��
                            
                            createXmlFile(orgReportPath,baseMap, reportInForm) ;
                        }
                    }
                }
            }
            
            //����zipѹ���ļ����ṩ����
            downzipFile(srcFileName,response) ;
            
            //ɾ���ļ�
            new File(srcFileName+".zip").delete() ;
            casadeDelete(src);
            
        }catch(Exception e){
            e.printStackTrace();
            ErrorOutPut(response) ;
        }

    }
    
    private void createQDXMLFile(String outPath, Map baseMap, ReportInForm reportInForm) {
        
        
        
    }

    private void createXmlFile(String outPath,Map baseMap,ReportInForm reportInForm){
        
        //add by ywz
        Map<String,String> versionMap = StrutsExportReportDataDelegate.getVersionMap();
        String newVersionId = versionMap.get(reportInForm.getChildRepId()+"_"+reportInForm.getVersionId());  
        
        outPath = outPath+File.separator+reportInForm.getChildRepId().replaceFirst("HB", "") 
                        + "_" + (null==newVersionId?reportInForm.getVersionId():newVersionId) + "_" 
                            + reportInForm.getDataRangeId() + "_"  + reportInForm.getCurId()+".xml" ;
        
        Map currMap = (Map) baseMap.get("MCurr") ;
        Map curUnitMap = (Map) baseMap.get("MCurUnit") ;
        Map rangeMap = (Map) baseMap.get("Range") ;
        
        
        Integer unit = StrutsExportReportDataDelegate.getUnit (reportInForm.getChildRepId(),reportInForm.getVersionId());
        try{
            Document document = DocumentHelper.createDocument() ;
            Element root = document.addElement("CbrcReports", "http://www.cbrc.gov.cn/report/1104") ;
            Element report = root.addElement("Report")
                                        .addAttribute("RepCode",reportInForm.getChildRepId())
                                        .addAttribute("VersionId", null==newVersionId?reportInForm.getVersionId().substring(0, 3):newVersionId)
                                        .addAttribute("ReportDate", getMonthLastDayStr(reportInForm.getYear(), reportInForm.getTerm()))
                                        .addAttribute("Range",rangeMap.get(reportInForm.getDataRangeId()).toString())
                                        .addAttribute("Currency",currMap.get(reportInForm.getCurId()).toString())
                                        .addAttribute("Unit",curUnitMap.get(unit).toString());
            
            Element info = report.addElement("Info") ;  
            String stakeholders[] = StakeholdersUtil.getStakeholders(reportInForm.getRepInId(), reportInForm.getChildRepId(), reportInForm.getVersionId());
            info.addElement("Tabulator").addText((reportInForm.getWriter()==null || "����ˣ�".equals(reportInForm.getWriter())) 
            		? stakeholders[0] : reportInForm.getWriter().replaceAll("�����", "").replaceAll("��", "").replaceAll(":", "")) ;
            info.addElement("Auditor").addText((reportInForm.getChecker()==null || "�����ˣ�".equals(reportInForm.getChecker())) 
            		? stakeholders[1] : reportInForm.getChecker().replaceAll("������", "").replaceAll("��", "").replaceAll(":", "")) ;
            info.addElement("Principal") .addText((reportInForm.getPrincipal()==null || "�����ˣ�".equals(reportInForm.getPrincipal()))
            		? stakeholders[2] : reportInForm.getPrincipal().replaceAll("������", "").replaceAll("��", "").replaceAll(":", "")) ;
            
            Element data = null ;
            Element row = null;
            Element cell = null;
            int partCount = StrutsExportReportDataDelegate.getdataPartCount(reportInForm.getChildRepId(),reportInForm.getVersionId()) ;
            //���ֲ��ֵı�
            if(partCount==0){               
                data = report.addElement("Data").addAttribute("part", "0") ;
                List cellList = StrutsExportReportDataDelegate.getCellList(reportInForm.getChildRepId(),reportInForm.getVersionId(), reportInForm.getRepInId());
                
                if(cellList!=null&&cellList.size()>0){
                    Object[] objects = null ;
                    int currentRow = -1 ;//��ʼ��-1
                    for(int n=0;n<cellList.size();n++){
                        objects = (Object[]) cellList.get(n) ;
                        if(Integer.valueOf(objects[0].toString())!=currentRow){
                            row = data.addElement("Row").addAttribute("id",objects[0].toString()) ;
                        }
                        cell = row.addElement("Cell").addAttribute("id", objects[1].toString()) ;
                        cell.addText(null != objects[2] ? objects[2].toString() : "") ;
                        currentRow = Integer.valueOf(objects[0].toString()) ;
                    }
                }
            }else{
                
                List cellList = StrutsExportReportDataDelegate.getCellListWithPart(reportInForm.getChildRepId(),reportInForm.getVersionId(), reportInForm.getRepInId()) ;
                if(cellList!=null&&cellList.size()>0){
                    Object[] objects = null ;
                    int currentRow = -1 ;//��ʼ��-1
                    int currentPart = -1 ;
                    for(int n=0;n<cellList.size();n++){
                        
                        objects = (Object[]) cellList.get(n) ;
                        if(Integer.valueOf(objects[0].toString())!=currentPart){
                            data = report.addElement("Data").addAttribute("part", objects[0].toString()) ;
                        }
                        if(Integer.valueOf(objects[1].toString())!=currentRow){
                            row = data.addElement("Row").addAttribute("id",objects[1].toString()) ;
                        }
                        
                        cell = row.addElement("Cell").addAttribute("id", objects[2].toString()) ;
                        cell.addText(null != objects[3] ? objects[3].toString() : "") ;
                        
                        currentPart = Integer.valueOf(objects[0].toString()) ;
                        currentRow = Integer.valueOf(objects[1].toString()) ;
                    }
                }                
            }
            //���: FileWriter��������,�˴�ʹ��FileOutputStream
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
            System.out.println("ѹ���ļ���ʼ");
            Zip zip = new Zip();
            zip.setEncoding("GBK");
            zip.setBasedir(new File(srcFileName));// ����ѹ���ļ���ַ
            zip.setExcludes("*.zip"); // ��������Щ�ļ����ļ���
            zip.setDestFile(new File(srcFileName+".zip"));// ѹ����Ŀ¼
            Project p = new Project();
            zip.setProject(p);
            zip.execute();
            System.out.println("ѹ���ļ�����");
            
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
     * �������
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
        out.println("<font color=\"blue\">û����Ҫ�����ı��������ļ�!</font>");
        out.close();     
    }
}