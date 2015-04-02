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
	 * ��ʵ����TemplateDownLoadToZip����һ��
	 */
	public static DownLoadDataToZip newInstance(){
		if(tdltZip == null)
			tdltZip = new DownLoadDataToZip();
		return tdltZip;
	}
	
	/**
	 * ���ָ���ļ�ZIP��ʽ
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
	 * �ж�ָ��·���µ�ѹ���ļ��Ƿ����
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
	 * ɾ��ָ���ļ��м��ļ����µ���������
	 * @param delFolder
	 * @return
	 */
	public boolean deleteFolder(File delFolder) { 
        //Ŀ¼�Ƿ���ɾ�� 
        boolean hasDeleted = true; 
        //�õ����ļ����µ������ļ��к��ļ����� 
        File[] allFiles = delFolder.listFiles(); 
        
        if(allFiles != null){
        	for (int i = 0; i < allFiles.length; i++) { 
                //Ϊtrueʱ���� 
                if (hasDeleted) { 
              	  if (allFiles[i].isDirectory()) { 
                        //���Ϊ�ļ���,��ݹ����ɾ���ļ��еķ��� 
              		  hasDeleted = deleteFolder(allFiles[i]); 
              	  }else if (allFiles[i].isFile()){ 
              		  try{
              			  if (!allFiles[i].delete()){  
              				  hasDeleted = false;   //ɾ��ʧ��,����false 
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
        	delFolder.delete();    //���ļ�����Ϊ���ļ���,ɾ����         
        }         
        return hasDeleted; 
    } 
	
	/**
	 * ���ָ���ļ�ZIP��ʽ
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
