package com.fitech.instiution.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.RequestUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.UploadFileForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.instiution.service.InstitutionService;
import com.fitech.instiution.service.impl.InstitutionServiceImpl;

import edu.emory.mathcs.backport.java.util.Arrays;

public class InstitutionAction extends DispatchAction
{
    private FitechException log = new FitechException(InstitutionAction.class);

    InstitutionService institutionService = InstitutionServiceImpl.getInstance();

    private final String FUNCTION_WEB = "function_web.txt";

    private final String FUNCTION_MODEL = "function_model_function_web.txt";

    private final String OFFRESOURCE = "OffsetResources_zh_CN.properties";

    private final String FORMU_EXCEL = "formu_excel.txt";

    private String flag = null;

    public ActionForward importFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
        String SERVICE_UP_TEMP = Config.TEMP_DIR + "institution" + File.separator + operator.getOperatorId()
                + File.separator;

        /* 上传文件解压后的临时目录 */
        String SERVICE_UP_RELEASE = SERVICE_UP_TEMP + "release" + File.separator;

        // FitechMessages messages = new FitechMessages();
        request.getSession().setMaxInactiveInterval(60 * 30);
        String bankType = request.getParameter("bankFlag");

        System.out.println("bankType : " + bankType);

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

        Map<String, List<String>> funWebMap = new HashMap<String, List<String>>();
        Map<String, List<String>> funModelFunWebMap = new HashMap<String, List<String>>();
        Map<String, String> resourceMap = new HashMap<String, String>();
        Map<String, List<String>> excelMap = new HashMap<String, List<String>>();

        try
        {
            // 解析文件
            parseFiles(zipRelFileList, funWebMap, funModelFunWebMap, resourceMap, excelMap);
        }
        catch (Exception e)
        {
            log.printStackTrace(e);
            messages.add("解析文件失败");
            request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
            return mapping.findForward("success");
        }

        Map<String, String> allDataMap = institutionService.deletemCellFormulaStandard(bankType);
        institutionService.saveCellFormulaStandard(funWebMap, funModelFunWebMap, bankType, allDataMap, excelMap);

