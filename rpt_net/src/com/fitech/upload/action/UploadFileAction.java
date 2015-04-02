package com.fitech.upload.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import com.fitech.fomula.service.ConverFormulaDelegate;
import com.fitech.fomula.service.ReadFormulaFileDelegate;
import com.fitech.upload.form.UploadInfoForm;

/**
 * 2014-06-05
 * ���ļ��ϴ�
 * @author wangxuewu
 *
 */
public class UploadFileAction extends DispatchAction{
	/**
	 * �����ϴ����ļ�����
	 */
	private List<String> allowUploadFileType = new ArrayList<String>();
	
	public UploadFileAction(){
		allowUploadFileType.add("application/x-zip-compressed");
		allowUploadFileType.add("text/plain");
		allowUploadFileType.add("application/octet-stream");
	}
	
	public ActionForward execUpload(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		FitechMessages messages = new FitechMessages();
		
		UploadInfoForm ufForm = (UploadInfoForm)form;
		
		if(ufForm.getFormFile()==null)
			return null;
		
		Operator operator = null;
		
		if (request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
			operator = (Operator) request.getSession()
					.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		
		if(operator==null)
			return null;
		
		if(allowUploadFileType.contains(ufForm.getFormFile().getContentType()) &&
				ufForm.getFormFile().getFileName().indexOf("rar")==-1){
			//������ʱ�ϴ�Ŀ¼
			String tempCatalog = Config.WEBROOTPATH+"tempUpload"+File.separator+operator.getOperatorId();
			
			File file = new File(tempCatalog);
			
			if(!file.exists())
				file.mkdirs();
			//�ϴ��ɹ����жϴ��ļ��Ƿ���ѹ���ļ��������ѹ���ļ�������ѹ�ڵ�ǰĿ¼
			
			String fileName = ufForm.getFormFile().getFileName();
			
			boolean uploadResultl = uploadFile(ufForm.getFormFile().getInputStream(),tempCatalog+File.separator+fileName);
			
			if(uploadResultl){
				messages.add("�ϴ�["+fileName+"]�ɹ�!");
				
				if(fileName.indexOf("zip")>-1){
					boolean unZipResult = unzipFile(tempCatalog,tempCatalog+File.separator+fileName);
					if(unZipResult){
						messages.add("��ѹ["+fileName+"]�ɹ�!");
					}
					else
						messages.add("��ѹ["+fileName+"]ʧ��!");
				}
				
				ReadFormulaFileDelegate rd = new ReadFormulaFileDelegate();
				rd.setTempCatalog(tempCatalog);
				//���ļ����ݶ�ȡ��������
				rd.readFormulaFile(file);
				
				ConverFormulaDelegate cfd = new ConverFormulaDelegate();
				cfd.setRfd(rd);
				//��ָ֤�����Ƿ���ڣ���ת���ɳ���֧�ֵ�У�鹫ʽ��ʽ�����浽��ʱ����
				cfd.validationFormula();
				//�Ա����п��еĹ�ʽ����ʱ���еĹ�ʽ�������п���û�е�У�鹫ʽ�������
				boolean saveResult = cfd.compareFormula();
				
				if(saveResult)
					messages.add("����["+fileName+"]�ɹ�!");
				else
					messages.add("����["+fileName+"]ʧ��!");
				
				rd.distory();
			}else{
				messages.add("�ϴ�["+fileName+"]ʧ��!");
			}
		}else{
			messages.add("��֧���ϴ�["+ufForm.getFormFile().getFileName()+"]�����͵��ļ�!");
		}
		if(messages.getSize()>0)
			request.setAttribute(Config.MESSAGES, messages);
		return mapping.findForward("success");
	}
	/**
	 * �ϴ��ļ�
	 * @param stream
	 * @param tempCatalogPath
	 */
	public boolean uploadFile(InputStream stream,String tempCatalogPath){
		boolean result = false;
		BufferedInputStream buffInputSream = null;
		BufferedOutputStream buffOutputStream = null; 
		try {
			buffInputSream = new BufferedInputStream(stream);
			buffOutputStream = new BufferedOutputStream(new FileOutputStream(tempCatalogPath));
			byte[] bytes = new byte[1024];
			int readIndex = 0;
			while((readIndex = buffInputSream.read(bytes))!=-1){
				buffOutputStream.write(bytes, 0, readIndex);
			}
			buffOutputStream.flush();
			result = true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		} catch (Exception e){
			e.printStackTrace();
			result = false;
		}finally{
			try {
				if(buffInputSream!=null)
					buffInputSream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(buffOutputStream!=null)
					buffOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * ��ѹ�ļ��ڵ�ǰĿ¼
	 * @param unzipFilePath
	 */
	public boolean unzipFile(String unZipCataloog,String unzipFilePath){
		boolean result = false;
		try {
			ZipUtils util = new ZipUtils();
			util.expandFile(new File(unzipFilePath), new File(unZipCataloog));
			result = true ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	
}
