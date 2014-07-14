package com.gather.down.reports;

import java.util.ArrayList;
import java.util.List;

public class FileContainer {

    public static final String referRelationRange="should_submit_form_web.txt";
    public static final String delayTime="should_submit_detail_web.txt";
    public static final String reportInfo="form_model_web.txt";
    public static final String dataRange="should_submit_range_web.txt";
    public static final String frequency= "should_submit_frequency_web.txt";
    public static final String agentRelation="affiliation_agent_web.txt";
    
    private List fileList = new ArrayList();
    
	public void add(List beanList){
		 fileList.add(beanList);
	}
	/**
	 * @return Returns the beanList.
	 */
	public List getFileList() {
		return fileList;
	}
	/**
	 * @param beanList The beanList to set.
	 */
	public void setFileList(List fileList) {
		this.fileList = fileList;
	}


}