        System.out.println("save date success");
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
     * 解析文件
     * 
     * @param zipRelFileList
     * @param funWebMap
     * @param funModelFunWebMap
     * @param resourceMap
     */
    public void parseFiles(List<File> zipRelFileList, Map<String, List<String>> funWebMap,
            Map<String, List<String>> funModelFunWebMap, Map<String, String> resourceMap,
            Map<String, List<String>> excelMap)
    {

        // 存放偏移量计算后的结果
        // Map<String, List<String>> resultFunWebMap = new HashMap<String,
        // List<String>>();

        // 分别解析三个对应的文件
        for (File file : zipRelFileList)
        {
            parseFunctionWeb(funWebMap, file);
            parseModelFunctionWeb(funModelFunWebMap, file);
            parseOffsetResource(resourceMap, file);
            parseFormuExcel(excelMap, file);
        }

        // 针对解析得到的数据进行偏移量计算
        Set<Entry<String, List<String>>> funWebSet = funWebMap.entrySet();
        Iterator<Entry<String, List<String>>> it = funWebSet.iterator();

        String tolerances = institutionService.getTolerances();
        while (it.hasNext())
        {
            Entry<String, List<String>> funWebEntry = it.next();
            String formulaId = funWebEntry.getKey();

            String formulaType = funWebEntry.getValue().get(0);
            String cell_formu = funWebEntry.getValue().get(1);
            String cell_formu_view = funWebEntry.getValue().get(2);
            if ("[8.]=[1.]+[2.]+[3.]+[4.]+[5.]+[6.]+[7.]C列".equals(cell_formu_view))
            {
                System.out.println(cell_formu);
            }

            List<String> resultCellFormula = new ArrayList<String>();

            if (StringUtils.isNotEmpty(tolerances))
            {
                String[] toleranceList = tolerances.split("\\,");
                for (String tolerance : toleranceList)
                {
                    if (cell_formu.contains("+" + tolerance) && cell_formu.contains("-" + tolerance))
                    {
                        cell_formu = cell_formu.replaceAll("\\*100", "").replaceAll("\\/100", "");
                    }
                }
            }
            // 替换*100
            // if (cell_formu.contains("*100"))
            // {
            // cell_formu = cell_formu.replaceAll("\\*100", "");
            // }

            // 根据formulaId查到模板和版本的组合，准备到偏移量文件中匹配
            List<String> tempVerValArray = funModelFunWebMap.get(formulaId);
            if (null != tempVerValArray && tempVerValArray.size() != 0)
            {
                String tempVer = tempVerValArray.get(0) + tempVerValArray.get(1);

                String location = resourceMap.get(tempVer);

                if (null != location)
                {

                    // 有的公式外层不偏移，但是内部部分偏移
                    if ("0".equalsIgnoreCase(location))
                    {
                        // 如果是表间公式
                        if (formulaType.contains("2"))
                        {
                            String regex = "\\[(.*?)\\]";
                            Pattern p = Pattern.compile(regex);
                            Matcher m = p.matcher(cell_formu);
                            Map<String, String> tempMap = new HashMap<String, String>();
                            while (m.find())
                            {
                                if (m.group().contains("_"))
                                {
                                    // String tempCopy = m.group();
                                    String temp = m.group().replace("[", "").replace("]", "");
                                    String[] tempArray = temp.split("_");
                                    String py = resourceMap.get(tempArray[0] + tempArray[1]);
                                    // 如果标间公式偏移量不为0
                                    if (null != py && !"0".equals(py))
                                    {

                                        String regexNum = "([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])";
                                        Pattern pNum = Pattern.compile(regexNum);
                                        Matcher mNum = pNum.matcher(tempArray[2]);
                                        String strCopy = m.group();
                                        while (mNum.find())
                                        {
                                            int real = Integer.valueOf(mNum.group().trim())
                                                    + Integer.valueOf(py.trim());
                                            // if
                                            // (tempMap.containsKey(m.group()))
                                            // {
                                            // continue;
                                            // }
                                            // tempMap.put(strCopy, null);
                                            strCopy = strCopy.replace(mNum.group(), String.valueOf(real));

                                            cell_formu = cell_formu.replaceFirst(
                                                    m.group().replace("[", "\\[").replace("]", "\\]"), strCopy);
                                        }
                                    }
                                }

                            }

                        }
                    }
                    else
                    {

                        // 如果表达式含有reg，解析替换这个表达式
                        if (cell_formu.contains("reg"))
                        {
                            String[] parts = cell_formu.split("reg");
                            // String regex = "\\[(.*?)\\]";
                            String regex = "([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])";
                            Pattern p = Pattern.compile(regex);
                            Matcher m = p.matcher(parts[0]);
                            while (m.find())
                            {
                                System.out.println(m.group());
                                int real = Integer.valueOf(m.group().trim()) + Integer.valueOf(location.trim());
                                cell_formu = cell_formu.replace(m.group(), String.valueOf(real));

                            }

                        }
                        // 表间公式
                        else if (formulaType.contains("2"))
                        {
                            String regex = "\\[(.*?)\\]";
                            Pattern p = Pattern.compile(regex);
                            Matcher m = p.matcher(cell_formu);
                            Map<Integer, List<List<String>>> tempMap = new HashMap<Integer, List<List<String>>>();
                            List<List<String>> innerTempList = new ArrayList<List<String>>();

                            List<String> bjList = new ArrayList<String>();
                            while (m.find())
                            {
                                if (!m.group().contains("_"))
                                {
                                    String regexNum = "([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])";
                                    Pattern pNum = Pattern.compile(regexNum);
                                    Matcher mNum = pNum.matcher(m.group());
                                    String strCopy = m.group();
                                    while (mNum.find())
                                    {
                                        System.out.println(mNum.group());
                                        int real = Integer.valueOf(mNum.group().trim())
                                                + Integer.valueOf(location.trim());
                                        // if (tempMap.containsKey(m.group()))
                                        // {
                                        // continue;
                                        // }
                                        // tempMap.put(strCopy, null);
                                        strCopy = strCopy.replace(mNum.group(), String.valueOf(real));

                                        List<String> inner = new ArrayList<String>();
                                        inner.add(m.group());
                                        inner.add(strCopy);
                                        innerTempList.add(inner);

                                        tempMap.put(Integer.valueOf(mNum.group()), innerTempList);
                                        // cell_formu = cell_formu.replaceFirst(
                                        // m.group().replace("[",
                                        // "\\[").replace("]", "\\]"), strCopy);
                                    }

                                }
                                else
                                {
                                    bjList.add(m.group());
                                    // String tempCopy = m.group();
                                    String temp = m.group().replace("[", "").replace("]", "");
                                    String[] tempArray = temp.split("_");
                                    String py = resourceMap.get(tempArray[0] + tempArray[1]);
                                    // 如果标间公式偏移量不为0
                                    if (null != py && !"0".equals(py))
                                    {

                                        String regexNum = "([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])";
                                        Pattern pNum = Pattern.compile(regexNum);
                                        Matcher mNum = pNum.matcher(tempArray[2]);
                                        String strCopy = m.group();
                                        while (mNum.find())
                                        {
                                            int real = Integer.valueOf(mNum.group().trim())
                                                    + Integer.valueOf(py.trim());
                                            // if
                                            // (tempMap.containsKey(m.group()))
                                            // {
                                            // continue;
                                            // }
                                            // tempMap.put(strCopy, null);
                                            strCopy = strCopy.replace(mNum.group(), String.valueOf(real));

                                            cell_formu = cell_formu.replaceFirst(
                                                    m.group().replace("[", "\\[").replace("]", "\\]"), strCopy);
                                        }
                                    }
                                }
                            }

                            // 1、先替换非表间
                            Object[] unsort_key = tempMap.keySet().toArray();
                            Arrays.sort(unsort_key);
                            if (Integer.valueOf(location.trim()) < 0)
                            {
                                for (int i = 0; i < unsort_key.length; i++)
                                {
                                    List<List<String>> forList = tempMap.get(unsort_key[i]);
                                    for (int j = 0; j < forList.size(); j++)
                                    {
                                        cell_formu = cell_formu.replace(forList.get(j).get(0), forList.get(j).get(1));
                                    }

                                }
                            }
                            else
                            {
                                for (int i = unsort_key.length - 1; i >= 0; i--)
                                {
                                    List<List<String>> forList = tempMap.get(unsort_key[i]);
                                    for (int j = 0; j < forList.size(); j++)
                                    {
                                        cell_formu = cell_formu.replace(forList.get(j).get(0), forList.get(j).get(1));
                                    }
                                }
                            }
                        }
                        else
                        {
                            String regex = "\\[(.*?)\\]";
                            Pattern p = Pattern.compile(regex);
                            Matcher m = p.matcher(cell_formu);
                            Map<Integer, String> temp = new HashMap<Integer, String>();
                            while (m.find())
                            {
                                String regexNum = "([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])";
                                Pattern pNum = Pattern.compile(regexNum);
                                Matcher mNum = pNum.matcher(m.group());
                                String strCopy = m.group();
                                while (mNum.find())
                                {
                                    System.out.println(mNum.group());
                                    int real = Integer.valueOf(mNum.group().trim()) + Integer.valueOf(location.trim());

                                    strCopy = strCopy.replace(mNum.group(), String.valueOf(real));
                                    // if (temp.containsValue(m.group()))
                                    // {
                                    // continue;
                                    // }
                                    // temp.put(m.group(), null);
                                    temp.put(Integer.valueOf(mNum.group()), String.valueOf(real));
                                    // cell_formu = cell_formu.replaceFirst(
                                    // m.group().replace("[",
                                    // "\\[").replace("]", "\\]"), strCopy);

                                }

                            }
                            Object[] unsort_key = temp.keySet().toArray();
                            Arrays.sort(unsort_key);
                            if (Integer.valueOf(location.trim()) < 0)
                            {
                                for (int i = 0; i < unsort_key.length; i++)
                                {
                                    cell_formu = cell_formu.replace(String.valueOf(unsort_key[i]),
                                            temp.get(unsort_key[i]));
                                }
                            }
                            else
                            {
                                for (int i = unsort_key.length - 1; i >= 0; i--)
                                {
                                    cell_formu = cell_formu.replace(String.valueOf(unsort_key[i]),
                                            temp.get(unsort_key[i]));
                                }
                            }
                        }
                    }

                }
                else
                {
                    // 如果是表间公式
                    if (formulaType.contains("2"))
                    {
                        String regex = "\\[(.*?)\\]";
                        Pattern p = Pattern.compile(regex);
                        Matcher m = p.matcher(cell_formu);
                        Map<String, String> tempMap = new HashMap<String, String>();
                        while (m.find())
                        {
                            if (m.group().contains("_"))
                            {
                                // String tempCopy = m.group();
                                String temp = m.group().replace("[", "").replace("]", "");
                                String[] tempArray = temp.split("_");
                                String py = resourceMap.get(tempArray[0] + tempArray[1]);
                                // 如果标间公式偏移量不为0
                                if (null != py && !"0".equals(py))
                                {

                                    String regexNum = "([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])";
                                    Pattern pNum = Pattern.compile(regexNum);
                                    Matcher mNum = pNum.matcher(tempArray[2]);
                                    String strCopy = m.group();
                                    while (mNum.find())
                                    {
                                        int real = Integer.valueOf(mNum.group().trim()) + Integer.valueOf(py.trim());
                                        // if (tempMap.containsKey(m.group()))
                                        // {
                                        // continue;
                                        // }
                                        // tempMap.put(strCopy, null);
                                        strCopy = strCopy.replace(mNum.group(), String.valueOf(real));

                                        cell_formu = cell_formu.replaceFirst(
                                                m.group().replace("[", "\\[").replace("]", "\\]"), strCopy);
                                    }
                                }
                            }

                        }

                    }
                }

                System.out.println(cell_formu);
            }

            resultCellFormula.add(formulaType);
            resultCellFormula.add(cell_formu);
            resultCellFormula.add(cell_formu_view);
            funWebMap.put(formulaId, resultCellFormula);

        }

    }

