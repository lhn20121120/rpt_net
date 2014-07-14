
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
	 * ��ʹ��hibernate ���Ը� 2011-12-28
	 * Ӱ�����ExchangeRate
	 * ��ҳ�����ѯ����
	 * @param rate				ExchangeRate		����
	 * @param apartPage			ApartPage			��ҳ����
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
	 * �������޸�һ������
	 * @param exchangeRate		ExchangeRate		����
	 * @return	�������޸ĳɹ�����	true	ʧ�ܷ��� false
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
	 * ��ʹ��hibernate ���Ը� 2011-12-28
	 * Ӱ�����ExchangeRate
	 * ���»��ʵ���Ч��
	 * 
	 * @param exchangeRate		ExchangeRate		����
	 * @param session Session �����ݵ����ӻỰ
	 * @return boolean ���³ɹ�������true;����,����false
	 */
	private static ExchangeRate updateRateExpire(ExchangeRate exchangeRate,Session session) throws Exception{
		if(exchangeRate == null || session == null) return null;
			
		//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
		//���ʵ���ʼ��Ч��
		//String starExpiDate = null;
		//���ʵĽ�ֹ��Ч��
		//String overExpiDate = null;
		
		//Date date = null;		//���ڶ���
		//Date prevDate = null;	//��һ�����ڶ���
		//Date nextDate = null;   //��һ�����ڶ���

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
			//�������ͬ��¼���򷵻�null
			return null;
		}
			
/*					ExchangeRate exchRate = (ExchangeRate)list.get(0);
			//��ȡ��ǰ���ʵ�ǰһ�죬��Ϊ�ϸ����ʵĽ�ֹ��Ч��
			//date = dateFormat.parse(exchangeRate.getRateDate());
			//prevDate = new Date(date.getTime() - 24*60*60*1000);	//��һ�����ڶ���
						
			exchRate.setRateDate(exchRate.getRateDate());

			session.update(exchRate);
			session.flush();
			
			//��ǰ���ʵ���ʼ��Ч��Ϊ��ǰ
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
			//��ȡ��ǰ���ʵĽ�ֹ��Ч��Ϊ��һ�����ʵ����ڵ�ǰһ��
			//date = dateFormat.parse(exchRate.getRateDate());
			//prevDate = new Date(date.getTime() - 24*60*60*1000);	//��һ�����ڶ���
						
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
//	 * ���»��ʵ���Ч��
//	 * 
//	 * @param exchangeRate		ExchangeRate		����
//	 * @param session Session �����ݵ����ӻỰ
//	 * @return boolean ���³ɹ�������true;����,����false
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
	 * ��ʹ��hibernate ���Ը� 2011-12-28
	 * Ӱ�����ExchangeRate
	 * ͨ�����ʵ��������һ���
	 * @param pId		Long	����Id
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
	 * ��ʹ��hibernate ���Ը� 2011-12-28
	 * Ӱ�����ExchangeRate
	 * ����ɾ������
	 * @param pId		Long[]			 ����Id ����
	 * @return	�ɹ�����	true 	ʧ�ܷ���	false
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
					//��ȡҪɾ���Ļ���
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
	 * ��ʹ��hibernate ���Ը� 2011-12-28
	 * Ӱ�����VCurrency
	 * �������л�������
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
