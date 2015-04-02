package com.fitech.net.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.DownLoadDataToZip;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.form.UploadFileForm;
import com.cbrc.smis.hibernate.MActuRep;
import com.cbrc.smis.hibernate.MChildReport;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.MDataRgType;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.ReportExcelHandler;
import com.fitech.net.Excel2Xml.ParseExcelUtil;
import com.fitech.net.Excel2Xml.Util;
import com.fitech.net.config.Config;

/**
 * ���������ϱ�
 * @author jcm
 * @2007-12-18
 */
public final class UploadMoreFileAction extends Action {

	private static FitechException log = new FitechException(UploadMoreFileAction.class);
	/*�ϴ��ļ���ʱĿ¼*/
	private final static String SERVICE_UP_TEMP = Config.REAL_ROOT_PATH + Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP;
	/*�ϴ��ļ���ѹ�����ʱĿ¼*/
	private final static String SERVICE_UP_RELEASE = SERVICE_UP_TEMP + Config.SERVICE_UP_RELEASE;
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
      
		String year = null;
		String term = null;
		String curPage = "1";
		String orgId = null;
		List aditingList = new ArrayList();
		ApartPage aPage = new ApartPage();
		String notshow = null;  //����ҳ���Ƿ���ʾ��ť
		String url = "/viewDataReport.do";  //��תҳ��URL
		
		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);		
		Operator operator=(Operator)request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		
		UploadFileForm upFileForm=(UploadFileForm)form;
		RequestUtils.populate(upFileForm, request);
		
		FormFile formFile = upFileForm.getFormFile();
		String versionParam = upFileForm.getVersionId();		
		if(formFile == null || versionParam == null || versionParam.equals("")){
			messages.add(resources.getMessage("select.upReport.failed"));
			if(messages.getMessages() != null && messages.getMessages().size() > 0)		
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
			return new ActionForward(url);
		}
		
		try{
			String[] param = versionParam.split("_");
			year = param[0].trim();
			term = param[1].trim();
			orgId = param[2].trim();
			curPage = param[3].trim();
			try{
				aPage.setCurPage(Integer.parseInt(curPage));
			}catch(Exception ex){
				aPage.setCurPage(1);
			}
		}catch(Exception ex){
			year = null;
			term = null;			
			orgId = null;
		}finally{
			if(year == null || year.equals("") || term == null || term.equals("")){
				messages.add(resources.getMessage("select.upReport.failed"));		
				if(messages.getMessages() != null && messages.getMessages().size() > 0)		
					request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
				return new ActionForward(url + "?curPage=" + curPage);
			}
			if(orgId == null || orgId.equals("")){
				messages.add(resources.getMessage("select.upReport.failed"));		
				if(messages.getMessages() != null && messages.getMessages().size() > 0)		
					request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
				return new ActionForward(url + "?year=" + year + "&term=" + term + "&curPage=" + curPage);
			}
		}			
		url += "?year=" + year + "&term=" + term + "&curPage=" + curPage + "&orgId" + orgId;
		
		try{
			/**�ж��ϴ�ZIP�ļ���С*/
			if(formFile.getFileSize() > 1024*1024){
				messages.add("�ϴ��ļ���С���ܳ���1M��");
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
				return new ActionForward(url);
			}
			
			/**������ʱ�ļ���*/
			this.make(SERVICE_UP_TEMP);     
			this.make(SERVICE_UP_RELEASE); 
			this.make(SERVICE_UP_RELEASE + orgId);			
			
			/**�ļ��ϴ�*/
			String path = SERVICE_UP_TEMP + orgId;
			String zipFileName = path + File.separator + formFile.getFileName();
			File pathDirectory = new File(path);
			if(!pathDirectory.exists()) pathDirectory.mkdir();
			List zipRelFileList = new ArrayList();  //ZIP����ѹ����ļ��б�
			File excelFile = null;
			MChildReport mChildReport = null;
			MDataRgType dataRange = null;
			String dataRangeDes = null;
			ReportInForm reportInForm = null;
			ReportIn reportInTemp = null;
			MCurr mCurr = null;
			String curId = null;	
			String childRepId = null;
			String versionId = null;
					
			InputStream  inStream=formFile.getInputStream();      
			OutputStream outStream=new FileOutputStream(zipFileName);       
			byte[] bytes=new byte[1024];        
			int index=0;       
			while((index=inStream.read(bytes))!=-1){ 	  
				outStream.write(bytes,0,index);
			}
			if(inStream != null) inStream.close();
			if(outStream != null) outStream.close();
			
			/**
			 * �����ϴ���ZIP�ļ�
			 * �Ƚ�ѹ���ٶԵ���Excel������
			 */			
			if(!release(formFile.getFileName(),path,SERVICE_UP_RELEASE + orgId,zipRelFileList)){				
				messages.add("ZIP�ļ���ѹʧ�ܣ�");
				
				request.setAttribute("term",term);
				request.setAttribute("year",year);		
				request.setAttribute("orgId",orgId);
				request.setAttribute("RequestParam", url);
				request.setAttribute("curPage", curPage);
				request.setAttribute(com.cbrc.smis.common.Config.APART_PAGE_OBJECT,aPage);
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
				return mapping.findForward("view");								
			}
			
			if(zipRelFileList != null && zipRelFileList.size() > 0){				
				for(Iterator iter=zipRelFileList.iterator();iter.hasNext();){			
					excelFile = (File)iter.next();
					//��֤Excel�ļ��Ƿ�����Ч���ļ�
                    mChildReport = getExcelReportISTrue(excelFile,year,term);
					if(mChildReport == null){
						messages.add(excelFile.getName()+"����ʧ�ܣ�");
						continue;
					}
					
					childRepId = mChildReport.getComp_id().getChildRepId();
					versionId = mChildReport.getComp_id().getVersionId();
					// �����������
					mChildReport.setReportName(new StrutsMChildReportDelegate().getMChildReport(childRepId, versionId)
							.getReportName());
					/**��ñ���ı��֣�Ŀǰ��֧�ֶ���ֵ��������ͣ�*/
					if (curId == null || curId.equals(""))
						curId = "1";
					mCurr= StrutsMCurrDelegate.getISMCurr(curId);
					
					/**�жϱ���ı��Ϳھ��Ƿ���ȷ*/
					if(mChildReport.getTemplateType().equals(com.fitech.net.config.Config.FZ_TEMPLATE.toString())){
						dataRangeDes = "���ڻ�������";
						dataRange = StrutsMDataRgTypeDelegate.selectDataRange(dataRangeDes);
					}else{
						dataRangeDes = Util.getDataRangeDes(excelFile.getPath());
						dataRange = StrutsMDataRgTypeDelegate.selectDataRange(dataRangeDes.trim());						
					}					
					if(dataRange == null){
						messages.add(excelFile.getName()+"���Ϳھ��������");
						continue;
					}
					
					/**�ж��Ƿ��иñ���ı��ͷ�Χ*/
					if(StrutsMRepRangeDelegate.getMRepRanageOncb(orgId,childRepId,versionId) == null){
						messages.add(excelFile.getName()+"���ͷ�Χ�����ڣ�");
						continue;
					}
					
					/**�ж��û��Ƿ��иñ���ı���Ȩ��*/
					if(operator.isSuperManager() == false){
						if(operator.getChildRepReportPopedom() == null || operator.getChildRepReportPopedom().equals("")){
							messages.add(excelFile.getName()+"ȱ�ٱ���Ȩ�ޣ�");							
							continue;
						}						
						if(StrutsMRepRangeDelegate.getMRepRanage(orgId,childRepId,versionId,operator) == null){
							messages.add(excelFile.getName()+"ȱ�ٱ���Ȩ�ޣ�");
							continue;
						}
					}
					
					/**�жϱ��Ϳھ��Ƿ���ȷ*/
					if(StrutsReportInDelegate.isExistDataRange(dataRange.getDataRangeId().toString(),childRepId,versionId) == false){
						messages.add(excelFile.getName()+"���Ϳھ�����");
						continue;
					}
					
					/**�жϱ���Ƶ���Ƿ����*/
					if(StrutsReportInDelegate.isExistDataRange(dataRange.getDataRangeId(),childRepId
							,versionId,new Integer(term)) == false){
						messages.add(excelFile.getName()+term+"�·ݲ���Ҫ���ͣ�");
						continue;
					}
					
					/**�жϱ����Ƿ��Ѿ����ͣ����ѱ��������ظ����ͣ�*/
					reportInTemp = StrutsReportInDelegate.getReportIn(childRepId,versionId,orgId,new Integer(year)
							,new Integer(term),dataRange.getDataRangeId(),mCurr.getCurId(),new Integer(1));
					if(reportInTemp != null && (reportInTemp.getCheckFlag().shortValue() == Config.CHECK_FLAG_PASS.shortValue() 
							|| reportInTemp.getCheckFlag().shortValue() == Config.CHECK_FLAG_UNCHECK.shortValue())){
						messages.add(excelFile.getName()+"�Ѿ����Ͳ������ظ����ͣ�");
						continue;
					}
					
					boolean result = false;
					try{
						Integer repInId = null;
						//Ԥ�Ȳ����¼�¼
						reportInForm = new ReportInForm();
						reportInForm.setChildRepId(childRepId);
						reportInForm.setVersionId(versionId);
						reportInForm.setDataRangeId(dataRange.getDataRangeId());
						reportInForm.setCurId(new Integer(curId));
						reportInForm.setYear(new Integer(year));
						reportInForm.setTerm(new Integer(term));
						reportInForm.setOrgId(orgId);
						reportInForm.setTimes(new Integer(1));
						reportInForm.setReportDate(new Date());
						reportInForm.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_UNREPORT);
						reportInForm.setCurName(mCurr.getCurName());
						reportInForm.setRepName(mChildReport.getReportName());
						// ��������
						StrutsReportInDelegate strutsReportInDelegate=new StrutsReportInDelegate();
						ReportIn reportIn = strutsReportInDelegate.insertNewReport(reportInForm);
						if (reportIn != null)
							repInId = reportIn.getRepInId();
													
						ReportExcelHandler excelHandler = new ReportExcelHandler(repInId, excelFile.getPath());
						result = excelHandler.copyExcelToDB(false);
						if (result)
							result = strutsReportInDelegate.updateReportInCheckFlag_e(repInId, Config.CHECK_FLAG_AFTERSAVE);
						
						if (result){
							notshow = "not null";
							Aditing aditing = new Aditing();
							aditing.setRepInId(repInId);
							aditing.setRepName(reportInForm.getRepName());
							aditing.setChildRepId(reportInForm.getChildRepId());
							aditing.setVersionId(reportInForm.getVersionId());
							aditing.setYear(reportInForm.getYear());
							aditing.setTerm(reportInForm.getTerm());
							aditing.setOrgId(reportInForm.getOrgId());
							aditing.setCurId(reportInForm.getCurId());
							aditing.setCurrName(reportInForm.getCurName());
							aditing.setDataRangeId(reportInForm.getDataRangeId());
							aditing.setDataRgTypeName(dataRange.getDataRgDesc());
							aditing.setCheckFlag(reportInForm.getCheckFlag());
							MActuRep mActuRep = StrutsReportInDelegate.GetFreR(reportIn);
							if(mActuRep != null) aditing.setActuFreqName(mActuRep.getMRepFreq().getRepFreqName());
							aditingList.add(aditing);							
						}						
					}catch (Exception e){
						messages.add(excelFile.getName()+"���ʧ�ܣ�");
						e.printStackTrace();
					}
				}				
				//���������ɾ����ѹ����ļ�				
				File zipReleaseFolder = new File(SERVICE_UP_RELEASE + orgId);
				DownLoadDataToZip dldt = DownLoadDataToZip.newInstance();
				if(zipReleaseFolder != null && zipReleaseFolder.listFiles() != null) dldt.deleteFolder(zipReleaseFolder);
			}							
		}catch(Exception ex){
			ex.printStackTrace();
			log.printStackTrace(ex);			
		}
		     
		if(aditingList!=null && aditingList.size()>0)
			request.setAttribute(com.cbrc.smis.common.Config.RECORDS,aditingList);
		if(notshow!=null)
			request.setAttribute(Config.NOT_SHOW,notshow); 	 
		if(messages.getMessages() != null && messages.getMessages().size() > 0)		
			request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
		
		request.setAttribute("term",term);
		request.setAttribute("year",year);		
		request.setAttribute("orgId",orgId);
		request.setAttribute("RequestParam", url);
		request.setAttribute("curPage", curPage);
		request.setAttribute(com.cbrc.smis.common.Config.APART_PAGE_OBJECT,aPage);
		
		return mapping.findForward("view");
	}
	
	/**
	 * ��ѹZIP��
	 * 
	 * @param zipFileName ZIP������
	 * @param fPath ZIP�������ļ�Ŀ¼
	 * @param tPath ��Ҫ��ѹ���ļ�Ŀ¼
	 * @param fileList ��ѹ����ļ��б�
	 * @return boolean true-��ѹ�ɹ� false-��ѹʧ��
	 */
	private boolean release(String zipFileName,String fPath,String tPath,List fileList){
		boolean result = false;		
		File file = null;
		InputStream inStream = null;
		OutputStream outStream = null;
		if(zipFileName == null || zipFileName.equals("") 
				|| fPath == null || fPath.equals("")
				|| tPath == null || tPath.equals("")) return result;
		
		try{
			if(fileList == null) fileList = new ArrayList();
			ZipFile zipFile = new ZipFile(fPath + File.separator + zipFileName);		
			Enumeration enu = zipFile.getEntries();			
			while(enu.hasMoreElements()){
				ZipEntry entry = (ZipEntry)enu.nextElement();				
				inStream = zipFile.getInputStream(entry);
				file = new File(tPath + File.separator + entry.getName());
				outStream = new FileOutputStream(file);
				byte[] bytes = new byte[1024];
		          int index=0;
		          while((index=inStream.read(bytes)) != -1)
		        	  outStream.write(bytes,0,index);
		          
		          if(inStream != null) inStream.close();
		          if(outStream != null) outStream.close();
		          
		          fileList.add(file);
			}			
			zipFile.close();
			result = true;			
		} catch (FileNotFoundException e) {
			result = false;
			log.printStackTrace(e);
			e.printStackTrace();
		} catch (IOException e) {
			result = false;
			log.printStackTrace(e);
			e.printStackTrace();
		}
		
		return result;
	}
	
    /**
     * �����ļ�Ŀ¼
     * 
     * @param filePath Ŀ¼·��
     * @return void
     */
    public void make(String filePath){
    	File file=new File(filePath);    	
    	
    	if(!file.exists()) file.mkdir();
    }
    
    /**
     * �鿴ģ�������Ƿ���ȷ
     * 
     * @param excelFile Excel�ļ�
     * @param year �������
     * @param term ��������
     * @return �ӱ������
     */
    private MChildReport getExcelReportISTrue(File excelFile, String year,String term){
		MChildReport mChildReport = null;
		String title = null;
		String subTitle = null;		

		try{
            ParseExcelUtil parse = new ParseExcelUtil();
            title = parse.getTitle(excelFile.getPath());
            subTitle = parse.getSubtitle(excelFile.getPath());
			Integer templateType = getFZORFRTemplate(title);
			if(title != null) title = title.trim();
			if(subTitle != null) subTitle = subTitle.trim();
			
			//����ӱ������
            mChildReport = StrutsMChildReportDelegate.findByTitle(title,subTitle,year,term);
			if(mChildReport != null) 
				mChildReport.setTemplateType(String.valueOf(templateType));
		}catch (Exception ex){
			mChildReport = null;
			log.printStackTrace(ex);
			ex.printStackTrace();
		}
		return mChildReport;		
	}
	
	/**
	 * ����ģ�����ƻ��ģ�����ͣ�����/��֧ģ�壩
	 * 
	 * @param title ģ������
	 * @return Integer ģ������
	 */
	private Integer getFZORFRTemplate(String title){
		Integer templateType = com.fitech.net.config.Config.FR_TEMPLATE;
		if (title != null && !title.equals("") && title.length() > 2){
			String titleStr = title.trim().substring(0, 2);
			if (titleStr.toUpperCase().indexOf(com.fitech.net.config.Config.FZ_SF_TAMPLATE) > -1
					|| titleStr.toUpperCase().indexOf(com.fitech.net.config.Config.FZ_GF_TAMPLATE) > -1)
				templateType = com.fitech.net.config.Config.FZ_TEMPLATE;
		}
		return templateType;
	}
}