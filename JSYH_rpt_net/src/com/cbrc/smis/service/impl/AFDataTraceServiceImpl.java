package com.cbrc.smis.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.org.form.AFDataTraceForm;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.entity.AFDataTrace;
import com.cbrc.smis.hibernate.MCell;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.hibernate.ReportInInfo;
import com.cbrc.smis.service.IAFDataTraceService;
import com.fitech.net.adapter.StrutsReportInDelegate;
import org.apache.commons.beanutils.BeanUtils;

public class AFDataTraceServiceImpl implements IAFDataTraceService {
	private DBConn dbConn = null;
	private Session session = null;
	
	
	
	@Override
	public List<AFDataTraceForm> findListByAFDataTrace(
			AFDataTraceForm traceForm, int pageSize, int pageNo)
			throws Exception {
		open();
		List<AFDataTraceForm> formList = null;//查询结果集合
		List<AFDataTrace> traceList = null;
		String oriHQL = "from AFDataTrace a where 1=1 ";
		String whereSQL = "";
		if(traceForm!=null){//对象不为空
			if(traceForm.getUsername()!=null && !traceForm.getUsername().equals(""))//修改人
				whereSQL += " and a.username='"+traceForm.getUsername()+"'";
			if(traceForm.getRepInId()!=null && !traceForm.getRepInId().equals(""))//报表ID
				whereSQL += " and a.repInId='"+traceForm.getRepInId()+"'";
			if(traceForm.getBeginDate()!=null && !traceForm.getBeginDate().equals(""))//开始时间
				whereSQL += " and a.dateTime>='"+traceForm.getBeginDate()+"'";
			if(traceForm.getEndDate()!=null && !traceForm.getEndDate().equals(""))//结束时间
				whereSQL += " and a.dateTime<='"+traceForm.getEndDate()+"'";
			if(traceForm.getRepInIds()!=null && !traceForm.getRepInIds().equals(""))//报表ID
				whereSQL += " and a.repInId in "+traceForm.getRepInIds();
		}
		whereSQL += "order by a.dateTime";
		String hql = oriHQL + whereSQL;
		Query query = session.createQuery(hql);
		query.setFirstResult(pageNo);
		query.setMaxResults(pageSize);
		traceList = query.list();
		formList = new ArrayList<AFDataTraceForm>();
		for(AFDataTrace af : traceList){
			ReportIn reportIn = StrutsReportInDelegate.findById(Integer.valueOf(af.getRepInId()));
			Integer traceId;//主键

		    AFDataTraceForm tf = new AFDataTraceForm();
			tf.setCellName(af.getCellName());
			tf.setRepInId(af.getRepInId());
			tf.setUsername(af.getUsername());
			tf.setDateTime(af.getDateTime());
			tf.setOriginalData(af.getOriginalData());
			tf.setChangeData(af.getChangeData());
			tf.setFinalData(af.getFinalData());
			tf.setDescTrace(af.getDescTrace());
			tf.setStatus(af.getStatus());
			
			tf.setRepName(reportIn.getRepName());
			tf.setReportTerm(reportIn.getYear()+"-"+reportIn.getTerm());//年+月
			formList.add(tf);
		}
		return formList;//返回集合
	}

	
	@Override
	public List<AFDataTraceForm> findListByAFDataTrace(AFDataTraceForm traceForm)
			throws Exception {
		open();
		List<AFDataTraceForm> formList = null;//查询结果集合
		List<AFDataTrace> traceList = null;
		String oriHQL = "from AFDataTrace a where 1=1 ";
		String whereSQL = "";
		if(traceForm!=null){//对象不为空
			if(traceForm.getUsername()!=null && !traceForm.getUsername().equals(""))//修改人
				whereSQL += " and a.username='"+traceForm.getUsername()+"'";
			if(traceForm.getRepInId()!=null && !traceForm.getRepInId().equals(""))//报表ID
				whereSQL += " and a.repInId='"+traceForm.getRepInId()+"'";
			if(traceForm.getBeginDate()!=null && !traceForm.getBeginDate().equals(""))//开始时间
				whereSQL += " and a.dateTime>='"+traceForm.getBeginDate()+"'";
			if(traceForm.getEndDate()!=null && !traceForm.getEndDate().equals(""))//结束时间
				whereSQL += " and a.dateTime<='"+traceForm.getEndDate()+"'";
			if(traceForm.getRepInIds()!=null && !traceForm.getRepInIds().equals(""))//报表ID
				whereSQL += " and a.repInId in "+traceForm.getRepInIds();
		}
		whereSQL += "order by a.dateTime";
		String hql = oriHQL + whereSQL;
		Query query = session.createQuery(hql);
		
		traceList = query.list();
		formList = new ArrayList<AFDataTraceForm>();
		for(AFDataTrace af : traceList){
			ReportIn reportIn = StrutsReportInDelegate.findById(Integer.valueOf(af.getRepInId()));
			AFDataTraceForm tf = new AFDataTraceForm();
			tf.setCellName(af.getCellName());
			tf.setRepInId(af.getRepInId());
			tf.setUsername(af.getUsername());
			tf.setDateTime(af.getDateTime());
			tf.setOriginalData(af.getOriginalData());
			tf.setChangeData(af.getChangeData());
			tf.setFinalData(af.getFinalData());
			tf.setDescTrace(af.getDescTrace());
			tf.setStatus(af.getStatus());
			
			tf.setRepName(reportIn.getRepName());
			tf.setReportTerm(reportIn.getYear()+"-"+reportIn.getTerm());//年+月
			formList.add(tf);
		}
		return formList;//返回集合
	}


