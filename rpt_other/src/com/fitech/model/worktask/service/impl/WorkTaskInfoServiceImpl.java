package com.fitech.model.worktask.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.fitech.framework.core.common.Config;
import com.fitech.framework.core.dao.BaseDaoException;
import com.fitech.framework.core.service.BaseServiceException;
import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.model.worktask.model.pojo.TFreqInfo;
import com.fitech.model.worktask.model.pojo.ViewWorktaskRole;
import com.fitech.model.worktask.model.pojo.WorkTaskBusiLine;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskTrriger;
import com.fitech.model.worktask.model.pojo.WorkTaskType;
import com.fitech.model.worktask.security.Operator;
import com.fitech.model.worktask.service.ITFreqInfoService;
import com.fitech.model.worktask.service.IWorkTaskInfoService;
import com.fitech.model.worktask.service.IWorkTaskOrgService;
import com.fitech.model.worktask.service.IWorkTaskRoleService;
import com.fitech.model.worktask.service.IWorkTaskTemplateService;

public class WorkTaskInfoServiceImpl extends
		DefaultBaseService<WorkTaskInfo, String> implements
		IWorkTaskInfoService {
	private IWorkTaskOrgService workTaskOrgService;
	private IWorkTaskTemplateService workTaskTemplateService;
	private IWorkTaskRoleService workTaskRoleService;
	
	private WorkTaskInfo task;
	
	public WorkTaskInfo getTask() {
		return task;
	}

	public void setTask(WorkTaskInfo task) {
		this.task = task;
	}

	public IWorkTaskRoleService getWorkTaskRoleService() {
		return workTaskRoleService;
	}

	public void setWorkTaskRoleService(IWorkTaskRoleService workTaskRoleService) {
		this.workTaskRoleService = workTaskRoleService;
	}

	public IWorkTaskTemplateService getWorkTaskTemplateService() {
		return workTaskTemplateService;
	}

	public void setWorkTaskTemplateService(
			IWorkTaskTemplateService workTaskTemplateService) {
		this.workTaskTemplateService = workTaskTemplateService;
	}

	public IWorkTaskOrgService getWorkTaskOrgService() {
		return workTaskOrgService;
	}

	public void setWorkTaskOrgService(IWorkTaskOrgService workTaskOrgService) {
		this.workTaskOrgService = workTaskOrgService;
	}

	public String createAFOrgDataJSONByAsc(Operator operator ,List list) {
		StringBuffer treeJSON = new StringBuffer("[");
		treeJSON.append("{\"id\":\"zh\",");
		treeJSON.append("\"text\":\"总行\"");
		
		if(list!=null&&list.contains("zh")){
			treeJSON.append(",\"checked\":"+"\""+true+"\"");
		}
		treeJSON.append("},");
		treeJSON.append("{\"id\":\"fh\",");
		treeJSON.append("\"text\":\"分行\"");
		if(list!=null&&list.contains("fh")){
			treeJSON.append(",\"checked\":"+"\""+true+"\"");
		}
		treeJSON.append("},");
		treeJSON.append("{\"id\":\"zhh\",");
		treeJSON.append("\"text\":\"支行\"");
		if(list!=null&&list.contains("zhh")){
			treeJSON.append(",\"checked\":"+"\""+true+"\"");
		}
		treeJSON.append("},");
		treeJSON.append("{\"id\":\"xn\",");
		treeJSON.append("\"text\":\"虚拟机构\"");
		if(list!=null&&list.contains("xn")){
			treeJSON.append(",\"checked\":"+"\""+true+"\"");
		}
		treeJSON.append("}");
			treeJSON.append("]");
		System.out.println(treeJSON);
		return treeJSON.toString();
	}
	
	public String createAFOrgDataJSON(Operator operator ,List list) {
		List orgList = null;
		if (operator.isSuperManager())// 超级用户查询所有机构
			orgList = workTaskOrgService.getAllFirstOrg();
		else
			// 查询用户机构
			orgList = workTaskOrgService.getFirstOrgById(operator.getOrgId(),operator.getOperatorId());
		StringBuffer treeJSON = new StringBuffer("[");

		for (int i = 0; i < orgList.size(); i++) {
			String[] strs = (String[]) orgList.get(i);
			treeJSON.append("{\"id\":\""+strs[0]+"\",");
			treeJSON.append("\"text\":"+"\""+strs[1]+"\"");
			if(list!=null&&list.contains(strs[0])){
				treeJSON.append(",\"checked\":true");
			}
			treeJSON.append(iteratorCreate(strs[0],list,operator.getOperatorId().toString()));//递归加载子机构

			treeJSON.append("}");
			if (i != orgList.size() - 1)
				treeJSON.append(",");
		}

		treeJSON.append("]");
		System.out.println(treeJSON);
		return treeJSON.toString();
	}

	private String iteratorCreate(String id ,List list ,String userId) {
		List orgList = workTaskOrgService.getChildListByOrgId(id ,userId);

		if (orgList == null || orgList.size() == 0)
			return "";
		StringBuffer treeJSON = new StringBuffer(",\"children\":[");
		for (int i = 0; i < orgList.size(); i++) {
			String[] strs = (String[]) orgList.get(i);
			treeJSON.append("{\"id\":\"" + strs[0] + "\",\"text\":\"" + strs[1]
					+ "\"");
			if(list!=null&&list.contains(strs[0])){
				treeJSON.append(",\"checked\":"+"\""+true+"\"");
			}
			treeJSON.append(iteratorCreate(strs[0], list,userId));
			treeJSON.append("}");
			if (i != orgList.size() - 1)
				treeJSON.append(",");

		}
		treeJSON.append("]");
		return treeJSON.toString();
	}

	public Map<String,String> findAllTempaltesId(String busiLine,String userId){
		Map<String,String> templateIds = new HashMap<String,String>();
		List groupList = workTaskTemplateService.getAllGroup(busiLine);

		if (groupList == null || groupList.size() == 0)
			return null;
		for (int i = 0; i < groupList.size(); i++) {
			Object[] strs = (Object[]) groupList.get(i);
			List templateList = workTaskTemplateService.getTemplateByGroupId((String)strs[0] ,userId ,busiLine,null);
			if (templateList == null || templateList.size() == 0)
				continue;
			for (int j = 0;j< templateList.size();j++) {
				Object[] ts = (Object[]) templateList.get(j);
				templateIds.put((String)ts[0], (String)ts[2]);
			}
		}
		System.out.println();
		return templateIds;
	}
	
	@Override
//	public String createAFTemplateDataJSON(Operator operator,List list) {
	public String createAFTemplateDataJSON(Operator operator,List list,WorkTaskInfo task) {
		List templateTypeList = null;
//		templateTypeList = workTaskTemplateService.findAllBusiLine();
		this.task = task;
		StringBuffer treeJSON = new StringBuffer("[");

//		for (int i = 0; i < templateTypeList.size(); i++) {
//			String busiLineId = (String) templateTypeList.get(i);
		String busiLineId=task.getBusiLineId();
			treeJSON.append("{\"id\":\""+busiLineId+"\"");
			if(busiLineId.trim().equals("yjtx"))
				treeJSON.append(",\"text\":"+"\""+"银监报表"+"\"");
			if(busiLineId.trim().equals("rhtx"))
				treeJSON.append(",\"text\":"+"\""+"人行报表"+"\"");
			if(busiLineId.trim().equals("qttx"))
				treeJSON.append(",\"text\":"+"\""+"其他报表"+"\"");
			System.out.println(operator.getOperatorId().toString());
			treeJSON.append(templateGroupIteratorCreate(busiLineId ,list,operator.getOperatorId().toString()));//递归加载子机构

			treeJSON.append("}");
//			if (i != templateTypeList.size() - 1)
//				treeJSON.append(",");
//		}

		treeJSON.append("]");
		System.out.println(treeJSON);
		return treeJSON.toString();
		
	}
	
/**
 * 生成 报表树
 * @param id
 * @return
 */
	private String templateGroupIteratorCreate(String busiLineId ,List list,String userId) {
		
		List groupList = workTaskTemplateService.getAllGroup(busiLineId);

		if (groupList == null || groupList.size() == 0)
			return "";
		StringBuffer treeJSON = new StringBuffer(",\"children\":[");
		for (int i = 0; i < groupList.size(); i++) {
			Object[] strs = (Object[]) groupList.get(i);
			treeJSON.append("{\"id\":\"" + strs[0] + "\",\"text\":\"" +strs[1]
					+ "\"");
//			if(list!=null&&list.contains(strs[0])){
//				treeJSON.append(",\"checked\":"+"\""+true+"\"");
//			}
			treeJSON.append(templateIteratorCreate((String)strs[0],list,userId,busiLineId));
			treeJSON.append("}");
			if (i != groupList.size() - 1)
				treeJSON.append(",");

		}
		treeJSON.append("]");
		return treeJSON.toString();
	}
	
	
	private String templateIteratorCreate(String templateGroupId ,List list ,String userId ,String busiLineId) {
		
		List templateList = workTaskTemplateService.getTemplateByGroupId(templateGroupId ,userId ,busiLineId,this.task.getFreqId());

		if (templateList == null || templateList.size() == 0)
			return "";
		StringBuffer treeJSON = new StringBuffer(",\"children\":[");
		for (int i = 0; i < templateList.size(); i++) {
			Object[] strs = (Object[]) templateList.get(i);
			treeJSON.append("{\"id\":\"" + strs[0]+"_"+strs[2] + "\",\"text\":\"" + strs[0]+"_"+strs[1]
					+ "\"");

			if(list!=null&&list.contains(strs[0])){
				treeJSON.append(",\"checked\":true");
			}
			treeJSON.append("}");
			if (i != templateList.size() - 1)
				treeJSON.append(",");

		}
		treeJSON.append("]");
		return treeJSON.toString();
	}

	public Map<String ,String> findAllRole() throws Exception{
		Map roleMap = null;
		List list  = workTaskRoleService.findAllRole();
		if(list!=null&&list.size()>0 ){
			roleMap = new HashMap();
		}
		for (int i = 0; i < list.size(); i++) {
			ViewWorktaskRole v = (ViewWorktaskRole) list.get(i);
			roleMap.put(v.getId().getRoleId(), v.getId().getRoleName());
		}
		
		return roleMap;
		
	}

	@Override
	public WorkTaskInfo findById(Integer taskId) {
		List list = null;
		WorkTaskInfo task = null;
		String hql = "from WorkTaskInfo t where t.taskId="+taskId;
		try {
			list = this.findListByHsql(hql, null);
			if(list!=null&&list.size()>0)
				task = (WorkTaskInfo)list.get(0);
		} catch (BaseServiceException e) {
			e.printStackTrace();
		}
		return task;
		
	}

	@Override
	public void updateFlag(WorkTaskInfo task) {
		try {
			this.update(task) ;
		} catch (BaseServiceException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public List<WorkTaskInfo> findListByParam(WorkTaskInfo task) {
		List list = null;
		
		StringBuffer hql = new StringBuffer("from WorkTaskInfo t where 1=1 ");
		if(task != null &&task.getTaskName()!=null&&!task.getTaskName().equals("")){
			hql.append("and  t.taskName like '%"+task.getTaskName() +"%'");
		}
		if(task != null && task.getFreqId()!=null&&!task.getFreqId().equals("-99")){
			hql.append("and  t.freqId = '" + task.getFreqId()+"'");
		}
		try {
			list = this.findListByHsql(hql.toString(), null);
			
		} catch (BaseServiceException e) {
			e.printStackTrace();
		}
		return list;
	}
	private ITFreqInfoService tFreqInfoService;
	public ITFreqInfoService gettFreqInfoService() {
		return tFreqInfoService;
	}

	public void settFreqInfoService(ITFreqInfoService tFreqInfoService) {
		this.tFreqInfoService = tFreqInfoService;
	}

	@Override
	public Map<String, String> findAllFreq() {
		Map freqMap = null;
		try {
			List list  = tFreqInfoService.findAll();
			if(list!=null ){
				freqMap = new HashMap();
			}
			for (int i = 0; i < list.size(); i++) {
				TFreqInfo v = (TFreqInfo) list.get(i);
				freqMap.put(v.getFreqId(), v.getFreqName());
			}
		} catch (BaseServiceException e) {
			e.printStackTrace();
		}
		
		return freqMap;
	
	}
	// add by wmm
	public List<WorkTaskBusiLine> findAllBusiLine() {
		List<WorkTaskBusiLine> busiLine = new ArrayList<WorkTaskBusiLine>();
		String hql="from WorkTaskBusiLine ";
		try {
			List<WorkTaskBusiLine> list  = tFreqInfoService.findListByHsql(hql, null);
		
			for (int i = 0; i < list.size(); i++) {
				WorkTaskBusiLine v = list.get(i);
				busiLine.add(v);
			}
		} catch (BaseServiceException e) {
			e.printStackTrace();
		}
		
		return busiLine;
		
	}
	
	//add by wmm
	public List<WorkTaskType> findAllTaskType() {
		List<WorkTaskType> taskType =new ArrayList<WorkTaskType>();
		String hql="from WorkTaskType where useingFlag=1 ";
		try {
			List<WorkTaskType> list  = tFreqInfoService.findListByHsql(hql, null);
			
			for (int i = 0; i < list.size(); i++) {
				WorkTaskType v = list.get(i);
				taskType.add(v);
			}
		} catch (BaseServiceException e) {
			e.printStackTrace();
		}
		
		return taskType;
		
	}
	
	/**By LK
	 * 生成树的JSON对象文件保存在WEBROOT下的 json目录
	 * @param operator
	 * @param obg_TreeData
	 * @param TreeName
	 * @throws Exception
	 */
	public void createTree(Operator operator ,String obg_TreeData,String TreeName ) throws Exception{
		String orgFilePath = Config.WEBROOTPATH +  Config.FILESEPARATOR+
		 "json" + Config.FILESEPARATOR +TreeName+"_tree_data_"+operator.getOperatorId()+".json";
System.out.println("filePath:"+orgFilePath);			
		File file = new File(orgFilePath);
		if(file.exists());
			file.delete();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
		bw.write(obg_TreeData);
		bw.flush();
		bw.close();
		System.out.println("TreeJSON写入成功");
	}

	@Override
	public WorkTaskTrriger finAllTrriger(String trriger) {
		List list =null;
		WorkTaskTrriger t =null;
		String hsql = "from WorkTaskTrriger t where t.trrigerId='"+trriger+"'";
		try {
			list = this.objectDao.findListByHsql(hsql, null);
			if(list!=null&&list.size()>0)
				t=(WorkTaskTrriger) list.get(0);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}

	@Override
	public boolean delTask(WorkTaskInfo task) {
		// TODO Auto-generated method stub
		boolean result = false;
		if(task==null)return result;
		if(task.getTaskId()==null)return result;
		Session session = null; 
		Connection conn = null;
		Statement st = null;
		try {
			session = this.objectDao.getHibernateTemplate().getSessionFactory().openSession();
			conn = session.connection();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			//--删除指定任务下的业务数据
			String sql ="delete FROM DATA_VALIDATE_INFO WHERE REP_IN_ID IN "
					+ " (SELECT REP_IN_ID from report_in r, m_actu_rep m "
					+ " WHERE trim(r.child_rep_id) || trim(r.org_id) in "
					+ " (select trim(template_id) || trim(org_id) from work_task_node_object "
					+ " where node_id in (select node_id from work_task_node_info where task_id = "+task.getTaskId()+")) "
					+ " and r.child_rep_id = m.child_rep_id and r.version_id = m.version_id "
					+ " and r.data_range_id = m.data_range_id and m.rep_freq_id = "
					+ " (select m.rep_freq_id from m_rep_freq m, t_freq_info t "
					+ " where m.rep_freq_name = t.freq_name and t.freq_id = '"+task.getFreqId()+"'))";
			st.addBatch(sql);
			
			sql = "delete from report_again_set where REP_IN_ID IN"
					+ " (SELECT REP_IN_ID from report_in r, m_actu_rep m "
					+ " WHERE trim(r.child_rep_id) || trim(r.org_id) in "
					+ " (select trim(template_id) || trim(org_id) from work_task_node_object "
					+ " where node_id in (select node_id from work_task_node_info where task_id = "+task.getTaskId()+")) "
					+ " and r.child_rep_id = m.child_rep_id and r.version_id = m.version_id "
					+ " and r.data_range_id = m.data_range_id and m.rep_freq_id = "
					+ " (select m.rep_freq_id from m_rep_freq m, t_freq_info t "
					+ " where m.rep_freq_name = t.freq_name and t.freq_id = '"+task.getFreqId()+"'))";
			st.addBatch(sql);
			
			sql = "delete from af_datavalidateinfo where rep_id in"
					+ "(select rep_id from af_report where trim(template_id) || trim(org_id) in "
					+ "(select trim(template_id) || trim(org_id) from " 
					+ " work_task_node_object where node_id in(select node_id from " 
					+ " work_task_node_info where task_id="+task.getTaskId()+")) "
					+ " and rep_freq_id = (select m.rep_freq_id from m_rep_freq m,t_freq_info t"
					+ " where  m.rep_freq_name = t.freq_name "
					+ " and t.freq_id='"+task.getFreqId()+"'))";
			st.addBatch(sql);
			
			sql = "delete from af_data_trace WHERE REP_IN_ID IN"
					+ " (SELECT REP_IN_ID from report_in r, m_actu_rep m "
					+ " WHERE trim(r.child_rep_id) || trim(r.org_id) in "
					+ " (select trim(template_id) || trim(org_id) from work_task_node_object "
					+ " where node_id in (select node_id from work_task_node_info where task_id = "+task.getTaskId()+")) "
					+ " and r.child_rep_id = m.child_rep_id and r.version_id = m.version_id "
					+ " and r.data_range_id = m.data_range_id and m.rep_freq_id = "
					+ " (select m.rep_freq_id from m_rep_freq m, t_freq_info t "
					+ " where m.rep_freq_name = t.freq_name and t.freq_id = '"+task.getFreqId()+"'))";
			st.addBatch(sql);
			
			sql = "delete FROM report_in_info  WHERE REP_IN_ID IN"
					+ " (SELECT REP_IN_ID from report_in r, m_actu_rep m "
					+ " WHERE trim(r.child_rep_id) || trim(r.org_id) in "
					+ " (select trim(template_id) || trim(org_id) from work_task_node_object "
					+ " where node_id in (select node_id from work_task_node_info where task_id = "+task.getTaskId()+")) "
					+ " and r.child_rep_id = m.child_rep_id and r.version_id = m.version_id "
					+ " and r.data_range_id = m.data_range_id and m.rep_freq_id = "
					+ " (select m.rep_freq_id from m_rep_freq m, t_freq_info t "
					+ " where m.rep_freq_name = t.freq_name and t.freq_id = '"+task.getFreqId()+"'))";
			st.addBatch(sql);

			sql = "delete from af_pbocreportdata where rep_id in"
					+ "(select rep_id from af_report where trim(template_id) || trim(org_id) in "
					+ "(select trim(template_id) || trim(org_id) from " 
					+ " work_task_node_object where node_id in(select node_id from " 
					+ " work_task_node_info where task_id="+task.getTaskId()+")) "
					+ " and rep_freq_id = (select m.rep_freq_id from m_rep_freq m,t_freq_info t"
					+ " where  m.rep_freq_name = t.freq_name "
					+ " and t.freq_id='"+task.getFreqId()+"'))";
			st.addBatch(sql);
			
			sql = "delete from af_otherreportdata where rep_id in"
					+ "(select rep_id from af_report where trim(template_id) || trim(org_id) in "
					+ "(select trim(template_id) || trim(org_id) from " 
					+ " work_task_node_object where node_id in(select node_id from " 
					+ " work_task_node_info where task_id="+task.getTaskId()+")) "
					+ " and rep_freq_id = (select m.rep_freq_id from m_rep_freq m,t_freq_info t"
					+ " where  m.rep_freq_name = t.freq_name "
					+ " and t.freq_id='"+task.getFreqId()+"'))";
			st.addBatch(sql);
			
			sql = "delete from af_force_rep WHERE REP_IN_ID IN"
					+ " (SELECT REP_IN_ID from report_in r, m_actu_rep m "
					+ " WHERE trim(r.child_rep_id) || trim(r.org_id) in "
					+ " (select trim(template_id) || trim(org_id) from work_task_node_object "
					+ " where node_id in (select node_id from work_task_node_info where task_id = "+task.getTaskId()+")) "
					+ " and r.child_rep_id = m.child_rep_id and r.version_id = m.version_id "
					+ " and r.data_range_id = m.data_range_id and m.rep_freq_id = "
					+ " (select m.rep_freq_id from m_rep_freq m, t_freq_info t "
					+ " where m.rep_freq_name = t.freq_name and t.freq_id = '"+task.getFreqId()+"'))";
			st.addBatch(sql);
			
			sql = "delete from af_report_force_rep where rep_id in"
					+ "(select rep_id from af_report where trim(template_id) || trim(org_id) in "
					+ "(select trim(template_id) || trim(org_id) from " 
					+ " work_task_node_object where node_id in(select node_id from " 
					+ " work_task_node_info where task_id="+task.getTaskId()+")) "
					+ " and rep_freq_id = (select m.rep_freq_id from m_rep_freq m,t_freq_info t"
					+ " where  m.rep_freq_name = t.freq_name "
					+ " and t.freq_id='"+task.getFreqId()+"'))";
			st.addBatch(sql);
			
			sql = "delete from af_report_again where rep_id in"
					+ "(select rep_id from af_report where trim(template_id) || trim(org_id) in "
					+ "(select trim(template_id) || trim(org_id) from " 
					+ " work_task_node_object where node_id in(select node_id from " 
					+ " work_task_node_info where task_id="+task.getTaskId()+")) "
					+ " and rep_freq_id = (select m.rep_freq_id from m_rep_freq m,t_freq_info t"
					+ " where  m.rep_freq_name = t.freq_name "
					+ " and t.freq_id='"+task.getFreqId()+"'))";
			st.addBatch(sql);
			
			sql = "delete from report_in r "
					+ " where trim(r.child_rep_id) || trim(r.org_id) in "
					+ " (select trim(template_id) || trim(org_id) from work_task_node_object "
					+ " where node_id in (select node_id from work_task_node_info where task_id = "+task.getTaskId()+")) "
					+ " and r.data_range_id = "
					+ " (select m.data_range_id from report_in r, m_actu_rep m "
					+ " where r.child_rep_id = m.child_rep_id and r.version_id = m.version_id and r.data_range_id = m.data_range_id "
					+ " and m.rep_freq_id = (select m.rep_freq_id from m_rep_freq m, t_freq_info t "
					+ " where m.rep_freq_name = t.freq_name and t.freq_id = '"+task.getFreqId()+"'))";
			st.addBatch(sql);
			
			sql = "delete from af_report where trim(template_id)  || trim(org_id) in "
					+ "(select trim(template_id) || trim(org_id) from work_task_node_object "
					+ " where node_id in(select node_id "
					+ " from work_task_node_info where task_id="+task.getTaskId()+"))"
					+ " and rep_freq_id = (select m.rep_freq_id from m_rep_freq m,t_freq_info t "
					+ " where  m.rep_freq_name = t.freq_name "
					+ " and t.freq_id='"+task.getFreqId()+"')";
			st.addBatch(sql);
			
			//--删除任务生成
			sql = "delete from work_task_rep_force where task_moni_id in(select task_moni_id from work_task_moni where task_id="+task.getTaskId()+")";
			st.addBatch(sql);
			
			sql = "delete from work_task_node_object_moni where task_moni_id in(select task_moni_id from work_task_moni where task_id="+task.getTaskId()+")";
			st.addBatch(sql);
			
			sql = "delete from work_task_node_moni where task_moni_id in(select task_moni_id from work_task_moni where task_id="+task.getTaskId()+")";
			st.addBatch(sql);
			
			sql = "delete from work_task_moni where task_id="+task.getTaskId()+"";
			st.addBatch(sql);
			
//			sql = "delete from af_template_org_relation where template_id in(select template_id " +
//					" from work_task_node_object where node_id in(select node_id from work_task_node_info where" 
//					+" task_id="+task.getTaskId()+"))";
//			st.addBatch(sql);
//			
//			sql = "delete from af_template_coll_rep where template_id in(select template_id " +
//					"from work_task_node_object where node_id in(select node_id from work_task_node_info where "
//					+"task_id="+task.getTaskId()+"))";
//			st.addBatch(sql);
//			
//			sql = "delete from m_rep_range where child_rep_id in(select template_id from work_task_node_object where " +
//					" node_id in(select node_id from work_task_node_info where task_id="+task.getTaskId()+"))";
//			st.addBatch(sql);
			
			sql = "delete from work_task_node_object where node_id in(select node_id from work_task_node_info where task_id="+task.getTaskId()+")";
			st.addBatch(sql);
			
			sql = "delete from WORK_TASK_NODE_REP_TIME where node_id in(select node_id from work_task_node_info where task_id="+task.getTaskId()+")";
			st.addBatch(sql);
			
			sql = "delete from work_task_node_role where node_id in(select node_id from work_task_node_info where task_id="+task.getTaskId()+")";
			st.addBatch(sql);
			
			sql = "delete from work_task_node_info where task_id="+task.getTaskId()+"";
			st.addBatch(sql);
			
			sql = "delete from work_task_info where task_id="+task.getTaskId()+"";
			st.addBatch(sql);
			
			int[] res = st.executeBatch();
			result = true;
			
			if(result)
				conn.commit();
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			try {
				if(conn!=null)
					conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			try {
				if(conn!=null)
					conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			try {
				if(st!=null)
					st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(session!=null)
				session.close();
		}
		return result;
	}
	
}
