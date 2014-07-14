package com.cbrc.org.form;

import java.util.List;

import com.cbrc.org.hibernate.MToRepOrg;


/**
 * 该类是代报关系实体类,封装了一些实体信息并提供相应的访问方法,并由Action来生成后存放至设置管理人员的Session中
 * 我的策略是:由于设置由某一家代报机构代报的金融机构的数量会很大,所以对某一个设置关系的改变都会及时的持久化到数据库中
 * 以减少应用服务器的负担
 * 
 * @author  cb
 *
 */
public class DeputationForm {
    /**
     * 代报机构
     */
	private  MToRepOrg  mToRepOrg; //代报机构
	
	/**
	 * 分页显示一页时的被代报的金融机构列表,注意这里的金融机构包括已经被该代报机构代报的和没有被该代报机构的代报的
	 */
    private  List mFinaOrgs; 
    
    /**
     * 已经设置成被该代报机构代报的金融机构的数量
     */
    private  int  setRelationAlreday; 
    
    /**
     * 可以由该代报机构代报的金融机构数
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

