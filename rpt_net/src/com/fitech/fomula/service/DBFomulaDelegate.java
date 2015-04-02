package com.fitech.fomula.service;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.common.DateUtil;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.po.AfValidateformula;
import com.fitech.gznx.service.AFCollectRelationDelegate;

public class DBFomulaDelegate {
	private static FitechException log = new FitechException(
			AFCollectRelationDelegate.class);
	/**
	 * 查询指标编号是否存在当前模板中
	 * @param templateId
	 * @param pid
	 * 
	 * @return
	 */
	public static String queryColNum(String[] params,String templateId,String versionId) {
		DBConn conn = null;
        Session session = null;
        String colNum = null;
        try { 
            conn = new DBConn();
            session = conn.openSession();
            
            String hql = "select a.colNum from AfCellinfo a where a.templateId=:templateId and a.cellPid=:pid"
            		+ " and a.colName=:colName"
            		+ " and a.versionId=:versionId";
            Query query = session.createQuery(hql);
            query.setParameter("templateId",templateId);
            query.setParameter("pid", params[0]);
            query.setParameter("colName", params[3]+params[1]);
            query.setParameter("versionId",versionId);
            colNum = (String)query.uniqueResult();
        } catch(Exception e){
            log.printStackTrace(e);
        } finally {
            if(conn!=null)
                conn.closeSession();
        }
        return colNum;
	}
	
	public static String[] queryTemplate(String params[]) {
		DBConn conn = null;
        Session session = null;
        String[] templates = null;
        try { 
            conn = new DBConn();
            session = conn.openSession();
            
           String sql = "select a.template_id,max(a.version_id) version_id from af_cellinfo a where a.cell_pid='"+params[0]+"' group by a.template_id";
           ResultSet rs = session.connection().createStatement().executeQuery(sql);
           if(rs.next()){
        	   templates = new String[2];
        	   templates[0] = rs.getString("template_id");
        	   templates[1] = rs.getString("version_id");
           }
           
//           sql = "select cur_id from m_curr m.cur_name='"++"'";
        } catch(Exception e){
            log.printStackTrace(e);
        } finally {
            if(conn!=null)
                conn.closeSession();
        }
        return templates;
	}
	
	public static Map<String,List> queryAllTemplateToHql(Map<String,List> templates){
		Map<String,List> ts = new HashMap<String,List>();
		
		DBConn conn = null;
        Session session = null;
        try { 
            conn = new DBConn();
            session = conn.openSession();
            for (String templateId : templates.keySet()) {
    			String versionId = queryVersion(templateId);
    			String hql = "from AfValidateformula a where a.templateId='"+templateId+"' and a.versionId='"+versionId+"'";
    			List<String> fls = new ArrayList<String>();
    			List<AfValidateformula> formulas = session.createQuery(hql).list();
    			for (int i = 0; i < formulas.size(); i++) {
					fls.add(formulas.get(i).getFormulaValue());
				}
    			ts.put(templateId+"_"+versionId, fls);
    		}
        } catch(Exception e){
            log.printStackTrace(e);
        } finally {
            if(conn!=null)
                conn.closeSession();
        }
		return ts;
	}
	
	
	public static Map<String,List> queryAllTemplateToSql(Map<String,List> templates){
		Map<String,List> ts = new HashMap<String,List>();
		
		DBConn conn = null;
        Session session = null;
        try { 
            conn = new DBConn();
            session = conn.openSession();
            for (String templateId : templates.keySet()) {
    			String versionId = queryVersion(templateId);
    			String sql = "select formula_value from af_validateformula_bak a where a.template_id='"+templateId+"' and a.version_id='"+versionId+"'";
    			List<String> fls = new ArrayList<String>();
    		    ResultSet rs = session.connection().createStatement().executeQuery(sql);
    		    while(rs.next()){
    		    	fls.add(rs.getString("formula_value"));
    		    }
    		    ts.put(templateId+"_"+versionId, fls);
    		}
        } catch(Exception e){
            log.printStackTrace(e);
        } finally {
            if(conn!=null)
                conn.closeSession();
        }
		return ts;
	}
	
