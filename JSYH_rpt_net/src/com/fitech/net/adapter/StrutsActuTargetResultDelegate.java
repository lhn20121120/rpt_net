package com.fitech.net.adapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.DateUtil;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MActuRepForm;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.MDataRgType;
import com.cbrc.smis.hibernate.MRepFreq;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.hibernate.ReportInInfo;
import com.cbrc.smis.util.FitechEXCELReport;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.common.CommMethod;
import com.fitech.net.config.Config;
import com.fitech.net.form.ActuTargetResultForm;
import com.fitech.net.form.FormulaForm;
import com.fitech.net.form.TargetDefineForm;
import com.fitech.net.hibernate.ActuTargetResult;
import com.fitech.net.hibernate.TargetDefine;
import com.fitech.net.hibernate.TargetDefineWarn;
import com.fitech.net.obtain.text.Formula;

public class StrutsActuTargetResultDelegate {
	//Catch到本类的抛出的所有异常
	private static FitechException log=new FitechException(StrutsActuTargetResultDelegate.class);

	/**
	 * 删除指标结果
	 * 
	 * @param list 要删除的指标值对应指标列表
	 * @param orgId 机构ID
	 * @param year 指标年份
	 * @param month 指标月份
	 * @return boolean true--删除成功 false--删除失败
	 */
	public static boolean deleteResult(List list,String orgId,Integer year,Integer month){
		if(list == null && list.size() <= 0) return false;
		
		boolean bool = false;
		TargetDefine targetDefine = null;
		String targetDefineIds = "";
		DBConn conn = null;
		Session session = null;
		try{
			conn = new DBConn();
            session = conn.beginTransaction();
			
			for(Iterator iter=list.iterator();iter.hasNext();){
				targetDefine = (TargetDefine)iter.next();
				targetDefineIds += (targetDefineIds.equals("") ? "" : ",") + targetDefine.getTargetDefineId();
			}
			String hql = "from ActuTargetResult atr where atr.year="+ year +
						 " and atr.month="+ month +" and atr.orgId="+ orgId +
						 " and atr.targetDefine.targetDefineId in ("+ targetDefineIds +")";
			
			session.delete(hql);
			conn.endTransaction(true);
			bool = true;
		}catch(Exception e){
			e.printStackTrace();
			log.printStackTrace(e);
		}finally{
			try{
				if(conn!=null) conn.closeSession();
				if(session!=null) session.close();
			}catch(HibernateException e){
				log.printStackTrace(e);
			}
		}
		return bool;
	}

	/**
	 * 查找出于指标相关的信息
	 * @param form
	 * @return
	 */
	public static ActuTargetResultForm searchDetail(ActuTargetResultForm form ,String type){
		Integer targetId=form.getTargetDefineId();
		Integer Id=form.getId();
		if(targetId==null || Id==null ) return null;
		
		DBConn conn=null;
		Session session=null;
		//用于获取所有警告分类信息
		String inner=com.fitech.net.figure.Config.SPLIT_INNER;
		String outer=com.fitech.net.figure.Config.SPLIT_OUTER;
		String allWarnMessage="";
		
		try{
			conn=new DBConn();
			session=conn.openSession();
			//指标定义
			String sql="from TargetDefine t where t.targetDefineId="+targetId;
			List list =session.find(sql);
			
			if(list!=null && list.size()>0){   
				//获取指标名称和公式
				TargetDefine t=(TargetDefine)list.get(0);
	            form.setTargetDefineName(t.getDefineName());
	            form.setPreFormula(t.getFormula());
			}
			sql="from ActuTargetResult actu where actu.id="+Id;
			list=session.find(sql);
			if(list!=null && list.size()>0){   
				//获取指标值
				ActuTargetResult actu=(ActuTargetResult)list.get(0);
				//格式化显示
				//form.setTargetResult(new Float(formatNumber(actu.getTargetResult().floatValue())));
				form.setTargetResult(new Float(actu.getTargetResult()));
			}
			
			if(type.equals(Config.Target_Warn))  //预警 
				 sql = "from TargetDefineWarn t where t.targetDefine.targetDefineId="+targetId
				    	+"  and type='"+Config.Target_Warn+"'"+"order by t.upLimit asc";
			else
				 sql = "from TargetDefineWarn t where t.targetDefine.targetDefineId="+targetId
				 		+"  and type='"+Config.Target_Pre_Standard+"'"+"order by t.upLimit asc";
				 
			list=session.find(sql);
			if(list!=null && list.size()>0){
			   //获取所有的警告信息
			   float result=type.equals(Config.Target_Warn)?form.getTargetResult().floatValue(): Float.parseFloat(form.getPreStandardValue());
			   for(int i=0;i<list.size();i++){
				   TargetDefineWarn tar=(TargetDefineWarn)list.get(i);
				   allWarnMessage+=tar.getUpLimit()+inner+tar.getDownLimit()+inner+tar.getColor().trim()+outer;
				   if(result>tar.getUpLimit().doubleValue() && result<tar.getDownLimit().doubleValue()){
					   form.setTemp1(tar.getUpLimit().toString());
					   form.setTemp2(tar.getDownLimit().toString());
					   form.setColor(tar.getColor());
				   }
			   }	
			   //所有的分段预警信息
			   form.setAllWarnMessage(allWarnMessage);
			}
			if(form.getColor()==null ||form.getColor().equals("")) form.setColor("red");
		}catch(Exception e){
			e.printStackTrace();
			log.printStackTrace(e);
		}finally{
			try{
				if(conn!=null) conn.closeSession();
				if(session!=null) session.close();
			}catch(HibernateException e){
				log.printStackTrace(e);
			}
		}
		return form;
	}
	
	/**
	 * 查找指标结果记录数
	 * 
	 * @param form
	 * @return
	 */
	public static int searchCount(ActuTargetResultForm form){
		if(form==null)return 0;
		
		String name=form.getTargetDefineName();
		Integer year=form.getYear();
		Integer month=form.getMonth();
		String orgId=form.getOrgId();
		int resultCount=0;
		DBConn conn=null;
		Session session=null;
		try{
			StringBuffer sql= new StringBuffer("select count(*) from ActuTargetResult  result  where 1=1 ");
			if(name!=null && !name.equals(""))
				sql.append(" and result.targetDefine.defineName like'%"+name+"%'");
            if(year!=null && year.intValue()!=0)
            	sql.append(" and result.year="+year);
            if(month!=null && month.intValue()!=0)
            	sql.append(" and result.month="+month);
            if(orgId!=null)
            	sql.append(" and result.orgId='"+orgId+"'");
            
            conn=new DBConn();
            session=conn.openSession();
            List list=session.find(sql.toString());
            if(list!=null && list.size()==1)
            	resultCount=((Integer)list.get(0)).intValue();
		}catch(Exception e){
			e.printStackTrace();
			log.printStackTrace(e);
		}finally{
			try{
				if(conn!=null) conn.closeSession();
				if(session!=null) session.close();
			}catch(Exception ex){
				log.printStackTrace(ex);
			}
		}
		return resultCount;
	}
	
	/**
	 * 查询指标结果集
	 * 
	 * @param form
	 * @return
	 */
	public static List search(ActuTargetResultForm form){
		if(form==null)return null;
		String name=form.getTargetDefineName();
		Integer year=form.getYear();
		Integer month=form.getMonth();
		String orgId=form.getOrgId();
		Integer freId=form.getRepFreId();
		Integer RangeId=form.getDataRangeId();
		
		List refval=null;
		DBConn conn=null;
		Session session=null;
		try{
			StringBuffer sql= new StringBuffer("from ActuTargetResult result where 1=1 ");
			if(name!=null && !name.equals(""))
				sql.append(" and result.targetDefine.defineName like '%"+name.trim()+"%' ");
            if(year!=null && year.intValue()!=0)
            	sql.append(" and result.year="+year);
            if(month!=null && month.intValue()!=0)
            	sql.append(" and result.month="+month);
            if(orgId!=null)
            	sql.append(" and result.orgId='"+orgId+"'");
            if(freId!=null)
            	sql.append(" and result.repFreId="+freId);
            if(RangeId!=null)
            	sql.append(" and result.dataRangeId="+RangeId);
            if(form.getBusinessId()!=null && !form.getBusinessId().equals(new Integer(-1)))
            	sql.append(" and result.targetDefine.mbusiness.businessId='"+form.getBusinessId()+"'");
            sql.append(" order by result.targetDefine.mbusiness.businessId,result.targetDefine.mnormal.normalId,result.targetDefine.defineName,result.dataRangeId");
            conn=new DBConn ();
            session=conn.openSession();
            Query query=session.createQuery(sql.toString());
            List list=query.list();
            if(list!=null && list.size()>0){
            	refval=new ArrayList();
            	for(Iterator it=list.iterator();it.hasNext();){
            		ActuTargetResultForm  resultForm=new ActuTargetResultForm();
            		ActuTargetResult result=(ActuTargetResult)it.next();
            		
            		TranslatorUtil.copyPersistenceToVo(result,resultForm);            		
            		//得到各个字段的名称
            		getOther(resultForm);
            		//设置颜色
            		if(resultForm.getTargetResult()!=null)
            		    setColor(resultForm,Config.Target_Warn);
            		//设置比上期值   
            		setPreStandard(resultForm,Config.Target_Pre_Standard);
            		//设置比上年同期值
            		setPreStandard(resultForm,Config.Target_Last_Year_Same_Month_Standard);
            		//设置比上年年末值
            		setPreStandard(resultForm,Config.Target_Last_Year_December_Standard);
            		refval.add(resultForm);
            	}
            }
		}catch(Exception e){
			e.printStackTrace();
			log.printStackTrace(e);
		}finally{
			try{
				if(conn!=null) conn.closeSession();
				if(session!=null) session.close();
			}catch(Exception ex){
				log.printStackTrace(ex);
			}
		}
		return refval;
	}
	
