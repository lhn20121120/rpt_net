package com.fitech.institution.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.ZipUtils;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.FileUtil;
import com.fitech.institution.adapter.ConverFormulaDelegate;
import com.fitech.institution.adapter.ReadFormulaFileDelegate;
import com.fitech.institution.form.UploadInfoForm;
import com.fitech.institution.service.ExecuteSqlService;
import com.fitech.institution.service.impl.ExecuteSqlServiceImpl;

public class UploadFileAction extends DispatchAction {
	private List<String> allowUploadFileType = new ArrayList<String>();
	private ExecuteSqlService executeSqlServiceImpl = new ExecuteSqlServiceImpl();

	// private static FitechException log = new FitechException(
	// UploadFileAction.class);
	public UploadFileAction() {
		this.allowUploadFileType.add("application/x-zip-compressed");
		this.allowUploadFileType.add("text/plain");
		this.allowUploadFileType.add("application/octet-stream");
	}

	public ActionForward execUpload(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		FitechMessages messages = new FitechMessages();
		UploadInfoForm ufForm = (UploadInfoForm) form;
//		Connection conn = null;
		if (ufForm.getFormFile() == null)
			return null;
		Operator operator = null;

		if (request.getSession().getAttribute("Operator") != null)
			operator = (Operator) request.getSession().getAttribute("Operator");
		else {
			messages.add("���ȵ�¼��");
			request.setAttribute("Message", messages);
			return mapping.findForward("success");
		}
		if ((this.allowUploadFileType.contains(ufForm.getFormFile()
				.getContentType()))
				&& (ufForm.getFormFile().getFileName().indexOf("rar") == -1)) {
			String tempCatalog = Config.WEBROOTPATH + "tempUpload"
					+ File.separator + operator.getOperatorId();
			File file = new File(tempCatalog);
			if (!(file.exists())) {
				file.mkdirs();
			}
			String fileName = ufForm.getFormFile().getFileName();
			String zipFilePath = tempCatalog + File.separator + fileName;
			boolean uploadResultl = uploadFile(ufForm.getFormFile()
					.getInputStream(), zipFilePath);
			String operation = "";
			if (uploadResultl) {
				messages.add("�ϴ�[" + fileName + "]�ɹ�!");
				File[] files = null;
				if (fileName.indexOf("zip") > -1) {

					files = unzipFile(tempCatalog, tempCatalog + File.separator
							+ fileName, files);
					if (files == null) {
						messages.add("��ѹ[" + fileName + "]ʧ�ܣ���ȷ��zip���Ƿ���ȷ!");
						request.setAttribute("Message", messages);
						return mapping.findForward("success");
					} else
						messages.add("��ѹ[" + fileName + "]�ɹ�!");
				}

				// ��������
				System.out.println("��ʼ���ݹ�ʽ");
				Date currentTime = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyyMMddHHmmss");
				String dateString = formatter.format(currentTime);
				String backTableName = "";
				String sbackTableName = "";
				String tableName = "af_validateformula";
				String standardName = tableName + "_standard";
				backTableName = "av" + "_" + dateString;
				sbackTableName = "avd" + "_" + dateString;
				if (executeSqlServiceImpl
						.bakTable(tableName, backTableName)
						&& executeSqlServiceImpl.bakTable(standardName,
								sbackTableName)
						&& executeSqlServiceImpl.saveBakInfo(dateString,
								tableName, backTableName)) {
					messages.add("����У�鹫ʽ�ɹ���");
					ReadFormulaFileDelegate readFormulaFileDelegate = new ReadFormulaFileDelegate();
					Map<String, List<String>> formulaMap = readFormulaFileDelegate
							.readFormulaFile(files);
					List<String> resultList = ConverFormulaDelegate
							.validationFormula(formulaMap);
					if (resultList != null && !resultList.isEmpty()) {
						StringBuffer sb = new StringBuffer();
						for (Iterator<String> iterator = resultList.iterator(); iterator
								.hasNext();) {
							String string = (String) iterator.next();
							sb.append(string + ",");
							messages.add(string);
						}
						log.info(sb.toString());
						operation = "���������ƶȰ������ɹ�!��" + sb.toString();
					} else {
						messages.add("���³ɹ���");
						operation = "���������ƶȰ������ɹ�!";
					}
					if (FileUtil.deleteFile(tempCatalog)) {
						messages.add(fileName + "ɾ���ɹ���");
					}
					/*try {
						conn = (new com.cbrc.smis.proc.jdbc.FitechConnection())
								.getConnect();// ��ȡ���ݿ�����
						LogInImpl.writeInstitutionLog(conn, operation,
								operator.getUserName());// д��ϵͳ��־
					} catch (Exception e) {
						log.error(e);
					} finally {
						if (conn != null) {
							conn.close();
							conn = null;
						}
					}*/
				} else {
					messages.add("���ݹ�ʽʧ��!����ϵ����Ա");
				}
			} else {
				messages.add("�ϴ�[" + fileName + "]ʧ��!");
			}
		} else {
			messages.add("��֧���ϴ�[" + ufForm.getFormFile().getFileName()
					+ "]�����͵��ļ�!");
		}
		if (messages.getSize() > 0)
			request.setAttribute("Message", messages);
		return mapping.findForward("success");
	}

	public boolean uploadFile(InputStream stream, String tempCatalogPath) {
		boolean result = false;
		BufferedInputStream buffInputSream = null;
		BufferedOutputStream buffOutputStream = null;
		try {
			buffInputSream = new BufferedInputStream(stream);
			buffOutputStream = new BufferedOutputStream(new FileOutputStream(
					tempCatalogPath));
			byte[] bytes = new byte[1024];
			int readIndex = 0;
			while ((readIndex = buffInputSream.read(bytes)) != -1)
				buffOutputStream.write(bytes, 0, readIndex);

			buffOutputStream.flush();
			File keyFlie = new File(tempCatalogPath);
			addkeyWord(keyFlie, keyFlie);// ����
			result = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			result = false;
			try {
				if (buffInputSream != null)
					buffInputSream.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			try {
				if (buffOutputStream != null)
					buffOutputStream.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			try {
				if (buffInputSream != null)
					buffInputSream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (buffOutputStream != null)
					buffOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (stream != null)
					stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public File[] unzipFile(String unZipCataloog, String unzipFilePath,
			File[] files) {
		try {
			ZipUtils util = new ZipUtils();
			files = util.expandFile(new File(unzipFilePath), new File(
					unZipCataloog));
		} catch (Exception e) {
			System.out.println("zip��ʽ����ȷ ����ѹʧ�ܣ�");
			e.printStackTrace();
			files = null;
		}
		return files;
	}

	private static File addkeyWord(File infile, File outfile) {
		FileOutputStream fo  = null;
		FileInputStream fs = null; 
		try {
			 fs = new FileInputStream(infile);
			int length = (int) infile.length();
			byte[] buffer = new byte[length];
			fs.read(buffer);
			
			byte[] buffer2 = new byte[length];
			int m = length;
			for (int i = 0; i < length; i++) {
				buffer2[i] = buffer[m - 1];
				m--;
			}
			fo = new FileOutputStream(outfile);
			fo.write(buffer2, 0, length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(fs != null)
					fs.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(fo != null)
				fo.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("������ɣ�");
		return outfile;
	}


}