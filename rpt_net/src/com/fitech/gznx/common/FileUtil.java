package com.fitech.gznx.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    // 缓冲区大小
    private static final int BUFFER_SIZE = 8 * 1024;

    /**
     * 
     * 方法说明:该方法用于将一个原文件拷贝为一个目的文件
     * 
     * @param srcFile
     *                File 需要复制的文件
     * @param destFile
     *                File 复制后文件的对象
     * @return 文件复制是否成功
     */
    public static boolean copyFile(File srcFile, File destFile) {
	InputStream is = null;
	OutputStream os = null;

	// 判断源文件是否存在
	if (!srcFile.exists()) {
	    return false;
	}

	try {
	    is = new FileInputStream(srcFile);
	    os = new FileOutputStream(destFile);
	    byte[] buffer = new byte[is.available()];
	    while (is.read(buffer) != -1) {
		os.write(buffer);
	    }
	    os.flush();
	    os.close();
	    is.close();
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    /**
     * 
     * 方法说明:该方法用于将一个原文件拷贝为一个目的文件
     * 
     * @param srcFile
     *                String 需要复制的文件路径
     * @param destFile
     *                String 复制后文件的路径
     * @return 文件复制是否成功
     */
    public static boolean copyFile(String srcPath, String destPath) {
	if (srcPath == null || srcPath.equals("") || destPath == null
		|| destPath.equals("")) {
	    return false;
	}
	File srcFile = new File(srcPath);
	File destFile = new File(destPath);
	return copyFile(srcFile, destFile);
    }

    /**
     * 
     * 删除文件,可以是文件或者文件夹
     * 
     * @author Nick
     * @param filePath
     *                String 文件路径
     * @return 删除文件夹是否成功
     */
    public static boolean deleteFile(String filePath) {
	// 如果文件路径不存在，则退出
	if (filePath == null || filePath.equals("")) {
	    return false;
	}
	File file = new File(filePath.trim());

	return deleteFile(file);
    }

    /**
     * 删除文件,可以是文件或者文件夹
     * 
     * @author Nick
     * @param file
     *                File 要删除的文件
     * @return 删除文件夹是否成功
     */
    public static boolean deleteFile(File file) {
	boolean result = true;
	// 判断文件或者文件夹是否存在
	if (!file.exists()) {
	    return false;
	}
	String dirPath = null;
	// 如果是文件，则直接删除;如果是文件夹，则递归删除
	if (file.isFile()) {
	    file.delete();
	} else {
	    File[] fileList = file.listFiles();
	    int fileListSize = fileList.length;
	    // 文件夹包括子文件或者子文件夹，则递归删除
	    if (fileList != null && fileListSize > 0) {
		for (int i = 0; i < fileListSize; i++) {
		    if (fileList[i].isFile()) {
			// System.out.println(fileList[i].getAbsolutePath());
			fileList[i].delete();
		    } else if (fileList[i].isDirectory()) {
			dirPath = fileList[i].getPath();
			// 递归删除
			deleteFile(dirPath);
		    }
		}
	    }
	    // 删除根文件夹
	    file.delete();
	}
	return result;
    }

    /**
     * 一行一行读取数据
     * 
     * @author Nick
     * 
     * @param path
     *                String 要读取的文件路径
     * @param charset
     *                String 读取文件时的编码[如果不设置编码，可以直接置null值或者空值]
     * 
     * @return List 返回文件每一行的信息
     */
    public static List readLineByLine(String path, String charset)
	    throws Exception {
	List lst = new ArrayList();

	InputStream is = new FileInputStream(path);
	InputStreamReader isr = null;
	// 建立InputStreamReader对象，并实例化为isr
	if (charset != null && !charset.equals("")) {
	    isr = new InputStreamReader(is, charset);
	} else {
	    isr = new InputStreamReader(is);
	}
	// 建立BufferedReader对象，并实例化为br
	BufferedReader br = new BufferedReader(isr);

	String line = br.readLine();// 从文件读取一行字符串
	// 判断读取到的字符串是否不为空
	while (line != null) {
	    lst.add(line);
	    // byte b[] = line.getBytes();
	    // for (int i = 0; i < b.length; i++) {
	    // System.out.print(b[i]);
	    // }
	    // System.out.println();
	    // System.out.println(line);// 输出从文件中读取的数据
	    line = br.readLine();// 从文件中继续读取一行数据
	}
	br.close();
	isr.close();
	is.close();
	return lst;
    }

    /**
     * 使用默认编码一行一行读取数据
     * 
     * @author Nick
     * 
     * @param path
     *                String 要读取的文件路径
     * 
     * @return List 返回文件每一行的信息
     */
    public static List readLineByLine(String path) throws Exception {
	return readLineByLine(path, null);
    }

    /**
     * 读取所有的文件数据
     * 
     * @author Nick
     * 
     * @param fileName
     *                String 要读取的文件路径
     * 
     * @return String 返回文件的所有内容
     * 
     * 关键在于读取过程中，要判断所读取的字符是否已经到了文件的末尾， 并且这个字符是不是文件中的断行符，即判断该字符值是否为13
     */
    public static String readAllData(String fileName) {
	File file = new File(fileName);
	Reader reader = null;
	StringBuffer sb = new StringBuffer();

	try {
	    // 一次读一个字符
	    reader = new InputStreamReader(new FileInputStream(file));
	    int tempchar;
	    while ((tempchar = reader.read()) != -1) {
		sb.append((char) tempchar);
	    }
	    reader.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return sb.toString();
    }

    public static String filterStr(String srcStr, int typeId) {
	String result = null;
	if (typeId == 0) {
	    try {
		result = new String(srcStr.getBytes("UTF-8"), "ISO8859-1");
	    } catch (Exception e) {
		result = null;
	    }
	} else if (typeId == 2) {
	    try {
		result = new String(srcStr.getBytes("ISO-8859-1"), "GB2312");
	    } catch (Exception e) {
		result = null;
	    }
	} else if (typeId==3) {
	    try {
		result = new String(srcStr.getBytes("UTF-8"), "GB2312");
	    } catch (Exception e) {
		result = null;
	    }
	} else {
	    try {
		result = new String(srcStr.getBytes("ISO8859-1"), "UTF8");
	    } catch (Exception e) {
		e.printStackTrace();
		result = null;
	    }
	}
	return result;
    }
    /**
	 * 将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名.
	 * 
	 * @param s 原文件名
	 * @return String 重新编码后的文件名
	 */
	public static String toUtf8String(String fileName) {
		if(fileName==null) return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < fileName.length(); i++) {
			char c = fileName.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					//toHexString 以十六进制的无符号整数形式返回一个整数参数的字符串表示形式。
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}
}
