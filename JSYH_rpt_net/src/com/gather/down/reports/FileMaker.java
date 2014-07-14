package com.gather.down.reports;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import com.gather.common.Config;
import com.gather.servlets.AutoSynchronized;

public class FileMaker {
    
    public static final String dir=AutoSynchronized.SMMIS_PATH+Config.FILESEPARATOR+Config.FILESEPARATOR+Config.DOWNLOAD_REPORT+Config.FILESEPARATOR+"listing";
    
	public FileMaker(){}
	
	public boolean make(List fileList){  //全部成功返回 true
		if(fileList==null )return false;
		boolean flag=true;
		for(int i=0;i<fileList.size();i++){
			if(!process((List)fileList.get(i))) return false;
		}
		return true;
	}
	
	private boolean process(List beanList){
		// System.out.println("---list is: "+beanList.get(0).getClass().getName());
		if(beanList==null) return false;
	    Object	obj = beanList.get(0);
	    StringBuffer str=new StringBuffer();
		if(obj instanceof ReportInfoBean){
			for(int i=0;i<beanList.size();i++){
				ReportInfoBean bean=(ReportInfoBean)beanList.get(i);
			    
			    str.append(bean.getParentName()+",");
			    str.append(bean.getSonName()+",");
			    str.append(bean.getAddress()+",");
			    str.append(bean.getStartTime()+",");
			    str.append(bean.getEndTime()+",");
			    str.append(bean.getFormModelID()+",");
			    str.append(bean.getVersion()+",");
			    str.append(bean.getFile());
			    str.append("\n");
			}
			return write(FileContainer.reportInfo,str);
		}else if(obj instanceof ReferRelationRangeBean){
			for(int i=0;i<beanList.size();i++){
				ReferRelationRangeBean bean=(ReferRelationRangeBean)beanList.get(i);
				
				str.append(bean.getAffiliationID()+",");
				str.append(bean.getFormModelID()+",");
				str.append(bean.getFormModelVersion());
				str.append("\n");
			}
			return write(FileContainer.referRelationRange,str);
		}else if(obj instanceof DelayTimeBean){
			for(int i=0;i<beanList.size();i++){
				DelayTimeBean bean=(DelayTimeBean)beanList.get(i);
				
				str.append(bean.getFormModelID()+",");
				str.append(bean.getFormModelVersion()+",");
				str.append(bean.getRangeID()+",");
				str.append(bean.getFrequencyID()+",");
				str.append(bean.getCommonDay()+",");
				str.append(bean.getUncommonDay());
				str.append("\n");
			}
			return write(FileContainer.delayTime,str);
		}else if(obj instanceof DataRangeBean){
			for(int i=0;i<beanList.size();i++){
				DataRangeBean bean=(DataRangeBean)beanList.get(i);
				
				str.append(bean.getRangeID()+",");
				str.append(bean.getReageName());
				str.append("\n");
			}
			return write(FileContainer.dataRange,str);
		}else if(obj instanceof AgentRelationBean){
			for(int i=0;i<beanList.size();i++){
				AgentRelationBean bean=(AgentRelationBean)beanList.get(i);
				
				str.append(bean.getParentAffiliationID()+",");
				str.append(bean.getSonAffiliationID()+",");
				str.append(bean.getVailid()+",");
				str.append(bean.getStartTime()+",");
				str.append(bean.getEndTime()+",");
				str.append(bean.getId());
				str.append("\n");
			}
			return write(FileContainer.agentRelation,str);
		}else if(obj instanceof FrequencyBean){
			for(int i=0;i<beanList.size();i++){
				FrequencyBean bean=(FrequencyBean)beanList.get(i);
				
				str.append(bean.getFreqId()+",");
				str.append(bean.getFreqName());
				str.append("\n");
			}
			return write(FileContainer.frequency,str);
		}
		return false;
	}
	
	private boolean write(String fileName,StringBuffer strInfo){
		//String myDir=FileMaker.dir+Config.FILESEPARATOR+"temp";
		File myFile=new File(dir+Config.FILESEPARATOR+fileName);
		FileWriter fw=null;
		try{
			fw=new FileWriter(myFile);
			fw.write(strInfo.toString());
			fw.flush();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void main(String[] args){
		try{
		File tempf=new File("c:/data/test.txt");
		FileWriter w=new FileWriter(tempf);
		w.write("ssssss");
		w.flush();
		}catch(Exception e){e.printStackTrace();}
		
	}

}
