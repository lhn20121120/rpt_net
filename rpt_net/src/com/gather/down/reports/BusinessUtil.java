package com.gather.down.reports;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.gather.adapter.StrutsMActuRepDelegate;
import com.gather.adapter.StrutsMDataRgTypeDelegate;
import com.gather.adapter.StrutsMRepRangeDelegate;
import com.gather.adapter.StrutsMappingRelationDelegate;
import com.gather.adapter.StrutsRepRangeDownLogDelegate;
import com.gather.adapter.StrutsReportDataDelegate;
import com.gather.common.BusinessCommonUtil;
import com.gather.common.Config;
import com.gather.common.DateUtil;
import com.gather.common.ZipUtils;
import com.gather.hibernate.MActuRep;
import com.gather.hibernate.MappingRelation;
import com.gather.hibernate.RepRangeDownLog;
import com.gather.hibernate.ReportData;
import com.gather.servlets.AutoSynchronized;
import com.gather.struts.forms.MChildReportForm;
import com.gather.struts.forms.MDataRgTypeForm;
import com.gather.struts.forms.MRepFreqForm;
import com.gather.struts.forms.MRepRangeForm;

public class BusinessUtil {
	 HttpSession session=null;
	 String orgId="";
	 String[] orgIds=null;
	 String[] childRepIds=null;
	
	 public    BusinessUtil(HttpSession session){
		 this.session=session;
		  orgId=((String)session.getAttribute("orgId")).trim();
		  orgIds=BusinessCommonUtil.getOrgId(orgId);
		  childRepIds=BusinessCommonUtil.getSubReportId(orgIds);
	 }
	 
	 /**
	  * @author linfeng
	  * @function ���ɱ���(blog)����Ӧ��Ŀ¼(ȫ����)
	  * @return boolean �ɹ� true ʧ�� falses
	  */
	 public String makeReport(){
         //����report
		 String fileName=makeReport(getAllRepInfo());
		 //����������־��
		 refillDownloadLog(getAllRepInfo());    
		 return fileName;
	 }
	 /**
	  * @author linfeng
	  * @function ���ɱ���(blog)����Ӧ��Ŀ¼(�����µ�)
	  * @param String newRep  ����ֵû��ʵ�����壬��Ϊ���������صĹ���
	  * @return boolean �ɹ� true ʧ�� falses
	  */
	 public String makeReport(String newRep){
		 //�õ���ģ����Ϣ�б�
		 List newRepList=getDownloadInfo();
		 // System.out.println("------------1---------------");
		 //����report
		 String fileName=makeReport(newRepList);
		 // System.out.println("-------------2--------------");
         //����������־��
		 refillDownloadLog(newRepList);
		 // System.out.println("-------------3--------------");
		 return fileName;
	 }
	 /**
	  * @author linfeng
	  * @function ���ɱ���(blog)����Ӧ��Ŀ¼
	  * @param reportInfoList
	  * @return boolean �ɹ� true ʧ�� falses
	  */
	 public String makeReport(List reportInfoList){
		 if(reportInfoList==null) reportInfoList=getAllRepInfo();
         List blobList=StrutsReportDataDelegate.getPdfs(reportInfoList);
         // System.out.println();
         if(blobList!=null && blobList.size()>0){
        	 //// System.out.println("----blobList.size() is: "+blobList.size());
         }else{
        	 //// System.out.println("-----no find and blobList-------------");
        	 return null;
         }
        
         //дblob���ļ�
         List pdfList=null;
         for(int i=0;i<blobList.size();i++){
        	 ReportData repData=(ReportData)blobList.get(i);
        	 String pdfFileName= repData.getComp_id().getChildRepId()+repData.getComp_id().getVersionId()+".pdf";
        	 File pdf=new BlobMaker().make(repData.getPdf(),pdfFileName);
        	 pdfList=new ArrayList();
        	 if(pdf!=null){
        		 //ѹ��blob�ļ���zip��
        		 pdfList.add(pdf);
        	 }else{
        		 return null;
        	 }
        	 
         }
         if(pdfList==null) return null;
         
         
         /*
          * ���������pdf��ѹ��������zip��,ֻ�������Ļ�����Ϣ�ŵ�һ��
          * 
         // File[] pdfs=new File[pdfList.size()];
         // pdfs=(File[])pdfList.toArray(pdfs);
         //String outDir=AutoSynchronized.SMMIS_PATH+Config.FILESEPARATOR+Config.DOWNLOAD_REPORT+Config.FILESEPARATOR+"listing";
        // try{
        //     ZipUtils.zipFiles(pdfs,outDir,"1.zip");
        // }catch(Exception e){
        //	 e.printStackTrace();
        //	 return null;
        // }
         */
         String fileName=zipAllFiles(pdfList);
         
         if(fileName==null) return null;
         
		 return fileName;
	 }
	 
