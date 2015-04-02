/*
 * Created on 2006-5-17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fitech.net.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileParser {
	
	/**
	 *  
	 * TODO �����������жϸ��ļ��Ƿ����
	 * 
	 * @author wunaigang(2005-4-10)	
	 * @param fileName
	 * @return ����ļ����ڷ��ظ��ļ��ľ���·�������򷵻�null
	 */
    public static boolean isFileExist(String fileName){
        try{
            File f = new File(fileName);
            if(f.exists()){                
                return true;
            } else{
            	return false;
            }
        }catch(Exception exception){
            return false;
        }
    }
    
    /**
     * ɾ���ļ�
     * @param fileName �ļ���
     */
    public static void deleteFile(String fileName){
    	try{
            File f = new File(fileName);
            if(f.exists()){                
                f.delete();
            } 
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
	/**
	 *  
	 * TODO �����������жϸ��ļ��Ƿ����
	 * 
	 * @author wunaigang(2005-4-10)	
	 * @param fileName
	 * @return ����ļ����ڷ��ظ��ļ��ľ���·�������򷵻�null
	 */
    public static String getFilePath(String fileName){
        String filePath = "";
        try{
            File f = new File(fileName);
            if(f.exists()){
                filePath = f.getCanonicalPath();
                return filePath;
            } else{
            	return "";
            }
        }catch(Exception exception){
            return "";
        }
    }
    

    /**
     *  
     * TODO �����������жϸ�Ŀ¼�Ƿ����
     * 
     * @author wunaigang(2005-4-10)	
     * @param folderName
     * @return�����Ŀ¼���ڷ���true���򷵻�false
     */
    public static boolean isFolder(String folderName){
        try{
            File f = new File(folderName);
            if (f.exists() && f.isDirectory()) {
            	return true;
            }else{
            	return false;
            }
         
        }catch(Exception exception){
            return false;
        }    	
    }

    /**
     *  
     * TODO �����������жϸ�Ŀ¼�Ƿ����
     * 
     * @author wunaigang(2005-4-10)	
     * @param folderName
     * @return�����Ŀ¼���ڷ���true���򷵻�false
     */
    public static String getFolderPath(String folderName){
        try{
            File f = new File(folderName);
            if (f.exists() && f.isDirectory()) {
                String path = f.getCanonicalPath();               
            	return path;
            }else{
            	return "";
            }
         
        }catch(Exception exception){
            return "";
        }    	
    }
    
   /**
    *  
    * TODO ����������Ŀ¼��������·����Ŀ¼�µ�Ŀ¼
    * 
    * @author wunaigang(2005-5-16)	
    * @param filePath Դ�ļ�Ŀ¼·��
    * @return
    */
    public static ArrayList folderHandle(String filePath){
   	   
    	ArrayList folderList = new ArrayList();
   	   //������ǰĿ¼���ļ���File����
   	   File folderObject = new File(filePath);
   	   //ȡ�ô���Ŀ¼�������ļ���File��������
   	   File[] list = folderObject.listFiles();
   	   if (list != null) {
	   	  for(int i=0,n=list.length; i<n; i++){
	   	  	 if (list[i].isDirectory()){
	   	  	 	 folderList.add(list[i].getName());	   	  	 	 
	   	  	 }
	   	  	 
	   	  }
	   }
   	   
   	   return  folderList;
    }
    
     

   	   /**
   	    *  
   	    * TODO ����������Ŀ¼��������·����Ŀ¼�µ�Ŀ¼
   	    * 
   	    * @author wunaigang(2005-5-16)	
   	    * @param filePath Դ�ļ�Ŀ¼·��
   	    * @return
   	    */
   	    public static ArrayList getFiles(String folderPath){
   	   	   
   	       ArrayList fileList = new ArrayList();
   	   	   //������ǰĿ¼���ļ���File����
   	   	   File fileObject = new File(folderPath);
   	   	   //ȡ�ô���Ŀ¼�������ļ���File��������
   	   	   File[] list = fileObject.listFiles();
   	   	   if (list != null) {
   		   	  for(int i=0,n=list.length; i<n; i++){
   		   	  	 if (list[i].isFile()){   		   	  
   		   	  	 	fileList.add(list[i].getName());	   	  	 	 
   		   	  	 }
   		   	  	
   		   	  }
   	   	   }   	   
   	   
   	      return fileList;
    }    
    
   		/**
   		 * copyԴ�ļ���Ŀ��λ��
   		 * @param srcFile Դ�ļ�
   		 * @param objFile Ŀ���ļ�
   		 * @return  ���srcFile,objFileΪnull����srcFile�ļ�������,����objFile���ܽ���������false,
   		 *          ���Ƴɹ�,����true
   		 */
   		public static boolean copyFile(String srcFile, String objFile){
   		   	boolean result = false;
   			//�жϲ���
   		   	if(srcFile==null || objFile==null){
   		   	     return result;
   		   	}
   			
   		   	DataInputStream  in  = null;
   		   	DataOutputStream out = null;
   			//�ж�Դ�ļ�
   			try{
   				in = new DataInputStream(new FileInputStream(srcFile));	
   			}catch(FileNotFoundException e){
   			    e.printStackTrace();
   			    return result;
   			}
   			//�ж�Ŀ���ļ�
   			File obj = new File(objFile);
   			if(!obj.exists()){
   			    try{
   			    	obj.createNewFile();	
   			    }catch(IOException ioe){
   			        ioe.printStackTrace();	
   			    }	
   			}
   			try{
   				out = new DataOutputStream(new FileOutputStream(obj));	
   			}catch(FileNotFoundException e){
   			    e.printStackTrace();
   			    return result;
   			}
   			
   			//copy
   			byte[] buffer = new byte[4096];
   			try{
   				int i = 0;
   				
   				while((i=in.read(buffer))>0){
   					out.write(buffer,0,i);
   				}
   				out.flush();
   				out.close();
   				in.close();
   				result = true;
   			}catch(IOException ioe){
   			    ioe.printStackTrace();	
   			}
   			return result;
   			 
   		}	
    
    
	public static void main(String args[]){
		FileParser fm = new FileParser();
		FileParser.copyFile("D:\\�½� �ı��ĵ�.txt", "d:\\aa.txt");
//		ArrayList fileList = fm.getFiles("D:\\tomcat\\webapps\\WebEditor\\Reports\\3�·�����(js)");
//		for(int i=0,n=fileList.size(); i<n; i++){
//			// System.out.println("fileList = " + fileList.get(i));
//		}
//		ArrayList folderList = null;
//		folderList = fm.folderHandle("F:\\��Ŀ����\\sisytech\\sdc_pro\\WEB-INF\\src\\com\\sisytech\\sdc");
//		for(int i=0,n=folderList.size(); i<n; i++){
//      		// System.out.println("i = " + (i+1) + ":" + folderList.get(i).toString());
//      	}
	}

}
