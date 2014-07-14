package com.cbrc.smis.adapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MCellForm;
import com.cbrc.smis.form.MExcelChildReportForm;
import com.cbrc.smis.hibernate.MCell;
import com.cbrc.smis.hibernate.MChildReport;
import com.cbrc.smis.hibernate.MChildReportPK;
import com.cbrc.smis.util.DataArea;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.common.ExcelParser;
import com.fitech.net.common.StringTool;

/**
 * This is a delegate class to handle interaction with the backend persistence
 * layer of hibernate. It has a set of methods to handle persistence for MCell
 * data (i.e. com.cbrc.smis.form.MCellForm objects).
 * 
 * @author <strong>Generated by Middlegen. </strong>
 */
public class StrutsMCellDelegate {
    private static FitechException log = new FitechException(
            StrutsMCellDelegate.class);

    /**
     * 保存报表模板中的输入域
     * 
     * @author rds
     * @serialData 2005-12-8
     * 
     * @param session
     *            Session;
     * @param cells
     *            List
     * @param childRepId
     *            String 子报表ID
     * @param versionId
     *            String 报表版本号
     * @return boolean 保存成功，返回true;返回失败，返回false
     */
    public static boolean insertPatch(Session session, List cells,
            MChildReport mChildReport) {
        boolean resPatch = false;

        if (session == null || cells == null)
            return resPatch;

        Iterator it = cells.iterator();
        try {
            while (it.hasNext()) {
                MCellForm mCellForm = (MCellForm) it.next();
                MCell mCell = new MCell();
                TranslatorUtil.copyVoToPersistence(mCell, mCellForm);
                mCell.setMChildReport(mChildReport);
                session.save(mCell);
                session.flush();
            }
            resPatch = true;
        } catch (HibernateException he) {
            log.printStackTrace(he);
        } catch (Exception e) {
            log.printStackTrace(e);
        }

        return resPatch;
    }

    /**
     * 获得单元格ID
     * 
     * @author rds
     * @serialData 2005-12-09
     * 
     * @param childRepId
     *            String 子报表ID
     * @param versionId
     *            String 版本号
     * @param cellName
     *            String 单元格名称
     * @return Integer 如果没有查询到单元格，返回null
     */
    public static Integer getCellId(String childRepId, String versionId,
            String cellName) {
        Integer cellId = null;
        if (childRepId == null || versionId == null || cellName == null)
            return cellId;

        DBConn conn = null;
        try {
            String hql = "from MCell mc where mc.MChildReport.comp_id.childRepId='"
                    + childRepId
                    + "'"
                    + " and mc.MChildReport.comp_id.versionId='"
                    + versionId
                    + "'" + " and mc.cellName='" + cellName + "'";
            	// System.out.println(hql);
            conn = new DBConn();

            List list = conn.openSession().find(hql);
            if (list != null && list.size() == 1) {
                MCell mCell = (MCell) list.get(0);
                cellId = mCell.getCellId();
            }
        } catch (HibernateException he) {
            log.printStackTrace(he);
        } finally {
            if (conn != null)
                conn.closeSession();
        }

        return cellId;
    }

