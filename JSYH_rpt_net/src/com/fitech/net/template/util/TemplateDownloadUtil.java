package com.fitech.net.template.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.cbrc.smis.action.ViewMChildReportAction;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.hibernate.MChildReport;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.config.Config;

public class TemplateDownloadUtil {
	
	 private static FitechException log = new FitechException(ViewMChildReportAction.class);	
	    
	 private static FitechMessages messages=null;
	 
	 private static HttpServletRequest request=null;
	 
	 private static HttpServletResponse response=null;
	
	 private static ActionMapping mapping=null;
	 
	 
	 
	 
	 /**
	  * �����ӷ������������ļ����ͻ���,��������ص����ļ�
	  */
	 public static FitechMessages downOneCopy(String fileName,Operator oper)
	 {
		 File file=null; 
		 
		 file=new File(Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.OBTAIN_RELEASE+com.cbrc.smis.common.Config.FILESEPARATOR+oper.getOrgId()+com.cbrc.smis.common.Config.FILESEPARATOR+fileName+".xls");
			
			if(!file.exists())
			{
				file=new File(Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.TEMPLATE_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+fileName+".xls");
				if(!file.exists())
				{					
					 messages.add("�����ļ�������");		
					 return messages;
				}
			}
			
			response.reset();
			response.setContentType("application/vnd.ms-excel;name=\"" + fileName+".xls" + "\""); 
			response.addHeader("Content-Disposition",
					"attachment; filename=\""
							+ FitechUtil.toUtf8String(fileName) + "\"");
			response.setHeader("Accept-ranges", "bytes");    				
			   				
			FileInputStream fips;
			try {
				fips = new FileInputStream(file);
				
				byte[] bytes=new byte[1024];
				 int index=0;
				 while((index=fips.read(bytes,0,1024))!=-1)
				 {					
					 response.getOutputStream().write(bytes,0,index);
				 }
				 response.getOutputStream().close();
				 fips.close();
				 
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}						
			messages.add("���سɹ�");			
			return messages;
	 }
	 
	 
	 
	 
	 /**
     * �����ӷ������������ļ����ͻ���,��������ظ�ѡ�ļ�
     * @param fileName �ļ�����
     * @param response �������������
     */
    public static FitechMessages downCopy(String[] strs,HttpServletResponse response,Operator oper,String finalfileName)
    {    	
		File file=null;
		
		ArrayList files=new ArrayList();
	
		
		//����ʱ�ļ����е��ļ����ص��ͻ���
		for(int i=0;i<strs.length;i++)
		{				
			file=new File(Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.OBTAIN_RELEASE+com.cbrc.smis.common.Config.FILESEPARATOR+oper.getOrgId()+com.cbrc.smis.common.Config.FILESEPARATOR+strs[i]+".xls");
			
			if(!file.exists())
			{
				file=new File(Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.TEMPLATE_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+strs[i]+".xls");
				if(!file.exists())
				{
					continue;
				}
			}
			files.add(file);				
		}
		if(files.size()==0)
		{
			messages.add("û���ļ�����");
			return messages;
		}
		copyToTempZip(files.toArray(),finalfileName);
		
		response.reset();
		response.setContentType("application/x-zip-compressed;name=\"" + finalfileName+".zip" + "\""); 
		response.addHeader("Content-Disposition",
				"attachment; filename=\""
						+ FitechUtil.toUtf8String(finalfileName) + "\"");
		response.setHeader("Accept-ranges", "bytes");    				
		   				
		move(finalfileName,response);		
		
		messages.add("���سɹ�");
		return messages;		
    }
    
    
        
