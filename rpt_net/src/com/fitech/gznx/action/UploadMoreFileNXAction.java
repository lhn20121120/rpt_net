package com.fitech.gznx.action;

import java.io.File;
import java.io.FileInputStream;
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
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.DownLoadDataToZip;
import com.cbrc.smis.form.MActuRepForm;
import com.cbrc.smis.form.UploadFileForm;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.ReportExcelHandler;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.excel.NXReportExcelHandler;
import com.fitech.gznx.excel.QDReportExcelHandler;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.fitech.gznx.service.QDDataDelegate;
import com.fitech.gznx.service.StrutsAFActuRepDelegate;
import com.fitech.net.Excel2Xml.ParseExcelUtil;
import com.fitech.net.config.Config;

/**
 * ���������ϱ�
 * @author jcm
 * @2007-12-18
 */
public final class UploadMoreFileNXAction extends Action {

	private static FitechException log = new FitechException(UploadMoreFileNXAction.class);
	/*�ϴ��ļ���ʱĿ¼*/
	private final static String SERVICE_UP_TEMP = Config.REAL_ROOT_PATH + Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP;
	/*�ϴ��ļ���ѹ�����ʱĿ¼*/
	private final static String SERVICE_UP_RELEASE = SERVICE_UP_TEMP + Config.SERVICE_UP_RELEASE;
	
	ParseExcelUtil parse = new ParseExcelUtil();
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
      
		String year = null;
		String term = null;
		String day	= null;
		String date = null;
		String orgName = null;
		String repFreqId = null;
		String curPage = "1";
		String orgId = null;
		List aditingList = new ArrayList();
		ApartPage aPage = new ApartPage();
		String notshow = null;  //����ҳ���Ƿ���ʾ��ť
		String url = "/viewNXDataReport.do";  //��תҳ��URL
		
		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);		
		Operator operator=(Operator)request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		
		UploadFileForm upFileForm = (UploadFileForm) form;
		RequestUtils.populate(upFileForm, request);
		
		FormFile formFile = upFileForm.getFormFile();
		String versionParam = upFileForm.getVersionId();
		
	//	System.out.println(versionParam);
		if(formFile == null || versionParam == null || versionParam.equals("")){
			messages.add(resources.getMessage("select.upReport.failed"));
			if(messages.getMessages() != null && messages.getMessages().size() > 0)		
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
			return new ActionForward(url);
		}
		
		try{
			String[] param = versionParam.split("_");
			date = param[0].trim();
			year = DateUtil.getYear(date);
			term = DateUtil.getMonth(date);
			day = DateUtil.getDay(date);
			orgName = param[1].trim();
			orgId = AFOrgDelegate.getIdByName(orgName);
			curPage = param[2].trim();
			try{
				aPage.setCurPage(Integer.parseInt(curPage));
			}catch(Exception ex){
				aPage.setCurPage(1);
			}
		}catch(Exception ex){
			//year = null;
			//term = null;			
			date = null;
			orgId = null;
		}finally{
			if(date == null || date.equals("")){
				messages.add(resources.getMessage("select.upReport.failed"));		
				if(messages.getMessages() != null && messages.getMessages().size() > 0)		
					request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
				return new ActionForward(url + "?curPage=" + curPage);
			}
			if(orgId == null || orgId.equals("")){
				messages.add(resources.getMessage("select.upReport.failed"));		
				if(messages.getMessages() != null && messages.getMessages().size() > 0)		
					request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
				return new ActionForward(url + "?date=" + date + "&curPage=" + curPage);
			}
		}
		url += "?&date=" + date + "&curPage=" + curPage + "&orgId=" + orgId;
		
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
			AfTemplate mChildReport = null;
			
			//MDataRgType dataRange = null;
			String dataRangeDes = null;
			
			AFReportForm reportInForm = null;
			AfReport reportInTemp = null;
			MCurr mCurr = null;
			String curId = null;
			String childRepId = null;
			String versionId = null;
			
			InputStream  inStream=formFile.getInputStream();      
			OutputStream outStream=new FileOutputStream(zipFileName);
			
			byte[] bytes=new byte[2048];        
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
				
				request.setAttribute("date", date);	
				request.setAttribute("orgId", orgId);
				request.setAttribute("orgName", orgName);
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
                    mChildReport = getExcelReportISTrue(excelFile,date);
                    
					if(mChildReport == null){
						messages.add(excelFile.getName()+"����ʧ�ܣ�ģ�������ӱ��ⲻ��ȷ,ģ��������뱨������һ��");
						continue;
					}
					
					childRepId = mChildReport.getId().getTemplateId();
					versionId = mChildReport.getId().getVersionId();
					
					List freq =  StrutsAFActuRepDelegate.select(childRepId, versionId);
					MActuRepForm mrf = (MActuRepForm)freq.get(0);
					repFreqId=mrf.getRepFreqId().toString();
