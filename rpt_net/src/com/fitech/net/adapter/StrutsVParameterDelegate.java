package com.fitech.net.adapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.dao.FitechConnection;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.VParameterForm;
import com.fitech.net.hibernate.VParameter;

/**
 * @StrutsVParamDelegate 参数表表单Delegate对象
 * 
 * @author James
 */
public class StrutsVParameterDelegate {
    //Catch到本类的抛出的所有异常
    private static FitechException log = new FitechException(StrutsVParameterDelegate.class);

    /**
     * 校验类别新增
     * 
     * @param mRepFreqForm VParameterForm
     * @return boolean result,新增成功返回true,否则返回false
     * @exception Exception，捕捉异常处理
     */
    public static boolean create(VParameterForm vParamForm) throws Exception {

        boolean result = false; //置result标记
        VParameter vParamPersistence = new VParameter();

        if (vParamForm == null) {
            return result;
        }
        DBConn conn = null;        
        Session session = null;

        try {
            //表示层到持久层的CopyTo方法(McurUnitPresistence持久层对象的实例,vParamForm表示层对象)            
            TranslatorUtil.copyVoToPersistence(vParamPersistence, vParamForm);
            
            //实例化连接对象
            conn = new DBConn();
            session = conn.beginTransaction();
           
            session.save(vParamPersistence);            
            session.flush();
            
            result = true;
        } catch (HibernateException e) {
        	result = false;
            log.printStackTrace(e);
        } finally {
            //如果连接状态有,则断开,结束事务,返回
            if (conn != null)
                conn.endTransaction(result);
        }
        return result;
    }

    /**
     * 取得按条件查询到的记录条数
     * 
     * @return int 查询到的记录条数
     * @param vParamForm
     *            包含查询的条件信息（币种ID，币种名称）
     */

    public static int getRecordCount(VParameterForm vParamForm) {
        int count = 0;

        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        //	 查询条件HQL的生成
        StringBuffer hql = new StringBuffer("select count(*) from VParameter vparam");
        StringBuffer where = new StringBuffer("");

        if (vParamForm != null) {
            // 查找条件的判断,查找名称不可为空
            if (vParamForm.getVpNote() != null
                    && !vParamForm.getVpNote().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "vparam.vpNote like '%" + vParamForm.getVpNote() + "%'");
        }

        try { //List集合的操作
            //初始化
            hql.append((where.toString().equals("") ? "" : " where ")
                    + where.toString());
            //conn对象的实例化
            conn = new DBConn();
            //打开连接开始会话
            session = conn.openSession();
            List list = session.find(hql.toString());
            if (list != null && list.size() == 1) {
                count = ((Integer) list.get(0)).intValue();
            }

        } catch (HibernateException he) {
            log.printStackTrace(he);
        } catch (Exception e) {
            log.printStackTrace(e);
        } finally {
            //如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.closeSession();
        }
        return count;
    }

    /**
     * 币种的查询
     * 
     * @param vParamForm
     *            VParameterForm 查询表单对象
     * @return List 如果查找到记录，返回List记录集；否则，返回null
     */
    public static List select(VParameterForm vParamForm, int offset, int limit) {

        //	List集合的定义
        List refVals = null;

        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        //	查询条件HQL的生成
        StringBuffer hql = new StringBuffer("from VParameter vparam");
        StringBuffer where = new StringBuffer("");
        if (vParamForm != null) {
            // 查找条件的判断,查找名称不可为空
            if (vParamForm.getVpNote() != null
                    && !vParamForm.getVpNote().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "vparam.vpNote like '%" + vParamForm.getVpNote() + "%'");
        }

        try { //List集合的操作
            //初始化
            
            hql.append((where.toString().equals("") ? "" : " where ")
                    + where.toString());
          
            //conn对象的实例化
            conn = new DBConn();
            //打开连接开始会话
            session = conn.openSession();
            //添加集合至Session
            //List list=session.find(hql.toString());
            Query query = session.createQuery(hql.toString());
            query.setFirstResult(offset).setMaxResults(limit);
            List list = query.list();

            if (list != null) {
                refVals = new ArrayList();
                //循环读取数据库符合条件记录
                for (Iterator it = list.iterator(); it.hasNext();) {
                    VParameterForm vParamFormTemp = new VParameterForm();
                    VParameter vParamPersistence = (VParameter)it.next();
                    TranslatorUtil.copyPersistenceToVo(vParamPersistence,
                    		vParamFormTemp);
                    refVals.add(vParamFormTemp);
                }
            }
        } catch (HibernateException he) {
            refVals = null;
            log.printStackTrace(he);
        } catch (Exception e) {
            refVals = null;
            log.printStackTrace(e);
        } finally {
            //如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.closeSession();
        }
        return refVals;
    }

