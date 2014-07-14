package com.cbrc.smis.system.cb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import WstFtCore.WstFtCoreClt;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.ConfigOncb;

/**
 * ����ר�����ڽ�������ǩ
 * 
 * @author cb
 * 
 */
public class OpenAndCheckData {

	/**
	 * �÷������ڶ������ļ����н�����ǩ
	 * 
	 * @param xmlName
	 *            ��Ҫ��֤��XML�����ļ�������,������NEW��һ��XML�ļ����󲢾�����֤���ø�XML���ݶ������ type:String
	 * @param xmlFileByDark
	 *            �����ļ����󣬶�Ӧ�ľ��Ǻ�׺ΪEnc.txt���ļ�����
	 * @param oneCertFile
	 *            ��һ����֤ǩ��ʱ�õ���֤���ļ���������Ӧ�ͻ��˵����һ��ǩ�� type:File
	 * @param twoCertFile
	 *            �ڶ�����֤ǩ��ʱ�õ���֤���ļ���������Ӧ�ͻ��˵ĵ����ڶ���ǩ�� type:File
	 * 
	 * @throws Exception
	 */
	public static File getXmlFileByBright(String xmlName, File xmlFileByDark,
			File oneCertFile, File twoCertFile) throws Exception {

		String errorMessage = ""; // ���������Ϣ�ַ���

		WstFtCoreClt wfc = null;

		int isSucess = 0;

		try {

			isSucess = WstFtCoreClt.SetParam(Config.CAIP, Config.CAPORT);

		} catch (Exception e) {

			errorMessage = "����CA�������ʱ�����������";

			throw new Exception(errorMessage);
		}

		if (isSucess == 1) {

			errorMessage = "����CA�������ʧ��";

			throw new Exception(errorMessage);

		}

		try {

			wfc = new WstFtCoreClt();

		} catch (Exception e) {

			errorMessage = "NEW ��һ��WstFtCoreClt����ʱ�Ƿ�������";

			throw new Exception(errorMessage);
		}

		byte[] byteOneCertFile = OpenAndCheckData.getBytes(oneCertFile); // ��һ��֤�鵼���ֽ�������Ӧ�ͻ��˵����һ��ǩ��

		byte[] byteTowCertFile = OpenAndCheckData.getBytes(twoCertFile); // ������֤�鵼���ֽ�������Ӧ�ͻ��˵ĵ����ڶ���ǩ��

		byte[] byteXmlFileByDark = OpenAndCheckData.getBytes(xmlFileByDark); // �����ĵ����ֽ���

		if ((byteOneCertFile == null) || (byteTowCertFile == null)
				|| (byteXmlFileByDark == null)) {

			errorMessage = "�������ļ���֤���ļ������ֽ���ʱ��������";

			throw new Exception(errorMessage);

		}

		int length = byteXmlFileByDark.length; // �����ֽ����ĳ���

		byte[] byteXmlFileByBright = new byte[length];// �������ֽ����ĳ�������ʼ�����ĵ��ֽ�����������������ʿͨ��������������

		try {

			isSucess = wfc.NewWstFtCoreClt(); // ����CA������

		} catch (Exception e) {

			errorMessage = "����CA����ʱ��������";

			throw new Exception(errorMessage);
		}

		if (isSucess == 1) {

			errorMessage = "����CA����ʱʧ��";

			throw new Exception(errorMessage);

		}

		try {

			isSucess = wfc.DecData(byteXmlFileByDark, byteXmlFileByDark.length,
					byteXmlFileByBright); // ִ������һ���byteXmlFileByBright��Ϊδ��ǩ�������ֽ���,�����һ��ǩ���������

		} catch (Exception e) {

			errorMessage = "���ܴ���ʱ�׳��Ĵ�����Ϣ:  " + e.toString();

			throw new Exception(errorMessage);
		}

		if (isSucess == 0) { // ����Ϊ��˵������ʧ�ܣ����ͷ����Ӳ��׳��쳣���򷵻ص������ĵĳ���

			isSucess = wfc.FreeWstFtCoreClt();

			if (isSucess == 1) {

				errorMessage = "����ʧ����ͼ�ͷ�CA����ʱ��������";

				throw new Exception(errorMessage);

			}

			errorMessage = "��������ʱ��������";

			throw new Exception(errorMessage);

		}
		
		// System.out.println("���ܳɹ�");
		
		/*************************************************/
		
		/*OutputStream  ooo = new FileOutputStream("C:/tt/" + xmlName + ".xml");
		
		ooo.write(byteXmlFileByBright);
		
		ooo.flush();
		
		ooo.close();*/
		
		/*************************************************/

		File xmlFileByBright = new File(ConfigOncb.TEMP_DIR
				+ "xmlFileByBright.xml"); // ������ʱ����XML���ݶ���

		OutputStream output = new FileOutputStream(xmlFileByBright); // �������Ķ�Ӧ�������

		output.write(byteXmlFileByBright, 0, isSucess); // д����ʱ����XML�ļ��й�����������

		output.close();

		byte[] buff = OpenAndCheckData.getBytesByNumber(xmlFileByBright, 1);

		byte[] oneOldSignedByte = OpenAndCheckData.getOneCode(xmlFileByBright);

		byte[] oneNewSignedByte = new byte[128];

		OpenAndCheckData.HexStrToBin(oneOldSignedByte, oneNewSignedByte, 256);
		
		/*************************************/
		
		/*ooo = null;
		
	    ooo = new FileOutputStream("C:/tt/" + xmlName + "sign1.txt");
	    
	    ooo.write(oneNewSignedByte);
	    
	    ooo.flush();
	    
	    ooo.close();
	    
	    ooo = null;
	    
	    ooo = new FileOutputStream("C:/tt/" + xmlName + "banjie.xml");
	    
	    ooo.write(buff);
	    
	    ooo.flush();
	    
	    ooo.close();*/
	    
		
		/*************************************/

		// �������ȶ�XML�ļ������һ��ǩ��������ǩ
		isSucess = wfc.VerifySign(buff, // �ڶ���ǩ��ǰ���ļ�Ϊ��һ��ǩ������ļ�
				buff.length, byteOneCertFile, // ֤���ֽ���
				byteOneCertFile.length, oneNewSignedByte); // byteXmlFileByBright�����ｫ��Ϊ���һ��ǩ�����XML�����ֽ���

		if (isSucess == 1) {

			isSucess = wfc.FreeWstFtCoreClt();

			if (isSucess == 1) {

				errorMessage = "��һ����ǩʧ����ͼ�ͷ�CA����ʱ��������";

				throw new Exception(errorMessage);

			}

			errorMessage = "��һ����ǩʱʧ��";

			throw new Exception(errorMessage);
		}
		
		// System.out.println("��һ����ǩ�ɹ�");

		wfc.FreeWstFtCoreClt();

		wfc = null;

		wfc = new WstFtCoreClt();

		wfc.NewWstFtCoreClt();

		// �����XML�ļ��ĵ����ڶ���ǩ��������ǩ��Ŀǰ�Ƕ�Ӧ�ͻ��˵ĵ�һ��ǩ��

		byte[] buffer = OpenAndCheckData.getBytesByNumber(xmlFileByBright, 2);

		byte[] twoOldSignedByte = OpenAndCheckData.getTwoCode(xmlFileByBright);

		byte[] twoNewSignedByte = new byte[128];

		OpenAndCheckData.HexStrToBin(twoOldSignedByte, twoNewSignedByte, 256);
		
		/*************************************************/
		
		/*ooo = null;
		
		ooo = new FileOutputStream("C:/tt/" + xmlName + "sign2.txt");
		
		ooo.write(twoNewSignedByte);
		
		ooo.flush();
		
		ooo.close();*/
		
		/*************************************************/

		isSucess = wfc.VerifySign(buffer,// ԭʼ�ļ��ֽ�����ȥ������ǩ���������XML�ļ���
				buffer.length, byteTowCertFile, byteTowCertFile.length,
				twoNewSignedByte);

		if (isSucess == 1) {

			isSucess = wfc.FreeWstFtCoreClt();

			if (isSucess == 1) {

				errorMessage = "�ڶ�����ǩʧ����ͼֹͣCA����ʱ��������";

				throw new Exception(errorMessage);
			}

			errorMessage = "�ڶ�����ǩʱʧ��";

			throw new Exception(errorMessage);

		}
		
		// System.out.println("�ڶ�����ǩ�ɹ�");

		File xmlFile = new File(ConfigOncb.TEMP_DIR + xmlName + ".xml");// �������ȶ��巵�ؾ���������ǩ��XML�����ļ��Ķ���

		output = null;

		output = new FileOutputStream(xmlFile);

		output.write(buffer);

		output.flush();

		output.close();

		xmlFileByBright.delete(); // ɾ����ʱ�����ļ�

		try {

			isSucess = wfc.FreeWstFtCoreClt(); // �ͷ�CA��������

		} catch (Exception e) {

			errorMessage = "���رշ���ʱ��������";

			throw new Exception(errorMessage);
		}

		if (isSucess == 1) {

			errorMessage = "���رշ���ʧ��";

			throw new Exception(errorMessage);

		}

		return xmlFile;
	}

