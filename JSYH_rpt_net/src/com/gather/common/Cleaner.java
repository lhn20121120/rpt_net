package com.gather.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import com.gather.servlets.AutoSynchronized;

/**
 * 
 * @author IBMUSER
 * @function 清除临时文件夹中的文件
 */
public class Cleaner extends TimerTask{
	
	List dirList=new ArrayList();
	
	public Cleaner(){
		
	}
	
	/**
	 * 定时器任务执行
	 */
	public void run(){
		//// System.out.println("------执行--------");
		Cleaner worker=new Cleaner();
		worker.init();
		if(!worker.clean()){
		   new Log(Cleaner.class).info(":::临时目录有文件未被成功删除，请检查;时间是: "+DateUtil.getToday(DateUtil.DATA_TIME));
		}else{
			new Log(Cleaner.class).info(":::成功删除临时文件;时间是: "+DateUtil.getToday(DateUtil.DATA_TIME));
		}
	}
	/**
	 * 初始化待清除的目录
	 *
	 */
	private void init(){
		String root=AutoSynchronized.SMMIS_PATH+Config.FILESEPARATOR;
		this.add(root+Config.DOWNLOAD_REPORT+Config.FILESEPARATOR+"result");
		this.add(root+Config.REFER_REPORT+Config.FILESEPARATOR+"temp");
		this.add(root+Config.REFER_FILE+Config.FILESEPARATOR+"temp");
	}
	
	/**
	 * @author IBMUSER
	 * @function 注册待清除的目录
	 * @param dirName
	 * @param fileName
	 */
	public void add(String dirName){
		dirList.add(dirName);
	}
	
	private boolean clean(){
		if(dirList.size()<1) return true;
		boolean flag=true;
		for(int i=0;i<this.dirList.size();i++){
			File dir=new File((String)dirList.get(i));
			File[] files=dir.listFiles();
			if(files!=null && files.length>0){
			  for(int j=0;j<files.length;j++){
				 if(!files[j].isDirectory()){
					 if(!files[j].delete()) flag=false; 
				 }
			  }
			}
		}
		return flag;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
