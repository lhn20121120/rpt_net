package com.cbrc.exesqlcbrc.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.RequestUtils;

import com.cbrc.exesqlcbrc.service.ExecuteSqlService;
import com.cbrc.exesqlcbrc.service.impl.ExecuteSqlServiceImpl;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.UploadFileForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;

public class ExecuteSqlAction extends DispatchAction
{

    private FitechException log = new FitechException(ExecuteSqlAction.class);

    /* �ϴ��ļ���ʱĿ¼ */
    private final String SERVICE_UP_TEMP = Config.TEMP_DIR + "fitetl" + File.separator;

    private ExecuteSqlService iGetResultSetBySQLService = new ExecuteSqlServiceImpl();

    private String flag = null;

    /***
     * ִ��sql
     * 
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws FileNotFoundException
     */
    public ActionForward importFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, FileNotFoundException, IOException
    {
        flag = null;
        request.getSession().setMaxInactiveInterval(60 * 30);

        UploadFileForm upFileForm = (UploadFileForm) form;
        RequestUtils.populate(upFileForm, request);

        FormFile formFile = upFileForm.getFormFile();
        FitechMessages messages = new FitechMessages();

        /** ������ʱ�ļ��� */
        this.make(SERVICE_UP_TEMP);

        /** �ļ��ϴ� */
        String path = SERVICE_UP_TEMP;
        String sqlFileName = path + File.separator + formFile.getFileName();
        InputStream inStream = formFile.getInputStream();
        OutputStream outStream = new FileOutputStream(sqlFileName);
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

        System.out.println("upload over. file : " + sqlFileName);
        File file = new File(sqlFileName);

        Map<String, String> sqlMap = new LinkedHashMap<String, String>();

        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new FileReader(file), 5 * 1024);
            String line;
            while ((line = br.readLine()) != null)
            {
                System.out.println(line);
                if (StringUtils.isNotEmpty(line))
                {
                    // ����ע��
                    if (line.contains("--"))
                    {
                        continue;
                    }

                    line = line.replaceAll(";", "");

                    sqlMap.put(line, null);

                }

            }

        }

        catch (IOException e)
        {
            log.printStackTrace(e);
            messages.add("�����ļ�ʧ�ܣ���ɾ���ѵ������ݺ����µ���");
            request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
        }
        finally
        {
            try
            {
                br.close();
            }
            catch (IOException e)
            {
                log.printStackTrace(e);
            }
        }

        try
        {
            if (sqlMap.isEmpty())
            {
                log.println("sql file has no sql to execute");
                messages.add(formFile.getFileName() + " has no sql to execute");
                request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
            }
            iGetResultSetBySQLService.execSQL(sqlMap, messages);
        }
        catch (Exception e)
        {
            log.printStackTrace(e);
            e.printStackTrace();
            messages.add("sqlִ��ʧ�ܣ���ɾ���ѵ����������µ���");
            request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
        }
        request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
        flag = "over";
        list(mapping, form, request, response);
        return mapping.findForward("success");
    }

    /**
     * �����ļ�
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public ActionForward downLoadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, FileNotFoundException, IOException
    {
        Map<String, String> mCellMap = new HashMap<String, String>();
        Map<String, String> mCellToMap = new HashMap<String, String>();

        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);

        String desFile = Config.TEMP_DIR + "CELL_FORMU_BAK_" + dateString + ".txt";
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(desFile));

        iGetResultSetBySQLService.bakData(mCellMap, mCellToMap);

        // truncate tables
        bufferedOutputStream.write("truncate table m_cell_formu\n".getBytes());
        bufferedOutputStream.write("truncate table m_cell_to_formu\n".getBytes());

        Set<String> mCellSet = mCellMap.keySet();
        Iterator<String> mCellIt = mCellSet.iterator();
        while (mCellIt.hasNext())
        {
            bufferedOutputStream.write(mCellIt.next().getBytes());

        }

        Set<String> mCellToSet = mCellToMap.keySet();
        Iterator<String> mCeTollIt = mCellToSet.iterator();
        while (mCeTollIt.hasNext())
        {
            bufferedOutputStream.write(mCeTollIt.next().getBytes());

        }

        // ���������е�����ȫ��д��
        bufferedOutputStream.flush();
        bufferedOutputStream.close();

        File file = new File(desFile);// path�Ǹ�����־·�����ļ���ƴ�ӳ�����
        String filename = file.getName();// ��ȡ��־�ļ�����

        InputStream fis = new BufferedInputStream(new FileInputStream(desFile));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        response.reset();
        // ��ȥ���ļ������еĿո�,Ȼ��ת�������ʽΪutf-8,��֤����������,����ļ�������������������ؿ����Զ���ʾ���ļ���
        response.addHeader("Content-Disposition", "attachment;filename="
                + new String(filename.replaceAll(" ", "").getBytes("utf-8"), "iso8859-1"));
        response.addHeader("Content-Length", "" + file.length());
        OutputStream os = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        os.write(buffer);// ����ļ�
        os.flush();
        os.close();
        return null;
    }

    /**
     * �����ļ�Ŀ¼
     * 
     * @param filePath Ŀ¼·��
     * @return void
     */
    public void make(String filePath)
    {
        File file = new File(filePath);

        if (!file.exists())
            file.mkdir();
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

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, FileNotFoundException, IOException
    {
        List<String> list = null;
        list = iGetResultSetBySQLService.getAllList();
        request.setAttribute(Config.RECORDS, list);

        return mapping.findForward("success");
    }

    public ActionForward backUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, FileNotFoundException, IOException
    {
        String bakTime = request.getParameter("bakTime");
        iGetResultSetBySQLService.bakUp(bakTime);
        list(mapping, form, request, response);
        FitechMessages messages = new FitechMessages();
        messages.add("��ԭ���");
        request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
        return mapping.findForward("success");
    }

}
