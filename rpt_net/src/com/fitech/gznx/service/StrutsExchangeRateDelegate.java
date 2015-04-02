
package com.fitech.gznx.service;

import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;


import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.form.ExchangeRateForm;
import com.fitech.gznx.po.ExchangeRate;



/**
 * @author gongming
 * @date	2007-07-26
 *
 */
public class StrutsExchangeRateDelegate {

	private static FitechException log = new FitechException(
			StrutsExchangeRateDelegate.class);
	
	public StrutsExchangeRateDelegate() {}

	/**
	 * 已使用hibernate 卞以刚 2011-12-28
	 * 影响对象：ExchangeRate
	 * 分页浏览查询汇率
	 * @param rate				ExchangeRate		汇率
	 * @param apartPage			ApartPage			分页对象
	 * @return		List
	 */
	public static List getExchangeRates(ExchangeRateForm rateForm ,ApartPage apartPage){
		List changeRateLst = null;
		DBConn dbConn = null;
		Session session = null;
		try {
			dbConn = new DBConn();
			session = dbConn.openSession();
			StringBuffer sbfHql = new StringBuffer("from ExchangeRate t ");
			StringBuffer sbWhere=new StringBuffer("");
			
			boolean append = false;
			if(rateForm != null){
				if(rateForm.getSourceVcId() != null){
					String currCode = rateForm.getSourceVcId();
					if( currCode != null && !"all".equals(currCode)){
						sbWhere.append(sbWhere.toString().equals("")?"":" and ")
							.append(" ( t.sourceVcId='").append(currCode).append("' or ")
							.append(" t.targetVcId='").append(currCode).append("')");
					}
				}
				if(rateForm.getRateDate() != null && !rateForm.getRateDate().trim().equals("")){
					sbWhere.append(sbWhere.toString().equals("")?"":" and ")
						.append(" t.rateDate='").append(rateForm.getRateDate()).append("'");
				}

				if(!sbWhere.toString().equals(""))
					sbfHql.append(" where ").append(sbWhere.toString());
			}
			Query query = session.createQuery("select count(t.erId) " + sbfHql.toString());
			changeRateLst = query.list();
			int count=0;
			if(changeRateLst!=null && changeRateLst.size()==1){
				count=((Integer)changeRateLst.get(0)).intValue();
			}
			int offset=0;
			if(apartPage!=null){
				offset = (apartPage.getCurPage() - 1) * Config.PER_PAGE_ROWS;
				apartPage.setCount(count);
			}
			sbfHql.append(" order by t.rateDate desc,t.sourceVcId asc");
			query = session.createQuery(sbfHql.toString());
			query.setFirstResult(offset);
			query.setMaxResults(Config.PER_PAGE_ROWS);
			changeRateLst = query.list();
		} catch (Exception e) {
			changeRateLst = null;
			log.printStackTrace(e);
		} finally {
			if (dbConn != null)	dbConn.closeSession();
		}
		return changeRateLst;
	}
	
