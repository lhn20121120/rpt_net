package com.fitech.gznx.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import com.fitech.gznx.common.PageListInfo;


/**
 * 已使用hibernate 卞以刚 2011-12-21
 * User: Rui.Feng Date: 2008-5-26 Time: 14:01:21
 */
public abstract class DaoModel  {
    
    public static PageListInfo findByPageWithSQL(Session session,String queryStr,  int pageSize , int curPage){
        PageListInfo pageListInfo = new PageListInfo(pageSize,curPage);
        try {
        	
            Query  query;
            query =  session.createQuery("select count(*) "+queryStr);
            Integer i = (Integer)query.list().get(0);
            pageListInfo.setRowCount(i);
            int firstPage = (pageListInfo.getCurPage()-1)*pageSize;
            query =  session.createQuery(queryStr);
            query.setFirstResult(firstPage);
            query.setMaxResults(pageSize);
            pageListInfo.setList(query.list());

            //wgm 09-3-20 添加setCurPageRowCount，因为在页面得不到正确的当前也的数据条数.
            //显示的始终是15，点击第二也时也是15，正确的应该是30，下面的正是解决此问题
            //当前看的是第8页（不论选中的是哪一页），应当是120才对，可是是15，
            //---begin
            int count = curPage * pageSize;
            //记录条数没有pageSize(目前是15条)条,显示实际条数
            if(pageListInfo.getRowCount()<pageSize || curPage == pageListInfo.getPageCount())
            	count = (int)pageListInfo.getRowCount();
            pageListInfo.setCurPageRowCount(count);
            //---end
            
            return pageListInfo;
        }catch (RuntimeException re) {
            
            throw re;
        }catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pageListInfo;
    }
}