	/**
	 * 计算指标值并设置颜色
	 * 
	 * @param resultForm
	 * @param calculateFlag
	 */
	public static void setPreStandard(ActuTargetResultForm resultForm,String calculateFlag){
		if(resultForm.getRepFreName()==null || resultForm.getRepFreName().equals(""))return ;
		
		String frequence=resultForm.getRepFreId().toString();
		Integer year=resultForm.getYear();
		Integer month=resultForm.getMonth();
		//上一年,上一月
		Integer preYear=null;
		Integer preMonth=null;

		if(frequence!=null && !frequence.equals("")){ //得到上期的年月
		  String[] preDate=calculateDate(year.toString(),month.toString(),frequence,calculateFlag);
		  preYear=new Integer(preDate[0]);
		  preMonth=new Integer(preDate[1]);
		}
		if(preYear!=null && preMonth!=null){
			resultForm.setYear(preYear);
			resultForm.setMonth(preMonth);
			//取得上一期
			List list=selectTargetResult(resultForm);
			if(list!=null && list.size()>0){
				Float tem2 = null;
				try{
				  tem2 = new Float(((ActuTargetResult)list.get(0)).getTargetResult());
				}catch(Exception ex){
				  tem2 = null;
				}
				Float tem1=resultForm.getTargetResult();
				if(tem1 != null && tem2 != null){
					//计算比上期			
					//float r=StrictMath.abs((tem1.floatValue()-tem2.floatValue())/tem1.floatValue());
					float r=tem1.floatValue()-tem2.floatValue();
					String tempStandardValue=null;   //默认处理	
					if(Float.isNaN(r) || r==0.0){
						tempStandardValue="0.00";
					}else{
						tempStandardValue=formatNumber(r);
					}
					
					//根据标志位不同设置相应的值
					if(calculateFlag.equals(Config.Target_Pre_Standard)){
						resultForm.setPreStandardValue(tempStandardValue.trim());
//					}else if(calculateFlag.equals(Config.Target_Last_Year_Same_Month_Standard)){
//						resultForm.setLastYearSameMonthStandardValue(tempStandardValue.trim());
//					}else if(calculateFlag.equals(Config.Target_Last_Year_December_Standard)){
//						resultForm.setLastYearDecemberStandardValue(tempStandardValue.trim());
					}

					
					//设置颜色
					setColor(resultForm,calculateFlag);
				}
			}
			resultForm.setYear(year);
			resultForm.setMonth(month);
		}
	}
	
	/**
	 * 查找指标结果集
	 * 
	 * @param form
	 * @return
	 */
	public static List selectTargetResult(ActuTargetResultForm form){
		if(form==null)return null;
		String name=form.getTargetDefineName().trim();
		Integer year=form.getYear();
		Integer month=form.getMonth();
		String  orgId=form.getOrgId();
		Integer freId=form.getRepFreId();
		Integer RangeId=form.getDataRangeId();
		List refval=null;
		DBConn conn=null;
		Session session=null;
		try{
			StringBuffer sql= new StringBuffer("from ActuTargetResult result where 1=1");
			if(name!=null && !name.equals(""))
				sql.append(" and result.targetDefine.defineName='"+ form.getTargetDefineName()+"'");
            if(year!=null)
            	sql.append(" and result.year="+year);
            if(month!=null)
            	sql.append(" and result.month="+month);
            if(orgId!=null)
            	sql.append(" and result.orgId='"+orgId+"'");
            if(freId!=null)
            	sql.append(" and result.repFreId="+freId);
            if(RangeId!=null)
            	sql.append(" and result.dataRangeId="+RangeId);
            
            conn=new DBConn ();
            session=conn.openSession();
            Query query=session.createQuery(sql.substring(0));
            refval=query.list();
		}catch(Exception e){
			e.printStackTrace();
			log.printStackTrace(e);
		}finally{
			try{
				if(conn!=null) conn.closeSession();
				if(session!=null) session.close();
			}catch(Exception ex){
				log.printStackTrace(ex);
			}
		}
		return refval;
	}

	/**
	 * 设置所在其间的颜色
	 * 
	 * @param resultForm
	 * @param type
	 */
	public static void setColor(ActuTargetResultForm resultForm,String type){
		DBConn conn=null;
		Session session=null;
		try{
			String sql="from TargetDefineWarn tw where tw.targetDefine.targetDefineId="+resultForm.getTargetDefineId()
			            +" and tw.type='"+type+"'";
			conn=new DBConn();
			session=conn.openSession();
			List list=session.find(sql);

			String color=null;
			if(list!=null &&list.size()>0){
				for(Iterator it=list.iterator();it.hasNext();){
					TargetDefineWarn targetDefineWarn=(TargetDefineWarn)it.next();
					if(type.equals(Config.Target_Warn)){
						Float re=resultForm.getTargetResult();
						if(re.doubleValue()>=targetDefineWarn.getUpLimit().doubleValue() && re.doubleValue()<=targetDefineWarn.getDownLimit().doubleValue())
							color=targetDefineWarn.getColor().trim();
					}else if(type.equals(Config.Target_Pre_Standard)){
						if(resultForm.getPreStandardValue().equals("∞")){
							color="blue";
						}else{
						Float rep=Float.valueOf(resultForm.getPreStandardValue());
						if(rep.doubleValue()>=targetDefineWarn.getUpLimit().doubleValue() && rep.doubleValue()<=targetDefineWarn.getDownLimit().doubleValue())
							color=targetDefineWarn.getColor().trim();
						}
					}else if(type.equals(Config.Target_Last_Year_Same_Month_Standard)){
//						if(resultForm.getLastYearSameMonthStandardValue().equals("∞")){
//							color="blue";
//						}else{
//						Float rep=Float.valueOf(resultForm.getLastYearSameMonthStandardValue());
//						if(rep.doubleValue()>=targetDefineWarn.getUpLimit().doubleValue() && rep.doubleValue()<=targetDefineWarn.getDownLimit().doubleValue())
//							color=targetDefineWarn.getColor().trim();
//						}
					}else if(type.equals(Config.Target_Last_Year_December_Standard)){
//						if(resultForm.getLastYearDecemberStandardValue().equals("∞")){
//							color="blue";
//						}else{
//						Float rep=Float.valueOf(resultForm.getLastYearDecemberStandardValue());
//						if(rep.doubleValue()>=targetDefineWarn.getUpLimit().doubleValue() && rep.doubleValue()<=targetDefineWarn.getDownLimit().doubleValue())
//							color=targetDefineWarn.getColor().trim();
//						}
					}
				}
			}
			if(color==null)color="red";//默认处理
			if(type.equals(Config.Target_Warn)) resultForm.setColor(color);
			if(type.equals(Config.Target_Pre_Standard))	resultForm.setPreStandardColor(color);
//			if(type.equals(Config.Target_Last_Year_Same_Month_Standard)) resultForm.setLastYearSameMonthStandardColor(color);
//			if(type.equals(Config.Target_Last_Year_December_Standard))	resultForm.setLastYearDecemberStandardColor(color);
		}catch(Exception e){
			e.printStackTrace();
			log.printStackTrace(e);
		}
		finally{
			try{
				if(conn!=null)conn.closeSession();
				if(session!=null)session.close();
			}catch(HibernateException e){
				log.printStackTrace(e);
			}
		}
	}
	
