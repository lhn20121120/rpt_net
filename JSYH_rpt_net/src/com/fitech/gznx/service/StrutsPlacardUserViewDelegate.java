package com.fitech.gznx.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.adapter.TranslatorUtil;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.fitech.gznx.form.PlacardUserViewForm;
import com.fitech.gznx.po.AfPlacard;
import com.fitech.gznx.po.AfPlacardUserView;
import com.fitech.gznx.po.AfPlacardUserViewId;



public class StrutsPlacardUserViewDelegate {


	/**
	 * 根据条件查询公告信息
	 * 
	 * @author db2admin
	 * @date 2007-4-4
	 * @param placardUserViewForm
	 * @return
	 * 
	 */
	public static List select(PlacardUserViewForm placardUserViewForm)
	{
		if (placardUserViewForm == null || placardUserViewForm.getUserId() == null)
		{
			return null;
		}
		List result = null;
		DBConn conn = null;
		Session session = null;
		try
		{
			conn = new DBConn();
			session = conn.openSession();
			StringBuffer hql = new StringBuffer("from AfPlacardUserView puv where puv.id.orgId='"
					+ placardUserViewForm.getUserId() + "' ");
			Integer viewState = placardUserViewForm.getViewState();
			String title = placardUserViewForm.getTitle();
			String startDate = placardUserViewForm.getStartDate();
			String endDate = placardUserViewForm.getEndDate();

			if (viewState != null && !viewState.equals(Config.FLAG_ALL))
			{
				hql.append(" and puv.viewState=" + viewState);
			}
			if (title != null && !title.equals(""))
			{
				hql.append(" and puv.id.placard.title like '%" + title.trim() + "'");
			}
			if (startDate != null && !startDate.equals(""))
			{
				hql.append(" and to_char(puv.id.placard.publicDate,'yyyy-mm-dd')>='" + startDate.trim() + "'");
			}
			if (endDate != null && !endDate.equals(""))
			{
				hql.append(" and to_char(puv.id.placard.publicDate,'yyyy-mm-dd')<='" + endDate.trim() + "'");
			}

			hql.append(" order by puv.id.placard.placardId desc");

			Query query = session.createQuery(hql.toString());
			List list = query.list();
			if (list != null && list.size() != 0)
			{
				result = new ArrayList();
				for (Iterator it = list.iterator(); it.hasNext();)
				{
					PlacardUserViewForm form = new PlacardUserViewForm();
					AfPlacardUserView placardUserView = (AfPlacardUserView) it.next();
					TranslatorUtil.copyPersistenceToVo(placardUserView, form);
					result.add(form);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return result;
		}
		finally
		{
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	/**
	 * 根据用户名获得它没有查看公告信息	
	 * @author db2admin
	 * @date 2007-4-4	
	 * @param userId
	 * @return
	 *
	 */
	public static int getNoViewPlacard(String userId)
	{
		PlacardUserViewForm placardUserViewForm = new PlacardUserViewForm();
		placardUserViewForm.setUserId(userId);
		placardUserViewForm.setViewState(new Integer("0"));
		List list = select(placardUserViewForm);
		return list==null ? 0:list.size();
	}
	/**
	 * 改变查看的状态
	 * 
	 * @param placardId
	 *            Long 公告ID
	 * @param userId
	 *            String 用户ID
	 * @return
	 */
	public static boolean changeViewState(Long placardId, String userId)
	{
		boolean result = false;
		DBConn conn = null;
		Session session = null;	

		
		try
		{
			conn = new DBConn();
			session = conn.beginTransaction();
			
			AfPlacardUserViewId PlacardUserViewId = new AfPlacardUserViewId();
			PlacardUserViewId.setOrgId(userId);
			PlacardUserViewId.setPlacard(new AfPlacard(placardId));
			
			AfPlacardUserView record = (AfPlacardUserView)session.load(AfPlacardUserView.class,PlacardUserViewId);
			record.setViewState(new Long(1));
			session.update(record);
			session.flush();
			result = true;

		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}
		finally
		{
			if(conn!=null)
				conn.endTransaction(result);
		}
		
		return result;
	}
	

}
