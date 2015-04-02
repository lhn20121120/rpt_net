package com.fitech.model.worktask.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.StatefulJob;

import com.fitech.model.worktask.service.IWorkTaskFreqService;
/**
 * 季频度实现类
  * @Title: ETLJobFreqTaskSeasonServiceImpl.java 
  * @Package com.fitech.model.etl.service.impl 
  * @Description: TODO
  * @author xfc100
  * @date Mar 3, 2012 5:06:10 PM 
  * @version V1.2
 */
public class WorkTaskFreqSeasonServiceImpl extends WorkTaskJobFreqBaseServiceImpl implements IWorkTaskFreqService,StatefulJob{
	public Log log = LogFactory.getLog(WorkTaskFreqSeasonServiceImpl.class);
}
