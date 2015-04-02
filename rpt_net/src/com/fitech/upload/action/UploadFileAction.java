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
 * 单文件上传
 * @author wangxuewu
 *
 */
public class UploadFileAction extends DispatchAction{
	/**
	 * 允许上传的文件类型
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
			//创建临时上传目录
			String tempCatalog = Config.WEBROOTPATH+"tempUpload"+File.separator+operator.getOperatorId();
			
			File file = new File(tempCatalog);
			
			if(!file.exists())
				file.mkdirs();
			//上传成功后判断此文件是否是压缩文件，如果是压缩文件则将它解压在当前目录
			
			String fileName = ufForm.getFormFile().getFileName();
			
			boolean uploadResultl = uploadFile(ufForm.getFormFile().getInputStream(),tempCatalog+File.separator+fileName);
			
			if(uploadResultl){
				messages.add("上传["+fileName+"]成功!");
				
				if(fileName.indexOf("zip")>-1){
					boolean unZipResult = unzipFile(tempCatalog,tempCatalog+File.separator+fileName);
					if(unZipResult){
						messages.add("解压["+fileName+"]成功!");
					}
					else
						messages.add("解压["+fileName+"]失败!");
				}
				
				ReadFormulaFileDelegate rd = new ReadFormulaFileDelegate();
				rd.setTempCatalog(tempCatalog);
				//将文件内容读取到集合中
				rd.readFormulaFile(file);
				
				ConverFormulaDelegate cfd = new ConverFormulaDelegate();
				cfd.setRfd(rd);
				//验证指标编号是否存在，并转换成程序支持的校验公式格式，保存到临时表中
				cfd.validationFormula();
				//对比现有库中的公式和临时表中的公式，将现有库中没有的校验公式保存入库
				boolean saveResult = cfd.compareFormula();
				
				if(saveResult)
					messages.add("更新["+fileName+"]成功!");
				else
					messages.add("更新["+fileName+"]失败!");
				
				rd.distory();
			}else{
				messages.add("上传["+fileName+"]失败!");
			}
		}else{
			messages.add("不支持上传["+ufForm.getFormFile().getFileName()+"]此类型的文件!");
		}
		if(messages.getSize()>0)
			request.setAttribute(Config.MESSAGES, messages);
		return mapping.findForward("success");
	}
	/**
	 * 上传文件
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
	 * 解压文件在当前目录
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
