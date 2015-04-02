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
	private List mainRep=null;    //������
    private List reports=null;   //�ӱ���
    private List realReps=null;  //ʵ���ӱ���
    private List mappingRelations=null; //����������ϵ
    private List mRepRanges=null;  //���������͹�ϵ
    private List dataRanges=null;    //���ݷ�Χ
    private List freqs=null;     //Ƶ��
    private List orgs=null;       //����
    
    
//  ʵ���Ĵ�������
    public static BaseInfoBuffer getInstance(){
 	   if(buffer==null){
 		   buffer=new BaseInfoBuffer();  
 	   }
 	   return buffer;
    }
    
    //˽�еĹ�����
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
