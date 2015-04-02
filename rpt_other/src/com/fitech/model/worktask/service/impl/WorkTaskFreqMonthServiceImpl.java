package com.fitech.model.worktask.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.StatefulJob;

import com.fitech.model.worktask.service.IWorkTaskFreqService;

/**
 *  月频度任务类
  * @Title: ETLJobFreqTaskMonthServiceImpl.java 
  * @Package com.fitech.model.etl.service.impl 
  * @Description: TODO
  * @author xfc100
  * @date Mar 3, 2012 4:43:50 PM 
  * @version V1.2
 */
public class WorkTaskFreqMonthServiceImpl extends WorkTaskJobFreqBaseServiceImpl implements IWorkTaskFreqService,StatefulJob{
	
	public Log log = LogFactory.getLog(WorkTaskFreqMonthServiceImpl.class);

}
