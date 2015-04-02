package com.gather.refer.data.file;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class XmlDataChecker {
	
	private String errorMessage="";
	private List _xmlList=null;
	
	public static final String NO_ZIP_FILE = "û���ϱ��ļ�";
	private static final String NO_ANY_DATA = "û������";
	private static final String NO_FULL_DATA = "����Ϊ��";
	private static final String INVALID_DATA_TYPE = "�������ʹ���";
	private static final String DATA_REPEAT = "�ϱ������ظ�";
	
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

//����Ƿ��������
public boolean ifContainData(){
	// System.out.println("-----the method in the checker.ifContainData()---");
	if(this._xmlList==null || this._xmlList.size()<1) return false;
	return true;
}

//�������������Ƿ���ֵ
public  boolean ifPerItemContainData(){
	// System.out.println("-----the method in the checker.ifPerItemContainData()---");
	for(int i=0;i<this._xmlList.size();i++){
		ListingXmlBean xmlBean=(ListingXmlBean)this._xmlList.get(i);
		if(xmlBean.getReportId().equals("")){
			this.errorMessage="����ID";
			return false;
		}else if(xmlBean.getVersion().equals("")){
			this.errorMessage="�汾��";
			return false;
		}else if(xmlBean.getOrgId().equals("")){
			this.errorMessage="��������";
			return false;
		}else if(xmlBean.getFrequencyId().equals("")){
			this.errorMessage="����Ƶ��";
			return false;
		}else if(xmlBean.getDataRangeId().equals("")){
			this.errorMessage="�������ݷ�Χ";
			return false;
		}else if(xmlBean.getYear().equals("")){
			this.errorMessage="�������";
			return false;
		}else if(xmlBean.getTerms().equals("")){
			this.errorMessage="��������";
			return false;
		}
		return true;
	}
	return false;
}

//���ÿ������������������Ƿ���ȷ
public  boolean checkPerItemDataType(){
	// System.out.println("-----the method in the checker.checkPerItemDataType()---");
	for(int i=0;i<this._xmlList.size();i++){
		ListingXmlBean xmlBean=(ListingXmlBean)this._xmlList.get(i);
		
	try{Integer.parseInt(xmlBean.getFrequencyId());}catch(Exception e){
			this.errorMessage=xmlBean.getReportId()+"-"+xmlBean.getVersion()+"����Ƶ��:";
			return false;
		}
	try{Integer.parseInt(xmlBean.getDataRangeId());}catch(Exception e){
		this.errorMessage=xmlBean.getReportId()+"-"+xmlBean.getVersion()+"�������ݷ�Χ:";
		return false;
	}
	try{Integer.parseInt(xmlBean.getYear());}catch(Exception e){
		this.errorMessage=xmlBean.getReportId()+"-"+xmlBean.getVersion()+"�������:";
		return false;
	}
	try{Integer.parseInt(xmlBean.getTerms());}catch(Exception e){
		this.errorMessage=xmlBean.getReportId()+"-"+xmlBean.getVersion()+"��������:";
		return false;
	}
  }
return true;
}

//����Ƿ����ظ�������
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
