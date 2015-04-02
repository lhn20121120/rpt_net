package com.fitech.gznx.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate;
import com.cbrc.smis.form.UploadFileForm;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.MRepFreq;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.excel.NXReportExcelHandler;
import com.fitech.gznx.excel.QDReportExcelHandler;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.po.AfTemplateShape;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.fitech.gznx.service.QDDataDelegate;
import com.fitech.net.adapter.StrutsExcelData;
import com.fitech.net.config.Config;

/**
 * wh 用来处理上传文件
 */
public class UploadFileNXAction extends Action
{

	private FitechException log = new FitechException(UploadFileNXAction.class);

	public  String messInfo = "";

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException
	{

		this.messInfo = "";

		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);

		UploadFileForm upFileForm = (UploadFileForm) form;
		
		RequestUtils.populate(upFileForm, request);

		// 设置页面是否显示按钮
		String notshow = null;

		FormFile formFile = upFileForm.getFormFile();
		
		System.out.println("path:"+formFile.getFileName());
		String str = upFileForm.getVersionId();
		
		String curpage = request.getParameter("curPage");

		String childRepId = null;
		String versionId = null;
		String date = null;
		String curId = null;
		String orgId = null;
		//String dataRangeId = null;
		String actuFreqID = null;
		String backQry = null;

		if (formFile == null || str == null || str.equals(""))
		{
			messages.add(resources.getMessage("select.upReport.failed"));
			if (messages.getMessages() != null && messages.getMessages().size() > 0)
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);

			return new ActionForward("/viewNXDataReport.do");
		}

		String[] param = str.split("_");
		childRepId = param[0].trim();
		versionId = param[1].trim();
		date = param[2].trim();
		curId = param[3].trim();
		orgId = param[4].trim();
		actuFreqID = param[5].trim();
		backQry = param[6].trim();
		
		//没有期数、版本，直接返回
		if (date == null || date.equals("") || versionId == null || versionId.equals(""))
		{
			messages.add(resources.getMessage("select.upReport.failed"));
			if (messages.getMessages() != null && messages.getMessages().size() > 0)
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);

			return new ActionForward("/viewNXDataReport.do");
		}
		
		String requestParam = backQry.replaceAll("amp;", "");//+ "&curPage=" + curpage;
		
		if (orgId == null || orgId.equals(""))
		{
			messages.add(resources.getMessage("select.upReport.failed"));
			if (messages.getMessages() != null && messages.getMessages().size() > 0)
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
			request.setAttribute("curPage", curpage);
			request.setAttribute("RequestParam", requestParam);
			return new ActionForward("/viewNXDataReport.do?");
		}

		/**
		 * 开始建立一系列的临时文件夹
		 */
