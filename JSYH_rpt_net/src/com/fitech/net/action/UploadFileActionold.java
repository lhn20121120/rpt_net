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
 * ���������ϴ��ļ�
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
		
		//����������ʾ��Ϣ
		ArrayList lists = new ArrayList();
		
		//����ҳ���Ƿ���ʾ��ť
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
        * ��ʼ����һϵ�е���ʱ�ļ���
        */
		this.make(Config.REAL_ROOT_PATH+Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP);     
		this.make(Config.REAL_ROOT_PATH+Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP + Config.SERVICE_UP_RELEASE); 
		this.make(Config.REAL_ROOT_PATH+Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP + Config.SERVICE_UP_RELEASE + orgId);
		this.make(Config.REAL_ROOT_PATH+Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP + Config.SERVICE_UP_XML);    
		this.make(Config.REAL_ROOT_PATH+Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP + Config.SERVICE_UP_IN);
            
        /**��ʼ�ϴ�*/      
      
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
        /**�ϴ����*/      
           
        /**��֤�Ƿ���Ҫ��ѹ��*/
		File file=new File(path+orgId);
		if(file!=null && file.isDirectory()){
			File[] files=file.listFiles(); 
			if(files!=null){
				for(int i=0;i<files.length;i++){
					String fileName=files[i].getName();    			  
    			  
					if(fileName!=null && !"".equals(fileName) && formFile.getFileName().equals(fileName)){
						/**����ϱ�����**/
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
										
										//����excelΪxml�ļ�
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
												if(!bool){//���ͼ�¼������    								  
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
											else messages.add("����ʧ�ܣ�Excel����ʧ�ܣ�");	
											continue;											
										}
									}									
									if(buttonFlag == false && buttonFlag2 == true) notshow = "not null";
								}											  					
							}
						/**����Excel�ϱ�����**/
						}else if(formFile.getContentType().equals(com.cbrc.smis.common.Config.FILE_CONTENTTYPE_EXCEL)){
							File onefile=new File(path + Config.SERVICE_UP_XML + orgId+File.separator);   
							if(!onefile.exists()) onefile.mkdir();
														
							Calendar calendar = Calendar.getInstance();
							StringBuffer buffer = new StringBuffer();
							buffer.append(calendar.get(Calendar.YEAR)).append(calendar.get(Calendar.MONTH)+1).append(calendar.get(Calendar.DATE))
											.append(calendar.get(Calendar.HOUR_OF_DAY)).append(calendar.get(Calendar.MINUTE)).append(calendar.get(Calendar.SECOND))
											.append(calendar.get(Calendar.MILLISECOND));
							
							//����excelΪxml�ļ�
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
									if(!bool){//���ͼ�¼������    								  
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
	    				 * ɾ����ѹ����ļ�
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
    	     * ���Ƿ�Ҫ���,��Ҫ�����ɾ��֮(Ҫ�����IntoTemplateAction��)
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
	 * ��ʼ��ѹ����
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
	 * ��ò����ϱ��ļ�¼�ļ���
	 */
	public ArrayList getNoUp()
	{
		//return this.lists;
		return null;
	}
	
	/**
	 * ͨ��lsiting���Aditing����
	 * 
	 */
	public Aditing getReportByListing(Node node)
	{
		Report report=new Report();
		Aditing aditing=new Aditing();
		
		//�����ӱ����id
		Node reportId=node.selectSingleNode("reportId");
		if(reportId!=null && !"".equals(reportId.getText().trim()))
		{
			aditing.setChildRepId(reportId.getText());
		}
		else
		aditing.setChildRepId(null);
		
		
		//���ñ���İ汾��
		Node version=node.selectSingleNode("version");
		if(version!=null && !"".equals(version.getText().trim()))
		{
			aditing.setVersionId(version.getText());
		}
		else
		aditing.setVersionId(null);
		
		//���ñ����ϱ����·�
		Node term=node.selectSingleNode("month");
		if(term!=null && !"".equals(term.getText().trim()))
		{
			aditing.setTerm(Integer.valueOf(term.getText()));
		}
		else
		aditing.setTerm(null);
		
//		���ñ����ϱ������
		Node year=node.selectSingleNode("year");
		if(year!=null && !"".equals(year.getText().trim()))
		{
			aditing.setYear(Integer.valueOf(year.getText()));
		}
		else
		aditing.setYear(null);
		
		//���ñ���Ļ��ҵ�λ
		Node curr=node.selectSingleNode("fitechCurr");
		
		if(curr!=null && !"".equals(curr.getText().trim()))
		{			
			aditing.setCurrName(curr.getText());
		}
		else
			aditing.setCurrName("�����");	
		
		
		//���ñ���ı��ͷ�Χ
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
			
		
		//���ñ��ʹ˱���Ļ���id
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
	 * ����ļ�������ͨ���ӱ����id,�汾��
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
	 * ���ļ��ƶ�����ʱ����ļ���
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
	 * �Ƚ�ѹ���ļ�,���ж��Ƿ�Ҫ���
	 * @param filers ���е��ļ�����
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
//			message.add("�ϴ�����û��listing.xml�ļ�,�������ϴ�");
//			request.setAttribute(Config.MESSAGE,message);
//			
//			mapping.findForward("upreport");
				
		}
		
		
		for(int j=0;j<filers.length;j++)
		{			
//			��ʼ�ж��Ƿ�Ҫ���
			if(filers[j].getName().equals("listing.xml"))
			{
				File file=new File(Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.SERVICE_UP_TEMP+Config.SERVICE_UP_RELEASE+"listing.xml");
				SAXReader reader=new SAXReader();
				try {
					Document doc=reader.read(file);
					List lists=doc.selectNodes("root/report");
					
					//�ȵõ��ļ�����,���Ƿ��д��ļ�
					for(int n=0;n<lists.size();n++)
					{
						Node node=(Node)lists.get(n);
						String filename=getFileName(node);
						
						//���Ƿ��д�����
						File newfile=new File(Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.SERVICE_UP_TEMP+Config.SERVICE_UP_RELEASE);
						
						if(newfile!=null)
						{	
							File[] newfiles=newfile.listFiles();
							for(int ne=0;ne<newfiles.length;ne++)
							{
								if((newfiles[ne].getName()).equals(filename))
								{										
									//��,�ж��Ƿ���Ҫ���(��,�ƶ���into_xml,��,��������)									
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
					log.println("�ļ�������");
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
////					if(isNeedInto(getReportByFile(allfiles[j],oper,upFile.getVersionId())))//�������ݿ�
////					{
////						//�ƶ���into_xml
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
	 * ����ļ�����
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
     * ��û�����Ƶ��
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
        //������Ӵ��ڣ���Ͽ��������Ự������
        if (conn != null)
            conn.closeSession();
    }
  	  return null;
    }



    /**
     * �����ļ���
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
     * ɾ���ļ���
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
		
    	Element fileXmlRoot = XmlUtils.getRootElement(xmlFile); // XML�����ļ��ĸ�Ԫ��
		String fileXmlRootName = fileXmlRoot.getName().toUpperCase();
		Element fileXmlRoot_1 = null; // ����һЩ����ʹ��
		Iterator fileXmlRoot_iterator = null;
		Element e = null;

		String fitechSubmitYear = null; // ������ϱ����
		String fitechSubmitMonth = null; // ������ϱ��·�

		try{
//			 ������ݸ�Ԫ���Ƿ���F���ж��ǵ�Ե�ʽ�����嵥ʽ
			if (fileXmlRootName.equals(ConfigOncb.UPPERELEMENT)) {
				for (fileXmlRoot_iterator = fileXmlRoot.elementIterator(); fileXmlRoot_iterator.hasNext();) {

					fileXmlRoot_1 = (Element) fileXmlRoot_iterator.next();
					if (fileXmlRoot_1.getName().toUpperCase().equals(ConfigOncb.SECONDUPPER)) { // �ж��Ƿ���Ԫ��P1
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
			} else { // ������嵥ʽ������￪ʼ
				fileXmlRoot_iterator = fileXmlRoot.elementIterator(); // ������嵥ʽ������￪ʼ
				fileXmlRoot_1 = (Element) fileXmlRoot_iterator.next();
				for (Iterator fileXmlRoot_iterator_1 = fileXmlRoot_1
						.elementIterator(); fileXmlRoot_iterator_1.hasNext();) {
					e = (Element) fileXmlRoot_iterator_1.next();
					if (inputData.isDetailHeader(e.getName())) { // �ҵ�detailHeaderԪ��
						title = inputData.getElementValue(e,ConfigOncb.FITECHTITLE);
						subTitle = inputData.getElementValue(e,ConfigOncb.FITECHSUBTITLE);
						
						fitechSubmitYear = inputData.getElementValue(e,ConfigOncb.FITECHSUBMITYEAR); // �����ϱ������
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
				if (fileXmlRootName.equals(ConfigOncb.G5301)) { // �����G5301ʱ
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
					//  �鿴M_child_report ��reportName �Ƿ��Excel��title+subTitle ƥ��
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
				this.messInfo = "������ʧ�ܣ��������ڣ�";
				return null;
			}
			
			if(mChildReport.getIsPublic().intValue() == 0){
				this.messInfo = "������ʧ�ܣ��ñ���ģ�廹δ������";
				return null;
			}
			aditing.setChildRepId(mChildReport.getComp_id().getChildRepId());
			aditing.setVersionId(mChildReport.getComp_id().getVersionId());
			aditing.setOrgName(orgId);
			
			
			
	
			
			String childRepId = aditing.getChildRepId() != null && !aditing.getChildRepId().equals("") ? aditing.getChildRepId() : "";			
				
			if(fitechSubmitMonth != null && fitechSubmitYear != null){
				fitechSubmitYear = fitechSubmitYear.trim().replaceAll("��","");
				fitechSubmitMonth = fitechSubmitMonth.trim().replaceAll("��","");
				
				if(!fitechSubmitMonth.equals("") && !fitechSubmitYear.equals("")){
					try{
						aditing.setTerm(new Integer(fitechSubmitMonth));
						aditing.setYear(new Integer(fitechSubmitYear));
						if(!fitechSubmitYear.equals(year) || !fitechSubmitMonth.equals(term)){
							this.messInfo = childRepId + "������ʧ�ܣ������������ѯ�ı������ڲ�һ�£�";
							return null;
						}
					}catch(Exception ex){
						this.messInfo = childRepId + "������ʧ�ܣ���������ȷ�ı������ڣ�";
						return null;
					}
				}else{
					this.messInfo = childRepId + "������ʧ�ܣ������뱨�����ڣ�";
					return null;
				}
				
			}else{
				this.messInfo = childRepId + "������ʧ�ܣ������뱨�����ڣ�";
				return null;
			}
			
			
			if(mCurrName == null || mCurrName.equals("")) mCurrName = "�����";
			mCurr = StrutsMCurrDelegate.getMCurr(mCurrName);
			if (mCurr == null) {
				this.messInfo = childRepId + "������ʧ�ܣ����ֲ����ڣ�";
				return null;
			}
			aditing.setCurrName(mCurrName);
			
			mrt = new StrutsMDataRgTypeDelegate().selectOneByName(dataRangeDes);
			if (mrt == null) {
				this.messInfo = childRepId + "������ʧ�ܣ����Ϳھ��������";
				return null;
			}					
			
			mrr = StrutsMRepRangeDelegate.getMRepRanageOncb(orgId,mChildReport.getComp_id().getChildRepId(),mChildReport.getComp_id().getVersionId()); // �õ�һ��������Χ���ö���
			if (mrr == null){
				this.messInfo = childRepId + "������ʧ�ܣ�û�иñ���ı��ͷ�Χ��";
				return null;
			}
			
			/**���ϱ���Ȩ��*/
			if(operator == null){
				this.messInfo = childRepId + "������ʧ�ܣ�";
				return null;								
			}					
			if(operator.isSuperManager() == false){
				if(operator.getChildRepReportPopedom() == null || operator.getChildRepReportPopedom().equals("")){
					this.messInfo = childRepId + "������ʧ�ܣ�û�иû������ͱ���Ȩ�ޣ�";
					return null;
				}
				mrr = StrutsMRepRangeDelegate.getMRepRanage(orgId,mChildReport.getComp_id().getChildRepId(),mChildReport.getComp_id().getVersionId(),operator);
				if(mrr == null){
					this.messInfo = childRepId + "������ʧ�ܣ�û�иû������ͱ���Ȩ�ޣ�";
					return null;
				}
			}
			
			boolean bool = StrutsReportInDelegate.isExistDataRange(dataRangeDes,mChildReport.getComp_id().getChildRepId(),mChildReport.getComp_id().getVersionId());
			
			if(bool == false){
				this.messInfo = childRepId + "������ʧ�ܣ�û�иñ���ı��Ϳھ���";
				return null;
			}
			aditing.setDataRgId(mrt.getDataRangeId());
			aditing.setDataRgTypeName(mrt.getDataRgDesc());
			
			bool = StrutsMActuRepDelegate.getfreq(mChildReport.getComp_id().getChildRepId(),mChildReport.getComp_id().getVersionId(),mrt.getDataRangeId(),fitechSubmitMonth.trim());
			
			if(bool == false){
				this.messInfo = childRepId + "������ʧ�ܣ�û�иñ���ı���Ƶ�ȣ�";
				return null;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		
    	return aditing;
    }
}
