package com.fitech.institution.action;

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
import java.util.ArrayList;
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

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.UploadFileForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.institution.po.BakTableInfoVo;
import com.fitech.institution.service.ExecuteSqlService;
import com.fitech.institution.service.impl.ExecuteSqlServiceImpl;

public class ExecuteSqlAction extends DispatchAction {

	private FitechException log = new FitechException(ExecuteSqlAction.class);

	/* �ϴ��ļ���ʱĿ¼ */
	private final String SERVICE_UP_TEMP = Config.TEMP_DIR + "fitetl"
			+ File.separator;

	private ExecuteSqlService iGetResultSetBySQLService = new ExecuteSqlServiceImpl();

	private String flag = null;

	/***
	 * ����
	 * 
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public ActionForward importFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, FileNotFoundException, IOException {
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
		while ((index = inStream.read(bytes)) != -1) {

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
		int num = 0;
		try {
			br = new BufferedReader(new FileReader(file), 5 * 1024);
			String line;
			while ((line = br.readLine()) != null) {

				System.out.println(line);
				if (StringUtils.isNotEmpty(line)) {
					// ����ע��
					if (line.contains("--")) {
						continue;
					}
					if (!line.contains("truncate table"))
						num++;
					line = line.replaceAll(";", "");

					sqlMap.put(line, null);

				}

			}

		}

		catch (IOException e) {
			log.printStackTrace(e);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				log.printStackTrace(e);
			}
		}

		try {
			if (sqlMap.isEmpty()) {
				log.println("sql file has no sql to execute");
				messages.add(formFile.getFileName() + " has no sql to execute");
			}
			iGetResultSetBySQLService.execSQL(sqlMap);
		} catch (Exception e) {
			log.printStackTrace(e);
			e.printStackTrace();
			messages.add("execute sql error");
		}
		messages.add("�ɹ�����" + num + "����");
		List <BakTableInfoVo>infoList  =  new ArrayList<BakTableInfoVo>();
		infoList  = iGetResultSetBySQLService.getBakInfoList();
		request.setAttribute("infoList", infoList);
		if (messages.getSize() > 0)
			request.setAttribute("Message", messages);
		flag = "over";
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
	public ActionForward downLoadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, FileNotFoundException, IOException {
		Map<String, String> mCellMap = new HashMap<String, String>();
		Map<String, String> mCellToMap = new HashMap<String, String>();
		FitechMessages messages = new FitechMessages();
		Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        String desFile = Config.TEMP_DIR + "CELL_FORMU_BAK_" + dateString + ".txt";

		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
				new FileOutputStream(desFile));

		iGetResultSetBySQLService.bakData(mCellMap, mCellToMap);

		Set<String> mCellSet = mCellMap.keySet();
		Iterator<String> mCellIt = mCellSet.iterator();
		bufferedOutputStream
				.write("truncate table  af_validateformula_standard\n"
						.getBytes());
		bufferedOutputStream.write("truncate table  af_validateformula\n"
				.getBytes());
		while (mCellIt.hasNext()) {
			bufferedOutputStream.write(mCellIt.next().getBytes());

		}

		Set<String> mCellToSet = mCellToMap.keySet();
		Iterator<String> mCeTollIt = mCellToSet.iterator();
		while (mCeTollIt.hasNext()) {
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
				+ new String(filename.replaceAll(" ", "").getBytes("utf-8"),
						"iso8859-1"));
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
	 * @param filePath
	 *            Ŀ¼·��
	 * @return void
	 */
	public void make(String filePath) {
		File file = new File(filePath);

		if (!file.exists())
			file.mkdir();
	}

	public ActionForward getOverFlag(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (null != flag) {
			flag = null;
			return mapping.findForward("success");
		} else {
			return null;
		}

	}
	/**
	 * ���ݹ�ʽ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public ActionForward bakTable(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, FileNotFoundException, IOException {
		FitechMessages messages = new FitechMessages();
		Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        String backTableName  = "";
        String sbackTableName  = "";
        String tableName  = request.getParameter("tableName");
        String standardName = tableName +"_standard";
        if(tableName.equals("af_validateformula")){
        	 backTableName  = "av"+"_"+dateString; 
        	 sbackTableName = "avd"+"_"+dateString;
        	 iGetResultSetBySQLService.bakTable(tableName, backTableName);
        	 iGetResultSetBySQLService.bakTable(standardName, sbackTableName);
        	 iGetResultSetBySQLService.saveBakInfo(dateString,tableName,backTableName);
        }else{
        	
        }
        if (messages.getSize() > 0)
        	request.setAttribute("Message", messages);
        return mapping.findForward("success");
	}
	
	/**
	 * ��ԭ����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public ActionForward rollBakTable(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, FileNotFoundException, IOException {
		FitechMessages messages = new FitechMessages();
        String backTableName  = "";
        String sbackTableName  = "";
        String tableName  = request.getParameter("tableName");
        String bakTime  = request.getParameter("bakTime");
        String standardName = tableName +"_standard";
        boolean flag  = false;
        if(tableName.equals("af_validateformula")){
        	 backTableName  = "av"+"_"+bakTime; 
        	 sbackTableName = "avd"+"_"+bakTime;
        	 flag =  iGetResultSetBySQLService.rollBakTable(tableName, backTableName);
        	 if(flag){
        		 flag = iGetResultSetBySQLService.rollBakTable(standardName, sbackTableName);
        	 } else{
        		 messages.add("�ָ�����ʧ��");
        	 	 return mapping.findForward("success");
        	}	
        	if(flag)
        		 messages.add("�ָ����ݳɹ�");
        	else
        		 messages.add("�ָ�����ʧ��");
        }else{
        	
        }
        List <BakTableInfoVo>infoList  =  new ArrayList<BakTableInfoVo>();
		infoList  = iGetResultSetBySQLService.getBakInfoList();
		request.setAttribute("infoList", infoList);
		if (messages.getSize() > 0)
			request.setAttribute("Message", messages);
        return mapping.findForward("success");
	}
	
	public ActionForward bakList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, FileNotFoundException, IOException {
		List <BakTableInfoVo>infoList  =  new ArrayList<BakTableInfoVo>();
		infoList  = iGetResultSetBySQLService.getBakInfoList();
		request.setAttribute("infoList", infoList);
		return mapping.findForward("infoList");
	}
	
	public ActionForward deleteBak(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, FileNotFoundException, IOException {
		String bakTime  = request.getParameter("bakTime");
		String tableName  = request.getParameter("tableName");
		iGetResultSetBySQLService.deleteBak(bakTime ,tableName);
		List <BakTableInfoVo>infoList  =  new ArrayList<BakTableInfoVo>();
		infoList  = iGetResultSetBySQLService.getBakInfoList();
		request.setAttribute("infoList", infoList);
		return mapping.findForward("infoList");
	}
	
}
