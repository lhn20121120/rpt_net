package com.fitech.gznx.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.po.QdCellinfo;
import com.fitech.gznx.service.AFQDCellInfoDelegate;

public class ColNumFormUtil {
	private String templateId = null;
	
	private String versionId = null;
	  /**
	   * 报表类型信息列表
	   */
	private List colNumList = null;
	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public List getColNumList() {

		  if(this.colNumList==null && !StringUtil.isEmpty(templateId) && !StringUtil.isEmpty(versionId)){	
			  List rtList=new ArrayList();
			  try{
				  QdCellinfo qdcellInfo = AFQDCellInfoDelegate.getCellInfo(templateId, versionId);
				  int startcol = this.convertColStringToNum(qdcellInfo.getStartCol());
				  int endcol = this.convertColStringToNum(qdcellInfo.getEndCol());
				  for(int i=(short)startcol+1;i<=endcol+1;i++){
						rtList.add(new LabelValueBean(convertNumToCol(i-1),"COL"+i));
					}
		
			  }catch(Exception e){
				  e.printStackTrace();
				  return rtList;
			  }
			  return rtList;
		  }else{
			return this.colNumList;
		  }

	}

	public void setColNumList(List colNumList) {
		this.colNumList = colNumList;
	}
	/**
	 * 将列号转换为数字
	 * 
	 * @param ref
	 * @return
	 * 
	 */
	private int convertColStringToNum(String ref) {
		int retval = 0;
		int pos = 0;
		for (int k = ref.length() - 1; k > -1; k--) {
			char thechar = ref.charAt(k);
			if (pos == 0)
				retval += Character.getNumericValue(thechar) - 9;
			else
				retval += (Character.getNumericValue(thechar) - 9) * (pos * 26);
			pos++;
		}

		return retval - 1;
	}
	/**
     * 将列号转换为数字
     * @param ref
     * @return
     */
    public String convertNumToCol(int num) {
    	StringBuffer col= new StringBuffer() ;
    	if(num<26){
    		int acs = num + 'A';
    		col.append((char) acs);
    	}else{
    		int acs = num/26-1 + 'A';
    		int acs1 = num%26 + 'A';
    		col.append((char) acs).append((char) acs1);
    	}
    	return col.toString();
    }
}
