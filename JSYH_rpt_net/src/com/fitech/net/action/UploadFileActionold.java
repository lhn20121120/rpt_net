package com.fitech.net.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.cbrc.smis.adapter.StrutsMActuRepDelegate;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.ConfigOncb;
import com.cbrc.smis.common.DownLoadDataToZip;
import com.cbrc.smis.common.XmlUtils;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.UploadFileForm;
import com.cbrc.smis.hibernate.MActuRep;
import com.cbrc.smis.hibernate.MChildReport;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.MDataRgType;
import com.cbrc.smis.hibernate.MRepRange;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.system.cb.InputData;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.Excel2Xml.Excel2XML;
import com.fitech.net.bean.Report;
import com.fitech.net.config.Config;

/**
 * 用来处理上传文件
 */
public final class UploadFileActionold extends Action {

	private static FitechException log = new FitechException(UploadFileAction.class);
	
	public static String messInfo = "";
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
      
		this.messInfo = "";
		
		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);		
		
		UploadFileForm upFileForm=(UploadFileForm)form;
		RequestUtils.populate(upFileForm, request);
		
		Operator oper=(Operator)request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		
		//放置所有提示信息
		ArrayList lists = new ArrayList();
		
		//设置页面是否显示按钮
		String notshow = null;
		
		FormFile formFile = upFileForm.getFormFile();
		String str = upFileForm.getVersionId();
		
		
		String year = null;
		String term = null;
		String versionId = null;
		String curPage = "1";
		String orgId = null;
		ApartPage aPage=new ApartPage();
		
		HashMap map = new HashMap();
		if(formFile == null || str == null || str.equals("")){
			messages.add(resources.getMessage("select.upReport.failed"));
			if(messages.getMessages() != null && messages.getMessages().size() > 0)		
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
			
			return new ActionForward("/viewDataReport.do");
		}
		try{
			String[] param = str.split("_");
			year = param[0].trim();
			term = param[1].trim();
			orgId = param[2].trim();
			curPage = param[3].trim();
			versionId = StrutsMRepRangeDelegate.selectVersionId(year,term,orgId);
			
		}catch(Exception ex){
			year = null;
			term = null;
			versionId = null;
		}
		if(year == null || year.equals("") || term == null || term.equals("") || versionId == null || versionId.equals("")){
			messages.add(resources.getMessage("select.upReport.failed"));		
			if(messages.getMessages() != null && messages.getMessages().size() > 0)		
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
			
			return new ActionForward("/viewDataReport.do?curPage=" + curPage);
		}
		if(orgId == null || orgId.equals("")){
			messages.add(resources.getMessage("select.upReport.failed"));		
			if(messages.getMessages() != null && messages.getMessages().size() > 0)		
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
			
			return new ActionForward("/viewDataReport.do?year=" + year + "&setDate=" + term + "&curPage=" + curPage);
		}
				
		try{
			aPage.setCurPage(Integer.parseInt(curPage));
		}catch(Exception ex){
			aPage.setCurPage(1);
		}
		
       /**
        * 开始建立一系列的临时文件夹
        */
		this.make(Config.REAL_ROOT_PATH+Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP);     
		this.make(Config.REAL_ROOT_PATH+Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP + Config.SERVICE_UP_RELEASE); 
		this.make(Config.REAL_ROOT_PATH+Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP + Config.SERVICE_UP_RELEASE + orgId);
		this.make(Config.REAL_ROOT_PATH+Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP + Config.SERVICE_UP_XML);    
		this.make(Config.REAL_ROOT_PATH+Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP + Config.SERVICE_UP_IN);
            
        /**开始上传*/      
      
		String path = Config.REAL_ROOT_PATH + Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP;
		
		File excelFile = new File(path + orgId);
		if(!excelFile.exists()) excelFile.mkdir();
				
		if(formFile!=null){
			InputStream ips=formFile.getInputStream();      
			FileOutputStream fops=new FileOutputStream(path + orgId + File.separator + formFile.getFileName());       
			byte[] bytes=new byte[1024];        
			int index=0;       
			while((index=ips.read(bytes))!=-1){ 	  
				fops.write(bytes,0,index);
			}
			fops.close();
			ips.close();
		}
        /**上传完毕*/      
           
        /**验证是否需要解压缩*/
		File file=new File(path+orgId);
		if(file!=null && file.isDirectory()){
			File[] files=file.listFiles(); 
			if(files!=null){
				for(int i=0;i<files.length;i++){
					String fileName=files[i].getName();    			  
    			  
					if(fileName!=null && !"".equals(fileName) && formFile.getFileName().equals(fileName)){
						/**打包上报数据**/
						if(formFile.getContentType().equals(com.cbrc.smis.common.Config.FILE_CONTENTTYPE_ZIP)){
							release(fileName,Config.REAL_ROOT_PATH+Config.REPORT_NAME+File.separator+Config.SERVICE_UP_TEMP+orgId+File.separator,
									Config.REAL_ROOT_PATH+Config.REPORT_NAME+File.separator+Config.SERVICE_UP_TEMP+Config.SERVICE_UP_RELEASE+orgId+File.separator);   					  
							
							File filer=new File(Config.REAL_ROOT_PATH+Config.REPORT_NAME+File.separator+Config.SERVICE_UP_TEMP+Config.SERVICE_UP_RELEASE+orgId);
							if(filer!=null && filer.isDirectory()){
								File[] filers=filer.listFiles();
								if(filers != null && filers.length > 0){	
									boolean buttonFlag = false;
									boolean buttonFlag2 = false;
									
									for(int j=0;j<filers.length;j++){										
										File excelReleaseFile = filers[j];
										if(!excelReleaseFile.getName().substring(excelReleaseFile.getName().indexOf(".")).equals(".xls"))
											continue;
										
										File onefile=new File(path + Config.SERVICE_UP_XML + orgId +File.separator);   
										if(!onefile.exists()) onefile.mkdir();
																	
										Calendar calendar = Calendar.getInstance();
										StringBuffer buffer = new StringBuffer();
										buffer.append(calendar.get(Calendar.YEAR)).append(calendar.get(Calendar.MONTH)+1).append(calendar.get(Calendar.DATE))
														.append(calendar.get(Calendar.HOUR_OF_DAY)).append(calendar.get(Calendar.MINUTE)).append(calendar.get(Calendar.SECOND))
														.append(calendar.get(Calendar.MILLISECOND));
										
										//解析excel为xml文件
										String tempFile = Config.REAL_ROOT_PATH + Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP
												+ Config.SERVICE_UP_RELEASE + orgId + File.separator + excelReleaseFile.getName();
										if(Excel2XML.excel2xml(tempFile,orgId,versionId,buffer.toString())){  						  
											File xmlFile = null;
											File[] filess =onefile.listFiles();   						  
											if(filess != null && filess.length > 0){
												for(int k=0;k<filess.length;k++){
													if(filess[k].getName().equals(buffer.toString().trim()+".xml")){
														xmlFile = filess[k];
														break;
													}
												}
											}
											Aditing aditing = null;
											if(xmlFile != null)
												aditing = this.getAditingByXML(xmlFile,versionId,orgId,year,term,oper);
											
											if(aditing != null && aditing.getYear().toString().equals(year) && aditing.getTerm().toString().equals(term)){ 
												buttonFlag2 = true;
												boolean bool = StrutsReportInDelegate.isReportInExist(aditing);    							  
												if(!bool){//报送记录不存在    								  
													aditing.setCheckFlag(Config.CHECK_FLAG_FAILED);   							  
												}else{
													buttonFlag = true;
													aditing.setCheckFlag(Config.CHECK_FLAG_UNCHECK); 																			 
												}
												lists.add(aditing);   							  
												
											}else{												
												if(!this.messInfo.equals("")) messages.add(this.messInfo);
												else messages.add(resources.getMessage("select.upReport.failed"));	
												continue;
												
											}
											aditing.setXmlFileName(buffer.toString()+".xml");
											map.put(buffer.toString()+".xml",aditing);								
											request.getSession().setAttribute(oper.getOperatorId().toString(),map);

										}else{
											if(!this.messInfo.equals("")) messages.add(this.messInfo);
											else messages.add("报送失败，Excel解析失败！");	
											continue;											
										}
									}									
									if(buttonFlag == false && buttonFlag2 == true) notshow = "not null";
								}											  					
							}
						/**单张Excel上报数据**/
						}else if(formFile.getContentType().equals(com.cbrc.smis.common.Config.FILE_CONTENTTYPE_EXCEL)){
							File onefile=new File(path + Config.SERVICE_UP_XML + orgId+File.separator);   
							if(!onefile.exists()) onefile.mkdir();
														
							Calendar calendar = Calendar.getInstance();
							StringBuffer buffer = new StringBuffer();
							buffer.append(calendar.get(Calendar.YEAR)).append(calendar.get(Calendar.MONTH)+1).append(calendar.get(Calendar.DATE))
											.append(calendar.get(Calendar.HOUR_OF_DAY)).append(calendar.get(Calendar.MINUTE)).append(calendar.get(Calendar.SECOND))
											.append(calendar.get(Calendar.MILLISECOND));
							
							//解析excel为xml文件
							if(Excel2XML.excel2xml(path + orgId + File.separator + fileName,orgId,versionId,buffer.toString())){  						  
								File xmlFile = null;
								File[] filess =onefile.listFiles();   						  
								if(filess != null && filess.length > 0){
									for(int j=0;j<filess.length;j++){
										if(filess[j].getName().equals(buffer.toString().trim()+".xml")){
											xmlFile = filess[j];
											break;
										}
									}
								}
								Aditing aditing = null;
								if(xmlFile != null)
									aditing = this.getAditingByXML(xmlFile,versionId,orgId,year,term,oper);
								
								if(aditing != null && aditing.getYear().toString().equals(year) && aditing.getTerm().toString().equals(term)){   							  
									boolean bool = StrutsReportInDelegate.isReportInExist(aditing);    							  
									if(!bool){//报送记录不存在    								  
										aditing.setCheckFlag(Config.CHECK_FLAG_FAILED);   
										notshow = "not null";
									}else{   								  
										aditing.setCheckFlag(Config.CHECK_FLAG_UNCHECK);   								  										 							 
									}
									lists.add(aditing);   							  
									
								}else{									
									if(!this.messInfo.equals("")) messages.add(this.messInfo);
									else messages.add(resources.getMessage("select.upReport.failed"));
									if(messages.getMessages() != null && messages.getMessages().size() > 0)		
										request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
									
									return new ActionForward("/viewDataReport.do?year=" + year + "&setDate=" + term + "&curPage=" + curPage + "&orgId=" + orgId);
								}
								aditing.setXmlFileName(buffer.toString()+".xml");
								map.put(buffer.toString()+".xml",aditing);								
								request.getSession().setAttribute(oper.getOperatorId().toString(),map);

							}else{
								if(!this.messInfo.equals("")) messages.add(this.messInfo);
								else messages.add(resources.getMessage("select.upReport.failed"));
								if(messages.getMessages() != null && messages.getMessages().size() > 0)		
									request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
								
								return new ActionForward("/viewDataReport.do?year=" + year + "&setDate=" + term + "&curPage=" + curPage + "&orgId" + orgId);
							}
						}else{
							messages.add(resources.getMessage("select.upReport.fileType.failed"));
							if(messages.getMessages() != null && messages.getMessages().size() > 0)		
								request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
							
							return new ActionForward("/viewDataReport.do?year=" + year + "&setDate=" + term + "&curPage=" + curPage + "&orgId" + orgId);
						}
						
	    				/**
	    				 * 删除解压后的文件
	    				 */						
						File zipReleaseFolder = new File(Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.SERVICE_UP_TEMP+Config.SERVICE_UP_RELEASE);
						DownLoadDataToZip dldt = DownLoadDataToZip.newInstance();
						if(zipReleaseFolder != null && zipReleaseFolder.listFiles() != null) dldt.deleteFolder(zipReleaseFolder);
					}
				}
			}    	  
		}
      
		if(lists!=null && lists.size()>0)
			request.setAttribute(com.cbrc.smis.common.Config.RECORDS,lists);     	    

		if(notshow!=null){
    	   /**
    	     * 看是否要入库,不要入库则删除之(要入库在IntoTemplateAction中)
	    	 */ 
			this.remove(Config.REAL_ROOT_PATH+Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP+Config.SERVICE_UP_IN);  
			this.remove(Config.REAL_ROOT_PATH+Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP);
			request.setAttribute(Config.NOT_SHOW,notshow);
		}  	 
		if(messages.getMessages() != null && messages.getMessages().size() > 0)		
			request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
		
		request.setAttribute("term",term);
		request.setAttribute("year",year);		
		request.setAttribute("orgId",orgId);
		request.setAttribute(com.cbrc.smis.common.Config.APART_PAGE_OBJECT,aPage);
		
		//if(lists == null || lists.size() == 0) return new ActionForward("/viewDataReport.do?year=" + year + "&setDate=" + term + "&curPage=" + curPage);
		return mapping.findForward("view");
	}
		
	
	
	/***
	 * 开始解压缩包
	 * @param fileName
	 */
	
	public void release(String fileName,String from,String to){		
		try {	
			ZipFile zipFile=new ZipFile(from+fileName);
			
			Enumeration enu=zipFile.getEntries();
			
			while(enu.hasMoreElements())
			{
				ZipEntry entry=(ZipEntry)enu.nextElement();
				
				InputStream ips=zipFile.getInputStream(entry);
				FileOutputStream fops=new FileOutputStream(to+entry.getName());
				byte[] bytes=new byte[1024];
		          int index=0;
		          while((index=ips.read(bytes))!=-1)
		          {
		        	  fops.write(bytes,0,index);
		          }		          		          
		          fops.close();
		          ips.close();		          
			}					
			zipFile.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获得不用上报的记录的集合
	 */
	public ArrayList getNoUp()
	{
		//return this.lists;
		return null;
	}
	
	/**
	 * 通过lsiting获得Aditing对象
	 * 
	 */
	public Aditing getReportByListing(Node node)
	{
		Report report=new Report();
		Aditing aditing=new Aditing();
		
		//设置子报表的id
		Node reportId=node.selectSingleNode("reportId");
		if(reportId!=null && !"".equals(reportId.getText().trim()))
		{
			aditing.setChildRepId(reportId.getText());
		}
		else
		aditing.setChildRepId(null);
		
		
		//设置报表的版本号
		Node version=node.selectSingleNode("version");
		if(version!=null && !"".equals(version.getText().trim()))
		{
			aditing.setVersionId(version.getText());
		}
		else
		aditing.setVersionId(null);
		
		//设置报表上报的月份
		Node term=node.selectSingleNode("month");
		if(term!=null && !"".equals(term.getText().trim()))
		{
			aditing.setTerm(Integer.valueOf(term.getText()));
		}
		else
		aditing.setTerm(null);
		
//		设置报表上报的年份
		Node year=node.selectSingleNode("year");
		if(year!=null && !"".equals(year.getText().trim()))
		{
			aditing.setYear(Integer.valueOf(year.getText()));
		}
		else
		aditing.setYear(null);
		
		//设置报表的货币单位
		Node curr=node.selectSingleNode("fitechCurr");
		
		if(curr!=null && !"".equals(curr.getText().trim()))
		{			
			aditing.setCurrName(curr.getText());
		}
		else
			aditing.setCurrName("人民币");	
		
		
		//设置报表的报送范围
		Node range=node.selectSingleNode("dataRangeId");
		
		if(range!=null && !"".equals(range.getText().trim()))
		{			
			aditing.setDataRgId(Integer.valueOf(range.getText().trim()));
			aditing.setDataRgTypeName(getDataRgNameById(Integer.valueOf(range.getText().trim())));
		}
		else
		{
			aditing.setDataRgId(null);
			aditing.setDataRgTypeName(null);
		}
			
		
		//设置报送此报表的机构id
		Node orgId=node.selectSingleNode("orgId");
		if(orgId!=null && ! "".equals(orgId.getText().trim()))
		{
			aditing.setOrgName(orgId.getText());
		}
		else 
			aditing.setOrgName(null);
		
		aditing.set_repName(getRepName(aditing.getChildRepId(),aditing.getVersionId()));		
		return aditing;
	}
	
	
	
	
	/**
	 * 获得文件的名字通过子报表的id,版本号
	 */
	public String getRepName(String childId,String version)
	{
		String repname=null;
		
		DBConn conn=new DBConn();
		Session session=conn.openSession();
		
		String hql="from MChildReport mcr where 1=1 and mcr.comp_id.childRepId='"+childId+"'"
		+" and mcr.comp_id.versionId='"+version+"'";
		
		try {
			List list=session.find(hql);
			
			if(list!=null && list.size()>0)
			{
				MChildReport report=(MChildReport)list.get(0);
				repname=report.getReportName();
			}
			
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if(conn!=null)
			{
				conn.closeSession();
			}
		}
		return repname;		
	}
	
	
	/**
	 * 
	 */
	public String getDataRgNameById(Integer id)
	{
		String desc=null;
		
		DBConn conn=new DBConn();
		Session session=conn.openSession();
		
		String hql="from MDataRgType mdrt where mdrt.dataRangeId="+id;
		
		try {
			List list=session.find(hql);
			if(list!=null && list.size()>0)
			{
				MDataRgType mdrt=(MDataRgType)list.get(0);
				desc=mdrt.getDataRgDesc();
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if(conn!=null)
				conn.closeSession();
		}
		return desc;
	}
	
	
	
	/**
	 * 将文件移动到临时入库文件夹
	 */
	public void move(String fileName,String from,String to)
	{		
		try {		
			FileInputStream fips=new FileInputStream(from+fileName);
			
			FileOutputStream fops=new FileOutputStream(to+fileName);
				
			byte[] bytes=new byte[1024];
	          int index=0;
	          while((index=fips.read(bytes))!=-1)
	          {
	        	  fops.write(bytes,0,index);
	          }
	          fops.close();
	          fips.close();
	         
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}	
	
	
	
	
	/***
	 * 先解压缩文件,再判断是否要入库
	 * @param filers 所有的文件对象
	 */	
	
	public void checkXML(File[] filers,HttpServletRequest request,ActionMapping mapping)
	{
		for(int i=0;i<filers.length;i++)
		{
			if(filers[i].getName().endsWith(".zip"))
			{
				release(filers[i].getName(),Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.SERVICE_UP_TEMP+Config.SERVICE_UP_RELEASE,Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.SERVICE_UP_TEMP+Config.SERVICE_UP_RELEASE);
				File delete=new File(Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.SERVICE_UP_TEMP+Config.SERVICE_UP_RELEASE+filers[i].getName());
				if(delete!=null)
				{
					delete.delete();
				}
			}						
		}		
		
		File checkFile=new File(Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.SERVICE_UP_TEMP+Config.SERVICE_UP_RELEASE+"listing.xml");
		if(!checkFile.exists())
		{			
//			message.add("上传包中没有listing.xml文件,请重新上传");
//			request.setAttribute(Config.MESSAGE,message);
//			
//			mapping.findForward("upreport");
				
		}
		
		
		for(int j=0;j<filers.length;j++)
		{			
//			开始判断是否要入库
			if(filers[j].getName().equals("listing.xml"))
			{
				File file=new File(Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.SERVICE_UP_TEMP+Config.SERVICE_UP_RELEASE+"listing.xml");
				SAXReader reader=new SAXReader();
				try {
					Document doc=reader.read(file);
					List lists=doc.selectNodes("root/report");
					
					//先得到文件名字,看是否有此文件
					for(int n=0;n<lists.size();n++)
					{
						Node node=(Node)lists.get(n);
						String filename=getFileName(node);
						
						//看是否有此名字
						File newfile=new File(Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.SERVICE_UP_TEMP+Config.SERVICE_UP_RELEASE);
						
						if(newfile!=null)
						{	
							File[] newfiles=newfile.listFiles();
							for(int ne=0;ne<newfiles.length;ne++)
							{
								if((newfiles[ne].getName()).equals(filename))
								{										
									//有,判断是否需要入库(是,移动到into_xml,否,不做处理)									
//									if(isNeedInto(getReportByListing(node)))
//									{
//										move(filename,Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.SERVICE_UP_TEMP+Config.SERVICE_UP_RELEASE,Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.SERVICE_UP_TEMP+Config.SERVICE_UP_IN);																										
//									}
								}														
							}				
						}						
					}							
					
				} catch (DocumentException e) {
					e.printStackTrace();
					log.println("文件不存在");
				}				
			}			
		}
	}
		
	
	
	
//	public void checkExcel(File[] filers,Operator oper,UploadFileForm upFile)
//	{
//		for(int i=0;i<filers.length;i++)
//		{			
//			release(filers[i].getName(),Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.SERVICE_UP_TEMP+Config.SERVICE_UP_RELEASE,Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.SERVICE_UP_TEMP+Config.SERVICE_UP_RELEASE);						
//		}
//		
//		for(int i=0;i<filers.length;i++)
//		{			
//			Excel2XML.excel2xml(Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.SERVICE_UP_TEMP+Config.SERVICE_UP_RELEASE+filers[i].getName(),oper.getOrgId(),upFile.getVersionId());
//						
//			
//			File files=new File(Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.SERVICE_UP_TEMP+Config.SERVICE_UP_XML);
//			if(files!=null)
//			{
//				File[] allfiles=files.listFiles();
//				for(int j=0;j<allfiles.length;j++)
//				{					
////					if(isNeedInto(getReportByFile(allfiles[j],oper,upFile.getVersionId())))//来自数据库
////					{
////						//移动到into_xml
////						move(allfiles[j].getName(),Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.SERVICE_UP_TEMP+Config.SERVICE_UP_XML,Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.SERVICE_UP_TEMP+Config.SERVICE_UP_IN);
////					}
////					if(allfiles[j]!=null)
////					{
////						allfiles[j].delete();
////					}					
//				}							
//			}
//		}
//		this.remove(Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.SERVICE_UP_TEMP+Config.SERVICE_UP_RELEASE);
//	}
//	
	
	/**
	 * 获得文件名称
	 * @param node
	 * @return
	 */
	public String getFileName(Node node)
	{
		String orgId=((Node)node.selectSingleNode("orgId")).getText();
		String reportId=((Node)node.selectSingleNode("reportId")).getText();
		String versionId=((Node)node.selectSingleNode("version")).getText();
		String dataRangeId=((Node)node.selectSingleNode("dataRangeId")).getText();
		String year=((Node)node.selectSingleNode("year")).getText();
		String month=((Node)node.selectSingleNode("month")).getText();
		
		return orgId+reportId+versionId+dataRangeId+year+month+".xml";
	}

	
	
	
    /**
     * 获得机构的频度
     * @param in
     * @return
     */
    public static MActuRep  GetFreR(Aditing aditing)
    {
  	  DBConn conn=null;
  	  Session session = null;
  	  
  	  StringBuffer sql=new StringBuffer("from MActuRep M where 1=1 ");
  	  String version=aditing.getVersionId();
  	  String RepId=aditing.getChildRepId();
  	  if(RepId!=null && !RepId.equals(""))
  		  sql.append(" and M.MChildReport.comp_id.childRepId='"+RepId+"'");
  	  if(version!=null && !version.equals(""))
  		  sql.append(" and M.MChildReport.comp_id.versionId='"+version+"'");
  	 
  	  try
  	  {
  		  conn=new DBConn();
  		  session = conn.openSession();	            
            Query query = session.createQuery(sql.toString());
            
            List list = query.list();

            if (list != null) {
          	  for (Iterator it = list.iterator(); it.hasNext();) {
                   
          		  MActuRep reportInPersistence = (MActuRep) it.next();
          		  
                   return reportInPersistence;
                }
            }	                
  	  }
  	  catch(Exception ex)
  	  {
  		  ex.printStackTrace();
  	  }
  	finally {
        //如果连接存在，则断开，结束会话，返回
        if (conn != null)
            conn.closeSession();
    }
  	  return null;
    }



    /**
     * 建立文件夹
     */
    public void make(String filePath)
    {
    	File file=new File(filePath);    	
    	
    	if(!file.exists())
    	{
    		file.mkdir();
    	}
    }



    /**
     * 删除文件夹
     */
    public void remove(String filePath)
    {
    	File file=new File(filePath);
    	
    	if(file!=null && file.isDirectory())
    	{
    		File[] files=file.listFiles();
    		for(int i=0;i<files.length;i++)
    		{
    			if(files[i]!=null)
    			{
    				files[i].delete();
    			}
    		}
    	}
    	file.delete();
    }
    
    private Aditing getAditingByXML(File xmlFile,String versionId,String orgId,String year,String term,Operator operator){
    	Aditing aditing = new Aditing();
    	InputData inputData = new InputData();
    	
    	String title = null;
    	String subTitle = null;
		String mCurrName = null;
		String writer = null;
		String checker = null;
		String principal = null;
		
		MCurr mCurr = null;
		MDataRgType mrt = null;
		MRepRange mrr = null;
		MChildReport mChildReport = null;
		String dataRangeDes = null;
		
    	Element fileXmlRoot = XmlUtils.getRootElement(xmlFile); // XML数据文件的根元素
		String fileXmlRootName = fileXmlRoot.getName().toUpperCase();
		Element fileXmlRoot_1 = null; // 申明一些对象供使用
		Iterator fileXmlRoot_iterator = null;
		Element e = null;

		String fitechSubmitYear = null; // 报表的上报年份
		String fitechSubmitMonth = null; // 报表的上报月份

		try{
//			 下面根据根元素是否是F来判断是点对点式还是清单式
			if (fileXmlRootName.equals(ConfigOncb.UPPERELEMENT)) {
				for (fileXmlRoot_iterator = fileXmlRoot.elementIterator(); fileXmlRoot_iterator.hasNext();) {

					fileXmlRoot_1 = (Element) fileXmlRoot_iterator.next();
					if (fileXmlRoot_1.getName().toUpperCase().equals(ConfigOncb.SECONDUPPER)) { // 判断是否是元素P1
						title = inputData.getElementValue(fileXmlRoot_1,ConfigOncb.FITECHTITLE);
						subTitle = inputData.getElementValue(fileXmlRoot_1,ConfigOncb.FITECHSUBTITLE);
						fitechSubmitYear = inputData.getElementValue(fileXmlRoot_1,ConfigOncb.FITECHSUBMITYEAR);
						fitechSubmitMonth = inputData.getElementValue(fileXmlRoot_1,ConfigOncb.FITECHSUBMITMONTH);
						mCurrName = inputData.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHCURR);
						writer = inputData.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHFILLER); 
						checker = inputData.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHCHECKER);
						principal = inputData.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHPRINCIPAL);
						dataRangeDes = inputData.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHRANGE);
						if(title.indexOf("F")>-1)versionId=inputData.getElementValue(fileXmlRoot_1, "fitechVersion");
						
					}
				}
			} else { // 如果是清单式则从这里开始
				fileXmlRoot_iterator = fileXmlRoot.elementIterator(); // 如果是清单式则从这里开始
				fileXmlRoot_1 = (Element) fileXmlRoot_iterator.next();
				for (Iterator fileXmlRoot_iterator_1 = fileXmlRoot_1
						.elementIterator(); fileXmlRoot_iterator_1.hasNext();) {
					e = (Element) fileXmlRoot_iterator_1.next();
					if (inputData.isDetailHeader(e.getName())) { // 找到detailHeader元素
						title = inputData.getElementValue(e,ConfigOncb.FITECHTITLE);
						subTitle = inputData.getElementValue(e,ConfigOncb.FITECHSUBTITLE);
						
						fitechSubmitYear = inputData.getElementValue(e,ConfigOncb.FITECHSUBMITYEAR); // 报表上报的年份
						fitechSubmitMonth = inputData.getElementValue(e,ConfigOncb.FITECHSUBMITMONTH);
						dataRangeDes = inputData.getElementValue(e,ConfigOncb.FITECHRANGE);
						break;
					}
				}
				for (Iterator fileXmlRoot_iterator_1 = fileXmlRoot_1
						.elementIterator(); fileXmlRoot_iterator_1.hasNext();) {
					e = (Element) fileXmlRoot_iterator_1.next();
					if (inputData.isTotal(e.getName())){
						if(writer == null && checker == null && principal == null){
							writer = inputData.getElementValue(e, ConfigOncb.FITECHFILLER); 
							checker = inputData.getElementValue(e, ConfigOncb.FITECHCHECKER);
							principal = inputData.getElementValue(e, ConfigOncb.FITECHPRINCIPAL);
						}
						break;
					}
				}
				if (fileXmlRootName.equals(ConfigOncb.G5301)) { // 如果是G5301时
					title = inputData.getElementValue(fileXmlRoot_1,ConfigOncb.FITECHTITLE);
					subTitle = inputData.getElementValue(fileXmlRoot_1,ConfigOncb.FITECHSUBTITLE);
					
					fitechSubmitYear = inputData.getElementValue(fileXmlRoot,ConfigOncb.FITECHSUBMITYEAR);
					fitechSubmitMonth = inputData.getElementValue(fileXmlRoot,ConfigOncb.FITECHSUBMITMONTH);
					
				}
			}
			
			Integer templateType = com.fitech.net.config.Config.FR_TEMPLATE ;
			int count = -1;
			if(title != null && !title.equals("") && title.length()>2){
				
				String titleStr=title.trim().substring(0,2);
				if(titleStr.toUpperCase().indexOf(com.fitech.net.config.Config.FZ_SF_TAMPLATE)>-1  ||
						titleStr.toUpperCase().indexOf(com.fitech.net.config.Config.FZ_GF_TAMPLATE)>-1){
					templateType = com.fitech.net.config.Config.FZ_TEMPLATE;
					//  查看M_child_report 的reportName 是否和Excel的title+subTitle 匹配
					 count  =new StrutsMChildReportDelegate().IsMatchingReportName(title,subTitle, versionId);
				}
				
			}

			if(subTitle == null || subTitle.equals("")){
				mChildReport = new StrutsMChildReportDelegate().getMChildReportByReportName(title,title,versionId);
				aditing.setRepName(title);
			}	
			else  if(templateType.equals(com.fitech.net.config.Config.FZ_TEMPLATE) && count > 0){
				String reportName = title.trim().equals(subTitle.trim()) ? title.trim() : title.trim() + "-" + subTitle.trim();
				mChildReport = StrutsMChildReportDelegate.getFZMChildReportByReportName(reportName,versionId);
				aditing.setRepName(reportName);
			}else if(templateType.equals(com.fitech.net.config.Config.FZ_TEMPLATE) ){
				String reportName = title.trim().equals(subTitle.trim()) ? title.trim() : title.trim();
				mChildReport = StrutsMChildReportDelegate.getFZMChildReportByReportName(reportName,versionId);
				aditing.setRepName(reportName);
			}
			else{
				String reportName = title.trim().equals(subTitle.trim()) ? title.trim() : title.trim() + "-" + subTitle.trim();
				mChildReport = new StrutsMChildReportDelegate().getMChildReportByReportName(title,subTitle,versionId);
				aditing.setRepName(reportName);
			}
			
			
			if (mChildReport == null){ 
				this.messInfo = "报表报送失败，报表不存在！";
				return null;
			}
			
			if(mChildReport.getIsPublic().intValue() == 0){
				this.messInfo = "报表报送失败，该报表模板还未发布！";
				return null;
			}
			aditing.setChildRepId(mChildReport.getComp_id().getChildRepId());
			aditing.setVersionId(mChildReport.getComp_id().getVersionId());
			aditing.setOrgName(orgId);
			
			
			
	
			
			String childRepId = aditing.getChildRepId() != null && !aditing.getChildRepId().equals("") ? aditing.getChildRepId() : "";			
				
			if(fitechSubmitMonth != null && fitechSubmitYear != null){
				fitechSubmitYear = fitechSubmitYear.trim().replaceAll("　","");
				fitechSubmitMonth = fitechSubmitMonth.trim().replaceAll("　","");
				
				if(!fitechSubmitMonth.equals("") && !fitechSubmitYear.equals("")){
					try{
						aditing.setTerm(new Integer(fitechSubmitMonth));
						aditing.setYear(new Integer(fitechSubmitYear));
						if(!fitechSubmitYear.equals(year) || !fitechSubmitMonth.equals(term)){
							this.messInfo = childRepId + "报表报送失败，报表日期与查询的报表日期不一致！";
							return null;
						}
					}catch(Exception ex){
						this.messInfo = childRepId + "报表报送失败，请输入正确的报表日期！";
						return null;
					}
				}else{
					this.messInfo = childRepId + "报表报送失败，请输入报表日期！";
					return null;
				}
				
			}else{
				this.messInfo = childRepId + "报表报送失败，请输入报表日期！";
				return null;
			}
			
			
			if(mCurrName == null || mCurrName.equals("")) mCurrName = "人民币";
			mCurr = StrutsMCurrDelegate.getMCurr(mCurrName);
			if (mCurr == null) {
				this.messInfo = childRepId + "报表报送失败，币种不存在！";
				return null;
			}
			aditing.setCurrName(mCurrName);
			
			mrt = new StrutsMDataRgTypeDelegate().selectOneByName(dataRangeDes);
			if (mrt == null) {
				this.messInfo = childRepId + "报表报送失败，报送口径输入错误！";
				return null;
			}					
			
			mrr = StrutsMRepRangeDelegate.getMRepRanageOncb(orgId,mChildReport.getComp_id().getChildRepId(),mChildReport.getComp_id().getVersionId()); // 得到一个机构范围适用对象
			if (mrr == null){
				this.messInfo = childRepId + "报表报送失败，没有该报表的报送范围！";
				return null;
			}
			
			/**加上报送权限*/
			if(operator == null){
				this.messInfo = childRepId + "报表报送失败！";
				return null;								
			}					
			if(operator.isSuperManager() == false){
				if(operator.getChildRepReportPopedom() == null || operator.getChildRepReportPopedom().equals("")){
					this.messInfo = childRepId + "报表报送失败，没有该机构报送报送权限！";
					return null;
				}
				mrr = StrutsMRepRangeDelegate.getMRepRanage(orgId,mChildReport.getComp_id().getChildRepId(),mChildReport.getComp_id().getVersionId(),operator);
				if(mrr == null){
					this.messInfo = childRepId + "报表报送失败，没有该机构报送报送权限！";
					return null;
				}
			}
			
			boolean bool = StrutsReportInDelegate.isExistDataRange(dataRangeDes,mChildReport.getComp_id().getChildRepId(),mChildReport.getComp_id().getVersionId());
			
			if(bool == false){
				this.messInfo = childRepId + "报表报送失败，没有该报表的报送口径！";
				return null;
			}
			aditing.setDataRgId(mrt.getDataRangeId());
			aditing.setDataRgTypeName(mrt.getDataRgDesc());
			
			bool = StrutsMActuRepDelegate.getfreq(mChildReport.getComp_id().getChildRepId(),mChildReport.getComp_id().getVersionId(),mrt.getDataRangeId(),fitechSubmitMonth.trim());
			
			if(bool == false){
				this.messInfo = childRepId + "报表报送失败，没有该报表的报送频度！";
				return null;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		
    	return aditing;
    }
}
