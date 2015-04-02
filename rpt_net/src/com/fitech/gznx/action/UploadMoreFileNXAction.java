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
 * 处理批量上报
 * @author jcm
 * @2007-12-18
 */
public final class UploadMoreFileNXAction extends Action {

	private static FitechException log = new FitechException(UploadMoreFileNXAction.class);
	/*上传文件临时目录*/
	private final static String SERVICE_UP_TEMP = Config.REAL_ROOT_PATH + Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP;
	/*上传文件解压后的临时目录*/
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
		String notshow = null;  //设置页面是否显示按钮
		String url = "/viewNXDataReport.do";  //跳转页面URL
		
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
			/**判断上传ZIP文件大小*/
			if(formFile.getFileSize() > 1024*1024){
				messages.add("上传文件大小不能超过1M！");
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
				return new ActionForward(url);
			}
			
			/**建立临时文件夹*/
			this.make(SERVICE_UP_TEMP);     
			this.make(SERVICE_UP_RELEASE); 
			this.make(SERVICE_UP_RELEASE + orgId);			
			
			/**文件上传*/
			String path = SERVICE_UP_TEMP + orgId;
			String zipFileName = path + File.separator + formFile.getFileName();
			File pathDirectory = new File(path);
			if(!pathDirectory.exists()) pathDirectory.mkdir();
			List zipRelFileList = new ArrayList();  //ZIP包解压后的文件列表
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
			 * 处理上传的ZIP文件
			 * 先解压，再对单张Excel做处理
			 */			
			if(!release(formFile.getFileName(),path,SERVICE_UP_RELEASE + orgId,zipRelFileList)){				
				messages.add("ZIP文件解压失败！");
				
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
					//验证Excel文件是否是有效的文件
                    mChildReport = getExcelReportISTrue(excelFile,date);
                    
					if(mChildReport == null){
						messages.add(excelFile.getName()+"载入失败！模板标题或子标题不正确,模板标题须与报表名称一致");
						continue;
					}
					
					childRepId = mChildReport.getId().getTemplateId();
					versionId = mChildReport.getId().getVersionId();
					
					List freq =  StrutsAFActuRepDelegate.select(childRepId, versionId);
					MActuRepForm mrf = (MActuRepForm)freq.get(0);
					repFreqId=mrf.getRepFreqId().toString();
//					// 查出报表名称
//					mChildReport.setReportName(new StrutsMChildReportDelegate().getMChildReport(childRepId, versionId)
//							.getReportName());
					
					/**获得报表的币种（目前不支持多币种的批量报送）*/
					if (curId == null || curId.equals(""))
						curId = "1";
					mCurr= StrutsMCurrDelegate.getISMCurr(curId);
					
					/**判断报表的报送口径是否正确*/
//					if(mChildReport.getTemplateType().equals(com.fitech.net.config.Config.FZ_TEMPLATE.toString())){
//						dataRangeDes = "境内汇总数据";
//						dataRange = StrutsMDataRgTypeDelegate.selectDataRange(dataRangeDes);
//					}else{
//						dataRangeDes = Util.getDataRangeDes(excelFile.getPath());
//						dataRange = StrutsMDataRgTypeDelegate.selectDataRange(dataRangeDes.trim());						
//					}					
//					if(dataRange == null){
//						messages.add(excelFile.getName()+"报送口径输入错误！");
//						continue;
//					}
					
//					/**判断是否有该报表的报送范围*/
//					if(StrutsMRepRangeDelegate.getMRepRanageOncb(orgId,childRepId,versionId) == null){
//						messages.add(excelFile.getName()+"报送范围不存在！");
//						continue;
//					}
//					
//					/**判断用户是否有该报表的报送权限*/
//					if(operator.isSuperManager() == false){
//						if(operator.getChildRepReportPopedom() == null || operator.getChildRepReportPopedom().equals("")){
//							messages.add(excelFile.getName()+"缺少报送权限！");							
//							continue;
//						}						
//						if(StrutsMRepRangeDelegate.getMRepRanage(orgId,childRepId,versionId,operator) == null){
//							messages.add(excelFile.getName()+"缺少报送权限！");
//							continue;
//						}
//					}
					
//					/**判断报送口径是否正确*/
//					if(StrutsReportInDelegate.isExistDataRange(dataRange.getDataRangeId().toString(),childRepId,versionId) == false){
//						messages.add(excelFile.getName()+"报送口径错误！");
//						continue;
//					}
					
//					/**判断报送频度是否存在*/
//					if(StrutsReportInDelegate.isExistDataRange(dataRange.getDataRangeId(),childRepId
//							,versionId,new Integer(term)) == false){
//						messages.add(excelFile.getName()+"在"+term+"不需要报送！");
//						continue;
//					}
//					
//					/**判断报表是否已经报送（若已报不允许重复报送）*/
//					reportInTemp = StrutsReportInDelegate.getReportIn(childRepId,versionId,orgId,new Integer(year)
//							,new Integer(term),dataRange.getDataRangeId(),mCurr.getCurId(),new Integer(1));
//					
//					if(reportInTemp != null && (reportInTemp.getCheckFlag().shortValue() == Config.CHECK_FLAG_PASS.shortValue() 
//							|| reportInTemp.getCheckFlag().shortValue() == Config.CHECK_FLAG_UNCHECK.shortValue())){
//						messages.add(excelFile.getName()+"已经报送不允许重复报送！");
//						continue;
//					}
					
					boolean result = false;
					try{
						Integer repInId = null;
						//预先插入新记录
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
						// 插入数据
						AFReportDelegate strutsReportInDelegate=new AFReportDelegate();
						AfReport reportIn = strutsReportInDelegate.insertNewReport(reportInForm);
						if (reportIn != null)
							repInId = reportIn.getRepId().intValue();
						
						String repType = null;
						HttpSession session = request.getSession();    
						if (session.getAttribute(com.cbrc.smis.common.Config.REPORT_SESSION_FLG) != null)
							repType = session.getAttribute(com.cbrc.smis.common.Config.REPORT_SESSION_FLG).toString();
						
						//加入点点、清单判断
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
							
							//拷贝报表数据进DB
							result = excelHandler.copyExcelToDB(repType);
						
						}else{
							//清单
							
							// 验证清单式报表的标题和子标题
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
							// 将清单式报表的数据插入数据库
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
							messages.add(excelFile.getName()+"载入成功！");
						}
										
					}catch (Exception e){
						messages.add(excelFile.getName()+"入库失败！");
						e.printStackTrace();
					}
				}				
				//处理结束后，删除解压后的文件				
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
	 * 解压ZIP包
	 * 
	 * @param zipFileName ZIP包名称
	 * @param fPath ZIP包所在文件目录
	 * @param tPath 需要解压的文件目录
	 * @param fileList 解压后的文件列表
	 * @return boolean true-解压成功 false-解压失败
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
     * 建立文件目录
     * 
     * @param filePath 目录路径
     * @return void
     */
    public void make(String filePath){
    	File file=new File(filePath);    	
    	
    	if(!file.exists()) file.mkdir();
    }
    
    /**
     * 查看模板载入是否正确
     * 
     * @param excelFile Excel文件
     * @param year 报表年份
     * @param term 报表期数
     * @return 子报表对象
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
			
			//获得子报表对象
            mChildReport = AFTemplateDelegate.findByTitle(title,subTitle,date,false);

		}catch (Exception ex){
			mChildReport = null;
			log.printStackTrace(ex);
			ex.printStackTrace();
		}
		return mChildReport;		
	}
	
	/**
	 * 根据模板名称获得模板类型（法人/分支模板）
	 * 
	 * @param title 模板名称
	 * @return Integer 模板类型
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