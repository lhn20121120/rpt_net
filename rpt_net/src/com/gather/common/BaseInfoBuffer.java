package com.gather.common;

import java.util.List;

import com.gather.adapter.StrutsMActuRepDelegate;
import com.gather.adapter.StrutsMChildReportDelegate;
import com.gather.adapter.StrutsMDataRgTypeDelegate;
import com.gather.adapter.StrutsMMainRepDelegate;
import com.gather.adapter.StrutsMOrg;
import com.gather.adapter.StrutsMRepFreqDelegate;
import com.gather.adapter.StrutsMRepRangeDelegate;
/**
 * 
 * @author linfeng
 *
 */
public class BaseInfoBuffer {
	private static BaseInfoBuffer buffer=null;
	private List mainRep=null;    //主报表
    private List reports=null;   //子报表
    private List realReps=null;  //实际子报表
    private List mappingRelations=null; //机构代报关系
    private List mRepRanges=null;  //机构报表报送关系
    private List dataRanges=null;    //数据范围
    private List freqs=null;     //频率
    private List orgs=null;       //机构
    
    
//  实例的创建方法
    public static BaseInfoBuffer getInstance(){
 	   if(buffer==null){
 		   buffer=new BaseInfoBuffer();  
 	   }
 	   return buffer;
    }
    
    //私有的构造子
    private BaseInfoBuffer(){
 	   this.reports=StrutsMChildReportDelegate.getAll();
 	   this.mainRep=StrutsMMainRepDelegate.getAll();
 	   this.realReps=StrutsMActuRepDelegate.getAll();
 	   this.dataRanges=StrutsMDataRgTypeDelegate.findAll();
 	   this.freqs=StrutsMRepFreqDelegate.findAll();
 	   this.mappingRelations=StrutsMMainRepDelegate.getAll();
 	   this.mRepRanges=StrutsMRepRangeDelegate.getAll();
 	   this.orgs=StrutsMOrg.getAll();
    }
  /**
	 * @return Returns the dataRanges.
	 */
	public List getDataRanges() {
		return dataRanges;
	}

	/**
	 * @param dataRanges The dataRanges to set.
	 */
	public void setDataRanges(List dataRanges) {
		this.dataRanges = dataRanges;
	}

	/**
	 * @return Returns the freqs.
	 */
	public List getFreqs() {
		return freqs;
	}

	/**
	 * @param freqs The freqs to set.
	 */
	public void setFreqs(List freqs) {
		this.freqs = freqs;
	}

	/**
	 * @return Returns the mainRep.
	 */
	public List getMainRep() {
		return mainRep;
	}

	/**
	 * @param mainRep The mainRep to set.
	 */
	public void setMainRep(List mainRep) {
		this.mainRep = mainRep;
	}

	/**
	 * @return Returns the mappingRelations.
	 */
	public List getMappingRelations() {
		return mappingRelations;
	}

	/**
	 * @param mappingRelations The mappingRelations to set.
	 */
	public void setMappingRelations(List mappingRelations) {
		this.mappingRelations = mappingRelations;
	}

	/**
	 * @return Returns the mRepRanges.
	 */
	public List getMRepRanges() {
		return mRepRanges;
	}

	/**
	 * @param repRanges The mRepRanges to set.
	 */
	public void setMRepRanges(List repRanges) {
		mRepRanges = repRanges;
	}

	/**
	 * @return Returns the realReps.
	 */
	public List getRealReps() {
		return realReps;
	}

	/**
	 * @param realReps The realReps to set.
	 */
	public void setRealReps(List realReps) {
		this.realReps = realReps;
	}


/**
 * @return Returns the reports.
 */
public List getReports() {
	return reports;
}

/**
 * @param reports The reports to set.
 */
public void setReports(List reports) {
	this.reports = reports;
}

/**
 * @return Returns the orgs.
 */
public List getOrgs() {
	return orgs;
}

/**
 * @param orgs The orgs to set.
 */
public void setOrgs(List orgs) {
	this.orgs = orgs;
}
   
   

}