	/***
	 * 增加数据痕迹
	 */
	public void addAFDataTrace(AFDataTrace af) throws Exception {
		open();
		if(af!=null){
			dbConn.beginTransaction();
			session.save(af);
			dbConn.endTransaction(true);
		}
		//close();
	}

	public List<AFDataTraceForm> findListByTemplateIDandVersionId(
			String repInId, String cellName)
			throws Exception {
		List<AFDataTrace> traceList = null;
		List<AFDataTraceForm> traceFormList = new ArrayList<AFDataTraceForm>();
		if(repInId==null || repInId.equals("")||
		   cellName==null || cellName.equals(""))
			return traceFormList;
		String hql = "from AFDataTrace a where a.repInId='"
					+repInId+"'"+" and a.cellName='"
					+cellName+"'  order by a.traceId desc";
		
		String oriHql = "from AFDataTrace a where a.repInId='"
			+repInId+"'"+" and a.cellName='"
			+cellName+"'  order by a.traceId asc";
		open();
		Query query = session.createQuery(hql);
		traceList = query.list();//查询符合条件的集合
		
		if(traceList==null || traceList.size()==0)
			return traceFormList;
		
		String oriData = ((AFDataTrace)session.createQuery(oriHql).list().get(0)).getOriginalData();
		for(AFDataTrace af : traceList){
			AFDataTraceForm tf = new AFDataTraceForm();
			BeanUtils.copyProperties(tf, af);
//			String curDate = tf.getDateTime();
//			if(curDate!=null && !curDate.equals("")){//对日期做处理，取年月日
//				curDate = curDate.substring(0,10);
//				tf.setDateTime(curDate);
//			}
			tf.setOriData(oriData);
			traceFormList.add(tf);
		}
		close();//释放资源
		return traceFormList;
	}
	
	
	@Override
	public void updateAFDataTraceStatusById(Integer traceId,Integer status) throws Exception {
		if(traceId==null)
			return;
		AFDataTrace data = new AFDataTrace();
		data.setTraceId(traceId);
		open();
		dbConn.beginTransaction();
		try {
			data = (AFDataTrace)session.get(data.getClass(), traceId);
			data.setStatus(status);
			
		
			ReportIn reportIn = (ReportIn)session.get(ReportIn.class, Integer.valueOf(data.getRepInId()));
			
			/**更新报表单元格的值..........................开始
			//查询出单个单元格信息
			String hqlMCell = "from MCell m where m.MChildReport.comp_id.childRepId='"
							+reportIn.getMChildReport().getComp_id().getChildRepId()
							+"' and m.MChildReport.comp_id.versionId='"
							+reportIn.getMChildReport().getComp_id().getVersionId()+"' and m.cellName='"+data.getCellName()+"'";
			MCell mCell = (MCell)session.createQuery(hqlMCell).uniqueResult();
			
			//查询出单元格对应的值
			String hqlInInfo = "from ReportInInfo r where r.comp_id.cellId='"+mCell.getCellId()+"' and r.comp_id.repInId='"+data.getRepInId()+"'";
			ReportInInfo reportInInfo = (ReportInInfo)session.createQuery(hqlInInfo).uniqueResult();
			
			String changeData = data.getChangeData();//修改值
			String reportValue = reportInInfo.getReportValue();//报表文件当前的值
			
			//值转换
			double changeDouble = 0;
			double reportValueDouble = 0;
			try {
				changeDouble = Double.valueOf(changeData);
			} catch (Exception e) {
				changeDouble = 0;
				
			}
			try {
				reportValueDouble = Double.valueOf(reportValue);
			} catch (Exception e) {
				reportValueDouble = 0;
			}
			
			//最终值为报表单元格的值减去痕迹修改的值
			String finalData = String.valueOf(reportValueDouble-changeDouble);
			
			//修改报表单元格的值
			reportInInfo.setReportValue(finalData);
			session.update(reportInInfo);//更新报表单元格的值*/
			/**更新报表单元格的值..........................结束*/
			
			
			session.update(data);//更新痕迹
			
			
			dbConn.endTransaction(true);
		} catch (Exception e) {
			dbConn.endTransaction(false);
			throw e;
			//throw new Exception("删除数据痕迹失败");
			
		}
		
		
		
	}

	@Override
	public void updateAFDataTraceStatusById(List<Integer> traceIds,Integer status)
			throws Exception {
		if(traceIds==null || traceIds.size()==0)
			return;
		for(Integer id : traceIds){
			//遍历更新单条痕迹数据
			updateAFDataTraceStatusById(id,status);
		}
		
	}
	
	
	
	@Override
	public String findOriDataByTemplateIDAndVersionId(String repInId,
			String cellName) throws Exception {
		List<AFDataTrace> traceList = null;
		if(repInId==null || repInId.equals("")||
				   cellName==null || cellName.equals(""))
			return "";
		String hql = "from AFDataTrace a where a.repInId='"
			+repInId+"'"+" and a.cellName='"
			+cellName+"'  order by a.traceId asc";
		open();
		Query query = session.createQuery(hql);
		traceList = query.list();//查询符合条件的集合
		
		if(traceList==null || traceList.size()==0)
			return "";
		String oriData = traceList.get(0).getOriginalData();
		return oriData;
	}

	public void open(){
		dbConn = new DBConn();
		session = dbConn.openSession();
	}
	
	public void close(){
		try {
			if(session.isOpen())
				session.close();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		dbConn = null;
	}
}
