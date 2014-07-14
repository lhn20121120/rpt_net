package com.gather.refer.files;

import java.util.List;

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
		}
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
		UploadShowForm xmlBean=(UploadShowForm)this._xmlList.get(i);
		if(xmlBean.getOrgId().equals("")){
			this.errorMessage="����";
			return false;
		}else if(xmlBean.getFileName().equals("")){
			this.errorMessage="�ļ�����";
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
