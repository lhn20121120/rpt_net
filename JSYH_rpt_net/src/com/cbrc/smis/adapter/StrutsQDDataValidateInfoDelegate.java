package com.cbrc.smis.adapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.DataValidateInfoForm;
import com.cbrc.smis.form.QDDataValidateInfoForm;
import com.cbrc.smis.form.ReportInParticularForm;
import com.cbrc.smis.hibernate.QDDataValidateInfo;
import com.cbrc.smis.other.Expression;
import com.cbrc.smis.util.FitechException;

/**
 * �嵥ʽ���ݱ�����У���ϵ�Ĳ���
 * @author ����
 *
 */
public class StrutsQDDataValidateInfoDelegate {
	private static FitechException log=new FitechException(StrutsDataValidateInfoDelegate.class);
	
	/**
	 * ����ʵ�����ݱ���Id��У�������У����Ϣ�б�
	 * 
	 * @author rds 
	 * @date 2006-01-02 23:14
	 * 
	 * @param repInId Integer ʵ�����ݱ�ID
	 * @param validateTypeId У�����ID
	 * @return List
	 */
	public static List find(Integer repInId,Integer validateTypeId){
		List resList=null;
		StringBuffer buffer=new StringBuffer();
		if(repInId==null && validateTypeId==null) return resList;
		
		if(validateTypeId.intValue()==1){
			buffer.append(" in ("+Expression.FLAG_BL+","+Expression.FLAG_KP+")");
		}
		else{
			buffer.append(" in ("+Expression.FLAG_BL+")");
		}
		DBConn conn=null;
		
		try{
			conn=new DBConn();
			
			String hql="from QDDataValidateInfo dvi where dvi.comp_id.repInId=" +
				repInId + " and dvi.comp_id.validateTypeID=" + validateTypeId;
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				resList=new ArrayList();
				QDDataValidateInfo dataValidateInfo=null;
				for(int i=0;i<list.size();i++){
					dataValidateInfo=(QDDataValidateInfo)list.get(i);
					QDDataValidateInfoForm form=new QDDataValidateInfoForm();
					TranslatorUtil.copyPersistenceToVo(dataValidateInfo,form);
					resList.add(form);
				}
			}
		}catch(HibernateException he){
			resList=null;
			log.printStackTrace(he);
		}catch(Exception e){
			resList=null;
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return resList;
	}
	
	/**
	 * jdbc���� ������oracle�﷨ ���ܲ���Ҫ�޸� ���Ը� 2011-12-27
	 * Ӱ���qd_data_validate_info
	 * ��ѯ���е�У����Ϣ
	 * 
	 * @author rds 
	 * @date 2006-01-02 23:14
	 * 
	 * @param repInId Integer ʵ�����ݱ�ID
	 * @param validateTypeId У�����ID
	 * @return List
	 */
	public static List find(Integer repInId){
		List resList=null;
		
		if(repInId==null) return resList;
				
		DBConn conn=null;
		//create
		Session session = null;
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			conn=new DBConn();
			
			//���з���ʹ��hibernate��ʽ��������������ͬʱ��¼��Ϣ��ʾ��ͬ
/*			String hql="from QDDataValidateInfo dvi where dvi.comp_id.repInId=" + repInId;
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				resList=new ArrayList();
				QDDataValidateInfo dataValidateInfo=null;
				for(int i=0;i<list.size();i++){
					dataValidateInfo=(QDDataValidateInfo)list.get(i);
					QDDataValidateInfoForm form=new QDDataValidateInfoForm();
					TranslatorUtil.copyPersistenceToVo(dataValidateInfo,form);
					resList.add(form);
				}
			}*/
			
			session = conn.openSession();
			connection = session.connection();
			stmt = connection.createStatement();
			ResultSet rSet = null;
			StringBuffer sql = null;

			sql = new StringBuffer();
			sql.append("select m.cell_formu_view,m.formu_type,t.result,t.cause,")
				.append("t.source_value,t.target_value ")
				.append("from qd_data_validate_info t,m_cell_formu m ")
				.append("where t.cell_formu_id=m.cell_formu_id and t.rep_in_id=")
				.append(repInId);
			
			rs = stmt.executeQuery(sql.toString().toUpperCase());

			resList = new ArrayList();

			while (rs.next()) {
//				QDDataValidateInfoForm form = new QDDataValidateInfoForm();
				DataValidateInfoForm form = new DataValidateInfoForm();
				form.setCellFormuView(rs.getString(1));
				form.setValidateTypeId(rs.getInt(2));
				form.setValidateTypeName(rs.getInt(2) == 1 ? "����У��" : "���У��");
				form.setResult(Integer.valueOf(rs.getInt(3)));
				form.setCause(rs.getString(4));
				
				/***
				 * -------20130107Ϊ����BARIII����У�鹫ʽ���������޸�          ���Ը�  -----------------��ʼ
				 */
				//������if min max��У�鹫ʽת��Ϊ��ֵ
				String sourceValue = "";
				try {
					sourceValue = StrutsDataValidateInfoDelegate.formuValue(rSet, stmt, rs.getString(5));
				} catch (Exception e) {
					sourceValue = e.getMessage().substring(e.getMessage().indexOf(":")+1);
					e.printStackTrace();
				}
				//������if min max��У�鹫ʽת��Ϊ��ֵ
				String targetValue = "";
				try {
					targetValue = StrutsDataValidateInfoDelegate.formuValue(rSet, stmt, rs.getString(6));
				} catch (Exception e) {
					targetValue = e.getMessage().substring(e.getMessage().indexOf(":")+1);
					e.printStackTrace();
				}
				if(sourceValue!=null && !sourceValue.equals(""))
					form.setSourceValue(sourceValue);
				else
					form.setSourceValue(rs.getString(5));
				if(targetValue!=null && !targetValue.equals(""))
					form.setTargetValue(targetValue);
				else
					form.setTargetValue(rs.getString(6));				
				/***
				 * -------20130107Ϊ����BARIII����У�鹫ʽ���������޸�          ���Ը�  -----------------����
				 */
				resList.add(form);
			}

		}catch(HibernateException he){
			resList=null;
			log.printStackTrace(he);
		}catch(Exception e){
			resList=null;
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return resList;
	}
	
