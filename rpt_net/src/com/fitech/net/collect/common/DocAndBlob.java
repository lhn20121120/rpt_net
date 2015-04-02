package com.fitech.net.collect.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.util.ArrayList;

import net.sf.hibernate.Hibernate;

import org.dom4j.Document;
import org.dom4j.io.XMLWriter;

import com.fitech.net.config.Config;

public class DocAndBlob {
	
	public static final String filePath=Config.REAL_ROOT_PATH+Config.WEBROOT_TEMP+File.separator+Config.WEBROOT_TEMP_DOCFILE;
	
	//把Document集合保存到文件
	public static void docsToFiles(ArrayList alDoc)
	{
		File folder = new File(filePath);
		if(!folder.exists()) folder.mkdir();
		for(int i=0;i<alDoc.size();i++)
		{
			String fileName = filePath + File.separator + "temp" + (i+1) + ".xml";
			File file=new File(fileName);
			try {
				FileOutputStream fos=new FileOutputStream(file);
				XMLWriter writer=new XMLWriter(fos);
				writer.write((Document)alDoc.get(i));
				writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public static File docToResultFile(Document doc,String fileName)
	{
		File file=new File(filePath+File.separator+fileName+".xml");
		try {
			FileOutputStream fos=new FileOutputStream(file);
			XMLWriter writer=new XMLWriter(fos);
			writer.write(doc);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}
	
	
	public static File docToFile(Document doc)
	{
		//File file=new File("D:\\collect\\result\\temp.xml");
		File file=new File(filePath+File.separator+"temp.xml");
		try {
			FileOutputStream fos=new FileOutputStream(file);
			XMLWriter writer=new XMLWriter(fos);
			writer.write(doc);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}
	
	public static int sizeOfDoc(Document doc)
	{
		File file=docToFile(doc);
		int size=0;
		try {
			InputStream input=new FileInputStream(file);
			size = input.available();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return size;
	}
	
	public static Blob docToBlob(Document doc)
	{
		Blob xmlBlob=null;
		File file=docToFile(doc);
		
		try {
			InputStream input=new FileInputStream(file);
			xmlBlob=Hibernate.createBlob(input);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return xmlBlob;
	}
	
	public static Blob fileToBlob(File file)
	{
		Blob xmlBlob=null;
		if(file==null) return null;
		try {
			InputStream input=new FileInputStream(file);
			xmlBlob=Hibernate.createBlob(input);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return xmlBlob;
	}
	
	public static int sizeOfFile(File file)
	{
		int size=0;
		if(file==null) return 0;
		try {
			InputStream input=new FileInputStream(file);
			size=input.available();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return size;
	}
	

}
