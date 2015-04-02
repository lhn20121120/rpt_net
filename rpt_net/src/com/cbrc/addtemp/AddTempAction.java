package com.cbrc.addtemp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.RequestUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MDataRgTypeForm;
import com.cbrc.smis.form.UploadFileForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;

public class AddTempAction extends DispatchAction
{
    private FitechException log = new FitechException(AddTempAction.class);

    private String flag = null;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        flag = null;

        Operator operator = null;
        HttpSession session = request.getSession();
        if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
        {
            operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
        }

        /* 上传文件临时目录 */
        String SERVICE_UP_TEMP = Config.TEMP_DIR + "addtemp" + File.separator + operator.getOperatorId()
                + File.separator;

        /* 上传文件解压后的临时目录 */
        // String SERVICE_UP_RELEASE = SERVICE_UP_TEMP + "release" +
        // File.separator;
        String SERVICE_UP_RELEASE = Config.RAQ_TEMPLATE_PATH + "templatefile";
        // FitechMessages messages = new FitechMessages();
        request.getSession().setMaxInactiveInterval(60 * 30);
        // String bankType = request.getParameter("bankFlag");

        // System.out.println("bankType : " + bankType);

        UploadFileForm upFileForm = (UploadFileForm) form;
        RequestUtils.populate(upFileForm, request);

        FormFile formFile = upFileForm.getFormFile();
        FitechMessages messages = new FitechMessages();
        // String orgId = (String) request.getParameter("orgId");
        List<File> zipRelFileList = new ArrayList<File>(); // ZIP包解压后的文件列表

        /** 建立临时文件夹 */
        this.make(SERVICE_UP_TEMP);
        this.make(SERVICE_UP_RELEASE);

        /** 文件上传 */
        String path = SERVICE_UP_TEMP;
        String zipFileName = path + formFile.getFileName();
        InputStream inStream = formFile.getInputStream();
        OutputStream outStream = new FileOutputStream(zipFileName);
        byte[] bytes = new byte[1024];
        int index = 0;
        while ((index = inStream.read(bytes)) != -1)
        {
            outStream.write(bytes, 0, index);
        }
        if (inStream != null)
            inStream.close();
        if (outStream != null)
            outStream.close();

        /**
         * 处理上传的ZIP文件 先解压
         */
        if (!release(formFile.getFileName(), path, SERVICE_UP_RELEASE, zipRelFileList))
        {
            messages.add("ZIP文件解压失败！");
            // request.setAttribute("RequestParam", url);
            // request.setAttribute("curPage", curPage);
            // request.setAttribute(com.cbrc.smis.common.Config.APART_PAGE_OBJECT,aPage);
            request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
            return mapping.findForward("success");
        }
        messages.add("载入成功");
        request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
        request.getSession().setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
        flag = "over";
        
        return mapping.findForward("success");
    }

    public ActionForward getOverFlag(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        if (null != flag)
        {
            flag = null;
            return mapping.findForward("success");
        }
        else
        {
            return null;
        }

    }

    /**
     * 解压ZIP包
     * 
     * @param zipFileName ZIP包名称
     * @param fPath ZIP包所在文件目录
     * @param tPath 需要解压的文件目录
     * @param fileList 解压后的文件列表
     * @return boolean true-解压成功 false-解压失败
     */
    private boolean release(String zipFileName, String fPath, String tPath, List<File> fileList)
    {
        boolean result = false;
        File file = null;
        InputStream inStream = null;
        OutputStream outStream = null;
        if (zipFileName == null || zipFileName.equals("") || fPath == null || fPath.equals("") || tPath == null
                || tPath.equals(""))
            return result;

        try
        {
            if (fileList == null)
            {
                fileList = new ArrayList<File>();
            }
            // File encodeFile = new File(fPath + File.separator + zipFileName);
            // addkeyWord(encodeFile, encodeFile);
            Map<String, String> dataRgMap = new HashMap<String, String>();
            Map<String, String> dataRgMapCell = new HashMap<String, String>();
            List dataRgList=new ArrayList();
            try
            {
                dataRgList = StrutsMDataRgTypeDelegate.findAll();
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            for (int index = 0; index < dataRgList.size(); index++)
            {
                MDataRgTypeForm form = (MDataRgTypeForm) dataRgList.get(index);
                dataRgMap.put(form.getDataRgDesc(), String.valueOf(form.getDataRangeId()));
                dataRgMapCell.put(String.valueOf(form.getDataRangeId()), form.getDataRgDesc());
            }
            
            ZipFile zipFile = new ZipFile(fPath + File.separator + zipFileName);
            Enumeration enu = zipFile.getEntries();
            while (enu.hasMoreElements())
            {
                ZipEntry entry = (ZipEntry) enu.nextElement();
                inStream = zipFile.getInputStream(entry);
                String zipName = entry.getName();
                if (zipName.indexOf("/") > -1)
                {
                    zipName = zipName.substring(zipName.indexOf("/") + 1);
                }
                file = new File(tPath + File.separator + zipName);
                //System.out.println(tPath + File.separator + zipName);
                // System.out.println(file.isDirectory());
                // System.out.println(file.isFile());
                if (file.isDirectory())
                {
                    continue;
                }
                
                //对文件重命名
//                String fileName = zipName.split("\\.")[0];
//                fileName = fileName.replace("（", "(").replace("）", ")");
//                String[] parts = fileName.split("\\-");
//                // String repId = parts[0];
//                String dataRg = parts[2];
//                String freq = parts[3]; 
//                String cur = parts[4];
//                String newName = fileName.split("\\-")[0]+"-"+fileName.split("\\-")[1]+"-"+dataRgMap.get(fileName.split("\\-")[2])+"."+zipName.split("\\.")[1];
//                file = new File(tPath + File.separator + newName);
                System.out.println(tPath + File.separator + file.getName());
                
                outStream = new FileOutputStream(file);
                byte[] bytes = new byte[1024];
                int index = 0;
                while ((index = inStream.read(bytes)) != -1)
                    outStream.write(bytes, 0, index);

                if (inStream != null)
                    inStream.close();
                if (outStream != null)
                    outStream.close();

                fileList.add(file);
            }
            zipFile.close();
            result = true;
        }
        catch (FileNotFoundException e)
        {

            result = false;
            log.printStackTrace(e);
            e.printStackTrace();
        }
        catch (IOException e)
        {
            result = false;
            log.printStackTrace(e);
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 建立文件目录
     * 
     * @param filePath 目录路径
     * @return void
     */
    public void make(String filePath)
    {
        File file = new File(filePath);

        if (!file.exists())
            file.mkdirs();
    }

}
