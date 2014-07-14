package com.fitech.gznx.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import com.fitech.gznx.common.PageListInfo;


/**
 * ��ʹ��hibernate ���Ը� 2011-12-21
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

            //wgm 09-3-20 ���setCurPageRowCount����Ϊ��ҳ��ò�����ȷ�ĵ�ǰҲ����������.
            //��ʾ��ʼ����15������ڶ�ҲʱҲ��15����ȷ��Ӧ����30����������ǽ��������
            //��ǰ�����ǵ�8ҳ������ѡ�е�����һҳ����Ӧ����120�Ŷԣ�������15��
            //---begin
            int count = curPage * pageSize;
            //��¼����û��pageSize(Ŀǰ��15��)��,��ʾʵ������
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