	 //���մ�������ļ���zip����
	 public String zipAllFiles(List pdfFiles){
		 String dir=AutoSynchronized.SMMIS_PATH+Config.FILESEPARATOR+Config.DOWNLOAD_REPORT+Config.FILESEPARATOR+"listing";
		 String resultDir=AutoSynchronized.SMMIS_PATH+Config.FILESEPARATOR+Config.DOWNLOAD_REPORT+Config.FILESEPARATOR+"result";
		 File dirFile=new File(dir);
		 File[] files=dirFile.listFiles();
		 // System.out.println("-----The base file info size is: "+files.length);
		 File[] resultFiles=new File[pdfFiles.size()+files.length];
		 //List tempList=new ArrayList();
		 for(int i=0;i<files.length;i++){
			 //���������Ϣ�ļ�
			 pdfFiles.add(files[i]);
		 }
		 resultFiles=(File[])pdfFiles.toArray(resultFiles);
		 String fileName=DateUtil.date2long(new Date())+".zip";
		 try{
		      ZipUtils.zipFiles(resultFiles,resultDir,fileName);
		 }catch(Exception e){
			 e.printStackTrace();
			 return null;
		 }
		 //// System.out.println("----fileName in class BusinessUtil "+fileName);
		 return fileName;
	 }
	 
	 //�õ����л����ı�����Ϣ�б�����ѯ
	 private List getAllRepInfo(){
		 List reportInfoList=new ArrayList();
		 List list=BusinessCommonUtil.getMySubReports(BusinessCommonUtil.getSubReportId(BusinessCommonUtil.getOrgId(orgId)));
		 if(list!=null && list.size()>0){
			 for(int i=0;i<list.size();i++){
				 MChildReportForm mcrBean=(MChildReportForm)list.get(i);
				 ReportInfoBean myBean=new ReportInfoBean();
				 myBean.setFormModelID(mcrBean.getChildRepId());
				 myBean.setVersion(mcrBean.getVersionId());
				 reportInfoList.add(myBean);
			 }
			 return reportInfoList;
		 }
		 return null;
	 }
	 
	 //�õ����л����Ѿ����ص�ģ�����Ϣ�б�
	 private List getDownloadInfo(){
		 List dlList=StrutsRepRangeDownLogDelegate.getInfoByOrgIds(orgIds);  //�Ѿ����صļ�¼
		 List crList=getAllRepInfo();   //���е�
		 if(dlList==null || dlList.size()<1) return crList;
		 if(crList==null || crList.size()<1) return null;
		 List toList=new ArrayList();
		 Map crCompare=new Hashtable();
		 //�Ƚϣ�����ȡ���µ�ģ������
		 for(int i=0;i<crList.size();i++){
			 ReportInfoBean myBean=(ReportInfoBean)crList.get(i);
			 String subId=myBean.getFormModelID().trim();
			 String version=myBean.getVersion().trim();
			 crCompare.put(subId+version,"");
		 }
		 Map dlCompare=new Hashtable();
		 for(int j=0;j<dlList.size();j++){
			 RepRangeDownLog logBean=(RepRangeDownLog)dlList.get(j);
			 String logSubId=logBean.getComp_id().getChildRepId().trim();
			 String logVersion=logBean.getComp_id().getVersionId().trim();
			 String key=logSubId+logVersion;
			 //���״ֵ̬Ϊ1��˵��ģ��ʱ���иĶ�����Ҫ����
			 if(logBean.getState()!=null || logBean.getState().intValue()==1){
				 ReportInfoBean toBean=new ReportInfoBean();
				 toBean.setFormModelID(logBean.getComp_id().getChildRepId().trim());
				 toBean.setVersion(logBean.getComp_id().getVersionId());
				 toBean.setOrgId(logBean.getComp_id().getOrgId().trim());
				 dlCompare.put(key,toBean);
			 }
	         if(!crCompare.containsKey(key)){
				 ReportInfoBean toBean=new ReportInfoBean();
				 toBean.setFormModelID(logBean.getComp_id().getChildRepId().trim());
				 toBean.setVersion(logBean.getComp_id().getVersionId());
				 toBean.setOrgId(logBean.getComp_id().getOrgId().trim());
				 dlCompare.put(key,toBean);
	         }
		 }
         Iterator iter=dlCompare.values().iterator();
         while(iter.hasNext()){
        	 toList.add(iter.next());
         }
         return toList;
	 }
	 