	/**
	 * ����������ļ�����ǩ�Ĵ����õ�һ��ȥ��i(����)��ǩ����������ֽ���
	 * 
	 * @param file
	 *            ������ļ�����
	 * @param i
	 *            ��ǩ�Ĵ���
	 * @return
	 */
	public static byte[] getBytesByNumber(File file, int i) {

		byte[] buffer = null;

		InputStream input = null;

		int move = 256;

		try {

			input = new FileInputStream(file);

			int length = input.available();

			int m = move * i;

			length = length - m;

			buffer = new byte[length];

			input.read(buffer, 0, length);

		} catch (Exception e) {

			buffer = null;

		} finally {

			try {

				input.close();

			} catch (IOException e) {

				buffer = null;

				e.printStackTrace();

			}
		}

		return buffer;

	}

	/**
	 * ����������ļ����ض�Ӧ���ֽ���
	 * 
	 * @param file
	 * @return
	 */
	public static byte[] getBytes(File file) {

		byte[] buffer = null;

		InputStream input = null;

		try {

			input = new FileInputStream(file);

			int length = input.available();

			buffer = new byte[length];

			input.read(buffer, 0, length);

		} catch (Exception e) {

			buffer = null;

		} finally {

			try {

				input.close();

			} catch (IOException e) {

				buffer = null;

				e.printStackTrace();
			}
		}

		return buffer;

	}

