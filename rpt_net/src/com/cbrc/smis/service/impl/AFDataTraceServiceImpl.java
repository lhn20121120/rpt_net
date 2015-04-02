package com.cbrc.smis.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.commons.beanutils.BeanUtils;

import com.cbrc.org.form.AFDataTraceForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.entity.AFDataTrace;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.service.IAFDataTraceService;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.service.AFReportDealDelegate;
import com.fitech.net.adapter.StrutsReportInDelegate;

public class AFDataTraceServiceImpl implements IAFDataTraceService {
	private DBConn dbConn = null;
	private Session session = null;
	
	
	
	@Override
	public List<AFDataTraceForm> findListByAFDataTrace(
			AFDataTraceForm traceForm, int pageSize, int pageNo,String reportFlag)
			throws Exception {
		open();
		List<AFDataTraceForm> formList = null;//��ѯ�������
		List<AFDataTrace> traceList = null;
		StringBuffer oriHQL=new StringBuffer();
		String term[]=traceForm.getReportTerm().split("-");
		String report_term=term[0]+"-"+term[1];
		try{
			  oriHQL.append( " select a from AFDataTrace a  ") ;
				if("1".equals(reportFlag)){
					oriHQL.append(" ,ReportIn b  where a.repInId=b.repInId ");
				}else{
					oriHQL.append(" ,AfReport b where  a.repInId=b.repId");
				}
					
			
			if(traceForm!=null){//����Ϊ��
//				add  by wmm �޸�Ϊ����Ȩ�����鿴���ݺۼ�
//				if(traceForm.getUsername()!=null && !traceForm.getUsername().equals(""))//�޸���
//					whereSQL += " and a.username='"+traceForm.getUsername()+"'";

				if(traceForm.getBeginDate()!=null && !traceForm.getBeginDate().equals(""))//��ʼʱ��
					oriHQL.append(" and substr(a.dateTime,1,10)>='"+traceForm.getBeginDate()+"'");
				if(traceForm.getEndDate()!=null && !traceForm.getEndDate().equals(""))//����ʱ��
					oriHQL.append(" and substr(a.dateTime,1,10)<='"+traceForm.getEndDate()+"'");
				if(traceForm.getReportTerm()!=null&&!"".equals(traceForm.getReportTerm())){//��������
					oriHQL.append(" and b.year="+term[0]+" and b.term="+term[1]);
				}
				if(traceForm.getRepName()!=null&&!"".equals(traceForm.getRepName())){//��������
					oriHQL.append(" and b.repName like '%"+traceForm.getRepName()+"%'");
				}
				if(traceForm.getOrgId()!=null&&!"".equals(traceForm.getOrgId())&&!Config.DEFAULT_VALUE.equals(traceForm.getOrgId())){//��������
					oriHQL.append(" and b.orgId= '"+traceForm.getOrgId()+"'");
				}
				
			}
			if(reportFlag!=null && !"".equals(reportFlag) && !"null".equals(reportFlag)){
				oriHQL.append( " and a.reportFlag='"+reportFlag+"' ");
			}
			oriHQL.append(" order by a.dateTime ");
			
			Query query = session.createQuery(oriHQL.toString());
			query.setFirstResult(pageNo);
			query.setMaxResults(pageSize);
			traceList = query.list();
			formList = new ArrayList<AFDataTraceForm>();
			for(AFDataTrace af : traceList){
				ReportIn reportIn=null;
				AfReport afReport=null;
				if("1".equals(reportFlag)){
					
					reportIn = StrutsReportInDelegate.findById(Integer.valueOf(af.getRepInId()));
				}else{
					afReport =AFReportDealDelegate.getAFReportByRepId(Long.valueOf(af.getRepInId()));
				}
				Integer traceId;//����

			    AFDataTraceForm tf = new AFDataTraceForm();
				tf.setCellName(af.getCellName());
				tf.setRepInId(Integer.valueOf(af.getRepInId()));
				tf.setUsername(af.getUsername());
				tf.setDateTime(af.getDateTime());
				tf.setOriginalData(af.getOriginalData());
				tf.setChangeData(af.getChangeData());
				tf.setFinalData(af.getFinalData());
				tf.setDescTrace(af.getDescTrace());
				tf.setStatus(af.getStatus());
				if("1".equals(reportFlag)){
					
					tf.setRepName(reportIn.getRepName());
					tf.setReportTerm(reportIn.getYear()+"-"+reportIn.getTerm());//��+��
				}else{
					tf.setRepName(afReport.getRepName());
					tf.setReportTerm(afReport.getYear()+"-"+afReport.getTerm());//��+��
				}
				formList.add(tf);
			}
		}catch(Exception e){
			e.printStackTrace();
			
		}finally{
			close();
		}
		
		
		return formList;//���ؼ���
	}

	
	@Override
	public List<AFDataTraceForm> findListByAFDataTrace(AFDataTraceForm traceForm,String reportFlag)
			throws Exception {
		
		open();
		List<AFDataTraceForm> formList = null;//��ѯ�������
		List<AFDataTrace> traceList = null;
		StringBuffer oriHQL=new StringBuffer();
		String term[]=traceForm.getReportTerm().split("-");
		String report_term=term[0]+"-"+term[1];
		try{
			  oriHQL.append( " select a from AFDataTrace a  ") ;
				if("1".equals(reportFlag)){
					oriHQL.append(" ,ReportIn b  where a.repInId=b.repInId ");
				}else{
					oriHQL.append(" ,AfReport b where  a.repInId=b.repId");
				}
					
			
			if(traceForm!=null){//����Ϊ��
//				add  by wmm �޸�Ϊ����Ȩ�����鿴���ݺۼ�
//				if(traceForm.getUsername()!=null && !traceForm.getUsername().equals(""))//�޸���
//					whereSQL += " and a.username='"+traceForm.getUsername()+"'";

				if(traceForm.getBeginDate()!=null && !traceForm.getBeginDate().equals(""))//��ʼʱ��
					oriHQL.append(" and substr(a.dateTime,1,10)>='"+traceForm.getBeginDate()+"'");
				if(traceForm.getEndDate()!=null && !traceForm.getEndDate().equals(""))//����ʱ��
					oriHQL.append(" and substr(a.dateTime,1,10)<='"+traceForm.getEndDate()+"'");
				if(traceForm.getReportTerm()!=null&&!"".equals(traceForm.getReportTerm())){//��������
					oriHQL.append(" and b.year="+term[0]+" and b.term="+term[1]);
				}
				if(traceForm.getRepName()!=null&&!"".equals(traceForm.getRepName())){//��������
					oriHQL.append(" and b.repName like '%"+traceForm.getRepName()+"%'");
				}
				if(traceForm.getOrgId()!=null&&!"".equals(traceForm.getOrgId())&&!Config.DEFAULT_VALUE.equals(traceForm.getOrgId())){//��������
					oriHQL.append(" and b.orgId= '"+traceForm.getOrgId()+"'");
				}
				
			}
			if(reportFlag!=null && !"".equals(reportFlag) && !"null".equals(reportFlag)){
				oriHQL.append( " and a.reportFlag='"+reportFlag+"' ");
			}
			oriHQL.append(" order by a.dateTime ");
			
			Query query = session.createQuery(oriHQL.toString());
			
			traceList = query.list();
			formList = new ArrayList<AFDataTraceForm>();
			for(AFDataTrace af : traceList){
				ReportIn reportIn=null;
				AfReport afReport=null;
				if("1".equals(reportFlag)){
					
					reportIn = StrutsReportInDelegate.findById(Integer.valueOf(af.getRepInId()));
				}else{
					afReport =AFReportDealDelegate.getAFReportByRepId(Long.valueOf(af.getRepInId()));
				}
//				ReportIn reportIn = StrutsReportInDelegate.findById(Integer.valueOf(af.getRepInId()));
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
				if("1".equals(reportFlag)){
					
					tf.setRepName(reportIn.getRepName());
					tf.setReportTerm(reportIn.getYear()+"-"+reportIn.getTerm());//��+��
				}else{
					tf.setRepName(afReport.getRepName());
					tf.setReportTerm(afReport.getYear()+"-"+afReport.getTerm());//��+��
				}
			
				formList.add(tf);
			}
			close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		
		return formList;//���ؼ���
	}


	/***
	 * �������ݺۼ�
	 */
	public void addAFDataTrace(AFDataTrace af) throws Exception {
		boolean flag=false;
		open();
		try{
			if(af!=null){
				dbConn.beginTransaction();
				session.save(af);
				flag=true;
			}
		}catch(Exception e){
			e.printStackTrace();
			flag=false;
		}finally{
			dbConn.endTransaction(flag);
			close();
		}
		
		
	}

	public List<AFDataTraceForm> findListByTemplateIDandVersionId(
			String repInId, String cellName)
			throws Exception {
		List<AFDataTrace> traceList = null;
		List<AFDataTraceForm> traceFormList = new ArrayList<AFDataTraceForm>();
		try{
			if(repInId==null || repInId.equals("")||
					   cellName==null || cellName.equals(""))
						return traceFormList;
					String hql = "from AFDataTrace a where a.repInId="
								+repInId+" and a.cellName='"
								+cellName+"'  order by a.traceId desc";
					
					String oriHql = "from AFDataTrace a where a.repInId="
						+repInId+" and a.cellName='"
						+cellName+"'  order by a.traceId asc";
					open();
					Query query = session.createQuery(hql);
					traceList = query.list();//��ѯ���������ļ���
					
					if(traceList==null || traceList.size()==0)
						return traceFormList;
					
					String oriData = ((AFDataTrace)session.createQuery(oriHql).list().get(0)).getOriginalData();
					for(AFDataTrace af : traceList){
						AFDataTraceForm tf = new AFDataTraceForm();
						BeanUtils.copyProperties(tf, af);
//						String curDate = tf.getDateTime();
//						if(curDate!=null && !curDate.equals("")){//������������ȡ������
//							curDate = curDate.substring(0,10);
//							tf.setDateTime(curDate);
//						}
						tf.setOriData(oriData);
						traceFormList.add(tf);
					}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();//�ͷ���Դ
		}
		
		
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
			
			/**���±���Ԫ���ֵ..........................��ʼ
			//��ѯ��������Ԫ����Ϣ
			String hqlMCell = "from MCell m where m.MChildReport.comp_id.childRepId='"
							+reportIn.getMChildReport().getComp_id().getChildRepId()
							+"' and m.MChildReport.comp_id.versionId='"
							+reportIn.getMChildReport().getComp_id().getVersionId()+"' and m.cellName='"+data.getCellName()+"'";
			MCell mCell = (MCell)session.createQuery(hqlMCell).uniqueResult();
			
			//��ѯ����Ԫ���Ӧ��ֵ
			String hqlInInfo = "from ReportInInfo r where r.comp_id.cellId='"+mCell.getCellId()+"' and r.comp_id.repInId='"+data.getRepInId()+"'";
			ReportInInfo reportInInfo = (ReportInInfo)session.createQuery(hqlInInfo).uniqueResult();
			
			String changeData = data.getChangeData();//�޸�ֵ
			String reportValue = reportInInfo.getReportValue();//�����ļ���ǰ��ֵ
			
			//ֵת��
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
			
			//����ֵΪ����Ԫ���ֵ��ȥ�ۼ��޸ĵ�ֵ
			String finalData = String.valueOf(reportValueDouble-changeDouble);
			
			//�޸ı���Ԫ���ֵ
			reportInInfo.setReportValue(finalData);
			session.update(reportInInfo);//���±���Ԫ���ֵ*/
			/**���±���Ԫ���ֵ..........................����*/
			
			
			session.update(data);//���ºۼ�
			
			
			dbConn.endTransaction(true);
		} catch (Exception e) {
			dbConn.endTransaction(false);
			throw e;
			//throw new Exception("ɾ�����ݺۼ�ʧ��");
			
		}finally{
			close();
		}
		
		
		
	}

	@Override
	public void updateAFDataTraceStatusById(List<Integer> traceIds,Integer status)
			throws Exception {
		if(traceIds==null || traceIds.size()==0)
			return;
		for(Integer id : traceIds){
			//�������µ����ۼ�����
			updateAFDataTraceStatusById(id,status);
		}
		
	}
	
	
	
	@Override
	public String findOriDataByTemplateIDAndVersionId(String repInId,
			String cellName) throws Exception {
		List<AFDataTrace> traceList = null;
		String oriData="";
		if(repInId==null || repInId.equals("")||
				   cellName==null || cellName.equals(""))
			return "";
		String hql = "from AFDataTrace a where a.repInId="
			+repInId+" and a.cellName='"
			+cellName+"'  order by a.traceId asc";
		open();
		try{
			Query query = session.createQuery(hql);
			traceList = query.list();//��ѯ���������ļ���
			
			if(traceList==null || traceList.size()==0)
				return "";
			oriData= traceList.get(0).getOriginalData();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		
		 
		
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
