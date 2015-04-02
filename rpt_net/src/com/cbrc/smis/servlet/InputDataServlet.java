package com.cbrc.smis.servlet;

/**
 * 该类用于对定时输入数据功能进行初始化并加以调用
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

		timer.schedule(new InputDataTask(), 0, this.getIntervalTime()); // 计划性的执行输入数据的任务

	}

	public long getIntervalTime() {

		long interval = 86390000; // 默认时间间隔

		String intervalString = "";

		try {
			InputStream input = new FileInputStream( // 下面几行代码用于通过读取InputDataTimeConfig.properties来读取时间间隔
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
