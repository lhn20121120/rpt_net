package com.cbrc.smis.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cbrc.smis.adapter.StrutsActuRep;
import com.cbrc.smis.adapter.StrutsExportReportDataDelegate;
import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate;
import com.cbrc.smis.adapter.StrutsMRepFreqDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.adapter.StrutsReportInInfoDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.DateUtil;
import com.cbrc.smis.common.DownLoadDataToZip;
import com.cbrc.smis.excel.CreateExcel;
import com.cbrc.smis.form.MActuRepForm;
import com.cbrc.smis.form.MCurrForm;
import com.cbrc.smis.form.MDataRgTypeForm;
import com.cbrc.smis.form.MRepFreqForm;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.form.ReportInInfoForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.common.FileUtil;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.fitech.gznx.util.POI2Util;
import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.usermodel.Context;
import com.runqian.report4.usermodel.Engine;
import com.runqian.report4.usermodel.IReport;
import com.runqian.report4.util.ReportUtils;

/**
 * @author jcm 导出子行报表数据
 */
public class CreateSubOrgReportServlet extends HttpServlet
{

    private static final long serialVersionUID = 1L;

    private static final String TMPCOPY="_copy_";

    /**
     * ServletContext
     */
    private ServletContext context = null;

    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        context = config.getServletContext();
    }

    /***
     * 使用了hibernate技术 卞以刚 2011-12-22 影响对象：ReportIn AfTemplate
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            String timeMillis = System.currentTimeMillis() + "";
            String fileTmpName = "";
            Operator operator = null;
            if (request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
                operator = (Operator) request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);

            fileTmpName = Config.RAQ_TEMPLATE_PATH + TMPCOPY + operator.getOperatorId() + "_" + timeMillis;
            System.out.println(fileTmpName);

            String reportFlg = (String) request.getSession().getAttribute(Config.REPORT_SESSION_FLG);
            String srcFileName = fileTmpName;
            String zipFileName = "REPORTS" + timeMillis + ".zip";
            deletefile(fileTmpName);
            DownLoadDataToZip dldtZip = DownLoadDataToZip.newInstance();
            if (dldtZip.isTemplateZipExist(srcFileName))
            {
                dldtZip.deleteFolder(new File(srcFileName));
                File zipFile = new File(srcFileName + ".zip");
                if (zipFile.exists())
                    zipFile.delete();
            }

            File outfile = new File(srcFileName);

            if (outfile.exists())
                dldtZip.deleteFolder(outfile);
            outfile.mkdir();

            HashMap map = new HashMap();

            String repInIdString = request.getParameter("repInIds") != null ? request.getParameter("repInIds") : "";

            // add by ywz
            // query all standard excel template
            Map<String, String> dataRgMap = new HashMap<String, String>();
            Map<String, String> dataRgMapCell = new HashMap<String, String>();
            Map<String, String> curMap = new HashMap<String, String>();
            Map<String, String> freqMap = new HashMap<String, String>();

            File tempFiles = new File(Config.RAQ_TEMPLATE_PATH + "templatefile");
            File[] fileList = tempFiles.listFiles();
            if (null != fileList && fileList.length > 0)
            {
                List dataRgList = StrutsMDataRgTypeDelegate.findAll();
                for (int index = 0; index < dataRgList.size(); index++)
                {
                    MDataRgTypeForm form = (MDataRgTypeForm) dataRgList.get(index);
                    dataRgMap.put(form.getDataRgDesc(), String.valueOf(form.getDataRangeId()));
                    dataRgMapCell.put(String.valueOf(form.getDataRangeId()), form.getDataRgDesc());
                }

                List curList = StrutsMCurrDelegate.findAll();
                for (int index = 0; index < curList.size(); index++)
                {
                    MCurrForm form = (MCurrForm) curList.get(index);
                    curMap.put(form.getCurName(), String.valueOf(form.getCurId()));
                }

                List freqList = StrutsMRepFreqDelegate.findAll();
                for (int index = 0; index < freqList.size(); index++)
                {
                    MRepFreqForm form = (MRepFreqForm) freqList.get(index);
                    freqMap.put(form.getRepFreqName(), String.valueOf(form.getRepFreqId()));
                }

            }
            else
            {
                System.out.println("---------------------没有载入过模板!");
            }
            File copyDir = new File(fileTmpName);
            if (!copyDir.exists())
            {
                copyDir.mkdirs();
            }

            //
            /***
             * 已使用hibernate 卞以刚 2011-12-27 影响对象：ReportIn AfTemplate
             */
            if (repInIdString == null || repInIdString.equals(""))
            {
                ReportInInfoForm reportInInfoForm = new ReportInInfoForm();

                reportInInfoForm.setChildRepId(request.getParameter("childRepId") != null ? request
                        .getParameter("childRepId") : null);
                reportInInfoForm.setRepName(request.getParameter("repName") != null ? request.getParameter("repName")
                        : null);
                reportInInfoForm.setYear(request.getParameter("year") != null ? new Integer(request
                        .getParameter("year")) : null);
                reportInInfoForm.setTerm(request.getParameter("term") != null ? new Integer(request
                        .getParameter("term")) : null);
                reportInInfoForm.setOrgId(request.getParameter("orgId") != null ? request.getParameter("orgId") : null);
                reportInInfoForm.setFrOrFzType(request.getParameter("frOrFzType") != null ? request
                        .getParameter("frOrFzType") : null);
                reportInInfoForm.setRepFreqId(request.getParameter("repFreqId") != null ? new Integer(request
                        .getParameter("repFreqId")) : null);
                reportInInfoForm.setCheckFlag(request.getParameter("checkFlag") != null ? new Short(request
                        .getParameter("checkFlag")) : null);

                /**
                 * 无特殊oracle语法 不需修改 卞以刚 2011-12-22 已使用hibernate 影响对象：ReportIn
                 **/
                List list = StrutsReportInInfoDelegate.searchReportRecord(reportInInfoForm, operator);
                if (list != null && list.size() > 0)
                {
                    for (int i = 0; i < list.size(); i++)
                    {
                        ReportInForm reportInFormTemp = (ReportInForm) list.get(i);

                        // OrgNet orgNet =
                        // StrutsOrgNetDelegate.selectOne(reportInFormTemp.getOrgId());
                        String orgId = reportInFormTemp.getOrgId();
                        String orgReportPath = srcFileName + File.separator + orgId;

                        String newPath = copyDir + File.separator + orgId;
                        File newPathFile = new File(newPath);
                        if (!newPathFile.exists())
                        {
                            newPathFile.mkdirs();
                        }

                        if (!map.containsKey(orgId))
                        {
                            map.put(orgId, orgReportPath);
                            File orgReportFileFolder = new File(orgReportPath);
                            orgReportFileFolder.mkdir();
                        }
                        else
                            orgReportPath = (String) map.get(orgId);
                        // 清单式报表
                        /**
                         * 已使用hibernate 卞以刚 2011-12-22 影响对象：AfTemplate
                         **/
                        AfTemplate aftemplate = AFTemplateDelegate.getTemplate(reportInFormTemp.getChildRepId(),
                                reportInFormTemp.getVersionId());
                        if (aftemplate.getReportStyle() != null
                                && com.fitech.gznx.common.Config.REPORT_QD.equals(String.valueOf(aftemplate
                                .getReportStyle())))
                        {
                            String raqFileName = Config.RAQ_TEMPLATE_PATH + "templateFiles" + File.separator
                                    + "printRaq" + File.separator + "qdfile" + File.separator
                                    + reportInFormTemp.getChildRepId() + "_" + reportInFormTemp.getVersionId() + ".raq";

                            try
                            {
                                ReportDefine rd = (ReportDefine) ReportUtils.read(raqFileName);
                                Context cxt = new Context(); // 构建报表引擎计算环境
                                cxt.setParamValue("RepID", reportInFormTemp.getRepInId());

                                Engine engine = new Engine(rd, cxt); // 构造报表引擎
                                IReport iReport = engine.calc(); // 运算报表
                                String excelFileName = orgReportPath + File.separator
                                        + reportInFormTemp.getChildRepId() + "_" + reportInFormTemp.getVersionId()
                                        + "_" + reportInFormTemp.getDataRangeId() + "_" + reportInFormTemp.getCurId()
                                        + ".xls";
                                ReportUtils.exportToExcel(excelFileName, iReport, false);
                            }
                            catch (Throwable e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            CreateExcel ce = new CreateExcel(reportInFormTemp.getRepInId());
                            ce.setReportFlag(reportFlg);

                            // add by ywz

                            String rptId = reportInFormTemp.getChildRepId();// repInIds[i].split(":")[3];
                            String versionId = reportInFormTemp.getVersionId().substring(0, 3);// repInIds[i].split(":")[4].substring(0,
                            // 3);

                            Map<String,String> versionMap = StrutsExportReportDataDelegate.getVersionMap();
                            String newVersionId = versionMap.get(reportInFormTemp.getChildRepId()+"_"+reportInFormTemp.getVersionId());
                            if(null!=newVersionId)
                            {
                                versionId=newVersionId;
                            }
                            Integer dataRangeId = reportInFormTemp.getDataRangeId();// reportInForm.getDataRangeId();
                            Integer curId = reportInFormTemp.getCurId();// repInIds[i].split(":")[5];
                            // get freqid
                            MActuRepForm mActuRepForm = new MActuRepForm();
                            List<MActuRepForm> formList = StrutsActuRep.getMActuRep(rptId,
                                    reportInFormTemp.getVersionId(), dataRangeId);

                            Integer freqId = formList.get(0).getRepFreqId();
                            // Integer freqId =
                            // reportInFormTemp.getRepFreqId();//
                            // repInIds[i].split(":")[2];
                            boolean findFile = false;
                            for (File cpFile : fileList)
                            {
                                String fileName = cpFile.getName().split("\\.")[0];
                                String lastPart = cpFile.getName().split("\\.")[1];
                                //System.out.println(fileName);
                                //fileName = fileName.replaceFirst("HB", "").replace("（", "(").replace("）", ")");
                                String[] parts = fileName.split("\\-");
                                // String repId = parts[0];


                                //fileName = fileName.replaceFirst("HB", "").replace("（", "(").replace("）", ")");
                                String copyName = fileName;
//                                        .replace(dataRg, dataRgMap.get(dataRg))
//                                        .replace(freq, (String) freqMap.get(freq))
//                                        .replace(cur, (String) curMap.get(cur));
                                if (fileName.split("\\-")[0].equals(rptId.replaceFirst("HB", ""))
                                        && fileName.split("\\-")[1].equals(versionId)
                                        && fileName.split("\\-")[2].equals(dataRangeId.toString()))
                                {
                                    FileUtil.copyFile(cpFile.getPath(), fileTmpName + File.separator
                                            + orgId + File.separator + rptId+"_"+reportInFormTemp.getVersionId()+"_"+dataRangeId.toString()+"_"+curId.toString() + "." + lastPart);
                                    findFile = true;
                                    break;
                                }

                            }
                            if (!findFile)
                            {
                                System.out.println("template not find:" + rptId + "-" + versionId);
//                              ErrorOutPut(response, "template not find:" + rptId + "-" + versionId);
//                              return;
                            }

                            System.out.println("rptId:" + rptId + ",versionId:" + versionId + ",dataRangeId:"
                                    + dataRangeId + ",curId:" + curId + ",freqId:" + freqId);

                            String newFileName = fileTmpName + File.separator + orgId
                                    + File.separator + rptId + "_" + reportInFormTemp.getVersionId() + "_" + dataRangeId+"_"+curId+".xls";
                            HSSFWorkbook book = ce.createDataReport(newFileName, reportInFormTemp.getChildRepId() + "_"
                                            + reportInFormTemp.getVersionId() + "_" + reportInFormTemp.getDataRangeId() + "_"
                                            + reportInFormTemp.getCurId() + ".xls",
                                    dataRgMapCell.get(String.valueOf(dataRangeId)));
                            srcFileName = fileTmpName;
                            // end

                            // File file = new File(orgReportPath +
                            // File.separator
                            // + reportInForm.getChildRepId().replaceFirst("HB",
                            // "")
                            // + "_"
                            // + reportInForm.getVersionId() + "_" +
                            // reportInForm.getDataRangeId() + "_"
                            // + reportInForm.getCurId() + ".xls");
                            File file = new File(newFileName);
                            FileOutputStream fos = new FileOutputStream(file);
                            book.write(fos);
                            fos.flush();
                            fos.close();
                            // 打开导出的Excel，重新计算所有的公式区域并保存
                            //POI2Util.excelFormulaEval(file);
                        }

                    }
                }
                else
                {
                    ErrorOutPut(response);
                    return;
                }
                /**
                 * 已使用hibernate 卞以刚 2011-12-22 影响对象：ReportIn
                 */
            }
            else
            {
                String[] repInIds = repInIdString.split(Config.SPLIT_SYMBOL_COMMA);

                for (int i = 0; i < repInIds.length; i++)
                {
                    String repInId = repInIds[i].split(":")[0];
                    String orgId = repInIds[i].split(":")[1];

                    // OrgNet orgNet = StrutsOrgNetDelegate.selectOne(orgId);
                    String orgReportPath = srcFileName + File.separator + orgId;

                    String newPath = copyDir + File.separator + orgId;
                    File newPathFile = new File(newPath);
                    if (!newPathFile.exists())
                    {
                        newPathFile.mkdirs();
                    }

                    if (!map.containsKey(orgId))
                    {
                        map.put(orgId, orgReportPath);
                        File orgReportFileFolder = new File(orgReportPath);
                        orgReportFileFolder.mkdir();
                    }
                    else
                        orgReportPath = (String) map.get(orgId);

                    ReportInForm reportInForm = StrutsReportInDelegate.getReportIn(new Integer(repInId));

                    // 清单式报表
                    /**
                     * 已使用hibernate 卞以刚 2011-12-22 影响对象：ReportIn
                     **/
                    AfTemplate aftemplate = AFTemplateDelegate.getTemplate(reportInForm.getChildRepId(),
                            reportInForm.getVersionId());
                    if (aftemplate.getReportStyle() != null
                            && com.fitech.gznx.common.Config.REPORT_QD.equals(String.valueOf(aftemplate
                            .getReportStyle())))
                    {

                        String raqFileName = Config.RAQ_TEMPLATE_PATH + "templateFiles" + File.separator + "printRaq"
                                + File.separator + "qdfile" + File.separator + reportInForm.getChildRepId() + "_"
                                + reportInForm.getVersionId() + ".raq";

                        try
                        {
                            ReportDefine rd = (ReportDefine) ReportUtils.read(raqFileName);
                            Context cxt = new Context(); // 构建报表引擎计算环境
                            cxt.setParamValue("RepID", reportInForm.getRepInId());

                            Engine engine = new Engine(rd, cxt); // 构造报表引擎
                            IReport iReport = engine.calc(); // 运算报表
                            String excelFileName = orgReportPath + File.separator + reportInForm.getChildRepId() + "_"
                                    + reportInForm.getVersionId() + "_" + reportInForm.getDataRangeId() + "_"
                                    + reportInForm.getCurId() + ".xls";
                            ReportUtils.exportToExcel(excelFileName, iReport, false);
                        }
                        catch (Throwable e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        CreateExcel ce = new CreateExcel(reportInForm.getRepInId());
                        ce.setReportFlag(reportFlg);

                        // add by ywz
                        String rptId = repInIds[i].split(":")[3];
                        String versionId = repInIds[i].split(":")[4].substring(0, 3);
                        Integer dataRangeId = reportInForm.getDataRangeId();
                        String curId = repInIds[i].split(":")[5];
                        String freqId = repInIds[i].split(":")[2];

                        Map<String,String> versionMap = StrutsExportReportDataDelegate.getVersionMap();
                        String newVersionId = versionMap.get(reportInForm.getChildRepId()+"_"+reportInForm.getVersionId());
                        if(null!=newVersionId)
                        {
                            versionId=newVersionId;
                        }

                        boolean findFile = false;
                        if(null != fileList) {
                            for (File cpFile : fileList) {
                                String fileName = cpFile.getName().split("\\.")[0];
                                String lastPart = cpFile.getName().split("\\.")[1];
                                //System.out.println(fileName);
                                //fileName = fileName.replaceFirst("HB", "").replace("（", "(").replace("）", ")");
                                String[] parts = fileName.split("\\-");

//                            fileName = fileName.replaceFirst("HB", "").replace("（", "(").replace("）", ")");
//                            String copyName = fileName.replace(dataRg, dataRgMap.get(dataRg))
//                                    .replace(freq, (String) freqMap.get(freq)).replace(cur, (String) curMap.get(cur));
                                String copyName = fileName;
                                if (fileName.split("\\-")[0].equals(rptId) && fileName.split("\\-")[1].equals(versionId)
                                        && fileName.split("\\-")[2].equals(dataRangeId.toString())) {
                                    FileUtil.copyFile(cpFile.getPath(), fileTmpName + File.separator
                                            + orgId + File.separator + rptId + "_" + reportInForm.getVersionId() + "_" + dataRangeId.toString() + "_" + curId.toString() + "." + lastPart);
                                    findFile = true;
                                    break;
                                }

                            }
                        }
                        if (!findFile)
                        {
                            System.out.println("template not find:" + rptId + "-" + versionId);
//                          ErrorOutPut(response, "template not find:" + rptId + "-" + versionId);
//                          return;
                        }

                        System.out.println("rptId:" + rptId + ",versionId:" + versionId + ",dataRangeId:" + dataRangeId
                                + ",curId:" + curId + ",freqId:" + freqId);

                        String newFileName = fileTmpName + File.separator + orgId
                                + File.separator + rptId + "_" + reportInForm.getVersionId() + "_" + dataRangeId + "_"+curId+".xls";
                        HSSFWorkbook book = ce.createDataReport(newFileName, reportInForm.getChildRepId() + "_"
                                + reportInForm.getVersionId() + "_" + reportInForm.getDataRangeId() + "_"
                                + reportInForm.getCurId() + ".xls", dataRgMapCell.get(String.valueOf(dataRangeId)));
                        srcFileName = fileTmpName;
                        // end

                        // File file = new File(orgReportPath + File.separator
                        // + reportInForm.getChildRepId().replaceFirst("HB", "")
                        // + "_"
                        // + reportInForm.getVersionId() + "_" +
                        // reportInForm.getDataRangeId() + "_"
                        // + reportInForm.getCurId() + ".xls");
                        File file = new File(newFileName);
                        FileOutputStream fos = new FileOutputStream(file);
                        book.write(fos);
                        fos.flush();
                        fos.close();
                        // 打开导出的Excel，重新计算所有的公式区域并保存
                        //POI2Util.excelFormulaEval(file);
                    }
                }
            }

            boolean bool = false;
            dldtZip.gzip(srcFileName, bool);

            response.reset();
            response.setContentType("application/x-zip-compressed;name=\"" + zipFileName + "\"");
            response.addHeader("Content-Disposition", "attachment; filename=\"" + FitechUtil.toUtf8String(zipFileName)
                    + "\"");
            response.setHeader("Accept-ranges", "bytes");

            FileInputStream inStream = new FileInputStream(srcFileName + ".zip");

            int len;
            byte[] buffer = new byte[100];

            while ((len = inStream.read(buffer)) > 0)
            {
                response.getOutputStream().write(buffer, 0, len);
            }
            inStream.close();
            new File(srcFileName + ".zip").delete();
            dldtZip.deleteFolder(outfile);

            deletefile(fileTmpName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ErrorOutPut(response);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException
    {
        this.doGet(request, response);
    }

    /**
     * 错误输出
     *
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

    /**
     * 错误输出
     *
     * @param response
     */
    private void ErrorOutPut(HttpServletResponse response, String errorMsg)
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
        out.println("<font color=\"blue\">" + errorMsg + "</font>");
        out.close();
    }

    public static boolean deletefile(String delpath) throws Exception
    {
        try
        {
            File file = new File(delpath);
            // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
            if (!file.isDirectory() && file.exists())
            {
                String absolutePath = file.getAbsolutePath();
                boolean delResult = file.delete();
                System.out.println(absolutePath + "删除" + (delResult ? "成功" : "失败"));
            }
            else if (file.isDirectory())
            {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++)
                {
                    deletefile(delpath + File.separator + filelist[i]);
                }
                String absolutePath = file.getAbsolutePath();
                if(file.exists())
                {
                    boolean delResult = file.delete();
                    System.out.println(absolutePath + "删除" + (delResult ? "成功" : "失败"));
                }
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("deletefile() Exception:" + e.getMessage());
        }
        return true;
    }
}
