package com.fitech.model.worktask.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.StatefulJob;

import com.fitech.model.worktask.service.IWorkTaskFreqService;


public class WorkTaskFreqDayServiceImpl extends WorkTaskJobFreqBaseServiceImpl implements IWorkTaskFreqService,StatefulJob{
	
	public Log log = LogFactory.getLog(WorkTaskFreqDayServiceImpl.class);

}
