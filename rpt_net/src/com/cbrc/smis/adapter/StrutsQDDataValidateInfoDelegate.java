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
 * 清单式数据表间表内校验关系的查找
 * @author 唐磊
 *
 */
public class StrutsQDDataValidateInfoDelegate {
	private static FitechException log=new FitechException(StrutsDataValidateInfoDelegate.class);
	
	/**
	 * 根据实际数据报表Id和校验类别获得校验信息列表
	 * 
	 * @author rds 
	 * @date 2006-01-02 23:14
	 * 
	 * @param repInId Integer 实际数据表ID
	 * @param validateTypeId 校验类别ID
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
	 * jdbc技术 无特殊oracle语法 可能不需要修改 卞以刚 2011-12-27
	 * 影响表：qd_data_validate_info
	 * 查询所有的校验信息
	 * 
	 * @author rds 
	 * @date 2006-01-02 23:14
	 * 
	 * @param repInId Integer 实际数据表ID
	 * @param validateTypeId 校验类别ID
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
			
			//旧有方法使用hibernate方式，复合主键都相同时记录信息显示相同
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
				form.setValidateTypeName(rs.getInt(2) == 1 ? "表内校验" : "表间校验");
				form.setResult(Integer.valueOf(rs.getInt(3)));
				form.setCause(rs.getString(4));
				
				/***
				 * -------20130107为增加BARIII报表校验公式所作增量修改          卞以刚  -----------------开始
				 */
				//将带有if min max的校验公式转换为数值
				String sourceValue = "";
				try {
					sourceValue = StrutsDataValidateInfoDelegate.formuValue(rSet, stmt, rs.getString(5));
				} catch (Exception e) {
					sourceValue = e.getMessage().substring(e.getMessage().indexOf(":")+1);
					e.printStackTrace();
				}
				//将带有if min max的校验公式转换为数值
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
				 * -------20130107为增加BARIII报表校验公式所作增量修改          卞以刚  -----------------结束
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
	 * 查询所有的表内校验不通过信息
	 * 
	 * @author rds 
	 * @date 2006-01-02 23:14
	 * 
	 * @param repInId Integer 实际数据表ID
	 * @param validateTypeId 校验类别ID null表示全部类别
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
	
	/**根据ReportInParticular中的RepInId查询QDDataValidateInfo中的Cell_Formu,colName,rowNo和Result字段
	    * 并且将StrutsValidateTypeDelegate中的validate_Type_Name也查询出来
	    * @author 唐磊
	    * @清单式表单表内表间校验关系的查询
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
							//单元格公式的查找
								QDDataValidateInfo qddateValidateInfo =(QDDataValidateInfo)list.get(i);
								if (qddateValidateInfo.getMCellFormu() != null
								&& !qddateValidateInfo.equals("")) {
							String cellFormu = qddateValidateInfo
									.getMCellFormu().getCellFormu();
							if (cellFormu != null && !cellFormu.equals("")) {
								reportInParticularForm.setCellFormu(cellFormu);
								}
								}
							//校验结果的查询
								String result = ((QDDataValidateInfo) resList.get(i)).getResult().toString();
								if(result!=null && !result.equals("")){
									reportInParticularForm.setResult(result);
								}
							//校验类型的查询
								String validateTypeName = ((QDDataValidateInfo) resList.get(i)).getValidateType().getValidateTypeName();
								if(validateTypeName!=null && !validateTypeName.equals("")){
									reportInParticularForm.setValidateTypeName(validateTypeName);
								}
							//校验结果的描述的查询
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