    /**
     * �����ӷ������������ļ����ͻ���,��������������ļ�
     * @param response �������������
     */
    public static FitechMessages downCopyAll(String finalfileName,HttpServletResponse response,Operator oper)
    {   	
		
		/**���ڷ�������������Ҫ���ص��ļ���ѹ����*/
		List records = null;
    	ArrayList files = null;
    	
    	if(oper.getOrgId() != null)
    	{
    		files = new ArrayList();
    		records = StrutsMChildReportDelegate.getTemplateDown(oper.getOrgId());
    	}    	
    	
    	
    	Iterator iter = records.iterator();
    	
    	while(iter != null && iter.hasNext())
    	{
    		File file = null;
    		
    		MChildReport report = (MChildReport)iter.next();
    		file=new File(Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.OBTAIN_RELEASE+com.cbrc.smis.common.Config.FILESEPARATOR+oper.getOrgId()+com.cbrc.smis.common.Config.FILESEPARATOR+getTemplateName(report));	    		
			if(file != null && !file.exists())
			{
				file=new File(Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.TEMPLATE_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+getTemplateName(report));
				if(file != null && !file.exists())
				{
					continue;
				}
			}				
    		files.add(file);
    	}	    	
    	
    	if(files.size()==0)
		{
			messages.add("û���ļ�����");
			return messages;
		}
    	copyToTempZip(files.toArray(),finalfileName);
    	
    	/**����ѹ�������*/
    	
    	
		response.reset();
		response.setContentType("application/x-zip-compressed;name=\"" + finalfileName+".zip" + "\""); 
		response.addHeader("Content-Disposition",
				"attachment; filename=\""
						+ FitechUtil.toUtf8String(finalfileName) + "\"");
		response.setHeader("Accept-ranges", "bytes");    				
		   				
		move(finalfileName,response);			
		
		messages.add("���سɹ�");
		return messages;
    }
        
    
    
    
    /**
     * �����ӷ������������ļ����ͻ���,������������е����µ��ļ�
     * @param response �������������
     */
    public static FitechMessages downCopyAllNew(String finalfileName,HttpServletResponse response,Operator oper)
    {    	
		/**���ڷ�������������Ҫ���ص��ļ���ѹ����*/
		List records = null;
		ArrayList files = null;
		
		if(oper.getOrgId() != null)
		{
			files = new ArrayList();
			records = StrutsMChildReportDelegate.getTemplateDown(oper.getOrgId());
		}    	
		
		
		Iterator iter = records.iterator();
		
		while(iter != null && iter.hasNext())
		{
			File file = null;
			
			MChildReport report = (MChildReport)iter.next();	    		
			
			String start=report.getStartDate();
			String  end=report.getEndDate();				
			
			if(start!=null && !"".equals(start) && end!=null &&!"".equals(end))
			{
				int starts=Integer.parseInt(start.replaceAll("-",""));
				int ends=Integer.parseInt(end.replaceAll("-",""));
				
				int nows=Integer.parseInt(getNowTime());
				
				if(nows>=starts && nows<=ends)
				{						
					//��ʼ����
					file=new File(Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.OBTAIN_RELEASE+com.cbrc.smis.common.Config.FILESEPARATOR+oper.getOrgId()+com.cbrc.smis.common.Config.FILESEPARATOR+getTemplateName(report));	    		
					if(file != null && !file.exists())
					{
						file=new File(Config.REAL_ROOT_PATH+Config.REPORT_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+Config.TEMPLATE_NAME+com.cbrc.smis.common.Config.FILESEPARATOR+getTemplateName(report));
						if(file != null && !file.exists())
						{
							continue;
						}
					}				
		    		files.add(file);						
				}					
			}	    		
		}	    	
		
		if(files.size()==0)
		{
			messages.add("û���ļ���Ҫ���»��ļ�������");
			return messages;
		}
		copyToTempZip(files.toArray(),finalfileName);
		
		/**����ѹ�������*/
		
		
		response.reset();
		response.setContentType("application/x-zip-compressed;name=\"" + finalfileName+".zip" + "\""); 
		response.addHeader("Content-Disposition",
				"attachment; filename=\""
						+ FitechUtil.toUtf8String(finalfileName) + "\"");
		response.setHeader("Accept-ranges", "bytes");    				
		move(finalfileName,response);		
		
		messages.add("���سɹ�");
		return messages;
    }    
      

    
    
    /**
     * �Ƚ����е��ļ���ɵ�ѹ�����ŵ��������˵���ʱĿ¼��
     * @param files ��Ҫ���ص������ļ�
     * @param finalfileName �ļ�����������(��Ϊ��Ϊѹ����)
     */
    public static void copyToTempZip(Object[] files,String finalfileName)
    {       	
		try
		{
			if(files!=null && files.length>0)
			{
				ZipOutputStream out=new ZipOutputStream(new FileOutputStream(Config.REAL_ROOT_PATH+finalfileName+".zip"));
				
				for(int i=0;i<files.length;i++)
				{
					File onefile=(File)files[i];
					FileInputStream fips=new FileInputStream(onefile);
					
					ZipEntry entry=new ZipEntry(onefile.getName());		
					out.putNextEntry(entry);
					
					byte[] bytes=new byte[1024];
					 int index=0;
					 while((index=fips.read(bytes,0,1024))!=-1)
					 {							 
						 out.write(bytes,0,index);				 
					 }				 
					 out.closeEntry();
					 fips.close();					 
				}
				out.close();
			}			
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		 
	}
    
    
    
    
    /**
     * ��zip���ļ��ƶ���ָ����λ��
     */
    public static void move(String finalfileName,HttpServletResponse response)
    {
    	FileInputStream fips;
		try {
			File file=new File(Config.REAL_ROOT_PATH+finalfileName+".zip");
			if(file.exists())
			{
				fips = new FileInputStream(file);
				
				byte[] bytes=new byte[1024];
				 int index=0;
				 while((index=fips.read(bytes,0,1024))!=-1)
				 {					
					 response.getOutputStream().write(bytes,0,index);
				 }
				 response.getOutputStream().close();
				 fips.close();
				 
				 file.delete();
			}			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.println("�ļ�������");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}							
		 	 
    }
    
    
    
    /**
     * ͨ��MChildReport �����ñ��������
     */
    public static String getTemplateName(MChildReport report)
    {
    	return report.getComp_id().getChildRepId()+"_"+report.getComp_id().getVersionId()+".xls";
    }

    


    /**
     * ��õ���ǰ��ϵͳʱ��
     * @return
     */
    public static String getNowTime()
    {
    	Calendar calendar=Calendar.getInstance(Locale.CHINA);
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		String now=format.format(calendar.getTime()).replaceAll("-","");
		
		return now;
    }



	/**
	 * @return Returns the request.
	 */
	public static HttpServletRequest getRequest() {
		return request;
	}



	/**
	 * @param request The request to set.
	 */
	public static void setRequest(HttpServletRequest request) {		
		TemplateDownloadUtil.request = request;
	}



	/**
	 * @return Returns the response.
	 */
	public static HttpServletResponse getResponse() {
		return response;
	}



	/**
	 * @param response The response to set.
	 */
	public static void setResponse(HttpServletResponse response) {
		TemplateDownloadUtil.response = response;
	}



	/**
	 * @return Returns the mapping.
	 */
	public static ActionMapping getMapping() {
		return mapping;
	}



	/**
	 * @param mapping The mapping to set.
	 */
	public static void setMapping(ActionMapping mapping) {
		TemplateDownloadUtil.mapping = mapping;
	}




	/**
	 * @return Returns the messages.
	 */
	public static FitechMessages getMessages() {
		return messages;
	}




	/**
	 * @param messages The messages to set.
	 */
	public static void setMessages(FitechMessages messages) {
		TemplateDownloadUtil.messages = messages;
	}       
    
}
