package com.cbrc.smis.adapter;

import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.auth.hibernate.MBankLevel;
import com.cbrc.auth.hibernate.MPurBanklevel;
import com.cbrc.auth.hibernate.MPurBanklevelKey;
import com.cbrc.auth.hibernate.MPurOrg;
import com.cbrc.auth.hibernate.MPurOrgKey;
import com.cbrc.auth.hibernate.MUserGrp;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.po.AfOrg;
/**
 * @author 王明明
 */
public class StrutsMUserGroupDelegate {
	private static FitechException log = new FitechException(
			StrutsMUserGroupDelegate.class);
	
	
	
	
	/**
	 * 
	 * 在增加报表频度时，为系统管理用户组的报送权限、审核权限、查看权限中分配新增报表
	 * 权限表：m_pur_org,m_pur_banklevel,
	 * 
	 */
	
	
	@SuppressWarnings("unchecked")
	public static void processMPurBanklevelPow(String templateId) throws Exception {
		// 置标志result
		boolean result = false;
		// 连接和会话对象的初始化
		DBConn conn = null;
		Session session = null;
		List<MBankLevel> bkList=new ArrayList<MBankLevel>();
		List<AfOrg> orgList=new ArrayList<AfOrg>();
		
		try {
			conn = new DBConn();
			session=conn.beginTransaction();
			String bankLevelHql="from MBankLevel ";
			String afOrgHql="from AfOrg ";
			String userGrp="select u.MUserGrp.userGrpId from MUserToGrp u where u.operator.userId in (select o.userId from Operator o where o.userName='admin') and u.MUserGrp.userGrpId in(select g.userGrpId from MUserGrp g where g.userGrpNm like '%系统管理%')";
			Query query=session.createQuery(bankLevelHql);
			Query queryUserGrp=session.createQuery(userGrp);
			Long userGrpId=(Long)queryUserGrp.list().get(0);
			
			
			bkList=query.list();
			for (int i = 0; i < bkList.size(); i++) {
				
				MPurBanklevel mpo=new MPurBanklevel();
				MPurBanklevelKey mpk=new MPurBanklevelKey();
				MBankLevel mbl=new MBankLevel();
				mbl.setBankLevelId(bkList.get(i).getBankLevelId());
				MUserGrp mug=new MUserGrp();
				mug.setUserGrpId(userGrpId);
				mpk.setMBankLevel(mbl);
				mpk.setMUserGrp(mug);
				mpk.setChildRepId(templateId);
				mpk.setPowType(1);
				mpo.setComp_id(mpk);
				if(isExist(templateId,mbl.getBankLevelId(),mug.getUserGrpId(),1)){
					
					session.save(mpo);
					
				}
				MPurBanklevel mpo_1=new MPurBanklevel();
				MPurBanklevelKey mpk_1=new MPurBanklevelKey();
				MBankLevel mbl_1=new MBankLevel();
				mbl_1.setBankLevelId(bkList.get(i).getBankLevelId());
				MUserGrp mug_1=new MUserGrp();
				mug_1.setUserGrpId(userGrpId);
				mpk_1.setMBankLevel(mbl_1);
				mpk_1.setMUserGrp(mug_1);
				mpk_1.setChildRepId(templateId);
				mpk_1.setPowType(2);
				mpo_1.setComp_id(mpk_1);
				if(isExist(templateId,mbl_1.getBankLevelId(),mug_1.getUserGrpId(),2)){
					
					session.save(mpo_1);
					
				}
				MPurBanklevel mpo_2=new MPurBanklevel();
				MPurBanklevelKey mpk_2=new MPurBanklevelKey();
				MBankLevel mbl_2=new MBankLevel();
				mbl_2.setBankLevelId(bkList.get(i).getBankLevelId());
				MUserGrp mug_2=new MUserGrp();
				mug_2.setUserGrpId(userGrpId);
				mpk_2.setMBankLevel(mbl_2);
				mpk_2.setMUserGrp(mug_2);
				mpk_2.setChildRepId(templateId);
				mpk_2.setPowType(3);
				mpo_2.setComp_id(mpk_2);
				if(isExist(templateId,mbl_2.getBankLevelId(),mug_2.getUserGrpId(),3)){
					
					session.save(mpo_2);
					
				}
				
				session.flush();
				session.clear();
			}
			query=session.createQuery(afOrgHql);
			orgList=query.list();
			for (int i = 0; i < orgList.size(); i++) {

				
				MPurOrg mpo=new MPurOrg();
				MPurOrgKey mpk=new MPurOrgKey();
				AfOrg org=new AfOrg();
				org.setOrgId(orgList.get(i).getOrgId());
				MUserGrp mug=new MUserGrp();
				mug.setUserGrpId(userGrpId);
				mpk.setOrg(org);
				mpk.setMUserGrp(mug);
				mpk.setChildRepId(templateId);
				mpk.setPowType(1);
				mpo.setComp_id(mpk);
				if(isExist(templateId,org.getOrgId(),mug.getUserGrpId(),1)){
					
					session.save(mpo);
					
				}
				MPurOrg mpo_1=new MPurOrg();
				MPurOrgKey mpk_1=new MPurOrgKey();
				AfOrg org_1=new AfOrg();
				org_1.setOrgId(orgList.get(i).getOrgId());
				MUserGrp mug_1=new MUserGrp();
				mug_1.setUserGrpId(userGrpId);
				mpk_1.setOrg(org_1);
				mpk_1.setMUserGrp(mug_1);
				mpk_1.setChildRepId(templateId);
				mpk_1.setPowType(2);
				mpo_1.setComp_id(mpk_1);
				if(isExist(templateId,org_1.getOrgId(),mug_1.getUserGrpId(),2)){
					
					session.save(mpo_1);
					
				}
				MPurOrg mpo_2=new MPurOrg();
				MPurOrgKey mpk_2=new MPurOrgKey();
				AfOrg org_2=new AfOrg();
				org_2.setOrgId(orgList.get(i).getOrgId());
				MUserGrp mug_2=new MUserGrp();
				mug_2.setUserGrpId(userGrpId);
				mpk_2.setOrg(org_2);
				mpk_2.setMUserGrp(mug_2);
				mpk_2.setChildRepId(templateId);
				mpk_2.setPowType(3);
				mpo_2.setComp_id(mpk_2);
				if(isExist(templateId,org_2.getOrgId(),mug_2.getUserGrpId(),3)){
					
					session.save(mpo_2);
					
				}
				
				
				session.flush();
				session.clear();
			}
		
		
			result = true;
		} catch (Exception he) {
			// 捕捉本类的异常,抛出
			result = false;
			session.clear();
			log.printStackTrace(he);
		} finally {
			// 如果由连接则断开连接，结束会话，返回
			if (conn != null)
				conn.endTransaction(result);
		}
		
	}
	/**
	 * 
	 * @param templateId
	 * @param bankLevel
	 * @param mUserGrp
	 * @param powType
	 * @return
	 * @throws Exception
	 */
	
