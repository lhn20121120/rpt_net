package com.gather.refer.data.file;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class XmlDataChecker {
	
	private String errorMessage="";
	private List _xmlList=null;
	
	public static final String NO_ZIP_FILE = "没有上报文件";
	private static final String NO_ANY_DATA = "没有数据";
	private static final String NO_FULL_DATA = "不能为空";
	private static final String INVALID_DATA_TYPE = "数据类型错误";
	private static final String DATA_REPEAT = "上报数据重复";
	
	public XmlDataChecker(){}
	public void init(List xmlList){
		this._xmlList=xmlList;
		if(!ifContainData()){
			this.errorMessage=XmlDataChecker.NO_ANY_DATA;
		}else if(!ifPerItemContainData()){
			this.errorMessage=this.errorMessage+XmlDataChecker.NO_FULL_DATA;
		}else if(!checkPerItemDataType()){
			this.errorMessage=this.errorMessage+XmlDataChecker.INVALID_DATA_TYPE;
		}else if(!ifContainRepeatData()){
			this.errorMessage=XmlDataChecker.DATA_REPEAT;
		}
		// System.out.println("---the error message is: "+this.getErrorMessage());
	}

//检查是否包含数据
public boolean ifContainData(){
	// System.out.println("-----the method in the checker.ifContainData()---");
	if(this._xmlList==null || this._xmlList.size()<1) return false;
	return true;
}

//检查各个数据项是否有值
public  boolean ifPerItemContainData(){
	// System.out.println("-----the method in the checker.ifPerItemContainData()---");
	for(int i=0;i<this._xmlList.size();i++){
		ListingXmlBean xmlBean=(ListingXmlBean)this._xmlList.get(i);
		if(xmlBean.getReportId().equals("")){
			this.errorMessage="报表ID";
			return false;
		}else if(xmlBean.getVersion().equals("")){
			this.errorMessage="版本号";
			return false;
		}else if(xmlBean.getOrgId().equals("")){
			this.errorMessage="机构名称";
			return false;
		}else if(xmlBean.getFrequencyId().equals("")){
			this.errorMessage="报表频率";
			return false;
		}else if(xmlBean.getDataRangeId().equals("")){
			this.errorMessage="报表数据范围";
			return false;
		}else if(xmlBean.getYear().equals("")){
			this.errorMessage="报表年份";
			return false;
		}else if(xmlBean.getTerms().equals("")){
			this.errorMessage="报表期数";
			return false;
		}
		return true;
	}
	return false;
}

//检查每个数据项的数据类型是否正确
public  boolean checkPerItemDataType(){
	// System.out.println("-----the method in the checker.checkPerItemDataType()---");
	for(int i=0;i<this._xmlList.size();i++){
		ListingXmlBean xmlBean=(ListingXmlBean)this._xmlList.get(i);
		
	try{Integer.parseInt(xmlBean.getFrequencyId());}catch(Exception e){
			this.errorMessage=xmlBean.getReportId()+"-"+xmlBean.getVersion()+"报表频率:";
			return false;
		}
	try{Integer.parseInt(xmlBean.getDataRangeId());}catch(Exception e){
		this.errorMessage=xmlBean.getReportId()+"-"+xmlBean.getVersion()+"报表数据范围:";
		return false;
	}
	try{Integer.parseInt(xmlBean.getYear());}catch(Exception e){
		this.errorMessage=xmlBean.getReportId()+"-"+xmlBean.getVersion()+"报表年份:";
		return false;
	}
	try{Integer.parseInt(xmlBean.getTerms());}catch(Exception e){
		this.errorMessage=xmlBean.getReportId()+"-"+xmlBean.getVersion()+"报表期数:";
		return false;
	}
  }
return true;
}

//检查是否有重复的数据
public  boolean ifContainRepeatData(){
	// System.out.println("-----the method in the checker.ifContainRepeatData()--");
	Map testMap=new Hashtable();
	for(int i=0;i<this._xmlList.size();i++){
		ListingXmlBean xmlBean=(ListingXmlBean)this._xmlList.get(i);
		
		StringBuffer testKey=new StringBuffer();
		testKey.append(xmlBean.getRealId());
		testKey.append(xmlBean.getVersion());
		testKey.append(xmlBean.getOrgId());
		testKey.append(xmlBean.getFrequencyId());
		testKey.append(xmlBean.getDataRangeId());
		testKey.append(xmlBean.getYear());
		testKey.append(xmlBean.getTerms());
		
		testMap.put(testKey.toString(),"");
	}
	if(testMap.size()<this._xmlList.size()){
		 return false;
	}
    return true;
}

/**
 * @return Returns the errorMessage.
 */
public String getErrorMessage() {
	return errorMessage;
}

/**
 * @param errorMessage The errorMessage to set.
 */
public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
}

public boolean check(List xmlList){
	// System.out.println("-----the method in the checker.check()---");
	this.init(xmlList);
	if(this.getErrorMessage().equals("")) return true;
	return false;	
}

}
