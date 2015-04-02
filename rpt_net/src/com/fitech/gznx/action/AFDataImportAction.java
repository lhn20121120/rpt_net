package com.fitech.gznx.action;

import java.io.File;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.form.AFDataImporForm;
import com.fitech.gznx.service.AFDataImportDelegate;
import com.gather.common.ZipUtils;

/**
 * 人行历史报文载入
 * @author rpt_net
 *
 */
public class AFDataImportAction extends Action {
	private static FitechException log = new FitechException(AFDataImportAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		FitechMessages messages = new FitechMessages();
		File zipTempDir = null;
		File idxFile = null;
		File datFile = null;
		try{
			 Operator operator = (Operator)request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			AFDataImporForm importForm = (AFDataImporForm)form;
			importForm.getZipFile();
			String zipBasePath = Config.TEMP_DIR + "\\pbocImport";
			File zipBase = new File(zipBasePath);
			if(!zipBase.exists())
				zipBase.mkdir();
			String zipFilePath = zipBasePath + "\\" + DateUtil.getTodayDetil() + "_" + operator.getOperatorId().intValue() + ".zip";
			File zipFile = new File(zipFilePath);
			if(zipFile.exists())
				zipFile.delete();
			FitechUtil.copyFile(importForm.getZipFile().getInputStream(), zipFilePath);
			ZipUtils zu = new ZipUtils();
			String zipTempDirPath = zipBasePath + "\\" + DateUtil.getTodayDetil() + "_" + operator.getOperatorId().intValue();
			zipTempDir = new File(zipTempDirPath);
			zu.expandFile(zipFile,zipTempDir);
			File[] fs = zipTempDir.listFiles();
			for(int i=0;i<fs.length;i++){
				if(fs[i].getName().toUpperCase().endsWith("IDX"))
					idxFile = fs[i];
				if(fs[i].getName().toUpperCase().endsWith("DAT"))
					datFile = fs[i];
			}
			if(!AFDataImportDelegate.dataRHImport(idxFile,datFile))
				throw new Exception ("dataimport error");
		}catch(Exception e){
			log.printStackTrace(e);
			messages.add("载入失败！");
		}finally{
			
			if(zipTempDir!=null && zipTempDir.exists()){
				if(idxFile!=null && idxFile.exists())
					idxFile.delete();
				if(datFile!=null && datFile.exists())
					datFile.delete();
				zipTempDir.delete();
			}
		}
		messages.add("载入成功！");
		request.setAttribute(Config.MESSAGES,messages);
		
	//	File idxFile = new File(zipBasePath + );
//		File datFile = new File("E:\\综合报送平台\\重庆农商行\\AJ6p01500000020140531411.DAT");
//		File idxFile = new File("E:\\综合报送平台\\重庆农商行\\AI6p01500000020140531411.IDX");
//		File datFile = new File("E:\\综合报送平台\\重庆农商行\\AJ6p01500000020140531411.DAT");
//		String idxFile = "c:\\" + importForm.getIdxFile().getFileName();
//		String datFile = "c:\\" + importForm.getDatFile().getFileName();
//		FitechUtil.copyFile(importForm.getIdxFile().getInputStream(), idxFile);
//		FitechUtil.copyFile(importForm.getDatFile().getInputStream(), datFile);
//		AFDataImportDelegate.dataRHImport(idxFile,datFile);
		return  mapping.findForward("pboc_data_import.jsp");
		
	}

}