    /**
     * 更新币种
     * 
     * @param vParamForm
     *            VParameterForm 存放数据的对象
     * @exception Exception
     *                如果VParameterForm更新失败,则捕捉抛出异常
     */
    public static boolean update(VParameterForm vParamForm) throws Exception {
        boolean result = false;
        DBConn conn = null;
        Session session = null;

        VParameter vParamPersistence = new VParameter();

        if (vParamForm == null) {
            return result;
        }
        try {
            if (vParamForm.getVpNote() == null
                    || vParamForm.getVpNote().equals("")) {
                return result;
            }
            conn = new DBConn();
            session = conn.beginTransaction();

            TranslatorUtil.copyVoToPersistence(vParamPersistence, vParamForm);
            session.update(vParamPersistence);

            result = true;
        } catch (HibernateException he) {
            log.printStackTrace(he);
        } finally {
            if (conn != null)
                conn.endTransaction(result);
        }
        return result;
    }

    /**
     * 编辑操作
     * 
     * @param vParamForm
     *            The VParameterForm 保持数据的传递
     * @exception Exception
     *                如果 VParameterForm 对象丢失则抛出异常.
     */
    public static boolean edit(VParameterForm vParamForm) throws Exception {
        boolean result = false;
        DBConn conn = null;
        Session session = null;

        VParameter mParamPersistence = new VParameter();
        vParamForm = new VParameterForm();
        vParamForm.getVpNote();

        if (vParamForm == null) {
            return result;
        }

        try {
            if (vParamForm.getVpNote() == null
                    && vParamForm.getVpNote().equals("")) {
                return result;
            }
            conn = new DBConn();
            session = conn.beginTransaction();

            mParamPersistence = (VParameter) session.load(VParameter.class, vParamForm.getVpNote());

            TranslatorUtil.copyVoToPersistence(mParamPersistence, vParamForm);

        } catch (HibernateException he) {
            log.printStackTrace(he);
        } finally {
            if (conn != null)
                conn.endTransaction(result);
        }
        return result;
    }

    /**
     * 删除操作
     * 
     * @param vParamForm
     *            VParameterForm 查询表单的对象
     * @return boolean 如果删除成功则返回true,否则false
     */
    public static boolean remove(VParameterForm vParamForm) throws Exception {
        //置标志result
        boolean result = false;
        //连接和会话对象的初始化
        DBConn conn = null;
        Session session = null;

        //mCurr是否为空,返回result
        if (vParamForm == null && vParamForm.getVpId() == null)
            return result;

        try {
            //	连接对象和会话对象初始化
            conn = new DBConn();
            session = conn.beginTransaction();
            //将vParamForm的货币单位的货币主键传入HQL中查询
            VParameter mCurr = (VParameter) session.load(VParameter.class, vParamForm
                    .getVpId());
            //会话对象删除持久层对象
            session.delete(mCurr);
            session.flush();

            //删除成功，置为true
            result = true;
        } catch (HibernateException he) {
            //捕捉本类的异常,抛出
            log.printStackTrace(he);
        } finally {
            //如果由连接则断开连接，结束会话，返回
            if (conn != null)
                conn.endTransaction(result);
        }
        return result;
    }

    /**
     * 查询一条记录,返回到EditAction中
     * 
     * @param vParamForm
     *            VParameterForm实例化对象
     * @return VParameterForm 包含一条记录
     */

    public static VParameterForm selectOne(VParameterForm vParamForm) {

        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        if (vParamForm != null)
         
        try {
            if (vParamForm.getVpId() != null
                    && !vParamForm.getVpId().equals(""))
                //conn对象的实例化
                conn = new DBConn();
            //打开连接开始会话
            session = conn.openSession();

            VParameter mCurrPersistence = (VParameter) session.load(VParameter.class,
                    vParamForm.getVpId());

            TranslatorUtil.copyPersistenceToVo(mCurrPersistence, vParamForm);

        } catch (HibernateException he) {
            log.printStackTrace(he);
        } catch (Exception e) {
            log.printStackTrace(e);
        } finally {
            //如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.closeSession();
        }
        return vParamForm;
    }
    