//					// �����������
//					mChildReport.setReportName(new StrutsMChildReportDelegate().getMChildReport(childRepId, versionId)
//							.getReportName());
					
					/**��ñ���ı��֣�Ŀǰ��֧�ֶ���ֵ��������ͣ�*/
					if (curId == null || curId.equals(""))
						curId = "1";
					mCurr= StrutsMCurrDelegate.getISMCurr(curId);
					
					/**�жϱ���ı��Ϳھ��Ƿ���ȷ*/
//					if(mChildReport.getTemplateType().equals(com.fitech.net.config.Config.FZ_TEMPLATE.toString())){
//						dataRangeDes = "���ڻ�������";
//						dataRange = StrutsMDataRgTypeDelegate.selectDataRange(dataRangeDes);
//					}else{
//						dataRangeDes = Util.getDataRangeDes(excelFile.getPath());
//						dataRange = StrutsMDataRgTypeDelegate.selectDataRange(dataRangeDes.trim());						
//					}					
//					if(dataRange == null){
//						messages.add(excelFile.getName()+"���Ϳھ��������");
//						continue;
//					}
					
//					/**�ж��Ƿ��иñ���ı��ͷ�Χ*/
//					if(StrutsMRepRangeDelegate.getMRepRanageOncb(orgId,childRepId,versionId) == null){
//						messages.add(excelFile.getName()+"���ͷ�Χ�����ڣ�");
//						continue;
//					}
//					
//					/**�ж��û��Ƿ��иñ���ı���Ȩ��*/
//					if(operator.isSuperManager() == false){
//						if(operator.getChildRepReportPopedom() == null || operator.getChildRepReportPopedom().equals("")){
//							messages.add(excelFile.getName()+"ȱ�ٱ���Ȩ�ޣ�");							
//							continue;
//						}						
//						if(StrutsMRepRangeDelegate.getMRepRanage(orgId,childRepId,versionId,operator) == null){
//							messages.add(excelFile.getName()+"ȱ�ٱ���Ȩ�ޣ�");
//							continue;
//						}
//					}
					
//					/**�жϱ��Ϳھ��Ƿ���ȷ*/
//					if(StrutsReportInDelegate.isExistDataRange(dataRange.getDataRangeId().toString(),childRepId,versionId) == false){
//						messages.add(excelFile.getName()+"���Ϳھ�����");
//						continue;
//					}
					