    private void parseFormuExcel(Map<String, List<String>> excelMap, File file)
    {

        if (file.getName().equalsIgnoreCase(FORMU_EXCEL))
        {
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
                        // 具体处理文件解析的内容
                        String[] excelParts = line.split("\\,");
                        System.out.println(excelParts[1]);
                        List<String> valList = excelMap.get(excelParts[1]);
                        if (null == valList)
                        {
                            List<String> list = new ArrayList<String>();
                            list.add(line);
                            excelMap.put(excelParts[1], list);
                        }
                        else
                        {
                            valList.add(line);
                            excelMap.put(excelParts[1], valList);
                        }

                    }

                }

            }
            catch (FileNotFoundException e)
            {
                log.printStackTrace(e);
            }
            catch (IOException e)
            {
                log.printStackTrace(e);
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
            System.out.println("parse file " + FUNCTION_MODEL + " over.");
        }

    }

    /**
     * 解析OffsetResources_zh_CN.properties文件
     * 
     * @param resourceMap
     * @param file
     */
    private void parseOffsetResource(Map<String, String> resourceMap, File file)
    {

        if (file.getName().equalsIgnoreCase(OFFRESOURCE))
        {
            BufferedReader br = null;
            try
            {
                br = new BufferedReader(new FileReader(file), 5 * 1024);
                String line;
                while ((line = br.readLine()) != null)
                {
                    System.out.println(line);
                    if (StringUtils.isNotEmpty(line) && line.contains("="))
                    {
                        // 具体处理文件解析的内容
                        String[] funWebParts = line.split("\\=");
                        System.out.println(funWebParts[0]);

                        resourceMap.put(funWebParts[0], funWebParts[1].trim());
                    }

                }

            }
            catch (FileNotFoundException e)
            {
                log.printStackTrace(e);
            }
            catch (IOException e)
            {
                log.printStackTrace(e);
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
            System.out.println("parse file " + OFFRESOURCE + " over.");
        }

    }

    /**
     * 解析function_model_function_web.txt文件，对应M_CELL_TO_FORMU表
     * 
     * @param funModelFunWebMap
     * @param file
     */
    private void parseModelFunctionWeb(Map<String, List<String>> funModelFunWebMap, File file)
    {
        if (file.getName().equalsIgnoreCase(FUNCTION_MODEL))
        {
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
                        // 具体处理文件解析的内容
                        String[] funWebParts = line.split("\\,");
                        System.out.println(funWebParts[2]);
                        List<String> funWebArray = new ArrayList<String>();
                        funWebArray.add(funWebParts[0]);
                        funWebArray.add(funWebParts[1]);

                        funModelFunWebMap.put(funWebParts[2], funWebArray);
                    }

                }

            }
            catch (FileNotFoundException e)
            {
                log.printStackTrace(e);
            }
            catch (IOException e)
            {
                log.printStackTrace(e);
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
            System.out.println("parse file " + FUNCTION_MODEL + " over.");
        }

    }

    /**
     * 解析function_web.txt文件，对应M_CELL_FORMU表
     * 
     * @param funWebMap
     * @param file
     */
    private void parseFunctionWeb(Map<String, List<String>> funWebMap, File file)
    {
        if (file.getName().equalsIgnoreCase(FUNCTION_WEB))
        {
            BufferedReader br = null;
            try
            {
                FileInputStream fis = new FileInputStream(file); 
                InputStreamReader isr = new InputStreamReader(fis,"GB2312");  
                br = new BufferedReader(isr, 5 * 1024);
                String line;
                while ((line = br.readLine()) != null)
                {
                    System.out.println(line);
                    if (StringUtils.isNotEmpty(line))
                    {
                        // 具体处理文件解析的内容
                        String[] funWebParts = line.split("※");
                        System.out.println(funWebParts[0]);
                        List<String> funWebArray = new ArrayList<String>();
                        funWebArray.add(funWebParts[1]);
                        funWebArray.add(funWebParts[2]);
                        funWebArray.add(funWebParts[3]);

                        funWebMap.put(funWebParts[0], funWebArray);
                    }

                }

            }
            catch (FileNotFoundException e)
            {
                log.printStackTrace(e);
            }
            catch (IOException e)
            {
                log.printStackTrace(e);
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
            System.out.println("parse file " + FUNCTION_WEB + " over.");
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
            File encodeFile = new File(fPath + File.separator + zipFileName);
            addkeyWord(encodeFile, encodeFile);

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

    /**
     * 加解密方法
     * 
     * @param infile
     * @param outfile
     * @return
     */
    public static File addkeyWord(File infile, File outfile)
    {
        try
        {
            FileInputStream fs = new FileInputStream(infile);
            int length = (int) infile.length();
            byte[] buffer = new byte[length];
            fs.read(buffer);
            fs.close();
            byte[] buffer2 = new byte[length];
            int m = length;
            for (int i = 0; i < length; i++)
            {
                buffer2[i] = buffer[m - 1];
                m--;
            }
            FileOutputStream fo = new FileOutputStream(outfile);
            fo.write(buffer2, 0, length);
            fo.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return outfile;
    }

    public InstitutionService getInstitutionService()
    {
        return institutionService;
    }

    public void setInstitutionService(InstitutionService institutionService)
    {
        this.institutionService = institutionService;
    }

}