	/**
	 * 得到详细信息
	 * 
	 * @param resultForm
	 */
	public static void getOther(ActuTargetResultForm  resultForm){
		DBConn conn=null;
		Session session=null;
		try{
			conn=new DBConn();
			session=conn.openSession();
			if(resultForm.getCurUnitId()!=null){ // 货币名称
				String sql=" from MCurr mc where mc.curId="+resultForm.getCurUnitId();
				List list=session.find(sql);
				if(list!=null && list.size()>0){
					String name=((MCurr)list.get(0)).getCurName();
					resultForm.setCurUnitName(name);
				}
			}
			if(resultForm.getDataRangeId()!=null){ //口径
				String sql=" from  MDataRgType dr where dr.dataRangeId="+resultForm.getDataRangeId();
				List list=session.find(sql);
				if(list!=null && list.size()>0){
					String name=((MDataRgType)list.get(0)).getDataRgDesc();
				    resultForm.setDataRangeName(name);
				}
			}
			if(resultForm.getRepFreId()!=null){ //频率
				String sql="from MRepFreq mr where mr.repFreqId="+resultForm.getRepFreId();
				List list=session.find(sql);
				if(list!=null && list.size()>0){
					String name=((MRepFreq)list.get(0)).getRepFreqName();
				    resultForm.setRepFreName(name);
				}
			}			
		}catch(Exception e){
			e.printStackTrace();
			log.printStackTrace(e);
		}finally{
			try{
				if(conn!=null)conn.closeSession();
				if(session!=null)session.close();
			}catch(HibernateException e){
				log.printStackTrace(e);
			}
		}
	}
	