    /**
     * 获得单元格对象
     * 
     * @auther rds
     * @date 2006-02-18
     * 
     * @param childRepId 子报表ID
     * @param versionId 版本号
     * @param cellName 单元格名称
     * @return MCell对象,失败返回null
     */
    public static MCell getMCell(String childRepId, String versionId,String cellName) {
        MCell mCell = null;
        if (childRepId == null || versionId == null || cellName == null)
            return mCell;

        DBConn conn = null;
        try {
            String hql = "from MCell mc where mc.MChildReport.comp_id.childRepId='"
                    + childRepId
                    + "'"
                    + " and mc.MChildReport.comp_id.versionId='"
                    + versionId
                    + "'" + " and mc.cellName='" + cellName + "'";

            conn = new DBConn();

            List list = conn.openSession().find(hql);
            if (list != null && list.size() == 1) {
            	mCell = (MCell) list.get(0);
            }
        } catch (HibernateException he) {
            log.printStackTrace(he);
        } finally {
            if (conn != null) conn.closeSession();
        }

        return mCell;
    }
    /**
     * 获取报表所有可输入单元格
     * 
     * @author rds
     * @serialData 2005-12-11 1:11
     * 
     * @param childRepId
     *            String 子报表ID
     * @param versionId
     *            String 版本号
     * @return List 无记录，返回null
     */
    public static List getCells(String childRepId, String versionId) {
        List resList = null;

        if (childRepId == null || versionId == null)
            return resList;

        DBConn conn = null;
        try {
            String hql = "from MCell mc where mc.MChildReport.comp_id.childRepId='"
                    + childRepId
                    + "' and "
                    + "mc.MChildReport.comp_id.versionId='"
                    + versionId
                    + "' order by mc.cellName";

            conn = new DBConn();

            List list = conn.openSession().find(hql);

            if (list != null && list.size() > 0) {
                resList = new ArrayList();
                MCell mCell = null;
                for (int i = 0; i < list.size(); i++) {
                    mCell = (MCell) list.get(i);
                    MCellForm form = new MCellForm();
                    TranslatorUtil.copyPersistenceToVo(mCell, form);
                    resList.add(form);
                }
            }
        } catch (HibernateException he) {
            resList = null;
            // System.out.println(he);
            log.printStackTrace(he);
        } catch (Exception e) {
            resList = null;
            // System.out.println(e);
            log.printStackTrace(e);
        } finally {
            if (conn != null)
                conn.closeSession();
        }

        return resList;
    }

    /**
     * 获取报表所有可输入单元格
     * 
     * @author rds
     * @serialData 2005-12-11 1:11
     * 
     * @param childRepId
     *            String 子报表ID
     * @param versionId
     *            String 版本号
     * @return List 无记录，返回null
     */
    public static List getMCells(String childRepId, String versionId) {
        List resList = null;

        if (childRepId == null || versionId == null)
            return resList;

        DBConn conn = null;
        try {
            String hql = "from MCell mc where mc.MChildReport.comp_id.childRepId='"
                    + childRepId
                    + "' and "
                    + "mc.MChildReport.comp_id.versionId='"
                    + versionId
                    + "' order by mc.rowId,mc.cellName";
            conn = new DBConn();

            List list = conn.openSession().find(hql);

            if (list != null && list.size() > 0) {
                resList = new ArrayList();
                MCell mCell = null;
                for (int i = 0; i < list.size(); i++) {
                    mCell = (MCell) list.get(i);
                    MCellForm form = new MCellForm();
                    TranslatorUtil.copyPersistenceToVo(mCell, form);
                    resList.add(form);
                }
            }
        } catch (HibernateException he) {
            resList = null;
            log.printStackTrace(he);
        } catch (Exception e) {
            resList = null;
            log.printStackTrace(e);
        } finally {
            if (conn != null)
                conn.closeSession();
        }

        return resList;
    }
    
    /**
     * Create a new com.cbrc.smis.form.MCellForm object and persist (i.e.
     * insert) it.
     * 
     * @param mCellForm
     *            The object containing the data for the new
     *            com.cbrc.smis.form.MCellForm object
     * @exception Exception
     *                If the new com.cbrc.smis.form.MCellForm object cannot be
     *                created or persisted.
     */
    public static com.cbrc.smis.form.MCellForm create(
            com.cbrc.smis.form.MCellForm mCellForm) throws Exception {
        com.cbrc.smis.hibernate.MCell mCellPersistence = new com.cbrc.smis.hibernate.MCell();
        TranslatorUtil.copyVoToPersistence(mCellPersistence, mCellForm);
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        // TODO: Make adapter get SessionFactory jndi name by ant task argument?
        net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
                .lookup("java:AirlineHibernateFactory");
        net.sf.hibernate.Session session = factory.openSession();
        net.sf.hibernate.Transaction tx = session.beginTransaction();
        // TODO error?: mCellPersistence =
        // (com.cbrc.smis.hibernate.MCell)session.save(mCellPersistence);
        session.save(mCellPersistence);
        tx.commit();
        session.close();
        TranslatorUtil.copyPersistenceToVo(mCellPersistence, mCellForm);
        return mCellForm;
    }

