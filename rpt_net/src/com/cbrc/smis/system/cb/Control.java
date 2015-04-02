/*
 * Created on 2005-12-9
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cbrc.smis.system.cb;

import java.io.File;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.ConfigOncb;
import com.cbrc.smis.util.FitechLog;

/**
 * @author cb
 * 
 * 该类用于系统后台数据录入的入口
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Control {

	/**
	 * 该方法用于处理一批ZIP文件
	 * 
	 * 
	 * @throws Exception
	 */

	public void conductZips() throws Exception {

		/** *********************************** */
		/*
		 * File testFile = new File("C:/test");
		 * 
		 * File[] testFiles = testFile.listFiles();
		 * 
		 * int testSize = testFiles.length;
		 * 
		 * for (int ti = 0; ti < testSize; ti++)
		 * 
		 * testFiles[ti].delete();
		 */

		/** *********************************** */

		int size = 0;

		int count = 0;

		File tempDir = new File(ConfigOncb.TEMP_DIR);

		if (!tempDir.exists())

			tempDir.mkdir();

		File file = new File(ConfigOncb.ADDRESSZIP); // 建立一个系统源文件夹的对象

		InputData inputData = new InputData();

		if (file.exists()) {

			File[] files = file.listFiles();

			size = files.length;

			for (int i = 0; i < size; i++) { // 从这里开始依次对该文件夹中的ZIP包进行循环处理

				if (this.isZipFile(files[i])) {

					count++;

					try {

						inputData.conductZip(files[i]); // 对ZIP文件进行处理

						FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, // 成功后记录日志
								ConfigOncb.HANDLER, files[i].getName()
										+ ConfigOncb.INPUTDATASUCCESS, "OVER");

					} catch (Exception e) {
						e.printStackTrace();
						FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, // 不成功记录日志
								ConfigOncb.HANDLER, e.getMessage(), "OVER");

					}
					// System.out.println(files[i] + "入库完成!");

					files[i].delete(); // 在这里将ZIP文件依次删除
				}

				this.deleteAllFile();

			}

		} else {
			file.mkdir(); // 如果不存在该系统文件夹就新建一个
		}

		FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, ConfigOncb.HANDLER,
				"本次入库操作共处理ZIP数据包文件" + count + "个", "OVER");

	}

	/**
	 * 判断一个文件名是否是zip文件名
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean isZipFile(File file) {

		String fileName = file.getName();

		int length = fileName.length();

		if (!file.isFile() || length < 5)

			return false;

		boolean is = false;

		String sub = fileName.substring(length - 4);

		if (sub.equals(".zip"))
			is = true;

		return is;
	}

	/**
	 * 删除所有的临时文件
	 * 
	 */
	public void deleteAllFile() {

		File file = new File(ConfigOncb.TEMP_DIR);

		if (file.exists()) {

			File[] files = file.listFiles();

			int size = files.length;

			for (int i = 0; i < size; i++)

				files[i].delete();
		}
	}

}