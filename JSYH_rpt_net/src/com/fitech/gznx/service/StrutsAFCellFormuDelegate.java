package com.fitech.gznx.service;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;



import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MCellFormuForm;
import com.cbrc.smis.form.MCellToFormuForm;
import com.cbrc.smis.hibernate.MCellFormu;

import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.PageListInfo;
import com.fitech.gznx.dao.DaoModel;
import com.fitech.gznx.po.AfCellinfo;
import com.fitech.gznx.po.AfGatherformula;
import com.fitech.gznx.po.AfValidateformula;

public class StrutsAFCellFormuDelegate  extends DaoModel{


	private static FitechException log=new FitechException(StrutsAFCellFormuDelegate.class);
	/**
	 * ����д����ڱ���ϵ���ʽ
	 * 
	 * @param cells List
	 * @return boolean ����ɹ�������true;���򣬷���false
	 */
	public static boolean savePatch(List cells){
		boolean result=true;
		
		if(cells==null && cells.size()<=0) return false;
		
		DBConn conn=null;
		Session session=null;
		
		try{
			conn=new DBConn();
			session=conn.beginTransaction();
			
			MCellFormuForm form=null;
			
			for(int i=0;i<cells.size();i++){
				form=(MCellFormuForm)cells.get(i);
				
				/*// System.out.println("" + form.getChildRepId());
				// System.out.println("" + form.getVersionId());*/
				if(insert(session,form)==false){ //����ϵ���ʽд�����ݿ�
					result=false;
					break;
				}
				
				/**���ܵ������������Ԫ����ʽ���**/
				if(form.getSameCells()!=null && form.getSameCells().size()>0){
				//	// System.out.println("[StrutsMCellFormu]SameCells is not null!");
					for(int j=0;i<form.getSameCells().size();i++){
					//	// System.out.println("SameCell:" + (String)form.getSameCells().get(j));
						if(form.getSameCells().get(j)!=null){
							form.setCellFormu((String)form.getSameCells().get(j));
							if(insert(session,form)==false){
								result=false;
								break;
							}
						}
					}
				}
			}				
		}catch(Exception e){
			log.printStackTrace(e);
			result=false;
		}finally{
			if(conn!=null) {
				conn.endTransaction(result);
			}
		}
		
		return result;
	}
	
	public static boolean UpdatePatch(List cells){
		boolean result=true;
		
		if(cells==null && cells.size()<=0) return false;
		
		DBConn conn=null;
		Session session=null;
		
		try{
			conn=new DBConn();
			session=conn.beginTransaction();
			
			MCellFormuForm form=null;
			
			for(int i=0;i<cells.size();i++){
				form=(MCellFormuForm)cells.get(i);
				
				/*// System.out.println("" + form.getChildRepId());
				// System.out.println("" + form.getVersionId());*/
				if(Update(session,form)==false){ //����ϵ���ʽд�����ݿ�
					result=false;
					break;
				}
				
				/**���ܵ������������Ԫ����ʽ���**/
				if(form.getSameCells()!=null && form.getSameCells().size()>0){
				//	// System.out.println("[StrutsMCellFormu]SameCells is not null!");
					for(int j=0;i<form.getSameCells().size();i++){
					//	// System.out.println("SameCell:" + (String)form.getSameCells().get(j));
						if(form.getSameCells().get(j)!=null){
							form.setCellFormu((String)form.getSameCells().get(j));
							if(insert(session,form)==false){
								result=false;
								break;
							}
						}
					}
				}
			}				
		}catch(Exception e){
			log.printStackTrace(e);
			result=false;
		}finally{
			if(conn!=null) {
				conn.endTransaction(result);
			}
		}
		
		return result;
	}
	/**
	 * ����д����ڱ���ϵ���ʽ
	 * 
	 * @param cells List
	 * @return boolean ����ɹ�������true;���򣬷���false
	 */
	public static boolean saveYJHPatch(List cells){
		boolean result=true;
		
		if(cells==null && cells.size()<=0) return false;
		
		DBConn conn=null;
		Session session=null;
		
		try{
			conn=new DBConn();
			session=conn.beginTransaction();
			
			MCellFormuForm form=null;
			
			for(int i=0;i<cells.size();i++){
				form=(MCellFormuForm)cells.get(i);
				
				/*// System.out.println("" + form.getChildRepId());
				// System.out.println("" + form.getVersionId());*/
				if(Updateyjh(session,form)==false){ //����ϵ���ʽд�����ݿ�
					result=false;
					break;
				}
				
				/**���ܵ������������Ԫ����ʽ���**/
				if(form.getSameCells()!=null && form.getSameCells().size()>0){
				//	// System.out.println("[StrutsMCellFormu]SameCells is not null!");
					for(int j=0;i<form.getSameCells().size();i++){
					//	// System.out.println("SameCell:" + (String)form.getSameCells().get(j));
						if(form.getSameCells().get(j)!=null){
							form.setCellFormu((String)form.getSameCells().get(j));
							if(Updateyjh(session,form)==false){
								result=false;
								break;
							}
						}
					}
				}
			}				
		}catch(Exception e){
			log.printStackTrace(e);
			result=false;
		}finally{
			if(conn!=null) {
				conn.endTransaction(result);
			}
		}
		
		return result;
	}
	