	/**
	 * 姓增或修改一个汇率
	 * @param exchangeRate		ExchangeRate		汇率
	 * @return	新增或修改成功返回	true	失败返回 false
	 */
	public static boolean saveOrUpdateExchangeRate(ExchangeRate exchangeRate,String todo){
		boolean insert = false;
		DBConn dbConn = null;
		Session session = null;
		try {
			if(exchangeRate != null){
				dbConn = new DBConn();
				session = dbConn.beginTransaction();
				if(todo.trim().equals("new")){
					if((exchangeRate = updateRateExpire(exchangeRate,session)) != null){
						session.saveOrUpdate(exchangeRate);
						session.flush();
						insert = true;
					}
				}else{
					session.saveOrUpdate(exchangeRate);
					session.flush();
					insert = true;
				}
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (dbConn != null)
				dbConn.endTransaction(insert);
		}
		return insert;
	}
	
	/**
	 * 已使用hibernate 卞以刚 2011-12-28
	 * 影响对象：ExchangeRate
	 * 更新汇率的用效期
	 * 
	 * @param exchangeRate		ExchangeRate		汇率
	 * @param session Session 与数据的连接会话
	 * @return boolean 更新成功，返回true;否则,返回false
	 */
	private static ExchangeRate updateRateExpire(ExchangeRate exchangeRate,Session session) throws Exception{
		if(exchangeRate == null || session == null) return null;
			
		//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
		//汇率的起始有效期
		//String starExpiDate = null;
		//汇率的结止用效期
		//String overExpiDate = null;
		
		//Date date = null;		//日期对象
		//Date prevDate = null;	//上一天日期对象
		//Date nextDate = null;   //下一天日期对象

		String hql = "from ExchangeRate e where e.sourceVcId=:currCode1 " +
			//"and e.rateDate<:rateDate and e.targetVcId=:currCode2 order by e.rateDate desc";
				"and e.rateDate=:rateDate and e.targetVcId=:currCode2 order by e.rateDate desc";

		Query query = session.createQuery(hql);
	
		query.setString("currCode1",exchangeRate.getSourceVcId());
		query.setString("rateDate",exchangeRate.getRateDate());
		query.setString("currCode2",exchangeRate.getTargetVcId());
		
		query.setFirstResult(0);
		query.setMaxResults(1);
		
		List list = query.list();
		
		if(list != null && list.size() == 1){
			//如果有相同记录，则返回null
			return null;
		}
			
/*					ExchangeRate exchRate = (ExchangeRate)list.get(0);
			//获取当前汇率的前一天，做为上个汇率的结止有效期
			//date = dateFormat.parse(exchangeRate.getRateDate());
			//prevDate = new Date(date.getTime() - 24*60*60*1000);	//上一天日期对象
						
			exchRate.setRateDate(exchRate.getRateDate());

			session.update(exchRate);
			session.flush();
			
			//当前汇率的起始有效期为当前
			//starExpiDate = exchangeRate.getRateDate();
		}

		else{
			//starExpiDate = "1000-01-01";
		}

		hql = "from ExchangeRate e where e.sourceVcId=:currCode1 " +
		"and e.rateDate>:rateDate and e.targetVcId=:currCode2 order by e.rateDate";
		query = session.createQuery(hql);
	
		query.setString("currCode1",exchangeRate.getSourceVcId());
		query.setString("rateDate",exchangeRate.getRateDate());
		query.setString("currCode2",exchangeRate.getTargetVcId());
		
		query.setFirstResult(0);
		query.setMaxResults(1);
		
		list = query.list();
		
		if(list != null && list.size() == 1){
			ExchangeRate exchRate = (ExchangeRate)list.get(0);
			exchRate.setRateDate(exchRate.getRateDate());
			//获取当前汇率的结止有效率为下一个汇率的日期的前一天
			//date = dateFormat.parse(exchRate.getRateDate());
			//prevDate = new Date(date.getTime() - 24*60*60*1000);	//上一天日期对象
						
			session.update(exchRate);
			session.flush();
			
			//overExpiDate = dateFormat.format(prevDate);
		}else{
			//overExpiDate = "9999-12-31";
		}
		
		//exchangeRate.setStarExpiDate(starExpiDate);
		//exchangeRate.setOverExpiDate(overExpiDate);
*/
		return exchangeRate;
	}
	
//	/**
//	 * 更新汇率的用效期
//	 * 
//	 * @param exchangeRate		ExchangeRate		汇率
//	 * @param session Session 与数据的连接会话
//	 * @return boolean 更新成功，返回true;否则,返回false
//	 */
//	private static boolean updateRateExpireByDelete(ExchangeRate exchangeRate,Session session) throws Exception{
//		boolean result = false;
//		
//		if(exchangeRate == null || session == null) return result;
//
//		String hql = "from ExchangeRate e where e.currencyCode1=:currCode1 " +
//			"and e.rateDate<:rateDate and e.currencyCode2=:currCode2 order by e.rateDate desc";
//		Query query = session.createQuery(hql);
//	
//		query.setString("currCode1",exchangeRate.getCurrencyCode1().getCurrCode());
//		query.setString("rateDate",exchangeRate.getRateDate());
//		query.setString("currCode2",exchangeRate.getCurrencyCode2().getCurrCode());
//		query.setFirstResult(0);
//		query.setMaxResults(1);
//		List list = query.list();
//		ExchangeRate prevExchRate = null;
//		if(list != null && list.size() == 1){
//			prevExchRate = (ExchangeRate)list.get(0);
//			prevExchRate.setRateDate(prevExchRate.getRateDate());
//		}
//
//		hql = "from ExchangeRate e where e.currencyCode1=:currCode1 " +
//		"and e.rateDate>:rateDate and e.currencyCode2=:currCode2 order by e.rateDate";
//		query = session.createQuery(hql);
//	
//		query.setString("currCode1",exchangeRate.getCurrencyCode1().getCurrCode());
//		query.setString("rateDate",exchangeRate.getRateDate());
//		query.setString("currCode2",exchangeRate.getCurrencyCode2().getCurrCode());
//		query.setFirstResult(0);
//		query.setMaxResults(1);
//		list = query.list();
//		ExchangeRate nextExchRate = null;
//		if(list != null && list.size() == 1){
//			nextExchRate = (ExchangeRate)list.get(0);
//			nextExchRate.setRateDate(nextExchRate.getRateDate());
//		}
//		
//		if(prevExchRate != null){
//			prevExchRate.setOverExpiDate(exchangeRate.getOverExpiDate());
//			session.update(prevExchRate);
//			session.flush();
//		}
//		
//		if(prevExchRate == null && nextExchRate != null){
//			nextExchRate.setStarExpiDate(exchangeRate.getStarExpiDate());
//			session.update(nextExchRate);
//			session.flush();
//		}
//		
//		result = true;
//		
//		return result;
//	}
	
	/**
	 * 已使用hibernate 卞以刚 2011-12-28
	 * 影响对象：ExchangeRate
	 * 通过汇率的主键查找汇率
	 * @param pId		Long	汇率Id
	 * @return	ExchangeRate
	 */
	public static ExchangeRate getExchangeRateById(Long pId){
		ExchangeRate rate = null;
		DBConn dbConn = null;
		Session session = null;
		try {
			if(pId != null){
				dbConn = new DBConn();
				session = dbConn.openSession();
				//rate = (ExchangeRate) session.get(ExchangeRate.class,pId);
				String hql = "from ExchangeRate e where e.erId=:erid";
				Query query = session.createQuery(hql);
				query.setLong("erid",pId.longValue());
				List list = query.list();
				if(list != null) rate = (ExchangeRate)list.get(0);
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (dbConn != null)
				dbConn.closeSession();
		}
		return rate;
	}
	
	/**
	 * 已使用hibernate 卞以刚 2011-12-28
	 * 影响对象：ExchangeRate
	 * 批量删除汇率
	 * @param pId		Long[]			 汇率Id 数组
	 * @return	成功返回	true 	失败返回	false
	 */
	public static boolean deleteExchangeRateById(String[] pId){
		boolean delete = false;
		DBConn dbConn = null;
		Session session = null;
		try {
			if (pId != null) {
				dbConn = new DBConn();
				session = dbConn.beginTransaction();
				int length = pId.length;
				for (int i = 0; i < length; i++) {
					//获取要删除的汇率
					ExchangeRate rate = (ExchangeRate) session.get(ExchangeRate.class, new Long(pId[i]));
					if (rate != null) {
						session.delete(rate);
					} else {
						delete = false;
						break;
					}
//					if(updateRateExpireByDelete(rate,session) == false){
//						delete = false;
//						break;
//					}
				}
				if (!delete) {
					session.flush();
					delete = true;
				}
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (dbConn != null)
				dbConn.endTransaction(delete);
		}
		return delete;
	}
	
	/**
	 * 已使用hibernate 卞以刚 2011-12-28
	 * 影响对象：VCurrency
	 * 查找所有货币类型
	 * 
	 * @return List
	 */
	public static List getCurrencys() {
		List lst = null;
		DBConn dbConn = null;
		Session session = null;
		
		try {
			String hql = "from VCurrency c order by c.VCcyId,c.VCcyEnname";
			
			if (hql != null) {
				dbConn = new DBConn();
				session = dbConn.openSession();
				Query query = session.createQuery(hql);
				lst = query.list();
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (dbConn != null)
				dbConn.closeSession();
		}
		return lst;
	}
}
