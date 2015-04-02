package com.cbrc.smis.system.cb;

import java.util.TimerTask;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.ConfigOncb;
import com.cbrc.smis.util.FitechLog;

;

/**
 * 该类是入库处理的任务类
 * 
 * @author cb
 * 
 */
public class InputDataTask extends TimerTask {

	public static boolean isWorking = true;

	public void run() {

		if (InputDataTask.isWorking) {
			
			InputDataTask.isWorking = false;

			Control control = new Control();

			try {
				control.conductZips();

			} catch (Exception e) {

				FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, ConfigOncb.HANDLER,
						"", "OVER");
			}
			
			InputDataTask.isWorking = true;
		}

	}

}
