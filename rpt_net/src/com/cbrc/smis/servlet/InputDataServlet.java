package com.cbrc.smis.servlet;

/**
 * �������ڶԶ�ʱ�������ݹ��ܽ��г�ʼ�������Ե���
 */
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.ConfigOncb;
import com.cbrc.smis.system.cb.InputDataTask;
import com.cbrc.smis.util.FitechLog;

public class InputDataServlet extends HttpServlet {

	public void init() throws ServletException {

		Timer timer = new Timer();

		timer.schedule(new InputDataTask(), 0, this.getIntervalTime()); // �ƻ��Ե�ִ���������ݵ�����

	}

	public long getIntervalTime() {

		long interval = 86390000; // Ĭ��ʱ����

		String intervalString = "";

		try {
			InputStream input = new FileInputStream( // ���漸�д�������ͨ����ȡInputDataTimeConfig.properties����ȡʱ����
					ConfigOncb.INPUTDATAPREPADDR);

			Properties props = new Properties();

			props.load(input);

			intervalString = props.getProperty("interval");

			interval = Long.parseLong(intervalString);

		} catch (Exception e) {

			FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, ConfigOncb.HANDLER,
					ConfigOncb.WRITERCOUNTONSTARTUP, "OVER");

		}

		return interval;
	}

}