//		this.make(Config.REAL_ROOT_PATH + Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP);
//		this.make(Config.REAL_ROOT_PATH + Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP + Config.SERVICE_UP_RELEASE);
//		this.make(Config.REAL_ROOT_PATH + Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP  + orgId);

		/** 开始上传 */

		String path = Config.REAL_ROOT_PATH + Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP+ orgId;

		String excelPath = path + File.separator + orgId+request.getSession().hashCode()+System.currentTimeMillis()+".xls";
		File excelFile = new File(path);
		if (!excelFile.exists())
			excelFile.mkdirs();
		if (formFile != null  && formFile.getFileSize()>0)
		{
			//excelPath = formFile.getInputStream().
			InputStream ips = formFile.getInputStream();
			FileOutputStream fops = new FileOutputStream(excelPath);
			byte[] bytes = new byte[1024];
			int index = 0;
			while ((index = ips.read(bytes)) != -1)
			{
				fops.write(bytes, 0, index);
			}
			fops.close();
			ips.close();
		}
		
		AFReportForm reportInForm = new AFReportForm();
		boolean result = false;
		try
		{
			if (curId == null || curId.equals(""))
				curId = "1";
			MCurr mCurr = StrutsMCurrDelegate.getISMCurr(curId);
			// 得到币种名称
			String CurrName = mCurr.getCurName();

			//获得报表正确期数
//			String repDays[] = DateUtil.getLastFreqDate(date, Integer.valueOf(actuFreqID)).split("-");
			String repDays[] = date.split("-");

			
			Integer repInId = null;
			
			// 预先插入新记录
			reportInForm.setTemplateId(childRepId);
			reportInForm.setVersionId(versionId);
			//reportInForm.setDataRangeId(new Integer(dataRangeId));
			
			reportInForm.setYear(repDays[0]);
			reportInForm.setTerm(repDays[1]);
			reportInForm.setDay(repDays[2]);
			
			reportInForm.setRepFreqId(actuFreqID);
			reportInForm.setOrgId(orgId);

			reportInForm.setTimes("1");
			reportInForm.setReportDate(new Date());

			reportInForm.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_UNREPORT.toString());
			
			reportInForm.setCurId(curId);
			reportInForm.setCurName(CurrName);

			AfTemplate mcr = AFTemplateDelegate.getTemplate(reportInForm.getTemplateId(), reportInForm.getVersionId());
			reportInForm.setRepName(mcr.getTemplateName());
			
			// 插入数据
			AFReportDelegate strutsReportInDelegate = new AFReportDelegate();
			AfReport reportIn = strutsReportInDelegate.insertNewReport(reportInForm);
			if (reportIn != null)
				repInId = reportIn.getRepId().intValue();
			
			
			String url = "childRepId=" + childRepId + 
			"&versionId=" + versionId + 
			"&year=" + repDays[0] + 
			"&term=" + repDays[1] + 
			"&day=" + repDays[2] + 
			"&curId=" + curId + 
			"&orgId=" + orgId + 
		//	"&dataRangeId=" + dataRangeId + 
			"&checkFlag=" + Config.CHECK_FLAG_UNCHECK + 
			"&actuFreqID=" + actuFreqID+ 
			"&curPage="+ curpage;
			
	        /***
	          * 取得当前报表类型
	          */   
			HttpSession session = request.getSession();    
			
			Map map = CheckUploadExcel(formFile.getInputStream(),versionId,childRepId);
			if(map!=null && !map.isEmpty()){
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
				request.setAttribute("uploadErrors", map);
				request.setAttribute("curPage", curpage);
				request.setAttribute("RequestParam", requestParam);
				return new ActionForward("/offLineNXReport.do?" + url);
			}
			formFile=null;
			/** 上传完毕 */
			
			String repType = null;
			if (session.getAttribute(com.cbrc.smis.common.Config.REPORT_SESSION_FLG) != null)
				repType = session.getAttribute(com.cbrc.smis.common.Config.REPORT_SESSION_FLG).toString();
			
			
			//加入点点、清单判断
			if(!mcr.getReportStyle().toString().equals(com.fitech.gznx.common.Config.REPORT_QD)){
			
			
				NXReportExcelHandler excelHandler = new NXReportExcelHandler(repInId, excelPath,messages);
				if(excelHandler.getMessages()!=null && excelHandler.getMessages().getSize()>0){
					messages=excelHandler.getMessages();
					request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
					request.setAttribute("curPage", curpage);
					request.setAttribute("RequestParam", requestParam);
					return new ActionForward("/offLineNXReport.do?" + url);
				}
				boolean flag = getExcelReportISTrue(excelHandler.title, excelHandler.subTitle, versionId, childRepId);
				if (!flag) {
					if (!this.messInfo.equals("")) {
						messages.add(this.messInfo);
					} else {
						messages.add("报表载入失败!标题验证未通过");
					}
					request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
					request.setAttribute("curPage", curpage);
					request.setAttribute("RequestParam", requestParam);
					return new ActionForward("/offLineNXReport.do?" + url);
				}
	
	
				
				//拷贝报表数据进DB
				result = excelHandler.copyExcelToDB(repType);
			
			}else{
				//清单
				
				// 验证清单式报表的标题和子标题
				QDReportExcelHandler qdExcelHandler = new QDReportExcelHandler(excelPath,messages);
				if(qdExcelHandler.getMessages()!=null && qdExcelHandler.getMessages().getSize()>0){
					messages=qdExcelHandler.getMessages();
					request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
					request.setAttribute("curPage", curpage);
					request.setAttribute("RequestParam", requestParam);
					return new ActionForward("/offLineNXReport.do?" + url);
				}
				boolean flag = getExcelReportISTrue(qdExcelHandler.title, qdExcelHandler.subTitle, versionId, childRepId);
				if (!flag) {
					if (!this.messInfo.equals("")) {
						messages.add(this.messInfo);
					} else {
						messages.add("报表载入失败");
					}
					request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
					request.setAttribute("curPage", curpage);
					request.setAttribute("RequestParam", requestParam);
					return new ActionForward("/offLineNXReport.do?" + url);
				}

				// 将清单式报表的数据插入数据库
				result = new QDDataDelegate().qdIntoDBNx(childRepId, versionId, repInId.toString(), excelPath);
				
			}
			
			if (result)
				result = strutsReportInDelegate.updateReportInCheckFlag(repInId, Config.CHECK_FLAG_AFTERSAVE);
			
			if (result) {

				notshow = "not null";

				Aditing aditing = new Aditing();

				// 得到报送口径
//				MDataRgType mrt = new StrutsMDataRgTypeDelegate().selectOneByName(dataRangeId);
//				if (mrt != null)
//					aditing.setDataRgTypeName(mrt.getDataRgDesc());
				
				// 得到报送频度
				MRepFreq mrf = new StrutsMDataRgTypeDelegate().getActuRepFlag(new Integer(actuFreqID));

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
				aditing.setActuFreqID(Integer.valueOf(actuFreqID));
				aditing.setCheckFlag(Short.valueOf(reportInForm.getCheckFlag()));
				
				request.setAttribute("aditing", aditing);

				if (notshow != null) {
					request.setAttribute(Config.NOT_SHOW, notshow);
				}
			} else {
				messages.add("报表载入失败");
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
				request.setAttribute("curPage", curpage);
				request.setAttribute("RequestParam", requestParam);
				return new ActionForward("/offLineNXReport.do?" + url);
			}
			
			File tempFile=new File(excelPath);
			if (tempFile.exists())
				tempFile.delete();

		} catch (Exception e) {
			
			messages.add("报表载入失败");
			e.printStackTrace();
			
		}

		requestParam = backQry.replaceAll("amp;", ""); //+ "&curPage=" + curpage ;//+ "&orgId=" + orgId;
		request.setAttribute("curPage", curpage);
		request.setAttribute("RequestParam", requestParam);

		request.setAttribute("backQry", backQry);
		messages.add("报表载入成功");
		request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);

		return mapping.findForward("view");
	}