	@SuppressWarnings("unchecked")
	public static boolean isExist(String templateId,long bankLevel,long mUserGrp,int powType) throws Exception {
		// 置标志result
		boolean result = true;
		// 连接和会话对象的初始化
		DBConn conn = null;
		Session session = null;
		List<MPurBanklevel> mpblList=new ArrayList<MPurBanklevel>();
		
		try {
			conn = new DBConn();
			session=conn.openSession();
			String bankLevelSql=" from MPurBanklevel  mp where mp.comp_id.MBankLevel.bankLevelId="+bankLevel+" and mp.comp_id.MUserGrp.userGrpId="+mUserGrp+"  and mp.comp_id.powType="+powType+" and mp.comp_id.childRepId='"+templateId+"'";
			Query query=session.createQuery(bankLevelSql);
			mpblList=query.list();
			if(mpblList.size()>0){
				
				result = false;
			}
		
			
		} catch (Exception he) {
			// 捕捉本类的异常,抛出
			result = true;
			log.printStackTrace(he);
		} finally {
			// 如果由连接则断开连接，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}
   
	
	/**
	 * 
	 * @param templateId
	 * @param orgId
	 * @param mUserGrp
	 * @param powType
	 * @return
	 * @throws Exception
	 */
	
	@SuppressWarnings("unchecked")
	public static boolean isExist(String templateId,String orgId,long mUserGrp,int powType) throws Exception {
		// 置标志result
		boolean result = true;
		// 连接和会话对象的初始化
		DBConn conn = null;
		Session session = null;
		List<MPurOrg> mpoList=new ArrayList<MPurOrg>();
		
		try {
			conn = new DBConn();
			session=conn.openSession();
			String orgHSql=" from MPurOrg  mp where mp.comp_id.org.orgId='"+orgId+"' and mp.comp_id.MUserGrp.userGrpId="+mUserGrp+"  and mp.comp_id.powType="+powType+" and mp.comp_id.childRepId='"+templateId+"'";
			Query query=session.createQuery(orgHSql);
			mpoList=query.list();
			if(mpoList.size()>0){
				
				result = false;
			}
		
			
		} catch (Exception he) {
			// 捕捉本类的异常,抛出
			result = true;
			log.printStackTrace(he);
		} finally {
			// 如果由连接则断开连接，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}
}
