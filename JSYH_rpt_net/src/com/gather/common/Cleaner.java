package com.gather.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import com.gather.servlets.AutoSynchronized;

/**
 * 
 * @author IBMUSER
 * @function �����ʱ�ļ����е��ļ�
 */
public class Cleaner extends TimerTask{
	
	List dirList=new ArrayList();
	
	public Cleaner(){
		
	}
	
	/**
	 * ��ʱ������ִ��
	 */
	public void run(){
		//// System.out.println("------ִ��--------");
		Cleaner worker=new Cleaner();
		worker.init();
		if(!worker.clean()){
		   new Log(Cleaner.class).info(":::��ʱĿ¼���ļ�δ���ɹ�ɾ��������;ʱ����: "+DateUtil.getToday(DateUtil.DATA_TIME));
		}else{
			new Log(Cleaner.class).info(":::�ɹ�ɾ����ʱ�ļ�;ʱ����: "+DateUtil.getToday(DateUtil.DATA_TIME));
		}
	}
	/**
	 * ��ʼ���������Ŀ¼
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
	 * @function ע��������Ŀ¼
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
