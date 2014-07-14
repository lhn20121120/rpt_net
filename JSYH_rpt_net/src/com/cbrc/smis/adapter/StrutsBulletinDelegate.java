package com.cbrc.smis.adapter;

import java.util.List;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Order;

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.Bulletin;
import com.cbrc.smis.util.FitechException;

/**
*
* <p>标题: StrutsBulletinDelegate</p>
*
* <p>描述: Bulletin对象的数据库操作 </p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   龚明
* @date     2008-1-15
* @version 1.0
*/

public class StrutsBulletinDelegate
{
    private static FitechException log = new FitechException(StrutsBulletinDelegate.class);
    
    /**
     * 分页获取公告
     * @param page      ApartPage
     * @return
     */
    public static List findBulletin(ApartPage page)
    {
        DBConn dbConn = null;
        Session session = null;
        List rsltLst = null;
        try
        {
            dbConn  = new DBConn();
            session = dbConn.openSession();
            Criteria criteria = session.createCriteria(Bulletin.class);
            criteria.addOrder(Order.desc("bullId"));
            rsltLst = criteria.list();
//          提取分页记录
            if(rsltLst != null && !rsltLst.isEmpty())
            {
                int from = (page.getCurPage()-1)*Config.PER_PAGE_ROWS;  
                int to = from + Config.PER_PAGE_ROWS;
                int size = rsltLst.size();
                if(from >= size) from = 0;
                if(to > size) to = size;
                page.setCount(size);
                rsltLst = rsltLst.subList(from,to); 
            }            
        }
        catch (Exception e)
        {
            log.printStackTrace(e);
        }
        finally
        {
            if(dbConn != null)
                dbConn.closeSession();
        }
        return rsltLst;        
    }
    
    /**
     * 根据主键查找公告
     * @param pId
     * @return
     */
    public static Bulletin findBulletinById(Integer pId)
    {
        DBConn dbConn = null;
        Session session = null;
        Bulletin bulletin = null;
        try
        {
            dbConn  = new DBConn();
            session = dbConn.openSession();
            bulletin = (Bulletin) session.get(Bulletin.class,pId);            
        }
        catch (Exception e)
        {
            log.printStackTrace(e);
        }
        finally
        {
            if(dbConn != null)
                dbConn.closeSession();
        }
        return bulletin;        
    }
}
