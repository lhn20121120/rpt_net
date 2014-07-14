/**
 * 该包定义一些实体类型
 */
package com.cbrc.org.entity;

import java.util.List;

import com.cbrc.org.hibernate.MToRepOrg;


/**
 * 该类是代报关系实体类,封装了一些实体信息并提供相应的访问方法,并由Action来生成后存放至设置管理人员的Session中
 * @author  cb
 *
 */
public class Deputation {

	private  MToRepOrg  mToRepOrg; //代报机构
	
    private  List mFinaOrgs; //分页显示时一页的被代报的金融机构列表,注意这里的金融机构包括已经被该代报机构代报的和没有被该代报机构的代报的
    
    private  List finaOrgCodes; //已经设置成由该家代报机构代报的金融机构的主键列表,注意这里出于节约系统资源上的考虑只保存金融机构的主键
    
    private  List newFinaOrgCods;//最新被设置成由该家代报机构代报的金融机构的主键列表
    
    public Deputation(){}
    
    public Deputation(MToRepOrg toRepOrg) {
		super();
		// TODO 自动生成构造函数存根
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
     * 该方法计算出已经设置成被该代报机构代报的金融机构的数量
     * @return type:int  已经设置成被该代报机构代报的金融机构的数量
     */
    public int getCountByDeputation(){
    	
    	return  this.finaOrgCodes.size();
    }
    
    
}