//					/**�жϱ���Ƶ���Ƿ����*/
//					if(StrutsReportInDelegate.isExistDataRange(dataRange.getDataRangeId(),childRepId
//							,versionId,new Integer(term)) == false){
//						messages.add(excelFile.getName()+"��"+term+"����Ҫ���ͣ�");
//						continue;
//					}
//					
//					/**�жϱ����Ƿ��Ѿ����ͣ����ѱ��������ظ����ͣ�*/
//					reportInTemp = StrutsReportInDelegate.getReportIn(childRepId,versionId,orgId,new Integer(year)
//							,new Integer(term),dataRange.getDataRangeId(),mCurr.getCurId(),new Integer(1));
//					
//					if(reportInTemp != null && (reportInTemp.getCheckFlag().shortValue() == Config.CHECK_FLAG_PASS.shortValue() 
//							|| reportInTemp.getCheckFlag().shortValue() == Config.CHECK_FLAG_UNCHECK.shortValue())){
//						messages.add(excelFile.getName()+"�Ѿ����Ͳ������ظ����ͣ�");
//						continue;
//					}
					
					boolean result = false;
					try{
						Integer repInId = null;
						//Ԥ�Ȳ����¼�¼
						reportInForm = new AFReportForm();
						reportInForm.setTemplateId(childRepId);
						reportInForm.setVersionId(versionId);
//						reportInForm.setDataRangeId(dataRange.getDataRangeId());
						reportInForm.setCurId(curId);
						reportInForm.setYear(year);
						reportInForm.setTerm(term);
						reportInForm.setDay(day);
						reportInForm.setRepFreqId(repFreqId);
						reportInForm.setOrgId(orgId);
						reportInForm.setTimes("1");
						reportInForm.setReportDate(new Date());
						reportInForm.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_AFTERSAVE.toString());
						reportInForm.setCurName(mCurr.getCurName());
						reportInForm.setRepName(mChildReport.getTemplateName());
						
						AfTemplate mcr = AFTemplateDelegate.getTemplate(reportInForm.getTemplateId(), reportInForm.getVersionId());
						reportInForm.setRepName(mcr.getTemplateName());
						// ��������
						AFReportDelegate strutsReportInDelegate=new AFReportDelegate();
						AfReport reportIn = strutsReportInDelegate.insertNewReport(reportInForm);
						if (reportIn != null)
							repInId = reportIn.getRepId().intValue();
						
						String repType = null;
						HttpSession session = request.getSession();    
						if (session.getAttribute(com.cbrc.smis.common.Config.REPORT_SESSION_FLG) != null)
							repType = session.getAttribute(com.cbrc.smis.common.Config.REPORT_SESSION_FLG).toString();
						
						//�����㡢�嵥�ж�
						if(!mcr.getReportStyle().toString().equals(com.fitech.gznx.common.Config.REPORT_QD)){
						
						
							NXReportExcelHandler excelHandler = null;
							try {
								excelHandler = new NXReportExcelHandler(repInId, excelFile.getPath(),messages);
							} catch (Exception e) {
								e.printStackTrace();
								messages=excelHandler.getMessages();
								request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
								request.setAttribute("date", date);	
								request.setAttribute("orgId", orgId);
								request.setAttribute("orgName",orgName );
								request.setAttribute("RequestParam", url);
								request.setAttribute("curPage", curPage);
								request.setAttribute(com.cbrc.smis.common.Config.APART_PAGE_OBJECT,aPage);
								request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
								return mapping.findForward("view");		
							}
							
							//�����������ݽ�DB
							result = excelHandler.copyExcelToDB(repType);
						
						}else{
							//�嵥
							
							// ��֤�嵥ʽ����ı�����ӱ���
							QDReportExcelHandler qdExcelHandler = null;
							try {
								qdExcelHandler = new QDReportExcelHandler(excelFile.getPath(),messages);
							} catch (Exception e) {
								messages=qdExcelHandler.getMessages();
								request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
								request.setAttribute("date", date);	
								request.setAttribute("orgId", orgId);
								request.setAttribute("orgName",orgName );
								request.setAttribute("RequestParam", url);
								request.setAttribute("curPage", curPage);
								request.setAttribute(com.cbrc.smis.common.Config.APART_PAGE_OBJECT,aPage);
								request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
								return mapping.findForward("view");		
							}
							// ���嵥ʽ��������ݲ������ݿ�
							result = new QDDataDelegate().qdIntoDBNx(childRepId, versionId, repInId.toString(), excelFile.getPath());
							
						}
						
						if (result){
							result = strutsReportInDelegate.updateReportInCheckFlag(repInId, Config.CHECK_FLAG_AFTERSAVE);
						}
						if (result){
							notshow = "not null";
							Aditing aditing = new Aditing();
							aditing.setRepInId(repInId);
							aditing.setRepName(reportInForm.getRepName());
							aditing.setChildRepId(reportInForm.getTemplateId());
							aditing.setVersionId(reportInForm.getVersionId());
							aditing.setYear(Integer.valueOf(reportInForm.getYear()));
							aditing.setTerm(Integer.valueOf(reportInForm.getTerm()));
							aditing.setDay(Integer.valueOf(reportInForm.getDay()));
							aditing.setOrgId(reportInForm.getOrgId());
							aditing.setOrgName(AFOrgDelegate.getOrgInfo(orgId).getOrgName());
							aditing.setCurId(Integer.valueOf(reportInForm.getCurId()));
							aditing.setCurrName(reportInForm.getCurName());
							//aditing.setDataRangeId(reportInForm.getDataRangeId());
							aditing.setActuFreqName(mrf.getRepFreqName());
							aditing.setActuFreqID(Integer.valueOf(repFreqId));
							aditing.setCheckFlag(Short.valueOf(reportInForm.getCheckFlag()));
							aditingList.add(aditing);	
							messages.add(excelFile.getName()+"����ɹ���");
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
		
//		request.setAttribute("term",term);
//		request.setAttribute("year",year);
		request.setAttribute("date",date);
		request.setAttribute("orgId",orgId);
		request.setAttribute("orgName",orgName );
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
			String zipPath = fPath + File.separator + zipFileName;
			ZipFile zipFile = new ZipFile(zipPath);		
			Enumeration enu = zipFile.getEntries();			
			while(enu.hasMoreElements()){
				ZipEntry entry = (ZipEntry)enu.nextElement();				
				inStream = zipFile.getInputStream(entry);
				String filePath = tPath + File.separator + entry.getName();
				file = new File(filePath);
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
    private AfTemplate getExcelReportISTrue(File excelFile, String date){
    	AfTemplate mChildReport = null;
		String title = null;
		String subTitle = null;		

		try{
            
            title = parse.getTitle(new HSSFWorkbook(new FileInputStream(excelFile.getPath())));
             subTitle = parse.getSubtitle(new HSSFWorkbook(new FileInputStream(excelFile.getPath())));
            
			//Integer templateType = getFZORFRTemplate(title);
			
            if(title != null) title = title.trim();
			if(subTitle != null) subTitle = subTitle.trim();
			
			//����ӱ������
            mChildReport = AFTemplateDelegate.findByTitle(title,subTitle,date,false);

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