	private static boolean Updateyjh(Session session,MCellFormuForm form){
		boolean result=true;
		
		if(session==null || form==null) return result;
		Connection conn = null;
		Statement stmt = null;
		try{
			conn = session.connection();
			stmt = conn.createStatement();
			
			//��ȡУ�鹫ʽ
			String cellFormu=form.getCellFormu();
			//�ж��Ƿ����reg
			if(cellFormu.toLowerCase().indexOf("reg")!=-1){
				//����reg�ָ��ַ���
				String[] cellFormus=cellFormu.toLowerCase().split("reg");
				//��regǰ����ַ���ת��Ϊ��д ��reg��reg������ַ������ֲ���
				cellFormu=cellFormus[0].toUpperCase()+form.getCellFormu().substring(cellFormu.toLowerCase().indexOf("reg"));
			}
			
			String sql ="update m_cell_formu set cell_formu='"+cellFormu+"',formu_type="+
						form.getFormuType()+",cell_formu_view='"+form.getCellFormuView()+
						"' where cell_formu_id="+form.getCellFormuId();
			
			System.out.println(cellFormu);
			//�˴�ȥ��ת�ɴ�д����
			//stmt.execute(sql.toString().toUpperCase());
			stmt.execute(sql.toString());
			conn.commit();
//			MCellFormu mCellForum=new MCellFormu();
//			mCellForum.setCellFormu(form.getCellFormu());
//			mCellForum.setCellFormuId(form.getCellFormuId());
//			mCellForum.setCellFormuView(form.getCellFormuView());
//			mCellForum.setFormuType(form.getFormuType());
//			
//			session.update(mCellForum);
//			session.flush();
			
		}catch(HibernateException he){
			log.printStackTrace(he);
			result=false;
		}catch(Exception e){
			log.printStackTrace(e);
			result=false;
		}
		
		return result;
	}
	public static Long getCellid(String childRepId,String versionId){
		List list=null;
		List retVals = new ArrayList();
		DBConn conn=null;
		try {
				String hql=" from AfCellinfo afc where afc.templateId='"
					+ childRepId + "' and " + "afc.versionId='"
						+ versionId + "'";	
				conn=new DBConn();
				list=conn.openSession().find(hql);
				AfCellinfo afcllinfo = (AfCellinfo)list.get(0);
					return afcllinfo.getCellId();
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return null;
		
		}
	/**
	 * ��������
	 * 
	 * @param session Session �Ự����
	 * @return boolean �����ɹ�������true;���򣬷���false
	 */
	private static boolean insert(Session session,MCellFormuForm form){
		boolean result=true;
		
		if(session==null || form==null) return result;
		
		try{
			AfValidateformula mCellForum=new AfValidateformula();
			copyVoToPersistence(mCellForum,form);
			
			session.save(mCellForum);
			session.flush();
			
		}catch(HibernateException he){
			log.printStackTrace(he);
			result=false;
		}catch(Exception e){
			log.printStackTrace(e);
			result=false;
		}
		
		return result;
	}
		//���²���
	private static boolean Update(Session session,MCellFormuForm form){
		boolean result=true;
		
		if(session==null || form==null) return result;
		
		try{
			AfValidateformula mCellForum=new AfValidateformula();
			copyVoToPersistences(mCellForum,form);
			
			session.update(mCellForum);
			session.flush();
			
		}catch(HibernateException he){
			log.printStackTrace(he);
			result=false;
		}catch(Exception e){
			log.printStackTrace(e);
			result=false;
		}
		
		return result;
	}
	private static void copyVoToPersistence(AfValidateformula cellForum,
			MCellFormuForm form) {
		cellForum.setTemplateId(form.getChildRepId());
		cellForum.setVersionId(form.getVersionId());
		cellForum.setFormulaValue(form.getCellFormu());
		cellForum.setFormulaName(form.getCellFormuView());
		cellForum.setValidateTypeId(new Long(form.getFormuType()));		
		cellForum.setCellId(new Long(1));
		
	}
	
	private static void copyVoToPersistences(AfValidateformula cellForum,
			MCellFormuForm form) {
		cellForum.setFormulaId(Long.valueOf(form.getCellFormuId()));
		cellForum.setTemplateId(form.getChildRepId());
		cellForum.setVersionId(form.getVersionId());
		cellForum.setFormulaValue(form.getCellFormu());
		cellForum.setFormulaName(form.getCellFormuView());
		cellForum.setValidateTypeId(Long.valueOf(String.valueOf(form.getFormuType())));
		cellForum.setCellId(Long.valueOf(form.getCellId()));	
	}
	/**
	 * ���ݱ��ʽ���ӱ���ID�Ͱ汾��,�жϵ�ǰ��ϵ���ʽ�Ƿ��Ѿ�����
	 * 
	 * @author rds
	 * @serialData 2005-12-20 0:34
	 * 
	 * @param cellFormu String ��ϵ���ʽ
	 * @param childRepId String �ӱ���ID
	 * @param versionId String �汾��
	 * @return boolean ���ʽ�Ѵ��ڣ�����true;���򣬷���false
	 */
	public static boolean isCellFormuExists(String cellFormu,String childRepId,String versionId){
		boolean result=false;
		
		if(cellFormu==null || childRepId==null || versionId==null) return result;
		
		DBConn conn=null;
		
		try{
			String hql="from AfValidateformula mcf where mcf.formulaValue='" + cellFormu + 
				"' and mcf.templateId='" + childRepId + "' and mcf.versionId='" + versionId + "'";
			
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				result=true;
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return result;
	}

	public static PageListInfo selectAllFormula(String childRepId,
			String versionId, int curPage) {
		List resList = new ArrayList();
    	DBConn conn = null;
    	Session session = null;
    	PageListInfo pageListInfo=null;
    	try{    		
			conn = new DBConn();
			session = conn.beginTransaction();
    		
			String hql="from AfValidateformula t where t.templateId='"+childRepId+"' and t.versionId="+versionId;
			pageListInfo = findByPageWithSQL(session,hql,Config.PER_PAGE_ROWS,curPage);

			List list=pageListInfo.getList();
			for(int i=0;i<list.size();i++){
				MCellToFormuForm form = new MCellToFormuForm();
				AfValidateformula fornula = (AfValidateformula) list.get(i);
				form.setChildRepId(fornula.getTemplateId());
				form.setVersionId(fornula.getVersionId());
				form.setFormuType(fornula.getValidateTypeId().intValue());
				form.setCellFormu(fornula.getFormulaValue());
				form.setCellFormuId(fornula.getFormulaId().intValue());
				resList.add(form);
			}
			pageListInfo.setList(resList);
	    	}catch(Exception he){
	    		if(conn != null) conn.endTransaction(true);
	    	}finally{
	    		if(conn != null) 
	    			conn.closeSession();
	    	}
			return pageListInfo;
	}
	
	/***
	 * ��ʹ��hibernate ���Ը� 2011-12-27
	 * Ӱ�����AfValidateformula
	 * @param childRepId
	 * @param versionId
	 * @return
	 */
	public static List selectAllFormula(String childRepId,
			String versionId) {
		List resList = new ArrayList();
    	DBConn conn = null;
    	Session session = null;

    	try{    		
			conn = new DBConn();
			session = conn.beginTransaction();
    		
			String hql="from AfValidateformula t where t.templateId='"+childRepId+"' and t.versionId="+versionId;
			List list = conn.openSession().find(hql);			
			for(int i=0;i<list.size();i++){
				MCellToFormuForm form = new MCellToFormuForm();
				AfValidateformula fornula = (AfValidateformula) list.get(i);
				form.setChildRepId(fornula.getTemplateId());
				form.setVersionId(fornula.getVersionId());
				form.setFormuType(fornula.getValidateTypeId().intValue());
				form.setCellFormu(fornula.getFormulaValue());
				form.setCellFormuId(fornula.getFormulaId().intValue());
				resList.add(form);
			}
	    	}catch(Exception he){
	    		if(conn != null) conn.endTransaction(true);
	    	}finally{
	    		if(conn != null) 
	    			conn.closeSession();
	    	}
			return resList;
	}
	
	public static List<AfValidateformula> selectAllFormulaByTemplate(String childRepId,
			String versionId) {
    	DBConn conn = null;
    	Session session = null;
    	List<AfValidateformula> list = new ArrayList<AfValidateformula>();
    	try{    		
			conn = new DBConn();
			session = conn.beginTransaction();
    		
			String hql="from AfValidateformula t where t.templateId='"+childRepId+"' and t.versionId="+versionId;
			list= conn.openSession().find(hql);			
			
	    	}catch(Exception he){
	    		if(conn != null) conn.endTransaction(true);
	    	}finally{
	    		if(conn != null) 
	    			conn.closeSession();
	    	}
			return list;
	}
	/**
	 * ɾ�����ڱ��Ĺ�ϵ���ʽ<br>
	 * ���ݱ��ʽ��ID��ֵ����������ɾ��
	 * 
	 * @param cellFormuIds ���ʽ��ID��ֵ
	 * @param reportFlg 
	 * @return boolean ɾ�������ɹ�������true;���򣬷���false
	 */
	public static boolean delete(String cellFormuIds,String templateId,String versionId, String reportFlg){
		boolean result=false;
		
		if(cellFormuIds==null) return result;
		if(cellFormuIds.equals("")) return result;
		
		if(cellFormuIds.substring(cellFormuIds.length()-1).equals(Config.SPLIT_SYMBOL_COMMA)){
			cellFormuIds=cellFormuIds.substring(cellFormuIds.length()-1);
		}
		if(cellFormuIds.equals("")) return result;
		
	
		DBConn conn=null;
		Session session=null;
		
		try{
			conn=new DBConn();
			session=conn.beginTransaction();
			String sql=null;
			if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
				sql="from MCellFormu t where t.cellFormuId in  (" + cellFormuIds + ")";
				session.delete(sql);
				sql="from MCellToFormu t where t.childRepId='"+templateId+"' and t.versionId="+versionId+" and t.cellFormuId in  (" + cellFormuIds + ")";
				session.delete(sql);	
			} else {
				sql="from AfValidateformula mcf where mcf.formulaId in (" + cellFormuIds + ")";
				session.delete(sql);	
			}
			session.flush();
			result = true;
		}catch(HibernateException he){
			log.printStackTrace(he);
		}finally{
			if(conn!=null) conn.endTransaction(result);
		}
		
		return result;
	}
	
}
