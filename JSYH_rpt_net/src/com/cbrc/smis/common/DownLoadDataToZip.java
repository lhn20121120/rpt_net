package com.cbrc.smis.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

public class DownLoadDataToZip {
	
	private static DownLoadDataToZip tdltZip = null;
	
	private DownLoadDataToZip(){}
	
	/**
	 * 仅实例化TemplateDownLoadToZip对象一次
	 */
	public static DownLoadDataToZip newInstance(){
		if(tdltZip == null)
			tdltZip = new DownLoadDataToZip();
		return tdltZip;
	}
	
	/**
	 * 打包指定文件ZIP格式
	 * @param srcName
	 */
	public void gzip(String srcName){
		File srcPath =new File(srcName);
		String outFilename=new String(srcName + ".zip");
		int len=srcPath.listFiles().length;
		String[] filenames = new String[len];
		
		byte[] buf = new byte[1024];
		try {
			File[]  files  =  srcPath.listFiles(); 
			for(int  i=0;i<len;i++){
				filenames[i]=srcPath.getPath()+File.separator+files[i].getName();
			}
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
			for (int i=0; i<filenames.length; i++) {
				FileInputStream in = new FileInputStream(filenames[i]);
				out.putNextEntry(new ZipEntry(files[i].getName()));
				int length;
				while ((length = in.read(buf)) > 0){
					out.write(buf, 0, length);
				}
	
				out.closeEntry();
				in.close();
			}
			out.close();
	    } 
	    catch(IOException e){
	    	e.printStackTrace(); 
	    }
	}
	
	/**
	 * 判断指定路径下的压缩文件是否存在
	 * @param srcName
	 * @return
	 */
	public boolean isTemplateZipExist(String srcName){
		File file = new File(srcName + ".zip");
		if(file.exists()) 
			return true;
		return false;
	}
	
	/**
	 * 删除指定文件夹及文件夹下的所以内容
	 * @param delFolder
	 * @return
	 */
	public boolean deleteFolder(File delFolder) { 
        //目录是否已删除 
        boolean hasDeleted = true; 
        //得到该文件夹下的所有文件夹和文件数组 
        File[] allFiles = delFolder.listFiles(); 
        
        if(allFiles != null){
        	for (int i = 0; i < allFiles.length; i++) { 
                //为true时操作 
                if (hasDeleted) { 
              	  if (allFiles[i].isDirectory()) { 
                        //如果为文件夹,则递归调用删除文件夹的方法 
              		  hasDeleted = deleteFolder(allFiles[i]); 
              	  }else if (allFiles[i].isFile()){ 
              		  try{
              			  if (!allFiles[i].delete()){  
              				  hasDeleted = false;   //删除失败,返回false 
              			  } 
              		  }catch (Exception e){ 
              			  hasDeleted = false; 
              		  } 
              	  } 
                }else{
              	  break;
                } 
           } 
        }         
        if (hasDeleted) {          
        	delFolder.delete();    //该文件夹已为空文件夹,删除它         
        }         
        return hasDeleted; 
    } 
	
	/**
	 * 打包指定文件ZIP格式
	 * @param srcName
	 */
	public void gzip(String srcName,boolean bool){
		try {
			File srcPath =new File(srcName);
			String outFilename=new String(srcName + ".zip");
			
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));			
			this.zip(out,srcPath,"");
		
			out.close();
	    }catch(IOException e){
	    	e.printStackTrace(); 
	    }catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void zip(ZipOutputStream out,File file,String fileName)throws Exception{
		if (file.isDirectory()){
			File[] fileList = file.listFiles();
			fileName = fileName.length() == 0 ? "" : fileName + File.separator;
			for (int i=0;i<fileList.length ;i++ ){
				zip(out,fileList[i],fileName+fileList[i].getName());
			}
		}else{
			out.putNextEntry(new ZipEntry(fileName));
			FileInputStream in = new FileInputStream(file);
			int buffer;
			while ((buffer=in.read()) != -1)
				out.write(buffer);
			in.close();
		}
	}
}
