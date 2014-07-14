package com.gather.refer.files;

import java.util.List;

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
		}
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
		UploadShowForm xmlBean=(UploadShowForm)this._xmlList.get(i);
		if(xmlBean.getOrgId().equals("")){
			this.errorMessage="机构";
			return false;
		}else if(xmlBean.getFileName().equals("")){
			this.errorMessage="文件名称";
			return false;
		}
		return true;
	}
	return false;
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
