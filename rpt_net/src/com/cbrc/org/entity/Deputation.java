/**
 * �ð�����һЩʵ������
 */
package com.cbrc.org.entity;

import java.util.List;

import com.cbrc.org.hibernate.MToRepOrg;


/**
 * �����Ǵ�����ϵʵ����,��װ��һЩʵ����Ϣ���ṩ��Ӧ�ķ��ʷ���,����Action�����ɺ��������ù�����Ա��Session��
 * @author  cb
 *
 */
public class Deputation {

	private  MToRepOrg  mToRepOrg; //��������
	
    private  List mFinaOrgs; //��ҳ��ʾʱһҳ�ı������Ľ��ڻ����б�,ע������Ľ��ڻ��������Ѿ����ô������������ĺ�û�б��ô��������Ĵ�����
    
    private  List finaOrgCodes; //�Ѿ����ó��ɸüҴ������������Ľ��ڻ����������б�,ע��������ڽ�Լϵͳ��Դ�ϵĿ���ֻ������ڻ���������
    
    private  List newFinaOrgCods;//���±����ó��ɸüҴ������������Ľ��ڻ����������б�
    
    public Deputation(){}
    
    public Deputation(MToRepOrg toRepOrg) {
		super();
		// TODO �Զ����ɹ��캯�����
		mToRepOrg = toRepOrg;
    }

	public List getFinaOrgCodes() {
		return finaOrgCodes;
	}

	public void setFinaOrgCodes(List finaOrgCodes) {
		this.finaOrgCodes = finaOrgCodes;
	}

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

	public List getNewFinaOrgCods() {
		return newFinaOrgCods;
	}

	public void setNewFinaOrgCods(List newFinaOrgCods) {
		this.newFinaOrgCods = newFinaOrgCods;
	}

	/**
     * �÷���������Ѿ����óɱ��ô������������Ľ��ڻ���������
     * @return type:int  �Ѿ����óɱ��ô������������Ľ��ڻ���������
     */
    public int getCountByDeputation(){
    	
    	return  this.finaOrgCodes.size();
    }
    
    
}
