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
 * ��������ϵͳ��̨����¼������
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Control {

	/**
	 * �÷������ڴ���һ��ZIP�ļ�
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

		File file = new File(ConfigOncb.ADDRESSZIP); // ����һ��ϵͳԴ�ļ��еĶ���

		InputData inputData = new InputData();

		if (file.exists()) {

			File[] files = file.listFiles();

			size = files.length;

			for (int i = 0; i < size; i++) { // �����￪ʼ���ζԸ��ļ����е�ZIP������ѭ������

				if (this.isZipFile(files[i])) {

					count++;

					try {

						inputData.conductZip(files[i]); // ��ZIP�ļ����д���

						FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, // �ɹ����¼��־
								ConfigOncb.HANDLER, files[i].getName()
										+ ConfigOncb.INPUTDATASUCCESS, "OVER");

					} catch (Exception e) {
						e.printStackTrace();
						FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, // ���ɹ���¼��־
								ConfigOncb.HANDLER, e.getMessage(), "OVER");

					}
					// System.out.println(files[i] + "������!");

					files[i].delete(); // �����ｫZIP�ļ�����ɾ��
				}

				this.deleteAllFile();

			}

		} else {
			file.mkdir(); // ��������ڸ�ϵͳ�ļ��о��½�һ��
		}

		FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, ConfigOncb.HANDLER,
				"����������������ZIP���ݰ��ļ�" + count + "��", "OVER");

	}

	/**
	 * �ж�һ���ļ����Ƿ���zip�ļ���
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
	 * ɾ�����е���ʱ�ļ�
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