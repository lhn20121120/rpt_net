/*
 * �������� 2005-7-4
 *
 */
package com.gather.common;

/**
 * @author ������
 *
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

//ע��:����������Ҫ���Ĳ���path��Ϊʵ��·��.

public class FileCtrl {
	
	

/**
 * ��������FileName
 * ����private
 * @param     String     �ļ���ʵ��·�� �� ����·�� + �ļ���
 * @return    String     �ļ���
 * @throws    (��)
 * ���ã����ļ���ʵ��·����ȡ���ļ���
 */
private String FileName(String path) {
  int pos = path.lastIndexOf('/');
  String FileName = path.substring(pos + 1);
  return (FileName);
}

/**
 * ��������PathName
 * ����private
 * @param     String     �ļ���ʵ��·�� �� ����·�� + �ļ���
 * @return    String     ·��
 * @throws    (��)
 * ���ã����ļ���ʵ��·����ȡ��·��
 */
private String PathName(String path) {
  int pos = path.lastIndexOf('\\');
  String PathName = path.substring(0, pos);
  return (PathName);
}

/**
 * ��������createNewFile
 * ����public
 * @param     String     �ļ���ʵ��·�� �� ����·�� + �ļ���
 * @param     boolean    �Ƿ񸲸�
 * @return    (��)
 * @throws    Exception  �����������
 * ���ã�����һ���µ��ļ�
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
 * ��������deleteFile ����public
 * 
 * @param String
 *            �ļ���ʵ��·�� �� ����·�� + �ļ���
 * @return (��)
 * @throws Exception
 *             ����������� ���ã�ɾ���ļ�
 */
public void deleteFile(String path) throws Exception {
  String FileName = new String(FileName(path));
  String PathName = new String(PathName(path));
  File f = new File(PathName, FileName);
  f.delete();
}

/**
 * ��������createNewDir
 * ����public
 * @param     String     Ŀ¼��ʵ��·��
 * @param     boolean    �Ƿ񸲸�
 * @return    (��)
 * @throws    Exception  �����������
 * ���ã�����һ���µ�Ŀ¼
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
 * ��������deleteDir
 * ����public
 * @param     String     Ŀ¼��ʵ��·��
 * @return    (��)
 * @throws    Exception  �����������
 * ���ã�ɾ��Ŀ¼
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
 * ��������clearDir
 * ����public
 * @param     String     Ŀ¼��ʵ��·��
 * @return    (��)
 * @throws    Exception  �����������
 * ���ã����Ŀ¼
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
 * ��������fileLength
 * ����public
 * @param     String     �ļ���ʵ��·�� �� ����·�� + �ļ���
 * @return    long       �ļ��ĳ���
 * @throws    Exception  �����������
 * ���ã������ļ��ĳ���
 */
public long fileLength(String path) throws Exception {
  String FileName = new String(FileName(path));
  String PathName = new String(PathName(path));
  File f = new File(PathName, FileName);
  long fileLength = f.length();
  return (fileLength);
}

/**
 * ��������isFile
 * ����public
 * @param     String     �ļ���ʵ��·�� �� ����·�� + �ļ���
 * @return    boolean    �Ƿ��ļ�
 * @throws    Exception  �����������
 * ���ã������ж��ǲ����ļ�
 */
public boolean isFile(String path) throws Exception {
  String FileName = new String(FileName(path));
  String PathName = new String(PathName(path));
  File f = new File(PathName, FileName);
  boolean isFile = f.isFile();
  return (isFile);
}

/**
 * ��������isDir
 * ����public
 * @param     String     Ŀ¼��ʵ��·��
 * @return    boolean    �Ƿ�Ŀ¼
 * @throws    Exception  �����������
 * ���ã������ж��ǲ���Ŀ¼
 */
public boolean isDir(String path) throws Exception {
  File d = new File(path);
  boolean isDir = d.isDirectory();
  return (isDir);
}

/*-------------------------����������ļ��Ĳ���-------------------------*/

/*-------------------------����������ļ��Ķ�д-------------------------*/

/**
 * ��������readLine
 * ����public
 * @param     String     �ļ���ʵ��·�� �� ����·�� + �ļ���
 * @return    String     �ļ��е�һ�е�����
 * @throws    Exception  �����������
 * ���ã�������ȡ�ļ��ĵ�һ��
 */
public String readLine(String path) throws Exception {
  FileReader fr = new FileReader(path);
  BufferedReader br = new BufferedReader(fr);
  String Line = br.readLine();
  return (Line);
}

/**
 * ��������readAll
 * ����public
 * @param     String     �ļ���ʵ��·�� �� ����·�� + �ļ���
 * @return    String     �ļ��е���������
 * @throws    Exception  �����������
 * ���ã�������ȡ�����ļ�������
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
 * ��������writeLine
 * ����public
 * @param     String     �ļ���ʵ��·�� �� ����·�� + �ļ���
 * @param     String     Ҫд���ļ��е�����
 * @return    (��)
 * @throws    Exception  �����������
 * ���ã�����������д���ļ�
 */
public void writeLine(String path, String content) throws Exception {
  FileWriter fw = new FileWriter(path);
  fw.write(content);
  fw.close();
}

/**
 * ��������writeAppend
 * ����public
 * @param     String     �ļ���ʵ��·�� �� ����·�� + �ļ���
 * @param     String     Ҫд���ļ��е�����
 * @return    (��)
 * @throws    Exception  �����������
 * ���ã�����������׷�����ļ�
 */
public void writeAppend(String path, String content) throws Exception {
  FileWriter fw = new FileWriter(path, true);
  PrintWriter pw = new PrintWriter(fw);
  pw.print(content + "\n");
  pw.close();
  fw.close();
}
}