    /**
     * Update (i.e. persist) an existing com.cbrc.smis.form.MCellForm object.
     * 
     * @param mCellForm
     *            The com.cbrc.smis.form.MCellForm object containing the data to
     *            be updated
     * @exception Exception
     *                If the com.cbrc.smis.form.MCellForm object cannot be
     *                updated/persisted.
     */
    public static com.cbrc.smis.form.MCellForm update(
            com.cbrc.smis.form.MCellForm mCellForm) throws Exception {
        com.cbrc.smis.hibernate.MCell mCellPersistence = new com.cbrc.smis.hibernate.MCell();
        TranslatorUtil.copyVoToPersistence(mCellPersistence, mCellForm);
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        // TODO: Make adapter get SessionFactory jndi name by ant task argument?
        net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
                .lookup("java:AirlineHibernateFactory");
        net.sf.hibernate.Session session = factory.openSession();
        net.sf.hibernate.Transaction tx = session.beginTransaction();
        session.update(mCellPersistence);
        tx.commit();
        session.close();
        TranslatorUtil.copyPersistenceToVo(mCellPersistence, mCellForm);
        return mCellForm;
    }

    /**
     * Retrieve an existing com.cbrc.smis.form.MCellForm object for editing.
     * 
     * @param mCellForm
     *            The com.cbrc.smis.form.MCellForm object containing the data
     *            used to retrieve the object (i.e. the primary key info).
     * @exception Exception
     *                If the com.cbrc.smis.form.MCellForm object cannot be
     *                retrieved.
     */
    public static com.cbrc.smis.form.MCellForm edit(
            com.cbrc.smis.form.MCellForm mCellForm) throws Exception {
        com.cbrc.smis.hibernate.MCell mCellPersistence = new com.cbrc.smis.hibernate.MCell();
        TranslatorUtil.copyVoToPersistence(mCellPersistence, mCellForm);
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        // TODO: Make adapter get SessionFactory jndi name by ant task argument?
        net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
                .lookup("java:AirlineHibernateFactory");
        net.sf.hibernate.Session session = factory.openSession();
        net.sf.hibernate.Transaction tx = session.beginTransaction();
        mCellPersistence = (com.cbrc.smis.hibernate.MCell) session.load(
                com.cbrc.smis.hibernate.MCell.class, mCellPersistence
                        .getCellId());
        tx.commit();
        session.close();
        TranslatorUtil.copyPersistenceToVo(mCellPersistence, mCellForm);
        return mCellForm;
    }

    /**
     * Remove (delete) an existing com.cbrc.smis.form.MCellForm object.
     * 
     * @param mCellForm
     *            The com.cbrc.smis.form.MCellForm object containing the data to
     *            be deleted.
     * @exception Exception
     *                If the com.cbrc.smis.form.MCellForm object cannot be
     *                removed.
     */
    public static void remove(com.cbrc.smis.form.MCellForm mCellForm)
            throws Exception {
        com.cbrc.smis.hibernate.MCell mCellPersistence = new com.cbrc.smis.hibernate.MCell();
        TranslatorUtil.copyVoToPersistence(mCellPersistence, mCellForm);
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        // TODO: Make adapter get SessionFactory jndi name by ant task argument?
        net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
                .lookup("java:AirlineHibernateFactory");
        net.sf.hibernate.Session session = factory.openSession();
        net.sf.hibernate.Transaction tx = session.beginTransaction();
        // TODO: is this really needed?
        mCellPersistence = (com.cbrc.smis.hibernate.MCell) session.load(
                com.cbrc.smis.hibernate.MCell.class, mCellPersistence
                        .getCellId());
        session.delete(mCellPersistence);
        tx.commit();
        session.close();
    }

