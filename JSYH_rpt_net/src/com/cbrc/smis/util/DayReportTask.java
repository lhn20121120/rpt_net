package com.cbrc.smis.util;

import java.util.TimerTask;

import com.fitech.gznx.service.DayReportDelegate;

/**
 * �ձ������ݴ�������
 * 
 * @author Nick
 * 
 */
public class DayReportTask extends TimerTask {

	// �����Ƿ�����ִ�б�־λ
	private boolean isWorking = false;

	private String taskDate = null;

	public DayReportTask() {
		System.out.println("�ձ������ݴ��������������");
	}

	@Override
	public void run() {
		// �����������ִ�У��򲻽����ձ���Ĵ���
		if (isWorking) {
			return;
		}

		isWorking = true;
		// �ձ������ݴ���
		DayReportDelegate.doDayReport(taskDate);// �Զ�ִ������ʱ����ѡ����δִ�е�����˳��ִ��;ǰ̨�ֶ����ʱ���趨��Ҫִ�е�������ͬʱִֻ�б�������

		isWorking = false;

	}

	public String getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}

}
