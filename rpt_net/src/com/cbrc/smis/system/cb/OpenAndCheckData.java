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
 * 该类专门用于解密与验签
 * 
 * @author cb
 * 
 */
public class OpenAndCheckData {

	/**
	 * 该方法用于对密文文件进行解密验签
	 * 
	 * @param xmlName
	 *            需要验证的XML数据文件的名字,用它来NEW出一个XML文件对象并经过验证后将用该XML数据对象入库 type:String
	 * @param xmlFileByDark
	 *            密文文件对象，对应的就是后缀为Enc.txt的文件对象
	 * @param oneCertFile
	 *            第一次验证签名时用到的证书文件对象，它对应客户端的最后一次签名 type:File
	 * @param twoCertFile
	 *            第二次验证签名时用到的证书文件对象，它对应客户端的倒数第二次签名 type:File
	 * 
	 * @throws Exception
	 */
	public static File getXmlFileByBright(String xmlName, File xmlFileByDark,
			File oneCertFile, File twoCertFile) throws Exception {

		String errorMessage = ""; // 定义错误信息字符串

		WstFtCoreClt wfc = null;

		int isSucess = 0;

		try {

			isSucess = WstFtCoreClt.SetParam(Config.CAIP, Config.CAPORT);

		} catch (Exception e) {

			errorMessage = "设置CA服务参数时发生意外错误";

			throw new Exception(errorMessage);
		}

		if (isSucess == 1) {

			errorMessage = "设置CA服务参数失败";

			throw new Exception(errorMessage);

		}

		try {

			wfc = new WstFtCoreClt();

		} catch (Exception e) {

			errorMessage = "NEW 出一个WstFtCoreClt对象时是发生错误";

			throw new Exception(errorMessage);
		}

		byte[] byteOneCertFile = OpenAndCheckData.getBytes(oneCertFile); // 将一级证书导成字节流，对应客户端的最后一次签名

		byte[] byteTowCertFile = OpenAndCheckData.getBytes(twoCertFile); // 将二级证书导成字节流，对应客户端的倒数第二次签名

		byte[] byteXmlFileByDark = OpenAndCheckData.getBytes(xmlFileByDark); // 将密文导成字节流

		if ((byteOneCertFile == null) || (byteTowCertFile == null)
				|| (byteXmlFileByDark == null)) {

			errorMessage = "将加密文件和证书文件导成字节流时发生错误";

			throw new Exception(errorMessage);

		}

		int length = byteXmlFileByDark.length; // 密文字节流的长度

		byte[] byteXmlFileByBright = new byte[length];// 用密文字节流的长度来初始化明文的字节流，我这是照着卫士通给的样例来做的

		try {

			isSucess = wfc.NewWstFtCoreClt(); // 连接CA服务器

		} catch (Exception e) {

			errorMessage = "连接CA服务时发生意外";

			throw new Exception(errorMessage);
		}

		if (isSucess == 1) {

			errorMessage = "连接CA服务时失败";

			throw new Exception(errorMessage);

		}

		try {

			isSucess = wfc.DecData(byteXmlFileByDark, byteXmlFileByDark.length,
					byteXmlFileByBright); // 执行完这一句后byteXmlFileByBright将为未验签的明文字节流,即最后一次签名后的数据

		} catch (Exception e) {

			errorMessage = "解密错误时抛出的错误信息:  " + e.toString();

			throw new Exception(errorMessage);
		}

		if (isSucess == 0) { // 返回为零说明解密失败，则释放连接并抛出异常否则返回的是密文的长度

			isSucess = wfc.FreeWstFtCoreClt();

			if (isSucess == 1) {

				errorMessage = "解密失败试图释放CA服务时发生错误";

				throw new Exception(errorMessage);

			}

			errorMessage = "解密数据时发生错误";

			throw new Exception(errorMessage);

		}
		
		// System.out.println("解密成功");
		
		/*************************************************/
		
		/*OutputStream  ooo = new FileOutputStream("C:/tt/" + xmlName + ".xml");
		
		ooo.write(byteXmlFileByBright);
		
		ooo.flush();
		
		ooo.close();*/
		
		/*************************************************/

		File xmlFileByBright = new File(ConfigOncb.TEMP_DIR
				+ "xmlFileByBright.xml"); // 定义临时明文XML数据对象

		OutputStream output = new FileOutputStream(xmlFileByBright); // 定义明文对应的输出流

		output.write(byteXmlFileByBright, 0, isSucess); // 写入临时明文XML文件中供下面代码调用

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

		// 下面首先对XML文件的最后一次签名进行验签
		isSucess = wfc.VerifySign(buff, // 第二次签名前的文件为第一次签名后的文件
				buff.length, byteOneCertFile, // 证书字节流
				byteOneCertFile.length, oneNewSignedByte); // byteXmlFileByBright在这里将作为最后一次签名后的XML数据字节流

		if (isSucess == 1) {

			isSucess = wfc.FreeWstFtCoreClt();

			if (isSucess == 1) {

				errorMessage = "第一次验签失败试图释放CA服务时发生错误";

				throw new Exception(errorMessage);

			}

			errorMessage = "第一次验签时失败";

			throw new Exception(errorMessage);
		}
		
		// System.out.println("第一次验签成功");

		wfc.FreeWstFtCoreClt();

		wfc = null;

		wfc = new WstFtCoreClt();

		wfc.NewWstFtCoreClt();

		// 下面对XML文件的倒数第二次签名进行验签，目前是对应客户端的第一次签名

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

		isSucess = wfc.VerifySign(buffer,// 原始文件字节流（去掉两次签名返回码的XML文件）
				buffer.length, byteTowCertFile, byteTowCertFile.length,
				twoNewSignedByte);

		if (isSucess == 1) {

			isSucess = wfc.FreeWstFtCoreClt();

			if (isSucess == 1) {

				errorMessage = "第二次验签失败试图停止CA服务时发生错误";

				throw new Exception(errorMessage);
			}

			errorMessage = "第二次验签时失败";

			throw new Exception(errorMessage);

		}
		
		// System.out.println("第二次验签成功");

		File xmlFile = new File(ConfigOncb.TEMP_DIR + xmlName + ".xml");// 在这里先定义返回经过解密验签的XML数据文件的对象

		output = null;

		output = new FileOutputStream(xmlFile);

		output.write(buffer);

		output.flush();

		output.close();

		xmlFileByBright.delete(); // 删除临时明文文件

		try {

			isSucess = wfc.FreeWstFtCoreClt(); // 释放CA服务连接

		} catch (Exception e) {

			errorMessage = "最后关闭服务时发生意外";

			throw new Exception(errorMessage);
		}

		if (isSucess == 1) {

			errorMessage = "最后关闭服务失败";

			throw new Exception(errorMessage);

		}

		return xmlFile;
	}

	/**
	 * 根据输入的文件和验签的次数得到一个去掉i(参数)个签名返回码的字节流
	 * 
	 * @param file
	 *            输入的文件对象
	 * @param i
	 *            验签的次数
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
	 * 根据输入的文件返回对应的字节流
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
	 * 该方法返回第一次验签是用到的签名返回码
	 * 
	 * @param file
	 *            源文件 + 签名返回码生成的文件
	 * @return byte[] 第一次签名返回码的字节流
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
	 * 该方法返回第二次验签时用到的签名返回码
	 * 
	 * @param file
	 *            源文件 + 签名返回码生成的文件
	 * @return byte[] 第二次签名返回码的字节流
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
	 * 转码工具
	 * 
	 * @param in
	 *            写入文本的签名值
	 * @param out
	 *            真正的签名值
	 * @param size
	 *            写入文本的签名值的长度
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