    /**
     * Retrieve all existing com.cbrc.smis.form.MCellForm objects.
     * 
     * @exception Exception
     *                If the com.cbrc.smis.form.MCellForm objects cannot be
     *                retrieved.
     */
    public static List findAll() throws Exception {
        List retVals = new ArrayList();
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        // TODO: Make adapter get SessionFactory jndi name by ant task argument?
        net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
                .lookup("java:AirlineHibernateFactory");
        net.sf.hibernate.Session session = factory.openSession();
        net.sf.hibernate.Transaction tx = session.beginTransaction();
        retVals.addAll(session.find("from com.cbrc.smis.hibernate.MCell"));
        tx.commit();
        session.close();
        ArrayList mCell_vos = new ArrayList();
        for (Iterator it = retVals.iterator(); it.hasNext();) {
            com.cbrc.smis.form.MCellForm mCellFormTemp = new com.cbrc.smis.form.MCellForm();
            com.cbrc.smis.hibernate.MCell mCellPersistence = (com.cbrc.smis.hibernate.MCell) it
                    .next();
            TranslatorUtil.copyPersistenceToVo(mCellPersistence, mCellFormTemp);
            mCell_vos.add(mCellFormTemp);
        }
        retVals = mCell_vos;
        return retVals;
    }

    /**
     * Retrieve a set of existing com.cbrc.smis.form.MCellForm objects for
     * editing.
     * 
     * @param mCellForm
     *            The com.cbrc.smis.form.MCellForm object containing the data
     *            used to retrieve the objects (i.e. the criteria for the
     *            retrieval).
     * @exception Exception
     *                If the com.cbrc.smis.form.MCellForm objects cannot be
     *                retrieved.
     */
    public static List select(com.cbrc.smis.form.MCellForm mCellForm)
            throws Exception {
        List retVals = new ArrayList();
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        // TODO: Make adapter get SessionFactory jndi name by ant task argument?
        net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
                .lookup("java:AirlineHibernateFactory");
        net.sf.hibernate.Session session = factory.openSession();
        net.sf.hibernate.Transaction tx = session.beginTransaction();
        retVals
                .addAll(session
                        .find("from com.cbrc.smis.hibernate.MCell as obj1 where obj1.cellName='"
                                + mCellForm.getCellName() + "'"));
        retVals
                .addAll(session
                        .find("from com.cbrc.smis.hibernate.MCell as obj1 where obj1.dataType='"
                                + mCellForm.getDataType() + "'"));
        retVals
                .addAll(session
                        .find("from com.cbrc.smis.hibernate.MCell as obj1 where obj1.rowId='"
                                + mCellForm.getRowId() + "'"));
        retVals
                .addAll(session
                        .find("from com.cbrc.smis.hibernate.MCell as obj1 where obj1.colId='"
                                + mCellForm.getColId() + "'"));
        tx.commit();
        session.close();
        ArrayList mCell_vos = new ArrayList();
        for (Iterator it = retVals.iterator(); it.hasNext();) {
            com.cbrc.smis.form.MCellForm mCellFormTemp = new com.cbrc.smis.form.MCellForm();
            com.cbrc.smis.hibernate.MCell mCellPersistence = (com.cbrc.smis.hibernate.MCell) it
                    .next();
            TranslatorUtil.copyPersistenceToVo(mCellPersistence, mCellFormTemp);
            mCell_vos.add(mCellFormTemp);
        }
        retVals = mCell_vos;
        return retVals;
    }

    /**
     * This method will return all objects referenced by MChildReport
     */
    public static List getMChildReport(com.cbrc.smis.form.MCellForm mCellForm)
            throws Exception {
        List retVals = new ArrayList();
        com.cbrc.smis.hibernate.MCell mCellPersistence = null;
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        // TODO: Make adapter get SessionFactory jndi name by ant task argument?
        net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
                .lookup("java:AirlineHibernateFactory");
        net.sf.hibernate.Session session = factory.openSession();
        net.sf.hibernate.Transaction tx = session.beginTransaction();
        mCellPersistence = (com.cbrc.smis.hibernate.MCell) session.load(
                com.cbrc.smis.hibernate.MCell.class, mCellPersistence
                        .getCellId());
        tx.commit();
        session.close();
        retVals.add(mCellPersistence.getMChildReport());
        ArrayList CHILD_REP_ID_vos = new ArrayList();
        for (Iterator it = retVals.iterator(); it.hasNext();) {
            com.cbrc.smis.form.MChildReportForm CHILD_REP_ID_Temp = new com.cbrc.smis.form.MChildReportForm();
            com.cbrc.smis.hibernate.MChildReport CHILD_REP_ID_PO = (com.cbrc.smis.hibernate.MChildReport) it
                    .next();
            TranslatorUtil.copyPersistenceToVo(CHILD_REP_ID_PO,
                    CHILD_REP_ID_Temp);
            CHILD_REP_ID_vos.add(CHILD_REP_ID_Temp);
        }
        retVals = CHILD_REP_ID_vos;
        return retVals;
    }

