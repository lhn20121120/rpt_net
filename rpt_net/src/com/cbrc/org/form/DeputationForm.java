package com.cbrc.org.form;

import java.util.List;

import com.cbrc.org.hibernate.MToRepOrg;


/**
 * �����Ǵ�����ϵʵ����,��װ��һЩʵ����Ϣ���ṩ��Ӧ�ķ��ʷ���,����Action�����ɺ��������ù�����Ա��Session��
 * �ҵĲ�����:����������ĳһ�Ҵ������������Ľ��ڻ�����������ܴ�,���Զ�ĳһ�����ù�ϵ�ĸı䶼�ἰʱ�ĳ־û������ݿ���
 * �Լ���Ӧ�÷������ĸ���
 * 
 * @author  cb
 *
 */
public class DeputationForm {
    /**
     * ��������
     */
	private  MToRepOrg  mToRepOrg; //��������
	
	/**
	 * ��ҳ��ʾһҳʱ�ı������Ľ��ڻ����б�,ע������Ľ��ڻ��������Ѿ����ô������������ĺ�û�б��ô��������Ĵ�����
	 */
    private  List mFinaOrgs; 
    
    /**
     * �Ѿ����óɱ��ô������������Ľ��ڻ���������
     */
    private  int  setRelationAlreday; 
    
    /**
     * �����ɸô������������Ľ��ڻ�����
     */
    private  int  setMaxRelation; 
    
    public DeputationForm(){}

	public List getMFinaOrgs() {
		return mFinaOrgs;
	}

	public void setMFinaOrgs(List finaOrgs) {
		mFinaOrgs = finaOrgs;
	}

	public MToRepOrg getMToRepOrg() {
		return mToRepOrg;
	}

	public void setMToRepOrg(MToRepOrg toRepOrg) {
		mToRepOrg = toRepOrg;
	}

	public int getSetMaxRelation() {
		return setMaxRelation;
	}

	public void setSetMaxRelation(int setMaxRelation) {
		this.setMaxRelation = setMaxRelation;
	}

	public int getSetRelationAlreday() {
		return setRelationAlreday;
	}

	public void setSetRelationAlreday(int setRelationAlreday) {
		this.setRelationAlreday = setRelationAlreday;
	}
    
    
	
    
	
    
}