	public static boolean queryTemplateIsValid(String templateId,String versionId) {
		DBConn conn = null;
        Session session = null;
        boolean result = false;
        try { 
            conn = new DBConn();
            session = conn.openSession();
            
            String hql = "select count(*) from AfTemplate a where a.id.templateId=:templateId "
            		+ " and a.id.versionId=:versionId and :sysdate between a.startDate and a.endDate";
            Query query = session.createQuery(hql);
            String sysdate = DateUtil.toSimpleDateFormat(new Date(), DateUtil.NORMALDATE);
            query.setParameter("templateId", templateId);
            query.setParameter("versionId", versionId);
            query.setParameter("sysdate", sysdate);
            int count = (Integer)query.uniqueResult();
            if(count>0)
            	result = true;
        } catch(Exception e){
            log.printStackTrace(e);
        } finally {
            if(conn!=null)
                conn.closeSession();
        }
        return result;
	}
	
	public static String queryFreqId(String freqName) {
		DBConn conn = null;
        Session session = null;
        String freqId = null;
        try { 
            conn = new DBConn();
            session = conn.openSession();
            
            String sql = "select m.rep_freq_id from m_rep_freq m where m.rep_freq_name='"+freqName+"'";
            ResultSet rs = session.connection().createStatement().executeQuery(sql);
            if(rs.next())
            	freqId = rs.getString("rep_freq_id");
        } catch(Exception e){
            log.printStackTrace(e);
        } finally {
            if(conn!=null)
                conn.closeSession();
        }
        return freqId;
	}
	
	public static String queryVersion(String templateId) {
		DBConn conn = null;
        Session session = null;
        String versionId = null;
        try { 
            conn = new DBConn();
            session = conn.openSession();
            
            String sql = "select max(a.version_id) as version_id   from af_template a where a.template_id='"+templateId+"' group by a.template_id";
            ResultSet rs = session.connection().createStatement().executeQuery(sql);
            if(rs.next())
            	versionId = rs.getString("version_id");
        } catch(Exception e){
            log.printStackTrace(e);
        } finally {
            if(conn!=null)
                conn.closeSession();
        }
        return versionId;
	}
	
	/**
	 * 删除临时表
	 */
	public static void dropTempTable(){
		DBConn conn = null;
        Session session = null;
        boolean result = false;
        try { 
            conn = new DBConn();
            session = conn.beginTransaction();
            
            try {
				String sql = "drop table af_validateformula_bak";   
				
				session.connection().createStatement().executeUpdate(sql);
			} catch (Exception e) {
			}
            result = true;
        } catch(Exception e){
            log.printStackTrace(e);
        } finally {
            if(conn!=null)
                conn.endTransaction(result);
        }
	}
	/**
	 * 创建临时表
	 */
	public static void createTempTable(){
		DBConn conn = null;
        Session session = null;
        boolean result = false;
        try { 
            conn = new DBConn();
            session = conn.beginTransaction();
            
            String sql = "create table AF_VALIDATEFORMULA_BAK "+
					"( "+
					 " formula_id       INTEGER not null, "+
					 " formula_value    VARCHAR2(2000),"+
					 " formula_name     VARCHAR2(4000),"+
					 " validate_type_id INTEGER not null,"+
					 " template_id      VARCHAR2(10) not null,"+
					 " version_id       VARCHAR2(10) not null,"+
					 " cell_id          INTEGER not null"+
					")";   
		
            session.connection().createStatement().executeUpdate(sql);
            result = true;
        } catch(Exception e){
            log.printStackTrace(e);
        } finally {
            if(conn!=null)
                conn.endTransaction(result);
        }
	}
	