    /**
     * 该方法用于返回一个单元记录
     * 
     * @param versionId
     * @param rowId
     * @param colId
     * @return MCell 单元格对象
     * @throws Exception
     */
    public static MCell getMCell(String childRepId, String versionId,
            Integer rowId, String colId) throws Exception {

        DBConn dBConn = null;

        Session session = null;

        Query query = null;

        List l = null;

        MCell mCell = null;

        String hsql = "from MCell mc where mc.rowId=:rowId and mc.colId=:colId and  ";

        hsql = hsql + "mc.MChildReport.comp_id.versionId=:versionId ";

        hsql = hsql + "and mc.MChildReport.comp_id.childRepId=:childRepId";

        try {

            dBConn = new DBConn();

            session = dBConn.openSession();

            query = session.createQuery(hsql);

            query.setInteger("rowId", rowId.intValue());

            query.setString("colId", colId);

            query.setString("childRepId", childRepId);

            query.setString("versionId", versionId);

            l = query.list();

            if (l.size() != 0)

                mCell = (MCell) l.get(0);

        } catch (Exception e) {

        	log.printStackTrace(e);
        	
        	mCell = null;
        	
        } finally {

            if (session != null)

                dBConn.closeSession();
        }

        return mCell;
    }

    /**
     * 向单元格库表插入一条记录
     * 
     * @param childRepId
     * @param versionId
     * @param rowId
     * @param collId
     * @return
     * @throws Exception
     */
    public static MCell insertMCell(String childRepId, String versionId,
            Integer rowId, String collId) throws Exception {

        MCell mCell = new MCell();

        MChildReport mcr = new MChildReport();

        MChildReportPK comp_id = new MChildReportPK();

        comp_id.setChildRepId(childRepId);

        comp_id.setVersionId(versionId);

        mcr.setComp_id(comp_id);

        mCell.setMChildReport(mcr);

        mCell.setRowId(rowId);

        mCell.setColId(collId);

        DBConn dBConn = null;

        Session session = null;

        try {

            dBConn = new DBConn();

            session = dBConn.beginTransaction();

            session.save(mCell);

      //    session.flush();

            dBConn.endTransaction(true);

        } catch (Exception e) {

            dBConn.endTransaction(false);
            
        } finally {

            if (session != null)

                dBConn.closeSession();
        }

        return mCell;
    }
    
    
    