	/**
	 * ��ѯ���еı���У�鲻ͨ����Ϣ
	 * 
	 * @author rds 
	 * @date 2006-01-02 23:14
	 * 
	 * @param repInId Integer ʵ�����ݱ�ID
	 * @param validateTypeId У�����ID null��ʾȫ�����
	 * @return List
	 */
	public static List findNotPass(Integer repInId,Integer dataValidateType){
		List resList=null;		
		if(repInId==null) return resList;
				
		DBConn conn=null;
		
		try{
			conn=new DBConn();			
			String hql="from QDDataValidateInfo dvi where dvi.comp_id.repInId=" + repInId+
			" and dvi.result=-1 ";
			if(dataValidateType!=null) hql+=" and dvi.validateType.validateTypeId=" + dataValidateType;
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				resList=new ArrayList();
				QDDataValidateInfo dataValidateInfo=null;
				for(int i=0;i<list.size();i++){
					dataValidateInfo=(QDDataValidateInfo)list.get(i);
					QDDataValidateInfoForm form=new QDDataValidateInfoForm();
					TranslatorUtil.copyPersistenceToVo(dataValidateInfo,form);
					resList.add(form);
				}
			}
		}catch(HibernateException he){
			resList=null;
			log.printStackTrace(he);
		}catch(Exception e){
			resList=null;
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return resList;
	}
	
	/**����ReportInParticular�е�RepInId��ѯQDDataValidateInfo�е�Cell_Formu,colName,rowNo��Result�ֶ�
	    * ���ҽ�StrutsValidateTypeDelegate�е�validate_Type_NameҲ��ѯ����
	    * @author ����
	    * @�嵥ʽ�����ڱ��У���ϵ�Ĳ�ѯ
	    */
	public static List select_AllRecords(String repInId){
		   DBConn conn=null;
		   Session session=null;
		   
		   List resList = null;
		   List list=null;
		   
		   try {
				if (repInId != null) {
					conn = new DBConn();
					session = conn.openSession();

					String hql = "from QDDataValidateInfo qddvi where 1=1";
					hql += " and qddvi.comp_id.repInId="+ repInId;
					Query query = session.createQuery(hql.toString());
					
					resList = query.list();
					
					if (resList != null && resList.size() > 0) {
						list=new ArrayList();
						for (int i = 0; i < resList.size(); i++) {
							ReportInParticularForm reportInParticularForm=new ReportInParticularForm();
							//��Ԫ��ʽ�Ĳ���
								QDDataValidateInfo qddateValidateInfo =(QDDataValidateInfo)list.get(i);
								if (qddateValidateInfo.getMCellFormu() != null
								&& !qddateValidateInfo.equals("")) {
							String cellFormu = qddateValidateInfo
									.getMCellFormu().getCellFormu();
							if (cellFormu != null && !cellFormu.equals("")) {
								reportInParticularForm.setCellFormu(cellFormu);
								}
								}
							//У�����Ĳ�ѯ
								String result = ((QDDataValidateInfo) resList.get(i)).getResult().toString();
								if(result!=null && !result.equals("")){
									reportInParticularForm.setResult(result);
								}
							//У�����͵Ĳ�ѯ
								String validateTypeName = ((QDDataValidateInfo) resList.get(i)).getValidateType().getValidateTypeName();
								if(validateTypeName!=null && !validateTypeName.equals("")){
									reportInParticularForm.setValidateTypeName(validateTypeName);
								}
							//У�����������Ĳ�ѯ
								String cause=((QDDataValidateInfo)resList.get(i)).getCause().toString();
								if(cause!=null && !cause.equals("")){
									reportInParticularForm.setCause(cause);
								}
						
							list.add(reportInParticularForm);
					}
				}
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return list;
	}
}
