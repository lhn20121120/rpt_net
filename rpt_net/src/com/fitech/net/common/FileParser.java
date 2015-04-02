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
	 * TODO 本方法的是判断该文件是否存在
	 * 
	 * @author wunaigang(2005-4-10)	
	 * @param fileName
	 * @return 如该文件存在返回该文件的绝对路径，否则返回null
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
     * 删除文件
     * @param fileName 文件名
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
	 * TODO 本方法的是判断该文件是否存在
	 * 
	 * @author wunaigang(2005-4-10)	
	 * @param fileName
	 * @return 如该文件存在返回该文件的绝对路径，否则返回null
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
     * TODO 本方法的是判断该目录是否存在
     * 
     * @author wunaigang(2005-4-10)	
     * @param folderName
     * @return　如该目录存在返回true否则返回false
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
     * TODO 本方法的是判断该目录是否存在
     * 
     * @author wunaigang(2005-4-10)	
     * @param folderName
     * @return　如该目录存在返回true否则返回false
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
    * TODO 本方法的是目录处理，返回路径中目录下的目录
    * 
    * @author wunaigang(2005-5-16)	
    * @param filePath 源文件目录路径
    * @return
    */
    public static ArrayList folderHandle(String filePath){
   	   
    	ArrayList folderList = new ArrayList();
   	   //建立当前目录中文件的File对象
   	   File folderObject = new File(filePath);
   	   //取得代表目录中所有文件的File对象数组
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
   	    * TODO 本方法的是目录处理，返回路径中目录下的目录
   	    * 
   	    * @author wunaigang(2005-5-16)	
   	    * @param filePath 源文件目录路径
   	    * @return
   	    */
   	    public static ArrayList getFiles(String folderPath){
   	   	   
   	       ArrayList fileList = new ArrayList();
   	   	   //建立当前目录中文件的File对象
   	   	   File fileObject = new File(folderPath);
   	   	   //取得代表目录中所有文件的File对象数组
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
   		 * copy源文件到目标位置
   		 * @param srcFile 源文件
   		 * @param objFile 目标文件
   		 * @return  如果srcFile,objFile为null或是srcFile文件不存在,或是objFile不能建立均返回false,
   		 *          复制成功,返回true
   		 */
   		public static boolean copyFile(String srcFile, String objFile){
   		   	boolean result = false;
   			//判断参数
   		   	if(srcFile==null || objFile==null){
   		   	     return result;
   		   	}
   			
   		   	DataInputStream  in  = null;
   		   	DataOutputStream out = null;
   			//判断源文件
   			try{
   				in = new DataInputStream(new FileInputStream(srcFile));	
   			}catch(FileNotFoundException e){
   			    e.printStackTrace();
   			    return result;
   			}
   			//判断目标文件
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
		FileParser.copyFile("D:\\新建 文本文档.txt", "d:\\aa.txt");
//		ArrayList fileList = fm.getFiles("D:\\tomcat\\webapps\\WebEditor\\Reports\\3月份数据(js)");
//		for(int i=0,n=fileList.size(); i<n; i++){
//			// System.out.println("fileList = " + fileList.get(i));
//		}
//		ArrayList folderList = null;
//		folderList = fm.folderHandle("F:\\项目备份\\sisytech\\sdc_pro\\WEB-INF\\src\\com\\sisytech\\sdc");
//		for(int i=0,n=folderList.size(); i<n; i++){
//      		// System.out.println("i = " + (i+1) + ":" + folderList.get(i).toString());
//      	}
	}

}