	public static boolean saveFormulaInfoToTemTable(String templateId,List<String> formulas){
		DBConn conn = null;
        Session session = null;
        boolean result = false;
        try { 
            conn = new DBConn();
            session = conn.beginTransaction();
            Statement st = session.connection().createStatement();
            String versionId = queryVersion(templateId);
            for (String formula : formulas) {
            	Integer validate_type_id = (formula.indexOf("_")>0)?2:1;
            	String sql = "insert into af_validateformula_bak values(seq_af_validateformula.nextval"
            			+ ",'"+formula+"','"+formula+"'," 
            			+ validate_type_id
            			+ ",'"+templateId+"','"+versionId+"',1"
            			+ ")";
            	st.addBatch(sql);
			}
            st.executeBatch();
            result = true;
        } catch(Exception e){
            log.printStackTrace(e);
        } finally {
            if(conn!=null)
                conn.endTransaction(result);
        }
        return result;
	} 
	
	public static void saveAfValidateFormula(AfValidateformula formula){
		DBConn conn = null;
        Session session = null;
        boolean result = false;
        try { 
            conn = new DBConn();
            session = conn.beginTransaction();
            
            session.save(formula);
            result = true;
        } catch(Exception e){
            log.printStackTrace(e);
        } finally {
            if(conn!=null)
                conn.endTransaction(result);
        }
	}
	
	public static List<String> queryFormulaIdsByReportFlag(String reportFlag){
		DBConn conn = null;
        Session session = null;
        List<String> formulaIds = new ArrayList<String>();
        try { 
            conn = new DBConn();
            session = conn.openSession();
            
            String sql = "select formula_id from AF_PRE_FORMULA where report_flag='"+reportFlag+"'";
            ResultSet rs = session.connection().createStatement().executeQuery(sql);
            while(rs.next()){
            	formulaIds.add(rs.getString("formula_id"));
            }
        } catch(Exception e){
            log.printStackTrace(e);
        } finally {
            if(conn!=null)
                conn.closeSession();
        }
        return formulaIds;
	}
	
	public static boolean saveAfPreFormula(String reportFlag,String formulaId){
		DBConn conn = null;
        Session session = null;
        boolean result = false;
        try { 
            conn = new DBConn();
            session = conn.beginTransaction();
            Statement st = session.connection().createStatement();
            String sql = "insert into AF_PRE_FORMULA values('"+reportFlag+"','"+formulaId+"')";
            st.executeUpdate(sql);
            result = true;
        } catch(Exception e){
            log.printStackTrace(e);
        } finally {
            if(conn!=null)
                conn.endTransaction(result);
        }
        return result;
	} 
	
	public static boolean delAfPreFormula(String reportFlag,String formulaIds){
		DBConn conn = null;
        Session session = null;
        boolean result = false;
        if(StringUtil.isEmpty(formulaIds))
        	return result;
        try { 
            conn = new DBConn();
            session = conn.beginTransaction();
            Statement st = session.connection().createStatement();
            String sql = "delete from AF_PRE_FORMULA where report_flag='"+reportFlag+"' and formula_id in("+formulaIds+")";
            st.executeUpdate(sql);
            result = true;
        } catch(Exception e){
            log.printStackTrace(e);
        } finally {
            if(conn!=null)
                conn.endTransaction(result);
        }
        return result;
	} 
	
	public static void delStanardProductFormulas(String reportFlag){
		DBConn conn = null;
        Session session = null;
        boolean result = false;
        try { 
            conn = new DBConn();
            session = conn.beginTransaction();
            Statement st = session.connection().createStatement();
            String sql = "select FORMULA_ID from AF_PRE_FORMULA where REPORT_FLAG='"+reportFlag+"'";
            ResultSet rs = st.executeQuery(sql);
            sql = "";
            while(rs.next()){
            	sql = "delete from AF_PRE_FORMULA where FORMULA_ID='"+rs.getString("FORMULA_ID")+"'";
        		st.addBatch(sql);
            	
            	sql = "delete from af_validateformula a where a.formula_id="+Long.valueOf(rs.getString("FORMULA_ID"));
            	st.addBatch(sql);
            }
            if(!sql.equals("")){
            	st.executeBatch();
            }
            result = true;
        } catch(Exception e){
            log.printStackTrace(e);
        } finally {
            if(conn!=null)
                conn.endTransaction(result);
        }
	}
	
}