	 //�����������ݺ�״̬
	 private void refillDownloadLog(List repInfoList){
		 if(repInfoList==null) return;
		 // System.out.println("repInfoList size is:"+repInfoList.size());
		 //��listת��Ϊmap,���ڱȽ�
		 Map compare=new Hashtable();
		 for(int i=0;i<repInfoList.size();i++){
			 ReportInfoBean myBean=(ReportInfoBean)repInfoList.get(i);
			 String key=myBean.getFormModelID().trim()+myBean.getVersion().trim();
			 // System.out.println("the key value is: "+key);
			 compare.put(key,"");
		 }
		 //ѭ�� ��per times �õ�һ����������ʷ��Ϣ��
		 //Ȼ��ȶԣ�������֣��͸��£����û�У��Ͳ���
		 for(int i=0;i<orgIds.length;i++){
			 // System.out.println("-------orgIds.length is: "+orgIds.length);
			 List oneOrgLogList=getDownloadLogByOrgId(orgIds[i]);
			 if(oneOrgLogList==null || oneOrgLogList.size()<1){
				 //�����¼Ϊ�գ������������ؼ�¼��Ϣ
				 for(int j=0;j<repInfoList.size();j++){
					 ReportInfoBean myBean=(ReportInfoBean)repInfoList.get(j);
					 //����
					 myBean.setOrgId(orgIds[i]);
					 StrutsRepRangeDownLogDelegate.insertOne(myBean.getFormModelID().trim(),myBean.getVersion().trim(),myBean.getOrgId(),new Integer(0));
				 }
			 }else{
				 //���򣬽��бȽ�,Ȼ�������������
				 for(int j=0;j<oneOrgLogList.size();j++){
					 RepRangeDownLog logBean=(RepRangeDownLog)oneOrgLogList.get(j);
					 String logKey=logBean.getComp_id().getChildRepId().trim()+logBean.getComp_id().getVersionId().trim();
					 String repId=logBean.getComp_id().getChildRepId();
					 String orgId=logBean.getComp_id().getOrgId();
					 String versionId=logBean.getComp_id().getVersionId();
					 if(compare.containsKey(logKey)){
						 //����
						 StrutsRepRangeDownLogDelegate.updateState(repId,versionId,orgId);
					 }else{
						 //����
						 StrutsRepRangeDownLogDelegate.insertOne(repId,versionId,orgId,new Integer(0));
					 }
					 
				 }
			 }
			
		 }
	 }
	 
	 //�õ�Ī���������ؼ�¼ list
	 private List getDownloadLogByOrgId(String orgId){
		 return StrutsRepRangeDownLogDelegate.getInfoByOrgId(orgId);
	 }
	 
	//�����������д�����ϵ���ݺ���ػ�������
	public  boolean makeBaseInfo(){
		FileContainer container=new FileContainer();
		container.add(getAgentRelationList());      //������ϵ
		container.add(getDataRangeList());          //���ݷ�Χ
		container.add(getDelayTimeList());          //�ӱ����ӳ�ʱ��
		container.add(getReferRelationRangeList()); //����ģ�屨�ͷ�Χ
		container.add(getReportInfoList());         //ģ����Ϣ
		container.add(getFrequency());              //Ƶ��
		List fileList=container.getFileList();
		FileMaker maker=new FileMaker();
		if(maker.make(fileList)) return true;
		return false;
	}
	
     /**
      * @author linfeng
      * @functiong �õ�������ϵ��Ϣ(�����Ի���)
      * @param orgId String ����id
      * @return AgentRelationBean list
      */
     public List getAgentRelationList(){
    	 List mrList=StrutsMappingRelationDelegate.findById(orgId);
    	 if(mrList!=null && mrList.size()>0){
    		 List list=new ArrayList();
    		 for(int i=0;i<mrList.size();i++){
    			 MappingRelation mrBean=(MappingRelation)mrList.get(i);
    			 AgentRelationBean myBean=new AgentRelationBean();
        		 myBean.setId(i);
        		 myBean.setParentAffiliationID(orgId.trim());
        		 myBean.setSonAffiliationID(mrBean.getComp_id().getReplaceOrgId().trim());
        		 myBean.setVailid("");
        		 myBean.setStartTime(DateUtil.toSimpleDateFormat(mrBean.getStartDate(),DateUtil.NORMALDATE));
        		 myBean.setEndTime(DateUtil.toSimpleDateFormat(mrBean.getEndDate(),DateUtil.NORMALDATE));
        		 list.add(myBean);
    		 }
    		 return list;
    	 }
    	 return null;
     }
     
