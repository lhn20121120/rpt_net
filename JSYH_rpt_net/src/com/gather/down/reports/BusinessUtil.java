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
	  * @function 生成报表(blog)到相应的目录(全部的)
	  * @return boolean 成功 true 失败 falses
	  */
	 public String makeReport(){
         //生成report
		 String fileName=makeReport(getAllRepInfo());
		 //回填下载日志表
		 refillDownloadLog(getAllRepInfo());    
		 return fileName;
	 }
	 /**
	  * @author linfeng
	  * @function 生成报表(blog)到相应的目录(所有新的)
	  * @param String newRep  参数值没有实际意义，仅为了利用重载的功能
	  * @return boolean 成功 true 失败 falses
	  */
	 public String makeReport(String newRep){
		 //得到新模板信息列表
		 List newRepList=getDownloadInfo();
		 // System.out.println("------------1---------------");
		 //生成report
		 String fileName=makeReport(newRepList);
		 // System.out.println("-------------2--------------");
         //回填下载日志表
		 refillDownloadLog(newRepList);
		 // System.out.println("-------------3--------------");
		 return fileName;
	 }
	 /**
	  * @author linfeng
	  * @function 生成报表(blog)到相应的目录
	  * @param reportInfoList
	  * @return boolean 成功 true 失败 falses
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
        
         //写blob到文件
         List pdfList=null;
         for(int i=0;i<blobList.size();i++){
        	 ReportData repData=(ReportData)blobList.get(i);
        	 String pdfFileName= repData.getComp_id().getChildRepId()+repData.getComp_id().getVersionId()+".pdf";
        	 File pdf=new BlobMaker().make(repData.getPdf(),pdfFileName);
        	 pdfList=new ArrayList();
        	 if(pdf!=null){
        		 //压缩blob文件到zip包
        		 pdfList.add(pdf);
        	 }else{
        		 return null;
        	 }
        	 
         }
         if(pdfList==null) return null;
         
         
         /*
          * 俞炎提出，pdf不压缩到里层的zip包,只需和外面的基础信息放到一起
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
	 
	 //最终打包所有文件到zip包中
	 public String zipAllFiles(List pdfFiles){
		 String dir=AutoSynchronized.SMMIS_PATH+Config.FILESEPARATOR+Config.DOWNLOAD_REPORT+Config.FILESEPARATOR+"listing";
		 String resultDir=AutoSynchronized.SMMIS_PATH+Config.FILESEPARATOR+Config.DOWNLOAD_REPORT+Config.FILESEPARATOR+"result";
		 File dirFile=new File(dir);
		 File[] files=dirFile.listFiles();
		 // System.out.println("-----The base file info size is: "+files.length);
		 File[] resultFiles=new File[pdfFiles.size()+files.length];
		 //List tempList=new ArrayList();
		 for(int i=0;i<files.length;i++){
			 //加入基本信息文件
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
	 
	 //得到所有机构的报表信息列表，供查询
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
	 
	 //得到所有机构已经下载的模板的信息列表
	 private List getDownloadInfo(){
		 List dlList=StrutsRepRangeDownLogDelegate.getInfoByOrgIds(orgIds);  //已经下载的记录
		 List crList=getAllRepInfo();   //所有的
		 if(dlList==null || dlList.size()<1) return crList;
		 if(crList==null || crList.size()<1) return null;
		 List toList=new ArrayList();
		 Map crCompare=new Hashtable();
		 //比较，并提取出新的模板数据
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
			 //如果状态值为1，说明模板时间有改动，需要下载
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
	 
	 //回填所有数据和状态
	 private void refillDownloadLog(List repInfoList){
		 if(repInfoList==null) return;
		 // System.out.println("repInfoList size is:"+repInfoList.size());
		 //把list转换为map,利于比较
		 Map compare=new Hashtable();
		 for(int i=0;i<repInfoList.size();i++){
			 ReportInfoBean myBean=(ReportInfoBean)repInfoList.get(i);
			 String key=myBean.getFormModelID().trim()+myBean.getVersion().trim();
			 // System.out.println("the key value is: "+key);
			 compare.put(key,"");
		 }
		 //循环 ：per times 得到一个机构的历史信息，
		 //然后比对：如果发现，就更新，如果没有，就插入
		 for(int i=0;i<orgIds.length;i++){
			 // System.out.println("-------orgIds.length is: "+orgIds.length);
			 List oneOrgLogList=getDownloadLogByOrgId(orgIds[i]);
			 if(oneOrgLogList==null || oneOrgLogList.size()<1){
				 //如果记录为空，插入所有下载记录信息
				 for(int j=0;j<repInfoList.size();j++){
					 ReportInfoBean myBean=(ReportInfoBean)repInfoList.get(j);
					 //插入
					 myBean.setOrgId(orgIds[i]);
					 StrutsRepRangeDownLogDelegate.insertOne(myBean.getFormModelID().trim(),myBean.getVersion().trim(),myBean.getOrgId(),new Integer(0));
				 }
			 }else{
				 //否则，进行比较,然后插入或更新数据
				 for(int j=0;j<oneOrgLogList.size();j++){
					 RepRangeDownLog logBean=(RepRangeDownLog)oneOrgLogList.get(j);
					 String logKey=logBean.getComp_id().getChildRepId().trim()+logBean.getComp_id().getVersionId().trim();
					 String repId=logBean.getComp_id().getChildRepId();
					 String orgId=logBean.getComp_id().getOrgId();
					 String versionId=logBean.getComp_id().getVersionId();
					 if(compare.containsKey(logKey)){
						 //更新
						 StrutsRepRangeDownLogDelegate.updateState(repId,versionId,orgId);
					 }else{
						 //插入
						 StrutsRepRangeDownLogDelegate.insertOne(repId,versionId,orgId,new Integer(0));
					 }
					 
				 }
			 }
			
		 }
	 }
	 
	 //得到莫机构的下载记录 list
	 private List getDownloadLogByOrgId(String orgId){
		 return StrutsRepRangeDownLogDelegate.getInfoByOrgId(orgId);
	 }
	 
	//处理生成所有代报关系数据和相关基础数据
	public  boolean makeBaseInfo(){
		FileContainer container=new FileContainer();
		container.add(getAgentRelationList());      //代报关系
		container.add(getDataRangeList());          //数据范围
		container.add(getDelayTimeList());          //子报表延迟时间
		container.add(getReferRelationRangeList()); //机构模板报送范围
		container.add(getReportInfoList());         //模板信息
		container.add(getFrequency());              //频率
		List fileList=container.getFileList();
		FileMaker maker=new FileMaker();
		if(maker.make(fileList)) return true;
		return false;
	}
	
     /**
      * @author linfeng
      * @functiong 得到代报关系信息(机构对机构)
      * @param orgId String 机构id
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
      * @functiong 得到数据范围信息
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
      * @functiong 得到延迟时间信息
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
      * @functiong 得到报送关系信息(机构对报表)
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
      * @functiong 得到模板信息
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
