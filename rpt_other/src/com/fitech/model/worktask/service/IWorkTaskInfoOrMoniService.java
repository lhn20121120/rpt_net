package com.fitech.model.worktask.service;

import java.util.Map;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.framework.core.web.PageResults;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskMoni;
import com.fitech.model.worktask.vo.WorkTaskInfoOrMoniVo;

/**
 * 查看工作任务定义
 * @author 姜明青
 *
 */
public interface IWorkTaskInfoOrMoniService extends IBaseService<WorkTaskInfo, String> {

	/**查看所有的工作任务定义
	 * @throws Exception */
	public PageResults findPendingTask(Map parameterMap,WorkTaskInfoOrMoniVo monivo,
			PageResults pageResults)throws Exception;
	
	/**改变工作任务的状态
	 * @throws Exception */
	public void updateTaskInfo(WorkTaskMoni moni) throws Exception;
	
	public WorkTaskMoni findTaskMoni(Integer monid);
}
