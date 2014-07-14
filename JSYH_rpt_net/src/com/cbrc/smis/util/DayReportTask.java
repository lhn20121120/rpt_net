package com.cbrc.smis.util;

import java.util.TimerTask;

import com.fitech.gznx.service.DayReportDelegate;

/**
 * 日报表数据处理任务
 * 
 * @author Nick
 * 
 */
public class DayReportTask extends TimerTask {

	// 任务是否正在执行标志位
	private boolean isWorking = false;

	private String taskDate = null;

	public DayReportTask() {
		System.out.println("日报表数据处理任务启动完成");
	}

	@Override
	public void run() {
		// 如果任务正在执行，则不进行日报表的处理
		if (isWorking) {
			return;
		}

		isWorking = true;
		// 日报表数据处理
		DayReportDelegate.doDayReport(taskDate);// 自动执行任务时，撒选所有未执行的任务顺序执行;前台手动点击时，设定需要执行的期数，同时只执行本期数据

		isWorking = false;

	}

	public String getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}

}
