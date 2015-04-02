/*
 * 创建日期 2005-7-4
 *
 */
package com.gather.common;

/**
 * @author 阿房公
 *
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

//注意:各个函数中要到的参数path均为实际路径.

public class FileCtrl {
	
	

/**
 * 方法名：FileName
 * 级别：private
 * @param     String     文件的实际路径 ， 即：路径 + 文件名
 * @return    String     文件名
 * @throws    (无)
 * 作用：从文件的实际路径中取出文件名
 */
private String FileName(String path) {
  int pos = path.lastIndexOf('/');
  String FileName = path.substring(pos + 1);
  return (FileName);
}

/**
 * 方法名：PathName
 * 级别：private
 * @param     String     文件的实际路径 ， 即：路径 + 文件名
 * @return    String     路径
 * @throws    (无)
 * 作用：从文件的实际路径中取出路径
 */
private String PathName(String path) {
  int pos = path.lastIndexOf('\\');
  String PathName = path.substring(0, pos);
  return (PathName);
}

/**
 * 方法名：createNewFile
 * 级别：public
 * @param     String     文件的实际路径 ， 即：路径 + 文件名
 * @param     boolean    是否覆盖
 * @return    (无)
 * @throws    Exception  如果发生错误
 * 作用：建立一个新的文件
 */
	public static boolean createNewFile(String fileName, boolean rebuild) {
		// String PathName = new String(PathName(path));
		// String FileName = new String(FileName(path));

		File f = new File(fileName);
		
		if (f.exists() == true) {
			if (rebuild == true)
				f.delete();
			else
				return false;
		}

		try {
			f.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

/**
 * 方法名：deleteFile 级别：public
 * 
 * @param String
 *            文件的实际路径 ， 即：路径 + 文件名
 * @return (无)
 * @throws Exception
 *             如果发生错误 作用：删除文件
 */
public void deleteFile(String path) throws Exception {
  String FileName = new String(FileName(path));
  String PathName = new String(PathName(path));
  File f = new File(PathName, FileName);
  f.delete();
}

/**
 * 方法名：createNewDir
 * 级别：public
 * @param     String     目录的实际路径
 * @param     boolean    是否覆盖
 * @return    (无)
 * @throws    Exception  如果发生错误
 * 作用：建立一个新的目录
 */
public void createNewDir(String path, boolean rebuild) throws Exception {
  File d = new File(path);
  if (rebuild == true) {
    if (d.exists() == true) {
      d.delete();
    }
    d.mkdir();
  }
  else {
    if (d.exists() == false) {
      d.mkdir();
    }
  }
}

/**
 * 方法名：deleteDir
 * 级别：public
 * @param     String     目录的实际路径
 * @return    (无)
 * @throws    Exception  如果发生错误
 * 作用：删除目录
 */
public void deleteDir(String path) throws Exception {
  File d = new File(path);
  if(d.isDirectory()){
    File[] list = d.listFiles();
    for(int i=0;i<list.length;i++){
      list[i].delete();
    }
  }
  d.delete();
}

/**
 * 方法名：clearDir
 * 级别：public
 * @param     String     目录的实际路径
 * @return    (无)
 * @throws    Exception  如果发生错误
 * 作用：清空目录
 */
public void clearDir(String path) throws Exception {
  File d = new File(path);
  if(d.isDirectory()){
    File[] list = d.listFiles();
    for(int i=0;i<list.length;i++){
      list[i].delete();
    }
  }
}

/**
 * 方法名：fileLength
 * 级别：public
 * @param     String     文件的实际路径 ， 即：路径 + 文件名
 * @return    long       文件的长度
 * @throws    Exception  如果发生错误
 * 作用：测量文件的长度
 */
public long fileLength(String path) throws Exception {
  String FileName = new String(FileName(path));
  String PathName = new String(PathName(path));
  File f = new File(PathName, FileName);
  long fileLength = f.length();
  return (fileLength);
}

/**
 * 方法名：isFile
 * 级别：public
 * @param     String     文件的实际路径 ， 即：路径 + 文件名
 * @return    boolean    是否文件
 * @throws    Exception  如果发生错误
 * 作用：用来判断是不是文件
 */
public boolean isFile(String path) throws Exception {
  String FileName = new String(FileName(path));
  String PathName = new String(PathName(path));
  File f = new File(PathName, FileName);
  boolean isFile = f.isFile();
  return (isFile);
}

/**
 * 方法名：isDir
 * 级别：public
 * @param     String     目录的实际路径
 * @return    boolean    是否目录
 * @throws    Exception  如果发生错误
 * 作用：用来判断是不是目录
 */
public boolean isDir(String path) throws Exception {
  File d = new File(path);
  boolean isDir = d.isDirectory();
  return (isDir);
}

/*-------------------------以上是针对文件的操作-------------------------*/

/*-------------------------以下是针对文件的读写-------------------------*/

/**
 * 方法名：readLine
 * 级别：public
 * @param     String     文件的实际路径 ， 即：路径 + 文件名
 * @return    String     文件中第一行的内容
 * @throws    Exception  如果发生错误
 * 作用：用来读取文件的第一行
 */
public String readLine(String path) throws Exception {
  FileReader fr = new FileReader(path);
  BufferedReader br = new BufferedReader(fr);
  String Line = br.readLine();
  return (Line);
}

/**
 * 方法名：readAll
 * 级别：public
 * @param     String     文件的实际路径 ， 即：路径 + 文件名
 * @return    String     文件中的所有内容
 * @throws    Exception  如果发生错误
 * 作用：用来读取整个文件的内容
 */
public String readAll(String path) throws Exception {
  FileReader fr = new FileReader(path);
  BufferedReader br = new BufferedReader(fr);
  String txt = new String();
  String Line = new String();
  Line = br.readLine();
  while (Line != null) {
    txt = txt + Line;
    Line = br.readLine();
  }
  br.close();
  fr.close();
  return (txt);
}

/**
 * 方法名：writeLine
 * 级别：public
 * @param     String     文件的实际路径 ， 即：路径 + 文件名
 * @param     String     要写入文件中的内容
 * @return    (无)
 * @throws    Exception  如果发生错误
 * 作用：用来把数据写入文件
 */
public void writeLine(String path, String content) throws Exception {
  FileWriter fw = new FileWriter(path);
  fw.write(content);
  fw.close();
}

/**
 * 方法名：writeAppend
 * 级别：public
 * @param     String     文件的实际路径 ， 即：路径 + 文件名
 * @param     String     要写入文件中的内容
 * @return    (无)
 * @throws    Exception  如果发生错误
 * 作用：用来把数据追加入文件
 */
public void writeAppend(String path, String content) throws Exception {
  FileWriter fw = new FileWriter(path, true);
  PrintWriter pw = new PrintWriter(fw);
  pw.print(content + "\n");
  pw.close();
  fw.close();
}
}