	/**
	 * 根据机构Id查出指标
	 * 过滤规则：1，必须是在Target_Range表中授权的机构对应的指标
	 *         2，必须在指标的有效期内
	 * @param orgId
	 * @return
	 */
	public static List searchTargetList(String orgId,Integer year,Integer month){
		if(orgId==null || year==null || month==null) return null;
		
		List refVal=null;
		DBConn conn=null;
		Session session=null;
		try{
			String sql=null;
			/**已增加数据库判断 卞以刚 2012-01-18*/
			if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("sqlserver")){
				sql = "from TargetDefine t where t.targetDefineId in (select tr.targetDefineId from TargetRange tr "+
				  "where tr.orgId='"+orgId+"') and convert(datetime,'"+year+"-"+month+"-01',120) " +
				  "between convert(datetime,t.startDate,120) and convert(datetime,t.endDate,120)";
			}
			if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("oracle")){
				sql = "from TargetDefine t where t.targetDefineId in (select tr.targetDefineId from TargetRange tr "+
					  "where tr.orgId='"+orgId+"') and to_date('"+year+"-"+month+"-01','yyyy-MM-dd') " +
					  "between to_date(t.startDate,'yyyy-MM-dd') and to_date(t.endDate,'yyyy-MM-dd')";
			}else if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("MySQL")){
				sql = "from TargetDefine t where t.targetDefineId in (select tr.targetDefineId from TargetRange tr "+
				  	  "where tr.orgId='"+orgId+"') and DATE_FORMAT(\""+ year+"-"+month+"-01\",\"%Y%m%d\") " +
				      "between DATE_FORMAT(t.startDate,\"%Y%m%d\") and DATE_FORMAT(t.endDate,\"%Y%m%d\")";				
        	}
			conn=new DBConn();
			session=conn.openSession();
			refVal=session.find(sql);
		}catch(Exception e){
			e.printStackTrace();
			log.printStackTrace(e);
		}
		finally{
			try{
				if(conn!=null) conn.closeSession();
				if(session!=null) session.close();
			}catch(Exception ex){
				log.printStackTrace(ex);
			}
		}
		return refVal;
	}

	/**
	 * 
	 * @param actuTargetResultForm
	 * @return
	 */
	public static List Exit(ActuTargetResultForm actuTargetResultForm){
		List refval=null;
		DBConn conn=null;
		Session session=null;
		try{
			String sql="from ActuTargetResult actu where actu.year="+actuTargetResultForm.getYear()
			               +" and actu.month="+actuTargetResultForm.getMonth()
			               +" and actu.orgId='"+actuTargetResultForm.getOrgId()+"'"
			               +" and actu.targetDefine.targetDefineId="+actuTargetResultForm.getTargetDefineId()
			               +" and actu.curUnitId="+actuTargetResultForm.getCurUnitId()
			               +" and actu.dataRangeId="+actuTargetResultForm.getDataRangeId()
			               +" and actu.repFreId="+actuTargetResultForm.getRepFreId();
			conn=new DBConn();
			session=conn.openSession();
			List list=session.find(sql);
			if(list!=null && list.size()>0)
				refval=list;
		}
		catch(Exception e){
			e.printStackTrace();
			log.printStackTrace(e);
		}finally{
			try{
				if(conn!=null) conn.closeSession();
				if(session!=null) session.close();
			}catch(Exception ex){
				log.printStackTrace(ex);
			}
		}
		return refval;
	
	}

	/**
	 * 保存指标生成数据
	 * 
	 * @param actuTargetResultForm
	 * @return boolean true-生成成功 false-生成失败
	 */
	public static boolean  saveData(ActuTargetResultForm actuTargetResultForm){
		boolean result=false;
		DBConn conn=null;
		Session session = null;
		if(actuTargetResultForm.getTargetResult()==null) 
			return false;	
		
		try{
//			List list=Exit(actuTargetResultForm);
			conn = new DBConn();;
//			if(list!=null && list.size()>0){
//			
//				ActuTargetResult actu=(ActuTargetResult)list.get(0);
//				actu.setTargetResult(formatNumber(actuTargetResultForm.getTargetResult().floatValue()));
//				session = conn.beginTransaction();
//				session.update(actu);
//				result=true;
//			}else{
				ActuTargetResult actu=new ActuTargetResult();
				TranslatorUtil.copyVoToPersistence(actu,actuTargetResultForm);
				session = conn.beginTransaction();
				session.save(actu);
				result=true;
//			}
		}catch(Exception e){
			e.printStackTrace();
			log.printStackTrace(e);
		}finally{
			try{
				if(conn!=null)conn.endTransaction(result);
				if(session!=null)session.close();
			}catch(HibernateException e){
				log.printStackTrace(e);
			}
	   }
	   return result;
	}

	/**
	 * 按报送口径生成单个指标值
	 * 
	 * @param atrForm 指标结果FormBean
	 * @param mActuRepForm 报送频度FormBean
	 * @param formula 指标公式
	 * @return boolean true-指标生成成功 false-指标生成失败
	 */
	public static boolean generateTargetResult(ActuTargetResultForm atrForm,MActuRepForm mActuRepForm,String formula){
		boolean result = false;
		
		if(atrForm == null || mActuRepForm == null || formula == null){
			atrForm.setAllWarnMessage("……生成指标方法参数不全！");
			return result;
		}
		
		try{
			/**判断该指标是否有折年系数*/
			if(formula!=null && formula.length()>3){
				String newFormual = formula.substring(formula.length()-2,formula.length());
				/**当公式后有*N的时候,认为这个指标有折年系数 */
				if(newFormual!=null && newFormual.equalsIgnoreCase("*n")){
					atrForm.setTemp1("Quotiety");  //借用了Temp1字段,quotiety为折年系数
					formula = formula.substring(0,formula.length()-2);
				}
			}
			
			/**判断该指标是否有平均余额*/
			String formulaAVG = null;
			String formulaAVGValue = null;
			if(formula!=null && formula.length()>3){
				int formualFlag=formula.indexOf("[");
				/**当公式后有[]的时候,认为这个指标有平均余额*/
				if(formualFlag>0){
					atrForm.setTemp2("AvgBalnace"); //借用了Temp2字段,AvgBalnace为平均余额
					//得到要计算平均余额的公式
					formulaAVG=formula.substring(formualFlag+1,formula.length()-1);
					formula = formula.substring(0,formualFlag);
				}
			}
			
			/**是否有平均余额 如有，直接解析公式的值*/
			if(formulaAVG != null && !formulaAVG.equals("")){
				//得到平均余额公式的值
				formulaAVGValue = StrutsActuTargetResultDelegate.getformualAVGValue(atrForm,mActuRepForm,formulaAVG);
				if(formulaAVGValue == null){
					if(atrForm.getAllWarnMessage() == null)
						atrForm.setAllWarnMessage("解析平均余额部分出错！");					
					
					return result;
				}
				formula+=formulaAVGValue;
			}
			
			/**是否有IF语句公式*/
			if((formula.indexOf("if"))>0 || (formula.indexOf("IF"))>0){
				formula = StrutsActuTargetResultDelegate.checkIF(atrForm,mActuRepForm,formula);
			}
			
			//格式化公式
			atrForm.setPreFormula(formula);
			result = StrutsActuTargetResultDelegate.parseFormula(atrForm,formula);
			if(result == false){
				if(atrForm.getAllWarnMessage() == null)
					atrForm.setAllWarnMessage("解析指标公式发生错误！");
				return result;
			}
			//计算指标值
			result = StrutsActuTargetResultDelegate.calTargetResult(atrForm);
		}catch(Exception e){
			result = false;
			log.printStackTrace(e);
		}
		return result;
	}
	
	/**
	 * 获得IF公式指标结果
	 * 
	 * @param atrForm
	 * @param mActuRepForm
	 * @param formula
	 * @return String 指标结果
	 */
	public static String getIfFormualValue(ActuTargetResultForm atrForm,MActuRepForm mActuRepForm,String formula){
		if(atrForm == null || mActuRepForm == null || formula == null)
			return null;
		
		String result = null;
		String aftFormula="";
		String tempstr="";
		String value = null;
		
		for(int i=0;i<formula.length();i++){
			char temp = formula.charAt(i);
			if(!CommMethod.IsMark(temp)){
				tempstr+=temp;
				if(i==(formula.length()-1)){
					//得到单元格和模板Id
					String[] tmpArr = tempstr.split("_");
					if(tmpArr.length == 1){//立即数时 
						if(tempstr.indexOf("%") > -1){
							tempstr = tempstr.substring(0,tempstr.indexOf("%"));
							tempstr = Float.parseFloat(tempstr)/100 + "";
						}
						aftFormula+=tempstr;
					}else{ //非立即数					  
						String childRepId = tmpArr[0];
						String cell = tmpArr[1];
						Integer dataRangeId = mActuRepForm.getDataRangeId();
//						if(tmpArr.length != 2) dataRangeId = new Integer(tmpArr[2]);
						
						String versionId = StrutsMRepRangeDelegate.getVerionId(childRepId,atrForm.getYear(),atrForm.getMonth());
						value = StrutsActuTargetResultDelegate.getCellValue(childRepId,versionId,cell,atrForm,"curr",dataRangeId);
						if(value == null){
							if(atrForm.getAllWarnMessage() == null){
								atrForm.setAllWarnMessage("IF语句中"+childRepId+"_"+cell+"单元格数据不存在！");
							}
							return result;
						}
						aftFormula+=value;
					}		
				}
			}else{
				if(!tempstr.equals("")){
					//得到单元格和模板Id						
					String[] tmpArr = tempstr.split("_");
					if(tmpArr.length == 1){//立即数时 
						if(tempstr.indexOf("%") > -1){
							tempstr = tempstr.substring(0,tempstr.indexOf("%"));
							tempstr = Float.parseFloat(tempstr)/100 + "";
						}
						aftFormula+=tempstr;
					}else{   //非立即数
						String childRepId = tmpArr[0];
						String cell = tmpArr[1];
						Integer dataRangeId = mActuRepForm.getDataRangeId();
//						if(tmpArr.length != 2) dataRangeId = new Integer(tmpArr[2]);
						
						String versionId = StrutsMRepRangeDelegate.getVerionId(childRepId,atrForm.getYear(),atrForm.getMonth());
						value = StrutsActuTargetResultDelegate.getCellValue(childRepId,versionId,cell,atrForm,"curr",dataRangeId);
						if(value == null){
							if(atrForm.getAllWarnMessage() == null){
								atrForm.setAllWarnMessage("IF语句中"+childRepId+"_"+cell+"单元格数据不存在！");
							}
							return result;
						}
						aftFormula+=value;
					}  
					tempstr="";
				}
				aftFormula+=temp;
			}
		}
		
		if(aftFormula!=null && !aftFormula.equals("")){		
			String sql = "select " + aftFormula + " from CALCULATE_TABLE";
			try{
				result = executeSQL(sql);
			}catch (Exception e){
				result = null;
				atrForm.setAllWarnMessage("解析IF语句发生异常！");
				log.printStackTrace(e);
			}
		}	
		return result;
	}
	
	/**
	 * 按单一口径解析平均余额公式计算公式
	 * 
	 * @param atrForm 指标结果FormBean
	 * @param mActuRepForm 报送频度
	 * @param formula 平均余额公式
	 * @return String 解析后的计算公式
	 */
	public static String getformualAVGValue(ActuTargetResultForm atrForm,MActuRepForm mActuRepForm,String formula){
		String result = null;
		String aftFormula = "";
		String tempstr = "";
		String type = null;
		String value = null;
		Map versionMap = new HashMap();
		
		/**算当前期数据的公式值*/
		type = "curr";
		for(int i=0;i<formula.length();i++){
			char temp = formula.charAt(i);
			if(!CommMethod.IsMark(temp)){
				tempstr+=temp;
				if(i==(formula.length()-1)){
					//得到单元格和模板Id
					String[] tmpArr = tempstr.split("_");
					if(tmpArr.length == 1){//立即数时 
						if(tempstr.indexOf("%") > -1){
							tempstr = tempstr.substring(0,tempstr.indexOf("%"));
							tempstr = Float.parseFloat(tempstr)/100 + "";
						}
						aftFormula+=tempstr;
					}else{ //非立即数(包含口径，特殊处理)
						String childRepId = tmpArr[0];
						String cell = tmpArr[1];
						Integer dataRangeId = mActuRepForm.getDataRangeId();
//						if(tmpArr.length != 2) dataRangeId = new Integer(tmpArr[2]);
						
						String versionId = null;
						if(versionMap.containsKey(childRepId)){
							versionId = (String)versionMap.get(childRepId);
						}else{
							versionId = StrutsMRepRangeDelegate.getVerionId(childRepId,atrForm.getYear(),atrForm.getMonth());
							versionMap.put(childRepId,versionId);
						}
						value = StrutsActuTargetResultDelegate.getCellValue(childRepId,versionId,cell,atrForm,type,dataRangeId);
						if(value == null){
							if(atrForm.getAllWarnMessage() == null){
								atrForm.setAllWarnMessage("本期"+childRepId+"_"+cell+"单元格数据不存在！");
							}
							return result;
						}
						aftFormula+=value;
					}
				}
			}else{
				if(!tempstr.equals("")){
					//得到单元格和模板Id						
					String[] tmpArr = tempstr.split("_");
					if(tmpArr.length == 1){  //立即数时
						if(tempstr.indexOf("%") > -1){
							tempstr = tempstr.substring(0,tempstr.indexOf("%"));
							tempstr = Float.parseFloat(tempstr)/100 + "";
						}
						aftFormula+=tempstr;
					}else{  //非立即数(若包含口径，特殊处理)				  
						String childRepId = tmpArr[0];
						String cell = tmpArr[1];
						String versionId = null;
						Integer dataRangeId = mActuRepForm.getDataRangeId();
//						if(tmpArr.length != 2) dataRangeId = new Integer(tmpArr[2]);
						
						if(versionMap.containsKey(childRepId)){
							versionId = (String)versionMap.get(childRepId);
						}else{
							versionId = StrutsMRepRangeDelegate.getVerionId(childRepId,atrForm.getYear(),atrForm.getMonth());
							versionMap.put(childRepId,versionId);
						}
						value = StrutsActuTargetResultDelegate.getCellValue(childRepId,versionId,cell,atrForm,type,dataRangeId);
						if(value == null){
							if(atrForm.getAllWarnMessage() == null){
								atrForm.setAllWarnMessage("本期"+childRepId+"_"+cell+"单元格数据不存在！");
							}
							return result;
						}
						aftFormula+=value;
					}  
					tempstr="";
				}
				aftFormula+=temp;
			}
		}
		aftFormula=aftFormula+"+";
		tempstr="";
		
		/**平均余额求去年末的值*/
		type = "pre";
		for(int i=0;i<formula.length();i++){
			char temp = formula.charAt(i);
			if(!CommMethod.IsMark(temp)){
				tempstr+=temp;
				if(i==(formula.length()-1)){
					//得到单元格和模板Id
					String[] tmpArr = tempstr.split("_");
					if(tmpArr.length == 1){//立即数时  
						if(tempstr.indexOf("%") > -1){
							tempstr = tempstr.substring(0,tempstr.indexOf("%"));
							tempstr = Float.parseFloat(tempstr)/100 + "";
						}
						aftFormula+=tempstr;
					}else{   //非立即数					  
						String childRepId = tmpArr[0];
						String cell = tmpArr[1];
						Integer dataRangeId = mActuRepForm.getDataRangeId();
//						if(tmpArr.length != 2) dataRangeId = new Integer(tmpArr[2]);
						
						//查找去年末的版本号
						String preVersionId = StrutsMRepRangeDelegate.getVerionId(childRepId,new Integer(atrForm.getYear().intValue()-1),new Integer(12));
						/**查看去年末版本与当前版本是否一致，若不一致，继续查找去年末版本对应的单元格*/
						if(!preVersionId.equals((String)versionMap.get(childRepId))){
							String preCell = StrutsActuTargetResultDelegate.getPreCellName(childRepId,(String)versionMap.get(childRepId),cell,childRepId,preVersionId);
							if(preCell != null){
								if(preCell.equals("NO")) //去本末不存在此单元格
									value = "0";
								else
									value = StrutsActuTargetResultDelegate.getCellValue(childRepId,preVersionId,preCell,atrForm,type,dataRangeId);
							} 
						}else
							value = StrutsActuTargetResultDelegate.getCellValue(childRepId,preVersionId,cell,atrForm,type,dataRangeId);
						if(value == null){
//							if(atrForm.getAllWarnMessage() == null){
//								atrForm.setAllWarnMessage("去年末"+childRepId+"_"+cell+"单元格数据不存在！");
//							}
//							return result;
							value = "0";  //若去年末不存在，则补零
						}
						aftFormula+=value;	  
					}		
				}
			}else{
				if(!tempstr.equals("")){
					//得到单元格和模板Id						
					String[] tmpArr = tempstr.split("_");
					if(tmpArr.length == 1){  //立即数时
						if(tempstr.indexOf("%") > -1){
							tempstr = tempstr.substring(0,tempstr.indexOf("%"));
							tempstr = Float.parseFloat(tempstr)/100 + "";
						}
						aftFormula+=tempstr;
					}else{ //非立即数					  
						String childRepId = tmpArr[0];
						String cell = tmpArr[1];
						Integer dataRangeId = mActuRepForm.getDataRangeId();
//						if(tmpArr.length != 2) dataRangeId = new Integer(tmpArr[2]);
						
						//查找去年末的版本号
						String preVersionId = StrutsMRepRangeDelegate.getVerionId(childRepId,new Integer(atrForm.getYear().intValue()-1),new Integer(12));
						/**查看去年末版本与当前版本是否一致，若不一致，继续查找去年末版本对应的单元格*/
						if(!preVersionId.equals((String)versionMap.get(childRepId))){
							String preCell = StrutsActuTargetResultDelegate.getPreCellName(childRepId,(String)versionMap.get(childRepId),cell,childRepId,preVersionId);
							if(preCell != null){
								if(preCell.equals("NO")) //去本末不存在此单元格
									value = "0";
								else
									value = StrutsActuTargetResultDelegate.getCellValue(childRepId,preVersionId,preCell,atrForm,type,dataRangeId);
							}
						}else
							value = StrutsActuTargetResultDelegate.getCellValue(childRepId,preVersionId,cell,atrForm,type,dataRangeId);
						if(value == null){
//							if(atrForm.getAllWarnMessage() == null){
//								atrForm.setAllWarnMessage("去年末"+childRepId+"_"+cell+"单元格数据不存在！");
//							}
//							return result;
							value = "0";  //若去年末不存在，则补零
						}
						aftFormula+=value;
					}				  				  				  
					tempstr="";
				}
				aftFormula+=temp;
			}
		}
		aftFormula="(("+aftFormula+")/2)";
		return aftFormula;
	}

	/***
	 * 平均余额获得报表单元格数值
	 * 
	 * @param childRepId 报表ID
	 * @param versionId 版本号
	 * @param cell 单元格
	 * @param atrForm 
	 * @param type 去年末/当期
	 * @param dataRangeId 报送口径
	 * @return 单元格值
	 */
	public static String getCellValue(String childRepId,String versionId,String cell,ActuTargetResultForm atrForm,String type,Integer dataRangeId){
		if(childRepId == null || cell == null || atrForm == null || dataRangeId == null)
			return null;
		
		String result = null;
		DBConn conn = null;
		Session session = null;
		Connection connection = null;
    	try{
    		conn = new DBConn();
            session = conn.beginTransaction();
            connection = session.connection();
            String value = null;
            
            int row=0;
            try{
            	//如果格式异常，直接返回默认值
            	row = Integer.parseInt(cell.substring(1,cell.length()));
            }catch(NumberFormatException ex){
            	atrForm.setAllWarnMessage("获取"+childRepId+"_"+cell+"单元格数据失败！");
            	log.printStackTrace(ex);
            	return result;
            }
		    int dataMove = FitechEXCELReport.getOffsetRows(childRepId, versionId);
		    int rowId = row-dataMove;
            String whereSql="select REPORT_VALUE from REPORT_IN_INFO " +
            		"where CELL_ID=(Select CELL_ID from M_CELL " +
            		"where CHILD_REP_ID=? and VERSION_ID=? and ROW_ID=? and COL_ID=?) and REP_IN_ID=(Select REP_IN_ID from REPORT_IN " +
            		"where CHILD_REP_ID=? and VERSION_ID=? and org_id=? and CUR_ID=1 and DATA_RANGE_ID=? and YEAR=? and TERM=? and CHECK_FLAG=? and TIMES>0) ";
           
            if("curr".equals(type)){
	            java.sql.PreparedStatement ps = connection.prepareStatement(whereSql);
	            ps.setString(1,childRepId);
	            ps.setString(2,versionId);
	            ps.setInt(3,rowId);
	            ps.setString(4,String.valueOf(cell.charAt(0)));
	            ps.setString(5,childRepId);
	            ps.setString(6,versionId);
	            ps.setString(7,atrForm.getOrgId());
	            ps.setInt(8,dataRangeId.intValue());
	            ps.setInt(9,atrForm.getYear().intValue());
	            ps.setInt(10,atrForm.getMonth().intValue());
	            ps.setInt(11,Config.CHECK_FLAG_PASS.intValue());
	            ResultSet rs=ps.executeQuery();
	
	            while(rs.next()){
	            	value=rs.getString(1);
	            }	            
            }else{
	            java.sql.PreparedStatement ps2 = connection.prepareStatement(whereSql);
	            //去年末的单元格数据
	            ps2.setString(1,childRepId);
	            ps2.setString(2,versionId);
	            ps2.setInt(3,rowId);
	            ps2.setString(4,String.valueOf(cell.charAt(0)));
	            ps2.setString(5,childRepId);
	            ps2.setString(6,versionId);
	            ps2.setString(7,atrForm.getOrgId());
	            ps2.setInt(8,dataRangeId.intValue());
	            ps2.setInt(9,atrForm.getYear().intValue()-1);
	            ps2.setInt(10,12);
	            ps2.setInt(11,Config.CHECK_FLAG_PASS.intValue());
	            ResultSet rs2=ps2.executeQuery();
	            
	            while(rs2.next()){
	            	value = rs2.getString(1);
	            }
            }
  
            /**判断是否找到相应单元格数据*/
            if(value != null){
            	value +="";
            	/*科学计数法数据处理*/
            	if(value.indexOf('E')>-1 || value.indexOf('e')>-1){
            		try{
            			//如果是数字格式将其格式化为小数点后两位
            			value = new DecimalFormat("##0.00").format(Double.parseDouble(value)).toString();
            		}catch(Exception e){
            			atrForm.setAllWarnMessage("处理"+childRepId+"_"+cell+"单元格科学计数法失败！");
            			log.printStackTrace(e);
            			return value;
            		}
            	}
            	result = value;
            }else{
            	if("curr".equals(type))
            		atrForm.setAllWarnMessage("本期"+childRepId+"_"+cell+"单元格数据不存在！");
            	else
            		atrForm.setAllWarnMessage("去年末"+childRepId+"_"+cell+"单元格数据不存在！");
            }         
    	}catch(Exception e){
    		result = null;
    		log.printStackTrace(e);    		
    	}finally{
    		try {
    			if(conn!=null) conn.closeSession();
    			if(session!=null)session.close();
    			if(connection!=null)connection.close();
			} catch (Exception e) {
				log.printStackTrace(e);
			}			    	
    	}
    	return result;	
	}

	/**
	 * 计算指标值
	 * 
	 * @param atrForm
	 * @author jcm
	 * @return boolean true-计算成功 false-计算失败
	 */
	public static boolean calTargetResult(ActuTargetResultForm atrForm){
		if(atrForm == null) return false;
		
		boolean result=false;		
		Map repInIdMap = null;
		String repInId = null;
		String childRepId = null;
		String versionId = null;
		String cellData = null;
		
		try{
			List list = atrForm.getCellList();
			if(atrForm.getCellList() != null && list.size() > 0){
				repInIdMap = new HashMap();
				for(int i=0;i<list.size();i++){
					FormulaForm formulaForm = (FormulaForm)list.get(i);
					childRepId = formulaForm.getReportId();
					//非立即数
					if(!Config.IMMEDIATE_DATA.equals(childRepId)){
						//查看是否已经访问过该表
						boolean signVisited=false;
						if(repInIdMap.containsKey(childRepId)){
							FormulaForm formualTemp = (FormulaForm)repInIdMap.get(childRepId);
							
							formulaForm.setVersionId(formualTemp.getVersionId());
							formulaForm.setRepInId(repInId);
							repInId = formualTemp.getRepInId();							
							signVisited=true;
						}
						//第一次访问
						if(!signVisited){
							versionId = StrutsMRepRangeDelegate.getVerionId(childRepId,atrForm.getYear(),atrForm.getMonth());
							formulaForm.setVersionId(versionId);
							
							repInId = StrutsActuTargetResultDelegate.getRelatedReportInId(atrForm,childRepId,versionId);
							if(repInId == null){
								atrForm.setAllWarnMessage("缺少"+childRepId+"_"+versionId+"的报表数据！");
								return result;
							}
							formulaForm.setRepInId(repInId);
							repInIdMap.put(childRepId,formulaForm);
						}
				
						//如果未找到单元格的值，则退出计算 
						cellData = StrutsActuTargetResultDelegate.getCellData(atrForm,formulaForm,repInId);
						if(cellData == null){
							atrForm.setAllWarnMessage("缺少"+childRepId+"_"+formulaForm.getCell()+"单元格数据！");
							return result;
						}
						
						formulaForm.setCellData(cellData);
					}else //立即数
						formulaForm.setCellData(formulaForm.getCell());
					
					repInId = null;
				}
			}
			/**计算解析后的指标公式*/
			atrForm.setAfFormula(StrutsActuTargetResultDelegate.parseAfterFormula(atrForm.getCellList(),atrForm.getAfFormula()));
			StrutsActuTargetResultDelegate.calcute(atrForm);	   
			if(atrForm.getTargetResult() == null){
				if(atrForm.getAllWarnMessage() == null)
					atrForm.setAllWarnMessage("解析指标公式出错！");
				
				return result;
			}
			result = StrutsActuTargetResultDelegate.saveData(atrForm);
		}catch(Exception ex){
			result = false;
			atrForm.setAllWarnMessage("计算指标值发生异常");
			log.printStackTrace(ex);
		}
		return result;
	}
	
	/**
	 * 查找已报送并审核通过的报表记录
	 * 
	 * @param atrForm
	 * @param childRepId 报表编号
	 * @param versionId 版本号
	 * @return 报表记录ID
	 */
	public static String getRelatedReportInId(ActuTargetResultForm atrForm,String childRepId,String versionId){
		if(atrForm == null || childRepId == null || versionId == null) 
			return null;
		
		DBConn conn=null;
		Session session = null;
		String repInIds = null;
		try{
			conn=new DBConn();
			session=conn.openSession();				 
			StringBuffer where =new StringBuffer("from ReportIn Report where Report.orgId='" + atrForm.getOrgId() + "' and Report.term=" + atrForm.getMonth() +
							   			" and Report.year=" + atrForm.getYear() +" and Report.MChildReport.comp_id.childRepId='" + childRepId + "'" +
							   			" and Report.MChildReport.comp_id.versionId='" + versionId + "' and Report.MDataRgType.dataRangeId=" + atrForm.getDataRangeId() + 
							   			" and Report.checkFlag="+Config.CHECK_FLAG_PASS+" and Report.times=(select max(r.times) from ReportIn r" +
							   			" where r.MChildReport.comp_id.childRepId=Report.MChildReport.comp_id.childRepId" +
							   			" and r.MChildReport.comp_id.versionId=Report.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=Report.MDataRgType.dataRangeId" +  
							   			" and r.orgId=Report.orgId and r.MCurr.curId=Report.MCurr.curId and r.year=Report.year and r.term=Report.term)");			   
			List resList=session.find(where.toString());
			
			if(resList!=null && resList.size()>0){
				ReportIn report = null;
				/**若是多币种，将多币的报表记录都取出，多币种单元格值相加*/
				if(resList.size()>1){
					for(int i=0;i<resList.size();i++){
						report = (ReportIn)resList.get(i);
						repInIds = (repInIds==null ? report.getRepInId()+"" : repInIds + "," + report.getRepInId());						
					}
				}else{
					report=(ReportIn)resList.get(0);
					repInIds = report.getRepInId().toString();
				}
			}
		}catch(Exception e){
			repInIds = null;
			log.printStackTrace(e);
		}finally{
			try{
				if(conn!=null) conn.closeSession();
				if(session!=null) session.close();
			}catch(Exception ex){
				log.printStackTrace(ex);
			}
		}
		return repInIds;
	}
	
	
	/**
	 * 获取单元格数据
	 * 
	 * @param atrForm
	 * @param formulaForm
	 * @param repInId
	 * @return
	 */
	public static String getCellData(ActuTargetResultForm atrForm,FormulaForm formulaForm,String repInId){
		if(atrForm == null || formulaForm == null || formulaForm.getReportId() == null 
				|| formulaForm.getVersionId() == null)
			return null;
		
		String result=null;
		DBConn conn=null;
		Session session=null;
		List list = null;
		try{
			conn=new DBConn();
			session=conn.openSession();
		
			int row = Integer.parseInt(formulaForm.getCell().substring(1,formulaForm.getCell().length()));
		    int dataMove = FitechEXCELReport.getOffsetRows(formulaForm.getReportId(),formulaForm.getVersionId());
		    int rowId = row - dataMove;
		    
		    //暂时仅考虑点对点式
			Integer type = com.cbrc.smis.common.Config.REPORT_STYLE_DD;
			if(type.intValue()==com.cbrc.smis.common.Config.REPORT_STYLE_DD.intValue()){//点对点
				String sq="from ReportInInfo info  where info.comp_id.repInId in(" + repInId + ")"
		                 	+" and info.comp_id.cellId in( select M.cellId from MCell M where M.MChildReport.comp_id.childRepId='"+formulaForm.getReportId()+"'"
		                 	+" and M.MChildReport.comp_id.versionId='"+formulaForm.getVersionId()+"'" +"  and M.rowId="+rowId
		                 	+" and M.colId='"+formulaForm.getCell().charAt(0)+"')";				
			
				list=session.find(sq);		
		
				if(list!=null && list.size()>0){
					String value = null;
					//多币种-将各币种单元格数值相加
					for(int i=0;i<list.size();i++){
						value = ((ReportInInfo)list.get(i)).getReportValue();
						if(value == null || value.equals("")) value = "0";
						result = (result == null) ? "("+value : result + "+" + value;
					}	
					result += ")";
				}
			}else{//清单
			}		  		 
		}catch(Exception e){
			e.printStackTrace();
			log.printStackTrace(e);
		}finally{
			try{
				if(conn!=null) conn.closeSession();
				if(session!=null) session.close();
			}catch(Exception ex){
				log.printStackTrace(ex);
			}
		}
		return result;
	}
   
	/**
	 * 把AfFormula代入公式计算类计算
	 * 结果放在targetresult里
	 * 
	 * @param atrForm
	 */
	public static void calcute(ActuTargetResultForm atrForm){
		Float resultValue = null;
		float resultTemp = 0;
		
		Formula formula = new Formula(atrForm.getAfFormula());
		if(formula.checkValid()){
			resultTemp=(float)formula.getResult();
		
			String s = atrForm.getAfFormula();	
			boolean flag=false; 
			for(int i=1;i<s.length() && flag!=true;i++){		
				if((s.substring(i-1,i).equals("+") || s.substring(i-1,i).equals("`") ||s.substring(i-1,i).equals("*") || s.substring(i-1,i).equals("/")  )){
					//如果是几个单元格的值相加就乘100
					resultValue = new Float(resultTemp*100);
					flag=true;
					
				}else if(s!=null && !s.substring(i-1,i).equals("+") && !s.substring(i-1,i).equals("`") && !s.substring(i-1,i).equals("*") && !s.substring(i-1,i).equals("/")){				
					//如果是某个单元格的值就直接等于这个值
					resultValue = new Float(resultTemp);
				}
			}
		
			//折年系数
			if(atrForm.getTemp1()!=null && atrForm.getTemp1().equals("Quotiety")){
				int month = atrForm.getMonth().intValue();
				resultValue = new Float(formatNumber(resultValue.floatValue()*12/month));
				
			}
			atrForm.setTargetResult(resultValue);
		}else{
			atrForm.setAllWarnMessage("解析后的公式不符合计算规范！");
		}
	}

	/**
	 * 把CellList里的值放到AfFormula里
	 * 即把AfFormula里的1和2索引用celllist里的单元格值代替
	 * 
	 * @param cellList
	 * @param formula
	 * @return String 
	 */
	public static String parseAfterFormula(List cellList,String formula){
		if(cellList == null || cellList.size() <= 0 || formula == null)
			return null;
		
		String aftFormula = "";
		String tempstr="";
		for(int i=0;i<formula.length();i++){
			char temp=formula.charAt(i);
			if(!CommMethod.IsMark(temp)){
				tempstr+=temp;
				if(i==(formula.length()-1)){
					int index=Integer.parseInt(tempstr.trim());
					aftFormula+=((FormulaForm)cellList.get(index)).getCellData().trim();
				}
			}else{
				if(!tempstr.equals("")){
					//得到单元格和模板Id
					int index=Integer.parseInt(tempstr.trim());
					aftFormula+=((FormulaForm)cellList.get(index)).getCellData().trim();
					tempstr="";
				}
				aftFormula+=temp;
			}
		}
		return aftFormula;
	}
	
	/**
	 * 解析公式G0100_A3+G0200_B3
	 * 得到的单元格保存在CellList
	 * 保存的解析后的公式为1+2 放在AFformula
	 * 1和2 为索引放在 celllist里的index里
	 * 
	 * @param actuTargetResultForm
	 * @return
	 */
	public static boolean parseFormula(ActuTargetResultForm atrForm,String formula){
		if(atrForm == null || formula == null || formula.length() <=0) 
			return false;
		
		boolean bool = false;
		List list = new ArrayList();
		int index = 0;
		String aftFormula = "";
		String tempstr = "";
		
		try{
			for(int i=0;i<formula.length();i++){
				char temp = formula.charAt(i);
				if(!CommMethod.IsMark(temp)){
					tempstr+=temp;
					if(i==(formula.length()-1)){
						//得到单元格和模板Id
						int ind=tempstr.indexOf("_");
						if(ind==-1){//立即数时  
							list.add(new FormulaForm(Config.IMMEDIATE_DATA,tempstr,index));  
						}else{   //非立即数					  
							String report=tempstr.substring(0,ind);
							String cell=tempstr.substring(ind+1,tempstr.length());				  
							list.add(new FormulaForm(report,cell,index));					  
						}		
						aftFormula+=index;
					}
				}else{
					if(!tempstr.equals("")){
						//得到单元格和模板Id						
						int ind=tempstr.indexOf("_");
						if(ind==-1){  //立即数时
							if(tempstr.indexOf("%") > -1){
								tempstr = tempstr.substring(0,tempstr.indexOf("%"));
								tempstr = Float.parseFloat(tempstr)/100 + "";
							}
							list.add(new FormulaForm(Config.IMMEDIATE_DATA,tempstr,index));
						}else{   //非立即数					  
							String report=tempstr.substring(0,ind);
							String cell=tempstr.substring(ind+1,tempstr.length());				  
							list.add(new FormulaForm(report,cell,index));
						}				  				  				  
						aftFormula+=index;
						index++;
						tempstr="";
					}
					aftFormula+=temp;
				}
			}
			atrForm.setCellList(list);
			atrForm.setAfFormula(aftFormula);
			bool = true;
		}catch(Exception ex){
			bool = false;
			atrForm.setAllWarnMessage("解析公式发生异常！");
			log.printStackTrace(ex);
		}
		return bool;
	}
  
	/**
	 * 保留小数点后两位小数,格式化数字
	 * @return String
	 */
	public static String formatNumber(float pFloat){
		return new DecimalFormat("##0.00").format(pFloat); 
	}

	/**
	 * 查询某机构同口径、币种所有月份的报表
	 * @param form
	 * @param offset
	 * @param limit
	 * @return
	 */
	public static List searchTargetResult2(ActuTargetResultForm form){
		if(form == null) return null;
		Integer year=form.getYear();
		Integer month=form.getMonth();
		String orgId=form.getOrgId();
		Integer freId=form.getRepFreId();
		Integer RangeId=form.getDataRangeId();
		List refval=null;
		DBConn conn=null;
		Session session=null;
		
		try{
			StringBuffer sql= new StringBuffer("from ActuTargetResult result where result.targetDefine.targetDefineId="+form.getTargetDefineId());
			if(year!=null && year.intValue()!=0)
				sql.append("  and result.year="+year);
			if(month!=null && month.intValue()!=0)
				sql.append("  and result.month="+month);
			if(orgId!=null)
				sql.append("  and result.orgId='"+orgId+"'");
			if(freId!=null)
				sql.append("  and result.repFreId="+freId);
			if(RangeId!=null)
				sql.append("  and result.dataRangeId"+RangeId);
          
			conn=new DBConn();
			session=conn.openSession();
			Query query=session.createQuery(sql.toString());
			List list=query.list();
			if(list!=null && list.size()>0){
				refval=new ArrayList();
				for(Iterator it=list.iterator();it.hasNext();){
					ActuTargetResultForm  resultForm=new ActuTargetResultForm();
					ActuTargetResult result=(ActuTargetResult)it.next();
					TranslatorUtil.copyPersistenceToVo(result,resultForm);           		
					refval.add(resultForm);
				}
			}
		}catch(Exception e){	
			e.printStackTrace();
			log.printStackTrace(e);		
		}finally{
			try{
				if(conn!=null) conn.closeSession();
				if(session!=null) session.close();
			}catch(Exception ex){
				log.printStackTrace(ex);
			}
		}
		return refval;
	}
  
	/**
	 * 查询某机构同口径、币种所有月份的报表
	 * @param form
	 * @param offset
	 * @param limit
	 * @return
	 */
	public static HashMap searchTargetResult(ActuTargetResultForm form,String years,int startMonth,int endMonth){ 
		if(form == null) return null;
		Integer year=form.getYear();
		String orgId=form.getOrgId();
		String[] yearArr=years.split(",");
		String ys=years.substring(0, years.length()-1);
     
		if(startMonth>endMonth){
			int maxMonth=(endMonth>startMonth) ? endMonth : startMonth;
			int minMonth=(endMonth>startMonth) ? startMonth : endMonth;
			endMonth=maxMonth;
			startMonth=minMonth;	  
		}
		String  freStr="";
		Integer freId=form.getRepFreId();
		if(freId.intValue()==2){
			for(int i=startMonth;i<=endMonth;i++){
				if(i%3==0){
					freStr+=i+",";
				}
			}
		}
		if(freStr.length()>=1)
			freStr= (String) freStr.trim().subSequence(0,freStr.length()-1);
		Integer RangeId=form.getDataRangeId();
		HashMap refval=null;
		DBConn conn=null;
		Session session=null;
		
		try{
			StringBuffer sql= new StringBuffer("from ActuTargetResult result where result.targetDefine.targetDefineId="+form.getTargetDefineId());
			if(year!=null ) 
				sql.append(" and result.year in("+ys+")");
			if(startMonth!=0 && endMonth!=0){
				if(freId.intValue()==2){
					sql.append(" and result.month in("+freStr+")");
				}else{
					sql.append(" and result.month between "+startMonth+" and "+endMonth);	        	  
				}
			}
			if(orgId!=null)
				sql.append(" and result.orgId='"+orgId+"'");
			if(freId!=null)
				sql.append(" and result.repFreId="+freId);
			if(RangeId!=null)
				sql.append(" and result.dataRangeId="+RangeId);

			conn=new DBConn();
			session=conn.openSession();
			Query query=session.createQuery(sql.toString());
			List list=query.list();
			
			List listArr[]=new ArrayList[yearArr.length];
			for(int i=0;i<yearArr.length;i++){
				listArr[i]=new ArrayList();
			}
			if(list!=null && list.size()>0){
				refval=new HashMap();
				for(Iterator it=list.iterator();it.hasNext();){
					ActuTargetResultForm  resultForm=new ActuTargetResultForm();
					ActuTargetResult result=(ActuTargetResult)it.next();
					TranslatorUtil.copyPersistenceToVo(result,resultForm);  
					refval.put(resultForm.getYear().toString()+resultForm.getMonth().toString(),resultForm);
				}
			}        
		}catch(Exception e){	
			e.printStackTrace();
			log.printStackTrace(e);		
		}finally{
			try{
				if(conn!=null) conn.closeSession();
				if(session!=null) session.close();
			}catch(Exception ex){
				log.printStackTrace(ex);
			}
		}
		return refval;	
	}

	/**
	 * 取所有年份
	 * @return
	 */
	public static List searchAllYear(String rangeId,String repFreId,String dataRangeId){		 
		List allYear=null;
		Integer rId=null;
		Integer rfId=null;
		Integer drId=null;
		
		if(rangeId!=null) rId= new Integer(rangeId);
		if(rangeId!=null) rfId= new Integer(repFreId);
		if(rangeId!=null) drId= new Integer(dataRangeId);
	  
		DBConn conn=null;
		Session session=null;
		try{
			StringBuffer sql= new StringBuffer("select distinct result.year from ActuTargetResult result where result.targetDefine.targetDefineId="
					+rId+" and result.repFreId="+rfId+" and result.dataRangeId="+drId);
		 
			if(rfId.intValue()==2) sql.append(" and result.month in(3,6,9,12)");
		  
			conn=new DBConn ();
			session=conn.openSession();
			Query query=session.createQuery(sql.toString());
			allYear=query.list();         
		}catch(Exception e){
			e.printStackTrace();
			log.printStackTrace(e);	
		}finally{
			try{
				if(conn!=null) conn.closeSession();
				if(session!=null) session.close();
			}catch(Exception ex){
				log.printStackTrace(ex);
			}
		}	
		return allYear;
	}
	
	/**
	 * 删除
	 */
	public static boolean remove(TargetDefineForm form){
		//置标志result		   
		boolean result=false;
		//连接和会话对象的初始化		   
		DBConn conn=null;		
		Session session=null;
		
		if (form == null || form.getTargetDefineId() == null) 
			return result;
				
		try{	
			String hql = "from ActuTargetResult atr where atr.targetDefine.targetDefineId=" + form.getTargetDefineId();
			//连接对象和会话对象初始化			
			conn=new DBConn();			
			session=conn.beginTransaction();
			List list = session.createQuery(hql).list();
			
			if(list != null && list.size() > 0){
				for(int i=0;i<list.size();i++){
					ActuTargetResult actuTargetResult = (ActuTargetResult)list.get(i);
					session.delete(actuTargetResult);
				}
			}
			session.flush();			
			//删除成功，置为true			
			result=true;
			
		}catch(HibernateException he){		
			//捕捉本类的异常,抛出	
			result = false;
			log.printStackTrace(he);		   
		}finally{			   
			//如果由连接则断开连接，结束会话，返回		
			try{
				if(conn!=null) conn.closeSession();
				if(session!=null) session.close();
			}catch(Exception ex){
				log.printStackTrace(ex);
			}		  
		}		
		return result;		
	}
	
	/**
	 * 执行SQL语句返回它的执行结果
	 * 
	 * @param sql
	 * @return String
	 */
	public static String executeSQL(String sql) throws Exception{
		if(sql == null || sql.equals(""))
			return null;
		
		String result = null;
		DBConn conn=null;
		Session session=null;
		Connection connection=null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			conn = new DBConn();
	        session = conn.beginTransaction();
	        connection=session.connection();
			stmt = connection.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (!rs.next())
				result = null;
			else{
				result = rs.getString(1);
				if(result!=null&& result.length()<20){
					try{
						/*科学计数法数据处理*/
						if(result.indexOf('E')>-1||result.indexOf('e')>-1){
							try{
								//如果是数字格式将其格式化为小数点后两位
								result = new java.text.DecimalFormat("##0.00").format(Double.parseDouble(result)).toString();
				    		}catch(Exception e){
				    			result = rs.getString(1);
				    			log.printStackTrace(e);
				    		}
						}
					}catch(Exception ex){
						result = rs.getString(1);
						log.printStackTrace(ex);
					}					
				}
			}		
		}catch (Exception e){
			result = null;
			log.printStackTrace(e);
		}finally{
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
			if(connection != null) connection.close();
			if(session != null) session.close();
			if(conn != null) conn.closeSession();			
		}
		return result;
	}
	
	/**
	 * 解析IF公式
	 * 
	 * @param atrForm
	 * @param mActuRepForm
	 * @param formula
	 * @return String IF公式解析后的运算结果
	 */
	public static String checkIF(ActuTargetResultForm atrForm,MActuRepForm mActuRepForm,String formula){
		if(atrForm == null || mActuRepForm == null || formula == null)
			return null;
		
		String result = null;
		StringBuffer buffer = new StringBuffer(formula);

		int index1 = formula.indexOf(",");
		int index2 = formula.indexOf(",",index1+1);
		int start = (formula.indexOf("if"))>0 ? formula.indexOf("if") : formula.indexOf("IF");
		String ask=buffer.substring(start+2,index1); 
		String answer1=buffer.substring(index1+1,index2);
		String answer2=buffer.substring(index2+1,buffer.indexOf("}")-1);
		
		/**
		 * 判断IF语句的第一个公式是否成立
		 * 若成立直接取第一个公式结果
		 * 若不立判断第二个公式结果
		 */
		String value = StrutsActuTargetResultDelegate.getIfFormualValue(atrForm,mActuRepForm,answer1);
		if(value == null){
			if(atrForm.getAllWarnMessage() == null)
				atrForm.setAllWarnMessage("IF语句解析第一部分失败！");
			return result;
		}
		if(!mark(ask,Double.parseDouble(value))){
			value = StrutsActuTargetResultDelegate.getIfFormualValue(atrForm,mActuRepForm,answer2);
			if(value == null){
				if(atrForm.getAllWarnMessage() == null)
					atrForm.setAllWarnMessage("IF语句解析第二部分失败！");
				return result;
			}			
		}
		result = value;
		
		try{
			buffer.delete(formula.indexOf("{"),formula.indexOf("}")+1);
			buffer.insert(formula.indexOf("{"),result);
			result = buffer.toString();
		}catch(StringIndexOutOfBoundsException e){
			result = null;
			atrForm.setAllWarnMessage("解析IF语句发生异常！");
			log.printStackTrace(e);
			return result;
		}
		return result;
	}
	
	public static int math(String check){
		String[] pase={">","<","!=","="};
		int result=-1;
		for(int i=0;i<pase.length;i++){
			result=check.indexOf(pase[i]);
			if(result!=-1)
				break;
		}
		return result;
		
	}
	public static boolean mark(String check,double test){
		String[] pase={">","<","!=","="};
		for(int i=0;i<pase.length;i++){
			if(check.indexOf(pase[i])!=-1){
				if(pase[i].equals(">"))
					return test>0;
				else if(pase[i].equals("<"))
					return test<0;
				else if(pase[i].equals("!="))
					return test!=0;
				else if(pase[i].equals("="))
					return test==0;
			}
		}
		return false;
	}
    
	/**
	 * 
	 * @param year
	 * @param month
	 * @param repFrequence
	 * @param calculateFlag   2:比上期，3：比上年同期 4：比上年年末
	 * @return
	 */
	public static String[] calculateDate(String year,String month,String repFrequence,String calculateFlag){
		Date resultDate=null;
		try{
			if(calculateFlag.equals(Config.Target_Pre_Standard)){   //如果是比上期
				Date firstDate=DateUtil.getDateByString(year+"-"+month, "yyyy-MM");
				int monthNumber=1;  //频率(frequence) 1,月 2，季，3，半年 4，年,默认为月报
				if(repFrequence.equals("2")){
					//季报为3个月
					monthNumber=3;
				}else if(repFrequence.equals("3")){
					//半年为6个月
					monthNumber=6;
				}else if(repFrequence.equals("4")){
					//年为12个月
					monthNumber=12;
				}
				resultDate=DateUtil.reduceMonth(firstDate, monthNumber);
			}else if(calculateFlag.equals(Config.Target_Last_Year_Same_Month_Standard)){  //如果是比上年同期
				year=""+(new Integer(year).intValue()-1);
				resultDate=DateUtil.getDateByString(year+"-"+month, "yyyy-MM");
			}else if(calculateFlag.equals(Config.Target_Last_Year_December_Standard)){   //如果是比上年年末
				year=""+(new Integer(year).intValue()-1);
				resultDate=DateUtil.getDateByString(year+"-"+"12", "yyyy-MM");
			}
			String simpleDateFormat="yyyy-MM";
			String resultDateStr=DateUtil.toSimpleDateFormat(resultDate,simpleDateFormat);
			String[] yearAndMonth={resultDateStr.substring(0,4),resultDateStr.substring(5,7)};
			return yearAndMonth;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据报表ID、版本号追述旧版本报表对应的单元格
	 * 
	 * @param childRepId 当前报表ID
	 * @param versionId 当前报表版本号
	 * @param cell 当前单元格名称
	 * @param preChildRepId 旧报表ID
	 * @param preVersionId 旧报表版本号
	 * @return String 旧报表的对应单元格
	 */
	public static String getPreCellName(String childRepId,String versionId,String cell,String preChildRepId,String preVersionId){
		if(childRepId == null || versionId == null || cell == null 
				|| preChildRepId == null || preVersionId == null)
			return cell;
		
		String preCell = cell;
		DBConn conn = null;
		Session session = null;
		Connection connection = null;
		Statement stmt = null;
    	try{
    		conn = new DBConn();
            session = conn.beginTransaction();
            connection = session.connection();
                        
            String sql="select PRE_CELL_NAME from CELL_CONTRAST where CHILD_REP_ID='" + childRepId + 
            		   "' and VERSION_ID='" + versionId + "' and CELL_NAME='" + cell + 
            		   "' and PRE_CHILD_REP_ID='" + childRepId + "' and PRE_VERSION_ID='" + preVersionId + "'";
           
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs != null && rs.next()){
            	preCell = rs.getString(1);
            }
    	}catch(Exception e){
    		preCell = cell;
    		log.printStackTrace(e); 		
    	}finally{
    		try{
    			if(stmt != null) stmt.close();
    			if(connection != null) connection.close();
    			if(session != null) session.close();
    			if(conn != null) conn.closeSession();
    		}catch(Exception ex){
    			log.printStackTrace(ex);
    		}    		
    	}
    	return preCell;	
	}
	
	/**
	 * 取所有年份
	 * @return
	 */
	public static List searchAllYear(String rangeId,String repFreId,String dataRangeId,String orgId){		 
		List allYear=null;
		Integer rId=null;
		Integer rfId=null;
		Integer drId=null;
		
		if(rangeId!=null) rId= new Integer(rangeId);
		if(rangeId!=null) rfId= new Integer(repFreId);
		if(rangeId!=null) drId= new Integer(dataRangeId);
	  
		DBConn conn=null;
		try{
			StringBuffer sql= new StringBuffer("select  distinct result.year  from ActuTargetResult result where  result.orgId='"+orgId+"' and result.targetDefine.targetDefineId="
					+rId+"  and  result.repFreId="+rfId+" and result.dataRangeId="+drId);
		 
			if(rfId.intValue()==2) sql.append(" and  result.month in(3,6,9,12)");
		  
			conn=new DBConn ();
			Query query=conn.openSession().createQuery(sql.toString());
			allYear=query.list();         
		}catch(Exception e){
			e.printStackTrace();
			log.printStackTrace(e);	
		}finally{
			if(conn!=null)conn.closeSession();	
		}	
		return allYear;
	}
}