     /**
      * @author linfeng
      * @functiong �õ����ݷ�Χ��Ϣ
      * @return DataRangeBean list
      */
     public  List getDataRangeList(){
    	 List mrtList=StrutsMDataRgTypeDelegate.findAll();
    	 if(mrtList!=null && mrtList.size()>0){
    		 List list=new ArrayList();
    		 for(int i=0;i<mrtList.size();i++){
    			 // System.out.println("mrtList.class is:"+mrtList.get(i).getClass().getName());
    			 MDataRgTypeForm mrtBean=(MDataRgTypeForm)mrtList.get(i);
    			 DataRangeBean myBean=new DataRangeBean();
        		 myBean.setRangeID(String.valueOf(mrtBean.getDataRangeId()));
        		 myBean.setReageName(mrtBean.getDataRgDesc());
        		 list.add(myBean);
    		 }
    		 return list;
    	 }
    	 return null;
     }
     
     /**
      * @author linfeng
      * @functiong �õ��ӳ�ʱ����Ϣ
      * @return DelayTimeBean list
      */
     public  List getDelayTimeList(){
    	 List arList=StrutsMActuRepDelegate.getActuRepByChildRepIds(childRepIds);
    	 List list=new ArrayList();
    	 if(arList!=null && arList.size()>0){
    		 for(int i=0;i<arList.size();i++){
    			 MActuRep arBean=(MActuRep)arList.get(i);
    			 DelayTimeBean myBean=new DelayTimeBean();
    			 myBean.setFormModelID(arBean.getComp_id().getChildRepId());
    			 myBean.setFormModelVersion(arBean.getComp_id().getVersionId());
    			 myBean.setFrequencyID(String.valueOf(arBean.getComp_id().getRepFreqId()));
    			 myBean.setRangeID(String.valueOf(arBean.getComp_id().getDataRangeId()));
    			 myBean.setCommonDay(String.valueOf(arBean.getNormalTime()));
    			 myBean.setUncommonDay(String.valueOf(arBean.getDelayTime()));
    			 list.add(myBean);    			 
    		 }
    		 return list;
    	 }
        return null;
     }
     
     /**
      * @author linfeng
      * @functiong �õ����͹�ϵ��Ϣ(�����Ա���)
      * @return ReferRelationRangeBean list
      */
     public  List getReferRelationRangeList(){
    	 List repRangeList=StrutsMRepRangeDelegate.getSubReportIds(orgIds);
    	 
    	 if(repRangeList!=null && repRangeList.size()>0){
    		 List list=new ArrayList();
    		 for(int i=0;i<repRangeList.size();i++){
    			 MRepRangeForm mrrBean=(MRepRangeForm)repRangeList.get(i);
    			 ReferRelationRangeBean myBean=new ReferRelationRangeBean();
        		 myBean.setFormModelID(String.valueOf(mrrBean.getChildRepId()).trim());
        		 myBean.setFormModelVersion(mrrBean.getVersionId().trim());
        		 myBean.setAffiliationID(mrrBean.getOrgId().trim());
        		 list.add(myBean);
    		 }
    		 return list;
    	 }
    	 return null;
     }
     
     /**
      * @author linfeng
      * @functiong �õ�ģ����Ϣ
      * @return ReporInfoBean list
      */
     public List getReportInfoList(){
    	 List namelist=BusinessCommonUtil.getMySubReports(childRepIds);
    	 List list=new ArrayList();
    	 if(namelist!=null && namelist.size()>0){
    		 for(int i=0;i<namelist.size();i++){
    			 MChildReportForm crBean=(MChildReportForm)namelist.get(i);
    			 ReportInfoBean myBean=new ReportInfoBean();
    			 myBean.setParentName(BusinessCommonUtil.getMRepName(crBean.getRepId()));
    			 myBean.setSonName(crBean.getReportName());
    			 myBean.setAddress("");
    			 myBean.setStartTime(DateUtil.toSimpleDateFormat(crBean.getStartDate(),DateUtil.NORMALDATE));
    			 myBean.setEndTime(DateUtil.toSimpleDateFormat(crBean.getEndDate(),DateUtil.NORMALDATE));
    			 myBean.setFormModelID(crBean.getChildRepId());
    			 myBean.setVersion(crBean.getVersionId());
    			 myBean.setFile("");
    			 list.add(myBean);
    		 }
    		 return list;
    	 }
    	 return null;
     }
     
     public List getFrequency(){
    	 List frList=BusinessCommonUtil.getAllFrequency();
    	 List list=new ArrayList();
    	 if(frList!=null && frList.size()>0){
    		 for(int i=0;i<frList.size();i++){
    			 MRepFreqForm frBean=(MRepFreqForm)frList.get(i);
    			 FrequencyBean myBean=new FrequencyBean();
    			 myBean.setFreqId(String.valueOf(frBean.getRepFreqId()));
    			 myBean.setFreqName(frBean.getRepFreqName());
    			 list.add(myBean);
    		 }
    		 return list;
    	 }
    	 return null;
     }

}
