package com.cbrc.smis.form;

import java.util.ArrayList;
import java.util.List;

import com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate;

/**
 * 
 * @author jcm
 * @serialData 2006-04-02
 */
public class UtilMDataRGTypeForm {
	
	private static UtilMDataRGTypeForm utilMDataRGTypeForm = null;
	
	private List dataRanges = null;
	
	private UtilMDataRGTypeForm(){}
	
	public static UtilMDataRGTypeForm getInstance(){
		if(utilMDataRGTypeForm == null)
			utilMDataRGTypeForm = new UtilMDataRGTypeForm();
		return utilMDataRGTypeForm;
	}
	/**
	 * 获取数据报送范围
	 */
	
	public List getDataRanges() {
		if(dataRanges == null){
			try {
				dataRanges = StrutsMDataRgTypeDelegate.findAll();
			} catch (Exception e) {
				dataRanges = new ArrayList();
				MDataRgTypeForm mDataRgTypeFormTemp = new MDataRgTypeForm();
				mDataRgTypeFormTemp.setDataRangeId(new Integer(1));
				mDataRgTypeFormTemp.setDataRgDesc("境内汇总数据");
				dataRanges.add(mDataRgTypeFormTemp);
			}
		}
		return dataRanges;
	}

	public void setDataRanges(List dataRanges) {
		this.dataRanges = dataRanges;
	}
}