    public static boolean insertMCell(MExcelChildReportForm mExcelChildReportForm,MChildReport mChildReport,InputStream inStream)
    {
    	boolean flag=false;
    	DBConn conn=new DBConn();
    	/**设置EXCEL数据域*/
    	DataArea dataArea=new DataArea();
 	    dataArea.setStartRow(mExcelChildReportForm.getStartRow());
 	    dataArea.setStartCol(mExcelChildReportForm.getStartCol().toUpperCase());
 	    dataArea.setEndRow(mExcelChildReportForm.getEndRow());
 	    dataArea.setEndCol(mExcelChildReportForm.getEndCol().toUpperCase());
 	    
 	   /**验证EXCEL数据域的有效性*/
    	if(!DataArea.isValid(dataArea)){
    		return flag;
    	}
    	try{
    		/**目前只支持列号在A-Z之间*/
    		if(dataArea.getStartCol().length()==1 && dataArea.getEndCol().length()==1){
    			MCell mCell=null;
    			HSSFRow row=null;
    			HSSFCell cell=null;
    			/**获取HSSFSheet*/
    			HSSFSheet sheet=ExcelParser.getNeedSheet(mExcelChildReportForm,inStream,0);
				if(sheet!=null){					
					Session session=conn.beginTransaction();
	    			for(int r=Integer.parseInt(dataArea.getStartRow());r<=Integer.parseInt(dataArea.getEndRow());r++)
	    			{
	    				/**获取HSSFRow对象*/
	    				row=sheet.getRow(r-1);
		    			for(char c=dataArea.getStartCol().charAt(0);c<=dataArea.getEndCol().charAt(0);c++)
		    				{
		    					/**获取HSSFCell对象*/
			    				if(row!=null){
			    					cell=row.getCell(Short.parseShort(String.valueOf(StringTool.colIndex(String.valueOf(c)))));
			    				}
			    				/**如果单元格非空，则入库*/
			    				if(cell!=null){
				    				mCell=new MCell();
				    				mCell.setCellName(String.valueOf(c)+String.valueOf(r));
				    				mCell.setDataType(new Integer(cell.getCellType()));
				    				mCell.setRowId(new Integer(r));
				    				mCell.setColId(String.valueOf(c));
				    				MChildReportPK comp_id=new MChildReportPK();
				    				comp_id.setChildRepId(mChildReport.getComp_id().getChildRepId());
				    				comp_id.setVersionId(mChildReport.getComp_id().getVersionId());
				    				MChildReport mcr=new MChildReport();
				    				mcr.setComp_id(comp_id);
				    				mCell.setMChildReport(mcr);
				    				try {
										session.save(mCell);
									} catch (HibernateException e) {
										e.printStackTrace();
									}
			    				}
		    				}
	    			}
	    			flag=true;
	    			conn.endTransaction(flag);
				}
    		}
    	}catch(Exception ex){
    		flag = false;
    		ex.printStackTrace();
    		conn.endTransaction(flag);
    	}
    	return flag;
    }
    
    public static List getAllCell(String childRepId,String versionId)
    {
    	DBConn conn=new DBConn();
    	Session session=conn.openSession();
    	List cellList=null;
    	String hql = "from MCell mc where mc.MChildReport.comp_id.childRepId='"
            + childRepId
            + "' and "
            + "mc.MChildReport.comp_id.versionId='"
            + versionId
            +"'";

    	try {
			cellList=session.find(hql);
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally{
			if(conn!=null) conn.closeSession();
		}
    	return cellList;
    }
    
    
    public static MCell getMCell(Integer cellId) {
        MCell mCell = null;
        if (cellId == null) return mCell;

        DBConn conn = null;
        try {
            String hql = "from MCell mc where mc.cellId=" + cellId;

            conn = new DBConn();
            List list = conn.openSession().find(hql);

            if (list != null && list.size() > 0) {
            	mCell = (MCell)list.get(0);                
            }
        } catch (HibernateException he) {
            mCell = null;
            log.printStackTrace(he);
        } catch (Exception e) {
            mCell = null;
            log.printStackTrace(e);
        } finally {
            if (conn != null)
                conn.closeSession();
        }
        return mCell;
    }
    /**
     * 
     * 	更新 MCell
     * @param list
     * @return boolean
     * @author zyl_xh
     */
    public static boolean updateCell(List list){
    	DBConn conn = null;
    	boolean result=false;
    	try {
            conn = new DBConn();
            Session session=conn.beginTransaction();
            for(int i=0;i<list.size();i++){
            	MCellForm cell=(MCellForm)list.get(i);
  //          	// System.out.println(cell.getCollectType()+"''''"+cell.getCellId());
            	MCell mcell=new MCell();
            	TranslatorUtil.copyVoToPersistence(mcell,cell);
            	session.update(mcell);
            	
            }
            session.flush();
           
            result=true;
       
        } catch (Exception e) {
             log.printStackTrace(e); 
        }
        
        finally{    	
           conn.endTransaction(result);
        }
        
        return result;
    }
}