    /**
     * 查找某张表字段是否已经建立（区分事实表和维度表）
     * @param vParamForm
     * @return
     */
    public static boolean isExist(VParameterForm vParamForm) {

    	boolean bool = false;
        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        if (vParamForm == null) return bool;         
        try {
        	
        	String hql = "from VParameter vparam where vparam.vpTableid='"+ vParamForm.getVpTableid() 
        				+"' and vparam.vpColumnid='"+ vParamForm.getVpColumnid() +"' and vparam.vttId='"+ vParamForm.getVttId()+"'";
        	
        	conn = new DBConn();
            session = conn.openSession();

            List list = session.find(hql.toString());
            if (list != null && list.size() > 0) {
                bool = true;
            }


        } catch (HibernateException he) {
        	bool = false;
            log.printStackTrace(he);
        } catch (Exception e) {
        	bool = false;
            log.printStackTrace(e);
        } finally {
            //如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.closeSession();
        }
        return bool;
    }
    
    /**
     * Retrieve all existing VParameterForm objects.
     * 
     * @exception Exception
     *                If the VParameterForm objects cannot be retrieved.
     */
    public static List findAll() throws Exception {
        List retVals = new ArrayList();
        DBConn conn=new DBConn();
        Session session = null;
        session=conn.openSession();
        net.sf.hibernate.Transaction tx = session.beginTransaction();
        retVals.addAll(session.find("from VParameter vparam"));
        tx.commit();
        session.close();
        ArrayList mParam = new ArrayList();
        for (Iterator it = retVals.iterator(); it.hasNext();) {
            VParameterForm vParamFormTemp = new VParameterForm();
            VParameter vParamPersistence = (VParameter) it.next();
            TranslatorUtil.copyPersistenceToVo(vParamPersistence, vParamFormTemp);
            mParam.add(vParamFormTemp);
        }
        retVals = mParam;
        return retVals;
    }
  
    public static VParameter getVParameter(String curName) throws Exception {

        VParameter mCurr = null;

        DBConn dBConn = null;

        Session session = null;
        
        Query  query = null;
        
        List l = null;
        
        String hsql = "from VParameter mc where mc.curName=:curName";

        try {

            dBConn = new DBConn();

            session = dBConn.openSession();

            query = session.createQuery(hsql);
            
            query.setString("curName",curName);
            
            l = query.list();
            
            int  size = l.size();
            
            if(size!=0)
                
                mCurr = (VParameter)l.get(0);

        } catch (HibernateException e) {

            log.printStackTrace(e);

        }
        finally{
            
            if(session!=null)
                
                dBConn.closeSession();
        }

        return   mCurr;
    }
    
    /**
	 * 根据中文的表名和字段名,取得相对应的数据库表名和字段名(英文)
	 * 
	 * @param tableName_CN String 中文表名
	 * @param colName_CN String 中文字段名
	 * @return String[3] {英文表名,英文字段名,数据类型}
	 */
	public static VParameterForm getENNameByCNName(String tableName_CN, String colName_CN) throws Exception
	{
		VParameterForm result = null;

		Connection conn = (new FitechConnection()).getConnect();
		if (conn == null)
		{
			return null;
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try
		{
			if (tableName_CN != null && !tableName_CN.equals("") && colName_CN != null
				&& !colName_CN.equals(""))
			{
			
				String sql = "select * from V_PARAMETER where VP_TABLEDESC=? and VP_COLUMNDESC=?";

				stmt = conn.prepareStatement(sql);
				stmt.setString(1,tableName_CN.trim());
				stmt.setString(2,colName_CN.trim());
				rs = stmt.executeQuery();
				if(rs.next())
				{
					result = new VParameterForm();
					result.setVpId(new Integer(rs.getInt("VP_ID")));
					result.setVpTabledesc(rs.getString("VP_TABLEDESC"));;
					result.setVpColumndesc(rs.getString("VP_COLUMNDESC"));
					result.setVpTableid(rs.getString("VP_TABLEID"));
					result.setVpColumnid(rs.getString("VP_COLUMNID"));
					result.setVpColtype(new Integer(rs.getInt("VP_COLTYPE")));
				}

			}
		}
		catch (Exception e)
		{
			result = null;
			e.printStackTrace();
		}
		finally
		{
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return result;
		/*try
		{
			if (tableName_CN != null && !tableName_CN.equals("") && colName_CN != null
					&& !colName_CN.equals(""))
			{
				conn = new DBConn();
				session = conn.openSession();
				StringBuffer hql = new StringBuffer("from VParameter vp where vp.vpTabledesc=? and vp.vpColumndesc=?");

				Query query = session.createQuery(hql.toString());
				query.setString(0, tableName_CN);
				query.setString(1, colName_CN);
				List list = query.list();

				if (list != null && list.size() != 0)
				{
					result = new VParameterForm();
					VParameter vParamPersistence = (VParameter)list.get(0);
					TranslatorUtil.copyPersistenceToVo(vParamPersistence, result);
				}
			}

		}
		catch (Exception e)
		{
			log.printStackTrace(e);
		}
		finally
		{
			//如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}*/
		//return result;
	}
}