//
//	/**
//	 * 建立文件夹
//	 */
//	public void make(String filePath)
//	{
//		File file = new File(filePath);
//
//		if (!file.exists())
//		{
//			file.mkdirs();
//		}
//	}
//
//	/**
//	 * 删除文件夹
//	 */
//	public void remove(String filePath)
//	{
//		File file = new File(filePath);
//
//		if (file != null && file.isDirectory())
//		{
//			File[] files = file.listFiles();
//			for (int i = 0; i < files.length; i++)
//			{
//				if (files[i] != null)
//				{
//					files[i].delete();
//				}
//			}
//		}
//		file.delete();
//	}
//	/**
//	 * 
//	 * @param title
//	 * @return
//	 */
	private Integer getFZORFRTemplate(String title)
	{
		Integer templateType = com.fitech.net.config.Config.FR_TEMPLATE;
		int count = -1;
		if (title != null && !title.equals("") && title.length() > 2)
		{

			String titleStr = title.trim().substring(0, 2);
			if (titleStr.toUpperCase().indexOf(com.fitech.net.config.Config.FZ_SF_TAMPLATE) > -1
					|| titleStr.toUpperCase().indexOf(com.fitech.net.config.Config.FZ_GF_TAMPLATE) > -1)
			{
				templateType = com.fitech.net.config.Config.FZ_TEMPLATE;
			}

		}
		return templateType;
	}

	/**
	 * 查看模板载入是否正确
	 * 
	 * @param excelPath
	 * @param versionId
	 * @param childRepId
	 * @return
	 */
	private boolean getExcelReportISTrue(String title, String subTitle, String versionId, String childRepId) {
//		
//		boolean flag = false;
//
//		AfTemplate afTemplate = null;
//
//		try {
//			String titles = "";
//			if(subTitle!=null && !subTitle.equals("") )
//				titles = title + "-" + subTitle;
//			else
//				titles = title;
//			
//			afTemplate = AFTemplateDelegate.getTemplate(childRepId, versionId);
//			
//			System.out.println(afTemplate.getTemplateName());
//			System.out.println(titles.trim());
//			boolean is = afTemplate.getTemplateName().equals(titles.trim());
//			System.out.println(is);
//			if (afTemplate == null) {
//				
//				this.messInfo = "报表载入失败！无该报表信息";
//				flag = false;
//
//			} else if ( afTemplate.getTemplateName().equals(titles.trim())
//					|| afTemplate.getTemplateName().equals(title.trim())) {
//				
//				flag = true;
//				
//			} else {
//				
//				this.messInfo = "报表载入错误!标题错误";
//				flag = false;
//				
//			}
//
//		}
//		catch (Exception ex) {
//			
//			flag = false;
//			ex.printStackTrace();
//
//		}

		return true;
		
	}
	
	private Map CheckUploadExcel(InputStream in,String versionId,String childRepId){
		HSSFWorkbook sourceWb = null;
		HSSFSheet sheet = null;
		int count=0;
		List<AfTemplateShape> datas=StrutsExcelData.getTemplateDate(versionId, childRepId);
		Map map=putDataToMap(datas);
		if(map==null || map.isEmpty())return null;
		String[] ARRCOLS =
		{ "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
				"U", "V", "W", "X", "Y", "Z"};
		try {
			POIFSFileSystem srcPOIFile = new POIFSFileSystem(in);
			sourceWb = new HSSFWorkbook(srcPOIFile);
			if (sourceWb.getNumberOfSheets() > 0)
			{
				sheet = sourceWb.getSheetAt(0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		HSSFRow row = null;
		HSSFCell cell = null;
		
		for (Iterator iter = sheet.rowIterator(); iter.hasNext();)
		{
			row = (HSSFRow) iter.next();
			for (short i = row.getFirstCellNum(), n = row.getLastCellNum(); i < n; i++)
			{
				cell = (HSSFCell) row.getCell(i);
				if (cell == null)
					continue;
				if (cell.getCellNum() >= ARRCOLS.length)
					continue;
				String cellName = ARRCOLS[cell.getCellNum()]+(row.getRowNum() + 1);
				if(map.containsKey(cellName)){
					String con=(String)map.get(cellName);
					if(con!=null && !con.equals("")){
						if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
							if(!cell.getStringCellValue().equals("")){
								String vl=cell.getStringCellValue().trim();
								if(con.equalsIgnoreCase(vl)){
									map.remove(cellName);
								}
							}
						}       // 修改版式校验excel单元格数值类型不能校验问题
						else if(cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC
								|| cell.getCellType()==HSSFCell.CELL_TYPE_FORMULA){
							if(!HSSFDateUtil.isCellDateFormatted(cell)){
								String num  = cell.getNumericCellValue()+"";
								if(!num.equals("")){
									if(num.indexOf(".")!=-1){
										num = num.substring(0,num.indexOf("."));
									}
									if(con.equalsIgnoreCase(num)){
										map.remove(cellName);
									}
								}
							}else{
								Date date = cell.getDateCellValue();
								if(date.toString().equalsIgnoreCase(con)){
									map.remove(cellName);
								}
							}
						}else if(cell.getCellType()==HSSFCell.CELL_TYPE_BOOLEAN){
							boolean result = cell.getBooleanCellValue();
							if(String.valueOf(result).equalsIgnoreCase(con)){
								map.remove(cellName);
							}
						}else{
							try {
								String msg = cell.getStringCellValue();
								if(msg!=null){
									if(msg.equalsIgnoreCase(con)){
										map.remove(cellName);
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
								log.println(cellName+",单元格类型"+cell.getCellType()+",errorMsg:"+e.getMessage());
							}
						}
					}
				}
			}
		}
		return map;
	}
	
	private Map putDataToMap(List<AfTemplateShape> list){
		Map map=new HashMap();
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				AfTemplateShape as=list.get(i);
				map.put(as.getId().getCellName(), as.getCellContext());
			}
		}
		if(map.isEmpty())
			return null;
		return map;
	}
}
