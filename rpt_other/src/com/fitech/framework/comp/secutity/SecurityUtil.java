package com.fitech.framework.comp.secutity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

import com.fitech.framework.comp.file.properties.FitechProperties;
import com.fitech.framework.core.common.Config;

public class SecurityUtil {
	/**
	 * 加密
	 * @param oldFile 待处理文件路径
	 * @param newFile 加密后文件存放路径
	 * @throws Exception
	 */
	public static void encryptFile(String oldFile, String newFile)throws Exception {
		File fileOld = new File(oldFile);
		FileInputStream in = new FileInputStream(oldFile);   
		File file = new File(newFile); 
		String line = "";		 
		String str = "";	
		 //读取令牌文件信息，此处令牌暂时放在d盘根目录下	
		 String basePath = FitechProperties.readValue("keyfile");
		  BufferedReader reader = new BufferedReader(new FileReader(basePath));		 		  
		  //读取令牌内容
		  while ((line = reader.readLine()) != null) {	    
		      str = str.concat(line);	    
	      }
		  //把令牌文件信息转成字节存入KEYVALUE
		  byte[] KEYVALUE =str.getBytes();
		  //需要读取的字节长度
		  int BUFFERLEN = 512;		
		  if (!file.exists())		
			  file.createNewFile();		 
		  FileOutputStream out = new FileOutputStream(file);		
		  int c, pos, keylen;		  
		  pos = 0;	
		  //读取令牌的字节长度
		  keylen = KEYVALUE.length;	  
		  byte buffer[] = new byte[BUFFERLEN];	
		  //异或处理文件信息
		  while ((c = in.read(buffer)) != -1) {		 
			   for (int i = 0; i < c; i++) {		   
				   buffer[i] ^= KEYVALUE[pos];		
				   out.write(buffer[i]);
		           pos++;	     
		           if (pos == keylen)          
			           pos = 0;		           
				   }	        	  
			  }   
		   //在加密完的信息后追加‘※※※’和令牌信息，以便解密时调用令牌信息
		      out.write("※※※".getBytes());
		      out.write(str.getBytes());	      
		      in.close();
			  out.close();
			//删除待加密文件
			  fileOld.delete();	         
	 }
	public static void main(String[] args){
		try {
			decryptFile("c:\\qyts_20110607.update","c:\\text.zip");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 解密
	 * @param oldFile 待解密文件路径
	 * @param newFile 解密后文件存放路径
	 * @throws Exception
	 */
	private static void decryptFile(String oldFile, String newFile)throws Exception {
		String fileStr = "";
		String keyStr = "";
	    String line = "";		 
		String str = "";	
		File fileOld = new File(oldFile);			
		File file = new File(newFile); 
		 //读取oldFile中的令牌值
		  FileInputStream inn=new FileInputStream (fileOld);
		  InputStreamReader inReader=new InputStreamReader (inn,"GB2312");	
		  BufferedReader reader = new BufferedReader(inReader);
		  while ((line = reader.readLine()) != null) {	    
		      str = str.concat(line);	    
	      }	
		  //以※※※分割，得到令牌值keyStr
		  String[] strSum = str.split("※※※");
		  fileStr = strSum[0];
		  keyStr = strSum[1];

		  FileInputStream in = new FileInputStream(oldFile);
		  //把令牌文件信息转成字节存入KEYVALUE
		  byte[] KEYVALUE =keyStr.getBytes();	
		  //需要读取的字节长度
		  int BUFFERLEN = 512;		
		  if (!file.exists())		
			  file.createNewFile();		
		 
		  FileOutputStream out = new FileOutputStream(file);		
		  int c, pos, keylen;		  
		  pos = 0;		   
		   //读取令牌的字节长度
		   keylen = KEYVALUE.length;	  
		   byte buffer[] = new byte[BUFFERLEN];
		   //异或处理文件信息
		   while ((c = in.read(buffer))!= -1) {		 
			   for (int i = 0; i < c; i++) {		   
				   buffer[i] ^= KEYVALUE[pos];		
				   out.write(buffer[i]);
		           pos++;	     
		           if (pos == keylen)          
			           pos = 0;		           
				   }	        	  
			  }   
	      in.close();
		  out.close();
		  //删除待加密文件
		  fileOld.delete();	         
	 }
}