	/**
	 * �÷������ص�һ����ǩ���õ���ǩ��������
	 * 
	 * @param file
	 *            Դ�ļ� + ǩ�����������ɵ��ļ�
	 * @return byte[] ��һ��ǩ����������ֽ���
	 * @throws Exception
	 */
	public static byte[] getOneCode(File file) throws Exception {

		byte[] buffer = null;

		InputStream input = null;

		int move = 256;

		try {

			buffer = new byte[move];

			input = new FileInputStream(file);

			int off = input.available() - move;

			input.skip(off);

			input.read(buffer);

		} catch (Exception e) {

			buffer = null;

		} finally {

			input.close();
		}

		return buffer;
	}

	/**
	 * �÷������صڶ�����ǩʱ�õ���ǩ��������
	 * 
	 * @param file
	 *            Դ�ļ� + ǩ�����������ɵ��ļ�
	 * @return byte[] �ڶ���ǩ����������ֽ���
	 * @throws Exception
	 */
	public static byte[] getTwoCode(File file) throws Exception {

		byte[] buffer = null;

		File temFileOncb = null;

		OutputStream output = null;

		try {

			temFileOncb = new File(Config.TEMP_DIR + "temFileOncb");

			buffer = OpenAndCheckData.getBytesByNumber(file, 1);

			output = new FileOutputStream(temFileOncb);

			output.write(buffer);

			output.flush();

			buffer = OpenAndCheckData.getOneCode(temFileOncb);

		} catch (Exception e) {

			buffer = null;
		}

		finally {

			output.close();

			temFileOncb.delete();
		}

		return buffer;
	}

	/**
	 * ת�빤��
	 * 
	 * @param in
	 *            д���ı���ǩ��ֵ
	 * @param out
	 *            ������ǩ��ֵ
	 * @param size
	 *            д���ı���ǩ��ֵ�ĳ���
	 */
	public static void HexStrToBin(byte[] in, byte[] out, int size) {
		byte[] ss = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		byte[] dd = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
		int i = 0;
		for (int id = 0; id < size; id++) {
			out[i] = 0;
			for (int j = 0; j < 16; j++) {
				if (in[id] == ss[j]) {
					out[i] = (byte) (dd[j] * 16);
					break;
				}
			}
			id++;
			for (int j = 0; j < 16; j++) {
				if (in[id] == ss[j]) {
					out[i] = (byte) (out[i] + dd[j]);
					break;
				}
			}
			i++;
		}
	}
}
